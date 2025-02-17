package crl.action.vanquisher;

import crl.action.AT;
import crl.action.HeartAction;
import crl.player.Consts;
import crl.player.Player;

public class Enchant extends HeartAction {
	public int getHeartCost() {
		return 5;
	}
	
	public AT getID() {
		return AT.Enchant;//"ENCHANT";
	}
	
	public void execute() {
		super.execute();
		Player p = getPlayer();
		if (p.weapon == null) {
			p.level.addMessage("The energy flies away");
			return;
		}
		p.weapon.setCounter(Consts.C_WEAPON_ENCHANTMENT, 50+p.getSoulPower());
		p.addCounteredItem(p.weapon);
		p.level.addMessage("The "+p.weapon.getDescription()+" glows with a blue aura!");
	}
}
