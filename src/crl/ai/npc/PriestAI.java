package crl.ai.npc;

import sz.util.Position;
import sz.util.Util;
import crl.action.Action;
import crl.action.npc.PeaceWalk;
import crl.actor.Actor;
import crl.player.Player;

public class PriestAI extends VillagerAI {
	
	public Action selectAction(Actor who) {
		if (onDanger || attackPlayer) {
			return super.selectAction(who);
		}
		Player p = who.level.getPlayer();
		if (Position.flatDistance(who.pos, p.pos) < 3) {
			if (p.getHP() < p.getHPMax()) {
				p.informPlayerEvent(crl.player.Player.EVT_CHAT, who);
				//who.level.addMessage("The priest says: 'Rest here for a while'");
				p.heal();
			}
		}
		if (Util.chance(10)) {
			Action ret = new PeaceWalk();
			ret.setDirection(Util.rand(0,7));
			return ret;
		}
		return null;
	}

	public String getID() {
		return "HEALPLAYER";
	}
	
}