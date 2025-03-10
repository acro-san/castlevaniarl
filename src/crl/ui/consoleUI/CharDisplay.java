package crl.ui.consoleUI;

import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

import crl.Main;
import crl.cuts.Chat;
import crl.cuts.CutsceneDialogue;
import crl.data.Text;
import crl.game.Game;
import crl.game.MonsterRecord;
import crl.monster.Monster;
import crl.npc.Hostage;
import crl.player.GameSessionInfo;
import crl.player.HiScore;
import crl.player.Player;
import crl.player.advancements.Advancement;
import static crl.ui.Colors.*;
import crl.ui.Display;
import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import sz.csi.textcomponents.TextBox;
import sz.util.Position;
import sz.util.TxtTpl;

public class CharDisplay extends Display {
	private ConsoleSystemInterface si;
	
	public CharDisplay(ConsoleSystemInterface si){
		this.si = si;
	}
	
	public int showTitleScreen() {
		((ConsoleUserInterface)Main.ui).showPersistantMessageBox = false;
		si.cls();
		printBars();

		//Brahms Castle
		int castlex = 35;
		int castley = 5;
		for (String c: Text.TUI_CASTLE) {
			si.print(castlex,castley, c, BROWN);
			castley++;
		}	// (15 lines ascii art)

		int titleMenuX = 20;
		int titleMenuY = 12;
		for (String optionTxt: Text.TITLE_MENU) {
			si.print(titleMenuX, titleMenuY, optionTxt, WHITE);
			titleMenuY++;	// goes to 18, with 7 titlemenu items.
		}
		
		// CRL Logo
		int logox = 20;
		int logoy = 4;
		si.print(logox+2,logoy, " /-  -----------\\", RED);
		si.print(logox+2,logoy+1, "<                >", RED);
		si.print(logox+2,logoy+2, " \\-  -----------/", RED);
		si.print(logox+5,logoy, "/\\", YELLOW);
		si.print(logox+5,logoy+1, "|astlevaniaRL", YELLOW);
		si.print(logox+5,logoy+2, "\\/", YELLOW);
		
		String t = Text.TITLE_DISCLAIMER;
		si.print((80 - t.length()) / 2, 20, t, DARK_RED);
		t = Text.TITLE_GAME_VER_DEV;
		si.print((80 - t.length()) / 2, 21, t, WHITE);
		t = Text.TITLE_MUSICCREDIT;
		si.print((80 - t.length()) / 2, 22, t, WHITE);

		si.refresh();
		Main.music.playKey("TITLE");
		CharKey x = new CharKey(CharKey.NONE);
		// TODO process menu-defined keycodes based on menu itself? ...
		while (x.code != CharKey.A && x.code != CharKey.a &&
				x.code != CharKey.B && x.code != CharKey.b &&
				x.code != CharKey.C && x.code != CharKey.c &&
				x.code != CharKey.E && x.code != CharKey.e &&
				x.code != CharKey.D && x.code != CharKey.d &&
				x.code != CharKey.G && x.code != CharKey.g &&
				x.code != CharKey.F && x.code != CharKey.f) {
			x = si.inkey();
		}
		si.cls();
		switch (x.code) {
		case CharKey.A: case CharKey.a:
			return 0;
		case CharKey.B: case CharKey.b:
			return 1;
		case CharKey.C: case CharKey.c:
			return 2;
		case CharKey.D: case CharKey.d:
			return 3;
		case CharKey.E: case CharKey.e:
			return 4;
		case CharKey.F: case CharKey.f:
			return 5;
		case CharKey.G: case CharKey.g:
			return 6;
		}
		return 0;
	}
	
	
	public void showIntro(Player player) {
		si.cls();
		printBars();
		si.print(32,2, "Prologue", DARK_RED);
		
		TextBox tb1 = new TextBox(si);
		tb1.setPosition(2,4);
		tb1.setHeight(3);
		tb1.setWidth(76);
		tb1.setForeColor(RED);
		tb1.setText(Text.PROLOGUE_LINE0);
		
		TextBox tb2 = new TextBox(si);
		tb2.setPosition(2,8);
		tb2.setHeight(4);
		tb2.setWidth(76);
		tb2.setForeColor(RED);
		tb2.setText(Text.PROLOGUE_LINE1);
		
		TextBox tb = new TextBox(si);
		tb.setPosition(2,13);
		tb.setHeight(4);
		tb.setWidth(76);
		tb.setForeColor(RED);
		tb.setText(player.getPlot()+", "+player.getDescription()+" journeys to the cursed castle.");
		tb1.draw();
		tb2.draw();
		tb.draw();
		si.print(2,18, "[Press Space]", BLUE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();
	}
	
	public boolean showResumeScreen(Player player) {
		GameSessionInfo gsi = player.getGameSessionInfo();
		si.cls();
		printBars();
		String heshe = (player.sex == Player.MALE ? "He" : "She");
		
		si.print(2,3, "The chronicles of "+player.getName(), RED);
		
		TextBox tb = new TextBox(si);
		tb.setPosition(2,5);
		tb.setHeight(3);
		tb.setWidth(70);
		tb.setForeColor(RED);
		tb.setText("  ...And so it was that "+player.getDescription() + ", "+gsi.getDeathString()+" on the "+player.level.getDescription()+"...");
		tb.draw();
		
		si.print(2,9, heshe+ " scored "+ player.score +" points and earned "+ player.getGold() +" gold", RED);
		si.print(2,10, heshe + " survived for "+gsi.turns+" turns ", RED);

		si.print(2,11, heshe + " took "+gsi.getTotalDeathCount()+" monsters to the other world", RED);
/*
        int i = 0;
		Enumeration monsters = gsi.getDeathCount().elements();
		while (monsters.hasMoreElements()){
			MonsterDeath mons = (MonsterDeath) monsters.nextElement();
			si.print(5,11+i, mons.getTimes() +" "+mons.getMonsterDescription(), RED);
			i++;
		}
*/		si.print(2,14, "Do you want to save your character memorial? [Y/N]");
		si.refresh();
		return Main.ui.prompt();
	}

	public void showEndgame(Player player) {
		si.cls();
		printBars();
		String heshe = (player.sex == Player.MALE ? "he" : "she");
		String hisher = (player.sex == Player.MALE ? "his" : "her");

		si.print(2,3, "                           ", RED);
		
		TextBox tb = new TextBox(si);
		tb.setPosition(2,5);
		tb.setHeight(8);
		tb.setWidth(76);
		tb.setForeColor(RED);
		
		//TODO TxtTpl replacements.
		tb.setText(player.getName()+ " made many sacrifices, but now the long fight is over. Dracula is dead "+
				"and all other spirits are asleep. In the shadows, a person watches the castle fall. "+
				player.getName()+" must go for now but "+heshe+" hopes someday "+heshe+" will get the "+
				"respect that "+heshe+" deserves.    After this fight the new Belmont name shall be honored "
				+"by all people.");
		tb.draw();
		si.print(2,15, "You played the greatest role in this history... ", RED);
		si.print(2,16, "Thank you for playing.", RED);

		si.print(2,17, "CastlevaniaRL: v"+Game.getVersion(), RED);
		si.print(2,18, "Santiago Zapata 2005-2007", RED);

		si.print(2,20, "[Press Space]", BLUE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();

	}
	
	public void showHiscores(HiScore[] scores) {
		si.cls();

		si.print(2,1, "                      Castlevania RL "+Game.getVersion(), RED);
		si.print(2,2, "                  ~ The most brave of Belmonts ~", RED);

		si.print(13,4, "Score");
		//si.print(21,4, "Score");
		si.print(25,4, "Date");	
		si.print(36,4, "Turns");
		si.print(43,4, "Death");

		for (int i = 0; i < scores.length; i++){
			si.print(2,5+i, scores[i].getName(), BLUE);
			si.print(21,5+i, ""+scores[i].getPlayerClass());
			si.print(13,5+i, ""+scores[i].getScore());
			si.print(25,5+i, ""+scores[i].getDate());
			si.print(36,5+i, ""+scores[i].getTurns());
			si.print(43,5+i, (""+scores[i].getDeathString()+" on level "+scores[i].getDeathLevel()));

		}
		si.print(2,23, "[Press Space]");
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.cls();
	}
	
	public void showHelp() {
		si.cls();
		//printBars();
		si.print( 1,1,  "                              * - HELP - *                                      ", RED);
		
		si.print( 3,3, "("+CharKey.getString(Display.keyBindings.getProperty("WEAPON_KEY"))+")", RED);
		si.print( 3,4, "("+CharKey.getString(Display.keyBindings.getProperty("ATTACK1_KEY"))+")", RED);
		si.print( 3,5, "("+CharKey.getString(Display.keyBindings.getProperty("DROP_KEY"))+")", RED);
		si.print( 3,6, "("+CharKey.getString(Display.keyBindings.getProperty("EQUIP_KEY"))+")", RED);
		si.print( 3,7, "("+CharKey.getString(Display.keyBindings.getProperty("TARGET_KEY"))+")", RED);
		si.print( 3,8, "("+CharKey.getString(Display.keyBindings.getProperty("GET_KEY"))+")", RED);
		si.print( 3,9, "("+CharKey.getString(Display.keyBindings.getProperty("JUMP_KEY"))+")", RED);
		si.print( 3,10, "("+CharKey.getString(Display.keyBindings.getProperty("DIVE_KEY"))+")", RED);
		si.print( 3,11, "("+CharKey.getString(Display.keyBindings.getProperty("RELOAD_KEY"))+")", RED);
		si.print( 3,12, "("+CharKey.getString(Display.keyBindings.getProperty("SHOW_SKILLS_KEY"))+")", RED);
		si.print( 3,13, "("+CharKey.getString(Display.keyBindings.getProperty("THROW_KEY"))+")", RED);
		si.print( 3,14, "("+CharKey.getString(Display.keyBindings.getProperty("USE_KEY"))+")", RED);
		si.print( 3,15, "("+CharKey.getString(Display.keyBindings.getProperty("UNEQUIP_KEY"))+")", RED);
		si.print( 3,16, "("+CharKey.getString(Display.keyBindings.getProperty("SWITCH_WEAPONS_KEY"))+")", RED);
		
		si.print( 6,3,  "Action: Aim special weapon", WHITE);
		si.print( 6,4,  "Attack: Uses a weapon", WHITE);
		si.print( 6,5,  "Drop: Drops an item", WHITE);
		si.print( 6,6,  "Equip: Wears equipment", WHITE);
		si.print( 6,7,  "Fire: Aims a ranged weapon", WHITE);
		si.print( 6,8,  "Get: Picks up an item", WHITE);
		si.print( 6,9,  "Jump: Jumps in a direction", WHITE);
		si.print( 6,10, "Plunge: Dive into the water", WHITE);
		si.print( 6,11, "Reload: Reloads a given weapon", WHITE);
		si.print( 6,12, "Skills: Use character skills", WHITE);
		si.print( 6,13, "Throw: Throws an Item", WHITE);
		si.print( 6,14, "Use: Uses an Item", WHITE);
		si.print( 6,15, "Unequip: Take off an item", WHITE);
		si.print( 6,16, "Switch: Exchange primary weapon", WHITE);

		// TODO DECLARE LIST OF KEYBIND IDENTIFIERS for KEYMAP. based on these things like:
		// K_SHOW_STATS, K_SHOW_INVENTORY, K_LOOK, K_MSG_HISTORY,viewlog?, ...
		si.print( 41,3 , "("+CharKey.getString(Display.keyBindings.getProperty("SHOW_STATS_KEY"))+")", RED);
		si.print( 41,4 , "("+CharKey.getString(Display.keyBindings.getProperty("SHOW_INVENTORY_KEY"))+")", RED);
		si.print( 41,5 , "("+CharKey.getString(Display.keyBindings.getProperty("LOOK_KEY"))+")", RED);
		si.print( 41,6 , "("+CharKey.getString(Display.keyBindings.getProperty("SHOW_MESSAGE_HISTORY_KEY"))+")", RED);
		si.print( 41,7, "("+CharKey.getString(Display.keyBindings.getProperty("SHOW_MAP_KEY"))+")", RED);
		si.print( 41,8, "("+CharKey.getString(Display.keyBindings.getProperty("EXAMINE_LEVEL_MAP_KEY"))+")", RED);
		si.print( 41,9, "("+CharKey.getString(Display.keyBindings.getProperty("QUIT_KEY"))+")", RED);
		si.print( 41,10, "("+CharKey.getString(Display.keyBindings.getProperty("PROMPT_SAVE_KEY"))+")", RED);
		si.print( 41,11, "("+CharKey.getString(Display.keyBindings.getProperty("SWITCH_MUSIC_KEY"))+")", RED);
		
		si.print( 44,3, "Character: Skills and attributes", WHITE);
		si.print( 44,4, "Inventory: Shows the inventory", WHITE);
		si.print( 44,5, "Look: Identifies map symbols", WHITE);
		si.print( 44,6, "Messages: Shows the latest messages", WHITE);
		si.print( 44,7, "Castle Map: Shows the castle map", WHITE);
		si.print( 44,8, "Area Map: Show the current area map", WHITE);
		si.print( 44,9, "Quit: Exits game", WHITE);
		si.print( 44,10, "Save: Saves game", WHITE);
		si.print( 44,11, "Switch Music: Turns music on/off", WHITE);
		
		si.print( 6,18, "[ Press space to exit ]", WHITE);
		
/*
		si.print(0,1,  "                                                                                ");
		si.print(0,2,  "                                                                                ");
		si.print(0,3,  "                              * - HELP - *                                      ", RED);
		si.print(0,4,  "                                                                                ");
		si.print(0,5,  "                                                                                ");
		si.print(0,6,  "        -- ACTIONS --                             -- MOVEMENT --                ", RED);
		si.print(0,7,  "                                                                      7 8 9     ");
		si.print(0,8,  "  ( ) Action: Aim mystic or ranged weapon                              \\|/      ");
		si.print(0,9,  "  (a) Attack: Uses a weapon                     Arrow Keys or Numpad  4-@-6     ");
		si.print(0,10, "  (d) Drop: Drops an item                                              /|\\      ");
		si.print(0,11, "  (D) Dive: Dives into deep water                                     1 2 3     ");
		si.print(0,12, "                                                  -- COMMANDS --                ", RED);
		si.print(0,12, "  (e) Equip: Wears equipment               ");
		si.print(0,13, "  (f) Fire: Aims a ranged weapon                                                ");
		si.print(0,14, "  (g) Get: Picks up an item                (c) Character info: Player attributes");
		si.print(0,15, "  (j) Jump: Jumps, emerge, fly             (i) Inventory: Shows the inventory   ");
		si.print(0,16, "  (r) Reload: Reloads a given weapon       (l) Look: Identifies map symbols 	");
		si.print(0,17, "  (s) Skills: Use character skills         (m) Messages: Latest messages        ");
		si.print(0,18, "  (t) Throw: Throws an Item                (M) Map: Shows the castle map        ");
		si.print(0,19, "  (u) Use: Uses an Item                    (Q) Quit: Exits game                 ");
		si.print(0,19, "  (U) Unequip: Take off an item            (S) Save: Saves game                 ");
		si.print(0,20, "  (x) Switch: Primary for secondary weapon (T) Switch music: Turns on/off music ");
		si.print(0,21, "                                                                                ");
		si.print(0,22, "                                                                                ");
		si.print(0,23, "                                            [ Press Space to Continue ]         ", RED);
		si.print(0,24, "                                                                                ");
*/
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}
	
	public void init(ConsoleSystemInterface syst){
		si = syst;
	}
	
	public void printBars() {
 		si.print(0,0,  "[==============================================================================]", WHITE);
		si.print(0,1,  "  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]", WHITE);
		
		si.print(0,23, "  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]  [==]", WHITE);
		si.print(0,24, "[==============================================================================]", WHITE);
	}
	
	public void showDraculaSequence(){
		TextBox tb = new TextBox(si);
		tb.setBounds(3,4,40,10);
		tb.setBorder(true);
		tb.setText("Ahhh... a human... the first one to get this far. HAHAHAHA! Now it is time to die!");
		tb.draw();
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}
	
	public void showBoxedMessage(String title, String msg, int x, int y, int w, int h){
		TextBox tb = new TextBox(si);
		tb.setBounds(x,y,w,h);
		tb.setBorder(true);
		tb.setText(msg);
		tb.setTitle(title);
		tb.draw();
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}
	
	
	public void showTimeChange(boolean day, boolean fog, boolean rain, boolean thunderstorm, boolean sunnyDay) {
		String dayMsg = Text.getTimeChangeMessage(day, fog, rain, thunderstorm, sunnyDay);
		// blocking/modal msgbox function! No reason for Display to have 'timechange' string-get function.
		showTextBox(dayMsg, 3, 4, 30, 10);	//Modaltextbox...?
	}
	

	public void showTextBox(String msg, int x, int y, int w, int h) {	// "ModalTextBox"?
		TextBox tb = new TextBox(si);
		tb.setBounds(x, y, w, h);
		tb.setBorder(true);
		tb.setText(msg);
		tb.draw();
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}
	
	
	public int showSavedGames(File[] saveFiles) {
		si.cls();
		printBars();
		if (saveFiles == null || saveFiles.length == 0){
			si.print(3,6, "No adventurers available");
			si.print(4,8, "[Space to Cancel]");
			si.refresh();
			si.waitKey(CharKey.SPACE);
			return -1;
		}
			
		si.print(3,6, "Pick an adventurer");
		for (int i = 0; i < saveFiles.length; i++){
			String saveFileName = saveFiles[i].getName();
			si.print(5,7+i, (char)(CharKey.a+i+1)+ " - "+ saveFileName.substring(0,saveFileName.indexOf(".sav")));
		}
		si.print(3,9+saveFiles.length, "[Space to Cancel]");
		si.refresh();
		CharKey x = si.inkey();
		while ((x.code < CharKey.a || x.code > CharKey.a+saveFiles.length) && x.code != CharKey.SPACE){
			x = si.inkey();
		}
		si.cls();
		if (x.code == CharKey.SPACE)
			return -1;
		else
			return x.code - CharKey.a;
	}
	
	public void showHostageRescue(Hostage h) {
		TextBox tb = new TextBox(si);
		tb.setBounds(3,4,30,10);
		tb.setBorder(true);
		
		String text = "Thanks for rescuing me! I will give you "+h.reward+" gold, it is all I have!";
		if (h.itemReward != null) {
			text += "\n\n\nTake this, the "+h.itemReward.getDescription()+", I found it inside the castle ";
		}
		tb.setText(text);
		tb.draw();
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}
	
	
	public Advancement showLevelUp(Vector<Advancement> advancements) {
		si.saveBuffer();
		si.cls();
		si.print(1,1, "You have gained a chance to pick an advancement!", BLUE);
		
		for (int i = 0; i < advancements.size(); i++) {
			si.print(1,3+i*2, ((char)('a'+i))+". "+(advancements.elementAt(i)).getName());
			si.print(1,4+i*2, (advancements.elementAt(i)).getDescription());
		}
		si.refresh();
		int choice = readAlphaToNumber(advancements.size());
		si.restore();
		return advancements.elementAt(choice);
		/*
		ItemDefinition[] defs = new ItemDefinition[soulIds.size()];
		for (int i = 0; i < defs.length; i++){
			defs[i] = ItemFactory.getItemFactory().getDefinition((String)soulIds.elementAt(i));
		}
		si.cls();
		printBars();
    
		si.print(2,3, " - - Level Up - -", RED);
		si.print(2,5,  "Please pick a spiritual memento");
		
		for (int i = 0; i < defs.length; i++){
			si.print(2,7+i, (char)('a'+i) + ") ", WHITE);
			si.print(5,7+i,  ((CharAppearance)defs[i].getAppearance()).getChar(), ((CharAppearance)defs[i].getAppearance()).getColor());
			si.print(7,7+i,  defs[i].getDescription() + ": " + defs[i].getMenuDescription());
		} 
		si.refresh();
		
		int choice = readAlphaToNumber(defs.length);
		si.cls();
		return choice;*/
	}
	
	private int readAlphaToNumber(int numbers) {
		while (true){
			CharKey key = si.inkey();
			if (key.code >= CharKey.A && key.code <= CharKey.A + numbers -1){
				return key.code - CharKey.A;
			}
			if (key.code >= CharKey.a && key.code <= CharKey.a + numbers -1){
				return key.code - CharKey.a;
			}
		}
	}
	
	public void showChat(String chatID, Game game) {
		si.saveBuffer();
		Chat chat = CutsceneDialogue.get(chatID);
		TextBox tb = new TextBox(si);
		tb.setBounds(3,4,40,10);
		tb.setBorder(true);
		String[] marks = new String[] {"%NAME", "%%INTRO_1", "%%CLARA1"};
		String[] replacements = new String[] {game.getPlayer().getName(), game.getPlayer().getCustomMessage("INTRO_1"), game.getPlayer().getCustomMessage("CLARA1")};
		for (int i = 0; i < chat.getConversations(); i++){
			tb.clear();
			tb.setText(TxtTpl.replace(marks, replacements, chat.getConversation(i)));
			tb.setTitle(TxtTpl.replace(marks, replacements, chat.getName(i)));
			tb.draw();
			si.refresh();
			si.waitKey(CharKey.SPACE);
		}
		si.restore();
	}
	
	public void showScreen(Object pScreen){
		si.saveBuffer();
		String screenText = (String) pScreen;
		TextBox tb = new TextBox(si);
		tb.setBounds(3,4,50,18);
		tb.setBorder(true);
		tb.setText(screenText);
		tb.draw();
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
	}
	
	private Hashtable<String, Position> locationKeys;
	{
		locationKeys = new Hashtable<>();
		locationKeys.put("TOWN", new Position(15,15));
		locationKeys.put("FOREST", new Position(23,15));
		locationKeys.put("BRIDGE", new Position(30,15));
		locationKeys.put("ENTRANCE", new Position(36,15));
		locationKeys.put("HALL", new Position(41,15));
		locationKeys.put("LAB", new Position(39,12));
		locationKeys.put("CHAPEL", new Position(37,9));
		locationKeys.put("RUINS", new Position(45,10));
		locationKeys.put("CAVES", new Position(46,18));
		locationKeys.put("VILLA", new Position(52,17));
		locationKeys.put("COURTYARD", new Position(52,17));
		locationKeys.put("DUNGEON", new Position(60,18));
		locationKeys.put("STORAGE", new Position(63,12));
		locationKeys.put("CLOCKTOWER", new Position(62,7));
		locationKeys.put("KEEP", new Position(52,5));
	}
	
	String[] mapImage = {
		"                                                                                ",
		" ''`.--..,''`_,''`.-''----.----..'     '`''''..,'''-'    `_,,'''`--- ./  ,'-.   ",
		" '                                                 /\\                   |,. `.  ",
		"  |                                               /  \\                    `.... ",
		"  |                                              | /-\\|      /'\\              | ",
		"  |                                               \\| |\\    .'   |             | ",
		"  |                            O    /\\             \\-/ \\   . /-\\`             | ",
		"  |                                |  |            `.===``/==| | \\           ,  ",
		" .\"                                |/-\\_              `===== \\-/  `.          \\ ",
		" |                                 `| |==.../-\\       .'      =    |          | ",
		" |                                 /\\-/ ====| ||  ,-.'.       ==   /         /  ",
		" \\.                                | =/-\\   \\-/| |   | '|     /-\\ /           \\ ",
		"  |                               .' =| |=   = | |   '..'     | | |           / ",
		"  |            ,-.     ..--.      |.  \\-/=   = |  |           \\-/  \\         |  ",
		"  |          _/-\\|.  ,/-\\  `./-\\   /-\\  /-\\  =  \\ |            =   |.        <. ",
		"  :|      ,,' | |=====| |====| |===| |==| |  ==  \\ `'\\.        =    `.        | ",
		"  |    _,'    \\-/     \\-/    \\-/   \\-/  \\-/   =   |/-\\|        =     |        | ",
		"   |.-'                                      /-\\ ==| |===  /-\\ =     `.      |  ",
		" .-' ''`''-.    ,'`..,''''''`.               | |== \\-/  ===| |==       ``.   `. ",
		" |.         ---'             `._,..,_        \\-/  ,..      \\-/   _  __   ___. | ",
		"  '                                  `.     .-''''   \\.   _....'' `'  \\''    ./ ",
		"  |                                    `_,.-          '`--'                  `. ",
		"  | ..           _                      .                                     | ",
		" |../'....,'-.../ ``...,''`--....''`..,' `\"-..,''`....,`...-'..''......'`...,-' ",
		"                                                                                "
	};

	
	public void showMap(String locationKey, String locationDescription) {
		si.saveBuffer();
		for (int i = 0; i < 25; i++){
			si.print(0,i, mapImage[i], BROWN);
		}
		si.print(15, 11, locationDescription);
		if (locationKey != null){
			Position location = (Position) locationKeys.get(locationKey);
			if (location != null)
				si.print(location.x, location.y, "X", RED);
		}
		si.waitKey(CharKey.SPACE);
		si.restore();
	}
	
	public void showMonsterScreen(Monster who, Player player) {
		CharAppearance app = (CharAppearance)who.getAppearance();
		si.cls();
		si.print(6,3, who.getDescription(), RED);
		si.print(4,3,app.getChar(),app.getColor());
		
		TextBox tb = new TextBox(si);
		tb.setPosition(3,5);
		tb.setHeight(8);
		tb.setWidth(70);
		tb.setForeColor(WHITE);
		if (who.getLongDescription() != null)
			tb.setText(who.getLongDescription());
		tb.draw();
		
		MonsterRecord record = Main.getMonsterRecordFor(who.getID());
		long baseKilled = 0;
		long baseKillers = 0;
		if (record != null){
			baseKilled = record.getKilled();
			baseKillers = record.getKillers();
		}
		si.print(2,17, "You have killed "+(baseKilled+player.getGameSessionInfo().getDeathCountFor(who))+" "+who.getDescription()+"s",WHITE);
		if (baseKillers == 0){
			si.print(2,18, "No "+who.getDescription()+"s have killed you",WHITE);
		} else {
			si.print(2,18, "You have been killed by "+baseKillers+" "+who.getDescription()+"s",WHITE);
		}
		si.print(2,20, "[Press Space]", WHITE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
		si.refresh();
	}
}
