package crl.ui.graphicsUI.effects;

import java.awt.Image;

import sz.util.Position;
import crl.ui.graphicsUI.GFXUserInterface;
import crl.ui.graphicsUI.SwingSystemInterface;

public class GFXAnimatedMissileEffect extends GFXDirectedEffect{
	private Image[] missile;
	
	public GFXAnimatedMissileEffect(String id, Image[] missile, int delay) {
		super(id, delay);
		setMissile(missile);
	}
	

	public void drawEffect(GFXUserInterface ui, SwingSystemInterface si){
		si.saveBuffer();
		int too = 0;
		
		for (int i = 0; i < depth; i++){
			Position next = effectLine.next();
			too++;
			if (too == missile.length)
				too = 0;
			int height = 0;
			if (ui.getPlayer().level.getMapCell(next) != null)
				height = ui.getPlayer().level.getMapCell(next).getHeight();
			Position relative = Position.subs(next, ui.getPlayer().pos);
			Position toPrint = Position.add(ui.PC_POS, relative);
			if (!ui.insideViewPort(toPrint))
				continue;
			ui.drawImageVP(toPrint.x * 32, toPrint.y * 32 - 4 * height, missile[too]);
			si.refresh();
			animationPause();
			si.restore();
			
		}
	}

	public void setMissile(Image[] value) {
		missile = value;
	}


}
