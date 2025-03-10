package crl.ui.graphicsUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JTextArea;

import sz.csi.CharKey;
import sz.util.ImageUtils;
import sz.util.Position;
import sz.util.PropertyFilters;
import sz.util.TxtTpl;

import crl.Main;
import crl.player.GameSessionInfo;
import crl.player.HiScore;
import crl.player.Player;
import crl.player.advancements.Advancement;
import crl.ui.Display;
import crl.ui.graphicsUI.components.GFXChatBox;
import crl.monster.Monster;
import crl.npc.*;
import crl.conf.gfx.data.GFXConfiguration;
import crl.conf.gfx.data.Textures;
import crl.cuts.Chat;
import crl.cuts.CutsceneDialogue;
import crl.data.Text;
import crl.game.Game;
import crl.game.MonsterRecord;

public class GFXDisplay extends Display {
	
	private SwingSystemInterface si;
	
	private String
		IMG_TITLE,
		IMG_PROLOGUE,
		IMG_RESUME,
		IMG_ENDGAME,
		IMG_HISCORES,
		//IMG_HELP;
		IMG_SAVED,
		IMG_LEVEL_UP;
	
	public static String
		IMG_FRAME;
	
	public static Font
		FNT_TEXT,
		FNT_PROLOGUE,
		FNT_TITLE,
		FNT_DIALOGUEIN,
		FNT_MONO;
	
	private BufferedImage
		IMG_MAP,
		IMG_MAPMARKER,
		IMG_PICKER,
		IMG_BORDERS;
	
	public static Color
		COLOR_BOLD;
	
	// global ref, from main? store all loaded fonts in global struct?
	protected GFXConfiguration conf;
	
	private void initProperties(Properties p) {
		IMG_TITLE = p.getProperty("IMG_TITLE");
		IMG_PROLOGUE = p.getProperty("IMG_PROLOGUE");
		IMG_RESUME = p.getProperty("IMG_RESUME");
		IMG_ENDGAME = p.getProperty("IMG_ENDGAME");
		IMG_HISCORES = p.getProperty("IMG_HISCORES");
		
		IMG_SAVED = p.getProperty("IMG_SAVED");
		IMG_LEVEL_UP = p.getProperty("IMG_LEVEL_UP");
		IMG_FRAME = p.getProperty("IMG_FRAME");
		COLOR_BOLD = PropertyFilters.getColor(p.getProperty("COLOR_BOLD"));
		
		try {
			IMG_MAP = ImageUtils.createImage(p.getProperty("IMG_MAP"));
			IMG_MAPMARKER = PropertyFilters.getImage(p.getProperty("IMG_MAPMARKER"), p.getProperty("IMG_MAPMARKER_BOUNDS"));
			IMG_PICKER = PropertyFilters.getImage(p.getProperty("IMG_PICKER"), p.getProperty("IMG_PICKER_BOUNDS"));
			IMG_BORDERS = PropertyFilters.getImage(p.getProperty("IMG_BORDERS"), p.getProperty("IMG_BORDERS_BOUNDS"));
			
			FNT_TITLE = PropertyFilters.loadFont(p.getProperty("FNT_TITLE"), p.getProperty("FNT_TITLE_SIZE"));
			FNT_TEXT = PropertyFilters.loadFont(p.getProperty("FNT_TEXT"), p.getProperty("FNT_TEXT_SIZE"));
			FNT_PROLOGUE = PropertyFilters.loadFont(p.getProperty("FNT_PROLOGUE"), p.getProperty("FNT_PROLOGUE_SIZE"));
			FNT_DIALOGUEIN = FNT_TEXT;
			FNT_MONO = PropertyFilters.loadFont(p.getProperty("FNT_MONO"), p.getProperty("FNT_MONO_SIZE"));
		} catch (FontFormatException ffe) {
			Game.crash("Error loading the font", ffe);
		} catch (IOException ioe) {
			Game.crash("Error loading the font", ioe);
		} catch (Exception e) {
			Game.crash("Error loading images", e);
		}
	}
	
	private AdornedBorderTextArea adornedTextArea;

	private GFXChatBox gfxChatBox;
	
	public GFXDisplay(SwingSystemInterface si, Properties p, GFXConfiguration configuration) {
		conf = configuration;
		initProperties(p);
		this.si = si;
		try {
			//BufferedImage BORDERS = ImageUtils.createImage(IMG_BORDERS);
			BufferedImage b1 = ImageUtils.crearImagen(IMG_BORDERS, 34,1,32,32);
			BufferedImage b2 = ImageUtils.crearImagen(IMG_BORDERS, 1,1,32,32);
			BufferedImage b3 = ImageUtils.crearImagen(IMG_BORDERS, 100, 1, 32,32);
			BufferedImage b4 = ImageUtils.crearImagen(IMG_BORDERS, 67,1,32,32);
			adornedTextArea = new AdornedBorderTextArea(
					b1,
					b2,
					b3,
					b4,
					new Color(187,161,80),
					new Color(92,78,36),
					32, 32);
			adornedTextArea.setVisible(false);
			adornedTextArea.setEnabled(false);
			adornedTextArea.setForeground(Color.WHITE);
			adornedTextArea.setBackground(Color.BLACK);
			adornedTextArea.setFont(FNT_DIALOGUEIN);
			adornedTextArea.setOpaque(false);
			
			gfxChatBox = new GFXChatBox(b1,b2,b3,b4,
					new Color(187,161,80),
					new Color(92,78,36),
					32, 32);
			
			gfxChatBox.setBounds(50,20,700,220);
			gfxChatBox.setVisible(false);
		} catch (Exception e) {
			Game.crash("Error loading UI data", e);
		}
		si.add(adornedTextArea);
		si.add(gfxChatBox);
	}
	
	
	public int showTitleScreen() {
		double scale = conf.screenScale;
		int centreX = conf.screenWidth / 2;
		int pickerX = (conf.screenWidth / 2) - (IMG_PICKER.getWidth() / 2);
		GFXUserInterface gui = (GFXUserInterface)Main.ui;
		gui.messageBox.setVisible(false);
		gui.persistantMessageBox.setVisible(false);
		si.setFont(FNT_TEXT);
		si.drawImage(IMG_TITLE);

		si.printAtPixelCentered(centreX, (int)(530*scale), Text.TITLE_DISCLAIMER, GFXDisplay.COLOR_BOLD);
		si.printAtPixelCentered(centreX, (int)(555*scale), Text.TITLE_GAME_VER_DEV, Color.WHITE);
		si.printAtPixelCentered(centreX, (int)(570*scale), Text.TITLE_ARTCREDIT, Color.WHITE);
		si.printAtPixelCentered(centreX, (int)(585*scale), Text.TITLE_MUSICCREDIT, Color.WHITE);
		CharKey x = new CharKey(CharKey.NONE);
		int choice = 0;
		si.saveBuffer();
		final int ROWHEIGHT = 20;
		while (true) {
			si.restore();
			
			si.drawImage(pickerX, (int)((356+choice*ROWHEIGHT)*scale), IMG_PICKER);	// TitleMenu-Selection-Bar.
			
			// 356 is? ..the text is drawn at 368 - so, +12? 12 being... leading/ascent?
			// then 356 is just hardcoded menu y position?
			// TODO: debug outlines and mouse picking logic also.
			int tmenuTxtY = 356+12;	// 368. thereafter add rowheights. and: *scale... why's that?
			for (String tItem: Text.TITLE_MENU) {
				si.printAtPixelCentered(centreX,(int)(tmenuTxtY*scale), tItem, Color.WHITE);
				tmenuTxtY += ROWHEIGHT;
			}
			si.refresh();
			
			while (	x.code != CharKey.A && x.code != CharKey.a &&
					x.code != CharKey.B && x.code != CharKey.b &&
					x.code != CharKey.C && x.code != CharKey.c &&
					x.code != CharKey.D && x.code != CharKey.d &&
					x.code != CharKey.E && x.code != CharKey.e &&
					x.code != CharKey.G && x.code != CharKey.g &&
					x.code != CharKey.F && x.code != CharKey.f &&
					x.code != CharKey.UARROW && x.code != CharKey.DARROW &&
					x.code != CharKey.SPACE && x.code != CharKey.ENTER)
			{
				x = si.inkey();
			}
			switch (x.code) {
			case CharKey.A:
			case CharKey.a:
				return 0;
			case CharKey.B:
			case CharKey.b:
				return 1;
			case CharKey.C:
			case CharKey.c:
				return 2;
			case CharKey.D:
			case CharKey.d:
				return 3;
			case CharKey.E: 
			case CharKey.e:
				return 4;
			case CharKey.F:
			case CharKey.f:
				return 5;
			case CharKey.G:
			case CharKey.g:
				return 6;
			case CharKey.UARROW:
				if (choice > 0) {
					choice--;
				} else if (choice == 0) {
					choice = Text.TITLE_MENU.length-1;
				}
				break;
			case CharKey.DARROW:
				if (choice < Text.TITLE_MENU.length-1) {
					choice++;
				} else if (choice == Text.TITLE_MENU.length-1) {
					choice = 0;	// wrap
				}
				break;
			case CharKey.SPACE:
			case CharKey.ENTER:
				return choice;	// Only if choice active/enabled (not disabled?)
			}
			x.code = CharKey.NONE;
		}
	}
	
	public void showIntro(Player player) {
		si.drawImage(IMG_PROLOGUE);
		si.setFont(FNT_TITLE);
		si.printAtPixel(156,136, "prologue", Color.WHITE);
		//si.drawImage(311,64, IMG_GBAT);
		si.setFont(FNT_TEXT);
		si.setColor(Color.GRAY);
		JTextArea t1 = createTempArea(150, 170, conf.screenWidth - 300, 400);
		t1.setFont(FNT_PROLOGUE);
		t1.setForeground(Color.WHITE);
		t1.setText(
		Text.PROLOGUE_LINE0 + "\n\n" + Text.PROLOGUE_LINE1 + "\n\n" +
		/*
		"In the year of 1691, a dark castle emerges from the cursed soils of the plains of Transylvannia. "+
		"Chaos and death spread along the land, as the evil count Dracula unleases his powers, "+ 
		"turning its forests and lakes into a pool of blood. \n\n"+
		"The trip to the castle was long and harsh, after enduring many challenges through all Transylvannia, "+
		"you are close to the castle of chaos. You are almost at Castlevania, and you are here on business: " + 
		"To destroy forever the Curse of the Evil Count.\n\n"+
		*/
		player.getPlot()+", "+player.getDescription()+" stands on a forest near the town of Petra and the cursed castle; "+
		player.getPlot2() +" and the fate running through his veins being the sole hope for mankind.");
		
		si.add(t1);
		si.printAtPixel(156,590, "[Space] to continue", COLOR_BOLD);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.remove(t1);
	}
	
	public boolean showResumeScreen(Player player) {
		((GFXUserInterface)Main.ui).messageBox.setVisible(false);
		si.drawImage(IMG_RESUME);
		
		GameSessionInfo gsi = player.getGameSessionInfo();
		String heshe = (player.sex == Player.MALE ? "He" : "She");
		
		si.setFont(FNT_TITLE);
		si.print(2,3, "The chronicles of "+player.getName(), COLOR_BOLD);
		JTextArea t1 = createTempArea(20,125,700,120);
		t1.setForeground(Color.WHITE);
		
		// TODO use TxtTpl.t replacement here, perhaps? More keywords?
		t1.setText("  ...And so it was that "+player.getDescription() + ", "+gsi.getDeathString()+" on the "+player.level.getDescription()+"...\n\n"+
				heshe+ " scored "+ player.score +" points and earned "+ player.getGold() +" gold \n\n"+
				heshe + " survived for "+gsi.turns+" turns \n\n"+
				heshe + " took "+gsi.getTotalDeathCount()+" monsters to the other world");
		si.add(t1);
/*
		int i = 0;
		Enumeration monsters = gsi.getDeathCount().elements();
		while (monsters.hasMoreElements()){
			MonsterDeath mons = (MonsterDeath) monsters.nextElement();
			si.print(5,11+i, mons.getTimes() +" "+mons.getMonsterDescription(), ConsoleSystemInterface.RED);
			i++;
		}
		
*/		si.setFont(FNT_TEXT);
		si.print(2,14, "Do you want to save your character memorial? [Y/N]", Color.WHITE);
		si.refresh();
		boolean ret = Main.ui.prompt();
		si.remove(t1);
		return ret;
	}

	public void showEndgame(Player player){
		((GFXUserInterface)Main.ui).messageBox.setVisible(false);
		si.drawImage(IMG_ENDGAME);
		si.setFont(FNT_TITLE);
		si.printAtPixel(156,136, "Epilogue", Color.WHITE);
		String heshe = (player.sex == Player.MALE ? "he" : "she");
		//String hisher = (player.getSex() == Player.MALE ? "his" : "her");
		JTextArea t1 = createTempArea(150,170,conf.screenWidth - 300, 400);
		
		t1.setForeground(Color.WHITE);
		t1.setText(player.getName()+ " made many sacrifices, but now the long fight is over. Dracula is dead "+
				"and all other spirits are asleep. In the shadows, a person watches the castle fall. "+
				player.getName()+" must go for now but "+heshe+" hopes someday "+heshe+" will get the "+
				"respect that "+heshe+" deserves.    After this fight the new Belmont name shall be honored "
				+"by all people. \n\n\n"+
				"You played the greatest role in this history... \n\n"+
				"Thank you for playing.\n\n\n"+
				"CastlevaniaRL: v"+Game.getVersion());
		si.add(t1);
		si.setFont(FNT_TEXT);
		si.print(2,20, "[Press Space]",Color.WHITE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.remove(t1);

	}
	
	public void showHiscores(HiScore[] scores) {
		int leftMargin;
		int widthAdjustment;
		
		if (conf.screenWidth == 1024) {	// FIXME Hardcoded, nonproportional and bad!
			leftMargin = 9;
			widthAdjustment = 112;
		} else {
			leftMargin = 0;
			widthAdjustment = 0;
		}
		si.drawImage(IMG_HISCORES);
		adornedTextArea.setBounds(8, 110, 782, 395);
		adornedTextArea.paintAt(si.getGraphics2D(), 8 + widthAdjustment,110);
		si.setFont(FNT_TITLE);
		si.printAtPixelCentered(conf.screenWidth / 2, 160, "The most brave of Belmonts", Color.WHITE);
		si.setFont(FNT_TEXT);
		si.print(18 + leftMargin,8, "SCORE", GFXDisplay.COLOR_BOLD);
		si.print(25 + leftMargin,8, "DATE", GFXDisplay.COLOR_BOLD);	
		si.print(36 + leftMargin,8, "TURNS", GFXDisplay.COLOR_BOLD);
		si.print(43 + leftMargin,8, "DEATH", GFXDisplay.COLOR_BOLD);

		for (int i = 0; i < scores.length; i++){
			si.print(7 + leftMargin,(9+i), scores[i].getName()+" ("+scores[i].getPlayerClass()+")", Color.WHITE);
			si.print(18 + leftMargin,(9+i), ""+scores[i].getScore(), Color.GRAY);
			si.print(25 + leftMargin,(9+i), ""+scores[i].getDate(), Color.GRAY);
			si.print(36 + leftMargin,(9+i), ""+scores[i].getTurns(), Color.GRAY);
			si.print(43 + leftMargin,(9+i), ""+scores[i].getDeathString()+" on level "+scores[i].getDeathLevel(), Color.GRAY);

		}
		si.print(7 + leftMargin,20, "[space] to continue", GFXDisplay.COLOR_BOLD);
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}
	
	
	public void showHelp() {
		// Delegated to GFXUserInterface.HelpBox
	}
	
	public void init(SwingSystemInterface syst) {
		si = syst;
	}
	
	public void showDraculaSequence() {
		showTextBox("Ahhh... a human... the first one to get this far. HAHAHAHA! Now it is time to die!",3,4,40,10);
	}
	
	
	public void showTimeChange(boolean day, boolean fog, boolean rain, boolean thunderstorm, boolean sunnyDay) {
		String timeMsg = Text.getTimeChangeMessage(day, fog, rain, thunderstorm, sunnyDay);
		showTextBox(timeMsg, 40,60,300,200);	// hardcoded coords. why these?
	}
	
	
	public int showSavedGames(File[] saveFiles) {
		si.drawImage(IMG_SAVED);
		if (saveFiles == null || saveFiles.length == 0){
			si.print(3,6, "No adventurers available",Color.WHITE);
			si.print(4,8, "[Space to Cancel]",Color.WHITE);
			si.refresh();
			si.waitKey(CharKey.SPACE);
			return -1;
		}
			
		si.print(3,6, "Pick an adventurer",Color.WHITE);
		for (int i = 0; i < saveFiles.length; i++){
			String saveFileName = saveFiles[i].getName();
			si.print(5,7+i, (char)(CharKey.a+i+1)+ " - "+ saveFileName.substring(0,saveFileName.indexOf(".sav")), COLOR_BOLD);
		}
		si.print(3,9+saveFiles.length, "[Space to Cancel]", Color.WHITE);
		si.refresh();
		CharKey x = si.inkey();
		while ((x.code < CharKey.a || x.code > CharKey.a+saveFiles.length-1) && x.code != CharKey.SPACE){
			x = si.inkey();
		}
		if (x.code == CharKey.SPACE)
			return -1;
		else
			return x.code - CharKey.a;
	}
	
	public void showHostageRescue(Hostage h) {
		String text = "Thanks for rescuing me! I will give you "+h.reward+" gold, it is all I have!";
		if (h.itemReward != null) {
			text += "\n Take this, the "+h.itemReward.getDescription()+", I found it inside the castle ";
		}
		showTextBox(text,30,40,300,300);
	}
	
	public Advancement showLevelUp(Vector<Advancement> advancements) {
		GFXUserInterface gui = (GFXUserInterface)Main.ui;
		gui.messageBox.setVisible(false);
		
		si.saveBuffer();
		si.drawImage(IMG_LEVEL_UP);
		si.print(4,3, "You have gained a chance to pick an advancement!", GFXDisplay.COLOR_BOLD);
		for (int i = 0; i < advancements.size(); i++){
			si.print(3,4+i*2, ((char)('a'+i))+". "+((Advancement)advancements.elementAt(i)).getName(), GFXDisplay.COLOR_BOLD);
			si.print(3,5+i*2, "   "+((Advancement)advancements.elementAt(i)).getDescription(), Color.WHITE);
		}
		si.refresh();
		int choice = readAlphaToNumber(advancements.size());
		si.restore();
		si.refresh();
		gui.messageBox.setVisible(true);
		return (Advancement)advancements.elementAt(choice);
		
		/*si.saveBuffer();
		ItemDefinition[] defs = new ItemDefinition[soulIds.size()];
		for (int i = 0; i < defs.length; i++){
			defs[i] = ItemFactory.getItemFactory().getDefinition((String)soulIds.elementAt(i));
		}
		si.drawImage(IMG_LEVEL_UP);
		si.print(2,5,  "Please pick a spiritual memento", COLOR_BOLD);
		
		for (int i = 0; i < defs.length; i++){
			si.print(2,7+i, (char)('a'+i) + ") ", COLOR_BOLD);
			si.drawImage(5*10,(6+i)*24+10,  ((GFXAppearance)defs[i].getAppearance()).getImage());
			si.print(7,7+i,  defs[i].getDescription() + ": " + defs[i].getMenuDescription(), COLOR_BOLD);
		} 
		si.refresh();
		
		int choice = readAlphaToNumber(defs.length);
		si.restore();
		si.refresh();
		return choice;*/
	}
	
	// aka showModalTextBox (i.e. await space key to confirm). TODO: And Escape.
	
	public void showTextBox(String text, int consoleX, int consoleY, int consoleW, int consoleH) {
		adornedTextArea.setBounds(consoleX, consoleY, consoleW, consoleH);
		adornedTextArea.setText(text);
		adornedTextArea.setVisible(true);
		si.waitKey(CharKey.SPACE);
		adornedTextArea.setVisible(false);
	}
	
	public void showTextBox(String title, String text, int consoleX, int consoleY, int consoleW, int consoleH) {
		showTextBox(title+" "+text, consoleX, consoleY, consoleW, consoleH);
	}
	
	public void showTextBoxNoWait(String text, int consoleX, int consoleY, int consoleW, int consoleH) {
		adornedTextArea.setBounds(consoleX, consoleY, consoleW, consoleH);
		adornedTextArea.setText(text);
		adornedTextArea.setVisible(true);
	}
	
	public void clearTextBox() {
		adornedTextArea.setVisible(false);
	}
	
	public boolean showTextBoxPrompt(String text, int consoleX, int consoleY, int consoleW, int consoleH) {
		adornedTextArea.setBounds(consoleX, consoleY, consoleW, consoleH);
		adornedTextArea.setText(text);
		adornedTextArea.setVisible(true);
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code != CharKey.Y && x.code != CharKey.y && x.code != CharKey.N && x.code != CharKey.n)
			x = si.inkey();
		boolean ret = (x.code == CharKey.Y || x.code == CharKey.y);
		adornedTextArea.setVisible(false);
		return ret;
	}
	
	public void showTextBox(String text, int consoleX, int consoleY, int consoleW, int consoleH, Font f){
		adornedTextArea.setBounds(consoleX, consoleY, consoleW, consoleH);
		adornedTextArea.setText(text);
		adornedTextArea.setFont(f);
		adornedTextArea.setVisible(true);
		si.waitKey(CharKey.SPACE);
		adornedTextArea.setVisible(false);
	}

	private int readAlphaToNumber(int numbers) {
		while (true) {
			CharKey key = si.inkey();
			if (key.code >= CharKey.A && key.code <= CharKey.A + numbers -1) {
				return key.code - CharKey.A;
			}
			if (key.code >= CharKey.a && key.code <= CharKey.a + numbers -1) {
				return key.code - CharKey.a;
			}
		}
	}
	
	private static BufferedImage chatPortrait(Chat chat, int i) {
		int prtIndex = chat.getPortraitIndex(i);
		if (prtIndex == Textures.PRT_NONE) {	// no portrait auto shows player portrait..!?
			return null;
		}
		return Textures.portraits[prtIndex];
	}
	
	//private Color TRANSPARENT_BLUE = new Color(100,100,100,200);
	
	private static BufferedImage getPortraitForPlayer(Player p) {
		int male0 = Textures.PRT_M1;
		if (p.sex == Player.MALE) {
			int idx = male0 + p.playerClass;
			return Textures.portraits[idx];
		} else {
			int female0 = Textures.PRT_F1;
			int idx = female0 + p.playerClass;
			return Textures.portraits[idx];
		}
	}
	
	public void showChat(String chatID, Game game) {
		si.saveBuffer();
		Chat chat = CutsceneDialogue.get(chatID);
		String[] marks = {"%NAME", "%%INTRO_1", "%%CLARA1"};
		String[] replacements = {
			game.getPlayer().getName(),
			game.getPlayer().getCustomMessage("INTRO_1"),
			game.getPlayer().getCustomMessage("CLARA1")
		};
		BufferedImage image = null;
		for (int i = 0; i < chat.getConversations(); i++) {
			si.restore();
			//si.setColor(TRANSPARENT_BLUE);
			//si.getGraphics2D().fillRect(26,26,665,185);
			image = chatPortrait(chat, i);
			if (image == null) {
				image = getPortraitForPlayer(game.getPlayer());
			}
			gfxChatBox.set(image, TxtTpl.replace(marks, replacements, chat.getName(i)), TxtTpl.replace(marks, replacements, chat.getConversation(i)));
			gfxChatBox.setVisible(true);
			si.waitKey(CharKey.SPACE);
		}
		gfxChatBox.setVisible(false);
		si.restore();
	}
	
	public void showScreen(Object pScreen) {
		si.saveBuffer();
		String screenText = (String) pScreen;
		showTextBox(screenText, conf.screenWidth - 370, 70,340,375);
		//si.waitKey(CharKey.SPACE);
		si.restore();
	}

	private Hashtable<String, Position> locationKeys;
	{
		locationKeys = new Hashtable<String, Position>();
		locationKeys.put("TOWN", new Position(130,206));
		locationKeys.put("FOREST", new Position(201,206));
		locationKeys.put("BRIDGE", new Position(273,206));
		locationKeys.put("ENTRANCE", new Position(316,206));
		locationKeys.put("HALL", new Position(348,206));
		locationKeys.put("LAB", new Position(316,171));
		locationKeys.put("CHAPEL", new Position(316,139));
		locationKeys.put("RUINS", new Position(383,172));
		locationKeys.put("CAVES", new Position(383,261));
		locationKeys.put("COURTYARD", new Position(448,232));
		locationKeys.put("VILLA", new Position(448,232));
		locationKeys.put("DUNGEON", new Position(512,261));
		locationKeys.put("STORAGE", new Position(555,206));
		locationKeys.put("CLOCKTOWER", new Position(555,119));
		locationKeys.put("KEEP", new Position(462,69));
	}
	
	public void showMap(String locationKey, String locationDescription) {
		si.saveBuffer();
		int mapX = conf.screenWidth / 2 - 696 /2;
		int mapY = 100;
		si.drawImage(mapX,mapY, IMG_MAP);
		si.printAtPixel(mapX + 130, mapY + 150, locationDescription, Color.BLACK);
		if (locationKey != null){
			Position location = (Position) locationKeys.get(locationKey);
			if (location != null)
				si.drawImage(location.x + mapX + 3, location.y + mapY + 3, IMG_MAPMARKER);
		}
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
		si.refresh();
	}

	
	public static JTextArea createTempArea(int xpos, int ypos, int w, int h) {
		JTextArea ret = new JTextArea();
		ret.setOpaque(false);
		ret.setForeground(Color.WHITE);
		ret.setVisible(true);
		ret.setEditable(false);
		ret.setFocusable(false);
		ret.setBounds(xpos, ypos, w, h);
		ret.setLineWrap(true);
		ret.setWrapStyleWord(true);
		ret.setFont(GFXDisplay.FNT_TEXT);
		return ret;
	}
	
	public void showMonsterScreen(Monster who, Player player) {
		GFXUserInterface gui = (GFXUserInterface)Main.ui;
		gui.messageBox.setVisible(false);
		GFXAppearance app = (GFXAppearance)who.getAppearance();
		//si.saveBuffer();
		si.drawImage(IMG_LEVEL_UP);
		si.print(8,2, who.getDescription(), GFXDisplay.COLOR_BOLD);
		si.drawImage(15,40,app.getImage());
		JTextArea t1 = createTempArea(80,64,700,400);
		t1.setForeground(Color.WHITE);
		t1.setText(who.getLongDescription());
		si.add(t1);
		si.setFont(FNT_TEXT);
		MonsterRecord record = Main.getMonsterRecordFor(who.getID());
		long baseKilled = 0;
		long baseKillers = 0;
		if (record != null){
			baseKilled = record.getKilled();
			baseKillers = record.getKillers();
		}
		si.print(2,17, "You have killed "+(baseKilled+player.getGameSessionInfo().getDeathCountFor(who))+" "+who.getDescription()+"s",Color.WHITE);
		if (baseKillers == 0){
			si.print(2,18, "No "+who.getDescription()+"s have killed you",Color.WHITE);
		} else {
			si.print(2,18, "You have been killed by "+baseKillers+" "+who.getDescription()+"s",Color.WHITE);
		}
		si.print(2,20, "[Press Space]",Color.WHITE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
		gui.messageBox.setVisible(true);
		t1.setVisible(false);
		si.remove(t1);
		si.restore();
		si.refresh();
	}

}
