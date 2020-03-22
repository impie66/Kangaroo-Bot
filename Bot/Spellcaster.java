package Bot;

import java.util.ArrayList;

import bwapi.*;

public class Spellcaster {
	Unit unit;
	Data data;
	ArrayList<TechType> hurtful;
	ArrayList<TechType> helpful;
	Game game;
	Util till;
	
	Spellcaster(Unit unitt, Data myData, Util til){
		this.unit = unitt;
		this.data = myData;
		hurtful = new ArrayList<TechType>();
		helpful = new ArrayList<TechType>();
		setSpells(unit.getType());
		game = this.data.game;
		this.till = til;
		
	}
	
	
	void onCombat(){
		// once combat starts near the spellcaster

	}
	
	void onAllyTakeDamage(){
		// trigger when an ally takes damage
	
	}
	
	
	void combatLoop(ArrayList<Unit> friendly, ArrayList<Unit> enemy){
		Unit myUnit = this.unit;
		
		
		if(myUnit.getType().equals(UnitType.Terran_Siege_Tank_Tank_Mode) && unit.canSiege() && this.data.getSimScore(myUnit) > 0.20){
			for(Unit unit : enemy){
				if(myUnit.getPosition().getApproxDistance(unit.getPosition()) < 520 && myUnit.getPosition().getApproxDistance(unit.getPosition()) > 110){
					if(myUnit.canSiege()){
					myUnit.siege();
					break;
					}
				}
			}
		}
		
		if(myUnit.getType().equals(UnitType.Zerg_Lurker) && unit.canBurrow()){
			for(Unit unit : enemy){
				if(myUnit.getPosition().getApproxDistance(unit.getPosition()) < 250){
					if(myUnit.canBurrow()){
					myUnit.burrow();
					break;
					}
				}
			}
		}
		
		
			
		if(myUnit.getType().equals(UnitType.Terran_Wraith) && myUnit.canUseTech(TechType.Cloaking_Field) && this.data.isNearEnemyOrBetter(unit)){
			myUnit.cloak();
		}
//		
//		if(myUnit.getType().equals(UnitType.Terran_Ghost) && myUnit.canUseTech(TechType.Personnel_Cloaking) && this.data.isNearEnemyOrBetter(unit)){
//			myUnit.cloak();
//		}
		
		if(myUnit.getType().equals(UnitType.Zerg_Defiler) && myUnit.getEnergy() < 200 && data.canBeDistrubed(myUnit)){
			for(Unit unit : friendly){
				if(unit.getType().equals(UnitType.Zerg_Zergling) || unit.getType().equals(UnitType.Zerg_Broodling)){
					if(!myUnit.getOrder().equals(Order.CastConsume) && !isBusy(myUnit)){
						myUnit.useTech(TechType.Consume, unit);
						break;
					}
				}
			}
		}
		
		if(myUnit.isCloaked() && !this.data.isNearEnemyOrBetter(unit) && myUnit.canDecloak()){
			myUnit.decloak();
		}
		
					
		if(!friendly.isEmpty()){
			for(TechType help : helpful){
				main:
				if(help.targetsUnit()){
					for(Unit unit : friendly){
						if(data.isInCombat(unit) && myUnit.canUseTech(help, unit) && data.self.hasResearched(help) && !isBusy(myUnit)){
							myUnit.useTech(help, unit);
							break main;
						}
					}
				}
			
				if(help.targetsPosition()){
					if(help.equals(TechType.Spider_Mines)){
						for(Unit unit : friendly){
							
							if(unit.getType().equals(UnitType.Terran_Vulture_Spider_Mine)){
								continue;
							}
							
						   if(data.isNearEnemyOrBetter(unit) && myUnit.canUseTech(help, myUnit.getPosition()) && data.self.hasResearched(help) && !isBusy(myUnit) && !data.AlreadyMinesNearby(unit.getPosition(), 50)){
								myUnit.useTech(help, myUnit.getPosition());
								data.DND(myUnit, game.getFrameCount() + 10);
								break;
							}
						   
						}
						
					}
					else {
						// if not mines
						for(Unit unit : friendly){
							if(data.isInCombat(unit) &&	 myUnit.canUseTech(help, unit.getPosition()) && data.self.hasResearched(help) && !isBusy(myUnit)){
								myUnit.useTech(help, unit.getPosition());
								break;
							}
						}
					}
				}
				

		
			}	
		}
		
		// MJGA (Make Java Greater Again)
		// https://www.youtube.com/watch?v=asDlYjJqzWE
		// IT'S CLOSING ON YOU
			
		if(!enemy.isEmpty()){
			for(TechType help : hurtful){
				main:
				if(help.targetsUnit()){
					for(Unit unit : enemy){
						if(myUnit.canUseTech(help, unit) && data.self.hasResearched(help) && !isBusy(myUnit) && !targetAlreadyEffected(help, unit)){
							myUnit.useTech(help, unit);
							//System.out.println("Casting: " + help.toString() + " Targetting: " + unit.getType().toString());
							data.game.drawLineMap(myUnit.getPosition(), unit.getPosition(), Color.White);
							break main;
						}
					}
				}
			
				if(help.targetsPosition()){
					for(Unit unit : enemy){
						if(help.equals(TechType.Psionic_Storm) && unit.getType().isBuilding()){
							continue;
						}
						
						if(myUnit.canUseTech(help, unit.getPosition()) && data.self.hasResearched(help) && !isBusy(myUnit) && !targetAlreadyEffected(help, unit)){
							myUnit.useTech(help, unit.getPosition());
							break;
						}
					}
				}
			}
		
		// end of this pile of shit
		}
		
		// the real end of this pile of shit
	}
	
	
	void setSpells(UnitType type){
		// wingardium leviosa
		// ronald weasley
		// It's levio-SAAAAUUUUH
		
		if(type.equals(UnitType.Terran_Medic)){
			helpful.add(TechType.Healing);
			hurtful.add(TechType.Optical_Flare);
		}
					
		if(type.equals(UnitType.Zerg_Queen)){
			hurtful.add(TechType.Ensnare);
			hurtful.add(TechType.Spawn_Broodlings);
			hurtful.add(TechType.Infestation);
		}
		
		if(type.equals(UnitType.Zerg_Defiler)){
			helpful.add(TechType.Dark_Swarm);
		}
		
		if(type.equals(UnitType.Zerg_Defiler)){
			hurtful.add(TechType.Plague);
		}
		
		if(type.equals(UnitType.Protoss_High_Templar)){
			hurtful.add(TechType.Psionic_Storm);
		}
		
		if(type.equals(UnitType.Terran_Ghost)){
			hurtful.add(TechType.Lockdown);
			//hurtful.add(TechType.Nuclear_Strike);
			// RIP NUKES
			// (FOR NOW)
			// The flight plan i just filed to the agency lists me, my men, doctor pavel here but only one of you
			// first to talk gets to stay on my aircraft
			// who payed you to grab doctor pavel?
			// He didn't fly so good
			// who wants to try next
			// tell me about bane
			// why does he wear the mask
			// alot of loyalty for a hired gun
			// prehaps he is wondering why someone would shoot a man
			// before throwing him out of a plane
			// at least you can talk
			// who are you
			// it doesn't matter who we are
			// what matters is our plan
			// no one cared who i was until i put on the mask
			// if i pull that of will you die?
			// it would be extremely painful
			// your a big guy
			// FOR YOU.
			// was getting caught a part of your plan
			// of course!
			// doctor pavel refused our offer in favor of yours
			// we are to find out what he told you
			// NOTHING
			// I SAID NOTHING
			// well congratulations
			// you've got yourself caught
			// sir????
			// what's the next step of your masterful plan?
			// crashing this plane
			// DUN DUN DUN DUN DUN DUN DUN DUN
			// WITH NO SURVIVORS
		}
		
		
		if(type.equals(UnitType.Terran_Science_Vessel)){
			hurtful.add(TechType.EMP_Shockwave);
			helpful.add(TechType.Defensive_Matrix);
		}
		
		if(type.equals(UnitType.Terran_Battlecruiser)){
			hurtful.add(TechType.Yamato_Gun);
		}
		
		if(type.equals(UnitType.Terran_Vulture)){
			helpful.add(TechType.Spider_Mines);
		}
		
		
				
	}
	
	boolean isBusy(Unit unit){
		// https://www.youtube.com/watch?v=owTWCbq_nSk
		// SOME SUPERHERO
		// SOME FAIRY TALE BLISS
		if(unit.getOrder().equals(Order.CastDarkSwarm) ||
		   unit.getOrder().equals(Order.CastDefensiveMatrix) ||
		   unit.getOrder().equals(Order.CastEMPShockwave) ||
		   unit.getOrder().equals(Order.CastLockdown) ||
		   //unit.getOrder().equals(Order.CastNuclearStrike) ||
		   unit.getOrder().equals(Order.CastOpticalFlare) ||
		   unit.getOrder().equals(Order.CastPlague) ||
		   unit.getOrder().equals(Order.CastPsionicStorm) ||
		   unit.getOrder().equals(Order.CastSpawnBroodlings) ||
		   unit.getOrder().equals(Order.FireYamatoGun) ||
		   unit.getOrder().equals(Order.CastEnsnare) ||
		   unit.getOrder().equals(Order.PlaceMine) ||
		   unit.getOrder().equals(Order.VultureMine) ||
		   unit.getOrder().equals(Order.InfestingCommandCenter) ||
		   unit.getOrder().equals(Order.MoveToInfest) ||
		   unit.getOrder().equals(Order.CastInfestation) ||
		   unit.getOrder().equals(Order.CastConsume)){
			return true;
		}
		   
		
		
		return false;
		
	}
	
	boolean targetAlreadyEffected(TechType what, Unit target){
		if(what.equals(TechType.Optical_Flare)){
			return target.isBlind();
		}
		
		if(what.equals(TechType.Lockdown)){
			return target.isLockedDown();
		}
		
		if(what.equals(TechType.Plague)){
			return target.isPlagued();
		}
		
		if(what.equals(TechType.Ensnare)){
			return target.isEnsnared();
		}
		
		if(what.equals(TechType.Defensive_Matrix)){
			return target.isDefenseMatrixed();
		}
		
		if(what.equals(TechType.Dark_Swarm)){
			return target.isUnderDarkSwarm();
		}
		
		if(what.equals(TechType.Psionic_Storm)){
			return target.isUnderStorm();
		}
		
		if(what.equals(TechType.EMP_Shockwave)){
			return target.getShields() != target.getType().maxShields();
		}
		
		return false;
		
		
	}
	

	
	
	
}
