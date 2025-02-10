package crl.action.vkiller;

import crl.Main;
import crl.action.HeartAction;
import crl.level.Level;
import crl.player.Player;

public class SoulWind extends HeartAction {
	
	public int getHeartCost() {
		return 10;
	}

	public String getID() {
		return "Soul Wind";
	}
	
	public void execute(){
		super.execute();
		Player aPlayer = (Player) performer;
		Level x = performer.level;
		Main.ui.drawEffect(Main.efx.createLocatedEffect(aPlayer.getPosition(), "SFX_SOUL_WIND"));
		x.addMessage("Soul Wind!");
		x.stopTime(20 + aPlayer.getShotLevel()*5+ 2*aPlayer.getSoulPower());
	}

	public String getSFX() {
		return "wav/clockbel.wav";
	}
	
	public int getCost() {
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}

}