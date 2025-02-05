package crl.cuts.arena;

import crl.cuts.Unleasher;
import crl.data.Text;
import crl.game.Game;
import crl.level.Level;
import crl.ui.Display;

public class PreludeArena1 extends Unleasher {
	public void unleash(Level level, Game game) {
		Display.thus.showScreen(Text.ARENA_INTRO_LINE0);
		Display.thus.showScreen(Text.ARENA_INTRO_LINE1);
		Display.thus.showScreen(Text.ARENA_INTRO_LINE2);
		level.removeExit("_START");
		enabled = false;
	}
}