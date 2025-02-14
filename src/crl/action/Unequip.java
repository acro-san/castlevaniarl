package crl.action;

import crl.actor.Actor;
import crl.player.Player;

public class Unequip extends Action{
	public String getID(){
		return "Unequip";
	}
	
	public boolean needsEquipedItem(){
		return true;
	}

	public String getPromptEquipedItem(){
		return "What do you want to unequip?";
	}

	public void execute(){
		Player player = (Player)performer;
		if (player.getArmor() == targetEquipedItem){
			if (player.playerClass == Player.CLASS_VAMPIREKILLER) {
				performer.level.addMessage("You can't abandon the vampire killer armor");
				return;
			}
			player.addItem(player.getArmor());
			player.level.addMessage("You take off your "+targetEquipedItem.getDescription());
			player.setArmor(null);
		}
		if (player.getWeapon() == targetEquipedItem){
			if (player.playerClass == Player.CLASS_VAMPIREKILLER && player.getFlag("ONLY_VK")) {
				performer.level.addMessage("You can't abandon the mystic whip");
				return;
			}
			player.addItem(player.getWeapon());
			player.level.addMessage("You unwield your "+targetEquipedItem.getDescription());
			player.setWeapon(null);
		}
		if (player.getShield() == targetEquipedItem){
			player.addItem(player.getShield());
			player.level.addMessage("You take off your "+targetEquipedItem.getDescription());
			player.setShield(null);
		}
		if (player.getSecondaryWeapon() == targetEquipedItem){
			player.addItem(player.getSecondaryWeapon());
			player.level.addMessage("You draw back your "+targetEquipedItem.getDescription());
			player.setSecondaryWeapon(null);
		}
	}
	
	public boolean canPerform(Actor a) {
		boolean ret = ((Player)a).canCarry();
		if (!ret){
			invalidationMessage = "Your pack is full";
		}
		return ret;
	}
}