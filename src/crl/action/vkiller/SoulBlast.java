package crl.action.vkiller;

import sz.util.Position;
import crl.Main;
import crl.action.AT;
import crl.action.HeartAction;
import crl.level.Level;
import crl.monster.VMonster;
import crl.player.Player;

public class SoulBlast extends HeartAction{
	
	public AT getID() {
		return AT.SoulBlast; // "Soul Flame"; TODO(debug): Investigate this bug-looking name
	}
	
	public int getHeartCost() {
		return 20;
	}
	
	public void execute() {
		super.execute();
		Level aLevel = performer.level;
		aLevel.addMessage("Soul Blast!");
		int damage = 50 + aLevel.getPlayer().getShotLevel()*2 + aLevel.getPlayer().getSoulPower()*3;

		Position pp = performer.pos;
		Main.ui.drawEffect(Main.efx.createLocatedEffect(pp, "SFX_SOUL_BLAST"));
		
		VMonster monsters = aLevel.getMonsters();
		for (int i = 0; i < monsters.size(); i++) {
			Position mp = monsters.elementAt(i).pos;
			if (mp.z == pp.z && Position.distance(mp, pp) < 5) {
				StringBuffer buff = new StringBuffer();
				if (monsters.elementAt(i).wasSeen()) {
					buff.append("The "+monsters.elementAt(i).getDescription()+" is hit by the holy blast!");
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
		Player p = (Player)performer;
		return (int)(25 / (p.getShotLevel()+1));
	}
	
}