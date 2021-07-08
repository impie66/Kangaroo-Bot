package Bot;

import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class Builder {
Unit worker;
TilePosition where;
UnitType type;
int frameCheck = 0;
// ^^ CHeck if the building is done by this frame count, if not retry the building
boolean isExpander;
int frameToTryAgain;


public Builder(Unit workerr, UnitType typee, TilePosition pos){
	this.worker = workerr;
	this.where = pos;
	this.type = typee;
	isExpander = false;
	int frameToTryAgain = 0;
}

public Builder(Unit workerr, UnitType typee, TilePosition pos, int f){
	this.worker = workerr;
	this.where = pos;
	this.type = typee;
	isExpander = false;
	int frameToTryAgain = 0;
}


public Builder(Unit workerr, UnitType typee, TilePosition pos, int f, boolean exp){
	this.worker = workerr;
	this.where = pos;
	this.type = typee;
	isExpander = exp;
	int frameToTryAgain = 0;
}

	
}
