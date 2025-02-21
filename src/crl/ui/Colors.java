package crl.ui;

import java.awt.Color;

public class Colors {
	
	// Extracted from 'ConsoleSystemInterface': want same named colours available
	// in any graphics situation, regardless of some commandline 'mode' flag.
	
	public static final int
		BLACK = 0,
		DARK_BLUE = 1,
		GREEN = 2,
		TEAL = 3,
		DARK_RED = 4,
		PURPLE = 5,
		BROWN = 6,
		LIGHT_GRAY = 7,
		GRAY = 8,
		BLUE = 9,
		LEMON = 10,
		CYAN = 11,
		RED = 12,
		MAGENTA = 13,
		YELLOW = 14,
		WHITE = 15;
	

	// was private to sz.csi.wswing.WSwingConsoleInterface:
	private final static Color
		DARKRED_COLOR = new Color(128,0,0),
		DARKBLUE_COLOR = new Color(0,0, 200),
		DARKGREEN_COLOR = new Color(0,128,0),
		DARKMAGENTA_COLOR = new Color(128,0,128),
		TEAL_COLOR = new Color(0,128,128),
		BROWN_COLOR = new Color(128,128,0);


	public int getColor(String colorName) {
		if (colorName == null) return -1;
		if (colorName.equals("BLACK")) return BLACK;
		if (colorName.equals("DARK_BLUE")) return DARK_BLUE;
		if (colorName.equals("GREEN")) return GREEN;
		if (colorName.equals("TEAL")) return TEAL;
		if (colorName.equals("DARK_RED")) return DARK_RED;
		if (colorName.equals("PURPLE")) return PURPLE;
		if (colorName.equals("BROWN")) return BROWN;
		if (colorName.equals("LIGHT_GRAY")) return LIGHT_GRAY;
		if (colorName.equals("GRAY")) return GRAY;
		if (colorName.equals("BLUE")) return BLUE;
		if (colorName.equals("LEMON")) return LEMON;
		if (colorName.equals("CYAN")) return CYAN;
		if (colorName.equals("RED")) return RED ;
		if (colorName.equals("MAGENTA")) return MAGENTA; 
		if (colorName.equals("YELLOW")) return YELLOW;
		if (colorName.equals("WHITE")) return WHITE;
		return -1;
	}
	
	
	public static Color getColorFromCode(int code) {
		switch (code){
			case BLACK:
				return Color.BLACK;
			case DARK_BLUE:
				return DARKBLUE_COLOR;
			case GREEN:
				return DARKGREEN_COLOR;
			case TEAL:
				return TEAL_COLOR;
			case DARK_RED:
				return DARKRED_COLOR;
			case PURPLE:
				return DARKMAGENTA_COLOR;
			case BROWN:
				return BROWN_COLOR;
			case LIGHT_GRAY:
				return Color.LIGHT_GRAY;
			case GRAY:
				return Color.GRAY;
			case BLUE:
				return Color.BLUE;
			case LEMON:
				return Color.GREEN;
			case CYAN:
				return Color.CYAN;
			case RED:
				return Color.RED;
			case MAGENTA:
				return Color.MAGENTA;
			case YELLOW:
				return Color.YELLOW;
			case WHITE:
				return Color.WHITE;
			default:
				return null;
		}
	}

}
