package crl.action.vkiller;

import sz.util.Debug;
import crl.action.AT;
import crl.action.HeartAction;
import crl.level.Level;
import crl.player.Player;

public class ItemBreakStopwatch extends HeartAction {
	
	public AT getID() {
		return AT.ItemBreak_Stopwatch;	// "Stopwatch"; <-- bug potential...!?
	}
	
	public void execute(){
		super.execute();
		Debug.doAssert(performer instanceof Player, "At action.Stopwatch");
		Player aPlayer = (Player) performer;
		Level x = performer.level;
		x.addMessage("You open the stopwatch! Time stops!");
		x.stopTime(2*(5 + aPlayer.getShotLevel()*2+ aPlayer.getSoulPower()));
	}

	public String getSFX() {
		return "wav/clockbel.wav";
	}
	
	public int getCost() {
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}

	public int getHeartCost() {
		return 5;
	}
}