package crl.action.vkiller;

import crl.Main;
import crl.action.HeartAction;
import crl.feature.Feature;
import crl.level.Level;
import crl.monster.Monster;
import crl.monster.VMonster;
import crl.player.Player;
import sz.util.Position;
import sz.util.Util;

public class ItemBreakHoly extends HeartAction {
	
	public int getHeartCost() {
		return 5;
	}
	public String getID(){
		return "Holy";
	}
	
	public void execute(){
		Player aPlayer = (Player) performer;
		Level aLevel = performer.level;
		aLevel.addMessage("You jump! You unleash a rain of holy water!");
		Main.ui.drawEffect(Main.efx.createLocatedEffect(aPlayer.pos, "SFX_HOLY_RAINSPLASH"));
		int damage = 6 + getPlayer().getShotLevel() + getPlayer().getSoulPower();
		
		VMonster monsters = aLevel.getMonsters();
		for (int i = 0; i < monsters.size(); i++){
			
			if (monsters.elementAt(i).pos.z == performer.pos.z && Position.distance(monsters.elementAt(i).pos, performer.pos) < 4){
				StringBuffer buff = new StringBuffer();
				if (monsters.elementAt(i).wasSeen()) {
					buff.append("The "+monsters.elementAt(i).getDescription()+" is splashed with holy water!");
				}
				monsters.elementAt(i).damage(buff, damage);
				aLevel.addMessage(buff.toString());
			}
		}
		Main.ui.refresh();
		
		int drops = Util.rand(20,40);

		for (int i = 0; i < drops; i++){
			int xdif = 5-Util.rand(0,10);
			int ydif = 5-Util.rand(0,10);
			Position destinationPoint = Position.add(aPlayer.pos, new Position(xdif,ydif));
			if (aLevel.isValidCoordinate(destinationPoint)){
				Main.ui.drawEffect(Main.efx.createLocatedEffect(destinationPoint, "SFX_HOLY_RAINDROP"));
				Main.ui.refresh();
				Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
				if (destinationFeature != null && destinationFeature.isDestroyable()) {
					Feature prize = destinationFeature.damage(aPlayer, damage);
					if (prize != null) {
						aLevel.addMessage("The "+destinationFeature.getDescription()+" burns until consumption!");
					} else {
						aLevel.addMessage("The "+destinationFeature.getDescription()+" burns.");
					}
				}
				Monster targetMonster = performer.level.getMonsterAt(destinationPoint);
				if (targetMonster != null){
					StringBuffer buff = new StringBuffer();
					buff.append("The "+monsters.elementAt(i).getDescription()+" is splashed with holy rain!");
					targetMonster.damage(buff, damage);
					aLevel.addMessage (buff.toString());
					if (targetMonster.isDead()) {
						if (targetMonster.wasSeen())
							aLevel.addMessage ("The "+targetMonster.getDescription()+" catches in flame and is roasted!");
						performer.level.removeMonster(targetMonster);
					} else {
						if (targetMonster.wasSeen())
							aLevel.addMessage ("The "+targetMonster.getDescription()+" catches in flame.");
					}
				}
			}
		}
	}

	public int getCost() {
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}
	public String getSFX() {
		return "wav/breakpot.wav";
	}
}