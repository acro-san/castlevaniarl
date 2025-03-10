package crl.action;

import java.util.Iterator;

import crl.item.Item;
import crl.player.Player;

public class FusionSpirits extends Action {
	
	public AT getID() {
		return AT.FusionSpirits;
	}
	
	public boolean needsSpirits() {
		return true;
	}

	public void execute() {
		Player aPlayer = (Player)performer;
		if (targetMultiItems.size() != 2){
			performer.level.addMessage("You can only fusion two spirits");
			return;
		}
		/*Checks all the items to be spirits*/
		for (Iterator<Item> item = targetMultiItems.iterator(); item.hasNext();) {
			Item element = (Item)item.next();
			if (!element.getDefinition().getID().endsWith("SPIRIT")) {
				performer.level.addMessage(element.getDescription() + " is not an spirit");
				return;
			}
		}
		
		String principal = "";
		String secondary = "";
		String principalDs = "";
		String secondaryDs = "";

		for (Iterator<Item> item = targetMultiItems.iterator(); item.hasNext();) {
			Item element = (Item)item.next();
			String fx =element.getDefinition().getID();
			/*Looks for attribute spirits*/
			if (fx.equals("VENUS_SPIRIT") || fx.equals("MERCURY_SPIRIT") || fx.equals("MARS_SPIRIT")){
				principal = fx;
				principalDs = element.getDescription();
			} else {
				secondary = fx;
				secondaryDs = element.getDescription();
			}
		}
		if (principal.equals("") || secondary.equals("")){
			performer.level.addMessage("That fusion won't work");
			return;
		}
		
		performer.level.addMessage("You try to fusion "+principalDs+" with "+secondaryDs+"...");
		aPlayer.addHistoricEvent("Fusioned "+principalDs+" with "+secondaryDs);
		
		if (principal.equals("VENUS_SPIRIT")) {
			if (secondary.equals("URANUS_SPIRIT")) {
				aPlayer.reduceCastCost(5);
				performer.level.addMessage("Your spellcasting ability increases!");
			} else if (secondary.equals("NEPTUNE_SPIRIT")){
				aPlayer.increaseHeartMax(7);
				performer.level.addMessage("You are able to hold more hearts!");
			} else if (secondary.equals("JUPITER_SPIRIT")){
				aPlayer.increaseSoulPower(1);
				performer.level.addMessage("Your soul power increases!");
			}
		} else if (principal.equals("MERCURY_SPIRIT")) {
			if (secondary.equals("URANUS_SPIRIT")){
				aPlayer.reduceWalkCost(5);
				performer.level.addMessage("You feel quicker!");
			} else if (secondary.equals("NEPTUNE_SPIRIT")){
				aPlayer.increaseCarryMax(5);
				performer.level.addMessage("You feel able to carry more!");
			} else if (secondary.equals("JUPITER_SPIRIT")){
				aPlayer.increaseEvadeChance(5);
				performer.level.addMessage("You feel more nimble!");
			}
		} else if (principal.equals("MARS_SPIRIT")) {
			if (secondary.equals("URANUS_SPIRIT")) {
				aPlayer.reduceAttackCost(5);
				performer.level.addMessage("You feel more able on combat!");
			} else if (secondary.equals("NEPTUNE_SPIRIT")){
				aPlayer.increaseHPMax(3);
				performer.level.addMessage("You feel hardy!");
			} else if (secondary.equals("JUPITER_SPIRIT")) {
				aPlayer.increaseAttack(1);
				performer.level.addMessage("You feel stronger!");
			}
		}
		
		for (Iterator<Item> item = targetMultiItems.iterator(); item.hasNext();) {
			Item element = (Item)item.next();
			aPlayer.reduceQuantityOf(element);
		}
	}
}