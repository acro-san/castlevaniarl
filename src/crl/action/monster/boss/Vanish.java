package crl.action.monster.boss;

import crl.Main;
import crl.action.Action;
import crl.level.Level;
import crl.monster.Monster;

public class Vanish extends Action {
	
	public String getID() {
		return "VANISH";
	}

	public void execute() {
		Level aLevel = performer.level;
		aLevel.addMessage("Dracula disappears!");
		Monster mon = (Monster)performer;
		mon.isVisible = false;
		//drawEffect(new SplashEffect(performer.getPosition(), ".oO", Appearance.WHITE));
		drawEffect(Main.efx.createLocatedEffect(performer.pos, "SFX_VANISH"));
		mon.setPosition(0,0,0);
	}
	
}