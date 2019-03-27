package Bot;
import bwapi.*;
import bwem.*;

import java.util.ArrayList;

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
		// Heheh, get it? ASS?? hahahah XDDDDDDDDDDDDDDDDDDDDDDDDD
		// God i need to stop drinking
		this.simulator = new Simulator();
		this.factory = new BWMirrorAgentFactory();
		int myScoreBefore = 0;
		int myScoreAfter = 0;
		int enemyScoreBefore = 0;
		int enemyScoreAfter = 0;
		int myScore;
		int enemyScore;
		simulator.reset();
		
		if(myUnits.isEmpty()){
			this.canWin = false;
			return false;
		}
		
		if(myData.myScore >= myData.enemyScore * 2){
			System.out.println("Global Attack via score");
			this.canWin = true;
			return true;
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
			 Agent asd = factory.of(unit);
			 simulator.addAgentB(asd);
			 enemyScoreBefore = enemyScoreBefore + asd.getHealth() + asd.getShields();
		 }
		 
		System.out.println("My Size Before: " + simulator.getAgentsA().size());
		System.out.println("Enemy Size Before: " + simulator.getAgentsB().size());
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

		int P1 = myScoreBefore - myScoreAfter;
		int P2 = enemyScoreBefore - enemyScoreAfter;
		System.out.println("P1 " + P1);
		System.out.println("P2 " + P2);
		
		if(P1 == P2){
			if(this.globalEvaluate(myUnits, enemyUnits) == true){
				this.canWin = true;
				return true;
			}
		}
		
		if(myScoreBefore == myScoreAfter && enemyScoreBefore == enemyScoreAfter){
			if(myScoreBefore >= enemyScoreBefore){
				if(this.globalEvaluate(myUnits, enemyUnits) == true){
					this.canWin = true;
					return true;
				}
			}
		}
		
		if(P1 >= P2){
			this.canWin = true;
			return true;
		}
		else {
			this.canWin = false;
			return false;
		}
		
		
	}
	
	boolean evaluateBattle(ArrayList<Unit> myUnits, ArrayList<Unit> enemyUnits){
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
		//System.out.println("Local Score: " + score);
		if(score >= 0.50){
			this.canWin = true;
			return true;
		}
		else {
		this.canWin = false;
		return false;
		}
		
		}
	
	boolean globalEvaluate(ArrayList<Unit> myUnits, ArrayList<UnitType> type){
		this.evaluator = new Evaluator();
		ArrayList<Agent>myA = new ArrayList<>();
		ArrayList<Agent>enemyA = new ArrayList<>();
		
		if(myUnits.isEmpty()){
			return false;
		}
		
		System.out.println("My Score: " + myData.myScore);
		System.out.println("Enemy Score: " + myData.enemyScore);
		
		if(myData.myScore >= myData.enemyScore * 2){
			System.out.println("Global Attack via score");
		}
					
		 for(Unit unit : myUnits){
			 Agent asd = factory.of(unit);
			 myA.add(asd);
			 
		 }
		 
		 for(UnitType unit : type){
			 Agent asd = factory.of(unit);
			 enemyA.add(asd);
		 }
		 
		 for(UnitType unit : myData.enemyDTypes){
			 Agent asd = factory.of(unit);
			 enemyA.add(asd);
		 }
		 
		//System.out.println("myUnits Size " + myUnits.size());
		//System.out.println("Type Size: " + type.size());
		double score = evaluator.evaluate(myA, enemyA);
		System.out.println("Global Score: " + score);
		if(score >= 0.35){
		this.canWin = true;
		return true;
		}
		else {
		this.canWin = false;
		return false;
		}
	}
		

		
}
