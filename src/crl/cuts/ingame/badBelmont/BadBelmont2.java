package crl.cuts.ingame.badBelmont;

import crl.cuts.Unleasher;
import crl.game.Game;
import crl.level.Level;
import crl.monster.Monster;
import crl.ui.Display;

public class BadBelmont2 extends Unleasher {

	public void unleash(Level level, Game game) {
		if (level.getPlayer().getFlag("SAVED_SOLEIYU")) {
			enabled = false;
			return;
		}
		Monster belmont = level.getMonsterByID("BADBELMONT");
		if (belmont != null)
			return;
		Display.thus.showChat("BADSOLEIYU2", game);
		enabled = false;
	}
}