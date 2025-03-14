package crl.action.renegade;

import sz.util.Position;
import crl.Main;
import crl.action.AT;
import crl.action.Action;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class BallOfDestruction extends Action {
	
	public AT getID() {
		return AT.BallOfDestruction;
	}
	
	public boolean needsDirection() {
		return true;
	}

	public String getSFX() {
		return "wav/fire.wav";
	}

	public void execute() {
		Position var = directionToVariation(targetDirection);
		Level aLevel = performer.level;
		Player aPlayer = aLevel.getPlayer();
		if (aPlayer.getHearts() < 4) {
			aLevel.addMessage("You need more hearts.");
			return;
		}
		aPlayer.reduceHearts(4);
		aLevel.addMessage("Three balls of fire emerge from your cape!");

		int otherDir1 = 0;
		int otherDir2 = 0;
		switch (targetDirection){
			case Action.UP:
				otherDir1 = Action.UPLEFT;
				otherDir2 = Action.UPRIGHT;
				break;
			case Action.DOWN:
				otherDir1 = Action.DOWNLEFT;
				otherDir2 = Action.DOWNRIGHT;
				break;
			case Action.LEFT:
				otherDir1 = Action.UPLEFT;
				otherDir2 = Action.DOWNLEFT;
				break;
			case Action.RIGHT:
				otherDir1 = Action.UPRIGHT;
				otherDir2 = Action.DOWNRIGHT;
				break;
			case Action.UPRIGHT:
				otherDir1 = Action.UP;
				otherDir2 = Action.RIGHT;
				break;
			case Action.UPLEFT:
				otherDir1 = Action.UP;
				otherDir2 = Action.LEFT;
				break;
			case Action.DOWNLEFT:
				otherDir1 = Action.LEFT;
				otherDir2 = Action.DOWN;
				break;
			case Action.DOWNRIGHT:
				otherDir1 = Action.RIGHT;
				otherDir2 = Action.DOWN;
				break;
			case Action.SELF:
				aLevel.addMessage("The balls dissapear uppon hitting the floor");
				return;
		}
		Position var1 = directionToVariation(otherDir1);
		Position var2 = directionToVariation(otherDir2);
		int i=0;
		for (; i<20; i++) {
			Position destinationPoint = Position.add(aPlayer.pos,
				Position.mul(var, i+1));
			if (hit (destinationPoint, i))
				break;
		}
		if (i == 20) {
			// drawEffect(new MissileEffect(new Position(aPlayer.pos), "*~", Appearance.RED, targetDirection,20, true, false));
			drawEffect(Main.efx.createDirectedEffect(aPlayer.pos, this.getPositionalDirectionFrom(aPlayer.pos), "SFX_RENEGADE_BOD", 20));
		}
		i = 0;
		for (; i<20; i++) {
			Position destinationPoint = Position.add(aPlayer.pos,
				Position.mul(var1, i+1));
			if (hit(destinationPoint, i)) {
				break;
			}
		}
		if (i == 20) {
			//drawEffect(new MissileEffect(new Position(aPlayer.pos), "*~", Appearance.RED, otherDir1,20, true, false));
			drawEffect(Main.efx.createDirectedEffect(aPlayer.pos, this.getPositionalDirectionFrom(aPlayer.pos, otherDir1), "SFX_RENEGADE_BOD", 20));
			//-----
		}
		i = 0;
		for (; i<20; i++) {
			Position destinationPoint = Position.add(aPlayer.pos,
				Position.mul(var2, i+1));
			if (hit (destinationPoint, i))
				break;
		}
		if (i == 20) {
			//drawEffect(new MissileEffect(new Position(aPlayer.pos), "*~", Appearance.RED, otherDir2,20, true, false));
			drawEffect(Main.efx.createDirectedEffect(aPlayer.pos, this.getPositionalDirectionFrom(aPlayer.pos, otherDir2), "SFX_RENEGADE_BOD", 20));
		}
	}

	public int getCost() {
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.3);
	}
	
	private boolean hit (Position destinationPoint, int i) {
		StringBuffer message = new StringBuffer();
		Level aLevel = performer.level;
		Player aPlayer = aLevel.getPlayer();

		Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
		if (destinationFeature != null && destinationFeature.isDestroyable()) {
			message.append("The fireball hits the "+destinationFeature.getDescription());
			//drawEffect(new MissileEffect(new Position(aPlayer.pos), "*~", Appearance.RED, targetDirection,i, true, false));
			drawEffect(Main.efx.createDirectedEffect(aPlayer.pos, this.getPositionalDirectionFrom(aPlayer.pos), "SFX_RENEGADE_BOD", i));
			Feature prize = destinationFeature.damage(aPlayer, 1);
			if (prize != null) {
				message.append(", burning it!");
			}
			aLevel.addMessage(message.toString());
			return true;
		}
		Monster targetMonster = performer.level.getMonsterAt(destinationPoint);
		Cell destinationCell = performer.level.getMapCell(destinationPoint);
		if (
			targetMonster != null &&
			!(targetMonster.isInWater() && targetMonster.canSwim()) &&
				(destinationCell.getHeight() == aLevel.getMapCell(aPlayer.pos).getHeight() ||
				destinationCell.getHeight() -1  == aLevel.getMapCell(aPlayer.pos).getHeight() ||
				destinationCell.getHeight() == aLevel.getMapCell(aPlayer.pos).getHeight()-1)
			)
		{
			
			if (targetMonster.wasSeen()) {
				message.append("The fireball burns the "+targetMonster.getDescription());
			}
			//targetMonster.damage(player.getWhipLevel());
			targetMonster.damage(message, 1+aPlayer.getSoulPower()*2);
			//drawEffect(new MissileEffect(new Position(aPlayer.pos), "*~", Appearance.RED, targetDirection,i, true, false));
			drawEffect(Main.efx.createDirectedEffect(aPlayer.pos, this.getPositionalDirectionFrom(aPlayer.pos), "SFX_RENEGADE_BOD", i));
			aLevel.addMessage(message.toString());
			return true;
		}
		return false;
	}

	public String getPromptDirection() {
		return "Where do you want to throw the fireball?";
	}
	
	public boolean canPerform(Actor a) {
		Player aPlayer = (Player) a;
	//	Level aLevel = performer.level;
		if (aPlayer.getHearts() < 4) {
			invalidationMessage = "You need more energy!";
			return false;
		}
		return true;
	}
}