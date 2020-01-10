package Bot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	Strategy(Race ee, ArrayList<Base> ep, Data myDataa, Game gamee, ArrayList<ChokePoint> cocks){
		this.enemy = ee;
		this.stuffQueue = new ArrayList<BotTech>();
		this.pBuildings = new ArrayList<pBuilding>();
		this.myChokes = cocks;
		this.Expands = ep;
		this.myData = myDataa;
		this.game = gamee;
		this.self = game.self();
		
		Opener();
	}

	private void Opener() {
		// TODO Auto-generated keyboard smash
		// feelsGoodMan Cancer clap
		if(self.getRace().equals(Race.Zerg)){
			//if z
			if(this.enemy.equals(Race.Zerg)){
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(0).getLocation(), true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, null, 300));
			pBuildings.add(new pBuilding(UnitType.Zerg_Extractor, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20, true, false, 1));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20, true, false, 1));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation(), 20));
			pBuildings.add(new pBuilding(UnitType.Zerg_Spire, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Queens_Nest, self.getStartLocation()));
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
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Lair, myData));
			stuffQueue.add(new BotTech(3, 1, TechType.None, UpgradeType.None, UnitType.Zerg_Hive, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Ensnare, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Adrenal_Glands, UnitType.None, myData));
		}
		else if (this.enemy.equals(Race.Terran)){
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(0).getLocation(), true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, null, 300));
			pBuildings.add(new pBuilding(UnitType.Zerg_Extractor, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20, true, false, 1));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20, true, false, 1));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20, true, false, 1));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation(), 20));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Queens_Nest, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Spire, self.getStartLocation()));
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
			stuffQueue.add(new BotTech(1, 0, TechType.Lurker_Aspect, UpgradeType.None, UnitType.None, myData));
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
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
			stuffQueue.add(new BotTech(3, 1, TechType.None, UpgradeType.None, UnitType.Zerg_Lair, myData));
			stuffQueue.add(new BotTech(3, 1, TechType.None, UpgradeType.None, UnitType.Zerg_Hive, myData));
			stuffQueue.add(new BotTech(3, 1, TechType.None, UpgradeType.None, UnitType.Zerg_Hive, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Ensnare, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Adrenal_Glands, UnitType.None, myData));
		}
		else {
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(0).getLocation(), true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, null, 50));
			pBuildings.add(new pBuilding(UnitType.Zerg_Extractor, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20, true, false, 1));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20, true, false, 1));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20, true, false, 1));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation(), 20));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Queens_Nest, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Spire, self.getStartLocation()));
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
			stuffQueue.add(new BotTech(1, 2, TechType.Lurker_Aspect, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Metabolic_Boost, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Muscular_Augments, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Grooved_Spines, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Melee_Attacks, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 2, TechType.None, UpgradeType.Zerg_Missile_Attacks, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Zerg_Carapace, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Pneumatized_Carapace, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 1, TechType.None, UpgradeType.Adrenal_Glands, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Ensnare, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Sunken_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
			stuffQueue.add(new BotTech(3, 0, TechType.None, UpgradeType.None, UnitType.Zerg_Spore_Colony, myData));
			stuffQueue.add(new BotTech(3, 1, TechType.None, UpgradeType.None, UnitType.Zerg_Lair, myData));
			stuffQueue.add(new BotTech(3, 1, TechType.None, UpgradeType.None, UnitType.Zerg_Hive, myData));
			// int typee, int RIDD, TechType techh, UpgradeType up, UnitType morph, Data dataa
		}
		

		
	}
	// UnitType ype, TilePosition where, int max, boolean creep, boolean isExpand, int save
	else if (self.getRace().equals(Race.Terran)){
		boolean tryP = false;
		// https://www.youtube.com/watch?v=6FEDrU85FLE
		if(game.enemy().getRace().equals(Race.Protoss) || tryP == true){
			// if we are T and the enemy is P.
			pBuildings.add(new pBuilding(UnitType.Terran_Supply_Depot, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Refinery, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Academy, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Armory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			stuffQueue.add(new BotTech(1, 0, TechType.Stim_Packs, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.U_238_Shells, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Optical_Flare, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Tank_Siege_Mode, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Lockdown, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Irradiate, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 6, TechType.Spider_Mines, UpgradeType.None, UnitType.None, myData, true));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Weapons, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Armor, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 0, TechType.Personnel_Cloaking, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Moebius_Reactor, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Plating, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Weapons, UnitType.None, myData));
			
		}
		else {
			// default build
			pBuildings.add(new pBuilding(UnitType.Terran_Supply_Depot, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
			pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
			pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 50));
			pBuildings.add(new pBuilding(UnitType.Terran_Refinery, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Academy, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
			pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, null, 30));
			pBuildings.add(new pBuilding(UnitType.Terran_Armory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));	
			pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			stuffQueue.add(new BotTech(1, 0, TechType.Stim_Packs, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.U_238_Shells, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Optical_Flare, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Tank_Siege_Mode, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Lockdown, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Spider_Mines, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Weapons, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Armor, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 1, TechType.Personnel_Cloaking, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Moebius_Reactor, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Plating, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Vehicle_Weapons, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Irradiate, UpgradeType.None, UnitType.None, myData));
		}
		
	}else {
		// p
		// https://www.youtube.com/watch?v=fe4EK4HSPkI
		// DOOT DOOT DOOT DOOT DOOT DOOT DOOOOT DOOOOOOOOOOOOOT DOOOOOOOOOOOOOOOOOOOOOOOOOOOOT
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
		// int typee, int RIDD, TechType techh, UpgradeType up, UnitType morph, Data dataa
		// https://www.youtube.com/watch?v=lX44CAz-JhU
		// OH NO IT'S A DANISH BAND.
		// FUUUUUUUUUURK....
		
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
					pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 300));
				}
						
				if(self.allUnitCount(UnitType.Terran_Bunker) != 2 && !placements.contains(UnitType.Terran_Bunker)){
					pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 300));
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
