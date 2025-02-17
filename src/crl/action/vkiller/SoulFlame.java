package crl.action.vkiller;

import sz.util.Position;
import crl.Main;
import crl.action.AT;
import crl.action.HeartAction;
import crl.level.Level;
import crl.monster.VMonster;
import crl.player.Player;

public class SoulFlame extends HeartAction {
	
	@Override
	public AT getID() {
		return AT.SoulFlame;//"Soul Flame";
	}
	
	@Override
	public int getHeartCost() {
		return 10;
	}
	
	@Override
	public void execute() {
		super.execute();
		Level aLevel = performer.level;
		aLevel.addMessage("Soul Flame!");
		int damage = 16 + aLevel.getPlayer().getShotLevel() * 5 + aLevel.getPlayer().getSoulPower()*2;
		Position pp = performer.pos;
		Main.ui.drawEffect(Main.efx.createLocatedEffect(pp, "SFX_SOUL_FLAME"));
		
		VMonster monsters = aLevel.getMonsters();
		// same code pattern as seen in other vkiller action classes.
		for (int i = 0; i < monsters.size(); i++) {
			Position mp = monsters.elementAt(i).pos;
			if (mp.z == pp.z && Position.distance(mp, pp) < 5){
				StringBuffer buff = new StringBuffer();
				if (monsters.elementAt(i).wasSeen()) {
					buff.append("The "+monsters.elementAt(i).getDescription()+" is hit by the holy flames!");
				}
				monsters.elementAt(i).damage(buff, damage);
				aLevel.addMessage(buff.toString());
			}
		}
	}


	@Override
	public String getPromptPosition() {
		return "Where do you want to throw the vial?";
	}
	
	@Override
	public int getCost() {
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}
	
}