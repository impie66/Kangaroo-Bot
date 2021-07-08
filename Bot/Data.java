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
import bwapi.WeaponType;


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
	ArrayList<BotBase> myBases;
	ArrayList<BotPlayer> players;
	ArrayList<Spellcaster> spellCasters;
	ArrayList<Position> Scans;
	ArrayList<FogUnit> fogUnits;
	ArrayList<Intent> tents;
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
	HashMap<Integer, TilePosition> geysers = new HashMap<>();
	int pStats = 0;
	int lastExpandFrame = 5000;
	ChokePoint mainChoke;
	boolean canFuckBab; // is there a 3 or more bases? if so we'll attack the bases. 
	boolean needsGas;
	HashMap<TechType, Integer> techScores = new HashMap<>();
	DecisionManager manager;
	boolean mineralFocus = false;
	

	
	public Data(Game gaem, BWEM b, Base myBasee, DecisionManager m){
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
		this.pStats = 0;
		DoTheThing();
		this.defencePos = TilePosition.None;
		this.fogUnits = new ArrayList<>();
		this.geysers = new HashMap<>();
		this.tents = new ArrayList<>();
		this.manager = m;
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
		DoTheGridThing();
		this.defencePos = getDefencePos();
		if(Expands.isEmpty()){
			System.out.println("RIP BWEM, MAP: " + game.mapName());
			game.sendText("Map analzyer failed to read the map, The bot will now crash.");
		}
		
		this.mainChoke = setMainChoke(myBase, Expands.get(0));
		FuckMeBannerLordIsComingOutIn3HoursIGottaGetThisShitDone();
			
		for(Base bs : new ArrayList<>(Expands)){
			for(Geyser g : bs.getGeysers()){
				if(!geysers.containsKey(g.getUnit().getID())){
					geysers.put(g.getUnit().getID(), g.getUnit().getTilePosition());
				}
			}
		}
			
		//System.out.println("Sizes: " + geysers.keySet().size());
	}
	
	void onFrame(){
		
		
//		for(bwapi.Region r : game.getAllRegions()){
//			if(game.isVisible(r.getCenter().toTilePosition())){
//				//game.drawTextMap(r.getCenter(), "Hello i am a region");
//				
//			}
//		}
		
		
		for(Integer i : new ArrayList<>(geysers.keySet())){
			Unit u = game.getUnit(i);
			if(u != null){
				if(u.getType().equals(UnitType.Resource_Vespene_Geyser)){
					game.drawCircleMap(u.getPosition(), 15, Color.Green);
				}
			}

		}
		
		
		if(this.nextAttackPosition != null){
			if(!this.currentTarget.attackPositions.contains(this.nextAttackPosition)){
				this.nextAttackPosition = null;
				//System.out.println("Hey, next attack position is shit. Give me a new one");
			}
		}
		
		
		if(this.currentTarget != null){
			for (Position p : new ArrayList<>(this.currentTarget.attackPositions)) {
				game.drawCircleMap(p, 20, Color.Orange);
				TilePosition tileCorrespondingToP = new TilePosition(p.getX()/32 , p.getY()/32);			
				if (game.isVisible(tileCorrespondingToP)) {
					boolean buildingStillThere = EnemysNearby(p,70);
					if (buildingStillThere == false) {
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
					game.sendText("Where's your base, mate?");
					if(self.allUnitCount(UnitType.Terran_Comsat_Station) > 0){
						ArrayList<Unit> scanners = getAllOf(UnitType.Terran_Comsat_Station);
						if(scanners != null){
							scanners:
							for(Unit unit : scanners){
								if(unit.getEnergy() < 50){
									continue;
								}
								
								for(Base starts : bewb.getMap().getBases()){
									if(!game.isExplored(starts.getLocation()) && game.hasPath(self.getStartLocation().toPosition(), starts.getCenter())){
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
					}
					

					for(Base starts : bewb.getMap().getBases()){

						if(!game.isVisible(starts.getCenter().toTilePosition()) && !scouts.contains(starts) && game.hasPath(self.getStartLocation().toPosition(), starts.getCenter())){
							this.scouts.add(starts);
						}
						
						if(!game.isExplored(starts.getLocation()) && starts.isStartingLocation() && game.hasPath(self.getStartLocation().toPosition(), starts.getCenter())){
							this.scouts.add(starts);
						}
						
						
					}
				}
			
			
				if(!scouts.isEmpty() && this.currentTarget.attackPositions.isEmpty()){
					Print("Trigger 1");
					for(Base pos : this.scouts){
						Position yes = pos.getCenter();
						if(!this.currentTarget.attackPositions.contains(yes)){
							this.currentTarget.attackPositions.add(yes);
							Area a = bewb.getMap().getNearestArea(pos.getCenter().toTilePosition());
							
							if(game.getFrameCount() >= 10000){
								if (!this.currentTarget.attackPositions.contains(a.getTopLeft())){
									this.currentTarget.attackPositions.add(a.getTopLeft().toPosition());
								}
								if (!this.currentTarget.attackPositions.contains(a.getBottomRight())){
									this.currentTarget.attackPositions.add(a.getBottomRight().toPosition());
								}
								
								ArrayList<Region> ja = getUnexporedRegionsNear(pos.getCenter(), 700);
								
								if(ja != null){
									for(Region r : ja){
										if (!this.currentTarget.attackPositions.contains(r.getCenter())){
											this.currentTarget.attackPositions.add(r.getCenter());
										}
									}
								}
							}
							
						}
					}
					this.nextAttackPosition = this.currentTarget.attackPositions.get(0);
				}
			
			}
			else {
				// if attack pos is not empty
				if(!scouts.isEmpty() && !currentTarget.Buildings.isEmpty()){
					Print("Trigger 2");
					for(Position pos : new ArrayList<>(this.currentTarget.attackPositions)){
						for(Base base : scouts){
							if(pos.getApproxDistance(base.getCenter()) < 25){
								this.currentTarget.attackPositions.remove(pos);
							}
						}
					}
					scouts.clear();
					
				}
				
				//trigger 2.5 is related to the issue
				
				// if target attack positions not empty
				if(!this.currentTarget.attackPositions.contains(this.nextAttackPosition)){
					Print("Trigger 2.5");
					for(Position pos : new ArrayList<>(this.currentTarget.attackPositions)){
						this.nextAttackPosition = pos;
						break;
					}
				}
				
				
				
				
				
				
				
			}
									
		}	
		
		
		if(!this.scouts.isEmpty()){
			if(!this.currentTarget.attackPositions.isEmpty() && !this.currentTarget.Buildings.isEmpty()){
				Print("Trigger 3");
				for(Base bass : new ArrayList<Base>(this.scouts)){
					if(this.currentTarget.attackPositions.contains(bass.getCenter())){
						this.currentTarget.attackPositions.remove(bass.getCenter());
					}
				}
				scouts.clear();
				if(this.nextAttackPosition == null){
					this.nextAttackPosition = this.currentTarget.attackPositions.get(0);
				}
				
				for(Base bass : new ArrayList<Base>(this.scouts)){
					//Print("Trigger 4");
					boolean cont = true;
					if(game.isVisible(bass.getCenter().toTilePosition())){
						cont = EnemysNearby(bass.getCenter(), 500);
						if(!cont == true){
							// if no enemies
							this.scouts.remove(bass);
							if(this.nextAttackPosition.equals(bass.getCenter())){
								this.nextAttackPosition = null;
							}
							
							if(this.currentTarget.attackPositions.contains(bass.getCenter())){
								this.currentTarget.attackPositions.remove(bass.getCenter());
							}
						}
					}
					
				}
				
				

			}
			

		}

		if(this.currentTarget != null){
			if(!this.currentTarget.attackPositions.isEmpty() && this.nextAttackPosition == null){
				//Print("Trigger 5");
				for(Position pos : this.currentTarget.attackPositions){
					this.nextAttackPosition = pos;
					break;
				}
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
		
		if(getDefencePos() != null){
			if(getDefencePos().isValid(game)){
				game.drawTextMap(getDefencePos().toPosition(), "Build Defences somewhere around here");
			}
		}
		
		
		for(Intent i : new ArrayList<>(tents)){
			if(!i.caster.exists() || !i.target.exists()){
				tents.remove(i);
				continue;
			}
			
			if(i.targetEffected()){
				tents.remove(i);
				continue;
			}
			
			if(!i.isCasting()){
				tents.remove(i);
				continue;
			}
			
			game.drawLineMap(i.caster.getPosition(), i.target.getPosition(), Color.Blue);
			
			continue;
		}
		
			
	} // end of update
	
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
			
			if(targets.getPlayer().isAlly(self) || targets.getPlayer() == game.self()){
						
				if(IsMilitrayUnit(targets) == true && Mine.contains(targets) == false) {
					Mine.add(targets);
				}
				
				if(targets.getType() == UnitType.Terran_Bunker && targets.getLoadedUnits().size() > 0 && Mine.contains(targets) == false && include == true ){
					Mine.add(targets);
				}
				
				
				if(isSpellCasterType(targets.getType())){
					if(!Mine.contains(targets)){
						Mine.add(targets);
					}
				}
			
			}
			
		}
		
		return Mine;

		
	}

	public ArrayList<Unit> GetEnemyUnitsNearby(Position pos, int radius, boolean include){
		 ArrayList<Unit> Mine = new ArrayList<Unit>();
		for (Unit targets : game.getUnitsInRadius(pos, radius)) {
			
			if(game.enemies().contains(targets.getPlayer())){
				// IF ENEMY
				// YOU FUCKING RETARD.
				if (IsMilitrayUnit(targets) == true && !Mine.contains(targets) && targets.isCompleted() && !targets.getType().isWorker()) {
					Mine.add(targets);
				}
				
				if(targets.getType().isWorker() && isWorkerDoingTheBad(targets)){
					Mine.add(targets);
				}
				
				if(IsMilitrayBuilding(targets) == true && include == true && targets.isCompleted()){
					Mine.add(targets);
				}
				
				if(isSpellCasterType(targets.getType())){
					if(!Mine.contains(targets)){
						Mine.add(targets);
					}
				}
			}
		}
		
		return Mine;

		
	}
	
	
	boolean isWorkerDoingTheBad(Unit unit){
		if(!unit.getType().isWorker()){
			return false;
		}
		
		if(unit.isAttacking() || unit.getOrder().equals(Order.AttackUnit) || unit.getOrder().equals(Order.AttackMove) || unit.getOrder().equals(Order.Repair) || unit.getOrder().equals(Order.MoveToRepair)){
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
		
		
		for(BotPlayer p : players){
			for(Base bah : p.Bases){
				if(!claimed.contains(bah)){
					claimed.add(bah);
				}
			}
					
		}
			
		return claimed.contains(bass);
	}
	
	
	
	int getScoreOf(Unit unit){
		UnitType auxType = unit.getType();
		if(auxType.maxHitPoints() == 0){
			return 0;
		}
		
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
			// https://www.youtube.com/watch?v=LL70vTa0E2g
			// goons are doing some retarded shit
			// TODO: FUCKING THIS.
			// Maybe it's TO-DONE
			// who knows
			if(unit.getType().equals(UnitType.Protoss_Dragoon)){
				if(unit.getGroundWeaponCooldown() > 14){
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
		
		
		if(unit.isUnderStorm()){
			return false;
		}
		
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
		if(amount < game.getFrameCount()){
			amount = game.getFrameCount() + amount;
		}
		this.DND.put(unit, amount);
	}
	
	void newTarget(BotPlayer ply){
		
		if(currentTarget == null){
			this.currentTarget = ply;
			this.attackPositions = ply.attackPositions;
			if(!this.attackPositions.isEmpty()){
				this.nextAttackPosition = attackPositions.get(0);
			}
		}
		else {
			if(!this.currentTarget.equals(ply)){
				this.currentTarget = ply;
				this.attackPositions = ply.attackPositions;
				if(!this.attackPositions.isEmpty()){
					this.nextAttackPosition = attackPositions.get(0);
				}
			}
		}
	}
	
	void newUnitScore(Unit unit, double mcravesuckslawlrektnerd){
		// Hang on a second, do i need a function to do this?
		// also wtf why don't i just unitScore.put(unit, mcravesuckslawlrektnerd); without checking if it exists?
		// ehh i already typed it out
		// also this is to store what "score" it got from the last sim (to see what dumb shit it can get away with)
		// https://www.youtube.com/watch?v=s_8KR-n2fBQ
		// <REDACTED DUE TO SPOILERS>
		// BANGER Alert
		
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
		
		int PStatsToExpand = 20;
		int max = 0;
		Base myStart = myBase;
		int i = 0;
		
		
		for(Base b : new ArrayList<>(this.startLocations)){
			if(b.isStartingLocation() && b != myStart){
				max++;
				i = i + myStart.getLocation().getApproxDistance(b.getLocation());
				//System.out.println("dist " + myStart.getLocation().getApproxDistance(b.getLocation()) );
			}
			
			
		}
		
		//System.out.println("I1 " + i);
		i = i/max;
		//System.out.println("I2 " + i);
		PStatsToExpand = i/5;
		//System.out.println("PStatsToExpand: " + PStatsToExpand);
		
		
		if(this.myBases.size() >= 5){
			this.needsToExpand = false;
		}
		
		
		if(game.getFrameCount() >= 13000 && this.myBases.size() == 1){
			this.needsToExpand = true;
		}
		
		
		if(pStats >= PStatsToExpand && this.myBases.size() == 1){
			this.needsToExpand = true;
		}
		
		if(pStats >= PStatsToExpand * 2 && this.myBases.size() <= 4){
			this.needsToExpand = true;
			// TAKE THE FUCKING MAP YOU DUMB DOG.
		}
		
			
		if(this.needsToExpand == false){
			for(Unit unit : game.self().getUnits()){
				if(yesyes.contains(unit.getType())){
					yesyesyes++;
				}
			}
				
			yesyesyesyesyes = this.myExpands.size();

			if(yesyesyes >= yesyesyesyesyes * 3){
				this.needsToExpand = true;
			}
			else {
				this.needsToExpand = false;
			}
		}

	}
	
	
	boolean canTellEnemyToFuckOffAndGetMoreCash(BotPlayer p){
		return canDefendAgainst(p);
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
		
		if(this.defencePos != null){
			if(!this.defencePos.equals(TilePosition.None)){
				return this.defencePos;
			}
		}
		
		ChokePoint chosen = null;
		
		if(this.Expands != null){
			Base bass;
			if(this.Expands.isEmpty()){
				Base notThisOne = getClosestBaseLocation(self.getStartLocation().toPosition());
				bass = DoTheThingThatGivesABaseThatsAlsoNotThat(notThisOne.getCenter(),notThisOne);
				//System.out.println("Expands for defencePos is null. Selecting nearest base to go off. ");
			}
			else {
			bass = Expands.get(0);
			}
			int lowest = 0;
			//System.out.println("Size:" + bass.getArea().getChokePoints().size());
			ArrayList<ChokePoint> yes = new ArrayList<>();
			
			for(ChokePoint asdf : bass.getArea().getChokePoints()){
				yes.add(asdf);
			}
			
			for(ChokePoint choke : getChokesNearby(bass.getCenter(), 600)){
				if(!yes.contains(choke)){
					yes.add(choke);
				}
			}
			
			for(ChokePoint choke : yes){
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
				game.sendText("Code failed, defencelocation is at base.");
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
		//System.out.println("No Expands");
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
		
		
		if(ply == null){
			return false;
		}
		
		
		if(self.getRace().equals(Race.Terran)){
			if(currentTarget.howManyHave(UnitType.Protoss_Dark_Templar) + currentTarget.howManyHave(UnitType.Zerg_Lurker) + currentTarget.howManyHave(UnitType.Protoss_Templar_Archives) == 0){
 				ArrayList<Unit> sats = getAllOf(UnitType.Terran_Comsat_Station);
 				if(sats != null){
		 			for(Unit sat : sats){
		 				if(sat.getEnergy() >= 50){
							sat.useTech(TechType.Scanner_Sweep, currentTarget.attackPositions.get(0));
							newScan(currentTarget.attackPositions.get(0));	
							break;
		 				}
		 			}
	 			}
			}
		}
		
		
		if(hasEarlyExpanded == true){
			return false;
		}
		
		if(game.getFrameCount() >= 12000){
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

		for(Spellcaster cast : new ArrayList<>(this.spellCasters)){
			if(cast.unit.equals(unit)){
				return true;
			}
		}
		
		return false;
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
			if(unit.getType().equals(UnitType.Terran_Vulture_Spider_Mine)){
				return true;
			}
			
			if(unit.getOrder().equals(Order.PlaceMine)){
				if(unit.getPosition().getApproxDistance(pos) < radius){
					return true;
				}
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
	
	Base getBaseFromHash(int h){
		for(Base bass : bewb.getMap().getBases()){
			if(bass.hashCode() == h){
				return bass;
			}
		}
		
		return null;
	}
	
	boolean canRetreat(){
		// check to see if my units can retreat near my base.
		if(self.getRace().equals(Race.Zerg)){
			if(self.allUnitCount(UnitType.Zerg_Sunken_Colony) > 0){
				return true;
			}
		}
		else if(self.getRace().equals(Race.Terran)){
			if(self.allUnitCount(UnitType.Terran_Bunker) > 0){
				return true;
			}
		}
		else {
			if(self.allUnitCount(UnitType.Protoss_Photon_Cannon) > 0){
				return true;
			}
		}
		

		return false;
	}
	
	boolean isInSpotToRetreat(Unit myUnit){
		int distToDefence = myUnit.getPosition().getApproxDistance(getDefencePos().toPosition());
		
		
		if(getSimScore(myUnit) > 0.75){
			return true;
		}

		if(myUnit.getPosition().getApproxDistance(self.getStartLocation().toPosition()) > 2500){
			// extreme case
			return true;
		}
		
		if(distToDefence > 250 && distToDefence < 150){
		return true;
		}
		
		
		return false;
	}
	
	void DoTheGridThing(){
		// nup
		// fuck this.
	}
	
	ArrayList<Region> getUnexporedRegionsNear(Position pos, int range){

		ArrayList<Region> ret = new ArrayList<>();
		if(pos == null){
			return null;
		}
		
		Region select = game.getRegionAt(pos);
		
		for(Region r : game.getAllRegions()){
			if(r.getDistance(select) < range){
				if(!game.isExplored(r.getCenter().toTilePosition())){
					ret.add(r);
				}
			}
		}
		
		
		
		return ret;
		
		
	}
	
	public static Base DoTheThingThatGivesABaseThatsAlsoNotThat(Position pos, Base notThisOne) {
	    Base closestBase = null;
		//System.out.println(aaaa.getId());
		//System.out.println(bewb.getMap().getBases().size());
	    double dist = Double.MAX_VALUE;
	    for (Base base : bewb.getMap().getBases()) {
	    	if(base.equals(notThisOne)){
	    		continue;
	    	}
	        double cDist = pos.getApproxDistance(base.getLocation().toPosition());
	    	//System.out.println(cDist);
	        if (closestBase == null || cDist < dist) {
	            closestBase = base;
	            dist = cDist;
	        }
	    }
	    
	    return closestBase;
	}
	
	
	ChokePoint setMainChoke(Base start, Base natural){
		ChokePoint ret = null;
		int d = 0;
		
		if(start == null){
			start = getClosestBaseLocation(self.getStartLocation().toPosition());
		}
		
		for(ChokePoint cp : start.getArea().getChokePoints()){
			int dist = cp.getCenter().toPosition().getApproxDistance(natural.getCenter());
			if(ret == null){
				ret = cp;
				d = dist;
			}
			else {
				if(dist <= d){
					ret = cp;
					d = dist;
				}
			}
			
			
		}
		
		
		return ret;
		
		
	}
	
	boolean EnemysNearby(Position pos, int max){
		ArrayList<Unit> units = new ArrayList<> (game.getUnitsInRadius(pos, max));	
			for(Unit unit : units){
				if(game.enemies().contains(unit.getPlayer())){
					return true;
				}
			}
			
		return false;
		
	}
	
	
	
	boolean EnemyUnitsNearby(Position pos, int max){
		ArrayList<Unit> units = new ArrayList<> (game.getUnitsInRadius(pos, max));
			for(Unit unit : units){
				if(IsMilitrayUnit(unit) && !unit.getType().isBuilding() && game.enemies().contains(unit.getPlayer()) && unit.isCompleted()){
					return true;
				}
			}
			
		return false;
		
	}
	
	
	
	boolean FriendlyUnitsNearby(Position pos, int max){
		ArrayList<Unit> units = new ArrayList<> (game.getUnitsInRadius(pos, max));
			for(Unit unit : units){
				if(IsMilitrayUnit(unit) || IsMilitrayBuilding(unit)){
					if(unit.getPlayer().equals(self)){
					return true;
					}
				}
			}
			
		return false;
		
	}
	
	boolean isTankDoingAOppsie(Unit target, int radius){
		for(Unit unit : game.getUnitsInRadius(target.getPosition(), radius)){
		
			if(unit.getPlayer().equals(self) && !unit.getType().isFlyer()){
				return true;
			}

		}
		
		
		
		return false;
	}
	
	ArrayList<Unit> getBonusUnitsToSim(Position pos, int max){
		ArrayList<Unit> ret = new ArrayList<>();
		for(Player p : game.enemies()){
			for(Unit unit : p.getUnits()){
				if(unit.isIdle()){
					continue;
				}
				
				if(unit.getOrder().equals(Order.AttackUnit) && unit.getOrderTargetPosition().getApproxDistance(pos) < max){
					if(ret.contains(unit)){
						ret.add(unit);
					}
				}
				
				if(unit.getOrder().equals(Order.Sieging) || unit.isSieged()){
					if(unit.getPosition().getApproxDistance(pos) < max){
						if(ret.contains(unit)){
							ret.add(unit);
						}
					}
				}
				
				
				
			}
		}
		
		return ret;
	}
	

	ArrayList<Position> whatDoOfTheThingThatGivesMeManyGoodWalkZones(Position start, Position end){
		ArrayList<Position> ret = new ArrayList<>();
		bewb.getMap().enableAutomaticPathAnalysis();
		bewb.getMap().getPath(start, end);

		
		CPPath cp = bewb.getMap().getPath(start, end);
		for(ChokePoint cpp : cp){
			System.out.println("cpp " + cpp.getCenter().toPosition());
			ret.add(cpp.getCenter().toPosition());
		}
		
		
		//System.out.println("Size: " + ret.size());
		return ret;
	}
	
	
	Region getNearestWalkableRegion(Position pos){
		Region ret = null;
		int i = 0;
		
		for(Region r : game.getAllRegions()){
			int dist = r.getCenter().getApproxDistance(pos);
			if(dist < i || i == 0){
				if(r.isAccessible()){
				i = dist;
				ret = r;
				}
			}
		}
		
		
		
		
		return ret;
	}
	
	
	void checkFor3rdBases(BotPlayer t){
		if(t == null){
			return;
		}
		
		if(t.Bases.size() > 1 && t.basePositions.size() > 1 && t.startLocation != null){
			
			if(this.nextAttackPosition != null){
				if(!t.basePositions.contains(this.nextAttackPosition) || this.nextAttackPosition == t.startLocation.getCenter()){
					for(Position pos : new ArrayList<>(t.basePositions)){
						if(pos.getApproxDistance(t.startLocation.getCenter()) > 1200){
							if(this.nextAttackPosition != pos){
								this.nextAttackPosition = pos;
								game.sendText("All your base are belong to us");
								this.canFuckBab = true;
								//System.out.println("Did the thing");
							}
						}
					}
				}
			}
		}
		else {
			//System.out.println("No vulnerable bases");
			this.canFuckBab = false;
		}
	}
	
	
	BotPlayer getBotPlayer(Player ply){
		for(BotPlayer bp : players){
			if(bp.player.equals(ply)){
				return bp;
			}
		}
		
		return null;
	}
	
	
	void Print(String str){
		boolean yes = false;	 // NO
		if(yes){
		System.out.println(str);
		}
	}
	
	
	boolean isGatheringGas(){
		for(Unit unit : getAllOf(self.getRace().getWorker())){
			if(unit.isGatheringGas()){
				return true;
			}
		}
		
		return false;
	}
	
	
	void FuckMeBannerLordIsComingOutIn3HoursIGottaGetThisShitDone(){
		// how much score do we need to use this
		// mostly for deciding when to cast offensive spells
		// https://www.youtube.com/watch?v=NwVwsW0XZvY
			
		techScores.put(TechType.Ensnare, 450);
		techScores.put(TechType.Nuclear_Strike, 300);
		techScores.put(TechType.Spawn_Broodlings, 75);
		techScores.put(TechType.Dark_Swarm, 250);
		techScores.put(TechType.Plague, 450);
		techScores.put(TechType.Psionic_Storm, 300);
		techScores.put(TechType.EMP_Shockwave, 300);
		techScores.put(TechType.Lockdown, 80);
		techScores.put(TechType.Irradiate, 100);
		techScores.put(TechType.Spawn_Broodlings, 100);
		techScores.put(TechType.Mind_Control, 150);
		techScores.put(TechType.Yamato_Gun, 70);
		
	}
	
	ArrayList<FogUnit> getFogUnitsNearby(Position pos, int max){
		ArrayList<FogUnit> ret = new ArrayList<>();
		for(FogUnit f : fogUnits){		
			if(f.pos.getApproxDistance(pos) <= max){
				ret.add(f);	
			}
		}
		
		
		return ret;
	}
	
	boolean isBeingCastedOn(Unit targett, TechType whatt){
		if(tents.isEmpty()){
			return false;
		}
		
		for(Intent i : tents){
			if(i.target.equals(targett)){
				if(i.what == whatt){
					return true;
				}
			}
		}
		
		return false;
	}
	
	boolean isSpellCasterType(UnitType type){
		
		if(type.equals(UnitType.Terran_Medic) || 
		 type.equals(UnitType.Terran_Ghost) || 
		 type.equals(UnitType.Terran_Science_Vessel) ||
		 type.equals(UnitType.Terran_Siege_Tank_Tank_Mode) ||
		 type.equals(UnitType.Terran_Siege_Tank_Siege_Mode) || 
		 type.equals(UnitType.Terran_Battlecruiser) ||
		 type.equals(UnitType.Zerg_Queen) ||
		 type.equals(UnitType.Zerg_Defiler) ||
		 type.equals(UnitType.Terran_Vulture) ||
		 type.equals(UnitType.Protoss_High_Templar) ||
		 type.equals(UnitType.Zerg_Lurker) ||
		 type.equals(UnitType.Protoss_Dark_Archon)){
			return true;
			
		}
		
		return false;
					
			
		}
	
	
	boolean canDefendAgainst(BotPlayer p){
		
		if(p.MilUnits.isEmpty()){
			return true;
		}
		
		if(this.manager == null){
			return myScore >= p.armyScore; // )))))))))))
		}
		
		ArrayList<Unit> defenders = new ArrayList<>(myMilUnits);
		
		for(Unit unit : self.getUnits()){
			
			if(IsMilitrayBuilding(unit) && !defenders.contains(unit) && myBases.size() < 2){
				defenders.add(unit);
			}
					
			if(isSpellCaster(unit) && !defenders.contains(unit)){
				defenders.add(unit);
			}
			
			if(unit.getType().isDetector() && !defenders.contains(unit)){
				defenders.add(unit);
			}
			
		}
		
		ArrayList<UnitType> types = p.getOffensiveUnits();
		
		
		return manager.evaluateBattle2(defenders, types, p.player, p.race);
		
		
	}
	
	
	BotPlayer getBotPlayer(Unit unit){
		if(this.players.isEmpty()){
			return null;
		}
		
		
		for(BotPlayer p : this.players){
			if(p.player.equals(unit.getPlayer())){
				return p;
			}
		}
		
		return null;
	}
	
	
	void updateResourceFocus(){
		int m = 0;
		int g = 0;
		for(BotBase b : new ArrayList<>(this.myBases)){
			for(Unit u : b.Pawns){
				if(u.isGatheringMinerals() || u.isCarryingMinerals()){
					m++;
				}
				
				if(u.isGatheringGas() || u.isCarryingGas()){
					g++;
				}
			}
		}
		
		if(g >= m + 2){
			mineralFocus = true;
		}
	}
	
	
	Unit findAGoodTargetToStomp(Unit me, ArrayList<Unit> targets){
		Unit r = null;
		ArrayList<Unit> goodT = new ArrayList<>();
		ArrayList<Unit> goodAT = new ArrayList<>();
		boolean yes = false;
		
		
		if(me.isFlying() && me.getType().groundWeapon() != WeaponType.None){
			yes = true;
		}
		
		if(targets.isEmpty()){
			return null;
		}
		
		if(yes == false){
			// ground to ground or air to air.
			for(Unit u : targets){
				if(IsMilitrayUnit(u) || IsMilitrayBuilding(u) || isSpellCasterType(u.getType())){
					int range = me.getType().sightRange() + 100;
					
					if(me.getPosition().getApproxDistance(u.getPosition()) < range && me.canTargetUnit(u)){
						if(me.canAttack(u)){
							if(goodT.contains(u)){
							goodT.add(u);
							}
						}
					}
				}
				else {
					continue;
				}
			}
		}
		else {
			// air to ground
			for(Unit u : targets){
				if(IsMilitrayUnit(u) || IsMilitrayBuilding(u) || isSpellCasterType(u.getType())){
					int range = me.getType().sightRange() + 100;
					
					if(!u.canTargetUnit(me)){
						continue;
					}

					if(me.getPosition().getApproxDistance(u.getPosition()) < range){
						if(me.canAttack(u)){
							if(goodAT.contains(u)){
							goodAT.add(u);
							}
						}
					}
				}
				else {
					continue;
				}
			}
			
			for(Unit u : targets){
				if(IsMilitrayUnit(u) || IsMilitrayBuilding(u) || isSpellCasterType(u.getType())){
					int range = me.getType().sightRange() + 100;
					if(me.getPosition().getApproxDistance(u.getPosition()) < range){
							if(me.canAttack(u)){
								if(goodT.contains(u)){
								goodT.add(u);
								}
							}
						}
				}
				else {
					continue;
				}
			}
			
		}
		
		if(yes == false){
			if(!goodT.isEmpty()){
				r = getClosestEnemyArmyUnitFromArray(me.getPosition(), goodT);
			}
		}
		else {
			if(!goodAT.isEmpty()){
				r = getClosestEnemyArmyUnitFromArray(me.getPosition(), goodAT);
			}
			else {
				if(!goodT.isEmpty()){
					r = getClosestEnemyArmyUnitFromArray(me.getPosition(), goodT);
				}
				else {
					return null;
				}
				
			}
		}
		
		return r;
	}
	
	
	
	Unit getClosestEnemyArmyUnitFromArray(Position start, ArrayList<Unit> stuff){
		
		if(stuff.isEmpty()){
			return null;
		}
		
		int l = 0;
		Unit c = null;
		for(Unit unit : stuff){
			int dist = start.getApproxDistance(unit.getPosition());
			if(dist < l || l == 0){
				c=unit;
				l=dist;
			}	
		}
		
		return c;
		

	}
	
	boolean isNearExpandLocation(Position pos){
		
		for(Base base : new ArrayList<>(this.Expands)){
			//System.out.println("(DATA SYS OUT) Dist: " + base.getCenter().getApproxDistance(pos));
			if(base.getCenter().getApproxDistance(pos) < 4){
				
				return true;
			}
		}
		
		
		return false;
	}
	
	ArrayList<Region> getRegionsNearby(Position pos, int max){
		ArrayList<Region> r = new ArrayList<>();
		
		for(Region re : game.getAllRegions()){
			
			if(!re.isAccessible()){
				continue;
			}
			
			int dist = re.getCenter().getApproxDistance(pos);
			
			if(dist <= max){
				r.add(re);
			}
			
		}
		
		if(r.isEmpty()){
			return null;
		}
		else {
			return r;
		}
		
	}

	
}


