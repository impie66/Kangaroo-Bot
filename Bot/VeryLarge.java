package Bot;

import java.util.ArrayList;


// VERY LARGE
// GRABS UNITS TO SIM IN NEARBY AREA


import bwapi.*;

public class VeryLarge {
	Position mystart;
	Position enemystart;
	Util tillwillbing;
	Data data;
	Pair<Boolean,Boolean> sidesHitAir;
	Pair<Boolean,Boolean> hasSpecialForces;
	Pair<Boolean,Boolean> detection;
	ArrayList<Unit> p1Units;
	ArrayList<Unit> p1AirUnits;
	ArrayList<Unit> p2Units;
	ArrayList<Unit> p2AirUnits;
	ArrayList<Unit> groundForces;
	ArrayList<Unit> enemyGroundForces;
	ArrayList<Unit> airForces;
	ArrayList<Unit> enemyAirForces;
	ArrayList<Unit> specialForces; // Cloaked or burrowed units
	ArrayList<Unit> enemySpecialForces; // ^^ what he said
	ArrayList<FogUnit> fogUnits;
	ArrayList<FogUnit> fogAirUnits;
	Pair<Integer,Integer> fapScores;
	ArrayList<Unit> combatReady;
	Pair<ArrayList<Unit>, ArrayList<FogUnit>> toSim;
	int radius;
	int bonusunits;
	int bonusunits2;
	boolean canFight;
	int frameStart;
	double simScore;
	boolean nearChokePoint;
	double airSimScore;
	boolean AirStay; // AIR CAN STAY. SET LATER AFTERS SIM
	String simType;
	Position P2A = Position.None; // no p1 just as let.

	VeryLarge(Position beginA, Position beginB, Util til, Data dataa, ArrayList<FogUnit> fogUnit, int rad) {
		// https://www.youtube.com/watch?v=b9Wlei7ZV1Y
		// https://www.youtube.com/watch?v=_GMQLjzVGfw
		// alle warter auf das licht
		this.tillwillbing = til;
		this.mystart = beginA;
		this.enemystart = beginB;
		this.data = dataa;
		this.p1Units = new ArrayList<>();
		this.p2Units = new ArrayList<>();
		this.groundForces = new ArrayList<>();
		this.enemyGroundForces = new ArrayList<>();
		this.airForces = new ArrayList<>();
		this.enemyAirForces = new ArrayList<>();
		this.sidesHitAir = new Pair<>(false,false);
		this.hasSpecialForces = new Pair<>(false,false);
		this.specialForces = new ArrayList<>();
		this.enemySpecialForces = new ArrayList<>();
		this.detection = new Pair<>(false,false);
		this.fogUnits = fogUnit;
		this.fogAirUnits = new ArrayList<>();
		this.fapScores = new Pair<>(0,0);
		this.combatReady = new ArrayList<>();	 
		this.radius = rad;
		this.bonusunits = 0;
		this.bonusunits2 = 0;
		this.canFight = true;
		this.frameStart = this.data.game.getFrameCount();
		this.simScore = 0.50;
		this.toSim = new Pair<>(new ArrayList<Unit>(), new ArrayList<FogUnit>());
		this.nearChokePoint = true;
		this.airSimScore = 0.50;
		this.AirStay = false;
		this.simType = "Evaluator";
		DoTheThingThatGivesManyNumberOfUnitsToAThingThatContainsTheThingThatContainsManyUnitsOnBothSidesAlsoBecauseYes();
		
	}
	
	
	
	//TODO work on spellcaster movement
	// dont let them ball up in the base while your army rapes everything else
	
	void DoTheThingThatGivesManyNumberOfUnitsToAThingThatContainsTheThingThatContainsManyUnitsOnBothSidesAlsoBecauseYes(){
		int p2AX = 0;
		int p2AY = 0;
		int max = 0;
		if(data.isNearEnemyCP(this.mystart)){
			this.p1Units = data.GetMyUnitsNearby(this.mystart, this.radius + 100, true);
			this.nearChokePoint = true;
		}
		else{
			this.p1Units = tillwillbing.getCombatUnits(this.mystart, this.radius + 100);
			this.nearChokePoint = false;;
		}
		
		ArrayList<Unit> bp1 = data.GetMyUnitsNearby(this.enemystart, 300, true);
		
		if(bp1 != null){
			for(Unit unit : bp1){
				if(!this.p1Units.contains(unit)){
					this.p1Units.add(unit);
				}
			}
		}
		
		this.p2Units = data.GetEnemyUnitsNearby(this.enemystart, this.radius, true);
		//System.out.println("BIG P2 DEBUG: " + this.p2Units.size());
		
		for(Unit unit : data.GetEnemyUnitsNearby(this.mystart, 350, true)){
			if(!this.p2Units.contains(unit)){
				this.p2Units.add(unit);
				bonusunits++;
			}
		}
			
		for(Unit unit : p1Units){
			this.fapScores = new Pair<>(this.fapScores.getFirst() + data.getScoreOf(unit),0);
			
						
			if(!unit.getType().isFlyer()){
				groundForces.add(unit);
			}
			else {
				airForces.add(unit);
			}
			
			if(unit.isCloaked() || unit.isBurrowed()){
				specialForces.add(unit);
				if(this.hasSpecialForces.getFirst() == false){
					this.hasSpecialForces = new Pair<>(true,false);
				}
			}
			
			if(unit.getType().isDetector() && this.detection.getFirst() == false){
				this.detection = new Pair<>(true,false);
			}
			
			if(!unit.getType().airWeapon().equals(WeaponType.None) && this.sidesHitAir.getFirst() == false){
				this.sidesHitAir = new Pair<>(true,false);
			}

			
			
		}
		
		for(Unit unit : p2Units){
			
			if(unit.getPlayer().equals(this.data.game.self()) || unit.getPlayer().isNeutral()){
				System.out.println("WHAT IS " + unit.getType().toString() + " DOING IN THE SIM, IT'S ALLY OR NEUTRAL");
				continue;
			}
			
			
			this.fapScores = new Pair<>(this.fapScores.getFirst(),this.fapScores.getSecond() + data.getScoreOf(unit));
			if(!unit.getType().isFlyer()){
				enemyGroundForces.add(unit);
			}
			else {
				enemyAirForces.add(unit);
			}
			
			if(unit.isCloaked() || unit.isBurrowed()){
				enemySpecialForces.add(unit);
				if(this.hasSpecialForces.getSecond() == false){
					this.hasSpecialForces = new Pair<>(this.hasSpecialForces.getFirst(),true);
				}
			}
			
			if(unit.getType().isDetector() && this.detection.getSecond() == false){
				this.detection = new Pair<>(this.detection.getFirst(),true);
			}
			
			if(!unit.getType().airWeapon().equals(WeaponType.None) && this.sidesHitAir.getSecond() == false){
				this.sidesHitAir = new Pair<>(this.detection.getFirst(),true);
			}
			
			p2AX = p2AX + unit.getX();
			p2AY = p2AY + unit.getY();
			max++;
			
			
		}
		
		for(FogUnit fog : new ArrayList<>(fogUnits)){
			Unit unit = fog.unit;
			if(p2Units.contains(unit)){
				// check if the unit is already simmed
				fogUnits.remove(fog);
				continue;
				// dont count the units always calcuated.
			}
			else {
				bonusunits2++;
			}
			
			if(!unit.getType().isFlyer()){
				enemyGroundForces.add(unit);
			}
			else {
				enemyAirForces.add(unit);
			}
			
			if(unit.isCloaked() || unit.isBurrowed()){
				enemySpecialForces.add(unit);
				if(this.hasSpecialForces.getSecond() == false){
					this.hasSpecialForces = new Pair<>(this.hasSpecialForces.getFirst(),true);
				}
			}
			
			if(unit.getType().isDetector() && this.detection.getSecond() == false){
				this.detection = new Pair<>(this.detection.getFirst(),true);
			}
			
			if(!unit.getType().airWeapon().equals(WeaponType.None) && this.sidesHitAir.getSecond() == false){
				this.sidesHitAir = new Pair<>(this.detection.getFirst(),true);
			}
			
			p2AX = p2AX + fog.pos.getX();
			p2AY = p2AY + fog.pos.getY();
			max++;
			
		
		}
		
		if(max == 0){
			max = 1; // FUCKING DIVIDE BY 0
		}
		
		P2A = new Position(Math.round(p2AX / max), Math.round(p2AY / max));
		
		this.toSim.setLeft(p2Units);
		this.toSim.setRight(fogUnits);
		
		this.combatReady = tillwillbing.combatReadyUnits(this.p1Units, this.mystart);
		setAirUnits();

	}
	
	void setAirUnits(){
			
			
		this.p1AirUnits = new ArrayList<>(this.p1Units);
		this.p2AirUnits = new ArrayList<>(this.p2Units);
		this.fogAirUnits = new ArrayList<>(this.fogUnits);

		
		for(Unit unit : new ArrayList<>(this.p1AirUnits)){
			if(!unit.isFlying()){
			this.p1AirUnits.remove(unit);
			}
			
		}
		
		
		for(Unit unit : new ArrayList<>(this.p2AirUnits)){
			if(unit.getType().airWeapon() == WeaponType.None){
			this.p2AirUnits.remove(unit);
			}
			
		}
		
		for(FogUnit unit : new ArrayList<>(this.fogAirUnits)){
			if(unit.type.airWeapon() == WeaponType.None){
			this.fogAirUnits.remove(unit);
			}
			
		}
		

	}
	
	
	double getSimScore(){
		return this.simScore;
	}
	
	
	
}
