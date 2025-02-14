package crl.ui.consoleUI.effects;

import sz.csi.ConsoleSystemInterface;
import sz.util.*;
import crl.ui.consoleUI.ConsoleUserInterface;

public class CharSequentialEffect extends CharEffect {
	
	private Position[] sequence;
	private String charTiles;
	private int color;

	public CharSequentialEffect(String ID, Position[] sequence, String tiles, int color, int delay) {
		super(ID);
		setAnimationDelay(delay);
		this.charTiles = tiles;
		this.color = color;
		this.sequence = sequence;
	}

	// This style is bad because it treats 'fx' like there's only ONE OF them.
	// instead, fx should be batch updated in a broader context.
	public void drawEffect(ConsoleUserInterface ui, ConsoleSystemInterface si) {
		Position relative = Position.subs(getPosition(), ui.getPlayer().pos);
		Position center = Position.add(ui.PC_POS, relative);
		int tileIndex = 0;
		
		for (Position p: sequence) {
			Position nextPosition = Position.add(center, p);
			tileIndex++;
			tileIndex %= charTiles.length();
			if (ui.insideViewPort(nextPosition)) {
				si.print(nextPosition.x, nextPosition.y, charTiles.charAt(tileIndex), color);
			}
			animationPause();
		}
	}

}