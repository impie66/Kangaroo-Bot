package Bot;
import java.util.ArrayList;
import java.util.HashMap;
import Bot.*;
import bwapi.BWClient;
import bwapi.BWEventListener;
import bwapi.Color;
import bwapi.Flag;
import bwapi.Game;
import bwapi.Player;
import bwapi.Position;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
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
	Base myStartLocation;
	int baseCheck = 0;
	
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
		game.enableFlag(Flag.UserInput);
		
		for(int i = 0; i < 8; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		
		UQ.add(UnitType.Zerg_Overlord);
		
		for(int i = 0; i < 8; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		for(int i = 0; i < 4; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		UQ.add(UnitType.Zerg_Overlord);
		
		for(int i = 0; i < 4; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		for(int i = 0; i < 8; i++){
			UQ.add(UnitType.Zerg_Zergling);
		}
		
		UQ.add(UnitType.Zerg_Overlord);
		
		for(int i = 0; i < 7; i++){
			UQ.add(UnitType.Zerg_Drone);
		}
		
		pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(0).getLocation()));
		pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, null, 300));
		pBuildings.add(new pBuilding(UnitType.Zerg_Extractor, null));
		pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
    }

    @Override	
    public void onFrame() {
    	StringBuilder cqs = new StringBuilder("Construction Queue:\n");
    	StringBuilder cqss = new StringBuilder("Unit Queue:\n");
    	StringBuilder sqs = new StringBuilder("Squads:\n");
    	game.drawTextScreen(250, 10, "Kangaroo Bot (Unfortunally) Running with " + game.getAPM() + " APM.");
       if(UQ.isEmpty() == false){
    	   UnitType next = UQ.get(0);
    	   for(Unit unit : Production){
    		   if(unit.canTrain(next) && unit.isIdle() == true && self.minerals() >= sMins && self.gas() >= sGas){
    			   unit.train(next);
    			   if(!UQ.isEmpty()){
    			   UQ.remove(0);
    			   }
    		   }
    	   }
       }
       else {
    	   updateUQ();
       }
       
   	   if(game.getFrameCount() >= winCheck){
   		   winCheck = game.getFrameCount() + 300;
   		   manager.globalEvaluate(myData.myMilUnits, myData.enemyTypes);
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
    	  boolean yes = next.waitForCreep;
    	  TilePosition where = next.getTilePosition();
    	  if(game.canMake(item) && !isInQueue(item)){
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
	    			constructors.put(builder.getID(), item);
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
    	  if(!sq.getUnits().isEmpty()){
    	  game.drawTextMap(sq.getUnits().get(0).getPosition(), "Squad: " + sq.id + "  ");
	    	  if(game.getFrameCount() >= microCheck){
	    	  sq.squadMicro();
	    	  microCheck = game.getFrameCount() + 100;
	    	  }
    	  }
      }
      
      game.drawTextScreen(10, 500, sqs.toString());
      
      if(UQ.isEmpty() && self.supplyUsed() >= self.supplyTotal()){
    	  UQ.add(0, UnitType.Zerg_Overlord);
      }
      
      if(self.supplyUsed() >= self.supplyTotal() && UQ.get(0)!=UnitType.Zerg_Overlord && myData.howManyBeingMorphed(UnitType.Zerg_Overlord) == 0){
    	  UQ.add(0, UnitType.Zerg_Overlord);
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
      
      
      
     for(Unit myUnit : self.getUnits()){
    	 
    	 if(!scouter.equals(null)){
    	 game.drawCircleMap(scouter.getPosition(), 60, Color.Yellow);
    	 }
    	 
    	 if(isInCombat(myUnit) && !simmedUnits.containsKey(myUnit) && IsMilitrayUnit(myUnit)){
    		 // BITCH LASAGNA
    		// System.out.println("Sim Trigger");
    		 ArrayList<Unit> mine = myData.GetMyUnitsNearby(myUnit.getPosition(), 450, true);
    		 ArrayList<Unit> enemy = myData.GetEnemyUnitsNearby(myUnit.getPosition(), 450, true);
    		 //System.out.println("Mine: " + mine.size());
    		// System.out.println("Enemy: " + enemy.size());
    		 boolean canWin = manager.evaluateBattle(mine, enemy);
			 for(Unit unit : mine){
				 if(!simmedUnits.containsKey(unit)){
					 simmedUnits.put(unit, game.getFrameCount() + 200);
				 }
			 }
    		 if(!canWin){
    			// System.out.println("Can Win: " + canWin);
    			 if(!enemy.isEmpty()){
        			 Position retreat = util.GetKitePos(mine.get(0), enemy.get(0));
	    			 for(Unit unit : mine){
	    				 if(myUnit.getPosition().getApproxDistance(self.getStartLocation().toPosition()) > 2000){
	    					 // NO RETREAT IN STALINGRAD
	    					 unit.move(retreat); 
	    				 }
	    				 
	    			 }
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
	    		 }
	    	 }
    	 }
    	 
    	 if(isBuilder(myUnit)){
    		 game.drawLineMap(myUnit.getPosition(), myUnit.getOrderTargetPosition(), Color.White);
    		 Builder build = getBuilder(myUnit);
    		 TilePosition where = build.where;
    		 UnitType type = build.type;
    		 game.drawTextMap(where.toPosition(), type.toString());
    		 
    		 if(!game.isVisible(where) && myUnit.isMoving() == false){
    			 myUnit.move(where.toPosition());
    		 }	
    		 
    		 if(game.isVisible(where)){
    			 if(!myUnit.isConstructing()){
    			 myUnit.build(type, where);
    			 }
    			 
    			 if(!game.canBuildHere(where, type, myUnit)){
        			constructors.remove(myUnit.getID());
        			builders.remove(build);
 	    			BotBase base = getBase(myUnit);
 	    			if(base != null){
 	    				base.unVoidWorker(myUnit);
 	    				System.out.println("unVoided worker " + myUnit.getID());
 	    			}
        			 //System.out.println("Can't build: " + type.toString() + " Trying again.");
    			 }
    		 }

    	 }
    	 
    	 if(isBuilder(myUnit) && myUnit.getType().isBuilding()){
    		 Builder build = getBuilder(myUnit);
    		 if(build != null){
    		 builders.remove(build);
    		 }
    	 }
    	 
    	 
    	 if(myUnit.getType() == UnitType.Zerg_Creep_Colony && myUnit.canMorph(UnitType.Zerg_Sunken_Colony)){
    		 myUnit.morph(UnitType.Zerg_Sunken_Colony);
    	 }
    	 
    	 if(myData.enemyBuildings.isEmpty()){
    		 // if we havent scouted anything
    		if(!scouter.equals(null) && scouter.isMoving() == false && !scouter.getType().isBuilding()){
   			 scouter.move(self.getStartLocation().toPosition());
    		 for(Base bass : myData.startLocations){
    			 scouter.move(bass.getCenter(), true);
    		 }
    		}
    		 
    	 }
    	 else {
    		 // if the enemy is scouted
    		 if(!scouter.equals(null) && !scouter.getType().isBuilding()){
    		 // move around and do shit
	    		if(!myData.enemyBases.isEmpty() && myUnit.isMoving()){
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
    	 
    	 
    	 if(myUnit.getType().isWorker() || myUnit.getType().isBuilding()){
    		 if(myUnit.isUnderAttack() == true){
    			 DefenceCall(myUnit.getPosition());
    		 }
    	 }
     
    	 //end of my units
     }
     
     for(Unit unit : new ArrayList<Unit>(simmedUnits.keySet())){
    	 if(simmedUnits.get(unit) <= game.getFrameCount()){
    		 simmedUnits.remove(unit);
    	 }
     }
          
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
    				
    		if(self.completedUnitCount(UnitType.Zerg_Hatchery) == 2){
    			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10));
    			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10));
    			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10));
    			pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, null));
    			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(1).getLocation()));
    			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(2).getLocation()));
    			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(3).getLocation()));
    			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(4).getLocation()));
    			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(5).getLocation()));
    			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(6).getLocation()));
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
		
		if(unit.getType().isResourceDepot()){
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
		}
		
		
		
	}
	
    @Override
    public void onUnitCreate(Unit unit) {
    	
    	if(unit.getPlayer().equals(self)){
    		
    		if(unit.getType().isResourceDepot()){
    			Base bass = getClosestBaseLocation(unit.getPosition());
    			myBases.add(new BotBase(game, unit, bass));
    			Production.add(unit);
    		}
    		  		
    		if(constructors.containsValue(unit.getType())){
    			pBuildings.remove(0);
    		}
    		
    		if(scouter == null && unit.getType().isWorker()){
    			scouter = unit;
    			BotBase bass = getBase(unit);
    			if(bass!=null){
    				bass.newVoidedWorker(unit);
    			}
    		}
    		
  		
    		if(unit.getType().equals(UnitType.Zerg_Overlord) && !myData.startLocations.isEmpty()){
    			for(Base bass : myData.startLocations){
    				Position pos = bass.getCenter();
    				unit.move(pos, true);
    				scouts.add(bass);
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
				constructors.remove(unit.getType());
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
    			myBases.add(new BotBase(game, unit, bass));
    			Production.add(unit);
    		}
    		
    		if(unit.getType().equals(UnitType.Zerg_Sunken_Colony)){
    			for(Squad sq : Squads){
    				sq.retreatPos = unit.getPosition();
    			}
    		}
    		
		}
		
		if(isPQueued(unit.getType())){
			pBuildings.remove(0);
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
				if(bass.Pawns.size() <= bass.maxWorkers){
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
		if(constructors.isEmpty() == true){
			return false;
		}
		
		return constructors.containsValue(type);
	}
	
	Unit getWorker(){
		
		for(Unit unit : self.getUnits()){
			if(unit.getType().isWorker()){
				if(unit.isGatheringMinerals() == true && unit.isCompleted() && !isBuilder(unit)){
					return unit;
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
						return n.getTilePosition();
				}
			}
		}
		
		if(buildingType.isResourceDepot()){
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
		Squads.add(new Squad(units, Squads.size() + 1, myData, game));
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
				}
				if(squad.priority() <= i && !squad.isSquadFull()){
					squad.absorbUnit(unit);
					//System.out.println("Added unit to squad: " + squad.getId());
					found = true;
				}
				
				if(ii >= max){
					//System.out.println("All squads goals filled, new squad");
					Squad neww = new Squad(list, Squads.size() + 1, myData, game);
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
			Squad neww = new Squad(list, Squads.size() + 1, myData, game);
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
				if(squad.getTarget().getDistance(pos) < 800){
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
			sq.setState(0);
			sq.retreat();
		}
	}
	
	void updateUQ(){
		
		int max = 0;
		for(BotBase bass : myBases){
			max = max + bass.maxWorkers;
		}
				
		if(manager.canWin == true){
			System.out.println("Drones: " + self.allUnitCount(UnitType.Zerg_Drone) + " Max: " + max);
			if(self.allUnitCount(UnitType.Zerg_Drone) <= max){
				for(int i = 0; i < 4; i++){
				UQ.add(UnitType.Zerg_Drone);
				}
			}

		}
		else {
			// else if we can't win
				if(!game.canMake(UnitType.Zerg_Hydralisk)){
					for(int i = 0; i < 4; i++){
					UQ.add(UnitType.Zerg_Zergling);
					}
				}
				else{
					for(int i = 0; i < 4; i++){
						UQ.add(UnitType.Zerg_Hydralisk);
					}
					
					for(int i = 0; i < 12; i++){
						UQ.add(UnitType.Zerg_Zergling);
					}
	
				}
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
		
		if(GlobalState == 1){
			BuildSquadToCounter(eScore, pos);
		}
		else {
			for(Squad squad : Squads){
				squad.target = pos;
				squad.operate();
				squad.setState(2);
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
					Squads.add(new Squad(list, Squads.size(), myData, game));
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
		if(unit.isAttacking() || unit.isUnderAttack() || unit.isStartingAttack()){
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
	
}

	
