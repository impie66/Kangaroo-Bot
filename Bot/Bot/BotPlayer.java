package Bot;
import java.util.ArrayList;

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
	int workerAmount;
	Game game;
	Util util;
	Data data;
	Player player;
	int armyScore;
	int defenceScore;
	int mIncome = 0;
	int gIncome = 0;
	
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

	}
	
	
	void newEnemyBuilding(Unit unit){
		if(!this.Buildings.contains(unit)){
			this.Buildings.add(unit);
		//System.out.println("Enemy Building Discovered: " + unit.getType().toString() + " For Player: " + unit.getPlayer().getName());
		}
		
		if(!this.attackPositions.contains(unit.getPosition())){
			this.attackPositions.add(unit.getPosition());
		}
		
		if(!this.race.equals(unit.getPlayer().getRace())){
			this.race = unit.getPlayer().getRace();
		}
	}
	
	void newEnemyBase(Base unit){
		if(!this.Bases.contains(unit)){
			this.Bases.add(unit);
			Base bass = Util.getClosestBaseLocation(unit.getCenter());
			if(!this.attackPositions.contains(unit.getCenter())){
				this.attackPositions.add(unit.getCenter());
			}
			if(data.Expands.contains(bass)){
				data.Expands.remove(bass);
			}
		}
		
	}
	
	// Unit unit, UnitType type, int h, int s, int e, int ID
	void newMilUnit(Unit unit){	
		if(game.enemies().contains(unit.getPlayer())){
			UnitType type = unit.getType();
			if(!this.MilUnits.contains(unit) && util.IsMilitrayUnit(unit)){
				this.MilUnits.add(unit);
				this.Types.add(unit.getType());
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
		this.mIncome = this.workerAmount * 35;
		this.gIncome = this.player.allUnitCount(this.race.getRefinery()) * 20;
		if(mIncome < 0){
			if(this.race.equals(Race.Terran)){
				mIncome = this.player.allUnitCount(UnitType.Terran_Command_Center) * 200;
			}
			
			if(this.race.equals(Race.Protoss)){
				mIncome = this.player.allUnitCount(UnitType.Protoss_Nexus) * 200;
			}
			
			if(this.race.equals(Race.Zerg)){
				mIncome = this.player.allUnitCount(UnitType.Zerg_Drone) * 35;
			}
			
		}
	}
	
	ArrayList<UnitType> addGhostUnits(){
		updateIncome();
		ArrayList<UnitType> yes = new ArrayList<UnitType>();
		
		if(this.race.equals(Race.Terran)){
			for(int i = 0; i < mIncome / 100; i++){
				yes.add(UnitType.Terran_Marine);
			}
			
			for(int i = 0; i < mIncome / 100; i++){
				yes.add(UnitType.Terran_Medic);
			}
			
			for(int i = 0; i < mIncome / 300; i++){
				yes.add(UnitType.Terran_Siege_Tank_Tank_Mode);
			}
			
			for(int i = 0; i < mIncome / 200; i++){
				yes.add(UnitType.Terran_Vulture);
			}
		}
		else if (this.race.equals(Race.Protoss)) {
			for(int i = 0; i < mIncome / 100; i++){
				yes.add(UnitType.Protoss_Zealot);
			}
			
			for(int i = 0; i < mIncome / 100; i++){
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
		
		if(this.Types.isEmpty()){
			return 0;
		}
		
		int i = 0;
		for(UnitType unit : this.Types){
			if(unit.equals(type)){
				i++;
			}
		}
		
		return i;
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
	
	
}
