package crl.action.renegade;

import crl.Main;
import crl.action.HeartAction;
import crl.level.Cell;
import crl.level.Level;
import crl.player.Player;
import sz.util.Line;
import sz.util.Position;

public class ShadeTeleport extends HeartAction {
	
	public String getID() {
		return "ShadeTeleport";
	}
	
	public int getHeartCost() {
		return 5;
	}
	
	public boolean needsPosition(){
		return true;
	}

	public String getPromptPosition(){
		return "Where do you want to blink?";
	}

	public void execute(){
		super.execute();
		Player player = (Player) performer;
		Level level = player.level;
		level.addMessage("You wrap in your cape and dissapear!");
		if (targetPosition.equals(performer.getPosition())){
        	level.addMessage("You appear in the same place!");
        	return;
        }

		//crl.ui.effects.FlashEffect x = new FlashEffect(player.getPosition(), Appearance.GRAY);
		Line line = new Line(player.getPosition(), targetPosition);
		Position runner = line.next();
		int i = 0;
		for (; i < 8; i++){
			runner = line.next();
        	Cell destinationCell = performer.level.getMapCell(runner);
			if (
				level.isWalkable(runner) &&	
				destinationCell.getHeight() == level.getMapCell(player.getPosition()).getHeight() 
			)
				;
			else
				break;
		}
		drawEffect(Main.efx.createDirectedEffect(player.getPosition(), targetPosition, "SFX_SHADETELEPORT", i));
		
		player.setPosition(new Position(runner));
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