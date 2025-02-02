package crl.cuts.ingame.clara1;

import crl.Main;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.level.Level;
import crl.ui.Display;

public class Clara1 extends Unleasher {

	public void unleash(Level level, Game game) {
		Display.thus.showChat("VINDELITH3", game);
		level.removeMonster(level.getNPCByID("VINDELITH"));
		level.removeMonster(level.getNPCByID("UNIDED_CLARA"));
		Main.ui.refresh();
		enabled = false;
	}

}
