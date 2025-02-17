package crl.action.renegade;

import crl.action.AT;
import crl.action.Action;
import crl.actor.Actor;
import crl.player.Damage;
import crl.player.Player;

public class MinorJinx extends Action {
	
	public AT getID() {
		return AT.MinorJinx;//"MINOR_JINX";
	}
	
	public String getSFX() {
		return null;
	}
	
	public int getCost() {
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.1);
	}
	
	
	public void execute() {
		Player aPlayer = (Player)performer;
		int recover = 3 + aPlayer.getSoulPower();
		aPlayer.addHearts(recover);
		aPlayer.selfDamage("You exchange vitality for power!! (+"+recover+")", Player.DAMAGE_JINX, new Damage(5, true));
	}
	
	
	public boolean canPerform(Actor a) {
		Player aPlayer = (Player) a;
		if (aPlayer.getHP() <= 5) {
			invalidationMessage = "That would be suicidal!";
			return false;
		}
		return true;
	}
}