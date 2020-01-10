package Bot;

import java.util.ArrayList;
import java.util.List;

import bwapi.Game;
import bwapi.Order;
import bwapi.Player;
import bwapi.Position;
import bwapi.Region;
import bwapi.TechType;
import bwapi.TilePosition;
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
		int Int4 = unitt.getY() - Int2;
		
		Position Kite1 = new Position(Int3, Int4);
		//System.out.println("Kite2: " + Kite1);
		return Kite1;
		
	}
	
	public Position GetPushPos2(Unit unitt, Unit targett){
		if(targett == null){
			return null;
		}
		int d = Math.round(unitt.getDistance(targett) / 100);
		
		int Int1  = targett.getX() + unitt.getX();
		int Int2 = targett.getY() + unitt.getY(); 
		int Int3 = unitt.getX() - Int1 - d;
		int Int4 = unitt.getY() - Int2 - d;
		
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
		for(Unit unit : game.getUnitsInRadius(me.getPosition(), radius)){
			if(game.enemies().contains(unit.getPlayer())){ // IF THE UNIT IS AN ENEMY UNIT, FUCKING DIPSHIT.
				if(bool == true){ // include buildings
					if(unit.getType().isBuilding()){
						ret.add(unit);
					}
				}
				else { // don't include buildings
					if(!unit.getType().isBuilding()){
						ret.add(unit);
					}
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
				//System.out.println("Has scouted Everyone");
				if(myData.currentTarget != null){
					for(Position p : target.attackPositions){
						ret.add(p);
					}
					
					if(target.attackPositions.size() < 5){
						Position yes = target.attackPositions.get(0);
						for(Region r : game.getAllRegions()){
							if(r.getCenter().getApproxDistance(yes) < 600){
								ret.add(r.getCenter());
							}
						}
					}
					
				}
				
			}
			else {
				//System.out.println("has not scouted everyone");
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
		
		if(unit.getOrder() == Order.EnterTransport){
			return;
		}
		
		ArrayList<Unit> s = getEnemyUnitsNearMe(unit, 500, true);
		
		if(s == null){
			if(unit.getDistance(self.getStartLocation().toPosition()) > 100){
				if(unit.isSieged()){
					unit.unsiege();
				}
				unit.move(self.getStartLocation().toPosition());
			}
		}
		else {
		Unit aa = s.get(0);
		Position pos = GetKitePos2(unit, aa);
			if(pos != null){
				if(unit.isSieged()){
					unit.unsiege();
				}
				
				if(!unit.hasPath(pos)){
				pos = self.getStartLocation().toPosition();
				}
				
				if(pos.isValid(game)){
				unit.move(pos);
				}
				else {
				unit.move(self.getStartLocation().toPosition());
				}
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
		
		if(yes == null){
			return null;
		}
		
		ArrayList<Unit> ready = new ArrayList<>();
		
		for(Unit unit : yes){
			
			if(myData.isSpellCaster(unit)){
				if(unit.getType().groundWeapon().equals(WeaponType.None) || unit.getType().airWeapon().equals(WeaponType.None)){
					// if this spellcaster is unarmed (which most are)
					if(unit.getDistance(pos) < unit.getType().sightRange() * 4){
						if(!ready.contains(unit)){
							ready.add(unit);
						}
					}
				}
				else{
					// if spellcaster is armed
					if(unit.getDistance(pos) < unit.getType().sightRange() + (unit.getType().groundWeapon().maxRange())){
						if(!ready.contains(unit)){
							ready.add(unit);
						}
					}
				}
			}
			
			
			// && 
			
			
			if(unit.equals(UnitType.Terran_Bunker)){
				if(!ready.contains(unit)){
					ready.add(unit);
				}
			}
			
			if(isMelee(unit.getType()) && unit.getDistance(pos) < unit.getType().sightRange() + (unit.getType().groundWeapon().maxRange())){
				if(!ready.contains(unit)){
					ready.add(unit);
				}
			}
			
			if(unit.getType().isDetector() && unit.getDistance(pos) < 400){
				if(!ready.contains(unit)){
					ready.add(unit);
				}
			}
			
			int range = unit.getType().sightRange() + (unit.getType().groundWeapon().maxRange());
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
	
	boolean isMelee(UnitType type){
		
		if(type.equals(UnitType.Zerg_Zergling) || type.equals(UnitType.Zerg_Ultralisk) || type.equals(UnitType.Protoss_Zealot) || type.equals(UnitType.Terran_Firebat)  ){
			return true;
		}
		
		return false;
	}

	
	ArrayList<Unit> getCombatUnits(Position pos, int radius){
		// ICH WILL
		// https://www.youtube.com/watch?v=EOnSh3QlpbQ
		ArrayList<Unit> ready = new ArrayList<>();
		for(Unit unit : game.getUnitsInRadius(pos, radius)){
			if(unit.getPlayer().equals(game.self())){
				
				if(myData.IsMilitrayUnit(unit) && !ready.contains(unit)){
					ready.add(unit);
				}
				
				if(myData.IsMilitrayBuilding(unit) && !ready.contains(unit)){
					ready.add(unit);
				}
				
				if(unit.getType().equals(UnitType.Zerg_Lurker) ||
				unit.getType().equals(UnitType.Terran_Vulture_Spider_Mine) ||
				myData.isSpellCaster(unit)){
					if(!ready.contains(unit)){
						ready.add(unit);
					} 
				}
				
				
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
		
		if(target.getType().isBuilding()){
			return false;
		}
			
		int enemyWeaponRange = realWeaponRange(target.getType(), target.getPlayer());
		int weaponRange = realWeaponRange(me.getType(), game.self());
		
		
		if(target.isFlying() && !me.isFlying()){
			return false;
		}
		
		if(!isMelee(me.getType()) && isMelee(target.getType()) && myData.getScoreOf(me) < 0.85){
			return true;
		}
		
		
		if(target.getType().isWorker() && weaponRange >= enemyWeaponRange){
			if(target.getOrder() != null){
				if(target.getOrder().equals(Order.AttackMove) || 
				target.getOrder().equals(Order.AttackUnit) ||
				target.getOrder().equals(Order.AttackTile)){
					return true;
				}
			}
		}
		
			
		if(target.getType().equals(UnitType.Terran_Siege_Tank_Siege_Mode)){
			return false;
		}
			

		if(weaponRange >= enemyWeaponRange){
			return true;
		}
		
		return false;
	}
	
	boolean shouldPushAgainst(Unit me, Unit target){
		
		if(myData.getScoreOf(me) > 0.90){
			return false;
		}
		
		if(target.getType().isBuilding()){
			return false;
		}
			
		int enemyWeaponRange = realWeaponRange(target.getType(), target.getPlayer());
		int weaponRange = realWeaponRange(me.getType(), game.self());
				
		if(target.getType().equals(UnitType.Terran_Siege_Tank_Siege_Mode)){
			return true;
		}
			
		if(weaponRange <= enemyWeaponRange){
			return true;
		}
		
		return false;
	}
	
	
	
	boolean AirWingUnit(UnitType type){
		if(type.equals(UnitType.Zerg_Mutalisk) || 
			type.equals(UnitType.Terran_Wraith) || 
			type.equals(UnitType.Protoss_Scout) || 
			type.equals(UnitType.Protoss_Carrier) ||
			type.equals(UnitType.Terran_Valkyrie) 
			){
			return true;
		}
		return false;
	}
	
	boolean isPlacingBuilding(Unit unit){
		if(unit.getOrder().equals(Order.PlaceBuilding) || 
			unit.getOrder().equals(Order.PlaceProtossBuilding) || 
			unit.getOrder().equals(Order.CreateProtossBuilding) ||
			unit.getOrder().equals(Order.DroneBuild) ||
			unit.getOrder().equals(Order.DroneStartBuild)){
			return true;
		}
		else {
			return false;
		}
	}
	
	Unit getClosestStorm(Position pos){
		//StoOOOOOOrm????
		//STOOOOOOOOOOOOOOOOOOOOOOOOOORMS
		
		return null;
	}
	
	
	Position getMinePos(Unit unit, int max){
		int maxDist = 2;
		int stopDist = max;
		boolean mineThere;
		TilePosition aroundTile = unit.getTilePosition();
		while ((maxDist < stopDist)) {
			for (int i=aroundTile.getX()-maxDist; i<=aroundTile.getX()+maxDist; i++) {
				for (int j=aroundTile.getY()-maxDist; j<=aroundTile.getY()+maxDist; j++) {
					if(unit.canUseTech(TechType.Spider_Mines, new TilePosition(i, j).toPosition())){
						ArrayList<Unit> yes = getAllOf(UnitType.Terran_Vulture_Spider_Mine);
						if(yes == null){
							return new TilePosition(i, j).toPosition();
						}
						else {
							for(Unit unitt : yes){
								if(unitt.getDistance(new TilePosition(i, j).toPosition()) < 8){
									mineThere = true;
									break;
								}
							}
							
							if(mineThere = false){
								return new TilePosition(i, j).toPosition();
							}
						}
					}
				}
			}
		}
		
		return null;
	}
	
	
	boolean HasBuildingsNearby(Position pos, int radius){
		List<Unit> buildCheck = game.getUnitsInRadius(pos, radius);
		return buildCheck.isEmpty();
	}
	
	
	void Print(String str){
		System.out.println(str);
		// WAH WAH WAH WAH WAH EAH WAH WAH EWAHWS HWA HEWAH WAH WAH WAHWSA WAH WAHU WAH WAH WHA'
		
	}

	
	// return new TilePosition(i, j).toPosition();
	
	
}
