package Bot;
import java.util.ArrayList;
import java.util.HashMap;

import bwapi.*;
import bwem.Base;
import bwem.ChokePoint;
import bwta.Chokepoint;

public class BotPlayer {
	Race race;
	int enemyScore;
	ArrayList<Position> attackPositions;
	ArrayList<Unit> units;
	ArrayList<UnitType> Types;
	ArrayList<UnitType> DTypes;
	ArrayList<Unit> Buildings;
	ArrayList<Base> Bases;
	ArrayList<Unit> MilUnits;
	ArrayList<Unit> DBuildings;
	ArrayList<ChokePoint> CCP;
	HashMap<UnitType, Integer> unitCount;
	ArrayList<Position> basePositions;
	Base startLocation;
	int workerAmount;
	Game game;
	Util util;
	Data data;
	Player player;
	int armyScore;
	int defenceScore;
	int mIncome = 0;
	int gIncome = 0;
	int Aggression = 1;
	int scanEnergy;
	ArrayList<Unit> c;
	
	public BotPlayer(Race racee, Game gaem, Util till, Data myData, Player p){
		this.race = racee;
		this.attackPositions = new ArrayList<>();
		this.units = new ArrayList<>();
		this.Types = new ArrayList<>();
		this.DTypes = new ArrayList<>();
		this.game = gaem;
		this.Buildings =  new ArrayList<>();
		this.util = till;
		this.data = myData;
		this.enemyScore = 0;
		this.player = p;
		this.DBuildings = new ArrayList<>();
		this.Bases = new ArrayList<>();
		this.MilUnits = new ArrayList<>();
		this.armyScore = 0;
		this.defenceScore = 0;
		this.mIncome = 0;
		this.gIncome = 0;
		this.workerAmount = 0;
		this.CCP = new ArrayList<ChokePoint>();
		this.unitCount = new HashMap<>();
		this.Aggression = 1;
		this.basePositions = new ArrayList<>();
		this.scanEnergy = 0;
		this.startLocation = null;
		this.c = new ArrayList<>();

	}
	
	
	void newEnemyBuilding(Unit unit){
		if(!this.Buildings.contains(unit)){
			this.Buildings.add(unit);
			if(unit.getType().equals(UnitType.Terran_Comsat_Station)){
				this.scanEnergy = this.scanEnergy + unit.getEnergy();
			}
		//System.out.println("Enemy Building Discovered: " + unit.getType().toString() + " For Player: " + unit.getPlayer().getName());
		}
		
		if(!this.attackPositions.contains(unit.getPosition())){
			this.attackPositions.add(unit.getPosition());
		}
		
		if(!this.race.equals(unit.getType().getRace())){
			this.race = unit.getType().getRace();
		}
	}
	
	
	void newUnitToCount(Unit unit){
		UnitType type = unit.getType();
		// THIS ONLY COUNTS
		// FOR THE UNITCOUNT
		// DOES NOTHING.
		// https://www.youtube.com/watch?v=e6QZCU9rTiw
		

		if(!this.unitCount.keySet().contains(type)){
			this.unitCount.put(type, 0);
		}
		else {
			if(!c.contains(unit)){
				c.add(unit);
				int value = this.unitCount.get(type);
				this.unitCount.put(type, value + 1);
				if(type.isBuilding() && type.getRace().equals(Race.Zerg)){
					if(!this.unitCount.keySet().contains(UnitType.Zerg_Drone)){
						this.unitCount.put(UnitType.Zerg_Drone, 0);
					}
					
					if(this.unitCount.get(UnitType.Zerg_Drone) > 0){
					this.unitCount.put(UnitType.Zerg_Drone, this.unitCount.get(UnitType.Zerg_Drone) - 1);
					}
				}
				
				
				if(type.equals(UnitType.Protoss_Archon)){
					if(!this.unitCount.keySet().contains(UnitType.Protoss_High_Templar)){
						this.unitCount.put(UnitType.Protoss_High_Templar, 0);
					}
					
					if(this.unitCount.get(UnitType.Protoss_High_Templar) > 0){
					this.unitCount.put(UnitType.Protoss_High_Templar, this.unitCount.get(UnitType.Protoss_High_Templar) - 2);
					}
				}
				
				if(type.equals(UnitType.Protoss_Dark_Archon)){
					if(!this.unitCount.keySet().contains(UnitType.Protoss_Dark_Templar)){
						this.unitCount.put(UnitType.Protoss_Dark_Templar, 0);
					}
					
					if(this.unitCount.get(UnitType.Protoss_Dark_Templar) > 0){
					this.unitCount.put(UnitType.Protoss_Dark_Templar, this.unitCount.get(UnitType.Protoss_Dark_Templar) - 2);
					}
				}
				
				if(type.equals(UnitType.Zerg_Lurker)){
					if(!this.unitCount.keySet().contains(UnitType.Zerg_Hydralisk)){
						this.unitCount.put(UnitType.Zerg_Hydralisk, 0);
					}
					
					if(this.unitCount.get(UnitType.Zerg_Hydralisk) > 0){
					this.unitCount.put(UnitType.Zerg_Hydralisk, this.unitCount.get(UnitType.Zerg_Hydralisk) - 1);
					}
				}
				

			}
		}
		

		
		
		
		
	}
	
	void newEnemyBase(Base unit){
		if(!this.Bases.contains(unit)){
			this.Bases.add(unit);
			Base bass = Util.getClosestBaseLocation(unit.getCenter());
			if(!this.attackPositions.contains(unit.getCenter())){
				this.attackPositions.add(unit.getCenter());
			}
			
			if(!this.basePositions.contains(unit.getCenter())){
				this.basePositions.add(unit.getCenter());
			}
			
			if(data.Expands.contains(bass)){
				data.Expands.remove(bass);
			}
			
			if(bass.isStartingLocation() && this.startLocation == null){
				util.Print("Has found enemy start location");
				this.startLocation = bass;
			}
			
			
			
		}
		
	}
	
	// Unit unit, UnitType type, int h, int s, int e, int ID
	void newMilUnit(Unit unit){	
		if(game.enemies().contains(unit.getPlayer())){
			UnitType type = unit.getType();
			
			if(!this.race.equals(unit.getType().getRace())){
				this.race = unit.getType().getRace();
			}
			

			
			if(!this.MilUnits.contains(unit) && util.IsMilitrayUnit(unit)){
				
				this.MilUnits.add(unit);
				this.Types.add(type);
				if(type.equals(UnitType.Zerg_Lurker)){
					if(this.Types.contains(UnitType.Zerg_Hydralisk)){
						this.Types.remove(UnitType.Zerg_Hydralisk);
						this.enemyScore = this.enemyScore - util.getScoreOf(UnitType.Zerg_Hydralisk);
						this.armyScore = this.armyScore - data.getScoreOf(unit);
					}
					
					this.unitCount.put(UnitType.Zerg_Hydralisk, this.unitCount.get(UnitType.Zerg_Hydralisk) - 1);
					
				}
				
				if(type.equals(UnitType.Zerg_Guardian)){
					if(this.Types.contains(UnitType.Zerg_Mutalisk)){
						this.Types.remove(UnitType.Zerg_Mutalisk);
						this.enemyScore = this.enemyScore - util.getScoreOf(UnitType.Zerg_Mutalisk);
						this.armyScore = this.armyScore - data.getScoreOf(unit);
					}
					this.unitCount.put(UnitType.Zerg_Mutalisk, this.unitCount.get(UnitType.Zerg_Mutalisk) - 1);
				}
				
				
				this.enemyScore = this.enemyScore + util.getScoreOf(unit);
				if(!unit.getType().isBuilding()){
				armyScore = armyScore + data.getScoreOf(unit);
				}
				
				//System.out.println("Enemy Unit: " + unit.getType().toString());
			}
			
			
		}
	}
	
	
	void unitDeath(Unit unit){

		if(game.enemies().contains(unit.getPlayer())){
			
			if(this.MilUnits.contains(unit)){
				this.MilUnits.remove(unit);
				this.enemyScore = this.enemyScore - util.getScoreOf(unit);
				Types.remove(unit.getType());
				if(!unit.getType().isBuilding()){
					armyScore = armyScore - data.getScoreOf(unit);
				}
				//System.out.println("Enemy Unit Death: " + unit.getType().toString());
			}
			
			if(this.Buildings.contains(unit)){
				this.Buildings.remove(unit);
				if(this.attackPositions.contains(unit.getPosition())){
					this.attackPositions.remove(unit.getPosition());
				}
			}
					
			if(DBuildings.contains(unit)){
				DBuildings.remove(unit);
				DTypes.remove(unit.getType());
				this.enemyScore = this.enemyScore + util.getScoreOf(unit);
				defenceScore = defenceScore - data.getScoreOf(unit);
			}
			
			if(this.Bases.contains(unit)){
				this.Bases.remove(unit);
			}
			
			if(this.basePositions.contains(unit)){
				this.basePositions.remove(unit);
			}
						
		}
		
		
		
		
	}
	
	
	void newUnitToUnCount(Unit unit){
		if(!this.unitCount.keySet().contains(unit.getType())){
			this.unitCount.put(unit.getType(), 0);
		}
		else {
			int value = this.unitCount.get(unit.getType());
			this.unitCount.put(unit.getType(), value - 1);
			
			if(this.unitCount.get(unit.getType()) < 0){
				this.unitCount.put(unit.getType(), 0);
			}
			
		}
	}
	
	
	void newDBuilding(Unit unit){
		if(game.enemies().contains(unit.getPlayer())){
			if(!DBuildings.contains(unit)){
				DBuildings.add(unit);
				DTypes.add(unit.getType());
				this.enemyScore = this.enemyScore + util.getScoreOf(unit);
				defenceScore = defenceScore + data.getScoreOf(unit);
			}
		}
	}
	
	void updateRace(Race yes){
		this.race = yes;
	}
	
	void updateIncome(){
		this.mIncome = (this.howManyHave(this.race.getWorker()) * 35) + 50;
		this.gIncome = (this.howManyHave(this.race.getRefinery()) * 20) + 50;
	}
	
	ArrayList<UnitType> addGhostUnits(){
		updateIncome();
		ArrayList<UnitType> yes = new ArrayList<UnitType>();
		
		if(this.race.equals(Race.Terran)){
			if(this.howManyHave(UnitType.Terran_Barracks) > this.howManyHave(UnitType.Terran_Factory)){
				for(int i = 0; i < mIncome / 100; i++){
					yes.add(UnitType.Terran_Marine);
				}
				
				for(int i = 0; i < mIncome / 100; i++){
					yes.add(UnitType.Terran_Medic);
				}
			} 
			else {
				for(int i = 0; i < mIncome / 300; i++){
					yes.add(UnitType.Terran_Siege_Tank_Tank_Mode);
				}
					
				for(int i = 0; i < mIncome / 200; i++){
					yes.add(UnitType.Terran_Vulture);
				}
			}
			
		}
		else if (this.race.equals(Race.Protoss)) {
			for(int i = 0; i < (mIncome / 100) / 2; i++){
				yes.add(UnitType.Protoss_Zealot);
			}
			
			for(int i = 0; i < (mIncome / 100) / 2; i++){
				yes.add(UnitType.Protoss_Dragoon);
			}
		}
		else {
			for(int i = 0; i < mIncome / 25; i++){
				yes.add(UnitType.Zerg_Zergling);
			}
			
			for(int i = 0; i < mIncome / 25; i++){
				yes.add(UnitType.Zerg_Hydralisk);
			}
		}
		
		
		return yes;
	}
	
	void newWorker(){
		this.workerAmount++;
	}
	
	void WorkerDeath(){
		this.workerAmount--;
	}
	
	
	int howManyHave(UnitType type){
		
		if(!this.unitCount.containsKey(type)){
			this.unitCount.put(type, 0);
		}
		
		return this.unitCount.get(type);
	}
	
	ArrayList<UnitType> getOffensiveUnits(){
		ArrayList<UnitType> yes = new ArrayList<>();	
		for(UnitType type : this.Types){
			yes.add(type);
		}
		
		for(UnitType typee : addGhostUnits()){
			yes.add(typee);
		}

		return yes;
	}
	
	void NewChokePoint(ChokePoint cp){
		if(!this.CCP.contains(cp)){
			this.CCP.add(cp);
		}
	}
	
	void RemoveChokePoint(ChokePoint cp){
		if(this.CCP.contains(cp)){
			this.CCP.remove(cp);
		}
	}
	
	int howManyHaveNoBullshit(UnitType yeah){
		
		if(this.Types.isEmpty()){
			return 0;
		}
		
		int eye = 0;
		for(UnitType type : this.Types){
			if(type.equals(yeah)){
				eye++;
			}
		}
		
		return eye;
	}
	
	
	
	void update(){
	// hue
		if(this.race.equals(Race.Terran)){
			if(this.scanEnergy <= this.howManyHave(UnitType.Terran_Comsat_Station) * 200){
				this.scanEnergy = (int) Math.round(this.scanEnergy + 0.744);
				if(this.scanEnergy >=  this.howManyHave(UnitType.Terran_Comsat_Station) * 200){
					this.scanEnergy = this.howManyHave(UnitType.Terran_Comsat_Station) * 200;
				}
				
				// end of scanEnergy
			}
		}
		
		
		for(Position pos : new ArrayList<>(this.basePositions)){
			if(game.isVisible(pos.toTilePosition()) && !data.EnemysNearby(pos, 400)){
				this.basePositions.remove(pos);
				util.Print("Hey! base is not here");
			}
		}
		
		
		
	}
	
	
	boolean canScan(){
		if(this.player.equals(Race.Terran)){
			return this.scanEnergy >= 50;
		}
		else{
			return false;
		}
	}
	
}
