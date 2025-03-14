package crl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.SwingUtilities;

import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import sz.csi.jcurses.JCursesConsoleInterface;
import sz.csi.wswing.WSwingConsoleInterface;
import sz.midi.STMidiPlayer;
import sz.util.Debug;
import crl.action.*;
import crl.action.monster.*;
import crl.action.monster.boss.*;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.ai.monster.BasicMonsterAI;
import crl.ai.monster.RangedAI;
import crl.ai.monster.UnderwaterAI;
import crl.ai.monster.WanderToPlayerAI;
import crl.ai.npc.PriestAI;
import crl.ai.npc.VillagerAI;
import crl.ai.player.WildMorphAI;
import crl.conf.console.data.CharAppearances;
import crl.conf.console.data.CharEffects;
import crl.conf.gfx.data.GFXAppearances;
import crl.conf.gfx.data.GFXConfiguration;
import crl.conf.gfx.data.GFXEffects;
import crl.data.Cells;
import crl.data.Features;
import crl.data.Items;
import crl.data.MonsterLoader;
import crl.debug.DebugBestiary;
import crl.feature.CountDown;
import crl.feature.FeatureFactory;
import crl.feature.SmartFeature;
import crl.feature.SmartFeatureFactory;
import crl.feature.ai.BlastCrystalAI;
import crl.feature.ai.CrossAI;
import crl.feature.ai.FlameAI;
import crl.feature.ai.NullSelector;
import crl.game.CRLException;
import crl.game.Game;
import crl.game.GameFiles;
import crl.game.GameVersion;
import crl.game.MonsterRecord;
import crl.game.PlayerGenerator;
import crl.game.SFXManager;
import crl.game.STMusicManagerNew;
import crl.item.ItemDataTable;
import crl.level.MapCellFactory;
import crl.monster.MonsterData;
import crl.npc.NPCData;
import crl.player.Player;
import crl.ui.Appearance;
import crl.ui.CommandListener;
import crl.ui.Display;
import crl.ui.UISelector;
import crl.ui.UserAction;
import crl.ui.UserCommand;
import crl.ui.UserInterface;
import crl.ui.consoleUI.CharAppearance;
import crl.ui.consoleUI.CharDisplay;
import crl.ui.consoleUI.CharPlayerGenerator;
import crl.ui.consoleUI.ConsoleUISelector;
import crl.ui.consoleUI.ConsoleUserInterface;
import crl.ui.consoleUI.effects.CharEffectFactory;
import crl.ui.effects.EffectFactory;
import crl.ui.graphicsUI.GFXDisplay;
import crl.ui.graphicsUI.GFXPlayerGenerator;
import crl.ui.graphicsUI.GFXUISelector;
import crl.ui.graphicsUI.GFXUserInterface;
import crl.ui.graphicsUI.SwingSystemInterface;
import crl.ui.graphicsUI.effects.GFXEffect;
import crl.ui.graphicsUI.effects.GFXEffectFactory;

public class Main {
	
	private final static int
		JCURSES_CONSOLE = 0,
		SWING_GFX = 1,
		SWING_CONSOLE = 2;
	
	public static final boolean
		DEBUG_MODE = true;
	
	// Controlled by COMMANDLINE FLAGS *ONLY*.
	private static int uiMode;	// default is SWING_GFX...
	
	public static UserInterface ui;
	private static UISelector uiSelector;
	
	private static final boolean
		CHECK_WEB_FOR_NEW_VERSION = false;
	
	// state/modes...
	// 'PlayScene': render tilemap+mobs etc, playing game/taking action. But:
	// there's 'PlayRun' - an actual roguelike run. 'PlayPrelude' - the intro.
	// PlayTutorial -- the Soleil/Chris control tutorial scene. 'PlayArena' -
	// the Sonja battle against waves of enemies in the courtyard. Also:
	// could make that mode more frantic / VS like, perhaps? Just a thought.
	// Then there's nonplay 'modes'. also subscreens within play mode / menus.
	// Options screen - turn down the damn music volume to match every other 
	// program on my system! Jesus, it's loud.
	
	
	// Appearances by ID: could EASILY be a fixed len, indexed array, using short IDs.
	public static HashMap<String, Appearance>
		appearances = new HashMap<>();

	// (AI)Selectors by ID: could EASILY be a fixed len, indexed array, using short IDs.
	public static HashMap<AIT, ActionSelector>
		selectors = new HashMap<>();
	
	public static HashMap<AT, Action>
		actions = new HashMap<>();
	
	public static EffectFactory efx;	//instead of EffectFactory.getSingleton()!
	public static ItemDataTable itemData;
	
	private static Game currentGame;
	private static boolean createNew = true;
	
	// user options, versus what's actual graphics/gamedata setup (moddable or otherwise).
	// seem like 2 different use cases. Not all players want to mod immediately.
	// but most want to have keybinds and set the screen size properly.
	
	// public static Conf conf;	// ?
	public static Properties configuration;
	private static Properties UIconfiguration;	// why's that separate??? WHY!?
	private static String uiFile;	// the file from which UIConfiguration is read...
	
	public static STMusicManagerNew music;

	// what *ARE* all the conf properties/fields? how're they set?
	
	/*
	public static String __getConfigurationVal(String key) {
		return configuration.getProperty(key);
	}
	*/

	private static void init() {
		if (createNew) {
			System.out.println("CastlevaniaRL "+Game.getVersion());
			System.out.println("by slashie ~ 2005-2007, 2010, 2024");
			System.out.println("Reading configuration");
			readConfiguration();
			GFXConfiguration gfx_configuration = null;
			try {
				
				// TODO just unify 'appearances'. Zero need for 2 sets
				// of classes. Load the spritedefs and char/colors as a struct.
				switch (uiMode) {
				case SWING_GFX:
					gfx_configuration = new GFXConfiguration(UIconfiguration);
					System.out.println("Initializing Graphics Appearances");
					initializeGAppearances(gfx_configuration);
					break;
				case JCURSES_CONSOLE:
				case SWING_CONSOLE:
					System.out.println("Initializing Char Appearances");
					initializeCAppearances();
					break;
				}
				
				System.out.println("Initializing Action Objects");
				initializeActions();
				initializeSelectors();
				System.out.println("Loading Data");
				initializeCells();
				initializeItems();
				initializeMonsters();
				NPCData.init();	// init NPCs
				initializeFeatures();
				initializeSmartFeatures();
				switch (uiMode) {
				case SWING_GFX:
					System.out.println("Initializing Swing GFX System Interface");
					SwingSystemInterface si = new SwingSystemInterface(gfx_configuration);
					System.out.println("Initializing Swing GFX User Interface");
					ui = new GFXUserInterface(gfx_configuration);
					Display.thus = new GFXDisplay(si, UIconfiguration, gfx_configuration);
					PlayerGenerator.thus = new GFXPlayerGenerator(si, gfx_configuration);
					
					// chicken-in-egg messy OOP init:
					efx = new GFXEffectFactory();
					GFXEffect[] fxDefs = new GFXEffects(gfx_configuration.effectsScale).effects;
					((GFXEffectFactory)efx).setEffects(fxDefs);
					
					initializeUI(si);
					break;
				case JCURSES_CONSOLE:
					System.out.println("Initializing JCurses System Interface");
					ConsoleSystemInterface csi = null;
					try {
						csi = new JCursesConsoleInterface();
					} catch (ExceptionInInitializerError eiie) {
						crash("Fatal Error Initializing JCurses", eiie);
						eiie.printStackTrace();
						System.exit(-1);
					}
					System.out.println("Initializing Console User Interface");
					ui = new ConsoleUserInterface();
					Display.thus = new CharDisplay(csi);
					PlayerGenerator.thus = new CharPlayerGenerator(csi);
					//messy oop init:
					efx = new CharEffectFactory();
					((CharEffectFactory)efx).setEffects(CharEffects.effects);
					initializeUI(csi);
					break;
				case SWING_CONSOLE:
					System.out.println("Initializing Swing Console System Interface");
					csi = new WSwingConsoleInterface();
					
					System.out.println("Initializing Console User Interface");
					ui = new ConsoleUserInterface();
					
					Display.thus = new CharDisplay(csi);
					PlayerGenerator.thus = new CharPlayerGenerator(csi);
					//messy oop init:
					efx = new CharEffectFactory();
					((CharEffectFactory)efx).setEffects(CharEffects.effects);
					initializeUI(csi);
				}
			} catch (Exception e) {
				crash("Error initializing", e);
			}
			music = new STMusicManagerNew();
			if (configuration.getProperty("enableSound") != null &&
				configuration.getProperty("enableSound").equals("true")) { // Sound
				if (configuration.getProperty("enableMusic") == null || 
					!configuration.getProperty("enableMusic").equals("true")) { // Music
					music.enabled = false;
				} else {
					System.out.println("Initializing Midi Sequencer");
					try {
						STMidiPlayer.sequencer = MidiSystem.getSequencer();
						//STMidiPlayer.setVolume(0.1);
						STMidiPlayer.sequencer.open();
						
					} catch (MidiUnavailableException mue) {
						Game.addReport("Midi device unavailable");
						System.out.println("Midi Device Unavailable");
						music.enabled = false;
						return;
					}
					System.out.println("Initializing Music Manager");
					
					Enumeration<Object> keys = configuration.keys();	// all conf fields.
					while (keys.hasMoreElements()) {
						String key = (String)keys.nextElement();
						if (key.startsWith("mus_")) {
							String musicTrackID = key.substring(4);
							music.addMusic(musicTrackID, configuration.getProperty(key));
						}
					}
					music.enabled = true;
				}
				if (configuration.getProperty("enableSFX") == null ||
					!configuration.getProperty("enableSFX").equals("true")) {
					SFXManager.enabled = false;
				} else {
					SFXManager.enabled = true;
				}
			}
			
			Player.initializeWhips(
			"LEATHER_WHIP", "CHAIN_WHIP", "VKILLERW","THORN_WHIP", "FLAME_WHIP", "LIT_WHIP");
			
			if (CHECK_WEB_FOR_NEW_VERSION) {
				GameVersion webVersion = GameVersion.getWebLatestVersion();
				if (webVersion == null) {
					ui.showVersionDialog("Error checking web for updates.", true);
					//System.err.println("null latest version");
				} else if (webVersion.equals(GameVersion.currentVersion)) {
					ui.showVersionDialog("You are using the latest available version", false);
				} else {
					ui.showVersionDialog("A newer version, "+webVersion.getCode()+" from "+webVersion.getFormattedDate()+" is available!", true);
				}
			}
			createNew = false;
		}
	}
	
	
	private static void readConfiguration() {
		configuration = new Properties();
		try {
			configuration.load(new FileInputStream("cvrl.cfg"));
		} catch (IOException e) {
			System.out.println("Error loading configuration file, please confirm existence of cvrl.cfg");
			System.exit(-1);
		}

		if (uiMode == SWING_GFX) {
			UIconfiguration = new Properties();

			try {
				UIconfiguration.load(new FileInputStream(uiFile));
			} catch (IOException e) {
				// TODO Gracefully fallback to some default??
				System.out.println("Error loading configuration file, please confirm existence of "+uiFile);
				System.exit(-1);
			}
		}
	}
	
	
	private static void title() {
		music.playKey("TITLE");
		int choice = Display.thus.showTitleScreen();
		switch (choice) {
		case 0:
			newGame();
			break;
		case 1:
			loadGame();
			break;
		case 2:
			prologue();
			break;
		case 3:
			training();
			break;
		case 4:
			arena();
			break;
		case 5:
			Display.thus.showHiscores(GameFiles.loadHighScores());
			Display.thus.showHiscores(GameFiles.loadArenaScores());
			title();
			break;
		
		case 6:
			System.out.println("CastlevaniaRL v"+Game.getVersion()+", clean Exit");
			System.out.println("Thank you for playing!");
			System.exit(0);
			break;
		}
	}
	
	private static void prologue() {
		if (currentGame != null){
			ui.removeCommandListener(currentGame);
		}
		currentGame = new Game();
		currentGame.setCanSave(false);
		currentGame.setInterfaces(ui, uiSelector);
		//si.cls();
		setMonsterRecord(GameFiles.getMonsterRecord());
		currentGame.prologue();
		title();
	}
	
	private static void arena() {
		if (currentGame != null) {
			ui.removeCommandListener(currentGame);
		}
		currentGame = new Game();
		currentGame.setCanSave(false);
		currentGame.setInterfaces(ui, uiSelector);
		setMonsterRecord(GameFiles.getMonsterRecord());
		//si.cls();
		currentGame.arena();
		title();
	}
	
	private static void training() {
		if (currentGame != null) {
			ui.removeCommandListener(currentGame);
		}
		currentGame = new Game();
		currentGame.setCanSave(false);
		currentGame.setInterfaces(ui, uiSelector);
		//si.cls();
		setMonsterRecord(GameFiles.getMonsterRecord());
		currentGame.training();
		//ui.refresh();	// or something? why's title gfx not clear when training ends?
		title();
	}
	
	private static void loadGame() {
		File[] saves = GameFiles.listSaves();
		
		int index = Display.thus.showSavedGames(saves);
		if (index == -1)
			title();
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saves[index]));
			currentGame = (Game)ois.readObject();
			ois.close();
		} catch (InvalidClassException ice) {
			// java.io.InvalidClassException: sz.util.Position; local class incompatible:
			//   stream classdesc serialVersionUID = -4833892013213657867,
			//   local class serialVersionUID = -7564520053860342315
			// Deserialisation failure (wrong version - game binary is more modern
			// than the attempted-to-load savefile).
			// beyond de/serialisation this can't be solved. Shouldn't auto-delete
			// user's files without asking though, right?
			// mark them in red / show 'outdated save file' msg on UI?
			// This is an "error" but not exceptional. Deal with it gracefully
			// and continue running. TODO don't crash. maybe offer option to
			// erase the save, and otherwise just abort loading.
			crash("Savefile no longer loadable (program version changed)", new CRLException("Savefile version obsolete"));
			
			return;	// or rather: go back to 'continue' / loadgame screen.
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			crash("Invalid savefile or wrong version", new CRLException("Invalid savefile or wrong version"));
		}
		
		currentGame.setInterfaces(ui, uiSelector);
		if (currentGame.getPlayer().level == null) {
			crash("Player wasnt loaded", new Exception("Player wasnt loaded"));
		}
		currentGame.setPlayer(currentGame.getPlayer());
		ui.setPlayer(currentGame.getPlayer());
		uiSelector.setPlayer(currentGame.getPlayer());
		setMonsterRecord(GameFiles.getMonsterRecord());
		currentGame.resume();
		
		title();
	}
	
	private static void newGame() {
		if (currentGame != null) {
			ui.removeCommandListener(currentGame);
		}
		currentGame = new Game();
		currentGame.setCanSave(true);
		currentGame.setInterfaces(ui, uiSelector);
		setMonsterRecord(GameFiles.getMonsterRecord());
		currentGame.newGame();
		
		title();
	}

	private static void initializeUI(Object si) throws CRLException {
		// isn't this already done ? elsewhere?
		Action walkAction = new Walk();
		Action jump = new Jump();
		Action thrown = new Throw();
		Action use = new Use();
		Action equip = new Equip();
		Action unequip = new Unequip();
		Action attack = new Attack();
		Action reload = new Reload();
		Action target = new TargetPS();
		Action switchWeapons = new SwitchWeapons();
		Action get = new Get();
		Action drop = new Drop();
		Action dive = new Dive(); 
		
		UserAction[] userActions = null;
		UserCommand[] userCommands = null;
		Properties keyBindings = null;
		// loadUserKeyMap()
		try {
			Properties keyConfig = new Properties();
			keyConfig.load(new FileInputStream("keys.cfg"));
			
			keyBindings = new Properties();
			keyBindings.put("WEAPON_KEY", readKeyString(keyConfig, "weapon"));
			keyBindings.put("DONOTHING1_KEY", readKeyString(keyConfig, "doNothing"));
			keyBindings.put("DONOTHING2_KEY", readKeyString(keyConfig, "doNothing2"));
			keyBindings.put("UP1_KEY", readKeyString(keyConfig, "up"));
			keyBindings.put("UP2_KEY", readKeyString(keyConfig, "up2"));
			keyBindings.put("LEFT1_KEY", readKeyString(keyConfig, "left"));
			keyBindings.put("LEFT2_KEY", readKeyString(keyConfig, "left2"));
			keyBindings.put("RIGHT1_KEY", readKeyString(keyConfig, "right"));
			keyBindings.put("RIGHT2_KEY", readKeyString(keyConfig, "right2"));
			keyBindings.put("DOWN1_KEY", readKeyString(keyConfig, "down"));
			keyBindings.put("DOWN2_KEY", readKeyString(keyConfig, "down2"));
			keyBindings.put("UPRIGHT1_KEY", readKeyString(keyConfig, "upRight"));
			keyBindings.put("UPRIGHT2_KEY", readKeyString(keyConfig, "upRight2"));
			keyBindings.put("UPLEFT1_KEY", readKeyString(keyConfig, "upLeft"));
			keyBindings.put("UPLEFT2_KEY", readKeyString(keyConfig, "upLeft2"));
			keyBindings.put("DOWNLEFT1_KEY", readKeyString(keyConfig, "downLeft"));
			keyBindings.put("DOWNLEFT2_KEY", readKeyString(keyConfig, "downLeft2"));
			keyBindings.put("DOWNRIGHT1_KEY", readKeyString(keyConfig, "downRight"));
			keyBindings.put("DOWNRIGHT2_KEY", readKeyString(keyConfig, "downRight2"));
			keyBindings.put("SELF1_KEY", readKeyString(keyConfig, "self"));
			keyBindings.put("SELF2_KEY", readKeyString(keyConfig, "self2"));
			keyBindings.put("ATTACK1_KEY", readKeyString(keyConfig, "attack1"));
			keyBindings.put("ATTACK2_KEY", readKeyString(keyConfig, "attack2"));
			keyBindings.put("JUMP_KEY", readKeyString(keyConfig, "jump"));
			keyBindings.put("THROW_KEY", readKeyString(keyConfig, "throw"));
			keyBindings.put("EQUIP_KEY", readKeyString(keyConfig, "equip"));
			keyBindings.put("UNEQUIP_KEY", readKeyString(keyConfig, "unequip"));
			keyBindings.put("RELOAD_KEY", readKeyString(keyConfig, "reload"));
			keyBindings.put("USE_KEY", readKeyString(keyConfig, "use"));
			keyBindings.put("GET_KEY", readKeyString(keyConfig, "get"));
			keyBindings.put("GET2_KEY", readKeyString(keyConfig, "get2"));
			keyBindings.put("DROP_KEY", readKeyString(keyConfig, "drop"));
			keyBindings.put("DIVE_KEY", readKeyString(keyConfig, "dive"));
			keyBindings.put("TARGET_KEY", readKeyString(keyConfig, "target"));
			keyBindings.put("SWITCH_WEAPONS_KEY", readKeyString(keyConfig, "switchWeapons"));
			keyBindings.put("QUIT_KEY", readKeyString(keyConfig, "PROMPTQUIT"));
			keyBindings.put("HELP1_KEY", readKeyString(keyConfig, "HELP1"));
			keyBindings.put("HELP2_KEY", readKeyString(keyConfig, "HELP2"));
			keyBindings.put("LOOK_KEY", readKeyString(keyConfig, "LOOK"));
			keyBindings.put("PROMPT_SAVE_KEY", readKeyString(keyConfig, "PROMPTSAVE"));
			keyBindings.put("SHOW_SKILLS_KEY", readKeyString(keyConfig, "SHOWSKILLS"));
			keyBindings.put("SHOW_INVENTORY_KEY", readKeyString(keyConfig, "SHOWINVEN"));
			keyBindings.put("SHOW_STATS_KEY", readKeyString(keyConfig, "SHOWSTATS"));
			keyBindings.put("CHARDUMP_KEY", readKeyString(keyConfig, "CHARDUMP"));
			keyBindings.put("SHOW_MESSAGE_HISTORY_KEY", readKeyString(keyConfig, "SHOWMESSAGEHISTORY"));
			keyBindings.put("SHOW_MAP_KEY", readKeyString(keyConfig, "SHOWMAP"));
			keyBindings.put("EXAMINE_LEVEL_MAP_KEY", readKeyString(keyConfig, "EXAMINELEVELMAP"));
			keyBindings.put("SWITCH_MUSIC_KEY", readKeyString(keyConfig, "SWITCHMUSIC"));
			
			Display.keyBindings = keyBindings;
			
			userActions = new UserAction[] {
				new UserAction(attack, i(keyBindings.getProperty("ATTACK1_KEY"))),
				new UserAction(attack, i(keyBindings.getProperty("ATTACK2_KEY"))),
				new UserAction(jump, i(keyBindings.getProperty("JUMP_KEY"))),
				new UserAction(thrown, i(keyBindings.getProperty("THROW_KEY"))),
				new UserAction(equip, i(keyBindings.getProperty("EQUIP_KEY"))),
				new UserAction(unequip, i(keyBindings.getProperty("UNEQUIP_KEY"))),
				new UserAction(reload, i(keyBindings.getProperty("RELOAD_KEY"))),
				new UserAction(use, i(keyBindings.getProperty("USE_KEY"))),
				new UserAction(get, i(keyBindings.getProperty("GET_KEY"))),
				new UserAction(drop, i(keyBindings.getProperty("DROP_KEY"))),
				new UserAction(dive, i(keyBindings.getProperty("DIVE_KEY"))),
				new UserAction(target, i(keyBindings.getProperty("TARGET_KEY"))),
				new UserAction(switchWeapons, i(keyBindings.getProperty("SWITCH_WEAPONS_KEY"))),
				new UserAction(get, i(keyBindings.getProperty("GET2_KEY"))),
			};

			userCommands = new UserCommand[] {
				new UserCommand(CommandListener.PROMPTQUIT, i(keyBindings.getProperty("QUIT_KEY"))),
				new UserCommand(CommandListener.HELP, i(keyBindings.getProperty("HELP1_KEY"))),
				new UserCommand(CommandListener.LOOK, i(keyBindings.getProperty("LOOK_KEY"))),
				new UserCommand(CommandListener.PROMPTSAVE, i(keyBindings.getProperty("PROMPT_SAVE_KEY"))),
				new UserCommand(CommandListener.SHOWSKILLS, i(keyBindings.getProperty("SHOW_SKILLS_KEY"))),
				new UserCommand(CommandListener.HELP, i(keyBindings.getProperty("HELP2_KEY"))),
				new UserCommand(CommandListener.SHOWINVEN, i(keyBindings.getProperty("SHOW_INVENTORY_KEY"))),
				new UserCommand(CommandListener.SHOWSTATS, i(keyBindings.getProperty("SHOW_STATS_KEY"))),
				new UserCommand(CommandListener.CHARDUMP, i(keyBindings.getProperty("CHARDUMP_KEY"))),
				new UserCommand(CommandListener.SHOWMESSAGEHISTORY, i(keyBindings.getProperty("SHOW_MESSAGE_HISTORY_KEY"))),
				new UserCommand(CommandListener.SHOWMAP, i(keyBindings.getProperty("SHOW_MAP_KEY"))),
				new UserCommand(CommandListener.EXAMINELEVELMAP, i(keyBindings.getProperty("EXAMINE_LEVEL_MAP_KEY"))),
				new UserCommand(CommandListener.SWITCHMUSIC, i(keyBindings.getProperty("SWITCH_MUSIC_KEY"))),
			};
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			if (si instanceof SwingSystemInterface) {
				((SwingSystemInterface)si).showAlert("Problem reading keys.cfg config file: " + e.getMessage());
			}
			throw new RuntimeException("keys.cfg config file not found");
		} catch (IOException e) {
			e.printStackTrace();
			if (si instanceof SwingSystemInterface) {
				((SwingSystemInterface)si).showAlert("Problem reading keys.cfg config file: " + e.getMessage());
			}
			throw new RuntimeException("Problem reading keys.cfg config file");
		} catch (Exception e) {
			e.printStackTrace();
			if (si instanceof SwingSystemInterface) {
				((SwingSystemInterface)si).showAlert("Problem initializing UI: " + e.getMessage());
			}
			throw e;
		}
		
		// FIXME game's internals shouldn't need to differ this much.
		switch (uiMode) {
		case SWING_GFX:
			((GFXUserInterface)ui).init((SwingSystemInterface)si, userCommands, target);
			uiSelector = new GFXUISelector();
			((GFXUISelector)uiSelector).init((SwingSystemInterface)si, userActions, UIconfiguration, walkAction, target, attack, keyBindings);
			break;
		case JCURSES_CONSOLE:
			((ConsoleUserInterface)ui).init((ConsoleSystemInterface)si, userCommands, target);
			uiSelector = new ConsoleUISelector();
			((ConsoleUISelector)uiSelector).init((ConsoleSystemInterface)si, userActions, walkAction, target, attack, keyBindings);
			break;
		case SWING_CONSOLE:
			((ConsoleUserInterface)ui).init((ConsoleSystemInterface)si, userCommands, target);
			uiSelector = new ConsoleUISelector();
			// ((WSwingConsoleInterface)uiSelector).in
	//		uiSelector.init(si, userCommands, target);	// ...
			// Swing Console UI .. boots, opens, doesn't work. doesn't centre.
			//uiSelector = new ConsoleUISelector();	// ?OK?
			break;
		}
	}


	private static int i(String s) {
		return Integer.parseInt(s);
	}


	private static String readKeyString(Properties config, String keyName) throws CRLException {
		return readKey(config, keyName)+"";
	}


	private static int readKey(Properties config, String keyName) throws CRLException {
		String fieldName = config.getProperty(keyName).trim();
		if (fieldName == null)
			throw new RuntimeException("Invalid key.cfg file, property not found: "+keyName);
		try {
			Field field = CharKey.class.getField(fieldName);
			return field.getInt(CharKey.class);
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading field : "+fieldName);
		} catch (NoSuchFieldException e) {
			throw new CRLException("Invalid key name: ["+fieldName+"]");
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading field : "+fieldName);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException("Error reading field : "+fieldName);
		}
	}


	private static void initializeGAppearances(GFXConfiguration gfx_configuration) {
		Appearance[] definitions = new GFXAppearances(gfx_configuration).getAppearances();
		ArrayList<String> allGfxIDs = new ArrayList<>(200);
		for (Appearance a: definitions) {
			String appID = a.getID();
			
			// check data parity with console appearances.
		//	CharAppearance ca = CharAppearances.defs[];	// indexOf ...??
			
			appearances.put(appID, a);
			allGfxIDs.add(appID);
		}
		HashMap<String, CharAppearance> charaps = new HashMap<>();
		for (CharAppearance ca: CharAppearances.defs) {
			String caid = ca.getID();
			charaps.put(caid, ca);
			
			// what about all the GfxAppearance IDs that are missing from charaps list?
			// Must list/find both possibilities.
			if (!appearances.containsKey(caid)) {
				System.err.println("CharAppearance defined that doesn't appear in GfxAppearances: "+caid);
			}
		}
		
		for (String gid: allGfxIDs) {
			if (!charaps.containsKey(gid)) {
				System.err.println("GfxAppearance defined that doesn't appear in CharAppearances: "+gid);
			}
		}
		
		System.err.println("Missing ones notwithstanding there's a total of "+allGfxIDs.size()+ " gfxapprs.");
		//472.
		/*
CharAppearance defined that doesn't appear in GfxAppearances: DARKTREE
CharAppearance defined that doesn't appear in GfxAppearances: COURTYARDDIRT
CharAppearance defined that doesn't appear in GfxAppearances: FOUNTAINPOOL
GfxAppearance defined that doesn't appear in CharAppearances: BLOOD1
GfxAppearance defined that doesn't appear in CharAppearances: BLOOD2
GfxAppearance defined that doesn't appear in CharAppearances: BLOOD3
Missing ones notwithstanding there's a total of 472 gfxApprearances.
		 */
	}
	
	
	private static void initializeCAppearances() {
		for (Appearance a: CharAppearances.defs) {
			appearances.put(a.getID(), a);
		}
	}
	
	
	private static void initializeActions() {
		// NB: these are all monster/boss actions.
		actions = new HashMap<>();
		Action[] adefs = {
			new Dash(),	// is in fact 'monster "SliceDive"!? Why called Dash!??
			new MonsterWalk(),
			new Swim(),
			new MonsterCharge(),
			new MonsterMissile(),
			new SummonMonster(),
			new MummyStrangle(),
			new MummyTeleport(),
			new Teleport(),
			new MandragoraScream()
		};
		for (Action a: adefs) {
			actions.put(a.getID(), a);
		}
	}
	
	
	// lifted from old 'ActionFactory'
	public static Action getAction(AT id) {
		Action a = actions.get(id);
		Debug.doAssert(a != null, "Tried to get an invalid "+id+" Action");
		return a;
	}
	
	
	// MAP TILE DEFINITIONS (terrain types...)
	private static void initializeCells() {
		MapCellFactory.getMapCellFactory().init(Cells.getCellDefinitions(appearances));
	}

	private static void initializeFeatures() {
		FeatureFactory.init(Features.getFeatureDefinitions(appearances));
	}

	private static void initializeSelectors() {
		ActionSelector[] definitions = {
			new WanderToPlayerAI(),	// defines ID of: "WANDER"... via METHOD 'getID' .. Data as Function. VERY P-OOP.
			new UnderwaterAI(),
			new RangedAI(),
			new FlameAI(),
			new CrossAI(),
			new BlastCrystalAI(),
			new CountDown(),
			new VillagerAI(),
			new PriestAI(),
			new NullSelector(),
			new BasicMonsterAI(),
			new WildMorphAI()
		};
		selectors.clear();	// init only once.
		for (int i=0; i<definitions.length; i++) {
			ActionSelector as = definitions[i];
			selectors.put(as.getID(), as);
		}
	}


	private static void initializeMonsters() throws CRLException {
		MonsterData.init(MonsterLoader.getMonsterDefinitions(
			"data/monsters.ecsv","data/monsters.exml"));
	}


	private static void initializeItems() {
		// if loaded from asset/data file, could 'user-override' location
		itemData = new ItemDataTable();
		itemData.init(Items.defs);
	}


	private static void initializeSmartFeatures() {
		//public static SmartFeature[] getSmartFeatures(HashMap<String, ActionSelector> acs) {
		HashMap<String, Appearance> aps = Main.appearances;
		HashMap<AIT, ActionSelector> ais = selectors;
		// Bad constructor parameterisation evident:
		SmartFeature[] sms = new SmartFeature[6];
		sms[0] = new SmartFeature("CROSS", "Holy cross", aps.get("CROSS"));
		sms[0].selector = ais.get(AIT.CROSS_SELECTOR);
		
		sms[1] = new SmartFeature("BURNING_FLAME", "Burning Flame", aps.get("FLAME"));
		sms[1].selector = ais.get(AIT.FLAME_SELECTOR);
		sms[1].damageOnStep = 1;
		
		sms[2] = new SmartFeature("GARLIC", "Garlic", aps.get("GARLIC"));
		sms[2].selector = ais.get(AIT.COUNTDOWN);
		sms[2].effectOnStep = "TRAP";
		
		sms[3] = new SmartFeature("BIBUTI", "Bibuti Powder", aps.get("BIBUTI"));
		sms[3].selector = ais.get(AIT.COUNTDOWN);
		sms[3].damageOnStep = 3;
		
		sms[4] = new SmartFeature("FLAME", "Magic Flame", aps.get("FLAME"));
		sms[4].selector = ais.get(AIT.COUNTDOWN);
		sms[4].damageOnStep = 4;
		
		sms[5] = new SmartFeature("BLAST_CRYSTAL", "Blast Crystal", aps.get("BLAST_CRYSTAL"));
		sms[5].selector = ais.get(AIT.CRYSTAL_SELECTOR);
		
		SmartFeatureFactory.init(sms);
	}


	public static void crash(String message, Throwable exception) {
		System.out.println("CastlevaniaRL "+Game.getVersion()+": Error");
		System.out.println("");
		System.out.println("Unrecoverable error: "+message);
		exception.printStackTrace();
		if (currentGame != null) {
			System.out.println("Trying to save game");
			GameFiles.saveGame(currentGame, currentGame.getPlayer());
		}
		ui.showCriticalError("Error: " + message);
		System.exit(-1);
	}


	// FIXME pointless getter/setter/accessors, just make this table public!!!
	private static Hashtable<String, MonsterRecord> monsterRecord;

	public static MonsterRecord getMonsterRecordFor(String monsterID) {
		return monsterRecord.get(monsterID);
	}

	public static void setMonsterRecord(Hashtable<String, MonsterRecord> monsterRecord) {
		Main.monsterRecord = monsterRecord;
	}

	public static Hashtable<String, MonsterRecord> getMonsterRecord() {
		return monsterRecord;
	}
	
	
	public static void main(String args[]) {
		uiMode = SWING_GFX;
		uiFile = "slash-barrett.ui";
		if (args.length > 0) {	//args != null && ?!
			if (args[0].equalsIgnoreCase("sgfx")) {	// Could allow 'sg'? swing graphical?
				uiMode = SWING_GFX;
				if (args.length > 1) {
					uiFile = args[1];
				} else {
					uiFile = "slash-barrett.ui";
				}
				// TODO: Check/only use arbitrary input name if .ui file exists?
				
			} else if (args[0].equalsIgnoreCase("jc")) {
				uiMode = JCURSES_CONSOLE;
			} else if (args[0].equalsIgnoreCase("sc")) {
				uiMode = SWING_CONSOLE;
			}
		}
		
		init();
		System.out.println("Launching game");
		
		if (DEBUG_MODE) {
			SwingUtilities.invokeLater(() -> {
				DebugBestiary db = new DebugBestiary();
				db.setVisible(true);
			});
		}
		
		try {
			title();
		} catch (Exception e) {
			Game.crash("Unrecoverable Exception [Press Space]", e);
			try {
				System.in.read();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
}
