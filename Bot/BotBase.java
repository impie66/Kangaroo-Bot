package Bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bwapi.Color;
import bwapi.Game;
import bwapi.Order;
import bwapi.Unit;
import bwapi.Position;
import bwapi.Race;
import bwapi.TilePosition;
import bwapi.UnitType;
import bwem.*;


public class BotBase {
Unit depot;
int id;
int workers;
int defenceScore;
int maxWorkers;
ArrayList<Mineral> Mins;
ArrayList<Geyser> Geysers;
HashMap<Unit, TilePosition> GeyserT;
ArrayList<Unit>Pawns;
ArrayList<Unit>Gases;
HashMap<Unit, ArrayList<Unit>> gasWorkers;
ArrayList<Unit> voidedWorkers;
Base Base;
Game game;
List<Mineral> blockingMinerals;
HashMap<Integer, ArrayList<Unit>> mineralG = new HashMap<>(); // MINERAL ID, ARRAYLIST (WORKERS)

	public BotBase(Game gamee, Unit unit, Base bass){
		this.depot = unit;
		this.Base = bass;
		this.game = gamee;
		this.Pawns = new ArrayList<Unit>();
		this.gasWorkers = new HashMap<Unit, ArrayList<Unit>>();
		this.Gases = new ArrayList<Unit>();
		this.Mins = new ArrayList<Mineral>(bass.getMinerals());
		this.Geysers = new ArrayList<Geyser>(bass.getGeysers());
		this.maxWorkers = 9 + (Mins.size() / 2) + (Geysers.size() * 3);
		this.voidedWorkers = new ArrayList<Unit>();
		this.blockingMinerals = bass.getBlockingMinerals();
		this.GeyserT = new HashMap<>();
		
		if(!this.Geysers.isEmpty()){
			for(Geyser g : this.Geysers){
				this.GeyserT.put(g.getUnit(), g.getCenter().toTilePosition());
			}
		}
		
		for(Mineral min : bass.getMinerals()){
			mineralG.put(min.getUnit().getID(), new ArrayList<>());
		}
		
	}
	
	void assignWorker(Unit worker){
		
		if(Pawns.contains(worker) == false){
			this.Pawns.add(worker);
		}
		
	}
	
	void UpdateThings(){

	}
	
	void doThings(){
		
		if(this.Pawns.isEmpty() == false){
			for(Unit unit : new ArrayList<>(this.Pawns)){ 
				
				game.drawLineMap(unit.getPosition(), depot.getPosition(), Color.Black);
						
				if(unit.isIdle() == true && unit.isCompleted() == true && !voidedWorkers.contains(unit)){
					if(unit.getDistance(this.Base.getCenter()) > 500 && !unit.isConstructing() && depot.isCompleted()){
						unit.move(this.Base.getCenter());
					}
					else {
					DoThingsYouLazyCunt(unit);
					}
				}
				
				if(isGasWorker(unit) && !voidedWorkers.contains(unit)){
					if(!unit.getOrder().equals(Order.HarvestGas) || !unit.getOrder().equals(Order.ReturnGas)){
						Unit refinery = getAssignedRefinery(unit);
						if(refinery != null && unit.isGatheringGas() == false){
							unit.gather(refinery);
						}
					}
				}
				
				if(unit == null || !unit.exists()){
					this.pawnDeath(unit);
				}
				
				if(unit.getType().isBuilding()){
					this.pawnDeath(unit);
				}
				
				// end of pawn loop
				
				if(unit.isGatheringGas() && !isGasWorker(unit)){
					unit.stop();
				}
				
				
			}
			
//			for(Geyser g : this.Geysers){
//				game.drawLineMap(this.GeyserT.get(g).toPosition(), this.depot.getPosition(), Color.Blue);
//			}
			
			for(Unit unit : gasWorkers.keySet()){
				ArrayList<Unit> workers = gasWorkers.get(unit);
				game.drawTextMap(unit.getPosition(), "Workers: " + workers.size() + " /3");
			}
			
			
			for(Unit unit : this.Gases){
				if(this.gasWorkers.get(unit).size() < 3 && canDoTheGreenStuff()){
					Unit worker = getPawnGatheringMins();
					if(worker != null){
						ArrayList<Unit> list = this.gasWorkers.get(unit);
						list.add(worker);
						worker.gather(unit);
						//System.out.println("Assigning a new gas worker to refinery");
						this.gasWorkers.put(unit, list);
						Unit m = getAssignedWorker(worker);
						if(m != null){
							ArrayList<Unit> ret = mineralG.get(m.getID());
							if(ret.contains(worker)){
								ret.remove(worker);
								mineralG.put(m.getID(), ret);
							}
						}
						break;
					}
				}
			}
			
			if(this.blockingMinerals.isEmpty()){
				for(Mineral min : new ArrayList<>(this.blockingMinerals)){
					Unit yes = min.getUnit();
					game.drawCircleMap(yes.getPosition(), yes.getType().width(), Color.Cyan);
					if(yes.getPosition() != Position.Unknown){
						game.drawTextMap(yes.getPosition(), "HELLO YES I AM A CUNT");
					}
				}
			}

		}
		// update the list with the new shit
			
		for(Mineral min : new ArrayList<>(this.Mins)){
			Unit unit = min.getUnit();
			if(game.isVisible(unit.getTilePosition())){
				if(!unit.getType().isMineralField()){
					this.MineralDeplete(unit);
					continue;
				}
			}
			

			ArrayList<Unit> w = mineralG.get(min.getUnit().getID());
			for(Unit u : w){
				if(!u.exists() || u.getType().isBuilding() || u.isGatheringGas()){
					if(w.contains(u)){	
					w.remove(unit);
					mineralG.put(min.getUnit().getID(), w);
					continue;
					//System.out.println("Caught invalid worker for base: " + this.id);
					}
				}
				
//				if(u.isGatheringMinerals()){
//					Unit t = getUnitTarget(u);
//					if(t != null){
//						if(t.getPosition().getApproxDistance(this.Base.getCenter()) > 1000){
//							
//						}
//					}
//				}
				
				if(u.isGatheringMinerals() && !u.isCarryingMinerals() && !voidedWorkers.contains(u)){
					Unit t = getUnitTarget(u);
					Unit myT = getAssignedWorker(u);
					if(t != null){
						if(t!=myT){
							// not my target mineral patch
							u.gather(myT);
						}
					}
				}
			}
			
			

		}
		
		for(Unit unit : new ArrayList<>(this.gasWorkers.keySet())){
			ArrayList<Unit> yes = new ArrayList<>(this.gasWorkers.get(unit));
			for(Unit unitt : yes){
				if(!unitt.exists()){
					yes.remove(unitt);
					this.gasWorkers.put(unit, yes);
					//System.out.println("Caught dead gas worker");
					// https://www.youtube.com/watch?v=NeQM1c-XCDc
					// du hast du hast
					// Hast viel geweint
				}
			}
		}
		
	}
	
	void DoThingsYouLazyCunt(Unit worker){
		boolean kitkat = false;
		if(!voidedWorkers.contains(worker)){
			if(!this.Gases.isEmpty() && canDoTheGreenStuff()){
				main:
				for(Unit unit : new ArrayList<Unit>(this.Gases)){
					if(isGas(unit)){
						if(this.gasWorkers.containsKey(unit) == true){
							if(this.gasWorkers.get(unit).size() != 3){
								if(isGasWorker(worker) == false){
								ArrayList<Unit> list = this.gasWorkers.get(unit);
								worker.gather(unit);
								list.add(worker);
								kitkat = true;
								this.gasWorkers.put(unit, list);
								//System.out.println("Assigning unit: " + worker.getID() + " To gather gas");
								}
							}
							else {
								Unit g = getAssignedWorker(worker);
								if(g == null){
									GatherMinerals(worker);
								}
								else {
				
									Unit t = getUnitTarget(worker);
									if(t != null){
										for(Mineral min : new ArrayList<>(blockingMinerals)){
											if(min.getUnit() == t){
												continue main;
											}
										}
									}
									
									if(worker.canGather(g)){
										worker.gather(g);
									}
								}
							}
						}
					}
				}
			}
			
			if(kitkat == false){
				if(blockingMinerals.isEmpty()){
					Unit g = getAssignedWorker(worker);
					if(g == null){
						GatherMinerals(worker);
					}
					else {
						if(worker.canGather(g)){
							worker.gather(g);
						}
					}
				}
				else {
					for(Mineral m : this.blockingMinerals){
						worker.gather(m.getUnit());
						return;
					}
				}
			}
			
			
		}
		
	}
	
	
	boolean isGas(Unit unit){
		
		if(unit.getType() == UnitType.Terran_Refinery || unit.getType() == UnitType.Zerg_Extractor || unit.getType() == UnitType.Protoss_Assimilator){
			return true;
		}
		else {
			return false;
		}
		
	}
	
	int workerCount(Unit resource){
		int i = 0;
		if(this.Pawns.isEmpty() == false){
			for(Unit unit : new ArrayList<>(this.Pawns)){
				if(unit.isGatheringGas() || unit.isGatheringMinerals()){
					if(unit.getTarget() != null){
						if(unit.getTarget().equals(resource)){
							i = i + 1;
							// i++ is for c++ losers
							// ^^^ THIS.
						}
					}
				}
				
			}
			
			return i;
		}
		
		return 0;
	}
	
	boolean isHarvesting(Unit worker){
		if(worker.isGatheringGas() || worker.isGatheringMinerals() || worker.isCarryingGas() || worker.isCarryingMinerals()){
			return true;
		}
		else {
			return false;
		}
		
	}
	
	void newRefinery(Unit refinery){
		this.Gases.add(refinery);
		
		if(this.gasWorkers == null){
			this.gasWorkers = new HashMap<Unit, ArrayList<Unit>>();
		}
		
		if(!this.gasWorkers.containsKey(refinery)){
			this.gasWorkers.put(refinery, new ArrayList<Unit>());
		}	
		
	}
	
	int getWorkers(){
		if(this.Pawns == null){
			return 0;
		}
		return this.Pawns.size();
	}
	
	
	
	void pawnDeath(Unit pawn){
		
		if(this.Pawns.contains(pawn)){
			this.Pawns.remove(pawn);
		}
		
		if(isGasWorker(pawn)){	
			Unit refinery = getAssignedRefinery(pawn);
			if(refinery != null){
				ArrayList<Unit> list = gasWorkers.get(refinery);
				list.remove(pawn);
				gasWorkers.put(refinery, list);
			}
			
			//System.out.println(this.depot.getPosition().getApproxDistance(refinery.getPosition()));
			
		}
		
		for(Mineral m : new ArrayList<>(this.Mins)){
			ArrayList<Unit> ret = mineralG.get(m.getUnit().getID());
			if(ret.contains(pawn)){
				ret.remove(pawn);
				mineralG.put(m.getUnit().getID(), ret);
				break;
			}
		}
	
		
	}
	
	boolean isGasWorker(Unit unit){
		for(Unit as : new ArrayList<>(this.gasWorkers.keySet())){
			ArrayList<Unit> list = this.gasWorkers.get(as);
			if(list.contains(unit) == true){
				return true;
			}
		}
		
		return false;
	}
	
	Unit getAssignedRefinery(Unit unit){
		for(Unit as : new ArrayList<>(this.gasWorkers.keySet())){
			ArrayList<Unit> list = this.gasWorkers.get(as);
			if(list.contains(unit) == true){
				return as;
			}
		}
		
		return null;
	}
	
	void newVoidedWorker(Unit pawn){
		
		if(!this.voidedWorkers.contains(pawn)){
			this.voidedWorkers.add(pawn);
		}
		
		
	}
	
	void unVoidWorker(Unit pawn){
		if(this.voidedWorkers.contains(pawn)){
			voidedWorkers.remove(pawn);
			pawn.stop();
		}
	}
	
	void unVoidWorker2(Unit pawn){
		if(this.voidedWorkers.contains(pawn)){
			voidedWorkers.remove(pawn);
		}
	}
	
	boolean isGatheringGas(Unit pawn){
		if(pawn.getOrder().equals(Order.HarvestGas) || pawn.getOrder().equals(Order.ReturnGas)){
			return true;
		}
		
		return false;
	}
	
	void EmptyPawns(){
		this.Pawns.clear();
		this.gasWorkers.clear();	
	}
	
	void MineralDeplete(Unit unit){
		for(Mineral min : new ArrayList<>(this.Mins)){
			if(min.getUnit().equals(unit)){
				this.Mins.remove(min);
				this.maxWorkers = (Mins.size() / 2) + (Geysers.size() * 3);
				if(mineralG.containsKey(unit.getID())){
					mineralG.remove(unit.getID());
				}
				//System.out.println("asdf");
			}
		}
	}
	
	void unAssignFromBase(Unit pawn){
		this.Pawns.remove(pawn);	
	}
	
	Unit getPawnGatheringMins(){
		
		if(this.Pawns.isEmpty()){
			return null;
		}
		
		for(Unit unit : new ArrayList<Unit>(this.Pawns)){
			if(!isGasWorker(unit)){
				return unit;
			}
		}
		
		return null;
	}
	
	void GatherMinerals(Unit worker){
		
		
		Unit closestMineral = getTheBestBlueCuntToDoCuntyThingsTo();
		if (closestMineral != null) {
			ArrayList<Unit> ret = mineralG.get(closestMineral.getID());
			if(!ret.contains(worker)){
				ret.add(worker);
			}
			mineralG.put(closestMineral.getID(), ret);
			if(!closestMineral.isVisible()){
				worker.move(this.depot.getPosition());
			}
			else{
				if(worker.canGather(closestMineral)){
					worker.gather(closestMineral, false);
				}
			}
		}
	}
	
	
	void DepotFinished(){
		Position pos = this.depot.getPosition();
		for(Unit unit : new ArrayList<Unit>(this.Pawns)){
			if(unit.isIdle() && unit.getDistance(pos) > 500){
				unit.move(pos);
			}
			
			if(unit.getOrderTargetPosition().getApproxDistance(pos) >= 500){
				unit.move(pos);
			}
			
		}
	}
	
	void blockingMineralDestroy(Unit unit){
		for(Mineral min : new ArrayList<>(this.blockingMinerals)){
			Unit yes = min.getUnit();
			if(yes.equals(unit)){
				this.blockingMinerals.remove(min);
			}
		}
	}
	
	boolean hasBlockingMin(Unit unit){
		
		if(this.blockingMinerals.isEmpty()){
			return false;
		}
		
		
		for(Mineral min : new ArrayList<>(this.blockingMinerals)){
			Unit yes = min.getUnit();
			if(yes.equals(unit)){
				return true;
			}
		}
		
		return false;
	}
	
	boolean canDoTheGreenStuff(){
		
		if(!game.self().getRace().equals(Race.Zerg)){
			if(game.getFrameCount() >= 15000 && game.self().gas() <= 200){
				return true;
			}
		}
		
		return this.Pawns.size() >= 8;
	}
	
	Unit getTheBestBlueCuntToDoCuntyThingsTo(){
		Unit r = null;
		int l = getHighestGatheredCount();
		
		if(this.Mins.isEmpty()){
			//System.out.println("No mins for workers to gather at base: (BotBase)" + this.id);
			return null;
		}
		
		for(Mineral m : this.Mins){
			
			int w = mineralG.get(m.getUnit().getID()).size();
			
			if(w <= l){
				r = m.getUnit();
				l = w;
			}
			
		}
		
		if(r == null){
			//System.out.println("Cannot find mineral to gather to at: " + this.id);
		}
		
		return r;

	}
	
	Unit getAssignedWorker(Unit pawn){
		Unit r = null;
		for(Mineral m : new ArrayList<>(this.Mins)){
			ArrayList<Unit> workers = mineralG.get(m.getUnit().getID());
			if(workers != null){
				if(workers.contains(pawn)){
					return m.getUnit();
				}
			}
		}
		
		return r;
	}
	
	int getHighestGatheredCount(){
		int r = 0;
		for(Mineral m : new ArrayList<>(this.Mins)){
			int size = mineralG.get(m.getUnit().getID()).size();
			if(size > r){
				r = size;
			}
		}
		
		return r;
	}
	
	Unit getUnitTarget(Unit unit){
		Unit ret = null;
		if(unit.getTarget() != null){
			ret = unit.getTarget();
		}
		
		if(ret == null){
			if(unit.getOrderTarget() != null){
				ret = unit.getOrderTarget();
			}
		}
		
		return ret;
		
	}

}
