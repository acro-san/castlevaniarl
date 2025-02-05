package crl.cuts.intro;

import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.cuts.Unleasher;
import crl.data.Text;
import crl.game.Game;
import crl.game.STMusicManagerNew;
import crl.level.Level;
import crl.monster.Monster;
import crl.monster.MonsterData;

public class Intro3 extends Unleasher {

	public void unleash(Level level, Game game) {
		if (level.getFlag("INTRO2") && level.getCounter("COUNTBACK_INTRO_2").isOver()) {
			level.addMessage( Text.LEVEL3_INTRO_LINE0 );
			Position playerFloor = new Position(level.getPlayer().getPosition());
			playerFloor.z = 2;
			while (true) {
				int xpos = Util.rand(3,5) * (Util.chance(50) ? 1 : -1);
				int ypos = Util.rand(3,5) * (Util.chance(50) ? 1 : -1);
				Position wargPosition = Position.add(playerFloor, new Position(xpos, ypos));
				if (level.isWalkable(wargPosition)){
					Monster warg = MonsterData.buildMonster("WARG");
					warg.setPosition(wargPosition);
					level.addMonster(warg);
					break;
				}
			}
			STMusicManagerNew.thus.playKey("WRECKAGE");
			level.setMusicKeyMorning("WRECKAGE");
			if (level.getNPCByID("MELDUCK") != null)
				level.getNPCByID("MELDUCK").setTalkMessage("On your way! I will take care of anything you leave here");
			enabled = false;
			level.removeCounter("COUNTBACK_INTRO_2");
			level.getPlayer().see();
			Main.ui.refresh();
		}
	}

}