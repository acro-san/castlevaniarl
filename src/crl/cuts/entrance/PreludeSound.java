package crl.cuts.entrance;

import crl.Main;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.level.Level;

public class PreludeSound extends Unleasher {
	public void unleash(Level level, Game game) {
		Main.music.stopMusic();
		Main.music.playKeyOnce("PRELUDE");
		enabled = false;
	}

}
