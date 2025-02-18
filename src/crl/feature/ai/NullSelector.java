package crl.feature.ai;

import crl.action.*;
import crl.ai.*;
import crl.actor.*;

public class NullSelector extends ActionSelector implements Cloneable {
	
	public AIT getID() {
		return AIT.NULL_SELECTOR;
	}

	public Action selectAction(Actor who) {
		return null;
	}

}