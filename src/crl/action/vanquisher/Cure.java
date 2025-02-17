package crl.action.vanquisher;

import crl.action.AT;
import crl.action.HeartAction;

public class Cure extends HeartAction {
	
	public int getHeartCost() {
		return 5;
	}
	
	public AT getID() {
		return AT.Cure;
	}
	
	public void execute() {
		super.execute();
		getPlayer().cure();
	}

}
