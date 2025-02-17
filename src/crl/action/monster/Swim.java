package crl.action.monster;

import sz.util.Debug;
import sz.util.Position;
import crl.action.AT;
import crl.action.Action;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Damage;

public class Swim extends Action {
	
	public AT getID() {
		return AT.Swim;
	}
	
	public boolean needsDirection() {
		return true;
	}

	public void execute() {
		Debug.doAssert(performer instanceof Monster, "The player tried to Swim...");
		Monster mon = (Monster)performer;
		Position var = directionToVariation(targetDirection);
		Position destinationPoint = Position.add(performer.pos, var);
		Level aLevel = performer.level;
		Cell destinationCell = aLevel.getMapCell(destinationPoint);
		Monster destinationMonster = aLevel.getMonsterAt(destinationPoint);
		if (destinationCell != null && !destinationCell.isSolid()){
			if (destinationCell.isShallowWater() || destinationCell.isWater()){
				if (destinationMonster == null)
					performer.pos = destinationPoint;
			} else {
				if (destinationMonster == null){
					aLevel.addMessage("A "+mon.getDescription()+" jumps out of the water!", destinationPoint);
					performer.pos = destinationPoint;
				}
			}
		}

		if (mon.getAttack() > 0 &&
			aLevel.getPlayer().pos.equals(destinationPoint) &&
			aLevel.getPlayer().getStandingHeight() == mon.getStandingHeight()) {
			// Damage the poor player and bounce him back
			if (aLevel.getPlayer().damage("The "+mon.getDescription()+" hits you with his jump!", mon, new Damage(mon.getAttack(), false)))
				aLevel.getPlayer().bounceBack(var,1);
		}
	}

}