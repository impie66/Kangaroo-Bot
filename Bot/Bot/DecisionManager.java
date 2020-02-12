package Bot;
import bwapi.*;

import java.util.ArrayList;
import org.bk.ass.sim.*;
import org.bk.ass.sim.Evaluator;
import org.bk.ass.sim.Simulator;

public class DecisionManager {
	Game game;
	Data myData;
	boolean canWin;
	Evaluator evaluator;
	Simulator simulator;
	BWMirrorAgentFactory factory;
	int myScore;
	int enemyScore;
	int myIncome;	
	int myOutcome;
	static Simulator sim;
	
	DecisionManager(Game game, Data data){
		this.game = game;
		this.myData = data;
		this.canWin = false;
		this.sim = new Simulator.Builder().build();
		this.evaluator = new Evaluator();
		this.factory =  new BWMirrorAgentFactory(game);
		this.myScore = 0;
		this.enemyScore = 0;

	}
	
	boolean shouldAttack(boolean include){
		//ArrayList<Unit> myUnits = new ArrayList<>();
		//ArrayList<Unit> enemyUnits = new ArrayList<>();
		//  simulator = new Simulator();
		//  evaluator = new Evaluator();
		
		//System.out.println("My numbers: " +  myData.myMilUnits.size());
		//System.out.println("Enemy numbers: " +  myData.enemyMilUnits.size());
		
		int mine = 0;
		int enemy = 0;
		
		if(myData.myMilUnits.isEmpty()){
			return false;
		}
		
		if(myData.enemyMilUnits.isEmpty()){
			return true;
		}
			
				
		for(Unit unit : myData.myMilUnits){
			mine = mine + getScoreOf(unit);	
		}
		
		for(Unit unit : myData.enemyMilUnits){
			enemy = enemy + getScoreOf(unit);
		}
		
		if(include){
			for(Unit unit : myData.enemyDBuildings){
				enemy = enemy + getScoreOf(unit);
			}
		}
		
		//System.out.println("Mine: " + mine + " Enemy: " + enemy);
		
		this.myScore = mine;
		this.enemyScore = enemy;
		
		if(mine > (enemy) + (enemy * 0.25)){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	int getScoreOf(Unit unit){
		UnitType auxType = unit.getType();
		return ((auxType.destroyScore() * auxType.maxHitPoints()) / (auxType.maxHitPoints() * 2));
	}
	
	int getScoreOf(UnitType auxType){
		return ((auxType.destroyScore() * auxType.maxHitPoints()) / (auxType.maxHitPoints() * 2));
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
	
	boolean simBattle(ArrayList<Unit> myUnits, ArrayList<UnitType> enemyUnits, int dur, Player ply){
		//Special thanks to Jabbo for telling me how to wipe my ass.
		this.simulator = sim;
		this.factory = new BWMirrorAgentFactory();
		int myScoreBefore = 0;
		int myScoreAfter = 0;
		int enemyScoreBefore = 0;
		int enemyScoreAfter = 0;
		simulator.reset();
		
		if(myUnits.isEmpty()){
			this.canWin = false;
			return false;
		}
				
		if(enemyUnits.isEmpty()){
			this.canWin = true;
			return true;
		}
		
		 for(Unit unit : myUnits){
			 Agent asd = factory.of(unit);
			 simulator.addAgentA(asd);
			 myScoreBefore = myScoreBefore + asd.getHealth() + asd.getShields(); 
		 }
		 
	 
		 for(UnitType unit : enemyUnits){
			 Agent asd = FogAgent(unit, ply);
			 simulator.addAgentB(asd);
			 enemyScoreBefore = enemyScoreBefore + asd.getHealth() + asd.getShields();
		 }
		 
		 
		//System.out.println("My Size Before: " + simulator.getAgentsA().size());
		//System.out.println("Enemy Size Before: " + simulator.getAgentsB().size());
		//ArrayList<Agent> p1Before = new ArrayList<>(simulator.getAgentsA());
		//ArrayList<Agent> p2Before = new ArrayList<>(simulator.getAgentsB());
		
		simulator.simulate(dur);
		
		//ArrayList<Agent> p1After = new ArrayList<>(simulator.getAgentsA());
		//ArrayList<Agent> p2After = new ArrayList<>(simulator.getAgentsB());
		
		for(Agent unit : simulator.getAgentsA()){
			myScoreAfter = myScoreAfter + unit.getHealth() + unit.getShields();
		}
		
		for(Agent unit : simulator.getAgentsB()){
			enemyScoreAfter = enemyScoreAfter + unit.getHealth() + unit.getShields();
		}
		
		//System.out.println("My Size After: " + simulator.getAgentsA().size());
		//System.out.println("Enemy Size After: " + simulator.getAgentsB().size());
		
		//System.out.println("P1 Health Before: " + myScoreBefore);
		//System.out.println("P2 Health Before: " + enemyScoreBefore);
		//System.out.println("P1 Health After: " + myScoreAfter);
		//System.out.println("P2 Health after: " + enemyScoreAfter);
		int P1 = Math.abs(myScoreBefore - myScoreAfter);
		int P2 = Math.abs(enemyScoreBefore - enemyScoreAfter);
		//System.out.println("P1 " + P1);
		//System.out.println("P2 " + P2);
		
		if(myScoreAfter >= myScoreBefore){
			if(myScoreAfter >= enemyScoreAfter){
				this.canWin = true;
				return true;
			}
		}
		
		if(myScoreAfter >= enemyScoreAfter){
			this.canWin = true;	
			return true;
		}
		else {
			this.canWin = false;
			return false;
		}
		
		
	}
	
	boolean evaluateBattle(ArrayList<Unit> myUnits, ArrayList<Unit> enemyUnits, double min, boolean setScore){
		this.evaluator = new Evaluator();
		
		ArrayList<Agent>myA = new ArrayList<>();
		ArrayList<Agent>enemyA = new ArrayList<>();
					
		 for(Unit unit : myUnits){
			 Agent asd = factory.of(unit);
			 asd.setKiter(false);
			 if(unit.getType().equals(UnitType.Protoss_Carrier)){
				 Agent yess = factory.of(UnitType.Protoss_Carrier);
				 ArrayList<Agent> childs = new ArrayList<>();
				 for(int i = 0; i < unit.getInterceptorCount(); i++){
					 Agent yes = factory.of(UnitType.Protoss_Interceptor);
					 if(childs.contains(yes)){
						 childs.add(yes);
					 }
					 myA.add(yes);
				 }
				 
				 yess.setInterceptors(childs);
				 myA.add(yess);
				 
			 }
			 
			 if(unit.getType().equals(UnitType.Terran_Vulture)){
				 
				 if(unit.getSpiderMineCount() > 0){
					 Agent yes = factory.of(UnitType.Terran_Vulture_Spider_Mine);
					 myA.add(yes);
				 }
				 
			 }
			 
			 		
			 myA.add(asd);	 
		 }
		 
		 for(Unit unit : enemyUnits){
			 Agent asd = factory.of(unit);
			 
			 if(unit.getType().equals(UnitType.Protoss_Carrier)){
				 Agent yess = factory.of(UnitType.Protoss_Carrier);
				 ArrayList<Agent> childs = new ArrayList<>();
				 for(int i = 0; i < unit.getInterceptorCount(); i++){
					 Agent yes = factory.of(UnitType.Protoss_Interceptor);
					 if(childs.contains(yes)){
						 childs.add(yes);
					 }
					 myA.add(yes);
				 }
				 
				 yess.setInterceptors(childs);
				 myA.add(yess);
				 
			 }
			 
			 if(unit.getType().equals(UnitType.Terran_Vulture)){
				 if(unit.getSpiderMineCount() > 0){
					 Agent yes = factory.of(UnitType.Terran_Vulture_Spider_Mine);
					 myA.add(yes);
				 }
			 }
			 
			 if(unit.isCloaked() || unit.getType().equals(UnitType.Zerg_Lurker)){
				 if(myData.self.getRace().equals(Race.Terran)){
					 if(myData.HasScansAvailable()){
						 asd.setDetected(true);
					 }
				 }
			 }
			 
			 enemyA.add(asd);
		 }	
		 
		 
		double score = evaluator.evaluate(myA, enemyA);
		if(setScore == true){
			for(Unit unit : myUnits){
				if(game.self().getUnits().contains(unit)){
				// ^^ apparently better then unit.exists();
				myData.newUnitScore(unit, score);
				}
			}
		}

		
		//System.out.println("Local Score: " + score);
		if(score >= min){
			return true;
		}
		else {
			return false;
		}
		
		
		// fin
		}
	
	
	boolean evaluateBattle2(ArrayList<Unit> myUnits, ArrayList<UnitType> type, Player target){
		this.evaluator = new Evaluator();
		ArrayList<Agent>myA = new ArrayList<>();
		ArrayList<Agent>enemyA = new ArrayList<>();
		
		if(myUnits.isEmpty()){
			return false;
		}
		
		if(type.isEmpty()){
			return true;
		}
		
					
		 for(Unit unit : myUnits){
			 Agent asd = factory.of(unit);
			 myA.add(asd);
		 }
		 
		 for(UnitType unit : type){
			 Agent asd = FogAgent(unit, target);
			 enemyA.add(asd);
		 }
		 
		 
		double score = evaluator.evaluate(myA, enemyA);
		if(score >= 0.75){
			return true;
		}
		else {
			return false;
		}
		
	}
		
	Agent FogAgent(UnitType unit, Player ply){
		 Agent asd = factory.of(unit);
		 asd.setMaxHealth(unit.maxHitPoints());
		 asd.setSpeed((float) unit.topSpeed());
		 asd.setMaxShields(unit.maxShields());	 
		 Weapon Gwep = new Weapon();
		 Weapon Awep = new Weapon();
		 WeaponType ground = unit.groundWeapon();
		 Gwep.setDamage(ground.damageAmount());
		 Gwep.setHits(unit.maxGroundHits());
		 Gwep.setInnerSplashRadius(ground.innerSplashRadius());
		 Gwep.setMaxRange(ground.maxRange());
		 Gwep.setMedianSplashRadius(ground.medianSplashRadius());
		 Gwep.setMinRange(ground.minRange());
		 Gwep.setOuterSplashRadius(ground.outerSplashRadius());
		 WeaponType air = unit.airWeapon();
		 Awep.setDamage(air.damageAmount());
		 Awep.setHits(1);
		 Awep.setInnerSplashRadius(air.innerSplashRadius());
		 Awep.setMaxRange(air.maxRange());
		 Awep.setMedianSplashRadius(air.medianSplashRadius());
		 Awep.setMinRange(air.minRange());
		 Awep.setOuterSplashRadius(air.outerSplashRadius());
		 asd.setOrganic(unit.isOrganic());
		 if(unit.isFlyer()){
			 asd.setFlyer(true);
		 }
		 else {
			 asd.setFlyer(false);
		 }
		 
		 if(unit.equals(UnitType.Zerg_Scourge) || unit.equals(UnitType.Terran_Vulture_Spider_Mine)){
			 asd.setSuicider(true);
		 }
		 else {
			 asd.setSuicider(false);
		 }
		 
		 if(unit.equals(UnitType.Zerg_Zergling) || 
		unit.equals(UnitType.Zerg_Ultralisk) ||
		unit.equals(UnitType.Protoss_Zealot) ||
		unit.equals(UnitType.Protoss_Dark_Templar) ||
		unit.equals(UnitType.Terran_Firebat) ){
		asd.setMelee(true);
			 
		 }
		 else {
			 asd.setMelee(false);
		 }
		 
		 Race race = myData.enemyRace;
		 
		 
		 if(race.equals(Race.Zerg)){
			 asd.setArmor(ply.getUpgradeLevel(UpgradeType.Zerg_Carapace));
			 asd.setRegeneratesHealth(true);
			 if(unit.equals(UnitType.Zerg_Lurker)){
				 asd.setBurrowedAttacker(true);
				 
			 }
	 
		 }
		 
		 else {
			 asd.setBurrowedAttacker(false);
		 }
		 
		 
		 if(race.equals(Race.Terran)){
			 if(!unit.isMechanical()){
				 // if bio
				 asd.setArmor(ply.getUpgradeLevel(UpgradeType.Terran_Infantry_Armor));
				 if(ply.hasResearched(TechType.Stim_Packs)){
					 if(unit.equals(UnitType.Terran_Firebat) || unit.equals(UnitType.Terran_Marine)){
					 asd.setCanStim(true);
					 }
				 }
				 else {
					 asd.setCanStim(false);
				 }

			 }
			 else {
				 // if vic
				 asd.setArmor(ply.getUpgradeLevel(UpgradeType.Terran_Vehicle_Plating)); 
				 asd.setMechanic(true);
			 }
			 
			 if(unit.equals(UnitType.Terran_Medic)){
				 asd.setHealer(true);
			 }
			 
			 
		 }
		 
		 if(race.equals(Race.Protoss)){
			 if(!unit.isFlyer()){
				 asd.setArmor(ply.getUpgradeLevel(UpgradeType.Protoss_Ground_Armor));
			 }
			 else {
				asd.setArmor(ply.getUpgradeLevel(UpgradeType.Protoss_Air_Armor));
			 }
			 asd.setShieldUpgrades(ply.getUpgradeLevel(UpgradeType.Protoss_Plasma_Shields));
			 			 
		 }
		 
		 

		 return asd;
	}
	

	

	
		
}
