package crl.action.vkiller;

import sz.util.Line;
import sz.util.Position;
import crl.Main;
import crl.action.AT;
import crl.action.HeartAction;
import crl.level.Cell;
import crl.level.Level;
import crl.player.Player;

public class WarpDash extends HeartAction {
	
	public int getHeartCost() {
		return 3;
	}

	public AT getID(){
		return AT.WarpDash;//"WarpDash";
	}
	
	public boolean needsPosition(){
		return true;
	}

	public String getPromptPosition() {
		return "Where do you want to dash?";
	}

	public void execute(){
		super.execute();
		Player player = (Player) performer;
		Level level = player.level;
		level.addMessage("You dash and disappear!");
		if (targetPosition.equals(performer.pos)) {
			level.addMessage("You appear in the same place!");
			return;
		}

		Line line = new Line(player.pos, targetPosition);
		Position runner = line.next();
		int i = 0;
		for (; i < 5; i++){
			runner = line.next();
			Cell destinationCell = performer.level.getMapCell(runner);
			if (
				destinationCell == null ||
				destinationCell.isSolid() ||
				destinationCell.getHeight() != level.getMapCell(player.pos).getHeight()
			)
				break;
			
		}
		player.landOn(new Position(runner));
		drawEffect(Main.efx.createDirectedEffect(performer.pos, targetPosition, "SFX_WARPDASH", i));
		player.see();
		Main.ui.refresh();
	}

	public int getCost(){
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.4);
	}
	
	public String getSFX(){
		return "wav/scrch.wav";
	}
}