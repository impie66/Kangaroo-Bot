package Bot;

import bwapi.*;
import Bot.*;

public class BuildingQueue{
UnitType type;
TilePosition pos;
int id;
boolean checkIfBuilt;


public BuildingQueue(int idd, UnitType btype, TilePosition location){
	this.type = btype;
	this.pos = location;
	this.id = idd; 
	this.checkIfBuilt = false;
}

public BuildingQueue(int idd, UnitType btype){
	this.type = btype;
	this.pos = null;
	this.id = idd; 
	this.checkIfBuilt = false;
}

public BuildingQueue(int idd, UnitType btype, TilePosition location, boolean iff){
	this.type = btype;
	this.pos = location;
	this.id = idd; 
	this.checkIfBuilt = iff;
}

public BuildingQueue(int idd, UnitType btype, boolean iff){
	this.type = btype;
	this.pos = null;
	this.id = idd; 
	this.checkIfBuilt = iff;
}

UnitType getType(){
	return this.type;
}

TilePosition getPos(){
	return pos;
}

boolean checkIfBuilt(){
	return this.checkIfBuilt;
}
}