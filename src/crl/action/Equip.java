package crl.action;

import crl.actor.Actor;
import crl.item.Item;
import crl.item.ItemDefinition;
import crl.player.Player;

public class Equip extends Action {
	
	public String getID() {
		return "Equip";
	}
	
	public boolean needsItem() {
		return true;
	}

	public String getPromptItem() {
		return "Wear what?";
	}

	public boolean canPerform(Actor a) {
		Player player = getPlayer(a);
		if (!player.canWield()){
			invalidationMessage = "You can't wear anything";
			return false;
		}
		return true;
	}
	
	public void execute(){
		ItemDefinition def = targetItem.getDefinition();
		Player player = (Player)performer;
		if (!player.canWield()) {
			performer.level.addMessage("You can't wear anything!");
			return;
		}
		
		switch (def.equipType) {
			case ItemDefinition.EQUIPTYPE_ARMOR:
				for (int i = 0 ; i < player.getBannedArmors().length; i++) {
					if (player.getBannedArmors()[i].equals(def.getID())) {
						performer.level.addMessage("You can't wear that kind of armor!");
						return;
					}
				}
				Item currentArmor = player.getArmor();
				player.reduceQuantityOf(targetItem);
				if (currentArmor != null) {
					player.addItem(currentArmor);
				}
				performer.level.addMessage("You wear the "+def.description);
				player.setArmor(targetItem);
				break;
				
			case ItemDefinition.EQUIPTYPE_WEAPON:
				if (player.playerClass == Player.CLASS_VAMPIREKILLER && player.getFlag("ONLY_VK")) {
					performer.level.addMessage("You can't abandon the mystic whip");
					return;
				}
				Item currentWeapon = player.getWeapon();
				Item currentSecondaryWeapon = player.getSecondaryWeapon();
				if (targetItem.isTwoHanded()){
					Item currentShield = player.getShield();
					if (currentShield != null){
						if (!player.canCarry()){
							performer.level.addMessage("You can't store your "+currentShield.getDescription()+" to wear this two handed weapon.");
							return;
						} else {
							performer.level.addMessage("You remove your "+currentShield.getDescription());
							player.addItem(currentShield);
							player.setShield(null);
						}
					}
				}
				
				if (currentWeapon != null){
					if (currentSecondaryWeapon != null){
						if (!player.canCarry()){
							performer.level.addMessage("You are unable to store your "+currentSecondaryWeapon.getDescription());
							return;
						} else {
							player.setSecondaryWeapon(currentWeapon);
							performer.level.addMessage("You put your "+currentWeapon.getDescription()+" in your belt.");
							player.addItem(currentSecondaryWeapon);
							performer.level.addMessage("You store your "+currentSecondaryWeapon.getDescription());
						}
					} else {
						player.setSecondaryWeapon(currentWeapon);
						performer.level.addMessage("You put your "+currentWeapon.getDescription()+" in your belt.");
					}
				}
				performer.level.addMessage("You wield the "+def.description);
				player.setWeapon(targetItem);
				player.reduceQuantityOf(targetItem);
				
				break;
				
			case ItemDefinition.EQUIPTYPE_SHIELD:
				Item currentShield = player.getShield();
				currentWeapon = player.getWeapon();
				
				if (currentWeapon != null && currentWeapon.isTwoHanded()){
					//Must remove weapon to use shield
					if (!player.canCarry()){
						performer.level.addMessage("You are unable to store your current weapon to wear the shield.");
						return;
					} else {
						player.setWeapon(null);
						player.addItem(currentWeapon);
					}
				}
				if (currentShield != null){
					//Remove the current shield
					if (!player.canCarry()){
						performer.level.addMessage("You are unable to store your current shiled to wear the new one.");
						return;
					} else {
						player.setShield(null);
						player.addItem(currentShield);
					}
				}
				
				player.setShield(targetItem);
				player.reduceQuantityOf(targetItem);
				performer.level.addMessage("You wear the "+def.description);
				break;
		}
	}
	
	public int getCost() {
		return 50;
	}
}