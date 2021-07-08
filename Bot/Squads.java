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
// 0 is not operating, 1 is operating 2 is defending.
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
double averageUnitScore;
HashMap<Unit, Integer> unitStrokes;
ArrayList<Kiter> kiters;
int SCM;
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
	this.kiters = new ArrayList<>();
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
	this.unitStrokes = new HashMap<Unit, Integer>();
	this.kiters = new ArrayList<>();
	this.SCM = 0;
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
	this.unitStrokes = new HashMap<Unit, Integer>();
	this.kiters = new ArrayList<>();
	this.SCM = 0;
	
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
	this.unitStrokes = new HashMap<Unit, Integer>();
	this.kiters = new ArrayList<>();
	this.SCM = 0;
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
	this.unitStrokes = new HashMap<Unit, Integer>();
	this.kiters = new ArrayList<>();
	this.SCM = 0;
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
		boolean has = false;
		ArrayList<Unit> yes = myData.GetEnemyUnitsNearby(det.getPosition(), 400, false);
		

		if(!yes.isEmpty()){
			loop:
			for(Unit u : yes){
				
				if(u.isDetected()){
					continue;
				}
				
				
				if(u.isCloaked() || u.isBurrowed()){
					pos = u.getPosition();
					has = true;
					break loop;
				}
			}
		}
			
		if(!has){
		
			for(Unit unit : new ArrayList<Unit>(this.units)){
				if(myData.isNearEnemyOrBetter(unit) && det.getPosition().getApproxDistance(unit.getPosition()) >= 100){
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
			

			
		}
		
		if(pos != null){
			det.move(pos);
		}
	}
	
	if(!this.units.isEmpty()){
		double i = 0;
		double ii = this.units.size();
		
		for(Unit unit : new ArrayList<>(this.units)){	
			
			i = i + myData.getSimScore(unit);
			
			if(!unit.exists()){
				this.units.remove(unit);
			}
			
			if(unit.getType().equals(UnitType.Zerg_Lurker) && unit.isBurrowed()){
				if(!EnemysNearby(unit.getPosition(), 270)){
					unit.unburrow();
				}
			}
			
			if(isAKiter(unit) && unit.getOrder() != Order.EnterTransport){
				Kite(unit);
				myData.DND(unit, 5);
			}
			
			// TODO investigate wierd hydra micro
			// maybe squad regrouping?
			
//				if(unit.getType().equals(UnitType.Zerg_Lurker) && !unit.isBurrowed()){
//					if(EnemysNearby(unit.getPosition(), 250) && unit.canBurrow()){
//						unit.burrow();
//					}
//				}
				
				if(unit.getType().equals(UnitType.Terran_Siege_Tank_Tank_Mode)){
					
					if(!unitStrokes.keySet().contains(unit)){
						unitStrokes.put(unit, 0);
					}
					
					if(myData.isNearEnemyOrBetter(unit) && myData.getSimScore(unit) > 0.60){
						unitStrokes.put(unit, this.unitStrokes.get(unit) + 1);
					}
					
				}
				
				if(unit.getType().equals(UnitType.Zerg_Lurker) && !unit.isBurrowed()){
					
					if(!unitStrokes.keySet().contains(unit)){
						unitStrokes.put(unit, 0);
					}
					
					if(myData.isNearEnemyOrBetter(unit) && myData.getSimScore(unit) > 0.60){
						unitStrokes.put(unit, this.unitStrokes.get(unit) + 1);
					}
					
				}
				
				
				if(unit.getType().equals(UnitType.Terran_Siege_Tank_Siege_Mode)){
					
					
					if(!unitStrokes.keySet().contains(unit)){
						unitStrokes.put(unit, 0);
					}
					
					if(!EnemysNearby(unit.getPosition(), 384)){
						unit.unsiege();
						unitStrokes.put(unit, 0);
						
					}
					
					
				}
				
				if(unit.getType().equals(UnitType.Zerg_Lurker)){
					
					if(!unitStrokes.keySet().contains(unit)){
						unitStrokes.put(unit, 0);
					}
					
			
				}
				
				// micro
				//push
				// pull
				// isMelee
				// && !unit.getType().equals(UnitType.Protoss_Dragoon)
				
				if(unit.isAttacking() && !isMelee(unit.getType()) == true && myData.canBeDistrubed(unit) && til.shouldMicro(unit.getType()) && !unit.isLoaded()){
					//System.out.println("Trigger");
					if(til.getUnitTarget(unit) != null){
						//int c = game.getFrameCount() + unit.getType().groundWeapon().damageCooldown();
						Unit target = til.getUnitTarget(unit);
						//int weaponRange = til.realWeaponRange(unit.getType(), game.self());
						//System.out.println("Unit Target: " + target.getType().toString());
						boolean test = false;
						if(til.shouldKiteAgainst(unit, target) || test){
							// if we outrange
							//rewrite
							if(!isAKiter(unit)){
							kiters.add(new Kiter(unit,target,myData,1));
							}
							//1 = Kite (Run while on weapon cooldown)
							//2 = Push (Approach the target while on weapon cooldown)
							
							
//							Position pos = til.GetJukePos(unit, target);
//							if(pos != null){
//							myData.DND(unit, c);
//							unit.move(pos);
//							unit.patrol(target.getPosition(), true);
//							}		
							
						}
						
						if(til.shouldPushAgainst(unit, target) && unit.getDistance(target) > 35 && !isMelee(target.getType())){
							// if the target outranges us
//							Position pos = til.GetJukePos(unit, target);
//							if(pos != null){
//							myData.DND(unit, c);
//							unit.move(pos);
//							}
							if(!isAKiter(unit)){
							kiters.add(new Kiter(unit,target,myData,2));
							}
							//1 = Kite (Run while on cooldown)
							//2 = Push (Approach the target)
						}
					}
				
					// fin
				}
				
				// TODO FIX GOON MICRO
				// really need to do
				
//				if(unit.getType().equals(UnitType.Zerg_Mutalisk) && unit.isAttacking() && myData.canBeDistrubed(unit)){
//					if(myData.weaponCoolingDown(unit)){
//						Unit target = unit.getOrderTarget();
//						if(til.shouldMutaKiteAgainst(unit, target)){
//							Position pos = til.GetKitePosMuta(unit, target);
//							if(pos != null){
//							int cc = game.getFrameCount() + unit.getType().groundWeapon().damageCooldown();
//							myData.DND(unit, cc);
//							unit.move(pos);
//							unit.patrol(unit.getPosition(), true);
//							}
//						}
//					}
//				}
				
				//Onion rings..... yummy.... I'm hungry now
//				if(unit.getKillCount() > 0){
//					for(int i = 0; i<=unit.getKillCount(); i++){
//						int bonus = unit.getType().width() + (unit.getKillCount() * 2) + (i * 3);
//						if(i%2==0){
//							game.drawCircleMap(unit.getPosition(), bonus, Color.Teal);
//						}
//						else {
//							game.drawCircleMap(unit.getPosition(), bonus, Color.Red);
//						}
//
//					}
//				}
				
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
		
		if(i != 0){
		this.averageUnitScore = ii / i;
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
			//til.Print(" " + this.id + " Regrouping");
			for(Unit unit : new ArrayList<>(this.units)){
							
				int dist = unit.getPosition().getApproxDistance(pos);
				// !this.flee.containsKey(unit) &&
				if(myData.canBeDistrubed(unit)){
					if(unit.isSieged() && !myData.isNearEnemyOrBetter(unit)){
						unit.unsiege();
					}
					
					if(myData.getSimScore(unit) > 0.55 && myData.isNearEnemyOrBetter(unit)){
						if(dist >= 200 && unit.getOrderTargetPosition().getApproxDistance(pos) >= 800){
							unit.attack(pos);
						}
						// win the local fight before retreating.
					}
					else {
						// if they can't win their fight.
						if(unit.getOrderTargetPosition().getApproxDistance(pos) > 500){
						unit.move(pos);
						}
					}
				
					if(unit.getType().groundWeapon().equals(WeaponType.None) && myData.getSimScore(unit) < 0.75){
						unit.move(pos);
					}
					
					if(unit.isIdle()){
						if(myData.getSimScore(unit) > 0.75 && unit.getDistance(pos) > 300){
						unit.attack(pos);
						}
						else {
							if(unit.getDistance(pos) > 300){
								unit.move(pos);
							}
						}
					}
							
					if(unit.isBurrowed() && unit.canUnburrow() && !myData.isNearEnemyOrBetter(unit)){
						unit.unburrow();
					}
					
					if(this.myData.isSpellCaster(unit) && unit.getDistance(pos) > 250 && !myData.IsMilitrayUnit(unit)){
						if(unit.isIdle() && unit.getDistance(pos) >= 400){
							unit.move(pos);
						}
						if(unit.getOrderTarget() != null){
							if(unit.getOrderTarget().getDistance(pos) > 600){
								unit.move(pos);
							}
						}
					}
				}
			}
			
		}
}

boolean shouldRegroup(){	
	//int bonus = game.getFrameCount() / 100;
	
	if(this.averageUnitScore >= 0.75){
		return false;
	}
	

	if(myData.currentTarget == null){
		return false;
	}
	
//	if(game.getFrameCount() >= 20000){
//		if(myData.pStats < 30){
//			return false;
//		}
//	}
	
	int max = 500;
	
	if(myData.pStats < 10){
		max = 200;
	}

	if(!this.units.isEmpty() && SquadsAverageDistTo(this.getUnits().get(0).getPosition()) >= max && 
	!myData.currentTarget.MilUnits.isEmpty() && 
	!EnemysNearby(this.getUnits().get(0).getPosition(), 500)){
		return true;
	}
	else {
		return false;
	}
}

void squadMicro(){
	targetChecking();
	// meleeSaving(); NOW IN ENEMY UNITS LOOP
	
	if(shouldRegroup() && this.State == 1 && !myData.currentTarget.Buildings.isEmpty() && !this.units.isEmpty()){
		Regroup(this.getUnits().get(0).getPosition());
	}

	if(this.State == 2 && !EnemysNearby(this.target, 600)){
		this.State = 0;
		this.retreat();
		//System.out.println("Squad: " + this.id + "No enemy units near defence location");
	}
	
	if(this.target == null && this.State == 1){
		if(myData.nextAttackPosition!=null){
			this.target = this.myData.nextAttackPosition;
		}
	}
	
	if(isAtTarget(true) && this.target != this.myData.nextAttackPosition && this.State == 1){
		this.target = this.myData.nextAttackPosition;
	}
	
	if(this.State == 1 && this.target!=null && !shouldRegroup()){
		// attacking
		for(Unit unit : new ArrayList<>(this.units)){
//			if(!isInCombat(unit)){
//				if(myData.canBeDistrubed(unit) && !unit.getOrder().equals(Order.AttackUnit)){
//					if(unit.getOrderTargetPosition().getApproxDistance(this.target) > 1200){
//						unit.attack(this.target);
//					}
//				}
//			}
			
			if(unit.isIdle() && til.IsMilitrayUnit(unit) && myData.canBeDistrubed(unit)){
				// https://www.youtube.com/watch?v=SBmz2DdSCPQ
				// WAH EWAH WAH WAH WAH WAH WAH WAH WAH WAH
				Position WAH = til.getPositionToFight(unit);
				if(WAH != null){
				unit.attack(WAH);
				}
				else {
				unit.attack(this.target);
				}
			}
			
//			if(unit.getType().equals(UnitType.Zerg_Lurker)){
//				if(unit.isIdle()){
//					unit.move(this.target);
//				}
//				else {
//					if(unit.getOrderTargetPosition().getApproxDistance(this.target) > 1000 && myData.canBeDistrubed(unit)){
//						unit.move(this.target);
//					}
//				}
//			}
			
			if(unit.getType().equals(UnitType.Terran_Ghost) && unit.isCloaked()){
				if(!myData.isNearEnemyOrBetter(unit)){
					if(unit.canDecloak()){
						unit.decloak();
					}
				}
			}
			
//			if(unit.getType().equals(UnitType.Zerg_Lurker) && !unit.isMoving() && !myData.isNearEnemyOrBetter(unit) && myData.canBeDistrubed(unit)){
//				unit.move(this.target);
//			}
			

			
			
		// end of units loop.	
		}
		
		if(game.getFrameCount() >= this.SCM){
			this.SCM = game.getFrameCount() + 48;
			for(Unit unit : new ArrayList<>(this.units)){
				
				if(!myData.canBeDistrubed(unit)){
					continue;
				}
				
				if(til.isBusy(unit)){
					continue;
				}
				
				if(unit.getOrder().equals(Order.Burrowing)){
					continue;
				}
							
				if(myData.isSpellCaster(unit) && !til.hasWeapons(unit.getType())){
					moveCaster(unit);
				}
				else {
					continue;
				}	
				
				if(unit.getType().equals(UnitType.Zerg_Lurker) && unit.getOrder() != Order.Burrowing && !unit.isBurrowed()){
					Position WAH = til.getPositionToFight(unit);
					if(WAH != null){
						unit.move(WAH);
					}
				}
			}
			
		}
		
	}
	// 
	
	
	if(this.State == 2 && target!=null){
		// defending
		for(Unit unit : new ArrayList<>(this.units)){
			if(unit.isIdle() && unit.getPosition().getApproxDistance(this.target) > 1000){
				unit.attack(this.target);
			}
			else {
				if(myData.canBeDistrubed(unit) && !myData.isNearEnemyOrBetter(unit)){
					if(unit.getOrderTargetPosition().getApproxDistance(this.target) > 1400){
						unit.attack(this.target);
					}
				}
			}
		}
	}
	
	for(Unit unit : new ArrayList<>(this.units)){
		// https://www.youtube.com/watch?v=tgyJNXv8dGQ
		// AND I
		// CAN FEEL MY HEART SKIP
		if(til.isSpellCaster(unit.getType()) && myData.canBeDistrubed(unit) && myData.isNearEnemyOrBetter(unit)){
			Spellcaster cast = myData.getCaster(unit);
			if(cast != null){
				
				if(unit.getType().equals(UnitType.Terran_Dropship) || unit.getType().equals(UnitType.Protoss_Shuttle) ){
					continue;
				}
				
				if(unit.getType().equals(UnitType.Terran_Siege_Tank_Tank_Mode)){
					
					if(!unitStrokes.keySet().contains(unit)){
						unitStrokes.put(unit, 0);
					}
					
					
					if(this.unitStrokes.get(unit) <= 90){
						// don't siege until we have the full picture
						continue;
					}
					else {
						this.unitStrokes.put(unit, 0);
					}
					
				}
				
				if(unit.getType().equals(UnitType.Zerg_Lurker)){
					
					if(!unitStrokes.keySet().contains(unit)){
						unitStrokes.put(unit, 0);
					}
												
					if(this.unitStrokes.get(unit) <= 90){
						// don't burrow until we have the full picture
						continue;
					}
					else {
						this.unitStrokes.put(unit, 0);
					}
					
				}
				
				int range = 0;
				// range = unit.getType().sightRange();
				range = unit.getType().sightRange() + (unit.getType().sightRange() / 2);
				
				if(unit.getType().equals(UnitType.Terran_Siege_Tank_Tank_Mode) || unit.isSieged()){
					range = unit.getType().sightRange() + (unit.getType().sightRange() / 2);
				}
				
				ArrayList<Unit> myUnits = til.getFriendlyUnitsNearMe(unit, range, false);
				ArrayList<Unit> enemyUnits = til.getEnemyUnitsNearMe(unit, range, false);
				if(enemyUnits == null){
					enemyUnits = new ArrayList<Unit>();
				}
				
				if(enemyUnits != null && myUnits != null){
					cast.combatLoop(myUnits, enemyUnits);
				}
				
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
	if(unit.isUnderAttack() || unit.isStartingAttack() || unit.getOrder().equals(Order.AttackUnit)){
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
			til.Print("Target is null, LOL");
		}
		
		for(Unit unit : new ArrayList<>(this.units)){
			
			
			if(unit.getOrder().equals(Order.ArchonWarp) || unit.getOrder().equals(Order.DarkArchonMeld)){
				continue;
			}
			
//			if(unit.isLoaded() && !myData.isNearEnemyOrBetter(unit)){
//				Unit mcravesucks = myData.getTransport(unit);
//				if(mcravesucks != null){
//					mcravesucks.unload(unit);
//				}
//			}

			if(myData.canBeDistrubed(unit)){
				// ^^ called inside the loop
						
				if(this.target != null){
					
					if(unit.isIdle() && this.target != null && til.hasWeapons(unit.getType())){
						Position fite = til.getPositionToFight(unit);
						if(fite != null){
							unit.attack(fite);
						}
						else {
							unit.attack(this.target);
						}
					}
					
//					if(unit.getType().equals(UnitType.Zerg_Lurker)){
//						if(unit.isIdle()){
//							unit.move(this.target);
//						}
//						else {
//							if(unit.getOrderTargetPosition().getApproxDistance(this.target) > 2000 && myData.canBeDistrubed(unit) && myData.isNearEnemyOrBetter(unit)){
//								unit.move(this.target);
//							}
//						}
//					}
					
					if(unit.isPatrolling() && !myData.isInCombat(unit)){
						unit.attack(this.target);
					}
					
					if(unit.equals(UnitType.Protoss_Carrier) && this.target != null && unit.isIdle()){
						if(unit.getOrderTargetPosition().getApproxDistance(this.target) > 2000){
							if(!myData.isNearEnemyOrBetter(unit)){
								if(til.hasWeapons(unit.getType())){
									unit.attack(this.target);
								}
								else {
									unit.move(this.target);
								}
							}
						}
					}
					
					if(unit.getOrderTargetPosition().getApproxDistance(this.target) > 2000 && til.hasWeapons(unit.getType()) ){
						if(!myData.isNearEnemyOrBetter(unit)){
						unit.attack(this.target);		
						}
					}
								
					if(unit.isIdle() && myData.isSpellCaster(unit)){
						moveCaster(unit);
					}
					
					if(unit.getType().equals(UnitType.Zerg_Lurker) && unit.isIdle()){
						moveCaster(unit);
					}
					
					
				} // end of this.target null check
				
				
						
			} // end of disturbed
			
			
		} // end of unit loop
		
		
	}
	
}

void retreat(){
	for(Unit unit : new ArrayList<>(this.units)){
		
		if(unit.getOrder().equals(Order.EnterTransport)){
			continue;
		}
		
		if(isAKiter(unit)){
			continue;
		}
		
		if(myData.isNearEnemyOrBetter(unit) && myData.getSimScore(unit) > 0.65 && unit.getOrder() != Order.EnterTransport){
			unit.attack(this.retreatPos);
		}
		else{	
			if(unit.getPosition().getApproxDistance(this.retreatPos) > 200 && unit.getOrder() != Order.EnterTransport){
				if(isInCombat(unit)){
					til.reteatUnit(unit);
					continue;
				}
				else {
					unit.move(this.retreatPos);
					if(unit.isBurrowed()){
						unit.unburrow();
						continue;
					}
					
					if(unit.isSieged()){
						unit.unsiege();
						continue;
					}
				}
				
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
	ArrayList<Unit> units = new ArrayList<> (game.getUnitsInRadius(pos, 300));
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
		if(!unit.getType().groundWeapon().equals(WeaponType.None) && myData.canBeDistrubed(unit) && !this.flee.containsKey(unit) ){
		int radius = Math.round(unit.getType().groundWeapon().maxRange() + unit.getType().groundWeapon().maxRange() / 4);
		 //List<Unit> enemy = unit.getUnitsInRadius(radius);
		 ArrayList<Unit> enemy2 = til.getEnemyUnitsNearMe(unit, radius, false);
		 if(enemy2 != null){
			 for(Unit enemies : enemy2){
				if(til.ShouldBeFocused(enemies)){
					if(!unit.getOrder().equals(Order.AttackUnit)){
						 if(unit.canAttack(enemies) && myData.canBeDistrubed(unit)){
							unit.attack(enemies);
							game.drawCircleMap(enemies.getPosition(), 5, Color.Brown);
							game.drawLineMap(unit.getPosition(), enemies.getPosition(), Color.Brown);
							//System.out.println("Unit: " + unit.getID() + " Targetting: " + enemies.getID());
							break;
							}
					}
					else {
						Unit target = unit.getOrderTarget();
						if(target != null){
							if(!til.ShouldBeFocused(target)){
								if(unit.canAttack(enemies) && myData.canBeDistrubed(unit)){
									unit.attack(enemies);
									game.drawCircleMap(enemies.getPosition(), 5, Color.Brown);
									game.drawLineMap(unit.getPosition(), enemies.getPosition(), Color.Brown);
									//System.out.println("Unit: " + unit.getID() + " Targetting: " + enemies.getID());
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
	
	if(type.equals(UnitType.Zerg_Zergling) || 
		type.equals(UnitType.Zerg_Ultralisk) || 
		type.equals(UnitType.Protoss_Zealot) || 
		type.equals(UnitType.Terran_Firebat) || 
		type.equals(UnitType.Protoss_Dark_Templar) ||
		type.equals(UnitType.Zerg_Broodling) ||
		type.equals(UnitType.Terran_Vulture_Spider_Mine)){
		return true;
	}
	
	return false;
}

boolean isAttacking2(Unit unit){
	 return unit.getOrder().equals(Order.AttackUnit) || unit.getOrder().equals(Order.AttackMove);
	 
}

boolean isAKiter(Unit u){
	for(Kiter kiters : new ArrayList<>(this.kiters)){
		if(kiters.me == u){
			return true;
		}
	}
	
	return false;
}

Kiter getKiterUnit(Unit u){
	for(Kiter kiters : new ArrayList<>(this.kiters)){
		if(kiters.me == u){
			return kiters;
		}
	}
	
	return null;
}

void Kite(Unit self){
	Kiter k = getKiterUnit(self);
	if(k != null){
		// is a kiter?
		k.tpos = k.target.getPosition();
		
		if(!myData.weaponCoolingDown(self)){
			if(!self.isAttackFrame()){
				self.attack(k.tpos);
			}
			this.kiters.remove(k);
			return;
		}
		
		
		if(k.target.getPosition() != Position.Unknown){
			// if visible
			Unit target = k.target;
			if(myData.weaponCoolingDown(self)){ // this breaks goons?
				// weapon cooling down
				if(k.state == 1){
					// kite
					til.retreatFrom(self, target);
					game.drawLineMap(self.getPosition(), target.getPosition(), Color.Green);
				}
				else {
					// push
					if(self.getDistance(target) > 20){
						Position pull = til.GetPushPos(self, target);
						if(pull != null && self.canMove() && pull.isValid(game)){
							self.move(pull);
							game.drawLineMap(self.getPosition(), pull, Color.Green);
						}
					}
				}
			}
			else {
				// weapon ready to be fired
				if(!self.isAttackFrame()){
					self.attack(k.tpos);
				}
				this.kiters.remove(k);
				return;
			}
		}
		
		this.kiters.remove(k);
			
		}
		
	}


	void moveCaster(Unit unit){
		
		if(unit == null){
			return;
		}
		
		if(!unit.exists()){
			return;
		}
		
		if(!unit.getType().equals(UnitType.Zerg_Lurker)){
			if(til.hasWeapons(unit.getType())){
				return;
			}
		}
		else {
			//if lurker
			if(unit.getOrder().equals(Order.Burrowing) || unit.getOrder().equals(Order.Unburrowing)){
				return;
			}
			
			if(myData.isInCombat(unit)){
				
			}
		}
		
		
		if(this.units.size() == 1){
			Position fite = til.getPositionToFight(unit);
			if(fite != null){
				unit.move(fite);
				return;
			}
			else {
				unit.move(this.target);
				return;
			}
		}
		
		
		List<Unit> loops = myData.GetMyUnitsNearby(unit.getPosition(), 600, true);
		
		for(Unit yes : loops ){
			if(yes == unit){
				continue;
			}
			
			if(myData.isNearEnemyOrBetter(yes) && myData.getSimScore(yes) >= 0.35){
				if(unit.getType().equals(UnitType.Terran_Medic)){
					if(!yes.getType().isOrganic()){
						continue;
					}
					else {
						til.Print("Found unit in combat!");
						unit.move(yes.getPosition());
						return;
					}
				}
				else {
					til.Print("Found unit in combat!");
					unit.move(yes.getPosition());
					return;
				}
				

			}
		}
		
		if(myData.isNearEnemyOrBetter(unit)){
			for(Unit u : loops){
				
				if(unit.getType().equals(UnitType.Terran_Medic)){
					if(!u.getType().isOrganic()){
						continue;
					}
				}
				
				if(myData.IsMilitrayUnit(u) && unit.getDistance(u) > 50){
					unit.move(u.getPosition());
					return;
				}
				
			}
		}
		else {
			// not near badguys
						
			if(unit.getPosition().getApproxDistance(this.target) > 2000){
				unit.move(this.target);
			}
			else {
				// if nearby
				for(Unit loop : loops){
					if(myData.isInCombat(loop)){
						unit.move(loop.getPosition());
						return;
						
					}
				}
				
				Unit last = til.getClosestUnitFromPoint(new ArrayList<>(this.units), this.target);
				if(last != null){
					unit.move(last.getPosition());
				}
				else{
					unit.move(this.target);
					return;
				}
			}
			
		}
		
		
		
		// not combat units found, maybe find closest ally unit and move there to assist.
		
		
		
		
//		ArrayList<Unit> loop = this.units;
//		
//		for(Unit a : myData.GetMyUnitsNearby(unit.getPosition(), 600, false)){
//			if(myData.isNearEnemyOrBetter(a) && !loop.contains(a)){
//				loop.add(a);
//			}
//		}
//		
//		if(loop.contains(unit)){
//			loop.remove(unit);
//		}
//		
//		int av = this.SquadsAverageDistTo(this.target);
//		
//		if(av >= 2000){
//			Position fight = til.getPositionToFight(unit);
//			if(fight != null){
//				unit.move(fight);
//				return;
//			}
//			else {
//				unit.move(this.target);
//				return;
//			}
//			
//		}
//		else {
//			Unit move = til.getClosestEnemyArmyUnitFromArray(this.target, loop);
//			
//			if(move != null){
//				til.Print("Found Unit: " + move.getType().toString() + " sdfsf: " + unit.getPosition().getApproxDistance(move.getPosition()));
//				unit.move(move.getPosition());	
//			}
//			else {
//				unit.move(this.target);
//			}
//
//		}
		
		
		
		
	}
	
//	void meleeSaving(){
//		// check to see if all squads units are in danger of dieing to melee
//		// if so retreat the unit and possibly save it.
//		for(Unit yes : new ArrayList<>(this.units)){
//			
//			if(!myData.isNearEnemyOrBetter(yes)){
//				continue;
//			}
//			
//	
//			ArrayList<Unit> around = myData.GetEnemyUnitsNearby(yes.getPosition(), 50, false);
//			
//			if(around != null){
//				enemyLoop:
//				for(Unit e : around){
//					
//					if(!til.hasWeapons(e.getType())){
//						continue;
//					}
//					
//					if(!e.canAttack(yes)){
//						continue;
//					}
//
//					if(e.getTarget() != null){
//						if(e.getTarget() == yes){
//							// if targetting me
//							int dmg = game.getDamageTo(yes.getType(), e.getType(), yes.getPlayer(), e.getPlayer());
//							if(dmg != 0){
//								if(yes.getHitPoints() <= dmg){
//									// RUN AWAY
//									til.retreatFrom(yes, e);
//									myData.DND(yes, 15);
//									til.Print("Unit: " + yes.getType().toString() + " Fleeing from: " + e.getType().toString() + " Due to OHS reasons");
//								}
//							}
//						}
//					}
//					else {
//						continue enemyLoop;
//					}
//				}
//			}
//			else {
//				continue;
//			}
//			
//			
//		}
//		
//		
//	}
//	
	


}


