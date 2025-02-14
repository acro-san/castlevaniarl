package crl.ui.consoleUI.effects;

import crl.ui.consoleUI.ConsoleUserInterface;
import sz.csi.ConsoleSystemInterface;
import sz.util.*;
import crl.Main;

public class CharIconMissileEffect extends CharDirectedEffect {
	private char missile;
	private int misColor;
	
	public void drawEffect(ConsoleUserInterface ui, ConsoleSystemInterface si){
		Main.ui.getPlayer().see();
		Main.ui.refresh();
		
		Position oldPoint = effectLine.next();
		int oldColor = -1;
		char oldChar = ' ';
		
		for (int i = 0; i < depth; i++) {
			Position next = effectLine.next();
			if (i != 0){
				Position relative = Position.subs(oldPoint, ui.getPlayer().getPosition());
				Position toPrint = Position.add(ui.PC_POS, relative);
				si.print(toPrint.x, toPrint.y, ""+oldChar, oldColor);
			}
		
			oldPoint = new Position(next);
			char icon = missile;
			
			Position relative = Position.subs(next, ui.getPlayer().getPosition());
			Position toPrint = Position.add(ui.PC_POS, relative);
			if (!ui.insideViewPort(toPrint))
				break;
			oldChar = si.peekChar(toPrint.x, toPrint.y);
			oldColor = si.peekColor(toPrint.x, toPrint.y);
			si.safeprint(toPrint.x, toPrint.y, icon, misColor);
			animationPause();
		}
	}

	public CharIconMissileEffect(String ID, char missile, int misColor,  int delay){
		super(ID);
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