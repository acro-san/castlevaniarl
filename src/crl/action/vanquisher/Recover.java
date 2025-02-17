package crl.action.vanquisher;

import crl.action.AT;
import crl.action.HeartAction;
import crl.player.Player;

public class Recover extends HeartAction {
	public int getHeartCost() {
		return 15;
	}
	
	public AT getID() {
		return AT.Recover;
	}
	
	public String getSFX() {
		return null;
	}
	
	public int getCost() {
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.1);
	}
	
	public void execute() {
		super.execute();
		Player p = (Player)performer;
		p.healHPPercentage(10 + p.getSoulPower());
		p.level.addMessage("You feel relieved!");
	}
}