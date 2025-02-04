package crl.data;

import crl.game.Game;

public class Text {
	
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
			// TODO OPTIONS!! Prefs-config screen within game, covering the current mess of properties files etc.
			// define the existing various screen/size setups as 'presets'.
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
		TITLE_GAME_VER_DEV = "CastlevaniaRL v"+Game.getVersion()+", Developed by Santiago Zapata 2005-2007, 2010, 2024",
		// (ac: unsure if this was going for more or less specific)
		TITLE_ARTCREDIT   = "Artwork by Christopher Barrett, 2006-2007",	// this notice not shown in the textmode UI
		TITLE_MUSICCREDIT = "Midi Tracks by Jorge E. Fuentes, JiLost, Nicholas and Tom Kim",
		
		FOO = 
		"In the year of 1691, a dark castle emerges from the cursed soils of "+
		"the plains of Transylvania. Chaos and death spread across the land, "+
		"as the evil Count Dracula unleases his powers, turning its forests "+
		"and lakes into a pool of blood.\n\n",	// "???ing its landscape into a grim and blood-soaked (waking) nightmare"
		FOO2 =
		"The trip to the castle was long and harsh, after enduring many challenges through all Transylvannia, "+
		"you are close to the castle of chaos. You are almost at Castlevania, and you are here on business: " + 
		"To destroy forever the Curse of the Evil Count.\n\n",
		
		PROLOGUE_LINE0 =
		"In the year of 1691, a dark castle emerges from the cursed soils of "+
		"the plains of Transylvania. Chaos and death spread across the land, "+
		"as the evil Count Dracula unleases his powers, turning it into a pool of blood",
		
		PROLOGUE_LINE1 =
		"The trip to the castle was long and harsh, and after enduring many "+
		"challenges through all Transylvania, you are close to the castle of "+
		"chaos. You are almost at Castlevania, and you are here on business: " +
		"To destroy forever the Curse of the Evil Count.",
		
	
		
		
		TIME_DAYBREAK  = "THE MORNING SUN HAS VANQUISHED THE HORRIBLE NIGHT...",
		TIME_NIGHTFALL = "... WHAT A HORRIBLE NIGHT TO HAVE A CURSE",
		TIME_FOGGY = "A HEAVY FOG ENGULFS THE PLACE",
		TIME_RAIN  = "RAIN STARTS POURING FROM THE DARK SKY",
		TIME_STORM = "A THUNDERSTORM BREAKS LOOSE THE SPIRITS OF DARK",
		TIME_SUNNY = "A GENTLE SUN SHINES IN THIS GRAY DAY",
		
		
		
		
		MERCHANT_BUY_CONFIRM = "Thanks!, May I interest you in something else?",
		MERCHANT_BUY_FAIL_NOGOLD = "I am afraid you don\'t have enough gold",
		MERCHANT_BUY_CANCEL = "Too bad... May I interest you in something else?";	// TUI logic only!?
	
	
	public static String getTimeChangeMessage(boolean day, boolean fog, 
		boolean rain, boolean thunderstorm, boolean sunnyDay)
	{
		String msg = day ? TIME_DAYBREAK: TIME_NIGHTFALL;
		if (fog) {
			msg += "\n\n"+TIME_FOGGY;
		}
		if (rain) {
			msg += "\n\n"+TIME_RAIN;
		}
		if (thunderstorm) {
			msg += "\n\n"+TIME_STORM;
		}
		if (sunnyDay) {
			msg += "\n\n"+TIME_SUNNY;
		}
		return msg;
	}


}
