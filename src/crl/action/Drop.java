package crl.action;

import crl.level.Level;
import crl.player.Player;

public class Drop extends Action {
	
	public AT getID() {
		return AT.Drop;// "Drop";
	}
	
	public boolean needsItem() {
		return true;
	}

	public String getPromptItem() {
		return "What do you want to drop?";
	}

	public void execute(){
		Level aLevel = performer.level;
		aLevel.addMessage("You drop a "+targetItem.getDescription());
		((Player)performer).reduceQuantityOf(targetItem);
		aLevel.addItem(performer.pos, targetItem);
	}
}