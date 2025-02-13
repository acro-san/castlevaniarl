package crl.cuts;

import java.util.Hashtable;

import crl.game.Game;

// for the portrait index varnames:
import static crl.conf.gfx.data.Textures.*;

public class CutsceneDialogue {
	
	private static Hashtable<String, Chat> hashCuts;	// cutscenesByStringID
	static {
		hashCuts = new Hashtable<>();
		init();	// or 'load'
	}
	
	private static void init() {
		Chat c = null;
		c = new Chat();
		// Prologue Sequence
		c.add("Count Dracula", "We meet again Vampire Killer. You are old now.", PRT_DRACULA);
		c.add("Christopher Belmont", "I came here to fulfill my fate as a Belmont; age bears no relevance.", PRT_CHRIS);
		c.add("Count Dracula", "Look at your own self! And look at me, just reborn from warm innocent blood, you stand no chance against my power!", PRT_DRACULA);
		c.add("Christopher Belmont", "It is for that one same blood that my whip shall seek revenge against thee, dark lord.", PRT_CHRIS);
		c.add("Count Dracula", "HAHAHAHA! Don't make me laugh, pitiful excuse for a warrior, you shall regret your words!", PRT_DRACULA);
		hashCuts.put("PRELUDE_DRACULA1", c);
		
		c = new Chat();
		c.add("Soleiyu Belmont", "Father! I finally understand, I am here to confront my destiny, the destiny marked by my legacy!", PRT_SOLEIYU);
		c.add("Soleiyu Belmont", "... Father? FATHER!", PRT_SOLEIYU);
		c.add("Count Dracula", "Your father belongs to me now, you are late, son of a Belmont", PRT_DRACULA);
		c.add("Soleiyu Belmont", "No! NO! this.. this cannot be! You miserable monster! Die!", PRT_SOLEIYU);
		c.add("Count Dracula", "HAHAHAHA!", PRT_DRACULA);
		hashCuts.put("PRELUDE_DRACULA2", c);
		
		c = new Chat();
		c.add("Count Dracula", "Don't be such a fool! Your soul has always been my possession!", PRT_DRACULA);
		c.add("Soleiyu Belmont", "What! What is this! What happens to my body! ARGH!", PRT_SOLEIYU);
		hashCuts.put("PRELUDE_DRACULA3", c);
		
		c = new Chat();
		c.add("Soleiyu Belmont", "WARGH!!! WARRRGH!", PRT_SOLEIYU_D);
		c.add("Count Dracula", "HAHAHAHA! Now get out of my sight, shameful creature! I have important things to do.", PRT_DRACULA);
		hashCuts.put("PRELUDE_DRACULA4", c);
		
		// Intro Sequence
		c = new Chat();
		c.add("Melduck", "Bah, no luck! With the chariot like this we won't make it to Petra, and this creepy forest...", PRT_MELDUCK);
		c.add("Melduck", ". . . wait . . . Did you hear that?", PRT_MELDUCK);
		c.add("%NAME", "%%INTRO_1");
		/*
		 * This can be any of the following:
		 * {Player_VK}: This forest suffers as darkness corrupts its roots, it is my fate to get to Castlevania and fight my way through.
		 * {Player_VA}: The time is almost over, the trip over here will be wasted if I dont reach the castle soon.
		 * {Player_RE}: I can almost breathe the chaos that invades every tree and animal in this forest. It is time to return to the castle.
		 * {Player_IN}: I can sense all kind of dreaded souls lurking in here, I am afraid my master was right, I really need to reach the castle.
		 * {Player_MB}: I can hear, I can smell, I can sense the damned creatures that almost anihilated my brothers... tell me, where is the castle?
		 * {Player_KN}: Indeed, those seem to be Wargs: demonic wolf-like creatures summoned by the Count of Darkness to protect his castle. We better get to the castle quickly.
		 */
		c.add("Melduck", "Petra is to the northwest, and the castle of Dracula is just east of there... you better proceed on foot... but quickly, I'm afraid I can't get past the forest with my feet like this...", PRT_MELDUCK);
		c.add("%NAME", "You will be safe inside this ruined building. I will come back with help");
		hashCuts.put("INTRO_1", c);
		
		// Castle Entrance Sequence
		c = new Chat();
		// gfx version here had name as "???" instead of 'Mysterious Girl':
		c.add("Mysterious Girl", "What is this... another fool going into the castle... Who are you?", PRT_CLAW);
		c.add("%NAME", "My name is %NAME");
		c.add("Mysterious Girl", "Leave this castle now if you want to live! This is no place for commoners like you!", PRT_CLAW);
		c.add("%NAME", "%%CLARA1");
		/*
		 * This can be any of the following:
		 * {Player_VK}: I came here to fulfil my fate as a Vampire Killer, and destroy the dark count Dracula
		 * {Player_VA}: Get out of my way, I have issues to attend inside the castle
		 * {Player_RE}: Commoner? . . . Ignorant human... get out of my way.
		 * {Player_IN}: Something bigger than myself has got me here, you would never understand it... the castle calls me.
		 * {Player_MB}: Don't let appearances fool you... the blood of my brothers and sisters fills my veins, anger will get me through this damned place and to the heart of the count
		 * {Player_KN}: Can you not see the mark of heavens? I am a Knight of the Sacred Order, and I came to cleanse this place from darkness.
		 */
		c.add("Mysterious Girl", "Oh really? You don't have a clue about anything... this castle is a creature of chaos, and it will engulf you mercilessly.", PRT_CLAW);
		c.add("Mysterious Girl", "Goodbye fool, maybe we will meet inside... if you survive!", PRT_CLAW);
		hashCuts.put("CLARA1", c);

		// Castle Garden Sequence
		c = new Chat();
		c.add("%NAME", "What is this, a garden inside this foul castle?");
		c.add("%NAME", "And here we have yet another evil forger of darkness whom soul must be freed!");
		// gfx version here had "???" instead of "Mysterious Woman"
		c.add("Mysterious Woman", ". . . Stop where you are, son of a Belmont!", PRT_MAIDEN);
		c.add("%NAME", "What? How do you...");
		c.add("Mysterious Woman", "You have come pretty far on the castle; this place is safe for you, for now.", PRT_MAIDEN);
		c.add("%NAME", "But... who are ---");
		c.add("Mysterious Woman", "I am known as Heliann, the blacksmith maiden. I inhabit the villa of Castlevania, I am here to help the Belmonts to find their path.", PRT_MAIDEN);
		c.add("Heliann", "We are running out of time though... the count is using the souls from the grand Belmonts to perform a ritual that will be catastrophic for the world, to open the portal to hell!", PRT_MAIDEN);
		c.add("%NAME", "If they couldn't stop him, I doubt I will be able to!");
		c.add("Heliann", "Only way to know if you are ready is to confront your fate; death will be the price to pay if you are not the chosen one to carry on with the Belmont legacy!", PRT_MAIDEN);
		c.add("Heliann", "Take this key, it opens the castle dungeon, from there you can reach the clock tower, and finally, the castle keep. That is the only way to go. Be careful %NAME Belmont.", PRT_MAIDEN);
		hashCuts.put("MAIDEN1", c);
		
		// Dracula Sequence
		c = new Chat();
		c.add("%NAME", "Your reign of blood ends here, Count Dracula!");
		// gfx version here had added "The color of your soul... " before 'Amusing'.
		c.add("Count Dracula", "Amusing... A Belmont!", PRT_DRACULA);
		c.add("%NAME", "And I am here to vanquish you for good. Prepare to fight!");
		c.add("Count Dracula", "HAHAHA! Humans! Mankind! Carrying hope as their standard, is it true that you cannot see everything is doomed?", PRT_DRACULA);
		c.add("Count Dracula", "Can you not see that you are not the heir of the night hunters? They are mine, already!", PRT_DRACULA);
		c.add("%NAME", "No! our blood will never be yours, our fate will never dissapear, as long as you are a threat to men!");
		c.add("Count Dracula", "You already rennounced everything when you ran away from your destiny!", PRT_DRACULA);
		c.add("Count Dracula", "Yes, the son of the Belmont, the one true hunter, with his demise he condemned this world!", PRT_DRACULA);
		c.add("%NAME", "Stop talking! It's time to fight!");
		c.add("Count Dracula", "HAHAHA!", PRT_DRACULA);
		hashCuts.put("DRACULA1", c);
		
		c = new Chat();
		c.add("Count Dracula", "Argh! You are strong, Belmont! But not enough... HAHAHA!", PRT_DRACULA);
		c.add("%NAME", "What?");
		c.add("Count Dracula", "TASTE MY TRUE POWER!", PRT_DRACULA);
		hashCuts.put("DRACULA2", c);
		
		c = new Chat();
		c.add("Count Dracula", "HAHAHA! It is worthless! As long as light exists on this world, I shall return as darkness made flesh!", PRT_DRACULA);
		c.add("%NAME", "And my Belmont heirs, will ever be there to vanquish you again. DIE!");
		c.add("Count Dracula", "This... cannot be! NOOOOOOOOOOOOOOOOOOOO!!!!!", PRT_DRACULA);
		hashCuts.put("DRACULA3", c);
		
		// First Death Sequence
		c = new Chat();
		c.add("Death", "The color of your soul, a Belmont?", PRT_DEATH);
		c.add("%NAME", "Indeed, and I am here to vanquish your lord for good.");
		c.add("Death", "I admire your attitude, but I am afraid THAT won't be enough... how dare you challenge the Castle of Chaos mortal!", PRT_DEATH);
		c.add("Death", "I would stop you here if I wanted to, but that would be much of a swift death for one that dares to challenge this castle! You will die as your brothers, and your soul will haunt this place for eternity!", PRT_DEATH);
		hashCuts.put("DEATH_HALL", c);
		
		// Soleiyu Belmont Fight Sequence
		c = new Chat();
		c.add("Soleiyu Belmont", "Begone... Your quest ends here", PRT_SOLEIYU_D);
		c.add("%NAME", "It is not time yet... I won't give up... YOU can't give up!");
		c.add("Soleiyu Belmont", "It is all worthless, all our efforts... A world of light is a dream built up by dreamers, for dreamers.", PRT_SOLEIYU_D);
		c.add("Soleiyu Belmont", "Your fight is a pitiful struggle against the natural evolution of universe. A blindfolded battle against something you don't event know about. The time has come for a new ordeal.", PRT_SOLEIYU_D);
		c.add("%NAME", "Our fight is something far more important than ourselves. Our fate is to protect all that is good in the world. You must never forget that... everything is important!");
		c.add("Soleiyu Belmont", "Shut up! And DIE!", PRT_SOLEIYU_D);
		hashCuts.put("BADSOLEIYU1", c);
		
		// Soleiyu Belmont Death Sequence
		c = new Chat();
		c.add("Soleiyu Belmont", "I am done for... I deserve this death...", PRT_SOLEIYU_D);
		c.add("%NAME", "Soleiyu! don't give up!");
		c.add("Soleiyu Belmont", "It is all over... my father, the only vampire killer... he is dead, because of me! I carry the burden of the destruction of this world... I DO!", PRT_SOLEIYU_D);
		c.add("%NAME", "What?");
		c.add("Soleiyu Belmont", "Shut up! Let me go... ARGH!!", PRT_SOLEIYU_D);
		hashCuts.put("BADSOLEIYU2", c);
		
		// Soleiyu Belmont Saved Sequence
		c = new Chat();
		c.add("Soleiyu Belmont", "What is... that sound!", PRT_SOLEIYU_D);
		c.add("%NAME", "This music box... is it yours?");
		c.add("Soleiyu Belmont", "That melody... father! I promised to never let you down, but I was not born to be a Vampire Killer!", PRT_SOLEIYU_D);
		c.add("Soleiyu Belmont", "No! that's wrong... empty promises, all of them worthless, I must destroy you at all costs!", PRT_SOLEIYU_D);
		c.add("%NAME", "I don't want to fight you!");
		c.add("Soleiyu Belmont", "My head! it hurts... it hurts! LET ME GO!", PRT_SOLEIYU_D);
		hashCuts.put("SAVESOLEIYU", c);
		
		// Girls Sequence 1
		c = new Chat();
		c.add("Mysterious Girl", "ARGH!!", PRT_CLAW);
		c.add("Sorceress?", "Give up now foolish girl! There is no place for your games in this place. Leave now, we do not need you!", PRT_VINDELITH);
		c.add("Mysterious Girl", "I will not give up, NEVER!", PRT_CLAW);
		c.add("Sorceress?", "Then we have a problem, and I only see one way to fix it!", PRT_VINDELITH);
		c.add("Sorceress?", "Forgive me for what I am about to do, but you must understand... I am sure they will understand", PRT_VINDELITH);
		c.add("Mysterious Girl", "I am tired of your words... now fall to my claws!", PRT_CLAW);
		hashCuts.put("VINDELITH1", c);
		
		c = new Chat();
		
		c.add("Sorceress?", "Girl, you are strong... stronger than I thought...", PRT_VINDELITH);
		c.add("Mysterious Girl", "Now don't blame me Vindelith... you asked for this", PRT_CLAW);
		c.add("Vindelith", "I hope you realize what you are doing now, the time is running out and you are just slowing us down!", PRT_VINDELITH);
		c.add("Mysterious Girl", "I don't have to listen to you... farewell", PRT_CLAW);
		c.add("Vindelith", "Wait!", PRT_VINDELITH);
		hashCuts.put("VINDELITH2", c);
		
		// Fight with Death
		c = new Chat();
		c.add("Death", "HAHAHAHAHAH! You have arrived... I am impressed!", PRT_DEATH);
		hashCuts.put("DEATH1", c);
		
		// Girls Sequence 2
		c = new Chat();
		c.add("Short haired girl", "What a nasty surprise... another annoying bug comes to stop our advancement", PRT_CLARA);
		c.add("%NAME", "I am not sure I know what you mean");
		c.add("Vindelith", "I saw this person back when Claw beat me up, perhaps they are a team", PRT_VINDELITH);
		c.add("Short haired girl", "Claw... I am tired of hearing that name! Your friend has been a nuisance for us for too long!", PRT_CLARA);
		c.add("%NAME", "I do not know who you are talking about!");
		c.add("Vindelith", "So, who are you, and why are you here?", PRT_VINDELITH);
		c.add("%NAME", "I am %NAME Belmont, I came here to kill count Dracula.");
		c.add("Short haired girl", "Belmont? So, you too are claiming to belong to the Belmont Heritage? That means we have a problem here!", PRT_CLARA);
		c.add("Vindelith", "Clara! We can't afford to lose any more time fiddling with scumbags like this... let's push ahead!", PRT_VINDELITH);
		c.add("Clara", "You are right... I have seen enough fake Belmonts lately... Just a word of advice \"Belmont\": get the hell out of here while you can!", PRT_CLARA);
		hashCuts.put("VINDELITH3", c);
	}


	public static Chat get(String ID) {
		Chat ret = hashCuts.get(ID);
		// TODO This is not an exception. Run an asset-checker loop or somesuch
		// to ensure every ID needed is defined. Dev controls all asset files!
		if (ret == null) {
			Game.crash("Couldnt find Chat "+ID, new Exception());
		}
		return ret;
	}

/*
	public static BufferedImage getPortraitForPlayer(Player p) {
		int male0 = Textures.PRT_M1;
		if (p.sex == Player.MALE) {
			int idx = male0 + p.playerClass;
			return Textures.portraits[idx];
		} else {
			int female0 = Textures.PRT_F1;
			int idx = female0 + p.playerClass;
			return Textures.portraits[idx];
		}
	}
*/

}
