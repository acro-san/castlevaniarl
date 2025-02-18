package crl.action.monster.boss;

import sz.util.Position;
import crl.action.Action;
import crl.action.monster.MonsterWalk;
import crl.actor.Actor;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.ai.monster.MonsterAI;
import crl.level.Cell;
import crl.monster.Monster;

public class SickleAI extends MonsterAI {
	
	public Action selectAction(Actor who) {
		Monster mon = (Monster)who;
		int directionToPlayer = mon.starePlayer();
		if (directionToPlayer == -1) {
			return null;
		}

		int distanceToPlayer = Position.flatDistance(mon.pos, mon.level.getPlayer().pos);
		if (distanceToPlayer > 20) {
			return null;
		}
		Action ret = new MonsterWalk();
		ret.setDirection(directionToPlayer);
		Cell currentCell = mon.level.getMapCell(mon.pos);
		Cell destinationCell = mon.level.getMapCell(
			Position.add(
				mon.pos,
				Action.directionToVariation(directionToPlayer)
			));

		if (destinationCell == null || currentCell == null){
			return null;
		}
		return ret;
	
	}

	public AIT getID() {
		return AIT.SICKLE_AI;
	}


}