package crl.action.renegade;

import crl.action.AT;
import crl.action.MorphAction;
import crl.player.Consts;
import crl.player.Player;

public class MystMorph extends MorphAction {
	
	public AT getID() {
		return AT.MystMorph;	//"BatMorph"; <- BUG POSSIBLE!?
	}
	
	public int getHeartCost() {
		return 10;
	}

	public String getSFX() {
		return "wav/swaashll.wav";
	}

	public int getCost() {
		Player p = (Player)performer;
		return (int)(p.getCastCost() * 1.5);
	}
	
	public int getMadChance() {
		return 0;
	}

	public String getMorphID() {
		return Consts.C_MYSTMORPH;
	}

	public String getMorphMessage() {
		return "You turn into thin myst!";
	}

	public int getMorphStrength() {
		return 10;
	}

	public int getMorphTime() {
		return 30+getPlayer().getSoulPower()*4+(!getPlayer().level.isDay()?40:0);
	}

	public boolean isBigMorph() {
		return false;
	}

	public boolean isSmallMorph() {
		return true;
	}
}