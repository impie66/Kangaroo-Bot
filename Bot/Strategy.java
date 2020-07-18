package Bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import bwapi.Game;
import bwapi.Player;
import bwapi.Race;
import bwapi.TechType;
import bwapi.TilePosition;
import bwapi.Unit;
import bwapi.UnitType;
import bwapi.UpgradeType;
import bwem.Base;
import bwem.ChokePoint;

public class Strategy {
	Race enemy;
	ArrayList<BotTech> stuffQueue;
	ArrayList<pBuilding> pBuildings;
	ArrayList<Base> Expands;
	ArrayList<ChokePoint> myChokes;
	Data myData;
	Player self;
	Game game;
	String buildName;
	TilePosition DP;
	int armySpending; // How much do we really want to spend on units? 1-5; Will remove bonus units based on this if(Make4Marines){Make3MarinesIfThisIs1}
	int Aggression; // How aggression are we 0-30
	ArrayList<UnitType> mainGoal; // What unit do we want to end up using. Will make amount based on what can produce it every update of UQ.
	String type = "None";
	TilePosition mainChoke;
	ArrayList<UnitType> techGoals;
	ArrayList<UnitType> AB; // Aggression buildings. What it may queue to build if the enemy is being highly aggressive. 
	String enemyName;
	int GFR; // How many frames till we start REALLY making our goals?
	boolean moveRegroup = false;
	boolean canGatherGasEarly = false;
	
	Strategy(Race ee, ArrayList<Base> ep, Data myDataa, Game gamee, ArrayList<ChokePoint> cocks){
		this.enemy = ee;
		this.stuffQueue = new ArrayList<BotTech>();
		this.pBuildings = new ArrayList<pBuilding>();
		this.myChokes = cocks;
		this.Expands = ep;
		this.myData = myDataa;
		this.game = gamee;
		this.self = game.self();
		this.buildName = "Jaedongs medic BBS build";
		this.DP = myData.getDefencePos();
		this.Aggression = 0;
		this.armySpending = 0;
		this.mainChoke = myData.mainChoke.getCenter().toTilePosition();
		this.techGoals = new ArrayList<>();
		this.mainGoal = new ArrayList<>();
		this.AB = new ArrayList<>();
		this.GFR = 8000;
		this.moveRegroup = false;
		this.canGatherGasEarly = false;
		Opener();
	}

	private void Opener() {
		// TODO Auto-generated keyboard smash
		// feelsGoodMan Cancer clap	
		if(self.getRace().equals(Race.Zerg)){
			this.AB.add(UnitType.Zerg_Hydralisk_Den);
			//if z
			int chosenBuild = 4;
			Random rand = new Random();
			int n = rand.nextInt(4) + 1;
			if(n == 4 || chosenBuild == 4){
				this.buildName = "Meme Proxy Sunken Build"; // NAME IS IMPORTANT. DON'T CHANGE
				pBuilding one = new pBuilding(UnitType.Zerg_Hatchery, null, Race.Terran);
				one.maxRange = 200;
				one.proxy = true;
				one.save = 1;
				one.waitForCreep = false;
				one.buildWithScout = true;
				pBuildings.add(one);
				pBuilding two = new pBuilding(UnitType.Zerg_Hatchery, null, Race.Protoss);
				two.proxy = true;
				two.maxRange = 200;
				two.save = 1;
				two.waitForCreep = false;
				pBuildings.add(two);
				two.buildWithScout = true;
				pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, self.getStartLocation()) );
				pBuilding b = new pBuilding(UnitType.Zerg_Creep_Colony, null, Race.Zerg);
				b.proxy = true;
				b.save = 1;
				b.buildWithScout = true;
				pBuildings.add(b);
				for(int i = 0; i<=6;i++){
					pBuilding yes = new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 50, true, false, 1);
					yes.proxy = true;
					pBuildings.add(yes);
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData, true));
				}
				pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null));
				pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Zerg_Spire, self.getStartLocation()));
			}
			else {
				if(this.enemy.equals(Race.Zerg)){
					this.buildName = "ZvZ Generic";
					this.mainGoal.add(UnitType.Zerg_Mutalisk);
					this.techGoals.add(UnitType.Zerg_Spire);
					this.canGatherGasEarly = true;
					pBuilding neww = new pBuilding(UnitType.Zerg_Hatchery, Expands.get(0).getLocation(), true);
					neww.canBeCancelled = false;
					neww.save = 0;
					pBuildings.add(neww);
					pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, self.getStartLocation(), 300));
					pBuildings.add(new pBuilding(UnitType.Zerg_Extractor, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 15, true, false, 1));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 15, true, false, 1));
					pBuildings.add(new pBuilding(UnitType.Zerg_Spire, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation(), 20));
					pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Queens_Nest, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Ultralisk_Cavern, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Defiler_Mound, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null));
					// int typee, int RIDD, TechType techh, UpgradeType up, UnitType morph, Data data
					stuffQueue.add(new BotTech(1, 1, TechType.Lurker_Aspect, UpgradeType.None, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Metabolic_Boost, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Flyer_Attacks, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Flyer_Carapace, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Muscular_Augments, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Grooved_Spines, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 10, TechType.None, UpgradeType.Zerg_Melee_Attacks, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 11, TechType.None, UpgradeType.Zerg_Missile_Attacks, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Carapace, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Pneumatized_Carapace, UnitType.None, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Lair, myData, true));
					stuffQueue.add(new BotTech(3, 1, TechType.None, UpgradeType.None, UnitType.Zerg_Hive, myData));
					stuffQueue.add(new BotTech(1, 1, TechType.Ensnare, UpgradeType.None, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Adrenal_Glands, UnitType.None, myData));
					stuffQueue.add(new BotTech(1, 2, TechType.Consume, UpgradeType.None, UnitType.None, myData));
					stuffQueue.add(new BotTech(1, 2, TechType.Plague, UpgradeType.None, UnitType.None, myData));
				}
				else if (this.enemy.equals(Race.Terran)){
					this.buildName = "ZvT Generic";
					this.techGoals.add(UnitType.Zerg_Spire);
					this.mainGoal.add(UnitType.Zerg_Mutalisk);
					this.canGatherGasEarly = true;
					pBuilding neww = new pBuilding(UnitType.Zerg_Hatchery, Expands.get(0).getLocation(), true);
					neww.canBeCancelled = false;
					neww.save = 0;
					pBuildings.add(neww);
					pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, self.getStartLocation(), 300));
					pBuildings.add(new pBuilding(UnitType.Zerg_Extractor, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 15, true, false, 1));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 15, true, false, 1));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 15, true, false, 1));
					pBuildings.add(new pBuilding(UnitType.Zerg_Spire, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation(), 20));
					pBuildings.add(new pBuilding(UnitType.Zerg_Queens_Nest, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Ultralisk_Cavern, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Defiler_Mound, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					stuffQueue.add(new BotTech(1, 8, TechType.Lurker_Aspect, UpgradeType.None, UnitType.None, myData, true));
					stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Metabolic_Boost, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Muscular_Augments, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Grooved_Spines, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 10, TechType.None, UpgradeType.Zerg_Melee_Attacks, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 11, TechType.None, UpgradeType.Zerg_Missile_Attacks, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Carapace, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Pneumatized_Carapace, UnitType.None, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Lair, myData, true));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Hive, myData, true));
					stuffQueue.add(new BotTech(1, 1, TechType.Ensnare, UpgradeType.None, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Adrenal_Glands, UnitType.None, myData));
					stuffQueue.add(new BotTech(1, 2, TechType.Consume, UpgradeType.None, UnitType.None, myData));
					stuffQueue.add(new BotTech(1, 2, TechType.Plague, UpgradeType.None, UnitType.None, myData));
				}
				else if (this.enemy.equals(Race.Protoss)) {
					// if enemy is P
					this.buildName = "ZvP generic";
					this.techGoals.add(UnitType.None);
					this.mainGoal.add(UnitType.Zerg_Hydralisk);
					this.canGatherGasEarly = true;
					pBuilding neww = new pBuilding(UnitType.Zerg_Hatchery, Expands.get(0).getLocation(), true);
					neww.canBeCancelled = false;
					neww.save = 0;
					pBuildings.add(neww);
					pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, self.getStartLocation(), 50));
					pBuildings.add(new pBuilding(UnitType.Zerg_Extractor, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 15, true, false, 1));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 15, true, false, 1));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 15, true, false, 1));
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
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
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
					// int typee, int RIDD, TechType techh, UpgradeType up, UnitType morph, Data dataa
				}
				else {
					// if unknown
					this.buildName = "ZvU Generic";
					this.mainGoal.add(UnitType.Zerg_Mutalisk);
					this.techGoals.add(UnitType.Zerg_Spire);
					this.canGatherGasEarly = true;
					pBuilding neww = new pBuilding(UnitType.Zerg_Hatchery, Expands.get(0).getLocation(), true);
					neww.canBeCancelled = false;
					neww.save = 0;
					pBuildings.add(neww);
					pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, self.getStartLocation(), 300));
					pBuildings.add(new pBuilding(UnitType.Zerg_Extractor, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 15, true, false, 1));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 15, true, false, 1));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, self.getStartLocation(), Race.Protoss));
					pBuildings.add(new pBuilding(UnitType.Zerg_Spire, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation(), 20));
					pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Queens_Nest, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Ultralisk_Cavern, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Defiler_Mound, self.getStartLocation()));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null));
					pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null));
					// int typee, int RIDD, TechType techh, UpgradeType up, UnitType morph, Data data
					stuffQueue.add(new BotTech(1, 1, TechType.Lurker_Aspect, UpgradeType.None, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Metabolic_Boost, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Flyer_Attacks, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Flyer_Carapace, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Muscular_Augments, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Grooved_Spines, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 10, TechType.None, UpgradeType.Zerg_Melee_Attacks, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 11, TechType.None, UpgradeType.Zerg_Missile_Attacks, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Carapace, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Pneumatized_Carapace, UnitType.None, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
					stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Lair, myData, true));
					stuffQueue.add(new BotTech(3, 1, TechType.None, UpgradeType.None, UnitType.Zerg_Hive, myData));
					stuffQueue.add(new BotTech(1, 1, TechType.Ensnare, UpgradeType.None, UnitType.None, myData));
					stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Adrenal_Glands, UnitType.None, myData));
					stuffQueue.add(new BotTech(1, 2, TechType.Consume, UpgradeType.None, UnitType.None, myData));
					stuffQueue.add(new BotTech(1, 2, TechType.Plague, UpgradeType.None, UnitType.None, myData));
				}
			}


		
	}
	// UnitType ype, TilePosition where, int max, boolean creep, boolean isExpand, int save
	else if (self.getRace().equals(Race.Terran)){
		boolean tryP = false;
		// https://www.youtube.com/watch?v=6FEDrU85FLE
		if(game.enemy().getRace().equals(Race.Protoss) || tryP == true){
			Random rand = new Random();
			int n = rand.nextInt(3) + 1;
			// if we are T and the enemy is P.
			if(n==1){
			this.type = "mech";
			this.buildName = "2 Fac";
			this.techGoals.add(UnitType.Terran_Armory);
			this.mainGoal.add(UnitType.Terran_Siege_Tank_Tank_Mode);
			pBuildings.add(new pBuilding(UnitType.Terran_Supply_Depot, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Refinery, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 5));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, self.getStartLocation(), 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, self.getStartLocation(), 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Armory, self.getStartLocation(), 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Academy, self.getStartLocation(), 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, self.getStartLocation(), 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			stuffQueue.add(new BotTech(1, 0, TechType.Stim_Packs, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.U_238_Shells, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 7, TechType.Optical_Flare, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Tank_Siege_Mode, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 9, TechType.Lockdown, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Irradiate, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 6, TechType.Spider_Mines, UpgradeType.None, UnitType.None, myData, true));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Weapons, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Armor, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 0, TechType.Personnel_Cloaking, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Moebius_Reactor, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Plating, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Weapons, UnitType.None, myData));
			}
			else if(n == 2){
				buildName = "Bio Tank";
				type = "bio";
				this.techGoals.add(UnitType.Terran_Armory);
				this.techGoals.add(UnitType.Terran_Starport);
				this.techGoals.add(UnitType.Terran_Science_Facility);
				this.AB.add(UnitType.Terran_Barracks);
				this.mainGoal.add(UnitType.Terran_Marine);
				this.mainGoal.add(UnitType.Terran_Siege_Tank_Siege_Mode);
				this.GFR = 4500;
				this.moveRegroup = true;
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Academy, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, myData.mainChoke.getCenter().toTilePosition(), 5));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, myData.mainChoke.getCenter().toTilePosition(), 5));
				pBuildings.add(new pBuilding(UnitType.Terran_Refinery, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, myData.mainChoke.getCenter().toTilePosition(), 5));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Armory, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				stuffQueue.add(new BotTech(1, 0, TechType.Stim_Packs, UpgradeType.None, UnitType.None, myData, true));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.U_238_Shells, UnitType.None, myData, true));
				stuffQueue.add(new BotTech(1, 7, TechType.Optical_Flare, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Tank_Siege_Mode, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 9, TechType.Lockdown, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Spider_Mines, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Weapons, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Armor, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 1, TechType.Personnel_Cloaking, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Moebius_Reactor, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Plating, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Weapons, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Irradiate, UpgradeType.None, UnitType.None, myData));
			}
			else {
				// if n == 3
				buildName = "McRave 12 BUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKER";
				type = "Dunno lol";
				this.AB.add(UnitType.Terran_Barracks); // make this if the enemy is being a cunt
				this.mainGoal.add(UnitType.Terran_Battlecruiser);
				pBuildings.add(new pBuilding(UnitType.Terran_Supply_Depot, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuilding neww = new pBuilding(UnitType.Terran_Command_Center, Expands.get(0).getLocation(), true);
				neww.canBeCancelled = false;
				pBuildings.add(neww);// don't ask
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50)); // adding one more to literally out-do mcrave
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300, true));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300, true));
				pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, null, 300, true));

			}
			
		}
		else {
			// default build
			int chosenBuild = 0;
			Random rand = new Random();
			int n = rand.nextInt(4) + 1;
			if(n == 1 || chosenBuild == 1){
				this.buildName = "The Neohuman Build";
				this.type = "bio";
				this.techGoals.add(UnitType.Terran_Academy);
				this.techGoals.add(UnitType.Terran_Factory);
				this.techGoals.add(UnitType.Terran_Science_Facility);
				this.AB.add(UnitType.Terran_Barracks);
				this.AB.add(UnitType.Terran_Barracks);
				this.mainGoal.add(UnitType.Terran_Ghost);
				this.mainGoal.add(UnitType.Terran_Marine);
				this.GFR = 8000;
				pBuildings.add(new pBuilding(UnitType.Terran_Supply_Depot, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 5));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 5));
				pBuildings.add(new pBuilding(UnitType.Terran_Refinery, null, 300));
				pBuilding neww = new pBuilding(UnitType.Terran_Command_Center, Expands.get(0).getLocation(), true);
				neww.canBeCancelled = false;
				pBuildings.add(neww);
				pBuildings.add(new pBuilding(UnitType.Terran_Academy, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Armory, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				stuffQueue.add(new BotTech(1, 0, TechType.Stim_Packs, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.U_238_Shells, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 7, TechType.Optical_Flare, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Tank_Siege_Mode, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 9, TechType.Lockdown, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Spider_Mines, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Weapons, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Armor, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 1, TechType.Personnel_Cloaking, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Moebius_Reactor, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Plating, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Weapons, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Irradiate, UpgradeType.None, UnitType.None, myData));
			}
			else if (n == 2 || chosenBuild == 2){
				buildName = "Bio Tank";
				type = "bio";
				this.techGoals.add(UnitType.Terran_Starport);
				this.techGoals.add(UnitType.Terran_Science_Facility);
				this.AB.add(UnitType.Terran_Barracks);
				this.mainGoal.add(UnitType.Terran_Marine);
				this.mainGoal.add(UnitType.Terran_Siege_Tank_Siege_Mode);
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Academy, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Refinery, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 5));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, null, 5));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Armory, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				stuffQueue.add(new BotTech(1, 0, TechType.Stim_Packs, UpgradeType.None, UnitType.None, myData, true));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.U_238_Shells, UnitType.None, myData, true));
				stuffQueue.add(new BotTech(1, 7, TechType.Optical_Flare, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Tank_Siege_Mode, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 9, TechType.Lockdown, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Spider_Mines, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Weapons, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Armor, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 1, TechType.Personnel_Cloaking, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Moebius_Reactor, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Plating, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Weapons, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Irradiate, UpgradeType.None, UnitType.None, myData));
			}
			else if (n == 3 || chosenBuild == 3){
				buildName = "Boring ass Mech Build";
				type = "mech";
				this.techGoals.add(UnitType.Terran_Armory);
				pBuildings.add(new pBuilding(UnitType.Terran_Supply_Depot, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Refinery, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 5));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Academy, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Armory, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				stuffQueue.add(new BotTech(1, 0, TechType.Stim_Packs, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.U_238_Shells, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 7, TechType.Optical_Flare, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Tank_Siege_Mode, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 9, TechType.Lockdown, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Irradiate, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 6, TechType.Spider_Mines, UpgradeType.None, UnitType.None, myData, true));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Weapons, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Armor, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 0, TechType.Personnel_Cloaking, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Moebius_Reactor, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Plating, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Weapons, UnitType.None, myData));
			}
			else if (n == 4){
				buildName = "Starship Troopers";
				type = "bio";
				this.techGoals.add(UnitType.Terran_Factory);
				this.techGoals.add(UnitType.Terran_Starport);
				this.techGoals.add(UnitType.Terran_Science_Facility);
				this.AB.add(UnitType.Terran_Barracks);
				this.AB.add(UnitType.Terran_Barracks);
				this.mainGoal.add(UnitType.Terran_Marine);
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Academy, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Refinery, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 5));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 5));
				pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, null, 30));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, self.getStartLocation(), 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				stuffQueue.add(new BotTech(1, 0, TechType.Stim_Packs, UpgradeType.None, UnitType.None, myData, true));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.U_238_Shells, UnitType.None, myData, true));
				stuffQueue.add(new BotTech(1, 7, TechType.Optical_Flare, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Tank_Siege_Mode, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 9, TechType.Lockdown, UpgradeType.None, UnitType.None, myData, true));
				stuffQueue.add(new BotTech(1, 1, TechType.Spider_Mines, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Weapons, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Armor, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 1, TechType.Personnel_Cloaking, UpgradeType.None, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Moebius_Reactor, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Plating, UnitType.None, myData));
				stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Weapons, UnitType.None, myData));
				stuffQueue.add(new BotTech(1, 1, TechType.Irradiate, UpgradeType.None, UnitType.None, myData));
			}
			else {
				buildName = "McRave 12 BUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKERBUNKER";
				type = "Dunno lol";
				this.AB.add(UnitType.Terran_Barracks); // make this if the enemy is being a cunt
				this.mainGoal.add(UnitType.Terran_Battlecruiser);
				pBuildings.add(new pBuilding(UnitType.Terran_Supply_Depot, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuilding neww = new pBuilding(UnitType.Terran_Command_Center, Expands.get(0).getLocation(), true);
				neww.canBeCancelled = false;
				pBuildings.add(neww);// don't ask
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
				pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50)); // adding one more to literally out-do mcrave
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300, true));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300, true));
				pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, null, 300, true));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300, true));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300, true));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300, true));
			}
		}
		
	} else {
		// p
		// https://www.youtube.com/watch?v=fe4EK4HSPkI
		// DOOT DOOT DOOT DOOT DOOT DOOT DOOOOT DOOOOOOOOOOOOOT DOOOOOOOOOOOOOOOOOOOOOOOOOOOOT
		int chosenBuild = 3;
		Random rand = new Random();
		int n;	
		if(chosenBuild != 0){
			n = chosenBuild;
		}
		else {
		n = rand.nextInt(5) + 1;
		}

		if (n == 1 || n == 2){
			this.buildName = "12 Nexus into retardation";
			this.techGoals.add(UnitType.Protoss_Citadel_of_Adun);
			this.techGoals.add(UnitType.Protoss_Templar_Archives);
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, self.getStartLocation()));
			pBuilding neww = new pBuilding(UnitType.Protoss_Nexus, Expands.get(0).getLocation(), true);
			neww.canBeCancelled = false;
			pBuildings.add(neww);
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Cybernetics_Core, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Assimilator, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Citadel_of_Adun, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Forge, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Robotics_Facility, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Observatory, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Templar_Archives, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Forge, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Stargate, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Fleet_Beacon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Stargate, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Stargate, null));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Singularity_Charge, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Leg_Enhancements, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Ground_Weapons, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Ground_Armor, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Plasma_Shields, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 0, TechType.Psionic_Storm, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Khaydarin_Amulet, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Carrier_Capacity, UnitType.None, myData));
		}
		else if (n == 3 || chosenBuild == 3){
			this.buildName = "Dumb DT build";
			this.mainGoal.add(UnitType.Protoss_Dark_Templar);
			this.techGoals.add(UnitType.Protoss_Citadel_of_Adun);
			this.techGoals.add(UnitType.Protoss_Templar_Archives);
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Cybernetics_Core, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Assimilator, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Citadel_of_Adun, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Stargate, self.getStartLocation(), Race.Zerg)); // build Stargate only against Z. (Corsair DT)
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Templar_Archives, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Forge, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Robotics_Facility, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Observatory, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Forge, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Stargate, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Fleet_Beacon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Stargate, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Stargate, null));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Singularity_Charge, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Leg_Enhancements, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Ground_Weapons, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Ground_Armor, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Plasma_Shields, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 0, TechType.Psionic_Storm, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Khaydarin_Amulet, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Carrier_Capacity, UnitType.None, myData));
		}
		else {
			this.buildName = "Generic 2 Gate build Much wow";
			this.techGoals.add(UnitType.Protoss_Citadel_of_Adun);
			this.techGoals.add(UnitType.Protoss_Templar_Archives);
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Cybernetics_Core, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Assimilator, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Forge, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Photon_Cannon, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Protoss_Citadel_of_Adun, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Robotics_Facility, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Observatory, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Forge, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Templar_Archives, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Stargate, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Fleet_Beacon, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Stargate, null));
			pBuildings.add(new pBuilding(UnitType.Protoss_Stargate, null));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Singularity_Charge, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Leg_Enhancements, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Ground_Weapons, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Ground_Armor, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Plasma_Shields, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 0, TechType.Psionic_Storm, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Khaydarin_Amulet, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Carrier_Capacity, UnitType.None, myData));
		}
		
	}
		
	}
		// https://www.youtube.com/watch?v=PBvwcH4XX6U
		// Denn du bist, was du isst
		// Und ihr wisst, was es ist
		// DAS IST MEIN TEIL (NEIN)

	// hippity hoppity give me your property
	
	void UpdateAgainstRandom(ArrayList<UnitType> placements, Race newEnemy){
		// https://www.youtube.com/watch?v=26SKP1UrEQ0
		// ICH BIN WASSER
		// ICH BIN BROT
		if(self.getRace().equals(Race.Terran)){
			if(newEnemy.equals(Race.Protoss)){
				pBuildings.clear();
				
				if(self.allUnitCount(UnitType.Terran_Supply_Depot) == 0 && !placements.contains(UnitType.Terran_Supply_Depot)){
					pBuildings.add(new pBuilding(UnitType.Terran_Supply_Depot, null, 300));
				}
				
				if(self.allUnitCount(UnitType.Terran_Barracks) == 0 && !placements.contains(UnitType.Terran_Barracks)){
					pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				}
				
				if(self.allUnitCount(UnitType.Terran_Bunker) == 0 && !placements.contains(UnitType.Terran_Bunker)){
					pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 5));
				}
						
				if(self.allUnitCount(UnitType.Terran_Bunker) != 2 && !placements.contains(UnitType.Terran_Bunker)){
					pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 5));
				}
				
				if(self.allUnitCount(UnitType.Terran_Refinery) == 0 && !placements.contains(UnitType.Terran_Refinery)){
					pBuildings.add(new pBuilding(UnitType.Terran_Refinery, null, 300));
				}
				
				if(self.allUnitCount(UnitType.Terran_Factory) == 0 && !placements.contains(UnitType.Terran_Factory)){
					pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				}
				
				if(self.allUnitCount(UnitType.Terran_Barracks) != 2 && !placements.contains(UnitType.Terran_Barracks)){
					pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				}
				
				if(self.allUnitCount(UnitType.Terran_Academy) == 0 && !placements.contains(UnitType.Terran_Academy)){
					pBuildings.add(new pBuilding(UnitType.Terran_Academy, null, 300));
				}
				
				if(self.allUnitCount(UnitType.Terran_Engineering_Bay) == 0 && !placements.contains(UnitType.Terran_Engineering_Bay)){
					pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, null, 300));	
				}
				
				if(self.allUnitCount(UnitType.Terran_Missile_Turret) == 0 && !placements.contains(UnitType.Terran_Missile_Turret)){
					pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, null, 300));	
				}
				
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
				pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, null, 300));
			}
		}
	}
	
	int getRecommendedAmountOf(UnitType type, BotPlayer t){
		if(t == null){
			return 0;
		}
		
//		if(type.equals(UnitType.Protoss_High_Templar)){
//			int yes = 5;
//			List<Unit> units = t.player.getUnits();
//			for(Unit unit : units){
//				if(unit.getType().equals(UnitType.Terran_Marine) || 
//				unit.getType().equals(UnitType.Terran_Firebat) || 
//				unit.getType().equals(UnitType.Zerg_Hydralisk) ||
//				unit.getType().equals(UnitType.Protoss_Dragoon)){
//					yes = yes + 2;
//				}
//			}
//		}
		
		
		return 0;
	}
	
	ArrayList<UnitType> buildArmyAgainst(Player self, BotPlayer t){
		ArrayList<UnitType> ret = new ArrayList<>();
		HashMap<UnitType, Integer> v = AssignValues();
		ArrayList<UnitType> ty = t.Types;
		for(UnitType asd : t.DTypes){
			ty.add(asd);
		}
		
		// https://www.youtube.com/watch?v=r6zIGXun57U
		// cbf adding this in let
		if(self.getRace().equals(Race.Zerg)){
			
			if(t.race.equals(Race.Zerg)){
				// ZvZ
				for(UnitType a : ty){
					if(a.equals(UnitType.Zerg_Zergling)){
						v.put(UnitType.Zerg_Zergling, v.get(UnitType.Zerg_Zergling) + 1);
						v.put(UnitType.Zerg_Lurker, v.get(UnitType.Zerg_Lurker) + 1);
						v.put(UnitType.Zerg_Mutalisk,  v.get(UnitType.Zerg_Mutalisk) + 1);
						v.put(UnitType.Zerg_Ultralisk,  v.get(UnitType.Zerg_Ultralisk) + 1);
						v.put(UnitType.Zerg_Queen,  v.get(UnitType.Zerg_Queen) - 2);
						v.put(UnitType.Zerg_Defiler,  v.get(UnitType.Zerg_Defiler) - 2);
						//v.put(UnitType.Zerg_Scourge, v.get(UnitType.Zerg_Scourge));
					}
					
					if(a.equals(UnitType.Zerg_Hydralisk)){
						v.put(UnitType.Zerg_Zergling, v.get(UnitType.Zerg_Zergling) + 3);
						v.put(UnitType.Zerg_Mutalisk, v.get(UnitType.Zerg_Mutalisk) + 2);
						v.put(UnitType.Zerg_Lurker, v.get(UnitType.Zerg_Lurker) + 1);
						v.put(UnitType.Zerg_Ultralisk,  v.get(UnitType.Zerg_Ultralisk) + 3);
						v.put(UnitType.Zerg_Queen,  v.get(UnitType.Zerg_Queen) - 2);
						v.put(UnitType.Zerg_Defiler,  v.get(UnitType.Zerg_Defiler) + 1);
						//v.put(UnitType.Zerg_Scourge, v.get(UnitType.Zerg_Scourge));
					}
					
					if(a.equals(UnitType.Zerg_Lurker)){
						v.put(UnitType.Zerg_Zergling, v.get(UnitType.Zerg_Zergling) - 3);
						v.put(UnitType.Zerg_Lurker, v.get(UnitType.Zerg_Lurker) - 1);
						v.put(UnitType.Zerg_Mutalisk,  v.get(UnitType.Zerg_Mutalisk) + 3);
						v.put(UnitType.Zerg_Ultralisk,  v.get(UnitType.Zerg_Ultralisk) + 1);
						//v.put(UnitType.Zerg_Queen,  v.get(UnitType.Zerg_Ultralisk) + 3);
						//v.put(UnitType.Zerg_Defiler,  v.get(UnitType.Zerg_Defiler) + 1);
						
					}
					
					if(a.equals(UnitType.Zerg_Mutalisk)){
						v.put(UnitType.Zerg_Zergling, v.get(UnitType.Zerg_Zergling) - 3);
						v.put(UnitType.Zerg_Lurker, v.get(UnitType.Zerg_Lurker) - 2);
						v.put(UnitType.Zerg_Mutalisk,  v.get(UnitType.Zerg_Mutalisk) + 3);
						v.put(UnitType.Zerg_Ultralisk,  v.get(UnitType.Zerg_Ultralisk) - 1);
						//v.put(UnitType.Zerg_Queen,  v.get(UnitType.Zerg_Ultralisk) + 3);
						//v.put(UnitType.Zerg_Defiler,  v.get(UnitType.Zerg_Defiler) + 1);
					}
					
					if(a.equals(UnitType.Zerg_Ultralisk)){
						v.put(UnitType.Zerg_Zergling, v.get(UnitType.Zerg_Zergling) - 3);
						v.put(UnitType.Zerg_Lurker, v.get(UnitType.Zerg_Lurker) + 1);
						v.put(UnitType.Zerg_Mutalisk,  v.get(UnitType.Zerg_Mutalisk) + 2);
						v.put(UnitType.Zerg_Ultralisk,  v.get(UnitType.Zerg_Ultralisk) + 1);
						//v.put(UnitType.Zerg_Queen,  v.get(UnitType.Zerg_Ultralisk) + 3);
						//v.put(UnitType.Zerg_Defiler,  v.get(UnitType.Zerg_Defiler) + 1);
					}
				}
				// end of ZvZ
			}
			else if(t.race.equals(Race.Terran)){
				// ZvT
				for(UnitType a : ty){
					if(a.equals(UnitType.Terran_Marine)){
						
					}
				}
			// end of ZvT
			}
			else {
				// ZvP
				for(UnitType a: ty){
					if(a.equals(UnitType.Protoss_Zealot)){
						v.put(UnitType.Zerg_Zergling, v.get(UnitType.Zerg_Zergling) + 1);
						v.put(UnitType.Zerg_Hydralisk, v.get(UnitType.Zerg_Hydralisk) + 3);
						v.put(UnitType.Zerg_Lurker, v.get(UnitType.Zerg_Lurker) + 1);
						v.put(UnitType.Zerg_Mutalisk, v.get(UnitType.Zerg_Mutalisk) + 2);
						v.put(UnitType.Zerg_Ultralisk, v.get(UnitType.Zerg_Ultralisk) + 2);
					}
					
					if(a.equals(UnitType.Protoss_Dragoon)){
						v.put(UnitType.Zerg_Zergling, v.get(UnitType.Zerg_Zergling) + 3);
						v.put(UnitType.Zerg_Hydralisk, v.get(UnitType.Zerg_Hydralisk) + 1);
						v.put(UnitType.Zerg_Mutalisk, v.get(UnitType.Zerg_Mutalisk) + 2);
						v.put(UnitType.Zerg_Lurker, v.get(UnitType.Zerg_Lurker) + 1);
						v.put(UnitType.Zerg_Ultralisk, v.get(UnitType.Zerg_Ultralisk) + 2);
						
					}
					
					if(a.equals(UnitType.Protoss_Reaver)){
						v.put(UnitType.Zerg_Zergling, v.get(UnitType.Zerg_Zergling) - 3);
						v.put(UnitType.Zerg_Hydralisk, v.get(UnitType.Zerg_Hydralisk) - 3);
						v.put(UnitType.Zerg_Mutalisk, v.get(UnitType.Zerg_Mutalisk) + 2);
						v.put(UnitType.Zerg_Lurker, v.get(UnitType.Zerg_Lurker) - 1);
						v.put(UnitType.Zerg_Ultralisk, v.get(UnitType.Zerg_Ultralisk) - 2);
						
					}
					
					if(a.equals(UnitType.Protoss_Carrier)){
						v.put(UnitType.Zerg_Zergling, v.get(UnitType.Zerg_Zergling) - 1);
						v.put(UnitType.Zerg_Hydralisk, v.get(UnitType.Zerg_Hydralisk) + 2);
						v.put(UnitType.Zerg_Mutalisk, v.get(UnitType.Zerg_Mutalisk) + 2);
						v.put(UnitType.Zerg_Lurker, v.get(UnitType.Zerg_Lurker) - 3);
						v.put(UnitType.Zerg_Ultralisk, v.get(UnitType.Zerg_Ultralisk) - 2);
						v.put(UnitType.Zerg_Scourge, v.get(UnitType.Zerg_Ultralisk) - 2);
						
						
					}
				// end of ZvP	
				}

					
				}
			// end for z	
			}
		else if(self.equals(Race.Terran)){
			
		for(UnitType a: ty){
			if(a.equals(UnitType.Zerg_Zergling)){
				v.put(UnitType.Terran_Marine, v.get(UnitType.Terran_Marine) + 1);
				v.put(UnitType.Terran_Firebat, v.get(UnitType.Terran_Firebat) + 3);
				v.put(UnitType.Terran_Vulture, v.get(UnitType.Terran_Vulture) - 1);
				v.put(UnitType.Terran_Siege_Tank_Tank_Mode, v.get(UnitType.Terran_Siege_Tank_Tank_Mode) + 1);
				v.put(UnitType.Terran_Goliath, v.get(UnitType.Terran_Goliath) - 1);
				v.put(UnitType.Terran_Wraith, v.get(UnitType.Terran_Wraith) - 1);
				//v.put(UnitType.Terran_Science_Vessel, v.get(UnitType.Terran_Science_Vessel) - 1);
				//v.put(UnitType.Terran_Battlecruiser, v.get(UnitType.Terran_Battlecruiser) - 1);
				//v.put(UnitType.Terran_Valkyrie, v.get(UnitType.Terran_Terran_Valkyrie) - 1);
			}
			
			if(a.equals(UnitType.Zerg_Hydralisk)){
				v.put(UnitType.Terran_Marine, v.get(UnitType.Terran_Marine) + 1);
				v.put(UnitType.Terran_Firebat, v.get(UnitType.Terran_Firebat) - 1);
				v.put(UnitType.Terran_Vulture, v.get(UnitType.Terran_Vulture) + 1);
				v.put(UnitType.Terran_Siege_Tank_Tank_Mode, v.get(UnitType.Terran_Siege_Tank_Tank_Mode) + 3);
				//v.put(UnitType.Terran_Goliath, v.get(UnitType.Terran_Goliath) - 1);
				//v.put(UnitType.Terran_Wraith, v.get(UnitType.Terran_Wraith) - 1);
				//v.put(UnitType.Terran_Science_Vessel, v.get(UnitType.Terran_Science_Vessel) - 1);
				//v.put(UnitType.Terran_Battlecruiser, v.get(UnitType.Terran_Battlecruiser) - 1);
				//v.put(UnitType.Terran_Valkyrie, v.get(UnitType.Terran_Terran_Valkyrie) - 1);
			}
			
			if(a.equals(UnitType.Zerg_Lurker)){
				v.put(UnitType.Terran_Marine, v.get(UnitType.Terran_Marine) - 1);
				v.put(UnitType.Terran_Firebat, v.get(UnitType.Terran_Firebat) - 1);
				v.put(UnitType.Terran_Vulture, v.get(UnitType.Terran_Vulture) - 1);
				v.put(UnitType.Terran_Siege_Tank_Tank_Mode, v.get(UnitType.Terran_Siege_Tank_Tank_Mode) + 3);
				//v.put(UnitType.Terran_Goliath, v.get(UnitType.Terran_Goliath) - 1);
				//v.put(UnitType.Terran_Wraith, v.get(UnitType.Terran_Wraith) - 1);
				//v.put(UnitType.Terran_Science_Vessel, v.get(UnitType.Terran_Science_Vessel) - 1);
				//v.put(UnitType.Terran_Battlecruiser, v.get(UnitType.Terran_Battlecruiser) - 1);
				//v.put(UnitType.Terran_Valkyrie, v.get(UnitType.Terran_Terran_Valkyrie) - 1);
			}
			
			if(a.equals(UnitType.Zerg_Mutalisk)){
				v.put(UnitType.Terran_Marine, v.get(UnitType.Terran_Marine) + 2);
				v.put(UnitType.Terran_Firebat, v.get(UnitType.Terran_Firebat) - 1);
				//v.put(UnitType.Terran_Vulture, v.get(UnitType.Terran_Vulture) - 1);
				v.put(UnitType.Terran_Siege_Tank_Tank_Mode, v.get(UnitType.Terran_Siege_Tank_Tank_Mode) - 3);
				v.put(UnitType.Terran_Goliath, v.get(UnitType.Terran_Goliath) + 1);
				v.put(UnitType.Terran_Wraith, v.get(UnitType.Terran_Wraith) + 1);
				//v.put(UnitType.Terran_Science_Vessel, v.get(UnitType.Terran_Science_Vessel) - 1);
				//v.put(UnitType.Terran_Battlecruiser, v.get(UnitType.Terran_Battlecruiser) - 1);
				v.put(UnitType.Terran_Valkyrie, v.get(UnitType.Terran_Valkyrie) + 1);
			}
			
			if(a.equals(UnitType.Zerg_Ultralisk)){
				v.put(UnitType.Terran_Marine, v.get(UnitType.Terran_Marine) - 1);
				v.put(UnitType.Terran_Firebat, v.get(UnitType.Terran_Firebat) - 1);
				v.put(UnitType.Terran_Vulture, v.get(UnitType.Terran_Vulture) + 1);
				v.put(UnitType.Terran_Siege_Tank_Tank_Mode, v.get(UnitType.Terran_Siege_Tank_Tank_Mode) + 2);
				v.put(UnitType.Terran_Goliath, v.get(UnitType.Terran_Goliath) + 1);
				v.put(UnitType.Terran_Wraith, v.get(UnitType.Terran_Wraith) + 1);
				//v.put(UnitType.Terran_Science_Vessel, v.get(UnitType.Terran_Science_Vessel) - 1);
				//v.put(UnitType.Terran_Battlecruiser, v.get(UnitType.Terran_Battlecruiser) - 1);
				v.put(UnitType.Terran_Valkyrie, v.get(UnitType.Terran_Valkyrie) + 1);
			}
			
			if(a.equals(UnitType.Zerg_Guardian)){
				v.put(UnitType.Terran_Marine, v.get(UnitType.Terran_Marine) - 1);
				//v.put(UnitType.Terran_Firebat, v.get(UnitType.Terran_Firebat) - 1);
				//v.put(UnitType.Terran_Vulture, v.get(UnitType.Terran_Vulture) + 1);
				//v.put(UnitType.Terran_Siege_Tank_Tank_Mode, v.get(UnitType.Terran_Siege_Tank_Tank_Mode) + 2);
				v.put(UnitType.Terran_Goliath, v.get(UnitType.Terran_Goliath) + 2);
				v.put(UnitType.Terran_Wraith, v.get(UnitType.Terran_Wraith) + 2);
				//v.put(UnitType.Terran_Science_Vessel, v.get(UnitType.Terran_Science_Vessel) - 1);
				//v.put(UnitType.Terran_Battlecruiser, v.get(UnitType.Terran_Battlecruiser) - 1);
				//v.put(UnitType.Terran_Valkyrie, v.get(UnitType.Terran_Valkyrie) + 1);
			}
			
			
			
		}
		
		}
		
		return ret;
	}
	
	HashMap<UnitType, Integer> AssignValues(){
		HashMap<UnitType, Integer> v = new HashMap<>();
		
		if(self.getRace().equals(Race.Zerg)){
			// if im Z
			v.put(UnitType.Zerg_Zergling, 0);
			v.put(UnitType.Zerg_Hydralisk, 0);
			v.put(UnitType.Zerg_Lurker, 0);
			v.put(UnitType.Zerg_Mutalisk, 0);
			v.put(UnitType.Zerg_Queen, 0);
			v.put(UnitType.Zerg_Scourge, 0);
			v.put(UnitType.Zerg_Ultralisk, 0);
			v.put(UnitType.Zerg_Defiler, 0);
		}
		else if(self.getRace().equals(Race.Terran)){
			// if im t
			v.put(UnitType.Terran_Marine, 0);
			v.put(UnitType.Terran_Firebat, 0);
			v.put(UnitType.Terran_Medic, 0);
			v.put(UnitType.Terran_Vulture, 0);
			v.put(UnitType.Terran_Siege_Tank_Tank_Mode, 0);
			v.put(UnitType.Terran_Goliath, 0);
			v.put(UnitType.Terran_Wraith, 0);
			v.put(UnitType.Terran_Science_Vessel, 0);
			v.put(UnitType.Terran_Valkyrie, 0);
			v.put(UnitType.Terran_Battlecruiser, 0);
			v.put(UnitType.Terran_Ghost, 0);
			
			
		}
		else {
			// if im p
			v.put(UnitType.Protoss_Zealot, 0);
			v.put(UnitType.Protoss_Dragoon, 0);
			v.put(UnitType.Protoss_High_Templar, 0);
			v.put(UnitType.Protoss_Dark_Templar, 0);
			v.put(UnitType.Protoss_Reaver, 0);
			v.put(UnitType.Protoss_Observer, 0);
			v.put(UnitType.Protoss_Scout, 0);
			v.put(UnitType.Protoss_Carrier, 0);
			v.put(UnitType.Protoss_Corsair, 0);		
		}
		
		return v;
	}
	
	ArrayList<UnitType> UpdateOpenerAgainst(BotPlayer p){
		ArrayList<UnitType> yesyes = new ArrayList<>();
		ArrayList<UnitType> ads = new ArrayList<>();
		yesyes.add(UnitType.Terran_Barracks);
		yesyes.add(UnitType.Terran_Factory);
		yesyes.add(UnitType.Terran_Starport);
		yesyes.add(UnitType.Zerg_Hatchery);
		yesyes.add(UnitType.Zerg_Lair);
		yesyes.add(UnitType.Zerg_Hive);
		yesyes.add(UnitType.Protoss_Gateway);
		yesyes.add(UnitType.Protoss_Robotics_Facility);
		yesyes.add(UnitType.Protoss_Stargate);
		
		int asd = 0;
		for(Unit unit : p.Buildings){
			if(yesyes.contains(unit.getType())){
				asd++;
			}
		}
		
		for(int i = 0; i < Math.round(asd/3); i++){
			if(self.equals(Race.Terran)){
				ads.add(UnitType.Terran_Barracks);
			}
		}
		
		if(ads.isEmpty()){
			return null;
		}
		else {
			return ads;
		}
		
	
		

	}
	
	
	ArrayList<UnitType> generateArmyBasic(BotPlayer p){
		// https://www.youtube.com/watch?v=yg8VFMZ6_g8
		// AAAAAAAAAAAAAAAAAAAAAAAAAAAAAH
		ArrayList<UnitType> ret = new ArrayList<>();
		
		
		if(self.getRace().equals(Race.Terran)){
			// if im t
			if(p.race.equals(Race.Terran)){
				// if enemy t
				
			}
			else if(self.getRace().equals(Race.Zerg)){
				// if enemy z
			}
			else {
				// if enemy p or unknown
			}
		}
		else if(self.getRace().equals(Race.Zerg)){
			// if im z
			if(p.race.equals(Race.Terran)){
				// if enemy t
				
			}
			else if(self.getRace().equals(Race.Zerg)){
				// if enemy z
			}
			else {
				// if enemy p or unknown
			}
		}
		else {
			// if im p
			if(p.race.equals(Race.Terran)){
				// if enemy t
				
			}
			else if(self.getRace().equals(Race.Zerg)){
				// if enemy z
			}
			else {
				// if enemy p or unknown
			}
		}
		
		return ret;
	}
	
	
	
	
	
}
