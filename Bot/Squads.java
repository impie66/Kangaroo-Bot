package Bot;
import bwapi.*;

import java.util.ArrayList;
import java.util.HashMap;

import Bot.*;

class Squad {
ArrayList<Unit> units;	
int score;
Position target;
int State;
// 0 is not operating, 1 is operating
boolean AWR;
// ^^ attack when ready
int targetScore;
int priority;
// ^^ how important is this squad to get more units?
boolean retreating;
Position retreatPos;
int type;
String squadName;
int id;
Data myData;
DecisionManager manager;
Game game;
Unit detector;
HashMap<Unit, Integer> flee;
// 1 == attacker, 2 == defender, 3 == harasser


public Squad(ArrayList<Unit> unitss, int idd, Data Data, Game gam, DecisionManager man){
	this.units = unitss;
	this.score = getSquadScore();
	this.target = null;
	this.State = 0;
	this.AWR = false;
	this.targetScore = 0;
	this.game = gam;
	this.retreatPos = game.self().getStartLocation().toPosition();
	this.priority = 3;
	this.retreating = false;
	this.type = 0;
	this.squadName = "asdf";
	this.id = idd;
	this.myData = Data;
	this.manager = man;
	this.flee = new HashMap<>();
}

public Squad(ArrayList<Unit> unitss, int idd, Data Data, Game gam, DecisionManager man, Position ret){
	this.units = unitss;
	this.score = getSquadScore();
	this.target = null;
	this.State = 0;
	this.AWR = false;
	this.targetScore = 0;
	this.game = gam;
	this.retreatPos = ret;
	this.priority = 3;
	this.retreating = false;
	this.type = 0;
	this.squadName = "asdf";
	this.id = idd;
	this.myData = Data;
	this.manager = man;
	this.flee = new HashMap<>();
}

public Squad(ArrayList<Unit> unitss, int idd, int targets, Data Data, Game gam, DecisionManager man){
	this.units = unitss;
	this.score = getSquadScore();
	this.target = null;
	this.State = 0;
	this.AWR = false;
	this.retreatPos = game.self().getStartLocation().toPosition();
	this.targetScore = targets;
	this.priority = 3;
	this.retreating = false;
	this.type = 0;
	this.squadName = "asdf";
	this.id = idd;
	this.myData = Data;
	this.manager = man;
	this.game = gam;
	this.flee = new HashMap<>();
}

void onFrame(){
	
	
	for(Unit unit : new ArrayList<>(flee.keySet())){
		int a = this.flee.get(unit);
		int left = a - game.getFrameCount();
		game.drawTextMap(unit.getPosition(), "" + left);
		if(a <= game.getFrameCount()){
			this.flee.remove(unit);
		}
		
	
	}
	
	if(this.detector != null && !this.units.isEmpty()){
		Unit det = this.detector;
		game.drawCircleMap(this.detector.getPosition(), this.detector.getType().width(), Color.Green);
		Position pos = null;
		if(det.isIdle()){
			for(Unit unit : new ArrayList<Unit>(this.units)){
				if(unit.isAttacking() && det.getPosition().getApproxDistance(unit.getPosition()) >= 100){
					pos = unit.getPosition();
					break;
				}
			}
			
			for(Unit unit : new ArrayList<Unit>(this.units)){
				if(unit.isMoving() && det.getPosition().getApproxDistance(unit.getPosition()) >= 100){
					pos = unit.getPosition();
					break;
				}
			}
			
			if(pos != null){
				det.move(pos);
			}
		}
	}
	
	if(!this.units.isEmpty()){
		for(Unit unit : new ArrayList<>(this.units)){
			
			if(unit.getType().equals(UnitType.Zerg_Lurker) && unit.isBurrowed()){
				WeaponType type = unit.getType().groundWeapon();
				if(!EnemysNearby(unit.getPosition(), 200)){
					unit.unburrow();
				}
			}
			
				if(unit.getType().equals(UnitType.Zerg_Lurker) && !unit.isBurrowed()){
					WeaponType type = unit.getType().groundWeapon();
					if(EnemysNearby(unit.getPosition(), 200) && unit.canBurrow()){
						unit.burrow();
					}
				}
								
		}
	}
	
	
}

boolean isDefending(){
	if(this.State == 2){
		return true;
	}
	
	return false;
}

void setState(int state){
	this.State = state;
}

ArrayList<Unit> getUnits(){
	return this.units;
}

int getScore(){
	return this.score;
}

Position getTarget(){
	return this.target;
}

void newTarget(Position pos){
	this.target = pos;
}


boolean isOperating(){
	if(this.State == 1){
		return true;
	}
	
	return false;
}

String getName(){
	return this.squadName;
}

int getTargetScore(){
	return this.targetScore;
}

int getUnitSize(){
return this.units.size();
}

int priority(){
	return this.priority;
}

int getSquadScore(){
	int i = 0;
	if(this.units.isEmpty()){
		return i;
	}
	for(Unit unit : new ArrayList<Unit>(this.units)){
		i = i + getScoreOf(unit);
	}
	
	return i;
}

boolean AWR(){
	return this.AWR;
}

int getScoreOf(Unit unit){
	UnitType auxType = unit.getType();
	return ((auxType.destroyScore() * auxType.maxHitPoints()) / (auxType.maxHitPoints() * 2));
}

void absorbUnit(Unit unit){
	if(!this.units.contains(unit)){
		this.units.add(unit);
	}
	
}

int SquadsAverageDistTo(Position pos){
	int ffinal = 0;
	int i = this.getUnitSize();
	int o = 0;
	for(Unit unit : new ArrayList<Unit>(this.units)){
		o = o + unit.getPosition().getApproxDistance(pos);
	}
	ffinal = o / i;
	return ffinal;
}

void Regroup(Position pos){
		if(this.units.isEmpty() == false){
			
			for(Unit unit : new ArrayList<>(this.units)){
				
				int dist = unit.getPosition().getApproxDistance(pos);
				
				if(!this.flee.containsKey(unit)){
					if(unit.isIdle() && dist >= 100){
						unit.move(pos);
					}
					
					if(unit.getOrderTargetPosition().getApproxDistance(pos) > 100){
					unit.move(pos);
					}
					
					if(unit.isBurrowed() && unit.canUnburrow() && !isInCombat(unit)){
						unit.unburrow();
					}
				}
			}
			
		}
}

boolean shouldRegroup(){	
	Position pos = this.target;
	if(SquadsAverageDistTo(this.getUnits().get(0).getPosition())>=75 + (this.getUnitSize() * 2)){
		return true;
	}
	else {
		return false;
	}
}

void squadMicro(){
	
	if(shouldRegroup() && SquadsAverageDistTo(this.retreatPos) > 2000 && this.State == 1 && !myData.enemyMilUnits.isEmpty()){
		Regroup(this.getUnits().get(0).getPosition());
	}
	
	targetChecking();
	
	
	if(this.State == 2 && !EnemysNearby(this.target, 500)){
		this.State = 0;
		this.retreat();
	}
	
	
	
	
	if(this.target == null && this.State == 1){
		if(myData.nextAttackPosition!=null){
			this.target = this.myData.nextAttackPosition;
		}
	}
	
	if(isAtTarget(true) && this.target != this.myData.nextAttackPosition && this.State == 1){
		this.target = this.myData.nextAttackPosition;
	}
	
	if(this.State == 1 && this.target!=null){
		// attacking
		for(Unit unit : new ArrayList<>(this.units)){
			if(unit.getOrder() != Order.AttackMove && !isInCombat(unit) && !this.flee.containsKey(unit)){
				unit.attack(this.target);
			}
			
			if(unit.getType().equals(UnitType.Zerg_Lurker) && !unit.isMoving() && !isInCombat(unit)){
				unit.move(this.target);
			}
			
		}
	}
	
	if(this.State == 2 && target!=null){
		// defending
		for(Unit unit : new ArrayList<>(this.units)){
			if(!IsAttackMoving(unit) && !isInCombat(unit)){
				unit.attack(this.target);
			}
			
			if(unit.getType().equals(UnitType.Zerg_Lurker) && !unit.isMoving() && !isInCombat(unit)){
				unit.move(this.target);
			}
		}
	}
	

	
}
	
		


public boolean IsAttackMoving(Unit unit){
	if(unit.getOrder() == Order.AttackMove){
		return true;
	}
	
	return false;
}


public boolean isInCombat(Unit unit){
	if(unit.isAttacking() || unit.isUnderAttack() || unit.isStartingAttack()){
		return true;
	}
	
	return false;
}


boolean isAtTarget(boolean idle){
	int i = 0;
	int max = 0;
	
	if(this.target == null){
		return false;
	}
	
	if(this.units.isEmpty() == false){
		max = (int) ((this.units.size()) - (this.units.size() * 0.75));
		for(Unit unit : new ArrayList<>(this.units)){
			if(idle == true){
				if(unit.isIdle() && unit.getPosition().getApproxDistance(this.target) < 200){
					i++;
				}
			}
			else {
				if(unit.getPosition().getApproxDistance(this.target) < 200){
					i++;
				}
			}
		}
		System.out.println("Is at Target i: " + i + " / " + max);
		if(i>=max){
			return true;
		}
	}
	
	return false;
}

void operate(){
	
	if(myData.nextAttackPosition != null){
		
		if(this.target==null){
			this.target = myData.nextAttackPosition;
		}
		
		for(Unit unit : new ArrayList<>(this.units)){
			if(!this.flee.containsKey(unit)){
				if(unit.isIdle() && this.target != null){
					unit.attack(this.target);
				}
				
				if(this.target != null){
					if(unit.getOrderTargetPosition().getApproxDistance(this.target) > 300 && !isInCombat(unit) && !IsAttackMoving(unit)){
					unit.attack(this.target);
					}
				}
					
				if(unit.getType().equals(UnitType.Zerg_Lurker) && !unit.isMoving() && !isInCombat(unit)){
					unit.move(this.target);
				}
				
			}
		}
		
		if(this.detector!=null){
			if(!this.units.isEmpty() && !this.detector.isMoving()){
			this.detector.move(this.units.get(0).getPosition());
			}
			else {
				if(!this.detector.isMoving()){
				this.detector.move(this.target);
				}
			}
			
		}
		
		
	}
	
}

void retreat(){
	for(Unit unit : new ArrayList<>(this.units)){
		if(unit.getPosition().getApproxDistance(this.retreatPos) > 200){
		unit.move(this.retreatPos);
			if(unit.isBurrowed()){
				unit.unburrow();
				unit.move(this.retreatPos);
			}
		}
	}
	
	if(this.detector != null){
		this.detector.move(this.retreatPos);
	}
}

void unitDeath(Unit unit){
	this.units.remove(unit);
	if(this.detector != null){
		if(this.detector.equals(unit)){
			this.detector = null;
		}
	}
}

boolean isSquadFull(){
	if(this.units.size() >= 25){
		return true;
	}
	return false;
}

boolean EnemysNearby(Position pos){
	ArrayList<Unit> units = new ArrayList<> (game.getUnitsInRadius(pos, 500));
		for(Unit unit : units){
			if(game.enemies().contains(unit.getPlayer())){
				return true;
			}
		}
		
		return false;
	
}

boolean EnemysNearby(Position pos, int max){
	ArrayList<Unit> units = new ArrayList<> (game.getUnitsInRadius(pos, max));
		for(Unit unit : units){
			if(game.enemies().contains(unit.getPlayer())){
				return true;
			}
		}
		
		return false;
	
}

boolean EnemysNearbyInWeaponRange(WeaponType type, Unit unitt){
	ArrayList<Unit> units = new ArrayList<> (unitt.getUnitsInWeaponRange(type));
		System.out.println("Lurker Size: " + units.size());
		for(Unit unit : units){
			if(game.enemies().contains(unit.getPlayer())){
				return true;
			}
		}
		
		return false;
	
}

void newRetreater(Unit unit, int value){
	if(!this.flee.containsKey(unit)){
		this.flee.put(unit, value);
	}
	else {
		this.flee.put(unit, value);
	}
}

void removeFlee(Unit unit){
	if(flee.containsKey(unit)){
		flee.remove(unit);
	}
}

void targetChecking(){
	ArrayList<Unit> checked = new ArrayList<>();
	for(Unit unit : new ArrayList<>(this.units)){
	 ArrayList<Unit> enemy = myData.GetEnemyUnitsNearby(unit.getPosition(), 300, true);
		for(Unit enemies : enemy){
			if(game.enemies().contains(unit.getPlayer()) && ShouldBeFocused(enemies)){
				if(unit.isInWeaponRange(enemies)){
					unit.attack(enemies);
					break;
				}
			}	
		}
	}
}

public boolean ShouldBeFocused(Unit weeheads){
	
	if(weeheads.getType() == UnitType.Terran_Vulture_Spider_Mine){
		return true;
	}
	
	if(weeheads.getType() == UnitType.Zerg_Lurker){
		return true;
	}
	
	if(weeheads.getType() == UnitType.Terran_SCV && weeheads.isRepairing() == true){
		return true;
	}
	
	if(weeheads.getType() == UnitType.Terran_Medic){
		return true;
	}
	
	if(weeheads.getType() == UnitType.Protoss_High_Templar){
		return true;
	}
	
	if(weeheads.getType() == UnitType.Protoss_Carrier){
		return true;
	}
	

	return false;
	
}


}