package Bot;
import java.util.ArrayList;

import bwapi.*;
import bwem.Base;

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
	Game game;
	Util util;
	Data data;
	Player player;
	int armyCost;
	
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
		this.armyCost = 0;
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
	
	void newMilUnit(Unit unit){	
		if(game.enemies().contains(unit.getPlayer())){
			UnitType type = unit.getType();
			if(!this.MilUnits.contains(unit) && util.IsMilitrayUnit(unit)){
				this.MilUnits.add(unit);
				this.Types.add(type);
				this.enemyScore = this.enemyScore + util.getScoreOf(unit);
				armyCost = armyCost + unit.getType().mineralPrice() + unit.getType().gasPrice();
				//System.out.println("Enemy Unit: " + unit.getType().toString());
			}
			
			
		}
	}
	
	void unitDeath(Unit unit){

		if(game.enemies().contains(unit.getPlayer())){
			if(this.MilUnits.contains(unit)){
				this.MilUnits.remove(unit);
				this.Types.remove(unit.getType());
				this.enemyScore = this.enemyScore - util.getScoreOf(unit);
				armyCost = armyCost - unit.getType().mineralPrice() - unit.getType().gasPrice();
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
			}
						
		}
	}
	
	
	void newDBuilding(Unit unit){
		if(game.enemies().contains(unit.getPlayer())){
			if(!DBuildings.contains(unit)){
				DBuildings.add(unit);
				DTypes.add(unit.getType());
				this.enemyScore = this.enemyScore + util.getScoreOf(unit);
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
}
