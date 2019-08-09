package Bot;
import bwapi.*;

import java.util.ArrayList;

import javax.management.monitor.GaugeMonitor;

import org.bk.ass.*;

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
	
	DecisionManager(Game game, Data data){
		this.game = game;
		this.myData = data;
		this.canWin = false;
		this.simulator = new Simulator();
		this.evaluator = new Evaluator();
		this.factory =  new BWMirrorAgentFactory();
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
	
	boolean simBattle(ArrayList<Unit> myUnits, ArrayList<UnitType> enemyUnits){
		//Special thanks to Jabbo for telling me how to wipe my ass.
		this.simulator = new Simulator();
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
			 Agent asd = FogAgent(unit, game.enemy());
			 simulator.addAgentB(asd);
			 enemyScoreBefore = enemyScoreBefore + asd.getHealth() + asd.getShields();
		 }
		 
		//System.out.println("My Size Before: " + simulator.getAgentsA().size());
		//System.out.println("Enemy Size Before: " + simulator.getAgentsB().size());
		//ArrayList<Agent> p1Before = new ArrayList<>(simulator.getAgentsA());
		//ArrayList<Agent> p2Before = new ArrayList<>(simulator.getAgentsB());
		
		simulator.simulate(200);
		
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
	
	boolean evaluateBattle(ArrayList<Unit> myUnits, ArrayList<Unit> enemyUnits, double min){
		this.evaluator = new Evaluator();
		
		ArrayList<Agent>myA = new ArrayList<>();
		ArrayList<Agent>enemyA = new ArrayList<>();
					
		 for(Unit unit : myUnits){
			 Agent asd = factory.of(unit);
			 myA.add(asd);	 
		 }
		 
		 for(Unit unit : enemyUnits){
			 Agent asd = factory.of(unit);
			 enemyA.add(asd);
		 }	
		 
		 
		double score = evaluator.evaluate(myA, enemyA);
		
		for(Unit unit : myUnits){
			if(game.self().getUnits().contains(unit)){
			// ^^ apparently better then unit.exists();
			myData.newUnitScore(unit, score);
			}
		}
		
		//System.out.println("Local Score: " + score);
		if(score >= min){
			this.canWin = true;
			return true;
		}
		else {
			this.canWin = false;
			return false;
		}
		
		
		// fin
		}
	
	
	boolean globalEvaluate(ArrayList<Unit> myUnits, ArrayList<UnitType> type){
		this.evaluator = new Evaluator();
		ArrayList<Agent>myA = new ArrayList<>();
		ArrayList<Agent>enemyA = new ArrayList<>();
		
		if(myUnits.isEmpty()){
			return false;
		}
		
		
		if(type.isEmpty()){
			this.canWin = true;
			return true;
		}
		
		//System.out.println("My Score: " + myData.myScore);
		//System.out.println("Enemy Score: " + myData.enemyScore);
					
		 for(Unit unit : myUnits){
			 Agent asd = factory.of(unit);
			 myA.add(asd);
			 
		 }
		 
		 for(UnitType unit : type){
			 Agent asd = factory.of(unit);
			 enemyA.add(asd);
		 }
		 
		 for(UnitType unit : myData.enemyDTypes){
			 Agent asd = FogAgent(unit, game.enemy());
			 enemyA.add(asd);
		 }
		 
		//System.out.println("myUnits Size " + myUnits.size());
		//System.out.println("Type Size: " + type.size());
		 
		double score = evaluator.evaluate(myA, enemyA);
		System.out.println("Global Score: " + score);
		if(score >= 0.25){
			// if we can win
			this.canWin = true;
			return true;
		}
		else {
			// if we can't
			this.canWin = false;
			return false;
		}
		
	}
		
	Agent FogAgent(UnitType unit, Player ply){
		 Agent asd = factory.of(unit);
		 asd.setHealth(unit.maxHitPoints());
		 asd.setMaxHealth(unit.maxHitPoints());
		 asd.setSpeed((float) unit.topSpeed());
		 asd.setMaxShields(unit.maxShields());
		 if(unit.isFlyer()){
			 asd.setFlyer(true);
		 }
		 else {
			 asd.setFlyer(false);
		 }
		 
		 if(unit.equals(UnitType.Zerg_Scourge)){
			 asd.setSuicider(true);
		 }
		 else {
			 asd.setSuicider(false);
		 }

		 Race race = myData.enemyRace;
		 
		 
		 if(race.equals(Race.Zerg)){
			 asd.setArmor(ply.getUpgradeLevel(UpgradeType.Zerg_Carapace));
			 asd.setRegeneratesHealth(true);
			 if(unit.equals(UnitType.Zerg_Lurker)){
				 asd.setBurrowedAttacker(true);
				 
			 }
			 else {
				 asd.setBurrowedAttacker(false);
			 }
			 
		 }
		 
		 if(race.equals(Race.Terran)){
			 if(!unit.isMechanical()){
				 // if bio
				 asd.setArmor(ply.getUpgradeLevel(UpgradeType.Terran_Infantry_Armor));
				 if(ply.hasResearched(TechType.Stim_Packs)){
					 asd.setCanStim(true);
				 }
				 else {
					 asd.setCanStim(false);
				 }

			 }
			 else {
				 // if vic
				 asd.setArmor(ply.getUpgradeLevel(UpgradeType.Terran_Vehicle_Plating)); 
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
