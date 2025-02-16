package crl.action;

import crl.actor.Actor;
import crl.player.Player;

public abstract class HeartAction extends Action {
	
	// TODO: just define a data table, don't have values as CODE!
	public abstract int getHeartCost();
	
	@Override
	public void execute() {
		reduceHearts();
	}
	
	//override? final?
	public void reduceHearts() {
		Player p = (Player)performer;
		p.reduceHearts(getHeartCost());
	}


	public Player getPlayer() {
		return (Player)performer;
	}


	@Override
	public boolean canPerform(Actor a) {
		Player p = getPlayer(a);
		setPerformer(a);
		if (p.getHearts() >= getHeartCost()) {
			return true;
		}
		invalidationMessage = "Your need more power!";
		return false;
	}
}