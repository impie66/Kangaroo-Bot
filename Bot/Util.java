package Bot;

import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;

public class Util {
	Game game;
	Data myData;
	
	Util(Game gaem, Data darta){
		this.game = gaem;
		this.myData = darta;
	}
	
	//NOT EVEN USED LUL
	
	
	public Position GetKitePos(Unit unit, Unit target) {
		Position Kite1 = new Position(target.getX() - unit.getX(), target.getY() - unit.getY());
		double Kite2 = Math.atan2(Kite1.getX(), Kite1.getY());
		int Int1 = (int) (64 * Math.cos(Kite2));
		int Int2 = (int) (64 * Math.sin(Kite2));
		if(Int1 < 0){
			Int1 = Math.abs(Int1);
			
		}
		if(Int2 < 0){
			Int2 = Math.abs(Int2);
			
		}
		Kite1 = new Position(Int1, Int2);
		return Kite1;
	}
	
	public Position GetKitePos2(Unit unitt, Unit targett){
		if(targett == null){
			return null;
		}
		
		
		int Int1  = targett.getX() - unitt.getX();
		int Int2 = targett.getY() - unitt.getY(); 
		int Int3 = unitt.getX() - Int1;
		int Int4 =  unitt.getY() - Int2;
		
		if(Int3 < 0){
			Int3 = Math.abs(Int3);
			
		}
		
		if(Int4 < 0){
			Int4 = Math.abs(Int4);
			
		}
		Position Kite1 = new Position(Int3, Int4);
		//System.out.println("Kite2: " + Kite1);
		return Kite1;
		
	}
	
	
	public Position GetPushPos(Unit unitt, Unit targett){
		if(targett == null){
			return null;
		}
				
		int Int1  = targett.getX() + unitt.getX();
		int Int2 = targett.getY() + unitt.getY(); 
		int Int3 = unitt.getX() - Int1;
		int Int4 =  unitt.getY() - Int2;
		
		if(Int3 < 0){
			Int3 = Math.abs(Int3);
			
		}
		
		if(Int4 < 0){
			Int4 = Math.abs(Int4);
			
		}
		Position Kite1 = new Position(Int3, Int4);
		//System.out.println("Kite2: " + Kite1);
		return Kite1;
		
	}
	
	
	public Position GetKitePos(Unit unit, Position pos) {
		Position Kite1 = new Position(pos.getX() - unit.getX(), pos.getY() - unit.getY());
		double Kite2 = Math.atan2(Kite1.getX(), Kite1.getY());
		int Int1 = (int) (64 * Math.cos(Kite2));
		int Int2 = (int) (64 * Math.sin(Kite2));
		if(Int1 < 0){
			Int1 = Math.abs(Int1);
			
		}
		if(Int2 < 0){
			Int2 = Math.abs(Int2);
			
		}
		Kite1 = new Position(Int1, Int2);
		return Kite1;
	}	
	
//	int realWeaponRange(UnitType type, Player ply){
//		TODO
//		COUNT UPGRADES FOR JUKE SCRIPT
//	}
	
	Unit getUnit(UnitType type){
		if(game.self().allUnitCount(type) == 0){
			return null;
		}
		
		for(Unit unit : game.self().getUnits()){
			if(unit.getType().equals(type)){
				return unit;
			}
		}
		
		return null;
	}
	

	

}
