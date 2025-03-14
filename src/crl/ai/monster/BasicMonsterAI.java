package crl.ai.monster;

import java.util.Iterator;

import sz.util.OutParameter;
import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.action.Action;
import crl.action.monster.MonsterCharge;
import crl.action.monster.MonsterMissile;
import crl.action.monster.MonsterWalk;
import crl.action.monster.SummonMonster;
import crl.action.monster.Swim;
import crl.actor.Actor;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.monster.Monster;
import crl.player.Consts;

/**
 * Multifunctional AI Module
 * @author Slash
 *
 */
public class BasicMonsterAI extends MonsterAI {

	/** Defines whether the monster never moves */
	private boolean isStationary;
	
	/**
	 * Determines if the monster waits for the player
	 * to get nearby than x tiles before attacking
	 */
	private int waitPlayerRange;
	
	/**
	 * Defines if the monster wants to keep away from the 
	 * player to a certain distance
	 */
	private int approachLimit = 0;
	
	/**
	 * Defines if the player patrols an area, and when he
	 * must leave his patrol range to attack a player
	 */
	private int patrolRange = 0;
	
	/**
	 * Keeps track of the charge count for attacks which
	 * need charge
	 * FREQUENTLY DUPLICATED STATE BY VARIOUS BOSS AIs...
	 */
	private int chargeCounter = 0;
	
	/** Tracks the last direction the monster walked */
	private int lastDirection = -1;
	
	/**
	 * Keeps track if the monster must change direction
	 * Used mainly for patrolling monsters
	 */
	private boolean changeDirection;
	

	public Action selectAction(Actor who) {
		Monster aMonster = (Monster)who;
		
		if (chargeCounter > 0) {
			chargeCounter--;
		}
		
		// If monster has an enemy, check if he is still at the level
		if (aMonster.enemy != null) {
			if (!aMonster.level.getMonsters().contains(aMonster.enemy)) {
				aMonster.enemy = null;
			}
		}
		// If monster has an enemy, or is charmed
		if (aMonster.enemy != null || aMonster.hasCounter(Consts.C_MONSTER_CHARM)) {
			// Stare at your enemy or pick a enemy from nearby monsters
			int directionToMonster = -1;
			if (aMonster.enemy != null) {
				directionToMonster = aMonster.stareMonster(aMonster.enemy);
			} else {
				directionToMonster = aMonster.stareMonster();
			}
			// If you found no enemy
			if (directionToMonster == -1) {
				//Stare to the player
				directionToMonster = aMonster.starePlayer();
				// If you didnt find the player, do nothing
				if (directionToMonster == -1){
					return null;
				} else {
					// Ensure we are not bumping the player
					Position targetPositionX = Position.add(who.pos, Action.directionToVariation(directionToMonster));
					if (who.level.getPlayer().pos.equals(targetPositionX)){
						return null;
					} else {
						return tryWalking(aMonster, directionToMonster);
					}
				}
			} else { // Found your enemy
				// If you are stationary, do nothing
				if (isStationary){
					return null;
				} else {
					return tryWalking(aMonster, directionToMonster);
				}
			} 
		}
		// else, monster has no enemy, and is not charmed
		// Stare to the player
		int directionToPlayer = aMonster.starePlayer();
		int playerDistance = Position.flatDistance(aMonster.pos, aMonster.level.getPlayer().pos);
		// If monster is a patroller, and player distance is bigger than patrol range, continue patrolling 
		if (patrolRange >0 && playerDistance > patrolRange){
			if (lastDirection == -1 || changeDirection){
				lastDirection = Util.rand(0,7);
				changeDirection = false;
			}
			return tryWalking(aMonster, lastDirection);
		}
		// monster is not a patroller
		// If monster didn't see the player
		if (directionToPlayer == -1) {
			// If is stationary or semistationary, do nothing
			if (isStationary || waitPlayerRange > 0) {
				return null;
			} else {
				// Else walk randomly
				int direction = Util.rand(0,7);
				return tryWalking (aMonster, direction);
			}
		} else {
			// else, the monster saw the player
			// If is stationary or semistationary and the player is still too far, do nothing
			if (waitPlayerRange > 0 && playerDistance > waitPlayerRange){
				return null;
			}
			
			// If monster has an approach limit, and player is closer than it
			if (playerDistance < approachLimit){
				// Get away from player
				int direction = Action.toIntDirection(Position.mul(Action.directionToVariation(directionToPlayer), -1));
				return tryWalking(aMonster, direction);
			} else {
				// else, just attack the player
				// If monster is on the water, swim to the player 
				if (aMonster.canSwim() && aMonster.isInWater() && !aMonster.level.getPlayer().isSwimming()){
					return tryWalking(aMonster, directionToPlayer);
				}
				// If monster sees the player and has attacks, may be try attacking the player
				if (aMonster.seesPlayer() && rangedAttacks != null){
					
					//Pick an attack from those available
					otherAttack: for (RangedAttack element: rangedAttacks) {
						// If the player is on attack range, and is either a direct attack or we are at the same height as the player, and randomly
						if (element.getRange() >= playerDistance &&
								Util.chance(element.getFrequency()) &&
								(element.getAttackType() == MonsterMissile.TYPE_DIRECT ||
								(element.getAttackType() != MonsterMissile.TYPE_DIRECT && aMonster.getStandingHeight() == aMonster.level.getPlayer().getStandingHeight())
							))
						{
							Action ret = Main.getAction(element.getAttackId());
							// If attacks needs charge, ensure I have charge, else try another attack
							if (element.getChargeCounter() > 0){
								if (chargeCounter > 0){
									continue otherAttack;
								} else {
									//Prepare to charge again, but try to execute the attack
									chargeCounter = element.getChargeCounter();
								}
							}
							
							// Configure attack according to its type
							if (ret instanceof MonsterMissile){
								((MonsterMissile)ret).set(
										element.getAttackType(),
										element.getStatusEffect(),
										element.getRange(),
										element.getAttackMessage(),
										element.getEffectType(),
										element.getEffectID(),
										element.getDamage(),
										element.getEffectWav()
										);
							}else if (ret instanceof MonsterCharge){
								((MonsterCharge)ret).set(element.getRange(), element.getAttackMessage(), element.getDamage(),element.getEffectWav());
							}else if (ret instanceof SummonMonster){
								((SummonMonster)ret).set(element.getSummonMonsterId(), element.getAttackMessage());
							}
							// Set the player position as the attack target
							ret.setPosition(who.level.getPlayer().getPreviousPosition());
							return ret;
						}
					}
				}
				
				// Didn't try to attack the player, so try to walk to him
				return tryWalking(aMonster, directionToPlayer);
			}
		}
	}
	
	private Action tryWalking(Monster aMonster, int direction) {
		if (isStationary) {
			return null;
		} else {
			Action ret = null;
			// Check if must swim or walk
			if (aMonster.canSwim() && aMonster.isInWater()) {
				ret = new Swim();
			} else {
				ret = new MonsterWalk();
			}
			
			// Check if can walk toward direction directly or in approximate direction
			OutParameter direction1 = new OutParameter();
			OutParameter direction2 = new OutParameter();
			fillAlternateDirections(direction1, direction2, direction);
			if (canWalkTowards(aMonster, direction)) {
				ret.setDirection(direction);
			} else if (canWalkTowards(aMonster, direction1.getIntValue())) {
				ret.setDirection(direction1.getIntValue());
			} else if (canWalkTowards(aMonster, direction2.getIntValue())) {
				ret.setDirection(direction2.getIntValue());
			} else {
				// Can't walk toward direction directly, so just bump around
				ret.setDirection(Util.rand(0,7));
			}
			return ret;
		}
	}
	
	/**
	 * Returns vectors adjacent to a general direction
	 * 
	 * Example:
	 *  If general direction is Left, will return Up-Left and Down-Left
	 *  
	 *  O--
	 *  <@-
	 *  O--
	 *  
	 *  If general direction is Down-Right, will return Down and Right
	 *  
	 *  ---
	 *  -@O
	 *  -OJ
	 *  
	 * @param direction1 Outparameter to be filled with one the adjacent directions
	 * @param direction2 Outparameter to be filled with one the adjacent directions
	 * @param generalDirection The general direction one of Action.UP, DOWN, LEFT, RIGHT, UPRIGHT, UPLEFT, DOWNRIGHT or DOWNLEFT
	 */
	private void fillAlternateDirections(OutParameter direction1, OutParameter direction2, int generalDirection){
		Position var = Action.directionToVariation(generalDirection);
		Position d1 = null;
		Position d2 = null;
		if (var.x == 0){
			d1 = new Position(-1, var.y);
			d2 = new Position(1, var.y);
		} else if (var.y == 0){
			d1 = new Position(var.x, -1);
			d2 = new Position(var.x, 1);
		} else {
			d1 = new Position(var.x, 0);
			d2 = new Position(0, var.y);
		}
		direction1.setIntValue(Action.toIntDirection(d1));
		direction2.setIntValue(Action.toIntDirection(d2));
	}
	
	/**
	 * Defines if a monster can walk toward a direction
	 * 
	 * @param aMonster
	 * @param direction
	 * @return
	 */
	private boolean canWalkTowards(Monster aMonster, int direction) {
		Position destination = Position.add(aMonster.pos, Action.directionToVariation(direction));
		if (!aMonster.level.isValidCoordinate(destination)) {
			return false;
		}
		if (aMonster.level.getMonsterAt(destination) != null) {
			return false;
		}
		if (aMonster.level.isAir(destination)) {
			return (aMonster.isEthereal() || aMonster.isFlying());
		}
		if (!aMonster.level.isWalkable(destination)) {
			return aMonster.isEthereal();
		} else {
			return true;
		}
	}


	public AIT getID() {
		return AIT.BASIC_MONSTER_AI;
	}


	public void setApproachLimit(int limit) {
		approachLimit = limit;
	}
	
	public void setWaitPlayerRange(int limit) {
		waitPlayerRange = limit;
	}
	
	public void setPatrolRange(int limit) {
		patrolRange = limit;
	}
	
	public int getPatrolRange() {
		return patrolRange;
	}

	public void setStationary(boolean isStationary) {
		this.isStationary = isStationary;
	}

	public void setChangeDirection(boolean value) {
		changeDirection = value;
	}
	
}