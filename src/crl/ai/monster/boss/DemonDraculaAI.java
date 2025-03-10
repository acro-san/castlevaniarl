package crl.ai.monster.boss;

import java.util.Iterator;

import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.action.Action;
import crl.action.monster.MonsterCharge;
import crl.action.monster.MonsterMissile;
import crl.action.monster.MonsterWalk;
import crl.action.monster.SummonMonster;
import crl.action.monster.boss.ShadowApocalypse;
import crl.actor.Actor;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.ai.monster.MonsterAI;
import crl.ai.monster.RangedAttack;
import crl.monster.Monster;

public class DemonDraculaAI extends MonsterAI {

///	private int approachLimit = 0;
	private int chargeCounter = 99;
	
	public Action selectAction(Actor who) {
		Monster aMonster = (Monster) who;
		int directionToPlayer = aMonster.starePlayer();
		int playerDistance = Position.flatDistance(aMonster.pos, aMonster.level.getPlayer().pos);
		if (directionToPlayer == -1) {
			int direction = Util.rand(0,7);
			Action ret = new MonsterWalk();
			ret.setDirection(direction);
			return ret;
		} else {
			//Randomly decide if will approach the player or attack
			if (Util.chance(70)) {
				if (playerDistance < 5) {
					if (Util.chance(70))
						return new ShadowApocalypse();
					else{
						Action ret = new MonsterWalk();
						int direction = Action.toIntDirection(Position.mul(Action.directionToVariation(directionToPlayer), -1));
						ret.setDirection(direction);
						return ret;
					}
				}
			} else if (rangedAttacks != null && Util.chance(80)) {
				//Try to attack the player
				for (Iterator iter = rangedAttacks.iterator(); iter.hasNext();  ) {
					RangedAttack element = (RangedAttack) iter.next();
					if (element.getRange() >= playerDistance && Util.chance(element.getFrequency())) {
						//Perform the attack
						Action ret = Main.getAction(element.getAttackId());
						if (element.getChargeCounter() > 0) {
							if (chargeCounter == 0) {
								chargeCounter = element.getChargeCounter();
							} else {
								chargeCounter --;
								continue;
							}
						}
						
						if (ret instanceof MonsterMissile) {
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
						ret.setPosition(who.level.getPlayer().pos);
						return ret;
					}
				}
			}
			// Couldnt attack the player, so walk to him
			Action ret = new MonsterWalk();
			ret.setDirection(directionToPlayer);
			return ret;
		}
	}


	public AIT getID() {
		return AIT.DEMON_DRACULA_AI;
	}


}