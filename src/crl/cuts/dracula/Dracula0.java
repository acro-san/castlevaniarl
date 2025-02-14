package crl.cuts.dracula;

import sz.util.Position;
import crl.Main;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.level.Level;
import crl.monster.Monster;

public class Dracula0 extends Unleasher {
	
	public void unleash(Level level, Game game) {
		Monster dracula = level.getMonsterByID("DRACULA");
		dracula.pos = new Position(level.getExitFor("#DRACPOS"));
		dracula.isVisible = false;
		level.getMapCell(level.getExitFor("#DRACPOS")).setAppearance(Main.appearances.get("DRACULA_THRONE2"));
		enabled = false;
	}
}
