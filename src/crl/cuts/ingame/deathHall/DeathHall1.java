package crl.cuts.ingame.deathHall;

import crl.Main;
import crl.ai.AIT;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.level.Level;
import crl.monster.Monster;

public class DeathHall1 extends Unleasher {

	public void unleash(Level level, Game game) {
		Monster death = level.getMonsterByID("DEATH");
		death.selector = Main.selectors.get(AIT.NULL_SELECTOR);
		level.addCounter("COUNTBACK_DEATHHALL", 2);
		level.setFlag("DEATH_HALL", true);
		enabled = false;
	}

}
