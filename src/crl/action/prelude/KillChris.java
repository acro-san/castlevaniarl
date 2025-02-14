package crl.action.prelude;

import sz.util.Position;
import crl.Main;
import crl.action.Action;
import crl.level.Level;
import crl.player.Player;

public class KillChris extends Action {

	public String getID() {
		return "PRELUDE_KILL_CHRIS";
	}
	
	public void execute() {
		//Monster aMonster = (Monster)performer;
		Level aLevel = performer.level;
		Player aPlayer = aLevel.getPlayer();
		aLevel.addMessage("Dracula invokes a deadly beam of chaos energy!");
		Position pp = aPlayer.pos;
		drawEffect(Main.efx.createLocatedEffect(new Position(pp.x, pp.y), "SFX_KILL_CHRIS"));
	}

	public int getCost() {
		return 50;
	}

}