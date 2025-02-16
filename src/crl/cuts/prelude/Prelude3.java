package crl.cuts.prelude;

import crl.Main;
import crl.ai.monster.boss.DraculaAI;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.game.PlayerGenerator;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;

public class Prelude3 extends Unleasher {

	public void unleash(Level level, Game game) {
		Monster dracula = level.getMonsterByID("PRELUDE_DRACULA");
		if (dracula.hp > dracula.getMaxHP()/2) {
			return;
		}
		
		level.addMessage("Dracula invokes a deadly beam of chaos energy!!!");
		Main.ui.drawEffect(Main.efx.createLocatedEffect(level.getPlayer().pos, "SFX_KILL_CHRIS"));
		
		Player p = level.getPlayer();
		game.setPlayer(PlayerGenerator.thus.createSpecialPlayer("SOLEIYU"));
		level.removeActor(p);
		p.die();
		level.getPlayer().pos = level.getExitFor("_START");
		level.getPlayer().see();
		Main.ui.refresh();
		level.setFlag("CHRIS_DEAD", true);
		((DraculaAI)level.getMonsterByID("PRELUDE_DRACULA").selector).reset();
		dracula.pos = level.getExitFor("#DRACPOS");
		dracula.isVisible = true;
		enabled = false;
	}
}
