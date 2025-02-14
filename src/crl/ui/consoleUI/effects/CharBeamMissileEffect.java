package crl.ui.consoleUI.effects;

import crl.ui.consoleUI.ConsoleUserInterface;
import sz.csi.ConsoleSystemInterface;
import sz.util.*;
import crl.Main;


public class CharBeamMissileEffect extends CharDirectedEffect {
	private String missile;
	private int misColor;
	
	public void drawEffect(ConsoleUserInterface ui, ConsoleSystemInterface si) {
		Main.ui.getPlayer().see();
		Main.ui.refresh();
		// ?? Why not use console ui parameter?
		
		//Position oldPoint = 
		effectLine.next();
		int too = 0;
		for (int i = 0; i < depth; i++){
			Position next = effectLine.next();
			too++;
			if (too == missile.length())
				too = 0;
			char icon = missile.charAt(too);
			Position relative = Position.subs(next, ui.getPlayer().pos);
			Position toPrint = Position.add(ui.PC_POS, relative);
			if (!ui.insideViewPort(toPrint))
				break;
			si.safeprint(toPrint.x, toPrint.y, icon, misColor);
			animationPause();
		}
	}

	public CharBeamMissileEffect(String ID, String missile, int misColor, int delay){
		super(ID);
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