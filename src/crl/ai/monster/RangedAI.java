package crl.ai.monster;

import java.util.Iterator;

import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.action.Action;
import crl.action.monster.MonsterWalk;
import crl.actor.Actor;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.monster.Monster;

public class RangedAI extends MonsterAI {
	
	private int approachLimit = 5;

	public Action selectAction(Actor who) {
		Monster aMonster = (Monster) who;
		int directionToPlayer = aMonster.starePlayer();
		int playerDistance = Position.flatDistance(aMonster.pos, aMonster.level.getPlayer().pos);
		if (directionToPlayer == -1){
			//A stationary here would do nothing
			int direction = Util.rand(0,7);
			Action ret = new MonsterWalk();
			ret.setDirection(direction);
			return ret;
		}
		if (playerDistance < approachLimit) {
			// get away from player
			Action ret = new MonsterWalk();
			int direction = Action.toIntDirection(Position.mul(Action.directionToVariation(directionToPlayer), -1));
			ret.setDirection(direction);
			return ret;
		} else {
			//Randomly decide if will approach the player or attack
			if (rangedAttacks != null && Util.chance(80)){
				//Try to attack the player
				for (Iterator iter = rangedAttacks.iterator(); iter.hasNext();) {
					RangedAttack element = (RangedAttack) iter.next();
					if (element.getRange() >= playerDistance && Util.chance(element.getFrequency())){
						//Perform the attack
						Action ret = Main.getAction(element.getAttackId());
						ret.setDirection(directionToPlayer);
						ret.setPosition(aMonster.level.getPlayer().pos);
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
		return AIT.RANGED_AI;
	}

	public void setApproachLimit(int limit) {
		approachLimit = limit;
	}

	
}