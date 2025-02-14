package crl.ui.consoleUI.effects;

import crl.ui.consoleUI.ConsoleUserInterface;
import sz.csi.ConsoleSystemInterface;
import sz.util.*;

public class CharIconMissileEffect extends CharDirectedEffect {
	// SURELY this is IDENTICAL to an 'AnimatedMissile',
	// it just ONLY HAS ONE FRAME!??
	private char missile;
	private int misColor;
	
	public void drawEffect(ConsoleUserInterface ui, ConsoleSystemInterface si){
		ui.getPlayer().see();
		ui.refresh();
		
		Position oldPoint = effectLine.next();
		int oldColor = -1;	// not a valid colour idx.
		char oldChar = ' ';
		
		for (int i = 0; i < depth; i++) {
			Position next = effectLine.next();
			if (i != 0) {
				Position relative = Position.subs(oldPoint, ui.getPlayer().pos);
				Position toPrint = Position.add(ui.PC_POS, relative);
				si.print(toPrint.x, toPrint.y, oldChar, oldColor);
			}
		
			oldPoint = new Position(next);
			char icon = missile;
			
			Position relative = Position.subs(next, ui.getPlayer().pos);
			Position toPrint = Position.add(ui.PC_POS, relative);
			if (!ui.insideViewPort(toPrint)) {
				break;
			}
			oldChar = si.peekChar(toPrint.x, toPrint.y);
			oldColor = si.peekColor(toPrint.x, toPrint.y);
			si.safeprint(toPrint.x, toPrint.y, icon, misColor);
			animationPause();
		}
	}

	public CharIconMissileEffect(String id, char missile, int misColor, int delay) {
		super(id);
		setMissile(missile);
		setMisColor(misColor);
		setAnimationDelay(delay);
	}

	public void setMissile(char value) {
		missile = value;
	}

	public void setMisColor(int value) {
		misColor = value;
	}


}
