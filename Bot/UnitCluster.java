package Bot;

import java.util.ArrayList;

import bwapi.Player;
import bwapi.Unit;

public class UnitCluster {

	ArrayList<Unit> units;
	Player player;
	int framesToRemove;
	
	
	UnitCluster(ArrayList<Unit> u, Player p, int frame){
		this.units = u;
		this.player = p;
		this.framesToRemove = frame;
	}
	
	
	
	ArrayList<Unit> getUnits(){
		return this.units;
	}
	
	
	Player getPlayer(){
		return this.player;
	}
	
	
}
