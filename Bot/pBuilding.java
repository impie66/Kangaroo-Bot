package Bot;

import bwapi.TilePosition;
import bwapi.UnitType;

public class pBuilding {
	UnitType type;
	TilePosition pos;
	int maxRange;
	boolean newBaseonBuild;
	
	
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
	
	
	public pBuilding(UnitType ype, TilePosition where, int max, boolean base){
		this.type = ype;
		this.pos = where;
		this.maxRange = max;
		this.newBaseonBuild = base;
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


