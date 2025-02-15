package crl.ui.graphicsUI.effects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import sz.util.Position;
import crl.conf.gfx.data.GFXConfiguration;
import crl.ui.graphicsUI.GFXUserInterface;
import crl.ui.graphicsUI.SwingSystemInterface;

public class GFXCircleBlastEffect extends GFXEffect {
	private Color blastColor;
	private static final int ADVANCE = 9;
	
	// Only needed at render time to draw the effect at proper position on
	// screen. So: UNIMPORTANT for the *data definition* of the EFFECT.
///	private GFXConfiguration conf;

	public GFXCircleBlastEffect(String ID, Color blastColor, int delay) {
		super(ID, delay);
		this.blastColor = blastColor;
	//	conf = configuration;
	}


	public void drawEffect(GFXUserInterface ui, SwingSystemInterface si) {
		ui.refresh();
		si.saveBuffer();
		Position relative = Position.subs(getPosition(), ui.getPlayer().pos);
		Position center = Position.add(ui.PC_POS, relative);
		Graphics2D g = si.getGraphics2D();
		Stroke oldStroke = g.getStroke();
		g.setStroke(new BasicStroke(10));
		g.setColor(blastColor);
		
		final int
			tw = ui.conf.tileWidth,
			halfw = tw / 2;
		
		int xcenter = center.x * tw + halfw;
		int ycenter = center.y * tw + halfw;
		for (int i = 0; i < 30; i++){
			g.fillOval(xcenter-i*(ADVANCE+i), ycenter-i*(ADVANCE+i),i*(ADVANCE+i)*2,i*(ADVANCE+i)*2);
			si.refresh();
			animationPause();
			//si.restore();
		}
		g.setStroke(oldStroke);
		si.cls();
		si.restore();
		si.refresh();
	}
}