package crl.cuts.entrance;

import crl.Main;
import crl.ai.npc.VillagerAI;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.level.Level;
import crl.monster.Monster;
import crl.ui.Display;
import sz.util.Position;

public class Entrance1 extends Unleasher {

	public void unleash(Level level, Game game) {
		Monster clara = level.getNPCByID("UNIDED_CLAW");
		int distance = Position.distance(clara.pos, game.getPlayer().pos);
		if (((VillagerAI)clara.selector).isHostile()) {
			enabled = false;
			return;
		}
		if (distance > 2)
			return;
		Display.thus.showChat("CLARA1", game);
		level.removeMonster(clara);
		Main.ui.refresh();
		enabled = false;
	}

}
