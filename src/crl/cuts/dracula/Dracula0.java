package crl.cuts.dracula;

import sz.util.Position;
import crl.Main;
import crl.cuts.Unleasher;
import crl.game.CRLException;
import crl.game.Game;
import crl.game.PlayerGenerator;
import crl.level.Level;
import crl.levelgen.LevelMaster;
import crl.monster.Monster;
import crl.ui.Display;

public class Dracula0 extends Unleasher {
	public void unleash(Level level, Game game){
		Monster dracula = level.getMonsterByID("DRACULA");
		dracula.setPosition(new Position(level.getExitFor("#DRACPOS")));
		dracula.setVisible(false);
		level.getMapCell(level.getExitFor("#DRACPOS")).setAppearance(Main.appearances.get("DRACULA_THRONE2"));
		enabled = false;
	}
}
