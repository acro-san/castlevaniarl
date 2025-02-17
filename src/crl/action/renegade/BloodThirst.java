package crl.action.renegade;

import crl.action.AT;
import crl.action.HeartAction;
import crl.player.Consts;
import crl.player.Player;

public class BloodThirst extends HeartAction {
	
	public AT getID() {
		return AT.BloodThirst;
	}
	
	public int getHeartCost() {
		return 10;
	}
	
	public String getSFX() {
		return "wav/alu_dark.wav";
	}
	
	public void execute() {
		super.execute();
		Player aPlayer = (Player)performer;
		aPlayer.level.addMessage("Your vampire instinct awakes!");
		aPlayer.setCounter(Consts.C_BLOOD_THIRST, 10+aPlayer.getSoulPower()*4);
	}
	
}