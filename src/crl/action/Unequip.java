package crl.action;

import crl.actor.Actor;
import crl.player.Player;

public class Unequip extends Action {
	
	public AT getID() {
		return AT.Unequip;
	}
	
	public boolean needsEquipedItem() {
		return true;
	}

	public String getPromptEquipedItem() {
		return "What do you want to unequip?";
	}

	@Override
	public void execute() {
		Player player = (Player)performer;
		assert(targetEquipedItem != null);
		
		if (targetEquipedItem == player.armor) {
			if (player.playerClass == Player.CLASS_VAMPIREKILLER) {
				performer.level.addMessage("You can't abandon the vampire killer armor");
				return;
			}
			player.addItem(player.armor);
			player.level.addMessage("You take off your "+targetEquipedItem.getDescription());
			player.armor = null;
		} else if (targetEquipedItem == player.weapon) {
			if (player.playerClass == Player.CLASS_VAMPIREKILLER && player.getFlag("ONLY_VK")) {
				performer.level.addMessage("You can't abandon the mystic whip");
				return;
			}
			player.addItem(player.weapon);
			player.weapon = null;
			player.level.addMessage("You stop wielding your "+targetEquipedItem.getDescription());
		} else if (targetEquipedItem == player.shield) {
			player.addItem(player.shield);
			player.shield = null;
			player.level.addMessage("You take off your "+targetEquipedItem.getDescription());
		} else if (targetEquipedItem == player.secondaryWeapon) {
			player.addItem(player.secondaryWeapon);
			player.secondaryWeapon = null;
			player.level.addMessage("You draw back your "+targetEquipedItem.getDescription());
		}
	}
	
	
	public boolean canPerform(Actor a) {
		boolean ret = ((Player)a).canCarry();
		if (!ret) {
			invalidationMessage = "Your pack is full";
		}
		return ret;
	}
}