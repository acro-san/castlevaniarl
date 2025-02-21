package crl.level;

import crl.Main;
import crl.action.*;

public class SFX extends Action {
	
	public AT getID() {
		return AT.SFX;
	}

	public void execute() {
	///	Level level = performer.level;
		//level.addEffect(new FlashEffect(performer.getLevel().getPlayer().pos, Appearance.WHITE));
		drawEffect(Main.efx.createLocatedEffect(performer.level.getPlayer().pos, "SFX_THUNDER_FLASH"));
	}

	private final static int THUNDER = 1;
	private int effect = THUNDER;	// by default...

}