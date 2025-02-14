package crl.action.monster.boss;

import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.action.Action;
import crl.feature.Feature;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Damage;
import crl.player.Player;

public class ShadowFlare extends Action {
	
	public String getID() {
		return "SHADOWFLARE";
	}
	
	public void execute() {
		Monster aMonster = (Monster)performer;
		Level aLevel = aMonster.level;
		if (targetDirection == -1)
			return;
		aLevel.addMessage("Dracula invokes shadow flares!");
		Position runner1 = new Position(performer.pos);
		Position runner2 = null;
		Position runner3 = null;
		int randy = Util.rand(1,2);
		if (targetDirection == Action.UP || targetDirection == Action.DOWN){
			runner2 = Position.add(performer.pos, new Position(-1*randy,0));
			runner3 = Position.add(performer.pos, new Position(1*randy,0));
		} else {
			runner2 = Position.add(performer.pos, new Position(0,-1*randy));
			runner3 = Position.add(performer.pos, new Position(0,1*randy));
		}

		Position directionVar = Action.directionToVariation(targetDirection);
		
		drawEffect(Main.efx.createDirectedEffect(runner1, Position.add(runner1, directionVar), "SFX_SHADOW_FLARE",10));
		drawEffect(Main.efx.createDirectedEffect(runner2, Position.add(runner2, directionVar), "SFX_SHADOW_FLARE",10));
		drawEffect(Main.efx.createDirectedEffect(runner3, Position.add(runner3, directionVar), "SFX_SHADOW_FLARE",10));
		for (int i = 0; i < 10; i++) {
			hit(runner1);
			hit(runner2);
			hit(runner3);
			runner1.add(directionVar);
			runner2.add(directionVar);
			runner3.add(directionVar);
		}
	}

	public int getCost() {
		return 50;
	}
	
	private void hit(Position destinationPoint) {
		String message = "";
		Level aLevel = performer.level;
		Player aPlayer = aLevel.getPlayer();
		Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
		if (destinationFeature != null && destinationFeature.isDestroyable()) {
			message = "The flares crush the "+destinationFeature.getDescription();
			Feature prize = destinationFeature.damage(aPlayer, 4);
			if (prize != null) {
				message += ", consuming it!";
			}
			aLevel.addMessage(message);
		}
		if (destinationPoint.equals(aPlayer.pos)) {
			aPlayer.damage("You are burned by the shadow flare!", (Monster)performer, new Damage(3, false));
		}
		//drawEffect(Main.efx.createLocatedEffect(destinationPoint, "SFX_SHADOW_FLARE"));
	}
}
