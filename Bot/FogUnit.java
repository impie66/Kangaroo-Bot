package Bot;

import bwapi.Player;
import bwapi.Position;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;

public class FogUnit {
	Unit unit;
	int unitID;
	UnitType type;
	Position pos;
	Position targetPos;
	Player ply;
	boolean isVisible;
	int framesToTarget;
	int hp;
	int shields;
	int energy;
	int lastSeen;


	FogUnit(Unit unit, UnitType type, int ID){
		this.unit = unit;
		this.type = unit.getType();
		this.unitID = ID;
		this.pos = unit.getPosition();
		this.targetPos = unit.getOrderTargetPosition();
		this.ply = unit.getPlayer();
		this.isVisible = true;
		this.hp = unit.getHitPoints();
		this.shields = unit.getShields();
		this.energy  = unit.getEnergy();
		this.lastSeen = 0;
	}
	
	
	void update(Unit unit){
		if(unit.getID() == this.unitID){
			
			this.pos = unit.getPosition();
			this.hp = unit.getHitPoints();
			this.energy = unit.getEnergy();
			this.shields = unit.getShields();
			
			if(this.type != unit.getType()){
				this.type = unit.getType();
			}
		
		
		}
		

	} // end of update
	
	void simRegen(){
		// called EVERY 24 frames
		if(this.hp != type.maxHitPoints() && type.regeneratesHP()){
			this.hp = (int) Math.round(this.hp + 0.372);
		}
		
		if(this.shields != type.maxShields() && type.maxShields() != 0){
			this.shields = (int) Math.round((this.shields + 0.651));
		}
		
		if(this.energy != type.maxEnergy() && type.maxEnergy() != 0){
			this.energy = (int) Math.round((this.energy + 0.744));
		}
		
		
	}
	

	

	
}
