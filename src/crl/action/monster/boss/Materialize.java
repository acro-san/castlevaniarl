package crl.action.monster.boss;

import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.action.Action;
import crl.level.Level;
import crl.monster.Monster;

public class Materialize extends Action {
	
	public String getID() {
		return "MATERIALIZE";
	}
	
	public void execute() {
		Level aLevel = performer.level;
		aLevel.addMessage("Dracula materializes!");
		Monster mon = (Monster)performer;
		mon.isVisible = true;
		Position var = new Position(Util.rand(-5,5),Util.rand(-5,5));
		Position pum =Position.add(aLevel.getPlayer().pos, var);
		mon.pos = pum;
		//drawEffect(new SplashEffect(pum, "Oo.", Appearance.WHITE));
		drawEffect(Main.efx.createLocatedEffect(pum, "SFX_MATERIALIZE"));
	}
}