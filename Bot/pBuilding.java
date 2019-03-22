package Bot;

import bwapi.TilePosition;
import bwapi.UnitType;

public class pBuilding {
	UnitType type;
	TilePosition pos;
	int maxRange;
	boolean waitForCreep;
	
	
	public pBuilding(UnitType ype, TilePosition where){
		this.type = ype;
		this.pos = where;
		this.maxRange = 200;
	}
	
	public pBuilding(UnitType ype, TilePosition where, int max){
		this.type = ype;
		this.pos = where;
		this.maxRange = max;
	}
	
	
	public pBuilding(UnitType ype, TilePosition where, int max, boolean creep){
		this.type = ype;
		this.pos = where;
		this.maxRange = max;
		this.waitForCreep = creep;
	}
	
	UnitType getType(){
		return this.type;
	}
	
	TilePosition getTilePosition(){
		return this.pos;
	}
	
	int getMaxRange(){
		return this.maxRange;
	}
	
}


