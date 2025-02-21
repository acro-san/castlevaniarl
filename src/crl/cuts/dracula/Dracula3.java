package crl.cuts.dracula;

import crl.Main;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.level.Level;
import crl.monster.Monster;
import crl.ui.Display;

public class Dracula3 extends Unleasher{

	public void unleash(Level level, Game game) {
		Monster dracula = level.getMonsterByID("DEMON_DRACULA");
		if (dracula != null)
			return;
		Main.music.stopMusic();
		Display.thus.showChat("DRACULA3", game);
		Main.music.playKey("VICTORY");
		game.wonGame();
		enabled = false;
	}
}