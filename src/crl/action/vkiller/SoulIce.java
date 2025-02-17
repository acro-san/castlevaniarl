package crl.action.vkiller;

import crl.action.AT;
import crl.action.HeartAction;
import crl.player.Player;

public class SoulIce extends HeartAction {
	
	public AT getID() {
		return AT.SoulIce;//"Soul Ice";
	}

	public int getHeartCost() {
		return 20;
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
		Player aPlayer = (Player)performer;
		aPlayer.healHPPercentage(20 + aPlayer.getSoulPower());
		aPlayer.level.addMessage("You feel relieved!");
	}
}