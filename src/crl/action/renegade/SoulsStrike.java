package crl.action.renegade;

import sz.util.Position;
import crl.Main;
import crl.action.HeartAction;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class SoulsStrike extends HeartAction {
	
	public String getID() {
		return "Souls Strike";
	}
	
	public int getHeartCost() {
		return 8;
	}
	
	public void execute() {
		super.execute();
		Level aLevel = performer.level;
		Player aPlayer = aLevel.getPlayer();
		aLevel.addMessage("Three souls come from under your cape");
		
		for (int i = 0; i < 3; i++) {
			Monster nearestMonster = aPlayer.getNearestMonster();
			if (nearestMonster == null || Position.flatDistance(nearestMonster.pos, aPlayer.pos)>15){
			} else {
				StringBuffer buff = new StringBuffer();
				if (nearestMonster.wasSeen())
					buff.append("The soul impacts the "+nearestMonster.getDescription()+"!");
				nearestMonster.damage(buff, 10+aPlayer.getSoulPower()*2);
				drawEffect(Main.efx.createDirectedEffect(aPlayer.pos, nearestMonster.pos, "SFX_SOULSSTRIKE", Position.flatDistance(performer.pos, nearestMonster.pos)));
				aLevel.addMessage(buff.toString());
			}
		}
	}

	public int getCost(){
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.5);
	}
	
}