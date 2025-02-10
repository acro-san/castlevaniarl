package crl.level;

import crl.Main;
import crl.action.*;

public class SFX extends Action {
	
//	private static SFX singleton = new SFX();
	
	public String getID() {
		return "SFX";
	}

	public void execute() {
	///	Level level = performer.level;
		//level.addEffect(new FlashEffect(performer.getLevel().getPlayer().getPosition(), Appearance.WHITE));
		drawEffect(Main.efx.createLocatedEffect(performer.level.getPlayer().getPosition(), "SFX_THUNDER_FLASH"));
	}

	private final static int THUNDER = 1;
	private int effect = THUNDER;	// by default...

//	public void setEffect(int pEffect) {
//		effect = pEffect;
//	}

	// replace with: return/ new SFX(); ??
	/*
	public static SFX getThunder() {
		singleton.setEffect(THUNDER);
		return singleton;
	}
	*/
}