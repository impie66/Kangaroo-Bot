package Bot;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class Builder {
Unit worker;
TilePosition where;
UnitType type;


public Builder(Unit workerr, UnitType typee, TilePosition pos){
	this.worker = workerr;
	this.where = pos;
	this.type = typee;
}


	
}
