package crl.ai.monster;

import java.util.Iterator;

import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.action.Action;
import crl.actor.Actor;
import crl.ai.ActionSelector;
import crl.monster.Monster;

public class StationaryAI extends MonsterAI {
	
	public Action selectAction(Actor who) {
		Monster aMonster = (Monster)who;
		int directionToPlayer = aMonster.starePlayer();
		int playerDistance = Position.flatDistance(aMonster.pos, aMonster.level.getPlayer().pos);
		if (directionToPlayer == -1) {
			return null;
		}
		//Randomly decide if will approach the player or attack
		if (rangedAttacks != null && Util.chance(80)) {
			// Try to attack the player
			for (Iterator<RangedAttack> iter = rangedAttacks.iterator(); iter.hasNext();) {
				RangedAttack element = iter.next();
				if (element.getRange() >= playerDistance && Util.chance(element.getFrequency())) {
					// Perform the attack
					Action ret = Main.getAction(element.getAttackId());
					ret.setDirection(directionToPlayer);
					return ret;
				}
			}
		}
		// Couldnt attack the player, so do nothing
		return null;
	}


	public String getID() {
		return "STATIONARY_AI";
	}

	public ActionSelector derive() {
		try {
			return (ActionSelector)clone();
		} catch (CloneNotSupportedException cnse) {
			return null;
		}
	}
}