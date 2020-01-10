package Bot;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bk.ass.sim.*;

import bwapi.BWClient;
import bwapi.BWEventListener;
import bwapi.Bullet;
import bwapi.BulletType;
import bwapi.Color;
import bwapi.Flag;
import bwapi.Game;
import bwapi.GameType;
import bwapi.Order;
import bwapi.Player;
import bwapi.Position;
import bwapi.Race;
import bwapi.Region;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwapi.WalkPosition;
import bwapi.WeaponType;
import bwem.BWEM;
import bwem.Base;
import bwem.CPPath;
import bwem.ChokePoint;
import bwem.Mineral;

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
	Position globalRetreat;
	DecisionManager manager;
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
	int CSWins = 0;
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


    Bot() {
    	bwClient = new BWClient(this);
        bwClient.startGame();
    }
    

    @Override
    public void onStart() {

		game = bwClient.getGame();
		self = game.self();
		GlobalState = 0;
		game.setLocalSpeed(0);
		bewb = new BWEM(game);
		bewb.initialize();
		bewb.getMap().getData();
		bewb.getMap().assignStartingLocationsToSuitableBases();
		myStartLocation = getClosestBaseLocation(self.getStartLocation().toPosition());
		myData = new Data(game, bewb, myStartLocation);
		util = new Util(game, myData, bewb);
		startLocations = myData.startLocations;
	    Expands = myData.getExpands();
	    myChokes = myData.myChokes;
	    manager = new DecisionManager(game, myData);
	    strat = new Strategy(game.enemy().getRace(), Expands, myData, game, myChokes);
		globalRetreat = self.getStartLocation().toPosition();
		game.enableFlag(Flag.UserInput);
	    // TilePosition were, int maxx, int PII, int MDD, int many, Game gaemm, UnitType typp
	
		System.out.println(game.mapName());
		//System.out.println(TechType.Nuclear_Strike.energyCost());
		//game.sendText("1800 Free Elo Ryan speaking how am i be of service?");
		for(Player ply : game.getPlayers()){
			if(!ply.isNeutral() && ply.isEnemy(self)){
				players.add(new BotPlayer(ply.getRace(), game, util, myData, ply));
				//if(currentTarget == null){
					//currentTarget = players.get(0);
				//}
			}
		}
		
		if(cheats){
			game.sendText("Cheats are ON");
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
		int n = rand.nextInt(8) + 1;
		
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
    	else {
    		newDumbTag = " Has Disappointed my parents: ";
    		newDumbTag2 = " times this week. ";
    	}
	
		if(game.enemies().size() > 1){
			isFFA = true;
		}
		
		if(game.enemies().size() == 1){
			BotPlayer p = getPlayerP(game.enemy());
			if(p != null){
				currentTarget = p;
				myData.newTarget(p);
			}
		}
		
		for(int i = 0; i < 9; i++){
			UQ.add(self.getRace().getWorker());
		}
			
		for(int i = 0; i < 6; i++){
			UQ.add(self.getRace().getWorker());
		}
		
		if(self.getRace().equals(Race.Zerg)){
			for(int i = 0; i < 8; i++){
				UQ.add(self.getRace().getWorker());
			}
		}
		

		// but you've gotta break a few eggs in order to make an omelette :wink: -- Krais0 4:43 9/05/2019
		
		for(pBuilding p : strat.pBuildings){
			pBuildings.add(p);
		}
		
		for(BotTech p : strat.stuffQueue){
			stuffQueue.add(p);
		}
		
		
		game.sendText("KangarooBot all systems green." + " Playing with: " + myData.Expands.size() + " Expands." );
		
		//util.Print("Marine: " + self.weaponDamageCooldown(UnitType.Terran_Marine));
		//util.Print("Dragoon: " + self.weaponDamageCooldown(UnitType.Protoss_Dragoon));
		//util.Print("Hydra: " + self.weaponDamageCooldown(UnitType.Zerg_Hydralisk));
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
        	game.drawTextScreen(150, 20, "Versing " + game.enemy().getName() + " playing as: " + myData.enemyRace.toString());
    	}
    	game.drawTextScreen(150, 30, "NeedsToExpand: " + myData.needsToExpand +  " Current State: " + manager.canWin + " Frame Count: " + game.getFrameCount());
    	if(currentTarget != null){
    	game.drawTextScreen(150, 40, "CTS: " + currentTarget.attackPositions.size());
    	game.drawTextScreen(150, 50, "DEBUG: " + "D: " +  currentTarget.defenceScore + " A: " + currentTarget.armyScore + " PStats: " + PStats + " My Score: " + myData.myScore + " EnemyScore: " + myData.currentTarget.enemyScore);
    	}
    	else {
    		game.drawTextScreen(150, 40, "CTS is currently null");
    	}
        myData.onFrame();
        myData.myExpands = myBases;
    	myData.spellCasters = casters;
		myData.updateEco();
        
       if(UQ.isEmpty() == false){
   		main:
    	 for(UnitType next : new ArrayList<UnitType>(UQ)){
    	   int max = 0;
    	   if(next.isWorker()){
    		 
    			for(BotBase bass : myBases){
    				max = max + bass.maxWorkers;
    			}
    			
    			if(max > 80){
    				max = 80;
    			} 
    			
    			if(self.allUnitCount(self.getRace().getWorker()) >= max){
    				if(UQ.size() == 1){
    					UQ.clear();
    					game.sendText("Oh hey, i'm full on workers. Maybe i should stop making them");
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
    	   
    	   
    	   game.drawCircleMap(new Position(1000,1001), 30, Color.White);
    	   
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
    		   game.drawTextMap(myData.nextAttackPosition, "Send Storms");
    	   }
    	   else {
    		   game.drawTextMap(myData.nextAttackPosition, "Send Infestation Here");
    	   }
 
       }
       
      
   	   if(game.getFrameCount() >= winCheck){
   		   winCheck = game.getFrameCount() + 100;
   		   ArrayList<UnitType> enemies = new ArrayList<>();;
   		   BotPlayer ply = getWeakestOpponent();
   		   
   		   
   		   if(ply != null){
   			   
   			CheckGasClog();
   			   
   			   for(UnitType type : ply.Types){
   				   enemies.add(type);
   			   }
   			   
 			   for(UnitType type : ply.DTypes){
   				   enemies.add(type);
   			   }
 			   

 				   
			   ArrayList<UnitType> yes = getNextItems(5);
			   if(game.getFrameCount() <= 8000 && myData.canEarlyExpand(ply) && isFFA == false){
	    			if(!yes.contains(self.getRace().getResourceDepot())){
	    				Expand();
	    				game.sendText("Okay bing, can i get a expansion ;)");
	    				myData.hasEarlyExpanded = true;
	    			}
			   }

	   		  //manager.globalEvaluate(myData.myMilUnits, enemies);
	   		   manager.simBattle(myData.myMilUnits, enemies, 250, ply.player);
	   		  // System.out.println("Can Win Global: " + manager.canWin);
			   if(manager.canWin == true || self.supplyUsed() >= 185){
				   boolean expand = myData.needsToExpand;
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
			    	if(expand && canExpand()){
					yes = getNextItems(5);
			    		if(yes != null){
			    			if(!yes.contains(self.getRace().getResourceDepot())){
			    				Expand();
			    			}
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
				   
					if(pBuildings.get(0).type.isResourceDepot() && !self.getRace().equals(Race.Zerg) && PStats < -8){
					 sMins = 0;
					 pBuildings.remove(0);
					}
					
//					if(game.getFrameCount() > 9000 && PStats < -9){
//						 for(UnitType p : new ArrayList<UnitType>(UQ)){
//							 if(p.equals(self.getRace().getWorker())){
//								 UQ.remove(p);
//							 }
//						}
//					}
				   
			   }
		   
   		   }

	   
   	   }

   	   if(game.getFrameCount() >= simCheck){
   		GlobalSimCheck();
   		simCheck = game.getFrameCount() + 10;
   	   }
             
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
	    		   assignWorkerToBase(unit);
	    		   }    		  
	    	   }
	    	   
	    	   for(Mineral min : bass.Mins){
	    		   game.drawLineMap(min.getUnit().getPosition(), bass.depot.getPosition(), Color.Blue);
	    	   }
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
 	  
    	  if(item.equals(self.getRace().getRefinery()) && AllGeysersBuilt()){
    		  pBuildings.remove(next);
    		  cont = false;
    		  //System.out.println("Removed unnesscary refinery queued");
    	  }
    	  
    	  if(isP >= 1){
    		  sMins = item.mineralPrice();
    		  sGas = item.gasPrice();
    	  }
    	  
    	  if(creep == true){
    		  if(item.getRace().equals(Race.Zerg)){
	    		  if(!game.hasCreep(where)){
	    			 cont = false;
	    		  }
	    		  else {
	    			  cont = true;
	    		  }
    		  }
    		  else {
	    		  if(!game.hasPower(where)){
	    			 cont = false;
	    		  }
	    		  else {
	    			  cont = true;
	    		  }
    		  }
    	  }
  	  
    	  if(item.requiresPsi() && self.allUnitCount(UnitType.Protoss_Pylon) == 0){
    		  pBuildings.add(0, new pBuilding(UnitType.Protoss_Pylon, null));
    		  cont = false;
    		  System.out.println("no pylon for building. adding one");
    	  }
    	  
    	  if(item.requiresPsi() && self.completedUnitCount(UnitType.Protoss_Pylon) == 0){
    		  cont = false;				 
    	  }
    	  
    	  if(game.canMake(item) && !isInQueue(item) && cont == true){
    		  
        	  if(item.equals(UnitType.Terran_Bunker) && where == null){
         		 if(defencePos == null){
         			defencePos = myData.getDefencePos();
         		 }
         		 where = defencePos;
         	  }
         	  
         	  if(item.equals(UnitType.Terran_Missile_Turret) && where == null){
         		  if(self.allUnitCount(UnitType.Terran_Bunker) == 1){
         			  ArrayList<Unit> yes = util.getAllOf(UnitType.Terran_Bunker);
         			  if(yes != null){
         				  where = yes.get(0).getTilePosition();
         			  }	 
         		  }
         		  else {
         			 if(defencePos == null){
         			 defencePos = myData.getDefencePos();
         			}
         		  where = defencePos;
         		  }
         	  }
    		  
    		  Unit builder = getWorker();
    		  
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
    			  //TilePosition build = getBuildTile(builder, item, where, 300);
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
    				  }
    				  build = getBuildTile(builder, item, where, max);
    			  }
    			  else {
    				 if(item.isRefinery() || item.equals(UnitType.Protoss_Pylon)){
    					 build = getBuildTile(builder, item, where, max);
    				 }
    				 else {
    					 //build = getBuildTile(builder, item, where, max);
    					 // no longer broken thanks to yegers --> build = game.getBuildLocation(item, where, max);
    					 build = game.getBuildLocation(item, where, max);
    				 }
	    						
    			  }
    			  
    			  if(build != null){			  
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
    		  };
    	  }
      }
      else {
    	  // https://www.youtube.com/watch?v=asDlYjJqzWE
    	  // if pBuildings are empty.
    	  if(self.getRace().equals(Race.Zerg)){
    		 pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation(), 700));
    		 pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation(), 700));
    	  }
    	  else if(self.getRace().equals(Race.Terran)){
  			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 700));
  			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 700));
    	  }
    	  else {
    		pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null, 700));
    		pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null, 700));
    	  }
      }
          
      if(!pBuildings.isEmpty()){
			if(pBuildings.size() <= 4){
				for(pBuilding bq : pBuildings){
					cqs.append("Item: ").append(bq.getType()).append("\n");
				}
			}
			else{
				for(int i=0;i<=3;i++){
					cqs.append("Item: ").append(pBuildings.get(i).getType().toString()).append("\n");
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
					cqsss.append("Item: ").append(bq.name).append("\n");
				}
			}
			else{
				for(int i=0;i<=3;i++){
					cqsss.append("Item: ").append(stuffQueue.get(i).name).append("\n");
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
						assignWorkerToBase(unittt);
					}
					game.sendText("Hey Mr.Base where's your depot?");
	    	  }
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
    	  for(Squad sq : Squads){
  			 sq.squadMicro();
    	  }
      }
      
      
      if(game.getFrameCount() >= UICCheck){
    	  UICCheck = game.getFrameCount() + 10;
    	  for(Unit unit : self.getUnits()){
    		 if(IsMilitrayUnit(unit) || myData.isSpellCaster(unit) || IsMilitrayBuilding(unit)){
	    		 ArrayList<Unit> yes = util.getEnemyUnitsNearMe(unit, unit.getType().sightRange() + (unit.getType().groundWeapon().maxRange() + unit.getType().airWeapon().maxRange() / 2), true);
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
    	 
    	 improveCheck = game.getFrameCount() + 40;
    	 
    	 
    	 if(!stuffQueue.isEmpty()){
    		 mainLoop:
    		for(BotTech tech :  new ArrayList<>(stuffQueue)){
    			tech.isReady();
    			int type = tech.type;
    			TechType TT = tech.tech;
    			UpgradeType UP = tech.upgrade;
    			int i = stuffQueue.indexOf(tech);
    			
    			if(tech.save == true && tech.requirementsMet(tech.RID)){
    				if(type == 1){
    					sMins = TT.mineralPrice();
    					sGas = TT.gasPrice();
    				}
    				
    				if(type == 2){
       					sMins = UP.mineralPrice();
    					sGas = UP.gasPrice();
    				}
    				
    				if(type == 3){
       					sMins = tech.morphType.mineralPrice();
    					sGas = tech.morphType.gasPrice();
    				}
    			}
    			
    			
    			boolean brek = false;
    			if(tech.ready == true){
    				// if requirements are met
    				if(type == 1 && game.canResearch(TT)){
    					// TechTypes
    					for(Unit myUnit : self.getUnits()){
    						if(myUnit.canResearch(TT) == true && canSpend(TT.mineralPrice(), TT.gasPrice()) && myUnit.isCompleted() && !myUnit.isResearching()){
    							myUnit.research(TT);
    							if(tech.save){
    		    					sMins = sMins - TT.mineralPrice();
    		    					sGas = sGas - TT.gasPrice();
    							}
    							//System.out.println("Researching: " + tech.name);
    							brek = true;
    							stuffQueue.remove(i); 
    							break mainLoop;
    						}
    					}
    					
    				}
    				
    				if(type == 2 && game.canUpgrade(UP)){
    					// UpgradeTypes
       					for(Unit myUnit : self.getUnits()){
    						if(myUnit.canUpgrade(UP) == true && canSpend(UP.mineralPrice(), UP.gasPrice()) && myUnit.isCompleted() && !myUnit.isUpgrading()){
    							myUnit.upgrade(UP);
    							if(tech.save){
    		       					sMins = sMins - UP.mineralPrice();
    		    					sGas = sGas - UP.gasPrice();
    							}
    							//System.out.println("Upgrading " + tech.name);
    							brek = true;
    							if(UP.maxRepeats() <= 1){
    							stuffQueue.remove(i);
    							}
    							break mainLoop;
    						}
    					}
    					
    				}
    				if(type == 3 && game.canMake(tech.morphType)){
    					// UnitTypes
    					// mostly zerg
    				 	 for(Unit myUnit : self.getUnits()){
    				 		UnitType nextMorph = tech.morphType;
    				 		if(nextMorph != null && myUnit != null){
    				 			//System.out.println("" + nextMorph.toString() + " " + myUnit.getType().toString() + " " + myUnit.getID() + " ");
	    					    if(myUnit.canMorph(nextMorph) && canSpend(nextMorph.mineralPrice(), nextMorph.gasPrice()) && myUnit.isCompleted() && !myUnit.isMorphing()){
	    					    	myUnit.morph(nextMorph);
	    					    	if(tech.save){
	    				       			sMins = sMins - tech.morphType.mineralPrice();
	    				    			sGas = sGas -  tech.morphType.gasPrice();
	    					    	}
	    					    	brek = true;
	    					    	// System.out.println("Morphing: Unit: " + myUnit.getType().toString() + " To " +  tech.name);
	    					    	stuffQueue.remove(i);
	    					    	break mainLoop;
	    					    }
    				 		}
    				 	 }
    					
    				}
    				
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
    	}
     }
     
     //
     
     for(Unit myUnit : self.getUnits()){
    	 
    	 
    	 if(myUnit.getType().equals(UnitType.Protoss_Carrier)){
    		 if(myUnit.getInterceptorCount() != 6){
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
    				for(Unit unit : game.getUnitsInRadius(target.getPosition(), 250)){
    					if(unit.getPlayer().equals(self) && unit.canMove()){
    					//System.out.println("Unit found: " + unit.getID());
    					Position pos = util.GetKitePos2(unit, target);
    					if(pos != null){
    						//System.out.println("Not null");
    						if(unit.getType().equals(UnitType.Terran_Marine) || unit.getType().equals(UnitType.Terran_Firebat)){
    							if(!unit.isStimmed() && unit.canUseTech(TechType.Stim_Packs)){
    								unit.useTech(TechType.Stim_Packs);
    							}
    						}
    						unit.move(pos);
    						myData.DND(unit, game.getFrameCount() + 30);
    						game.drawLineMap(unit.getPosition(), pos, Color.Black);
    						game.drawTextMap(unit.getPosition(), "!");
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
    		 
    	 
    	 
    	 if(myUnit.getHitPoints() != myUnit.getType().maxHitPoints() && myUnit.getType().getRace().equals(Race.Terran) && myUnit.getType().isBuilding() && !myUnit.isBeingConstructed()){
    		 //repairs 
    		 if(!isBeingRepaired(myUnit)){
    			 if(myUnit.getType().equals(UnitType.Terran_Bunker) || myUnit.getType().equals(UnitType.Terran_Missile_Turret)){
    				 for(int i = 0; i<3; i++){
    	    			  Unit worker = getWorker();
    	    			  if(worker != null){
    	    				  BotBase bass = getBase(worker);
    	    				  repairs.add(new Repairer(worker, myUnit, true, game.getFrameCount() + 500));
    	    				  if(bass != null){
    	    					  bass.newVoidedWorker(worker);
    	    				  }
    	    			  }
    				 }
    			 }
    			 else {
    			  Unit worker = getWorker();
	    			  if(worker != null){
	    				  BotBase bass = getBase(worker);
	    				  repairs.add(new Repairer(worker, myUnit, false, 0));
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

    	
	    if(myUnit.getType().equals(UnitType.Zerg_Sunken_Colony) && myUnit.isAttacking()){
	    	ArrayList<Unit> enemy = myData.GetEnemyUnitsNearby(myUnit.getPosition(), 400, true);
	    		for(Unit enemies : enemy){
	    			if(game.enemies().contains(enemies.getPlayer()) && util.ShouldBeFocused(enemies)){
	    				if(myUnit.getDistance(enemies) < myUnit.getType().groundWeapon().maxRange()){
	    				myUnit.attack(enemies);
	    				break;
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
	    		 game.drawLineMap(myUnit.getPosition(), sq.target, Color.Purple);
	    		 game.drawTextMap(new Position(myUnit.getX() + 5, myUnit.getY() + 5), myUnit.getOrder().toString());
	    		 }
	    		 game.drawTextMap(new Position(myUnit.getX() + 15, myUnit.getY() + 12), "" + myData.getSimScore(myUnit));
    		 }
    		 int yes1 = myUnit.getX() - 10;
    		 int yes2 = myUnit.getY() - 10;
    		 game.drawTextMap(new Position(yes1, yes2), "UIC: " + myData.isNearEnemyOrBetter(myUnit));		 
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
  	 
    	 if(myUnit.getType().isWorker() == true && myUnit.isCompleted() && assignedToBase(myUnit) == false){
    		 assignWorkerToBase(myUnit);
    	 }
    	 
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
    		 
    		 if(!game.isVisible(where) && myUnit.isMoving() == false && myUnit.getPosition().getApproxDistance(where.toPosition()) > 30){
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
    		 
    		 if(game.isVisible(where)){
    			 if(!myUnit.isConstructing()){
    			 myUnit.build(type, where);
    			 }
   			 
    			 if(!game.canBuildHere(where, type, myUnit) && util.isPlacingBuilding(myUnit)){
        			builders.remove(build);
 	    			placements.remove(type);
	    			//sMins = sMins - type.mineralPrice();
	    			//sGas = sGas - type.gasPrice();
 	    			if(base != null){
 	    				base.unVoidWorker(myUnit);
 	    				//System.out.println("unVoided worker " + myUnit.getID());
 	    			}
        			 //System.out.println("Can't build: " + type.toString() + " At: " + where + " Trying again.");
    			 }
    			 
    		 }
    		 
    		 
    		 if(!where.isValid(game)){
    			builders.remove(build);
	    		placements.remove(type);
    			//sMins = sMins - type.mineralPrice();
    			//sGas = sGas - type.gasPrice();
    			System.out.println("invalid placement for " + type.toString() + " At: " + where);
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
    			System.out.println("No path to construct: " + type.toString() + " At: " + where);
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
		    	 
		    	 if(scouter.isAttacking()){
		    		 scouter.stop();
		    	 }
		    	 
    		 }
 					
    	 }
    	 	    	 
    	 if(scouter != null){
    		 if(scouter.exists()){
    			 Unit attacker = null;
		    	 boolean isBeingAttacked = false;
		    	 for(Unit unit : game.getUnitsInRadius(scouter.getPosition(), 200)){
		    		 if(unit.getOrder().equals(Order.AttackUnit) || unit.getOrder().equals(Order.AttackMove)){
    					 isBeingAttacked = true;
    					 attacker = unit;
    					 break;
    					 //System.out.println("Worker is being attacked");
		    		 }
		    	 }
    		 
		    	 // TODO fix the mass early expand
    			 
		 		if(isBeingAttacked){
					if(attacker != null){
						Position pos = util.GetKitePos2(scouter, attacker);
						if(pos != null){
						scouter.move(pos);
						}
						else {
						scouter.move(self.getStartLocation().toPosition(), false);
						}
					}
		 		}
		 		
		 		if(!isBeingAttacked){
		    		Position tt = util.scouterPriorityTask(scouter);
		    		if(tt != null){
		    		scouter.move(tt);
		    		}
		    		
		    		if(!scouter.isMoving()){
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
		    		
		 		}
		 		
		 		
    		}
    		
    	 }

    	   	 
    	 if(myUnit.getType().isWorker() && myUnit.isUnderAttack() == true && defenceCheck < game.getFrameCount()){
    		 defenceCheck = game.getFrameCount() + 24;
    		 if(scouter == null){
    			 DefenceCall(myUnit.getPosition());
    		 }
    		 if(myUnit != scouter){
    			 DefenceCall(myUnit.getPosition());
    		 }
    	 }
    	 
    	 if(myUnit.getType().isBuilding() && myUnit.isUnderAttack() == true){
    		 DefenceCall(myUnit.getPosition());
    	 }
    	 
    	 if(scouter!=null && myUnit.equals(scouter) && myUnit.exists()){
    		 BotBase bass = getBase(myUnit);
    		 if(!bass.voidedWorkers.contains(myUnit)){
    			 bass.newVoidedWorker(myUnit);
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
    		 
  
    	 //end of my units
     }
     
	     for(Unit unit : new ArrayList<Unit>(simmedUnits.keySet())){
	    	 if(simmedUnits.get(unit) <= game.getFrameCount()){
	    		 simmedUnits.remove(unit);
	    	 }
	     }
	     
	     for(Player plyy : game.enemies()){
		     for(Unit unit : plyy.getUnits()){
		    	 
		    	 if(unit.isCloaked() || unit.isBurrowed()){
		    		 game.drawTextMap(unit.getPosition(), "" + unit.getType().toString());
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
		 		
		 		if(self.allUnitCount(UnitType.Terran_Comsat_Station) > 0){
		 			if(!unit.getType().groundWeapon().equals(WeaponType.None) && !unit.getType().airWeapon().equals(WeaponType.None)){
				 		if(unit.isCloaked() || unit.isBurrowed()){
				 			if(isInCombat(unit) || unit.isMoving() || unit.getOrder().equals(Order.AttackMove)){
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
		 		
		 		}
		     
		    	 
		    	 // end of enemyUnits
		     }
	     
	     }
	     
	     //TODO dunno lol
	     
	     myData.players = players;
	     // end on onFrame
	     
	     
	     for(BotPlayer ply : players){
	    	 for(Unit unit : ply.units){
	    		 if(!ply.player.getUnits().contains(unit)){
	    			 ply.unitDeath(unit);
	    			 System.out.println("Caught possibly dead unit");
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
			
    		if(!unit.getType().isSpell() && IsSquadUnit(unit)){
    			assignUnit(unit);
    		}
    		
    		if(util.isSpellCaster(unit.getType())){
    			if(getSquad(unit) == null){
    			assignUnit(unit);
    			}
    		}
    			//  myChokes.get(1).getCenter().toTilePosition();
    		  		
    		if(unit.getType().isDetector() && !unit.getType().isBuilding()){
    			assignDetecter(unit);
    		}
    		
    		if(retreaters.contains(unit) && unit.getType().equals(UnitType.Terran_Siege_Tank_Tank_Mode)){
    			util.reteatUnit(unit);
    		}
    		
    		if(unit.getType().isResourceDepot()){
    			BotBase bass = getBaseDepot(unit);
    			if(bass != null){
    				bass.DepotFinished();
    				//util.Print("Base: " + myBases.indexOf(bass) + " Is done");
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
		
		if(IsMilitrayUnit(unit) || unit.getType().equals(UnitType.Terran_Medic)){
			Squad sq = getSquad(unit);
			
			if(sq!=null){
				sq.unitDeath(unit);
			}
			
			myData.unitDeath(unit);
			
			BotPlayer ply = getPlayer(unit.getPlayer());
			
			if(ply != null){
			ply.unitDeath(unit);
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
		

		
		
		
		
	}
	
    @Override
    public void onUnitCreate(Unit unit) {
    	
    	
		if(IsMilitrayUnit(unit)){
			myData.newMilUnit(unit);

			BotPlayer ply = getPlayer(unit.getPlayer());
			if(ply != null){
				ply.newMilUnit(unit);
			}
		}
		
		
					
		if(game.enemies().contains(unit.getPlayer())){
			// if enemy
			// https://www.youtube.com/watch?v=l9PxOanFjxQ
			
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
				
				if(unit.getType().isWorker()){
					ply.newWorker();
				}
			
//	    		if(ply.race != unit.getType().getRace()){
//	    			//System.out.println("" + unit.getType().getRace());
//	    			if(self.getRace().equals(Race.Terran) && unit.getType().getRace().equals(Race.Protoss)){
//	    				// we are only going to update our builds against P as T
//	    				// cause bio is trash
//	    				ply.updateRace(unit.getType().getRace());
//	    				System.out.println(ply.race.toString());
//	    				strat.UpdateAgainstRandom(placements, unit.getType().getRace());
//	    				pBuildings = strat.pBuildings;
//	    				game.sendText("Random player race scouted, adjusted build!");
//	    			}
//	    		}
	    		
			}
					
			
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
		    		for(int i = 0; i <= bass.getGeysers().size(); i++){
			    	pBuildings.add(0, new pBuilding(self.getRace().getRefinery(), null));
		    		}
    			}
    			}
    			
    			if(self.getRace().equals(Race.Protoss) && myBases.size() > 2 && self.completedUnitCount(UnitType.Protoss_Forge) > 0){
    				pBuildings.add(0, new pBuilding(UnitType.Protoss_Pylon, unit.getTilePosition(), 300));
    				pBuildings.add(1, new pBuilding(UnitType.Protoss_Photon_Cannon, unit.getTilePosition(), 70, true));
    				pBuildings.add(2, new pBuilding(UnitType.Protoss_Photon_Cannon, unit.getTilePosition(), 70, true));
    				pBuildings.add(3, new pBuilding(UnitType.Protoss_Photon_Cannon, unit.getTilePosition(), 70, true));
    			}
    			
    			if(needsWorkers()){
    				for(int i = 0; i<howManyWorkersDoINeed()/ 2;i++){
    					UQ.add(self.getRace().getWorker());
    				}
    			}
    		}
    		 
    		
    		if(placements.contains(unit.getType())){
    			pBuilding p = getPBuilding(unit.getType());
    			if(p != null){
    				if(p.save == 1){
    	    			sMins = sMins - unit.getType().mineralPrice();
    	    			sGas = sGas - unit.getType().gasPrice();
    				}
    				pBuildings.remove(p);
	    			placements.remove(unit.getType());

    			}
    			else {
    				if(!pBuildings.isEmpty()){
    					if(pBuildings.get(0).type.equals(unit.getType())){
    						pBuilding pp = pBuildings.get(0);
    						if(pp.save == 1){
    			    			sMins = sMins - unit.getType().mineralPrice();
    			    			sGas = sGas - unit.getType().gasPrice();
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
    			assignWorkerToBase(unit);
    		}
    		
    		
    		if(util.isSpellCaster(unit.getType())){
    			casters.add(new Spellcaster(unit, myData, util));
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
    		
    		// end of my units creation
    	}
    	
    	
    	
    }
    

	@Override
	public void onUnitDiscover(Unit unit) {
		
    	if(game.enemies().contains(unit.getPlayer())){
    		BotPlayer ply = getPlayer(unit.getPlayer());
    		
    		if(unit.getType().isBuilding() == true){
    			if(!IsMilitrayBuilding(unit)){
    				ply.newEnemyBuilding(unit);
    			}
    			else {
    				ply.newDBuilding(unit);
    			}
    			
    			if(game.enemies().size() == 1 && game.getFrameCount() < 8000){
    				if(openerCheck == 0){
    					openerCheck = game.getFrameCount() + 1000;
    				}
    				openerCheck = game.getFrameCount() + 300;
    			}
 
    		}
    		
    		if(unit.getType().isResourceDepot()){
    			Base bass = getClosestBaseLocation(unit.getPosition());
    			if(ply != null){
    			ply.newEnemyBase(bass);
	    		for(ChokePoint cp : bass.getArea().getChokePoints()){
	    		ply.NewChokePoint(cp);
	    		}
    			}
    		}
    		
    		//todo fix attackpositions
    		// and that retarded script below
    		
    		
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
		
		if(util.isSpellCaster(unit.getType()) && unit.getPlayer().equals(self)){
			if(getSquad(unit) == null){
			assignUnit(unit);
			casters.add(new Spellcaster(unit, myData, util));
			}
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
	    			sMins = sMins - unit.getType().mineralPrice();
	    			sGas = sGas - unit.getType().gasPrice();
    			}
    			else {
    				if(!pBuildings.isEmpty()){
    					if(pBuildings.get(0).type.equals(unit.getType())){
    						pBuildings.remove(0);
			    			placements.remove(unit.getType());
			    			sMins = sMins - unit.getType().mineralPrice();
			    			sGas = sGas - unit.getType().gasPrice();
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
				
    		if(unit.getType().isResourceDepot()){
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
	    			sMins = sMins - unit.getType().mineralPrice();
	    			sGas = sGas - unit.getType().gasPrice();
    			}
    			else {
    				if(!pBuildings.isEmpty()){
    					if(pBuildings.get(0).type.equals(unit.getType())){
    						pBuildings.remove(0);
			    			placements.remove(unit.getType());
			    			sMins = sMins - unit.getType().mineralPrice();
			    			sGas = sGas - unit.getType().gasPrice();
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
    		
    		
		}
				
		if(assignedToBase(unit)){
			BotBase bass = getBase(unit);
			if(bass != null){
				bass.pawnDeath(unit);
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
			

			
					
		}
		
		
	}
	

	@Override
	public void onPlayerDropped(Player arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onNukeDetect(Position arg0) {
		// TODO Auto-generated method stub
		
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
		if(!workers.containsKey(unit.getID())){

			for(BotBase bass : myBases){
				if(bass.Pawns.size() < bass.maxWorkers / 4){
					bass.assignWorker(unit);
					workers.put(unit.getID(), bass);
					break;
				}
			}
			
			
			for(BotBase bass : myBases){
				if(bass.Pawns.size() < bass.maxWorkers){
					bass.assignWorker(unit);
					workers.put(unit.getID(), bass);
					break;
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
		for(BotBase bass : myBases){
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
		for(Unit unit : workers){
			int dist = unit.getDistance(pos);
			if(dist <= c || c == 0){
				if(unit.isGatheringMinerals() == true && unit.isCompleted() && !isBuilder(unit)){
					c = dist;
					chosen = unit;
				}
			}
		}
		

	
		return chosen;
		
	}
	
	
	pBuilding getPBuilding(UnitType type){
		if(pBuildings.isEmpty() == true){
			return null;
		}
		
		for(pBuilding as : pBuildings){
			if(as.equals(type)){
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
		
		if(Damage > 0 && unit.getType().isWorker() == false && unit.getType().isBuilding() == false && unit.getType().isSpell() == false){
			return true;
		}
		return false;
	}
	
	
			
	public TilePosition getBuildTile(Unit builder, UnitType buildingType, TilePosition aroundTile, int maxrange) {
		TilePosition ret = null;
		int maxDist = 2;
		int stopDist = maxrange;
		
		if (buildingType.isRefinery()) {
			for(BotBase bass : myBases){
				for (bwem.Geyser an : bass.Geysers) {
					Unit n = an.getUnit();
					if(n.getType().equals(UnitType.Resource_Vespene_Geyser)){
					return n.getTilePosition();
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
							if(game.hasPower(new TilePosition(i,j))){
								continue;
							}
							else {
								return new TilePosition(i, j);
							}
						}
						

						if(!buildingType.equals(UnitType.Protoss_Photon_Cannon) || !buildingType.equals(UnitType.Terran_Bunker) || !buildingType.equals(UnitType.Zerg_Creep_Colony)){
							if(!util.HasBuildingsNearby(new Position(i, j), 10)){
								continue;
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
					}
				}
			}
			maxDist += 2;
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
	
	Position getAttackLocation(){
		if(!myData.enemyBuildings.isEmpty()){
			return myData.enemyBuildings.get(0).getPosition();
		}
		else {
			return null;
		}
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
				if(unit.getLoadedUnits().size() != 0){
					for(Unit unitt : unit.getLoadedUnits()){
						if(!myData.isNearEnemyOrBetter(unit)){
							unit.unload(unitt);
						}
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
		if(bunks != null){
			HashMap<Unit, Integer> yes = new HashMap<>();
			for(Unit unit : bunks){
				//System.out.println("Amount: " + util.getAmountGettingIn(unit));
				yes.put(unit, util.getAmountGettingIn(unit) + unit.getLoadedUnits().size());
				if(yes.get(unit) <= 3){
					ArrayList<Unit> marines = util.getAllOf(UnitType.Terran_Marine);
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
			int amount =  bass.maxWorkers - bass.Pawns.size();
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
	
		if(manager.canWin == true){
			//System.out.println("Drones: " + self.allUnitCount(UnitType.Zerg_Drone) + " Max: " + max);
				// else if we are maxed with drones, just make units
				if(self.getRace().equals(Race.Zerg)){
									
					if(self.allUnitCount(UnitType.Zerg_Defiler_Mound) > 0 && self.allUnitCount(UnitType.Zerg_Extractor) >= 3 && amountInUQ(UnitType.Zerg_Defiler) + self.completedUnitCount(UnitType.Zerg_Defiler) < Squads.size()){
						UQ.add(UnitType.Zerg_Defiler);
					}
					
					if(self.minerals() * 10 >= self.gas()){
						int VMax = Math.round(self.minerals() / 50);
						
						if(VMax >= 15){
							VMax = 15;
						}
						
						for(int i = 0; i < VMax; i++){
						UQ.add(UnitType.Zerg_Zergling);
						}
						
						if(self.minerals() * 9 >= self.gas()){
							for(int i = 0; i < self.allUnitCount(UnitType.Zerg_Zergling); i++){
							UQ.add(UnitType.Zerg_Zergling);
							}
						}
					}
					
					if(amountInUQ(UnitType.Zerg_Queen) + self.completedUnitCount(UnitType.Zerg_Queen) < Squads.size() && self.allUnitCount(UnitType.Zerg_Queens_Nest) > 0){
						UQ.add(UnitType.Zerg_Queen);
					}
					
					// z
					if(!myData.enemyRace.equals(Race.Protoss)){
						if(self.completedUnitCount(UnitType.Zerg_Hydralisk_Den) == 0){
							for(int i = 0; i < 4; i++){
							UQ.add(UnitType.Zerg_Zergling);
							}
							
						}
						else{
							for(int i = 0; i < 4; i++){
								UQ.add(UnitType.Zerg_Hydralisk);
							}
							
							for(int i = 0; i < 6; i++){
								UQ.add(UnitType.Zerg_Zergling);
							}
							
						}
					}
					else {
						
						if(self.completedUnitCount(UnitType.Zerg_Hydralisk_Den) == 0){
							for(int i = 0; i < 4; i++){
							UQ.add(UnitType.Zerg_Zergling);
							}
							
						}
						else {
							for(int i = 0; i < 4; i++){
							UQ.add(UnitType.Zerg_Hydralisk);
							}
						}
					}
					
				}
				else if (self.getRace().equals(Race.Terran)){
					//what to build if we are t and canWin
					int bioCount = self.allUnitCount(UnitType.Terran_Marine) + self.allUnitCount(UnitType.Terran_Firebat);
					int medicCount = self.allUnitCount(UnitType.Terran_Medic) + amountInUQ(UnitType.Terran_Medic);
					int medicNeed = bioCount - (medicCount / 4);
					
					if(self.completedUnitCount(UnitType.Terran_Factory) > 0){
						int VMax = Math.round(self.minerals() / 75);
						if(VMax >= 4){
							VMax = 4;
						}
						
						for(int i = 0; i < VMax; i++){
							UQ.add(UnitType.Terran_Vulture);
						}
					}
					
//					if(self.completedUnitCount(UnitType.Terran_Nuclear_Silo) > 0){
//						ArrayList<Unit> silos = util.getAllOf(UnitType.Terran_Nuclear_Silo);
//						if(silos != null){
//							for(Unit unit : silos){
//								if(!unit.hasNuke() && !unit.getOrder().equals(Order.NukeTrain)){
//									UQ.add(UnitType.Terran_Nuclear_Missile);
//								}
//							}
//						}
//					}
					
					//TODO vulture stuff (more against p)
					//TODO planting mines further away from other mines
					
					if(self.completedUnitCount(UnitType.Terran_Machine_Shop) > 0 && self.allUnitCount(UnitType.Terran_Siege_Tank_Tank_Mode) + self.allUnitCount(UnitType.Terran_Siege_Tank_Siege_Mode) > myData.getRecommendedUnitCount(UnitType.Terran_Siege_Tank_Tank_Mode,  currentTarget)){
						UQ.add(UnitType.Terran_Siege_Tank_Tank_Mode);
					}
										
					if(game.getFrameCount() > 20000){
						for(int i = 0;  i < self.completedUnitCount(UnitType.Terran_Barracks) * 5; i++){
							UQ.add(UnitType.Terran_Marine);
						}
					}
					else {
						int mMax = Math.round(self.minerals() / 50);
						if(mMax >= 15){
							mMax = 15;
						}
						
						for(int i = 0;  i < mMax; i++){
							UQ.add(UnitType.Terran_Marine);
						}

					}
					
					if(self.completedUnitCount(UnitType.Terran_Academy) > 0 && self.gas() >= 50){
						int asdf = currentTarget.howManyHave(UnitType.Protoss_Zealot) + currentTarget.howManyHave(UnitType.Zerg_Zergling);
						if(asdf >= 4){
							asdf = 4;
						}
						int ihave = self.completedUnitCount(UnitType.Terran_Firebat) + self.incompleteUnitCount(UnitType.Terran_Firebat);
						if(asdf != ihave){
							for(int i = 0; i <= asdf - ihave; i++){
								UQ.add(UnitType.Terran_Firebat);
							}
						}
						
					}
					
					if(self.completedUnitCount(UnitType.Terran_Covert_Ops) > 0){
						int g = self.allUnitCount(UnitType.Terran_Ghost);
						int need = Squads.size() - g; 
						if(need > 0){
							for(int i = 0; i < need; i++){
								UQ.add(UnitType.Terran_Ghost);
							}
						}
					}
					
					if(self.completedUnitCount(UnitType.Terran_Covert_Ops) > 0 && 
					self.allUnitCount(UnitType.Terran_Ghost) < currentTarget.player.allUnitCount(UnitType.Protoss_Dragoon) +
					currentTarget.player.allUnitCount(UnitType.Protoss_Reaver) + 
					currentTarget.player.allUnitCount(UnitType.Terran_Vulture) +
					currentTarget.player.allUnitCount(UnitType.Terran_Siege_Tank_Tank_Mode) +
					currentTarget.player.allUnitCount(UnitType.Terran_Siege_Tank_Siege_Mode) +
					currentTarget.player.allUnitCount(UnitType.Terran_Goliath))
					{
						UQ.add(UnitType.Terran_Ghost);
					}
					
					if(medicCount <= medicNeed && self.completedUnitCount(UnitType.Terran_Refinery) > 0){
						if(self.completedUnitCount(UnitType.Terran_Academy) > 0){
							for(int i = 0; i < self.allUnitCount(UnitType.Terran_Barracks); i++){
								UQ.add(UnitType.Terran_Medic);
							}
						}
					}
					
					if(currentTarget != null){
						if(currentTarget.race.equals(Race.Zerg)){
							if(self.allUnitCount(UnitType.Terran_Starport) > 0 && self.allUnitCount(UnitType.Terran_Control_Tower) > 0 && self.allUnitCount(UnitType.Terran_Armory) > 0){
								if(currentTarget.player.allUnitCount(UnitType.Zerg_Mutalisk) > 3){
									if(self.allUnitCount(UnitType.Terran_Valkyrie) < currentTarget.player.allUnitCount(UnitType.Zerg_Mutalisk) / 3){
										UQ.add(UnitType.Terran_Valkyrie);
									}
								}
							}
						}
						
						if(currentTarget.race.equals(Race.Protoss)){
							if(self.allUnitCount(UnitType.Terran_Starport) > 0 && self.allUnitCount(UnitType.Terran_Control_Tower) > 0 && self.allUnitCount(UnitType.Terran_Armory) > 0){
								if(currentTarget.player.allUnitCount(UnitType.Protoss_Carrier) > 3){
									if(self.allUnitCount(UnitType.Terran_Valkyrie) < currentTarget.player.allUnitCount(UnitType.Protoss_Carrier) / 4){
										UQ.add(UnitType.Terran_Valkyrie);
									}
								}
							}
						}
					}
					
					if(self.allUnitCount(UnitType.Terran_Starport) > 0 && self.allUnitCount(UnitType.Terran_Control_Tower) > 0){
						UQ.add(UnitType.Terran_Wraith);
					}
					
					if(self.allUnitCount(UnitType.Terran_Factory) > 0 && self.allUnitCount(UnitType.Terran_Armory) > 0){
						UQ.add(UnitType.Terran_Goliath);
					}
					
				}
				else {
					// what to build if we are toss and canWin
					
					if(self.allUnitCount(UnitType.Protoss_Zealot) + amountInUQ(UnitType.Protoss_Zealot) < self.allUnitCount(UnitType.Protoss_Dragoon) / 2 + 4 ){
						UQ.add(UnitType.Protoss_Zealot);
					}
					else {
						if(self.completedUnitCount(UnitType.Protoss_Cybernetics_Core) > 0){
							for(int i = 0; i < self.completedUnitCount(UnitType.Protoss_Gateway); i++){
								UQ.add(UnitType.Protoss_Dragoon);
							}
							UQ.add(UnitType.Protoss_Zealot);
						}
					}
					
					if(self.minerals() * 7 >= self.gas()){
						for(int i = 0; i < 4; i++){
							UQ.add(UnitType.Protoss_Zealot);
						}
					}
					
					if(self.completedUnitCount(UnitType.Protoss_Templar_Archives) > 0 && self.allUnitCount(UnitType.Protoss_Dark_Templar) + amountInUQ(UnitType.Protoss_Dark_Templar) < 6){
						UQ.add(UnitType.Protoss_Dark_Templar);
					}
					
					if(self.completedUnitCount(UnitType.Protoss_Stargate) > 0){
						UQ.add(UnitType.Protoss_Scout);
					}
					
					if(self.completedUnitCount(UnitType.Protoss_Templar_Archives) > 0 && self.allUnitCount(UnitType.Protoss_High_Templar) + amountInUQ(UnitType.Protoss_High_Templar) < 6){
						UQ.add(UnitType.Protoss_High_Templar);
					}
					
					if(self.completedUnitCount(UnitType.Protoss_Stargate) > 0 && self.completedUnitCount(UnitType.Protoss_Fleet_Beacon) > 0 && myBases.size() >= 3){
						UQ.add(UnitType.Protoss_Carrier);
					}
									
				}
				
		}
		else {
			// if we can't win
			
			if(self.getRace().equals(Race.Zerg)){
			// If we can't win zerg
				
				
				if(self.allUnitCount(UnitType.Zerg_Defiler_Mound) > 0 && self.allUnitCount(UnitType.Zerg_Extractor) >= 3 && amountInUQ(UnitType.Zerg_Defiler) + self.completedUnitCount(UnitType.Zerg_Defiler) < Squads.size()){
					UQ.add(UnitType.Zerg_Defiler);
				}
							
				if(self.minerals() * 10 >= self.gas()){
					int VMax = Math.round(self.minerals() / 50);
					
					if(VMax >= 15){
						VMax = 15;
					}
					
					for(int i = 0; i < VMax; i++){
					UQ.add(UnitType.Zerg_Zergling);
					}
				}

				if(self.completedUnitCount(UnitType.Zerg_Ultralisk_Cavern) > 0 && self.allUnitCount(UnitType.Zerg_Ultralisk) <= 5){
					UQ.add(UnitType.Zerg_Ultralisk);
					// ALWAYS BUILD ULTRAS CAUSE WHY NOT
				}
				
				if(self.completedUnitCount(UnitType.Zerg_Spire) > 0 || self.completedUnitCount(UnitType.Zerg_Greater_Spire) > 0){
					if(self.allUnitCount(UnitType.Zerg_Mutalisk) < 10 && self.gas() >= 100){
						for(int i = 0; i <= 6; i++){
						UQ.add(UnitType.Zerg_Mutalisk);
						}
					}
				}
				
				// ????
				if(game.canMake(UnitType.Zerg_Lurker) && self.allUnitCount(UnitType.Zerg_Lurker) <= 4 && !asdf(UnitType.Zerg_Lurker)){
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Lurker, myData));
				}
				
				if(myData.enemyRace.equals(Race.Protoss)){
					// if we are z
					// and can't win vs P
					// Hydra Spam
					if(self.completedUnitCount(UnitType.Zerg_Hydralisk_Den) == 1){
						for(int i = 0; i < 8; i++){
							UQ.add(UnitType.Zerg_Hydralisk);
						}
						
					}
					else {
						for(int i = 0; i < 6; i++){
							UQ.add(UnitType.Zerg_Zergling);
						}
					}
							
			}
				
			if(myData.enemyRace.equals(Race.Terran)){	
				// if we can't win vs T
				// mutas and stuff
				if(self.completedUnitCount(UnitType.Zerg_Hydralisk_Den) == 0){
					for(int i = 0; i < 10; i++){
					UQ.add(UnitType.Zerg_Zergling);
					}
				}
				else{
					if(self.completedUnitCount(UnitType.Zerg_Spire) > 0 || self.completedUnitCount(UnitType.Zerg_Greater_Spire) > 0){
						UQ.add(UnitType.Zerg_Mutalisk);
					}
					else {
						for(int i = 0; i < 3; i++){
							UQ.add(UnitType.Zerg_Hydralisk);
						}
						for(int i = 0; i < 20; i++){
							UQ.add(UnitType.Zerg_Zergling);
						}
						

					}

				}

			}
					
			if(myData.enemyRace.equals(Race.Zerg) || myData.enemyRace.equals(Race.Unknown)){	
				// if we can't win vs Z or Unknown
				if(self.completedUnitCount(UnitType.Zerg_Hydralisk_Den) == 0){
					for(int i = 0; i < 4; i++){
					UQ.add(UnitType.Zerg_Zergling);
					}
				}
				else{
					for(int i = 0; i < 4; i++){
					UQ.add(UnitType.Zerg_Hydralisk);
					}
				}

					
				if(self.completedUnitCount(UnitType.Zerg_Spire) > 0 || self.completedUnitCount(UnitType.Zerg_Greater_Spire) > 0){
					for(int i = 0; i < 4; i++){
						UQ.add(UnitType.Zerg_Mutalisk);
					}
						
				}
									
					
				}
			
			}
			else if (self.getRace().equals(Race.Terran)){
				// if we are terran and we can't win
				int bioCount = self.allUnitCount(UnitType.Terran_Marine) + self.allUnitCount(UnitType.Terran_Firebat);
				int medicCount = self.allUnitCount(UnitType.Terran_Medic) + amountInUQ(UnitType.Terran_Medic);
				
				if(self.completedUnitCount(UnitType.Terran_Barracks) > 0){
					if(game.getFrameCount() > 20000){
						for(int i = 0;  i < self.completedUnitCount(UnitType.Terran_Barracks) * 5; i++){
							UQ.add(UnitType.Terran_Marine);
						}
					}
					else {
						int mMax = Math.round(self.minerals() / 50);
						if(mMax >= 15){
							mMax = 15;
						}
						
						for(int i = 0;  i < mMax; i++){
							UQ.add(UnitType.Terran_Marine);
						}

					}

				}
				
				if(self.completedUnitCount(UnitType.Terran_Academy) > 0 && self.gas() >= 50){
					int asdf = currentTarget.howManyHave(UnitType.Protoss_Zealot) + currentTarget.howManyHave(UnitType.Zerg_Zergling);
					if(asdf >= 4){
						asdf = 4;
					}
					int ihave = self.completedUnitCount(UnitType.Terran_Firebat) + self.incompleteUnitCount(UnitType.Terran_Firebat);
					if(asdf != ihave){
						for(int i = 0; i <= asdf - ihave; i++){
							UQ.add(UnitType.Terran_Firebat);
						}
					}
					
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
					int g = self.allUnitCount(UnitType.Terran_Ghost);
					int need = Squads.size() - g; 
					if(need > 0){
						for(int i = 0; i < need; i++){
							UQ.add(UnitType.Terran_Ghost);
						}
					}
				}
	
				// more TvP stuff
				
				if(Math.round(bioCount / 6) >= medicCount){
					if(self.completedUnitCount(UnitType.Terran_Academy) > 0 && self.completedUnitCount(UnitType.Terran_Refinery) > 0){
						UQ.add(UnitType.Terran_Medic);
					}
				}

				if(self.completedUnitCount(UnitType.Terran_Factory) > 0){
					int VMax = Math.round(self.minerals() / 75);
					if(VMax >= 4){
						VMax = 4;
					}
					
					for(int i = 0; i < VMax; i++){
						UQ.add(UnitType.Terran_Vulture);
					}
				}
				
				if(currentTarget != null){
					if(currentTarget.race.equals(Race.Protoss)){
						if(self.completedUnitCount(UnitType.Terran_Machine_Shop) > 0 && self.completedUnitCount(UnitType.Terran_Vulture) > self.allUnitCount(UnitType.Terran_Siege_Tank_Tank_Mode) + self.allUnitCount(UnitType.Terran_Siege_Tank_Siege_Mode) + 3){
							UQ.add(UnitType.Terran_Siege_Tank_Tank_Mode);
						}
						if(game.getFrameCount() < 10000){
							for(int i = 0; i < self.allUnitCount(UnitType.Terran_Factory) * 2; i++){
								UQ.add(UnitType.Terran_Vulture);
							}
						}
					}
					else {
						if(self.completedUnitCount(UnitType.Terran_Machine_Shop) > 0){
							UQ.add(UnitType.Terran_Siege_Tank_Tank_Mode);
						}
					}
				}
				if(currentTarget != null){
					if(currentTarget.race.equals(Race.Zerg)){
						if(self.allUnitCount(UnitType.Terran_Starport) > 0 && self.allUnitCount(UnitType.Terran_Control_Tower) > 0 && self.allUnitCount(UnitType.Terran_Armory) > 0){
							if(currentTarget.player.allUnitCount(UnitType.Zerg_Mutalisk) > 3){
								if(self.allUnitCount(UnitType.Terran_Valkyrie) < currentTarget.player.allUnitCount(UnitType.Zerg_Mutalisk) / 3){
									UQ.add(UnitType.Terran_Valkyrie);
								}
							}
						}
					}
					
					if(currentTarget.race.equals(Race.Protoss)){
						if(self.allUnitCount(UnitType.Terran_Starport) > 0 && self.allUnitCount(UnitType.Terran_Control_Tower) > 0 && self.allUnitCount(UnitType.Terran_Armory) > 0){
							if(currentTarget.player.allUnitCount(UnitType.Protoss_Carrier) > 3){
								if(self.allUnitCount(UnitType.Terran_Valkyrie) < currentTarget.player.allUnitCount(UnitType.Protoss_Carrier) / 4){
									UQ.add(UnitType.Terran_Valkyrie);
								}
							}
						}
					}
				}
				
				if(self.allUnitCount(UnitType.Terran_Starport) > 0 && self.allUnitCount(UnitType.Terran_Control_Tower) > 0){
					UQ.add(UnitType.Terran_Wraith);
				}
				
				if(self.allUnitCount(UnitType.Terran_Factory) > 0 && self.allUnitCount(UnitType.Terran_Armory) > 0){
					UQ.add(UnitType.Terran_Goliath);
				}

			}
			else {
				// if we can't win as protoss.
				
				if(self.minerals() * 10 >= self.gas()){
					int VMax = Math.round(self.minerals() / 100);
					
					if(VMax >= 8){
						VMax = 8;
					}
					
					for(int i = 0; i < VMax; i++){
					UQ.add(UnitType.Protoss_Zealot);
					}
				}
				
				
				if(self.allUnitCount(UnitType.Protoss_Zealot) + amountInUQ(UnitType.Protoss_Zealot) < self.allUnitCount(UnitType.Protoss_Dragoon) / 4 ){
					UQ.add(UnitType.Protoss_Zealot);
				}
				else {
					if(self.completedUnitCount(UnitType.Protoss_Cybernetics_Core) > 0){
						for(int i = 0; i < self.completedUnitCount(UnitType.Protoss_Gateway); i++){
							UQ.add(UnitType.Protoss_Dragoon);
						}
						UQ.add(UnitType.Protoss_Zealot);
					}
				}
				
				if(self.minerals() * 7 >= self.gas()){
					for(int i = 0; i < 4; i++){
						UQ.add(UnitType.Protoss_Zealot);
					}
				}
				
				if(self.hasUnitTypeRequirement(UnitType.Protoss_Dark_Templar) && self.allUnitCount(UnitType.Protoss_Dark_Templar) + amountInUQ(UnitType.Protoss_Dark_Templar) < 6){
					UQ.add(UnitType.Protoss_Dark_Templar);
				}
				
				if(self.hasUnitTypeRequirement(UnitType.Protoss_High_Templar) && self.allUnitCount(UnitType.Protoss_High_Templar) + amountInUQ(UnitType.Protoss_High_Templar) < 6){
					UQ.add(UnitType.Protoss_High_Templar);
				}
				
				
				if(self.completedUnitCount(UnitType.Protoss_Stargate) > 0 && self.completedUnitCount(UnitType.Protoss_Fleet_Beacon) > 0 && myBases.size() >= 3){
				UQ.add(UnitType.Protoss_Carrier);
				}
				
			}
		
		// end of else	
		
		}

	
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
					System.out.println("New Squad Assigned to Defend: " + pos);
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
		for(Unit myUnit : new ArrayList<>(myData.myMilUnits)){
	    	 if(myData.isNearEnemyOrBetter(myUnit) && IsMilitrayUnit(myUnit)){
	    		 ArrayList<Unit> mine;
	    		 ArrayList<Unit> enemy;
	    		 ArrayList<Unit> realMine;
	    		 // BITCH LASAGNA
	    		 BWMirrorAgentFactory fac = new BWMirrorAgentFactory();
    			 if(myData.isNearEnemyCP(myUnit.getPosition())){
    	    		mine = util.getCombatUnits(myUnit.getPosition(), 350);
    	    		enemy = myData.GetEnemyUnitsNearby(myUnit.getPosition(), 350, true);
    	    		realMine = null;
    			 }
    			 else {
    				 mine = util.getCombatUnits(myUnit.getPosition(), 500);
    	    		 enemy = myData.GetEnemyUnitsNearby(myUnit.getPosition(), 500, true);
    	    		realMine = util.combatReadyUnits(mine, myUnit.getPosition());
    			 }

	    		 //System.out.println("Mine: " + mine.size());
	    		// System.out.println("Enemy: " + enemy.size());
	    		 ArrayList<Agent> mine2 = new ArrayList<>();
	    		 
    			 for(Unit unit : mine){
    				 mine2.add(fac.of(unit));
    			 }
    			 			
	    		 boolean canWin;
	    		 if(realMine == null){
	    			 for(Unit unit : mine){
	    				 mine2.add(fac.of(unit));
	    			 }
		    		 canWin = manager.evaluateBattle(mine, enemy, 0.85, true);
	    		 }	
	    		 else {
			    	 canWin = manager.evaluateBattle(realMine, enemy, 0.85, true);
	    		 }
	    		 
	    		 if(!canWin){	
	    			//System.out.println("Can Win: " + canWin);
	    			 if(!enemy.isEmpty()){
	    				// System.out.println("Enemies is not empty");
		    			 for(Unit unit : mine){
		    				 if(myUnit.getPosition().getApproxDistance(myData.getDefencePos().toPosition()) > 300){
		    					 // NO RETREAT IN STALINGRAD
		    					 // URAAAAAAAAAAAAAAAAAAAAA
		    					 util.reteatUnit(unit);
		    					 Squad SQ = getSquad(myUnit);
		    					 if(SQ != null){
		    						 SQ.newRetreater(unit, game.getFrameCount() + 100);
		    						// System.out.println("New retreater");
		    					 }
		    					// System.out.println("Move");
		    					 if(unit.isBurrowed() && unit.canUnburrow()){
		    						 unit.unburrow();
		    					 }
		    					 
		    					 if(unit.isSieged()){
		    						 unit.unsiege();
		    					 }
		    					 
	    						 if(!retreaters.contains(unit)){
	    							 retreaters.remove(unit);
	    						 }

		    				 }
		    				 
		    			 }
	    			 }
	    		 }
	    		 else {
	    			 // if we can win
	    			 // check for those whom are retreating
	    			 Position ey = null;
	    			 if(!enemy.isEmpty()){
	    				ey = enemy.get(0).getPosition();
	    			 }
	    			 
	    			 for(Unit unit : mine){
    					 Squad SQ = getSquad(myUnit);
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
	        					 if(unit.getOrderTargetPosition().getApproxDistance(ey) > 500 && unit.getOrder() != Order.EnterTransport && !myData.isInCombat(unit)){
	        						 unit.attack(ey);
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
		
		if(type.equals(UnitType.Zerg_Overlord)){
			// if zerg
			
			if(myData.howManyBeingMorphed(UnitType.Zerg_Overlord) > 0){
				return true;
			}
			
			if(UQ.contains(type)){
				return true;
			}
			
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
		
		return self.minerals() + min >= sMins && self.gas() + gas >= sGas;
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
		if(play.player == t){
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
		 t.equals(UnitType.Protoss_Stargate)){
			return true;
		}
			
	
		return false;
	}
	
	void Expand(){
		// https://www.youtube.com/watch?v=njvA03rMHx4
		// SAVE ANDDDDD SOOOUUUUUUUUND
		// HOOOOOOOOOOOOOOLD YOOOOOOUUUUUUUUUUURRRRRRRR GROOOOOOOOOOOOOOUND
		if(myData.nextExpand != null){
			pBuildings.add(0, new pBuilding(self.getRace().getResourceDepot(), myData.nextExpand, 3, false, true, 1));
		}
		else {
			System.out.println("Next Expand is null, cannot expand.");
		}
	}
	
	ArrayList<UnitType> getNextItems(int a){
		// https://www.youtube.com/watch?v=2dbR2JZmlWo
		
		ArrayList<UnitType>ret = new ArrayList<>();
		
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
			if(p.equals(pp)){
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
		
		
		BotPlayer ply = getStrongestOpponent();
		
		if(ply != null){
			
			
		if(game.getFrameCount() < 9000 && ply.MilUnits.isEmpty()){
			//STOP FUCKUNG BLIND EXPANDING YOU DOPEY CUNT.
			return false;
		}
			
		ArrayList<Unit> defenders = new ArrayList<>(myData.myMilUnits);
		for(Unit unit : self.getUnits()){
			
			if(IsMilitrayBuilding(unit) && defenders.contains(unit)){
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

		return manager.evaluateBattle2(defenders, types, ply.player);
		
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
		Position pos = bass.Base.getCenter();
		for(BotBase b : myBases){
			if(b != bass && b.Pawns.size() >= b.maxWorkers){
				int asd = 0;
				int overflow = b.Pawns.size() - b.maxWorkers;
				for(Unit workers : new ArrayList<Unit>(b.Pawns)){
					if(getBase(workers) != null){
						if(getBase(workers).equals(bass)){
							continue;
						}
					}
					BotBase bb = getBase(workers);
					bb.pawnDeath(workers);
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
				for(Unit unit : new ArrayList<Unit>(b.Pawns)){
					if(getBase(unit) != null){
						if(getBase(unit).equals(bass)){
							continue;
						}
					}
					BotBase bb = getBase(unit);
					bb.pawnDeath(unit);
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
		for(pBuilding p : pBuildings){
			UnitType type = p.type;
			if(self.minerals() * 6 >= type.mineralPrice() && self.gas() < type.gasPrice()){
				ArrayList<UnitType> yes = new ArrayList<>();
				for(int i = 0; i<5; i++){
					yes.add(pBuildings.get(i).type);
				}
				
				if(self.getRace().equals(Race.Terran)){
					if(!yes.contains(UnitType.Terran_Barracks)){
						pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
					}
					break;
				}
				else if (self.getRace().equals(Race.Protoss)){
					if(!yes.contains(UnitType.Protoss_Gateway)){
						pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null, 300));
					}
					break;
				}
				else {
					if(!yes.contains(UnitType.Zerg_Hatchery)){
						pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, 300));
					}
					break;
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
	
	
	
}

	
