package crl.cuts.prelude;

import sz.util.Position;
import crl.Main;
import crl.ai.monster.boss.DraculaAI;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.level.Level;
import crl.ui.Display;

public class Prelude4 extends Unleasher {

	public void unleash(Level level, Game game) {
		if (!level.getFlag("CHRIS_DEAD") || 
			Position.distance(level.getMonsterByID("PRELUDE_DRACULA").pos, game.getPlayer().pos) > 5) {
			return;
		}
		Display.thus.showChat("PRELUDE_DRACULA2", game);
		Main.music.playKey("CHRIS_DEAD");
		level.setMusicKeyMorning("CHRIS_DEAD");
		((DraculaAI)level.getMonsterByID("PRELUDE_DRACULA").selector).inBattle = true;
		enabled = false;
	}

}
