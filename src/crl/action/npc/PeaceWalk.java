package crl.action.npc;

import sz.util.Position;
import crl.action.AT;
import crl.action.Action;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;

public class PeaceWalk extends Action {
	
	public AT getID() {
		return AT.PeaceWalk;
	}
	
	public boolean needsDirection() {
		return true;
	}

	public void execute() {
		Position var = directionToVariation(targetDirection);
		Position dstPos = Position.add(performer.pos, var);
		Level aLevel = performer.level;
		if (!aLevel.isValidCoordinate(dstPos)) {
			return;
		}
		Cell dstCell = aLevel.getMapCell(dstPos);
		Cell currentCell = aLevel.getMapCell(performer.pos);
		Monster dstMonster = aLevel.getMonsterAt(dstPos);
		//SmartFeature standing = aLevel.getSmartFeature(performer.getPosition());
		if (dstCell == null ||
			dstCell.isSolid() ||	// No ethereal NPCs (passing through stuff)
			dstMonster != null) {
			return;
		}
		// npcs can't/don't walk up/down Z-levels:
		if (currentCell == null || currentCell.getHeight() != dstCell.getHeight()) {
			return;
		}
		// npcs don't swim.
		if (dstCell.isWater() || dstCell.isShallowWater()) {
			return;
		}
		// dont walk/bump into player:
		if (aLevel.getPlayer().pos.equals(dstPos)) {
			return;
		}
		if (dstCell.isEthereal()) {
			performer.pos = aLevel.getDeepPosition(dstPos);
		} else {
			performer.pos = dstPos;
		}
	}

}