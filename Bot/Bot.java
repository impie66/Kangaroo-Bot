package Bot;
import java.util.ArrayList;
import java.util.HashMap;
import Bot.*;
import bwapi.BWClient;
import bwapi.BWEventListener;
import bwapi.Color;
import bwapi.Flag;
import bwapi.Game;
import bwapi.Order;
import bwapi.Player;
import bwapi.Position;
import bwapi.Race;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwem.BWEM;
import bwem.Base;
import bwem.ChokePoint;
import bwem.unit.Geyser;
import org.bk.ass.*;
import org.bk.ass.Simulator.Behavior;
import org.bk.ass.Simulator.RoleBasedBehavior;
import org.bk.ass.collection.UnorderedCollection;

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
	ArrayList<Base> scouts = new ArrayList<>();
	ArrayList<UnitType> placements = new ArrayList<>();
	ArrayList<Unit> retreaters = new ArrayList<>();
	ArrayList<UnitType> morphQueue = new ArrayList<>();
	ArrayList<UpgradeType> upgradeQueue = new ArrayList<>();
	ArrayList<TechType> techQueue = new ArrayList<>();
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
	static BWEM bewb;
	
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
		util = new Util(game, myData);
		startLocations = myData.startLocations;
	    Expands = myData.getExpands();
	    myChokes = myData.myChokes;
	    manager = new DecisionManager(game, myData);
		globalRetreat = self.getStartLocation().toPosition();
		game.enableFlag(Flag.UserInput);
		for(int i = 0; i < 8; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		
		for(int i = 0; i < 8; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		
		for(int i = 0; i < 4; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		
		
		for(int i = 0; i < 4; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		for(int i = 0; i < 8; i++){
			UQ.add(UnitType.Zerg_Zergling);
		}
		
		
		for(int i = 0; i < 7; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(0).getLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, null, 300));
		pBuildings.add(new pBuilding(UnitType.Zerg_Extractor, null));
		pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 6, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 12, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 18, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, null));
		pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(1).getLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Queens_Nest, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(2).getLocation() ) );
		pBuildings.add(new pBuilding(UnitType.Zerg_Ultralisk_Cavern, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(3).getLocation()) );
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(4).getLocation()) );
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(5).getLocation()) );
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(6).getLocation()) );
		upgradeQueue.add(UpgradeType.Metabolic_Boost);
		upgradeQueue.add(UpgradeType.Muscular_Augments);
		upgradeQueue.add(UpgradeType.Grooved_Spines);
		upgradeQueue.add(UpgradeType.Zerg_Melee_Attacks);
		upgradeQueue.add(UpgradeType.Zerg_Missile_Attacks);
		upgradeQueue.add(UpgradeType.Zerg_Carapace);
		upgradeQueue.add(UpgradeType.Pneumatized_Carapace);
		morphQueue.add(UnitType.Zerg_Sunken_Colony);
		morphQueue.add(UnitType.Zerg_Sunken_Colony);
		morphQueue.add(UnitType.Zerg_Sunken_Colony);
		morphQueue.add(UnitType.Zerg_Lair);
		morphQueue.add(UnitType.Zerg_Hive);
		techQueue.add(TechType.Lurker_Aspect);
    }

    @Override	
    public void onFrame() {
    	StringBuilder cqs = new StringBuilder("Construction Queue:\n");
    	StringBuilder cqss = new StringBuilder("Unit Queue:\n");
    	StringBuilder sqs = new StringBuilder("Squads:\n");
    	game.drawTextScreen(150, 10, "LeapingDingoAI (fortunally) lagging the game with " + game.getAPM() + " APM.");
    	game.drawTextScreen(150, 30, "Versing " + game.enemy().getName() + " playing as: " + myData.enemyRace.toString());
    	
       if(UQ.isEmpty() == false){
    	   UnitType next = UQ.get(0);
    	   for(Unit unit : Production){
    		   if(unit.getType().equals(UnitType.Zerg_Hatchery) || unit.getType().equals(UnitType.Zerg_Lair) ){
	    		   if(unit.canTrain(next)){
	    			   unit.train(next);
	    			   if(!UQ.isEmpty()){
	    			   UQ.remove(0);
	    			   }
	    		   }
    		   }
    		   else {
	    		   if(unit.canTrain(next) && unit.isIdle() == true){
	    			   unit.train(next);
	    			   if(!UQ.isEmpty()){
	    			   UQ.remove(0);
	    			   }
	    		   }
    		   }
    	   }
       }
       else {
    	   updateUQ();
       }
       
   	   if(game.getFrameCount() >= winCheck){
   		   winCheck = game.getFrameCount() + 250;
   		   ArrayList<UnitType> enemies = new ArrayList<>();
   		   
   		   for(UnitType unit : myData.enemyTypes){
   			   enemies.add(unit);
   		   }
   		   for(UnitType type : myData.enemyDTypes){
   			   enemies.add(type);
   		   }
   		  //manager.globalEvaluate(myData.myMilUnits, enemies);
   		  manager.simBattle(myData.myMilUnits, enemies);
   		   System.out.println("Can Win Global: " + manager.canWin);
		   if(manager.canWin == true){
		    	allSquadsAttack();
		    	GlobalState = 1;
		   }
		   else {
			   allSquadsRetreat();
			   GlobalState = 0;
		   }
   	   }

    
   	   if(game.getFrameCount() >= simCheck){
   		GlobalSimCheck();
   		simCheck = simCheck + 100;
   	   }
       
       myData.onFrame();
       
      if(myBases.isEmpty() == false){
	       for(BotBase bass : myBases){
    	   int workers = bass.getWorkers();
	    	   int x = bass.depot.getX();
	    	   int y = bass.depot.getY();
	    	   game.drawTextMap(x, y, "Workers: " + workers + " Max Workers: " + bass.maxWorkers);
	       }
       }
      
      if(!pBuildings.isEmpty()){
    	  // build the priority shit first
    	  pBuilding next = pBuildings.get(0);
    	  UnitType item = next.getType();
    	  int max = next.getMaxRange();
    	  TilePosition where = next.getTilePosition();
    	  boolean creep = next.waitForCreep;
    	  boolean cont = true;
    	  if(item.equals(self.getRace().getRefinery()) && AllGeysersBuilt()){
    		  pBuildings.remove(0);
    		  System.out.println("Removed unnesscary refinery queued");
    	  }
    	  if(creep == true){
    		  if(!game.hasCreep(where)){
    			 cont = false;
    		  }
    		  else {
    			  cont = true;
    		  }
    	  }
    	  if(game.canMake(item) && !isInQueue(item) && cont == true){
    		  Unit builder = getWorker();
    		  if(where == null){
    			  where = self.getStartLocation();
    		  }
    		  if(builder != null){
    			  //TilePosition build = getBuildTile(builder, item, where, 300);
    			  TilePosition build; 
    			  if(item.requiresCreep() == true){
    				  if(item.equals(UnitType.Zerg_Creep_Colony)){
    					  build = game.getBuildLocation(item, where, max, true);
    				  } 
    				// build = game.getBuildLocation(item, where, max, true);
    				  build = getBuildTile(builder, item, where, max);
    			  }
    			  else {
	    			 //build = game.getBuildLocation(item, where, max);
	    			 build = getBuildTile(builder, item, where, max);
    			  }
    			  if(build != null){
	    			builder.build(item, build);
	    			builders.add(new Builder(builder, item, build));
	    			placements.add(item);
	    			BotBase base = getBase(builder);
	    			if(base != null){
	    				base.newVoidedWorker(builder);
	    			}
	    	    	//System.out.println("Starting pBuilding: " + item.toString());
	    	    	sMins = item.mineralPrice();
	    	    	sGas = item.gasPrice();
    			  }
    		  }
    	  }
    	  
      }
      else {
  
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
      
      game.drawTextScreen(0, 10, cqs.toString());
      game.drawTextScreen(0, 130, cqss.toString());
      
      if(baseCheck <= game.getFrameCount() || baseCheck == 0){
    	  baseCheck = game.getFrameCount() + 50;
	      for(BotBase bass : myBases){
	    	  bass.doThings();
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
		    	if(game.getFrameCount() >= microCheck){
		    	 sq.squadMicro();
		    	 microCheck = game.getFrameCount() + 100;
		    	 //squadDebug();
		    	}
    	  }
      }
      
      game.drawTextScreen(10, 500, sqs.toString());
              
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
    		  i++;
    		  game.drawTextMap(choke.getCenter().toPosition(), "Chokepoint " + i);
    	  }
      }
      
      
     if(game.getFrameCount() >= improveCheck){
    	 
    	 improveCheck = game.getFrameCount() + 40;
    	 
	 	 if(!morphQueue.isEmpty()){
	 	 UnitType nextMorph = morphQueue.get(0);
	 	 int minsCost = nextMorph.mineralPrice();
	 	 int gasCost = nextMorph.gasPrice();
		 	 for(Unit myUnit : self.getUnits()){
		 	 //canMake doesn't count cost
			    	 if(myUnit.canMorph(nextMorph) && self.minerals() >= minsCost && self.gas() >= gasCost && myUnit.isCompleted() && !myUnit.isMorphing()){
			    		 myUnit.morph(nextMorph);
			    		 System.out.println("Morphing: " + nextMorph.toString());
			     		 morphQueue.remove(nextMorph);
			    		 break;
			    	 }
		 	 }
	 	 }
		 	 
	 	 if(!upgradeQueue.isEmpty()){
	 		 UpgradeType next = upgradeQueue.get(0);
	     	 int minsCost = next.mineralPrice();
	     	 int gasCost = next.gasPrice();
	     	 game.drawTextScreen(500, 70, next.toString());
	     	 for(Unit myUnit : self.getUnits()){
		     	 if(myUnit.canUpgrade(next) && self.minerals() >= minsCost && self.gas() >= gasCost && myUnit.isCompleted() && !myUnit.isUpgrading()){
		     		 myUnit.upgrade(next);
		     		 System.out.println("Upgrading: " + next.toString());
		     		 upgradeQueue.remove(next);
		     		 break;
		     	 }
	     	 }
	 	 }
	 	 
	 	 if(!techQueue.isEmpty()){
	 		 TechType next = techQueue.get(0);
	     	 int minsCost = next.mineralPrice();
	     	 int gasCost = next.gasPrice();
	     	 for(Unit myUnit : self.getUnits()){
		     	 if(myUnit.canResearch(next) && self.minerals() >= minsCost && self.gas() >= gasCost && myUnit.isCompleted() && !myUnit.isResearching()){
		     		 myUnit.research(next);
		     		 game.sendText("Researching " + next.toString());
		     		 System.out.println("Researching " + next.toString());
		     		 techQueue.remove(next);
		     		 break;

		     	 }
	     	 }
	 	 }
	 	 
     }
     
     if(!techQueue.isEmpty()){
    	 UnitType nextMorph = morphQueue.get(0);
    	 game.drawTextScreen(500, 40, nextMorph.toString());
     }
     
     if(!upgradeQueue.isEmpty()){
    	 UpgradeType next = upgradeQueue.get(0);
     	 game.drawTextScreen(500, 70, next.toString());
     }
     
     if(!techQueue.isEmpty()){
    	 TechType next = techQueue.get(0);
     	 game.drawTextScreen(500, 100, next.toString());
     }
 	 
     if(self.supplyUsed() >= self.supplyTotal() && myData.howManyBeingMorphed(UnitType.Zerg_Overlord) <= myBases.size() && !UQ.contains(UnitType.Zerg_Overlord) && self.supplyTotal() != 200){
   	  UQ.add(0, UnitType.Zerg_Overlord);
   	  return;
     }
     
     for(Unit myUnit : self.getUnits()){
	 	  	 
    	 if(!scouter.equals(null)){
    	 game.drawCircleMap(scouter.getPosition(), 60, Color.Yellow);
    	 }

    	 
    	 if(myUnit.isSelected() && IsMilitrayUnit(myUnit)){
    		 Squad sq =  getSquad(myUnit);
    		 if(sq==null){
   			 System.out.println("Unit: " + myUnit.getID() + " Squads is null"); 
    		 }
    		 else {
	    		 System.out.println("Unit Squad: " + sq.id);
	    		 System.out.println("Squad State: " + sq.State);
	    		 if(sq.target != null){
	    		 game.drawLineMap(myUnit.getPosition(), sq.target, Color.Purple);
	    		 }
    		 }
    		 
    	 }
    	 
  	 
    	 if(myUnit.getType().isWorker() == true && assignedToBase(myUnit) == false){
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
	  	    				System.out.println("unVoided worker " + myUnit.getID());
	  	    			}
	         			System.out.println("Can't build: " + b.type.toString() + " Trying again."); 
	    		 
	    	 }
	       		 game.drawTextMap(b.where.toPosition(), b.type.toString());
	       		 
    	 }
    	 
    	 if(isBuilder(myUnit)){
    		 game.drawLineMap(myUnit.getPosition(), myUnit.getOrderTargetPosition(), Color.White);
    		 Builder build = getBuilder(myUnit);
    		 TilePosition where = build.where;
    		 UnitType type = build.type;
    		 BotBase base = getBase(myUnit);
    		 
    		 if(!game.isVisible(where) && myUnit.isMoving() == false && myUnit.getPosition().getApproxDistance(where.toPosition()) > 30){
    			 myUnit.move(where.toPosition());
    		 }	
    		 
    		 if(game.isVisible(where)){
    			 if(!myUnit.isConstructing()){
    			 myUnit.build(type, where);
    			 }
   			 
    			 if(!game.canBuildHere(where, type, myUnit) && !myUnit.isConstructing()){
        			builders.remove(build);
 	    			placements.remove(type);
 	    			if(base != null){
 	    				base.unVoidWorker(myUnit);
 	    				System.out.println("unVoided worker " + myUnit.getID());
 	    			}
        			 //System.out.println("Can't build: " + type.toString() + " Trying again.");
    			 }
    			 
    		 }
    		 
    		 
    		 if(!where.isValid(game)){
    			 builders.remove(build);
    			 System.out.println("invalid placement for " + type.toString());
    		 }
    		 
    		 if(base != null){
	    		 if(!base.voidedWorkers.contains(myUnit)){
	    			 base.newVoidedWorker(myUnit);
	    		 }
    		 }
    		 
    	 }
    	 
    	 if(isBuilder(myUnit) && myUnit.getType().isBuilding()){
    		 Builder build = getBuilder(myUnit);
    		 if(build != null){
    		 builders.remove(build);
    		 }
    	 }
    	 
    	 }
    	 	 
    	 if(myData.enemyBuildings.isEmpty()){
    		 // if we havent scouted anything
    		if(!scouter.equals(null) && scouter.isMoving() == false && !scouter.getType().isBuilding()){
   			 scouter.move(self.getStartLocation().toPosition());
	    		 for(Base bass : myData.startLocations){
		    		if(!game.isExplored(bass.getLocation())){
		    			 scouter.move(bass.getCenter(), true);
		    	     }
	    		 }
    		}
    		 
    	 }
    	 else {
    		 // if the enemy is scouted
    		 if(!scouter.equals(null) && !scouter.getType().isBuilding()){
    		 // move around and do shit
	    		if(!myData.enemyBases.isEmpty() && !myUnit.isMoving()){
	    			for(Base bass : myData.enemyBases){
	    			Position one = bass.getArea().getTopLeft().toPosition();
	    			Position two = bass.getArea().getBottomRight().toPosition();
	    			scouter.move(one, true);
	    			scouter.move(two, true);
	    			}
	    		}
	    	 }
    		 //end of basic scouting
    	 }
    	 
    	 
    	 if(myUnit.getType().isWorker() && myUnit.isUnderAttack() == true){
    		 if(scouter == null){
    			 DefenceCall(myUnit.getPosition());
    		 }
    		 if(myUnit!= scouter){
    			 DefenceCall(myUnit.getPosition());
    		 }
    	 }
    	 
    	 if(myUnit.getType().isBuilding() && myUnit.isUnderAttack() == true){
    		 DefenceCall(myUnit.getPosition());
    	 }
     
    	 //end of my units
     }
     
	     for(Unit unit : new ArrayList<Unit>(simmedUnits.keySet())){
	    	 if(simmedUnits.get(unit) <= game.getFrameCount()){
	    		 simmedUnits.remove(unit);
	    	 }
	     }
	     
	     for(Unit unit : game.enemy().getUnits()){
	    	 
	    	 if(unit.isCloaked() || unit.isBurrowed()){
	    		 game.drawTextMap(unit.getPosition(), "" + unit.getType().toString());
	    	 }
	    	 
	    	 // end of enemyUnits
	     }
	     
	     
	     // end on onFrame
     }
          
    

    public static void main(String[] args) {
        new Bot();
    }

	@Override
	public void onEnd(boolean arg0) {
		
	}


	@Override
	public void onUnitComplete(Unit unit) {
		
		if(unit.getPlayer().equals(self)){
			
    		if(IsMilitrayUnit(unit)){
    			assignUnit(unit);
    		}
    			//  myChokes.get(1).getCenter().toTilePosition();
    		
    		if(unit.getType().equals(UnitType.Zerg_Hatchery) && myBases.size() > 1){
    			pBuildings.add(0, new pBuilding(UnitType.Zerg_Extractor, null));
    		}
    		
    		if(unit.getType().isDetector() && !unit.getType().isBuilding()){
    			assignDetecter(unit);
    		}
    				
		}
	}

	@Override
	public void onUnitDestroy(Unit unit) {
		
		if(IsMilitrayUnit(unit)){
			Squad sq = getSquad(unit);
			if(sq!=null){
				sq.unitDeath(unit);
			}
			myData.unitDeath(unit);
		}
		
		if(unit.getType().isResourceDepot() && unit.getPlayer().equals(self)){
			Base bass = getClosestBaseLocation(unit.getPosition());
			BotBase remove = getBaseAt(bass);
			ArrayList<Unit> pawns = remove.Pawns;
			for(Unit unitt : pawns){
				if(workers.containsKey(unitt)){
					workers.remove(unitt);
				}
			}
			remove.EmptyPawns();
			myBases.remove(remove);
			for(Unit unittt : pawns){
				assignWorkerToBase(unittt);
			}
			
			if(pBuildings.contains(self.getRace().getRefinery())){
				pBuildings.remove(self.getRace().getRefinery());
			}
		}
		
		
		if(game.enemies().contains(unit.getPlayer()) && unit.getType().isBuilding()){
			myData.unitDeath(unit);
		}
		
		
		
	}
	
    @Override
    public void onUnitCreate(Unit unit) {
    	
    	if(unit.getPlayer().equals(self)){
    		
    		if(unit.getType().isResourceDepot()){
    			Base bass = getClosestBaseLocation(unit.getPosition());
    			if(!alreadyBasedHere(bass)){
    			myBases.add(new BotBase(game, unit, bass));
    			Production.add(unit);
    			}
    		}
    		  		
    		if(placements.contains(unit.getType())){
    			pBuildings.remove(0);
    			placements.remove(unit.getType());
    		}
    		
    		if(scouter == null && unit.getType().isWorker() && game.getFrameCount() < 300){
    			scouter = unit;
    			BotBase bass = getBase(unit);
    			if(bass!=null){
    				bass.newVoidedWorker(unit);
    			}
    		}
    		
    		if(unit.getType().equals(UnitType.Zerg_Overlord) && !myData.startLocations.isEmpty()){
    			for(Base bass : myData.startLocations){
    				if(!game.isExplored(bass.getLocation())){
    				Position pos = bass.getCenter();
    				unit.move(pos, true);
    				scouts.add(bass);
    				}
    			}
    		}
    				
    		if(unit.getType().isWorker()){
    			assignWorkerToBase(unit);
    		}
    		
    		// end of my units creation
    	}
    	
    	
    	
    }
    

	@Override
	public void onUnitDiscover(Unit unit) {
		
    	if(game.enemies().contains(unit.getPlayer())){
    		if(unit.getType().isBuilding() == true){
    			if(myData.enemyBuildings.isEmpty() && !scouter.equals(null)){
    				scouter.stop();
    			}
    			
    			if(myData.enemyBuildings.isEmpty()){
    			myData.newEnemyBuilding(unit);
    			}
    			else {
    				if(!myData.enemyBuildings.contains(unit)){
    					myData.newEnemyBuilding(unit);
    				}
    				if(IsMilitrayBuilding(unit)){
        				if(!myData.enemyDBuildings.contains(unit)){
        					myData.newDBuilding(unit);
        				}
    				}
    			}  			
    		}
    		
    		if(unit.getType().isResourceDepot()){
    			Base bass = getClosestBaseLocation(unit.getPosition());
    			myData.newEnemyBase(bass);
    		}
    		
    		// end of enemy units
    	}
  	
		if(IsMilitrayUnit(unit)){
			myData.newMilUnit(unit);
		}
		
	}

	@Override
	public void onUnitHide(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitMorph(Unit unit) {
		
		if(unit.getType().isRefinery() && unit.getPlayer().equals(self)){
			if(isPQueued(unit.getType()) == true){
				pBuildings.remove(0);
				placements.remove(unit.getType());
				constructors.remove(unit.getType());
				for(Builder build : new ArrayList<Builder>(builders)){
					if(build.type == unit.getType()){
						builders.remove(build);
					}
				}
			}
			
			Base closest = getClosestBaseLocation(unit.getPosition());
			for(BotBase bass : myBases){
				if(bass.Base.equals(closest)){
					bass.newRefinery(unit);
					System.out.println("New Gas to base: " + myBases.indexOf(bass));
				}
											
			}
				
		}
		
		
		if(unit.getPlayer().equals(self)){
			
    		if(unit.getType().isResourceDepot()){
    			Base bass = getClosestBaseLocation(unit.getPosition());
    			if(!alreadyBasedHere(bass)){
    			myBases.add(new BotBase(game, unit, bass));
    			}
    			Production.add(unit);
    		}
    		
    		if(unit.getType().equals(UnitType.Zerg_Sunken_Colony)){
    			for(Squad sq : Squads){
    				sq.retreatPos = unit.getPosition();
    			}
    			globalRetreat = unit.getPosition();
    		}
    		
		}
		
		if(placements.contains(unit.getType())){
			pBuildings.remove(0);
			placements.remove(unit.getType());
			sMins=sMins-unit.getType().mineralPrice();
			sGas=sGas-unit.getType().gasPrice();
		}
		
		if(assignedToBase(unit)){
			BotBase bass = getBase(unit);
			if(bass != null){
				bass.pawnDeath(unit);
			}
		}
		
	}

	@Override
	public void onUnitRenegade(Unit arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitShow(Unit arg0) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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

    }

	
	void assignWorkerToBase(Unit unit){
		if(!workers.containsKey(unit.getID())){
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
		for(BotBase bass : myBases){
			if(bass.Pawns.contains(worker) == true){
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
				for (Geyser an : bass.Geysers) {
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
						if (!unitsInWay) {
							return new TilePosition(i, j);
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
					}
				}
			}
			maxDist += 2;
		}
		
		return ret;
		
	}
	
	
	void createSquad(ArrayList<Unit> units){
		Squads.add(new Squad(units, Squads.size() + 1, myData, game, manager, globalRetreat));
	}
	
	void assignUnit(Unit unit){
		ArrayList<Unit> list = new ArrayList<>();
		list.add(unit);
		if(Squads.isEmpty() == false){
		int i = 1;
		int max = Squads.size();
		boolean found = false;
		while(found == false && Squads.isEmpty() == false){ 
			int ii = 0;
			for(Squad squad : Squads){
				
				if(squad.isSquadFull()){
					ii++;
				}
				
				int index = Squads.indexOf(squad);
				//System.out.println("Squad: " + index);
				int score = squad.getScore();
				//System.out.println("Squad: " + index + " score: " + score);
				int target = squad.getTargetScore();
				//System.out.println("Squad: " + index + " targetScore: " + target);
				//System.out.println("Squad: " + index + " priority " + squad.priority());
				if(squad.priority() <= i && score <= target && target > 0){
					squad.absorbUnit(unit);
					//System.out.println("Added unit to squad with target score: " + squad.getId());
					found = true;
					break;
				}
				if(squad.priority() <= i && !squad.isSquadFull()){
					squad.absorbUnit(unit);
					System.out.println("Added unit: " + unit.getID() + " to squad: " + squad.id);
					found = true;
					break;
				}
				
				if(ii >= max){
					//System.out.println("All squads goals filled, new squad");
					Squad neww = new Squad(list, Squads.size() + 1, myData, game, manager, globalRetreat);
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
			Squad neww = new Squad(list, Squads.size() + 1, myData, game, manager, globalRetreat);
			Squads.add(neww);
		}

	}
	
	
	Squad getSquad(Unit unit){
		
		for(Squad sq : Squads){
			for(Unit unitt : sq.getUnits()){
				if(unitt.equals(unit)){
					return sq;
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
			if(myData.nextAttackPosition != null ){
				squad.target = myData.nextAttackPosition;
			}
			squad.operate();
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
	}
	
	void updateUQ(){
		
		int max = 0;
		for(BotBase bass : myBases){
			max = max + bass.maxWorkers;
		}
		
		
		
		if(manager.canWin == true){
			System.out.println("Drones: " + self.allUnitCount(UnitType.Zerg_Drone) + " Max: " + max);
			if(self.allUnitCount(UnitType.Zerg_Drone) <= max - 3){
				for(int i = 0; i < 8; i++){
				UQ.add(UnitType.Zerg_Drone);
				}
				
			}
			// else if we are maxed with drones, just make units
			else {
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
				
				if(self.hasResearched(TechType.Lurker_Aspect) && !morphQueue.contains(UnitType.Zerg_Lurker) && self.allUnitCount(UnitType.Zerg_Lurker) <= 5){
					morphQueue.add(UnitType.Zerg_Lurker);
				}
			}

		}
		else {
			// If we are currently behind in military strength
				if(self.completedUnitCount(UnitType.Zerg_Ultralisk_Cavern) > 0){
					UQ.add(UnitType.Zerg_Ultralisk);
					// ALWAYS BUILD ULTRAS CAUSE WHY NOT
				}
				
				if(self.hasResearched(TechType.Lurker_Aspect) && !morphQueue.contains(UnitType.Zerg_Lurker) && self.allUnitCount(UnitType.Zerg_Lurker) <= 5){
					morphQueue.add(UnitType.Zerg_Lurker);
					// NEED ME SOME SPINY BOIS
				}
				
				if(myData.enemyRace.equals(Race.Protoss)){
					// Hydra Spam
				if(self.completedUnitCount(UnitType.Zerg_Hydralisk_Den) == 0){
					for(int i = 0; i < 4; i++){
					UQ.add(UnitType.Zerg_Zergling);
					}
				}
				else{
					for(int i = 0; i < 8; i++){
						UQ.add(UnitType.Zerg_Hydralisk);
					}
					for(int i = 0; i < 6; i++){
						UQ.add(UnitType.Zerg_Zergling);
					}
					
					if(self.allUnitCount(UnitType.Zerg_Drone) <= max - 3){
						UQ.add(UnitType.Zerg_Drone);
						UQ.add(UnitType.Zerg_Drone);
						}
					}
			}
				
			if(myData.enemyRace.equals(Race.Terran)){	
				if(self.completedUnitCount(UnitType.Zerg_Hydralisk_Den) == 0){
					for(int i = 0; i < 10; i++){
					UQ.add(UnitType.Zerg_Zergling);
					}
				}
				else{
					for(int i = 0; i < 3; i++){
						UQ.add(UnitType.Zerg_Hydralisk);
					}
					for(int i = 0; i < 20; i++){
						UQ.add(UnitType.Zerg_Zergling);
					}
					
					if(self.allUnitCount(UnitType.Zerg_Drone) <= max - 3){
						UQ.add(UnitType.Zerg_Drone);
						UQ.add(UnitType.Zerg_Drone);
						}
					}
			}
					
			if(myData.enemyRace.equals(Race.Zerg) || myData.enemyRace.equals(Race.Unknown)){	
				if(self.completedUnitCount(UnitType.Zerg_Hydralisk_Den) == 0){
					for(int i = 0; i < 4; i++){
					UQ.add(UnitType.Zerg_Zergling);
					}
				}
				else{
					for(int i = 0; i < 8; i++){
						UQ.add(UnitType.Zerg_Hydralisk);
					}
					for(int i = 0; i < 6; i++){
						UQ.add(UnitType.Zerg_Zergling);
					}
					
					if(self.allUnitCount(UnitType.Zerg_Drone) <= max - 3){
						UQ.add(UnitType.Zerg_Drone);
						UQ.add(UnitType.Zerg_Drone);
						}
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
	
	
	public ArrayList<Unit> GetEnemyUnitsNearby(Position pos, int radius, boolean include){
		 ArrayList<Unit> Mine = new ArrayList<Unit>();
		for (Unit targets : game.getUnitsInRadius(pos, radius)) {
			int damage = targets.getType().groundWeapon().damageAmount() + targets.getType().airWeapon().damageAmount();
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
		boolean canWin = false;
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
				// 0 = idle, 1 = attacking, 2 is attacking;
			}
			return;
		}
		
		if(GlobalState == 1){
			BuildSquadToCounter(eScore, pos);
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
		int cscore = 0;
		boolean breaking = false;
		
		// if our total score isnt big enough to defend, we'll send all there
		if(tscore > manager.myScore){
			for(Squad squad : Squads){
				squad.newTarget(pos);
				squad.operate();
				squad.setState(2);
			}
			return;

		}
		
		if(breaking != true){
		int newscore = 0;
		ArrayList<Unit> list = new ArrayList<>();
		for(Unit unit : myData.myMilUnits){
			if(!list.contains(unit)){
				list.add(unit);
				newscore = newscore + manager.getScoreOf(unit);
				if(newscore >= tscore){
					Squads.add(new Squad(list, Squads.size(), myData, game, manager));
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
		for(BotBase basss : myBases){
			if(basss.Base.equals(bass)){
				return basss;
			}
		}
		
		return null;
	}
	
	boolean alreadyBasedHere(Base bass){
		
		for(BotBase vase : myBases){
			if(vase.Base.equals(bass)){
				return true;
			}
		}
		
		return false;
	}
	
	
	public void squadDebug() {
		for(Squad sq : Squads){
   		 System.out.println("Squad State: " + sq.State);
   		 System.out.println("Squads Target: " + sq.type);
		 System.out.println("Squad Units: " + sq.getUnitSize());
		}
	}
	

	void GlobalSimCheck(){
		for(Unit myUnit : new ArrayList<>(myData.myMilUnits)){
			ArrayList<Integer> done = new ArrayList<>();
	    	 if(isInCombat(myUnit) && !done.contains(myUnit.getID()) && IsMilitrayUnit(myUnit)){
	    		 // BITCH LASAGNA
	    		// System.out.println("Sim Trigger");
	    		 ArrayList<Unit> mine = myData.GetMyUnitsNearby(myUnit.getPosition(), 500, true);
	    		 ArrayList<Unit> enemy = myData.GetEnemyUnitsNearby(myUnit.getPosition(), 500, true);
	    		 //System.out.println("Mine: " + mine.size());
	    		// System.out.println("Enemy: " + enemy.size());
	    		 boolean canWin = manager.evaluateBattle(mine, enemy);
				 for(Unit unit : mine){
					 if(!done.contains(unit.getID())){
						done.add(myUnit.getID());
					 }
				 }
	    		 if(!canWin){
	    			System.out.println("Can Win: " + canWin);
	    			 if(!enemy.isEmpty()){
	    				 System.out.println("Enemies is not empty");
		    			 for(Unit unit : mine){
	        				 Position retreat = self.getStartLocation().toPosition();
		    				 if(myUnit.getPosition().getApproxDistance(self.getStartLocation().toPosition()) > 2000){
		    					 // NO RETREAT IN STALINGRAD
		    					 // URAAAAAAAAAAAAAAAAAAAAA
		    					 Squad SQ = getSquad(myUnit);
		    					 if(SQ != null){
		    						 SQ.newRetreater(unit, game.getFrameCount() + 200);
		    						 System.out.println("New retreater");
		    					 }
		    					 unit.move(retreat); 
		    					 System.out.println("Move");
		    					 if(unit.isBurrowed() && unit.canUnburrow()){
		    						 unit.unburrow();
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
    					 }
    					 
    					 if(unit.getType().equals(UnitType.Zerg_Lurker) && !unit.isBurrowed() && unit.canBurrow(true)){
    						 unit.burrow();
    					 }
    					 
    					 if(!unit.isAttacking() && ey != null){
    						 unit.attack(ey);
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
					System.out.println("New Detecter for squad: " + sq.id);
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
		
		if(self.allUnitCount(self.getRace().getRefinery()) >= max){
			return true;
		}
		
		return false;
	}
		
	
}

	
