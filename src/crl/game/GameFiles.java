package crl.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import crl.Main;
import crl.actor.Message;
import crl.item.ItemDefinition;
import crl.player.Equipment;
import crl.player.GameSessionInfo;
import crl.player.HiScore;
import crl.player.MonsterDeath;
import crl.player.Player;
import crl.player.Skill;
import crl.ui.UserInterface;
import sz.util.Debug;

public class GameFiles {
	
	private static final String
		APP_NAME = "castlevaniarl",
		OSV = System.getProperty("os.name").toLowerCase();
	public static final boolean
		OS_IS_WINDOWS = OSV.contains("win"),
		OS_IS_MACOSX  = OSV.startsWith("mac os x"),
		OS_IS_LINUX   = OSV.contains("linux") || OSV.contains("unix");
	
	// User Profile/Folder items:
	// High Scores; [DONE]
	// Arena Scores; [DONE]
	// Graveyard(KillCounts/HuntRecord);
	// 'Mods' Folder; (ensure exists on startup. check for override names!?)
	// Memorials.
	// SaveGames (runs in progress). Should be encrypted?
	// also: cvrl.cfg, keys.cfg, and slash-barret.ui equivalents. (move to profile dir)
	
	// The deployed game has its asserts/defs bundled in the jar. But other
	// (user) files should go in user's 'application data'. Changeable format
	// things should probably be labelled with the version they're made by.
	
	// Anything users are not meant to meddle with could be DES encrypted.
	// Users could have a 'mod' folder, with naming conventions allowing for
	// game resource loaders to override internal assets...
	public static File getProfileFolder() {
		String userhome = System.getProperty("user.home");
		File profileFolder = null;
		if (OS_IS_WINDOWS) {
			String appDataFolder = System.getenv("APPDATA");
			if (appDataFolder == null) {
				// can this clause even happen though? (win permissions issue?)
				profileFolder = new File(userhome, "."+APP_NAME+"/");
			} else {
				profileFolder = new File(appDataFolder, APP_NAME+"/");
			}
		} else if (OS_IS_MACOSX) {
			// ~/Library/Application Support/ is 'just for this user'.
			// And ROOT /Library/Application Support/ is for ALL USERS.
			// Root one is visible by default in Finder, but personal one is NOT
			// but OSX doesn't give us write access for the global Library.
			// BUT: as a user, after making userhome/Library visible in Finder,
			// that seems workably fine.
			//editorDataFolder = new File("/Library/Application Support/arcworks/realmseditor/");
			//we do not have write access for global Library, though! DAMN IT!
			//editorDataFolder = new File(userhome, "/realmseditor/");	//copout!
			profileFolder = new File(userhome,
				"Library/Application Support/" + APP_NAME + "/");
		} else if (OS_IS_LINUX) {
			profileFolder = new File(userhome, ".config/" + APP_NAME + "/");
		} else {
			System.err.println("Unknown OS.");
			profileFolder = new File(userhome, "."+APP_NAME + "/");
		}
		ensureFolderExists(profileFolder);
		return profileFolder;
	}
	
	
	public static void ensureFolderExists(File folder) {
		if (!folder.exists() || !folder.isDirectory()) {
			if (folder.mkdirs()) {
				// All ok!
			} else {
				Game.crash("Unable to create folder: "+folder.getAbsolutePath());
			}
		}
	}
	
	// guarantees (except in exceptional disk conditions/permissions) that 
	// folder path of requested file exists.
	public static File getUserFile(String userFilePath) {
		File pf = getProfileFolder();
		File uf = new File(pf, userFilePath);
		File parentfolder = uf.getParentFile();
		ensureFolderExists(parentfolder);
		return uf;
	}

	private static File getUserFolder(String userFolderPath) {
		File pf = getProfileFolder();
		File uf = new File(pf, userFolderPath);
		ensureFolderExists(uf);
		return uf;
	}

	public static boolean writeToFile(String text, File f) {
		String filepath = f.getAbsolutePath();
		File parentfolder = f.getParentFile();
		if (parentfolder != null) {
			ensureFolderExists(parentfolder);
		}
		
		if (f.exists() && !f.canWrite()) {
			// Attempt deletion (or save as .old?). Failing that, return false.
			boolean deleted = f.delete();
			if (!deleted) {
				System.err.println("File ["+filepath+"] already exists, and could not be edited or deleted.");
				//logger / critical event/errlog somewhere in a txtfile, for user-submittable sendbacks?
				return false;
			}
		}
		
		try {
			f.createNewFile();	// Must ensure it's there! Filewriter crashes otherwise!
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			bw.write(text);
			bw.close();
		} catch (IOException ioe) {
			System.err.println("Error while writing text data to file: ["+filepath+"]");
			ioe.printStackTrace();
			return false;
		}
		return true;
	}


	public static HiScore[] loadHighScores() {
		File scorefile = getUserFile(HIGH_SCORE_FILE);
		if (!scorefile.exists()) {
			// Create default high score table.
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<10; i++) {
				sb.append("Simon;VKL;0;29/04/2007;0;-;0\r\n");
			}
			writeToFile(sb.toString(), scorefile);
			// 1. Arikado, whatever, 0, 2, xyz, whatnot.
			// 2. PowPow, ???, ...?
			// all should be easily beatable things though, yes?
			// +How about writing 10x: Simon;VKL;(score?);TODAY's Date or install date, minus a few days?
		}
		return _loadScores(scorefile);
	}
	
	
	public static HiScore[] loadArenaScores() {
		File scorefile = getUserFile(ARENA_SCORE_FILE);
		if (!scorefile.exists()) {
			// Create default arena high score table.
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<10; i++) {
				sb.append("Arena;VKL;0;29/04/2007;0;-;0\r\n");
			}
			writeToFile(sb.toString(), scorefile);
		}
		return _loadScores(scorefile);
	}
	
	
	private static HiScore[] _loadScores(File scoreTableFile) {
		Debug.enterStaticMethod("GameFiles", "loadScores");
		HiScore[] ret = new HiScore[10];
		try {
			BufferedReader br = new BufferedReader(new FileReader(scoreTableFile));
			
			for (int i = 0; i < 10; i++) {
				String line = br.readLine();
				String[] regs = line.split(";");
				if (regs == null) {
					br.close();
					Game.crash("Invalid or corrupt hiscore table");
					// TODO Resilience!: Recover/keep going by creating a new
					// hiscore file after archiving (rename) the corrupt one.
				}
				HiScore x = new HiScore();
				x.setName(regs[0]);
				x.setPlayerClass(regs[1]);
				x.setScore(Integer.parseInt(regs[2]));
				x.setDate(regs[3]);
				x.setTurns(regs[4]);
				x.setDeathString(regs[5]);
				x.setDeathLevel(Integer.parseInt(regs[6]));
				ret[i] = x;
			}
			br.close();
			Debug.exitMethod(ret);
			return ret;
		} catch (IOException ioe) {
			Game.crash("Invalid or corrupt hiscore table", ioe);
			// TODO Resilience/recover. create default highscores file...?
		}
		return null;
	}
	
	
	static class SaveGameFilenameFilter implements FilenameFilter {
		public boolean accept(File arg0, String arg1) {
			// if (arg0.getName().endsWith(".sav"))
			return (arg1.endsWith(".sav"));
		}
	}
	
	public static File[] listSaves() {
		File saveDir = GameFiles.getUserFolder(GameFiles.SAVES_PATH);
		File[] files = saveDir.listFiles(new SaveGameFilenameFilter());
		if (files == null) {
			return new File[0];
		}
		return files;
	}
	
	private static final String
		SAVES_PATH = "savegames",
		BONES_PATH = "memorials";
	
	private static final String
		HIGH_SCORE_FILE  = "tables/hiscore.tbl",
		ARENA_SCORE_FILE = "tables/arena.tbl",	// "scores/" rather than tables?
		HUNTRECORD_FILE  = "tables/graveyard";
	
	public static void saveHighScore(Player player) {
		File scorefile = getUserFile(HIGH_SCORE_FILE);
		_saveHiScore(player, scorefile);
	}
	
	public static void saveArenaHighScore(Player player) {
		File scorefile = getUserFile(ARENA_SCORE_FILE);
		_saveHiScore(player, scorefile);
	}
	
	private static void _saveHiScore(Player player, File scoreFile) {
		Debug.enterStaticMethod("GameFiles", "saveHiscore");
		int score = player.getScore();
		String name = player.getName();
		String playerClass = "NONE";
		switch (player.getPlayerClass()){
		case Player.CLASS_INVOKER:
			playerClass="INV";
			break;
		case Player.CLASS_KNIGHT:
			playerClass="KNG";
			break;
		case Player.CLASS_MANBEAST:
			playerClass = "MNB";
			break;
		case Player.CLASS_RENEGADE:
			playerClass="RNG";
			break;
		case Player.CLASS_VAMPIREKILLER:
			playerClass="VKL";
			break;
		case Player.CLASS_VANQUISHER:
			playerClass = "VAN";
			break;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String now = sdf.format(new Date());

		// read existing hi scores
		HiScore[] scores = _loadScores(scoreFile);

		try {
			//BufferedWriter bw = FileUtil.getWriter(hiscoreFile);
			BufferedWriter bw = new BufferedWriter(new FileWriter(scoreFile));
			for (int i = 0; i < 10; i++) {
				if (score > scores[i].getScore()) {
					bw.write(name+";"+playerClass+";"+score+";"+now+";"+player.getGameSessionInfo().turns+";"+player.getGameSessionInfo().getShortDeathString()+";"+player.getGameSessionInfo().deathLevel);
					bw.newLine();
					score = -1;
					if (i == 9) {
						break;
					}
				}
				bw.write(scores[i].getName()+";"+scores[i].getPlayerClass()+";"+scores[i].getScore()+";"+scores[i].getDate()+";"+scores[i].getTurns()+";"+scores[i].getDeathString()+";"+scores[i].getDeathLevel());
				bw.newLine();
			}
			bw.close();
			Debug.exitMethod();
		}catch(IOException ioe){
			ioe.printStackTrace(System.out);
			Game.crash("Invalid or corrupt hiscore table", ioe);
		}
	}


	public static void saveMemorialFile(Player player) {
		// the character died, you game-overed. eg "Sonia(31-01-2025).life"
		// seems insufficient, if I immediately do another run and choose Sonia
		// as charname, on same day!?
		try {
			File bonesFile = makeBonesFile(player, false);
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(bonesFile));
			
			GameSessionInfo gsi = player.getGameSessionInfo();
			gsi.deathLevelDescription = player.getLevel().getDescription();
			String heshe = (player.getSex() == Player.MALE ? "He" : "She");
			
			fileWriter.write("/-----------------------------------");fileWriter.newLine();
			fileWriter.write(" CastlevaniaRL"+Game.getVersion()+ " Post Mortem");fileWriter.newLine();
			fileWriter.write(" -----------------------------------/");fileWriter.newLine();
			fileWriter.newLine();fileWriter.newLine();
			fileWriter.write(player.getPlot()+", "+player.getDescription()+" journeys to the cursed castle.");fileWriter.newLine();fileWriter.newLine();
			fileWriter.write(player.getName()+ ", the "+player.getClassString()+", "+gsi.getDeathString()+" on the "+gsi.deathLevelDescription+" (Level "+gsi.deathLevel+")...");fileWriter.newLine();
			fileWriter.write(heshe+" survived for "+gsi.turns+" turns and scored "+player.getScore()+" points, collecting a total of "+gsi.goldCount+" gold.");fileWriter.newLine();
			fileWriter.newLine();
			fileWriter.write(heshe +" was able to use the following skills:");
			fileWriter.newLine();
			Vector<Skill> skills = player.getAvailableSkills();
			for (int i = 0; i < skills.size(); i++){
				fileWriter.write(((Skill)skills.elementAt(i)).getMenuDescription());fileWriter.newLine();
			}
			
			fileWriter.newLine();
			fileWriter.write(heshe+" had the following proficiencies:");
			fileWriter.newLine();
			fileWriter.write("Hand to hand combat "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_UNARMED)]);
			fileWriter.newLine();
			fileWriter.write("Daggers             "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_DAGGERS)]);
			fileWriter.newLine();
			fileWriter.write("Swords              "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_SWORDS)]);
			fileWriter.newLine();
			fileWriter.write("Spears              "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_SPEARS)]);
			fileWriter.newLine();
			fileWriter.write("Whips               "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_WHIPS)]);fileWriter.newLine();
			fileWriter.write("Maces and Flails    "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_MACES)]);fileWriter.newLine();
			fileWriter.write("Pole Weapons        "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_STAVES)]);fileWriter.newLine();
			fileWriter.write("Rings               "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_RINGS)]);fileWriter.newLine();
			fileWriter.write("Hand thrown items   "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_PROJECTILES)]);fileWriter.newLine();
			fileWriter.write("Bows / XBows        "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_BOWS)]);fileWriter.newLine();
			fileWriter.write("Missile machinery   "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_PISTOLS)]);fileWriter.newLine();
			fileWriter.write("Shields             "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_SHIELD)]);fileWriter.newLine();
			
			fileWriter.newLine();
			Vector<String> history = gsi.getHistory();
			for (int i = 0; i < history.size(); i++){
				fileWriter.write(heshe + " " + history.elementAt(i));
				fileWriter.newLine();
			}
			fileWriter.newLine();
			fileWriter.write(heshe +" took "+gsi.getTotalDeathCount()+" souls to the other world");fileWriter.newLine();
			
			//int i = 0;
			Enumeration<MonsterDeath> monsters = gsi.getDeathCount().elements();
			while (monsters.hasMoreElements()) {
				MonsterDeath mons = monsters.nextElement();
				fileWriter.write(mons.getTimes() +" "+mons.getMonsterDescription());fileWriter.newLine();
				//i++;
			}
			fileWriter.newLine();
			fileWriter.write("-- Final Stats --");fileWriter.newLine();
			fileWriter.newLine();
			fileWriter.write(player.getName()+" the level "+ player.getPlayerLevel()+" "+player.getClassString() + " "+player.getStatusString());fileWriter.newLine();
			fileWriter.write("Sex: "+ (player.getSex() == Player.MALE ? "M" : "F"));
			fileWriter.newLine();
			fileWriter.write("Hits: "+player.getHits()+ "/"+player.getHitsMax()+" Hearts: " + player.getHearts() +"/"+player.getHeartsMax()+
				  " Gold: "+player.getGold()+ " Keys: "+player.getKeys());fileWriter.newLine();
			fileWriter.write("Carrying: "+player.getItemCount()+"/"+player.getCarryMax());
			fileWriter.newLine();
			fileWriter.write("Attack: +"+player.getAttack());fileWriter.newLine();
			fileWriter.write("Soul Power: +"+player.getSoulPower());fileWriter.newLine();
			fileWriter.write("Evade: "+player.getEvadeChance()+"%");fileWriter.newLine();
			fileWriter.write("Combat: "+(50-player.getAttackCost()));fileWriter.newLine();
			fileWriter.write("Invokation: "+(50-player.getCastCost()));fileWriter.newLine();
			fileWriter.write("Movement: "+(50-player.getWalkCost()));fileWriter.newLine();
			
			fileWriter.write("Experience: "+player.getXp()+"/"+player.getNextXP());
			fileWriter.newLine();
			fileWriter.newLine();
			
			Vector<Equipment> inventory = player.getInventory();
			fileWriter.newLine();
			fileWriter.write("-- Inventory --");fileWriter.newLine();
			fileWriter.write("Weapon    "+player.getEquipedWeaponDescription());fileWriter.newLine();
			fileWriter.write("Secondary "+player.getSecondaryWeaponDescription());fileWriter.newLine();
			fileWriter.write("Armor     "+player.getArmorDescription());fileWriter.newLine();
			fileWriter.write("Shield    "+player.getAccDescription());fileWriter.newLine();fileWriter.newLine();
			
			for (Iterator<Equipment> iter = inventory.iterator(); iter.hasNext();) {
				Equipment element = iter.next();
				fileWriter.write(element.getQuantity()+ " - "+ element.getMenuDescription());
				fileWriter.newLine();
			}
			fileWriter.newLine();
			fileWriter.write("-- Last Messages --");
			fileWriter.newLine();
			Vector<String> messages = Main.ui.getMessageBuffer();
			for (int j = 0; j < messages.size(); j++) {
				fileWriter.write(messages.elementAt(j).toString());
				fileWriter.newLine();
			}
			
			fileWriter.close();
		} catch (IOException ioe){
			Game.crash("Error writing the memorial file", ioe);
		}
		
	}
	
	
	static class SaveFilenameFilter implements FilenameFilter {
		private Player p;
		public SaveFilenameFilter(Player p) {
			this.p = p;
		}
		public boolean accept(File dir, String name) {
			return name.startsWith(p.getName());
		}
	}
	
	
	public static void saveGame(Game g, Player p) {
		// Delete previous saves
		File saveDir = GameFiles.getUserFile(SAVES_PATH);
		File[] previousSaves = saveDir.listFiles(new SaveFilenameFilter(p));
		if (previousSaves != null && previousSaves.length > 0) {
			for (File file: previousSaves) {
				file.delete();
			}
		}
		
		String filename = p.getName()+", a Lv"+p.getPlayerLevel()+" "+p.getClassString()+".sav";
		File savef = new File(saveDir, filename);
		p.setSelector(null);
		try {
			/*
			SerializableChecker sc = new SerializableChecker();
			sc.writeObject(g);
			sc.close();
			*/
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(savef));
			os.writeObject(g);
			os.close();
		} catch (IOException ioe){
			Game.crash("Error saving the game", ioe);
		} catch (ExceptionInInitializerError eiie) {
			System.err.println("serializablechecker may have failed?");
			eiie.printStackTrace();
		}
		
	}
	
	/**
	 * Deletes all savegame files with name of given player.
	 * @param p
	 */
	public static void permadeath(Player p) {
		File saveDir = getUserFile(SAVES_PATH);
		File[] previousSaves = saveDir.listFiles(new SaveFilenameFilter(p));
		if (previousSaves == null) {
			return;	// nothing to delete/save directory empty or not found.
		}
		for (File file: previousSaves) {
			file.delete();
		}
	}
	
	
	public static void saveChardump(Player player) {
		try {
			File bonesFile = makeBonesFile(player, true);
			BufferedWriter fileWriter = new BufferedWriter(new FileWriter(bonesFile));
			
			GameSessionInfo gsi = player.getGameSessionInfo();
			gsi.deathLevelDescription = player.getLevel().getDescription();
			String heshe = (player.getSex() == Player.MALE ? "He" : "She");
			
			fileWriter.write("/-----------------------------------");fileWriter.newLine();
			fileWriter.write(" CastlevaniaRL"+Game.getVersion()+ " Post Mortem");fileWriter.newLine();
			fileWriter.write(" -----------------------------------/");fileWriter.newLine();
			fileWriter.newLine();
			fileWriter.newLine();
			fileWriter.write(player.getPlot()+", "+player.getDescription()+" journeys to the cursed castle.");fileWriter.newLine();fileWriter.newLine();
			fileWriter.write(player.getName()+ ", the "+player.getClassString()+", survives on the "+player.getLevel().getDescription()+" (Level "+player.getLevel().levelNumber+")...");fileWriter.newLine();
			fileWriter.write(heshe+" has survived for "+gsi.turns+" turns and has scored "+player.getScore()+" points, collecting a total of "+gsi.goldCount+" gold.");fileWriter.newLine();
			fileWriter.newLine();
			fileWriter.write(heshe +" is able to use the following skills:");fileWriter.newLine();
			Vector<Skill> skills = player.getAvailableSkills();
			for (int i = 0; i < skills.size(); i++){
				fileWriter.write(skills.elementAt(i).getMenuDescription());
				fileWriter.newLine();
			}
			
			fileWriter.newLine();
			fileWriter.write(heshe+" has the following proficiencies:");
			fileWriter.newLine();
			fileWriter.write("Hand to hand combat "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_UNARMED)]);fileWriter.newLine();
			fileWriter.write("Daggers             "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_DAGGERS)]);fileWriter.newLine();
			fileWriter.write("Swords              "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_SWORDS)]);fileWriter.newLine();
			fileWriter.write("Spears              "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_SPEARS)]);fileWriter.newLine();
			fileWriter.write("Whips               "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_WHIPS)]);fileWriter.newLine();
			fileWriter.write("Maces and Flails    "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_MACES)]);fileWriter.newLine();
			fileWriter.write("Pole Weapons        "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_STAVES)]);fileWriter.newLine();
			fileWriter.write("Rings               "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_RINGS)]);fileWriter.newLine();
			fileWriter.write("Hand thrown items   "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_PROJECTILES)]);fileWriter.newLine();
			fileWriter.write("Bows / XBows        "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_BOWS)]);fileWriter.newLine();
			fileWriter.write("Missile machinery   "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_PISTOLS)]);fileWriter.newLine();
			fileWriter.write("Shields             "+UserInterface.verboseSkills[player.weaponSkill(ItemDefinition.CAT_SHIELD)]);fileWriter.newLine();
			
			fileWriter.newLine();
			Vector<String> history = gsi.getHistory();
			for (int i = 0; i < history.size(); i++){
				fileWriter.write(heshe + " " + history.elementAt(i));
				fileWriter.newLine();
			}
			fileWriter.newLine();
			fileWriter.write(heshe +" has taken "+gsi.getTotalDeathCount()+" souls to the other world");
			fileWriter.newLine();
			
		///	int i = 0;
			Enumeration<MonsterDeath> monsters = gsi.getDeathCount().elements();
			while (monsters.hasMoreElements()) {
				MonsterDeath mons = monsters.nextElement();
				fileWriter.write(mons.getTimes() +" "+mons.getMonsterDescription());
				fileWriter.newLine();
		///		i++;
			}
			fileWriter.newLine();
			fileWriter.write("-- Current Stats --");fileWriter.newLine();fileWriter.newLine();
			fileWriter.write(player.getName()+" the level "+ player.getPlayerLevel()+" "+player.getClassString() + " "+player.getStatusString());fileWriter.newLine();
			fileWriter.write("Sex: "+ (player.getSex() == Player.MALE ? "M" : "F"));fileWriter.newLine();
			fileWriter.write("Hits: "+player.getHits()+ "/"+player.getHitsMax()+" Hearts: " + player.getHearts() +"/"+player.getHeartsMax()+
				  " Gold: "+player.getGold()+ " Keys: "+player.getKeys());fileWriter.newLine();
			fileWriter.write("Carrying: "+player.getItemCount()+"/"+player.getCarryMax());fileWriter.newLine();
			fileWriter.write("Attack: +"+player.getAttack());fileWriter.newLine();
			fileWriter.write("Soul Power: +"+player.getSoulPower());fileWriter.newLine();
			fileWriter.write("Evade: "+player.getEvadeChance()+"%");fileWriter.newLine();
			fileWriter.write("Combat: "+(50-player.getAttackCost()));fileWriter.newLine();
			fileWriter.write("Invokation: "+(50-player.getCastCost()));fileWriter.newLine();
			fileWriter.write("Movement: "+(50-player.getWalkCost()));fileWriter.newLine();
			
			fileWriter.write("Experience: "+player.getXp()+"/"+player.getNextXP());fileWriter.newLine();
			fileWriter.newLine();
			
			Vector<Equipment> inventory = player.getInventory();
			fileWriter.newLine();
			fileWriter.write("-- Inventory --");
			fileWriter.newLine();
			fileWriter.write("Weapon "+player.getEquipedWeaponDescription());fileWriter.newLine();
			fileWriter.write("Secondary "+player.getSecondaryWeaponDescription());fileWriter.newLine();
			fileWriter.write("Armor     "+player.getArmorDescription());fileWriter.newLine();
			fileWriter.write("Shield    "+player.getAccDescription());fileWriter.newLine();fileWriter.newLine();
			
			for (Iterator<Equipment> iter = inventory.iterator(); iter.hasNext();) {
				Equipment element = iter.next();
				fileWriter.write(element.getQuantity()+ " - "+ element.getMenuDescription());fileWriter.newLine();
			}
			fileWriter.newLine();
			fileWriter.write("-- Latest Messages --");fileWriter.newLine();
			Vector<String> messages = Main.ui.getMessageBuffer();
			for (int j = 0; j < messages.size(); j++){
				fileWriter.write(messages.elementAt(j).toString());
				fileWriter.newLine();
			}
			
			fileWriter.close();
		} catch (IOException ioe){
			Game.crash("Error writing the chardump", ioe);
		}
	}


	private static String getMemorialFileName(Player player, boolean succeeded) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");
		String now = sdf.format(new Date());
		String live = succeeded ? " {Alive}": "";
		String bonesName = player.getName()+live+"("+now+").life";
		return bonesName;
	}
	
	
	private static File makeBonesFile(Player player, boolean playerWon) {
		File bonesDir = getUserFolder(BONES_PATH);
		String bonesName = getMemorialFileName(player, playerWon);
		File bonesFile = new File(bonesDir, bonesName);
		try {
			//boolean createdOK = 
			bonesFile.createNewFile();
		} catch (IOException e) {
			return null;
		}
		return bonesFile;
	}


	public static Hashtable<String, MonsterRecord> getMonsterRecord() {
		Hashtable<String, MonsterRecord> ret = new Hashtable<>();
		// 'graveyard' isn't an intuitive name. maybe "Hunting Records"? killcounts?
		File huntRecord = getUserFile(HUNTRECORD_FILE);
		if (!huntRecord.exists()) {
			// Create default 'graveyard'. Current default is: blank file.
			writeToFile("", huntRecord);
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(huntRecord));
			String line = br.readLine();
			while (line != null) {
				String[] regs = line.split(",");
				MonsterRecord x = new MonsterRecord();
				x.setMonsterID(regs[0]);
				x.setKilled(Integer.parseInt(regs[1]));
				x.setKillers(Integer.parseInt(regs[2]));
				ret.put(x.getMonsterID(), x);
				line = br.readLine();
			}
			br.close();
			return ret;
		} catch(IOException ioe) {
			Game.crash("Invalid/corrupt hunt-records.", ioe);
		} catch (NumberFormatException nfe) {
			Game.crash("Corrupt hunt-records", nfe);
		}
		return null;
	}
	
	
	public static void updateGraveyard(Hashtable<String,MonsterRecord> graveyard, GameSessionInfo gsi) {
		Hashtable<String,MonsterDeath> session = gsi.getDeathCount();
		Enumeration<String> keys = session.keys();
		while (keys.hasMoreElements()) {
			String monsterID = keys.nextElement();
			MonsterDeath deaths = session.get(monsterID);
			MonsterRecord record = graveyard.get(monsterID);
			if (record == null) {
				record = new MonsterRecord();
				record.setMonsterID(monsterID);
				record.setKilled(deaths.getTimes());
				graveyard.put(monsterID, record);
			} else {
				record.setKilled(record.getKilled()+deaths.getTimes());
			}
		}
		if (gsi.getKillerMonster() != null) {
			MonsterRecord record = graveyard.get(gsi.getKillerMonster().getID());
			if (record == null) {
				record = new MonsterRecord();
				record.setMonsterID(gsi.getKillerMonster().getID());
				record.setKillers(1);
				graveyard.put(gsi.getKillerMonster().getID(), record);
			}else{
				record.setKillers(record.getKillers()+1);
			}
		}
		
		// Save to file
		File huntRecordFile = getUserFile(HUNTRECORD_FILE);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(huntRecordFile));
			Enumeration<String> wKeys = graveyard.keys();
			while (wKeys.hasMoreElements()){
				MonsterRecord record = graveyard.get(wKeys.nextElement());
				bw.write(record.getMonsterID()+","+record.getKilled()+","+record.getKillers());
				bw.newLine();
			}
			bw.close();
		} catch (IOException ioe) {
			ioe.printStackTrace(System.out);
			Game.crash("Invalid or corrupt graveyard", ioe);
		}
	}
}
