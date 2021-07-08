package Bot;

import bwapi.TechType;
import bwapi.Unit;


public class Intent {
	//https://www.youtube.com/watch?v=VYOjWnS4cMY
	Unit caster; 
	Unit target;
	TechType what;
	int removeframe;
	
	Intent(Unit cast, Unit t, TechType w) {
		this.caster = cast;
		this.target = t;
		this.what = w;
		this.removeframe = 0;
	}
	
	
	boolean isCasting(){
		if(this.caster.isMoving() || this.caster.isAttacking() || this.caster.isIdle() || this.caster.isPatrolling()){
			return false;
		}
		else {
			return true;
		}
	}
	
	boolean targetEffected(){
		if(!this.target.exists()){
			return true; // returning true would remove the intent
		}
		
		if(this.what.equals(TechType.Lockdown)){
			return this.target.isLockedDown();
		}
		
		if(this.what.equals(TechType.Irradiate)){
			return this.target.isIrradiated();
		}
		
		
		return false;
	
	}
	


}
