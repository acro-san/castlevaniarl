package crl.ui.graphicsUI.effects;

import java.awt.Image;

import sz.util.Position;
import crl.ui.graphicsUI.GFXUserInterface;
import crl.ui.graphicsUI.SwingSystemInterface;

public class GFXAnimatedEffect extends GFXEffect {
	private Image[] frames;
	
	private int xoff, yoff;
	
	public GFXAnimatedEffect(String ID, Image[] frames, int delay) {
		super (ID, delay);	//configurationGFXConfiguration configuration
		this.frames = frames;
	}
	
	public GFXAnimatedEffect(String ID, Image[] frames, int delay, int xoff, int yoff) {
		this (ID, frames, delay);	//GFXConfiguration configuration
		this.xoff = xoff;
		this.yoff = yoff;
	}

	public void drawEffect(GFXUserInterface ui, SwingSystemInterface si){
		int height = 0;
		if (ui.getPlayer().level.getMapCell(getPosition()) != null)
			height = ui.getPlayer().level.getMapCell(getPosition()).getHeight();
		Position relative = Position.subs(getPosition(), ui.getPlayer().pos);
		Position absolute = Position.add(ui.PC_POS, relative);
		if (!ui.insideViewPort(absolute))
			return;
		si.saveBuffer();
		for (int j = 0; j<frames.length; j++) {
			ui.drawImageVP(
				absolute.x * 32 + xoff,
				absolute.y * 32 - 4 * height + yoff, frames[j]);
			si.refresh();
			animationPause();
			si.restore();
		}
	}
}
