package Bot;

import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;

public class FogUnit {
	Unit unit;
	UnitType type;
	double hp;
	double shields;
	double energy;
	int iHP;
	int iShields;
	int iEnergy;
	int ID;
	
	
	FogUnit(Unit unit, UnitType type, int h, int s, int e, int ID){
		this.unit = unit;
		this.hp = h;
		this.shields = s;
		this.energy = e;
		this.ID = ID;
	
	}
	
	
	void update(){
		
		if(type.regeneratesHP() && this.hp <= type.maxHitPoints()){
			this.hp = this.hp + 0.372;
			if(this.hp >= type.maxHitPoints()){
				this.hp = type.maxHitPoints();
			}
		
		}
		
		if(type.getRace().equals(Race.Protoss) && type.maxShields() > 0 && this.shields <= type.maxShields()){
			this.shields = this.shields + 0.651;
			if(this.shields >= type.maxShields()){
				this.shields = type.maxShields();
			}	
		}
		
		if(type.maxEnergy() > 0 && this.energy != type.maxEnergy()){
			this.energy = this.energy + 0.744;
			if(this.energy >= type.maxShields()){
				this.energy = type.maxShields();
			}	
		}
		
		this.iHP = (int) Math.round(this.hp);
		this.iShields = (int) Math.round(this.shields);
		this.iEnergy = (int) Math.round(this.energy);
		System.out.println(" " + iHP + " " + iShields + " " + iEnergy);
		
	}
	
	void update(Unit unit){
		if(this.type != unit.getType() && this.ID == unit.getID()){
			this.type = unit.getType();
		}
		
		update();
	}
	
	
	
}
