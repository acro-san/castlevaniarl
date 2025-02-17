package crl.action.vkiller;

import crl.action.AT;
import crl.action.HeartAction;
import crl.player.Consts;
import crl.player.Player;

public class SoulSaint extends HeartAction {
	
	public AT getID() {
		return AT.SoulSaint;//"Soul Saint";
	}
	
	public int getHeartCost() {
		return 15;
	}

	public String getSFX() {
		return null;
	}
	
	public int getCost() {
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.1);
	}
	
	public void execute(){
		super.execute();
		Player aPlayer = (Player)performer;
		aPlayer.setCounter(Consts.C_FIREBALL_WHIP, 50+3*aPlayer.getSoulPower());
		aPlayer.level.addMessage("Your whip glows!");
	}
}