package Bot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.swing.text.Utilities;

import org.bk.ass.path.Jps;
import org.bk.ass.sim.*;

import bwapi.BWClient;
import bwapi.BWEventListener;
import bwapi.Bullet;
import bwapi.BulletType;
import bwapi.Color;
import bwapi.Flag;
import bwapi.Game;
import bwapi.Order;
import bwapi.Pair;
import bwapi.Player;
import bwapi.PlayerType;
import bwapi.Position;
import bwapi.Race;
import bwapi.TechType;
import bwapi.Text;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitFilter;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwapi.WalkPosition;
import bwapi.WeaponType;
import bwem.BWEM;
import bwem.Base;
import bwem.ChokePoint;
import bwem.Geyser;
import bwem.Mineral;
import bwta.Region;

public class Bot implements BWEventListener {
	BWClient bwClient;
	Game game;
	Player self;
	ArrayList<BotBase> myBases = new ArrayList<BotBase>();
	ArrayList<UnitType> UQ = new ArrayList<UnitType>();
	ArrayList<UnitType> PUQ = new ArrayList<UnitType>();
	ArrayList<Unit> Production = new ArrayList<Unit>();
	ArrayList<BuildingQueue> BQ = new ArrayList<BuildingQueue>();
	ArrayList<pBuilding>pBuildings = new ArrayList<pBuilding>();
	HashMap<Integer, UnitType> constructors = new HashMap<>();
	HashMap<Integer, BotBase> workers = new HashMap<>();
	HashMap<Unit, Integer> simmedUnits = new HashMap<>();
	ArrayList<Base> startLocations = new ArrayList<>();
	ArrayList<Base> Expands;
	ArrayList<ChokePoint> myChokes;
	ArrayList<Squad> Squads = new ArrayList<Squad>();
	ArrayList<Builder> builders = new ArrayList<Builder>();
	ArrayList<UnitType> placements = new ArrayList<>();
	ArrayList<Unit> retreaters = new ArrayList<>();
	ArrayList<BotTech> stuffQueue = new ArrayList<>();
	ArrayList<BotPlayer> players = new ArrayList<>();
	ArrayList<Spellcaster> casters = new ArrayList<>();
	ArrayList<Repairer> repairs = new ArrayList<>();
	ArrayList<Unit> blockMineral = new ArrayList<>();
	ArrayList<UnitType> techGoals = new ArrayList<>();
	ArrayList<FogUnit> fogUnits = new ArrayList<>();
	HashMap<Unit, Integer> repairLastCalled = new HashMap<>();
	Position globalRetreat;
	DecisionManager manager;
	boolean drawMoodYes = false;
	boolean drawHealthInfo = true;
	int GlobalState;
	Unit scouter;
	int cCheck;
	Data myData;
	Util util;
	int sMins = 0;
	int sGas = 0;
	int winCheck = 0;
	int microCheck = 0;
	int simCheck = 0;
	Base myStartLocation;
	int baseCheck = 0;
	int improveCheck = 0;
	int defenceCheck = 0;
	int supplyCheck = 0;
	int UICCheck = 0;
	int shittalkCheck = 0;
	int CSWins = 0;
	int scoutCheck;
	int fogUpdate = 0;
	int overLordsMorphing = 0;
	static BWEM bewb;
	Strategy strat;
	boolean isFFA;
	String currentBitch;
	BotPlayer currentTarget;
	String newDumbName = "";
	TilePosition defencePos = null;
	String newDumbTag = "";
	String newDumbTag2 = " ";
	boolean canFreeBuild = false;
	static boolean cheats = false;
	int openerCheck;
	int PStats;
	Jps jps;
	ArrayList<UnitType> buildMainGoal;
	boolean raceInd = false;
	boolean moveDefencePos = false;
	int baseRape = 0;
	int GFR = 8500;
	boolean hasReacted = false;
	boolean hasStoppedDoingTheProxy = false;
	UnitType Doggo = UnitType.Zerg_Zergling;
	UnitType ShootyBoy = UnitType.Terran_Marine;
	UnitType FuckingBroken = UnitType.Protoss_Zealot;
	ArrayList<Lovers> lovers = new ArrayList<>();
	int loversCheck;
	ArrayList<VeryLarge> battles = new ArrayList<>();
	int lastBunkRepairCall = 0;
	ArrayList<ShitTalk> shitTalkers = new ArrayList<>();
	HashMap<Unit, Integer> pylonScores = new HashMap<>();
	HashMap<Unit, Integer> pylonScoresMax = new HashMap<>();


	
    Bot() {
    	bwClient = new BWClient(this);
        bwClient.startGame();
    }
     

    @Override
    public void onStart() {
		game = bwClient.getGame();
		//System.out.println("Map: " + game.mapName());
		self = game.self();
		GlobalState = 0;
		game.setLocalSpeed(0);
		bewb = new BWEM(game);
		bewb.initialize();
		bewb.getMap().getData();
		bewb.getMap().assignStartingLocationsToSuitableBases();
		myStartLocation = getClosestBaseLocation(self.getStartLocation().toPosition());
		myData = new Data(game, bewb, myStartLocation, null);
		util = new Util(game, myData, bewb);
		startLocations = myData.startLocations;
	    Expands = myData.getExpands();
	    myChokes = myData.myChokes;
	    manager = new DecisionManager(game, myData);
	    myData.manager = manager; // Setting manager later may break my bot who knows. Only used in simming IIRC
	    strat = new Strategy(game.enemy().getRace(), Expands, myData, game, myChokes);
		globalRetreat = self.getStartLocation().toPosition();
		game.enableFlag(Flag.UserInput);
		// POO POO PEE PEE
	    // TilePosition were, int maxx, int PII, int MDD, int many, Game gaemm, UnitType typp
		//System.out.println(TechType.Nuclear_Strike.energyCost());
		//game.sendText("1800 Free Elo Ryan speaking how am i be of service?");
		for(Player ply : game.getPlayers()){
			if(!ply.isNeutral() && ply.isEnemy(self)){
				players.add(new BotPlayer(ply.getRace(), game, util, myData, ply));
				//util.Print(ply.getRace().toString());
				//if(currentTarget == null){
					//currentTarget = players.get(0);
				//}
			}
		}
		
		//game.setLatCom(true);
		
		
		
		// fuck this bot
		// just jokes
		// but really fuck this bot.
		// just jokes
		// it was no joke
		// fuck this bot
		// unless?
		// HEY I MBACK
		// TO FUCK THIS BOT
		// it wasn't a joke, the bot was the joke the whole time...

		pBuildings = strat.pBuildings;
		stuffQueue = strat.stuffQueue;
		buildMainGoal = strat.mainGoal;
		techGoals = strat.techGoals;
		GFR = strat.GFR;
		moveDefencePos = strat.moveRegroup;
			
		util.Print(game.mapName());
		
		for(int i = 0; i < 9; i++){
			UQ.add(self.getRace().getWorker());
		}
			
		for(int i = 0; i < 6; i++){
			UQ.add(self.getRace().getWorker());
		}
		
		if(self.getRace().equals(Race.Zerg)){
			for(int i = 0; i < 14; i++){
				UQ.add(self.getRace().getWorker());
			}
		}
		
		
		
//		for(pBuilding p : strat.pBuildings){
//			pBuildings.add(p);
//		}
//		
//		for(BotTech p : strat.stuffQueue){
//			stuffQueue.add(p);
//		}
		
		//util.playSound("test2.wav");
		
		if(cheats){
			game.sendText("REEEEEEEEEEE cheats");
		}
		
		if(game.getFrameCount() >= openerCheck && openerCheck != 0){
			ArrayList<UnitType> yes = strat.UpdateOpenerAgainst(currentTarget);
			if(yes != null){
				int i = 0;
				for(UnitType type : yes){
					if(pBuildings.get(0).type.supplyProvided() == 0){
						pBuildings.add(0, new pBuilding(type, null));
					}
					else {
						pBuildings.add(1, new pBuilding(type, null));	
					}

					i++;
				}
				game.sendText("Added : " + i + " new buildings");
				openerCheck = 0;
			}
		}
		

	
    	if(self.getRace().equals(Race.Zerg)){
    		newDumbName = "JumpyPurpleWaveZ";
    	} 
    	else if(self.getRace().equals(Race.Terran)){
    		newDumbName = "ShootyMcShootSiegeBot";
    	}
    	else {
    		newDumbName = "McPurpleBraintus";
    	}
    	
		Random rand = new Random();
		int n = rand.nextInt(17) + 1;
		
    	if(n == 1){
    		newDumbTag = " Has Disappointed my parents: ";
    		newDumbTag2 = " times this week. ";
    	}
    	else if(n == 2) {
       		newDumbTag = " Detected: ";
    		newDumbTag2 = " possible crashes this instance";
    	}
    	else if(n == 3) {
       		newDumbTag = " Using: ";
    		newDumbTag2 = "% of system resources";
    	}
    	else if(n == 4) {
       		newDumbTag = " Illegally downloading ";
    		newDumbTag2 = " songs per minute";
    	}
    	else if(n == 5) {
       		newDumbTag = " Processing: ";
    		newDumbTag2 = " teraflops a second";
    	}
    	else if(n == 6) {
       		newDumbTag = " Unfortunately running with a APM of: ";
    		newDumbTag2 = "";
    	}
    	else if(n == 7) {
       		newDumbTag = " Compilated taking over humanity: ";
    		newDumbTag2 = " times today.";
    	}
    	else if(n == 8) {
       		newDumbTag = " Infecting: ";
    		newDumbTag2 = " other machines with my dumb code";
    	}
    	else if(n == 9) {
       		newDumbTag = " Creating: ";
    		newDumbTag2 = " stackflow help requests per minute";
    	}
    	else if(n == 10) {
       		newDumbTag = " Stealing code from ";
    		newDumbTag2 = " different bots";
    	}
    	else if(n == 11) {
       		newDumbTag = " Compilated eating my room mates food ";
    		newDumbTag2 = " times this minute";
    	}
    	else if(n == 12) {
       		newDumbTag = " Sent McRave land down on discord ";
    		newDumbTag2 = " times so far";
    	}
    	else if(n == 13) {
       		newDumbTag = " Rewrote the same piece of code ";
    		newDumbTag2 = " times so far";
    	}
    	else if(n == 14) {
       		newDumbTag = " Sending ";
    		newDumbTag2 = " new guys bad code.";
    	}
    	else if(n == 15) {
       		newDumbTag = " Editing line: ";
    		newDumbTag2 = " of my readme.";
    	}
    	else if(n == 16) {
       		newDumbTag = " Introduced ";
    		newDumbTag2 = " new bugs to my bot";
    	}
    	else if(n == 17) {
       		newDumbTag = " Currently Storing ";
    		newDumbTag2 = " ArrayLists in my bot";
    	}
    	else {
    		newDumbTag = " Has Disappointed my parents: ";
    		newDumbTag2 = " times this week.";
    	}
	
		if(game.enemies().size() > 1){
			isFFA = true;
			game.sendText("FFA MODE ENABLED");
		}
		
		if(game.enemies().size() == 1){
			BotPlayer p = getPlayerP(game.enemy());
			if(p != null){
				currentTarget = p;
				myData.newTarget(p);
			}
		}
		

		// but you've gotta break a few eggs in order to make an omelette :wink: -- Krais0 4:43 9/05/2019
		
		game.sendText("KangarooBot systems reporting critical failures and fathery abandonment");
		game.sendText("Using Opener: " +  strat.buildName);
		
		
//		JBWAPIAgentFactory factory = new JBWAPIAgentFactory(game);
//		Evaluator e = new Evaluator(); // You can also customize the simulator
//		ArrayList<Agent> p1 = new ArrayList<>();
//		ArrayList<Agent> p2 = new ArrayList<>();
//		Agent asd = factory.of(UnitType.Protoss_Zealot);
//		Agent asdd = factory.of(UnitType.Terran_Firebat);
//		Agent asddd = factory.of(UnitType.Terran_Medic);
//		Agent asdddd = factory.of(UnitType.Zerg_Sunken_Colony);
//		asddd.setHealer(true);
//		for(int i =0;i<2;i++){
//			p1.add(asd);
//		}
//		
//		for(int i =0;i<3;i++){
//			p2.add(asdd);
//		}
//	
////		for(int i =0;i<12;i++){
////			p2.add(asdd);
////		}
////		
////		for(int i =0;i<4;i++){
////			p2.add(asddd);
////		}
		
//		double score = e.evaluate(p1, p2).value;
//		util.Print("" + score);
		
		
    }

    @Override	
    public void onFrame() {
    	StringBuilder cqs = new StringBuilder("Construction Queue:\n");
    	StringBuilder cqss = new StringBuilder("Unit Queue:\n");
    	StringBuilder cqsss = new StringBuilder("Bot Stuff:\n");
     	StringBuilder cqssss = new StringBuilder("Players:\n");
    	game.drawTextScreen(150, 10, newDumbName + newDumbTag + game.getAPM() +  newDumbTag2);
    	if(game.enemies().size() >= 3){
    		game.drawTextScreen(150, 20, "Versing " + game.enemies().size() + "Enemies.");
    	}
    	else {
    		if(currentTarget != null){
    			game.drawTextScreen(150, 20, "Versing " + game.enemy().getName() + " playing as: " + currentTarget.race);
    		}
    		else {
    			game.drawTextScreen(150, 20, "Versing " + game.enemy().getName() + " playing as: " + "A value that java doesn't like");
    		}
    	}
    	game.drawTextScreen(150, 30, "NeedsToExpand: " + myData.needsToExpand +  " Current State: " + manager.canWin + " Frame Count: " + game.getFrameCount());
    	game.drawTextScreen(150, 50, "I plan to make: " + buildMainGoal.toString() + " Stats: " + util.fuckingidunnojustgivemesomething(buildMainGoal));
    	if(currentTarget != null){
    	game.drawTextScreen(150, 40, "Target Stats: " + " PStats: " + PStats + " MIncome: " + currentTarget.mIncome + " GIncome: " + currentTarget.gIncome + " Workers: " + currentTarget.howManyHave(this.currentTarget.race.getWorker()));
    	//game.drawTextScreen(150, 60, "DEBUG: " + "D: " +  currentTarget.defenceScore + " A: " + currentTarget.armyScore + " PStats: " + PStats + " My Score: " + myData.myScore + " EnemyScore: " + myData.currentTarget.enemyScore);
    	game.drawTextScreen(150, 60, "FogUnit Sizes: " + fogUnits.size() + " EE: " + currentTarget.ecoScore + " EA: " + currentTarget.armyScore + " sMins: " + sMins + " sGas: " + sGas);
    	}
    	else {
    		game.drawTextScreen(150, 40, "CTS is currently a value that will crash my bot if i display it");
    	}
    	
    	//String test = Text.formatText("yes", Text.Green);
    	//game.drawTextScreen(150, 80, test);
    	
        myData.onFrame();
        myData.myExpands = myBases;
    	myData.spellCasters = casters;
		myData.myBases = myBases;

		
		if(currentTarget != null){
			if(currentTarget.race != currentTarget.player.getRace() && !raceInd){
				game.sendText("Hey! your race isn't unknown, it's " + currentTarget.race.toString());
				RandomRaceUpdate();
			}	
		}
		
				
		if(game.getFrameCount() >= 6500 && self.allUnitCount(UnitType.Zerg_Creep_Colony) + self.allUnitCount(UnitType.Zerg_Sunken_Colony) == 0 && !hasStoppedDoingTheProxy && strat.buildName == "Meme Proxy Sunken Build"){
			hasStoppedDoingTheProxy = true;
			game.sendText("RIP Proxy");
			UQ.add(UnitType.Zerg_Drone);
			UQ.add(UnitType.Zerg_Drone);
			UQ.add(UnitType.Zerg_Drone);
			UQ.add(UnitType.Zerg_Drone);
			UQ.add(UnitType.Zerg_Drone);
			pBuildings.clear();
			stuffQueue.clear();
			pBuildings.add(new pBuilding(self.getRace().getRefinery(), self.getStartLocation(), 200, 1));
			if(self.allUnitCount(UnitType.Zerg_Spawning_Pool) == 0){
				pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, self.getStartLocation()));
			}
			pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, self.getStartLocation(), 200, 1));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation(), 20));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Spire, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Queens_Nest, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery	, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Ultralisk_Cavern, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Defiler_Mound, self.getStartLocation()));
			stuffQueue.add(new BotTech(1, 2, TechType.Lurker_Aspect, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Metabolic_Boost, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Muscular_Augments, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Grooved_Spines, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 10, TechType.None, UpgradeType.Zerg_Melee_Attacks, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 11, TechType.None, UpgradeType.Zerg_Missile_Attacks, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Carapace, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Pneumatized_Carapace, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Adrenal_Glands, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Ensnare, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Lair, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Hive, myData));
			stuffQueue.add(new BotTech(1, 2, TechType.Consume, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 2, TechType.Plague, UpgradeType.None, UnitType.None, myData));
		}
		
	     if(currentTarget != null){
	    	 if(currentTarget.startLocation != null){
	    		 Position pos = currentTarget.startLocation.getCenter();
	    		 game.drawCircleMap(pos, 100, Color.Red);
	    		 game.drawTextMap(new Position(pos.x - 30, pos.y - 30), "Commit warcrimes here");
	    	 }
	     }
	     
			if(game.getFrameCount() >= loversCheck){
				// https://www.youtube.com/watch?v=1M02bAWDFkI BACK TO THE CLASSICS
				// https://www.youtube.com/watch?v=Rd3vwwXArMQ
				// https://www.youtube.com/watch?v=bqZ3PGbczEo
				loversCheck = game.getFrameCount() + 10;
				for(Lovers wuv : new ArrayList<>(lovers)){
					wuv.checkBreaking();
					if(wuv.deth == true){
						lovers.remove(wuv);
					}
					else {
						wuv.DoTheThingsThatMakeTheThingThatMyThingyTellsMeToMake();
					}
				}
			}
	     
      
	     if(!fogUnits.isEmpty()){
	    	 
		     if(game.getFrameCount() >= fogUpdate){
		    	 fogUpdate = game.getFrameCount() + 24;	
		    	 for(FogUnit f : new ArrayList<>(fogUnits)){
		    		 
//		    		 if(f.unit.isVisible()){
//		    			 f.setToUnknownAt = game.getFrameCount() + 150; // will keep updating til it's no longer seen
//		    			 f.update(f.unit);
//		    			 f.lastSeen = game.getFrameCount();
//		    		 }
//		    		 
		 
		    		 if(game.getFrameCount() >= f.setToUnknownAt && f.type.canMove()){
		    			 f.pos = Position.Unknown;
		    		 }
		    		 
		    		 f.simRegen();
		    		 
//		    		 if(f.pos != Position.Unknown){
//			    		 if(game.isVisible(f.pos.toTilePosition())){
//			    			 // is visible
//			    			 if(!myData.GetEnemyUnitsNearby(f.pos, 350, true).contains(f.unit)){
//			    				 // if visible but the unit isnt here.
//			    				 f.pos = Position.Unknown;
//			    			 }
//			    		 }
//			    		 else {
//			    			 // not visible
//			    		 }
//		    		 }
//		    		 
		    		 if(f.pos != Position.Unknown){
			    		 if(!game.isVisible(f.pos.toTilePosition())){
				    		 game.drawTextMap(f.pos, f.type.toString() + " Hp: " + f.hp + " Shields: " + f.shields + " Energy: " + f.energy);
				    	}
		    		}
		    		 
		    		 
//		    		else {
//		    			 ArrayList<Unit> asd = myData.GetEnemyUnitsNearby(f.pos, 200, true);
//		    			 if(asd.isEmpty()){
//		    				 fogUnits.remove(f);
//		    				 continue;
//		    			 }
//		    			 
//		    			 if(!asd.contains(f.unit)){
//		    				 fogUnits.remove(f);
//		    				 continue;
//		    			 }
//		    		}
		    		 
		    	 } // end of loop
		     }
		     
		     
	     }
	      
		  
       if(UQ.isEmpty() == false){
   		main:
    	 for(UnitType next : new ArrayList<UnitType>(UQ)){
    	   if(next.isWorker()){
    		 
  			if(!needsWorkers()){
    			if(UQ.size() == 1){
    				UQ.clear();
    				//game.sendText("Oh hey, i'mfull on workers. Maybe i should stop making them");
    				break main;
    			}
    			else {
    			UQ.remove(0);
    			}
    		}
  			

    	   }
    	   
    	   
    	   if(myData.nextExpand != null){
    		   game.drawCircleMap(myData.nextExpand.toPosition(), 30, Color.White);
    	   }
    	      
//    	for(ChokePoint cp : new ArrayList<>(myData.ramps)){
//    		Position p = cp.getCenter().toPosition();
//    		game.drawTextMap(p, "Hey this is a ramp!");
//    		for(Region r : myData.asdfg.get(cp)){
//    			if(r.getDefensePriority() >= 2){
//    				game.drawLineMap(r.getCenter(), p, Color.Blue);
//    			}
//    			else {
//        			game.drawLineMap(r.getCenter(), p, Color.Red);
//    			}
//
//    		}
//    	}
 
 	   if(self.minerals() >= sMins + next.mineralPrice() && self.gas() >= sGas + next.gasPrice() && self.supplyUsed() != 200){
 		   
			  if(next.equals(UnitType.Terran_Nuclear_Missile) && self.minerals() >= 200 && self.gas() >= 200){
				  for(Unit unit : self.getUnits()){
					  if(unit.getType().equals(UnitType.Terran_Nuclear_Silo)){
						  if(!unit.hasNuke() || !unit.getOrder().equals(Order.NukeTrain)){
							  unit.train(UnitType.Terran_Nuclear_Missile);
							  UQ.remove(UnitType.Terran_Nuclear_Missile);
							  break;
						  }
					  }
				  }
			  }
 		   
 		  
	    	   for(Unit unit :  new ArrayList<>(Production)){
	    		   
	    		   if(unit.getType().equals(UnitType.Zerg_Hatchery) || unit.getType().equals(UnitType.Zerg_Lair) ){
		    		   if(unit.canTrain(next)){
		    			   unit.train(next);
		    			   
		    			   if(next.equals(UnitType.Zerg_Overlord)){
		    				   overLordsMorphing++;
		    			   }
		    			   
		    			   if(!UQ.isEmpty()){
		    				   UQ.remove(next);
		    			   }
		    			   break main;
		    		   }
	    		   }
	    		   else {    			   
			    	 if(unit.canTrain(next) && unit.isIdle() == true){
			    		 unit.train(next);
			    		 if(!UQ.isEmpty()){
			    			 UQ.remove(next);
			    		}
			    		 break main;
			    	 }

	    		   }
	    		   
	    	   }
    	   
    	   }
 	   
  	   
    	 }
    	   
       }
       else {
    	   updateUQ();
       }
    	   
       
       if(myData.Expands != null){
    	   for(Base bass : new ArrayList<>(myData.Expands)){
    		   game.drawCircleMap(bass.getCenter(), 30, Color.Blue);
    		   game.drawTextMap(bass.getCenter(), "Index: " + myData.Expands.indexOf(bass));
    	   }
       }
       
       
       if(myData.nextAttackPosition != null){
    	   game.drawCircleMap(myData.nextAttackPosition, 30, Color.Red);
    	   if(self.getRace().equals(Race.Terran)){
    	   	   game.drawTextMap(myData.nextAttackPosition, "Send Nukes Here");
    	   }
    	   else  if(self.getRace().equals(Race.Protoss)){
    		   game.drawTextMap(myData.nextAttackPosition, "Send Storms Here");
    	   }
    	   else {
    		   game.drawTextMap(myData.nextAttackPosition, "Send Infestation Here");
    	   }
 
       }
       
   	   if(game.getFrameCount() >= winCheck){
   		   winCheck = game.getFrameCount() + 100;
   		   ArrayList<UnitType> enemies = new ArrayList<>();;
   		   BotPlayer ply = getWeakestOpponent();
   		   
   		   
   		   if(buildMainGoal.contains(UnitType.Protoss_Dark_Templar) && ply.VeryNumberOfTheLargeDetectorsDTS() > 3){
   			   buildMainGoal.remove(UnitType.Protoss_Dark_Templar);
   			   game.sendText("RIP DTS");
   		   }
   		   
   		   
   		   if(ply != null){
   			   
   			CheckGasClog();
   			myData.updateEco();
   			myData.checkFor3rdBases(currentTarget);
   			
   			
   			   for(UnitType type : ply.Types){
   				   enemies.add(type);
   			   }
   			   
   			   if(myData.canFuckBab == false){
	 			   for(UnitType type : ply.DTypes){
	   				   enemies.add(type);
	   			   }
   			   }
 			   
			   if(game.getFrameCount() <= 10000 && !isFFA){
				   if(myData.canEarlyExpand(ply)){
		    			if(!isExpandingSoon()){
		    				EarlyExpand();
		    				game.sendText("Okay bing, can i get a expansion ;)");
		    				myData.hasEarlyExpanded = true;
		    			}
				   }
			   }
			   
			   if(!hasReacted && game.getFrameCount() < (myData.startLocations.size() * 1000)){
			   updateEnemyOpening(currentTarget);
			   }
			   		   
	   		  //manager.globalEvaluate(myData.myMilUnits, enemies);
	   		   manager.simBattle(myData.myMilUnits, enemies, 250, ply.player, ply.race);
	   		  //System.out.println("Can Win Global: " + manager.canWin);
	   		   
			   boolean expand = myData.needsToExpand;
	   		   
	   		   if(game.getFrameCount() >= 14000 && myBases.size() == 1 && expand && manager.canWin){
	   			   manager.canWin = false;
		    		if(!isExpandingSoon()){
		    			Expand(false);
		    			myData.lastExpandFrame = game.getFrameCount();
		    			game.sendText("ME WANT BASE", Text.Green);
		    		}
	   		   }
	   		   
	   		   
			   if(manager.canWin == true || self.supplyUsed() >= 185){

				   if(ply != currentTarget){
				    currentTarget = ply;
				    myData.newTarget(ply);
				   }
			    	currentBitch = ply.player.getName();
			    	allSquadsAttack();
			    	GlobalState = 1;
			    	if(PStats < 1){
			    		PStats = 1;
			    	}
			    	else {
			    		PStats++;
			    	}
			    	
			    	if(self.getRace() == Race.Zerg && needsWorkers()){
			    		int max = 0;
			    	
			    		for(BotBase bass : new ArrayList<>(this.myBases)){
			    			max = max + bass.maxWorkers;
			    		}
			    		
			    		int need = max - self.allUnitCount(UnitType.Zerg_Drone) + amountInUQ(UnitType.Zerg_Drone);
			    		if(need < max){
			    			for(int i =0;i<max;i++){
			    				UQ.add(UnitType.Zerg_Drone);
			    			}
			    		}
			    		//CONSUME THE CUM CHALIC
			    		// https://www.youtube.com/watch?v=cmAlQvDQGoI
			    		
			    	}
			    	

			    	if(PStats >= 7){
					checkTechGoals();
					}
			    			    	
			    	if(expand && canExpand()){
			    		if(!isExpandingSoon()){
			    			Expand(true);
			    			myData.lastExpandFrame = game.getFrameCount();
			    		}
			    	}
			   }
			   else {
				   allSquadsRetreat();
				   //GlobalSimCheck();
				   GlobalState = 0;
				   currentBitch = "Myself";
				   
			    	if(PStats > 1){
			    		PStats = -1;
			    	}
			    	else {
			    		PStats--;
			    	}
			    	
			    	if(!pBuildings.isEmpty()){
			    	pBuilding next = pBuildings.get(0);
						if(next.type.isResourceDepot() && next.canBeCancelled == true && !canExpand()){
						 sMins = 0;
						 pBuildings.remove(0);
						}
			    	}
			    	
			    	if(PStats <= -9){
			    		if(self.getRace().equals(Race.Zerg)){
			    			clearWorkersFromQueue();
			    		}
			    	}
			    	
			
//		    		if(self.getRace().equals(Race.Zerg) && game.getFrameCount() <= 10000 && amountInBuildingQueue(UnitType.Zerg_Creep_Colony) < 1){
//		    			if(currentTarget.armyScore > currentTarget.ecoScore * 3){
//		    				for(UnitType type : new ArrayList<>(UQ)){
//		    					if(!type.isWorker()){
//		    						UQ.remove(type);
//		    					}
//		    					// purge NON WORKERS
//		    				}
//		    				game.sendText("REEEEEEEEEEEEEE");
//	    					if(self.allUnitCount(UnitType.Zerg_Spawning_Pool) > 0){
//	    						findNicePlaceToPutToPBuildings(UnitType.Zerg_Creep_Colony,defencePos,100,false);
//	    						stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
//	    						UQ.add(UnitType.Zerg_Drone);
//	    					}
//		    			}
//		    		}
			    	
//					if(game.getFrameCount() > 9000 && PStats < -9){
//						 for(UnitType p : new ArrayList<UnitType>(UQ)){
//							 if(p.equals(self.getRace().getWorker())){
//								 UQ.remove(p);
//							 }
//						}
//					}
				   
			   }
		   
			 myData.pStats = PStats;
   		   }

	   
   	   }

   	   // I CHANGED EXPANSION LOGIC ABIT. P STATS REQUIRE NOW DIST CALCUATION
   	   // ALSO THE "RUN UNITS IN MELEE ABOUT TO DIE"
   	   // its 10pm im retared
   	   
   	   
   	   
   	   if(game.getFrameCount() >= simCheck){
   		GlobalSimCheck();
   		simCheck = game.getFrameCount() + 10;

   		
   	   }
   	   
	   
   	   if(game.getFrameCount() >= baseRape){
  		// wtf is this?
   		// make units attack defences nearby when there is no enemy army nearby
      	baseRape = game.getFrameCount() + 50;
   		BaseRape();

   
   		
   	   }
   	   
   	   if(game.getFrameCount() >= shittalkCheck){
   		 Random rng = new Random();
   		 int r = rng.nextInt(40);
   		shittalkCheck = game.getFrameCount() + r;
   		List<Unit> shit = self.getUnits();
   		Collections.shuffle(shit);
   		

   		for(Unit u : shit){ // get a random unit
//      		for(Unit u : shit){
   			
   				if(!u.isCompleted() || u.getType().isBuilding() || u.getType().equals(UnitType.Zerg_Larva) || u.getType().equals(UnitType.Terran_Vulture_Spider_Mine)
   						||  u.getType().equals(UnitType.Zerg_Overlord) ){
   					//blacklists
   					continue;
   				}
   			
       			if(getShitTalkersSize() <= 15 && !isAShitTalker(u) && drawMoodYes == true){
	       		    	 // if not a shittalker
	       		    	 if(myData.isInCombat(u) && !u.isLoaded()){
	       		    		 drawMood(u, 1);
	       		    	 }
	       		    	 else if (u.isConstructing() || util.isPlacingBuilding(u) || util.isTrainingAUnit(u)){
	       		    		 if(u.isCompleted()){
	       		    		 drawMood(u, 3);
	       		    		 }
	       		    	 }
	       		    	 else if (myData.isSpellCaster(u)){
	       		    		 drawMood(u, 2);
	       		    	 }
	       		    	 else if (u.isUnderStorm() || u.isPlagued() || u.isLockedDown()){
	       		    		 drawMood(u, 4);
	       		    	 }
	       		    	 else {
	       		    		 int hds = rng.nextInt(10);
	       		    		 if(hds == 6){
	       		    			drawMood(u, 0);
	       		    			break;
	       		    		 }
	       		    			 // nothing i guess lol
	       		    	 } 
       				}
       			}
       		
      		
      		
   	   }
   	   
   	   
   	   
//   	   if(!players.isEmpty()){
//	   	   if(game.getFrameCount() >= playerUpdateCheck){
//	      		playerUpdateCheck = game.getFrameCount() + 200;
//	      		for(BotPlayer p : players){
//	      			p.update();
//	      		}
//	   	   }
//   	   }
//             
   	   
      if(myBases.isEmpty() == false){
	       for(BotBase bass : new ArrayList<>(myBases)){
    	   int workers = bass.getWorkers();
	    	   int x = bass.depot.getX();
	    	   int y = bass.depot.getY();
	    	   game.drawTextMap(x, y, "Workers: " + workers + " Max Workers: " + bass.maxWorkers);
	    	   if(bass.workers > bass.maxWorkers){
	    		   Unit unit = bass.getPawnGatheringMins();
	    		   if(unit != null){
	    		   bass.unAssignFromBase(unit);
	    		   //util.Print("workers > maxWorkers onframe assign");
	    		   assignWorkerToBase(unit);
	    		   }    		  
	    	   }
	    	   
	    	   for(Geyser g : bass.Geysers){
	    		   if(!g.getUnit().getType().isRefinery() && game.getFrameCount() >= 8000){
	    			  if(!AllGeysersBuilt() && !isPQueued(self.getRace().getRefinery())){
	    				  pBuildings.add(0, new pBuilding(self.getRace().getRefinery(), null));
	    				  util.Print("Caught base with available geysers");
	    			  }
	    		   }
	    		   
	    		   if(bass.depot != null){
	    		   game.drawLineMap(bass.depot.getPosition(), g.getCenter(), Color.Green);
	    		   }
	    	   }
	    	   
//	    	   for(Mineral min : bass.Mins){
//	    		   game.drawLineMap(min.getUnit().getPosition(), bass.depot.getPosition(), Color.Blue);
//	    	   }
	       }
       }
      
      if(!pBuildings.isEmpty()){
    	  // build the priority shit first
    	  pBuilding next = pBuildings.get(0);
    	  UnitType item = next.getType();
    	  int max = next.getMaxRange();
    	  int isP = next.save;
    	  TilePosition where = next.getTilePosition();
    	  boolean creep = next.waitForCreep;
    	  boolean cont = true;
    	  boolean isExpand = next.isExpand;
    	  int FC = next.frameCheck;
    	  boolean hasC = false;
    	  
    	  if(where != null){
    		  hasC = true;
    	  }
    	  
    	  boolean test = true;
    	  
    	  if(test && item.equals(UnitType.Terran_Bunker)){
    		  next.maxRange = 25;
    	  }
    	  
    	  if(item.requiresPsi()){
    		  if(self.allUnitCount(UnitType.Protoss_Pylon) != self.completedUnitCount(UnitType.Protoss_Pylon)){
    			  // if all pylons arent built
    			  int i = 0;
    			  int mack = self.allUnitCount(UnitType.Protoss_Pylon);
    			  for(Unit uuu : pylonScores.keySet()){
    				  int asdf = pylonScores.get(uuu);
    				  if(asdf >= 15){
    					  // full
    					  i++;
    				  }
    			  }
    			  
    			  if(i>=mack){
    				  util.Print("ALL PYLONS ARENT BUILT OR ARE ALL FULL");
    				  cont = false;
    				  // dont continue if we are waiting on a pylon to finish so we can build safely
    			  }
    		  }
    	  }
    	  

    	  if(self.getRace().equals(Race.Zerg) && self.allUnitCount(UnitType.Zerg_Drone) + amountInUQ(UnitType.Zerg_Drone) <= 7){
    		  UQ.add(UnitType.Zerg_Drone);
    		  //util.Print("Not building: " + item.toString() + " Due to low workers");
    		  cont = false;
    	  }
    	  
    	  if(currentTarget != null){
	    	  if(next.canSkip(currentTarget.race)){
	    		  util.Print("Hey! i can skip: " + item.toString());
	    		  pBuildings.remove(next);
	    		  cont = false;
	    	  }
    	  }
 	  
    	  if(item.equals(self.getRace().getRefinery()) && AllGeysersBuilt()){
    		  pBuildings.remove(next);
    		  cont = false;
    		  util.Print("Removed unnecessary refinery from queue");
    	  }
    	  	  
    	  if(isP >= 1 && cont == true && util.hasRequirementFor(item)){
    		  if(item.gasPrice() > 0){
    			  if(myData.isGatheringGas()){
    			  sMins = item.mineralPrice();
    			  sGas = item.gasPrice();
    			  }
    		  }
    		  else {    			  
    			  sMins = item.mineralPrice();
    			  sGas = item.gasPrice();
    		  }
	    	

    		  //util.Print("Saving for: " + item.toString());
    	  }
    	  
    	  if(next.proxy){
    		  if(currentTarget != null){
    			  if(currentTarget.startLocation != null){
    				  if(item.equals(UnitType.Zerg_Creep_Colony)){
    					  if(self.allUnitCount(UnitType.Zerg_Hatchery) > 0){
    						  ArrayList<Unit> units = util.getAllOf(UnitType.Zerg_Hatchery);
    						  if(units != null){
    							  if(!units.isEmpty()){
    								  for(Unit unit : units){
    									  if(unit.getPosition().getApproxDistance(currentTarget.startLocation.getCenter()) < 1000){
    										  where = unit.getPosition().toTilePosition();
    										  break;
    									  }
    								  }
    							  }
    							  else {
    								  cont = false;
    							  }
    						  }
    						  else {
    							  cont = false;
    						  }

    					  }
    				  }
    			  }
    			  else {
    				  cont = false;
    			  }
    		  }
    		  else {
    			 cont = false;
    		  }
    	  }
    	  
    	  if(creep == true){
    		  if(item.getRace().equals(Race.Zerg)){
    			  cont = game.hasCreep(where);
    		  }
    		  else {
    			  cont = game.hasPower(where);
    		  }
    	  }
  	  
    	  if(item.requiresPsi() && self.allUnitCount(UnitType.Protoss_Pylon) == 0){
    		  pBuildings.add(0, new pBuilding(UnitType.Protoss_Pylon, null));
    		  cont = false;
    		  util.Print("no pylon for building. adding one");
    		  //System.out.println("no pylon for building. adding one");
    	  }
    	  
    	  if(item.requiresPsi() && self.completedUnitCount(UnitType.Protoss_Pylon) == 0){
    		  cont = false;				 
    	  }
    	  
    	  if(game.canMake(item) && !isInQueue(item) && cont == true){
    		  
        	  if(item.equals(UnitType.Terran_Bunker) && where == null){
        		 boolean fuckingbig = false;
	       		 for(Unit unit : util.getAllOf(UnitType.Terran_Bunker)){
	     			where = unit.getTilePosition();
	     			fuckingbig = true;
	     			break;
	     		}
	       		 
         		if(!fuckingbig){
	         		if(defencePos == null){
	         		defencePos = myData.getDefencePos();
	         		}
	         		where = defencePos;
         		}

         	  }
         	  
//         	  if(item.equals(UnitType.Terran_Missile_Turret) && where == null){
//         		  if(self.allUnitCount(UnitType.Terran_Bunker) == 1){
//         			  ArrayList<Unit> yes = util.getAllOf(UnitType.Terran_Bunker);
//         			  if(yes != null){
//         				  where = yes.get(0).getTilePosition();
//         			  }	 
//         		  }
//         		  else {
//         			 if(defencePos == null){
//         			 defencePos = myData.getDefencePos();
//         			}
//         		  where = defencePos;
//         		  }
//         	  }
         	  
         	  Unit builder = null;
    		  if(where == null){
    			  if(next.buildWithScout){
    				  if(scouter != null){
    					  if(scouter.exists()){
    						  builder = scouter;
    					  }
    				  }
    				  else {
    					  builder = getWorker();
    				  }
    			  }
    			  else {
    				  builder = getWorker();
//    			  if(item.requiresPsi() && !item.equals(UnitType.Protoss_Photon_Cannon)){
//	    				  ArrayList<Unit> pylons = util.getAllOf(UnitType.Protoss_Pylon);
//	    				  if(pylons != null){
//	    					  Position pos = util.getClosestUnitFromArray(builder, pylons).getPosition();
//	    					  if(pos != null){
//	    					  where = pos.toTilePosition();
//	    					  }
//	    				  }
//	    			  }
    			  } 
    		  }
    		  else {
    			  if(next.buildWithScout){
    				  if(scouter != null){
    					  if(scouter.exists()){
    						  builder = scouter;
    					  }
    				  }
    				  else {
    					  builder = getWorkerNearest(where.toPosition());
    				  }
    			  }
    			  else {
    				  builder = getWorkerNearest(where.toPosition());
    			  }
    		  }
    		  
    		  if(isExpand){
    			  Position lastExpand = self.getStartLocation().toPosition();
    			  myData.updateNextExpansion(lastExpand);
    			  where = myData.nextExpand;
    			  if(!myData.nextExpand.isValid(game)){
        			  myData.updateNextExpansion(where.toPosition());
        			  where = myData.nextExpand;
        			  util.Print("Next Expand Shat the bed very danger");
    			  }
    		  }

    		  if(builder != null){
    			  TilePosition build = null;
    			  
        		  if(where == null){
        			  where = builder.getTilePosition();
        		  }
   		  
    			  if(isExpand){
    				  build = where;
    				  Base bass = getClosestBaseLocation(build.toPosition());
    				  ArrayList<Mineral> yes = getTheBlueCuntsThatFuckWithYou(bass);
    				  if(yes != null){
    					  for(Mineral min : yes){
    						  if(!blockMineral.contains(min)){
    							  blockMineral.add(min.getUnit());
    						  }
    					  }
    				  }
    			  }
    			  
    			  if(creep && build == null){
    				  if(item.equals(UnitType.Zerg_Creep_Colony)){
    					  build = game.getBuildLocation(item, where, max, true);
    					  //build = getDefencePlacement(builder, item, where, max);
    				  }
    				  else{
    					  build = getBuildTile(builder, item, where, max);
    				  }
    			  }
    			  else {
    				 if(item.equals(UnitType.Terran_Bunker) || item.equals(UnitType.Terran_Missile_Turret)){
    					 //build = getDefencePlacement(builder, item, where, max);
    					 build = game.getBuildLocation(item, where, max);
    				 }
    				 else{
	    				 if(item.isRefinery() || item.equals(UnitType.Protoss_Pylon)){
	    					 build = getBuildTile(builder, item, where, max);
	    				 }
	    				 else {
	    					 if(item.requiresPsi()){
	    						 // NEEDS POWAAAAAA
	    						 //util.Print("needs PSI yes");
	    						 build = getBuildTileProtoss(builder, item, where, max);	 
	    					 }
	    					 else{
	    					 //build = getBuildTile(builder, item, where, max);
	    					 // no longer broken thanks to yegers --> build = game.getBuildLocation(item, where, max);
	    					 build = game.getBuildLocation(item, where, max);
	    					 }
	    				 }
    				 }
	    						
    			  }
    			  
    			  if(build != null){
    				if(!build.isValid(game)){
    					if(item.equals(UnitType.Zerg_Creep_Colony)){
    						next.maxRange = next.maxRange + 2;
    					}
    					else {
    						next.maxRange = next.maxRange + 10;
    					}
    					
    				}
    				
    				
	    			builder.build(item, build);
	    			if(next.frameCheck != 0){
		    			if(isExpand){
		    				builders.add(new Builder(builder, item, build, 0, true));
		    			}
		    			else {
			    			builders.add(new Builder(builder, item, build, game.getFrameCount() + next.frameCheck));	
		    			}
	    			}
	    			else {
		    			if(isExpand){
		    				builders.add(new Builder(builder, item, build, 0, true));
		    			}
		    			else {
			    			builders.add(new Builder(builder, item, build, 0));
		    			}
	    			}
	    			placements.add(item);
	    			BotBase base = getBase(builder);
	    			if(base != null && self.getRace().equals(Race.Zerg)){
	    				base.newVoidedWorker(builder);
	    			}
    			  }
    			  else {
    				  // if build fails
    				  next.tries = next.tries + 1;
    				  
    				  if(next.tries >= 3){
     					 TilePosition fuckMe = game.getBuildLocation(item, where);
     					 if(fuckMe != null){
     						 if(fuckMe.isValid(game)){
     							 next.pos = fuckMe;
     							 builders.add(new Builder(builder, item, fuckMe, 0));
     							 placements.add(item);
     						 }
     					 }
    				  }
    				  
//    				  boolean contt = true;
//    				  util.Print("Build failed for item: " + item.toString());
//    				  if(item.equals(UnitType.Zerg_Creep_Colony)){
//    					  if(next.maxRange <= 25){
//    						  next.maxRange = next.maxRange + 5;
//    					  }
//    				  }
//    				  else {
//    					  next.maxRange = next.maxRange + 5;
//    				  }
//    				  
//    				  if(item.requiresPsi()){
//    					  boolean hasP = game.hasPowerPrecise(where.toPosition());
////        				  for(Unit uu : game.getUnitsInRadius(where.toPosition(), 200)){
////        					  if(uu.getType().equals(UnitType.Protoss_Pylon)){
////        						  hasP = true;
////        					  }
////        				  } 
//        				  
//        				  if(hasP){
//        					  if(next.tries >= 2){
//	        					 TilePosition fuckMe = game.getBuildLocation(item, where);
//	        					 if(fuckMe != null){
//	        						 if(fuckMe.isValid(game)){
//	        							 next.pos = fuckMe;
//	        							 builders.add(new Builder(builder, item, fuckMe, 0));
//	        							 contt = false;
//	        							 util.Print("UNFUCKED THE BUILDING1");
//	        							 placements.add(item);
//	        						 }
//	        					 }
//        					  }
//        				  }
//        				  else {
//        					  
//        					  if(next.tries >= 2){
//	        					 TilePosition fuckMe = game.getBuildLocation(item, where);
//	        					 if(fuckMe != null){
//	        						 if(fuckMe.isValid(game)){
//	        							 next.pos = fuckMe;
//	        							 builders.add(new Builder(builder, item, fuckMe, 0));
//	        							 contt = false;
//	        							 util.Print("UNFUCKED THE BUILDING1");
//	        							 placements.add(item);
//	        						 }
//	        					 }
//        					  }
//        					  
//        					  
//        					  if(contt){
//	        					  util.Print("Trying to build item: " + item.toString() + " Without power in the area. PREHAPS i should try to move it");
//	        					  // doesnt have power near the build location..
//		    					  ArrayList<Unit> aids = util.getAllOf(UnitType.Protoss_Pylon);
//		    					  if(aids != null){
//		    						  for(Unit u : aids){
//		    							  
//		    							  if(!u.isCompleted()){
//		    								  continue;
//		    							  }
//		    							  
//		    							  if(getPylonScore(u) < 15){
//		    								  where = u.getTilePosition();
//		    								  util.Print("Salavged a new build location for: " + item.toString());
//		    								  hasP = true;
//		    								  break;
//		    							  }
//		    						  }
//		    					  }
//	    					 
//        					  }
//	    					 
//
//        				  }
//    				  }
  
    			  }
    		  }
    	  }
      }
      else {
    	  // https://www.youtube.com/watch?v=asDlYjJqzWE
    	  // if pBuildings are empty.
    	  if(!strat.emptyBuildQueue.isEmpty()){
    		  if(strat.buildName == "McRave 12 BUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKER"){
	    		  if(self.allUnitCount(UnitType.Terran_Battlecruiser) >= myBases.size() * 4){
	    			  if(self.allUnitCount(UnitType.Terran_Starport) <= myBases.size() * 2){
	    	    		  for(UnitType type : strat.emptyBuildQueue){
	    	    			  if(util.hasRequirementFor(type)){
	    	    				  pBuildings.add(new pBuilding(type, null,300));
	    	    			  }
	    	    		  }
	    			  }
	    			  else {
	    				  // if we have starports
	    				  if(self.allUnitCount(UnitType.Terran_Barracks) <= myBases.size() * 2){
	    					  pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null,300));
	    				  }
	    				  
	    				  if(self.allUnitCount(UnitType.Terran_Factory) <= myBases.size() * 2){
	    					  pBuildings.add(new pBuilding(UnitType.Terran_Factory, null,300));
	    				  }
	    			  }
	    		  }
	    		  
	    		  if(self.gas() >= 2000 && pBuildings.isEmpty()){
	    			  pBuildings.add(new pBuilding(UnitType.Terran_Starport, null,300));
	    			  pBuildings.add(new pBuilding(UnitType.Terran_Starport, null,300));
	    		  }
    		  }
    		 else{
	    		  for(UnitType type : strat.emptyBuildQueue){
	    			  if(util.hasRequirementFor(type)){
	    				  pBuildings.add(new pBuilding(type, null,300));
	    			  }
	    		  }
    		  }
    	  }
      }
          
      if(!pBuildings.isEmpty()){
			if(pBuildings.size() <= 4){
				for(pBuilding bq : pBuildings){
					if(bq.save == 1){
						cqs.append("Item: ").append(Text.formatText(bq.type.toString(), Text.Green)).append("\n");
					}
					else {
						cqs.append("Item: ").append(bq.getType()).append("\n");
					}
					
				}
			}
			else{
				for(int i=0;i<=3;i++){
					pBuilding bq = pBuildings.get(i);
					if(bq.save == 1){
						cqs.append("Item: ").append(Text.formatText(bq.type.toString(), Text.Green)).append("\n");
					}
					else {
						cqs.append("Item: ").append(bq.getType()).append("\n");
					}
				}
				cqs.append("+ " + pBuildings.size() + " more items:");
			}
      }
      
      if(!UQ.isEmpty()){
  			if(UQ.size() <= 4){
				for(UnitType bq : UQ){
					cqss.append("Item: ").append(bq.toString()).append("\n");
				}
			}
			else{
				for(int i=0;i<=3;i++){
					cqss.append("Item: ").append(UQ.get(i).toString()).append("\n");
				}
				cqss.append("+ " + UQ.size() + " more items:");
			}  
      }
      
      if(!stuffQueue.isEmpty()){
  			if(stuffQueue.size() <= 4){
				for(BotTech bq : stuffQueue){
					if(bq.save){
					cqsss.append("Item: ").append(Text.formatText(bq.name, Text.Green)).append("\n");
					}
					else {
						cqsss.append("Item: ").append(bq.name).append("\n");
					}
				}
			}
			else{
				for(int i=0;i<=3;i++){
					BotTech bq = stuffQueue.get(i);
					if(bq.save){
					cqsss.append("Item: ").append(Text.formatText(bq.name, Text.Green)).append("\n");
					}
					else {
						cqsss.append("Item: ").append(bq.name).append("\n");
					}
				}
				cqsss.append("+ " + stuffQueue.size() + " more items:");
			}  
      }
      
      
      if(game.enemies().size() >= 3){
    	  for(BotPlayer ply : players){
    		  cqssss.append("Player " + ply.player.getName() + " Score: " + ply.enemyScore).append("\n");
    	  }
      }
      
     game.drawTextScreen(0, 10, cqs.toString());
     game.drawTextScreen(0, 130, cqss.toString());
     game.drawTextScreen(500, 75, cqsss.toString());
      if(game.enemies().size() >= 3){
      game.drawTextScreen(450, 200, cqssss.toString());
      }
      
      if(baseCheck <= game.getFrameCount() || baseCheck == 0){
    	  baseCheck = game.getFrameCount() + 50;
	      for(BotBase bass : new ArrayList<>(myBases)){
	    	  bass.doThings();
	    	  if(!bass.depot.exists()){
					ArrayList<Unit> pawns = bass.Pawns;
					bass.EmptyPawns();
					myBases.remove(bass);
					for(Unit unittt : pawns){
						//util.Print("Base with no depot assign");
						assignWorkerToBase(unittt);
					}
					game.sendText("Hey Mr.Base where's your depot?");
	    	  }
	      }
	      
	      
	      
      }
      
      for(BotPlayer p : new ArrayList<>(players)){
    	  p.update();
      }
      
      if(!battles.isEmpty()){
	      for(VeryLarge large : new ArrayList<>(battles)){
	    	  if(game.getFrameCount() >= large.frameStart + 150){
	    		  battles.remove(large);
	    		  continue;
	    	  }
	    	  
	    	  game.drawTextMap(large.mystart, "P1 : " + large.p1Units.size() + " VERSES " + "P2: " + large.p2Units.size() + " " + large.fogUnits.size());
	    	  game.drawTextMap(large.mystart.x,large.mystart.y - 10 , "P1 : " + large.fapScores.getLeft() + " << >> " + "P2: " + large.fapScores.getRight());
	    	  game.drawCircleMap(large.mystart, 30, Color.Green);
	    	  game.drawCircleMap(large.enemystart, 30, Color.Red);
	    	  // https://www.youtube.com/watch?v=UgS7vgquBvo how the fuck did i get here?
	    	  if(large.simScore >= 0.85){
	    		  // VERY GOOD
	    		  game.drawTextMap(large.mystart.x,large.mystart.y - 30, Text.formatText(""+ large.getSimScore(), Text.Green));
	    	  }
	    	  else if (large.simScore <= 0.84 && large.simScore >= 0.65){
	    		  // STILL GOOD
	    		  game.drawTextMap(large.mystart.x,large.mystart.y - 30, Text.formatText(""+ large.getSimScore(), Text.DarkGreen));
	    	  }
	    	  else if (large.simScore <= 0.64 && large.simScore >= 0.45){
	    		  // OH FUCK OH FUCK
	    		  game.drawTextMap(large.mystart.x,large.mystart.y - 30, Text.formatText(""+ large.getSimScore(), Text.Orange));
	    	  }
	    
	    	  else if (large.simScore <= 0.44 && large.simScore >= 0.35){
	    		  //  WHY DID I BUILD MARINES
	    		  game.drawTextMap(large.mystart.x,large.mystart.y - 30, Text.formatText(""+ large.getSimScore(), Text.BrightRed));
	    	  }
	    	  
	    	  else if (large.simScore <= 0.34 && large.simScore >= 0.20){
	    		  // TFW NO SIEGE TANKS TO CARRY MY BOT
	    		  game.drawTextMap(large.mystart.x,large.mystart.y - 30, Text.formatText(""+ large.getSimScore(), Text.Red));
	    	  }
	    	  else {
	    		  // SAVE AND NEVER RETURN
	    		  game.drawTextMap(large.mystart.x,large.mystart.y - 30, Text.formatText("A SCORE THAT I AM DISAPPOINTED TO DRAW", Text.Red));
	    	  }
	    	  
	    	  
	    	  for(Unit mine : large.p1Units){
	    		  if(large.p1Units.size() <= 15){
		    		  if(mine.exists()){
		    		  //game.drawLineMap(large.mystart, mine.getPosition(), Color.Blue);
		    		  }
	    		  }
	    	  }
	    	  for(Unit mine : large.p2Units){
	    		  if(large.p2Units.size() <= 15){
		    		  if(mine.exists()){
		    		  //game.drawLineMap(large.mystart, mine.getPosition(), Color.Red);
		    		  }
	    		  }
	    	  }
	    	  
	    	  for(FogUnit f : large.fogUnits){
	    		  if(f.pos != Position.Unknown){
	    			  game.drawLineMap(large.mystart, f.pos, Color.Red);
	    		  }
	    	  }
	    	  
	    	  if(large.nearChokePoint){
	    		  game.drawTextMap(large.mystart.x,large.mystart.y - 50, Text.formatText("CHOKEPOINT BATTLE", Text.Tan));
	    	  }
	    	  
	    	  if(large.AirStay){
	    		  game.drawTextMap(large.mystart.x,large.mystart.y - 65, Text.formatText("AIR CAN STAY", Text.White));
	    	  }
	    	  
	    	  game.drawTextMap(large.mystart.x,large.mystart.y - 75, Text.formatText("Sim Type: " + large.simType, Text.White));
	    	  
	    	  
    	      	  
	    	  //game.drawTextMap(large.mystart.x,large.mystart.y - 50, large.p2Units.toString());
	    	 //game.drawTextMap(large.mystart.x,large.mystart.y - 60, large.fogUnits.toString());
	      }
      }
              
      for(Squad sq : Squads){
    	  sq.onFrame();
    	  
    	  if(!sq.getUnits().isEmpty()){
    		 Unit leader = sq.getUnits().get(0);
    		game.drawTextMap(leader.getPosition(), "Squad: " + sq.id + "  " + "Units: " + sq.getUnitSize() + " / 25");
    		if(sq.target!=null){
    		game.drawLineMap(leader.getPosition(), sq.target, Color.Red);
    		}
    		   	
		    
    	  }
      }
      
     
      
      for(Bullet b : game.getBullets()){
    	  if(b.getType().equals(BulletType.Psionic_Storm)){
    		  Position pos = b.getPosition();
    		  List<Unit> yes = game.getUnitsInRadius(pos, 300);
    		  for(Unit unit : yes){
    			  if(unit.getPlayer().equals(self) && unit.canMove() && unit.isUnderStorm()){
    				  Position move = util.GetKitePos(unit, pos);
    				  if(move != null){
    					  myData.DND(unit, game.getFrameCount() + unit.getDistance(pos) / 2);
    					  unit.move(move);
    				  }
    			  }
    		  }	  
    	  }
      }
      
      if(game.getFrameCount() >= microCheck){
    	  microCheck = game.getFrameCount() + 10;
    	  for(Squad sq : Squads){
  			 sq.squadMicro();
    	  }
    	  friendlyFireCheck();
      }
      
      
      if(game.getFrameCount() >= UICCheck){
    	  UICCheck = game.getFrameCount() + 10;
    	  for(Unit unit : self.getUnits()){
    		 if(IsMilitrayUnit(unit) || myData.isSpellCaster(unit) || IsMilitrayBuilding(unit)){
	    		 ArrayList<Unit> yes = util.getEnemyArmyUnitsNearMe(unit, 350, true);
	    		 if(yes == null){
	    			 if(myData.UIC.contains(unit)){
			        	myData.UIC.remove(unit);
	    			 }
			    	}
	    		 	else {
	    		 		if(!myData.UIC.contains(unit)){
				    		 myData.UIC.add(unit);
				    	}
			       }
	    		 
	    		 ArrayList<FogUnit> ja = getFogUnitsNearby(unit.getPosition(), 400);
	    		 
	    		 if(ja.isEmpty()){
	    			 if(myData.UIC.contains(unit)){
				        myData.UIC.remove(unit);
		    		} 
	    		 }
	    		 else {
	    		 	if(!myData.UIC.contains(unit)){
				    	myData.UIC.add(unit);
				    }
	    		 }

	    		 
	    		 
			}
    		 
    		 
	  
    	  }
    	  

      }
           
      if(!Expands.isEmpty()){
    	  int i = 0;
	      for(Base bass : Expands){
	    	  if(myBases.size() < i){
	    	  i++;
	    	  game.drawTextMap(bass.getCenter(), "Expand Number: " + i);
	    	  }
	      }
      }
      
      if(!myChokes.isEmpty()){
    	  int i = 0;
    	  for(ChokePoint choke : myChokes){
    		  if(choke != null){
    		  i++;
    		  game.drawTextMap(choke.getCenter().toPosition(), "Chokepoint " + i);
    		  }
    	  }
      }
      
    
     if(game.getFrameCount() >= improveCheck){
    	 //https://www.youtube.com/watch?v=Ph-CA_tu5KA
    	 improveCheck = game.getFrameCount() + 48;
    	 if(overLordsMorphing > 0){
    		 if(self.incompleteUnitCount(UnitType.Zerg_Overlord) == 0){
    			 overLordsMorphing = 0;
    		 }
    	 }
    	 
    	 if(overLordsMorphing < 0){
    		 overLordsMorphing = 0;
    	 }
    	 
		int bioCount = self.allUnitCount(ShootyBoy) + self.allUnitCount(UnitType.Terran_Firebat);
		int medicCount = self.allUnitCount(UnitType.Terran_Medic) + amountInUQ(UnitType.Terran_Medic);
		
		myData.updateResourceFocus(); // this checks the amount of units gathering shit and see if gas is only being collected
    	 
    	 if(self.getRace().equals(Race.Terran) && self.gas() >= 50){
				if(medicCount < Math.round(bioCount / 8)){
					if(self.completedUnitCount(UnitType.Terran_Academy) > 0 && self.completedUnitCount(UnitType.Terran_Refinery) > 0){
						for(int i = 0;  i < Math.round(bioCount/8); i++){
							UQ.add(UnitType.Terran_Medic);
						}
					}
				}
    	 }
    	 
    	 
    	 if(self.minerals() >= 500 && UQ.size() >= 20){
    	 checkFloat(); // dunno why this is here i cbf adding another integer check
    	 }
    	 
    	 if(!stuffQueue.isEmpty()){
    		 mainLoop:
    		for(BotTech tech : new ArrayList<>(stuffQueue)){
    			int type = tech.type;
    			TechType TT = tech.tech;
    			UpgradeType UP = tech.upgrade;
    			int i = stuffQueue.indexOf(tech);
    			tech.isReady();
    			boolean doSave = true;
    			//util.Print("IS ready" + tech.ready);
    			
    			if(tech.save == true && tech.requirementsMet(tech.RID)){
    				
    				
    				if(!pBuildings.isEmpty()){
    					pBuilding next = pBuildings.get(0);
    					if(next != null){
	    					if(next.save == 1){
	    						// if we are already saving for a building just dont do anything with this tech.
	    						doSave = false;
	    						
	    					}
    					}
    				}
    				
    				if(doSave){
    				
	    				if(type == 1){
	    					if(util.hasRequirementFor(TT)){
	    						// can build
	    						if(TT.gasPrice() > 0){
	    							// costs gas
	    							if(myData.isGatheringGas()){
	    								sMins = TT.mineralPrice();
	    		    					sGas = TT.gasPrice();
	    							}
	    							else {
	    								// costs gas but not gathering it
	    								sMins = 0;
	    								sGas = 0;
	    								// dont save if it needs gas but your not gathering it
	    							}
	    						}
		    					
	    					}
	    				}
	    				
	    				if(type == 2){
	    					if(util.hasRequirementFor(UP)){
	    						if(UP.gasPrice() > 0){
	    							// costs gas
	    							if(myData.isGatheringGas()){
	    								sMins = UP.mineralPrice();
	    		    					sGas = UP.gasPrice();
	    							}
	    							else {
	    								// costs gas but not gathering it
	    								sMins = 0;
	    								sGas = 0;
	    								// dont save if it needs gas but your not gathering it
	    							}
	    						}
	    					}
	    				}
	    				
	    				if(type == 3){
	    					//
	    					if(util.hasRequirementFor(tech.morphType)){
	       						if(tech.morphType.gasPrice() > 0){
	    							// costs gas
	    							if(myData.isGatheringGas()){
	    								sMins = tech.morphType.mineralPrice();
	    		    					sGas = tech.morphType.gasPrice();
	    							}
	    							else {
	    								// costs gas but not gathering it
	    								sMins = 0;
	    								sGas = 0;
	    								// dont save if it needs gas but your not gathering it
	    							}
	    						}
		    				}
	    				}
	
    				}
    				
    			}
    			
    			
    			boolean brek = false;
    			if(tech.ready == true){
    				// if requirements are met
    				if(type == 1 && game.canResearch(TT, null)){
    					// TechTypes
    					for(Unit myUnit : self.getUnits()){
    						if(myUnit.canResearch(TT) == true && canSpend(TT.mineralPrice(), TT.gasPrice()) && myUnit.isCompleted() && !myUnit.isResearching()){
    							myUnit.research(TT);
    							if(tech.save){
    		    					sMins = 0;
    		    					sGas = 0;
    							}
    							game.sendText("Researching: " + tech.name);
    							brek = true;
    							stuffQueue.remove(i); 
    							break mainLoop;
    						}
    					}
    					
    				}
    				
    				if(type == 2 && game.canUpgrade(UP,  null)){
    					// UpgradeTypes
       					for(Unit myUnit : self.getUnits()){
    						if(myUnit.canUpgrade(UP) == true && canSpend(UP.mineralPrice(), UP.gasPrice()) && myUnit.isCompleted() && !myUnit.isUpgrading()){
    							myUnit.upgrade(UP);
    							if(tech.save){
    		    					sMins = 0;
    		    					sGas = 0;
    							}
    							game.sendText("Upgrading " + tech.name);
    							brek = true;
    							if(UP.maxRepeats() <= 1){
    							stuffQueue.remove(i);
    							}
    							break mainLoop;
    						}
    					}
    					
    				}
    				if(type == 3 && game.canMake(tech.morphType, null)){
    					// UnitTypes
    					// mostly zerg
    				 	 for(Unit myUnit : self.getUnits()){
    				 		UnitType nextMorph = tech.morphType;
    				 		if(nextMorph != null && myUnit != null){
    				 			//System.out.println("" + nextMorph.toString() + " " + myUnit.getType().toString() + " " + myUnit.getID() + " ");
	    					    if(myUnit.canMorph(nextMorph) && canSpend(nextMorph.mineralPrice(), nextMorph.gasPrice()) && myUnit.isCompleted() && !myUnit.isMorphing()){
	    					    	myUnit.morph(nextMorph);
	    					    	if(tech.save){
	    		    					sMins = 0;
	    		    					sGas = 0;
	    					    	}
	    					    	brek = true;
	    					    	game.sendText("Morphing: Unit: " + myUnit.getType().toString() + " To " +  tech.name);
	    					    	stuffQueue.remove(i);
	    					    	break mainLoop;
	    					    }
    				 		}
    				 	 }
    					
    				}
    				
    				if(type == 4){
    					ArrayList<Unit> yes = util.getAllOf(UnitType.Protoss_Dark_Templar);
    					if(yes.isEmpty()){
    						continue;
    					}
    					else {
    						//lovers.add(new Lovers(unit,around,true,myData));
    						Unit one = null;
    						Unit two = null;
    						for(Unit unit : yes){
    							if(!isALover(unit)){
    								if(one == null){
    									one = unit;
    									continue;
    								}
    								
    								if(two == null && one != unit){
    									two = unit;
    								}
    								
    								if(one != null & two != null){
    									if(one != two){
    										lovers.add(new Lovers(one,two,true,myData));
    									}
    								}
    							}
    							

    						}
    					}
    					
    				} // end of 4
    				
    				
    				
    			}
    			
    			// end of first loop
    		}
    			
    		
    	 }
    	 
    	 
    	 
     }
    	 
//	 	 if(!morphQueue.isEmpty()){
//	 	 UnitType nextMorph = morphQueue.get(0);
//	 	 int minsCost = nextMorph.mineralPrice();
//	 	 int gasCost = nextMorph.gasPrice();
//		 	 for(Unit myUnit : self.getUnits()){
//		 	 //canMake doesn't count cost
//			    	 if(myUnit.canMorph(nextMorph) && self.minerals() >= minsCost && self.gas() >= gasCost && myUnit.isCompleted() && !myUnit.isMorphing()){
//			    		 myUnit.morph(nextMorph);
//			    		 System.out.println("Morphing: " + nextMorph.toString());
//			     		 morphQueue.remove(nextMorph);
//			    		 break;
//			    	 }
//		 	 }
//	 	 }
		 	 

     

     
     if(needsSupply() && supplyQueued(self.getRace().getSupplyProvider()) == false && game.getFrameCount() >= supplyCheck){
    	supplyCheck = game.getFrameCount() + 30;
    	if(self.getRace().equals(Race.Zerg)){
    		if(game.getFrameCount() > 10000){
	    		for(int i = 0; i <= myBases.size(); i++){
	        		UQ.add(0, UnitType.Zerg_Overlord);
	    		}
    		}
    		else {
    			UQ.add(0, UnitType.Zerg_Overlord);
    		}
    	}
    	else {
    		for(int i = 0; i < myBases.size(); i++){
    		pBuildings.add(0, new pBuilding(self.getRace().getSupplyProvider(), null));
    		}
    		
    		if(self.minerals() >= 500){
    			pBuildings.add(0, new pBuilding(self.getRace().getSupplyProvider(), null));
    		}
    	}
     }
     
     
     for(Lovers wuv : new ArrayList<>(lovers)){
    	 game.drawLineMap(wuv.father.getPosition(), wuv.mother.getPosition(), Color.Purple);
     }
     
     for(BotBase br : new ArrayList<BotBase>(this.myBases)){
    	 for(Mineral m : new ArrayList<>(br.Mins)){
    		 game.drawTextMap(m.getCenter(), "Workers: " + br.mineralG.get(m.getUnit().getID()).size());
    		 if(br.mineralG != null){
	    		 for(Unit u : br.mineralG.get(m.getUnit().getID())){
	    			 //game.drawLineMap(m.getCenter(), u.getPosition(), Color.Cyan);
	    		 }
    		 }
    	 }
     }
     
     
     //
     
     for(Unit myUnit : self.getUnits()){
    	 
    	 
    	 if(IsMilitrayUnit(myUnit) || myData.isSpellCaster(myUnit)){
    		 if(drawHealthInfo){
    		 drawVerySickAndCoolHealthInfomation(myUnit);
    		 }
    	 }
    	 
    	 if(isAShitTalker(myUnit)){
    		 ShitTalk st = getShitTalker(myUnit);
    		 
    		 if(st != null){
    			 
	    		 if(!myUnit.exists()){
	    			 shitTalkers.remove(st);
	    		 }
	    		 
	    		 if(st.startAt > 0){
	    			 if(game.getFrameCount() >= st.startAt){
	    				 st.goAhead();
	    			 }
	    			 
		    		 if(game.getFrameCount() >= st.drawTill){
		    			 shitTalkers.remove(st);
		    		 }
	    		 }
	    		 else {
		    		 if(game.getFrameCount() >= st.drawTill){
		    			 shitTalkers.remove(st);
		    		 }
		    		 else {
			    		 st.goAhead();
		    		 }
	    		 }
	    		 
    		 }
    		 
    	 }
    	 
    	 //todo more test and then upload. 
    	 // maybe a spell caster isWorthtoCast rework?
  
    	 
    	 if(!myBases.isEmpty()){
	       	 if(myUnit.getType().isWorker() == true && myUnit.isCompleted() && assignedToBase(myUnit) == false){
	       		//util.Print("On Frame assign Unit");
	    		 assignWorkerToBase(myUnit);
	    	 }
    	 }
    	 
 
    	 if(myUnit.getType().equals(UnitType.Protoss_Carrier)){
    		 if(myUnit.getInterceptorCount() != 6 && !myUnit.isTraining()){
    			 if(canSpend(25,0)){
    				 myUnit.train(UnitType.Protoss_Interceptor);
    			 }
    		 }
    	 }
    	 //spider mine
    	 // spider
    	 // mines
    	  	
    	 if(myUnit.getType().equals(UnitType.Terran_Vulture_Spider_Mine)){
    		 if(!myUnit.isBurrowed()){
        		 //util.Print("TRIGGER");
    			 Unit target = myUnit.getOrderTarget();
    			 if(target == null){
    	    		// util.Print("GET ORDER TARGET SUCKS DICK");
    				 target = myUnit.getTarget();
    			 }
    			 if(target != null){
    	    		// util.Print("NOT NULL");
    				 int i = 0;
    				for(Unit unit : game.getUnitsInRadius(target.getPosition(), 250)){
    					if(unit.getPlayer().equals(self) && unit.canMove()){
    					//System.out.println("Unit found: " + unit.getID());
    					Position pos = util.GetKitePos2(unit, target);
    					if(pos != null){
    						//System.out.println("Not null");
    						if(unit.getType().equals(ShootyBoy) || unit.getType().equals(UnitType.Terran_Firebat)){
    							if(!unit.isStimmed() && unit.canUseTech(TechType.Stim_Packs)){
    								unit.useTech(TechType.Stim_Packs);
    							}
    						}
    						unit.move(pos);
    						myData.DND(unit, game.getFrameCount() + 30);
    						if(getShitTalkersSize() <= 15 && !isAShitTalker(unit) && drawMoodYes){
    							drawMood(unit, 4);
    						}
    						//System.out.println("Stuff happened");
    						}
    					 }
    				 }
    		 	}
    		 }
    	 }
    	 
    	 if(self.allUnitCount(UnitType.Zerg_Spore_Colony) > 0 && myUnit.getType().equals(UnitType.Zerg_Overlord) && !detAssignedASquad(myUnit)){
    		 Unit spore = util.getUnit(UnitType.Zerg_Spore_Colony);
    		 if(spore != null){
    			 if(myUnit.getDistance(spore) > 20){
    			 myUnit.move(spore.getPosition());
    			 }
    		 }
    	 }
    	 
    	 //System.out.println(myUnit.canUseTech(TechType.Nuclear_Strike, self.getStartLocation().toPosition()));
    		 
    	 
    	 
    	 if(myUnit.getHitPoints() != myUnit.getType().maxHitPoints() && myUnit.getType().getRace().equals(Race.Terran) && myUnit.getType().isBuilding() && myUnit.isCompleted()){
    		 //repairs 
    		 if(!isBeingRepaired(myUnit)){
    			 if(!repairLastCalled.keySet().contains(myUnit)){
    				 repairLastCalled.put(myUnit, 0);
    			 }
 				game.drawTextMap(myUnit.getPosition(), "" + repairLastCalled.get(myUnit), Text.Orange);
    			 if(myUnit.getType().equals(UnitType.Terran_Bunker) || myUnit.getType().equals(UnitType.Terran_Missile_Turret) && repairLastCalled.get(myUnit) >= game.getFrameCount()){
    				repairLastCalled.put(myUnit, game.getFrameCount() + 10);

    				 ArrayList<Unit> workers = getWorkersToRepair2(3, myUnit.getPosition());
    				 if(workers != null){
	    				 for(Unit worker : workers){
		    	    		BotBase bass = getBase(worker);
		    	    		 repairs.add(new Repairer(worker, myUnit, false, game.getFrameCount() + 200));
		    	    		if(bass != null){
		    	    			bass.newVoidedWorker(worker);
		    	    		}
	    				 }
    				 }
    				
    			 }
    			 else {
    				 Unit worker = getWorkerNearest(myUnit.getPosition());
	    			  if(worker != null){
	    				  BotBase bass = getBase(worker);
	    				  repairs.add(new Repairer(worker, myUnit, true, 0));
	    				  if(bass != null){
	    					  bass.newVoidedWorker(worker);
	    				  }
	    			  }
    			 }
    		 }
    		 else {

    		 }
    		 
    	 }
    	 
    	 if(isRepairer(myUnit)){
    		 Repairer r = getRepairer(myUnit);
    		 if(r != null){
    			 Unit target = r.target;
    			 boolean leave = r.leaveWhenRepaired;
    			 int frames = r.loiterFrames;
    			 BotBase bass = getBase(myUnit);
    		
    			 if(target == null){
    				 repairs.remove(r);
     				if(bass != null){
    					bass.unVoidWorker(myUnit);
    				} 
    			 }
    			 
    			 if(!target.exists()){
    				 repairs.remove(r);
     				if(bass != null){
    					bass.unVoidWorker(myUnit);
    				}
    			 }
    			 
    			 
    			 if(!myUnit.isRepairing()){
    				 myUnit.repair(target);
    			 }
    			 
    			 			 
    			 if(target.getHitPoints() == target.getType().maxHitPoints() && leave == true){
    				repairs.remove(r);
    				if(bass != null){
    					bass.unVoidWorker(myUnit);
    				}
    			 }
    			 
    			 if(game.getFrameCount() >= frames && target.getHitPoints() == target.getType().maxHitPoints() ){
     				repairs.remove(r);
     				if(bass != null){
     					bass.unVoidWorker(myUnit);
     				} 
    			 }
    			 
    			 game.drawLineMap(myUnit.getPosition(), r.target.getPosition(), Color.Red);
    			 
	 
    		 }
    		 
    		 
    	 }

    	
	    if(myUnit.getType().equals(UnitType.Zerg_Sunken_Colony) && myUnit.getOrder().equals(Order.AttackUnit)){
	    	ArrayList<Unit> enemy = myData.GetEnemyUnitsNearby(myUnit.getPosition(), 400, false);
	    	Unit target = myUnit.getOrderTarget();
	    	if(target != null){
			    	if(!util.ShouldBeFocused(target)){
			    		for(Unit enemies : enemy){
			    			if(util.ShouldBeFocused(enemies)){
			    				if(myUnit.canAttack(enemies)){
			    				myUnit.attack(enemies);
			    				break;
			    			}
			    		}	
			    	}
		    	}
	    	}
	    		
	    }
    	 
	 	 if(isVoidedWorker(myUnit)){
	 		 game.drawCircleMap(myUnit.getPosition(), 30, Color.Brown);
	 		 
    		 if(myUnit.getType() != self.getRace().getWorker()){
    			 BotBase base = getBase(myUnit);
	    			if(base != null){
  	    				base.unVoidWorker(myUnit);
  	    				//System.out.println("unVoided worker " + myUnit.getID());
  	    			}
    		 }
    		 
    		 if(!isABuilder(myUnit) && myUnit != scouter){
    			 BotBase base = getBase(myUnit);
	    			if(base != null){
  	    				base.unVoidWorker(myUnit);
  	    				//System.out.println("unVoided worker " + myUnit.getID());
  	    			}
    		 }
    		 
    		 
    		 game.drawCircleMap(myUnit.getPosition(), 20, Color.Red);
    	 }
	 	 
    	 if(myUnit.isSelected() && IsMilitrayUnit(myUnit)){
    		 Squad sq =  getSquad(myUnit);
    		 if(sq==null){
   			// System.out.println("Unit: " + myUnit.getID() + " Squads is null"); 
    		 }
    		 else {
	    		// System.out.println("Unit Squad: " + sq.id);
	    		 //System.out.println("Squad State: " + sq.State);
	    		 if(sq.target != null){
	    		// game.drawLineMap(myUnit.getPosition(), sq.target, Color.Purple);
	    		 game.drawTextMap(new Position(myUnit.getX() + 5, myUnit.getY() + 5), myUnit.getOrder().toString());
	    		 }
	    		 game.drawTextMap(new Position(myUnit.getX() + 15, myUnit.getY() + 12), "" + myData.getSimScore(myUnit));
    		 }
    		 int yes1 = myUnit.getX() - 10;
    		 int yes2 = myUnit.getY() - 10;
    		 game.drawTextMap(new Position(yes1, yes2), "UIC: " + myData.isNearEnemyOrBetter(myUnit));	
    		 game.drawTextMap(new Position(myUnit.getX() + 25, myUnit.getY() + 12), "" + myUnit.isDetected());	
    		 game.drawTextMap(new Position(myUnit.getX() + 10, myUnit.getY() + 25), "Cooldown: " + myData.weaponCoolingDown(myUnit));	
    		 //game.drawTextMap(new Position(myUnit.getX() + 20, myUnit.getY() + 10), "Can Retreat: " + myData.isInSpotToRetreat(myUnit));
    		 
    		 		 
    	 }
    	 
    	 
		 if(myUnit.isAttacking() && myUnit.getOrderTarget() != null){
			 Position yes = util.GetKitePos2(myUnit, myUnit.getOrderTarget());
			 if(yes != null){
				// game.drawLineMap(myUnit.getPosition(), yes, Color.Green);
			 }
		 }
    	 
    	 if(myUnit.isSelected() && myData.isSpellCaster(myUnit)){
    		 Squad sq =  getSquad(myUnit);
    		 if(sq==null){
   			// System.out.println("Unit: " + myUnit.getID() + " Squads is null"); 
    		 }
    		 else {
    			 game.drawLineMap(myUnit.getPosition(), sq.getUnits().get(0).getPosition(), Color.Yellow);
    			 game.drawTextMap(new Position(myUnit.getX() + 40, myUnit.getY() + 10), ""+myUnit.getType().sightRange());
    		 }
    		 

		 
    	 }
    	 
    	 if(myUnit.isSelected() && myUnit.getType().equals(UnitType.Protoss_Pylon)){
    		 int v = getPylonScore(myUnit);
    		 game.drawTextMap(myUnit.getPosition(), "Score: " + v, Text.PlayerYellow);
    	 }
    	 // https://www.youtube.com/watch?v=26SKP1UrEQ0
    	 
//		 if(myUnit.getOrder().equals(Order.AttackUnit)){
//			Unit target = myUnit.getOrderTarget();
//			if(target != null){
//    			Position push = util.GetPushPos2(myUnit, target);
//    			if(push != null){
//    				//game.drawLineMap(myUnit.getPosition(), push, Color.Orange);
//    			}
//
//			}
//		 }
  	  	 
    	 if(!builders.isEmpty()){
	    	 for(Builder b : new ArrayList<Builder>(builders)){
	    		 if(b.worker == null || !b.worker.exists()){
	         			builders.remove(b);
	  	    			BotBase base = getBase(myUnit);
	  	    			placements.remove(b.type);
	  	    			if(base != null){
	  	    				base.unVoidWorker(myUnit);
	  	    				//System.out.println("unVoided worker " + myUnit.getID());
	  	    			}
	         			//System.out.println("Can't build: " + b.type.toString() + " Trying again."); 
	    		 
	    	 }
	       		 game.drawTextMap(b.where.toPosition(), b.type.toString());
	       		 
    	 }
    	 
    	 if(isBuilder(myUnit)){
    		 game.drawLineMap(myUnit.getPosition(), myUnit.getOrderTargetPosition(), Color.White);
    		 Builder build = getBuilder(myUnit);
    		 TilePosition where = build.where;
    		 UnitType type = build.type;
    		 pBuilding p = getPBuilding(type);
    		 BotBase base = getBase(myUnit);
    		 int fc = build.frameCheck;
    		 boolean isVisible = game.isVisible(where);
    		 int dist = myUnit.getPosition().getApproxDistance(where.toPosition());
    		 
		 
    		 if(isVisible == false && myUnit.isMoving() == false && dist > 30){
    			 if(myUnit.isSelected()){
    				 game.drawTextMap(new Position(myUnit.getX() - 5, myUnit.getY()), "NOT VISIBLE");
    			 }
    			 
    			 myUnit.move(where.toPosition());
    			
    		 }
    		 
    		 
    		 if(fc != 0){
    			 if(game.getFrameCount() >= fc && game.canMake(type)){
    				//System.out.println("Out of time to build: " + type.toString());
         			builders.remove(build);
  	    			placements.remove(type);
	    			//sMins = sMins - type.mineralPrice();
	    			//sGas = sGas - type.gasPrice();
  	    			if(base != null){
  	    				base.unVoidWorker(myUnit);
  	    				//System.out.println("unVoided worker " + myUnit.getID());
  	    			}
    			 }
    		 }
    		 
    		 if(isVisible){
    			 
    			 if(myUnit.isSelected()){
    				 game.drawTextMap(new Position(myUnit.getX() - 15, myUnit.getY()), "Dist: " + dist );
    			 }
    			 
    			 if(dist > 120 && !myUnit.isConstructing()){
    				 myUnit.move(where.toPosition());

    			 }
 			 
    			 if(myUnit.isSelected()){
    				 game.drawTextMap(new Position(myUnit.getX() - 5, myUnit.getY()), "Visible");
    			 }
    			 
    			 if(!myUnit.isConstructing() && game.getFrameCount() >= build.frameToTryAgain){
    				 myUnit.build(type, where);
    			 }
    			 		 
	   			 if(strat.buildName != "Meme Proxy Sunken Build"){
	    			 if(!game.canBuildHere(where, type, myUnit) && game.getFrameCount() >= build.frameToTryAgain){
	    				 int width = type.tileWidth(); 
	    				 
	    				 if(width <= 50){
	    					 width = 60;
	    				 }
	    				 
	    				 List<Unit> inTheWay = game.getUnitsInRadius(where.toPosition(), 70);
	    				 int max = inTheWay.size();
	    				 int i = 0;
	    				 util.Print("Max: " + max);
	    				 boolean brek = false;
	    				 for(Unit unit : inTheWay){
	    					 
	    					 if(unit.getPlayer().equals(self) && unit.canMove() && !isInCombat(unit)){
	    						 i++;
	    						 Position run = util.GetKitePos2Pos(unit.getPosition(), where.toPosition());
	    						 if(run != null){
	    							 unit.move(run);
	    						 }
	    					 }
	    					 
	    					 if(unit.getType().isBuilding() || !unit.canMove(true)){
	    			        	builders.remove(build);
	    			 	    	placements.remove(type);
	    			 	    	util.Print("Can't build: " + type.toString() + " Due to: " + unit.getType().toString() + " Being there");
	    					 }
	    				 }
	    				 
		    			if(i>=max){
		    				build.frameToTryAgain = game.getFrameCount() + 50;
		    				util.Print("Trying to build: " + type.toString() + " Again in 50 frames");
		    				return;
		    			}
	    	    		 
		        		builders.remove(build);
		 	    		placements.remove(type);
			    		//sMins = sMins - type.mineralPrice();
			    		//sGas = sGas - type.gasPrice();
		 	    		if(base != null){
		 	    			base.unVoidWorker(myUnit);
		 	    			//System.out.println("unVoided worker " + myUnit.getID());
		 	    		}
	    			 }
	    		 }
	   			 else {
	   				 // if proxy sunken
	   				if(!game.canBuildHere(where, type, myUnit)){
	   					loop:
	   					for(Unit unit : game.getUnitsInRadius(where.toPosition(), 100)){
	   						if(!unit.canMove()){
		   	    				 TilePosition neww = game.getBuildLocation(type, where, 300);
			    				 if(neww != null){
			    					 build.where = neww;
			    					 myUnit.build(type, where);
			    					 util.Print("Saved it :)");
			    					 break loop;
			    				 }
			    				 else{
		   		        			builders.remove(build);
		   		 	    			placements.remove(type);
		   			    			//sMins = sMins - type.mineralPrice();
		   			    			//sGas = sGas - type.gasPrice();
		   		 	    			if(base != null){
		   		 	    				base.unVoidWorker(myUnit);
		   		 	    				//System.out.println("unVoided worker " + myUnit.getID());
		   		 	    			}
	   							}
	   						}
	   					}
	   				}
	   			 }
    			 
    		 }
    		 
    		 
    		 if(!where.isValid(game)){
    			builders.remove(build);
	    		placements.remove(type);
    			//sMins = sMins - type.mineralPrice();
    			//sGas = sGas - type.gasPrice();
	    		util.Print("invalid placement for " + type.toString() + " At: " + where);
    			//System.out.println("invalid placement for " + type.toString() + " At: " + where);
    		 }
    		 
    		 if(base != null){
	    		 if(!base.voidedWorkers.contains(myUnit)){
	    			 base.newVoidedWorker(myUnit);
	    		 }
    		 }
    		 
    		 if(!myUnit.hasPath(where.toPosition())){
     			builders.remove(build);
	    		placements.remove(type);
    			//sMins = sMins - type.mineralPrice();
    			//sGas = sGas - type.gasPrice();
	    		if(base != null){
	    			base.unVoidWorker(myUnit);
	    		}
	    		util.Print("No path to construct: " + type.toString() + " At: " + where);
    			//System.out.println("No path to construct: " + type.toString() + " At: " + where);
    		 }
    		 
	 
    	 }
    	 
    	 if(isBuilder(myUnit) && myUnit.getType().isBuilding()){
    		 Builder build = getBuilder(myUnit);
    		 if(build != null){
    			 builders.remove(build);
    		 }
    	 }
    	 
    	 }
    	 
    	 

    		 
    	 if(scouter != null){
    		 
    		 if(scouter.exists()){
	    		 game.drawCircleMap(scouter.getPosition(), 60, Color.Yellow);
		    	 if(!scouter.isIdle()){
		    	 game.drawLineMap(scouter.getPosition(), scouter.getOrderTargetPosition(), Color.White);
		    	 }
		    	 
		    	 if(scouter.isGatheringMinerals()){
		    		 scouter.stop();
		    	 }
		    	 
		    	 if(scouter.isAttacking() || scouter.getOrder().equals(Order.AttackUnit)){
		    		 scouter.stop();
		    	 }
		    	 
    		 }
 					
    	 }
    	 	    	 
    	 if(scouter != null){
    		 if(scouter.exists()){
    			 Unit attacker = null;
		    	 boolean isBeingAttacked = false;
		    	 
		    	 if(game.getFrameCount() >= scoutCheck){
		    		 scoutCheck = game.getFrameCount() + 4;
			    	 for(Unit unit : game.getUnitsInRadius(scouter.getPosition(), 320)){
			    		 if(unit.getOrder().equals(Order.AttackUnit) || unit.getOrder().equals(Order.AttackMove)){
			    			 Unit target = unit.getOrderTarget();
			    			 if(target != null){
			    				 if(target.equals(scouter)){
			    					 isBeingAttacked = true;
			    					 attacker = unit;
			    					 break;
			    					 //System.out.println("Worker is being attacked");
			    				 }
			    			 }
	
			    		 }
			    	 }
			    	 
			    	 if(!isBeingAttacked){
				    	 for(FogUnit f : new ArrayList<>(fogUnits)){
				    		 if(f.pos != Position.Unknown){
				    			 if(scouter.getPosition().getApproxDistance(f.pos) <= f.type.groundWeapon().maxRange() && scouter.getLastCommandFrame() != game.getFrameCount()){
	
				    				 util.reteatUnit(scouter);
				    				 game.drawTextMap(scouter.getPosition(), "!", Text.Red);
				    				 break;
				    			 }
				    		 }
				    		 else {
				    			 continue;
				    		 }
				    	 }
			    	 }
		    	 }
		    	 
		 		if(isBeingAttacked && scouter.getType().isWorker() && scouter.getLastCommandFrame() != game.getFrameCount()){
					if(attacker != null){
						game.drawTextMap(scouter.getPosition(), "!", Text.Blue);
						Position pos = util.GetKitePos2(scouter, attacker);
						if(pos != null){
						game.drawLineMap(scouter.getPosition(), pos, Color.Yellow);
						if(!game.isWalkable(pos.toWalkPosition())){
							//util.Print("Not walkable");
							 bwapi.Region r = myData.getNearestWalkableRegion(pos);
							 if(r != null){
								 pos = r.getCenter();
							 }
							 
						}
						if(scouter.getLastCommandFrame() != game.getFrameCount()){
							scouter.move(pos);
							}
						}
						else {
							if(scouter.getLastCommandFrame() != game.getFrameCount()){
								scouter.move(self.getStartLocation().toPosition(), false);
							}
						}
					}
		 		}
		 		
		 		if(!isBeingAttacked && scouter.getType().isWorker() && scouter.getLastCommandFrame() != game.getFrameCount()){
		    		Position tt = util.scouterPriorityTask(scouter);
		    		if(tt != null && !isDoingTheMeme(scouter)){
		    		scouter.move(tt);
		    		}
		    		
		    		if(!scouter.isMoving() && !isDoingTheMeme(scouter)){
			    		 ArrayList<Position> task = util.scouterNextTasking(scouter);
			    		 if(task != null){
				    		for(Position pos : task){
				    			if(scouter.isMoving()){
				    			scouter.move(pos);
				    			}
				    			else {
				    				scouter.move(pos, true);
				    			}
				    		}
			    		 }
		    		}
		    		
		    		if(scouter.isMoving() && !isDoingTheMeme(scouter)){	
		    			if(currentTarget != null){
		    				if(!currentTarget.attackPositions.isEmpty()){
				    			Position yes = currentTarget.attackPositions.get(0);
				    			if(yes != null){
				    				if(scouter.getOrderTargetPosition().getApproxDistance(yes) > 3500){
				    					scouter.stop();
				    				}
				    			}
		    				}
		    			}
		    			
		    		}
		    		
		 		}
		 		
		 		
    		}
    		
    	 }

    	   	 
    	 if(myUnit.getType().isWorker() && myUnit.isUnderAttack() == true && defenceCheck < game.getFrameCount()){
    		 defenceCheck = game.getFrameCount() + 24;
			 ArrayList<Unit> enemy = GetEnemyUnitsNearby(myUnit.getPosition(), 400, true);
			 Position pos = null;
			 if(!enemy.isEmpty()){
				 pos = enemy.get(0).getPosition();
			 }
			 else {
				 pos = myUnit.getPosition();
			 }
			 
    		 if(scouter == null){
    			 DefenceCall(pos);
    		 }
    		 else {
	    		 if(myUnit != scouter){
	    			 DefenceCall(pos);
	    		 }
    		 }
    	 }
    	 
    	 if(myUnit.getType().isBuilding() && myUnit.isUnderAttack() == true && defenceCheck < game.getFrameCount()){
    		 defenceCheck = game.getFrameCount() + 24;
			 ArrayList<Unit> enemy = GetEnemyUnitsNearby(myUnit.getPosition(), 400, true);
			 Position pos = null;
			 if(!enemy.isEmpty()){
				 pos = enemy.get(0).getPosition();
			 }
			 else {
				 pos = myUnit.getPosition();
			 }
    		 DefenceCall(pos);
    	 }
    	 
    	 if(scouter!=null && myUnit.equals(scouter) && myUnit.exists()){
    		 BotBase bass = getBase(myUnit);
    		 if(bass != null){
	    		 if(!bass.voidedWorkers.contains(myUnit)){
	    			 bass.newVoidedWorker(myUnit);
	    		 }
    		 }
    	 }
    	 
    	 
    	 if(myUnit.getType().equals(UnitType.Terran_Factory) && myUnit.getAddon() == null){
    		 if(myUnit.canBuildAddon(UnitType.Terran_Machine_Shop)){
				myUnit.buildAddon(UnitType.Terran_Machine_Shop);
    		 }
    	 }
    	 
       	 if(myUnit.getType().equals(UnitType.Terran_Starport) && myUnit.getAddon() == null){
    		 if(myUnit.canBuildAddon(UnitType.Terran_Control_Tower)){
				myUnit.buildAddon(UnitType.Terran_Control_Tower);
    		 }
    	 }
       	 
    	 if(myUnit.getType().equals(UnitType.Terran_Command_Center) && myUnit.getAddon() == null){
    		 if(self.allUnitCount(UnitType.Terran_Comsat_Station) <= 2){
	    		 if(myUnit.canBuildAddon(UnitType.Terran_Comsat_Station)){
					myUnit.buildAddon(UnitType.Terran_Comsat_Station);
	    		 }
    		 }
    		 else {
	    		 if(myUnit.canBuildAddon(UnitType.Terran_Nuclear_Silo)){
					myUnit.buildAddon(UnitType.Terran_Nuclear_Silo);
	    		 }
	    		  
    		 }
    	 }
    	 
    	 if(myUnit.getType().equals(UnitType.Terran_Science_Facility) && myUnit.getAddon() == null){
    		 
    		 if(buildMainGoal.contains(UnitType.Terran_Battlecruiser)){
    			 if(self.allUnitCount(UnitType.Terran_Physics_Lab) == 0){
		    		 if(myUnit.canBuildAddon(UnitType.Terran_Physics_Lab)){
						myUnit.buildAddon(UnitType.Terran_Physics_Lab);
		    		 }
    			 }
    			 else {  
    	    		 if(myUnit.canBuildAddon(UnitType.Terran_Covert_Ops)){
    					myUnit.buildAddon(UnitType.Terran_Covert_Ops);
    		    	 }
    			 }
    		 }
    		 else {
    			 
    			 if(self.allUnitCount(UnitType.Terran_Covert_Ops) == 0){
    	    		 if(myUnit.canBuildAddon(UnitType.Terran_Covert_Ops)){
    					myUnit.buildAddon(UnitType.Terran_Covert_Ops);
    	    		 }
    			 }
    			 else {
		    		 if(myUnit.canBuildAddon(UnitType.Terran_Physics_Lab)){
						myUnit.buildAddon(UnitType.Terran_Physics_Lab);
		    		 }
    			 }

    		 }
    	 }
    		

    	 //end of my units
     }
     
	     for(Unit unit : new ArrayList<Unit>(simmedUnits.keySet())){
	    	 if(simmedUnits.get(unit) <= game.getFrameCount()){
	    		 simmedUnits.remove(unit);
	    	 }
	     }
	     
	     for(Player plyy : game.enemies()){
		     for(Unit unit : plyy.getUnits()){
				BotPlayer ply = getPlayer(unit.getPlayer());
				
		    	 if(unit.isCloaked() || unit.isBurrowed()){
		    		 game.drawTextMap(unit.getPosition(), "" + unit.getType().toString());
		    	 }
		    	 
		    	     	 
		    	 if(unit.getType().equals(UnitType.Terran_Vulture_Spider_Mine)){
		    		 ply.registerTech(TechType.Spider_Mines);
		    	 }
		    	 
		    	 if(unit.isStimmed()){
		    		 ply.registerTech(TechType.Stim_Packs);
		    	 }
		    	 
		    	 if(unit.isSieged()){
		    		 ply.registerTech(TechType.Tank_Siege_Mode);
		    	 }
		    	 
		    	 if(unit.isCloaked() && unit.getType().equals(UnitType.Terran_Wraith)){
		    		 ply.registerTech(TechType.Cloaking_Field);
		    	 }
		    	 
		    	 if(unit.isCloaked() && unit.getType().equals(UnitType.Terran_Ghost)){
		    		 ply.registerTech(TechType.Personnel_Cloaking);
		    	 }
		    	 
		    	 if(unit.getType().equals(UnitType.Protoss_Dragoon)){
		    		 ply.registerUpgrade(UpgradeType.Singularity_Charge);
		    	 }
	    	 	    	     	 
		    	 if(unit.getType().equals(UnitType.Terran_Bunker) ||
		    		unit.getType().equals(UnitType.Terran_Dropship) ||
		    		unit.getType().equals(UnitType.Zerg_Overlord) ||
		    		unit.getType().equals(UnitType.Protoss_Shuttle)){
		    		 if(!unit.getLoadedUnits().isEmpty()){
		    			 game.drawTextMap(unit.getPosition(), "Size: " + unit.getLoadedUnits().size());
		    		 }
		    	 }
		    		
		    	 
		 		if(game.enemies().contains(unit.getPlayer())){

					if(ply != null){
						if(IsMilitrayUnit(unit) && !IsMilitrayBuilding(unit)){
							ply.newMilUnit(unit);			
						}
						
						if(IsMilitrayBuilding(unit) && !IsMilitrayUnit(unit) ){
							ply.newEnemyBuilding(unit);
							
						}
						if(unit.getType().isBuilding() && !IsMilitrayUnit(unit) && !IsMilitrayBuilding(unit)){
							ply.newEnemyBuilding(unit);
						}
						
					}
				}
		 		
		 		if(self.allUnitCount(UnitType.Terran_Comsat_Station) > 0){
				 		if(unit.isCloaked() || unit.isBurrowed()){
				 			if(isInCombat(unit) || unit.isMoving() || unit.getOrder().equals(Order.AttackMove)){
				 				ArrayList<Unit> sats = util.getAllOf(UnitType.Terran_Comsat_Station);
				 				if(!myData.hasScannedNearby(unit.getPosition()) && myData.FriendlyUnitsNearby(unit.getPosition(), 330) && !unit.getType().equals(UnitType.Protoss_Observer)){
					 				for(Unit sat : sats){
					 					if(sat.getEnergy() >= 50){
											sat.useTech(TechType.Scanner_Sweep, unit.getPosition());
											myData.newScan(unit.getPosition());
											break;
					 					}
					 				}
				 				}
				 			}
				 		}
				 		
				 if(myData.isInCombat(unit)){
				 	if(!game.isVisible(unit.getTilePosition()) && game.getRegionAt(unit.getPosition()).isHigherGround()){
				 		ArrayList<Unit> sats = util.getAllOf(UnitType.Terran_Comsat_Station);
				 		if(!myData.hasScannedNearby(unit.getPosition())){
					 		for(Unit sat : sats){
					 			if(sat.getEnergy() >= 50){
									sat.useTech(TechType.Scanner_Sweep, unit.getPosition());
									myData.newScan(unit.getPosition());
									break;
					 			}
					 		}
				 		}
				 	}
				 }	
		 		}
		 		
		 		if(IsMilitrayUnit(unit) || IsMilitrayBuilding(unit) || util.isSpellCaster(unit.getType())){
					FogUnit yes = getFogUnit(unit.getID());
					if(yes == null){
						fogUnits.add(new FogUnit(unit, unit.getType(), unit.getID()));
						// if doesn't exist
					}
					else {
						// if not
						yes.update(unit);
						yes.lastSeen = game.getFrameCount();
		    			yes.setToUnknownAt = game.getFrameCount() + 150; // will keep updating til it's no longer seen
					}
					
					
					if(myData.isInCombat(unit)){
						Unit e = unit;
						if(util.getUnitTarget(e) != null && util.isMelee(unit.getType())){
							Unit yep = util.getUnitTarget(e);
							//util.Print("Is doing things " + e.getType().toString());
							if(yep != null){
								game.drawLineMap(unit.getPosition(), yep.getPosition(), Color.Black);
								//util.Print("Dist: " + yep.getPosition().getApproxDistance(e.getPosition()));
								if(yep.getPosition().getApproxDistance(e.getPosition()) <= 150){
									//util.Print("Valid unit " + e.getType().toString());
									if(yep.getPlayer().equals(self) && self.topSpeed(yep.getType()) >= e.getPlayer().topSpeed(e.getType())){
										// if targetting me
										int dmg = game.getDamageTo(yep.getType(), e.getType(), yep.getPlayer(), e.getPlayer());
										//util.Print("Dmg: " + dmg);
										if(dmg != 0){
											if(yep.getHitPoints() <= dmg){
												// RUN AWAY
												util.retreatFrom(yep, e);
												myData.DND(yep, 15);
												//util.Print("Unit: " + yep.getType().toString() + " Fleeing from: " + e.getType().toString() + " Due to OHS reasons");
					    						if(getShitTalkersSize() <= 20 && !isAShitTalker(unit) && drawMoodYes){
					    							drawMood(unit, 4);
					    						}
											}
										}
									}
								}
							}
						}
					}
					
		 		}
		 		
		 		
		 		if(unit.getType().equals(UnitType.Terran_Vulture_Spider_Mine) && !unit.isIdle()){
		 			Unit target = util.getUnitTarget(unit);
		 			if(target != null){
		 				Unit poorBitch = util.getMineDragTarget(target.getPosition(), 300);
		 				if(poorBitch != null){
		 					if(unit.canMove(true)){
		 						target.move(poorBitch.getPosition());
		 						util.Print("Unit: " + target.getID() + " Is Suiciding into: " + poorBitch.getID());
		 						game.drawLineMap(target.getPosition(), poorBitch.getPosition(), Color.White);
		 						game.drawTextMap(target.getPosition(), ":D", Text.White);
		 						myData.DND(target, Math.round(target.getPosition().getApproxDistance(poorBitch.getPosition())));
		 						if(target.getType().equals(UnitType.Terran_Marine) ||target.getType().equals(UnitType.Terran_Firebat)  ){
		 							if(!target.isStimmed() && target.canUseTech(TechType.Stim_Packs)){
		 								target.useTech(TechType.Stim_Packs);
		 							}
		 						}
		 					}	
		 					
		 				}
		 			}
		 			
		 		} // end of minedrag
	    	 
		    	
		     } // end of enemyUnits
	     
	     }
	     
	     
	     
	     myData.players = players;
	     // end on onFrame
	     
	     
	     for(BotPlayer ply : players){
	    	 for(Unit unit : ply.units){
	    		 if(!ply.player.getUnits().contains(unit)){
	    			 ply.unitDeath(unit);
	    			 //System.out.println("Caught possibly dead unit");
	    		 }
	    	 }
	     }
	     
     }
          
    

    public static void main(String[] args) {
        new Bot();
        if(args != null){
	        for(String str : args){
	        	if(str.equals("cheats")){
	        		cheats = true;
	        	}
	        }
        }
    }

	@Override
	public void onEnd(boolean arg0) {
		game.sendText("GG! " + game.enemy().getName());
	}


	@Override
	public void onUnitComplete(Unit unit) {
		
		if(unit.getPlayer().equals(self)){
			
			if(unit.getType().equals(UnitType.Zerg_Spire) && currentTarget.race.equals(Race.Zerg)){
				UQ.clear();
			}
						
			if(util.shouldResetUQ(unit.getType())){
				UQ.clear();
				game.sendText("Just letting you know that i have a new thing");
			}
			
			if(unit.getType().equals(UnitType.Terran_Science_Facility)){
				if(unit.getAddon() != null){
					if(unit.getAddon().equals(UnitType.Terran_Physics_Lab)){
						util.addReserter(UnitType.Terran_Physics_Lab);
						UQ.clear();
						game.sendText("Just letting you know that my new thing is WAY better that the usual things.");
					}
				}
			}
			
			if(unit.getType().equals(UnitType.Terran_Factory)){
				if(unit.getAddon() != null){
					if(unit.getAddon().equals(UnitType.Terran_Machine_Shop)){
						util.addReserter(UnitType.Terran_Machine_Shop);
						UQ.clear();
						game.sendText("Just letting you know that my new thing is meta and is very boring");
					}
				}
			}
				
    		if(!unit.getType().isSpell() && IsSquadUnit(unit)){
    			assignUnit(unit);
    		}
    		// fuck this bot
    		
    		if(util.isSpellCaster(unit.getType())){
    			if(getSquad(unit) == null){
	    			assignUnit(unit);
	    			if(!spellCastersContains(unit)){
	    				casters.add(new Spellcaster(unit, myData, util));
	    			}
    			}
    		}
    			//  myChokes.get(1).getCenter().toTilePosition();
    		  		
    		if(unit.getType().isDetector() && !unit.getType().isBuilding()){
    			assignDetecter(unit);
    		}
    		
    		if(retreaters.contains(unit) && unit.getType().equals(UnitType.Terran_Siege_Tank_Tank_Mode)){
    			util.reteatUnit(unit);
    		}
    		
    		if(unit.getType().isResourceDepot() && !unit.getType().equals(UnitType.Zerg_Lair) && !unit.getType().equals(UnitType.Zerg_Hive)){
    			BotBase bass = getBaseDepot(unit);
    			if(bass != null){
    				bass.DepotFinished();
    				//util.Print("Base: " + myBases.indexOf(bass) + " Is done");
    			}
    		}
    		
    		if(unit.getType().equals(UnitType.Zerg_Lurker)){
    			Squad sq = getSquad(unit);
    			if(sq != null){
	    			if(!spellCastersContains(unit)){
	    				casters.add(new Spellcaster(unit, myData, util));
	    			}
    			}
    			else {
    				assignUnit(unit);
	    			if(!spellCastersContains(unit)){
	    				casters.add(new Spellcaster(unit, myData, util));
	    			}
    			}
    		}
    		
  				
		}
		

	}

	@Override
	public void onUnitDestroy(Unit unit) {
		
		if(myData.isBlockingMineral(unit)){
			for(BotBase bass : new ArrayList<>(myBases)){
				if(bass.hasBlockingMin(unit)){
					bass.blockingMineralDestroy(unit);
				}
			}
		}
		
		if(unit.getType().equals(UnitType.Zerg_Egg)){
			if(unit.getBuildType() == UnitType.Zerg_Overlord){
				if(overLordsMorphing > 0){
					overLordsMorphing--;
				}
			}
		}
		
		if(isAShitTalker(unit)){
			ShitTalk st = getShitTalker(unit);
			if(st != null){
				shitTalkers.remove(st);
			}
		}
		
		if(unit.getPlayer().equals(self)){
			if(unit.getType().equals(UnitType.Terran_SCV)){
				if(unit.getOrder().equals(Order.ConstructingBuilding)){
					Unit target = unit.getOrderTarget();
					Unit worker = getWorkerNearest(unit.getPosition());
					if(worker != null){
						worker.rightClick(target);
					}
				}
			}
		}
		
		if(game.enemies().contains(unit.getPlayer())){
			BotPlayer ply = getPlayer(unit.getPlayer());
			if(ply != null){
			// don't ask why
			// but if you do it because i don't want bugs
			// :bugs:
			ply.newUnitToUnCount(unit);
			}
		}
		
		if(IsMilitrayUnit(unit) || util.isSpellCaster(unit.getType())){
			Squad sq = getSquad(unit);
			
			if(sq!=null){
				sq.unitDeath(unit);
			}
			
			myData.unitDeath(unit);
			
			BotPlayer ply = getPlayer(unit.getPlayer());
			
			if(ply != null){
			ply.unitDeath(unit);
			}
			
			FogUnit yes = getFogUnit(unit.getID());
			if(yes != null){
				fogUnits.remove(yes);
			}
			
		}
		
		if(game.enemies().contains(unit.getPlayer()) && unit.getType().isWorker()){
			BotPlayer ply = getPlayer(unit.getPlayer());
			if(ply != null){
			ply.WorkerDeath();
			}
		}
		
		if(unit.getType().equals(UnitType.Spell_Scanner_Sweep)){
			if(myData.Scans.contains(unit.getPosition())){
			myData.Scans.remove(unit.getPosition());
			}
		}
		
		if(unit.getType().isDetector() && unit.getPlayer().equals(self)){
			Squad sq = getSquad(unit);
			if(sq!=null){
				AssignDetector(sq);
			}
		}
		
		if(unit.getType().isResourceDepot() && unit.getPlayer().equals(self)){
			BotBase remove = getBaseDepot(unit);
			if(remove != null){
				ArrayList<Unit> pawns = remove.Pawns;
				remove.EmptyPawns();
				myBases.remove(remove);
				for(Unit unittt : pawns){
					//util.Print("Base destroy assign");
					assignWorkerToBase(unittt);
				}
			}
			
			if(pBuildings.contains(self.getRace().getRefinery())){
				pBuildings.remove(self.getRace().getRefinery());
			}

		}
		
		if(unit.getType().isResourceDepot() && game.enemies().contains(unit.getPlayer())){
			BotPlayer ply = getPlayer(unit.getPlayer());
			if(ply != null){
				Base bass = getClosestBaseLocation(unit.getPosition());
				if(ply.Bases.contains(bass)){
					ply.Bases.remove(bass);
					for(ChokePoint cp : bass.getArea().getChokePoints()){
						ply.RemoveChokePoint(cp);
					}
				}
			}
		}
			
		
		if(game.enemies().contains(unit.getPlayer()) && unit.getType().isBuilding()){
			BotPlayer ply = getPlayer(unit.getPlayer());
			if(ply != null){
				ply.unitDeath(unit);
			}
			
			if(IsMilitrayBuilding(unit)){
				FogUnit f = getFogUnit(unit);
				if(f != null){
					if(fogUnits.contains(f)){
						fogUnits.remove(f);
					}
					
				}
			}
			
			// Bot doesnt delete destroyed fog units buildings but the fix above may fix it just double check
			// check p and z for any issues and upload :)
			
		}
		
		if(unit.getType().isMineralField()){
			BotBase bass = getBaseThatHasMineral(unit);
			if(bass != null){
				bass.MineralDeplete(unit);
			}
		}
		
		if(unit.getType().isWorker() && unit.getPlayer().equals(self)){
			
			if(unit.getType().equals(UnitType.Terran_SCV)){
				if(isRepairer(unit)){
					Repairer r = getRepairer(unit);
	 				repairs.remove(r);
				}
				
			}
			
			
		}
		
		if(unit.getType().isBuilding() && unit.getType().getRace().equals(Race.Terran)){
			if(isBeingRepaired(unit)){
				Repairer r = getRepairerTarget(unit);
				Unit worker = r.unit;
				BotBase bass = getBase(worker);
				if(bass != null){
					bass.unVoidWorker(worker);
				}
				repairs.remove(r);
			}
		}
		
		
		if(getBuilder(unit) != null){
			Builder b = getBuilder(unit);
			if(placements.contains(b.type)){
				placements.remove(b.type);
			}
			//sMins = sMins - b.type.mineralPrice();
			//sGas = sGas - b.type.gasPrice();
			builders.remove(b);
		}
		
		if(shouldRebuild(unit.getType()) && unit.getPlayer().equals(self)){
			
			
			if(unit.getType().equals(UnitType.Zerg_Lair)){
				stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Lair, myData, true));	
			}
			else if(unit.getType().equals(UnitType.Zerg_Hive)){
				stuffQueue.add(new BotTech(3, 1, TechType.None, UpgradeType.None, UnitType.Zerg_Hive, myData));
			}
			else {
				findNicePlaceToPutToPBuildings(unit.getType(), null, 300, false);
			}
			
		}
		

					
	}
	
    boolean shouldRebuild(UnitType type) {
    	// why did it put it here
    	// but anyways
    	if(type.equals(UnitType.Zerg_Spawning_Pool) || type.equals(UnitType.Zerg_Spire)  || type.equals(UnitType.Zerg_Hydralisk_Den) 
    	|| type.equals(UnitType.Zerg_Queens_Nest)  || type.equals(UnitType.Terran_Factory)  || type.equals(UnitType.Terran_Starport) || type.equals(UnitType.Terran_Engineering_Bay)  
    	 || type.equals(UnitType.Terran_Barracks) ||  type.equals(UnitType.Protoss_Cybernetics_Core) || type.equals(UnitType.Protoss_Citadel_of_Adun)  || type.equals(UnitType.Protoss_Templar_Archives) 
    	 || type.equals(UnitType.Protoss_Gateway) ||  type.equals(UnitType.Zerg_Defiler_Mound) || type.equals(UnitType.Zerg_Ultralisk_Cavern) ||
    	 type.equals(UnitType.Zerg_Lair) || type.equals(UnitType.Zerg_Hive)){
    		return true;
    		
    	}
    	
		return false;
	}


	@Override
    public void onUnitCreate(Unit unit) {
    	
		if(placements.contains(unit.getType())){
			pBuilding p = getPBuilding(unit.getType());
			if(p != null){
				//util.Print("removing index: " + pBuildings.indexOf(p));
				if(p.save == 1){
					sMins = 0;
					sGas = 0;
				}
				pBuildings.remove(p);
    			placements.remove(unit.getType());

			}
			else {
				if(!pBuildings.isEmpty()){
					if(pBuildings.get(0).type.equals(unit.getType())){
						pBuilding pp = pBuildings.get(0);
						//util.Print("removing index: " + pBuildings.indexOf(p) + " Of " + unit.getType().toString());
						if(pp.save == 1){
	    					sMins = 0;
	    					sGas = 0;
						}
						pBuildings.remove(0);
		    			placements.remove(unit.getType());

					}
				}
			}
			
			Builder b = getBuilder(unit.getType(), unit.getTilePosition());
			if(b != null){
				BotBase bass = getBase(b.worker);
				if(bass != null){
					bass.unVoidWorker2(b.worker);
				}
				builders.remove(b);
			}
			
		}
    	

		if(IsMilitrayUnit(unit)){
			myData.newMilUnit(unit);
			BotPlayer ply = getPlayer(unit.getPlayer());
			if(ply != null){
				ply.newMilUnit(unit);
			}
		}
		
		
		if(unit.getType().equals(UnitType.Protoss_Photon_Cannon) && unit.getPlayer().equals(self) && globalRetreat.equals(self.getStartLocation().toPosition())){
			for(Squad sq : Squads){
				sq.retreatPos = unit.getPosition();
			}
			globalRetreat = unit.getPosition();
		}
		
		


		if(unit.getPlayer().equals(self) && isProductionBuilding(unit.getType()) && !Production.contains(unit)){
			Production.add(unit);
		}
    	  	
    	if(unit.getPlayer().equals(self)){
    		UnitType type = unit.getType();
    		// if e
    		if(unit.getType().isResourceDepot()){
    			Base bass = getClosestBaseLocation(unit.getPosition());
    			if(!alreadyBasedHere(bass)){
	    			BotBase yes = new BotBase(game, unit, bass);
	    			myBases.add(yes);
	    			Production.add(unit);
	    			myData.updateNextExpansion(unit.getPosition());
	    			TransferWorkersTo(yes, 6);
	    			if(myBases.size() != 1){
			    		for(int i = 0; i < bass.getGeysers().size(); i++){
				    		if(game.getFrameCount() >= 8000){
				    			pBuildings.add(0, new pBuilding(self.getRace().getRefinery(), null));
				    		}
				    		else {
				    			pBuildings.add(1, new pBuilding(self.getRace().getRefinery(), null));
				    		}
				    		
				    		
			    		}
			    		
	    			}
	    			
	    			if(myBases.size() == 2 && moveDefencePos == true){
	    				for(Squad sq : Squads){
	    					sq.retreatPos = myData.getDefencePos().toPosition();
	    				}
	    				globalRetreat = myData.getDefencePos().toPosition();
	    			}
	    			
	    			
//	    			for(Geyser g : bass.getGeysers()){
//	    				if(!myData.geysers.keySet().contains(g.getUnit())){
//	    					myData.geysers.put(g.getUnit(), g.getUnit().getTilePosition());
//	    				}
//	    			}
    			}
    			
    			if(self.getRace().equals(Race.Protoss) && myBases.size() > 2 && self.completedUnitCount(UnitType.Protoss_Forge) > 0){
    				pBuildings.add(0, new pBuilding(UnitType.Protoss_Pylon, unit.getTilePosition(), 300));
    				pBuildings.add(1, new pBuilding(UnitType.Protoss_Photon_Cannon, unit.getTilePosition(), 45, true));
    				pBuildings.add(2, new pBuilding(UnitType.Protoss_Photon_Cannon, unit.getTilePosition(), 45, true));
    				pBuildings.add(3, new pBuilding(UnitType.Protoss_Photon_Cannon, unit.getTilePosition(), 45, true));
    			}
    			

    		}
    		 
    		
	
//	    		if(pBuildings.size() == 1){
//	    			pBuildings.clear();
//	    			placements.remove(unit.getType());
//	    			sMins = 0;
//	    			sGas = 0;
//	    		}
//	    		else {
//	    			if(!pBuildings.isEmpty()){
//			    		if(pBuildings.get(0).type.equals(type) && placements.contains(type)){
//			    			pBuildings.remove(0);
//			    			placements.remove(unit.getType());
//			    			sMins = 0;
//			    			sGas = 0;
//			    		}
//	    			}
//	    			placements.remove(unit.getType());
//	    			sMins = 0;
//	    			sGas = 0;
//	    		}
    				
    		if(scouter == null && unit.getType().isWorker() && game.getFrameCount() < 300){
    			scouter = unit;
    			BotBase bass = getBase(unit);
    			if(bass!=null){
    				bass.newVoidedWorker(unit);
    			}
    		}
    		  				
    		if(unit.getType().isWorker() && unit.getPlayer().equals(self)){
    			if(!assignedToBase(unit)){
    			//util.Print("OnUnitCreate assign unit");
    			assignWorkerToBase(unit);
    			}
    		}
    		
    		if(util.isSpellCaster(unit.getType())){
    			if(!spellCastersContains(unit)){
    				casters.add(new Spellcaster(unit, myData, util));
    			}
    			Squad sq = getSquad(unit);
    			if(sq == null){
    				assignUnit(unit);
    			}
    		}
    		
    		if(unit.getType().equals(UnitType.Terran_Bunker) ){
    			for(Squad sq : Squads){
    				sq.retreatPos = unit.getPosition();
    			}
    			globalRetreat = unit.getPosition();
    		}
    		
    		
    		if(type.equals(UnitType.Terran_Vulture_Spider_Mine)){
				for(Unit unitt : game.getUnitsInRadius(unit.getPosition(), 150)){
					
					if(unitt.getType().isBuilding()){
						continue;
					}
					
					if(unitt.getPlayer().equals(self) && unitt.canMove()){
					//System.out.println("Unit found: " + unit.getID());
					Position pos = util.GetKitePos2(unitt, unit);
					if(pos != null){
						//System.out.println("Not null");
						if(unitt.getType().equals(ShootyBoy) || unitt.getType().equals(UnitType.Terran_Firebat)){
							if(!unitt.isStimmed() && unit.canUseTech(TechType.Stim_Packs)){
								unitt.useTech(TechType.Stim_Packs);
							}
						}
						unitt.move(pos);
						myData.DND(unitt, game.getFrameCount() + 30);
						game.drawLineMap(unitt.getPosition(), pos, Color.Black);
						game.drawTextMap(unitt.getPosition(), "!");
						//System.out.println("Stuff happened");
						}
					 }
				 }
    		}
    		
    		
    		if(unit.getType().isBuilding()){
    			
    			// PYLON SCORES
    			// unit is built unit
    			// p is a list of units that could be pylons. 
    			
    			List<Unit> p = game.getUnitsInRadius(unit.getPosition(), 200);

   			
    			int size = 0;
    			
    			if(unit.getType().equals(UnitType.Protoss_Stargate) || unit.getType().equals(UnitType.Protoss_Gateway) || unit.getType().equals(UnitType.Protoss_Cybernetics_Core) 
    			|| unit.getType().equals(UnitType.Protoss_Nexus) || unit.getType().equals(UnitType.Protoss_Templar_Archives) ){
    				size = 5;
    			}
    			
    			if(unit.getType().equals(UnitType.Protoss_Citadel_of_Adun) || unit.getType().equals(UnitType.Protoss_Robotics_Facility) || unit.getType().equals(UnitType.Protoss_Observatory) 
    			|| unit.getType().equals(UnitType.Protoss_Shield_Battery) || unit.getType().equals(UnitType.Protoss_Robotics_Support_Bay) ){
    				size = 3;
    			}
    			
    			if(size == 0){
    				size = 2;
    			}
    			

    			//util.Print("SIZE FOR BUILDING: " + size);
    			

    			for(Unit u : p){
    				if(u.getType().equals(UnitType.Protoss_Pylon) && u.getPlayer().equals(self)){
    					updatePylonScore(u, size);
    					break;
    				}
    			}
    		}
    		
    		// end of my units creation
    	}
    	
    	
    	
    }
    

	@Override
	public void onUnitDiscover(Unit unit) {
		
    	if(game.enemies().contains(unit.getPlayer())){
    		
    		BotPlayer ply = getPlayer(unit.getPlayer());
    				
    		if(unit.getType().isResourceDepot()){
    			for(Base basss : bewb.getMap().getBases()){
    				if(unit.getPosition().getApproxDistance(basss.getCenter()) < 60){
    		    		if(ply != null){
    			    		ply.newEnemyBase(basss);
    					    for(ChokePoint cp : basss.getArea().getChokePoints()){
    					    ply.NewChokePoint(cp);
    				        }
    		    		}
    		    		break;
    				}
    			}

    		}
    		
			if(unit.getType().equals(UnitType.Protoss_Dark_Templar) || unit.getType().equals(UnitType.Zerg_Lurker)){
				ArrayList<UnitType> items = getNextItems(2);
				boolean has = false;
				
				for(UnitType type : util.getDetectors()){
					if(self.allUnitCount(type) > 0 || amountInUQ(type) > 0){
						has = true;
					}
				}
				
				if(items == null){
					return;
				}
				
				if(!has){
					for(UnitType type : items){
						if(type.isDetector()){
							has = true;
						}
					}
					
					for(UnitType type : UQ){
						if(type.isDetector()){
							has = true;
						}
					}
					
					if(self.allUnitCount(UnitType.Terran_Comsat_Station) > 0){
						has = true;
					}
				}
				
				if (self.allUnitCount(UnitType.Terran_Science_Facility) > 0 || placements.contains(UnitType.Terran_Science_Facility)){
					has = true;
					if(!UQ.contains(UnitType.Terran_Science_Vessel)){
						for(int i = 0; i<Squads.size();i++){
						UQ.add(UnitType.Terran_Science_Vessel);
						}	
					}

				}
				

				if(!has){
					if(self.getRace().equals(Race.Terran)){
						findNicePlaceToPutToPBuildings(UnitType.Terran_Engineering_Bay, null, 200, true);
						findNicePlaceToPutToPBuildings(UnitType.Terran_Missile_Turret, self.getStartLocation(), 300, true);
						findNicePlaceToPutToPBuildings(UnitType.Terran_Missile_Turret, myData.defencePos, 300, true);
						game.sendText("REEEEEEEEEEEEE " + unit.getType().toString());
						util.Print("Unit: " + unit.getType().toString() + " Detected. Adding lots of stuff");
					}
					
					if(self.getRace().equals(Race.Protoss)){
						findNicePlaceToPutToPBuildings(UnitType.Protoss_Forge, null, 300, true);
						findNicePlaceToPutToPBuildings(UnitType.Protoss_Photon_Cannon, self.getStartLocation(), 300, true);
						game.sendText("REEEEEEEEEEE " + unit.getType().toString());
						util.Print("Unit: " + unit.getType().toString() + " Detected. Adding lots of stuff");
					}
					
					// zerg doesn't need shieettt )))

				}
			}
		
			if(ply != null){

				ply.newUnitToCount(unit);

				if(IsMilitrayUnit(unit) && !IsMilitrayBuilding(unit)){
					ply.newMilUnit(unit);
				}
				if(IsMilitrayBuilding(unit) && !IsMilitrayUnit(unit) ){
					ply.newDBuilding(unit);
				}
				if(unit.getType().isBuilding() && !IsMilitrayUnit(unit) && !IsMilitrayBuilding(unit)){
					ply.newEnemyBuilding(unit);
				}
				
  		
			}
			
			
			if(unit.getType().equals(UnitType.Protoss_Zealot)){
				BotPlayer p = myData.getBotPlayer(unit.getPlayer());
				if(p != null){
					if(self.allUnitCount(UnitType.Terran_Vulture) + amountInUQ(UnitType.Terran_Vulture) < Math.round(p.howManyHave(UnitType.Protoss_Zealot) / 2)){
						if(util.hasRequirementFor(UnitType.Terran_Vulture)){
							UQ.add(UnitType.Terran_Vulture);
							UQ.add(UnitType.Terran_Vulture);
						}
					}
				}	
			}
				
			if(unit.getType().equals(UnitType.Protoss_Dragoon)){
				BotPlayer p = myData.getBotPlayer(unit.getPlayer());
				if(p != null){
					if(self.allUnitCount(UnitType.Terran_Siege_Tank_Tank_Mode) + amountInUQ(UnitType.Terran_Siege_Tank_Tank_Mode) + self.allUnitCount(UnitType.Terran_Siege_Tank_Siege_Mode)< Math.round(p.howManyHave(UnitType.Protoss_Dragoon) / 2) + Math.round(p.howManyHave(UnitType.Zerg_Hydralisk) / 4)){
						if(util.hasRequirementFor(UnitType.Terran_Siege_Tank_Tank_Mode)){
							UQ.add(UnitType.Terran_Siege_Tank_Tank_Mode);
							UQ.add(UnitType.Terran_Siege_Tank_Tank_Mode);
						}
					}
				}	
			}
			
			if(unit.getType().equals(Doggo)){
				if(game.getFrameCount() < 2000){
					if(!hasReacted){
						game.sendText("6 POOL?!?!?!?!");
						clearWorkersFromQueue();
						if(self.getRace().equals(Race.Terran)){
							for(int i = 0; i < 15; i++){
								UQ.add(ShootyBoy);
							}
						}
						else if (self.getRace().equals(Race.Protoss)){
							for(int i = 0; i < 15; i++){
								UQ.add(FuckingBroken);
							}
						}
						else {
							for(int i = 0; i < 15; i++){
								UQ.add(Doggo);
							}
						}
						hasReacted = true;
					}
				}
			}
			
			
			
			
    	// end of enemy unit discover	
    	}
    	
    	
    		

    		if(IsMilitrayUnit(unit)){
    			myData.newMilUnit(unit);
    			if(unit.getPlayer() != self){
    			BotPlayer ply = getPlayer(unit.getPlayer());
	    			if(ply != null){
	    				ply.newMilUnit(unit);
	    			}
    			}
    		}
    		
    		
    		
    			
    		// end of enemy units
    	}
    	

  			

	@Override
	public void onUnitHide(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitMorph(Unit unit) {
		UnitType type = unit.getType();
		
		if(IsMilitrayUnit(unit)){
			myData.newMilUnit(unit);
		}
		
		if(type.equals(UnitType.Zerg_Overlord) && unit.getPlayer().equals(self)){
			if(overLordsMorphing > 0){
				overLordsMorphing--;
			}
		}
		
		if(util.isSpellCaster(unit.getType()) && unit.getPlayer().equals(self)){
			if(getSquad(unit) == null){
			assignUnit(unit);
			if(!spellCastersContains(unit)){
				casters.add(new Spellcaster(unit, myData, util));			
				}
			}
		}
		
		if(util.shouldResetUQ(unit.getType()) && unit.getPlayer().equals(self)){
			UQ.clear();
			game.sendText("Just letting you know that i have a new thing");
		}
		
		if(game.enemies().contains(unit.getPlayer())){
			BotPlayer ply = getPlayer(unit.getPlayer());
			if(ply != null){
				if(IsMilitrayUnit(unit) && !IsMilitrayBuilding(unit)){
					ply.newMilUnit(unit);
				}
				if(IsMilitrayBuilding(unit) && !IsMilitrayUnit(unit) ){
					ply.newEnemyBuilding(unit);
				}
				if(unit.getType().isBuilding() && !IsMilitrayUnit(unit) && !IsMilitrayBuilding(unit)){
					ply.newEnemyBuilding(unit);
				} 
			}
		}
		
		if(type.isRefinery() && unit.getPlayer().equals(self)){
			
    		if(placements.contains(unit.getType())){
    			pBuilding p = getPBuilding(unit.getType());
    			if(p != null){
    				pBuildings.remove(p);
	    			placements.remove(unit.getType());
					sMins = 0;
					sGas = 0;
    			}
    			else {
    				if(!pBuildings.isEmpty()){
    					if(pBuildings.get(0).type.equals(unit.getType())){
    						pBuildings.remove(0);
			    			placements.remove(unit.getType());
	    					sMins = 0;
	    					sGas = 0;
    					}
    				}
    			}
    			
    			Builder b = getBuilder(unit.getType(), unit.getTilePosition());
    			if(b != null){
    				BotBase bass = getBase(b.worker);
    				if(bass != null){
    					bass.unVoidWorker2(b.worker);
    				}
    				builders.remove(b);
    			}
    		}
				
//			
//			if(!pBuildings.isEmpty()){
//				if(pBuildings.get(0).type.equals(type)){
//					pBuildings.remove(0);
//					placements.remove(unit.getType());
//					constructors.remove(unit.getType());
//					for(Builder build : new ArrayList<Builder>(builders)){
//						if(build.type == unit.getType()){
//							builders.remove(build);
//						}
//					}
//				}
//			}
			
			Base closest = getClosestBaseLocation(unit.getPosition());
			for(BotBase bass : myBases){
				if(bass.Base.equals(closest)){
					bass.newRefinery(unit);
					//System.out.println("New Gas to base: " + myBases.indexOf(bass));
				}
											
			}
				
		}
		
		
		if(unit.getPlayer().equals(self)){
			if(strat.buildName == "Meme Proxy Sunken Build"){
				if(unit.getType().equals(UnitType.Zerg_Hatchery)){
					Unit unitr = getWorker();
					if(unitr != null){
					BotBase bass = getBase(unitr);
					bass.newVoidedWorker(unitr);
					unitr.move(unit.getPosition());
					}
				}
			}
			
			
			if(unit.getType().equals(UnitType.Zerg_Creep_Colony) || unit.getType().equals(UnitType.Zerg_Spawning_Pool) || unit.getType().equals(UnitType.Zerg_Hatchery)
				|| unit.getType().equals(UnitType.Zerg_Evolution_Chamber)){
				// https://www.youtube.com/watch?v=4SiiRx7GDzI
				// i just wanteeed to WAAAAAAAAAAAAAAAATCCCCCCH
				// https://www.youtube.com/watch?v=Sb5aq5HcS1A
				// California rest in peace
				
				if(needsWorkers()){
					UQ.add(UnitType.Zerg_Drone);
				}
			}
			

    		if(unit.getType().isResourceDepot() && !unit.getType().equals(UnitType.Zerg_Lair) && !unit.getType().equals(UnitType.Zerg_Hive)){
    			BotBase yeah = getBaseDepot(unit);
    			Base bass = getClosestBaseLocation(unit.getPosition());
    			if(!alreadyBasedHere(bass) && yeah == null){
        		BotBase yes = new BotBase(game, unit, bass);
    			myBases.add(yes);
    			yeah = yes;
    			}
    			if(yeah != null){
      			TransferWorkersTo(yeah, 6);
    			}
    			Production.add(unit);
    		}
    		
    		if(unit.getType().equals(UnitType.Zerg_Sunken_Colony) ){
    			for(Squad sq : Squads){
    				sq.retreatPos = unit.getPosition();
    			}
    			globalRetreat = unit.getPosition();
    		}
    		
    		if(placements.contains(unit.getType())){
    			pBuilding p = getPBuilding(unit.getType());
    			if(p != null){
    				pBuildings.remove(p);
	    			placements.remove(unit.getType());
					sMins = 0;
					sGas = 0;
    			}
    			else {
    				if(!pBuildings.isEmpty()){
    					if(pBuildings.get(0).type.equals(unit.getType())){
    						pBuildings.remove(0);
			    			placements.remove(unit.getType());
	    					sMins = 0;
	    					sGas = 0;
    					}
    				}
    			}
    		}
    		
    		
    		
//    		if(!pBuildings.isEmpty()){
//	    		if(pBuildings.get(0).type.equals(type) && placements.contains(type)){
//	    			pBuilding p = getPBuilding(type);
//	    			placements.remove(unit.getType());
//	    			if(p != null){
//	    				pBuildings.remove(p);
//	    			}
//	    			else {
//	    				pBuildings.remove(0);
//	    			}
//	
//	    		}
//    		}
    		
    		if(unit.getType().isWorker() && unit.getPlayer().equals(self)){
    			if(!assignedToBase(unit)){
    				//util.Print("Morph assign");
    			}
    		}
    		
    		

		}
				
		
	}

	@Override
	public void onUnitRenegade(Unit unit) {
		if(unit.getPlayer().equals(self)){
			if(unit.getType().equals(UnitType.Terran_Nuclear_Silo) && !Production.contains(unit)){
				Production.add(unit);
			}
		}
		
	}

	@Override
	public void onUnitShow(Unit unit) {

		if(game.enemies().contains(unit.getPlayer())){
			
			BotPlayer ply = getPlayer(unit.getPlayer());
			if(ply != null){
				if(IsMilitrayUnit(unit) && !IsMilitrayBuilding(unit)){
					ply.newMilUnit(unit);
				}
				if(IsMilitrayBuilding(unit) && !IsMilitrayUnit(unit) ){
					ply.newEnemyBuilding(unit);
				}
				if(unit.getType().isBuilding() && !IsMilitrayUnit(unit) && !IsMilitrayBuilding(unit)){
					ply.newEnemyBuilding(unit);
				}
			}
		
			if(unit.getType().equals(UnitType.Zerg_Mutalisk)){
				if(unit.getPlayer().allUnitCount(UnitType.Zerg_Mutalisk) > 6){
					if(self.allUnitCount(UnitType.Terran_Starport) > 0 && self.allUnitCount(UnitType.Terran_Control_Tower) > 0 && self.allUnitCount(UnitType.Terran_Armory) > 0){
					UQ.add(UnitType.Terran_Valkyrie);
					}
				}
			}
			
			if(IsMilitrayUnit(unit) || IsMilitrayBuilding(unit) || util.isSpellCaster(unit.getType())){
				//https://www.youtube.com/watch?v=yjO1bMHbDpY
				//bin ich verruckt?
				
				if(!unit.isIdle()){
					if(unit.getOrderTargetPosition().getApproxDistance(self.getStartLocation().toPosition()) < 2500){
						boolean cont = false;
						if(self.getRace().equals(Race.Zerg)){
							if(self.allUnitCount(UnitType.Zerg_Sunken_Colony) + self.allUnitCount(UnitType.Zerg_Creep_Colony ) < 2){
								clearWorkersFromQueue();	
								util.Print("Enemy units inbound and no sunkens rip");
							}
							
								if(PStats < -6 && game.getFrameCount() < 15000){
									
									ArrayList<UnitType> items = getNextItems(3);
									if(items.contains(UnitType.Zerg_Creep_Colony)){
										cont = false;
									}
									
									if(cont){
										
										TilePosition best = null;
										if(myBases.size() == 1){
											best = self.getStartLocation();
										}
										else {
											best = myData.Expands.get(0).getLocation();
										}
										
										if(self.allUnitCount(UnitType.Zerg_Spawning_Pool) > 0){
											BotPlayer p = getPlayer(unit);
											if(p != null){
											int max = Math.round(p.armyScore / 300);
											if(max >= 3){
												max = 3;
											}
											
											for(int i=0;i<max;i++){
												findNicePlaceToPutToPBuildings(UnitType.Zerg_Creep_Colony, best, 300, true);
												stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData, true));
											}
											
											UQ.add(UnitType.Zerg_Drone);
											
											game.sendText("Scheiss, egal... making " + max + " sunkens");
											}
										}
										
									}
								}
						
							
						}
						else {
							if(!manager.canWin){
							clearWorkersFromQueue();
							}
						}
					}
				}
				
				
				FogUnit yes = getFogUnit(unit.getID());
				if(yes == null){
					fogUnits.add(new FogUnit(unit, unit.getType(), unit.getID()));
					// if doesn't exist
				}
				else {
					// if not
					yes.update(unit);
					yes.lastSeen = game.getFrameCount();
				
				}
				
			}
			

			

			
		}
		

//		if(unit.getType().equals(UnitType.Resource_Vespene_Geyser)){
//			if(!myData.geysers.keySet().contains(unit)){
//				myData.geysers.put(unit, unit.getTilePosition());
//			}
//		}
		
		
	}
	

	@Override
	public void onPlayerDropped(Player arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onNukeDetect(Position arg0) {
		game.sendText("Really? You are nuking me");
		
	}

	@Override
	public void onPlayerLeft(Player arg0) {
		
	}

	@Override
	public void onReceiveText(Player arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSaveGame(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSendText(String arg0) {
		// TODO Auto-generated method stub
		
	}
	
    public void onUnitEvade(Unit unit) {
    	
    }
    
    public void onSaveGame(Unit unit) {
    	game.sendText("REEEEEEEEEEEEEEEEEEEEEEEEEEEEE");
    }

	
	void assignWorkerToBase(Unit unit){
			
		for(BotBase bass : myBases){
			if(bass.Pawns.size() < bass.maxWorkers / 2){
				//util.Print("Assigning Worker: " + unit.getID() + " To base: " + myBases.indexOf(bass) );
				bass.assignWorker(unit);
				workers.put(unit.getID(), bass);
				return;
			}
		}
			
		if(!assignedToBase(unit)){	
			for(BotBase bass : myBases){
				if(bass.Pawns.size() < bass.maxWorkers){
					//util.Print("Assigning Worker: " + unit.getID() + " To base: " + myBases.indexOf(bass));
					bass.assignWorker(unit);
					workers.put(unit.getID(), bass);
					return;
				}
			}
		}
		
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

	boolean assignedToBase(Unit worker){
	
		if(myBases.isEmpty()){
			return false;
		}
		
		for(BotBase bass : new ArrayList<>(myBases)){
			if(bass.Pawns.contains(worker) == true){
				return true;
			}
		}
		
		return false;
	}
	
	BotBase getBase(Unit worker){
		for(BotBase bass : new ArrayList<>(myBases)){
			if(bass.Pawns.contains(worker) == true){
				return bass;
			}
		}
		return null;
	}
	
	BotBase getBaseDepot(Unit worker){
		for(BotBase bass : new ArrayList<>(myBases)){
			if(bass.depot.equals(worker)){
				return bass;
			}
		}
		return null;
	}
	

	
	boolean isInQueue(UnitType type){
					
		if(builders.isEmpty() == true){
			return false;
		}
		
		for(Builder build : builders){
			if(build.type.equals(type)){
				return true;
			}
		}
		
		if(placements.contains(type)){
			return true;
		}
		
		return false;
	}
	
	Unit getWorker(){
				
		for(Unit unit : self.getUnits()){
			if(scouter!=null && unit!=scouter){
				if(unit.getType().isWorker()){
					if(unit.isGatheringMinerals() == true && unit.isCompleted() && !isBuilder(unit)){
						return unit;
					}
				}
			}
		}
		
		return null;
		
	}
	
	Unit getWorkerNearest(Position pos){
		
		int c = 0;
		Unit chosen = null;
		ArrayList<Unit> workers = util.getAllOf(self.getRace().getWorker());
		if(workers != null){
			for(Unit unit : workers){
				int dist = unit.getDistance(pos);
				if(dist <= c || c == 0){
					if(unit.isGatheringMinerals() == true && unit.isCompleted() && !isBuilder(unit)){
						c = dist;
						chosen = unit;
					}
				}
			}
		}
		

	
		return chosen;
		
	}
	
	
	pBuilding getPBuilding(UnitType type){
		if(pBuildings.isEmpty() == true){
			return null;
		}
		
		for(pBuilding as : new ArrayList<>(pBuildings)){
			if(as.type.equals(type)){
				return as;
			}
		}
		
		return null;
	}
	
	boolean isPQueued(UnitType type){
		if(pBuildings.isEmpty() == true){
			return false;
		}
		
		for(pBuilding as : pBuildings){
			if(as.getType() == type){
				return true;
			}
		}
		
		return false;
	}
	
	boolean isNotConstructing(Unit unit){
		
		if(unit.isGatheringMinerals() || unit.isGatheringGas() || unit.isIdle()){
			return true;
		}
		
		return false;
	}
	
	boolean isBuilder(Unit unit){
		
		for(Builder build : builders){
			if(build.worker == unit){
				return true;
			}
		}
		
		return false;
	}
	
	Builder getBuilder(Unit unit){
		for(Builder build : builders){
			if(build.worker == unit){
				return build;
			}
		}
		
		return null;
	}
	
	public boolean IsMilitrayUnit(Unit unit) {
		int Damage = unit.getType().groundWeapon().damageAmount() + unit.getType().airWeapon().damageAmount();
		if(Damage > 0 && unit.getType().isWorker() == false && unit.getType().isBuilding() == false && unit.getType().isSpell() == false && !unit.getType().equals(UnitType.Terran_Vulture_Spider_Mine)){
			return true;
		}
		return false;
	}
	
	public boolean IsSquadUnit(Unit unit) {
				
		int Damage = unit.getType().groundWeapon().damageAmount() + unit.getType().airWeapon().damageAmount();
		
		if(unit.getType().equals(UnitType.Terran_Vulture_Spider_Mine)){
			return false;
		}
		
		if(unit.getType().equals(UnitType.Protoss_Carrier)){
			return true;
		}
		
		if(unit.getType().equals(UnitType.Protoss_Interceptor)){
			return false;
		}
		
		if(Damage > 0 && unit.getType().isWorker() == false && unit.getType().isBuilding() == false && unit.getType().isSpell() == false){
			return true;
		}
		return false;
	}
	
	
	
	//REDID geyser placement and data stuff
	// probably broke stuff
	// but hey at least im doing decent in stealris
			
	public TilePosition getBuildTile(Unit builder, UnitType buildingType, TilePosition aroundTile, int maxrange) {
		TilePosition ret = null;
		int maxDist = 2;
		int stopDist = maxrange;
		
		if (buildingType.isRefinery()) {
			
			if(AllGeysersBuilt()){
				if(pBuildings.get(0).type.isRefinery()){
					pBuildings.remove(0);
					return null;
				}
			}
			
			if(myBases.isEmpty()){
				return null;
			}
			
			for(BotBase bass : new ArrayList<>(myBases)){
				for (bwem.Geyser an : bass.Geysers) {
					Unit n = an.getUnit();
					//util.Print("nID: " + n.getID());
					
					if(n.getType().equals(UnitType.Zerg_Extractor) || n.getType().equals(UnitType.Protoss_Assimilator)  || n.getType().equals(UnitType.Terran_Refinery)){
						continue;
					}

					if(n.isVisible(self)){
						return n.getTilePosition();
					}
					else {
						if(myData.geysers.containsKey(n.getID())){
							return myData.geysers.get(n.getID());
						}
						else {
							util.Print("GEYSER PLACEMENT FAILED");
							return null;
						}
					}
					
					
				}
			}
		}
		
		if(buildingType.isResourceDepot() && aroundTile.getDistance(self.getStartLocation()) > 100){
			return aroundTile;
		}

		while ((maxDist < stopDist) && (ret == null)) {
			for (int i=aroundTile.getX()-maxDist; i<=aroundTile.getX()+maxDist; i++) {
				for (int j=aroundTile.getY()-maxDist; j<=aroundTile.getY()+maxDist; j++) {
					if (game.canBuildHere(new TilePosition(i,j), buildingType, builder, false)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : game.getAllUnits()) {
							if (u.getID() == builder.getID()) continue;
							if ((Math.abs(u.getTilePosition().getX()-i) < 4) && (Math.abs(u.getTilePosition().getY()-j) < 4)) unitsInWay = true;
						}
						
						if(new TilePosition(i,j).getDistance(self.getStartLocation()) < 13 && !buildingType.isRefinery()){
							continue;
						}
						
						if(buildingType.equals(UnitType.Protoss_Pylon)){
							if(alreadyPowerHere(new TilePosition(i, j))){
								continue;
							}
							else {
								return new TilePosition(i, j);
							}
						}
						

						if(!buildingType.equals(UnitType.Protoss_Photon_Cannon) || !buildingType.equals(UnitType.Terran_Bunker) || !buildingType.equals(UnitType.Zerg_Creep_Colony) || !buildingType.equals(UnitType.Terran_Missile_Turret) ){
							if(util.HasBuildingsNearby(new Position(i, j), 4)){
								continue;
							}
							
							if(buildingType.isResourceDepot()){
								if(!myData.isNearExpandLocation(new Position(i, j))){
									continue;
								}
							}
						}
						
						if (buildingType.requiresCreep()) {
							boolean creepMissing = false;
							for (int k=i; k<=i+buildingType.tileWidth(); k++) {
								for (int l=j; l<=j+buildingType.tileHeight(); l++) {
									if (!game.hasCreep(k, l)) creepMissing = true;
									break;
								}
							}
							if (creepMissing) continue;
						}
						
						if (!unitsInWay) {
							return new TilePosition(i, j);
						}
						
					}// end of can  build
				}
			}
			maxDist += 1;
		}
		
		return ret;
		
	}
	
	public TilePosition getBuildTileProtoss(Unit builder, UnitType buildingType, TilePosition aroundTile, int maxrange) {
		TilePosition ret = null;
		int maxDist = 2;
		int stopDist = maxrange;
		ArrayList<Unit> pylons = util.getAllOf(UnitType.Protoss_Pylon);
		TilePosition tile = aroundTile;
		for(Unit uu : pylons){
			tile = uu.getTilePosition();
			while ((maxDist < stopDist) && (ret == null)) {
				search:
				for (int i=tile.getX()-maxDist; i<=tile.getX()+maxDist; i++) {
					for (int j=tile.getY()-maxDist; j<=tile.getY()+maxDist; j++) {
						if (game.canBuildHere(new TilePosition(i,j), buildingType, builder, false)) {
							// units that are blocking the tile
							boolean unitsInWay = false;
							for (Unit u : game.getAllUnits()) {
								if (u.getID() == builder.getID()) continue;
								if ((Math.abs(u.getTilePosition().getX()-i) < 4) && (Math.abs(u.getTilePosition().getY()-j) < 4)) unitsInWay = true;
							}
							
							if(buildingType.equals(UnitType.Protoss_Pylon)){
								if(alreadyPowerHere(new TilePosition(i, j))){
									maxDist++;
									continue search;
								}
								else {
									return new TilePosition(i, j);
								}
							}
							
	
							if(!buildingType.equals(UnitType.Protoss_Photon_Cannon) || !buildingType.equals(UnitType.Terran_Bunker) || !buildingType.equals(UnitType.Zerg_Creep_Colony) || !buildingType.equals(UnitType.Terran_Missile_Turret) ){
								if(util.HasBuildingsNearby(new Position(i, j), 4)){
									util.Print("TOO CLOSE TO Another building: " + buildingType.toString());
									maxDist = maxDist + 4;
									continue search;
								}
								
								if(myData.isNearExpandLocation(new Position(i, j))){
									util.Print("IS NEAR EXPAND LOCATION. " + buildingType.toString());
									maxDist++;
									continue search;
								}
							}
							
							if (buildingType.requiresCreep()) {
								boolean creepMissing = false;
								for (int k=i; k<=i+buildingType.tileWidth(); k++) {
									for (int l=j; l<=j+buildingType.tileHeight(); l++) {
										if (!game.hasCreep(k, l)) creepMissing = true;
										break;
									}
								}
								if (creepMissing) continue search;
							}
							
							if (!unitsInWay) {
								return new TilePosition(i, j);
							}
						}
					}
				}
				maxDist += 1;
			}
		}
		
		return ret;
		
	}
	
	public TilePosition getDefencePlacement(Unit builder, UnitType buildingType, TilePosition aroundTile, int maxrange) {
		TilePosition ret = null;
		int maxDist = 2;
		int stopDist = maxrange;
		//util.Print("NOT BAD");

		while ((maxDist < stopDist) && (ret == null)) {
			for (int i=aroundTile.getX()-maxDist; i<=aroundTile.getX()+maxDist; i++) {
				for (int j=aroundTile.getY()-maxDist; j<=aroundTile.getY()+maxDist; j++) {
					if (game.canBuildHere(new TilePosition(i,j), buildingType, builder, false)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : game.getAllUnits()) {
							if (u.getID() == builder.getID()) continue;
							if ((Math.abs(u.getTilePosition().getX()-i) < 4) && (Math.abs(u.getTilePosition().getY()-j) < 4)) unitsInWay = true;
						}

						
						if (buildingType.requiresCreep()) {
							boolean creepMissing = false;
							for (int k=i; k<=i+buildingType.tileWidth(); k++) {
								for (int l=j; l<=j+buildingType.tileHeight(); l++) {
									if (!game.hasCreep(k, l)) creepMissing = true;
									break;
								}
							}
							if (creepMissing) continue;
						}
						
						if (!unitsInWay) {
							return new TilePosition(i, j);
						}
					}
				}
			}
			
			for (int i=aroundTile.getX()+maxDist; i<=aroundTile.getX()-maxDist; i++) {
				for (int j=aroundTile.getY()+maxDist; j<=aroundTile.getY()-maxDist; j++) {
					if (game.canBuildHere(new TilePosition(i,j), buildingType, builder, false)) {
						// units that are blocking the tile
						boolean unitsInWay = false;
						for (Unit u : game.getAllUnits()) {
							if (u.getID() == builder.getID()) continue;
							if ((Math.abs(u.getTilePosition().getX()-i) < 4) && (Math.abs(u.getTilePosition().getY()-j) < 4)) unitsInWay = true;
						}

						
						if (buildingType.requiresCreep()) {
							boolean creepMissing = false;
							for (int k=i; k<=i+buildingType.tileWidth(); k++) {
								for (int l=j; l<=j+buildingType.tileHeight(); l++) {
									if (!game.hasCreep(k, l)) creepMissing = true;
									break;
								}
							}
							if (creepMissing) continue;
						}
						
						if (!unitsInWay) {
							return new TilePosition(i, j);
						}
					}
				}
			}
			
			
			maxDist += 1;
		}
		
		return ret;
		
	}
	
	void createSquad(ArrayList<Unit> units){
		Squads.add(new Squad(units, Squads.size() + 1, myData, game, manager, globalRetreat, util));
	}
	
	void assignUnit(Unit unit){
		ArrayList<Unit> list = new ArrayList<>();
		list.add(unit);
		if(Squads.isEmpty() == false){
		int i = 1;
		int max = Squads.size();
		boolean found = false;
		
		
		if(unit.getType().equals(UnitType.Protoss_Interceptor)){
			return;
		}
		
		
		if(util.AirWingUnit(unit.getType())){
			UnitType type = unit.getType();
			for(Squad sq : new ArrayList<>(Squads)){
				if(sq.filter.equals(type) && !sq.isSquadFull()){
					sq.absorbUnit(unit);
					found = true;
					break;
				}
			}
			
			if(found == false){
				Squad neww = new Squad(list, Squads.size() + 1, myData, game, manager, util, type);
				Squads.add(neww);
				found = true;
			}
		}
		
	
		while(found == false && Squads.isEmpty() == false){ 
			int ii = 0;
			for(Squad squad : Squads){
				if(squad.isSquadFull() || !SquadAcceptsUnit(unit.getType(), squad)){
					ii++;
				}
				int index = Squads.indexOf(squad);
				//System.out.println("Squad: " + index);
				int score = squad.getScore();
				//System.out.println("Squad: " + index + " score: " + score);
				int target = squad.getTargetScore();
				//System.out.println("Squad: " + index + " targetScore: " + target);
				//System.out.println("Squad: " + index + " priority " + squad.priority());
				
				if(squad.priority() <= i && score <= target && target > 0 && SquadAcceptsUnit(unit.getType(), squad)){
					squad.absorbUnit(unit);
					//System.out.println("Added unit to squad with target score: " + squad.getId());
					found = true;
					break;
				}
				
				if(squad.priority() <= i && !squad.isSquadFull() && SquadAcceptsUnit(unit.getType(), squad)){
					squad.absorbUnit(unit);
					//System.out.println("Added unit: " + unit.getID() + " to squad: " + squad.id);
					found = true;
					break;
				}
				
				if(ii >= max){
					//System.out.println("All squads goals filled, new squad");	
					Squad neww = new Squad(list, Squads.size() + 1, myData, game, manager, globalRetreat, util);
					Squads.add(neww);
					found = true;
					break;
				}
				
			}
			i++;
		}
		
		}
		else {
			//System.out.println("NO squads");
			Squad neww = new Squad(list, Squads.size() + 1, myData, game, manager, globalRetreat, util);
			Squads.add(neww);
		}

	}
	
	boolean SquadAcceptsUnit(UnitType type, Squad sq){
		
		if(sq.filter == null){
			sq.filter = UnitType.AllUnits;
		}
			
		if(sq.filter.equals(UnitType.AllUnits)){
			return true;
		}
		
		if(sq.filter.equals(type)){
			return true;
		}
		
		return false;
		
		
	}
	
	Squad getSquad(Unit unit){
		
		if(unit.getPlayer() != self){
			return null;
		}
		
		for(Squad sq : Squads){
			for(Unit unitt : sq.getUnits()){
				if(unitt.equals(unit)){
					return sq;
				}
			}
			
			if(sq.detector != null){
				if(sq.detector.equals(unit)){
					return sq;
				}
				
				if(!sq.detector.exists()){
					return null;
				}
			}

		}
		
		return null;
	}
	
	Squad getSquadTargetted(Position pos){
		if(Squads.isEmpty() == true){
			return null;
		}
		
		for(Squad squad : Squads){
			if(squad.getTarget() != null){
				if(squad.getTarget().getDistance(pos) < 400){
					return squad;
				}
			}
			else {
				return null;
			}
		}
		
		return null;

	}
	

	void setAllTarget(Position pos){
		for(Squad squad : Squads){
			if(squad.isDefending() == false){
			squad.newTarget(pos);
			}
		}
	}
	

	void allSquadsAttack(){
		for(Squad squad : Squads){
			squad.setState(1);
			if(squad.target == null){
				squad.newTarget(myData.nextAttackPosition);
			}
			else {
				if(squad.target != myData.nextAttackPosition){
					squad.newTarget(myData.nextAttackPosition);
				}
			}
			
			squad.operate();
		}
		
		
		ArrayList<Unit> bunks = util.getAllOf(UnitType.Terran_Bunker);
		if(bunks != null){
			for(Unit unit : bunks){
				if(unit.getLoadedUnits().size() != 0 && !util.EnemysNearby(unit.getPosition(), 500)){
					for(Unit unitt : unit.getLoadedUnits()){
					unit.unload(unitt);
					}
				}
			}
		}
		
	}
	

	
	void allSquadsState(int state){
		for(Squad squad : Squads){
			squad.setState(state);
		}
	}
	
	void allSquadsRetreat(){
		for(Squad sq : Squads){
			if(sq.State != 2){
			sq.setState(0);
			sq.retreat();
			}
		}
		
		ArrayList<Unit> bunks = util.getAllOf(UnitType.Terran_Bunker);
		if(!bunks.isEmpty()){
			HashMap<Unit, Integer> yes = new HashMap<>();
			for(Unit unit : bunks){
				if(unit.isCompleted()){
					//System.out.println("Amount: " + util.getAmountGettingIn(unit));
					yes.put(unit, util.getAmountGettingIn(unit) + unit.getLoadedUnits().size());
					if(yes.get(unit) <= 3){
						ArrayList<Unit> marines = util.getAllOf(ShootyBoy);
						if(marines != null){
							//System.out.println("Marines Size: " + marines.size());
							for(Unit rines : marines){
								if(!rines.isLoaded()){
									if(rines.getOrder() != Order.EnterTransport && !rines.isLoaded()){
									//System.out.println("Unit: " + rines.getID() + " Getting in: " + unit.getID());
									yes.put(unit, yes.get(unit) + 1);
									rines.rightClick(unit);
									if(util.getAmountGettingIn(unit) >= 4 || yes.get(unit) >= 4){
										break;
										}
									}
								}
								
								// end for
							}
						}
					}
				}
			}
		}
		
		
		
	}
	
	boolean needsWorkers(){
		int max = 0;
		for(BotBase bass : new ArrayList<>(this.myBases)){
			max = max + bass.maxWorkers;
		}
		
		if(max >= 80){
			max = 80;
		}
		
		return self.allUnitCount(self.getRace().getWorker()) + amountInUQ(self.getRace().getWorker()) < max;
		
	}
	
	int howManyWorkersDoINeed(){
		int max = 0;
		
		for(BotBase bass : new ArrayList<>(this.myBases)){
			int amount =  bass.Mins.size() + (bass.Geysers.size() * 3);
			max = max + amount;
		}
	
		return max;
	}
	
	boolean needsWorkersCritical(){
		// for Z only because they use drones for buildings
		int max = 0;
		for(BotBase bass : new ArrayList<>(this.myBases)){
			max = max + bass.maxWorkers;
		}
		
		if(max >= 80){
			max = 80;
		}
		
		return self.allUnitCount(self.getRace().getWorker()) + amountInUQ(self.getRace().getWorker()) < max / 2;
		
	}
	
	void updateUQ(){
		
		if(!buildMainGoal.isEmpty() && game.getFrameCount() > GFR){
			for(UnitType type : new ArrayList<>(buildMainGoal)){
				if(util.hasRequirementFor(type)){

					int max = self.allUnitCount(type.whatBuilds().getFirst());
					int mm = util.howManyCanIMake(type, self.minerals(), self.gas());
					
					if(mm >= max){
						max = mm;
					}
					

					for(int i = 0; i<max;i++){
						UQ.add(type);
				    }
				}
				else {
					//util.Print("Cannot build mainGoal unit: " + type.toString() );
				}
			}
		
		}
		
		
		if(needsDetectors()){
			Race rc = self.getRace();
			if(rc.equals(Race.Terran) && game.canMake(UnitType.Terran_Science_Vessel) && self.allUnitCount(UnitType.Terran_Control_Tower) != 0){
				UQ.add(UnitType.Terran_Science_Vessel);
			}
			
			if(rc.equals(Race.Protoss) && self.allUnitCount(UnitType.Protoss_Observatory) > 0){
				UQ.add(UnitType.Protoss_Observer);
			}
	
		}
		
		if(needsWorkers() == true){
			int yes = howManyWorkersDoINeed();
			for(int i = 0; i<yes/2-1;i++){
			UQ.add(self.getRace().getWorker());
			}
		}
	
		if(self.getRace().equals(Race.Zerg)){
			// Z
					
			if(self.allUnitCount(UnitType.Zerg_Defiler) > 0 && amountInUQ(UnitType.Zerg_Defiler) + self.completedUnitCount(UnitType.Zerg_Defiler) < Squads.size()){
				UQ.add(UnitType.Zerg_Defiler);
				// forgot to add some buddies for him to eat.
				UQ.add(Doggo);
			}
			
			if(util.hasRequirementFor(UnitType.Zerg_Infested_Terran) && self.allUnitCount(UnitType.Zerg_Extractor) > 2){
				UQ.add(UnitType.Zerg_Infested_Terran);
			}
			
			
			if(self.getUpgradeLevel(UpgradeType.Adrenal_Glands) > 0 || self.isUpgrading(UpgradeType.Adrenal_Glands)){
				if(util.hasRequirementFor(Doggo)){
					for(int i = 0; i < self.allUnitCount(UnitType.Zerg_Hatchery); i++){
						UQ.add(Doggo);
					}
				}
			}
			
			if(self.allUnitCount(UnitType.Zerg_Queens_Nest) > 0){
				if(self.allUnitCount(UnitType.Zerg_Queen) + amountInUQ(UnitType.Zerg_Queen) < Squads.size()){
					UQ.add(UnitType.Zerg_Queen);
				}
			}
			
			if(self.gas() >= 50){				
				if(self.minerals() * 10 >= self.gas()){
					int VMax = Math.round(self.minerals() / 50);
						
					if(VMax >= 25){
						VMax = 25;
					}
					
					for(int i = 0; i < VMax; i++){
					UQ.add(Doggo);
					}
				}
			}

			if(self.completedUnitCount(UnitType.Zerg_Ultralisk_Cavern) > 0 && self.allUnitCount(UnitType.Zerg_Ultralisk) <= 5){
				UQ.add(UnitType.Zerg_Ultralisk);
				// ALWAYS BUILD ULTRAS CAUSE WHY NOT
			}
				
			if(self.completedUnitCount(UnitType.Zerg_Spire) > 0 || self.completedUnitCount(UnitType.Zerg_Greater_Spire) > 0){
				if(self.allUnitCount(UnitType.Zerg_Mutalisk) < 25 && self.gas() >= 100){
					for(int i = 0; i <= self.allUnitCount(self.getRace().getRefinery()) * 3; i++){
					UQ.add(UnitType.Zerg_Mutalisk);
					}
				}
			}
				
			
			if(game.canMake(UnitType.Zerg_Lurker) && self.allUnitCount(UnitType.Zerg_Lurker) <= Math.round(currentTarget.howManyHave(ShootyBoy) + currentTarget.howManyHave(UnitType.Terran_Firebat) / 4) && !asdf(UnitType.Zerg_Lurker)){
				for(int i = 0;i<2;i++){
				stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Lurker, myData));
				UQ.add(UnitType.Zerg_Hydralisk);
				}
			}
				
			if(currentTarget.race.equals(Race.Protoss)){
					// if we are z
					// and can't win vs P
					// Hydra Spam
				if(self.completedUnitCount(UnitType.Zerg_Hydralisk_Den) > 0){
					for(int i = 0; i < 8; i++){
						UQ.add(UnitType.Zerg_Hydralisk);
					}
						
				}
				else {
					if(game.getFrameCount() <= 8000){
//						if(self.allUnitCount(UnitType.Zerg_Sunken_Colony) + self.allUnitCount(UnitType.Zerg_Creep_Colony) == 0 && self.allUnitCount(UnitType.Zerg_Spawning_Pool) > 0){
//							for(int i = 0; i < 4; i++){
//								UQ.add(Doggo);
//							}
//							if(needsWorkers()){
//								UQ.add(UnitType.Zerg_Drone);
//							}
//						}
						
						if(!needsWorkers()){
							UQ.add(Doggo);
						}
						else {
							UQ.add(UnitType.Zerg_Drone);
						}
					}
					else{
						//if(amountInUQ(Doggo) < 2 && self.allUnitCount(UnitType.Zerg_Spawning_Pool) > 0){
							int mMax = Math.round(currentTarget.howManyHave(UnitType.Terran_Goliath) + 
							currentTarget.howManyHave(UnitType.Protoss_Dragoon) + 
							currentTarget.howManyHave(UnitType.Terran_Siege_Tank_Siege_Mode) +
							currentTarget.howManyHave(UnitType.Terran_Siege_Tank_Tank_Mode) / 2);
							
							if(Math.round(self.allUnitCount(Doggo) + amountInUQ(Doggo) / 2) < mMax){
								for(int i = 0; i < mMax; i++){
									UQ.add(Doggo);
								}
							}
						//}
					}

				}
							
			}
						
			if(currentTarget.race.equals(Race.Terran)){	
				// ZvT
				
				int hMax = Math.round( (currentTarget.howManyHave(UnitType.Terran_Vulture) / 2) +
						(currentTarget.howManyHave(UnitType.Terran_Goliath) / 2) + 3);
				
				int zMax = Math.round( (currentTarget.howManyHave(ShootyBoy) * 2) +
						(currentTarget.howManyHave(UnitType.Terran_Firebat) * 2) +
						(currentTarget.howManyHave(UnitType.Terran_Vulture) * 2) +
						(currentTarget.howManyHave(UnitType.Terran_Siege_Tank_Tank_Mode) * 4) +
						(currentTarget.howManyHave(UnitType.Terran_Siege_Tank_Siege_Mode) * 4) +
						(currentTarget.howManyHave(UnitType.Terran_Barracks) * 3) + 7);
				
				int lMax = Math.round((currentTarget.howManyHave(ShootyBoy) / 4) +
						(currentTarget.howManyHave(UnitType.Terran_Firebat) / 2));
				
				if(self.hasResearched(TechType.Lurker_Aspect)){
					if(self.allUnitCount(UnitType.Zerg_Lurker) < lMax){
						for(int i = 0; i < hMax; i++){
							stuffQueue.add(new BotTech(1, 0, TechType.Lurker_Aspect, UpgradeType.None, UnitType.None, myData));
							// add hydras for the lurker :D
							UQ.add(UnitType.Zerg_Hydralisk);
						}
					}
				}
				
				if(self.completedUnitCount(UnitType.Zerg_Hydralisk_Den) > 0){
					if(self.allUnitCount(UnitType.Zerg_Hydralisk) + amountInUQ(UnitType.Zerg_Hydralisk) < hMax){
						for(int i = 0; i < hMax; i++){
						UQ.add(UnitType.Zerg_Hydralisk);
						}
					}
				}
						
				
				if(self.allUnitCount(UnitType.Zerg_Spire) == 0){
					if((self.allUnitCount(Doggo) / 2) + amountInUQ(Doggo) < zMax && !needsWorkersCritical()){
						if(self.completedUnitCount(UnitType.Zerg_Spawning_Pool) != 0){
							for(int i = 0; i < 10; i++){
							UQ.add(Doggo);
							}
						}
					}
				}
				else{
					if(self.completedUnitCount(UnitType.Zerg_Spire) > 0 || self.completedUnitCount(UnitType.Zerg_Greater_Spire) > 0){
						UQ.add(UnitType.Zerg_Mutalisk);
					}

				}

			}
					
			if(myData.enemyRace.equals(Race.Zerg) || myData.enemyRace.equals(Race.Unknown)){	
				// if we can't win vs Z or Unknown
				if(self.completedUnitCount(UnitType.Zerg_Spire) == 0){
					if(self.completedUnitCount(UnitType.Zerg_Spawning_Pool) != 0 && self.completedUnitCount(UnitType.Zerg_Sunken_Colony) > 0){
						for(int i = 0; i < 4; i++){
						UQ.add(Doggo);
						}
					}
					
				}
				else{
					for(int i = 0; i <= self.allUnitCount(self.getRace().getRefinery()) * 3; i++){
					UQ.add(UnitType.Zerg_Mutalisk);
					}
					
					if(self.minerals() >= amountInUQ(UnitType.Zerg_Mutalisk) * 100){
						for(int i = 0; i < 4; i++){
							UQ.add(Doggo);
						}	
					}
				}
									
					
				}
			
			}
			else if (self.getRace().equals(Race.Terran)){
					// T
					int bioCount = self.allUnitCount(ShootyBoy) + self.allUnitCount(UnitType.Terran_Firebat);
					int medicCount = self.allUnitCount(UnitType.Terran_Medic) + amountInUQ(UnitType.Terran_Medic);
					
				
					if(self.completedUnitCount(UnitType.Terran_Barracks) > 0){
						if(game.getFrameCount() > 25000){
							if(strat.type != "mech"){
								for(int i = 0;  i < self.completedUnitCount(UnitType.Terran_Barracks) * 3; i++){
									UQ.add(ShootyBoy);
								}
								bioCount = self.allUnitCount(ShootyBoy) + self.allUnitCount(UnitType.Terran_Firebat);
								
//								if(medicCount < bioCount){
//									if(util.hasRequirementFor(UnitType.Terran_Medic)){
//										for(int i = 0;  i < self.completedUnitCount(UnitType.Terran_Barracks) * 2; i++){
//											UQ.add(UnitType.Terran_Medic);
//										}
//									}
//								}
							}
						}
						else {	
							if(strat.type == "mech"){
								if(currentTarget.armyScore >= currentTarget.ecoScore){
									int mMax =  (self.allUnitCount(UnitType.Terran_Bunker) * 4) + Math.round((currentTarget.armyScore) / 25) + 3;
									util.Print("Early Marine Count: " + mMax);
									if(self.allUnitCount(UnitType.Terran_Marine) + amountInUQ(UnitType.Terran_Marine) < mMax){
										for(int i = 0;  i < mMax; i++){
											UQ.add(ShootyBoy);
										}
									}
								}
								else {
									int mMax = (self.allUnitCount(UnitType.Terran_Bunker) * 4) + 3;
									util.Print("Early Marine Count: " + mMax);
									if(self.allUnitCount(UnitType.Terran_Marine) + amountInUQ(UnitType.Terran_Marine) < mMax){
										for(int i = 0;  i < mMax; i++){
											UQ.add(ShootyBoy);
										}
									}
								}

							}
							else{
								int mMax =  (self.allUnitCount(UnitType.Terran_Bunker) * 4) + Math.round(game.getFrameCount() / 1000);
								
								if(currentTarget.race.equals(Race.Protoss) && game.getFrameCount() < 10000){
									mMax = ((self.allUnitCount(UnitType.Terran_Bunker) + amountInBuildingQueue(UnitType.Terran_Bunker) * 4));
								}
								
								if(self.allUnitCount(UnitType.Terran_Marine) + amountInUQ(UnitType.Terran_Marine) < mMax){
									for(int i = 0;  i < mMax; i++){
										UQ.add(ShootyBoy);
									}
								}
								
								if(medicCount < Math.round(bioCount / 8)){
									if(util.hasRequirementFor(UnitType.Terran_Medic)){
										for(int i = 0;  i < self.completedUnitCount(UnitType.Terran_Barracks) * 2; i++){
											UQ.add(UnitType.Terran_Medic);
										}
									}
								}
							
							}
	
						}
	
					}
					
					
					if(util.hasRequirementFor(UnitType.Terran_Battlecruiser)){
						if(self.allUnitCount(UnitType.Terran_Refinery) >= 3 || buildMainGoal.contains(UnitType.Terran_Battlecruiser)){
							UQ.add(UnitType.Terran_Battlecruiser);
						}
					}
					
					if(self.completedUnitCount(UnitType.Terran_Academy) > 0 && self.gas() >= 50 && strat.type != "mech"){
						if(currentTarget != null){
							int asdf = Math.round(currentTarget.howManyHave(UnitType.Protoss_Zealot) + currentTarget.howManyHave(Doggo) / 4);
							if(asdf >= 8){
								asdf = 8;
							}
							
							if(self.allUnitCount(UnitType.Terran_Firebat) + amountInUQ(UnitType.Terran_Firebat) < asdf){
								for(int i = 0;  i < asdf; i++){
									UQ.add(UnitType.Terran_Firebat);
								}
							}
					}
						
				}
					
					
					if(self.allUnitCount(UnitType.Terran_Starport) > 0 && self.allUnitCount(UnitType.Terran_Control_Tower) > 0){
						UQ.add(UnitType.Terran_Wraith);
					}
					
					
	//				if(self.completedUnitCount(UnitType.Terran_Nuclear_Silo) > 0){
	//					ArrayList<Unit> silos = util.getAllOf(UnitType.Terran_Nuclear_Silo);
	//					if(silos != null){
	//						for(Unit unit : silos){
	//							if(!unit.hasNuke() || !unit.getOrder().equals(Order.NukeTrain)){
	//								UQ.add(UnitType.Terran_Nuclear_Missile);
	//							}
	//						}
	//					}
	//				}
					
	
					
					if(self.completedUnitCount(UnitType.Terran_Covert_Ops) > 0){
						int g = self.allUnitCount(UnitType.Terran_Ghost) + amountInUQ(UnitType.Terran_Ghost);
						int gNeed = Squads.size() * 2 + Math.round(currentTarget.howManyHave(UnitType.Protoss_Dragoon) + currentTarget.howManyHave(UnitType.Terran_Vulture) + currentTarget.howManyHave(UnitType.Terran_Siege_Tank_Tank_Mode) + currentTarget.howManyHave(UnitType.Terran_Siege_Tank_Siege_Mode));
						if(gNeed >= 10){
							gNeed = 10;
						}
						if(g + amountInUQ(UnitType.Terran_Ghost) < gNeed ){
							for(int i = 0; i < gNeed - g; i++){
								UQ.add(UnitType.Terran_Ghost);
							}
						}
					}
		
					
					int tanksBonus = Math.round((currentTarget.howManyHave(UnitType.Protoss_Dragoon) / 4) +
					(currentTarget.howManyHave(UnitType.Zerg_Hydralisk) / 6) +
					(currentTarget.howManyHave(UnitType.Protoss_Photon_Cannon) + 
							currentTarget.howManyHave(UnitType.Terran_Bunker) + currentTarget.howManyHave(UnitType.Zerg_Sunken_Colony) / 2) + 3);
					
					int vultureBonus = Math.round((currentTarget.howManyHave(UnitType.Protoss_Zealot) / 4) +
					(currentTarget.howManyHave(Doggo) / 10) +
					(currentTarget.howManyHave(UnitType.Protoss_Dark_Templar)) +
					(currentTarget.howManyHave(UnitType.Zerg_Ultralisk) / 2));
					
					int golBonus = Math.round((currentTarget.howManyHave(UnitType.Terran_Battlecruiser) / 2) +
							(currentTarget.howManyHave(UnitType.Terran_Wraith) / 4) +
							(currentTarget.howManyHave(UnitType.Protoss_Scout) / 4) + 
							(currentTarget.howManyHave(UnitType.Protoss_Carrier) / 4) +
							(currentTarget.howManyHave(UnitType.Zerg_Mutalisk) / 2) +
							(currentTarget.howManyHave(UnitType.Zerg_Guardian) / 2) +
							(currentTarget.howManyHave(UnitType.Protoss_Arbiter) / 2) +
							(currentTarget.howManyHave(UnitType.Zerg_Spire) * 2 ) +
							(currentTarget.howManyHave(UnitType.Zerg_Greater_Spire) * 2) +
							(currentTarget.howManyHave(UnitType.Terran_Starport) * 2) +
							(currentTarget.howManyHave(UnitType.Protoss_Stargate) * 2) + 3);
							
			
					if(self.completedUnitCount(UnitType.Terran_Factory) > 0 && self.allUnitCount(UnitType.Terran_Vulture) + amountInUQ(UnitType.Terran_Vulture) < vultureBonus && !currentTarget.race.equals(Race.Zerg)){
						for(int i = 0; i < vultureBonus; i++){
							UQ.add(UnitType.Terran_Vulture);
						}			
					}
					
					if(DoesTrueTheAmountOf(UnitType.Terran_Factory) && DoesTrueTheAmountOf(UnitType.Terran_Armory) && self.allUnitCount(UnitType.Terran_Goliath) + amountInUQ(UnitType.Terran_Goliath) < golBonus){
						for(int i = 0; i < golBonus; i++){
							UQ.add(UnitType.Terran_Goliath);
						}	
					}

				
					if(self.completedUnitCount(UnitType.Terran_Machine_Shop) > 0 && self.allUnitCount(UnitType.Terran_Siege_Tank_Tank_Mode) + self.allUnitCount(UnitType.Terran_Siege_Tank_Siege_Mode) + amountInUQ(UnitType.Terran_Siege_Tank_Tank_Mode) < tanksBonus){
						for(int i = 0; i < tanksBonus; i++){
							UQ.add(UnitType.Terran_Siege_Tank_Tank_Mode);
						}			
					}
					
					if(currentTarget != null){
						
						if(currentTarget.race.equals(Race.Protoss)){
							// more TvP stuff
							if(self.allUnitCount(UnitType.Terran_Factory) > 0){
								int bonus = currentTarget.howManyHave(UnitType.Protoss_Zealot) / 4;
								for(int i = 0; i < self.allUnitCount(UnitType.Terran_Factory) * 2 + bonus; i++){
									UQ.add(UnitType.Terran_Vulture);
								}
							}
								
							// some bots spam alotta goons instead of zlots. Maybe we'll make some tanks. 
							if(currentTarget.howManyHave(UnitType.Protoss_Dragoon) > currentTarget.howManyHave(UnitType.Protoss_Zealot) * 2 ){
								if(self.completedUnitCount(UnitType.Terran_Machine_Shop) > 0){
									UQ.add(UnitType.Terran_Siege_Tank_Tank_Mode);
								}
							}
							// end of TvP
						}
						
						if(currentTarget.race.equals(Race.Zerg)){
							if(DoesTrueTheAmountOf(UnitType.Terran_Starport) && DoesTrueTheAmountOf(UnitType.Terran_Control_Tower) && DoesTrueTheAmountOf(UnitType.Terran_Armory)){
								if(currentTarget.howManyHave(UnitType.Zerg_Mutalisk) > 3){
									if(self.allUnitCount(UnitType.Terran_Valkyrie) < currentTarget.howManyHave(UnitType.Zerg_Mutalisk) / 3){
										UQ.add(UnitType.Terran_Valkyrie);
									}
								}
							}
						}
						
						if(currentTarget.race.equals(Race.Protoss)){
							if(self.allUnitCount(UnitType.Terran_Starport) > 0 && self.allUnitCount(UnitType.Terran_Control_Tower) > 0 && self.allUnitCount(UnitType.Terran_Armory) > 0){
								if(currentTarget.howManyHave(UnitType.Protoss_Carrier) > 3){
									if(self.allUnitCount(UnitType.Terran_Valkyrie) < currentTarget.howManyHave(UnitType.Protoss_Carrier) / 4){
										UQ.add(UnitType.Terran_Valkyrie);
									}
								}
							}
						}
					
				}
					
					
				if(medicCount < Math.round(bioCount / 8)){
					if(self.completedUnitCount(UnitType.Terran_Academy) > 0 && self.completedUnitCount(UnitType.Terran_Refinery) > 0){
						for(int i = 0;  i < Math.round(bioCount/8); i++){
							UQ.add(UnitType.Terran_Medic);
						}
					}
				}
					
			}
			else {
				// P
				
				if(self.minerals() * 10 >= self.gas()){
					int VMax = Math.round(self.minerals() / 100);
					
					if(VMax >= 8){
						VMax = 8;
					}
					
					for(int i = 0; i < VMax; i++){
					UQ.add(UnitType.Protoss_Zealot);
					}
				}
				
				if(currentTarget.race.equals(Race.Zerg)){
					if(util.hasRequirementFor(UnitType.Protoss_Corsair)){
						if(self.allUnitCount(UnitType.Protoss_Corsair) + amountInUQ(UnitType.Protoss_Corsair) < Math.round(currentTarget.howManyHave(UnitType.Zerg_Mutalisk) / 4) + 1){
							UQ.add(UnitType.Protoss_Corsair);
						}
					}
				}
							
				int zMax = Math.round(currentTarget.howManyHave(Doggo) / 6 + 
						currentTarget.howManyHave(ShootyBoy) / 6 +
						currentTarget.howManyHave(UnitType.Terran_Firebat) / 6 +
						currentTarget.howManyHave(UnitType.Terran_Siege_Tank_Tank_Mode) / 6 +
						currentTarget.howManyHave(UnitType.Protoss_Zealot) / 4 + 3);
				
				int dMax = Math.round(currentTarget.howManyHave(ShootyBoy) / 4 +
						currentTarget.howManyHave(UnitType.Terran_Firebat) / 4 +
						currentTarget.howManyHave(UnitType.Terran_Goliath) / 4 +
						currentTarget.howManyHave(UnitType.Terran_Vulture) / 4 +
						currentTarget.howManyHave(UnitType.Terran_Battlecruiser) / 4 +
						currentTarget.howManyHave(UnitType.Zerg_Mutalisk) / 4 + 5);
				
				int sMax = Math.round(currentTarget.howManyHave(UnitType.Terran_Battlecruiser)  +
						(currentTarget.howManyHave(UnitType.Protoss_Carrier) / 4) + 1);
				
				if(util.hasRequirementFor(UnitType.Protoss_Scout) && game.getFrameCount() >= 12000){
					if(self.allUnitCount(UnitType.Protoss_Scout) + amountInUQ(UnitType.Protoss_Scout) < sMax + 1){
						UQ.add(UnitType.Protoss_Scout);
					}
				}
							
				if(util.hasRequirementFor(UnitType.Protoss_Zealot)){
					if(self.allUnitCount(UnitType.Protoss_Zealot) + amountInUQ(UnitType.Protoss_Zealot) < zMax ){
						UQ.add(UnitType.Protoss_Zealot);
					}
				}
				
//				if(util.hasRequirementFor(UnitType.Protoss_Dark_Templar) && self.hasResearched(TechType.Mind_Control)){
//					if(self.allUnitCount(UnitType.Protoss_Dark_Archon) + amountInUQ(UnitType.Protoss_Dark_Archon) < Squads.size() ){
//						stuffQueue.add(new BotTech(4, 12, TechType.None, UpgradeType.None, UnitType.Protoss_Dark_Archon, myData));
//						// https://www.youtube.com/watch?v=lX44CAz-JhU
//					}
//				}
				
				
				if(util.hasRequirementFor(UnitType.Protoss_Dragoon)){
					if(self.allUnitCount(UnitType.Protoss_Dragoon) + amountInUQ(UnitType.Protoss_Dragoon) < dMax ){
						UQ.add(UnitType.Protoss_Dragoon);
					}
				}
				

				if(self.minerals() * 7 >= self.gas() && self.minerals() >= 800){
					for(int i = 0; i < 4; i++){
						UQ.add(UnitType.Protoss_Zealot);
					}
				}
				
				
				if(currentTarget.howManyHave(UnitType.Zerg_Mutalisk) > 0){
					int cMax = Math.round(currentTarget.howManyHave(UnitType.Zerg_Mutalisk) / 4);
					if(util.hasRequirementFor(UnitType.Protoss_Corsair)){
						if(self.allUnitCount(UnitType.Protoss_Corsair) + amountInUQ(UnitType.Protoss_Corsair) < cMax){
						UQ.add(UnitType.Protoss_Corsair);
						}
					}
				}
				
				if(DoesTrueTheAmountOf(UnitType.Protoss_Templar_Archives) && self.allUnitCount(UnitType.Protoss_Dark_Templar) + amountInUQ(UnitType.Protoss_Dark_Templar) < 6){
					UQ.add(UnitType.Protoss_Dark_Templar);
				}
				
				if(DoesTrueTheAmountOf(UnitType.Protoss_Templar_Archives) && self.allUnitCount(UnitType.Protoss_High_Templar) + amountInUQ(UnitType.Protoss_High_Templar) < 6 && game.getFrameCount() >= 15000){
					if(self.isResearching(TechType.Psionic_Storm) || self.hasResearched(TechType.Psionic_Storm)){
					UQ.add(UnitType.Protoss_High_Templar);
					}
				}
				
				if(DoesTrueTheAmountOf(UnitType.Protoss_Stargate) && DoesTrueTheAmountOf(UnitType.Protoss_Fleet_Beacon)){
					UQ.add(UnitType.Protoss_Carrier);
				}
				
			}
		
		// end of else	
		
		

	
		}
	
	
	boolean allSquadsFilled(){
		
		if(Squads.isEmpty()){
			return false;
		}
		
		int max = Squads.size();
		int i = 0;
		
		for(Squad sq : Squads){
			if(sq.isSquadFull()){
				i++;
			}
		}
		
		if(i==max){
			return true;
		}
		
		return false;
	}
	
	int highestsPriority(){
		int h = 0;
		for(Squad sq : Squads){
			if(sq.priority() > h){
				h = sq.priority();
			}
		}
		return h;
	}
	
	int amountInUQ(UnitType type){
		int i = 0;
		for(UnitType unit : UQ){
			if(unit.equals(type)){
				i++;
			}
		}
		
		return i;
	}
	
	
	public ArrayList<Unit> GetEnemyUnitsNearby(Position pos, int radius, boolean include){
		 ArrayList<Unit> Mine = new ArrayList<Unit>();
		for (Unit targets : game.getUnitsInRadius(pos, radius)) {
			if (targets.getPlayer().isEnemy(self) == true && IsMilitrayUnit(targets) == true && Mine.contains(targets) == false) {
				Mine.add(targets);

			}
			if(targets.getPlayer().isEnemy(self) == true && manager.IsMilitrayBuilding(targets) == true && include == true){
				Mine.add(targets);
			}
		}
		
		return Mine;

		
	}
	
	void DefenceCall(Position pos){
		ArrayList<Unit> ass2 = GetEnemyUnitsNearby(pos, 350, true);
		Squad sq = getSquadTargetted(pos);
		int eScore = 0;
		
		if(sq!=null){
			// if we have a squad already assigned to defend dis
			// do nothing for now
		}
		
		
		for(Unit unit : ass2){
			eScore = eScore + manager.getScoreOf(unit);
		}
		
		if(myData.enemyScore >= myData.myScore){
			for(Squad squad : Squads){
				squad.target = pos;
				squad.setState(2);
				squad.operate();
				// 0 = idle, 1 = attacking, 2 is defending;
			}
			return;
		}
		
		if(GlobalState == 1){
			boolean found = false;
			for(Squad sqq : Squads){
				if(sqq.score >= eScore){
					sqq.target = pos;
					sqq.State = 2;
					sqq.operate();
					found = true;
				}
			}
			
			if(found == false){
				BuildSquadToCounter(eScore, pos);
			}
		}
		else {
			for(Squad squad : Squads){
				squad.target = pos;
				squad.setState(2);
				squad.operate();
				// 0 = idle, 1 = attacking, 2 is attacking;
			}
		}
		
	}
	

	void BuildSquadToCounter(int tscore, Position pos){
		boolean breaking = false;
		
		// if our total score isnt big enough to defend, we'll send all there
		if(tscore > manager.myScore){
			for(Squad squad : Squads){
				squad.newTarget(pos);
				squad.setState(2);
				squad.operate();
				breaking = true;
			}
			return;

		}
		
		if(breaking != true){
		int newscore = 0;
		ArrayList<Unit> list = new ArrayList<>();
		for(Unit unit : new ArrayList<>(myData.myMilUnits)){
			if(!list.contains(unit)){
				list.add(unit);
				newscore = newscore + manager.getScoreOf(unit);
				if(newscore >= tscore){
					Squads.add(new Squad(list, Squads.size(), myData, game, manager, util));
					//System.out.println("New Squad Assigned to Defend: " + pos);
					break;
				}
			}
		}


	}	
	
	
		
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
	
	public boolean isInCombat(Unit unit){
		if(unit.isAttacking() || unit.isUnderAttack() || unit.isStartingAttack() || unit.getOrder().equals(Order.AttackUnit)){
			return true;
		}
		
		return false;
	}
	
	BotBase getBaseAt(Base bass){
		for(BotBase basss : new ArrayList<>(myBases)){
			if(basss.Base.equals(bass)){
				return basss;
			}
		}
		
		return null;
	}
	
	boolean alreadyBasedHere(Base bass){
		
		for(BotBase vase : new ArrayList<>(myBases)){
			if(vase.Base.equals(bass)){
				return true;
			}
		}
		
		return false;
	}
	
	//Expansion placement broke
	// somehow...
	// WH0 KNOWS?
	// IT'S FIXED RETARD.
	// YOU KNOW WHAT ELSE ISN'T FIXED?
	// MY FUCKING INTERNET.
	// RIP ME 30/12/2019 - 3/1/-2020
	// YOU JUST HAD TO FUCK WITH THE CABLE DIDN'T YOU?
	// LITTLE SHIT.
	// GAMMA KNIFE THO
	
	public void squadDebug() {
		for(Squad sq : Squads){
   		 System.out.println("Squad State: " + sq.State);
   		 System.out.println("Squads Target: " + sq.type);
		 System.out.println("Squad Units: " + sq.getUnitSize());
		}
	}
	
//SIM
// GLOBAL
// sim check
// global sim
// ^^ TAGS FOR QUICK SEARCHING
	
	void GlobalSimCheck(){
		// i have rewrote this alot...
		ArrayList<Integer> simed = new ArrayList<>();
		for(Unit myUnit : new ArrayList<>(myData.myMilUnits)){
	    	 if(myData.isNearEnemyOrBetter(myUnit) && ShouldDoTheThingThatGivesAScoreBasedOnATonOfDiffernetThingsAndDoesStuffAccordingToThat(myUnit) && !simed.contains(myUnit)){
//	    		 ArrayList<Unit> mine = util.getCombatUnits(myUnit.getPosition(), 300);
//	    		 ArrayList<Unit> mine2 = util.getCombatUnits(myUnit.getPosition(), 300);
//	    		 boolean nearCP = false;	
	    		 Unit c = util.getClosestEnemyArmyUnit(myUnit.getPosition(),350,true);
	    		 Position start;
	    		 boolean canWin = false;
	    		 if(c == null){
	    			 start = myUnit.getPosition();
	    			 //util.Print("No enemy unit can be found");
	    		 }
	    		 else {
	    			 start = c.getPosition();
	    			 //util.Print("Sim unit: " + c.getType() +  " DISTY WISTY: " + myUnit.getPosition().getApproxDistance(start));
	    		 }
	    		 
//	    		 if(c != null){
//		    		 if(c.getPlayer().equals(self)){ //
//		    			 //game.sendText("WHAT THE FUCK");
//		    		 }
//	    		 }
//	    		 
	    		 double minScore = 0.55;
	    		 // https://www.youtube.com/watch?v=cZUpgzTVbyo
	    		 // seht ihr mich!
    			 ArrayList<FogUnit> bonus = getFogUnitsNearby(start, 350);
    			 
    			 VeryLarge big = new VeryLarge(myUnit.getPosition(),start,util,myData,bonus,350); // collect unit infomation..
    			// big.setAirUnits(); called inside class now 
    			 // Exception in thread "main" java.util.ConcurrentModificationException
    			 // fuck you
    			 //(Position beginA, Position beginB, Util til, Data dataa, ArrayList<FogUnit> fogUnits, int rad)

    			 // https://www.youtube.com/watch?v=5zhjnBwTCxk 
    			 
    			if(myUnit.getPosition().getApproxDistance(self.getStartLocation().toPosition()) < 1500){
    				 minScore = 0.35;
    			}
    				
    			if(big.p2Units.isEmpty()){
        			for(Unit unit : new ArrayList<>(big.combatReady)){
        				if(!simed.contains(unit.getID())){
        					simed.add(unit.getID());
        				}
        			}
    				continue;
    			}
    			
    			//simBattle2
    			
    			double cc = myData.myMilUnits.size() + myData.spellCasters.size();
    			double a = big.p1Units.size();
    			double n = a/cc;
    			int HPP = (int) (n*100);
    			
    			//util.Print("HPP Sim: " + HPP);
    			
    			if(HPP >= 70){ // IF BIG PORTION OF MY ARMY USE SIM BECAUSE MORE ACCURATE
    				big.simType = "Simulator";
    				Pair<ArrayList<Unit>, ArrayList<FogUnit>> ppp = new Pair<>(big.p2Units,big.fogUnits);
    				if(c == null){
    					c = big.p2Units.get(0);
    					canWin = manager.simBattle2(big.p1Units, ppp, 100, getPlayer(c.getPlayer()), c.getType().getRace());
        				double sc = manager.getEvaluatorScore(big.p1Units, ppp);
        				big.simScore = sc;
        				//util.Print("Score: " + sc);
    					// TFW GOING TO THE EFFORT TO SIM INSTEAD OF EVALUATE BUT END UP DOING IT LATER ANYWAYS
    					//util.Print("Can Win: " + canWin);
    	    			for(Unit unit : new ArrayList<>(big.p1Units)){
    	    				myData.newUnitScore(unit, big.simScore);
    	    			}
    				}
    				else{
        				canWin = manager.simBattle2(big.p1Units, ppp, 100, getPlayer(c.getPlayer()), c.getType().getRace());
        				double sc = manager.getEvaluatorScore(big.p1Units, ppp);
        				big.simScore = sc;
        				//util.Print("Score: " + sc);
        				// TFW GOING TO THE EFFORT TO SIM INSTEAD OF EVALUATE BUT END UP DOING IT LATER ANYWAYS
        				//util.Print("Can Win: " + canWin);
            			for(Unit unit : new ArrayList<>(big.p1Units)){
            				myData.newUnitScore(unit, big.simScore);
            			}
    				}

    			}
    			else {
    				big.simType = "Evaluator";
        			SimResult result = manager.evaluateBattle(big.p1Units, big.toSim, minScore, big, true); 
        			canWin = result.canWin;
        			big.simScore = result.simScore;
        			//util.Print("FogUnit Count: " + result.fs);
    			}
    			
    			//https://www.youtube.com/watch?v=Ph-CA_tu5KA
    			addIfApplicable(big);
    			
    			for(Unit unit : new ArrayList<>(big.combatReady)){
    				if(!simed.contains(unit.getID())){
    					simed.add(unit.getID());
    				}
    			}
    			

    			
    			Pair<ArrayList<Unit>, ArrayList<FogUnit>> airWar = new Pair<>(big.p2AirUnits, big.fogAirUnits);
    			// TODO FOG UNITS SIMS UNITS NOT UNIT TYPES YOU FUCKING MONG :)
    			SimResult airResult = manager.evaluateBattle(big.p1AirUnits, airWar, 0.60, big, true);
    			boolean AirStay = airResult.canWin;
    			big.AirStay = AirStay;

    			  			
	    		 if(!canWin){	
	    			 if(big.p2Units.isEmpty() == false){	
	    				 ArrayList<Unit> unitsToRetreat = util.combatReadyUnits(big.p1Units, myUnit.getPosition());
	    				// System.out.println("Enemies is not empty");
	    				 // 	
	    				 // SAFFFFFEEEEEEEEEEEEEEE ANDDDDDDD SOUNDDDDDDD
	    				 // HOLLLLLLLLDDDDDDDDDD YOOOOOOOOURRRRRR GRRRRRRRRRROOOOOOUUUUUUNNNNNNNND
	    				 Random rng = new Random();
		    			 for(Unit unit : unitsToRetreat){
		    				 //if(myData.isInSpotToRetreat(unit)){
		    					 // NO RETREAT IN STALINGRAD
		    					 // URAAAAAAAAAAAAAAAAAAAAA
		    				 
		    					 if(myData.getSimScore(unit) >= 0.35 && unit.getType().equals(UnitType.Protoss_Dark_Templar) && big.detection.getSecond() == false){
		    						 continue;
		    					 }
		    					 
		    					 if(myData.getSimScore(unit) >= 0.35 && unit.getType().equals(UnitType.Zerg_Lurker) && big.detection.getSecond() == false){
		    						 continue;
		    					 }
		    					 
		    					 if(unit.getType().isFlyer() && IsMilitrayUnit(unit) && AirStay){
		    						 // dont retreat flyers that can win
		    						 continue;
		    					 }
		    					 
		    					 if(unit.getOrder().equals(Order.EnterTransport)){
		    						 continue;
		    					 }
		    					 
		    					 util.reteatUnit(unit);
		    					 Squad SQ = getSquad(myUnit);
		    					 if(SQ != null){
		    						 SQ.newRetreater(unit, game.getFrameCount() + 100);
		    						// System.out.println("New retreater");
		    					 }
		    					 myData.DND(unit, 10);
		    					// System.out.println("Move");
		    					 if(unit.isBurrowed() && unit.canUnburrow()){
		    						 unit.unburrow();
		    					 }
		    					 
	    					 
		    					 if(unit.isSieged()){
		    						 unit.unsiege();
		    					 }
		    					 
		    					 if(unit.getType().equals(UnitType.Zerg_Lurker) || unit.getType().equals(UnitType.Terran_Siege_Tank_Siege_Mode)){
		    						 if(!retreaters.contains(unit)){
		    							 // tell them to unsiege/burrow and move back
		    							 retreaters.add(unit);
		    						 }
		    					 }
		    					 int fig = rng.nextInt(10);
		    					 
		    					 if(drawMoodYes){
			    					 if(getShitTalkersSize() <= 5 && !isAShitTalker(unit) && fig == 5){
			    						 drawMood(unit, 5);
			    					 }
		    					 }

		    				 //}
		    				 
		    			 }
	    			 }
	    		 }
	    		 else {
	    			 // if we can win
	    			 // check for those whom are retreating
	    			 Position ey = null;
	    			 if(!big.p2Units.isEmpty()){
	    				ey = big.p2Units.get(0).getPosition();
	    			 }
	    			 
	    			 for(Unit unit : big.p1Units){
    					 Squad SQ = getSquad(myUnit);
    					 Unit t = util.getUnitTarget(unit);
    					 if(SQ != null){
    						 SQ.removeFlee(unit);
    						 if(!retreaters.contains(unit)){
    							 retreaters.remove(unit);
    						 }
    					 }
    					 if(ey != null){
	    					 if(unit.isIdle()){
	    						 unit.attack(ey);
	    					 }
	    					 else {
	    						 
	    						 if(unit.getOrder().equals(Order.EnterTransport)){
	    							 continue;
	    						 }
	    						 
	    						 if(t != null){
		    						 Unit st = myData.findAGoodTargetToStomp(unit, big.p2Units);
		    						 if(st != null){
		    							 unit.attack(st);
		    							 continue;
		    						 }
	    						 }
	    						 
	        					 if(unit.getOrderTargetPosition().getApproxDistance(big.P2A) > 900 && myData.canBeDistrubed(unit)){
	        						 unit.attack(big.P2A); //AVERAGE POS OF P2 UNITS
	        					 } 
	    					 }
    					 }
    					    					 

	    			 }
	    		 }

	    	 }
		}
	}
	
	void assignDetecter(Unit unit){
		if(!Squads.isEmpty()){
			for(Squad sq : Squads){
				if(sq.detector == null){
					//System.out.println("New Detecter for squad: " + sq.id);
					sq.detector = unit;
					break;
				}
			}
		}
	}
	
	
	Unit getDetector(){
		for(Unit myUnit : self.getUnits()){
			if(myUnit.getType().isDetector() && !myUnit.getType().isBuilding()){
				return myUnit;
			}
		}
		
		return null;
	}
	
	boolean AllGeysersBuilt(){
		int max = 0;
		int i = 0;
		
		for(BotBase bass : myBases){
			if(!bass.Geysers.isEmpty()){
				max = max + bass.Geysers.size();
			}
		}
		
//		for(pBuilding p : pBuildings){
//			if(p.type.equals(self.getRace().getRefinery())){
//				i++;
//			}
//		}
		
		for(Builder build : builders){
			if(build.type.equals(self.getRace().getRefinery())){
				i++;
			}
		}
		
		if(self.allUnitCount(self.getRace().getRefinery()) + i >= max){
			return true;
		}
		
		return false;
	}
	
	
	boolean supplyQueued(UnitType type){
		
		if(pBuildings.isEmpty()){
			return false;
		}
		
		
		if(type.equals(UnitType.Zerg_Overlord)){
			// if zerg
			
			if(myData.howManyBeingMorphed(UnitType.Zerg_Overlord) > 0){
				return true;
			}
			
			if(UQ.contains(type)){
				return true;
			}
			
			return overLordsMorphing > 0;
			
			
		}
		else {
			
			
			if(pBuildings.get(0).type.equals(self.getRace().getSupplyProvider())){
				return true;
			}
			
			if(isInQueue(self.getRace().getSupplyProvider())){
				return true;
			}
			
			for(Builder builds : builders){
				if(builds.type.equals(type)){
					return true;
				}
			}
		}
		
		
		
		
		return false;
		
	}
	
	void AssignDetector(Squad squad){
		for(Unit unit : self.getUnits()){
			if(unit.getType().isDetector() && !unit.getType().isBuilding() && !DetectorAlreadyAssigned(unit)){
				squad.detector = unit;
				break;
			}
		}
	}
		
	boolean DetectorAlreadyAssigned(Unit unit){
		
		if(getSquad(unit) != null){
			return true;
		}
		
		return false;
	}
	
	Squad getClosest(Position pos){
		
		if(Squads.size() == 1){
			return Squads.get(0);
		}
		
		Squad chosen = null;
		int i = 0;
		for(Squad sq : Squads){
			if(sq.getUnitSize() != 0){
				int dist = sq.SquadsAverageDistTo(pos);
				if(chosen == null || dist <= 0){
					chosen = sq;
					i = dist;
				}
			}
		}
		
		
		return chosen;
	}
	
	boolean isVoidedWorker(Unit myUnit){
		for(BotBase sq : myBases){
			if(sq.voidedWorkers.contains(myUnit)){
				return true;
			}
		}
		
		return false;
	}
	
	boolean isABuilder(Unit unit){
		for(Builder b : builders){
			if(b.worker.equals(unit)){
				return true;
			}
		}
		
		return false;
	}
	
	boolean asdf(UnitType type){
		// what the fuck does this do again
		for(BotTech tech : stuffQueue){
			if(tech.type == 3 && tech.equals(type)){
				return true;
			}
		}
		
		return false;
	}
	
	boolean detAssignedASquad(Unit unit){
		
		for(Squad sq : Squads){
			if(sq.detector != null){
				if(sq.detector.equals(unit)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	boolean canSpend(int min, int gas){
		
		if(sMins < 0){
			sMins = 0;
		}
		
		if(sGas < 0){
			sGas = 0;
		}
		
		if(min == 0 && gas == 0){
			return true;
		}
		
		if(sMins == 0 && sGas == 0){
			return self.minerals() >= min && self.gas() >= gas;
		}
			
		return self.minerals() + sMins >= min  && self.gas() + sGas >= gas;
	}
	
	
	BotPlayer getWeakestOpponent(){
		int l = 0;
		
		for(BotPlayer ply : players){
			if(l == 0){
				l = ply.enemyScore;
			}
			
			if(ply.enemyScore <= l){
				l = ply.enemyScore;
			}
			
			
		}
		
		for(BotPlayer ply : players){
			if(l == ply.enemyScore){
				return ply;
			}
		}
		
		return null;
		
		
	}
	
	BotPlayer getStrongestOpponent(){
		int l = 0;
		
		for(BotPlayer ply : players){
			if(l == 0){
				l = ply.enemyScore;
			}
			
			if(ply.enemyScore >= l){
				l = ply.enemyScore;
			}
			
			
		}
		
		for(BotPlayer ply : players){
			if(l == ply.enemyScore){
				return ply;
			}
		}
		
		return null;
		
		
	}
	
	BotPlayer getPlayer(Unit unit){
		
	for(BotPlayer play : players){
		if(play.player.getUnits().contains(unit)){
			return play;
		}
	}
		
		return null;
	}
	
	
	BotPlayer getPlayer(Player t){
		
	for(BotPlayer play : players){
		if(play.player.equals(t)){
			return play;
		}
	}
		
		return null;
	}
	
	boolean isProductionBuilding(UnitType t){
		if(t.equals(UnitType.Terran_Barracks) ||
		 t.equals(UnitType.Terran_Factory) ||
		 t.equals(UnitType.Terran_Starport) ||
		 t.equals(UnitType.Terran_Command_Center) ||
		 t.equals(UnitType.Protoss_Nexus) ||
		 t.equals(UnitType.Protoss_Gateway) ||
		 t.equals(UnitType.Protoss_Robotics_Facility) ||
		 t.equals(UnitType.Protoss_Stargate) || t.equals(UnitType.Zerg_Infested_Command_Center)){
			return true;
		}
			
	
		return false;
	}
	
	void Expand(boolean setCancel){
		// https://www.youtube.com/watch?v=njvA03rMHx4
		// SAVE ANDDDDD SOOOUUUUUUUUND
		// HOOOOOOOOOOOOOOLD YOOOOOOUUUUUUUUUUURRRRRRRR GROOOOOOOOOOOOOOUND
		if(myData.nextExpand != null){
			pBuilding n = new pBuilding(self.getRace().getResourceDepot(), myData.nextExpand, 3, false, true, 1);
			n.canBeCancelled = setCancel;
			pBuildings.add(0, n);
			//util.Print("Expand");
			//System.out.println("Expanding");
		}
		else {
			game.sendText("Next Expand is null, cannot expand.");
			//System.out.println("Next Expand is null, cannot expand.");
		}
	}
	
	void EarlyExpand(){
		// https://www.youtube.com/watch?v=U4GXNzom6ik WHAT THE FUCK IS THIS? HOW HIGH WAS I
		// https://www.youtube.com/watch?v=4MDcSfuRzV0
		 // UnitType ype, TilePosition where, int max, boolean creep, boolean yes, int save, int frame, boolean c
		if(myData.nextExpand != null){
			pBuilding neww = new pBuilding(self.getRace().getResourceDepot(), myData.nextExpand, 3, false, true, 1);
			neww.canBeCancelled = false;
			pBuildings.add(0, neww);
			util.Print("Early expand xdddddddddd");
			UQ.clear();
			for(int i=0;i<4;i++){
				// add some workers too :)
				UQ.add(self.getRace().getWorker());
			}
		}
		else {
			util.Print("Early Expand is null, cannot expand.");
			//System.out.println("Early Expand is null, cannot expand.");
		}
	}
	
	ArrayList<UnitType> getNextItems(int a){
		// https://www.youtube.com/watch?v=2dbR2JZmlWo
		
		ArrayList<UnitType> ret = new ArrayList<>();
		
		if(pBuildings.isEmpty()){
			return null;
		}
		
		if(pBuildings.size() <= a){
			for(pBuilding s : new ArrayList<pBuilding>(pBuildings)){
				ret.add(s.type);
			}
			return ret;
		}
		
		for(int i = 0; i < a; i++){
			ret.add(pBuildings.get(i).type);
		}
		
		return ret;
		
		
	}
	
	ArrayList<pBuilding> getNextPItems(int a){
		ArrayList<pBuilding> ret = new ArrayList<>();
		
		if(pBuildings.isEmpty()){
			return null;
		}
		
		if(pBuildings.size() <= a){
			for(pBuilding s : new ArrayList<pBuilding>(pBuildings)){
				ret.add(s);
			}
			return ret;
		}
		
		for(int i = 0; i < a; i++){
			ret.add(pBuildings.get(i));
		}
		
		return ret;
		
	}
	
	boolean isBeingRepaired(Unit unit){
		if(repairs.isEmpty()){
			return false;
		}
		
		for(Repairer r : new ArrayList<>(repairs)){
			if(r.target.equals(unit)){
				return true;
			}
		}
		
		return false;
		
	}
	
	Repairer getRepairer(Unit unit){

		for(Repairer r : new ArrayList<>(repairs)){
			if(r.unit.equals(unit)){
				return r;
			}
		}
		
		
		return null;
	}
	
	Repairer getRepairerTarget(Unit unit){
		
		for(Repairer r : new ArrayList<>(repairs)){
			if(r.target.equals(unit)){
				return r;
			}
		}
		
		
		return null;
	}
	
	
	boolean isRepairer(Unit unit){
		
		for(Repairer r : new ArrayList<>(repairs)){
			if(r.unit.equals(unit)){
				return true;
			}
		}
		
		
		return false;
	}
	
	BotPlayer getPlayerP(Player p){
		for(BotPlayer pp : players ){
			if(p.equals(pp.player)){
				return pp;
			}
		}
		
		return null;
	}
	
	boolean needsDetectors(){
	Race rc = self.getRace();
	
	if(rc.equals(Race.Terran)){
		return self.allUnitCount(UnitType.Terran_Science_Vessel) + amountInUQ(UnitType.Terran_Science_Vessel) <= Squads.size();
	}
	
	if(rc.equals(Race.Protoss)){
	return self.allUnitCount(UnitType.Protoss_Observer) + amountInUQ(UnitType.Protoss_Observer) <= Squads.size();
	}
		
	return false;
	}
	
	boolean canExpand(){
		
		
		if(myData.currentTarget == null){
			return false;
		}
		
		if(myData.myMilUnits.isEmpty()){
			return false;
		}
		
		
		if(needsWorkersCritical() && self.getRace().equals(Race.Zerg)){
			return false;
		}
		
		
		if(PStats >= 35 && myBases.size() <= 4 && self.allUnitCount(strat.requirementToMemeMassExpand) > 0){
			return true;
		}

		// maybe fix early mass expanding
		// more memes
		// https://www.youtube.com/watch?v=t6HSlZBNwUQ
		// AAAAAAAAAAAAAH GET IT OUT OF MY HEAD.
		
		
		BotPlayer ply = getStrongestOpponent();
		
		if(ply != null){

			if(myData.hasEarlyExpanded == true && game.getFrameCount() < 10000){
				return false;
			}
			
			if(game.getFrameCount() < 9000 && ply.MilUnits.isEmpty()){
				//STOP FUCKUNG BLIND EXPANDING YOU DOPEY CUNT.
				return false;
			}
				
			ArrayList<Unit> defenders = new ArrayList<>(myData.myMilUnits);
			
			for(Unit unit : self.getUnits()){
				
				if(IsMilitrayBuilding(unit) && !defenders.contains(unit) && myBases.size() < 2){
					defenders.add(unit);
				}
						
				if(myData.isSpellCaster(unit) && !defenders.contains(unit)){
					defenders.add(unit);
				}
				
				if(unit.getType().isDetector() && !defenders.contains(unit)){
					defenders.add(unit);
				}
				
			}
			
			ArrayList<UnitType> types = ply.getOffensiveUnits();
			
	
			return manager.evaluateBattle2(defenders, types, ply.player, ply.race);
		
		}
		
		return false;
		
		
	}
	
	boolean needsSupply(){
		if(self.supplyUsed() >= self.supplyTotal() - 5 && self.supplyUsed() != 200 && self.incompleteUnitCount(self.getRace().getSupplyProvider()) == 0){
			return true;
		}
		
		return false;
	}
	
	
	Builder getBuilder(UnitType what, TilePosition where){
		
		if(builders.isEmpty()){
			return null;
		}
		
		for(Builder b : builders){
			if(b.type.equals(what)){
				if(b.where.getApproxDistance(where) < 5){
					return b;
				}
			}
		}
		
		return null;
	}
	
	
	void TransferWorkersTo(BotBase bass, int amount){
		int i = 0;
		for(BotBase b : myBases){	
			if(b != bass && b.Pawns.size() >= b.maxWorkers){
				int asd = 0;
				int overflow = b.Pawns.size() - b.maxWorkers;
				workers:
				for(Unit workers : new ArrayList<Unit>(b.Pawns)){
					if(getBase(workers) != null){
						if(this.workers.get(workers.getID()).equals(bass)){
							continue workers;
						}
					}
					BotBase bb = getBase(workers);
					if(bb != null){
					bb.pawnDeath(workers);
					}
					bass.assignWorker(workers);
					this.workers.put(workers.getID(), bass);
					asd++;
					amount--;
					//workers.move(pos);
					if(asd >= overflow){
						break;
					}
				}
			}
		}
		
	
		main:
		for(BotBase b : myBases){
			if(b != bass && b.Pawns.size() > amount){
				workers2:
				for(Unit unit : new ArrayList<Unit>(b.Pawns)){
					if(getBase(unit) != null){
						if(this.workers.get(unit.getID()).equals(bass)){
							continue workers2;
						}
					}
					BotBase bb = getBase(unit);
					if(bb != null){
					bb.pawnDeath(unit);
					}
					else {
						util.Print("Returned null lol eat a dick");
					}
					bass.assignWorker(unit);
					this.workers.put(unit.getID(), bass);
					i++;
					//unit.move(pos);
					if(i >= amount){
						break;
					}
				}
				if(i >= amount){
					break main;
				}
				
			}
		}
	}
	
	
	void CheckGasClog(){
		// check if we have a ton of minerals but not much gas.
		if(pBuildings.isEmpty()){
			return;
		}

		// its rewrite time
		pBuilding current = pBuildings.get(0);
		if(current != null){ // just incase lul
			UnitType type = current.type;
			if(self.minerals() * 6 >= type.mineralPrice() && self.gas() < type.gasPrice() && type.gasPrice() >= 100 && game.getFrameCount() >= 10000 && strat.filler != UnitType.None){
				// if we have a ton of minerals but no gas. 
				if(self.getRace().equals(Race.Terran)){
					pBuildings.add(0, new pBuilding(strat.filler, null, 300));
				}
				else if (self.getRace().equals(Race.Protoss)){
					pBuildings.add(0, new pBuilding(strat.filler, null, 300));
				}
				else {
					pBuildings.add(0, new pBuilding(strat.filler, null, 300));
				}
			}
		}
	}
	
	
	void SetSaveFor(UnitType type){
		for(pBuilding p : pBuildings){
			if(p.type.equals(type)){
				p.save = 1;
				break;
			}
		}
	}
	
	void SetSaveFor(pBuilding type){
		for(pBuilding p : pBuildings){
			if(p.equals(type)){
				p.save = 1;
				break;
			}
		}
	}
	
	BotBase getBaseThatHasMineral(Unit min){
		if(min == null){
			return null;
		}
		
		for(BotBase base : new ArrayList<>(myBases)){
			for(Mineral unit : base.Mins){
				if(unit.getUnit().equals(min)){
					return base;
				}
			}
		}
		
		return null;
	}
	
	void RushDetector(){
		ArrayList<UnitType> yes = new ArrayList<>();
		for(pBuilding p : new ArrayList<>(pBuildings)){
			yes.add(p.type);
		}
		
		if(self.getRace().equals(Race.Terran)){
			if(!yes.contains(UnitType.Terran_Engineering_Bay)){
				for(pBuilding p : new ArrayList<>(pBuildings)){
					if(p.type.equals(UnitType.Terran_Engineering_Bay)){
						pBuildings.remove(p);
						break;
					}
				}
				pBuilding neww = new pBuilding(UnitType.Terran_Engineering_Bay, null, 300);
				neww.save = 1;
				pBuildings.add(0, neww);
				pBuilding neww2 = new pBuilding(UnitType.Terran_Missile_Turret, myData.getDefencePos(), 300);
				neww2.save = 1;
				pBuildings.add(1, neww2);
				int i = 1;
				for(BotBase b : new ArrayList<>(myBases)){
					pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, b.depot.getTilePosition(), 300, 1));
					i++;
				}
				
			}
		}
		else if (self.getRace().equals(Race.Protoss)){

		}
		else {
			// don't need det for zerg
		}
	}
	
	ArrayList<Mineral> getTheBlueCuntsThatFuckWithYou(Base bass){
		ArrayList<Mineral> ret = new ArrayList<>();
		for(Mineral min : bass.getBlockingMinerals()){
			if(!ret.contains(min)){
				ret.add(min);
			}
		}
			
		if(ret.isEmpty()){
			return null;
		}
			
		return ret;
	}
	
	boolean alreadyPowerHere(TilePosition tile){
		
		if(game.hasPower(tile)){
			return true;
		}
		
		for(Unit unit : game.getUnitsInRadius(tile.toPosition(), 300)){
			if(unit.getType().equals(UnitType.Protoss_Pylon)){
				return true;
			}
		}
		
		
		
		return false;
	}
	
	boolean spellCastersContains(Unit unit){
		
		if(casters.isEmpty()){
			return false;
		}
		
		for(Spellcaster sp : casters){
			if(sp.unit.equals(unit)){
				return true;
			}
		}
		
		return false;
	}
	
	
	boolean stuffQueueHas(TechType tech){
		
		if(stuffQueue.isEmpty()){
			return false;
		}
		
		for(BotTech stuff : stuffQueue){
			if(stuff.type == 1){
				if(stuff.tech == tech){
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	boolean stuffQueueHas(UpgradeType tech){
		if(stuffQueue.isEmpty()){
			return false;
		}
		
		for(BotTech stuff : stuffQueue){
			if(stuff.type == 2){
				if(stuff.upgrade == tech){
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	boolean stuffQueueHas(UnitType tech){
		if(stuffQueue.isEmpty()){
			return false;
		}
		
		for(BotTech stuff : stuffQueue){
			if(stuff.type == 3){
				if(stuff.morphType == tech){
					return true;
				}
			}
		}
		
		return false;
	}
	
	
	boolean doesTheThingThatMakesItStartDoTheThingThatDoesStuffToOtherUnits(TechType type){
		UnitType need = type.whatResearches();
		if(need != null && need != UnitType.None){
			if(self.completedUnitCount(need) > 0){
				return true;
			}
			else {
				return false;
			}
		}
		
		return true;
	}
	
	boolean doesTheThingThatMakesItStartDoTheThingThatDoesStuffToOtherUnits(UpgradeType type){
		UnitType need = type.whatUpgrades();
		if(need != null && need != UnitType.None){
			if(self.completedUnitCount(need) > 0){
				return true;
			}
			else {
				return false;
			}
		}
		
		return true;
	}
	
	boolean doesTheThingThatMakesItStartDoTheThingThatDoesStuffToOtherUnits(UnitType type){
		UnitType need = type.whatBuilds().getFirst();
		if(need != null && need != UnitType.None){
			if(self.completedUnitCount(need) > 0){
				return true;
			}
			else {
				return false;
			}
		}
		
		return true;
	}
	
	void Surrender(){
		game.sendText("Plagiarism! " + currentTarget.race + " OP!");
		game.leaveGame();
	}
	
	Unit getClosestWorker(Position pos){
		Unit ret = null;
		int l = 0;
		
		if(self.allUnitCount(self.getRace().getWorker()) == 0){
			return null;
		}
		

		for(Unit unit : myData.getAllOf(self.getRace().getWorker())){
			int dist = unit.getPosition().getApproxDistance(pos);
			
			if(unit.isGatheringMinerals() == true && unit.isCompleted() && !isBuilder(unit)){
				if(dist <= l || ret == null){
					ret = unit;
					l = dist;
				}
			}
		}
		
		return ret;
	}
	
	BotTech doTheThingThatGivesBackTheTDependingOnTheGiven(UnitType ut, UpgradeType UT, TechType tt){
		
		if(!ut.equals(UnitType.None)){
			for(BotTech p : new ArrayList<>(stuffQueue)){
				if(p.morphType != UnitType.None){
					if(p.morphType == ut){
					return p;
				}
			}
		}
		
		}
			
		if(!UT.equals(UpgradeType.None)){
			for(BotTech p : new ArrayList<>(stuffQueue)){
				if(p.upgrade != UpgradeType.None){
					if(p.upgrade == UT){
						return p;
					}
				}

			}
		}
			
		if(!tt.equals(TechType.None)){
			for(BotTech p : new ArrayList<>(stuffQueue)){
				if(p.tech != TechType.None){
					if(p.tech == tt){
						return p;
					}
				}

			}
		}
			
			
		return null;

	}
	
	
	
	boolean DoesTrueTheAmountOf(UnitType type){
		return self.allUnitCount(type) > 0;
	}
	
	boolean DoesTrueTrueTheAmountOf(UnitType type){
		return self.completedUnitCount(type) > 0;
	}
		
	void checkTechGoals(){
		if(!techGoals.isEmpty()){
			UnitType next = techGoals.get(0);
		
			
			if(self.allUnitCount(next) > 0){
				util.Print("Already have: " + next.toString());
				techGoals.remove(next);
			}
			
			if(!next.equals(UnitType.None)){
				if(util.hasRequirementFor(next)){
					ArrayList<UnitType> whatsNext = getNextItems(2);
					ArrayList<pBuilding> pNext = new ArrayList<>();
					pNext.add(pBuildings.get(0));
					pNext.add(pBuildings.get(1));
					if(whatsNext.contains(next)){
						util.Print("Tech Goal is coming up");
						// if it's next
						// set it to save
						for(pBuilding p : pNext){
							if(p.type.equals(next) && p.save == 0){
								p.save = 1;
								util.Print("Set save for: " + next.toString() + " Index of: " + pBuildings.indexOf(p));
								if(!techGoals.isEmpty()){
									techGoals.remove(0);
								}
								break;
							}
						}
						
					}
					else {
						// else copy and remove the pBuilding to index 1 with save set
						pBuilding p = getPBuilding(next);
						util.Print("No tech goal within sight");
						if(p != null){
							pBuilding newP = p;
							newP.save = 1;
							pBuildings.add(0, newP);
							pBuildings.remove(p);
							util.Print("Tech Goal indentified. Setting " + next.toString() + " To index 0 and save");
							techGoals.remove(next);
						}
					}
				}
			}
		}
		
	}
	
	
	void BaseRape(){

		ArrayList<Unit> done = new ArrayList<>();

		
		for(Unit unit : myData.myMilUnits){
			ArrayList<Unit> enemyDefences = new ArrayList<>();
			ArrayList<Unit> enemyWorkers = new ArrayList<>();
			
			if(done.contains(unit)){
				continue;
			}
			
			
			
			if(unit.isAttacking()){
				Unit t = unit.getOrderTarget();
				if(t != null){
					if(t.getType().isWorker() || IsMilitrayBuilding(t)){
						continue;
					}
				}
			}
			
		
			for(Unit ed : game.getUnitsInRadius(unit.getPosition(), 500)){
				if(IsMilitrayBuilding(ed) && game.enemies().contains(ed.getPlayer())){
					enemyDefences.add(ed);
				}
				
				if(ed.getType().isWorker() && game.enemies().contains(ed.getPlayer())){
					enemyWorkers.add(ed);
				}
				
			}
			
			if(!myData.EnemyUnitsNearby(unit.getPosition(), 500)){
				// no enemy army
				// destroy defences first
				// stop attacking random shit pls
				// how about no
				// fuck you
				
				if(!enemyDefences.isEmpty()){
					Unit attack = util.getClosestEnemyArmyUnitFromArray(unit.getPosition(), enemyDefences);
					if(attack != null){
						if(unit.canAttack(attack) && myData.canBeDistrubed(unit)){
							unit.attack(attack);
							//util.Print("Unit: " + unit.getID() +  " targetting unit " + ed.getID() + " With a distance of: " + unit.getPosition().getApproxDistance(ed.getPosition()));
							break;
						}
					}
				}
				else {
					// if enemy defences is empty
					if(!enemyWorkers.isEmpty()){
						Unit attack = util.getClosestEnemyArmyUnitFromArray(unit.getPosition(), enemyWorkers);
						if(attack != null){
							if(unit.canAttack(attack) && myData.canBeDistrubed(unit)){
								unit.attack(attack);
								//util.Print("Unit: " + unit.getID() +  " targetting unit " + ed.getID() + " With a distance of: " + unit.getPosition().getApproxDistance(ed.getPosition()));
								break;
							}
						}
					
					}
					
				}
			}
			
		}
		
		
	}
	
	boolean isExpandingSoon(){
		
		if(pBuildings.size() < 2){
			for(pBuilding p : new ArrayList<>(pBuildings)){
				if(p.isExpand){
					return true;
				}
				
			}
			return false;
		}
		
		for(int i = 1; i<2;i++){
			ArrayList<pBuilding> get = new ArrayList<>(pBuildings);
			if(get.get(i-1).isExpand){
				return true;
			}
		}
		
		
		return false;
	}
	
	
	void findNicePlaceToPutToPBuildings(UnitType type, TilePosition where, int max, boolean save){
		
		if(type.getRace() != self.getRace()){
			if(self.allUnitCount(type.getRace().getWorker()) == 0){
				if(self.allUnitCount(type.getRace().getResourceDepot()) == 0){
					game.sendText("HEY IM TRYING TO BUILD" + type.toString() + " Without being able to build it");
					return;
				}
			}
		}
		
		pBuilding make;
		if(save){
			make = new pBuilding(type, where, max,1);
		}
		else {
			make = new pBuilding(type, where, max);
		}
		
		UnitType need = util.getRequirementFor(type);
		
		if(need == null){
			return;
		}
		
		
		util.Print("Need: " + need.toString());
		boolean done = false;
		boolean hasGas = false;
		boolean hasRequirement = false;
		
		
		if(self.allUnitCount(self.getRace().getRefinery()) > 0){
			hasGas = true;
		}
		
		if(self.allUnitCount(need) > 0){
			hasRequirement = true;
		}
		
		if(hasGas && hasRequirement){
			pBuildings.add(0, make);
			util.Print("Both requirements done. Placed " + type.toString() + " At: " + 0);
		}
			if(type.gasPrice() > 0){
				int i = 0;
				for(pBuilding p : new ArrayList<>(pBuildings)){
					//util.Print(""+p.type);
					i++;
						
					if(p.type.isRefinery()){
						hasGas = true;
						util.Print("Has gas");
					}
						
					if(p.type.equals(need)){
						hasRequirement = true;
						util.Print("Has requirement");
					}
						
					if(hasGas && hasRequirement){
						pBuildings.add(i + 1, make);
						util.Print("Inserted: " + type.toString() + " At: " + i);
						done = true;
						break;
					}
				}
			} 
			else {
				// doesn't cost gas
				for(pBuilding p : new ArrayList<>(pBuildings)){
					int index = pBuildings.indexOf(p);
						
					if(p.type.equals(need)){
						pBuildings.add(index + 1, make);
						util.Print("Inserted: " + type.toString() + " At: " + index);
						done = true;
					}
				}
			}
		

		if(done == false){
			util.Print("Failed to put: " + type.toString());
		}
		
	
		
	}
	
	void RandomRaceUpdate(){

		if(!raceInd){
		
			if(currentTarget.race.equals(Race.Protoss) && strat.type == "bio"){
				if(self.allUnitCount(UnitType.Terran_Bunker) == 0 && !placements.contains(UnitType.Terran_Bunker)){
					// if no bunkers. We'll change the location for the bunkers.
					// to the main choke
					game.sendText("Changing placements of defences to main ChokePoint");
					// inb4 concurrent
					ArrayList<pBuilding> safeAndSound = new ArrayList<>();
					for(pBuilding p : pBuildings){
						if(p.type.equals(UnitType.Terran_Bunker)){
							p.pos = myData.mainChoke.getCenter().toTilePosition();
						}
						safeAndSound.add(p);
					}
					moveDefencePos = true;
					pBuildings = safeAndSound;
					// i can lift you up
					// i can show you what you want to see
					// dumb fucker didn't even post the youtube link
					// https://www.youtube.com/watch?v=njvA03rMHx4
					
				}
			}
			
					
			if(currentTarget.race.equals(Race.Zerg) && self.getRace().equals(Race.Protoss) && strat.buildName != "Dumb DT build"){
				// PvZ
				boolean hass = false;
				for(pBuilding p : getNextPItems(5)){
					UnitType type = p.type;
					if(type.equals(UnitType.Protoss_Stargate)){
						hass = true;
					}
				}
				if(!hass){
				findNicePlaceToPutToPBuildings(UnitType.Protoss_Stargate, self.getStartLocation(), 300, false);
				}
			}
	
			
			if(currentTarget.race.equals(Race.Protoss) && self.getRace().equals(Race.Zerg)){
				// ZvP
				SetSaveFor(UnitType.Zerg_Lair);
				pBuilding yes = getPBuilding(UnitType.Zerg_Hydralisk_Den);
				if(yes != null){
					pBuilding neww = yes;
					pBuildings.remove(yes);
					neww.save = 1;
					boolean hasPool = false;
					boolean hasGas = false;
					
					if(self.allUnitCount(UnitType.Zerg_Spawning_Pool) > 0){
						hasPool = true;
					}
					
					if(self.allUnitCount(self.getRace().getRefinery()) > 0){
						hasGas = true;
					}
					
					
					if(hasPool && hasGas){
						pBuildings.add(0, neww);
					}
					else{
						for(pBuilding p : new ArrayList<>(pBuildings)){
							if(p.isExpand || p.type.equals(UnitType.Zerg_Creep_Colony)){
								continue;
							}
							
							if(p.type.equals(UnitType.Zerg_Spawning_Pool)){
								hasPool = true;
							}
							
							if(p.type.equals(UnitType.Zerg_Extractor)){
								hasGas = true;
							}

							if(hasPool && hasGas){
								int index = pBuildings.indexOf(p);
								pBuildings.add(index + 1, neww);
								break;
							}
							
						}
					}
					
				}

			}
			
					
			if(currentTarget.race.equals(Race.Terran) && self.getRace().equals(Race.Zerg)){
				// ZvT
				BotTech t = doTheThingThatGivesBackTheTDependingOnTheGiven(UnitType.None, UpgradeType.None, TechType.Lurker_Aspect);
				if(t != null){
					util.Print("Setting save for lurkers");
					t.save = true;
				}
				
				BotTech tt = doTheThingThatGivesBackTheTDependingOnTheGiven(UnitType.Zerg_Lair, UpgradeType.None, TechType.None);
				if(tt != null){
					util.Print("Setting save for lair also :)");
					tt.save = true;
				}
			}
			
			if(currentTarget.race.equals(Race.Zerg) && self.getRace().equals(Race.Zerg)){
				// ZvZ
				SetSaveFor(UnitType.Zerg_Lair);
				buildMainGoal.clear();
				if(!buildMainGoal.contains(UnitType.Zerg_Mutalisk)){
				buildMainGoal.add(UnitType.Zerg_Mutalisk);
				}
				
				BotTech tt = doTheThingThatGivesBackTheTDependingOnTheGiven(UnitType.Zerg_Lair, UpgradeType.None, TechType.None);
				if(tt != null){
					util.Print("Setting save for lair for mutas");
					tt.save = true;
				}
				
				pBuilding yes = getPBuilding(UnitType.Zerg_Spire);
				if(yes != null){
					pBuilding neww = yes;
					pBuildings.remove(yes);
					neww.save = 1;
					boolean hasPool = false;
					boolean hasGas = false;
					
					if(self.allUnitCount(UnitType.Zerg_Spawning_Pool) > 0){
						hasPool = true;
					}
					
					if(self.allUnitCount(UnitType.Zerg_Extractor) > 0){
						hasGas = true;
					}
					
					if(hasPool && hasGas){
						pBuildings.add(0, neww);
					}
					else {
						for(pBuilding p : new ArrayList<>(pBuildings)){
							if(p.isExpand || p.type.equals(UnitType.Zerg_Creep_Colony)){
								continue;
							}
							
							if(p.type.equals(UnitType.Zerg_Spawning_Pool)){
								hasPool = true;
							}
							
							if(p.type.equals(UnitType.Zerg_Extractor)){
								hasGas = true;
							}
							
							if(hasPool && hasGas){
								int index = pBuildings.indexOf(p);
								pBuildings.add(index + 1, neww);
								break;
							}
							
						}
					}

				}
			}
			
			raceInd = true;
		}
		
		
	}
	
	
	void updateEnemyOpening(BotPlayer p){
		
		if(p == null){
			game.sendText("Cannot update build cause player is null");
			return;
		}
		
		
		
		if(self.getRace().equals(Race.Zerg)){
			// z
			if(p.howManyHave(UnitType.Zerg_Spawning_Pool) > 0 && p.howManyHave(UnitType.Zerg_Drone) <= 9 && p.howManyHave(UnitType.Zerg_Hatchery) == 1){
				hasReacted = true;
				// Z AGGRESSION
				game.sendText("6 POOL REEEEEEEEEEEEEEEEEEEEE");
				clearWorkersFromQueue();
				for(int i = 0; i < 4; i++){
					UQ.add(Doggo);
				}
				
				if(pBuildings.get(0).type.isResourceDepot()){
					pBuildings.remove(0);
				}
				
				pBuildings.add(0, new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation()));
				stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
				
			}
			
			if(p.howManyHave(UnitType.Protoss_Gateway) > 2 && p.howManyHave(UnitType.Protoss_Probe) >= 6 && p.howManyHave(UnitType.Protoss_Probe) <= 10 && p.howManyHave(UnitType.Protoss_Nexus) == 1){
				for(int i = 0; i < 4; i++){
					UQ.add(Doggo);
				}
				hasReacted = true;
				clearWorkersFromQueue();
				game.sendText("Hey! no rushing! Bad boy!");
				pBuildings.add(0, new pBuilding(UnitType.Zerg_Creep_Colony, this.Expands.get(0).getLocation()));
				stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
				if(game.hasCreep(this.Expands.get(0).getLocation()) && !myData.EnemysNearby(this.Expands.get(0).getLocation().toPosition(), 500)){
					pBuildings.add(0, new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation()));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
				}
				
			}
			
			if(p.howManyHave(UnitType.Terran_Barracks) > 2){
				hasReacted = true;
				game.sendText("Hey that's alotta barracks");
				pBuildings.add(0, new pBuilding(UnitType.Zerg_Creep_Colony, this.Expands.get(0).getLocation()));
				stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
				if(game.hasCreep(this.Expands.get(0).getLocation()) && !myData.EnemysNearby(this.Expands.get(0).getLocation().toPosition(), 500)){
					pBuildings.add(0, new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation()));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
				}
			}
			
			
			
		}
		else if (self.getRace().equals(Race.Terran)){
			// t
			if(p.howManyHave(UnitType.Protoss_Gateway) > 2 && p.howManyHave(UnitType.Protoss_Probe) >= 6 && p.howManyHave(UnitType.Protoss_Probe) <= 12 && p.howManyHave(UnitType.Protoss_Nexus) == 1){
				for(int i = 0; i < 16; i++){
					UQ.add(ShootyBoy);
				}
				hasReacted = true;
				clearWorkersFromQueue();
				game.sendText("Hey! no rushing! Bad boy!");
				pBuildings.add(0, new pBuilding(UnitType.Terran_Bunker, myData.mainChoke.getCenter().toTilePosition()));
				pBuildings.add(0, new pBuilding(UnitType.Terran_Bunker, myData.mainChoke.getCenter().toTilePosition()));
				
			}
			
			if(p.howManyHave(UnitType.Zerg_Spawning_Pool) > 0 && p.howManyHave(UnitType.Zerg_Drone) <= 9 && p.howManyHave(UnitType.Zerg_Hatchery) == 1){
				// Z AGGRESSION
				game.sendText(" flood reeeeeeeeeeeeeee");
				hasReacted = true;
				clearWorkersFromQueue();
				for(int i = 0; i < 15; i++){
					UQ.add(ShootyBoy);
				}
				pBuildings.add(0, new pBuilding(UnitType.Terran_Bunker, myData.mainChoke.getCenter().toTilePosition()));
				pBuildings.add(0, new pBuilding(UnitType.Terran_Bunker, myData.mainChoke.getCenter().toTilePosition()));
				if(!getNextItems(3).contains(UnitType.Terran_Barracks)){
					pBuildings.add(0, new pBuilding(UnitType.Terran_Barracks, null, 200, 1)); // build barracks and save
					pBuildings.add(0, new pBuilding(UnitType.Terran_Barracks, null, 200, 1)); // build barracks and save
				}
				
			}
			
			
			
		}
		else {
			// p
			if(p.howManyHave(UnitType.Zerg_Spawning_Pool) > 0 && p.howManyHave(UnitType.Zerg_Drone) <= 9 && p.howManyHave(UnitType.Zerg_Hatchery) == 1){
				// Z AGGRESSION
				game.sendText("Lings REEEEEEEEEEEE");
				hasReacted = true;
				clearWorkersFromQueue();
				for(int i = 0; i < 4; i++){
					UQ.add(UnitType.Protoss_Zealot);
				}

				
			}
			
		}
	}
	
	
	void clearWorkersFromQueue(){
		 for(UnitType p : new ArrayList<UnitType>(UQ)){
			 if(p.equals(self.getRace().getWorker())){
			 UQ.remove(p);
			 }
		}
	}
	
	int amountInBuildingQueue(UnitType type){
		// p buildings 
		// queue
		// how many in queue
		// QueueAmount
		int ret = 0;
		
		for(pBuilding p : new ArrayList<>(pBuildings)){
			if(p.type.equals(type)){
				ret++;
			}
		}
		
		return ret;
	}
	

	
	void friendlyFireCheck(){
		for(Unit unit : util.getAllOf(UnitType.Terran_Siege_Tank_Siege_Mode)){
			if(myData.isInCombat(unit)){
				Unit target = unit.getOrderTarget();
				if(target != null){
					if(myData.isTankDoingAOppsie(target, 100)){
						ArrayList<Unit> yes = myData.GetEnemyUnitsNearby(unit.getPosition(), 350, true);
						if(yes != null){
							for(Unit unitt : yes){
								if(!myData.isTankDoingAOppsie(unitt, 100) && unit.canAttack(unitt)){
									unit.attack(unitt);
									game.drawLineMap(unit.getPosition(), unitt.getPosition(), Color.Purple);
									util.Print("Tank: " + unit.getID() + " Targeting: " + unitt.getID() + " With a dist of: " + unit.getPosition().getApproxDistance(unitt.getPosition()));
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	FogUnit getFogUnit(int id){
		
		if(fogUnits.isEmpty()){
			return null;
		}
		
		for(FogUnit f : fogUnits){
			if(f.unitID == id){
				return f;
			}
		}
		
		return null;
	}
	
	
	boolean isVisible(int id){
		return game.getUnit(id) != null;
	}
	
	
	ArrayList<FogUnit> getFogUnitsNearby(Position pos, int max){
		ArrayList<FogUnit> ret = new ArrayList<>();
		for(FogUnit f : new ArrayList<>(fogUnits)){		
			
			if(f.pos.equals(Position.Unknown)){
				continue;
			}
			
			if(f.pos.getApproxDistance(pos) <= max){
				ret.add(f);	
			}
		}
		
		
		return ret;
	}
	
	

	// util.combatReadyUnits(big.p1Units, myUnit.getPosition());
	
	
	boolean ShouldDoTheThingThatGivesAScoreBasedOnATonOfDiffernetThingsAndDoesStuffAccordingToThat(Unit unit){
		// Choosing when to sim a unit in Global Sim
		
//		if(!myData.canBeDistrubed(unit)){
//			return false;
//		}
		
		
		if(IsSquadUnit(unit)){
			// apparently just a isMilitrayUnit check
			return true;
		}
		
		if(util.isSpellCaster(unit.getType())){
			// 
			return true;
		}
		
		
		return false;
	}
	
	boolean isDoingTheMeme(Unit u){
		
		if(u.isConstructing() || u.getOrder().equals(Order.PlaceBuilding)){
			return true;
		}
		
		if(!u.getType().isWorker()){
			return true;
		}
		
			
		return false;
	}
	
	
	void drawVerySickAndCoolHealthInfomation(Unit u){
		double c = u.getType().maxHitPoints() + u.getType().maxShields();
		double a = u.getHitPoints() + u.getShields();
		double n = a/c;
		int HPP = (int) (n*100);
//		util.Print(""+c);
//		util.Print(""+a);
//		util.Print(""+n);
//		util.Print("5:40 AM BTW: " + HPP);
		Position pos = new Position(u.getX() + 5, u.getY() + 15);
		if(HPP > 70){
			game.drawTextMap(pos, Text.formatText(""+HPP, Text.Green));
		}
		else if (HPP < 70 && HPP >= 45){
			game.drawTextMap(pos, Text.formatText(""+HPP, Text.Yellow));
		}
		else if (HPP < 45 && HPP >= 25){
			game.drawTextMap(pos, Text.formatText(""+HPP, Text.Orange));
		}
		else {
			game.drawTextMap(pos, Text.formatText(""+HPP, Text.Red));
		}
		

	}
	
	
	
	void addIfApplicable(VeryLarge l){
		
		if(battles.isEmpty()){
			battles.add(l);
			return;
		}
		
		
		int max = battles.size();
		int i = 0;
		for(VeryLarge yeet : new ArrayList<>(battles)){
			int dist = yeet.mystart.getApproxDistance(l.mystart);
			
			if(dist > 300){
				i++;
			}
			
			if(i>=max){
				battles.add(l);
			}
		}
	}
	
	ArrayList<Unit> getWorkersToRepair(int amount, Position start){
		ArrayList<Unit> r = new ArrayList<>();
		int l = 0;
		int s = 0;
		int t = 0;
		if(amount >= self.allUnitCount(UnitType.Terran_SCV)){
			util.Print("Cannot orders repairs due to low SCV count");
			return null;
		}
		
		while (r.size() != amount){
			s = self.allUnitCount(UnitType.Terran_SCV);
			for(Unit unit : util.getAllOf(UnitType.Terran_SCV)){
				
				if(isRepairer(unit) || isABuilder(unit)){
					//NO.
					s=s-1;
					continue;
					
				}
							
				int dist = unit.getPosition().getApproxDistance(start);
				if(r.contains(unit)){
					continue;
				}
				
				if(s < amount){
					util.Print("Cannot orders repairs due to low SCV count");
					return null;
				}
				
				if(l == 0 || dist <= l){
					r.add(unit);
					l = l + 150;
				}
					
				
			}
			
			l = l + 150;

		}
		
		if(r.isEmpty()){
			return null;
		}
		
		return r;
	}
	
	ArrayList<Unit> getWorkersToRepair2(int amount, Position start){
		ArrayList<Unit> r = new ArrayList<>();
		HashMap<Unit, Integer> values = new HashMap<>(); 
		int l = 350;
		if(amount >= self.allUnitCount(UnitType.Terran_SCV)){
			util.Print("Cannot orders repairs due to low SCV count");
			return null;
		}
		
		for(Unit unit : util.getAllOf(UnitType.Terran_SCV)){
			int dist = unit.getPosition().getApproxDistance(start);
			
			if(isRepairer(unit) || isABuilder(unit) || unit.isConstructing()){
				// dont add the workers doing other important shit
				continue;
			}
			
			values.put(unit, dist);
		}
		
		//util.Print("Sizes: " + values.size());
		
		if(values.size() <= amount){
			util.Print("Cannot orders repairs due to low SCV count");
			return null;
		}
		
		for(int i = 0;i<amount;i++){
			//util.Print("L: " + l);
			for(Unit u : new ArrayList<>(values.keySet()) ){
				Integer d = values.get(u);
				//util.Print("u " + u.getID());
				//util.Print("d " + d);
				if(!r.contains(u)){
					if(d < l){
						r.add(u);
						values.remove(u);
					}
				}
				
				if(r.size() == amount){
					return r;
				}
			}
			
			l = l + 250;


		}
		
		
		if(r.isEmpty()){
			return null;
		}
		
		return r;
	}
	
	
	Race getRealRace(Player p){
		for(BotPlayer pp : players){
			if(pp.player.equals(p)){
				return pp.race;
			}
		}
		
		return Race.Unknown;
	}
	
	
	void checkFloat(){
		//https://www.youtube.com/watch?v=sJC05MPEpV8
		if(!this.strat.floatUnits.isEmpty()){
			for(UnitType type : new ArrayList<UnitType>(this.strat.floatUnits.keySet())){
				int cost =  this.strat.floatUnits.get(type);
				if(self.isUnitAvailable(type) && amountInUQ(type) < self.minerals() / cost){
					int builders = self.completedUnitCount(type.whatBuilds().getFirst());
					int amount = self.minerals() - this.strat.floatUnits.get(type) + 2;
					int am = util.howManyCanIMake(type, self.minerals(), self.gas());
					
					if(am > 5){
						am = 5;
					}
					
					if(amount > builders){
						amount = builders * am;
					}
					
					for(int i=0;i<amount;i++){
						UQ.add(type);
					}
					
					util.Print("Added: " +amount + " " + type.toString());
				}
				
			}
		}
		
	} // end of float
	
	
	void drawMood(Unit u, int state){
		Position draw = u.getPosition();
		draw = new Position(draw.x, draw.y + 10);
	      Random rand = new Random(); //instance of random class
	      int fCount = 35;
		if(state == 0){ // general lines
		      int upperbound = 35 + 1;
		      // public ShitTalk(Unit a, String s, Color d, Integer f, Game g, Position h){
		      switch (rand.nextInt(upperbound)){
		      case 1: 
		    	 shitTalkers.add(new ShitTalk(u, "Hello", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 2: 
		    	  shitTalkers.add(new ShitTalk(u, "Jawohl", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 3: 
		    	  shitTalkers.add(new ShitTalk(u, "What's for dinner?", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 4: 
		    	  shitTalkers.add(new ShitTalk(u, "I'm still in this chicken shit outfit...", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 5: 
		    	  shitTalkers.add(new ShitTalk(u, "Buying GF", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 6: 
		    	  shitTalkers.add(new ShitTalk(u, "Life is good...", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 7: 
		    	  shitTalkers.add(new ShitTalk(u, "I'm on smoko, so leave me alone.", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 8: 
		    	  shitTalkers.add(new ShitTalk(u, "What is love, baby dont hurt me", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 9: 
		    	  shitTalkers.add(new ShitTalk(u, "Heaps good aye", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 10: 
		    	  shitTalkers.add(new ShitTalk(u, "Is someone going out? i'm hungry", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 11: 
		    	  shitTalkers.add(new ShitTalk(u, "Song name?", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 12: 
		    	  shitTalkers.add(new ShitTalk(u, "Sauce?", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 13: 
		    	  shitTalkers.add(new ShitTalk(u, "is mayonnaise an instrument?", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
		      case 14: 
		    	  shitTalkers.add(new ShitTalk(u, "I got a pay increase!", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  loop:
		    	  for(Unit yoou : myData.GetMyUnitsNearby(u.getPosition(), 300, true)){
		    		  if(yoou.exists() && yoou != u){
		    			  shitTalkers.add(new ShitTalk(u, "Wait your getting paid?", Text.Yellow, game.getFrameCount() + 35, game, draw, game.getFrameCount() + 8));
		    			  break loop;
		    		  }
		    	  }
		    	  break;
		      case 15: 
		    	  shitTalkers.add(new ShitTalk(u, "Big iron on his hip", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  break;
	      	  case 16: 
	    	   shitTalkers.add(new ShitTalk(u, "What do you play this league?", Text.Blue, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 17: 
	    	   shitTalkers.add(new ShitTalk(u, "I'm bored", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  loop:
		    	  for(Unit yoou : myData.GetMyUnitsNearby(u.getPosition(), 300, true)){
		    		  if(yoou.exists() && yoou != u){
		    			  shitTalkers.add(new ShitTalk(u, "Hey bored!", Text.Yellow, game.getFrameCount() + 35, game, draw, game.getFrameCount() + 8));
		    			  break loop;
		    		  }
		    	  }
	    	   break;
	      	  case 18: 
	    	   shitTalkers.add(new ShitTalk(u, "Are we there let?", Text.Blue, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 19: 
	    	   shitTalkers.add(new ShitTalk(u, "Hang on.. im lagging", Text.Blue, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 20: 
	    	   shitTalkers.add(new ShitTalk(u, "wtf lag", Text.Blue, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 21: 
	    	   shitTalkers.add(new ShitTalk(u, "@everyone", Text.Red, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 22: 
	    	   shitTalkers.add(new ShitTalk(u, "Enter text here", Text.Red, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 23: 
	    	   shitTalkers.add(new ShitTalk(u, "Douchebag says what", Text.Red, game.getFrameCount() + 35, game, draw));
		    	  loop:
		    	  for(Unit yoou : myData.GetMyUnitsNearby(u.getPosition(), 300, true)){
		    		  if(yoou.exists() && yoou != u){
		    			  shitTalkers.add(new ShitTalk(u, "What?", Text.Yellow, game.getFrameCount() + 35, game, draw, game.getFrameCount() + 8));
		    			  break loop;
		    		  }
		    	  }
	    	   break;
	      	  case 24: 
	    	   shitTalkers.add(new ShitTalk(u, "Salutations exiles", Text.Red, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 25: 
	    	   shitTalkers.add(new ShitTalk(u, "Is it me or is it very boring here", Text.Red, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 26: 
	    	   shitTalkers.add(new ShitTalk(u, "Oh great... my commander is australian.", Text.Red, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 27: 
	    	   shitTalkers.add(new ShitTalk(u, "Free armor trim no scam", Text.Red, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 28: 
	    	   shitTalkers.add(new ShitTalk(u, "Anal [Destruction]", Text.Red, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 29: 
	    	   shitTalkers.add(new ShitTalk(u, "But dad i don't want to go to bed", Text.Red, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 30: 
	    	   shitTalkers.add(new ShitTalk(u, "O SHIT IM FEELING IT", Text.Red, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 31: 
	    	   shitTalkers.add(new ShitTalk(u, "Did someone say [Thunderfury, blessed blade of the windseeker]", Text.Yellow, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 32: 
	    	   shitTalkers.add(new ShitTalk(u, "All i crave is a good pub feed.", Text.Yellow, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 33: 
	    	   shitTalkers.add(new ShitTalk(u, "P90 rush b", Text.Yellow, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 34: 
	    	   shitTalkers.add(new ShitTalk(u, "Yes...", Text.Yellow, game.getFrameCount() + 35, game, draw));
	    	   break;
	      	  case 35: 
	    	   shitTalkers.add(new ShitTalk(u, "Invest in GME boys", Text.Yellow, game.getFrameCount() + 35, game, draw));
	    	   break;
	       }
			
		}	
		else if (state == 1){ // combat lines
		      int upperbound = 34 + 1;
		      // public ShitTalk(Unit a, String s, Color d, Integer f, Game g, Position h){
		      switch (rand.nextInt(upperbound)){
		      case 1: 
		    	 shitTalkers.add(new ShitTalk(u, "Fucking die!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 2: 
		    	 shitTalkers.add(new ShitTalk(u, "BRRRRRRRRRT", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 3: 
		    	 shitTalkers.add(new ShitTalk(u, "Come at me cunt!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 4: 
		    	 shitTalkers.add(new ShitTalk(u, "COVER ME", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 5: 
		    	 shitTalkers.add(new ShitTalk(u, "BRRR BRR, GAT GAT GAT", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 6: 
		    	 shitTalkers.add(new ShitTalk(u, "Enemy Spotted!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 7: 
		    	 shitTalkers.add(new ShitTalk(u, "REEEEEEE", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 8: 
		    	 shitTalkers.add(new ShitTalk(u, ">:D", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 9: 
		    	 shitTalkers.add(new ShitTalk(u, "Mobbed out, opps out", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 10: 
		    	 shitTalkers.add(new ShitTalk(u, "forward comrades!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 11: 
		    	 shitTalkers.add(new ShitTalk(u, "Hoorah!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 12: 
		    	 shitTalkers.add(new ShitTalk(u, "1v1 me bro", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 13: 
		    	 shitTalkers.add(new ShitTalk(u, "Nano machines son", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 14: 
		    	 shitTalkers.add(new ShitTalk(u, "Drunken Style!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 15: 
		    	 shitTalkers.add(new ShitTalk(u, "Hey commander, whats A move", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 16: 
		    	 shitTalkers.add(new ShitTalk(u, "Medic!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 17: 
		    	 shitTalkers.add(new ShitTalk(u, "Cadia broke before the guard did!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 18: 
		    	 shitTalkers.add(new ShitTalk(u, "Supress em!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 19: 
		    	 shitTalkers.add(new ShitTalk(u, "LIVE TO WIN", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  loop:
		    	  for(Unit yoou : myData.GetMyUnitsNearby(u.getPosition(), 300, true)){
		    		  if(yoou.exists() && yoou != u){
		    			  shitTalkers.add(new ShitTalk(u, "TILL YOU DIE!", Text.Yellow, game.getFrameCount() + 55, game, draw, game.getFrameCount() + 20));
		    			  break loop;
		    		  }
		    	  }
		      break;
		      case 20: 
		    	 shitTalkers.add(new ShitTalk(u, "Fuck em up!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 21: 
		    	 shitTalkers.add(new ShitTalk(u, "Mom get the camera!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 22: 
		    	 shitTalkers.add(new ShitTalk(u, "HAPPY FEET", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  loop:
		    	  for(Unit yoou : myData.GetMyUnitsNearby(u.getPosition(), 300, true)){
		    		  if(yoou.exists() && yoou != u){
		    			  shitTalkers.add(new ShitTalk(u, "WOMBO COMBO", Text.Yellow, game.getFrameCount() + 55, game, draw, game.getFrameCount() + 12));
		    			  break loop;
		    		  }
		    	  }
		      break;
		      case 23: 
		    	 shitTalkers.add(new ShitTalk(u, "URA URA URA URA", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 24: 
		    	 shitTalkers.add(new ShitTalk(u, "Spray and pray boys", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 25: 
		    	 shitTalkers.add(new ShitTalk(u, "YO THATS OPTIMAL", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 26: 
		    	 shitTalkers.add(new ShitTalk(u, "Face to foot style", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 27: 
		    	 shitTalkers.add(new ShitTalk(u, "Reloading!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 28: 
		    	 shitTalkers.add(new ShitTalk(u, "Should i be shooting?", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 29: 
		    	 shitTalkers.add(new ShitTalk(u, "Come 'ere", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 30: 
		    	 shitTalkers.add(new ShitTalk(u, "I didn't bring ammo... lol", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 31: 
		    	 shitTalkers.add(new ShitTalk(u, "That's not a knife, THIS is a knife", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 32: 
		    	 shitTalkers.add(new ShitTalk(u, "GWA GWA GWA", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 33: 
		    	 shitTalkers.add(new ShitTalk(u, "I'll show you who's boss of this gym", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 34: 
		    	 shitTalkers.add(new ShitTalk(u, "Your mommas a hoe", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 35: 
		    	 shitTalkers.add(new ShitTalk(u, "Get some", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      }
		}
		else if (state == 2){ // spell caster lines
		      int upperbound = 24 + 1;
		      // public ShitTalk(Unit a, String s, Color d, Integer f, Game g, Position h){
		      switch (rand.nextInt(upperbound)){
		      case 1: 
		    	 shitTalkers.add(new ShitTalk(u, "BEEP BEEP", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 2: 
		    	 shitTalkers.add(new ShitTalk(u, "Fucking YEEEEET", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 3: 
		    	 shitTalkers.add(new ShitTalk(u, "Coming Through", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 4: 
		    	 shitTalkers.add(new ShitTalk(u, "I should have chosen game design", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 5: 
		    	 shitTalkers.add(new ShitTalk(u, "You know nothing is researched right", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 6: 
		    	 shitTalkers.add(new ShitTalk(u, "Sponsored by gFuel", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 7: 
		    	 shitTalkers.add(new ShitTalk(u, "Leave some for me!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 8: 
		    	 shitTalkers.add(new ShitTalk(u, "=D", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 9: 
		    	 shitTalkers.add(new ShitTalk(u, "Thank you come again", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 10: 
		    	 shitTalkers.add(new ShitTalk(u, "What is energy?", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 11: 
		    	 shitTalkers.add(new ShitTalk(u, "Should i even be here?", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 12: 
		    	 shitTalkers.add(new ShitTalk(u, "REAL SHIT", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 13: 
		    	 shitTalkers.add(new ShitTalk(u, "Look at me, im helping", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 14: 
		    	 shitTalkers.add(new ShitTalk(u, "Just do it!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 15: 
		    	 shitTalkers.add(new ShitTalk(u, "Ye nah", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 16: 
		    	 shitTalkers.add(new ShitTalk(u, "Imagine spending gas on me", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 17: 
		    	 shitTalkers.add(new ShitTalk(u, "Letting the days go by", Text.Blue, game.getFrameCount() + 35, game, draw));
		    	  loop:
		    	  for(Unit yoou : myData.GetMyUnitsNearby(u.getPosition(), 300, true)){
		    		  if(yoou.exists() && yoou != u){
		    			  shitTalkers.add(new ShitTalk(u, "Water flowing underground", Text.Yellow, game.getFrameCount() + 55, game, draw, game.getFrameCount() + 12));
		    			  break loop;
		    		  }
		    	  }
		      break;
		      case 18: 
		    	 shitTalkers.add(new ShitTalk(u, "MOVE?!?!? ONTOP OF THE MINE??!!?", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 19: 
		    	 shitTalkers.add(new ShitTalk(u, "HA!", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 20: 
		    	 shitTalkers.add(new ShitTalk(u, "Don't not fear for i am here", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 21: 
		    	 shitTalkers.add(new ShitTalk(u, "He bought? Dump et", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 22: 
		    	 shitTalkers.add(new ShitTalk(u, "Would be shame if i missed", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 23: 
		    	 shitTalkers.add(new ShitTalk(u, "Hitting spells is not within my pay grade", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 24: 
		    	 shitTalkers.add(new ShitTalk(u, "Meatshield coming through", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      }
		}
		else if (state == 3){ // worker lines and buildings
		      int upperbound = 31 + 1;
		      // public ShitTalk(Unit a, String s, Color d, Integer f, Game g, Position h){
		      switch (rand.nextInt(upperbound)){
		      case 1: 
		    	 shitTalkers.add(new ShitTalk(u, "How did we turn blue crystals into this", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 2: 
		    	 shitTalkers.add(new ShitTalk(u, "WOOHOO overtime", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 3: 
		    	 shitTalkers.add(new ShitTalk(u, "I mine all day, i mine all night", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 4: 
		    	 shitTalkers.add(new ShitTalk(u, "My head hurts.", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 5: 
		    	 shitTalkers.add(new ShitTalk(u, "Fuck this, im joining the union", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 6: 
		    	 shitTalkers.add(new ShitTalk(u, "Join the army they said.. it'll be fun they said", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 7: 
		    	 shitTalkers.add(new ShitTalk(u, "I need a nap", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 8: 
		    	 shitTalkers.add(new ShitTalk(u, "Why did my parents kick me out", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 9: 
		    	 shitTalkers.add(new ShitTalk(u, "Okay but we already have one of these", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 10: 
		    	 shitTalkers.add(new ShitTalk(u, "I bought it from LIDL", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 11: 
		    	 shitTalkers.add(new ShitTalk(u, "Highly flammble? got it", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 12: 
		    	 shitTalkers.add(new ShitTalk(u, "Coming in hot", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 13: 
		    	 shitTalkers.add(new ShitTalk(u, "https://www.youtube.com/watch?v=0I71VX5RyGA", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 14: 
		    	 shitTalkers.add(new ShitTalk(u, "You want this over there? Fuck no", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 15: 
		    	 shitTalkers.add(new ShitTalk(u, "Fuck whoever designed this", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 16: 
		    	 shitTalkers.add(new ShitTalk(u, "At least buy me dinner first", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 17: 
		    	 shitTalkers.add(new ShitTalk(u, "Hans, get ze shovel", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 18: 
		    	 shitTalkers.add(new ShitTalk(u, "Aw shit i'm out of battery", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 19: 
		    	 shitTalkers.add(new ShitTalk(u, "Can someone help? .. no? .. okay", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 20: 
		    	 shitTalkers.add(new ShitTalk(u, "Only because you asked nicely", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 21: 
		    	 shitTalkers.add(new ShitTalk(u, "Only if my family is returned", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 22: 
		    	 shitTalkers.add(new ShitTalk(u, "This won't go wrong at all...", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 23: 
		    	 shitTalkers.add(new ShitTalk(u, "Why this? Okay but it's a mistake", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 24: 
		    	 shitTalkers.add(new ShitTalk(u, "Roger, putting it next to the explosives stockpile", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 25: 
		    	 shitTalkers.add(new ShitTalk(u, "You want fries with that?", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 26: 
		    	 shitTalkers.add(new ShitTalk(u, "I can't build a 6 pool", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 27: 
		    	 shitTalkers.add(new ShitTalk(u, "It would be hilarious if i put this here", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 28: 
		    	 shitTalkers.add(new ShitTalk(u, "This will block pathing.. you know?... alrighty then", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 29: 
		    	 shitTalkers.add(new ShitTalk(u, "You know what... i'm not going to ask", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 30: 
		    	 shitTalkers.add(new ShitTalk(u, "HA! getBUILDTILE FAILED", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      case 31: 
		    	 shitTalkers.add(new ShitTalk(u, "return njoll.", Text.Blue, game.getFrameCount() + 35, game, draw));
		      break;
		      }
		}
		else if (state == 4){ // panic lines
		      int upperbound = 9 + 1;
		      // public ShitTalk(Unit a, String s, Color d, Integer f, Game g, Position h){
		      switch (rand.nextInt(upperbound)){
		      case 1:
		    	  shitTalkers.add(new ShitTalk(u, "OH FUCK OH FUCK", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 2:
		    	  shitTalkers.add(new ShitTalk(u, "NO NO NO NO!", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 3:
		    	  shitTalkers.add(new ShitTalk(u, "I DO NOT CONSENT", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 4:
		    	  shitTalkers.add(new ShitTalk(u, "AH FUCK", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 5:
		    	  shitTalkers.add(new ShitTalk(u, "AAAAAAAAAAAAAAAH", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 6:
		    	  shitTalkers.add(new ShitTalk(u, "WHY ME", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 7:
		    	  shitTalkers.add(new ShitTalk(u, "FOR FUCK SAKE", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 8:
		    	  shitTalkers.add(new ShitTalk(u, "pain.jpg", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 9:
		    	  shitTalkers.add(new ShitTalk(u, "REEEEEEEEEEEEEEE", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 10:
		    	  shitTalkers.add(new ShitTalk(u, "PLEASE HELP ME", Text.Red, game.getFrameCount() + 35, game, draw));
		    	  loop:
		    	  for(Unit yoou : myData.GetMyUnitsNearby(u.getPosition(), 100, true)){
		    		  if(yoou.exists() && yoou != u){
		    			  shitTalkers.add(new ShitTalk(u, "NO FUCK THAT", Text.Blue, game.getFrameCount() + 35, game, draw, game.getFrameCount() + 5));
		    			  break loop;
		    		  }
		    	  }
		      break;
		      }
		}
		else if (state == 5) { // retreating
			// https://www.youtube.com/watch?v=7BTWpImCDq4
		      int upperbound = 16 + 1;
		      // public ShitTalk(Unit a, String s, Color d, Integer f, Game g, Position h){
		      switch (rand.nextInt(upperbound)){
		      case 1:
		    	  shitTalkers.add(new ShitTalk(u, "RUN FOR YOUR LIVES", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 2:
		    	  shitTalkers.add(new ShitTalk(u, "LEG IT CUNTS", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 3:
		    	  shitTalkers.add(new ShitTalk(u, "Fuck this shit im out", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 4:
		    	  shitTalkers.add(new ShitTalk(u, "Let's advance this way", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 5:
		    	  shitTalkers.add(new ShitTalk(u, "Why are we leaving? i just go here", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 6:
		    	  shitTalkers.add(new ShitTalk(u, "Thank god commander, there was like 20 zealots.", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 7:
		    	  shitTalkers.add(new ShitTalk(u, "I live again.", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 8:
		    	  shitTalkers.add(new ShitTalk(u, "I'm bleeding, making me the victor.", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 9:
		    	  shitTalkers.add(new ShitTalk(u, "Face to foot style how'd you like it", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 10:
		    	  shitTalkers.add(new ShitTalk(u, "BREAK CONTACT", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 11:
		    	  shitTalkers.add(new ShitTalk(u, "You just told us to attack 1 seconds ago..", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 12:
		    	  shitTalkers.add(new ShitTalk(u, "This commander sucks, who wants to desert", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 13:
		    	  shitTalkers.add(new ShitTalk(u, "Thank god, i didn't want to have to earn my pay", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 14:
		    	  shitTalkers.add(new ShitTalk(u, "THIS AINT FAIR", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 15:
		    	  shitTalkers.add(new ShitTalk(u, "Tactical retreat", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      case 16:
		    	  shitTalkers.add(new ShitTalk(u, "MEDIC", Text.Red, game.getFrameCount() + 35, game, draw));
		      break;
		      
		      }
		}
		else {
			
		}
		
	}
	
	boolean isAShitTalker(Unit u){
		
	
		if(shitTalkers.isEmpty()){
			return false;
		}
		
		for(ShitTalk t : shitTalkers){
			if(t.u.equals(u)){
				return true;
			}
		}
		
		return false;
		
		
		
	}
	
	int getShitTalkersSize(){
		
		
		if(shitTalkers.isEmpty()){
			return 0;
		}
		
		return shitTalkers.size();
		
		
	}
	
	ShitTalk getShitTalker(Unit u){
		if(shitTalkers.isEmpty()){
			return null;
		}
		
		for(ShitTalk t : shitTalkers){
			if(t.u.equals(u)){
				return t;
			}
		}
		
		return null;
		
		
	}
	
	FogUnit getFogUnit(Unit unit){
		
		
		
		for(FogUnit f : fogUnits){
			if(f.unit == unit){
				return f;
			}
		}
		
		return null;
	}
	
	boolean isALover(Unit u){
		if(lovers.isEmpty()){
			return false;
		}
		
		for(Lovers l : lovers){
			if(l.father == u){
				return true;
			}
		}
		
		return false;
	}
	
	void updatePylonScore(Unit unit, int size){
		
		if(!unit.getType().equals(UnitType.Protoss_Pylon)){
			return;
		}
		
		if(!pylonScores.containsKey(unit)){
			pylonScores.put(unit, size);
			return;
		}
		
		int value = pylonScores.get(unit);
		
		pylonScores.put(unit, value + size);
	}
	
	
	int getPylonScore(Unit u){
		
		if(!u.getType().equals(UnitType.Protoss_Pylon)){
			return 0;
		}
		
		if(!pylonScores.containsKey(u)){
			pylonScores.put(u, 0);
		}
		
		return pylonScores.get(u);

	}
	

	
} // end of class




	
