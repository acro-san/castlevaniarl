package crl.action;

import sz.util.Debug;
import crl.item.Item;
import crl.level.Level;
import crl.player.Player;


public class Get extends Action {
	
	public AT getID() {
		return AT.Get;
	}
	
	public boolean needsUnderlyingItem(){return true;}
	
	public String getPrompUnderlyingItem(){return "What Item will you get?";}
	
	
	public void execute() {
		Debug.doAssert(performer instanceof Player, 
			"An actor different from the player tried to execute Get action");
		Debug.enterMethod(this, "execute");
		Player p = (Player)performer;
		Level aLevel = performer.level;
		
		Item itm = targetItem;
		if (itm == null) {
			aLevel.addMessage("There is nothing to pick up here!");
			Debug.exitMethod();
			return;
		}
		
		if (p.canCarry()) {
			aLevel.addMessage("You pick up the "+itm.getDescription()+".");
			p.addItem(itm);
			aLevel.removeItemFrom(itm, performer.pos);
		} else {
			// other wording? "You already have as many X as you can carry".
			aLevel.addMessage("You can't carry any more "+itm.getDescription()+"!");
		}
		Debug.exitMethod();
	}

}