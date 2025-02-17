package crl.action.vanquisher;

import crl.action.AT;
import crl.action.HeartAction;
import crl.level.Level;
import crl.player.Consts;
import crl.player.Player;

public class EnergyShield extends HeartAction {
	
	public AT getID() {
		return AT.EnergyShield;
	}
	
	public int getHeartCost() {
		return 15;
	}
	
	public int getCost(){
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.1);
	}
	
	public void execute(){
		super.execute();
		Player p = (Player)performer;
		Level aLevel = p.level;
		aLevel.addMessage("You are covered with a shimmering shield!!");
		p.setCounter(Consts.C_ENERGYSHIELD, 50 + p.getSoulPower() * 2);
	}
	
	
}