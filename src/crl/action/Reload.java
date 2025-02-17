package crl.action;

import crl.actor.Actor;
import crl.item.Item;
import crl.item.ItemDefinition;
import crl.player.Player;

public class Reload extends Action {
	private transient Item weapon;	// ...
	public int getCost() {
		if (weapon != null)
			return 10 * weapon.getDefinition().reloadCostGold;
		else
			return 50;
	}

	public AT getID() {
		return AT.Reload;
	}
	
	
	public void execute() {
		Player p = (Player)performer;
		weapon = p.weapon;
		if (weapon == null) {
			p.level.addMessage("You can't reload yourself");
			return;
		}
		
		ItemDefinition wdef = weapon.getDefinition();
		if (wdef.isSingleUse || p.getGold() < wdef.reloadCostGold) {
			p.level.addMessage("You can't reload the " + weapon.getDescription());
			return;
		}
		
		weapon.reload();
		p.reduceGold(wdef.reloadCostGold);
		p.reduceHearts(1);
		p.level.addMessage("You reload the " + weapon.getDescription() +
			" ("+wdef.reloadCostGold+" gold)");
		
	}
	
	
	public boolean canPerform(Actor a) {
		Player aPlayer = getPlayer(a);
		// Item 
		weapon = aPlayer.weapon;
		if (weapon == null) {
			invalidationMessage = "You can't reload yourself";
			return false;
		}
		
		if (weapon.getReloadTurns() <= 0) {
			invalidationMessage = "The " + weapon.getDescription()+" cannot be reloaded";
			return false;
		}
		if (aPlayer.getGold() < weapon.getDefinition().reloadCostGold) {
			invalidationMessage = "You need "+weapon.getDefinition().reloadCostGold+" gold to reload the " + weapon.getDescription();
			return false;
		}

		if (aPlayer.getHearts() > 0) {
			return true;
		} else {
			invalidationMessage = "You need soul power to reload the " + weapon.getDescription();
			return false;
		}
	}

}