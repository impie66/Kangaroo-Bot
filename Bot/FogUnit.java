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


	FogUnit(Unit unit, UnitType type, int ID){
		this.unit = unit;
		this.type = unit.getType();
		this.unitID = ID;
		this.pos = unit.getPosition();
		this.targetPos = unit.getOrderTargetPosition();
		this.ply = unit.getPlayer();
		this.isVisible = true;
	}
	
	
	void update(){
		Unit unit = this.unit;
		this.pos = unit.getPosition();

		
		if(this.type != unit.getType()){
			this.type = unit.getType();
		}
		
		if(!unit.isIdle()){
			this.targetPos = unit.getOrderTargetPosition();
			this.framesToTarget = (int) Math.round(unit.getPosition().getApproxDistance(targetPos) / unit.getType().topSpeed());
		}
		
		
		
		
	}
	

	

	
}
