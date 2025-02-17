package crl.action.vkiller;

import crl.action.AT;
import crl.action.HeartAction;
import crl.level.Level;
import crl.player.Player;

public class Stopwatch extends HeartAction {
	
	public AT getID() {
		return AT.MW_Stopwatch;	// "Stopwatch";
	}
	
	public int getHeartCost() {
		return 3;
	}
	
	public int getCost() {
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}
	
	public void execute() {
		super.execute();
		Player aPlayer = (Player) performer;
		Level x = performer.level;
		x.addMessage("You open the stopwatch! Time stops!");
		x.stopTime(5 + aPlayer.getShotLevel()*2+ aPlayer.getSoulPower());
	}

	public String getSFX() {
		return "wav/clockbel.wav";
	}
	
	
}