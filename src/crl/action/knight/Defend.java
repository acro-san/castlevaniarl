package crl.action.knight;

import crl.action.AT;
import crl.action.Action;
import crl.action.HeartAction;
import crl.actor.Actor;

public class Defend extends HeartAction {
	
	public int getHeartCost() {
		return 1;
	}
	// FIXME What about getCost()!???

	public AT getID() {
		return AT.Defend;
	}
	
	public boolean needsDirection() {
		return true;
	}
	
	public String getPromptDirection() {
		return "Where will you locate your shield to?";
	}
	
	public void execute() {
		super.execute();
		if (targetDirection == Action.SELF) {
			return;
		}
		getPlayer().level.addMessage("You defend yourself with your "+getPlayer().shield.getDescription());
		getPlayer().setShieldGuard(targetDirection, 5);
	}
	
	public boolean canPerform(Actor a) {
		if (getPlayer().shield == null) {
			invalidationMessage = "You don't have a shield.";
			return false;
		}
		return super.canPerform(a);
	}

}
