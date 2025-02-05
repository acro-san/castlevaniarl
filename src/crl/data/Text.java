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
		},
		
		
		QUIT_MESSAGES = {
			"Do you really want to abandon Transylvania?",
			"Quit now, and let the evil count roam the world free?",
			"Leave now, and lose this unique chance to fight for freedom?",
			"Abandon the people of Transylvania?",
			"Deceive everybody who trusted you?",
			"Throw your weapons away and live a 'peaceful' life?"
		},
		
		VERBOSE_SKILLS = {
			"Unskilled",
			"Mediocre(1)",
			"Mediocre(2)",
			"Mediocre(3)",
			"Trained(1)",
			"Trained(2)",
			"Trained(3)",
			"Skilled(1)",
			"Skilled(2)",
			"Skilled(3)",
			"Master"
		},
		
		HOSTAGE_RESCUE_TIPS = {
			"If you dont kill the Aluras quickly, you are dead",
			"The Nova Skeleton has a deadly secret, dont let him let you know about it",
			"It is better to run from Iron Golems!",
			"All caves have two keys to find!",
			"You must not face Dracula unless you have enough shields",
			"Dracula turns into a Demon uppon death",
			"I met a Knight which could use all the ultimate weapon skills"
		},
		
		DEATH_MESSAGES = {
			"You are dead... and Dracula is still alive",
			"All hopes are lost.",
			"It's the end.",
			"Let us enjoy this evening for pleasure, the night is still young...",
			"Game Over",
			"Better luck next time, Son of a Belmont"
		},
		
		RANDOM_PLAYER_MALE_NAMES = {"Slash", "Simon", "Trevor", "Alan", "Reinhart", "Juste", "Alucard", "Daniel"},
		RANDOM_PLAYER_FEMALE_NAMES = {"Eliann","Sonia", "Valentina", "Carrie", "Sypha", "Mina"},
		
		// Indexed by character class ID (see Player/ PlayerGenerator).
		/* It's:
		CLASS_VAMPIREKILLER = 0,
		CLASS_RENEGADE = 1,
		CLASS_VANQUISHER = 2,
		CLASS_INVOKER = 3,
		CLASS_MANBEAST = 4,
		CLASS_KNIGHT = 5; */
		CLASS_DESCRIPTIONS = {
			"Heir of the Belmont fate, destined to confront the dark Count or die "+
			"trying. Master in the use of the mystic Vampire Killer whip and the "+
			"only human able to use mystic weapons",
			
			"A Vampire turned to the side of light, %%his will is to cleanse the world "+
			"of the Count's dark influence. Adept with weapons and wielder of "+
			"vampiric skills.",
			
			"A human envowed since childhood with mystical powers to fight darkness, %%he is able "+
			"to enchant weapons, and learn spells from the dark tomes hidden within the castle",
			
			"Able to manipulate spirits and bestow them a physical form, to bring "+
			"havoc upon its enemies.",	// could be clearer/better. 'its'? the spirit?
			
			"Fighting with an ancient martial style, %%his racial powers allow %%him"+
			" to transform into a powerful bestial creature. Can withstand more "+
			"damage than common humans.",
			
			"An agent from the church, trained to defend the world from chaos and "+
			"armed with the most advanced human weaponry"
		},
		
		VANQUISHER_PLOTS = {
			"After being taken to the castle by a group of skeletons, ",
			"After discovering %%SEX powers when %%SEX friends were killed by the armies of the count",
			"Interpreting the latest events as a call of destiny for a need to be fulfilled with %%SEX powers",
			"After being brought to the castle door by a mysterious cartman",
			"After being chased as a witch on %%SEX home town"
		},
		
		VKILLER_PLOTS = {
			"On a last attempt to rescue %%SEX loved one, which was taken by an evil daemon last night",
			"Seeking to bring an end to the problem as %%SEX grandfather did, about 100 years ago",
			"After seeing %%SEX home town leveled, and all of %%SEX friends slain by the dark armies of the count",
			"Finally seeing an opportunity to drop some vampire blood",
			"After a long trip around all transylvannia, becoming stronger"
		},
		
		RENEGADE_PLOTS = {
			"Coming back to the castle which once was %%SEX home",
			"Looking for a way to redeem the evil brought by the vampire race to the mankind",
			"After being mysteriously awaken from %%SEX self imposed eternal slumber",
			"Looking forward to give this 'Dark Count' a lesson about true power",
			"After being told about a powerful vampire breaking the sacred pact of peace made with mankind",
			"After rebelling against %%SEX father and being shunned by the vampire race"
		},
		
		INVOKER_PLOTS = {
			"Feeling the call of destiny to make use of %%SEX powers",
			"After taking monsters out of %%SEX village, using %%SEX supernatural powers",
			"Finally seeing an oportunity to show off %%SEX powers to battle evil",
			"Going after a secret tome, told to be hidden inside the castle",
			"After years of study and preparation, reading ancient tomes and discovering %%SEX true powers"
		},
		
		MANBEAST_PLOTS = {
			"After seeing %%SEX village burned by Dracula's minions, and looking forward to avenge the death of %%SEX loved ones",
			"As one of the few survivors from the raids of the legion of the undead, and looking forward to avenge the death of %%SEX loved ones",
			"After being taken captive by a group of careless skeletons, smashed when %%SEX power awoke",
			"After having learned of the destruction of the manbeast race, and abandoning %%SEX secret hideout in the caverns"
		},
		
		KNIGHT_PLOTS = {
			"After seeing %%SEX village burned by Dracula's minions, and looking forward to avenge the death of %%SEX father",
			"As the sole survivor of the company sent to battle the count Dracula on his castle",
			"On a last effort to rid the world from the influence of the darkness that rose again",
			"After years of preparation on %%SEX order, anticipating the return of the Dark Count"
		},

		VKILLER_DESCRIPTIONS = {
			"%%NAME, last member on the lineage of vampire hunters, the Belmonts",
			"%%NAME, the wielder of the vampire hunter fate",
			"%%NAME",
			"%%NAME the Vampire Hunter"
		},
		
		VANQUISHER_DESCRIPTIONS = {
			"%%NAME, heir of the powers of Sypha",
			"%%NAME, a witch for some, the last hope for anothers,",
			"%%NAME",
			"%%NAME the slayer"
		},
		
		RENEGADE_DESCRIPTIONS = {
			"%%NAME, heir to the throne of darkness",
			"%%NAME, a vampire turned to the light side",
			"%%NAME",
			"%%NAME the renegade"
		},
		
		INVOKER_DESCRIPTIONS = {
			"%%NAME, a mysterious resident of the woods of Transylvannia",
			"%%NAME, the human who discovered %%SEX fate was to battle evil using %%SEX powers",
			"%%NAME",
			"%%NAME the monster summoner"
		},
		
		MANBEAST_DESCRIPTIONS = {
			"%%NAME, the powerful beast morpher from the mountains",
			"%%NAME, the only survivor of the manbeast race",
			"%%NAME",
			"%%NAME the half-beast"
		},
		
		KNIGHT_DESCRIPTIONS = {
			"%%NAME, knight of the Order of Light",
			"%%NAME, the knight of light",
			"%%NAME",
			"%%NAME the sacred knight"
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
		
		ARENA_INTRO_LINE0 =
		"In the year of 1451, the castle of Dracula emerges from the dust of "+
		"the cursed soils of Transylvania.\n\nSonia, a girl choosen by fate "+
		"to be the first of vampire hunters, heads to the mysterious castle "+
		"seeking to finish the misery and havoc unleashed by the servants of "+
		"the Count of chaos, Dracula.\n\nWielding the sacred Vampire Killer "+
		"whip, and full of courage within her heart, she opens the porticullis"+
		" of the castle courtyard.",
		
		ARENA_INTRO_LINE1 = "Sonia crosses the castle courtyard, leaving behind"+
		" her all traces of light and venturing into the source of chaos "+
		"itself.\n\nSuddenly, she hears a voice unto her head... \"This is "+
		"not a place for mortals! Yet prove to be worthy...  and you may then "+
		"come in...\"",
	
		ARENA_INTRO_LINE2 = "All at once, monstrous creatures of every foul "+
		"kind crawl out of the soil, and soar in from the surrounding night,"+
		" and the castle doors are sealed shut.\n\nKnowing that there is no "+
		"way back, Sonia cracks her Vampire Killer whip...\n\n\n"+
		"And so this adventure begins...",	// could be tighter still.
		
		
		
		LEVEL3_INTRO_LINE0 = "A monstrous Warg appears out of nowhere! Remember to [j]ump, attack from safe distance (with [.]), and use your [p]owers!",
		
		
		TIME_DAYBREAK  = "THE MORNING SUN HAS VANQUISHED THE HORRIBLE NIGHT...",
		TIME_NIGHTFALL = "... WHAT A HORRIBLE NIGHT TO HAVE A CURSE",
		TIME_FOGGY = "A HEAVY FOG ENGULFS THE PLACE",
		TIME_RAIN  = "RAIN STARTS POURING FROM THE DARK SKY",
		TIME_STORM = "A THUNDERSTORM BREAKS LOOSE THE SPIRITS OF DARK",
		TIME_SUNNY = "A GENTLE SUN SHINES IN THIS GRAY DAY",
		
		
		
		
		MERCHANT_BUY_CONFIRM = "Thanks!, May I interest you in something else?",
		MERCHANT_BUY_FAIL_NOGOLD = "I am afraid you don\'t have enough gold",
		MERCHANT_BUY_CANCEL = "Too bad... May I interest you in something else?";	// TUI logic only!?
	
	
	public static final String[][] USE_CLUES = {
			{//Lv 0 clues
			"They say stairs are the safest place if you dont want to be moved",
			"The thorn bracelet exchanges pain for power",
			"Every 100 years, the castle rises as its lord revives",
			"They say the cross is the best weapon against Dracula",
			"Bibuti powder is a great weapon against slow enemies",
			"It is said that the powder can be used even in water",
			"The flame lasts less than the powder, but it is more powerful"
			},//Lv 1 clues
			{"..Secret treasures raised when I step on the spots..",
			"..And the books powered the mystic whip, giving it unlimited power..",
			"..Still, it was useless when used on the major monsters, as they were exempt from time..",
			"..And I discovered, the midget was unbeatable, but there was a way...",
			"..but the snakes were unimportant and just a distraction..",
			".. there I saw, always, a pair on them on the dark caverns..",
			".. and the mystic axe, able to cut through flying foes and spectres.."
			}, //Lv 2 clues
			{".. saw the Grim Reaper, saw the servant of Dracula..",
			".. and the weapon, the dark sickles to surround him..",
			".. finally, the void, and powered by the big stars..",
			".. and the menbeast were born, using the axe for the ancient skill...",
			".. and there was a pair of rings, and the sacred vanquisher sliced through time..",
			".. and the ultimate whip, born from the crystals..",
			".. confronted the ancient count face to face, certain death.."
			}
		};
	
	
	
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
