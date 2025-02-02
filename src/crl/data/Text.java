package crl.data;

import crl.game.Game;

public class Text {
	/*
	si.printAtPixelCentered(centreX,(int)(388*scale), "b. Load Character", Color.WHITE);
	 */
	
	public static final String[]
		TITLE_MENU = {
		//Continue should be the first item if there are saves present. HOWEVER: Can we try to preload them and on fail,
		// see which ones are serialised out of date and can't be reloaded...!?
			"a. New Game",
			//"Journey Onward" - it was this on TextUI. or: "Load Character" on GUI.
			"b. Continue",	// grey out if no saves present! Or, DONT EVEN SHOW this item! Also: Continue should be FIRST!
			"c. Prologue",		// 'View'..!? PLAY (Drac encounter).
			"d. Training",
			"e. Arena",		// "Prelude Arena"? Why did it say that!? Why 'Prelude'?!
			"f. High Scores",		// needlessly wordy! 'High Scores'.
			// TODO OPTIONS!!
			"g. Quit",	//Esc!
		},
		
		TUI_CASTLE = {
			"                   /\\",
			"                  |  |",
			"                  |  |",
			"                  \\  / .",
			"                   || / \\",
			"                  / , | |",
			"                  | \\/' |.:",
			"                  ',      |",
			"                   |      |",
			"                  /      <",
			"                  |       |",
			"                 ,'       `\\",
			"               _.|          \\__",
			"__....__     ,'                `'--.",
			"        `''''                       `''",
		};
		
	
	
	public static final String
		
		TITLE_DISCLAIMER = "'CastleVania' is a trademark of Konami Corporation.",
		TITLE_GAME_VER_DEV = "CastlevaniaRL v"+Game.getVersion()+", Developed by Santiago Zapata 2005-2024",
		TITLE_MUSICCREDIT = "Midi Tracks by Jorge E. Fuentes, JiLost, Nicholas and Tom Kim",
		
		PROLOGUE_LINE0 = "In the year of 1691, a dark castle emerges from the "+
		"cursed soils of the plains of Transylvannia. Chaos and death spread "+
		"across the land, as the evil Count Dracula unleases his powers, "+
		"turning it into a pool of blood",
		
		PROLOGUE_LINE1 =
		"The trip to the castle was long and harsh, and after enduring many "+
		"challenges through all Transylvannia, you are close to the castle of "+
		"chaos. You are almost at Castlevania, and you are here on business: " +
		"To destroy forever the Curse of the Evil Count.";
		
	
	
}
