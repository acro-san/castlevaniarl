package crl.action.weapon;

import crl.action.AT;
import crl.action.Action;
import crl.action.Attack;
import crl.actor.Actor;
import crl.item.Item;
import crl.level.Level;
import crl.player.Player;

public class EnergyBurst extends Action {
	
	public AT getID() {
		return AT.EnergyBurst;
	}
	
	public boolean needsDirection() {
		return true;
	}

	public String getPromptDirection() {
		return "Where do you want to fire at?";
	}

	public void execute() {
		Player p = (Player)performer;
		Level aLevel = p.level;
		Item wp = p.weapon;
		if (wp == null || wp.getReloadTurns() == 0) {
			aLevel.addMessage("This will only work with ranged weapons!");
			return;
		}
		if (!checkHearts(10)) {
			aLevel.addMessage("You need more power!");
			return;
		}
		
		int shots = wp.getRemainingTurnsToReload();
		Attack atk = new Attack();
		atk.setDirection(targetDirection);
		atk.setPerformer(performer);
		
		for (int i = 0; i < shots; i++) {
			atk.execute();
		}
	}
	
	
	public boolean canPerform(Actor a) {
		Player p = (Player) a;
		if (p.getHearts() < 10) {
			invalidationMessage = "You need more energy!";
			return false;
		}
		Item wp = p.weapon;
		if (wp == null || wp.getReloadTurns() == 0) {
			invalidationMessage = "This will only work with ranged weapons!";
			return false;
		}
		return true;
	}
	
}