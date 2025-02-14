package crl.cuts.prelude;

import crl.Main;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.level.Level;
import crl.ui.Display;

public class Prelude5 extends Unleasher {
	public void unleash(Level level, Game game) {
		if (!level.getFlag("CHRIS_DEAD") || level.getMonsterByID("PRELUDE_DRACULA").getHits () > 50)
			return;
		Display.thus.showChat("PRELUDE_DRACULA3", game);
		Main.ui.drawEffect(Main.efx.createLocatedEffect(level.getPlayer().pos, "SFX_MORPH_SOLEIYU"));
		Main.ui.refresh();
		Display.thus.showChat("PRELUDE_DRACULA4", game);
		//level.getMonsterByID("PRELUDE_DRACULA").execute(ActionFactory.getActionFactory().getAction("PRELUDE_MORPH_SOLEIYU"));
		//Display.thus.showChat(CharCuts.thus.get("PRELUDE_DRACULA4"));
		Display.thus.showScreen(
			"Soleiyu arrived far too late to Dracula's dwelling... His "+
			"father's body lay upon the cold marble floor, in grim "+
			"account of the battle just finished. The Count, seemingly unscathed, "+
			"reclined in his "+
			"throne, sipping blood from an ornate glass.\nSoleiyu, feeling the weight of "+
			"his treason over his legacy, full of hatred against the murderer "+
			"of his father, but more against himself for not following the "+
			"path destiny had marked for him, he rushed against Dracula, "+
			"who with a lone raised hand and slight gesture of dark "+
			"manipulation, stopped the warrior and made him take again the "+
			"shape that still lay deep inside him");
		
		Display.thus.showScreen(
			"Darkness swept the world, as all hopes seemed to be lost, and "+
			"long forgotten Belmont bloodlines had to rise again and take "+
			"their paths again to the cursed castle. The time has come for "+
			"the future of mankind to be decided.");
		game.exitGame();
		//game.newGame();
	}

}
