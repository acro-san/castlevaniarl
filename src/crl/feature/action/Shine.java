package crl.feature.action;

import crl.action.*;

// looks deprecated?
public class Shine extends Action {
	
	public AT getID() {
		return AT.Shine;
	}
	
	public void execute() {
		//Level aLevel = performer.getLevel();
		//aLevel.addMessage("The holy flame glows!");
		//aLevel.addEffect(new StaticAnimEffect(performer.getPosition(), "\\|/|\\", Appearance.YELLOW));
		//aLevel.addEffect(new StaticAnimEffect(performer.getPosition(), "�o�o", Appearance.YELLOW));
	}

	/*
	private static Shine singleton = new Shine();
	public static Shine getAction(){
		return singleton;
	}
	*/
}