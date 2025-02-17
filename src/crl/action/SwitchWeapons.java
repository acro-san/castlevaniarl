package crl.action;

import crl.actor.Actor;
import crl.item.Item;
import crl.player.Player;

public class SwitchWeapons extends Action {
	
	public int getCost() {
		return 25;
	}

	public AT getID() {
		return AT.SwitchWeapons;
	}
	
	@Override
	public void execute() {
		Player p = (Player)performer;
		Item secondary = p.secondaryWeapon;
		if (secondary == null) {
			/*aPlayer.level.addMessage("You don't have a secondary weapon");
			return;*/
			Item primary = p.weapon;
			p.weapon = null;
			p.level.addMessage("You attack unarmed");
			if (primary != null) {
				p.secondaryWeapon = primary;
			}
			return;
		}
		Item primary = p.weapon;
		p.weapon = secondary;
		if (primary != null) {
			p.secondaryWeapon = primary;
			p.level.addMessage("You switch your "+primary.getDescription()+" for your "+secondary.getDescription());
		} else {
			p.secondaryWeapon = null;
			p.level.addMessage("You equip your "+secondary.getDescription());
		}
 	}
	
	public boolean canPerform(Actor a) {
		/*Player aPlayer = (Player) a;
		Item secondary = aPlayer.getSecondaryWeapon();
		if (secondary == null){
			invalidationMessage = "You don't have a secondary weapon";
			return false;
		}*/
		return true;
	}
}