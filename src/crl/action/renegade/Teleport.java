package crl.action.renegade;

import crl.Main;
import crl.action.HeartAction;
import crl.level.Cell;
import crl.level.Level;
import crl.player.Player;
import sz.util.Line;
import sz.util.Position;

public class Teleport extends HeartAction {
	
	public String getID() {
		return "Teleport";
	}
	
	public int getHeartCost() {
		return 5;
	}
	
	public boolean needsPosition() {
		return true;
	}

	public String getPromptPosition() {
		return "Where do you want to blink?";
	}

	public void execute() {
		super.execute();
		Player player = (Player) performer;
		Level level = player.level;
		level.addMessage("You wrap in your cape and dissapear!");
		if (targetPosition.equals(performer.pos)) {
			level.addMessage("You appear in the same place!");
			return;
		}

		//crl.ui.effects.FlashEffect x = new FlashEffect(player.pos, Appearance.GRAY);
		Line line = new Line(player.pos, targetPosition);
		Position runner = line.next();
		Position prerunner = new Position(runner);
		int i = 0;
		for (; i < 8; i++) {
			prerunner.x = runner.x;
			prerunner.y = runner.y;
			prerunner.z = runner.z;
			runner = line.next();
			Cell destinationCell = performer.level.getMapCell(runner);
			if (level.isWalkable(runner) && !level.isAir(runner) &&
				destinationCell.getHeight() == level.getMapCell(player.pos).getHeight()
			)
				;
			else
				break;
		}
		drawEffect(Main.efx.createDirectedEffect(player.pos, targetPosition, "SFX_TELEPORT", i));
		
		player.landOn(prerunner);
		player.see();
		Main.ui.refresh();
		
	}

	public int getCost() {
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.4);
	}
	
	public String getSFX() {
		return "wav/scrch.wav";
	}

}