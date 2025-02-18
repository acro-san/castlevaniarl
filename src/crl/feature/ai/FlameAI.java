package crl.feature.ai;

import crl.action.*;
import crl.ai.*;
import crl.actor.*;
import crl.feature.SmartFeature;
import crl.feature.action.Shine;
import crl.monster.Monster;


/** Stays alive for 5 turns, causes an animation each turn
 * then dies;*/

public class FlameAI extends ActionSelector implements Cloneable {
	
	private int turnsToDie;
	private boolean activated;
	private static final Action SHINE_ACTION = new Shine();
	
	public AIT getID() {
		return AIT.FLAME_SELECTOR;
	}

	public Action selectAction(Actor who) {
		if (activated) {
			turnsToDie--;
			if (turnsToDie <= 0) {
				who.die();
				who.level.removeSmartFeature((SmartFeature)who);
				activated = false;
				return null;
			}
			Monster m = who.level.getMonsterAt(who.pos);
			if (m != null) {
				m.damage(new StringBuffer(), 1);
			}
			return SHINE_ACTION;//Shine.getAction();
		} else {
			turnsToDie = 5;
			activated = true;
			return SHINE_ACTION;	//Shine.getAction();
		}
	}

}