package crl.cuts.ingame.badBelmont;

import crl.Main;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.level.Level;
import crl.player.Player;
import crl.ui.Display;

public class BadBelmont1 extends Unleasher {

	public void unleash(Level level, Game game) {
		Display.thus.showChat("BADSOLEIYU1", game);
		Player p = level.getPlayer();
		if (p.hasItemByID("JUKEBOX")) {
			level.addMessage("The music box plays a mellow melody");
			Display.thus.showChat("SAVESOLEIYU", game);
			level.removeMonster(level.getMonsterByID("BADBELMONT"));
			p.setFlag("SAVED_SOLEIYU", true);
			level.removeBoss();
			p.addHistoricEvent("saved Soleiyu Belmont from certain doom");
			p.addKeys(1);
		} else {
			level.setMusicKeyMorning("BADBELMONT");
			level.setMusicKeyNoon("BADBELMONT");
			Main.music.playKey("BADBELMONT");
		}
		enabled = false;
	}

}
