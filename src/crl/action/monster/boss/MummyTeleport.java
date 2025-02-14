package crl.action.monster.boss;

import sz.util.Position;
import sz.util.Util;
import crl.action.Action;
import crl.level.Level;
import crl.monster.Monster;

public class MummyTeleport extends Action {
	public String getID(){
		return "MUMMY_TELEPORT";
	}

	public void execute(){
		Level aLevel = performer.level;
		aLevel.addMessage("Akmodan dematerializes! Akmodan flies to you!");
		Monster mon = (Monster)performer;
		// FIXME: this theoretically runs for an unbounded amount of time.
		// instead, it really should whittle down a list of possible destinations
		// randomly.
		do {
			Position dest = Position.add(aLevel.getPlayer().pos, new Position(Util.rand(-2,2),Util.rand(-2,2)));
			if (!aLevel.isWalkable(dest)) {
				continue;
			}
			mon.pos = dest;
			break;
		} while (true);
	}
}