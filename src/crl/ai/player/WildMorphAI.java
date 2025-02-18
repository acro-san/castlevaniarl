package crl.ai.player;


import sz.util.Position;
import sz.util.Util;
import crl.action.Action;
import crl.action.Attack;
import crl.action.Walk;
import crl.actor.Actor;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.player.Player;

public class WildMorphAI extends ActionSelector {
	
	public Action selectAction(Actor who) {
		Player p = (Player)who;
		if (p.getEnemy() != null) {
			if (!p.level.getMonsters().contains(p.getEnemy())) {
				p.setEnemy(null);
			}
		}
		
		int directionToMonster = -1;
		if (p.getEnemy() != null) {
			directionToMonster = p.stareMonster(p.getEnemy());
		} else {
			directionToMonster = p.stareMonster();
		}
		
		if (directionToMonster == -1) {
			return null;
		}

		Position destination = Position.add(who.pos, Action.directionToVariation(directionToMonster));
		if (p.level.getMonsterAt(destination) != null) {
			p.setEnemy(p.level.getMonsterAt(destination));
		}
		if (p.getEnemy() != null && destination.equals(p.getEnemy().pos)) {
			Action ret = new Attack();
			ret.setPerformer(p);
			ret.setDirection(directionToMonster);
			return ret;
		} else {
			Action ret = new Walk();
			if (!who.level.isWalkable(Position.add(who.pos, Action.directionToVariation(directionToMonster)))) {
				directionToMonster = Util.rand(0,7);
				ret.setDirection(directionToMonster);
			} else {
				ret.setDirection(directionToMonster);
			}
			return ret;
		}
	}


	public AIT getID() {
		return AIT.WILD_MORPH_AI;
	}

}