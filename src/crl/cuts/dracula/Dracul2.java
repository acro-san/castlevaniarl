package crl.cuts.dracula;

import crl.Main;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import crl.ui.Display;
import sz.util.Position;

public class Dracul2 extends Unleasher {

	public void unleash(Level level, Game game) {
		Monster dracula = level.getMonsterByID("DRACULA");
		if (dracula != null) {
			return;
		}
		Display.thus.showChat("DRACULA2", game);
		Player player = level.getPlayer();
		player.informPlayerEvent(Player.EVT_GOTO_LEVEL, "VOID0");
		player.see();
		Main.ui.refresh();
		player.pos = new Position(player.level.getExitFor("#START"));
		enabled = false;
	}
}