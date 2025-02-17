package crl.action.vkiller;

import sz.util.Debug;
import sz.util.Position;
import crl.action.AT;
import crl.action.Action;
import crl.feature.Feature;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class Rebound extends Action {
	
	public AT getID() {
		return AT.Rebound;	//"Rebound";
	}
	
	public boolean needsDirection() {
		return true;
	}

	public void execute() {
		Debug.doAssert(performer instanceof Player, "action.Rebound");
		Player aPlayer = (Player) performer;

		Level aLevel = performer.level;
		if (aPlayer.getHearts() < 1) {
			aLevel.addMessage("You don't have enough hearts");
			return;
		}
		aPlayer.reduceHearts(1);
		aLevel.addMessage("You throw a rebound crystal!");

		Position var = new Position(directionToVariation(targetDirection));
		int runLength = 0;
		//DirectionMissileEffect x = new DirectionMissileEffect(aPlayer.pos, "\\|/--/|\\", Appearance.BLUE, targetDirection, 20, 15);
		Position runner = new Position(performer.pos);
		StringBuffer message = new StringBuffer();
		Position bouncePoint  = new Position(performer.pos);
		for (int i=0; i<20; i++) {
			runLength++;
			runner.add(var);
			Feature destinationFeature = aLevel.getFeatureAt(runner);
			if (destinationFeature != null && destinationFeature.isDestroyable()) {
				message.append("The crystal hits the "+destinationFeature.getDescription());
				Feature prize = destinationFeature.damage(aPlayer, 1);
				if (prize != null) {
					message.append(", and destroys it");
				}
				aLevel.addMessage(message.toString());
				/*x.setPosition(bouncePoint);
				x.setDepth(runLength);
				x.setDirection(Action.toIntDirection(var));
				aLevel.addEffect(x);*/
				break;
			}
			Monster targetMonster = performer.level.getMonsterAt(runner);
			message = new StringBuffer();
			if (targetMonster != null && !targetMonster.isInWater()) {
				message.append("The crystal hits the "+targetMonster.getDescription());
				//targetMonster.damage(player.getWhipLevel());
				targetMonster.damage(message, 1);
				if (targetMonster.isDead()) {
					message.append(", destroying it!");
					performer.level.removeMonster(targetMonster);
				}
				if (targetMonster.wasSeen()) {
					aLevel.addMessage(message.toString());
				}
				break;
			}

			Cell targetCell = performer.level.getMapCell(runner);
			if (targetCell != null && (targetCell.isSolid() || targetCell.getHeight() > aLevel.getMapCell(performer.pos).getHeight()+2)){
				aLevel.addMessage("The crystal rebounds in the "+targetCell.getDescription());
				bouncePoint = new Position(runner);
				Position bounce = new Position(0,0);
				if (aLevel.getMapCell(runner.x + var.x, runner.y, performer.pos.z) == null || aLevel.getMapCell(runner.x + var.x, runner.y, performer.pos.z).isSolid()) {
					bounce.x = 1;
				}
				if (aLevel.getMapCell(runner.x, runner.y + var.y, performer.pos.z) == null || aLevel.getMapCell(runner.x, runner.y + var.y, performer.pos.z).isSolid()) {
					bounce.y = 1;
				}
				
				var.mul(Position.mul(new Position(-1,-1), bounce));
				runLength = 0;
			}

			if (i == 19) {
				/*x.setPosition(bouncePoint);
				x.setDepth(runLength);
				aLevel.addEffect(x);*/
			}
		}
	}
	
	
	public int getCost() {
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}
	
	
	public String getPromptDirection() {
		return "Where do you want to throw the Rebound Crystal?";
	}

}