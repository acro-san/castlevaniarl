package crl.action.vanquisher;

import crl.Main;
import crl.action.HeartAction;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import sz.util.Position;

public class Mindblast extends HeartAction {
	
	public int getHeartCost() {
		return 15;
	}
	public String getID() {
		return "Mindblast";
	}
	
	public void execute() {
		super.execute();
		Level aLevel = performer.level;
		Player aPlayer = aLevel.getPlayer();
		aLevel.addMessage("You cause great disruption!");

		for (int i = 0; i < 5; i++){
			Monster nearestMonster = aPlayer.getNearestMonster();
			if (nearestMonster == null || Position.flatDistance(nearestMonster.getPosition(), aPlayer.getPosition())>15){
				
			} else {
				StringBuffer buff = new StringBuffer();
				if (nearestMonster.wasSeen()){
					drawEffect(Main.efx.createDirectedEffect(aPlayer.getPosition(), nearestMonster.getPosition(), "SFX_LIT_SPELL", Position.flatDistance(performer.getPosition(), nearestMonster.getPosition())));
					buff.append("The "+nearestMonster.getDescription()+" mind is blasted!");
				}
				nearestMonster.damage(buff, 25+aPlayer.getSoulPower()*2);
				aLevel.addMessage(buff.toString());
				
			}
		}
	}

	public int getCost(){
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.5);
	}
	

	
}