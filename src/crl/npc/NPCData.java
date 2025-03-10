package crl.npc;

import java.util.*;
import crl.player.*;
import crl.Main;
import crl.ai.AIT;
import crl.item.*;
import sz.util.*;

public class NPCData {
	
	// static villager data definitions.
	private static final AIT VAI = AIT.VILLAGER;
	private static Hashtable<String, NPCDef> definitions = new Hashtable<>(40);
	private static Vector<String> hostages = new Vector<>();
	
	private static final NPCDef[] NPC_DEFS = {
		new NPCDef("WOMAN0","Woman","WOMAN",VAI, "First thing to do in this town is buy a White Crystal", 1,3,"Argh! You will pay for this!","Somebody, Help me!", false, false),
		new NPCDef("WOMAN1","Woman","WOMAN",VAI, "To restore your life, shout in front of the church", 1,3, "You are evil!", "Don't hurt me, please!", false, false),
		new NPCDef("WOMAN2","Woman","WOMAN",VAI, "Don't look into the Death Star, or you will die.", 1,3, "I will defend myself!", "Argh!!!", false, false),

		new NPCDef("OLDWOMAN0","Old Woman","OLDWOMAN",VAI, "They say a vampire killer is able to use mystic weapons", 1,3, "Don't underestimate me!", "Everybody stay alert!", false, false),
		new NPCDef("OLDWOMAN1","Old Woman","OLDWOMAN",VAI, "I saw the last of the menbeast heading towards the castle", 2,3, "You will pay for this!!!", "Intruder!", false, false),
		new NPCDef("OLDWOMAN2","Old Woman","OLDWOMAN",VAI, "A mysterious girl came by here asking for directions", 1,3, "I won't die alone!", "Help me!! somebody!!", false, false),

		new NPCDef("MAN0","Man","MAN",VAI, "Buy some Garlic. It has special powers", 2,4, "I was far too trusting!", "Hrrmmm", false, false),
		new NPCDef("MAN1","Man","MAN",VAI, "Take my daughter, please!", 2,3, "Defend yourself rogue!","You are stronger than I tought", false, false),
		new NPCDef("MAN2","Man","MAN",VAI, "Dracula Castle is just east of this town", 1,4, "What's wrong with you!", "You sold your powers to chaos!", false, false),

		new NPCDef("OLDMAN0","Old Man","OLDMAN",VAI, "The order of knights sent some men, but they didn't return", 2,3, "To the charge!", "Your soul is dark", false, false),
		new NPCDef("OLDMAN1","Old Man","OLDMAN",VAI, "A brave merchant went to Dracula's castle some months ago", 1,3, "Engarde","End my suffering...", false, false),
		new NPCDef("OLDMAN2","Old Man","OLDMAN",VAI, "They say vampires regain their health drinking blood", 1,4, "To the arms!", "Go ahead and kill me...", false, false),

		new NPCDef("PRIEST","Priest","PRIEST",VAI, "Rest here for a while.", 15,6, "How dare you attack me!", "Your mind will not hold for long", false, true),
		
		new NPCDef("MERCHANT","Merchant","MERCHANT",VAI, "Good day. I don't think I have anything for you.", 5,20, "Aha! Yet another one is possessed!", "This is the end... *GASP*", false, false),
		
		new NPCDef("DOG","Dog","DOG",VAI, "ROOF ROOF", 4,4, "GRRRRR!!!", "UUUF... UFF...", false, false),
		
		new NPCDef("VALENTINA","Valentina","HOSTAGE_GIRL",VAI, "Did you come to rescue me? Thanks!", 4,10, "Why are you doing this!", "Let my death be swift...", true, false),
		new NPCDef("MARIE","Marie","HOSTAGE_GIRL",VAI, "It was horrible! I am glad you finally arrived!", 4,10, "Why are you doing this!", "Let my death be swift...", true, false),
		new NPCDef("ANNETE","Annete","HOSTAGE_GIRL",VAI, "I knew you would come! Let's get out of here...", 4,10, "Why are you doing this!", "Let my death be swift...", true, false),
		new NPCDef("LAURA","Laura","HOSTAGE_GIRL",VAI, "Thanks God! I am saved!", 4,10, "Why are you doing this!", "Let my death be swift...", true, false),
		new NPCDef("ROSE","Rose","HOSTAGE_GIRL",VAI, "We are dead... this castle has no way out..", 4,10, "Why are you doing this!", "Let my death be swift...", true, false),
		new NPCDef("BLUE","Blue","HOSTAGE_GIRL",VAI, "You are my last hope...", 4,10, "Why are you doing this!", "Let my death be swift...", true, false),
		new NPCDef("MARCEL","Marsh","HOSTAGE_GIRL",VAI, "Deep inside me, I always knew you would come back", 4,10, "Why are you doing this!", "Let my death be swift...", true, false),
		new NPCDef("PROX","Prox","HOSTAGE_GUY",VAI, "Friend! Lets get the heck out of here!", 4,10, "Why are you doing this!", "Let my death be swift...", true, false),
		new NPCDef("KOREL","Korned","HOSTAGE_GUY",VAI, "Now I have a chance to get out of here!", 4,10, "Why are you doing this!", "Let my death be swift...", true, false),
		new NPCDef("ANUBIS","Anubis","HOSTAGE_GUY",VAI, "This castle is creepy...", 4,10, "Why are you doing this!", "Let my death be swift...", true, false),
		new NPCDef("ALENOME","Alenome","HOSTAGE_GUY",VAI, "I learned something, there is no light without darkness", 4,10, "Why are you doing this!", "Let my death be swift...", true, false),
		
		new NPCDef("MELDUCK","Melduck","MELDUCK",VAI, "Be on your guard...", 6,25, "What are you doing!", "I shall never have carried you here!", false, false),
		new NPCDef("UNIDED_CLAW","???","CLAW",VAI, "...", 10,40, "Die!", "Argh!", false, false),
		new NPCDef("UNIDED_VINDELITH","???","VINDELITH",VAI, "...", 10,40, "Die!", "Argh!", false, false),
		new NPCDef("CLAW","Claw","CLAW",VAI, "...", 10,40, "Die!", "Argh!", false, false),
		new NPCDef("VINDELITH","Vindelith","VINDELITH",VAI, "...", 10,40, "Die!", "Argh!", false, false),
		new NPCDef("UNIDED_CLARA","???","CLARA",VAI, "...", 10,40, "Die!", "Argh!", false, false),
		
		
		new NPCDef("MAIDEN","???","MAIDEN",VAI, "...", 20,60, "...", "...", false, false),
		new NPCDef("CHRISTRAIN","Christopher","CHRISTOPHER_BELMONT_NPC",VAI, "Go outside for a while, my child", 6,25, "Die Monster!", "You dont belong in this world!!", false, false),
		
		new NPCDef("ICEY","Icey","ICEY",VAI, "Name is Icey, Posh Icey. Would you join me for the tea-time? I need my bowler hat first.", 6,25, "For the great Fifur and justice!", "I am terribly posh!", false, false),
		new NPCDef("BARRY","Christopher","BARRETT",VAI, "Call me Barry, I'm this town's artist. Got any good job for me?", 6,25, "Die to my paintbrush!", "In the end, it doesn't even matters..", false, false),
		new NPCDef("LARDA","Larda","LARDA",VAI, "Don't be paranoid... rest here for a night, only 200 in gold? [y/n]", 15,40, "Fall to the power of my Hammer!", "It's a good day to die", false, false),
		new NPCDef("ELI","Eli","WOMAN",VAI, "The mystic key? beats me!", 2,4, "This is completely pointless", "A wasted effort, I will revive!", false, false),
		new NPCDef("MAN3","Man","MAN",VAI, "A crooked trader is offering bum deals in this town.", 2,4, "Somebody help me!", "Just help me!", false, false),
		new NPCDef("MAN4","Man","MAN",VAI, "After Castlevania, I warned you not to return.", 2,4, "Killing me will only reduce your score", "I deserve to die.", false, false),
		new NPCDef("WOMAN3","Woman","WOMAN",VAI, "I want to get to know you better.", 1,3, "I don't want to know anything else!", "Argh!!!", false, false),
		new NPCDef("WOMAN4","Woman","OLDWOMAN",VAI, "Laurels in your soup enhances it's aroma..", 1,3, "Garlic in your soup is awful!", "Argh!!!", false, false),
		new NPCDef("WOMAN5","Woman","WOMAN",VAI, "You look pale my son. You must rest in the church.", 1,3, "You are so mean!", "Argh!!!", false, false),
		
		new NPCDef("CUILOT","Cuilot","MORPHED_LUPINE",VAI, "The time as yet to come", 25,200, "...", "...", false, false),
		new NPCDef("LIONARD","Lionard","MORPHED_WOLF",VAI, "You are not ready yet.", 25,300, "...", "...", false, false),
		
		
		new NPCDef("GRAM","Gram","MAN",VAI, "So, you found me! but there is still not much to see here. Come back later!", 25,150, "Die fool!", "You are good!", false, false),
	};
	
	public static void init() {
		for (NPCDef n: NPC_DEFS) {
			definitions.put(n.getID(), n);
			if (n.isHostage) {
				hostages.add(n.getID());
			}
		}
	}
	
	
	public static NPC buildNPC(String id) {
		return new NPC((NPCDef)definitions.get(id));
	}
	
	public static Merchant buildMerchant(int merchandiseType) {
		return new Merchant(definitions.get("MERCHANT"), merchandiseType);
	}
	
	public static Hostage buildHostage() {
		Hostage ret = new Hostage((NPCDef)definitions.get( Util.pick(hostages) ));
		Player p = Main.ui.getPlayer();	//FIXME: *SURELY* the UI isn't where the player's stored though?
		if (p.playerClass != Player.CLASS_VAMPIREKILLER) {
			int artifactCategory = ((int) (p.getPlayerLevel() / 6.0));
			ret.itemReward = Main.itemData.createWeapon(Util.pick(hostageArtifacts[artifactCategory]),"");
		}
		return ret;
	}

	
	public static NPCDef getDefinition(String id) {
		return definitions.get(id);
	}


	private static String[][] hostageArtifacts = {
		{"HOLBEIN_DAGGER", "SHOTEL"},
		{"WEREBANE", "ALCARDE_SPEAR", "ETHANOS_BLADE"},
		{"FIREBRAND", "GURTHANG", "HADOR"},
		{"ICEBRAND", "MORMEGIL", "VORPAL_BLADE"},
		{"GRAM", "CRISSAEGRIM"},
		{"KAISER_KNUCKLE", "OSAFUNE", "MASAMUNE"}
	};
}