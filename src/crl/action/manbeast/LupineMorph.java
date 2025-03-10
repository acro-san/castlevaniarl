package crl.action.manbeast;

import crl.action.AT;
import crl.action.MorphAction;
import crl.player.Consts;
import crl.player.Player;

public class LupineMorph extends MorphAction{
	public int getHeartCost() {
		return 15;
	}
	
	public AT getID(){
		return AT.LupineMorph;
	}

	public String getSFX(){
		return "wav/growll.wav";
	}

	public int getCost() {
		Player p = (Player) performer;
		return (int)(p.getAttackCost() * 1.5);
	}
	
	public int getMadChance() {
		return 20-getPlayer().getSoulPower();
	}

	public String getMorphID() {
		return Consts.C_LUPINEMORPH;
	}

	public String getMorphMessage() {
		return "You turn into a wolf-life creature!";
	}

	public int getMorphStrength() {
		return 10;
	}

	public int getMorphTime() {
		return 50+getPlayer().getSoulPower()*4+(!getPlayer().level.isDay()?50:0);
	}

	public boolean isBigMorph() {
		return true;
	}

	public boolean isSmallMorph() {
		return false;
	}
}