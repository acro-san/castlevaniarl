package crl.action.monster.boss;

import sz.util.Position;
import sz.util.Util;
import crl.action.AT;
import crl.action.Action;
import crl.level.Level;
import crl.monster.Monster;
import crl.monster.MonsterData;

public class UnleashSickles extends Action {
	
	public AT getID(){
		return AT.UnleashSickles;
	}
	
	public int getCost() {
		return 40;
	}
	
	public void execute() {
		Level aLevel = performer.level;
		aLevel.addMessage("Death invokes tiny flying sickles!");
		int sickles = Util.rand(5,10);
		for (int i=0; i<sickles; i++) {
			// FIXME Able to spawn sickles on top of each other???
			int xvar = Util.rand(-8,8);
			int yvar = Util.rand(-8,8);
			Position destinationPoint = Position.add(performer.pos,
				new Position(xvar, yvar));
			Monster sickle = MonsterData.buildMonster("SICKLE");
			sickle.pos = destinationPoint;
			aLevel.addMonster(sickle);
		}
	}
}