package Bot;

import java.util.ArrayList;
import java.util.function.Predicate;

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
	
	
	void combatLoop(ArrayList<Unit> friendly, ArrayList<Unit> enemy){
		Unit myUnit = this.unit;
		
		if(unit.getType() != UnitType.Zerg_Lurker){
			if(helpful.isEmpty() && hurtful.isEmpty()){
				return;
			}
		}
		
		
		if(myUnit.getType().equals(UnitType.Terran_Siege_Tank_Tank_Mode) && myUnit.canSiege()){
			for(Unit unit : enemy){
				if(myUnit.getPosition().getApproxDistance(unit.getPosition()) < 520 && myUnit.getPosition().getApproxDistance(unit.getPosition()) > 110){
					myUnit.siege();
					break;
				}
			}
		}
		
		if(myUnit.getType().equals(UnitType.Zerg_Lurker) && !myUnit.isBurrowed() && myUnit.canBurrow()){
			for(Unit unit : enemy){
				if(myUnit.getPosition().getApproxDistance(unit.getPosition()) < 300){			
					myUnit.burrow();
					break;
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
		
		if(myUnit.getType().equals(UnitType.Zerg_Defiler) && myUnit.getEnergy() < 175 && data.canBeDistrubed(myUnit)){
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
			help:
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
					if(help.equals(TechType.Spider_Mines) && data.self.hasResearched(help)){
						
							boolean canDrop = false;
							
							for(Unit unit : game.getUnitsInRadius(myUnit.getPosition(), 200)){
								
								if(unit.getType().isFlyer() || unit.getType().isBuilding()){
									continue;
								}
								else {
									if(unit.getType().isWorker() && !till.isAttacking(unit)){
										continue;
									}
									
									if(unit.isLockedDown()){
										continue;
									}
									// will set because invalid units are continuing.
									canDrop = true;
								}

								
							}

						if(canDrop == true){						
							if(!data.AlreadyMinesNearby(myUnit.getPosition(), 80)){
								// if mines nearby
									
								if(myUnit.canUseTech(help, myUnit.getPosition()) && !isBusy(myUnit)){
									myUnit.useTech(help, myUnit.getPosition());
									data.DND(myUnit, game.getFrameCount() + 10);
								}
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
		
				} // end of target pos
			} // end of help	
		} // end of friendy check
		
		
		// MJGA (Make Java Greater Again)
		// https://www.youtube.com/watch?v=asDlYjJqzWE
		// IT'S CLOSING ON YOU
			
		if(!enemy.isEmpty()){
			main:
			for(TechType help : hurtful){
				
				
				if(!data.self.hasResearched(help)){
					continue;
				}
					
				if(help.targetsUnit()){
					

				for(Unit unit : enemy){
						
						if(help.equals(TechType.Irradiate)){
							if(!unit.getType().isOrganic()){
								continue;
							}
						}
						
						
						
						if(unit.isCloaked() || unit.isBurrowed()){
							if(!unit.isDetected()){
								continue;
							}
						}
											
						if(myUnit.canUseTech(help, unit) && data.self.hasResearched(help) && !isBusy(myUnit) && !targetAlreadyEffected(help, unit) && !data.isBeingCastedOn(unit, help) && isWorthToCastSingleTarget(help, unit) && isItFuckingWorth(unit)){
							myUnit.useTech(help, unit);
							data.tents.add(new Intent(myUnit, unit, help));
							//System.out.println("Casting: " + help.toString() + " Targetting: " + unit.getType().toString());
							data.game.drawLineMap(myUnit.getPosition(), unit.getPosition(), Color.White);
							break main;
						}
					}
				}
			
				if(help.targetsPosition()){
					for(Unit unit : enemy){
						if(data.self.hasResearched(help) && !isBusy(myUnit) && !targetAlreadyEffected(help, unit)){
							if(help.equals(TechType.Psionic_Storm) && unit.getType().isBuilding()){
								continue;
							}
							
							if(!isWorthToCast(myUnit, unit.getPosition(), help, 70)){
								continue;
							}
												
							if(myUnit.canUseTech(help, unit.getPosition())){
								myUnit.useTech(help, unit.getPosition());
								break;
							}
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
			//hurtful.add(TechType.Optical_Flare);
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
			hurtful.add(TechType.Irradiate);
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
		   unit.getOrder().equals(Order.CastConsume) ||
		   unit.getOrder().equals(Order.CastIrradiate) ||
		   unit.getOrder().equals(Order.CastMindControl) || 
		   unit.getOrder().equals(Order.HealMove) || 
		   unit.getOrder().equals(Order.MedicHeal) ||
		   unit.getOrder().equals(Order.MedicHealToIdle)){
			return true;
		}
		   
		
		
		return false;
		
	}
	
	boolean targetAlreadyEffected(TechType what, Unit target){
		if(what.equals(TechType.Optical_Flare)){
			return target.isBlind();
		}
		
		
		if(what.equals(TechType.Healing)){
			return target.isBeingHealed();
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
			if(target.getType().maxShields() > 0 && target.getShields() > 0){
				return true;
				// return yes;
				//https://www.youtube.com/watch?v=_un9PYsE1_g
			}
		}
		
		if(what.equals(TechType.Irradiate)){
			return target.isIrradiated();
		}
		
		if(what.equals(TechType.Mind_Control)){
			return target.getPlayer() != game.self();
		}
		
		return false;
		
		
	}
	

	boolean canFriendlyFire(TechType type){
		if(type.equals(TechType.Ensnare) || type.equals(TechType.Plague) || type.equals(TechType.Psionic_Storm) || type.equals(TechType.Nuclear_Strike)){
			return true;
		}
		
		return false;
	}
	
	
	boolean isWorthToCast(Unit caster, Position pos, TechType type, int radius){

		int myScore = 0;
		int enemyScore = 0;
		int max = caster.getType().sightRange();
		WeaponType ouch = type.getWeapon();
		int r = ouch.innerSplashRadius() + ouch.outerSplashRadius();
		if(r == 0){
			r = 60;
		}
		
		if(!this.data.techScores.containsKey(type)){
			this.data.techScores.put(type, type.energyCost());
		}
		
				
		for(Unit unit : game.getUnitsInRadius(pos, radius)){
			
			//till.Print("is Mili: " + this.data.IsMilitrayUnit(unit) );
			

			
			if(game.enemies().contains(unit.getPlayer())){
				

				
					for(Unit a : game.getUnitsInRadius(unit.getPosition(), r)){
						
						if(a == unit){
							continue;
						}
								
						if(!data.IsMilitrayUnit(unit)){
							if(a.getType().isWorker()){
								if(!data.isWorkerDoingTheBad(a)){ // if attacking, moving to attack or repairing
									continue;
								}
							}
							else {
								continue;
							}
						}
						
						// look for units around that one.

						if(ouch.targetsAir() && a.isFlying()){
							continue;
						}
						
						if(ouch.targetsNonBuilding() && a.getType().isBuilding()){
							continue;
						}
						
						if(ouch.targetsOrganic() && !a.getType().isOrganic()){
							continue;
						}
							
						
						if(!targetAlreadyEffected(type, a)){
							enemyScore = enemyScore + data.getScoreOf(a);
						}
					}

				
			}
			else {
				// if good guy
				if(game.allies().contains(unit.getPlayer()) || unit.getPlayer().equals(game.self())){
					for(Unit a : game.getUnitsInRadius(unit.getPosition(), r)){

						if(a == unit){
							continue;
						}
						
						if(ouch.targetsAir() && a.isFlying()){
							continue;
						}
						
						if(ouch.targetsNonBuilding() && a.getType().isBuilding()){
							continue;
						}
						
						if(ouch.targetsOrganic() && !a.getType().isOrganic()){
							continue;
						}
						
						if(game.allies().contains(a.getPlayer()) || a.getPlayer().equals(game.self())){
							if(data.IsMilitrayUnit(a)){
								myScore = myScore + data.getScoreOf(a);
							}
						}
						
						
					}
					
				}
			}
			
			if(enemyScore >= this.data.techScores.get(type) && enemyScore >= myScore){
				return true;
			}
			
			enemyScore = 0;
			myScore = 0;
				
		}
		

		return false;

		
		
	}
	
	
	boolean isWorthToCastSingleTarget(TechType type, Unit target){
		if(!this.data.techScores.containsKey(type)){
			this.data.techScores.put(type, 70);
		}
		int cost = this.data.techScores.get(type);
		int score = data.getScoreOf(target);
		return score >= cost;
	}
	
	boolean isItFuckingWorth(Unit target){
		// https://www.youtube.com/watch?v=vVy9Lgpg1m8
		
		
		if(target.getType().isWorker()){
			return this.data.isInCombat(target);
		}
		
	
		if(target.getType().equals(UnitType.Zerg_Overlord) || target.getType().equals(UnitType.Zerg_Larva) || target.getType().equals(UnitType.Zerg_Egg) ||
		target.getType().equals(UnitType.Zerg_Egg) || target.getType().equals(UnitType.Protoss_Scarab) || target.getType().equals(UnitType.Protoss_Interceptor) || target.getType().equals(UnitType.Protoss_Observer) ||
		target.getType().equals(UnitType.Terran_Vulture_Spider_Mine)){
			// FUCKING BLACKLIST THESE MOTHER FUCKERS
			// FUCKING LAGGERS
			return false;
		}
		
		
		return till.hasWeapons(target.getType());
		
	}
	
	// todo idk 
	
	
	
}
