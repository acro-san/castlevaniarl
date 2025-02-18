package crl.feature.ai;

import crl.action.Action;
import crl.actor.Actor;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.feature.SmartFeature;
import crl.feature.action.*;

public class BlastCrystalAI extends ActionSelector implements Cloneable {
	
	private int blastCounter;
	private int turnsToBlast; 
	private boolean activated;
	
	public AIT getID() {
		return AIT.CRYSTAL_SELECTOR;
	}

	public Action selectAction(Actor who) {
		if (activated) {
			if (blastCounter > 2){
				who.die();
				who.level.removeSmartFeature((SmartFeature)who);
				activated = false;
				return null;
			}else{
				turnsToBlast--;
				if (turnsToBlast == 0) {
					turnsToBlast = 5;
					blastCounter++;
					return new Blast();
				} else {
					return null;
				}
			}
		} else {
			turnsToBlast = 5;
			activated = true;
			return new Blast();
		}
	}


}