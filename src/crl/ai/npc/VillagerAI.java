package crl.ai.npc;

import sz.util.Position;
import sz.util.Util;
import crl.action.Action;
import crl.action.monster.MonsterWalk;
import crl.action.npc.PeaceWalk;
import crl.actor.Actor;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.level.Cell;
import crl.level.Level;
import crl.npc.NPC;

public class VillagerAI extends ActionSelector {
	
	protected boolean onDanger;
	protected boolean attackPlayer;

	public Action selectAction(Actor who) {
		NPC aNPC = (NPC)who;
		Level lvl = who.level;
		if (attackPlayer && aNPC.hp > 1) {
			if (Util.chance(10)) {
				lvl.addMessage("The "+who.getDescription()+" yells: '"+aNPC.getAngryMessage()+"'", who.pos);
			}
			lvl.signal(who.pos, 5, "ATTACK_PLAYER");
			int directionToPlayer = aNPC.starePlayer();
			if (directionToPlayer != -1){
				Action ret = new MonsterWalk();
				ret.setDirection(directionToPlayer);
				return ret;
			}
		} else if (onDanger) {
			lvl.addMessage("The "+who.getDescription()+" yells: '"+aNPC.getDangerMessage()+"'", who.pos);
			lvl.signal(who.pos, 7, "ATTACK_PLAYER");
			int directionToPlayer = aNPC.starePlayer();
			if (directionToPlayer != -1){
				Action ret = new MonsterWalk();
				ret.setDirection(Action.toIntDirection(Position.mul(Action.directionToVariation(directionToPlayer), -1)));
				return ret;
			}
		} else {
			if (Util.chance(30)) {
				Action ret = new PeaceWalk();
				int tries = 100;
				while (tries > 0) {
					int direction = Util.rand(0,7);
					Position destinationPoint = Position.add(aNPC.pos, Action.directionToVariation(direction));
					Cell cell = lvl.getMapCell(destinationPoint);
					if (cell == null) {
						tries--;
						continue;
					}
					if (cell.isSolid() || cell.isStair()) {
						tries--;
						continue;
					}
					if (cell.getID().startsWith("TOWN_DOOR")) {
						tries--;
						continue;
					}
					if (cell.getID().startsWith("AIR")) {
						tries--;
						continue;
					}
					ret.setDirection(direction);
					return ret;
				}
				return null;
			} else {
				return null;
			}
		}
		return null;
	}

	public AIT getID() {
		return AIT.VILLAGER;
	}


	public void setOnDanger(boolean value) {
		onDanger = value;
	}

	public void setAttackPlayer(boolean value) {
		attackPlayer = value;
	}

	public boolean isHostile() {
		return attackPlayer || onDanger;
	}
}