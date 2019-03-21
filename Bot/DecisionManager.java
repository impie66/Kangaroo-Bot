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
	
	boolean simBattle(ArrayList<Unit> myUnits, ArrayList<Unit> enemyUnits){
		//Special thanks to Jabbo for telling me how to wipe my ass.
		// Heheh, get it? ASS?? hahahah XDDDDDDDDDDDDDDDDDDDDDDDDD
		// God i need to stop drinking
		this.simulator = new Simulator();
		this.factory = new BWMirrorAgentFactory();
		int i = 0;
		int ii = 0;
		simulator.reset();
		
		 for(Unit unit : myUnits){
			 Agent asd = factory.of(unit);
			 simulator.addAgentA(asd);
			 i = i + unit.getHitPoints();
			 
		 }
		 
		 for(Unit unit : enemyUnits){
			 Agent asd = factory.of(unit);
			 simulator.addAgentA(asd);
			 ii = ii + unit.getHitPoints();
		 }
		 
		int preSimMyScore = i;
		int preSimEnemyScore = ii;
		// fuck im too lazy to type that whole word

		simulator.simulate(300);
		
		int iii = 0;
		int iiii = 0;
		
		for(Agent unit : simulator.getAgentsA()){
			iii = iii + unit.getHealth();
		}
		
		for(Agent unit : simulator.getAgentsB()){
			iiii = iiii + unit.getHealth();
		}
		
		int p1D = iii - iiii;
		int p2D = i - ii;
		
		if(p1D > p2D){
			return true;
		}
		else {
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
		if(score >= 0.35){
			return true;
		}
		else {
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
		
		 for(Unit unit : myUnits){
			 Agent asd = factory.of(unit);
			 myA.add(asd);
			 
		 }
		 
		 for(UnitType unit : type){
			 Agent asd = factory.of(unit);
			 enemyA.add(asd);
		 }
		 
		//System.out.println("myUnits Size " + myUnits.size());
		//System.out.println("Type Size: " + type.size());
		double score = evaluator.evaluate(myA, enemyA);
		//System.out.println("Global Score: " + score);
		if(score >= 0.55){
		this.canWin = true;
		return true;
		}
		else {
		this.canWin = false;
		return false;
		}
	}
		

		
}
