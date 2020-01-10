package Bot;

import java.util.ArrayList;
import java.util.HashMap;

import bwapi.Game;
import bwapi.Race;
import bwapi.Unit;
import bwapi.UnitType;

//currently not used

public class ProductionManager {
	ArrayList<UnitType> builds;
	Game game;
	BotPlayer currentTarget;
	Data myData;
	Race race;
	HashMap<UnitType, Integer> values = new HashMap<>();
	
	ProductionManager(Data data, Game geam){
		this.builds = new ArrayList<UnitType>();
		this.currentTarget = null;
		this.myData = data;
		this.game = geam;
		this.race = game.self().getRace();
	}
	
	
	void buildAgainstTarget(BotPlayer ply){
		
	}
	
	
	void updateValues(ArrayList<Unit> visible, ArrayList<UnitType> notVisible){
		
	}
	
	
	void applyValuesAgainst(UnitType type, int bonus){
		
	}
	
	
	
}
