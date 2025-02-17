package crl.action.invoker;

import sz.util.Line;
import sz.util.Position;
import crl.Main;
import crl.action.AT;
import crl.action.HeartAction;
import crl.feature.Feature;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class Egg extends HeartAction {
	
	public int getHeartCost() {
		return 1;
	}
	
	public AT getID() {
		return AT.Egg;
	}
	
	public boolean needsPosition() {
		return true;
	}

	public void execute() {
		super.execute();
		Player aPlayer = (Player) performer;
		Level aLevel = performer.level;
		aLevel.addMessage("You throw an egg of souls");
		
		int damage = 5 + aPlayer.getSoulPower()*3;
		Position flameOrigin = null;
		Cell destinationCell = null;
		if (targetPosition.equals(performer.pos)){
			flameOrigin = performer.pos;
			destinationCell = aLevel.getMapCell(flameOrigin);
		} else {
			Line holyLine = new Line(performer.pos, targetPosition);
			int i = 0;
			for (; i < 3; i++) {
				flameOrigin = holyLine.next();
				destinationCell = aLevel.getMapCell(flameOrigin);
				if (!aLevel.isValidCoordinate(flameOrigin) || (destinationCell!= null && destinationCell.isSolid())) {
					break;
				}
			}
			Main.ui.drawEffect(Main.efx.createDirectedEffect(performer.pos, targetPosition, "SFX_EGG", i));
		}
		
		if (destinationCell == null) {
			flameOrigin = aLevel.getDeepPosition(flameOrigin);
			if (flameOrigin == null) {
				aLevel.addMessage("The eggs falls into a pit!");
				return;
			}
		}
		
		Main.ui.drawEffect(Main.efx.createLocatedEffect(flameOrigin, "SFX_EGG_BLAST"));

		StringBuffer message = new StringBuffer();
		for (int x = flameOrigin.x -1; x <= flameOrigin.x+1; x++) {
			for (int y = flameOrigin.y -1; y <= flameOrigin.y+1; y++){
				Position destinationPoint = new Position(x,y, flameOrigin.z);
				Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
				if (destinationFeature != null && destinationFeature.isDestroyable()) {
					message.append("The "+destinationFeature.getDescription()+" is scratched");
					Feature prize = destinationFeature.damage(aPlayer, damage);
					if (prize != null) {
						message.append(" and destroyed");
					}
					message.append(".");
				}
				Monster targetMonster = performer.level.getMonsterAt(destinationPoint);
				if (targetMonster != null){
					if (targetMonster.wasSeen())
						message.append("The "+targetMonster.getDescription()+" is scratched");
					//targetMonster.damage(player.getWhipLevel());
					targetMonster.damage(message, damage);
					if (targetMonster.isDead()) {
						if (targetMonster.wasSeen()) {
							message.append(" to the death!");
						}
						performer.level.removeMonster(targetMonster);
					}
				}
			}
		}
		aLevel.addMessage(message.toString());
	}

	@Override
	public String getPromptPosition() {
		return "Where do you want to throw the egg?";
	}
	
	@Override
	public int getCost() {
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}
	
	@Override
	public String getSFX() {
		return "wav/breakpot.wav";
	}

}