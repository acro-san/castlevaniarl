package crl.ai.monster;

import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.action.Action;
import crl.action.monster.MonsterWalk;
import crl.action.monster.Swim;
import crl.actor.Actor;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.monster.Monster;

public class UnderwaterAI extends MonsterAI {
	/** Dwells in the water swimming until he spots the player, swims to him
	 * and Jumps out of the water. walks to it and fires */
	public Action selectAction(Actor who) {
		Debug.doAssert(who instanceof Monster, "Underwater AI selectAction");
		Monster aMonster = (Monster)who;
	//	Player aPlayer = who.level.getPlayer();
		int directionToPlayer = aMonster.starePlayer();
		if (directionToPlayer == -1){
			//The player is somewhere
			if (!aMonster.waitsPlayer()){
				int direction = Util.rand(0,7);
				if (aMonster.isInWater()) {
					Action ret = new Swim();
					ret.setDirection(direction);
					return ret;
				} else {
					Action ret = new MonsterWalk();
					ret.setDirection(direction);
					return ret;
				}
			} else {
				return null;
			}
		} else {
			/*if (aMonster.isInWater() && !aPlayer.isSwimming()){
                Action ret = new Swim();
	            ret.setDirection(directionToPlayer);
		     	return ret;
			}*/
			if (rangedAttacks != null) {
				int distanceToPlayer = Position.flatDistance(aMonster.pos, aMonster.level.getPlayer().pos);
				//Try to attack
				for (int i = 0; i < rangedAttacks.size(); i++){
					RangedAttack ra = (RangedAttack)rangedAttacks.elementAt(i);
					if (distanceToPlayer <= ra.getRange()) {
						if (Util.chance(ra.getFrequency())){
							Action ret = Main.getAction(ra.getAttackId());
							ret.setDirection(directionToPlayer);
							return ret;
						}
					}
				}
			}
			// Couldnt attack... walk or swim to the player
			if (aMonster.isInWater()) {
				Action ret = new Swim();
				ret.setDirection(directionToPlayer);
				return ret;
			} else {
				Action ret = new MonsterWalk();
				ret.setDirection(directionToPlayer);
				return ret;
			}
		}
	}

	public AIT getID() {
		return AIT.UNDERWATER;
	}

}