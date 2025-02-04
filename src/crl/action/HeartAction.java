package crl.action;

import crl.actor.Actor;
import crl.player.Player;

public abstract class HeartAction extends Action {
	
	public abstract int getHeartCost();
	
	public void execute(){
		reduceHearts();
	}
	
	public void reduceHearts() {
		Player aPlayer = (Player) performer;
		aPlayer.reduceHearts(getHeartCost());
	}


	public Player getPlayer(){
		return (Player)performer;
	}

	public boolean canPerform(Actor a) {
		Player p = getPlayer(a);
		setPerformer(a);
		if (p.getHearts() >= getHeartCost()){
			return true;
		}
		invalidationMessage = "Your need more power!";
		return false;
	}
}