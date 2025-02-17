package crl.action.monster;

import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;
import crl.action.AT;
import crl.action.Action;
import crl.level.Level;
import crl.monster.Monster;
import crl.monster.MonsterData;

public class SummonMonster extends Action {
	private String monsterId;
	private String actionMessage;
	
	public void set(String pMonsterId, String pActionMessage) {
		monsterId = pMonsterId;
		actionMessage = pActionMessage;
	}
	
	public AT getID(){
		return AT.SummonMonster;
	}
	
	public void execute() {
		Debug.doAssert(performer instanceof Monster, "Someone not a monster tried to do 'SummonMonster'");
		Monster aMonster = (Monster) performer;
		Level aLevel = performer.level;
		aLevel.addMessage("The "+aMonster.getDescription()+" "+actionMessage+".");
		int monsters = Util.rand(5,10);
		for (int i=0; i<monsters; i++){
			int xvar = Util.rand(-8,8);
			int yvar = Util.rand(-8,8);
			Position destinationPoint = Position.add(performer.pos,
				new Position(xvar, yvar));
			if (aLevel.isWalkable(destinationPoint)){
				Monster m = MonsterData.buildMonster(monsterId);
				m.pos = destinationPoint;
				aLevel.addMonster(m);
			}
		}
	}

}