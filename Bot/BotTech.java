package Bot;

import bwapi.Player;
import bwapi.TechType;
import bwapi.UnitType;
import bwapi.UpgradeType;

public class BotTech {
	// so how is this going to work?
	// fuck man i dunno. 
	// Hmm okay a giant blob of if statements with different IDS. ID 1 = army score of 300, ID 2 = 3 hydras. SO ON SO ON. 
	// SOUNDS GOOD
	// https://www.youtube.com/watch?v=OofVqpVHmgE
	
	// Es wird genug fur alle sein
	// wir trinken zusammen
	
	// type will be either a TechType, UpgradeType or unit type. 
	// type 1 = Tech
	// type 2 = Upgrade
	// type 3 = type
	
	
	// ***
		//RequirementIDs
	
		// 0: no requirement
		// 1: armyscore >= x
		// 2: x amount of hydras
		// 3: bio?
	
	
	
	
	/// ***
	
	int type = 0;
	Data data;
	TechType tech;
	UnitType morphType;
	UpgradeType upgrade;
	int RID;
	boolean ready;
	Player self;
	Player enemy;
	String name;
	
	public BotTech(int typee, int RIDD, TechType techh, UpgradeType up, UnitType morph, Data dataa){
		this.type = typee;
		this.RID = RIDD;
		this.data = dataa;
		this.self = this.data.self;
		this.enemy = this.data.game.enemy();
		if(type == 1){
			this.tech = techh;
			this.name = techh.toString();
		}
		
		if(type == 2){
			this.upgrade = up;
			this.name = up.toString();
		}
		
		if(type == 3){
			this.morphType = morph;
			this.name = morph.toString();
		}
		

	}
	
	
	boolean requirementsMet(int a){
		
		if(a == 0){
			return true;
		}
		
		if(a == 1){
			if(data.myScore >= 600){
				return true;
			}
			
		}
		
		if(a == 2){
			if(self.allUnitCount(UnitType.Zerg_Hydralisk) > 6){
			// IF NUMBER IS BIGGER THEN OTHER NUMBER STUFF HAPPEBS.
			// >HAPPEBS
				return true;
			}
		}
		
		if(a == 3){
			// SO YEAH WE COUNT HOW MANY BIO
			// AND COMPARE TO HIS WHOLE UNIT COUNT
			// STUFF HAPPENS
			int max = this.data.enemyMilUnits.size();
			int amount = this.enemy.allUnitCount(UnitType.Terran_Marine) + this.enemy.allUnitCount(UnitType.Terran_Firebat) + this.enemy.allUnitCount(UnitType.Terran_Marine);
			if(amount >= max * 0.75){
				//SUPRISE, IT'S A BIO BUILD
				return true;
			}
		}
		
		if(a == 4){
			if(self.getUpgradeLevel(UpgradeType.Grooved_Spines) > 0 && self.getUpgradeLevel(UpgradeType.Muscular_Augments) > 0){
				return true;
			}
		}
		
		if(a == 5){
			if(self.allUnitCount(UnitType.Zerg_Sunken_Colony) > 2){
				return true;
			}
		}
		
		
		
		return false;
		
	}
	
	void isReady(){
		
		if(requirementsMet(this.RID)){
			this.ready = true;
		}
		else {
			this.ready = false;
		}
		

	}

	

}
