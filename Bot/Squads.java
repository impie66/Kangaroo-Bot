package Bot;
import bwapi.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
Util til;
UnitType filter;
// 1 == attacker, 2 == defender, 3 == harasser

//BRINGING BACK THE INTEGERS

public Squad(ArrayList<Unit> unitss, int idd, Data Data, Game gam, DecisionManager man, Util till){
	this.units = unitss;
	this.score = getSquadScore();
	this.target = null;
	this.State = 0;
	this.AWR = false;
	this.targetScore = 0;
	this.game = gam;
	if(game.self().getRace().equals(Race.Zerg)){
		this.retreatPos = game.self().getStartLocation().toPosition();
	}
	else {
		this.retreatPos = Data.myChokes.get(1).getCenter().toPosition();	
	}
	this.priority = 3;
	this.retreating = false;
	this.type = 0;
	this.squadName = "asdf";
	this.id = idd;
	this.myData = Data;
	this.manager = man;
	this.flee = new HashMap<>();
	this.til = till;
	this.filter = UnitType.AllUnits;
}

public Squad(ArrayList<Unit> unitss, int idd, Data Data, Game gam, DecisionManager man, Util till, UnitType f){
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
	this.til = till;
	this.filter = f;
	
}

public Squad(ArrayList<Unit> unitss, int idd, Data Data, Game gam, DecisionManager man, Position ret, Util till){
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
	this.til = till;
	this.filter = UnitType.AllUnits;
}

public Squad(ArrayList<Unit> unitss, int idd, int targets, Data Data, Game gam, DecisionManager man, Util till){
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
	this.til = till;
	this.filter = UnitType.AllUnits;
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
			for(Unit unit : new ArrayList<Unit>(this.units)){
				if(isInCombat(unit) && det.getPosition().getApproxDistance(unit.getPosition()) >= 100){
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
	
	if(!this.units.isEmpty()){
		
		for(Unit unit : new ArrayList<>(this.units)){	
			
			if(unit.getType().equals(UnitType.Zerg_Lurker) && unit.isBurrowed()){
				if(!EnemysNearby(unit.getPosition(), 200)){
					unit.unburrow();
				}
			}
			
				if(unit.getType().equals(UnitType.Zerg_Lurker) && !unit.isBurrowed()){
					if(EnemysNearby(unit.getPosition(), 200) && unit.canBurrow()){
						unit.burrow();
					}
				}
				
				if(unit.isUnderStorm()){
					if(!unit.isMoving()){
						unit.move(game.self().getStartLocation().toPosition());
					}
					else {
						if(unit.getOrderTargetPosition().getApproxDistance(game.self().getStartLocation().toPosition()) > 100){
							unit.move(game.self().getStartLocation().toPosition());
						}
					}
				}
				if(unit.isAttacking() && !isMelee(unit.getType()) == true && !unit.getType().equals(UnitType.Protoss_Dragoon)){
					double score = myData.getSimScore(unit);
					//System.out.println("Trigger");
					if(unit.getType().equals(UnitType.Protoss_Dragoon)){
						System.out.println("Cooldown: " + myData.weaponCoolingDown(unit));
						System.out.println("WPC: " + unit.getGroundWeaponCooldown());
					}
					if(unit.getOrderTarget() != null){
						int c = game.getFrameCount() + unit.getType().groundWeapon().damageCooldown();
						Unit target = unit.getOrderTarget();
						int weaponRange = til.realWeaponRange(unit.getType(), game.self());
						//System.out.println("Unit Target: " + target.getType().toString());
						
						if(til.shouldKiteAgainst(unit, target) && myData.weaponCoolingDown(unit) && !target.getType().isBuilding() && score < 0.85 && target.isAttacking()){
							// if we outrange
							Position pos = til.GetKitePos2(unit, target);
							if(pos != null){
							int cc = (int) unit.getPosition().getDistance(pos);
							if(cc >= unit.getType().groundWeapon().damageCooldown()){
								cc = unit.getType().groundWeapon().damageCooldown();
							}
							myData.DND(unit, cc);
							unit.move(pos);
							unit.attack(unit.getPosition(), true);
							}
							
						}
						
						if(!til.shouldKiteAgainst(unit, target) && myData.weaponCoolingDown(unit) && unit.getDistance(target) > (weaponRange / 2) && score >= 0.85){
							// if the target outranges us
							myData.DND(unit, c);
							unit.move(target.getPosition());
							unit.attack(target.getPosition(), true);
						}
					}
					
					// fin
				}
				
				if(unit.getType().equals(UnitType.Zerg_Mutalisk) && unit.isAttacking()){
					if(myData.weaponCoolingDown(unit)){
						Unit target = unit.getOrderTarget();
						Position pos = til.GetKitePos2(unit, target);
						if(pos != null){
						int cc = unit.getType().groundWeapon().damageCooldown();
						myData.DND(unit, cc);
						unit.move(pos);
						unit.attack(unit.getPosition(), true);
						//System.out.println("Retreat: " + pos);
						}
					}
				}
				
				//Onion rings..... yummy.... I'm hungry now
				if(unit.getKillCount() > 0){
					for(int i = 0; i<=unit.getKillCount(); i++){
						int bonus = unit.getType().width() + (unit.getKillCount() * 2) + (i * 3);
						if(i%2==0){
							game.drawCircleMap(unit.getPosition(), bonus, Color.Teal);
						}
						else {
							game.drawCircleMap(unit.getPosition(), bonus, Color.Red);
						}

					}
				}
				
//				if (unit.getHitPoints() != unit.getType().maxHitPoints() && til.canBeHealed(unit.getType())) {
//					ArrayList<Unit> myMedics = til.getAllOf(UnitType.Terran_Medic);
//					if(myMedics != null){
//						for (Unit medics : myMedics) {
//							if (medics.getType() == UnitType.Terran_Medic && medics.getEnergy() > 5 && medics.getOrder().equals(Order.MedicHeal) || medics.getOrder().equals(Order.MedicHealToIdle)) {
//								medics.useTech(TechType.Healing, unit);
//							}
//	
//						}
//					
//					}
//					
//					
//				}
				
				if (isInCombat(unit) == true && unit.getType() == UnitType.Terran_Marine && unit.isStimmed() == false && unit.getHitPoints() > 20) {
					unit.useTech(TechType.Stim_Packs);
				}				
			// end of units loop									
		}
		
		
		

		
	}
	
	
	
}

boolean isDefending(){
	return this.State == 2;
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
	if(pos != null){
	int ffinal = 0;
	int i = this.getUnitSize();
	int o = 0;
	for(Unit unit : new ArrayList<Unit>(this.units)){
		o = o + unit.getPosition().getApproxDistance(pos);
	}
	ffinal = o / i;
	return ffinal;
	}
	else {
		return 0;
	}
}

void Regroup(Position pos){
		if(this.units.isEmpty() == false){
			
			for(Unit unit : new ArrayList<>(this.units)){
				
				int dist = unit.getPosition().getApproxDistance(pos);
				
				if(!this.flee.containsKey(unit)){
					
					if(myData.getScoreOf(unit) < 0.70 && isInCombat(unit)){
						unit.move(pos);
					}
					else{
						if(!unit.isAttackFrame() && unit.getOrderTargetPosition().getApproxDistance(pos) > 100){
						// don't cancel attacks you fucking retard.
						unit.attack(pos);
						}
						
						if(unit.getType().groundWeapon().equals(WeaponType.None)){
							unit.move(pos);
						}
					}
					
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
	if(!this.units.isEmpty() && SquadsAverageDistTo(this.getUnits().get(0).getPosition()) >= 150 + (this.units.size() * 2) && !myData.enemyMilUnits.isEmpty()  && this.SquadsAverageDistTo(this.target) < 2000){
		return true;
	}
	else {
		return false;
	}
}

void squadMicro(){
	targetChecking();
	
	if(shouldRegroup() && this.State == 1 && !myData.currentTarget.Buildings.isEmpty() && !this.units.isEmpty()){
		Regroup(this.getUnits().get(0).getPosition());
	}

	if(this.State == 2 && !EnemysNearby(this.target)){
		this.State = 0;
		this.retreat();
		System.out.println("Squad: " + this.id + "To far from defence location");
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
			if(unit.getOrder() != Order.AttackMove && !isInCombat(unit) && !this.flee.containsKey(unit) && myData.canBeDistrubed(unit)){
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
	
	for(Unit unit : new ArrayList<>(this.units)){
		// https://www.youtube.com/watch?v=tgyJNXv8dGQ
		// TO COME TAKE ME AWAY
		// AND I
		// CAN FEEL MY HEART SKIP
		if(til.isSpellCaster(unit.getType())){
			Spellcaster cast = myData.getCaster(unit);
			if(cast != null){
				ArrayList<Unit> myUnits = til.getFriendlyUnitsNearMe(unit, 300, false);
				ArrayList<Unit> enemyUnits = til.getEnemyUnitsNearMe(unit, 300, false);
				if(enemyUnits == null){
					enemyUnits = new ArrayList<Unit>();
				}
				cast.combatLoop(myUnits, enemyUnits);
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
	if(unit.isUnderAttack() || unit.isStartingAttack()){
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
		//System.out.println("Is at Target i: " + i + " / " + max);
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
			System.out.println("Target is null, LUL");
		}
		
		for(Unit unit : new ArrayList<>(this.units)){
			
			
			if(!this.flee.containsKey(unit)){
				if(unit.isIdle() && this.target != null){
					unit.attack(this.target);
				}
								
				if(this.target != null){
					if(unit.getOrderTargetPosition().getApproxDistance(this.target) > 300 && !isInCombat(unit) && myData.canBeDistrubed(unit)){
					unit.attack(this.target);
					}
					
					if(unit.isIdle() && unit.getType().equals(UnitType.Terran_Medic) || unit.getType().equals(UnitType.Zerg_Lurker)){
						unit.move(target);
					}
				}
						
			}
		}
		
		
	}
	
}

void retreat(){
	for(Unit unit : new ArrayList<>(this.units)){
		if(unit.getPosition().getApproxDistance(this.retreatPos) > 100 && unit.getOrder() != Order.EnterTransport){
		unit.move(this.retreatPos);
			if(unit.isBurrowed()){
				unit.unburrow();
				unit.move(this.retreatPos);
			}
		}
		
		if(unit.isSieged()){
			unit.unsiege();
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
	ArrayList<Unit> units = new ArrayList<> (game.getUnitsInRadius(pos, 350));
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
		//System.out.println("Lurker Size: " + units.size());
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
	for(Unit unit : new ArrayList<>(this.units)){
		if(!unit.getType().groundWeapon().equals(WeaponType.None)){
		int radius = Math.round(unit.getType().groundWeapon().maxRange() + unit.getType().groundWeapon().maxRange() / 4);
		 //List<Unit> enemy = unit.getUnitsInRadius(radius);
		 ArrayList<Unit> enemy2 = til.getEnemyUnitsNearMe(unit, radius, false);
		 if(enemy2 != null){
			 for(Unit enemies : enemy2){
				if(til.ShouldBeFocused(enemies)){
					if(!unit.getOrder().equals(Order.AttackUnit)){
						 if(unit.canAttack(enemies)){
							unit.attack(enemies);
							game.drawCircleMap(enemies.getPosition(), 5, Color.Brown);
							game.drawLineMap(unit.getPosition(), enemies.getPosition(), Color.Brown);
							System.out.println("Unit: " + unit.getID() + " Targetting: " + enemies.getID());
							break;
							}
					}
					else {
						Unit target = unit.getOrderTarget();
						if(target != null){
							if(!til.ShouldBeFocused(target)){
								if(unit.canAttack(enemies)){
									unit.attack(enemies);
									game.drawCircleMap(enemies.getPosition(), 5, Color.Brown);
									game.drawLineMap(unit.getPosition(), enemies.getPosition(), Color.Brown);
									System.out.println("Unit: " + unit.getID() + " Targetting: " + enemies.getID());
									break;
								}
							}
						}
					}
				}	
			}
		 }	
		 
		}
		
	}
	
	// fin
}


boolean isMelee(UnitType type){
	
	if(type.equals(UnitType.Zerg_Zergling) || type.equals(UnitType.Zerg_Ultralisk) || type.equals(UnitType.Protoss_Zealot) || type.equals(UnitType.Terran_Firebat)  ){
		return true;
	}
	
	return false;
}


}