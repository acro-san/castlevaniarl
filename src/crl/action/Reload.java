package crl.action;

import crl.actor.Actor;
import crl.item.Item;
import crl.player.Player;

public class Reload extends Action{
	private transient Item weapon;
	public int getCost() {
		if (weapon != null)
			return 10 * weapon.getDefinition().reloadCostGold;
		else
			return 50;
	}

	public String getID(){
		return "Reload";
	}
	
	public void execute(){
		Player aPlayer = (Player) performer;
		weapon = aPlayer.getWeapon();
		if (weapon != null){
			if (weapon.getDefinition().isSingleUse){
				aPlayer.level.addMessage("You can't reload the " + weapon.getDescription());
			} else if (aPlayer.getGold() < weapon.getDefinition().reloadCostGold)
				aPlayer.level.addMessage("You can't reload the " + weapon.getDescription());
			else {
				weapon.reload();
				aPlayer.reduceGold(weapon.getDefinition().reloadCostGold);
				aPlayer.reduceHearts(1);
				aPlayer.level.addMessage(
					"You reload the " + weapon.getDescription()+
					" ("+weapon.getDefinition().reloadCostGold+" gold)");
			}
		} else
			aPlayer.level.addMessage("You can't reload yourself");
 	}
	
	public boolean canPerform(Actor a){
		Player aPlayer = getPlayer(a);
		Item weapon = aPlayer.getWeapon();
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