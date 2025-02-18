package crl.ai.monster.boss;

import sz.util.Position;
import sz.util.Util;
import crl.action.Action;
import crl.action.monster.MonsterWalk;
import crl.action.monster.boss.SummonSnakes;
import crl.actor.Actor;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.ai.monster.MonsterAI;
import crl.monster.Monster;

public class MedusaAI extends MonsterAI {
	
	private int powerCounter = 5;
	private boolean powerActive;
	
	public Action selectAction(Actor who) {
		Monster aMonster = (Monster) who;
		int playerDistance = Position.flatDistance(aMonster.pos, aMonster.level.getPlayer().pos);
		if (playerDistance > 20) {
			powerActive = false;
			powerCounter = 15;
		} else {
			powerActive = true;
		}
		
		if (powerActive) {
			if (powerCounter < 0) {
				powerCounter = 15;
				return new SummonSnakes();
			} else {
				powerCounter--;
			}
		}
		int directionToPlayer = aMonster.starePlayer();
		
		if (directionToPlayer == -1) {
			//A stationary here would do nothing
			int direction = Util.rand(0,7);
			Action ret = new MonsterWalk();
			ret.setDirection(direction);
			return ret;
		}
		if (powerCounter > 3) {
			// walk randomly
			Action ret = new MonsterWalk();
			ret.setDirection(Util.rand(0,7));
			return ret;
		} else {
			// Walk to the player
			Action ret = new MonsterWalk();
			ret.setDirection(directionToPlayer);
			return ret;
		}
	}


	public AIT getID() {
		return AIT.MEDUSA_AI;
	}


}