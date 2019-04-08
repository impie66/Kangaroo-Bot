package Bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import bwapi.Game;
import bwapi.Order;
import bwapi.Unit;
import bwapi.UnitType;
import bwem.unit.Geyser;
import bwem.unit.Mineral;
import bwem.*;
import Bot.*;


public class BotBase {
Unit depot;
int id;
int workers;
int defenceScore;
int maxWorkers;
ArrayList<Mineral> Mins;
ArrayList<Geyser> Geysers;
ArrayList<Unit>Pawns;
ArrayList<Unit>Gases;
HashMap<Unit, ArrayList<Unit>> gasWorkers;
ArrayList<Unit> voidedWorkers;
Base Base;
Game game;

	public BotBase(Game gamee, Unit unit, Base bass){
		this.depot = unit;
		this.Base = bass;
		this.game = gamee;
		this.Pawns = new ArrayList<Unit>();
		this.gasWorkers = new HashMap<Unit, ArrayList<Unit>>();
		this.Gases = new ArrayList<Unit>();
		this.Mins = new ArrayList<Mineral>(bass.getMinerals());
		this.Geysers = new ArrayList<Geyser>(bass.getGeysers());
		this.maxWorkers = 6 + (Mins.size() / 2) + (Geysers.size() * 3);
		this.voidedWorkers = new ArrayList<Unit>();
	}
	
	void assignWorker(Unit worker){
		
		if(Pawns.contains(worker) == false){
			this.Pawns.add(worker);
		}
		
	}
	
	void doThings(){
		
		if(this.Pawns.isEmpty() == false){
			for(Unit unit : new ArrayList<>(this.Pawns)){
				
				if(unit.isIdle() == true && unit.isCompleted() == true && !voidedWorkers.contains(unit)){
					if(unit.getDistance(this.Base.getCenter()) > 500){
						unit.move(this.Base.getCenter());
					}
					else {
					DoThingsYouLazyCunt(unit);
					}
				}
				
				if(isGasWorker(unit) && !voidedWorkers.contains(unit)){
					if(unit.getOrder()!= Order.HarvestGas || unit.getOrder()!=Order.ReturnGas){
						Unit refinery = getAssignedRefinery(unit);
						if(refinery != null && unit.isGatheringGas() == false){
							unit.gather(refinery);
						}
					}
				}
				
				game.drawTextMap(unit.getPosition(), "" + unit.getID());
				
				
				if(unit == null || !unit.exists()){
					this.Pawns.remove(unit);
				}
				
				if(unit.getType().isBuilding()){
					this.Pawns.remove(unit);
				}
				
				if(this.Pawns.size() > this.maxWorkers){
					pawnDeath(unit);
				}
				
				// end of pawn loop
				
				
			}
			
			for(Unit unit : gasWorkers.keySet()){
				ArrayList<Unit> workers = gasWorkers.get(unit);
				game.drawTextMap(unit.getPosition(), "Workers: " + workers.size() + " /3");
			}
			

		}
		// update the list with the new shit
				
		
	}
	
	void DoThingsYouLazyCunt(Unit worker){
		
		if(!voidedWorkers.contains(worker)){

		if(this.Gases.isEmpty() == false && this.Pawns.size() >= 6){
			for(Unit unit : new ArrayList<Unit>(this.Gases)){
				if(isGas(unit)){
					if(this.gasWorkers.containsKey(unit) == true){
						if(this.gasWorkers.get(unit).size() != 3){
							if(isGasWorker(worker) == false){
							ArrayList<Unit> list = this.gasWorkers.get(unit);
							worker.gather(unit);
							list.add(worker);
							this.gasWorkers.put(unit, list);
							//System.out.println("Assigning unit: " + worker.getID() + " To gather gas");
							}
						}
					}
				}
			}
		}
		
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
		if(this.Pawns.contains(pawn) == true){
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
	
	void FuckConc(Unit pawn){	
//		for(Iterator<Unit> aids = this.Pawns.iterator(); aids.hasNext();){
//			Unit unit = aids.next();
//			if(unit.equals(pawn)){
//				aids.remove();
//			}
//		}
		
		
	}
	
	boolean isGatheringGas(Unit pawn){
		if(pawn.getOrder().equals(Order.HarvestGas) || pawn.getOrder().equals(Order.ReturnGas)){
			return true;
		}
		
		return false;
	}
	
	void EmptyPawns(){
		this.Pawns.clear();
	}
	
	void MineralDeplete(Unit min){
		if(this.Mins.contains(min)){
			this.Mins.remove(min);
			this.maxWorkers = this.maxWorkers - 2;
		}
	}
	

}
