package crl.action.weapon;

import sz.util.Position;
import crl.action.AT;
import crl.action.Action;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class SpinningSlice extends Action {
	
	public AT getID() {
		return AT.SpinningSlice;
	}
	
	public void execute() {
		Player p = (Player)performer;
		Level aLevel = performer.level;
		if (!checkHearts(8)) {
			return;
		}
		if (p.weapon == null) {
			aLevel.addMessage("You can't slice without a proper weapon");
			return;
		}

		Position runner = new Position(p.pos.x-1, p.pos.y-1, p.pos.z);
		for (; runner.x <= p.pos.x+1; runner.x++) {
			for (; runner.y <= p.pos.y+1; runner.y++) {
				Cell destinationCell = aLevel.getMapCell(runner);
				//aLevel.addBlood(runner, 8);
				if (destinationCell == null) {
					continue;
				}
				hit(runner, aLevel, p);
			}
			runner.y = p.pos.y-1;
		}
	}


	private void hit(Position destinationPoint, Level aLevel, Player p) {
		StringBuffer message = new StringBuffer();
		Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
		if (destinationFeature != null && destinationFeature.isDestroyable()) {
			message.append("You slice the "+destinationFeature.getDescription());
			Feature prize = destinationFeature.damage(p, p.weapon.getAttack());
			if (prize != null) {
				message.append(", and cut it apart!");
			}
			aLevel.addMessage(message.toString());
		}
		
		Monster targetMonster = performer.level.getMonsterAt(destinationPoint);
		message = new StringBuffer();
		if (targetMonster != null &&
			!(targetMonster.isInWater() && targetMonster.canSwim()) )
		{
			message.append("You slice the "+targetMonster.getDescription());
			targetMonster.damageWithWeapon(message, p.getWeaponAttack()+p.getAttack());
			if (targetMonster.isDead()) {
				message.append(", and cut it apart!");
				//performer.level.removeMonster(targetMonster);
			}
			if (targetMonster.wasSeen()) {
				aLevel.addMessage(message.toString());
			}
		}
	}
	
	
	public boolean canPerform(Actor a) {
		Player aPlayer = (Player) a;
		if (aPlayer.getHearts() < 8) {
			invalidationMessage = "You need more energy!";
			return false;
		}
		return true;
	}
	
}