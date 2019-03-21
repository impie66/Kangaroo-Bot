package Bot;
import bwapi.*;

import java.util.ArrayList;

import com.sun.jna.platform.win32.Sspi.SecPkgContext_Lifespan;

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
// 1 == attacker, 2 == defender, 3 == harasser


public Squad(ArrayList<Unit> unitss, int idd, Data Data, Game game){
	this.units = unitss;
	this.score = getSquadScore();
	this.target = null;
	this.State = 0;
	this.AWR = false;
	this.targetScore = 0;
	this.retreatPos = game.self().getStartLocation().toPosition();
	this.priority = 3;
	this.retreating = false;
	this.type = 0;
	this.squadName = "asdf";
	this.id = idd;
	myData = Data;
}

public Squad(ArrayList<Unit> unitss, int idd, int targets, Data Data, Game game){
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
	myData = Data;
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
return units.size();
}

int priority(){
	return this.priority;
}

int getSquadScore(){
	int i = 0;
	if(this.units.isEmpty()){
		return i;
	}
	
	for(Unit unit : this.units){
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
	for(Unit unit : this.getUnits()){
		o = o + unit.getPosition().getApproxDistance(pos);
		
	}
	ffinal = o / i;
	return ffinal;
}

void Regroup(Position pos){
		if(this.units.isEmpty() == false){
			for(Unit unit : units){
				if(unit.isIdle()){
					unit.move(pos);
				}
				if(unit.getOrderTargetPosition()!=pos){
				unit.move(pos);
				}
			}
		}
}

boolean shouldRegroup(){
	if(SquadsAverageDistTo(this.getUnits().get(0).getPosition())>=400){
		return true;
	}
	else {
		return false;
	}
}

void squadMicro(){
	
	if(this.target == null){
		if(!myData.nextAttackPosition.equals(null)){
			this.target = myData.nextAttackPosition;
		}
	}
	
	if(shouldRegroup() && this.State == 1){
		Regroup(this.getUnits().get(0).getPosition());
	}
	
	if(shouldRegroup() && this.State == 2){
		Regroup(this.getUnits().get(0).getPosition());
	}
	
	if(this.State == 1 && target!=null){
		// attacking
		for(Unit unit : new ArrayList<>(this.units)){
			if(unit.getOrder() != Order.AttackMove && !isInCombat(unit)){
				unit.attack(this.target);
			}
		}
	}
	
	if(this.State == 2 && target!=null){
		// defending
		for(Unit unit : new ArrayList<>(this.units)){
			if(!IsAttackMoving(unit) && !isInCombat(unit)){
				unit.attack(this.target);
			}
		}
	}
	
	
	if(isAtTarget() == true){
		if(myData.nextAttackPosition!=null){
			this.target = myData.nextAttackPosition;
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


boolean isAtTarget(){
	int i = 0;
	int max = 0;
	
	if(this.target == null){
		return false;
	}
	
	if(this.units.isEmpty() == false){
		max = (int) ((this.units.size()) - (this.units.size() * 0.45));
		for(Unit unit : this.units){
			if(unit.getPosition().getApproxDistance(this.target) < 200){
				i++;
			}
		}
		
		if(i>=max){
			return true;
		}
	}
	
	return false;
}

void operate(){
	
	if(this.target==null){
		this.target = myData.nextAttackPosition;
	}
	
	for(Unit unit : this.units){
		if(unit.isIdle()){
			unit.attack(target);
		}
		
		if(unit.getOrderTargetPosition().getApproxDistance(target) > 300 && !isInCombat(unit)){
		unit.attack(target);
		}
	}
}

void retreat(){
	for(Unit unit : this.units){
		if(unit.getPosition().getApproxDistance(this.retreatPos) > 200){
		unit.move(this.retreatPos);
		}
	}
}

void unitDeath(Unit unit){
	this.units.remove(unit);
}

boolean isSquadFull(){
	if(this.units.size() > 35){
		return true;
	}
	return false;
}

}