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
					break;
					}
				}
			}
			
			if(this.blockingMinerals.isEmpty()){
				for(Mineral min : new ArrayList<>(this.blockingMinerals)){
					Unit yes = min.getUnit();
					game.drawCircleMap(yes.getPosition(), yes.getType().width(), Color.Cyan);
				}
			}

		}
		// update the list with the new shit
			
		for(Mineral min : new ArrayList<>(this.Mins)){
			Unit unit = min.getUnit();
			if(game.isVisible(unit.getTilePosition())){
				if(!unit.getType().isMineralField()){
					this.MineralDeplete(unit);
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
								GatherMinerals(worker);
							}
						}
					}
				}
			}
			
			if(kitkat == false){
				if(blockingMinerals.isEmpty()){
				GatherMinerals(worker);
				}
				else {
					for(Mineral min : this.blockingMinerals){
						Unit unit = min.getUnit();
						if(worker.canGather(unit) && worker.hasPath(unit.getPosition())){
							worker.gather(unit);
						}
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
		Unit closestMineral = null;
		for (Mineral things : new ArrayList<Mineral>(this.Mins)) {
			Unit neutralUnit = things.getUnit();
			if (neutralUnit.isBeingGathered() == false) {
				if (closestMineral == null
						|| worker.getDistance(neutralUnit) < worker.getDistance(closestMineral)) {
					closestMineral = neutralUnit;
				}
			}
		}

		if (closestMineral != null) {
			worker.gather(closestMineral, false);
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
		
		if(game.getFrameCount() >= 15000){
			return true;
		}
		
		return this.Pawns.size() >= 8;
	}

}
