package crl.action.monster.boss;

import sz.util.Position;
import sz.util.Util;
import crl.action.AT;
import crl.action.Action;
import crl.level.Level;
import crl.monster.Monster;

public class Teleport extends Action {
	
	public AT getID() {
		return AT.BossTeleport;//"TELEPORT";
	}

	public void execute() {
		Level aLevel = performer.level;
		Monster mon = (Monster)performer;
		// FIXME "Unbounded" behaviour.
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