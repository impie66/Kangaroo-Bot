package Bot;

import bwapi.Game;
import bwapi.Position;
import bwapi.Unit;

public class Util {
	Game game;
	Data myData;
	
	Util(Game gaem, Data darta){
		this.game = gaem;
		this.myData = darta;
	}
	
	
	public Position GetKitePos(Unit unit, Unit target) {
		Position Kite1 = new Position(target.getX() - unit.getX(), target.getY() - unit.getY());
		double Kite2 = Math.atan2(Kite1.getX(), Kite1.getY());
		int Int1 = (int) (64 * Math.cos(Kite2));
		int Int2 = (int) (64 * Math.sin(Kite2));
		Kite1 = new Position(Int1, Int2);
		return Kite1;
	}	
	
	
	

}
