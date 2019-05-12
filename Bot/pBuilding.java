package Bot;

import bwapi.TilePosition;
import bwapi.UnitType;

public class pBuilding {
	UnitType type;
	TilePosition pos;
	int maxRange;
	boolean waitForCreep;
	boolean isExpand;
	
	public pBuilding(UnitType ype, TilePosition where){
		this.type = ype;
		this.pos = where;
		this.maxRange = 200;
		this.waitForCreep = false;
		this.isExpand = false;
	}
	
	public pBuilding(UnitType ype, TilePosition where, int max){
		this.type = ype;
		this.pos = where;
		this.maxRange = max;
		this.waitForCreep = false;
		this.isExpand = false;
	}
	
	public pBuilding(UnitType ype, TilePosition where, int max, boolean creep){
		this.type = ype;
		this.pos = where;
		this.maxRange = max;
		this.waitForCreep = creep;
		this.isExpand = false;
	}
	
	
	public pBuilding(UnitType ype, TilePosition where, boolean expand){
		this.type = ype;
		this.pos = where;
		this.isExpand = expand;
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


