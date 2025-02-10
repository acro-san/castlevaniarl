
package crl.game;

import java.util.HashMap;
import java.util.Hashtable;

import crl.Main;
import crl.data.Text;
import sz.util.TxtTpl;
import sz.util.Util;
import crl.item.ItemDefinition;
import crl.item.ItemDataTable;
import crl.player.Consts;
import crl.player.Player;
import crl.ui.Appearance;

public abstract class PlayerGenerator {
	
	public static PlayerGenerator thus;
	
	public abstract Player generatePlayer();
	
	private Hashtable<String, Player> SPECIAL_PLAYERS = new Hashtable<>();	// FIXME actually just an array?
	
	//static?
	private void initSpecialPlayers() {
		SPECIAL_PLAYERS.clear();
		ItemDataTable it = Main.itemData;
		HashMap<String, Appearance> aps = Main.appearances;
		
		Player chris = new Player();
		chris.sex = Player.MALE;
		chris.setDoNotRecordScore(true);
		chris.setName("Christopher");
		chris.setPlayerClass(Player.CLASS_VAMPIREKILLER);
		chris.setPlayerLevel(38);
		chris.setBaseSightRange(6);
		chris.setGold(Util.rand(30,70) * 100);
		chris.setAttack(20);
		chris.setWalkCost(15);
		chris.setAttackCost(20);
		chris.setBaseEvadeChance(25);
		chris.setCastCost(15);
		chris.setCarryMax(35);
		chris.setSoulPower(15);
		// *WTF*!?? :-
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_WHIPS);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_WHIPS);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_WHIPS);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_UNARMED);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_UNARMED);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_WHIPS);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_WHIPS);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_WHIPS);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_UNARMED);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_UNARMED);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_WHIPS);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_WHIPS);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_WHIPS);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_UNARMED);
		chris.increaseWeaponSkillLevel(ItemDefinition.CAT_UNARMED);
		chris.setAppearance(aps.get("CHRISTOPHER_B"));
		chris.setPlot("After defeating Dracula two times, he returns to the castle to fulfill his fate for a last time", "");
		chris.setDescription("The famous Vampire Killer, heir of the Belmont fate");
		chris.setWeapon(it.createWeapon("LEATHER_WHIP",""));
		chris.setArmor(it.createArmor("VARMOR",""));
		chris.setAdvancementLevels(ADVANCEMENT_LEVELS_HARDER);
		SPECIAL_PLAYERS.put("CHRIS", chris);
		
		Player solei = new Player();
		solei.sex = Player.MALE;
		solei.setName("Soleiyu");
		solei.setDoNotRecordScore(true);
		solei.setAttack(15);
		solei.setPlayerLevel(35);
		solei.setPlayerClass(Player.CLASS_VANQUISHER);
		solei.setBaseSightRange(6);
		solei.setGold(Util.rand(30,70) * 100);
		solei.setWalkCost(25);
		solei.setAttackCost(25);
		solei.setBaseEvadeChance(20);
		solei.setCastCost(10);
		solei.setCarryMax(25);
		solei.setSoulPower(20);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_SWORDS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_STAVES);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_SWORDS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_RINGS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_RINGS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_SWORDS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_STAVES);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_SWORDS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_RINGS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_RINGS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_RINGS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_SWORDS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_STAVES);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_SWORDS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_RINGS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_RINGS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_SWORDS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_STAVES);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_SWORDS);
		solei.increaseWeaponSkillLevel(ItemDefinition.CAT_RINGS);
		solei.setAppearance(aps.get("SOLEIYU_B"));
		solei.setPlot("Finally deciding to face his own fate, he travels to the cursed Castlevania", "");
		solei.setDescription("The son of Christopher Belmont");
		solei.setWeapon(it.createWeapon("HARPER",""));
		solei.setArmor(it.createArmor("PLATE", "STEEL"));
		solei.setAdvancementLevels(ADVANCEMENT_LEVELS_HARDER);
		SPECIAL_PLAYERS.put("SOLEIYU", solei);
		
		Player kidso = new Player();
		kidso.sex = Player.MALE;
		kidso.setName("Child Soleiyu");
		kidso.setDoNotRecordScore(true);
		kidso.setAttack(1);
		kidso.setPlayerLevel(1);
		kidso.setPlayerClass(Player.CLASS_VAMPIREKILLER);
		kidso.setBaseSightRange(5);
		kidso.setGold(200);
		kidso.setWalkCost(60);
		kidso.setHearts(15);
		kidso.setAttackCost(80);
		kidso.setBaseEvadeChance(5);
		kidso.setCastCost(40);
		kidso.setCarryMax(5);
		kidso.setSoulPower(0);
		kidso.setAppearance(aps.get("SOLEIYU_B_KID"));
		kidso.setPlot("Trains to become the next vampire killer", "");
		kidso.setDescription("The son of Christopher Belmont");
		kidso.setAdvancementLevels(ADVANCEMENT_LEVELS_HARDER);
		SPECIAL_PLAYERS.put("SOLEIYU_KID", kidso);
		
		Player sonia = new Player();
		sonia.sex = Player.FEMALE;
		sonia.setDoNotRecordScore(false);
		sonia.setName("Sonia");
		sonia.setPlayerClass(Player.CLASS_VAMPIREKILLER);
		sonia.setPlayerLevel(1);
		sonia.setBaseSightRange(7);
		sonia.setGold(Util.rand(30,70) * 100);
		sonia.setAttack(7);
		sonia.setWalkCost(40);
		sonia.setAttackCost(50);
		sonia.setBaseEvadeChance(5);
		sonia.setCastCost(30);
		sonia.setCarryMax(10);
		sonia.setSoulPower(4);
		sonia.setSoulPower(1);
		sonia.setFlag("ARENA_FIGHTER", true);
		sonia.increaseWeaponSkillLevel(ItemDefinition.CAT_WHIPS);
		sonia.increaseWeaponSkillLevel(ItemDefinition.CAT_PROJECTILES);
		int items = Util.rand(10, 15);
		for (int i = 0; i < items; i++) {
			sonia.addItem(it.createItem(Util.pick(SONIA_ITEMS)));
		}
		sonia.setAppearance(aps.get("SONIA_B"));
		sonia.setPlot("Came to the castle as the first Belmont ever", "");
		sonia.setDescription("Sonia Belmont, a mysterious girl");
		sonia.setWeapon(it.createWeapon("LEATHER_WHIP",""));
		sonia.setArmor(it.createArmor("VARMOR",""));
		sonia.setAdvancementLevels(ADVANCEMENT_LEVELS_NORMAL);
		SPECIAL_PLAYERS.put("SONIA", sonia);
	}
	
	public Player createSpecialPlayer(String playerID) {
		initSpecialPlayers();
		return SPECIAL_PLAYERS.get(playerID);
	}
	
	
	public Player getPlayer(String name, byte sex, byte classChoice) {
		Player player = new Player();
		player.sex = sex;
		if (name.trim().equals("")) {
			if (sex == Player.MALE) {
				player.setName(Util.pick(Text.RANDOM_PLAYER_MALE_NAMES));
			} else {
				player.setName(Util.pick(Text.RANDOM_PLAYER_FEMALE_NAMES));
			}
		} else {
			player.setName(name);
		}
		
		assert(	classChoice >= Player.CLASS_VAMPIREKILLER &&
				classChoice <= Player.CLASS_KNIGHT);
		player.setPlayerClass(classChoice);
		
		String[] plots = null;
		String[] descriptions = null;
		String[] initWeapons = null;
		String[] initArmors = null;
		String[] initItems = null;
		player.setBaseSightRange(4);
		// "setClass()" - from CharClassDefs? or 'initAsClass(..)' since it randomizes gold amount.
		switch (player.playerClass) {
		case Player.CLASS_VAMPIREKILLER:
			player.setGold(Util.rand(3,7) * 100);
			plots = Text.VKILLER_PLOTS;
			descriptions = Text.VKILLER_DESCRIPTIONS;
			initWeapons = VKILLER_WEAPONS;
			initArmors = VKILLER_ARMORS;
			initItems = VKILLER_ITEMS;
			player.setWalkCost(40);
			player.setAttackCost(50);
			player.setAttack(5);
			player.setBaseEvadeChance(5);
			player.setCastCost(30);
			player.setSoulPower(1);
			player.setCarryMax(15);
			player.setBreathing(35);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_WHIPS);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_PROJECTILES);
			player.setBannedArmors(VKILLER_BANNED_ARMOR);
			player.setAdvancementLevels(ADVANCEMENT_LEVELS_NORMAL);
			player.setFlag("ONLY_VK", true);
			player.putCustomMessage("INTRO_1", "This forest suffers as darkness corrupts its roots, it is my fate to get to Castlevania and fight my way through.");
			player.putCustomMessage("CLARA1", "I came here to fulfill my fate as a Vampire Killer, and destroy the dark count Dracula");
			break;
			
		// RENEGADE HERE. consistency please.
		case Player.CLASS_RENEGADE:
			player.setGold(Util.rand(2,10) * 100);
			plots = Text.RENEGADE_PLOTS;
			descriptions = Text.RENEGADE_DESCRIPTIONS;
			initWeapons = RENEGADE_WEAPONS;
			initArmors = RENEGADE_ARMORS;
			initItems = RENEGADE_ITEMS;
			player.setBaseSightRange(5);
			player.setBaseEvadeChance(5);
			player.setWalkCost(40);
			player.setAttackCost(40);
			player.setAttack(5);
			player.setCastCost(40);
			player.setCarryMax(15);
			player.setSoulPower(2);
			player.setBreathing(35);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_DAGGERS);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_SWORDS);
			player.setBannedArmors(RENEGADE_BANNED_ARMOR);
			player.setAdvancementLevels(ADVANCEMENT_LEVELS_NORMAL);

			player.setShield(Main.itemData.createShield(Util.pick(RENEGADE_SHIELDS), Util.pick(ARMOR_MATERIALS)));
			player.setSecondaryWeapon(Main.itemData.createWeapon("BASELARD", Util.pick(WEAPON_MATERIALS)));

			player.putCustomMessage("INTRO_1", "I can almost breathe the chaos that invades every tree and animal in this forest. It is time to return to the castle.");
			player.putCustomMessage("CLARA1", "Commonner? . . . Ignorant human... get out of my way.");
			break;
			
		case Player.CLASS_VANQUISHER:

			player.setGold(Util.rand(5,15) * 100);
			player.setHitsMax(17);
			player.heal();
			plots = Text.VANQUISHER_PLOTS;
			descriptions = Text.VANQUISHER_DESCRIPTIONS;
			initWeapons = VANQUISHER_WEAPONS;
			initArmors = VANQUISHER_ARMORS;
			initItems = VANQUISHER_ITEMS;
			player.setWalkCost(60);
			player.setAttackCost(60);
			player.setAttack(3);
			player.setSoulPower(3);
			player.setCastCost(30);
			player.setCarryMax(15);
			player.setHeartMax(30);
			player.setBreathing(25);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_STAVES);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_RINGS);
			player.setBannedArmors(VANQUISHER_BANNED_ARMOR);
			player.setAdvancementLevels(ADVANCEMENT_LEVELS_HARD);
			player.putCustomMessage("INTRO_1", "The time is almost over, the trip over here will be wasted if I dont reach the castle soon.");
			player.putCustomMessage("CLARA1", "Get out of my way, I have issues to attend inside the castle");
			break;

		case Player.CLASS_INVOKER:
			player.setGold(Util.rand(2,10) * 100);
			player.setHitsMax(17);
			player.heal();
			plots = Text.INVOKER_PLOTS;
			descriptions = Text.INVOKER_DESCRIPTIONS;
			initWeapons = INVOKER_WEAPONS;
			initArmors = INVOKER_ARMORS;
			initItems = INVOKER_ITEMS;
			player.setWalkCost(60);
			player.setSoulPower(3);
			player.setAttackCost(50);
			player.setAttack(3);
			player.setHeartMax(30);
			player.setCastCost(40);
			player.setCarryMax(15);
			player.setBreathing(25);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_STAVES);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_RINGS);
			player.setBannedArmors(INVOKER_BANNED_ARMOR);
			player.setAdvancementLevels(ADVANCEMENT_LEVELS_NORMAL);
			player.putCustomMessage("INTRO_1", "I can sense all kind of dreaded souls lurking in here, I am afraid my master was right, I really need to reach the castle.");
			player.putCustomMessage("CLARA1", "Something bigger than myself has got me here, you would never understand it... the castle calls me.");
			break;
		case Player.CLASS_MANBEAST:
			//player.setRegenRate(40);
			player.setGold(Util.rand(1,5) * 100);
			player.setHitsMax(26);
			player.setBaseSightRange(6);
			player.heal();
			plots = Text.MANBEAST_PLOTS;
			descriptions = Text.MANBEAST_DESCRIPTIONS;
			initWeapons = null;
			initArmors = MANBEAST_ARMORS;
			initItems = MANBEAST_ITEMS;
			player.setWalkCost(35);
			player.setAttackCost(40);
			player.setCastCost(60);
			player.setSoulPower(1);
			player.setHeartMax(15);
			player.setAttack(3);
			player.setCarryMax(20);
			player.setBreathing(45);
			player.setBaseEvadeChance(20);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_UNARMED);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_PROJECTILES);
			player.setBannedArmors(MANBEAST_BANNED_ARMOR);
			player.setAdvancementLevels(ADVANCEMENT_LEVELS_HARDER);
			player.addItem(Main.itemData.createItem("THROWING_KNIFE"));
			player.putCustomMessage("INTRO_1", "I can hear, I can smell, I can sense the damned creatures that almost anihilated my brothers... tell me, where is the castle?");
			player.putCustomMessage("CLARA1", "Don't let appearances fool you... the blood of my brothers and sisters fills my veins, anger will get me through this damned place and to the heart of the count");
			break;
		case Player.CLASS_KNIGHT:
			player.setGold(Util.rand(5,12) * 100);
			player.setHitsMax(23);
			player.setHeartMax(10);
			player.setBreathing(15);
			player.heal();
			int skilleds = Util.rand(4,5);
			String[] randWpnSkillUp = {
				ItemDefinition.CAT_UNARMED,
				ItemDefinition.CAT_DAGGERS,
				ItemDefinition.CAT_SWORDS,
				ItemDefinition.CAT_SPEARS,
				ItemDefinition.CAT_WHIPS,
				ItemDefinition.CAT_MACES,
				ItemDefinition.CAT_STAVES,
				ItemDefinition.CAT_RINGS,
				ItemDefinition.CAT_PROJECTILES,
				ItemDefinition.CAT_BOWS,
				ItemDefinition.CAT_PISTOLS,
				ItemDefinition.CAT_SHIELD
			};
			for (int i = 0; i < skilleds; i++) {
				player.increaseWeaponSkillLevel(Util.pick(randWpnSkillUp));
			}
			plots = Text.KNIGHT_PLOTS;
			descriptions = Text.KNIGHT_DESCRIPTIONS;
			initWeapons = KNIGHT_WEAPONS;
			initArmors = KNIGHT_ARMORS;
			initItems = KNIGHT_ITEMS;
			player.setWalkCost(50);
			player.setAttackCost(40);
			player.setCastCost(60);
			player.setAttack(7);
			player.setCarryMax(20);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_SWORDS);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_SPEARS);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_PISTOLS);
			player.increaseWeaponSkillLevel(ItemDefinition.CAT_SHIELD);
			player.setBannedArmors(KNIGHT_BANNED_ARMOR);
			player.setAdvancementLevels(ADVANCEMENT_LEVELS_NORMAL);
			player.setShield(Main.itemData.createShield(Util.pick(KNIGHT_SHIELDS), Util.pick(ARMOR_MATERIALS)));
			player.putCustomMessage("INTRO_1", "Indeed, those seem to be Wargs: demonic wolf-like creatures summoned by the Count of Darkness to protect his castle. We better get to the castle quickly.");
			player.putCustomMessage("CLARA1", "Can you not see the mark of heavens? I am a Knight of the Sacred Order, and I came to cleanse this place from darkness.");
			break;
		}
		
		String classAppearanceID = CLASS_ID_STRINGS[player.playerClass];
		HashMap<String, Appearance> aps = Main.appearances;
		if (player.sex == Player.MALE)
			player.setAppearance(aps.get(classAppearanceID));
		else
			player.setAppearance(aps.get(classAppearanceID+"_W"));
		
		player.setPlot(
			TxtTpl.t(player, Util.pick(plots)),
			TxtTpl.t(player, getClassStartEquipmentDescription(player.playerClass)));
		player.setDescription( TxtTpl.t(player, Util.pick(descriptions)) );
		
		ItemDataTable itf = Main.itemData;
		if (initWeapons != null) {
			player.setWeapon(itf.createWeapon(Util.pick(initWeapons), Util.pick(WEAPON_MATERIALS)));
		}
		player.setArmor(itf.createArmor(Util.pick(initArmors), Util.pick(ARMOR_MATERIALS)));
		int items = Util.rand(5,7);
		for (int i = 0; i < items; i++){
			player.addItem(itf.createItem(Util.pick(initItems)));
		}
		player.coolness += 10;
		return player;
	}


	public static final String[]
		CLASS_EQUIP_DESCRIPTIONS = {
			"the vampire killer whip, %%SEX sacred charm",	// 0: vampirekiller
			"%%SEX dark powers",	// 1: renegade
			"%%SEX abilities to conjure the forces of light",	// 2: vanquisher
			"%%SEX power to invoke souls unto aid",	// 3: invoker
			"%%SEX martial art and shapeshifting skills",	// 4: manbeast
			"the weapons of the sacred church"	// 5: knight
		},
		CLASS_ID_STRINGS = {
			"VKILLER",	// 0: vampirekiller
			"RENEGADE",	// 1: renegade
			"VANQUISHER",	// 2: vanquisher
			"INVOKER",	// 3: invoker
			"MANBEAST",	// 4: manbeast
			"KNIGHT"	// 5: knight
		};
	
	// TODO Move to Player. then deprecate ... use ints.
	protected String[] CLASS_APPEARANCES = {
		"VKILLER", "RENEGADE", "VANQUISHER", "INVOKER", "MANBEAST", "KNIGHT"
	};
	// TODO Move to Text.
	protected String[] CLASS_NAMES = {	// ManBeast aka 'Werebeast'? (Werewolf=='Man'wolf),Wolfwere also valid, but sounds odd.
		"Vampire Killer", "Darkness Renegade","Vanquisher Wizard","Souls' Master","Beastman","Knight of the Order"
	};
	
	
	private String getClassStartEquipmentDescription(int classID) {
		if (classID < 0 || classID > Player.CLASS_KNIGHT) {
			// But is there even reason to be checking for this to occur!?
			System.err.println("Invalid character class!: "+classID);
			return null;
		}
		return CLASS_EQUIP_DESCRIPTIONS[classID];
	}
	
	public static String getClassID(int classID) {
		if (classID < Player.CLASS_VAMPIREKILLER || classID > Player.CLASS_KNIGHT) {
			// But is there even reason to be checking for this to occur!?
			System.err.println("Invalid character class!: "+classID);
			return "";
		}
		return CLASS_ID_STRINGS[classID];
	}
	
	// static??!! final? was just private String[] before.
	private String[]
		VKILLER_BANNED_ARMOR = {"DIAMOND_PLATE", "LEATHER_ARMOR", "CLOTH_TUNIC", "VEST", "CUIRASS", "SUIT", "PLATE"},
		VKILLER_WEAPONS = {"LEATHER_WHIP"},
		VKILLER_ARMORS = {"VARMOR"},
		VKILLER_ITEMS = {"HEAL_HERB", "GARLIC"},
		
		RENEGADE_BANNED_ARMOR = {"DIAMOND_PLATE", "SUIT", "PLATE"},
		RENEGADE_WEAPONS = {"SHORT_SWORD", "BOW"},
		RENEGADE_ARMORS = {"FINE_GARMENT"},
		RENEGADE_ITEMS = {"HEAL_HERB", "BIBUTI"},
		RENEGADE_SHIELDS = {"BUCKLER"},
	
		VANQUISHER_BANNED_ARMOR = {"DIAMOND_PLATE", "VEST", "CUIRASS", "SUIT", "PLATE"},
		VANQUISHER_WEAPONS = {"RINGS", "STAFF"},
		VANQUISHER_ARMORS = {"CLOTH_TUNIC"},
		VANQUISHER_ITEMS = {"HEAL_POTION", "BIBUTI"},
		
		INVOKER_BANNED_ARMOR = {"DIAMOND_PLATE", "VEST", "CUIRASS", "SUIT", "PLATE"},
		INVOKER_WEAPONS = {"BASELARD", "STAFF"},
		INVOKER_ARMORS = {"CLOTH_TUNIC"},
		INVOKER_ITEMS = {"HEAL_POTION", "GARLIC"},
		
		MANBEAST_BANNED_ARMOR = {"DIAMOND_PLATE", "VEST", "CUIRASS", "SUIT", "PLATE"},
		MANBEAST_ARMORS = {"CLOTH_TUNIC"},
		MANBEAST_ITEMS = {"HEAL_HERB"},
		
		KNIGHT_BANNED_ARMOR = {},
		KNIGHT_WEAPONS = {"SHORT_SPEAR", "FLAIL", "SABRE"},
		KNIGHT_ARMORS = {"CUIRASS"},
		KNIGHT_ITEMS = {"HEAL_POTION", "BIBUTI", "GARLIC"},
		KNIGHT_SHIELDS = {"BUCKLER", "WOODEN_SHIELD"};
	
	private String[]
		WEAPON_MATERIALS = {"STEEL"},
		ARMOR_MATERIALS  = {"BRONZE"};
	
	
	protected String[][] CLASS_STATS = {
		{"5","1","Normal","5","Quick","Normal","Deadly","Normal","Normal","Poor"},
		{"5","2","Normal","5","Quick","Deadly","Normal","Normal","Long","Normal"},
		{"3","3","Weak","0","Slow","Poor","Deadly","Weak","Normal","Wealthy"},
		{"3","3","Weak","0","Slow","Normal","Normal","Weak","Normal","Normal"},
		{"7","1","Very Hardy","20","Quick","Deadly","Poor","Strong","Very Long","Very poor"},
		{"7","0","Hardy","0","Normal","Deadly","Poor","Strong","Normal","Wealthy"}
	};
	
	
	private static final int[]
		ADVANCEMENT_LEVELS_NORMAL = {
			1,  4,  7, 10, 14, 18, 22, 27, 32, 37, 43, 49, 55, 62, 69, 76
		},
		ADVANCEMENT_LEVELS_HARD = {
			1,  5,  9, 13, 18, 23, 28, 34, 40, 46, 53, 60, 67, 75, 83, 91
		},
		ADVANCEMENT_LEVELS_HARDER = {
			1,  6, 11, 16, 22, 28, 34, 41, 48, 55, 63, 71, 79, 88, 97, 106
		};

	private String[] SONIA_ITEMS = {"HEAL_HERB", "GARLIC", "BIBUTI"};

}
