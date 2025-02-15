package crl.ui.graphicsUI.effects;

import java.awt.Image;

import sz.util.Position;
import crl.ui.graphicsUI.GFXUserInterface;
import crl.ui.graphicsUI.SwingSystemInterface;

public class GFXSequentialEffect extends GFXEffect {
	
	private Position[] sequence;
	private Image[] tiles;
	//private String charTiles;	// ascii for same.
	//private int color;	// FGC of the ascii chars.

	// KEY QUESTION: Why do the EFFECT DEFINITIONS require a 'GraphicsConfiguration'!?????
	// That seems like a re-entrant (wrong direction) dependency.
	public GFXSequentialEffect(String ID, Position[] sequence, Image[] tiles, int delay) {
		super(ID);
		setAnimationDelay(delay);
		this.tiles = tiles;
		this.sequence = sequence;
	}


	public void drawEffect(GFXUserInterface ui, SwingSystemInterface si) {
		si.saveBuffer();
		Position relative = Position.subs(getPosition(), ui.getPlayer().pos);
		Position center = Position.add(ui.PC_POS, relative);
		int tileIndex = 0;

		for (Position p: sequence) {
			Position nextPosition = Position.add(center, p);
			tileIndex++;
			tileIndex %= tiles.length;
			if (ui.insideViewPort(nextPosition)) {
				ui.drawImageVP(nextPosition.x * 32, nextPosition.y * 32, tiles[tileIndex]);
			}
			si.refresh();
			animationPause();
		}
		si.restore();
	}

}