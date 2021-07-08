package Bot;

import bwapi.Race;
import bwapi.TilePosition;
import bwapi.UnitType;

public class pBuilding {
	UnitType type;
	TilePosition pos;
	int maxRange;
	boolean waitForCreep;
	boolean isExpand;
	int requirement;
	int save;
	int frameCheck = 0;
	boolean canBeCancelled = false;
	boolean rangeIncreaseOnFail = true;
	boolean canSkip;
	Race buildOnlyForRace;
	boolean proxy = false;
	boolean buildWithScout = false;
	int tries = 0;
	
	public pBuilding(UnitType ype, TilePosition where){
		this.type = ype;
		this.pos = where;
		this.maxRange = 200;
		this.waitForCreep = false;
		this.isExpand = false;
		this.save = 0;
		this.frameCheck = 0;
		this.canSkip = false;
		this.buildOnlyForRace = Race.None;
		this.proxy = false;
		this.buildWithScout = false;
	}
	
	public pBuilding(UnitType ype, TilePosition where, Race r){
		this.type = ype;
		this.pos = where;
		this.maxRange = 200;
		this.waitForCreep = false;
		this.isExpand = false;
		this.save = 0;
		this.frameCheck = 0;
		this.canSkip = false;
		this.buildOnlyForRace = r;
		this.proxy = false;
		this.buildWithScout = false;
	}
	
	public pBuilding(UnitType ype, TilePosition where, int max){
		this.type = ype;
		this.pos = where;
		this.maxRange = max;
		this.waitForCreep = false;
		this.isExpand = false;
		this.save = 0;
		this.frameCheck = 0;
		this.canSkip = false;
		this.buildOnlyForRace = Race.None;
		this.proxy = false;
		this.buildWithScout = false;
	}
	
	public pBuilding(UnitType ype, TilePosition where, int max, boolean creep){
		this.type = ype;
		this.pos = where;
		this.maxRange = max;
		this.waitForCreep = creep;
		this.isExpand = false;
		this.save = 0;
		this.frameCheck = 0;
		this.canSkip = false;
		this.buildOnlyForRace = Race.None;
		this.proxy = false;
		this.buildWithScout = false;
	}
	
	
	public pBuilding(UnitType ype, TilePosition where, boolean expand){
		this.type = ype;
		this.pos = where;
		this.isExpand = expand;
		this.save = 0;
		this.maxRange = 10;
		this.frameCheck = 0;
		this.canSkip = false;
		this.buildOnlyForRace = Race.None;
		this.proxy = false;
		this.buildWithScout = false;
	}
	
	
	public pBuilding(UnitType ype, TilePosition where, boolean expand, boolean canBeCanceled){
		this.type = ype;
		this.pos = where;
		this.isExpand = expand;
		this.save = 0;
		this.maxRange = 10;
		this.frameCheck = 0;
		this.canBeCancelled = true;
		this.canSkip = false;
		this.buildOnlyForRace = Race.None;
		this.proxy = false;
		this.buildWithScout = false;
	}
	
	
	public pBuilding(UnitType ype, TilePosition where, int max, int sav){
		this.type = ype;
		this.pos = where;
		this.maxRange = 200;
		this.waitForCreep = false;
		this.isExpand = false;
		this.save = sav;
		this.frameCheck = 0;
		this.canSkip = false;
		this.buildOnlyForRace = Race.None;
		this.proxy = false;
		this.buildWithScout = false;
	}
	
	public pBuilding(UnitType ype, TilePosition where, int max, boolean creep, boolean yes, int save){
		this.type = ype;
		this.pos = where;
		this.maxRange = max;
		this.waitForCreep = creep;
		this.isExpand = yes;
		this.save = 1;
		this.frameCheck = 0;
		this.canSkip = false;
		this.buildOnlyForRace = Race.None;
		this.proxy = false;
		this.buildWithScout = false;
	}
	
	public pBuilding(UnitType ype, TilePosition where, int max, boolean creep, boolean yes, int save, int frame){
		this.type = ype;
		this.pos = where;
		this.maxRange = max;
		this.waitForCreep = creep;
		this.isExpand = yes;
		this.save = 1;
		this.frameCheck = frame;
		this.canSkip = false;
		this.buildOnlyForRace = Race.None;
		this.proxy = false;
		this.buildWithScout = false;
	}
	
	public pBuilding(UnitType ype, TilePosition where, int max, boolean creep, boolean yes, int save, int frame, boolean c){
		this.type = ype;
		this.pos = where;
		this.maxRange = max;
		this.waitForCreep = creep;
		this.isExpand = yes;
		this.save = 1;
		this.frameCheck = frame;
		this.canBeCancelled = c;
		this.canSkip = false;
		this.buildOnlyForRace = Race.None;
		this.proxy = false;
		this.buildWithScout = false;
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
	
	boolean canSkip(Race race){
		
		
		if(race == null){
			return false;
		}
		
		if(race.equals(Race.Unknown)){
			return false;
		}
		
		if(this.buildOnlyForRace.equals(Race.None)){
			return false;
		}
		else {
			// if race field
			if(!race.equals(Race.Unknown)){
				if(race.equals(this.buildOnlyForRace)){
					return false;
				}
			}
		}
		
		return true;
	}
	
	
	
}


