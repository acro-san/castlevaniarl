package crl.ui.consoleUI;

import crl.ui.Appearance;

public class CharAppearance extends Appearance {	// No. Not extends. This is not a class.
	
	private char character;
	private int color;

	public final static CharAppearance
		VOID = new CharAppearance("VOID", ' ', crl.ui.Colors.BLACK);
	
	public static CharAppearance getVoidAppearance() {
		return VOID;
	}
	
	public CharAppearance(String ID, char character, int color) {
		super(ID);
		this.character = character;
		this.color = color;
	}

	public char getChar(){
		return character;
	}

	public int getColor(){
		return color;
	}
	
}