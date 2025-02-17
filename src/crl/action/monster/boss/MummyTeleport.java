package crl.action.monster.boss;

import sz.util.Position;
import sz.util.Util;
import crl.action.AT;
import crl.action.Action;
import crl.level.Level;
import crl.monster.Monster;

public class MummyTeleport extends Action {
	
	public AT getID() {
		return AT.MummyTeleport;
	}

	public void execute() {
		Level aLevel = performer.level;
		aLevel.addMessage("Akmodan dematerializes! Akmodan flies to you!");
		Monster mon = (Monster)performer;
		// FIXME: this theoretically runs for unbounded time.
		// instead, it should whittle down a list of possible destinations
		// randomly.
		// AND: What'd happen if the player stood in the middle of a 5x5 area
		// where player's in middle, and all the outer squares are unwalkable?
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