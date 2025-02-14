package crl.action.monster;

import sz.util.Debug;
import sz.util.Position;
import crl.action.Action;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Damage;

public class Swim extends Action{
	public String getID(){
		return "Swim";
	}
	
	public boolean needsDirection(){
		return true;
	}

	public void execute(){
		Debug.doAssert(performer instanceof Monster, "The player tried to Swim...");
		Monster aMonster = (Monster) performer;
        Position var = directionToVariation(targetDirection);
        Position destinationPoint = Position.add(performer.getPosition(), var);
        Level aLevel = performer.level;
        Cell destinationCell = aLevel.getMapCell(destinationPoint);
        Monster destinationMonster = aLevel.getMonsterAt(destinationPoint);
        if (destinationCell != null && !destinationCell.isSolid()){
			if (destinationCell.isShallowWater() || destinationCell.isWater()){
                 if (destinationMonster == null)
                 	performer.setPosition(destinationPoint);
			} else {
				if (destinationMonster == null){
					aLevel.addMessage("A "+aMonster.getDescription()+" jumps out of the water!", destinationPoint);
					performer.setPosition(destinationPoint);
				}
			}
		}

		if (aMonster.getAttack() > 0 && aLevel.getPlayer().getPosition().equals(destinationPoint) && aLevel.getPlayer().getStandingHeight() == aMonster.getStandingHeight()){
			// Damage the poor player and bounce him back
			if (aLevel.getPlayer().damage("The "+aMonster.getDescription()+" hits you with his jump!", aMonster, new Damage(aMonster.getAttack(), false)))
				aLevel.getPlayer().bounceBack(var,1);
		}
	}

}