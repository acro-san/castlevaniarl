package crl.action.manbeast;

import crl.action.AT;
import crl.action.MorphAction;
import crl.player.Consts;
import crl.player.Player;

public class BearMorph extends MorphAction {
	
	public int getHeartCost() {
		return 15;
	}
	
	public AT getID() {
		return AT.BearMorph;
	}

	public String getSFX() {
		return "wav/growll.wav";
	}

	public int getCost() {
		Player p = (Player) performer;
		return (int)(p.getAttackCost() * 1.5);
	}
	
	// FIXME - Whoa! It's cool that this is a mechanic! SURFACE IT TO THE PLAYER!
	// It's not apparent AT ALL that there's possible unknown random downsides 
	// to using the Manbeast's abilities.
	public int getMadChance() {
		return 60 - getPlayer().getSoulPower() * 2;
	}

	public String getMorphID() {
		return Consts.C_BEARMORPH;
	}

	public String getMorphMessage() {
		return "You turn into a huge bear!";
	}

	public int getMorphStrength() {
		return 10;
	}

	public int getMorphTime() {
		Player p = getPlayer();
		
		return 50 + p.getSoulPower()*4 + (!p.level.isDay() ? 50 : 0);
	}

	public boolean isBigMorph() {
		return true;
	}

	public boolean isSmallMorph() {
		return false;
	}
}