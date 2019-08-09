package Bot;

import java.util.ArrayList;

import bwapi.Game;
import bwapi.Order;
import bwapi.Player;
import bwapi.Position;
import bwapi.Region;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwapi.WeaponType;
import bwem.BWEM;
import bwem.Base;
import bwem.CPPath;
import bwem.ChokePoint;

public class Util {
	Game game;
	Data myData;
	static BWEM bewb;
	Player self;
	
	Util(Game gaem, Data darta, BWEM b){
		this.game = gaem;
		this.myData = darta;
		this.bewb = b;
		self = game.self();
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
		int Int3;
		int Int4;
		
		if(unitt.getDistance(targett) <= 60){
			int Int1  = targett.getX() - unitt.getX();
			int Int2 = targett.getY() - unitt.getY(); 
			Int3 = (int) (unitt.getX() - Int1 * 0.05);
			Int4 =  (int) (unitt.getY() - Int2 * 0.05);
		}
		else {
			int Int1  = targett.getX() - unitt.getX();
			int Int2 = targett.getY() - unitt.getY(); 
			Int3 = unitt.getX() - Int1;
			Int4 =  unitt.getY() - Int2;
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
		
	public static int midOf(int x, int y) {
		   return x/2 + y/2 + (x%2 + y%2)/2;
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
	
	int realWeaponRange(UnitType type, Player ply){
		
		
		if(type.equals(UnitType.Protoss_Dragoon) && ply.getUpgradeLevel(UpgradeType.Singularity_Charge) == 1){
			return UnitType.Protoss_Dragoon.groundWeapon().maxRange() + 32;
		}
			
		if(type.equals(UnitType.Terran_Marine) && ply.getUpgradeLevel(UpgradeType.U_238_Shells) == 1){
			return UnitType.Terran_Marine.groundWeapon().maxRange() + 32;
		}
		
		if(type.equals(UnitType.Zerg_Hydralisk) && ply.getUpgradeLevel(UpgradeType.Grooved_Spines) == 1){
			return UnitType.Zerg_Hydralisk.groundWeapon().maxRange() + 32;
		}
					
		return type.groundWeapon().maxRange();
	}
	
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
	
	
	ArrayList<Unit> getEnemyUnitsNearMe(Unit me, Integer radius, Boolean bool){
		ArrayList<Unit> ret = new ArrayList<>();
		for(Unit unit : me.getUnitsInRadius(radius)){
			if(game.enemies().contains(unit.getPlayer())){ // IF THE UNIT IS AN ENEMY UNIT, FUCKING DIPSHIT.
				if(bool == true){ // include buildings
					if(unit.getType().isBuilding()){
						ret.add(unit);
					}
				}
				else { // don't include buildings
					ret.add(unit);
				}
			}
		}
		if(ret.isEmpty()){
			return null;
		}
		
		
		return ret;
	}
	
	ArrayList<Unit> getFriendlyUnitsNearMe(Unit me, Integer radius, Boolean bool){
		ArrayList<Unit> ret = new ArrayList<>();
		for(Unit unit : me.getUnitsInRadius(radius)){
			if(game.self().isAlly(unit.getPlayer()) || unit.getPlayer().equals(game.self())){ 
				if(bool == true){ // include buildings
					if(unit.getType().isBuilding()){
						ret.add(unit);
					}
				}
				else { // don't include buildings
					ret.add(unit);
				}
			}
		}
		if(ret.isEmpty()){
			return null;
		}
		
		
		return ret;
	}
	
	
	Position scouterPriorityTask(Unit scouter){
		ArrayList<Unit> s = getEnemyUnitsNearMe(scouter, 500, false);
		
		// check if we can block some buildings.
		// && scouter.getDistance(ss.getOrderTargetPosition()) <= ss.getDistance(ss.getOrderTargetPosition())
		if(s != null){
			for(Unit ss : s){
				if(ss.getOrder().equals(Order.PlaceBuilding)){
					// used to check if we are closer to the construction site, but we'll just try to block it anyway
					return ss.getOrderTargetPosition();
				}
			}
		}
		
		return null;
	}
	
	
	ArrayList<Position> scouterNextTasking(Unit scouter){
		ArrayList<Position> ret = new ArrayList<>();
		BotPlayer target = myData.currentTarget;

		ArrayList<Unit> s = getEnemyUnitsNearMe(scouter, 500, false);
		
		// check if we can block some buildings.
		// && scouter.getDistance(ss.getOrderTargetPosition()) <= ss.getDistance(ss.getOrderTargetPosition())
		if(s != null){
			for(Unit ss : s){
				if(ss.getOrder().equals(Order.PlaceBuilding)){
					// used to check if we are closer to the construction site, but we'll just try to block it anyway
					ret.add(ss.getOrderTargetPosition());
				}
			}
		}

		// well if we can't then we'll just move around. 
		if(ret.isEmpty()){
			if(hasScoutedEveryone()){
				if(myData.currentTarget != null){
					for(Position p : target.attackPositions){
						ret.add(p);
					}
				}
				
			}
			else {
				for(Base starts : bewb.getMap().getBases()){
					if(!game.isExplored(starts.getLocation()) && starts.isStartingLocation()){
						ret.add(starts.getCenter());
					}
					
				}
			}
			
		}

		if(ret.isEmpty()){
			return null;
		}
		
		return ret;
	}
	
// literally just copy and pasted for the main file because im lazy
// Fuck you that's why
// KLAPPER ERA HANDER
	
	public static Base getClosestBaseLocation(Position pos) {
	    Base closestBase = null;
		//System.out.println(aaaa.getId());
		//System.out.println(bewb.getMap().getBases().size());
	    double dist = Double.MAX_VALUE;
	    for (Base base : bewb.getMap().getBases()) {
	        double cDist = pos.getApproxDistance(base.getLocation().toPosition());
	    	//System.out.println(cDist);
	        if (closestBase == null || cDist < dist) {
	            closestBase = base;
	            dist = cDist;
	        }
	    }
	    
	    return closestBase;
	}

	
	void retreatFrom(Unit a, Unit aa){
		// a = retreater;
		// a = target;
		Position pos = GetKitePos2(a, aa);
		if(pos != null){
			if(a.isSieged()){
				a.unsiege();
			}
		a.move(pos, true);
		}
		
	}
	
	void reteatUnit(Unit unit){
		
		ArrayList<Unit> s = getEnemyUnitsNearMe(unit, 500, true);
		
		if(s.isEmpty()){
			if(unit.getDistance(self.getStartLocation().toPosition()) > 100){
				if(unit.isSieged()){
					unit.unsiege();
				}
				unit.move(self.getStartLocation().toPosition(), true);
			}
		}
		else {
		Unit aa = s.get(1);
		Position pos = GetKitePos2(unit, aa);
		if(pos != null){
			if(unit.isSieged()){
				unit.unsiege();
			}
			unit.move(pos, true);
		}
		
		}
		
		
	}
	

 boolean ShouldBeFocused(Unit weeheads){
	
	if(weeheads.getType() == UnitType.Terran_Vulture_Spider_Mine){
		return true;
	}
	
	if(weeheads.getType() == UnitType.Zerg_Lurker){
		return true;
	}
	
	if(weeheads.getType() == UnitType.Terran_SCV && weeheads.isRepairing() == true){
		return true;
	}
	
	if(weeheads.getType() == UnitType.Terran_Medic){
		return true;
	}
	
	if(weeheads.getType() == UnitType.Protoss_High_Templar){
		return true;
	}
	
	if(weeheads.getType() == UnitType.Protoss_Carrier){
		return true;
	}
	

	return false;
	
}
 
	public boolean IsMilitrayUnit(Unit unit) {
		int Damage = unit.getType().groundWeapon().damageAmount() + unit.getType().airWeapon().damageAmount();
		if(Damage > 0 && unit.getType().isWorker() == false && unit.getType().isBuilding() == false && unit.getType().isSpell() == false){
			return true;
		}
		return false;
	}
	
	int getScoreOf(Unit unit){
		UnitType auxType = unit.getType();
		return ((auxType.destroyScore() * auxType.maxHitPoints()) / (auxType.maxHitPoints() * 2));
	}
	
	

	int numberOfScoutedPlayers(int max){
		int i = 0;
		for(BotPlayer ply : myData.players){
			if(!ply.Buildings.isEmpty()){
				i++;
			}
		}
		
		return i;
	}
	
	boolean hasScoutedEveryone(){
		return numberOfScoutedPlayers(game.enemies().size()) >= game.enemies().size();
	}
	
	
	ArrayList<Unit> getAllOf(UnitType unit){
		ArrayList<Unit> ret = new ArrayList<>();
		for(Unit units : self.getUnits()){
			if(!ret.contains(units) && units.getType().equals(unit)){
				ret.add(units);
			}
		}
		
		
		if(ret.isEmpty()){
			return null;
		}
		
		return ret;
				
	}
	
	boolean canBeHealed(UnitType type){
		if(type.equals(UnitType.Terran_Marine) || type.equals(UnitType.Terran_Firebat) || type.equals(UnitType.Terran_Medic)){
			return true;
		}
		
		return false;
	}
	
	boolean isSpellCaster(UnitType type){
		
		if(type.equals(UnitType.Terran_Medic) || 
		 type.equals(UnitType.Terran_Ghost) || 
		 type.equals(UnitType.Terran_Science_Vessel) ||
		 type.equals(UnitType.Terran_Siege_Tank_Tank_Mode) ||
		 type.equals(UnitType.Terran_Siege_Tank_Siege_Mode) || 
		 type.equals(UnitType.Terran_Battlecruiser) ||
		 type.equals(UnitType.Zerg_Queen) ||
		 type.equals(UnitType.Zerg_Defiler) ||
		 type.equals(UnitType.Terran_Vulture) ||
		 type.equals(UnitType.Protoss_High_Templar) ){
			return true;
			
		}
		
		return false;
					
			
		}
	
	int getAmountGettingIn(Unit unit){
		int i = 0;
		i = i + unit.getLoadedUnits().size();
		for(Unit units : new ArrayList<Unit>(self.getUnits())){
			if(units.getOrder().equals(Order.EnterTransport)){
				if(unit.getOrderTarget() != null){
					if(unit.getOrderTarget().equals(unit)){
						i++;
					}
				}
			}
		}
		
		return i;
	}
	
	ArrayList<Unit> combatReadyUnits(ArrayList<Unit> yes, Position pos){
		// ICH WILL
		// https://www.youtube.com/watch?v=EOnSh3QlpbQ
		ArrayList<Unit> ready = new ArrayList<>();
		for(Unit unit : yes){
			if(unit.getType().groundWeapon().equals(WeaponType.None)){
				continue;
			}
			int range = Math.round(unit.getType().groundWeapon().maxRange() + unit.getType().groundWeapon().maxRange() * 2);
			if(unit.getDistance(pos) <= range){
				ready.add(unit);
			}
		}
		
		if(ready.isEmpty()){
			return null;
		}
		else {
			return ready;
		}
		
		
		
	}
	
	boolean shouldKiteAgainst(Unit me, Unit target){
		
		int enemyWeaponRange = realWeaponRange(target.getType(), target.getPlayer());
		int weaponRange = realWeaponRange(me.getType(), game.self());
		if(weaponRange >= (enemyWeaponRange + enemyWeaponRange / 4)){
			return true;
		}
		
		return false;
	}
	

	
	
}
