package crl.conf.console.data;
import sz.csi.ConsoleSystemInterface;
import crl.ui.*;
import crl.ui.consoleUI.CharAppearance;

public class CharAppearances {
	public static Appearance[] defs = {
		new CharAppearance("NOTHING", ' ', ConsoleSystemInterface.BLACK),
		new CharAppearance("VOID", ' ', ConsoleSystemInterface.BLACK),
		new CharAppearance("CHRISTOPHER_B", '@', ConsoleSystemInterface.WHITE),
		new CharAppearance("SOLEIYU_B", '@', ConsoleSystemInterface.RED),
		new CharAppearance("BADBELMONT", '@', ConsoleSystemInterface.GRAY),
		new CharAppearance("PRELUDE_DRACULA", '@', ConsoleSystemInterface.RED),
		new CharAppearance("SOLEIYU_B_KID", '@', ConsoleSystemInterface.TEAL),
		new CharAppearance("SONIA_B", '@', ConsoleSystemInterface.PURPLE),
		new CharAppearance("VKILLER", '@', ConsoleSystemInterface.BROWN),
		new CharAppearance("VKILLER_W", '@', ConsoleSystemInterface.PURPLE),
		new CharAppearance("MERCHANT", '@', ConsoleSystemInterface.GREEN),
		new CharAppearance("CLAW", '@', ConsoleSystemInterface.GREEN),
		new CharAppearance("VINDELITH", '@', ConsoleSystemInterface.DARK_BLUE),
		new CharAppearance("CLARA", '@', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("MAIDEN", '@', ConsoleSystemInterface.MAGENTA),
		new CharAppearance("MELDUCK", '@', ConsoleSystemInterface.BROWN),
		
		new CharAppearance("ICEY", '@', ConsoleSystemInterface.CYAN),
		new CharAppearance("LARDA", '@', ConsoleSystemInterface.RED),
		new CharAppearance("SHADOW", '_', ConsoleSystemInterface.WHITE),
		new CharAppearance("CHRISTOPHER_BELMONT_NPC", '@', ConsoleSystemInterface.BROWN),
		new CharAppearance("BARRETT", '@', ConsoleSystemInterface.CYAN),
		
		new CharAppearance("TELEPORT", '*', ConsoleSystemInterface.CYAN),
		
		/*Town*/
		new CharAppearance("TOWN_GRASS", ',', ConsoleSystemInterface.GREEN),
		new CharAppearance("TOWN_WALL", '#', ConsoleSystemInterface.GRAY),
		new CharAppearance("HOUSE_FLOOR", '-', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("TOWN_DOOR", '/', ConsoleSystemInterface.BROWN),
		new CharAppearance("MIDWALKWAY", ':', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("TOWN_CHURCH_FLOOR", '.', ConsoleSystemInterface.MAGENTA),
		new CharAppearance("TOWN_WATERWAY", '~', ConsoleSystemInterface.BLUE),
		new CharAppearance("BRICKWALKWAY",  '.', ConsoleSystemInterface.BROWN),
		new CharAppearance("BRICKWALKWAY2", '*', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("TOWN_ROOF", '^', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("TOWN_STAIRSDOWN", '>', ConsoleSystemInterface.BROWN),
		new CharAppearance("TOWN_STAIRSUP", '<', ConsoleSystemInterface.BROWN),
		new CharAppearance("TOWN_TREE", '&', ConsoleSystemInterface.GREEN),
		new CharAppearance("TOWN_STAIRS", '=', ConsoleSystemInterface.GRAY),
		
		/*Dark Forest*/
		new CharAppearance("FOREST_TREE", '&', ConsoleSystemInterface.GREEN),
		new CharAppearance("FOREST_GRASS", '.', ConsoleSystemInterface.GREEN),
		new CharAppearance("FOREST_DIRT", ',', ConsoleSystemInterface.BROWN),
		new CharAppearance("FOREST_TREE_1", '\'', ConsoleSystemInterface.BROWN),
		new CharAppearance("FOREST_TREE_2", '\'', ConsoleSystemInterface.BROWN),
		new CharAppearance("WRECKED_CHARRIOT", '#', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("WRECKED_WHEEL", 'o', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("SIGN_POST", '+', ConsoleSystemInterface.LIGHT_GRAY),
		
		/*Castle Bridge*/
		new CharAppearance("WOODEN_BRIDGE", '#', ConsoleSystemInterface.BROWN),
		new CharAppearance("DARK_LAKE", '~', ConsoleSystemInterface.DARK_BLUE),
		new CharAppearance("BRIDGE_WALKWAY", ':', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("BRIDGE_COLUMN", 'O', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("STONE_BLOCK", '#', ConsoleSystemInterface.LIGHT_GRAY),
		
		
		/*Castle Garden*/
		new CharAppearance("GARDEN_GRASS", ',', ConsoleSystemInterface.GREEN),
		new CharAppearance("GARDEN_WALKWAY",  '.', ConsoleSystemInterface.BROWN),
		new CharAppearance("DEAD_STUMP", '&', ConsoleSystemInterface.BROWN),
		new CharAppearance("GARDEN_TREE", '&', ConsoleSystemInterface.GREEN),
		new CharAppearance("GARDEN_TORCH", 'I', ConsoleSystemInterface.BROWN),
		new CharAppearance("GARDEN_FENCE", '#', ConsoleSystemInterface.BROWN),
		new CharAppearance("CASTLE_DOOR", '[', ConsoleSystemInterface.RED),
		new CharAppearance("GARDEN_FOUNTAIN_CENTER", 'O', ConsoleSystemInterface.BLUE),
		new CharAppearance("GARDEN_FOUNTAIN_AROUND", '.', ConsoleSystemInterface.BLUE),
		new CharAppearance("GARDEN_FOUNTAIN_POOL", '~', ConsoleSystemInterface.BLUE),
		new CharAppearance("GARGOYLESTATUE", '&', ConsoleSystemInterface.GRAY),
		new CharAppearance("HUMANSTATUE", '@', ConsoleSystemInterface.GRAY),
		new CharAppearance("GARDEN_DOOR", '|', ConsoleSystemInterface.RED),
		new CharAppearance("GARDEN_WALL", '#', ConsoleSystemInterface.GRAY),
		new CharAppearance("CASTLE_WALL", '#', ConsoleSystemInterface.GRAY),

		/*Marble Hall*/
		new CharAppearance("GRAY_COLUMN", 'o', ConsoleSystemInterface.BROWN),
		new CharAppearance("MARBLE_COLUMN", 'o', ConsoleSystemInterface.BROWN),
		new CharAppearance("MARBLE_FLOOR", '.', ConsoleSystemInterface.WHITE),
		new CharAppearance("MARBLE_STAIRS", '=', ConsoleSystemInterface.GRAY),
		new CharAppearance("MARBLE_MIDFLOOR", ':', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("BIG_WINDOW", '=', ConsoleSystemInterface.BLUE),
		new CharAppearance("RED_CURTAIN", '~', ConsoleSystemInterface.RED),
		new CharAppearance("GODNESS_STATUE", '@', ConsoleSystemInterface.GRAY),
		new CharAppearance("RED_CARPET", '-', ConsoleSystemInterface.RED),
		
		new CharAppearance("QUARTERS_WALL", '#', ConsoleSystemInterface.RED),
		new CharAppearance("QUARTERS_FLOOR", '.', ConsoleSystemInterface.WHITE),
		
		/*Moat*/
		new CharAppearance("MOSS_COLUMN", 'O', ConsoleSystemInterface.GRAY),
		new CharAppearance("MOSS_FLOOR", '.', ConsoleSystemInterface.GREEN),
		new CharAppearance("MOSS_STAIRS", '=', ConsoleSystemInterface.BROWN),
		new CharAppearance("MOSS_MIDFLOOR", ':', ConsoleSystemInterface.DARK_BLUE),
		
		/*Sewers*/
		new CharAppearance("SEWERS_WALL", '#', ConsoleSystemInterface.BLUE),
		new CharAppearance("SEWERS_FLOOR", '~', ConsoleSystemInterface.DARK_BLUE),
		new CharAppearance("SEWERS_UP", '<', ConsoleSystemInterface.BLUE),
		new CharAppearance("SEWERS_DOWN", '>', ConsoleSystemInterface.BLUE),
		new CharAppearance("WEIRD_MACHINE", '*', ConsoleSystemInterface.CYAN),
		
		
		new CharAppearance("RUSTY_PLATFORM", '\"', ConsoleSystemInterface.GRAY),
		new CharAppearance("MOSS_WATERWAY", '~', ConsoleSystemInterface.BLUE),
		new CharAppearance("MOSS_WATERWAY_ETH", '~', ConsoleSystemInterface.BLUE),
		new CharAppearance("MOAT_DOWN", '>', ConsoleSystemInterface.BLUE),
		new CharAppearance("MOAT_UP", '<', ConsoleSystemInterface.BLUE),
		
		/*Alchemy Lab*/
		new CharAppearance("RED_FLOOR", '.', ConsoleSystemInterface.BROWN),
		new CharAppearance("RED_WALL", '#', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("SMALL_WINDOW", '+', ConsoleSystemInterface.DARK_RED),
		
		/*Chapel*/
		new CharAppearance("CHURCH_WALL", '#', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("CHURCH_FLOOR", '.', ConsoleSystemInterface.WHITE),
		new CharAppearance("CHURCH_WOODEN_BARRIER_H", '+', ConsoleSystemInterface.BROWN),
		new CharAppearance("CHURCH_WOODEN_BARRIER_V", '|', ConsoleSystemInterface.BROWN),
		new CharAppearance("CHURCH_CHAIR", ';', ConsoleSystemInterface.BROWN),
		new CharAppearance("CHURCH_CONFESSIONARY", '#', ConsoleSystemInterface.BROWN),
		new CharAppearance("CHURCH_CARPET", '-', ConsoleSystemInterface.RED),
		new CharAppearance("ATRIUM", ':', ConsoleSystemInterface.BROWN),
		new CharAppearance("CHURCH_STAINED_WINDOW", '+', ConsoleSystemInterface.MAGENTA),
		new CharAppearance("CHURCH_FLOOR_H", ':', ConsoleSystemInterface.WHITE),
		
		/*Ruins*/
		/**/new CharAppearance("STONE_WALL", '#', ConsoleSystemInterface.GRAY),
		/**/new CharAppearance("STONE_FLOOR", '.', ConsoleSystemInterface.LIGHT_GRAY),

		/*Ruins*/
		new CharAppearance("RUINS_COLUMN", 'O', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("RUINS_FLOOR", '.', ConsoleSystemInterface.WHITE),
		new CharAppearance("RUINS_WALL", '#', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("RUINS_FLOOR_H", ':', ConsoleSystemInterface.WHITE),
		new CharAppearance("RUINS_STAIRS", '=', ConsoleSystemInterface.WHITE),
		new CharAppearance("RUINS_DOOR", '/', ConsoleSystemInterface.BROWN),
		
		/*Caves*/
		new CharAppearance("CAVE_WALL", '*', ConsoleSystemInterface.GRAY),
		new CharAppearance("CAVE_FLOOR", ',', ConsoleSystemInterface.BROWN),
		new CharAppearance("CAVE_WATER", '~', ConsoleSystemInterface.BLUE),
		new CharAppearance("LAVA", '~', ConsoleSystemInterface.RED),
		
		/*Warehouse*/
		new CharAppearance("WAREHOUSE_WALL", '#', ConsoleSystemInterface.GRAY),
		new CharAppearance("WAREHOUSE_FLOOR", '.', ConsoleSystemInterface.BROWN),
		
		/*Courtyard*/
		new CharAppearance("COURTYARD_WALL", '#', ConsoleSystemInterface.LIGHT_GRAY), 
		new CharAppearance("COURTYARD_FLOOR", '.', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("COURTYARD_COLUMN", 'O', ConsoleSystemInterface.WHITE),
		new CharAppearance("COURTYARD_FENCE", '+', ConsoleSystemInterface.GRAY),
		new CharAppearance("COURTYARD_GRASS", ',', ConsoleSystemInterface.GREEN),
		new CharAppearance("COURTYARD_FLOWERS", '*', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("COURTYARD_FOUNTAIN_CENTER", 'O', ConsoleSystemInterface.CYAN),
		new CharAppearance("COURTYARD_FOUNTAIN_AROUND", '.', ConsoleSystemInterface.BLUE),
		new CharAppearance("COURTYARD_FOUNTAIN_POOL", '~', ConsoleSystemInterface.DARK_BLUE),
		new CharAppearance("COURTYARD_TREE", '&', ConsoleSystemInterface.BROWN),
		new CharAppearance("COURTYARD_RUINED_WALL", '#', ConsoleSystemInterface.BROWN),
		new CharAppearance("COURTYARD_STAIRS", '=', ConsoleSystemInterface.BROWN),
		
		/*Dining Hall*/
		
		new CharAppearance("DINING_CHAIR", '/', ConsoleSystemInterface.BROWN),
		new CharAppearance("DINING_TABLE", '=', ConsoleSystemInterface.BROWN),
		new CharAppearance("MARBLE_STAIRSUP", '<', ConsoleSystemInterface.GRAY),
		new CharAppearance("MARBLE_STAIRSDOWN", '>', ConsoleSystemInterface.GRAY),
		
		/*Dungeon*/
		new CharAppearance("DUNGEON_FLOOR", '.', ConsoleSystemInterface.WHITE),
		new CharAppearance("DUNGEON_DOOR", '+', ConsoleSystemInterface.YELLOW),
		new CharAppearance("DUNGEON_WALL", '#', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("DUNGEON_PASSAGE", '.', ConsoleSystemInterface.GRAY),
		new CharAppearance("DUNGEON_DOWN", '>', ConsoleSystemInterface.YELLOW),
		new CharAppearance("DUNGEON_UP", '<', ConsoleSystemInterface.YELLOW),
		
		/*Frank Lab, goes with Dungeon Theme*/
		new CharAppearance("WIRES", '"', ConsoleSystemInterface.GRAY),
		new CharAppearance("FRANK_TABLE", 'I', ConsoleSystemInterface.RED),
		
		/*Clock Tower*/
		new CharAppearance("TOWER_FLOOR", '.', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("TOWER_WALL", '#', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("TOWER_COLUMN", 'O', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("TOWER_WINDOW", '#', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("TOWER_DOWN", '>', ConsoleSystemInterface.YELLOW),
		new CharAppearance("TOWER_UP", '<', ConsoleSystemInterface.YELLOW),
		new CharAppearance("CAMPANARIUM", '^', ConsoleSystemInterface.BROWN),
		new CharAppearance("TOWER_FLOOR_H", ':', ConsoleSystemInterface.BROWN),
		new CharAppearance("TOWER_STAIRS", '=', ConsoleSystemInterface.BROWN),
		new CharAppearance("CLOCK_GEAR_1", '>', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("CLOCK_GEAR_2", '<', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("CLOCK_GEAR_3", 'o', ConsoleSystemInterface.BROWN),
		new CharAppearance("CLOCK_GEAR_4", '-', ConsoleSystemInterface.DARK_RED),
		
		/*Castle Keep*/
		new CharAppearance("BARRED_WINDOW", '#', ConsoleSystemInterface.BLUE),
		new CharAppearance("DRACULA_THRONE1", '=', ConsoleSystemInterface.YELLOW),
		new CharAppearance("DRACULA_THRONE2", '@', ConsoleSystemInterface.RED),
		new CharAppearance("DRACULA_THRONE2_X", '^', ConsoleSystemInterface.RED),
		new CharAppearance("BALCONY", '|', ConsoleSystemInterface.GRAY),
		new CharAppearance("STONE_STAIRWAY", '<', ConsoleSystemInterface.GRAY),
		new CharAppearance("KEEP_FLOOR", '.', ConsoleSystemInterface.GRAY),
		new CharAppearance("KEEP_WALL", '#', ConsoleSystemInterface.BROWN),
		new CharAppearance("KEEP_CARPET", '=', ConsoleSystemInterface.RED),
		
		/*Void*/
		new CharAppearance("VOID_STAR", '.', ConsoleSystemInterface.BLUE),
		new CharAppearance("VOID_SUN", '*', ConsoleSystemInterface.BLUE),
		
		/*tO rEMOVE*/
		new CharAppearance("DARKTREE", '&', ConsoleSystemInterface.GREEN),
		new CharAppearance("DOOR", '/', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("COURTYARDDIRT", 'o', ConsoleSystemInterface.BROWN),
		new CharAppearance("FOUNTAINPOOL", 'o', ConsoleSystemInterface.BROWN),
		
		
		// Items
		
		// Monsters
		new CharAppearance("R_SKELETON", 's', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("GZOMBIE", 'z', ConsoleSystemInterface.LEMON),
		new CharAppearance("ZOMBIE", 'z', ConsoleSystemInterface.PURPLE),
		new CharAppearance("WHITE_SKELETON", 's', ConsoleSystemInterface.WHITE),
		
		new CharAppearance("PANTHER", 'p', ConsoleSystemInterface.GRAY),
		new CharAppearance("WARG", 'w', ConsoleSystemInterface.GRAY),
		new CharAppearance("BLACK_KNIGHT", 'k', ConsoleSystemInterface.BLUE),
		new CharAppearance("SPEAR_KNIGHT", 'k', ConsoleSystemInterface.PURPLE),
		new CharAppearance("APE_SKELETON", 'a', ConsoleSystemInterface.WHITE),
		new CharAppearance("PARANTHROPUS", 'S', ConsoleSystemInterface.WHITE),
		new CharAppearance("BAT", 'b', ConsoleSystemInterface.BLUE),
		new CharAppearance("SKULL_HEAD", 'h', ConsoleSystemInterface.GRAY),
		new CharAppearance("SKULL_LORD", 'H', ConsoleSystemInterface.GRAY),
		new CharAppearance("MERMAN", 'm', ConsoleSystemInterface.LEMON),
		new CharAppearance("WEREBEAR", 'B', ConsoleSystemInterface.BROWN),
		new CharAppearance("HUNCHBACK", 'i', ConsoleSystemInterface.GREEN),
		new CharAppearance("BONE_ARCHER", 's', ConsoleSystemInterface.GREEN),
		new CharAppearance("SKELETON_PANTHER", 'p', ConsoleSystemInterface.GRAY),
		new CharAppearance("BONE_PILLAR", 'o', ConsoleSystemInterface.GRAY),
		new CharAppearance("AXE_KNIGHT", 'k', ConsoleSystemInterface.GREEN),
		new CharAppearance("MEDUSA_HEAD", 'm', ConsoleSystemInterface.GREEN),
		new CharAppearance("DURGA", 'd', ConsoleSystemInterface.BROWN),
		new CharAppearance("SKELETON_ATHLETE", 's', ConsoleSystemInterface.BLUE),
		new CharAppearance("BLADE_SOLDIER", 'S', ConsoleSystemInterface.GRAY),
		new CharAppearance("BONE_HALBERD", 'S', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("CROW", 'c', ConsoleSystemInterface.GRAY),
		new CharAppearance("BLOOD_SKELETON", 's', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("LIZARD_SWORDSMAN", 'l', ConsoleSystemInterface.GREEN),
		new CharAppearance("COCKATRICE", 'C', ConsoleSystemInterface.WHITE),
		new CharAppearance("COOPER_ARMOR", 'A', ConsoleSystemInterface.BROWN),
		new CharAppearance("GHOUL", 'Z', ConsoleSystemInterface.PURPLE),
		new CharAppearance("SALOME", 'w', ConsoleSystemInterface.MAGENTA),
		new CharAppearance("ECTOPLASM", 'e', ConsoleSystemInterface.GREEN),
		new CharAppearance("RULER_SWORD_LV1", 'T', ConsoleSystemInterface.WHITE),
		new CharAppearance("BEAST_DEMON", 'd', ConsoleSystemInterface.BROWN),
		new CharAppearance("DEVIL", 'D', ConsoleSystemInterface.RED),
		new CharAppearance("BALLOON_POD", 'y', ConsoleSystemInterface.CYAN),
		new CharAppearance("LILITH", 'x', ConsoleSystemInterface.BLUE),
		new CharAppearance("BONE_MUSKET", 's', ConsoleSystemInterface.BROWN),
		new CharAppearance("KILLER_PLANT", 'p', ConsoleSystemInterface.GREEN),
		new CharAppearance("VAMPIRE_BAT", 'b', ConsoleSystemInterface.GRAY),
		new CharAppearance("DEATH_MANTIS", 'M', ConsoleSystemInterface.GREEN),
		new CharAppearance("DHURON", 's', ConsoleSystemInterface.TEAL),
		new CharAppearance("DRAGON_SKULL_CANNON", 'O', ConsoleSystemInterface.WHITE),
		new CharAppearance("MUMMY_MAN", 'm', ConsoleSystemInterface.GRAY),
		new CharAppearance("ZELDO", 'Z', ConsoleSystemInterface.GRAY),
		new CharAppearance("MUD_MAN", 'm', ConsoleSystemInterface.BROWN),
		new CharAppearance("CAGNAZOO", 'C', ConsoleSystemInterface.BROWN),
		new CharAppearance("ALRAUNE", 'A', ConsoleSystemInterface.LEMON),
		new CharAppearance("GOLEM", 'G', ConsoleSystemInterface.BROWN),
		new CharAppearance("ARACHNE", 'a', ConsoleSystemInterface.BROWN),
		new CharAppearance("SPEAR_SKELETON", 's', ConsoleSystemInterface.YELLOW),
		new CharAppearance("FLYING_SPEAR_SKELETON", 's', ConsoleSystemInterface.PURPLE),
		new CharAppearance("KNIFE_MERMAN", 'm', ConsoleSystemInterface.PURPLE),
		new CharAppearance("MASTER_LIZARD", 'L', ConsoleSystemInterface.GREEN),
		new CharAppearance("WHIP_SKELETON", 's', ConsoleSystemInterface.DARK_BLUE),
		new CharAppearance("FROZEN_SHADE", 'g', ConsoleSystemInterface.BLUE),
		new CharAppearance("MINOTAUR", 'M', ConsoleSystemInterface.BROWN),
		new CharAppearance("TRITON", 'P', ConsoleSystemInterface.CYAN),
		new CharAppearance("NOVA_SKELETON", 's', ConsoleSystemInterface.GREEN),
		new CharAppearance("ARMOR_LORD", 'A', ConsoleSystemInterface.GRAY),
		new CharAppearance("FLEA_ARMOR", 'I', ConsoleSystemInterface.GREEN),
		new CharAppearance("BUER", 'b', ConsoleSystemInterface.GRAY),
		new CharAppearance("WIGHT", 'Z', ConsoleSystemInterface.PURPLE),
		new CharAppearance("SPECTER", 'S', ConsoleSystemInterface.WHITE),
		new CharAppearance("RULER_SWORD_LV2", 'T', ConsoleSystemInterface.CYAN),
		new CharAppearance("CURLY", 'd', ConsoleSystemInterface.CYAN),
		new CharAppearance("FIRE_WARG", 'W', ConsoleSystemInterface.RED),
		new CharAppearance("BONE_ARK", 's', ConsoleSystemInterface.WHITE),
		new CharAppearance("MIMIC", '?', ConsoleSystemInterface.BROWN),
		new CharAppearance("MANTICORE", 'M', ConsoleSystemInterface.PURPLE),
		new CharAppearance("FLAME_KNIGHT", 'S', ConsoleSystemInterface.RED),
		new CharAppearance("ARMOR_GUARDIAN", 'A', ConsoleSystemInterface.PURPLE),
		new CharAppearance("DEMON_LORD", 'D', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("HEAT_SHADE", 'g', ConsoleSystemInterface.RED),
		new CharAppearance("FLESH_GOLEM", 'G', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("WEREWOLF", 'W', ConsoleSystemInterface.BLUE),
		new CharAppearance("ALURA_UNE", 'A', ConsoleSystemInterface.PURPLE),
		new CharAppearance("DRAHIGNAZOO", 'C', ConsoleSystemInterface.GRAY),
		new CharAppearance("SUCCUBUS", 'X', ConsoleSystemInterface.PURPLE),
		new CharAppearance("BLADE_MASTER", 'S', ConsoleSystemInterface.GREEN),
		new CharAppearance("BASILISK", 'C', ConsoleSystemInterface.GRAY),
		new CharAppearance("GARGOYLE", 'G', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("HARPY", 'H', ConsoleSystemInterface.YELLOW),
		new CharAppearance("KICKER_SKELETON", 'S', ConsoleSystemInterface.BROWN),
		new CharAppearance("BEHEMOTH", 'B', ConsoleSystemInterface.RED),
		new CharAppearance("DISCUS_LORD", 'A', ConsoleSystemInterface.MAGENTA),
		new CharAppearance("GIANT_ARMOR", 'A', ConsoleSystemInterface.LEMON),
		new CharAppearance("WITCH", 'W', ConsoleSystemInterface.GREEN),
		new CharAppearance("MANDRAGORA", 'M', ConsoleSystemInterface.GREEN),
		new CharAppearance("IRON_GOLEM", 'G', ConsoleSystemInterface.GRAY),
		new CharAppearance("VICTORY_ARMOR", 'A', ConsoleSystemInterface.GREEN),
		new CharAppearance("RULER_SWORD_LV3", 'T', ConsoleSystemInterface.TEAL),

		new CharAppearance("GIANTBAT", 'B', ConsoleSystemInterface.GRAY),
		new CharAppearance("AKMODAN", 'M', ConsoleSystemInterface.YELLOW),
		new CharAppearance("DRAGON_KING", 'D', ConsoleSystemInterface.WHITE),
		new CharAppearance("DEATH", 'G', ConsoleSystemInterface.GRAY),
		new CharAppearance("SICKLE", 'x', ConsoleSystemInterface.GRAY),
		new CharAppearance("DRACULA", '@', ConsoleSystemInterface.RED),
		new CharAppearance("MEDUSA", 'M', ConsoleSystemInterface.GREEN),
		new CharAppearance("SNAKE", 's', ConsoleSystemInterface.LEMON),
		new CharAppearance("FRANK", 'F', ConsoleSystemInterface.GRAY),
		new CharAppearance("IGOR", 'i', ConsoleSystemInterface.GRAY),
		new CharAppearance("DEMON_DRACULA", 'D', ConsoleSystemInterface.TEAL),
		new CharAppearance("ORLOX", '@', ConsoleSystemInterface.TEAL),
		new CharAppearance("WATER_DRAGON", 'D', ConsoleSystemInterface.CYAN),
		new CharAppearance("LEGION", 'L', ConsoleSystemInterface.PURPLE),
		new CharAppearance("CERBERUS", 'C', ConsoleSystemInterface.PURPLE),
		new CharAppearance("DOPPELGANGER", '@', ConsoleSystemInterface.CYAN),

		new CharAppearance("S_CAT", 'c', ConsoleSystemInterface.TEAL),
		new CharAppearance("S_BIRD", 'b', ConsoleSystemInterface.TEAL),
		new CharAppearance("S_TURTLE", 't', ConsoleSystemInterface.TEAL),
		new CharAppearance("S_TIGER", 'C', ConsoleSystemInterface.TEAL),
		new CharAppearance("S_EAGLE", 'B', ConsoleSystemInterface.TEAL),
		new CharAppearance("S_TORTOISE", 'T', ConsoleSystemInterface.TEAL),
		new CharAppearance("S_DRAGON", 'D', ConsoleSystemInterface.TEAL),

		// Features
		new CharAppearance("COFFIN", '+', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("CANDLE", 'i', ConsoleSystemInterface.YELLOW),
		new CharAppearance("UPGRADE", '*', ConsoleSystemInterface.RED),
		new CharAppearance("MUPGRADE", '*', ConsoleSystemInterface.BLUE),
		new CharAppearance("FLAME", '%', ConsoleSystemInterface.YELLOW),
		new CharAppearance("URN_FLAME", '%', ConsoleSystemInterface.RED),
		new CharAppearance("FLAME_ITEM", 'i', ConsoleSystemInterface.YELLOW),
		new CharAppearance("BLAST_CRYSTAL", 'O', ConsoleSystemInterface.CYAN),

		//Characters
		new CharAppearance("RENEGADE", '@', ConsoleSystemInterface.GRAY),
		new CharAppearance("VANQUISHER", '@', ConsoleSystemInterface.GREEN),
		new CharAppearance("INVOKER", '@', ConsoleSystemInterface.RED),
		new CharAppearance("MANBEAST", '@', ConsoleSystemInterface.BROWN),
		new CharAppearance("KNIGHT", '@', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("BEAST", 'B', ConsoleSystemInterface.GRAY),

		new CharAppearance("RENEGADE_W", '@', ConsoleSystemInterface.GRAY),
		new CharAppearance("VANQUISHER_W", '@', ConsoleSystemInterface.GREEN),
		new CharAppearance("INVOKER_W", '@', ConsoleSystemInterface.RED),
		new CharAppearance("MANBEAST_W", '@', ConsoleSystemInterface.BROWN),
		new CharAppearance("KNIGHT_W", '@', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("BEAST_W", 'B', ConsoleSystemInterface.GRAY),

		new CharAppearance("MORPHED_WOLF", 'w', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("MORPHED_MYST", 'm', ConsoleSystemInterface.GRAY),
		new CharAppearance("MORPHED_BAT", 'b', ConsoleSystemInterface.GRAY),
		new CharAppearance("MORPHED_WOLF2", 'w', ConsoleSystemInterface.GRAY),
		new CharAppearance("MORPHED_MYST2", 'm', ConsoleSystemInterface.CYAN),
		new CharAppearance("MORPHED_BAT2", 'b', ConsoleSystemInterface.WHITE),

		new CharAppearance("MORPHED_LUPINE", 'h', ConsoleSystemInterface.WHITE),
		new CharAppearance("MORPHED_WEREBEAR", 'B', ConsoleSystemInterface.BROWN),
		new CharAppearance("MORPHED_WEREBEAST", 'B', ConsoleSystemInterface.WHITE),
		new CharAppearance("MORPHED_WEREDEMON", 'd', ConsoleSystemInterface.RED),
		new CharAppearance("MORPHED_WEREWOLF", 'W', ConsoleSystemInterface.GRAY),

		new CharAppearance("MAN", 't', ConsoleSystemInterface.RED),
		new CharAppearance("WOMAN", 't', ConsoleSystemInterface.BLUE),
		new CharAppearance("OLDMAN", 't', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("OLDWOMAN", 't', ConsoleSystemInterface.DARK_BLUE),
		new CharAppearance("PRIEST", '@', ConsoleSystemInterface.CYAN),
		new CharAppearance("DOG", 'd', ConsoleSystemInterface.BROWN),
		new CharAppearance("HOSTAGE_GIRL", '@', ConsoleSystemInterface.MAGENTA),
		new CharAppearance("HOSTAGE_GUY", '@', ConsoleSystemInterface.BLUE),

		new CharAppearance("MELDUCK", '@', ConsoleSystemInterface.TEAL),

		//Items
		new CharAppearance("ART_CARD_SOL", 'C', ConsoleSystemInterface.YELLOW),
		new CharAppearance("ART_CARD_MOONS", 'C', ConsoleSystemInterface.BLUE),
		new CharAppearance("ART_CARD_DEATH", 'C', ConsoleSystemInterface.GRAY),
		new CharAppearance("ART_CARD_LOVE", 'C', ConsoleSystemInterface.RED),
		new CharAppearance("OXY_HERB", '%', ConsoleSystemInterface.CYAN),
		new CharAppearance("JUKEBOX", 'J', ConsoleSystemInterface.BROWN),

		new CharAppearance("RED_CARD", '?', ConsoleSystemInterface.RED),
		new CharAppearance("GOLDEN_MEDALLION", '^', ConsoleSystemInterface.YELLOW),
		new CharAppearance("SILVER_MEDALLION", '^', ConsoleSystemInterface.GRAY),
		new CharAppearance("THORN_BRACELET", '^', ConsoleSystemInterface.GREEN),
		new CharAppearance("LIFE_POTION", '!', ConsoleSystemInterface.RED),
		new CharAppearance("FLAME_BOOK", '?', ConsoleSystemInterface.RED),
		new CharAppearance("ICE_BOOK", '?', ConsoleSystemInterface.CYAN),
		new CharAppearance("LIT_BOOK", '?', ConsoleSystemInterface.BLUE),
		new CharAppearance("HEART_CONTAINER", 'V', ConsoleSystemInterface.WHITE),
		new CharAppearance("MIRACLE_POTION", '!', ConsoleSystemInterface.BLUE),
		new CharAppearance("TEPES_RING", '*', ConsoleSystemInterface.BLUE),
		new CharAppearance("CLUE_PAGE2", '?', ConsoleSystemInterface.WHITE),
		new CharAppearance("CLUE_PAGE3", '?', ConsoleSystemInterface.WHITE),
		new CharAppearance("CLUE_PAGE1", '?', ConsoleSystemInterface.WHITE),
		new CharAppearance("JUMPING_WING", '^', ConsoleSystemInterface.WHITE),
		new CharAppearance("FIRE_GEM", '*', ConsoleSystemInterface.RED),
		new CharAppearance("FLAME_ITEM", 'i', ConsoleSystemInterface.RED),
		new CharAppearance("MAGIC_SHIELD", ']', ConsoleSystemInterface.RED),
		new CharAppearance("LIGHT_CRYSTAL", '*', ConsoleSystemInterface.CYAN),
		new CharAppearance("LANTERN", '^', ConsoleSystemInterface.YELLOW),
		new CharAppearance("SOUL_RECALL", '*', ConsoleSystemInterface.BLUE),
		new CharAppearance("SUN_CARD", '#', ConsoleSystemInterface.YELLOW),
		new CharAppearance("MOON_CARD", '#', ConsoleSystemInterface.DARK_BLUE),
		new CharAppearance("HEAL_POTION", '!', ConsoleSystemInterface.DARK_RED),
		new CharAppearance("HEAL_HERB", '%', ConsoleSystemInterface.GREEN),
		new CharAppearance("BIBUTI", '#', ConsoleSystemInterface.WHITE),
		new CharAppearance("GARLIC", '%', ConsoleSystemInterface.WHITE),
		new CharAppearance("TORCH", 'i', ConsoleSystemInterface.BROWN),
		new CharAppearance("SILK_BAG", '&', ConsoleSystemInterface.BLUE),
		new CharAppearance("LAUREL", '%', ConsoleSystemInterface.LEMON),
		new CharAppearance("VARMOR", '{', ConsoleSystemInterface.BROWN),
		new CharAppearance("VEST", '{', ConsoleSystemInterface.BROWN),
		new CharAppearance("STUDDED_LEATHER", '{', ConsoleSystemInterface.BROWN),
		new CharAppearance("LEATHER_ARMOR", '{', ConsoleSystemInterface.BROWN),
		new CharAppearance("CLOTH_TUNIC", '{', ConsoleSystemInterface.WHITE),
		new CharAppearance("FINE_GARMENT", '{', ConsoleSystemInterface.GRAY),
		new CharAppearance("CUIRASS", '{', ConsoleSystemInterface.GRAY),
		new CharAppearance("SUIT", '{', ConsoleSystemInterface.WHITE),
		new CharAppearance("PLATE", '{', ConsoleSystemInterface.WHITE),
		new CharAppearance("DIAMOND_PLATE", '{', ConsoleSystemInterface.WHITE),
		new CharAppearance("BOW", ')', ConsoleSystemInterface.BROWN),
		new CharAppearance("HOLBEIN_DAGGER", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("WEREBANE", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("SHOTEL", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("COMBAT_KNIFE", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("STAKE", '/', ConsoleSystemInterface.BROWN),
		new CharAppearance("BASELARD", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("KAISER_KNUCKLE", '-', ConsoleSystemInterface.BROWN),
		new CharAppearance("MARTIAL_ARMBAND", '-', ConsoleSystemInterface.WHITE),
		new CharAppearance("TULKAS_FIST", '-', ConsoleSystemInterface.BROWN),
		new CharAppearance("SPIKY_KNUCKLES", '-', ConsoleSystemInterface.GRAY),
		new CharAppearance("COMBAT_GAUNTLET", '-', ConsoleSystemInterface.GRAY),
		new CharAppearance("KNUCKLES", '-', ConsoleSystemInterface.BROWN),
		new CharAppearance("GAUNTLET", '-', ConsoleSystemInterface.GRAY),
		new CharAppearance("HAMMER_JUSTICE", '|', ConsoleSystemInterface.GRAY),
		new CharAppearance("MORNING_STAR", '|', ConsoleSystemInterface.GRAY),
		new CharAppearance("FLAIL", '|', ConsoleSystemInterface.GRAY),
		new CharAppearance("MACE", '|', ConsoleSystemInterface.GRAY),
		new CharAppearance("SILVER_HANDGUN", '}', ConsoleSystemInterface.GRAY),
		new CharAppearance("REVOLVER", '}', ConsoleSystemInterface.GRAY),
		new CharAppearance("HANDGUN", '}', ConsoleSystemInterface.GRAY),
		new CharAppearance("AGUEN", '^', ConsoleSystemInterface.CYAN),
		new CharAppearance("CROSSBOW", '}', ConsoleSystemInterface.BROWN),
		new CharAppearance("ROD", '|', ConsoleSystemInterface.GRAY),
		new CharAppearance("STAFF", '|', ConsoleSystemInterface.WHITE),
		new CharAppearance("BLADE_RINGSET", '&', ConsoleSystemInterface.GRAY),
		new CharAppearance("COMBAT_RINGS", '&', ConsoleSystemInterface.GRAY),
		new CharAppearance("SPIKED_RINGS", '&', ConsoleSystemInterface.GRAY),
		new CharAppearance("RINGS", '&', ConsoleSystemInterface.GRAY),
		new CharAppearance("TOWER_SHIELD", ']', ConsoleSystemInterface.RED),
		new CharAppearance("BUCKLER", ']', ConsoleSystemInterface.GRAY),
		new CharAppearance("WOODEN_SHIELD", ']', ConsoleSystemInterface.BROWN),
		new CharAppearance("ROUND_SHIELD", ']', ConsoleSystemInterface.WHITE),
		new CharAppearance("SHIELD", ']', ConsoleSystemInterface.GRAY),
		new CharAppearance("DUALBLADE_SPEAR", '\\', ConsoleSystemInterface.GRAY),
		new CharAppearance("HALBERD", '\\', ConsoleSystemInterface.GRAY),
		new CharAppearance("ALCARDE_SPEAR", '\\', ConsoleSystemInterface.RED),
		new CharAppearance("BATTLE_SPEAR", '\\', ConsoleSystemInterface.WHITE),
		new CharAppearance("LONG_SPEAR", '\\', ConsoleSystemInterface.GRAY),
		new CharAppearance("SHORT_SPEAR", '\\', ConsoleSystemInterface.GRAY),
		new CharAppearance("MASAMUNE", '/', ConsoleSystemInterface.BLUE),
		new CharAppearance("CRISSAEGRIM", '/', ConsoleSystemInterface.YELLOW),
		new CharAppearance("TERMINUS", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("MOURNEBLADE", '/', ConsoleSystemInterface.CYAN),
		new CharAppearance("OSAFUNE", '/', ConsoleSystemInterface.WHITE),
		new CharAppearance("MORMEGIL", '/', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("GRAM", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("RAPIER", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("BASTARDSWORD", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("BROADSWORD", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("VORPAL_BLADE", '/', ConsoleSystemInterface.RED),
		new CharAppearance("FIREBRAND", '/', ConsoleSystemInterface.RED),
		new CharAppearance("ICEBRAND", '/', ConsoleSystemInterface.CYAN),
		new CharAppearance("GURTHANG", '/', ConsoleSystemInterface.GREEN),
		new CharAppearance("KATANA", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("FALCHION", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("HARPER", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("HADOR", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("GLADIUS", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("CUTLASS", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("CLAYMORE", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("ETHANOS_BLADE", '/', ConsoleSystemInterface.RED),
		new CharAppearance("FLAMBERGE", '/', ConsoleSystemInterface.RED),
		new CharAppearance("SABRE", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("MABLUNG", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("SCIMITAR", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("ESTOC", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("SHORT_SWORD", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("BWAKA_KNIFE", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("CHAKRAM", '&', ConsoleSystemInterface.GRAY),
		new CharAppearance("BUFFALO_STAR", 'x', ConsoleSystemInterface.GRAY),
		new CharAppearance("SHURIKEN", 'x', ConsoleSystemInterface.GRAY),
		new CharAppearance("THROWING_KNIFE", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("LIT_WHIP", '@', ConsoleSystemInterface.YELLOW),
		new CharAppearance("FLAME_WHIP", '@', ConsoleSystemInterface.RED),
		new CharAppearance("VKILLERW", '@', ConsoleSystemInterface.GRAY),
		new CharAppearance("WHIP", '@', ConsoleSystemInterface.GRAY),
		new CharAppearance("CHAIN_WHIP", '@', ConsoleSystemInterface.LIGHT_GRAY),
		new CharAppearance("THORN_WHIP", '@', ConsoleSystemInterface.LEMON),
		new CharAppearance("LEATHER_WHIP", '@', ConsoleSystemInterface.GRAY),

		// Features
		new CharAppearance("CANDLE", 'i', ConsoleSystemInterface.YELLOW),
		new CharAppearance("SMALLHEART", 'v', ConsoleSystemInterface.RED),
		new CharAppearance("DAGGER", '/', ConsoleSystemInterface.GRAY),
		new CharAppearance("AXE", '\\', ConsoleSystemInterface.GRAY),
		new CharAppearance("VIAL", '!', ConsoleSystemInterface.CYAN),
		new CharAppearance("CROSS", '+', ConsoleSystemInterface.CYAN),
		new CharAppearance("CLOCK", '&', ConsoleSystemInterface.WHITE),
		new CharAppearance("BIGHEART", 'V', ConsoleSystemInterface.RED),
		new CharAppearance("KEY", 'k', ConsoleSystemInterface.YELLOW),
		new CharAppearance("UPGRADE", '*', ConsoleSystemInterface.RED),
		new CharAppearance("ROSARY", '+', ConsoleSystemInterface.BLUE),
		new CharAppearance("COIN", '*', ConsoleSystemInterface.YELLOW),
		new CharAppearance("RED_MONEY_BAG", '%', ConsoleSystemInterface.RED),
		new CharAppearance("BLUE_MONEY_BAG", '%', ConsoleSystemInterface.BLUE),
		new CharAppearance("WHITE_MONEY_BAG", '%', ConsoleSystemInterface.WHITE),
		new CharAppearance("CROWN", '%', ConsoleSystemInterface.YELLOW),
		new CharAppearance("CHEST", '%', ConsoleSystemInterface.BROWN),
		new CharAppearance("MOAUI_HEAD", '@', ConsoleSystemInterface.YELLOW),
		new CharAppearance("RAINBOW_MONEY_BAG", '%', ConsoleSystemInterface.PURPLE),
		new CharAppearance("POT_ROAST", '%', ConsoleSystemInterface.BROWN),
		new CharAppearance("INVISIBILITY_POTION", '!', ConsoleSystemInterface.YELLOW),
		new CharAppearance("BIBLE", '?', ConsoleSystemInterface.WHITE),
		new CharAppearance("CRYSTAL", '*', ConsoleSystemInterface.CYAN),
		new CharAppearance("FIST", '}', ConsoleSystemInterface.CYAN),
		new CharAppearance("REBOUND_CRYSTAL", '*', ConsoleSystemInterface.CYAN),
		new CharAppearance("MUPGRADE", '*', ConsoleSystemInterface.BLUE),
		new CharAppearance("URN_FLAME", '^', ConsoleSystemInterface.RED),
		new CharAppearance("BLAST_CRYSTAL", '*', ConsoleSystemInterface.CYAN),

		new CharAppearance("FLAME", '^', ConsoleSystemInterface.RED),
		new CharAppearance("MOUND", '^', ConsoleSystemInterface.BROWN),
	};

}