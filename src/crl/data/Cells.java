package crl.data;

import java.util.HashMap;

import crl.level.Cell;
import crl.ui.Appearance;

public class Cells {
	public static Cell[] getCellDefinitions(HashMap<String, Appearance> aps){

		Cell[] ret = new Cell[179];
		
		ret[149] = new Cell("AIR", "nothing", "Nothing", aps.get("NOTHING"));
		ret[149].setEthereal(true);
		
		
		ret [153] = new Cell("DINING_CHAIR", "chair", "Dining chair", aps.get("DINING_CHAIR"), true, false);
		ret [154] = new Cell("DINING_TABLE", "table", "Dining table", aps.get("DINING_TABLE"), true, false);
		ret [155] = new Cell("MARBLE_STAIRSUP", "stairs", "Stairs", aps.get("MARBLE_STAIRSUP"));
		ret [155].setHeightMod(-1);
		ret [156] = new Cell("MARBLE_STAIRSDOWN", "stairs", "Stairs", aps.get("MARBLE_STAIRSDOWN"));
		ret [156].setHeightMod(1);
		ret [161] = new Cell("MARBLE_STAIRSUP_FAKE", "stairs", "Stairs", aps.get("MARBLE_STAIRSUP"));
		ret [162] = new Cell("MARBLE_STAIRSDOWN_FAKE", "stairs", "Stairs", aps.get("MARBLE_STAIRSDOWN"));
		
		/*Town*/
		ret [1] = new Cell("DIRT", "dirt", "A patch of dirt", aps.get("TOWN_GRASS"));
		ret [8] = new Cell("DARK_FOREST", "moss tree", "A moss tree", aps.get("TOWN_GRASS"), true, true);
		ret [16] = new Cell("STREAM", "stream of water", "A stream of water", aps.get("TOWN_GRASS"));
		ret [16].setWater(true);
		ret [30] = new Cell("VOID", "", "", aps.get("VOID"), true, true);
		ret [165] = new Cell("STATIC_VOID", "", "", aps.get("VOID"));

		ret [133] = new Cell("SIGNPOST_T1", "signpost", "Lesson 1: Items\n\n"+
			"Pick up herbs by moving onto them and pressing 'g'. Move to the house and drop them using 'D'.\n\n"+
			"During your journeys, you will find items lying on the ground. You must pick them up before using "+
			"or wearing them.", aps.get("SIGN_POST"), true, false);
		ret [177] = new Cell("SIGNPOST_T2", "signpost", "Lesson 2: Jumping\n\n"+
			"Jump the water by pressing 'j' then a direction.\n\n"+
			"Jumping is useful in combat to gain tactical advantage, and to pass over gaps in the terrain.", aps.get("SIGN_POST"), true, false);

		ret [132] = new Cell("SIGNPOST_T3", "signpost", "Lesson 3: Attacking\n\n"+
			"Move next to a tree and press [PERIOD], then the direction to attack the tree.\n\n"+
			"The world is full of monsters eager to kill you. This is the main way of attacking. Depending on your " +
			"current weapon, you may hit enemies which are not adjacent to you.", aps.get("SIGN_POST"), true, false);
		
		ret [135] = new Cell("SIGNPOST_T4", "signpost", "Lesson 4: Ranged Weapons\n\n"+
			"Pick up this bow, and equip it (with 'E').\n\n"+
			"Press 'f' to begin firing: use the arrow keys to aim at a tree, and press 'f' again to fire.\n\n"+
			"Ranged weapons let you attack from a distance, but must be reloaded (costing hearts and gold).", aps.get("SIGN_POST"), true, false);

		ret [134] = new Cell("SIGNPOST_T5", "signpost", "Lesson 5: Swimming\n\n"+
			"In the nearby lake, use 'P' to plunge underwater. "+
			"Swim north to the far side, and use 'j' to re-surface.\n\n"+
			"Diving is a dangerous! You'll die as soon as your oxygen runs out."+
			"\n\nBe sure to resurface to replenish your oxygen.", aps.get("SIGN_POST"), true, false);
		// Why throw herbs onto dirt tiles? Does it do anything?
		// if it doesn't do anything, why 'tutorial' it?
		ret [136] = new Cell("SIGNPOST_T6", "signpost", "Lesson 6: Throwing\n\n"+
			"Pick up the herbs and throw them onto the dirt. Press 't', select"+
			" the herb, aim at a landing spot and then press 'space'.\n\n"+
			"Some items explode or have special effects when thrown", aps.get("SIGN_POST"), true, false);
		
		ret [137] = new Cell("SIGNPOST_T7", "signpost", "Lesson 7: Mystic Weapons\n\n"+
			"This is a mystic weapon. Only Vampire Killers can use it. "+
			"Step on it and use 'space' to aim it like a ranged weapon. Press "+
			"'space' again to shoot.\n\nMystic weapons cost hearts for "+
			"each shot.", aps.get("SIGN_POST"), true, false);
		
		ret [178] = new Cell("SIGNPOST_T8", "signpost", "Lesson 8: Monsters!\n\n"+
			"You're now ready to fight! With your mystic weapon you should have no problem dispatching these weak foes.\n\n"+
			"You can evade the skeleton bones by moving out of their firing range. They always target your previous location!", aps.get("SIGN_POST"), true, false);

		ret [139] = new Cell("SIGNPOST_T9", "signpost", "You can access your skills using 'p' " +
			"and check your inventory using 'i'. Press '?' for complete reference. \n \n You can access most of the item manipulation commands from the Inventory screen!\n \n Walk through here to leave this area and end your training \n \n CastleVania awaits!", aps.get("SIGN_POST"), true, false);
		
		
		/*Town*/
		ret [31] = new Cell("TOWN_GRASS", "grass", "Darkened grass", aps.get("TOWN_GRASS"));
		ret [32] = new Cell("TOWN_WALL", "wall", "Wall from somebody house", aps.get("TOWN_WALL"), true, true);
		ret [33] = new Cell("HOUSE_FLOOR", "floor", "A common house floor", aps.get("HOUSE_FLOOR"));
		ret [34] = new Cell("MIDWALKWAY", "brick Walkway (high)", "Walkway made of old bricks", aps.get("MIDWALKWAY"));
		ret [35] = new Cell("TOWN_CHURCH_FLOOR", "floor", "A beautifully mantained floor", aps.get("TOWN_CHURCH_FLOOR"));
		ret [28] = new Cell("TOWN_WATERWAY", "water", "Stream of water", aps.get("TOWN_WATERWAY"));ret[28].setShallowWater(true);
		ret [144] = new Cell("TOWN_WATERWAY_UP", "water", "Stream of water", aps.get("TOWN_WATERWAY"));ret[144].setWater(true);ret[144].setHeightMod(-1);
		ret [145] = new Cell("TOWN_WATERWAY_DOWN", "water", "Stream of water", aps.get("TOWN_WATERWAY"));ret[145].setShallowWater(true);ret[145].setHeightMod(1);
		ret [146] = new Cell("TOWN_SEWER", "water", "Stream of water", aps.get("TOWN_WATERWAY"));ret[146].setWater(true);
		ret [150] = new Cell("TOWN_WATER", "water", "Stream of water", aps.get("TOWN_WATERWAY"));ret[150].setWater(true);
		ret [2] = new Cell("BRICK_WALKWAY", "walkway", "Walkway made of old damaged bricks", aps.get("BRICKWALKWAY"));
		ret [111] = new Cell("TOWN_TREE", "tree", "Dark mossy tree", aps.get("TOWN_TREE"), true, true);
		ret [59] = new Cell("BRICK_WALKWAY2", "walkway", "Walkway made of old damaged bricks", aps.get("BRICKWALKWAY2"));
		ret [60] = new Cell("TOWN_ROOF", "roof", "House roof", aps.get("TOWN_ROOF"));
		ret [57] = new Cell("TOWN_STAIRSUP", "stairs", "Stairs", aps.get("TOWN_STAIRSUP")); ret[57].setHeightMod(-1); ret[57].setHeight(1);
		ret [58] = new Cell("TOWN_STAIRSDOWN", "stairs", "Stairs", aps.get("TOWN_STAIRSDOWN")); ret[58].setHeightMod(1);/*ret[58].setHeight(1);*/
		ret [118] = new Cell("TOWN_STAIRS", "stairs", "Stairs", aps.get("TOWN_STAIRS"));ret[118].setHeight(1);ret[118].setIsStair(true);
		ret [127] = new Cell("TOWN_DOOR", "wooden door", "Wooden Door", aps.get("TOWN_DOOR"), false, true);
		ret [128] = new Cell("TOWN_DOOR_H", "wooden door", "Wooden Door", aps.get("TOWN_DOOR"), false, true); ret [128].setHeight(3);
		
		/*Dark Forest*/
		ret [105] = new Cell("FOREST_TREE", "tree", "Dark mossy tree", aps.get("FOREST_TREE"), true, true);
		ret [106] = new Cell("FOREST_GRASS", "grass", "Humid grass", aps.get("FOREST_GRASS"));
		ret [107] = new Cell("FOREST_DIRT", "dirt patch", "Wet dirt patch", aps.get("FOREST_DIRT"));
		ret [108] = new Cell("WRECKED_CHARRIOT", "wrecked charriot", "Destroyed charriot", aps.get("WRECKED_CHARRIOT"));
		ret [109] = new Cell("WRECKED_WHEEL", "wrecked wheel", "Wrecked Wheel", aps.get("WRECKED_WHEEL"));
		ret [110] = new Cell("SIGNPOST", "signpost", "AHEAD: \"Left to Petra Town, Right to Dracula's Castle\"", aps.get("SIGN_POST"), true, false);
		ret [173] = new Cell("FOREST_TREE_1", "tree", "Dark mossy tree", aps.get("FOREST_TREE_1"), false, false);
		ret [174] = new Cell("FOREST_TREE_2", "tree", "Dark mossy tree", aps.get("FOREST_TREE_2"), false, false);
		
		/*Bridge*/
		ret [62] = new Cell("WOODEN_BRIDGE", "wooden bridge", "Wooden bridgeway", aps.get("WOODEN_BRIDGE")); ret [62].setHeight(4);
		ret [63] = new Cell("DARK_LAKE_ETH", "dark lake", "Dark Lake", aps.get("DARK_LAKE"));ret[63].setShallowWater(true); 
		ret [147] = new Cell("DARK_LAKE", "dark lake", "Dark Lake", aps.get("DARK_LAKE"));ret[147].setWater(true);
		ret [148] = new Cell("LAKE_STAIRSUP", "stairs up", "Stairs Up", aps.get("WOODEN_BRIDGE"));ret[148].setWater(true);ret[148].setHeightMod(-1);
		ret [175] = new Cell("DARK_LAKE_ETH_STAIR", "dark lake", "Dark Lake", aps.get("DARK_LAKE"));ret[175].setShallowWater(true);ret [175].setHeight(2);
		ret [176] = new Cell("STONE_BLOCK", "stone block", "A massive stone block", aps.get("STONE_BLOCK"), true, true);
		
		
		ret [112] = new Cell("BRIDGE_WALKWAY", "walkway", "Walkway made of old damaged bricks", aps.get("BRIDGE_WALKWAY")); ret [112].setHeight(4);
		ret [113] = new Cell("BRIDGE_COLUMN", "granite column", "A weary granite column", aps.get("BRIDGE_COLUMN"), true, true);
		
		/*Castle Garden*/
		ret [0] = new Cell("GARDEN_GRASS", "grass", "Somewhat grown grass", aps.get("GARDEN_GRASS"));
		ret [140] = new Cell("GARDEN_GRASS_ORNAMENTAL", "grass", "Somewhat grown grass", aps.get("GARDEN_GRASS"), true, false);
		ret [114] = new Cell("GARDEN_WALKWAY", "walkway", "Walkway made of old damaged bricks", aps.get("GARDEN_WALKWAY"));
		ret [9] = new Cell("DEAD_STUMP", "dead stump", "A dead stump", aps.get("DEAD_STUMP"), true, false);
		ret [11] = new Cell("GARDEN_FENCE", "wooden fence", "A wooden fence", aps.get("GARDEN_FENCE"), true, false);		
		ret [13] = new Cell("GARDEN_FOUNTAIN_CENTER", "water fountain", "Source of water", aps.get("GARDEN_FOUNTAIN_CENTER"));
		ret [14] = new Cell("GARDEN_FOUNTAIN_AROUND", "water fountain", "A fountain of water",aps.get("GARDEN_FOUNTAIN_AROUND"));
		ret [15] = new Cell("GARDEN_FOUNTAIN_POOL", "fountain wall", "A fountain of water",aps.get("GARDEN_FOUNTAIN_POOL"));ret[15].setShallowWater(true);
		ret [3] = new Cell("GARDEN_DOOR", "castle door", "The doors of Dracula castle", aps.get("GARDEN_DOOR"), true, false);
		ret [4] = new Cell("GARDEN_WALL", "stone wall", "Beaten stone wall", aps.get("GARDEN_WALL"), true, true);
		ret [5] = new Cell("CASTLE_WALL", "castle wall", "Imponent wall of the castle", aps.get("CASTLE_WALL"), true, true);
		ret [6] = new Cell("GARGOYLE_STATUE", "gargoyle statue", "A Gargoyle statue, or a sleeping monster", aps.get("GARGOYLESTATUE"), true, true);
		ret [7] = new Cell("HUMAN_STATUE", "human statue", "A Sculpture of an human", aps.get("HUMANSTATUE"), true, true);
		ret [10] = new Cell("GARDEN_TORCH", "torch", "A torch", aps.get("GARDEN_TORCH"));
		ret [12] = new Cell("CASTLE_DOOR", "castle door", "The castle doors", aps.get("CASTLE_DOOR"), true, false);
		ret [138] = new Cell("GARDEN_TREE", "tree", "A Tree", aps.get("GARDEN_TREE"), true, true);

		/*Marble Hall*/
		ret [29] = new Cell("MARBLE_COLUMN", "stone pillar", "Stone pillar", aps.get("MARBLE_COLUMN"), true,true);
		ret [18] = new Cell("MARBLE_FLOOR", "marble floor", "Marble Floor", aps.get("MARBLE_FLOOR"));
		ret [19] = new Cell("MARBLE_STAIRS", "stairs", "Stairs", aps.get("MARBLE_STAIRS"));ret[19].setHeight(1);ret[19].setIsStair(true);
		ret [20] = new Cell("MARBLE_MIDFLOOR", "marble floor", "Marble Floor", aps.get("MARBLE_MIDFLOOR"));
		/**/ret [21] = new Cell("MAGIC_DOOR", "magic door", "A Magic door", aps.get("VOID"));
		ret [22] = new Cell("BIG_WINDOW", "tall window", "A tall addorned window", aps.get("BIG_WINDOW"), true, false);
		ret [23] = new Cell("RED_CURTAIN", "red curtain", "A badly worn red curtain", aps.get("RED_CURTAIN"));
		ret [24] = new Cell("GODNESS_STATUE", "statue of a goddess", "The statue of a goddess", aps.get("GODNESS_STATUE"), true, true);
		ret [25] = new Cell("RED_CARPET", "red carpet", "Damaged red carpet", aps.get("RED_CARPET"));
		
		ret [157] = new Cell("QUARTERS_WALL", "stucco wall", "Imponent wall of the castle", aps.get("QUARTERS_WALL"), true, true);
		ret [158] = new Cell("QUARTERS_FLOOR", "tiled floor", "Tiled Floor", aps.get("QUARTERS_FLOOR"));
		ret [159] = new Cell("QUARTERS_CORRIDOR", "tiled floor", "Tiled Floor", aps.get("QUARTERS_FLOOR"),false,false);
		ret [160] = new Cell("QUARTERS_DOOR", "tiled floor", "Tiled Floor", aps.get("QUARTERS_FLOOR"));
		
		/*Moat*/
		ret [17] = new Cell("GRAY_COLUMN", "granite column", "A weary granite column", aps.get("GRAY_COLUMN"), true, true);
		ret [61] = new Cell("MOSS_COLUMN", "moss column", "Mossy stone column", aps.get("MOSS_COLUMN"), true, true);
		ret [26] = new Cell("MOSS_FLOOR", "moss stone", "Mossy stone floor", aps.get("MOSS_FLOOR"));
		ret [27] = new Cell("RUSTY_PLATFORM", "rusty platform", "Rusty iron platform", aps.get("RUSTY_PLATFORM"));
		ret [117] = new Cell("MOSS_WATERWAY_ETH", "water", "Stream of putrid water", aps.get("MOSS_WATERWAY_ETH"));ret[117].setShallowWater(true);ret[117].setHeight(-3);
		ret [141] = new Cell("MOSS_WATERWAY", "water", "Stream of putrid water", aps.get("MOSS_WATERWAY"));ret[141].setWater(true);
		
		ret [142] = new Cell("MOSS_STAIRS", "mossy stairs", "Mossy Stairs", aps.get("MOSS_STAIRS"));ret[142].setHeight(1);ret[142].setIsStair(true);
		ret [143] = new Cell("MOSS_MIDFLOOR", "mossy floor", "Mossy Floor", aps.get("MOSS_MIDFLOOR"));ret[143].setHeight(3);
		
		
		/*Lab*/
		ret [47] = new Cell("RED_FLOOR", "brick floor", "Damaged brick floor", aps.get("RED_FLOOR"));
		ret [48] = new Cell("RED_WALL", "brick wall", "Decrepit brick wall", aps.get("RED_WALL"), true, true);
		ret [52] = new Cell("SMALL_WINDOW", "small window", "A cross shaped window made inside the wall", aps.get("SMALL_WINDOW"), true, false);
		
		/*Ruins*/
		ret [38] = new Cell("STONE_FLOOR", "stone floor", "Stone floor", aps.get("STONE_FLOOR"));
		ret [39] = new Cell("STONE_WALL", "stone wall", "Stone wall", aps.get("STONE_WALL"), true, true);
		
		/*Sewers*/
		ret [163] = new Cell("SEWERS_FLOOR", "mossy floor", "Mossy floor", aps.get("SEWERS_FLOOR")); ret [163].setShallowWater(true);
		ret [164] = new Cell("SEWERS_WALL", "mossy wall", "Mossy wall", aps.get("SEWERS_WALL"), true, true);
		ret [166] = new Cell("SEWERS_DOWN", "hole down", "Hole going deeper", aps.get("SEWERS_DOWN")); ret [166].setShallowWater(true); 
		ret [167] = new Cell("SEWERS_UP", "hole up", "Hole on the ceiling", aps.get("SEWERS_UP")); ret [167].setShallowWater(true);
		ret [168] = new Cell("SEWERS_FLOOR_WATER", "mossy floor", "Mossy floor", aps.get("SEWERS_FLOOR")); ret [168].setWater(true);
		ret [169] = new Cell("SEWERS_WALL_WATER", "mossy wall", "Mossy wall", aps.get("SEWERS_WALL"), true, true); 
		ret [170] = new Cell("SEWERS_DOWN_WATER", "hole down", "Hole going deeper", aps.get("SEWERS_DOWN"));  ret [170].setWater(true);
		ret [171] = new Cell("SEWERS_UP_WATER", "hole up", "Hole on the ceiling", aps.get("SEWERS_UP")); ret [171].setWater(true);
		
		ret [172] = new Cell("WEIRD_MACHINE", "weird machine", "Weird Machine", aps.get("WEIRD_MACHINE")); 
		
		
		/*Caves*/
		ret [36] = new Cell("CAVE_FLOOR", "soil", "Harsh cave soil", aps.get("CAVE_FLOOR"));
		ret [37] = new Cell("CAVE_WALL", "cave", "Rocky wall", aps.get("CAVE_WALL"), true, true);
		ret [119] = new Cell("CAVE_WATER", "subterranean lake", "Subterranean Lake", aps.get("CAVE_WATER")); ret[119].setShallowWater(true);
		ret [55] = new Cell("LAVA", "lava", "Lava", aps.get("LAVA"));
		
		/*Warehouse*/
		ret [151] = new Cell("WAREHOUSE_FLOOR", "ruined stone floor", "Ruined Stone Floor", aps.get("WAREHOUSE_FLOOR"));
		ret [152] = new Cell("WAREHOUSE_WALL", "cracked stone wal", "Cracked Stone Wall", aps.get("WAREHOUSE_WALL"), true, true);

		/*Castle Keep*/
		ret [41] = new Cell("BARRED_WINDOW", "barred window", "A wide window, covered with iron bars", aps.get("BARRED_WINDOW"), true, false);
		ret [42] = new Cell("DRACULA_THRONE1", "Dracula's Throne", "The throne of Dracula", aps.get("DRACULA_THRONE1"),true,false);
		ret [43] = new Cell("DRACULA_THRONE2", "Dracula's Throne", "The throne of Dracula", aps.get("DRACULA_THRONE2"));
		ret [44] = new Cell("BALCONY", "balcony", "Marble Balcony", aps.get("BALCONY"), true, false);
		ret [45] = new Cell("STONE_STAIRWAY", "stone stairway", "Rough stone stairway", aps.get("STONE_STAIRWAY"));
		ret [46] = new Cell("CLOCK_TOWER", "clock tower", "Clock Tower", aps.get("STONE_WALL"), true, true);
		ret [129] = new Cell("KEEP_FLOOR", "tiled floor", "Tiled floor", aps.get("KEEP_FLOOR"));
		ret [130] = new Cell("KEEP_WALL", "painted wall", "Painted Wall", aps.get("KEEP_WALL"),true,true);
		ret [131] = new Cell("KEEP_CARPET", "red carpet", "Red Carpet", aps.get("KEEP_CARPET"));
		/*Void*/
		ret [49] = new Cell("VOID_STAR", "star", "A star", aps.get("VOID_STAR"));
		ret [50] = new Cell("VOID_SUN", "star", "A star", aps.get("VOID_SUN"));
		ret [51] = new Cell("VOID_FLOOR", "nothing", "Nothing", aps.get("VOID"));
		ret [56] = new Cell("VOID_WALL", "nothing", "Nothing", aps.get("VOID"),true, true);
		
		/*Frank Lab*/
		ret [53] = new Cell("WIRES", "wires", "Electricity Wires", aps.get("WIRES"));
		ret [54] = new Cell("FRANK_TABLE", "experiment table", "Experiment Table", aps.get("FRANK_TABLE"));
		
		/*Chapel*/
		ret [64] = new Cell("CHURCH_WALL", "chapel wall", "Decrepit brick wall", aps.get("CHURCH_WALL"), true, true);
		ret [65] = new Cell("CHURCH_FLOOR", "chapel floor", "Polished stone floor", aps.get("CHURCH_FLOOR"));
		ret [66] = new Cell("CHURCH_WOODEN_BARRIER_H", "wooden barrier", "Ellaborated wooden barrier", aps.get("CHURCH_WOODEN_BARRIER_H"), true, false);
		ret [67] = new Cell("CHURCH_WOODEN_BARRIER_V", "wooden barrier", "Ellaborated wooden barrier", aps.get("CHURCH_WOODEN_BARRIER_V"), true, false);
		ret [68] = new Cell("CHURCH_CHAIR", "chapel chair", "Decrepit church chair", aps.get("CHURCH_CHAIR"), true, false);
		ret [69] = new Cell("CHURCH_CONFESSIONARY", "confessionary", "A wooden confessionary", aps.get("CHURCH_CONFESSIONARY"), true, true);
		ret [70] = new Cell("CHURCH_CARPET", "purple carpet", "A purple carpet", aps.get("CHURCH_CARPET"));
		ret [71] = new Cell("ATRIUM", "atrium", "The Chapel Atrium", aps.get("ATRIUM")); ret[71].setHeight(3);
		ret [72] = new Cell("CHURCH_STAINED_WINDOW", "stained window", "Beautiful glass window", aps.get("CHURCH_STAINED_WINDOW"), true, false);
		ret [73] = new Cell("CHURCH_FLOOR_H", "chapel floor", "Polished stone floor", aps.get("CHURCH_FLOOR_H"));
		ret [115] = new Cell("CHURCH_STAIRSUP", "stairs", "Stairs", aps.get("TOWN_STAIRSUP"));ret[115].setHeightMod(-1);
		ret [116] = new Cell("CHURCH_STAIRSDOWN", "stairs", "Stairs", aps.get("TOWN_STAIRSDOWN"));ret[116].setHeightMod(1);
		
		/*Ruins and Mummies Lair*/
		ret [74] = new Cell("RUINS_COLUMN", "stone column", "Ruined stone column", aps.get("RUINS_COLUMN"), true, true);
		ret [75] = new Cell("RUINS_FLOOR", "stone floor", "Ruined stone floor", aps.get("RUINS_FLOOR"));
		ret [76] = new Cell("RUINS_WALL", "stone wall", "Ruined stone wall", aps.get("RUINS_WALL"), true, true);
		ret [77] = new Cell("RUINS_FLOOR_H", "stone floor", "Ruined stone floor", aps.get("RUINS_FLOOR_H")); ret[77].setHeight(3);
		ret [78] = new Cell("RUINS_STAIRS", "stone stairs", "Ruined stone stairway", aps.get("RUINS_STAIRS")); ret[78].setHeight(1); ret[78].setIsStair(true);
		ret [126] = new Cell("RUINS_DOOR", "broken door", "Ruined door", aps.get("RUINS_DOOR"), false, true);
		
		/*Courtyard*/
		ret [79] = new Cell("COURTYARD_WALL", "castle wall", "Cracked stone wall", aps.get("COURTYARD_WALL"),true, true); 
		ret [80] = new Cell("COURTYARD_FLOOR", "stone floor", "Cracked stone floor", aps.get("COURTYARD_FLOOR"));
		ret [81] = new Cell("COURTYARD_COLUMN", "massive column", "Massive stone column", aps.get("COURTYARD_COLUMN"), true, true);
		ret [82] = new Cell("COURTYARD_FENCE", "iron fence", "Iron fence", aps.get("COURTYARD_FENCE"), true, false);
		ret [83] = new Cell("COURTYARD_GRASS", "dry grass", "Dry grass", aps.get("COURTYARD_GRASS"));
		ret [84] = new Cell("COURTYARD_FLOWERS", "flowers", "Flowers", aps.get("COURTYARD_FLOWERS"));
		ret [120] = new Cell("COURTYARD_FOUNTAIN_CENTER", "water fountain", "Source of water", aps.get("COURTYARD_FOUNTAIN_CENTER")); 
		ret [121] = new Cell("COURTYARD_FOUNTAIN_AROUND", "water fountain", "A fountain of water",aps.get("COURTYARD_FOUNTAIN_AROUND")); 
		ret [122] = new Cell("COURTYARD_FOUNTAIN_POOL", "fountain wall", "A fountain of water",aps.get("COURTYARD_FOUNTAIN_POOL")); ret[122].setShallowWater(true);
		ret [123] = new Cell("COURTYARD_TREE", "burnt tree", "Burnt tree", aps.get("COURTYARD_TREE"), true, true);
		
		ret [124] = new Cell("COURTYARD_RUINED_WALL", "ruined brick wall", "Ruined brick wall", aps.get("COURTYARD_RUINED_WALL"),true, true);
		ret [125] = new Cell("COURTYARD_STAIRS", "ruined brick stairs", "Ruined brick stairs", aps.get("COURTYARD_STAIRS")); ret[125].setHeight(1);ret[125].setIsStair(true);
		
		/*Clock Tower*/
		ret [85] = new Cell("TOWER_FLOOR", "brick floor", "Gray brick floor", aps.get("TOWER_FLOOR"));
		ret [86] = new Cell("TOWER_WALL", "brick wall", "Gray brick wall", aps.get("TOWER_WALL"), true, true);
		ret [87] = new Cell("TOWER_COLUMN", "massive brick column", "Gigantic bricks pillar", aps.get("TOWER_COLUMN"), true, true);
		ret [88] = new Cell("TOWER_WINDOW", "small barred window", "Arc barred window", aps.get("TOWER_WINDOW"), true, false);
		ret [89] = new Cell("CAMPANARIUM", "campanarium", "Tower with bronze bell", aps.get("CAMPANARIUM"), true, true);
		ret [90] = new Cell("TOWER_FLOOR_H", "brick floor (high)", "Gray brick floor", aps.get("TOWER_FLOOR_H"));ret [90].setHeight(3);
		ret [91] = new Cell("TOWER_STAIRS", "brick stairs", "Brick stairs", aps.get("TOWER_STAIRS")); ret [91].setHeight(1);ret [91].setIsStair(true);
		ret [92] = new Cell("CLOCK_GEAR_1", "big clock gear", "Rotating clock gear", aps.get("CLOCK_GEAR_1"));
		ret [93] = new Cell("CLOCK_GEAR_2", "big clock gear", "Rotating clock gear", aps.get("CLOCK_GEAR_2"));
		ret [94] = new Cell("CLOCK_GEAR_3", "big clock gear", "Clock gear", aps.get("CLOCK_GEAR_3"));
		ret [95] = new Cell("CLOCK_GEAR_4", "big clock gear", "Rotating clock gear", aps.get("CLOCK_GEAR_4"));
		ret [102] = new Cell("TOWER_UP", "stairway up", "Up stairway", aps.get("TOWER_UP")); ret[102].setHeightMod(-1);
		ret [103] = new Cell("TOWER_DOWN", "stairway down", "Down stairway", aps.get("TOWER_DOWN")); ret[103].setHeightMod(1);
		ret [104] = new Cell("TOWER_FLOOR_UP", "stairway up", "Up stairway", aps.get("TOWER_UP")); 
		ret [40] = new Cell("FAKE_STAIRDOWN", "Downward stairs", "Downward stairs", aps.get("TOWER_DOWN"));
		
		/*Dungeon*/
		ret [96] = new Cell("DUNGEON_FLOOR", "dungeon floor", "Dungeon floor", aps.get("DUNGEON_FLOOR"));
		ret [97] = new Cell("DUNGEON_DOOR", "eerie door", "Badly damaged door", aps.get("DUNGEON_DOOR"));
		ret [98] = new Cell("DUNGEON_WALL", "dungeon wall", "Dungeon Wall", aps.get("DUNGEON_WALL"),true,true);
		ret [99] = new Cell("DUNGEON_PASSAGE", "dark passageway", "Dark passageway", aps.get("DUNGEON_PASSAGE"),false,true);
		ret [100] = new Cell("DUNGEON_DOWN", "stairs down", "Stairs going down", aps.get("DUNGEON_DOWN")); 
		ret [101] = new Cell("DUNGEON_UP", "stairs up", "Stairs going up", aps.get("DUNGEON_UP"));
		
		ret[20].setHeight(3);
		//ret[29].setHeight(3);
		ret[34].setHeight(3);
		
		
		ret[55].setDamageOnStep(2);
		
		return ret;
	}

}
