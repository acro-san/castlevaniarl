package crl.action.invoker;

import sz.util.Position;
import crl.Main;
import crl.action.AT;
import crl.action.Action;
import crl.action.HeartAction;
import crl.feature.Feature;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class Dragon extends HeartAction {
	public int getHeartCost() {
		return 8;
	}
	public AT getID(){
		return AT.Dragon;
	}
	
	public boolean needsDirection(){
		return true;
	}

	public String getPromptDirection(){
		return "Where do you want to invoke the dragon?";
	}

	public String getSFX(){
		return "wav/gurgle.wav";
	}
	public int getCost(){
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.5);
	}
	
	public void execute() {
		super.execute();
		Player aPlayer = (Player)performer;
		Level aLevel = aPlayer.level;
		aLevel.addMessage("You invoke a dragonfire!");
		int damage = 15 + 3 * aPlayer.getSoulPower();
		int otherDir1 = 0;
		int otherDir2 = 0;
		final Position pp = performer.pos;
		switch (targetDirection) {
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
				aLevel.addMessage("The dragonfire rises as a flaming column!");
				hit (Position.add(pp, Action.directionToVariation(Action.UP)), damage);
				hit (Position.add(pp, Action.directionToVariation(Action.UPLEFT)), damage);
				hit (Position.add(pp, Action.directionToVariation(Action.LEFT)), damage);
				hit (Position.add(pp, Action.directionToVariation(Action.DOWNLEFT)), damage);
				hit (Position.add(pp, Action.directionToVariation(Action.DOWN)), damage);
				hit (Position.add(pp, Action.directionToVariation(Action.DOWNRIGHT)), damage);
				hit (Position.add(pp, Action.directionToVariation(Action.RIGHT)), damage);
				hit (Position.add(pp, Action.directionToVariation(Action.UPRIGHT)), damage);
				return;
		}
		Position directionVar = Action.directionToVariation(targetDirection);
		Position runner1 = Position.add(pp, Action.directionToVariation(otherDir1));
		Position runner2 = Position.add(pp, Action.directionToVariation(targetDirection));
		Position runner3 = Position.add(pp, Action.directionToVariation(otherDir2));
		for (int i = 0; i < 10; i++){
			hit (runner1, damage);
			hit (runner2, damage);
			hit (runner3, damage);
			runner1.add(directionVar);
			runner2.add(directionVar);
			runner3.add(directionVar);
		}
	}

	private boolean hit (Position destinationPoint, int damage) {
		StringBuffer message = new StringBuffer();
		Level aLevel = performer.level;
		Player aPlayer = aLevel.getPlayer();
		//UserInterface.getUI().drawEffect(new TileEffect(destinationPoint, 'o', Appearance.GREEN, 150));
		Main.ui.drawEffect(Main.efx.createLocatedEffect(destinationPoint, "SFX_DRAGONFIRE"));
		//aLevel.addBlood(destinationPoint, 8);
		Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
		if (destinationFeature != null && destinationFeature.isDestroyable()) {
			message.append("The dragon crushes the "+destinationFeature.getDescription());
			Feature prize = destinationFeature.damage(aPlayer, damage);
			if (prize != null) {
				message.append(", breaking it apart!");
			}
			aLevel.addMessage(message.toString());
			return true;
		}
		Monster targetMonster = performer.level.getMonsterAt(destinationPoint);
		if (targetMonster != null &&
			!(targetMonster.isInWater() && targetMonster.canSwim())
			)
		{
			if (targetMonster.wasSeen()) {
				message.append("The dragon slashes the "+targetMonster.getDescription());
			}
			targetMonster.damage(message, damage);
			if (targetMonster.isDead()) {
				message.append(", finishing it!");
			}
			aLevel.addMessage(message.toString());
			return true;
		}
		return false;
	}

}