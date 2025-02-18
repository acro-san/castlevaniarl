package crl.feature;

import crl.action.*;
import crl.ai.*;
import crl.actor.*;

public class CountDown extends ActionSelector implements Cloneable {
	
	private int turnsToDie;

	public void setTurns(int turns) {
		turnsToDie = turns;
	}

	public AIT getID() {
		return AIT.COUNTDOWN;
	}

	public Action selectAction(Actor who) {
		// Debug.say("cpuntdown " + turnsToDie);
		turnsToDie--;
		if (turnsToDie == 0) {
			who.die();
			who.level.removeSmartFeature((SmartFeature)who);
		}
		return null;
	}


}