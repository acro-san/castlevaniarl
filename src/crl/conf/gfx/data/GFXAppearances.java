package crl.conf.gfx.data;
import java.awt.image.BufferedImage;

import sz.util.ImageUtils;

import crl.game.Game;
import crl.ui.*;
import crl.ui.graphicsUI.*;

public class GFXAppearances {
	
	private final static int
		ICON_SIZE = 16;	// hardcoded...
	
	protected GFXConfiguration conf;
	
	private Appearance[] defs;
	
	public GFXAppearances(GFXConfiguration configuration) {
		this.conf = configuration;
		
		setAppearances();
	}
	
	
	private void setAppearances() {
		final int
			tw = conf.tileWidth,
			halfw = conf.tileWidth / 2;
		
	///	Textures imgConfig = configuration.textures;
		defs = new Appearance[] {
		createTAppearance("VOID", 4,5),
		new GFXAppearance("NOTHING", null, null,null,null,0,0),

		createAppearance("BLOOD1", Textures.EffectsImage, 5, 4),
		createAppearance("BLOOD2", Textures.EffectsImage, 6, 4),
		createAppearance("BLOOD3", Textures.EffectsImage, 7, 4),
		
		/*NEEDED*/
		//createAppearance("COFFIN", Textures.getItemsImage(), 1,1),
		createTAppearance("COFFIN", 14, 2),
		
		createTAppearance("DOOR", 11,2),
		
		createAppearance("SHADOW", Textures.ShadowImage, 1,1),
		
		createAppearance("CHRISTOPHER_B", Textures.CharactersImage, 1,5),
		
		createAppearance("SOLEIYU_B", Textures.CharactersImage, 2,5),
		createAppearance("BADBELMONT", Textures.CharactersImage, 2,6),
		createBAppearance("PRELUDE_DRACULA", Textures.BigMonstersImage, 4, 5),
		
		/*Town*/
		createTAppearance("TOWN_GRASS", 4, 1),
		createTAppearance("TOWN_WALL", 1, 1),
		createTAppearance("HOUSE_FLOOR",9, 1),
		createTAppearance("TOWN_DOOR", 1, 11),
		createTAppearance("MIDWALKWAY", 2, 1),
		createTAppearance("TOWN_CHURCH_FLOOR", 10, 1),
		createTAppearance("TOWN_WATERWAY", 7, 1),
		createTAppearance("BRICKWALKWAY", 6, 1),
		createTAppearance("BRICKWALKWAY2", 10, 1),
		createTAppearance("TOWN_ROOF", 14, 1),
		createTAppearance("TOWN_STAIRSDOWN", 15, 1),
		createTAppearance("TOWN_STAIRSUP", 16, 1),
		createTAppearance("TOWN_TREE", 12, 1),
		createTAppearance("TOWN_STAIRS", 3, 1),
		
		/*Dark Forest*/
		createTAppearance("FOREST_TREE", 8, 1),
		createTAppearance("FOREST_TREE_1", 12, 1),
		createTAppearance("FOREST_TREE_2", 13, 1),
		createTAppearance("FOREST_GRASS", 17, 1),
		createTAppearance("FOREST_DIRT", 19, 1),
		createTAppearance("WRECKED_CHARRIOT", 15, 2),
		createTAppearance("WRECKED_WHEEL", 14, 2),
		createTAppearance("SIGN_POST", 16,2),
		
		/*Castle Bridge*/
		createTAppearance("WOODEN_BRIDGE", 17, 3),
		createTAppearance("DARK_LAKE", 13, 3),
		createTAppearance("BRIDGE_WALKWAY", 16, 3),
		createTAppearance("BRIDGE_COLUMN", 14, 3),
		createTAppearance("STONE_BLOCK", 1, 1),
		
		/*Castle Garden*/
		createTAppearance("GARDEN_GRASS", 17, 1),
		createTAppearance("GARDEN_WALKWAY", 19, 1),
		createTAppearance("DEAD_STUMP", 4, 2),
		createTAppearance("GARDEN_TREE", 5, 2),
		createTAppearance("GARDEN_TORCH", 3, 2),
		createTAppearance("GARDEN_FENCE", 6, 2),
		createTAppearance("CASTLE_DOOR", 11, 2),
		createTAppearance("GARDEN_FOUNTAIN_CENTER", 9, 2),
		createTAppearance("GARDEN_FOUNTAIN_AROUND", 7, 2),
		createTAppearance("GARDEN_FOUNTAIN_POOL", 8, 2),
		createTAppearance("GARGOYLESTATUE", 2, 2),
		createTAppearance("HUMANSTATUE", 1, 2),
		createTAppearance("GARDEN_DOOR", 11, 1),
		createTAppearance("GARDEN_WALL", 10, 2),
		createTAppearance("CASTLE_WALL", 10, 2),

		/*Marble Hall*/
		createTAppearance("GRAY_COLUMN", 15, 3),
		createTAppearance("MARBLE_COLUMN", 5, 3),
		createTAppearance("MARBLE_FLOOR", 1, 3),
		createTAppearance("MARBLE_STAIRS", 2, 3),
		createTAppearance("MARBLE_MIDFLOOR", 3, 3),
		createTAppearance("BIG_WINDOW", 8, 3),
		createTAppearance("RED_CURTAIN", 6, 3),
		createTAppearance("GODNESS_STATUE", 7, 3),
		createTAppearance("RED_CARPET", 4, 3),
		
		/*Moat*/
		createTAppearance("MOSS_COLUMN", 7, 4),
		createTAppearance("MOSS_FLOOR", 2, 4),
		createTAppearance("RUSTY_PLATFORM", 3, 10),
		createTAppearance("MOSS_WATERWAY_ETH", 8, 4, 0,-10),
		createTAppearance("MOSS_WATERWAY", 8, 4, 0,-10),
		/*createAppearance("MOAT_DOWN", 
				Textures.getTerrainImage(), 
				128, 164,WIDTH_NORMAL,WIDTH_NORMAL,
				0,0),
		createAppearance("MOAT_UP", 
				Textures.getTerrainImage(), 
				160, 154,WIDTH_NORMAL,42,
				0,10),*/
		createTAppearance("MOAT_DOWN", 5, 4, 0, 8),
		createTAppearance("MOAT_UP", 6, 4, 0, 8),
		createTAppearance("MOSS_MIDFLOOR", 4, 4),
		createTAppearance("MOSS_STAIRS", 3, 4),
		//createTAppearance("MOAT_UP", 6, 4),
		
		/*Sewers*/
		createTAppearance("SEWERS_FLOOR", 8, 4),
		createTAppearance("SEWERS_WALL", 1, 4),
		createTAppearance("SEWERS_DOWN", 9, 10),
		createTAppearance("SEWERS_UP", 10, 10),
		createTAppearance("WEIRD_MACHINE",8,10),
		
		/*Alchemy Lab*/
		createTAppearance("RED_FLOOR", 3, 5),
		createTAppearance("RED_WALL", 1, 5),
		createTAppearance("SMALL_WINDOW", 2, 5),
		
		/*Chapel*/
		createTAppearance("CHURCH_WALL", 11, 4),
		createTAppearance("CHURCH_FLOOR", 13, 4),
		createTAppearance("CHURCH_WOODEN_BARRIER_H", 19, 4),
		createTAppearance("CHURCH_WOODEN_BARRIER_V", 20, 4),
		createTAppearance("CHURCH_CHAIR", 18, 4),
		createTAppearance("CHURCH_CONFESSIONARY", 11, 5),
		createTAppearance("CHURCH_CARPET", 16, 4),
		createTAppearance("ATRIUM", 14, 5),
		createTAppearance("CHURCH_STAINED_WINDOW", 12, 4),
		createTAppearance("CHURCH_FLOOR_H", 13, 4),
		
		createTAppearance("QUARTERS_WALL", 11, 4),/*Pending*/
		createTAppearance("QUARTERS_FLOOR", 13, 4), /*Pending*/
		
		/*left some unused*/
		
		/*Ruins*/
		/**/createTAppearance("STONE_WALL", 4, 1),
		/**/createTAppearance("STONE_FLOOR", 4, 1),

		/*Ruins*/ 
		createTAppearance("RUINS_COLUMN", 4, 6),
		createTAppearance("RUINS_FLOOR", 2, 6),
		createTAppearance("RUINS_WALL", 1, 6),
		createTAppearance("RUINS_FLOOR_H", 5, 6),
		createTAppearance("RUINS_STAIRS", 6, 6),
		createTAppearance("RUINS_DOOR", 3, 11),
		
		/*Caves*/
		createTAppearance("CAVE_WALL", 1, 7),
		createTAppearance("CAVE_FLOOR", 2, 7),
		createTAppearance("CAVE_WATER", 5, 7), /*Missing*/
		createTAppearance("LAVA", 4, 7),
		
		/*Warehouse*/
		createTAppearance("WAREHOUSE_WALL", 1, 12),
		createTAppearance("WAREHOUSE_FLOOR", 2, 12),
		
		/*Courtyard*/ /*Missing*/
		createTAppearance("COURTYARD_WALL", 1, 6), 
		createTAppearance("COURTYARD_FLOOR", 2, 6),
		createTAppearance("COURTYARD_COLUMN", 4, 6),
		createTAppearance("COURTYARD_FENCE", 6, 2),
		createTAppearance("COURTYARD_GRASS", 18, 1),
		createTAppearance("COURTYARD_FLOWERS", 13, 2),
		createTAppearance("COURTYARD_FOUNTAIN_CENTER", 9, 2),
		createTAppearance("COURTYARD_FOUNTAIN_AROUND", 7, 2),
		createTAppearance("COURTYARD_FOUNTAIN_POOL", 8, 2),
		createTAppearance("COURTYARD_TREE", 12, 2),
		createTAppearance("COURTYARD_RUINED_WALL", 1, 6),
		createTAppearance("COURTYARD_STAIRS",6, 6),
		
		createTAppearance("DINING_CHAIR",15, 5), /*Missing*/
		createTAppearance("DINING_TABLE",8, 10), /*Missing*/
		createTAppearance("MARBLE_STAIRSUP",6, 4),
		createTAppearance("MARBLE_STAIRSDOWN",5, 4),
		
		
		/*Dungeon*/
		createTAppearance("DUNGEON_FLOOR", 2, 10),
		createTAppearance("DUNGEON_DOOR", 4, 11),
		createTAppearance("DUNGEON_WALL", 1, 10),
		createTAppearance("DUNGEON_PASSAGE", 3, 10),
		createTAppearance("DUNGEON_DOWN", 9, 10),
		createTAppearance("DUNGEON_UP", 10, 10),
		
		/*Frank Lab, goes with Dungeon Theme*/
		createTAppearance("WIRES", 7, 10),
		createTAppearance("FRANK_TABLE", 8, 10),
		
		/*Clock Tower*/ /*Missing*/
		createTAppearance("TOWER_FLOOR", 13, 4),
		createTAppearance("TOWER_WALL", 11, 4),
		createTAppearance("TOWER_COLUMN", 4, 6),
		createTAppearance("TOWER_WINDOW",11, 4),
		createTAppearance("TOWER_DOWN", 9, 10),
		createTAppearance("TOWER_UP", 10, 10),
		createTAppearance("CAMPANARIUM", 13, 4),
		createTAppearance("TOWER_FLOOR_H", 5, 10),
		createTAppearance("TOWER_STAIRS", 4, 10),
		createTAppearance("CLOCK_GEAR_1", 4, 6),
		createTAppearance("CLOCK_GEAR_2", 4, 6),
		createTAppearance("CLOCK_GEAR_3", 4, 6),
		createTAppearance("CLOCK_GEAR_4", 4, 6),
		
		/*Castle Keep*/
		createTAppearance("BARRED_WINDOW", 2, 8),
		createTAppearance("DRACULA_THRONE1", 6, 8),
		createTAppearance("DRACULA_THRONE2", 5, 8),
		createTAppearance("DRACULA_THRONE2_X", 4, 8),
		createTAppearance("BALCONY", 3, 8),
		createTAppearance("STONE_STAIRWAY", 8, 8),
		createTAppearance("KEEP_FLOOR", 7, 8),
		createTAppearance("KEEP_WALL", 1, 8),
		createTAppearance("KEEP_CARPET",16,4),
		
		/*Void*/
		createTAppearance("VOID_STAR", 1, 9),
		createTAppearance("VOID_SUN", 2, 9),
		
		createTAppearance("TELEPORT", 7, 3,0,8),
		
		// Sacred weapons	// TODO use IT enum indices..
		createIAppearance("ART_CARD_SOL", 1, 1),
		createIAppearance("ART_CARD_MOONS", 2, 1),
		createIAppearance("ART_CARD_DEATH", 3, 1),
		createIAppearance("ART_CARD_LOVE", 4, 1),
		createIAppearance("RED_CARD", 5, 1),
		createIAppearance("GOLDEN_MEDALLION", 6, 1),
		createIAppearance("SILVER_MEDALLION", 7, 1),
		createIAppearance("THORN_BRACELET", 8, 1),
		createIAppearance("LIFE_POTION", 9, 1),
		createIAppearance("FLAME_BOOK", 1, 2),
		createIAppearance("ICE_BOOK", 2, 2),
		createIAppearance("LIT_BOOK", 3, 2),
		createIAppearance("HEART_CONTAINER", 4, 2),
		createIAppearance("MIRACLE_POTION", 5, 2),
		createIAppearance("TEPES_RING", 6, 2),
		createIAppearance("CLUE_PAGE2", 8, 2),
		createIAppearance("CLUE_PAGE3", 9, 2),
		createIAppearance("JUKEBOX", 9, 2),
		createIAppearance("CLUE_PAGE1", 7, 2),
		createIAppearance("JUMPING_WING", 1, 3),
		createIAppearance("FIRE_GEM", 2, 3),
		createIAppearance("FLAME_ITEM", 3, 3),
		createIAppearance("MAGIC_SHIELD", 8, 4),
		createIAppearance("LIGHT_CRYSTAL", 4, 3),
		createIAppearance("LANTERN", 5, 3),
		createIAppearance("SOUL_RECALL", 6, 3),
		createIAppearance("SUN_CARD", 7, 3),
		createIAppearance("MOON_CARD", 8, 3),
		createIAppearance("HEAL_POTION", 9, 3),
		createIAppearance("HEAL_HERB", 1, 4),
		createIAppearance("OXY_HERB", 1, 4),
		createIAppearance("BIBUTI", 2, 4),
		createIAppearance("GARLIC", 4, 4),
		createIAppearance("TORCH", 5, 4),
		createIAppearance("SILK_BAG", 6, 4),
		createIAppearance("LAUREL", 7, 4),
		createIAppearance("VARMOR", 1, 8),
		createIAppearance("VEST", 2, 8),
		createIAppearance("STUDDED_LEATHER", 3, 8),
		createIAppearance("LEATHER_ARMOR", 4, 8),
		createIAppearance("CLOTH_TUNIC", 5, 8),
		createIAppearance("FINE_GARMENT", 5, 8),
		createIAppearance("CUIRASS", 6, 8),
		createIAppearance("SUIT", 7, 8),
		createIAppearance("PLATE", 8, 8),
		createIAppearance("DIAMOND_PLATE", 9, 8),
		createIAppearance("BOW", 1, 11),
		createIAppearance("HOLBEIN_DAGGER", 2, 11),
		createIAppearance("WEREBANE", 3, 11),
		createIAppearance("SHOTEL", 4, 11),
		createIAppearance("COMBAT_KNIFE", 5, 11),
		createIAppearance("STAKE", 6, 11),
		createIAppearance("BASELARD", 7, 11),
		createIAppearance("KAISER_KNUCKLE", 1, 12),
		createIAppearance("MARTIAL_ARMBAND", 2, 12),
		createIAppearance("TULKAS_FIST", 3, 12),
		createIAppearance("SPIKY_KNUCKLES", 4, 12),
		createIAppearance("COMBAT_GAUNTLET", 5, 12),
		createIAppearance("KNUCKLES", 6, 12),
		createIAppearance("GAUNTLET", 7, 12),
		createIAppearance("HAMMER_JUSTICE", 1, 13),
		createIAppearance("MORNING_STAR", 2, 13),
		createIAppearance("FLAIL", 3, 13),
		createIAppearance("MACE", 4, 13),
		createIAppearance("SILVER_HANDGUN", 1, 14),
		createIAppearance("REVOLVER", 2, 14),
		createIAppearance("HANDGUN", 3, 14),
		createIAppearance("AGUEN", 4, 14),
		createIAppearance("CROSSBOW", 5, 14),
		createIAppearance("ROD", 1, 15),
		createIAppearance("STAFF", 2, 15),
		createIAppearance("BLADE_RINGSET", 1, 16),
		createIAppearance("COMBAT_RINGS", 2, 16),
		createIAppearance("SPIKED_RINGS", 3, 16),
		createIAppearance("RINGS", 4, 16),
		createIAppearance("TOWER_SHIELD", 1, 17),
		createIAppearance("BUCKLER", 2, 17),
		createIAppearance("WOODEN_SHIELD", 3, 17),
		createIAppearance("ROUND_SHIELD", 4, 17),
		createIAppearance("SHIELD", 5, 17),
		createIAppearance("DUALBLADE_SPEAR", 1, 18),
		createIAppearance("HALBERD", 2, 18),
		createIAppearance("ALCARDE_SPEAR", 3, 18),
		createIAppearance("BATTLE_SPEAR", 4, 18),
		createIAppearance("LONG_SPEAR", 5, 18),
		createIAppearance("SHORT_SPEAR", 6, 18),
		createIAppearance("MASAMUNE", 1, 19),
		createIAppearance("CRISSAEGRIM", 2, 19),
		createIAppearance("TERMINUS", 3, 19),
		createIAppearance("MOURNEBLADE", 4, 19),
		createIAppearance("OSAFUNE", 5, 19),
		createIAppearance("MORMEGIL", 6, 19),
		createIAppearance("GRAM", 7, 19),
		createIAppearance("RAPIER", 8, 19),
		createIAppearance("BASTARDSWORD", 9, 19),
		createIAppearance("BROADSWORD", 1, 22),
		createIAppearance("VORPAL_BLADE", 1, 20),
		createIAppearance("FIREBRAND", 2, 20),
		createIAppearance("ICEBRAND", 3, 20),
		createIAppearance("GURTHANG", 4, 20),
		createIAppearance("KATANA", 5, 20),
		createIAppearance("FALCHION", 6, 20),
		createIAppearance("HARPER", 7, 20),
		createIAppearance("HADOR", 8, 20),
		createIAppearance("GLADIUS", 9, 20),
		createIAppearance("CUTLASS", 1, 21),
		createIAppearance("CLAYMORE", 2, 21),
		createIAppearance("ETHANOS_BLADE", 3, 21),
		createIAppearance("FLAMBERGE", 4, 21),
		createIAppearance("SABRE", 5, 21),
		createIAppearance("MABLUNG", 6, 21),
		createIAppearance("SCIMITAR", 7, 21),
		createIAppearance("ESTOC", 8, 21),
		createIAppearance("SHORT_SWORD", 9, 21),
		createIAppearance("BWAKA_KNIFE", 1, 24),
		createIAppearance("CHAKRAM", 2, 24),
		createIAppearance("BUFFALO_STAR", 3, 24),
		createIAppearance("SHURIKEN", 4, 24),
		createIAppearance("THROWING_KNIFE", 5, 24),
		createIAppearance("LIT_WHIP", 1, 25),
		createIAppearance("FLAME_WHIP", 2, 25),
		createIAppearance("VKILLERW", 3, 25),
		createIAppearance("WHIP", 4, 25),
		createIAppearance("CHAIN_WHIP", 5, 25),
		createIAppearance("THORN_WHIP", 6, 25),
		createIAppearance("LEATHER_WHIP", 7, 25),
		
		
		// Monsters TODO use MT enumeration ids?
		// TODO Could pass another 'last minute decision-making' parameter of:
		// 'std / lrg'. it could index into a small array of w,h params?
		createAppearance("R_SKELETON", Textures.MonstersImage, 1, 1),
		createAppearance("GZOMBIE", Textures.MonstersImage, 4, 2),
		createAppearance("ZOMBIE", Textures.MonstersImage, 7, 8),
		createAppearance("WHITE_SKELETON", Textures.MonstersImage, 1, 1),
		createAppearance("PANTHER", Textures.MonstersImage, 5, 3),
		createBAppearance("WARG", Textures.BigMonstersImage, 7, 1),
		createAppearance("BLACK_KNIGHT", Textures.MonstersImage, 9, 4),
		createAppearance("APE_SKELETON", Textures.MonstersImage, 7, 3),
		createBAppearance("PARANTHROPUS", Textures.BigMonstersImage, 1, 1),
		createAppearance("BAT", Textures.MonstersImage, 8, 3),
		createAppearance("SKULL_HEAD", Textures.MonstersImage, 2, 6),
		createAppearance("SKULL_LORD", Textures.MonstersImage, 3, 6),
		createAppearance("MERMAN", Textures.MonstersImage, 9, 5),
		createAppearance("WEREBEAR", Textures.MonstersImage, 6, 6),
		createAppearance("HUNCHBACK", Textures.MonstersImage, 6, 7),
		createAppearance("BONE_ARCHER", Textures.MonstersImage, 2, 1),
		createAppearance("SKELETON_PANTHER", Textures.MonstersImage, 6, 3),
		createAppearance("BONE_PILLAR", Textures.MonstersImage, 9, 6),
		createAppearance("AXE_KNIGHT", Textures.MonstersImage, 1, 5),
		createAppearance("MEDUSA_HEAD", Textures.MonstersImage, 4, 6),
		createAppearance("DURGA", Textures.MonstersImage, 1, 4),
		createAppearance("SKELETON_ATHLETE", Textures.MonstersImage, 3, 1),
		createAppearance("BLADE_SOLDIER", Textures.MonstersImage, 1, 2),
		createAppearance("BONE_HALBERD", Textures.MonstersImage, 4, 1),
		createAppearance("CROW", Textures.MonstersImage, 4, 8),
		createAppearance("BLOOD_SKELETON", Textures.MonstersImage, 9, 1),
		createAppearance("LIZARD_SWORDSMAN", Textures.MonstersImage, 7, 5),
		createBAppearance("COCKATRICE", Textures.BigMonstersImage, 4, 1),
		createAppearance("COOPER_ARMOR", Textures.MonstersImage, 10, 4),
		createAppearance("GHOUL", Textures.MonstersImage, 8, 8),
		createAppearance("SALOME", Textures.MonstersImage, 7, 4),
		createAppearance("ECTOPLASM", Textures.MonstersImage, 3, 3),
		createBAppearance("RULER_SWORD_LV1", Textures.BigMonstersImage, 2, 2),
		createBAppearance("BEAST_DEMON", Textures.BigMonstersImage, 2, 1),
		createBAppearance("DEVIL", Textures.BigMonstersImage, 3, 1),
		createAppearance("BALLOON_POD", Textures.MonstersImage, 5, 7),
		createAppearance("LILITH", Textures.MonstersImage, 5, 4),
		createAppearance("BONE_MUSKET", Textures.MonstersImage, 5, 1),
		createAppearance("KILLER_PLANT", Textures.MonstersImage, 3, 7),
		createAppearance("VAMPIRE_BAT", Textures.MonstersImage, 9, 3),
		createBAppearance("DEATH_MANTIS", Textures.BigMonstersImage, 5, 2),
		createAppearance("DHURON", Textures.MonstersImage, 7, 2),
		createAppearance("DRAGON_SKULL_CANNON", Textures.MonstersImage, 10, 6),
		createAppearance("MUMMY_MAN", Textures.MonstersImage, 5, 2),
		createAppearance("ZELDO", Textures.MonstersImage, 8, 2),
		createAppearance("MUD_MAN", Textures.MonstersImage, 2, 3),
		createAppearance("CAGNAZOO", Textures.MonstersImage, 4, 5),
		createBAppearance("ALRAUNE", Textures.BigMonstersImage, 4, 4),
		createBAppearance("GOLEM", Textures.BigMonstersImage, 2, 3),
		createAppearance("ARACHNE", Textures.MonstersImage, 3, 4),
		createAppearance("SPEAR_SKELETON", Textures.MonstersImage, 8, 1),
		
		createAppearance("KNIFE_MERMAN", Textures.MonstersImage, 10, 5),
		createAppearance("MASTER_LIZARD", Textures.MonstersImage, 8, 5),
		createAppearance("WHIP_SKELETON", Textures.MonstersImage, 6, 1),
		createAppearance("FROZEN_SHADE", Textures.MonstersImage, 10, 2),
		createAppearance("MINOTAUR", Textures.MonstersImage, 7, 6),
		createBAppearance("TRITON", Textures.BigMonstersImage, 6, 2),
		createAppearance("NOVA_SKELETON", Textures.MonstersImage, 10, 1),
		createBAppearance("ARMOR_LORD", Textures.BigMonstersImage, 1, 3),
		createAppearance("FLEA_ARMOR", Textures.MonstersImage, 7, 7),
		createAppearance("BUER", Textures.MonstersImage, 4, 7),
		createAppearance("WIGHT", Textures.MonstersImage, 9, 2),
		createAppearance("SPECTER", Textures.MonstersImage, 4, 3),
		createBAppearance("RULER_SWORD_LV2", Textures.BigMonstersImage, 3, 2),
		createAppearance("CURLY", Textures.MonstersImage, 2, 4),
		createBAppearance("FIRE_WARG", Textures.BigMonstersImage, 1, 2),
		createAppearance("BONE_ARK", Textures.MonstersImage, 1, 7),
		createAppearance("MIMIC", Textures.MonstersImage, 5, 6),
		createBAppearance("MANTICORE", Textures.BigMonstersImage, 7, 2),
		createAppearance("FLAME_KNIGHT", Textures.MonstersImage, 2, 5),
		createBAppearance("ARMOR_GUARDIAN", Textures.BigMonstersImage, 1, 4),
		createBAppearance("DEMON_LORD", Textures.BigMonstersImage, 6, 1),
		createAppearance("HEAT_SHADE", Textures.MonstersImage, 1, 3),
		createBAppearance("FLESH_GOLEM", Textures.BigMonstersImage, 4, 3),
		createAppearance("WEREWOLF", Textures.MonstersImage, 8, 6),
		createBAppearance("ALURA_UNE", Textures.BigMonstersImage, 5, 4),
		createAppearance("DRAHIGNAZOO", Textures.MonstersImage, 5, 5),
		createAppearance("SUCCUBUS", Textures.MonstersImage, 6, 4),
		createAppearance("BLADE_MASTER", Textures.MonstersImage, 2, 2),
		createBAppearance("BASILISK", Textures.BigMonstersImage, 5, 1),
		createAppearance("GARGOYLE", Textures.MonstersImage, 6, 5),
		createAppearance("HARPY", Textures.MonstersImage, 4, 4),
		createAppearance("KICKER_SKELETON", Textures.MonstersImage, 7, 1),
		createBAppearance("BEHEMOTH", Textures.BigMonstersImage, 6, 3),
		createBAppearance("DISCUS_LORD", Textures.BigMonstersImage, 7, 3),
		createBAppearance("GIANT_ARMOR", Textures.BigMonstersImage, 2, 4),
		createAppearance("WITCH", Textures.MonstersImage, 8, 4),
		createAppearance("MANDRAGORA", Textures.MonstersImage, 8, 7),
		createBAppearance("IRON_GOLEM", Textures.BigMonstersImage, 2, 3),
		createBAppearance("VICTORY_ARMOR", Textures.BigMonstersImage, 3, 4),
		createBAppearance("RULER_SWORD_LV3", Textures.BigMonstersImage, 4, 2),
		createAppearance("SPEAR_KNIGHT", Textures.MonstersImage, 3, 9),
		createAppearance("FLYING_SPEAR_SKELETON", Textures.MonstersImage, 4, 9),

		createBAppearance("GIANTBAT", Textures.BigMonstersImage, 2, 5),
		createBAppearance("DEATH", Textures.BigMonstersImage, 3, 5),
		createAppearance("SICKLE", Textures.MonstersImage, 2, 9),
		createBAppearance("DRACULA", Textures.BigMonstersImage, 4, 5),
		createBAppearance("MEDUSA", Textures.BigMonstersImage, 1, 5),
		createAppearance("SNAKE", Textures.MonstersImage, 1, 9),
		createBAppearance("FRANK", Textures.BigMonstersImage, 7, 4),
		createAppearance("IGOR", Textures.MonstersImage, 10, 8),
		createBAppearance("DEMON_DRACULA", Textures.BigMonstersImage, 5, 5),
		createBAppearance("AKMODAN", Textures.BigMonstersImage, 6, 5),
		createBAppearance("DRAGON_KING", Textures.BigMonstersImage, 1, 6),
		createBAppearance("ORLOX", Textures.BigMonstersImage, 4, 6),
		createBAppearance("WATER_DRAGON", Textures.BigMonstersImage, 2, 6),
		createBAppearance("LEGION", Textures.BigMonstersImage, 7, 5),
		createBAppearance("CERBERUS", Textures.BigMonstersImage, 3, 6),
		createAppearance("DOPPELGANGER", Textures.MonstersImage, 6, 5), /*Pending*/
		
		
		createAppearance("S_CAT", Textures.MonstersImage, 5, 9),
		createAppearance("S_BIRD", Textures.MonstersImage, 6, 9),
		createAppearance("S_TURTLE", Textures.MonstersImage, 7, 9),
		createBAppearance("S_TIGER", Textures.MonstersImage, 8, 9),
		createAppearance("S_EAGLE", Textures.MonstersImage, 9, 9),
		createAppearance("S_TORTOISE", Textures.MonstersImage, 7, 9), /*Pending*/
		createBAppearance("S_DRAGON", Textures.MonstersImage, 6, 5), /*Pending*/
		
		// Features
		createXAppearance("CANDLE",
			Textures.FeaturesImage,
			0,
			7 * halfw,
			halfw,
			tw
		),
		createXAppearance("URN_FLAME",
			Textures.FeaturesImage,
			2 * halfw,
			7 * halfw,
			halfw,
			tw,
			12
		),
		createAppearance("FLAME",
			Textures.EffectsImage,
			13 * tw,
			14 * tw,
			tw,
			tw,
			0,
			0
		),

		createFAppearance("SMALLHEART", 2, 1),
		createFAppearance("DAGGER", 3, 1),
		createFAppearance("AXE", 4, 1),
		createFAppearance("VIAL", 8, 1),
		createFAppearance("CROSS", 5, 1),
		createFAppearance("CLOCK", 6, 1),
		createFAppearance("BIGHEART", 1, 2),
		createFAppearance("KEY", 2, 2),
		createFAppearance("UPGRADE", 3, 2),
		createFAppearance("ROSARY", 5, 2),
		createFAppearance("COIN", 6, 2),
		createFAppearance("RED_MONEY_BAG", 7, 2),
		createFAppearance("BLUE_MONEY_BAG", 8, 2),
		createFAppearance("WHITE_MONEY_BAG", 9, 2),
		createFAppearance("CROWN", 1, 3),
		createFAppearance("CHEST", 2, 3),
		createFAppearance("MOAUI_HEAD", 3, 3),
		createFAppearance("RAINBOW_MONEY_BAG", 10, 2),
		createFAppearance("POT_ROAST", 4, 3),
		createFAppearance("INVISIBILITY_POTION", 5, 3),
		createFAppearance("BIBLE", 7, 1),
		createFAppearance("CRYSTAL", 9, 1),
		createFAppearance("FIST", 10, 1),
		createFAppearance("REBOUND_CRYSTAL", 9, 1),
		createFAppearance("MUPGRADE", 4, 2),
		createFAppearance("BLAST_CRYSTAL", 9, 1),
		

		createAppearance("MOUND", Textures.EffectsImage, 11, 17),

		// Characters
		createAppearance("VKILLER", Textures.CharactersImage, 1, 1),
		createAppearance("VANQUISHER", Textures.CharactersImage, 3, 1),
		createAppearance("RENEGADE", Textures.CharactersImage, 5, 1),
		createAppearance("INVOKER", Textures.CharactersImage, 1, 2),
		createAppearance("MANBEAST", Textures.CharactersImage, 3, 2),
		createAppearance("BEAST", Textures.CharactersImage, 5, 2),
		createAppearance("KNIGHT", Textures.CharactersImage, 1, 3),
		
		createAppearance("VKILLER_W", Textures.CharactersImage, 2, 1),
		createAppearance("SONIA_B", Textures.CharactersImage, 2, 1),
		createAppearance("VANQUISHER_W", Textures.CharactersImage, 4, 1),
		createAppearance("RENEGADE_W", Textures.CharactersImage, 6, 1),
		createAppearance("INVOKER_W", Textures.CharactersImage, 2, 2),
		createAppearance("MANBEAST_W", Textures.CharactersImage, 4, 2),
		createAppearance("BEAST_W", Textures.CharactersImage, 6, 2),
		createAppearance("KNIGHT_W", Textures.CharactersImage, 2, 3),

		createAppearance("MORPHED_WOLF", Textures.MonstersImage, 1, 10), 
		createAppearance("MORPHED_WOLF2", Textures.MonstersImage, 2, 10), 
		createAppearance("MORPHED_BAT", Textures.MonstersImage, 3, 10), 
		createAppearance("MORPHED_BAT2", Textures.MonstersImage, 4, 10), 
		createAppearance("MORPHED_MYST", Textures.MonstersImage, 5, 10), 
		createAppearance("MORPHED_MYST2", Textures.MonstersImage, 6, 10), 
		createAppearance("MORPHED_WEREBEAR", Textures.MonstersImage, 7, 10), 
		createAppearance("MORPHED_WEREDEMON", Textures.MonstersImage, 8, 10), 
		createAppearance("MORPHED_WEREWOLF", Textures.MonstersImage, 10, 10), 
		createAppearance("MORPHED_WEREBEAST", Textures.MonstersImage, 9, 10),
		createAppearance("MORPHED_LUPINE", Textures.CharactersImage, 5, 2), 

		createAppearance("SOLEIYU_B_KID", Textures.CharactersImage, 6, 6),
		createAppearance("MAN", Textures.CharactersImage, 3, 3),
		createAppearance("WOMAN", Textures.CharactersImage, 4, 3),
		createAppearance("OLDMAN", Textures.CharactersImage, 5, 3),
		createAppearance("OLDWOMAN", Textures.CharactersImage, 6, 3),
		createAppearance("MERCHANT", Textures.CharactersImage, 1, 4),
		createAppearance("PRIEST", Textures.CharactersImage, 2, 4),
		createAppearance("DOG", Textures.CharactersImage, 3, 4),
		createAppearance("HOSTAGE_GUY", Textures.CharactersImage, 4, 4),
		createAppearance("HOSTAGE_GIRL", Textures.CharactersImage, 5, 4),
		createAppearance("CLARA", Textures.CharactersImage, 1, 6),
		createAppearance("VINDELITH", Textures.CharactersImage, 1, 6),
		createAppearance("CLAW", Textures.CharactersImage, 5, 5),
		createAppearance("MAIDEN", Textures.CharactersImage, 4, 5),
		createAppearance("MELDUCK", Textures.CharactersImage, 3, 5),
		createAppearance("ICEY", Textures.CharactersImage, 4, 6),
		createAppearance("LARDA", Textures.CharactersImage, 3, 3),
		createAppearance("CHRISTOPHER_BELMONT_NPC", Textures.CharactersImage, 3, 6),
		createAppearance("BARRETT", Textures.CharactersImage, 5, 6),

		// Weapons
		};
	}


	public Appearance[] getAppearances() {
		return defs;
	}
	
	// Graphics Appearance Types
	enum GAT {
		
	}
	
	public GFXAppearance createAppearance(String ID, BufferedImage bigImage, int xpos, int ypos) {
		xpos--;
		ypos--;
		
		final int TW = conf.tileWidth;
		
		try {
			BufferedImage img = ImageUtils.crearImagen(bigImage,
				xpos*TW, ypos*TW, TW, TW);
			GFXAppearance ret = new GFXAppearance(ID, img, 0, 0);
			return ret;
		} catch (Exception e) {
			Game.crash("Error loading image ", e);
		}
		return null;
	}
	
	
	// catch-all general...??
	public GFXAppearance createAppearance(String ID, BufferedImage bigImage,
			int xpos, int ypos, int width, int height,int superw, int superh) {
		xpos--;
		ypos--;
		try {
			BufferedImage img = ImageUtils.crearImagen(bigImage, xpos, ypos, width, height);
			GFXAppearance ret = new GFXAppearance(ID, img,superw,superh);
			return ret;
		} catch (Exception e) {
			Game.crash("Error loading image ", e);
		}
		return null;
	}
	
	// ??
	public GFXAppearance createXAppearance(String ID, BufferedImage bigImage, int xpos, int ypos, int width, int height) {
		return createXAppearance(ID, bigImage, xpos, ypos, width, height, 0);
	}
	
	
	// ??
	public GFXAppearance createXAppearance(String ID, BufferedImage bigImage, int xpos, int ypos, int width, int height, int yoff) {
		try {
			BufferedImage img = ImageUtils.crearImagen(bigImage, xpos, ypos, width, height);
			GFXAppearance ret = new GFXAppearance(ID, img,(width-48)/2,(height-64)/2+yoff);
			return ret;
		} catch (Exception e) {
			Game.crash("Error loading image ", e);
		}
		return null;
	}
	
	
	// init separated single-Tile graphic
	public GFXAppearance createTAppearance(String ID, int xpos, int ypos) {
		xpos--;
		ypos--;
		//Textures imgConfig = configuration.textures;
		BufferedImage bigImage = Textures.TerrainImage;
		BufferedImage bigDarkImage = Textures.DarkTerrainImage;
		BufferedImage bigNiteImage = Textures.NightTerrainImage;
		BufferedImage bigDarkNiteImage = Textures.DarkNightTerrainImage;
		
		final int
			TW = conf.tileWidth,
			CH = conf.cellHeight;
		try {
			BufferedImage
				img = ImageUtils.crearImagen(bigImage, xpos*TW, ypos*CH, TW, CH),
			darkimg = ImageUtils.crearImagen(bigDarkImage, xpos*TW, ypos*CH, TW, CH),
			niteimg = ImageUtils.crearImagen(bigNiteImage, xpos*TW, ypos*CH, TW, CH),
			darkniteimg = ImageUtils.crearImagen(bigDarkNiteImage, xpos*TW, ypos*CH, TW, CH);
			return new GFXAppearance(ID, img, darkimg, niteimg, darkniteimg, 0,0);
			
		} catch (Exception e) {
			Game.crash("Error loading terrain image ", e);
		}
		return null;
	}
	
	// Terrain/Tile appearance
	public GFXAppearance createTAppearance(String ID, int xpos, int ypos, int xoff, int yoff) {
		xpos--;
		ypos--;
		///Textures imgConfig = configuration.textures;
		BufferedImage bigImage = Textures.TerrainImage;
		BufferedImage bigDarkImage = Textures.DarkTerrainImage;
		BufferedImage bigNiteImage = Textures.NightTerrainImage;
		BufferedImage bigDarkNiteImage = Textures.DarkNightTerrainImage;
		final int
			TW = conf.tileWidth,
			CH = conf.cellHeight,
			UIS= conf.viewportUserInterfaceScale;
		try {
			BufferedImage
				img = ImageUtils.crearImagen(bigImage, xpos*TW, ypos*CH, TW, CH),
				darkimg = ImageUtils.crearImagen(bigDarkImage, xpos*TW, ypos*CH, TW, CH);
			BufferedImage niteimg = ImageUtils.crearImagen(bigNiteImage, xpos*TW, ypos*CH, TW, CH);
			BufferedImage darkniteimg = ImageUtils.crearImagen(bigDarkNiteImage, xpos*TW, ypos*CH, TW, CH);
			return new GFXAppearance(ID, img, darkimg, niteimg, darkniteimg, xoff * UIS, yoff * UIS);
		} catch (Exception e) {
			Game.crash("Error loading terrain image ", e);
		}
		return null;
	}
	
	
	/** Big Sprite Appearance (96x96 @2x, or 48x48 */
	public GFXAppearance createBAppearance(String ID, BufferedImage bigImage, int xpos, int ypos) {
		xpos--;
		ypos--;
		final int BTW = conf.bigTileWidth;
		try {
			BufferedImage img = ImageUtils.crearImagen(bigImage, xpos*BTW, ypos*BTW, BTW, BTW);
			GFXAppearance ret = new GFXAppearance(ID, img, 8, 16);	// ?!?!
			return ret;
		} catch (Exception e) {
			Game.crash("Error loading image ", e);
		}
		return null;
	}
	
	
	// Item OR FEATURE Appearance. (could do with pinning down: what's a FEATURE?
	public GFXAppearance createIAppearance(String ID, int xpos, int ypos) {
		BufferedImage
			bigImage = Textures.ItemsImage,
			iconImage = Textures.ItemIconsImage;
		xpos--;
		ypos--;
		final int HW = conf.tileWidth / 2;	// halfw
		try {
			BufferedImage img = ImageUtils.crearImagen(bigImage, xpos*HW, ypos*HW, HW, HW);
			BufferedImage iconImg = ImageUtils.crearImagen(
				iconImage, xpos * ICON_SIZE, ypos * ICON_SIZE, ICON_SIZE, ICON_SIZE);
			GFXAppearance ret = new GFXAppearance(ID, img, iconImg, -8, -8);
			return ret;
		} catch (Exception e){
			Game.crash("Error loading image ", e);
		}
		return null;
	}
	
	
	// is feature.
	public GFXAppearance createFAppearance(String ID, int xpos, int ypos) {
		BufferedImage bigImage, iconImage;
		bigImage  = Textures.FeaturesImage;
		iconImage = Textures.FeatureIconsImage;
		
		xpos--;
		ypos--;
		final int HW = conf.tileWidth / 2;	// halfw
		try {
			BufferedImage img = ImageUtils.crearImagen(bigImage, xpos*HW, ypos*HW, HW, HW);
			BufferedImage iconImg = ImageUtils.crearImagen(
				iconImage, xpos * ICON_SIZE, ypos * ICON_SIZE, ICON_SIZE, ICON_SIZE);
			GFXAppearance ret = new GFXAppearance(ID, img, iconImg, -8, -8);
			return ret;
		} catch (Exception e) {
			Game.crash("Error loading image ", e);
		}
		return null;
	}
	
}