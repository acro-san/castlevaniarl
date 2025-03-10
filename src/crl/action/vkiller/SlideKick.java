package crl.action.vkiller;

import crl.Main;
import crl.action.AT;
import crl.action.HeartAction;
import crl.feature.Feature;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import sz.util.Line;
import sz.util.Position;

public class SlideKick extends HeartAction {
	
	public int getHeartCost() {
		return 2;
	}

	public AT getID(){
		return AT.SlideKick;	// "SlideKick";
	}
	
	public boolean needsPosition() {
		return true;
	}

	public String getPromptPosition() {
		return "Where do you want to slide?";
	}

	public void execute() {
		super.execute();
		Player aPlayer = (Player)performer;
		Level aLevel = aPlayer.level;
		
		if (targetPosition.equals(performer.pos)) {
			aLevel.addMessage("You fall back.");
			return;
		}
		aLevel.addMessage("You slide!");
		int damage = 5 + aPlayer.getAttack();

		Line fireLine = new Line(performer.pos, targetPosition);
		Position previousPoint = aPlayer.pos;
		int projectileHeight = aLevel.getMapCell(aPlayer.pos).getHeight();
		for (int i=0; i<3; i++) {
			Position destinationPoint = fireLine.next();
			if (aLevel.isSolid(destinationPoint)){
				drawEffect(Main.efx.createDirectedEffect(performer.pos, targetPosition, "SFX_SLIDEKICK", i));
				aPlayer.landOn(previousPoint);
				return;
			}
			
			String message = "";
			int destinationHeight = aLevel.getMapCell(destinationPoint).getHeight();

			if (destinationHeight == projectileHeight) {
				Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
				if (destinationFeature != null && destinationFeature.isDestroyable()) {
					message = "You hit the "+destinationFeature.getDescription();
					drawEffect(Main.efx.createDirectedEffect(performer.pos, targetPosition, "SFX_SLIDEKICK", i));
					Feature prize = destinationFeature.damage(aPlayer, damage);
					if (prize != null) {
						message += " and destroys it.";
					}
					aLevel.addMessage(message);
					aPlayer.landOn(previousPoint);
					return;
				}
			}
			Monster targetMonster = performer.level.getMonsterAt(destinationPoint);
			
			if (targetMonster != null){
				int monsterHeight = destinationHeight + targetMonster.getHoverHeight();
				if (projectileHeight == monsterHeight){
					if (targetMonster.tryMagicHit(aPlayer,damage, 100, targetMonster.wasSeen(), "dash", false, performer.pos)){
						drawEffect(Main.efx.createDirectedEffect(aPlayer.pos, targetPosition, "SFX_SLIDEKICK", i));
						aPlayer.landOn(previousPoint);
						return;
					};
				} else if (projectileHeight < monsterHeight) {
					aLevel.addMessage("You slide under the "+targetMonster.getDescription());
				} else {
					aLevel.addMessage("You slide over the "+targetMonster.getDescription());
				}
			}
			previousPoint = destinationPoint;
		}
		
		drawEffect(Main.efx.createDirectedEffect(aPlayer.pos, targetPosition, "SFX_SLIDEKICK", 4));
		aPlayer.landOn(previousPoint);
	}

	public int getCost() {
		Player p = (Player) performer;
		return (int)(p.getWalkCost() * 1.4);
	}
	
	public String getSFX() {
		return "wav/scrch.wav";
	}
}