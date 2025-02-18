package crl.level;

import crl.action.*;
import crl.ai.*;
import crl.actor.*;

public class EmergerAI extends ActionSelector {
	
	private static final Action
		EMERGE_ACTION = new EmergeMonster();
	
	private int counter;

	public AIT getID() {
		return AIT.Emerge;
	}

	public Action selectAction(Actor who) {
		Emerger x = (Emerger)who;
		counter++;
		if (x.getCounter() < counter) {
			who.die();
			return EMERGE_ACTION;
		}
		//if (Util.chance(20))
			//return PreemergeEffects.getAction();
			//return null;
		return null;
	}


}