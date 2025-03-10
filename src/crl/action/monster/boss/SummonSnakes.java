package crl.action.monster.boss;

import sz.util.Position;
import sz.util.Util;
import crl.action.AT;
import crl.action.Action;
import crl.level.Level;
import crl.monster.Monster;
import crl.monster.MonsterData;

public class SummonSnakes extends Action {
	
	public AT getID() {
		return AT.SummonSnakes;
	}
	
	public int getCost() {
		return 30;
	}
	
	public void execute() {
		Level aLevel = performer.level;
		aLevel.addMessage("Medusa throws some snakes from her head!");
		int sickles = Util.rand(5,10);
		for (int i=0; i<sickles; i++) {
			int xvar = Util.rand(-8,8);
			int yvar = Util.rand(-8,8);
			Position destinationPoint = Position.add(performer.pos,
				new Position(xvar, yvar));
			if (!aLevel.isWalkable(destinationPoint)) {
				i--;
				continue;
			}
			Monster snake = MonsterData.buildMonster("SNAKE");
			snake.pos = destinationPoint;
			aLevel.addMonster(snake);
		}
	}
}