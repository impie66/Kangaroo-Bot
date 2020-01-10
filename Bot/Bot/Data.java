package Bot;
import bwem.*;
import bwta.Chokepoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bwapi.Color;
import bwapi.Game;
import bwapi.Order;
import bwapi.Player;
import bwapi.Position;
import bwapi.Race;
import bwapi.Region;
import bwapi.TechType;
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
	ArrayList<Unit> UIC;
	ArrayList<Unit> enemyDBuildings;
	HashMap<Unit, Integer> DND;
	Position nextAttackPosition;
	ArrayList<Position> attackPositions;
	ArrayList<UnitType> eggs;
	ArrayList<UnitType> enemyTypes;
	ArrayList<UnitType> enemyDTypes;
	ArrayList<Base> scouts;
	ArrayList<BotPlayer> players;
	ArrayList<Spellcaster> spellCasters;
	ArrayList<Position> Scans;
	HashMap<Unit, Double> unitScore = new HashMap<Unit, Double>();
	////REEEEEEEEEEEEEEEEEEEEE
	int enemyScore;
	int myScore;
	Race enemyRace;
	TilePosition nextExpand;
	int enemyUnitScore;
	TilePosition defencePos;
	BotPlayer currentTarget;
	boolean needsToExpand;
	ArrayList<BotBase> myExpands;
	HashMap<UnitType, Integer> unitValues = new HashMap<>();
	boolean hasEarlyExpanded = false;
	ArrayList<ChokePoint> ramps = new ArrayList<>();
	HashMap<ChokePoint, ArrayList<Region>> asdfg = new HashMap<>();
	
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
		this.spellCasters = new ArrayList<Spellcaster>();
		ArrayList<Base> Expands = new ArrayList<Base>();
		this.enemyUnitScore = 0;
		this.currentTarget = null;
		this.players = new ArrayList<>();
		this.needsToExpand = false;
		this.Scans = new ArrayList<Position>();
		this.myExpands = new ArrayList<BotBase>();
		this.UIC = new ArrayList<>();
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
		TilePosition start = self.getStartLocation();
		while(i!=max-1){
			dist = dist + 100;
			for (Base Expand : bewb.getMap().getBases()) {
				if(!game.hasPath(start.toPosition(), Expand.getCenter())){
					//FUCK ISLAND BASES.
					//System.out.println("Fuck off island base");
					max--;
				}
				if(getGroundDistance(start, Expand.getCenter().toTilePosition()) <= dist && !temp.contains(Expand) && !Expand.equals(myBase) && game.hasPath(start.toPosition(), Expand.getCenter())){
					temp.add(Expand);
					i++;
					//System.out.println("Added Base with dist of " + start.getApproxDistance(Expand.getLocation()));
				}
				if(i==max-1){
					break;
				}
			}
			
		}
		
		
			this.Expands = temp;
			DoTheThingsThatDoChokesPointsThatDoOfTheGiveBack();
			

	}
	
	void onFrame(){
		if(this.currentTarget != null){
			for (Position p : new ArrayList<>(this.currentTarget.attackPositions)) {
				game.drawCircleMap(p, 20, Color.Orange);
				// compute the TilePosition corresponding to our remembered Position p
				TilePosition tileCorrespondingToP = new TilePosition(p.getX()/32 , p.getY()/32);
	
				//if that tile is currently visible to us...
				if (game.isVisible(tileCorrespondingToP)) {
					//loop over all the visible enemy buildings and find out if at least
					//one of them is still at that remembered position
					boolean buildingStillThere = false;
					for (Unit u : new ArrayList<Unit>(this.currentTarget.Buildings)) {
						if ((u.getType().isBuilding()) && (u.getPosition().equals(p))) {
							buildingStillThere = true;
							if(u.getType().equals(UnitType.Resource_Vespene_Geyser)){
								this.currentTarget.Buildings.remove(p);
								//Just incase it's trying to attack an geyser.
								if(this.currentTarget.attackPositions.contains(u.getPosition())){
									this.currentTarget.attackPositions.remove(u.getPosition());
								}
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
						if(this.currentTarget.attackPositions.contains(p)){
							this.currentTarget.attackPositions.remove(p);
						}
						break;
					}
				}
						
			}
		}
		
		if(this.currentTarget != null){
			if(this.currentTarget.attackPositions.isEmpty()){
				if(this.scouts.isEmpty()){
					ArrayList<Unit> scanners = getAllOf(UnitType.Terran_Comsat_Station);
					int e = 0;
					if(scanners != null){
						scanners:
						for(Unit unit : scanners){
							if(unit.getEnergy() < 50){
								continue;
							}
							for(Base starts : bewb.getMap().getBases()){
								if(!game.isExplored(starts.getLocation())){
									unit.useTech(TechType.Scanner_Sweep);
									break scanners;
								}
							}
							
							for(Base starts : bewb.getMap().getBases()){
								if(!game.isVisible(starts.getLocation())){
									unit.useTech(TechType.Scanner_Sweep);
									break scanners;
								}
							}
						}
					}
					

					for(Base starts : bewb.getMap().getBases()){

						
						if(!game.isExplored(starts.getLocation()) && starts.isStartingLocation()){
							this.scouts.add(starts);
						}
						
						if(!game.isVisible(starts.getCenter().toTilePosition()) && !scouts.contains(starts)){
							this.scouts.add(starts);
						}
						
					}
				}
			
			
			if(!scouts.isEmpty()){
				if(!this.attackPositions.contains(this.scouts.get(0).getCenter())){
				this.attackPositions.add(this.scouts.get(0).getCenter());
				}
				this.nextAttackPosition = this.attackPositions.get(0);
			}
			
			}
			else {
				if(!scouts.isEmpty()){
					scouts.clear();
				}
				// if target attack positions not empty
				if(!this.currentTarget.attackPositions.contains(this.nextAttackPosition)){
					//this.nextAttackPosition = this.attackPositions.get(1);
					for(Position pos : new ArrayList<>(this.attackPositions)){
						this.nextAttackPosition = pos;
						break;
					}
				}
			}
									
		}	
		
		
		if(!this.scouts.isEmpty()){
			if(!this.currentTarget.attackPositions.isEmpty()){
				scouts.clear();
				this.attackPositions = this.currentTarget.attackPositions;
				for(Position pos : this.attackPositions){
					this.nextAttackPosition = pos;
					break;
				}

			}
			
			for(Base bass : new ArrayList<Base>(this.scouts)){
				boolean cont = true;
				if(game.isVisible(bass.getCenter().toTilePosition())){
					List<Unit> hostiles = game.getUnitsInRadius(bass.getCenter(), 500);
					for(Unit unit : hostiles){
						if(unit.getPlayer().isEnemy(self)){
							cont = false;
							break;
						}
					}
					if(cont == true){
						this.scouts.remove(bass);
						if(this.nextAttackPosition.equals(bass.getCenter())){
							this.nextAttackPosition = null;
						}
						if(this.attackPositions.contains(bass.getCenter())){
							this.attackPositions.remove(bass.getCenter());
						}
					}
				}
				
			}
		}

		
		if(!this.attackPositions.isEmpty() && this.nextAttackPosition == null){
			for(Position pos : this.attackPositions){
				this.nextAttackPosition = pos;
				break;
			}
		}
				
		for(Unit unit : new ArrayList<Unit>(this.myMilUnits)){
			if(!unit.exists()){
				this.myMilUnits.remove(unit);			
			}
		}
		
		
		
		for(Base starts : bewb.getMap().getBases()){
			for(ChokePoint choke : starts.getArea().getChokePoints()){
				game.drawLineMap(starts.getCenter(), choke.getCenter().toPosition(), Color.Blue);
			}
			
			for(Mineral min : starts.getBlockingMinerals()){
				game.drawLineMap(starts.getCenter(), min.getUnit().getPosition(), Color.Purple);
			}
		}
		
		

		
	}
	
	void DoTheThingsThatDoChokesPointsThatDoOfTheGiveBack(){
		for(Base bass : Expands){
			int dist = 0;
			ChokePoint chosen = null;
			for(ChokePoint point : bass.getArea().getChokePoints()){
				if(getGroundDistance(point.getCenter().toTilePosition(), self.getStartLocation()) >= dist){
					chosen = point;
				}
			}
			myChokes.add(chosen);
		}

		for(ChokePoint cp : bewb.getMap().getChokePoints()){
			// ramp code
			ArrayList<Region> yes = new ArrayList<>();
			int size = 0;
			int a = 0;
			for(Region r : game.getAllRegions()){
				if(r.getCenter().getApproxDistance(cp.getCenter().toPosition()) < 250){
					TilePosition tile = r.getCenter().toTilePosition();
					//System.out.println("Region: " + r.getID() + " < 250");
					size++;
					if(r.getDefensePriority()>=2){
						a++;
						//System.out.println("Region: " + r.getID() + " Is higher ground");
					}
					if(!yes.contains(r)){
						yes.add(r);
					}	
				
				}
			}
			
			if(a >= size / 2){
				if(!ramps.contains(cp)){
					ramps.add(cp);
					//System.out.println("Hey is ramp very good");
				}
			}
			
			asdfg.put(cp,yes);

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
				this.enemyUnitScore = this.enemyUnitScore + getScoreOf(unit);
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
				this.enemyUnitScore = this.enemyUnitScore - getScoreOf(unit);
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
			if(targets.getPlayer() == self && IsMilitrayUnit(targets) == true && Mine.contains(targets) == false) {
				Mine.add(targets);
			}
			
			if(targets.getPlayer().isAlly(self) && targets.getType() == UnitType.Terran_Bunker && targets.getLoadedUnits().size() > 0 && Mine.contains(targets) == false && include == true ){
				Mine.add(targets);
			}
			
			
		}
		
		return Mine;

		
	}

	public ArrayList<Unit> GetEnemyUnitsNearby(Position pos, int radius, boolean include){
		 ArrayList<Unit> Mine = new ArrayList<Unit>();
		for (Unit targets : game.getUnitsInRadius(pos, radius)) {
			if (targets.getPlayer().isEnemy(self) == true && IsMilitrayUnit(targets) == true && !Mine.contains(targets) && targets.isCompleted()) {
				Mine.add(targets);

			}
			if(targets.getPlayer().isEnemy(self) == true && IsMilitrayBuilding(targets) == true && include == true && targets.isCompleted()){
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
						int tree = getGroundDistance(pos.toTilePosition(), Expand.getLocation());
						if (tree <= dist && !alreadyClaimed(Expand) && !Expand.equals(getClosestBaseLocation(self.getStartLocation().toPosition())) && game.hasPath(self.getStartLocation().toPosition(), Expand.getCenter())) {
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
		
		for(Base yes : new ArrayList<Base>(enemyBases)){
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
			//System.out.println("Next base at " + bass.getLocation());
		}
	}
	
	boolean weaponCoolingDown(Unit unit){
			// https://www.youtube.com/watch?v=njvA03rMHx4
			// goons are doing some retarded shit
			// TODO: FUCKING THIS.
			// Maybe it's TO-DONE
			// who knows
			if(unit.getType().equals(UnitType.Protoss_Dragoon)){
				if(unit.getGroundWeaponCooldown() >= 30){
					return false;
				}
				else {
					return true;
				}
			}
			else{
				if(unit.getGroundWeaponCooldown() == 0){
					return false;
				}
				else {
					return true;
				}
				
			}
			

	}
	
	boolean canBeDistrubed(Unit unit){
		
		if(this.DND.containsKey(unit) == false){
			this.DND.put(unit, 0);
			return true;
		}
		
		if(game.getFrameCount() >= this.DND.get(unit)){
			return true;
		}
		else {
			return false;
		}
	}
		
	void DND(Unit unit, int amount){
		this.DND.put(unit, amount);
	}
	
	void newTarget(BotPlayer ply){
		this.currentTarget = ply;
		this.attackPositions = ply.attackPositions;
		if(!this.attackPositions.isEmpty()){
			this.nextAttackPosition = attackPositions.get(0);
		}
	}
	
	void newUnitScore(Unit unit, double mcravesuckslawlrektnerd){
		// Hang on a second, do i need a function to do this?
		// also wtf why don't i just unitScore.put(unit, mcravesuckslawlrektnerd); without checking if it exists?
		// ehh i already typed it out
		// also this is to store what "score" it got from the last sim (to see what dumb shit it can get away with)
		// https://www.youtube.com/watch?v=s_8KR-n2fBQ
		// I'M OUT OF TOOOOOOOUCH!
		if(this.unitScore.keySet().contains(unit)){
			this.unitScore.put(unit, mcravesuckslawlrektnerd);
		}
		else {
			this.unitScore.put(unit, mcravesuckslawlrektnerd);
		}
	}
	
	double getSimScore(Unit unit){
		if(this.unitScore.keySet().contains(unit)){
			return this.unitScore.get(unit);
		}
		else {
			return 0;
		}
	}
	
	int getGroundDistance(TilePosition start, TilePosition end){
		// thank
		return this.bewb.getMap().getPathLength(start.toPosition(), end.toPosition());
	}
	
	Spellcaster getCaster(Unit yes){
		for(Spellcaster cast : this.spellCasters){
			if(cast.unit.equals(yes)){
				return cast;
			}
		}
		
		return null;
	}
	
	void updateEco(){
		// https://www.youtube.com/watch?v=dhmfosCx4sI
		ArrayList<UnitType> yesyes = new ArrayList<>();
		yesyes.add(UnitType.Terran_Barracks);
		yesyes.add(UnitType.Terran_Factory);
		yesyes.add(UnitType.Terran_Starport);
		yesyes.add(UnitType.Zerg_Hatchery);
		yesyes.add(UnitType.Zerg_Lair);
		yesyes.add(UnitType.Zerg_Hive);
		yesyes.add(UnitType.Protoss_Gateway);
		yesyes.add(UnitType.Protoss_Robotics_Facility);
		yesyes.add(UnitType.Protoss_Stargate);
		int yesyesyes = 0;
		int yesyesyesyesyes = 0;
		
		if(this.Expands.size() >= 5){
			this.needsToExpand = false;
		}
		
		// if we aren't z
			for(Unit unit : game.self().getUnits()){
				if(yesyes.contains(unit.getType())){
					yesyesyes++;
				}
			}
			
			yesyesyesyesyes = this.myExpands.size();
			int score;
			
			if(currentTarget == null){
				score = 300;
			}
			else {
				score = currentTarget.enemyScore;
			}
			
			if(yesyesyes >= yesyesyesyesyes * 3 && myScore >= score){
				this.needsToExpand = true;
			}
			else {
				this.needsToExpand = false;
			}

	}
	
	boolean hasScannedNearby(Position pos){
		if(this.Scans.isEmpty()){
			return false;
		}
		
		for(Position p : new ArrayList<>(this.Scans)){
			if(p.getApproxDistance(pos) < 200){
				return true;
			}
		}
		
		return false;
	}
	
	void newScan(Position pos){
		this.Scans.add(pos);
	}
	
	TilePosition getDefencePos() {
		ChokePoint chosen = null;
		if(this.Expands != null){
			Base bass = Expands.get(0);
			int lowest = 0;
			//System.out.println("Size:" + bass.getArea().getChokePoints().size());
			for(ChokePoint choke : getChokesNearby(bass.getCenter(), 700)){
				int distt = 0;
				int thiss = 0;
				int amount = 0;
				for(Base bases : startLocations){
					int dsfdsf = getGroundDistance(choke.getCenter().toTilePosition(), bases.getLocation());
					distt = distt + dsfdsf;
					amount++;
				}
				thiss = distt / amount;
				//System.out.println("thiss: " + thiss);
				if(thiss <= lowest || chosen == null){
					lowest = thiss;
					chosen = choke;
				}

			}
			
			if(chosen != null){
				TilePosition fin = chosen.getCenter().toTilePosition();
				//System.out.println("Chosen not null");
				return fin;
			}
			else {
				//System.out.println("Chosen is null LULW");
				//game.sendText("BWEM shit the bed, building bunker in default");
				int f = 0;
				ChokePoint cho = null;
				main:
				for(Base expand : this.Expands){
					for(ChokePoint chokee : expand.getArea().getChokePoints()){
						int dist = getGroundDistance(self.getStartLocation(), chokee.getCenter().toTilePosition());
						if(dist >= f || cho == null){
							f = dist;
							cho = chokee;
						}
					}
					break main;
				}
				
				if(cho != null){
					return cho.getCenter().toTilePosition();
				}
				else {
					return self.getStartLocation();
				}

				
			}
				
			
		}
		System.out.println("No Expands");
		return null;
				
	}
	
	
	ArrayList<ChokePoint> getChokesNearby(Position pos, int range){
		ArrayList<ChokePoint> asf = new ArrayList<ChokePoint> ();
		for(ChokePoint cock : bewb.getMap().getChokePoints()){
			if(cock.getCenter().toPosition().getApproxDistance(pos) <= range && asf.contains(cock) == false){
				asf.add(cock);
			}
		}
		
		return asf;
	}
	


	public boolean isInCombat(Unit unit){
		if(unit.isAttacking() || unit.isUnderAttack() || unit.isStartingAttack() || unit.getOrder().equals(Order.AttackUnit)){
			return true;
		}
		
				
		return false;
	}
	
	public boolean isNearEnemyOrBetter(Unit unit){
		
		if(unit.isAttacking() || unit.isUnderAttack() || unit.isStartingAttack() || unit.getOrder().equals(Order.AttackUnit)){
			return true;
		}
		
		return this.UIC.contains(unit);
	}
	
	
	boolean canEarlyExpand(BotPlayer ply){
		
		if(hasEarlyExpanded == true){
			return false;
		}
		
		if(ply == null){
			return false;
		}
			
		
		if(ply.Buildings.isEmpty()){
			return false;
		}
				
		return ply.defenceScore >= ply.armyScore + (ply.armyScore * 0.60) + 200;
	}
	
	boolean isEarlyExpanding(BotPlayer ply){
		
		if(ply == null){
			return false;
		}
			
		
		if(ply.Buildings.isEmpty()){
			return false;
		}
				
		return ply.defenceScore >= ply.armyScore + (ply.armyScore * 0.60) + 200;
	}
	
	
	boolean isSpellCaster(Unit unit){
		return this.spellCasters.contains(unit);
	}
	
	
	int getRecommendedUnitCount(UnitType type, BotPlayer ply){
		int GC = 0;
		if(ply != null){
			if(type.equals(UnitType.Terran_Siege_Tank_Tank_Mode)){
				// siege tank amounts
				if(ply.race.equals(Race.Zerg)){
					// if enemy Z
					 GC = ply.player.allUnitCount(UnitType.Zerg_Sunken_Colony) / 2 + ply.player.allUnitCount(UnitType.Zerg_Hydralisk) / 3;
					return GC;
				}
				else if (ply.race.equals(Race.Terran)){
					// if Enemy P
					 GC = (ply.player.allUnitCount(UnitType.Protoss_Photon_Cannon) / 2) + ply.player.allUnitCount(UnitType.Protoss_Dragoon) / 3;
					return GC;
				}
				else {
					// if Enemy T
					GC = ply.player.allUnitCount(UnitType.Terran_Bunker) + ply.player.allUnitCount(UnitType.Terran_Marine) + ply.player.allUnitCount(UnitType.Terran_Firebat) / 3;
					return GC;
				}
			}		
		}
		else {
			return GC;
		}
		
		return 0;
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
	
	boolean isNearEnemyCP(Position pos){
		ChokePoint c = getNearestChokePoint(pos);
		if(c == null){
			return false;
		}
		
		for(BotPlayer p : players){
			if(p.CCP.contains(c)){
				return true;
			}
		}
		
		return false;
	}
	
	ChokePoint getNearestChokePoint(Position pos){
		int lowest = 0;
		ChokePoint chosen = null;
		for(ChokePoint cp : bewb.getMap().getChokePoints()){
			int dist = cp.getCenter().toPosition().getApproxDistance(pos);
			if(dist < lowest || lowest == 0){
				lowest = dist;
				chosen = cp;
			}
		}
		
		return chosen;
	}
	
	
	boolean AlreadyMinesNearby(Position pos, int radius){
		
		for(Unit unit : game.getUnitsInRadius(pos, radius)){
			if(unit.equals(UnitType.Terran_Vulture_Spider_Mine)){
				return true;
			}
		}
		
		return false;
	}
	
	boolean isBlockingMineral(Unit min){
		
		for(Base bass : bewb.getMap().getBases()){
			for(Mineral mine : bass.getBlockingMinerals()){
				if(mine.getUnit().equals(mine)){
					return true;
				}
			}
		}
		
		return false;
	}

	boolean HasScansAvailable(){
		ArrayList<Unit> yes = getAllOf(UnitType.Terran_Comsat_Station);
		if(yes != null){
			for(Unit unit : yes){
				if(unit.getEnergy() >= 50){
					return true;
				}
			}
		}
		else {
			return false;
		}
		
		return false;
	}
	
	Unit getTransport(Unit unit){
		for(Unit unitt : self.getUnits()){
			if(!unitt.getLoadedUnits().isEmpty()){
				if(unitt.getLoadedUnits().contains(unit)){
					return unitt;
				}
			}
		}
		
		return null;
	}

}
