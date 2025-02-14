package crl.feature.action;

import sz.util.Position;
import crl.Main;
import crl.action.Action;
import crl.level.Level;
import crl.monster.Monster;

public class Blast extends Action {
	
	public String getID() {
		return "Blast";
	}
	
	public void execute() {
		Level aLevel = performer.level;
		int damage = 20 + aLevel.getPlayer().getShotLevel() + aLevel.getPlayer().getSoulPower();

		aLevel.addMessage("The crystal emits a holy blast!");

		Position blastPosition = performer.pos;
		
		//aLevel.addEffect(new SplashEffect(blastPosition, "Oo,.", Appearance.CYAN));
		Main.ui.drawEffect(Main.efx.createLocatedEffect(blastPosition, "SFX_CRYSTAL_BLAST"));
		Position destinationPoint = new Position(0,0, performer.pos.z);
		for (int x = blastPosition.x -3; x <= blastPosition.x+3; x++) {
			for (int y = blastPosition.y -3; y <= blastPosition.y+3; y++) {
				destinationPoint.x = x;
				destinationPoint.y = y;
				Monster targetMonster = performer.level.getMonsterAt(destinationPoint);
				if (targetMonster != null) {
					if (targetMonster.wasSeen()) {
						aLevel.addMessage("The "+targetMonster.getDescription()+" is hit by the holy wave!");
					}
					targetMonster.damage(new StringBuffer(), damage);
				}
			}
		}
	}


	public String getSFX() {
		return "wav/lazrshot.wav";
	}


	public int getCost() {
		return 50;
	}
}
