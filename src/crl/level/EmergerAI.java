package crl.level;

import crl.action.*;
import crl.ai.*;
import crl.actor.*;

public class EmergerAI implements ActionSelector {
	
	private static final Action
		EMERGE_ACTION = new EmergeMonster();
	
	private int counter;

	// AIT
	public String getID() {
		return "Emerge";
	}

	public Action selectAction(Actor who) {
		Emerger x = (Emerger) who;
		counter++;
		if (x.getCounter() < counter){
			who.die();
			return EMERGE_ACTION;
		}
		//if (Util.chance(20))
			//return PreemergeEffects.getAction();
			//return null;
		return null;
	}

	public ActionSelector derive() {
		try {
			return (ActionSelector) clone();
		} catch (CloneNotSupportedException cnse) {
			return null;
		}
	}
}