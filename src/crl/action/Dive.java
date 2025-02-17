package crl.action;

import sz.util.Position;
import crl.actor.Actor;
import crl.level.Cell;
import crl.level.Level;
import crl.player.Player;

public class Dive extends Action {
	
	public AT getID() {
		return AT.Dive;//"DIVE";
	}
	
	public void execute() {
		Player aPlayer = (Player)performer;
		Level aLevel = aPlayer.level;
		Cell currentCell = aLevel.getMapCell(aPlayer.pos);
		if (currentCell.isShallowWater()){
			if (aPlayer.pos.z != aLevel.getDepth()-1){
				Position deep = new Position(aPlayer.pos);
				deep.z++;
				if (!aLevel.getMapCell(deep).isSolid()){
					aLevel.addMessage("You dive into the water");
					aPlayer.landOn(deep, false);
				} else {
					aLevel.addMessage("You can't dive lower");
				}
			}
		}
	}
	
	
	public boolean canPerform(Actor a) {
		Player aPlayer = getPlayer(performer);
		Level aLevel = aPlayer.level;
		Cell currentCell = aLevel.getMapCell(aPlayer.pos);
		if (currentCell.isShallowWater()) {
			if (aPlayer.pos.z == aLevel.getDepth()-1) {
				invalidationMessage = "You can't dive lower";
				return false;
			}
		} else {
			invalidationMessage = "You can only dive on water";
			return false;
		}
		return true;
	}
}
