package crl.action.monster;

import sz.util.Position;
import crl.action.Action;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Damage;
import crl.player.Player;

public class Dash extends Action{
	public String getID(){
		return "SLICE_DIVE";
	}
	
	public boolean needsDirection(){
		return true;
	}

	public void execute() {
		Monster aMonster = (Monster) performer;
		targetDirection = aMonster.starePlayer();
		Position var = directionToVariation(targetDirection);
		Level aLevel = performer.level;
		Player aPlayer = aLevel.getPlayer();
		StringBuffer message = new StringBuffer("The "+aMonster.getDescription()+" dives to you!");
		Position destinationPoint = null;
		for (int i=0; i<5; i++) {
			destinationPoint = Position.add(performer.pos, var);
			if (!aLevel.isValidCoordinate(destinationPoint) || aLevel.isSolid(destinationPoint)) {
				break;
			}
			if (aPlayer.pos.equals(destinationPoint) && 
					aPlayer.getStandingHeight() == aMonster.getStandingHeight()) {
				aPlayer.damage("The "+aMonster.getDescription()+ " slices you!", aMonster, new Damage(aMonster.getAttack(), false));
			}
			
			aMonster.pos = destinationPoint;
			//currentCell = aLevel.getMapCell(destinationPoint);
		}
		aLevel.addMessage(message.toString());
	}


	public String getPromptDirection() {
		return "Where do you want to slice?";
	}

}