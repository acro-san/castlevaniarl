package crl.ui.graphicsUI.effects;

import java.awt.Image;

import sz.util.Position;
import crl.ui.graphicsUI.GFXUserInterface;
import crl.ui.graphicsUI.SwingSystemInterface;

public class GFXIconEffect extends GFXEffect{
	private Image tile;

	public GFXIconEffect(String ID, Image tile, int delay) {
		super(ID);
		this.tile = tile;
		setAnimationDelay(delay);
	}

	public void drawEffect(GFXUserInterface ui, SwingSystemInterface si){
		si.saveBuffer();
		//si.setAutoRefresh(false);
		int height = 0;
		if (ui.getPlayer().level.getMapCell(getPosition()) != null)
			height = ui.getPlayer().level.getMapCell(getPosition()).getHeight();
		Position relative = Position.subs(getPosition(), ui.getPlayer().pos);
		Position center = Position.add(ui.PC_POS, relative);
		if (ui.insideViewPort(center))
			ui.drawImageVP(center.x * 32, center.y * 32 - 4 * height, tile);
		si.refresh();
		animationPause();
		si.restore();
	}
}