package crl.action.manbeast;

import crl.action.AT;
import crl.action.HeartAction;
import crl.level.Level;
import crl.player.Consts;
import crl.player.Player;

public class PowerBlow2 extends HeartAction{
	public int getHeartCost() {
		return 3;
	}
	
	public AT getID() {
		return AT.PowerBlow2;
	}
	
	public int getCost() {
		Player p = (Player)performer;
		return (int)(p.getAttackCost() * 1.1);
	}
	
	public void execute(){
		super.execute();
		Player aPlayer = (Player)performer;
		Level aLevel = aPlayer.level;
		aLevel.addMessage("WAAAAARGH!!!! You feel very powerful!");
		aPlayer.setCounter(Consts.C_POWERBLOW2, 5);
	}
}