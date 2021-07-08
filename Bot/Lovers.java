package Bot;

import bwapi.TechType;
import bwapi.Unit;

public class Lovers {
	Unit father;
	Unit mother;
	boolean doWhenSafe;
	boolean deth;
	Data myData;
	

	
	Lovers(Unit f, Unit m, boolean bool, Data data){
		this.father = f;
		this.mother = m;
		this.doWhenSafe = bool;
		this.deth = false;
		this.myData = data;
	}
	
	
	void checkBreaking(){
		if(!this.father.exists()){
			this.deth = true;
		}
		
		if(!this.mother.exists()){
			this.deth = true;
		}
		

	}
	
	
	void DoTheThingsThatMakeTheThingThatMyThingyTellsMeToMake(){
		if(this.doWhenSafe){
			// do only when safe
			if(this.myData.isNearEnemyOrBetter(this.father) || this.myData.isNearEnemyOrBetter(this.mother) ) {
				return;
			}
		}

		
		this.father.useTech(TechType.Archon_Warp, this.mother);
		this.mother.useTech(TechType.Archon_Warp, this.father);
		
		myData.DND(this.father, 30);
		myData.DND(this.mother, 30);
		
	}
	
}
