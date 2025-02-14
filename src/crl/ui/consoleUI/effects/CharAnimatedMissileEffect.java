package crl.ui.consoleUI.effects;

import sz.csi.ConsoleSystemInterface;
import sz.util.Position;
import crl.ui.consoleUI.ConsoleUserInterface;

public class CharAnimatedMissileEffect extends CharDirectedEffect {
	private String missile;
	private int misColor;

	public void drawEffect(ConsoleUserInterface ui, ConsoleSystemInterface si){
		ui.getPlayer().see();
		ui.refresh();
		
		Position oldPoint = effectLine.next();
		int oldColor = ConsoleSystemInterface.WHITE;
		char oldChar = ' ';
		int too = 0;
		// i'm a bit confused why it's looping over 'depth' and not all 
		// the 'characters' in the animated missile? but kinda is over 'too'?
		for (int i = 0; i < depth; i++) {
			Position next = effectLine.next();
			if (i != 0) {
				Position relative = Position.subs(oldPoint, ui.getPlayer().pos);
				Position toPrint = Position.add(ui.PC_POS, relative);
				si.safeprint(toPrint.x, toPrint.y, oldChar, oldColor);
			}
		
			oldPoint = new Position(next);
			char icon = missile.charAt(too);
			too++;
			too %= missile.length();
			
			Position relative = Position.subs(next, ui.getPlayer().pos);
			Position toPrint = Position.add(ui.PC_POS, relative);
			if (!ui.insideViewPort(toPrint)) {
				//break;
				continue;	//
			}
			oldChar = si.peekChar(toPrint.x, toPrint.y);
			oldColor = si.peekColor(toPrint.x, toPrint.y);
			si.print(toPrint.x, toPrint.y, icon, misColor);
			animationPause();
		}
	}

	public CharAnimatedMissileEffect(String id, String missile, int misColor, int delay) {
		super(id);
		setMissile(missile);
		setMisColor(misColor);
		setAnimationDelay(delay);
	}

	public void setMissile(String value) {
		missile = value;
	}

	public void setMisColor(int value) {
		misColor = value;
	}


}
