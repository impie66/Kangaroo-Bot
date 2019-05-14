package Bot;
import bwem.*;
import java.util.ArrayList;
import java.util.HashMap;

import bwapi.Bullet;
import bwapi.Color;
import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.Race;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;

public class Data {
	
	ArrayList<ChokePoint> myChokes;
	ArrayList<ChokePoint> enemyChokes;
	ArrayList<Base> Expands;
	ArrayList<Base> startLocations;
	Game game;
	static BWEM bewb;
	Player self;
	Base myBase;
	ArrayList<Unit> enemyBuildings;
	ArrayList<Base> enemyBases;
	ArrayList<Unit> enemyMilUnits;
	ArrayList<Unit> myMilUnits;
	ArrayList<Unit> enemyDBuildings;
	HashMap<Unit, Integer> DND;
	Position nextAttackPosition;
	ArrayList<Position> attackPositions;
	ArrayList<UnitType> eggs;
	ArrayList<UnitType> enemyTypes;
	ArrayList<UnitType> enemyDTypes;
	ArrayList<Base> scouts;
	int enemyScore;
	int myScore;
	Race enemyRace;
	TilePosition nextExpand;
	
	public Data(Game gaem, BWEM b, Base myBasee){
		this.game = gaem;
		this.bewb = b;
		this.self = game.self();
		this.myChokes = new ArrayList<ChokePoint>();
		this.enemyChokes = new ArrayList<ChokePoint>();
		this.myBase = myBasee;
		this.startLocations = new ArrayList<Base>();
		this.scouts = new ArrayList<Base>();
		this.enemyMilUnits = new ArrayList<>();
		this.enemyBuildings = new ArrayList<Unit>();
		this.DND = new HashMap<Unit, Integer>();
		this.enemyBases = new ArrayList<Base>();
		this.myMilUnits = new ArrayList<>();
		this.nextAttackPosition = null;
		this.enemyDBuildings = new ArrayList<>();
		this.eggs = new ArrayList<>();
		this.enemyTypes = new ArrayList<UnitType>();
		this.enemyDTypes = new ArrayList<UnitType>();
		this.enemyScore = 0;
		this.myScore = 0;
		this.attackPositions = new ArrayList<>();
		this.enemyRace = game.enemy().getRace();
		this.nextExpand = null;
		ArrayList<Base> Expands = new ArrayList<Base>();
		DoTheThing();

	}
	


	void DoTheThing(){
		
		for(Base starts : bewb.getMap().getBases()){
			if(starts.isStartingLocation() && !startLocations.contains(starts)){
				startLocations.add(starts);
			}
		}
		
		int max = bewb.getMap().getBases().size();
		ArrayList<Base> temp = new ArrayList<Base>();
		int i = 0;
		int dist = 200;
		Position start = self.getStartLocation().toPosition();
		while(temp.size()!=max-1){
			dist = dist + 200;
			for (Base Expand : bewb.getMap().getBases()) {
				if(start.getApproxDistance(Expand.getCenter())<= dist && !temp.contains(Expand) && !Expand.equals(myBase)){
					temp.add(Expand);
					i++;
					//System.out.println("Added Base with dist of " + start.getApproxDistance(Expand.getCenter()));
				}
				
				if(temp.size()==max-1){
					break;
				}
			}
			
		}
		
		
			this.Expands = temp;
			DoTheThingsThatDoChokesPointsThatDoOfTheGiveBack();
			

	}
	
	void onFrame(){
		
		for (Position p : new ArrayList<>(this.attackPositions)) {
			game.drawCircleMap(p, 20, Color.Orange);
			// compute the TilePosition corresponding to our remembered Position p
			TilePosition tileCorrespondingToP = new TilePosition(p.getX()/32 , p.getY()/32);

			//if that tile is currently visible to us...
			if (game.isVisible(tileCorrespondingToP)) {

				//loop over all the visible enemy buildings and find out if at least
				//one of them is still at that remembered position
				boolean buildingStillThere = false;
				for (Unit u : new ArrayList<Unit>(this.enemyBuildings)) {
					if ((u.getType().isBuilding()) && (u.getPosition().equals(p))) {
						buildingStillThere = true;
						if(u.getType().equals(UnitType.Resource_Vespene_Geyser)){
							this.attackPositions.remove(p);
							//Just incase it's trying to attack an geyser.
							if(this.nextAttackPosition != null){
								if(this.nextAttackPosition.equals(p)){
									this.nextAttackPosition = null;
								}
							}
						}
						break;
					}
				}
				//if there is no more any building, remove that position from our memory
				if (buildingStillThere == false) {
					this.attackPositions.remove(p);
					break;
				}
			}
		}
		
		
		if(this.attackPositions.isEmpty()){
		
			if(scouts.isEmpty()){
				
				for(Base starts : bewb.getMap().getBases()){
					
					if(!game.isExplored(starts.getLocation()) && starts.isStartingLocation()){
						scouts.add(starts);
					}
					
					if(!game.isVisible(starts.getCenter().toTilePosition()) && !scouts.contains(starts)){
						scouts.add(starts);
					}
					
				}
			}
			
			if(!scouts.isEmpty()){	
				this.attackPositions.add(this.scouts.get(0).getCenter());
				this.nextAttackPosition = this.attackPositions.get(0);
			}
			
			for(Base bass : new ArrayList<Base>(this.scouts)){
				if(game.isVisible(bass.getCenter().toTilePosition())){
					this.scouts.remove(bass);
				}
			}
			
		}
		
		if(!this.attackPositions.isEmpty() && this.nextAttackPosition == null){
			this.nextAttackPosition = this.attackPositions.get(0);
		}
		
		if(!this.attackPositions.isEmpty()){
			if(this.nextAttackPosition != null){
				if(!this.attackPositions.contains(this.nextAttackPosition)){
					this.nextAttackPosition = null;
				}
			}
		}
		
		
		if(this.nextExpand != null){
			game.drawCircleMap(this.nextExpand.toPosition(), 30, Color.Yellow);
			game.drawTextMap(this.nextExpand.toPosition(), "HMMM MAYBE I PUT BASE HERE? HMMMMMMMMM");
		}
		
		for(Unit unit : new ArrayList<Unit>(this.myMilUnits)){
			
			if(!unit.exists()){
				this.myMilUnits.remove(unit);
			}
		
		}
		
//		for(Bullet b : game.getBullets()){
//			Unit sauce = b.getSource();
//			if(sauce != null){
//				if(sauce.getPlayer().equals(self)){
//					ACDS.put(sauce, game.getFrameCount() + sauce.getGroundWeaponCooldown());
//					System.out.println("Boulets: " + sauce.getID() + " ");
//				}
//			}
//			
//			
//		}
		

	}
	
	void DoTheThingsThatDoChokesPointsThatDoOfTheGiveBack(){
		for(Base bass : Expands){
			int dist = 0;
			ChokePoint chosen = null;
			for(ChokePoint point : bass.getArea().getChokePoints()){
				if(point.getCenter().toPosition().getApproxDistance(self.getStartLocation().toPosition()) >= dist){
					chosen = point;
				}
			}
			myChokes.add(chosen);
		}
	}
	
	
	ArrayList<Base> getExpands(){
		
		if(Expands.isEmpty() == false){
			return this.Expands;
		}
		
		
		return null;
	}
	
	void newEnemyBuilding(Unit unit){
		if(!this.enemyBuildings.contains(unit)){
			this.enemyBuildings.add(unit);
			if(!this.enemyRace.equals(unit.getPlayer().getRace() ) ){
				this.enemyRace = unit.getPlayer().getRace();
			}
			if(!this.attackPositions.contains(unit.getPosition())){
				this.attackPositions.add(unit.getPosition());
			}
			if(this.nextAttackPosition != null){
				if(this.nextAttackPosition.equals(unit.getPosition())){
					this.nextAttackPosition = null;
				}
			}
			//System.out.println("Enemy Building Discovered: " + unit.getType().toString());
		}
	}
	
	void newEnemyBase(Base unit){
		if(!this.enemyBases.contains(unit)){
			this.enemyBases.add(unit);
			Base bass = getClosestBaseLocation(unit.getCenter());
			if(this.Expands.contains(bass)){
				this.Expands.remove(bass);
			}
		}
	}
	
	void newMilUnit(Unit unit){
		if(unit.getPlayer().equals(self)){
			// if it's one of my units
			if(!this.myMilUnits.contains(unit)){
				this.myMilUnits.add(unit);
				this.myScore = this.myScore + getScoreOf(unit);
				//System.out.println("My Unit: " + unit.getType().toString());
			}
		}
		
		if(game.enemies().contains(unit.getPlayer())){
			UnitType type = unit.getType();
			if(!this.enemyMilUnits.contains(unit) && IsMilitrayUnit(unit)){
				this.enemyMilUnits.add(unit);
				this.enemyTypes.add(type);
				this.enemyScore = this.enemyScore + getScoreOf(unit);
				//System.out.println("Enemy Unit: " + unit.getType().toString());
			}
			
			
		}
	}
	
	void unitDeath(Unit unit){
		if(unit.getPlayer().equals(self)){
			if(this.myMilUnits.contains(unit)){
				this.myMilUnits.remove(unit);
				this.myScore = this.myScore - getScoreOf(unit);
			}
			
			
		}
		
		if(game.enemies().contains(unit.getPlayer())){
			if(this.enemyMilUnits.contains(unit)){
				this.enemyMilUnits.remove(unit);
				this.enemyTypes.remove(unit.getType());
				this.enemyScore = this.enemyScore - getScoreOf(unit);
				//System.out.println("Enemy Unit Death: " + unit.getType().toString());
			}
			
			if(this.enemyBuildings.contains(unit)){
				this.enemyBuildings.remove(unit);
				if(this.attackPositions.contains(unit.getPosition())){
					this.attackPositions.remove(unit.getPosition());
				}
			}
		}
	}
	
	boolean isBeingBirthed(UnitType type){
		for(Unit unit : self.getUnits()){
			if(unit.getType().equals(UnitType.Zerg_Egg)){
				UnitType what = unit.getBuildType();
				if(type.equals(what)){
					return true;
				}
			}
		}
		
		return false;
	}
	
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

	public static ChokePoint getClosestChokePoint(Position pos) {
	    ChokePoint closestBase = null;
	    double dist = Double.MAX_VALUE;
	    for (ChokePoint base : bewb.getMap().getChokePoints()) {
	        double cDist = pos.getApproxDistance(base.getCenter().toPosition());
	        if (closestBase == null || cDist < dist) {
	            closestBase = base;
	            dist = cDist;
	        }
	    }

	    return closestBase;
	}

	void newDBuilding(Unit unit){
		if(game.enemies().contains(unit.getPlayer())){
			if(!enemyDBuildings.contains(unit)){
				enemyDBuildings.add(unit);
				enemyDTypes.add(unit.getType());
				this.enemyScore = this.enemyScore + getScoreOf(unit);
			}
		}
	}
	
	public ArrayList<Unit> GetMyUnitsNearby(Position pos, int radius, boolean include){
		 ArrayList<Unit> Mine = new ArrayList<Unit>();
		for (Unit targets : game.getUnitsInRadius(pos, radius)) {
			int damage = targets.getType().groundWeapon().damageAmount() + targets.getType().airWeapon().damageAmount();
			
			if (targets.getPlayer() == self && IsMilitrayUnit(targets) == true && Mine.contains(targets) == false) {
				Mine.add(targets);

			}
			if(targets.getPlayer().isAlly(self) && targets.getType() == UnitType.Terran_Bunker && targets.getLoadedUnits().size() > 0 &&Mine.contains(targets) == false && include == true ){
				Mine.add(targets);
			}
		}
		
		return Mine;

		
	}

	public ArrayList<Unit> GetEnemyUnitsNearby(Position pos, int radius, boolean include){
		 ArrayList<Unit> Mine = new ArrayList<Unit>();
		for (Unit targets : game.getUnitsInRadius(pos, radius)) {
			int damage = targets.getType().groundWeapon().damageAmount() + targets.getType().airWeapon().damageAmount();
			if (targets.getPlayer().isEnemy(self) == true && IsMilitrayUnit(targets) == true && !Mine.contains(targets)) {
				Mine.add(targets);

			}
			if(targets.getPlayer().isEnemy(self) == true && IsMilitrayBuilding(targets) == true && include == true){
				Mine.add(targets);
			}
		}
		
		return Mine;

		
	}
	
	public boolean IsMilitrayUnit(Unit unit) {
		int Damage = unit.getType().groundWeapon().damageAmount() + unit.getType().airWeapon().damageAmount();
		if(Damage > 0 && unit.getType().isWorker() == false && unit.getType().isBuilding() == false && unit.getType().isSpell() == false){
			return true;
		}
		return false;
	}
	
	public boolean IsMilitrayBuilding(Unit unit) {
		if(unit.getType() == UnitType.Terran_Bunker ||
		unit.getType() == UnitType.Terran_Missile_Turret ||
		unit.getType() == UnitType.Zerg_Sunken_Colony ||
		unit.getType() == UnitType.Zerg_Spore_Colony ||
		unit.getType() == UnitType.Protoss_Photon_Cannon){
			return true;
		}
	

	return false;

}
	
	public int howManyBeingMorphed(UnitType type){
		int i = 0;
		for(Unit unit : self.getUnits()){
			if(unit.getType().equals(UnitType.Zerg_Egg)){
				if(unit.getBuildType().equals(type)){
					i++;
				}
			}
		}
		//System.out.println("Morphed: " + i);
		return i;
	}
	
//	Base nearestUnclaimedBase(Position pos){
//		int i = 0;
//		int lowest = 0;
//		boolean found = false;
//		Base chosen = null;
//		while (found == false) {
//			for(Base bass : Expands){
//				int dist = bass.getCenter().getApproxDistance(pos);
//				if(dist <= lowest && !alreadyClaimed(bass)){
//					chosen = bass;
//					lowest = dist;
//				}
//			}
//			
//			
//		}
//		
//		return chosen;
//	}
//	
	
	Base nearestUnclaimedBase(Position pos){
		//copyright 2019
			boolean hasLocation = false;
			int stopdist = 5000;
			int dist = 0;
			int i = 0;
			int max = Expands.size();
			while (hasLocation == false && dist < stopdist) {
				dist = dist + 200;
					for (Base Expand : Expands) {
						int tree = (int) pos.getApproxDistance(Expand.getCenter());
						if (tree < dist && !alreadyClaimed(Expand) ) {
							hasLocation = true;
							return Expand;
						}
		
					}

			}
			return null;
		
	}
	
	boolean alreadyClaimed(Base bass){
		ArrayList<Base> claimed = new ArrayList<Base>();
		for(Unit unit : self.getUnits()){	
			Base looped = getClosestBaseLocation(unit.getPosition());
			if(unit.getType().isResourceDepot()){
				if(!claimed.contains(looped)){
					claimed.add(looped);
				}
			}
		}
		
		for(Base yes : enemyBases){
			if(!claimed.contains(yes)){
				claimed.add(yes);
			}
		}
			
		return claimed.contains(bass);
	}
	
	
	
	int getScoreOf(Unit unit){
		UnitType auxType = unit.getType();
		return ((auxType.destroyScore() * auxType.maxHitPoints()) / (auxType.maxHitPoints() * 2));
	}
	
	
	void updateNextExpansion(Position pos){
		Base bass = nearestUnclaimedBase(pos);
		if(bass != null){
			this.nextExpand = bass.getLocation();
			System.out.println("Next base at " + bass.getLocation());
		}
		
	}
	
	boolean weaponCoolingDown(Unit unit){
		if(unit.getGroundWeaponCooldown() == 0){
			return false;
		}
		
		return true;
	}
	
	boolean canBeDistrubed(Unit unit){
		
		if(DND.containsKey(unit) == false){
			DND.put(unit, 0);
			return true;
		}
		
		if(game.getFrameCount() >= DND.get(unit)){
			return true;
		}
		else {
			return false;
		}
	}
		
	void DND(Unit unit, int amount){
		DND.put(unit, amount);
	}
	
	
	
}
