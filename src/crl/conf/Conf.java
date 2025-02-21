package crl.conf;

public class Conf {
	
	// default conf as a struct. Seems loaded as Properties from cvrl.cfg ...
	
	// but UIConf is 2nd separate file which controls:
	// mouse control, tileset/sprite gfx, 
	
	// i dislike it itself being a 'Properties' object because you have to 
	// query everything by String key. instead just DEFINE FIELDS in a struct.
	// read/write it to file easily with either Serialisation, stringio, xml or
	// anything. .ini lines or yaml better, plainer.
	
	//public Conf() {
	//	// ??
	//}
	
	public boolean
		showBlood = true,
	
		enableSound = true,	// All Sound / master mute
		enableBGM = true,		// previously called 'enableMusic'
		enableSFX = true;
	
	// double / relative... 0-1? Log? etc?
	public double
		volumeBGM = 0.91,
		volumeSFX = 0.91;
	
	
	// Music Track Indices
	public static final int
		PRELUDE = 0,
		WRECKAGE = 1,
		DAY_TOWN = 2,	// what about TOWN_DAY and TOWN_NIGHT!???
		TRANSYLVANIA_DAY = 3,
		TRANSYLVANIA_NIGHT = 4,
		CASTLE_BRIDGE = 5,
		GARDEN = 6,
		HALLS = 7,
		BOSS1 = 8,
		LAB = 9,
		BOSS2 = 10,
		CHAPEL = 11,
		RUINS = 12,
		CAVES = 13,
		COURTYARD = 14,
		DUNGEON = 15,
		TOWER = 16,
		DEATH = 17,
		KEEP = 18,
		DRACULA = 19,
		EXECUTION = 20,
		BADBELMONT = 21,
		QUARTERS = 22,
		WAREHOUSE = 23,
		CATACOMBS = 24,
		RESERVOIR = 25,
		DINING_HALL = 26,
		SEWERS = 27,
		ARENA  = 28,
		ARENA2 = 29,
		ARENA3 = 30,
		ARENA4 = 31,
		ARENA5 = 32,
		ARENA6 = 33,
		ARENA7 = 34,
		ARENA8 = 35,
		THIRD_BOUT = 36,
		CHRIS_DEAD = 37,
		VICTORY = 38,
		TRAINING = 39,
		TITLE = 40,
		GAME_OVER = 41;
		
	// Music Files, use mp3 or mid
	// Level music
	public String[]
		MUSIC_TRACKS =
	{
		"midi/Nicholas - Prelude (CV).mid",		// PRELUDE
		"midi/Jorge Fuentes - Beginning (CV3).mid",	// WRECKAGE
		"midi/Jorge Fuentes - Silence of the Daylight (CV2).mid",	// DAY_TOWN
		"midi/Jorge Fuentes - Bloody Tears (CV2).mid",	// DAY_TRANSYLVANIA
		"midi/Jorge Fuentes - Monster Dance (CV2).mid",	// NIGHT_TRANSYLVANIA
		"midi/Jorge Fuentes - Aquarius (CV3).mid",//mus_CASTLE_BRIDGE = 
		"midi/Jorge Fuentes - Within these Castle Walls (CV2).mid",//mus_GARDEN = 
		"midi/Jorge Fuentes - Vampire Killer (CV).mid",//mus_HALLS = 
		"midi/Nicholas - Reincarnated Soul (CVB).mid",//mus_BOSS1 = 
		"midi/JiLost - Stalker (CV).mid",//mus_LAB = 
		"midi/Nicholas - Reincarnated Soul (CVB).mid",//mus_BOSS2 = 
		"midi/Jorge Fuentes - Prayer of a Tragic Queen (CVB).mid",//mus_CHAPEL = 
		"midi/Jorge Fuentes - Wicked Child (CV).mid",//mus_RUINS = 
		"midi/Nicholas - Walking On The Edge (CV).mid",//mus_CAVES = 
		"midi/Jorge Fuentes - Maze Garden (CV64).mid",//mus_COURTYARD = 
		"midi/JiLost - Heart Of Fire (CV).mid",//mus_DUNGEON = 
		"midi/Jorge Fuentes - Clocktower (HoD).mid",//mus_TOWER = 
		"midi/Jorge Fuentes - Dancing in Phantasmic Hell (CVX).mid",//mus_DEATH = 
		"midi/Jorge Fuentes - Iron Blue Intention (CVB).mid",//mus_KEEP = 
		"midi/Jorge Fuentes - Dance of Illusions (CVX).mid",//mus_DRACULA = 
		"midi/Jorge Fuentes - Overture (CV3).mid",//mus_EXECUTION = 
		"midi/Jorge Fuentes - Opposing Bloodlines (CVX).mid",	//mus_BADBELMONT = 
		"midi/Jorge Fuentes - Offense and Defense (HoD).mid",	//mus_QUARTERS = 
		"midi/Jorge Fuentes - Demon Seed (CV3).mid",	//mus_WAREHOUSE = 
		"midi/Jorge Fuentes - Clockwork Mansion (CV4).mid",	//mus_CATACOMBS = 
		"midi/Jorge Fuentes - Rising (CV3).mid",	//mus_RESERVOIR = 
		"midi/Jorge Fuentes - Chandeliers (CV4).mid",	//mus_DINING_HALL = 
		"midi/Jorge Fuentes - Encounter (CV3).mid",	//mus_SEWERS = 
		"midi/Jorge Fuentes - Bloody Tears (CVA3).mid",	//mus_ARENA = 
		"midi/Nicholas - Stage 4 (CVA3).mid",	//mus_ARENA2 = 
		"midi/JiLost - Ripe Seeds (CVA2).mid",	//mus_ARENA3 = 
		"midi/JiLost - Ripe Seeds (CVA2).mid",	//mus_ARENA4 = 
		"midi/Jorge Fuentes - Passpied  (CVA2).mid",	//mus_ARENA5 = 
		"midi/Jorge Fuentes - Passpied  (CVA2).mid",	//mus_ARENA6 = 
		"midi/Jorge Fuentes - Passpied  (CVA2).mid",	//mus_ARENA7 = 
		"midi/Jorge Fuentes - Battle of the Holy (CVA).mid",	//mus_ARENA8 = 
		"midi/Jorge Fuentes - New Messiah (CVA2).mid",	//mus_THIRD_BOUT = 
		"midi/Tom Kim - Praying Hands (CVA2).mid",	//mus_CHRIS_DEAD = 
		"midi/Jorge Fuentes - Evergreen (CV3).mid",	//mus_VICTORY = 
		"midi/Jorge Fuentes - Journey to Chaos (CVL).mid",	//mus_TRAINING = 
		"midi/Jorge Fuentes - Message of Darkness (CV2).mid",	//mus_TITLE = 
		"midi/Nicholas - Nightmare (CoTM).mid"	//mus_GAME_OVER = 
	};
	// these get loaded in as music-named keys in a loop. so... yep. int IDs.
	
	
	// but then the .wav *sound effects' are all specified internally in the
	// spreadsheet. better detached and assigned numerical indices so can be
	// swapped out/easily modded via priority load locations.
	
	// So what's in UIConfig?
	
	// default is from: slash-barrett.ui
	// CastlevaniaRL UI Configuration File
	// Slash - Christopher Barrett UI
	// 1024x768, 64x64 "big" sprites

	boolean
	useMouse = false;
	
	// Tileset conf. (should this be in config file, or some mod addon format?)
	String
	TILESET = "dragontileset.gif",
	TILESET_DARK = "dragontileset_dark.gif";		// Q: How directly-overlay-able is it??
	
	int TILESIZE = 64,
		BIG_TILESIZE = 96,
		CELL_HEIGHT = 98;
	
	int[]
		PC_POS = {8,6},
		CAMERA_POS = {-32,-32};
	
	int XRANGE = 8,
		YRANGE = 6,
		UPLEFTBORDER = 0,
		
		// *bad* that these are redeclarations of same number.
		CAMERA_SCALE = 2,
		VIEWPORT_UI_SCALE = 2,
		EFFECTS_SCALE = 2,
	
		// Message Box (!?!)
		// Q: What's the point in putting in a conf file as a hard 'preset' when:
		// other alternative fonts aren't readily listed/available to players/users,
		// and secondly, there's no in-game controls/way to adjust said font ?
		FNT_MESSAGEBOX_SIZE = 20,				// in "Points"
		FNT_PERSISTANTMESSAGEBOX_SIZE = 20;		// in "Points"

	String
		FNT_MESSAGEBOX = "res/v5easter.ttf",
		FNT_PERSISTANTMESSAGEBOX = "res/v5easter.ttf";

	int[] MSGBOX_BOUNDS = {10,500,775,90};
	
	int 
		COLOR_MSGBOX_ACTIVE = 0xff0064c8,	// 0,100,200
		COLOR_MSGBOX_INACTIVE = 0xff00c864,	// 0,200,100
		MSGBOX_BGROUND = 0xff000000,//0,0,0	// "COLOR_MSGBOX_BG"?
		
		POS_LEVELDESC_X = 20,
		POS_LEVELDESC_Y = 40;


	String
		IMG_STATUSSCR_BGROUND = "gfx/barrett-moon_2x.gif";
	
	int
		WINDOW_WIDTH = 1024,
		WINDOW_HEIGHT = 768;
	double SCREEN_SCALE = 1.28;

	String IMG_ICON = "res/crl_icon.png";	// why make this configurable?

	// ??? How are these colours read in via properties?
	// See "PropertyFilters". but it's R,G,B OR R,G,B,A.
	// hence it uses 3-element Color constructor when it's RGB, meaning alpha is
	// by default: 255 (0xff).
	int
		COLOR_WINDOW_BACKGROUND = 0xff000000,
		COLOR_BORDER_IN = 0xffbba150,//187,161,80
		COLOR_BORDER_OUT = 0xff5c4e60,// 92,78,96
		COLOR_BOLD = 0xffa3a54a;//163,165,74;
	
	String
	// "Textures" (Tiles, Sprite sheets, UI elements)
		IMG_TITLE = "gfx/barrett-title_2x.gif",
		IMG_PROLOGUE = "gfx/barrett-moon_2x.gif",
		IMG_RESUME = "gfx/barrett-moon_2x.gif",
		IMG_INTERFACE = "gfx/barrett-moon_2x.gif",
		IMG_ENDGAME = "gfx/barrett-moon_2x.gif",
		IMG_HISCORES = "gfx/barrett-moon_2x.gif",
		IMG_SAVED = "gfx/barrett-moon_2x.gif",
		IMG_LEVEL_UP = "gfx/barrett-moon_2x.gif",
		IMG_FRAME = "gfx/barrett-moon_2x.gif",
		IMG_MAP = "gfx/barrett-map.gif",
		IMG_MAPMARKER = "gfx/barrett-interface.gif";

	// sprite definition for mapmarker sprite in IMG_MAPMARKER (presumably?)
	int[] IMG_MAPMARKER_BOUNDS = {318,3,14,14};
	String IMG_PICKER = "gfx/barrett-picker.gif";
	int[] IMG_PICKER_BOUNDS = {0,0,214,19};
	String IMG_BORDERS = "gfx/barrett-interface.gif";
	int[] IMG_BORDERS_BOUNDS = {0,0,133,34};
	String IMG_GADGETS = "gfx/barrett-picker.gif";
	int[] IMG_GADGETS_BOUNDS = {0,0,36,150};

	//String FNT_TITLE = "res/avatar.ttf",	// Defined+duped/overridden in SAME PROPS FILE.
	String FNT_TITLE = "res/FETTEFRA.ttf";
	int FNT_TITLE_SIZE = 32;

	String FNT_TEXT = "res/v5easter.ttf";
	int FNT_TEXT_SIZE = 16;

	String FNT_PROLOGUE = "res/v5easter.ttf";
	int FNT_PROLOGUE_SIZE = 24;

	String FNT_MONO = "res/v5easter.ttf";
	int FNT_MONO_SIZE = 15;

	String SFXSET = "gfx/dragon-drashsfx.gif";
	String SFXSET2 = "gfx/tempSlashes_2x.gif";

	String
	TILES_SHADOW = "gfx/shadow_2x.gif",
	TILES_CHARACTERS = "gfx/crl_chars_2x.gif",
	TILES_MONSTERS = "gfx/crl_mons32_2x.gif",
	TILES_BIG_MONSTERS = "gfx/crl_mons48_2x.gif",
	TILES_ITEMS = "gfx/crl_items_2x.gif",
	TILES_ITEM_ICONS = "gfx/crl_items.gif",
	TILES_TERRAIN = "gfx/crl_terrain_2x.gif",
	TILES_DARK_TERRAIN = "gfx/crl_terrain_d_2x.gif",
	TILES_NIGHT_TERRAIN = "gfx/crl_terrain_night_2x.gif",
	TILES_DARK_NIGHT_TERRAIN = "gfx/crl_terrain_night_d_2x.gif",
	TILES_FEATURES = "gfx/crl_features_2x.gif",
	TILES_FEATURE_ICONS = "gfx/crl_features.gif",
	TILES_EFFECTS = "gfx/crl_effects_2x.gif",

	// 'FX', not sound effects.
	SFX_EFFECTS = "gfx/crl_effects_2x.gif",
	SFX_SLASHES = "gfx/tempSlashes_2x.gif",
	SFX_CURVED_SLASHES = "gfx/crl_slashes1_2x.gif",
	SFX_STRAIGHT_SLASHES = "gfx/crl_slashes2_2x.gif",

	UI_TILES = "gfx/barrett-interface.gif",
	VIEWPORT_UI_TILES = "gfx/barrett-interface_2x.gif";
	
	
	
}
