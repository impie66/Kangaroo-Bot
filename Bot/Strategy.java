package Bot;

import java.util.ArrayList;

import bwapi.Game;
import bwapi.Player;
import bwapi.Race;
import bwapi.TechType;
import bwapi.TilePosition;
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
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 5, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, self.getStartLocation(), 20));
			pBuildings.add(new pBuilding(UnitType.Zerg_Spire, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, self.getStartLocation()));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, null, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Queens_Nest, self.getStartLocation()));
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
		}
		else if (this.enemy.equals(Race.Terran)){
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(0).getLocation(), true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, null, 300));
			pBuildings.add(new pBuilding(UnitType.Zerg_Extractor, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 5, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10));
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
		}
		else {
			pBuildings.add(new pBuilding(UnitType.Zerg_Hatchery, Expands.get(0).getLocation(), true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Spawning_Pool, null, 300));
			pBuildings.add(new pBuilding(UnitType.Zerg_Extractor, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 5, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 20, true));
			pBuildings.add(new pBuilding(UnitType.Zerg_Hydralisk_Den, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Evolution_Chamber, null));
			pBuildings.add(new pBuilding(UnitType.Zerg_Creep_Colony, Expands.get(0).getLocation(), 10));
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
		boolean tryP = true;
		// https://www.youtube.com/watch?v=6FEDrU85FLE
		if(game.enemy().getRace().equals(Race.Protoss) || tryP == true){
			pBuildings.add(new pBuilding(UnitType.Terran_Supply_Depot, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Refinery, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Academy, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, null, 300));
			stuffQueue.add(new BotTech(1, 0, TechType.Stim_Packs, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.U_238_Shells, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Optical_Flare, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Tank_Siege_Mode, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Lockdown, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Spider_Mines, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Weapons, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Armor, UnitType.None, myData));
		}
		else {
		// if enemy is P
		// cause they imba.
			pBuildings.add(new pBuilding(UnitType.Terran_Supply_Depot, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Bunker, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Refinery, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Academy, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Engineering_Bay, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Missile_Turret, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Barracks, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Starport, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Factory, null, 300));
			pBuildings.add(new pBuilding(UnitType.Terran_Science_Facility, null, 300));
			stuffQueue.add(new BotTech(1, 0, TechType.Stim_Packs, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.U_238_Shells, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Optical_Flare, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Tank_Siege_Mode, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Lockdown, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(1, 1, TechType.Spider_Mines, UpgradeType.None, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Weapons, UnitType.None, myData));
			stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Terran_Infantry_Armor, UnitType.None, myData));
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
		pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Forge, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Citadel_of_Adun, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Pylon, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Gateway, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Robotics_Facility, null));
		pBuildings.add(new pBuilding(UnitType.Protoss_Observatory, null));
		stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Singularity_Charge, UnitType.None, myData));
		stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Leg_Enhancements, UnitType.None, myData));
		stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Ground_Weapons, UnitType.None, myData));
		stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Ground_Armor, UnitType.None, myData));
		stuffQueue.add(new BotTech(2, 0, TechType.None, UpgradeType.Protoss_Plasma_Shields, UnitType.None, myData));
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
	
	
	
}
