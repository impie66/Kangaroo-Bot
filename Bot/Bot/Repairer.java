package Bot;
import bwapi.*;

public class Repairer {
	Unit unit;
	Unit target;
	boolean leaveWhenRepaired;
	int loiterFrames;
	
	Repairer(Unit me, Unit what, boolean leave, int stayFrames){
		this.unit = me;
		this.target = what;
		this.leaveWhenRepaired = leave;
		this.loiterFrames = stayFrames;
	}
	
	
	


	
}
