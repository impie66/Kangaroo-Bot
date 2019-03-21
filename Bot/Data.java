package Bot;
import bwem.*;
import java.util.ArrayList;

import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
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
	Position nextAttackPosition;
	ArrayList<UnitType> eggs;
	ArrayList<UnitType> enemyTypes;
	ArrayList<UnitType> enemyDTypes;
	
	public Data(Game gaem, BWEM b, Base myBasee){
		this.game = gaem;
		this.bewb = b;
		this.self = game.self();
		this.myChokes = new ArrayList<ChokePoint>();
		this.enemyChokes = new ArrayList<ChokePoint>();
		this.myBase = myBasee;
		this.startLocations = new ArrayList<Base>();
		this.enemyMilUnits = new ArrayList<>();
		this.enemyBuildings = new ArrayList<Unit>();
		this.enemyBases = new ArrayList<Base>();
		this.myMilUnits = new ArrayList<>();
		this.nextAttackPosition = null;
		this.enemyDBuildings = new ArrayList<>();
		this.eggs = new ArrayList<>();
		this.enemyTypes = new ArrayList<UnitType>();
		this.enemyDTypes = new ArrayList<UnitType>();
		ArrayList<Base> Expands = new ArrayList<Base>();
		DoTheThing();
	}
	
	// SMILE
	// SWEET
	// SISTER
	// SADISTIC
	// SUPRISE
	// SERVICE
	// SCHWERER PANZERSPÄHWAGEN SIEBEN KOMMA FÜNF ZENTIMETER SONDERKRAFTFAHRZEUG ZWEIHUNDERTVIERUNDDREISSIG / VIER PANZERABWEHRKANONENWAGEN

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
			dist = dist + 500;
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
		
		if(this.nextAttackPosition == null && this.enemyBuildings != null){
			if(!this.enemyBuildings.isEmpty()){
			this.nextAttackPosition = this.enemyBuildings.get(0).getPosition();
			}
		}
				

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
			System.out.println("Enemy Building Discovered: " + unit.getType().toString());
		}
	}
	
	void newEnemyBase(Base unit){
		if(!this.enemyBases.contains(unit)){
			this.enemyBases.add(unit);
			if(this.Expands.contains(unit)){
				this.Expands.remove(unit);
			}
		}
	}
	
	void newMilUnit(Unit unit){
		if(unit.getPlayer().equals(self)){
			if(!this.myMilUnits.contains(unit)){
				this.myMilUnits.add(unit);
				//System.out.println("My Unit: " + unit.getType().toString());
			}
		}
		
		if(game.enemies().contains(unit.getPlayer())){
			if(!this.enemyMilUnits.contains(unit)){
				this.enemyMilUnits.add(unit);
				this.enemyTypes.add(unit.getType());
				//System.out.println("Enemy Unit: " + unit.getType().toString());
			}
		}
	}
	
	void unitDeath(Unit unit){
		if(unit.getPlayer().equals(self)){
			if(this.myMilUnits.contains(unit)){
				this.myMilUnits.remove(unit);
			}
			
			
		}
		
		if(game.enemies().contains(unit.getPlayer())){
			if(this.enemyMilUnits.contains(unit)){
				this.enemyMilUnits.remove(unit);
				this.enemyTypes.remove(unit.getType());
				//System.out.println("Enemy Unit Death: " + unit.getType().toString());
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
			if (targets.getPlayer().isEnemy(self) == true && IsMilitrayUnit(targets) == true && Mine.contains(targets) == false) {
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
		System.out.println("Morphed: " + i);
		return i;
	}

	
	
}
