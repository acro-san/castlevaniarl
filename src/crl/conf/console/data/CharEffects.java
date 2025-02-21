package crl.conf.console.data;

import static crl.ui.Colors.*;
import crl.ui.consoleUI.effects.CharAnimatedMissileEffect;
import crl.ui.consoleUI.effects.CharBeamMissileEffect;
import crl.ui.consoleUI.effects.CharDirectionalMissileEffect;
import crl.ui.consoleUI.effects.CharEffect;
import crl.ui.consoleUI.effects.CharFlashEffect;
import crl.ui.consoleUI.effects.CharIconEffect;
import crl.ui.consoleUI.effects.CharIconMissileEffect;
import crl.ui.consoleUI.effects.CharMeleeEffect;
import crl.ui.consoleUI.effects.CharSequentialEffect;
import crl.ui.consoleUI.effects.CharSplashEffect;

import static crl.action.vkiller.Bible.BIBLE_STEPS;

public class CharEffects {
	
	public static CharEffect[] effects = {
		// Animated Missile Effects
		new CharAnimatedMissileEffect("SFX_CHARGE_BALL", "*~", RED, 50),
		new CharIconEffect("SFX_HOMING_BALL",'*', CYAN, 60),
		// FIXME METEOR_BALL is undefined in CharEffects!!
		new CharBeamMissileEffect("SFX_ITEMBREAKBIBLE", "OOOO", CYAN, 55),
		new CharAnimatedMissileEffect("SFX_LIT_SPELL", "*.", YELLOW, 55),
		new CharIconEffect("SFX_RED_HIT",'*', RED, 100),
		new CharIconEffect("SFX_WHITE_HIT",'*', WHITE, 100),
		// FIXME QUICK_WHITE_HIT is undefined in CharEffects!!
		new CharIconMissileEffect("SFX_RENEGADE_BOD",'O', RED, 20),
		new CharSequentialEffect("SFX_BIBLE", BIBLE_STEPS, "?�", CYAN, 10),
		new CharIconMissileEffect("SFX_CAT",'c', GRAY, 55),
		new CharIconMissileEffect("SFX_BIRD",'b', BROWN, 20),
		new CharIconMissileEffect("SFX_DRAGONFIRE",'o', GREEN, 150),
		new CharAnimatedMissileEffect("SFX_SUMMON_SPIRIT", "sS",GRAY, 45),
		new CharAnimatedMissileEffect("SFX_CROSS", "+x", CYAN, 70),
		new CharIconMissileEffect("SFX_HOLY",'p', CYAN, 20),
		new CharIconMissileEffect("SFX_EGG",'O', WHITE, 20),
		new CharAnimatedMissileEffect("SFX_FIREBALL", "*~", RED, 30),
		// SFX_FIREBALL was declared TWICE, the 2nd one with 30ms delay instead of 50. That would've taken precedence.
		new CharAnimatedMissileEffect("SFX_ICEBALL", "*x", CYAN, 30),
		new CharAnimatedMissileEffect("SFX_WHIP_FIREBALL", "*~", PURPLE, 30),
		new CharAnimatedMissileEffect("SFX_FLAMESSHOOT", "*~", RED, 50),
		new CharSplashEffect("SFX_EGG_BLAST","O.*",WHITE,20),
		new CharAnimatedMissileEffect("SFX_AXE", "/-\\|", WHITE, 50),
		new CharIconEffect("SFX_HOLY_RAINDROP",'/', CYAN, 30),
		new CharSplashEffect("SFX_HOLY_RAINSPLASH","*~,-",CYAN,50),
		// 25-33 all here ok:
		new CharDirectionalMissileEffect("SFX_SOUL_STEAL", "\\|/--/|\\", WHITE, 40),
		new CharDirectionalMissileEffect("SFX_SLEEP_SPELL", "\\|/--/|\\", PURPLE, 40),
		new CharDirectionalMissileEffect("SFX_TELEPORT", "\\|/--/|\\", WHITE, 40),
		new CharDirectionalMissileEffect("SFX_SHADETELEPORT", "\\|/--/|\\", GRAY, 40),
		new CharDirectionalMissileEffect("SFX_WHITE_DAGGER", "\\|/--/|\\", WHITE, 10),
		new CharDirectionalMissileEffect("SFX_THROWN_DAGGER", "\\|/--/|\\", WHITE, 10),
		new CharDirectionalMissileEffect("SFX_SILVER_DAGGER", "\\|/--/|\\", CYAN, 10),
		new CharDirectionalMissileEffect("SFX_GOLD_DAGGER", "\\|/--/|\\", YELLOW, 10),
		new CharDirectionalMissileEffect("SFX_SOULSSTRIKE", "\\|/--/|\\", WHITE, 40),
		//Beam fx  (not rly tho)
		new CharBeamMissileEffect("SFX_FLAME_SPELL", "Oo.o", RED, 46),
		new CharBeamMissileEffect("SFX_ICE_SPELL", "X*.*", CYAN, 46),
		new CharBeamMissileEffect("SFX_ENERGY_BEAM", "Oo.o", PURPLE, 30),
		// THIS DUPLICATE DECLARATION WAS AT THE BOTTOM OF THE LIST!:
		//new CharBeamMissileEffect("SFX_ENERGY_BEAM", "O", MAGENTA, 30),
		// Splash fx
		new CharSplashEffect("SFX_MATERIALIZE","Oo.",WHITE,60),
		new CharSplashEffect("SFX_VANISH",".oO",WHITE,50),
		new CharSplashEffect("SFX_SHADOW_APOCALYPSE","Oo*'.",CYAN,60),
		new CharSplashEffect("SFX_SHADOW_EXTINCTION","o*'.",RED,60),
		new CharSplashEffect("SFX_HOLY_FLAME","*~,",YELLOW,20),
		new CharSplashEffect("SFX_MANDRAGORA_SCREAM","............",YELLOW,20),
		new CharSplashEffect("SFX_DOVE_BLAST","Bbb.",WHITE,20),
		new CharSplashEffect("SFX_CRYSTAL_BLAST","Oo,.",CYAN,30),

		new CharSplashEffect("SFX_BOSS_DEATH","O....,,..,.,.,,......", RED,50),
		new CharSplashEffect("SFX_ROSARY_BLAST","****~~~~,,,,....", WHITE,50),
		new CharSplashEffect("SFX_KILL_CHRIS","Oo*'.",MAGENTA,180),
		new CharSplashEffect("SFX_MORPH_SOLEIYU","Oo*'.",GRAY,180),
		new CharSplashEffect("SFX_SOUL_FLAME","****~~~~,,,,....", RED,50),
		new CharSplashEffect("SFX_SOUL_BLAST","****~~~~,,,,....", GRAY,50),

		// 'tile effects' (directional missiles?)
		new CharAnimatedMissileEffect("SFX_SHADOW_FLARE","OX", RED, 20),
		new CharAnimatedMissileEffect("SFX_HELLFIRE","OX", RED, 20),
		new CharIconEffect("SFX_MONSTER_CRAWLING",'^', BROWN, 40),
		
		new CharIconMissileEffect("SFX_MISSILE_HOMING_BALL",'*', RED, 20),
		
		new CharFlashEffect("SFX_THUNDER_FLASH", WHITE),
		
		// En Target.java : 60 "SFX_"+weaponDef.getID()
		// En MonsterMissile.java : 48 "SFX_MONSTER_ID_"+aMonster.getID()
		// En MonsterMissile.java : 51 "SFX_MONSTER_ID_"+aMonster.getID()
		// En MonsterMissile.java : 42 "SFX_MONSTER_ID_"+aMonster.getID()
		// Melee Effects
		// En MonsterMissile.java  : 45 "SFX_MONSTER_ID_"+aMonster.getID()
		// En Attack.java : 116 "SFX_"+weaponDef.getID()
		// En Attack.java : 128 "SFX_"+weaponDef.getID()
		// Weapons
		new CharBeamMissileEffect("SFX_WP_AGUEN", "/\\", BLUE, 46),
		// 'wait'? was this intended as a delaying anim/antic? (SEARCH ID).
		new CharMeleeEffect("SFX_WP_BASELARD","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_STAFF","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_MACE","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_SHORT_SWORD","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_GAUNTLET","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_SHORT_SPEAR","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_FLAIL","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_RINGS","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_COMBAT_RINGS","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_WHIP","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_SCIMITAR","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_ROD","||--/\\\\/", BLUE),
		new CharMeleeEffect("SFX_WP_KNUCKLES","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_LONG_SPEAR","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_SABRE","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_GLADIUS","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_BATTLE_SPEAR","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_CUTLASS","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_ESTOC","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_COMBAT_GAUNTLET","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_MORNING_STAR","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_HALBERD","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_COMBAT_KNIFE","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_KATANA","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_BROADSWORD","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_SPIKY_KNUCKLES","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_SPIKED_RINGS","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_RAPIER","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_FALCHION","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_BASTARDSWORD","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_DUALBLADE_SPEAR","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_MARTIAL_ARMBAND","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_BLADE_RINGSET","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_CLAYMORE","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_LEATHER_WHIP","||--/\\\\/", BROWN),
		new CharMeleeEffect("SFX_WP_THORN_WHIP","||--/\\\\/", GREEN),
		new CharMeleeEffect("SFX_WP_CHAIN_WHIP","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_VKILLERW","||--/\\\\/", BROWN),
		new CharMeleeEffect("SFX_WP_LIT_WHIP","||--/\\\\/", YELLOW),
		new CharMeleeEffect("SFX_WP_FLAME_WHIP","||--/\\\\/", RED),
		new CharMeleeEffect("SFX_WP_HOLBEIN_DAGGER","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_SHOTEL","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_FLAMBERGE","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_WEREBANE","||--/\\\\/", PURPLE),
		new CharMeleeEffect("SFX_WP_ETHANOS_BLADE","||--/\\\\/", RED),
		new CharMeleeEffect("SFX_WP_MABLUNG","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_HADOR","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_HARPER","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_TULKAS_FIST","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_ALCARDE_SPEAR","||--/\\\\/", YELLOW),
		new CharMeleeEffect("SFX_WP_FIREBRAND","||--/\\\\/", RED),
		new CharMeleeEffect("SFX_WP_ICEBRAND","||--/\\\\/", CYAN),
		new CharMeleeEffect("SFX_WP_VORPAL_BLADE","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_GURTHANG","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_OSAFUNE","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_MORMEGIL","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_GRAM","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_KAISER_KNUCKLE","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_TERMINUS","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_MOURNEBLADE","||--/\\\\/", GRAY),
		new CharMeleeEffect("SFX_WP_CRISSAEGRIM","||--/\\\\/", WHITE),
		new CharMeleeEffect("SFX_WP_MASAMUNE","||--/\\\\/", WHITE),
		new CharMeleeEffect("SFX_WP_HAMMER_JUSTICE","||--/\\\\/", WHITE),
		
		new CharIconMissileEffect("SFX_WP_HANDGUN", '\'', GRAY, 20),
		new CharIconMissileEffect("SFX_WP_SILVER_HANDGUN", '\'', GRAY, 20),
		new CharIconMissileEffect("SFX_WP_REVOLVER", '\'', GRAY, 20),
		
		new CharDirectionalMissileEffect("SFX_WP_BOW", "\\|/--/|\\", WHITE, 40),
		new CharDirectionalMissileEffect("SFX_WP_CROSSBOW", "\\|/--/|\\", WHITE, 20),
		new CharAnimatedMissileEffect("SFX_WP_SHURIKEN", "x+", GRAY, 10),
		new CharAnimatedMissileEffect("SFX_WP_CHAKRAM", "o-", GRAY, 10),
		new CharAnimatedMissileEffect("SFX_WP_BUFFALO_STAR", "X+", GRAY, 10),
		new CharAnimatedMissileEffect("SFX_WP_BWAKA_KNIFE", "/-\\|", GRAY, 10),
		new CharAnimatedMissileEffect("SFX_WP_THROWING_KNIFE", "/-\\|", GRAY, 10),

		// Monsters
		new CharAnimatedMissileEffect("SFX_ROTATING_BONE", "/-\\|", WHITE, 20),
		new CharAnimatedMissileEffect("SFX_ROTATING_AXE", "/-\\|", BLUE, 40),
		new CharMeleeEffect("SFX_AXE_MELEE","||--/\\\\/", BLUE),
		new CharMeleeEffect("SFX_SPEAR_MELEE","||--/\\\\/", BLUE),
		new CharMeleeEffect("SFX_GIANT_BONE_MELEE","||--/\\\\/", BLUE),
		new CharMeleeEffect("SFX_BLADE_SOLDIER_MELEE","||--/\\\\/", BLUE),
		new CharMeleeEffect("SFX_BONE_HALBERD_MELEE","||--/\\\\/", BLUE),
		new CharMeleeEffect("SFX_LIZARD_SWORDSMAN_MELEE","||--/\\\\/", BLUE),
		
		new CharAnimatedMissileEffect("SFX_SUMMON_CAT_WITCH", "c", GRAY, 20),
		new CharBeamMissileEffect("SFX_FIRE_BEAM", "oOo", YELLOW, 30),
		new CharBeamMissileEffect("SFX_NOVA_BEAM", "*oOo", CYAN, 30),
		new CharBeamMissileEffect("SFX_ARK_BEAM", "O", CYAN, 30),
		new CharBeamMissileEffect("SFX_POISON_CLOUD", "X*..", PURPLE, 40),
		new CharMeleeEffect("SFX_FLAME_SWORD_MELEE","||--/\\\\/", RED),
		new CharBeamMissileEffect("SFX_FLAME_WAVE", "~_", RED, 40),
		new CharBeamMissileEffect("SFX_TOXIC_POWDER", "X*..", PURPLE, 40),
		new CharBeamMissileEffect("SFX_STONE_BEAM", "X*..", GRAY, 46),
		new CharBeamMissileEffect("SFX_STUN_BEAM", "X*..", YELLOW, 46),
		new CharBeamMissileEffect("SFX_ANCIENT_BEAM", "O", CYAN, 30),
		new CharBeamMissileEffect("SFX_PURPLE_FLAME_BEAM","~_", PURPLE, 40),

		new CharAnimatedMissileEffect("SFX_MAGIC_MISSILE", "*-", BLUE, 20),
		new CharAnimatedMissileEffect("SFX_BIG_FIREBALL", "*~", RED, 30),
		new CharAnimatedMissileEffect("SFX_FLAMING_BARREL", "0o", BROWN, 40),
		new CharAnimatedMissileEffect("SFX_BULLET", ".", GRAY, 10),
		new CharAnimatedMissileEffect("SFX_SEED", ".", GREEN, 30),
		new CharAnimatedMissileEffect("SFX_WINDING_SHARD", "~-", BLUE, 30),
		// FIXME MONSTER_ID_ZELDO undefined !?
		new CharAnimatedMissileEffect("SFX_WHIRLING_SICKLE", "+x", GRAY, 30),
		new CharAnimatedMissileEffect("SFX_SPINNING_DISK", "OX", GRAY, 50),
		new CharAnimatedMissileEffect("SFX_SPINNING_SWORD", "/-\\|", GREEN, 40),
		new CharDirectionalMissileEffect("SFX_ARROW", "\\|/--/|\\", WHITE, 10),
		new CharDirectionalMissileEffect("SFX_THROWN_SPEAR", "\\|/--/|\\", WHITE, 10),
		new CharDirectionalMissileEffect("SFX_THROWN_SWORD", "\\|/--/|\\", WHITE, 10),
		
	};

}
