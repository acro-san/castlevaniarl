package crl.level;

import crl.Main;
import crl.action.*;

public class PreemergeEffects extends Action {
	
///	private static PreemergeEffects singleton = new PreemergeEffects();
	
	public AT getID() {
		return AT.Preemerge;
	}

	public void execute() {
		///Level aLevel = performer.level;
		Emerger em = (Emerger) performer;
		//aLevel.addMessage("You see something crawling out of the soil!", em.getPoint());
		//aLevel.addEffect(new StaticAnimEffect(em.getPoint(), "^", Appearance.BROWN));
		
		drawEffect(Main.efx.createLocatedEffect(em.getPoint(), "SFX_MONSTER_CRAWLING"));
	}

//	public static PreemergeEffects getAction(){
//		return singleton;
//	}
}