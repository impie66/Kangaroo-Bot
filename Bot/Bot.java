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
	ArrayList<UnitType> placements = new ArrayList<>();
	ArrayList<Unit> retreaters = new ArrayList<>();
	ArrayList<BotTech> stuffQueue = new ArrayList<>();
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
		
		for(int i = 0; i < 6; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		
		UQ.add(UnitType.Zerg_Overlord);
		
		
		for(int i = 0; i < 6; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		
		UQ.add(UnitType.Zerg_Overlord);
		
		// but you've gotta break a few eggs in order to make an omelette :wink: -- Krais0 4:43 9/05/2019
		
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
		pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation(), 30, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, null));
		pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Queens_Nest, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Spire, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Ultralisk_Cavern, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
		// int typee, int RIDD, TechType techh, UpgradeType up, UnitType morph, Data dataa
		stuffQueue.add(new BotTech(1, 2, TechType.Lurker_Aspect, UpgradeType.None, UnitType.None, myData));
		stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Metabolic_Boost, UnitType.None, myData));
		stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Muscular_Augments, UnitType.None, myData));
		stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Grooved_Spines, UnitType.None, myData));
		stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Melee_Attacks, UnitType.None, myData));
		stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Zerg_Missile_Attacks, UnitType.None, myData));
		stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Carapace, UnitType.None, myData));
		stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Pneumatized_Carapace, UnitType.None, myData));
		stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
		stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
		stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
		stuffQueue.add(new BotTech(3, 5, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
		stuffQueue.add(new BotTech(3, 1, TechType.None, UpgradeType.None, UnitType.Zerg_Lair, myData));
		stuffQueue.add(new BotTech(3, 1, TechType.None, UpgradeType.None, UnitType.Zerg_Hive, myData));
    }

    @Override	
    public void onFrame() {
    	StringBuilder cqs = new StringBuilder("Construction Queue:\n");
    	StringBuilder cqss = new StringBuilder("Unit Queue:\n");
    	StringBuilder cqsss = new StringBuilder("Bot Stuff:\n");
    	game.drawTextScreen(150, 10, "JumpyPurpleWaveZ Processing " + game.getAPM() + " Terra-flops of data a second");
    	game.drawTextScreen(150, 20, "Versing " + game.enemy().getName() + " playing as: " + myData.enemyRace.toString());
    	game.drawTextScreen(150, 30, "Debug:" + " My Units Size: " + myData.myMilUnits.size() + " Enemy Units Size: " + myData.enemyTypes.size()+ " Current State: " + manager.canWin + " Frame Count: " + game.getFrameCount());
    	
       if(UQ.isEmpty() == false){
    	   UnitType next = UQ.get(0);
    	   int max = 0;
    	   if(next.isWorker()){
    			for(BotBase bass : myBases){
    				max = max + bass.maxWorkers;
    			}
    			
    			if(max > 52){
    				max = 52;
    			} 
    			
    			if(self.allUnitCount(self.getRace().getWorker()) >= max){
    				if(UQ.size() == 1){
    					UQ.clear();
    				}
    				else {
    				UQ.remove(0);
    				}
    			}
    			
    	   }
    	   for(Unit unit :  new ArrayList<>(Production)){
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
    	   
       
       
       if(myData.nextAttackPosition != null){
    	   game.drawCircleMap(myData.nextAttackPosition, 30, Color.Red);
    	   game.drawTextMap(myData.nextAttackPosition, "Send Army Here");
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
			   GlobalSimCheck();
			   GlobalState = 0;
		   }
   	   }

   	   if(game.getFrameCount() >= simCheck){
   		GlobalSimCheck();
   		simCheck = game.getFrameCount() + myData.myMilUnits.size();
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
    	  boolean isExpand = next.isExpand;
    	  if(item.equals(self.getRace().getRefinery()) && AllGeysersBuilt()){
    		  pBuildings.remove(0);
    		  //System.out.println("Removed unnesscary refinery queued");
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
    		  if(isExpand){
    			  Position lastExpand = self.getStartLocation().toPosition();
    			  myData.updateNextExpansion(lastExpand);
    			  where = myData.nextExpand;
    		  }
    		  
    		  if(where == null){
    			  where = self.getStartLocation();
    		  }
    		  if(builder != null){
    			  //TilePosition build = getBuildTile(builder, item, where, 300);
    			  TilePosition build = null;
    			  
    			  if(isExpand){
    				  build = where;
    			  }
    			  
    			  if(item.requiresCreep() == true && build == null){
    				// build = game.getBuildLocation(item, where, max, true);
    				  build = getBuildTile(builder, item, where, max);
    			  }
    			  else {
	    			 //build = game.getBuildLocation(item, where, max);
    				if(build == null){
    					build = getBuildTile(builder, item, where, max);
    				 }
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
      
      game.drawTextScreen(0, 10, cqs.toString());
      game.drawTextScreen(0, 130, cqss.toString());
      game.drawTextScreen(500, 75, cqsss.toString());
      
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
		    	 microCheck = game.getFrameCount() + 24;
		    	 //squadDebug();
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
    		  i++;
    		  game.drawTextMap(choke.getCenter().toPosition(), "Chokepoint " + i);
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
    			boolean brek = false;
    			if(tech.ready == true){
    				// if requirements are met
    				
    				if(type == 1 && game.canResearch(TT) && brek == false){	
    					// TechTypes
    					for(Unit myUnit : self.getUnits()){
    						if(myUnit.canResearch(TT) == true && self.minerals() >= TT.mineralPrice() && self.gas() >= TT.gasPrice() && myUnit.isCompleted() && !myUnit.isResearching()){
    							myUnit.research(TT);
    							System.out.println("Researching: " + tech.name);
    							brek = true;
    							stuffQueue.remove(i); 
    							break mainLoop;
    						}
    					}
    					
    				}
    				
    				if(type == 2 && game.canUpgrade(UP) && brek == false){
    					// UpgradeTypes
       					for(Unit myUnit : self.getUnits()){
    						if(myUnit.canUpgrade(UP) == true && self.minerals() >= UP.mineralPrice() && self.gas() >= UP.gasPrice() && myUnit.isCompleted() && !myUnit.isUpgrading()){
    							myUnit.upgrade(UP);
    							System.out.println("Upgrading " + tech.name);
    							brek = true;
    							stuffQueue.remove(i);
    							break mainLoop;
    						}
    					}
    					
    				}
    				if(type == 3 && game.canMake(tech.morphType) && brek == false){ 
    					// UnitTypes
    					// mostly zerg
    				 	 for(Unit myUnit : self.getUnits()){
    				 		 UnitType nextMorph = tech.morphType;
    					    	 if(myUnit.canMorph(nextMorph) && self.minerals() >= nextMorph.mineralPrice() && self.gas() >= nextMorph.gasPrice() && myUnit.isCompleted() && !myUnit.isMorphing()){
    					    		 myUnit.morph(nextMorph);
    					    		 brek = true;
    					    		 System.out.println("Morphing: Unit: " + myUnit.getType().toString() + " To " +  tech.name);
    					    		 stuffQueue.remove(i);
    					    		 break mainLoop;
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
		 	 

     
     if(self.supplyUsed() == self.supplyTotal() && supplyQueued(self.getRace().getSupplyProvider()) == false && self.supplyUsed() != 200 && game.getFrameCount() >= supplyCheck){
    	 supplyCheck = game.getFrameCount() + 30;
    	if(self.getRace().equals(Race.Zerg)){
    		UQ.add(0, UnitType.Zerg_Overlord);
    	}
    	else {
    		pBuildings.add(0, new pBuilding(self.getRace().getSupplyProvider(), null));
    	}
     }
     
     if(self.supplyUsed() > self.supplyTotal() && supplyQueued(self.getRace().getSupplyProvider()) == false && self.supplyUsed() != 200 && game.getFrameCount() >= supplyCheck){
    	 supplyCheck = game.getFrameCount() + 30;
    	if(self.getRace().equals(Race.Zerg)){
    		UQ.add(0, UnitType.Zerg_Overlord);
    	}
    	else {
    		pBuildings.add(0, new pBuilding(self.getRace().getSupplyProvider(), null));
    	}
     }
     
     
     for(Unit myUnit : self.getUnits()){
    	 
    	 if(self.allUnitCount(UnitType.Zerg_Spore_Colony) > 0 && myUnit.getType().equals(UnitType.Zerg_Overlord) && !detAssignedASquad(myUnit)){
    		 Unit spore = util.getUnit(UnitType.Zerg_Spore_Colony);
    		 if(spore != null){
    			 myUnit.move(spore.getPosition());
    		 }
    	 }	
    	 
	 	  	 
    	
    	 if(scouter != null){
    	 game.drawCircleMap(scouter.getPosition(), 60, Color.Yellow);
    	 }
    	 
    	 if(isVoidedWorker(myUnit)){
    		 if(myUnit.getType() != self.getRace().getWorker()){
    			 BotBase base = getBase(myUnit);
	    			if(base != null){
  	    				base.unVoidWorker(myUnit);
  	    				System.out.println("unVoided worker " + myUnit.getID());
  	    			}
    		 }
    		 
    		 if(!isABuilder(myUnit) && myUnit != scouter){
    			 BotBase base = getBase(myUnit);
	    			if(base != null){
  	    				base.unVoidWorker(myUnit);
  	    				System.out.println("unVoided worker " + myUnit.getID());
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
	    		 }
    		 }
    		 
    		 if(myUnit.isAttacking() && myUnit.getType().equals(UnitType.Zerg_Hydralisk)){
    			 Unit target = myUnit.getOrderTarget();
					Position pos = util.GetKitePos2(myUnit, target);
					if(pos != null){
						game.drawLineMap(myUnit.getPosition(), pos, Color.White);
					}
					Position pos2 = util.GetPushPos(myUnit, target);
					if(pos != null){
						game.drawLineMap(myUnit.getPosition(), pos2, Color.Yellow);
					}
    		 }
    		 
    	 }
    	 
  	 
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
    	 	 
    	 if(myData.nextAttackPosition != null){
    		if(scouter != null && scouter.isMoving() == false && !scouter.getType().isBuilding() && scouter.getDistance(myData.nextAttackPosition) > 100){
    			scouter.move(myData.nextAttackPosition);
    		}
    		 
    	 }

    	   	 
    	 if(myUnit.getType().isWorker() && myUnit.isUnderAttack() == true && defenceCheck < game.getFrameCount()){
    		 defenceCheck = game.getFrameCount() + 24;
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
    	 
    	 if(scouter!=null && myUnit.equals(scouter) && myUnit.exists()){
    		 BotBase bass = getBase(myUnit);
    		 if(!bass.voidedWorkers.contains(myUnit)){
    			 bass.newVoidedWorker(myUnit);
    		 }
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
    			if(pBuildings.size() > 3){
    			pBuildings.add(2, new pBuilding(self.getRace().getRefinery(), null));
    			}
    			else {
    				pBuildings.add(new pBuilding(self.getRace().getRefinery(), null));
    			}
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
		
		if(unit.getType().isDetector() && unit.getPlayer().equals(self)){
			Squad sq = getSquad(unit);
			if(sq!=null){
				AssignDetector(sq);
			}
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
		
		if(unit.getType().isMineralField()){
			Base bass = getClosestBaseLocation(unit.getPosition());
			if(bass != null){
				
			}
		}
		
	}
	
    @Override
    public void onUnitCreate(Unit unit) {
    	
    	
		if(IsMilitrayUnit(unit)){
			myData.newMilUnit(unit);
		}
    	  	
    	if(unit.getPlayer().equals(self)){
    		UnitType type = unit.getType();
    		// if e
    		if(unit.getType().isResourceDepot()){
    			Base bass = getClosestBaseLocation(unit.getPosition());
    			if(!alreadyBasedHere(bass)){
    			myBases.add(new BotBase(game, unit, bass));
    			Production.add(unit);
    			myData.updateNextExpansion(unit.getPosition());
    			}
    		}
    		  		
    		if(pBuildings.get(0).type.equals(type) && placements.contains(type)){
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
    			

    			if(!myData.enemyBuildings.contains(unit)){
    				myData.newEnemyBuilding(unit);
    			}
    			
    			if(IsMilitrayBuilding(unit)){
        			if(!myData.enemyDBuildings.contains(unit)){
        				myData.newDBuilding(unit);
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
		UnitType type = unit.getType();
		
		if(IsMilitrayUnit(unit)){
			myData.newMilUnit(unit);
		}
		
		
		if(type.isRefinery() && unit.getPlayer().equals(self)){
			if(pBuildings.get(0).type.equals(type)){
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
    		
    		if(pBuildings.get(0).type.equals(type) && placements.contains(type)){
    			pBuilding p = getPBuilding(type);
    			placements.remove(unit.getType());
    			if(p != null){
    				pBuildings.remove(p);
    			}
    			else {
    				pBuildings.remove(0);
    			}

    		}
    		
    		
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
		Squads.add(new Squad(units, Squads.size() + 1, myData, game, manager, globalRetreat, util));
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
			if(myData.nextAttackPosition != null){
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
		
		if(max > 52){
			max = 52;
		}
		
		boolean needWorkers = self.allUnitCount(self.getRace().getWorker()) + amountInUQ(self.getRace().getWorker()) < max;
	
		if(manager.canWin == true){
			//System.out.println("Drones: " + self.allUnitCount(UnitType.Zerg_Drone) + " Max: " + max);
			if(needWorkers == true){
				UQ.add(self.getRace().getWorker());
			}
			// else if we are maxed with drones, just make units
			else {
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
					for(int i = 0; i < 4; i++){
						UQ.add(UnitType.Zerg_Hydralisk);
					}
				}
				

			}

		}
		else {
			// If we are currently behind in military strength
				if(self.completedUnitCount(UnitType.Zerg_Ultralisk_Cavern) > 0 && self.allUnitCount(UnitType.Zerg_Ultralisk) <= 5){
					UQ.add(UnitType.Zerg_Ultralisk);
					// ALWAYS BUILD ULTRAS CAUSE WHY NOT
				}
				
				if(game.canMake(UnitType.Zerg_Lurker) && self.allUnitCount(UnitType.Zerg_Lurker) <= 4 && !asdf(UnitType.Zerg_Lurker)){
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Lurker, myData));
				}
				
				if(myData.enemyRace.equals(Race.Protoss)){
					
					// Hydra Spam
					if(self.completedUnitCount(UnitType.Zerg_Hydralisk_Den) == 1){
						for(int i = 0; i < 8; i++){
							UQ.add(UnitType.Zerg_Hydralisk);
						}
						
						if(needWorkers == true){
							UQ.add(self.getRace().getWorker());
						}
					}
					else {
						int amount = Math.round(4 + myData.enemyScore - myData.myScore / 10);
						if(self.allUnitCount(UnitType.Zerg_Zergling) < amount){
							for(int i = 0; i < 2; i++){
								UQ.add(UnitType.Zerg_Zergling);
							}
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
					
					if(needWorkers == true){
						UQ.add(self.getRace().getWorker());
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
					
					if(needWorkers == true){
						UQ.add(self.getRace().getWorker());
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
		int cscore = 0;
		boolean breaking = false;
		
		// if our total score isnt big enough to defend, we'll send all there
		if(tscore > manager.myScore){
			for(Squad squad : Squads){
				squad.newTarget(pos);
				squad.operate();
				squad.setState(2);
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
	    			//System.out.println("Can Win: " + canWin);
	    			 if(!enemy.isEmpty()){
	    				// System.out.println("Enemies is not empty");
		    			 for(Unit unit : mine){
	        				 Position retreat = self.getStartLocation().toPosition();
		    				 if(myUnit.getPosition().getApproxDistance(self.getStartLocation().toPosition()) > 2000){
		    					 // NO RETREAT IN STALINGRAD
		    					 // URAAAAAAAAAAAAAAAAAAAAA
		    					 Squad SQ = getSquad(myUnit);
		    					 if(SQ != null){
		    						 SQ.newRetreater(unit, game.getFrameCount() + 100);
		    						// System.out.println("New retreater");
		    					 }
		    					 unit.move(retreat); 
		    					// System.out.println("Move");
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
			 // if not
			if(type.supplyProvided() != 0){
				for(pBuilding p : pBuildings){
					if(p.type.equals(type)){
						return true;
					}
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
	
	
	
		
	
}

	
