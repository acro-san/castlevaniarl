package crl.ui.graphicsUI.effects;

import java.awt.image.BufferedImage;

import sz.util.Position;
import crl.action.Action;
import crl.ui.graphicsUI.GFXUserInterface;
import crl.ui.graphicsUI.SwingSystemInterface;

public class GFXAnimatedMeleeEffect extends GFXDirectionalEffect {
	
	private BufferedImage[][] frames;
	private Position[][] variations;

	public GFXAnimatedMeleeEffect(String ID, BufferedImage[][] frames, Position vars[][], int delay) {
		super(ID, delay);
		setMissile(frames);
		variations = vars;
	}
	
	
	public void drawEffect(GFXUserInterface ui, SwingSystemInterface si) {
		BufferedImage[] sequence = null;
		Position[] vars = null;
		si.saveBuffer();
		setAnimationDelay(animationDelay);
		
		switch (direction){
		case Action.RIGHT:
			sequence = frames[4];
			vars = variations[4];
			break;
		case Action.UP:
			sequence = frames[1];
			vars = variations[1];
			break;
		case Action.LEFT:
			sequence = frames[3];
			vars = variations[3];
			break;
		case Action.DOWN:
			sequence = frames[6];
			vars = variations[6];
			break;
		case Action.DOWNRIGHT:
			sequence = frames[7];
			vars = variations[7];
			break;
		case Action.DOWNLEFT:
			sequence = frames[5];
			vars = variations[5];
			break;
		case Action.UPLEFT:
			sequence = frames[0];
			vars = variations[0];
			break;
		case Action.UPRIGHT:
			sequence = frames[2];
			vars = variations[2];
			break;
		}
		if (sequence == null){
			si.restore();
			si.refresh();
			return;
		}
			
		Position runner = getPosition();
		for (int i = 0; i < depth; i++){
			int height = 0;
			if (ui.getPlayer().level.getMapCell(runner) != null)
				height = ui.getPlayer().level.getMapCell(runner).getHeight();
			Position relative = Position.subs(runner, ui.getPlayer().pos);
			Position toPrint = Position.add(ui.PC_POS, relative);
			/*if (!ui.insideViewPort(toPrint))
				break;*/
			toPrint = new Position(toPrint.x * 32, 
					               toPrint.y * 32 - 4 * height);
			toPrint = Position.add(toPrint, vars[i]);
			ui.drawImageVP(toPrint.x, toPrint.y, sequence[i]);
			si.refresh();
			animationPause();
			si.restore();
		}
	}


	public void setMissile(BufferedImage[][] frames) {
		this.frames = frames;
	}


}