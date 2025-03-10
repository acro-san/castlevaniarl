package crl.action.vanquisher;

import crl.action.AT;
import crl.action.HeartAction;
import crl.level.Level;
import crl.player.Consts;
import crl.player.Player;

public class Light extends HeartAction {
	
	public AT getID() {
		return AT.Light;
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
	
	public void execute() {
		super.execute();
		Player aPlayer = (Player)performer;
		Level aLevel = aPlayer.level;
		aLevel.addMessage("Magical light illuminates the place.");
		aPlayer.setCounter(Consts.C_MAGICLIGHT, 70+aPlayer.getSoulPower()*3);
	}
	
	
}