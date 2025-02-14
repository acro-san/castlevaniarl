package crl.action.weapon;

import crl.action.Action;
import crl.action.Attack;
import crl.actor.Actor;
import crl.item.Item;
import crl.level.Level;
import crl.player.Player;

public class EnergyBurst extends Action {
	
	public String getID(){
		return "ENERGY_BURST";
	}
	
	public boolean needsDirection(){
		return true;
	}

	public String getPromptDirection(){
		return "Where do you want to fire at?";
	}

	public void execute() {
		Player aPlayer = (Player)performer;
		Level aLevel = aPlayer.level;
		Item wp = aPlayer.getWeapon();
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
		Player aPlayer = (Player) a;
		if (aPlayer.getHearts() < 10) {
			invalidationMessage = "You need more energy!";
			return false;
		}
		Item wp = aPlayer.getWeapon();
		if (wp == null || wp.getReloadTurns() == 0) {
			invalidationMessage = "This will only work with ranged weapons!";
			return false;
		}
		return true;
	}
	
}