package crl.feature.action;

import sz.util.Line;
import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.action.AT;
import crl.action.Action;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.feature.SmartFeature;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import crl.ui.effects.Effect;

public class CrossBack extends Action {
	
	public AT getID() {
		return AT.CrossBack;
	}
	
	public boolean needsPosition() {
		return true;
	}

	public void execute() {
		Level aLevel = performer.level;
		aLevel.addMessage("The cross comes back!");
		int damage = 3 + aLevel.getPlayer().getShotLevel() + aLevel.getPlayer().getSoulPower();
		
		if (targetPosition.equals(performer.pos)) {
			if (Util.chance(50)) {
				aLevel.addMessage("The cross falls heads! You catch the cross.");
			} else {
				aLevel.addMessage("The cross falls tails! You catch the cross.");
			}
			return;
		}
		
		Line crossLine = new Line(performer.pos, targetPosition);
		int startHeight = ((SmartFeature)performer).height;
		crossLine.next();
		Position destinationPoint = null;
		int i = 0;
		for (i=0; i<20; i++) {
			destinationPoint = crossLine.next();
			if (destinationPoint.equals(aLevel.getPlayer().pos)){
				aLevel.addMessage("You catch the cross");
				break;
			}

			Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
			if (destinationFeature != null && destinationFeature.isDestroyable()) {
				aLevel.addMessage("The cross cuts the "+destinationFeature.getDescription());
				destinationFeature.damage(aLevel.getPlayer(), damage);
			}
			
			Monster targetMonster = performer.level.getMonsterAt(destinationPoint);
			Cell destinationCell = performer.level.getMapCell(destinationPoint);
			
			if (targetMonster != null) {
				if ((targetMonster.isInWater() && targetMonster.canSwim()) || destinationCell.getHeight() < startHeight-1){
					if (targetMonster.wasSeen())
						aLevel.addMessage("The cross flies over the "+targetMonster.getDescription());
				} else if (destinationCell.getHeight() > startHeight + 1){
					if (targetMonster.wasSeen())
						aLevel.addMessage("The cross flies under the "+targetMonster.getDescription());
				} else {
					StringBuffer buff = new StringBuffer();
					if (targetMonster.wasSeen())
						buff.append("The cross slashes the "+targetMonster.getDescription());
					targetMonster.damage(buff, damage);
					aLevel.addMessage(buff.toString());
					if (targetMonster.isDead()) {
						performer.level.removeMonster(targetMonster);
					}
				}
			}
		}

		Effect crossEffect = Main.efx.createDirectedEffect(performer.pos, targetPosition, "SFX_CROSS", i);
		crossEffect.setPosition(performer.level.getPlayer().pos);
		drawEffect(crossEffect);
	}


	public String getPromptPosition() {
		return "Where do you want to throw the Cross?";
	}
	
	public int getCost() {
		if (performer instanceof Player) {
			Player p = (Player)performer;
			return (int)(25 / (p.getShotLevel()+1));
		} else {
			return 40;
		}
	}
	
	public String getSFX() {
		return "wav/misswipe.wav";
	}

	public boolean canPerform(Actor a) {
		if (a instanceof Player) {
			Player aPlayer = (Player) a;
			if (aPlayer.getHearts() < 1) {
				invalidationMessage = "You don't have enough hearts";
				return false;
			}
		}
		return true;
	}
	
}
