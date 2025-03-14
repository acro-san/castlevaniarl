package crl.ui.consoleUI;

import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import static crl.ui.Colors.*;
import sz.csi.textcomponents.BasicListItem;
import sz.csi.textcomponents.ListBox;
import sz.csi.textcomponents.MenuBox;
import sz.csi.textcomponents.SimpleMenuItem;
import sz.csi.textcomponents.TextBox;
import sz.csi.textcomponents.TextInformBox;
import sz.util.*;
import crl.Main;
import crl.action.*;
import crl.ui.consoleUI.effects.CharEffect;
import crl.ui.effects.*;
import crl.player.*;
import crl.player.advancements.Advancement;
import crl.item.*;
import crl.level.*;
import crl.npc.*;
import crl.monster.*;
import crl.feature.*;
import crl.game.GameFiles;
import crl.actor.*;
import crl.data.Text;
import crl.ui.*;

import java.util.Hashtable;//Map plz;
import java.util.Vector;//ArrayList plz;

/**
 * Shows the level using characters.
 * Informs the Actions and Commands of the player.
 * Must be listening to a System Interface
 */

public class ConsoleUserInterface extends UserInterface implements CommandListener {

	private int xrange = 25;
	private int yrange = 9;
	private Monster lockedMonster;
	
	private TextInformBox messageBox;
	private TextBox persistantMessageBox;
	public boolean showPersistantMessageBox = false;
	private ListBox idList;	// ...?
	
	private boolean eraseOnArrival; // Erase the buffer upon the arrival of a new msg
	
	private Hashtable<String,BasicListItem> sightListItems = new Hashtable<>();

	private transient ConsoleSystemInterface si;

	// font?

	public Position getAbsolutePosition(Position insideLevel){
		Position relative = Position.subs(insideLevel, player.pos);
		return Position.add(PC_POS, relative);
	}

	public final Position
		VP_START = new Position(1,3),
		VP_END = new Position (51,21),
		PC_POS = new Position (25,12);

///	private boolean[][] FOVMask;	// ALREADY EXISTS IN ABSTRACT SUPERCLASS "UserInterface"!!

	public void doLook() {
		Position offset = new Position (0,0);
		messageBox.setForeColor(RED);
		si.saveBuffer();
		Monster lookedMonster = null;
		while (true){
			Position browser = Position.add(player.pos, offset);
			String looked = "";
			si.restore();
			if (FOVMask[PC_POS.x + offset.x][PC_POS.y + offset.y]) {
				Cell choosen = level.getMapCell(browser);
				Feature feat = level.getFeatureAt(browser);
				Vector<Item> items = level.getItemsAt(browser);
				Item item = null;
				if (items != null) {
					item = (Item) items.elementAt(0);
				}
				Actor actor = level.getActorAt(browser);
				if (choosen != null)
					looked += choosen.getDescription();
				if (level.getBloodAt(browser) != null)
					looked += "{bloody}";
				if (feat != null)
					looked += ", "+ feat.getDescription();
				if (item != null)
					if (items.size() == 1)
						looked += ", "+ item.getDescription();
					else
						looked += ", "+ item.getDescription()+" and some items";
				if (actor != null) {
					if (actor instanceof Monster){
						looked += ", "+ actor.getDescription()+" ['m' for extended info]";
						lookedMonster = (Monster) actor;
					} else{
						looked += ", "+ actor.getDescription();
					}
				}
			}
			messageBox.setText(looked);
			messageBox.draw();
			si.print(PC_POS.x + offset.x, PC_POS.y + offset.y, '_', RED);
			si.refresh();
			CharKey x = new CharKey(CharKey.NONE);
			while (x.code != CharKey.SPACE && x.code != CharKey.m && x.code != CharKey.ESC &&
				   ! x.isArrow())
				x = si.inkey();
			if (x.code == CharKey.SPACE || x.code == CharKey.ESC) {
				si.restore();
				break;
			}
			if (x.code == CharKey.m) {
				if (lookedMonster != null)
					Display.thus.showMonsterScreen(lookedMonster, player);
			} else {
				offset.add(Action.directionToVariation(Action.toIntDirection(x)));
	
				if (offset.x >= xrange) offset.x = xrange;
				if (offset.x <= -xrange) offset.x = -xrange;
				if (offset.y >= yrange) offset.y = yrange;
				if (offset.y <= -yrange) offset.y = -yrange;
			}
		}
		messageBox.setText("Look mode off");
		refresh();
	}


	public void launchMerchant(Merchant who) {
		Debug.enterMethod(this, "launchMerchant", who);
		si.saveBuffer();
		
		Vector<Item> merchandise = who.getMerchandiseFor(player);
		if (merchandise == null || merchandise.size() == 0) {
			chat(who);
			return;
		}
		Equipment.eqMode = true;
		Item.shopMode = true;
		MenuBox menuBox = new MenuBox(si);
		menuBox.setHeight(24);
		menuBox.setWidth(79);
		menuBox.setPosition(0,0);
		menuBox.setMenuItems(merchandise);
		menuBox.setPromptSize(5);
		
		String merchantPrompt = String.format(
		"Greetings %s... I am %s, the %s merchant. May I interest you in an item?",
		player.getName(), who.getName(), who.getMerchandiseTypeDesc());
		menuBox.setPrompt(merchantPrompt);
		menuBox.setForeColor(RED);
		menuBox.setBorder(true);
		while (true) {
			menuBox.setTitle(who.getName()+" (Gold:"+player.getGold()+")");
			Item item = (Item)menuBox.getSelection();
			if (item == null) {
				break;
			}
			
			String sellPrompt = String.format(
			"The %s, %s; it costs %s, Do you want to buy it? (Y/n)",
			item.getDescription(), item.getShopDescription(), item.getGoldPrice()
			);
			menuBox.setPrompt(sellPrompt);
			menuBox.draw();
			if (prompt()) {
				if (player.getGold() >= item.getGoldPrice()) {
					player.reduceGold(item.getGoldPrice());
					if (player.canCarry()) {
						player.addItem(item);
					} else {
						level.addItem(player.pos, item);
					}
					menuBox.setPrompt(Text.MERCHANT_BUY_CONFIRM);
				} else {
					menuBox.setPrompt(Text.MERCHANT_BUY_FAIL_NOGOLD);
				}
			} else {
				menuBox.setPrompt(Text.MERCHANT_BUY_CANCEL);
			}
			//menuBox.draw();
		}
		Equipment.eqMode = false;
		Item.shopMode = false;
		si.restore();
		Debug.exitMethod();
	}
	
	
	public void chat(NPC who) {
		si.saveBuffer();
		Debug.enterMethod(this, "chat", who);
		TextBox chatBox = new TextBox(si);
		chatBox.setHeight(7);
		chatBox.setWidth(33);
		chatBox.setPosition(28, 3);
		chatBox.setBorder(true);
		chatBox.setForeColor(WHITE);
		chatBox.setBorderColor(WHITE);
		chatBox.setText(who.getTalkMessage());
		chatBox.setTitle(who.getDescription());
		chatBox.draw();
		si.refresh();
		waitKey();
		si.restore();
		Debug.exitMethod();
	}


	public boolean promptChat (NPC who) {
		si.saveBuffer();
		Debug.enterMethod(this, "chat", who);
		TextBox chatBox = new TextBox(si);
		chatBox.setHeight(7);
		chatBox.setWidth(33);
		chatBox.setPosition(28, 3);
		chatBox.setBorder(true);
		chatBox.setForeColor(WHITE);
		chatBox.setBorderColor(WHITE);
		chatBox.setText(who.getTalkMessage());
		chatBox.draw();
		si.refresh();
		boolean ret = prompt();
		si.restore();
		Debug.exitMethod();
		return ret;
	}


	public void drawEffect(Effect what) {
		//Debug.enterMethod(this, "drawEffect", what);
		if (what == null)
			return;
		//drawLevel();
		if (insideViewPort(getAbsolutePosition(what.getPosition()))) {
			si.refresh();
			si.setAutoRefresh(true);
			((CharEffect)what).drawEffect(this, si);
			si.setAutoRefresh(false);
		}
		//Debug.exitMethod();
	}
	
	public boolean isOnFOVMask(int x, int y) {
		return FOVMask[x][y];
	}

	private void drawLevel() {
		Debug.enterMethod(this, "drawLevel");
		//Cell[] [] cells = level.getCellsAround(player.pos.x,player.pos.y, player.pos.z, range);
		Cell[] [] rcells = level.getMemoryCellsAround(player.pos.x,player.pos.y, player.pos.z, xrange,yrange);
		Cell[] [] vcells = level.getVisibleCellsAround(player.pos.x,player.pos.y, player.pos.z, xrange,yrange);
		
		Position runner = new Position(player.pos.x - xrange, player.pos.y-yrange, player.pos.z);
		
		for (int x = 0; x < rcells.length; x++) {
			for (int y=0; y<rcells[0].length; y++) {
				if (rcells[x][y] != null && !rcells[x][y].getAppearance().getID().equals("NOTHING")) {
					CharAppearance app = (CharAppearance)rcells[x][y].getAppearance();
					char cellChar = app.getChar();
					if (level.getFrostAt(runner) != 0) {
						cellChar = '#';
					}
					//if (!level.isVisible(runner.x, runner.y))
					if (vcells[x][y] == null) {
						si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, cellChar, GRAY);
					}
				} else if (vcells[x][y] == null || vcells[x][y].getID().equals("AIR")) {
					si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, CharAppearance.getVoidAppearance().getChar(), Colors.BLACK);
				}
				runner.y++;
			}
			runner.y = player.pos.y-yrange;
			runner.x ++;
		}
		
		runner.x = player.pos.x - xrange;
		runner.y = player.pos.y-yrange;
		
		monstersOnSight.removeAllElements();
		featuresOnSight.removeAllElements();
		itemsOnSight.removeAllElements();
		
		for (int x = 0; x < vcells.length; x++) {
			for (int y=0; y<vcells[0].length; y++) {
				FOVMask[PC_POS.x-xrange+x][PC_POS.y-yrange+y] = false;
				if (vcells[x][y] != null) {
					FOVMask[PC_POS.x-xrange+x][PC_POS.y-yrange+y] = true;
					String bloodLevel = level.getBloodAt(runner);
					CharAppearance cellApp = (CharAppearance)vcells[x][y].getAppearance();
					int cellColor = cellApp.getColor();
					if (!level.isDay()){
						cellColor = DARK_BLUE;
					}
					if (bloodLevel != null){
						switch (Integer.parseInt(bloodLevel)){
							case 0:
								if (!level.isDay()){
									cellColor = DARK_RED;
								}else{
									cellColor = RED;
								}
								break;
							case 1:
								if (!level.isDay()){
									cellColor = PURPLE;
								}else{
									cellColor = DARK_RED;
								}
								break;
							case 8:
								cellColor = LEMON;
								break;
						}
					}
					if (vcells[x][y].isWater()){
						if (level.canFloatUpward(runner)){
							cellColor = BLUE;
						} else {
							cellColor = DARK_BLUE;
						}
					}
					
					char cellChar = cellApp.getChar();
					if (level.getFrostAt(runner) != 0){
						cellChar = '#';
						cellColor = CYAN;
					}
					if (level.getDepthFromPlayer(player.pos.x - xrange + x, player.pos.y - yrange + y) != 0 ){
						cellColor = TEAL;
					}

					if (player.isInvisible() || x!=xrange || y != yrange)
						si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, cellChar, cellColor);
					Feature feat = level.getFeatureAt(runner);
					if (feat != null) {
						if (feat.isVisible()) {
							BasicListItem li = sightListItems.get(feat.getID());
							if (li == null) {
								Debug.say("Adding "+feat.getID()+" to the hashtable");
								sightListItems.put(feat.getID(), new BasicListItem(((CharAppearance)feat.getAppearance()).getChar(), ((CharAppearance)feat.getAppearance()).getColor(), feat.getDescription()));
								li = sightListItems.get(feat.getID());
							}
							if (feat.isRelevant() && !featuresOnSight.contains(li)) {
								featuresOnSight.add(li);
							}
							CharAppearance featApp = (CharAppearance)feat.getAppearance();
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, featApp.getChar(), featApp.getColor());
						}
					}
					
					SmartFeature sfeat = level.getSmartFeature(runner);
					if (sfeat != null){
						if (sfeat.isVisible()){
							CharAppearance featApp = 
								(CharAppearance)sfeat.getAppearance();
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, featApp.getChar(), featApp.getColor());
						}
					}
					
					Vector<Item> items = level.getItemsAt(runner);
					Item item = null;
					if (items != null) {
						item = items.elementAt(0);
					}
					if (item != null) {
						if (item.isVisible()) {
							CharAppearance itemApp = (CharAppearance)item.getAppearance();
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, itemApp.getChar(), itemApp.getColor());
							BasicListItem li = (BasicListItem)sightListItems.get(item.getDefinition().getID());
							if (li == null) {
								//Debug.say("Adding "+item.getDefinition().getID()+" to the hashtable");
								sightListItems.put(item.getDefinition().getID(), new BasicListItem(((CharAppearance)item.getAppearance()).getChar(), ((CharAppearance)item.getAppearance()).getColor(), item.getDefinition().description));
								li = (BasicListItem)sightListItems.get(item.getDefinition().getID());
							}
							if (!itemsOnSight.contains(li))
								itemsOnSight.add(li);
						}
					}
					
					Monster monster = level.getMonsterAt(runner);
					if (monster != null && monster.isVisible) {
						BasicListItem li = null;
						if (monster instanceof NPC){
							li = (BasicListItem)sightListItems.get(((NPC)monster).getDescription());
							if (li == null){
								CharAppearance monsterApp = (CharAppearance)monster.getAppearance();
								Debug.say("Adding "+monster.getID()+" to the hashtable");
								sightListItems.put(((NPC)monster).getDescription(), new BasicListItem(monsterApp.getChar(), monsterApp.getColor(), monster.getDescription()));
								li = (BasicListItem)sightListItems.get(((NPC)monster).getDescription());
							}
						}else {
							li = (BasicListItem)sightListItems.get(monster.getID());
							if (li == null){
								CharAppearance monsterApp = (CharAppearance)monster.getAppearance();
								Debug.say("Adding "+monster.getID()+" to the hashtable");
								sightListItems.put(monster.getID(), new BasicListItem(monsterApp.getChar(), monsterApp.getColor(), monster.getDescription()));
								li = (BasicListItem)sightListItems.get(monster.getID());
							}
						}
						if (!monstersOnSight.contains(li))
							monstersOnSight.add(li);
						
						CharAppearance monsterApp = (CharAppearance) monster.getAppearance();
						if (monster.canSwim() && level.getMapCell(runner)!= null && level.getMapCell(runner).isShallowWater())
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, '~', monsterApp.getColor());
						else
						if (monster.hasCounter(Consts.C_MONSTER_FREEZE))
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, monsterApp.getChar(), CYAN);
						else
							si.print(PC_POS.x-xrange+x,PC_POS.y-yrange+y, monsterApp.getChar(), monsterApp.getColor());
					}
					
					if (!player.isInvisible()){
						si.print(PC_POS.x,PC_POS.y, ((CharAppearance)player.getAppearance()).getChar(), ((CharAppearance)player.getAppearance()).getColor());
					} else {
						
						si.print(PC_POS.x,PC_POS.y, ((CharAppearance)Main.appearances.get("SHADOW")).getChar(), ((CharAppearance)Main.appearances.get("SHADOW")).getColor());
					}

					
				}
				runner.y++;
			}
			runner.y = player.pos.y-yrange;
			runner.x ++;
		}
		
		/*monstersList.clear();
		itemsList.clear();*/
		idList.clear(); 
		if (player.hasHostage()){
			BasicListItem li = (BasicListItem)sightListItems.get(((NPC)player.getHostage()).getDescription());
			if (li == null){
				CharAppearance hostageApp = (CharAppearance)player.getHostage().getAppearance();
				Debug.say("Adding "+hostageApp.getID()+" to the hashtable");
				sightListItems.put(player.getHostage().getDescription(), new BasicListItem(hostageApp.getChar(), hostageApp.getColor(), player.getHostage().getDescription()));
				li = (BasicListItem)sightListItems.get(((NPC)player.getHostage()).getDescription());
			}
			idList.addElement(li);
		}
		idList.addElements(monstersOnSight);
		idList.addElements(itemsOnSight);
		idList.addElements(featuresOnSight);

		
		Debug.exitMethod();
	}
	
	@Override
	public void setPersistantMessage(String description) {
		persistantMessageBox.setText(description);
		showPersistantMessageBox = true;
	}
	
	private Vector<String> messageHistory = new Vector<>(20,10);
	public void addMessage(Message message) {
		Debug.enterMethod(this, "addMessage", message);
		if (eraseOnArrival) {
			messageBox.clear();
			messageBox.setForeColor(RED);
			eraseOnArrival = false;
		}
		if ((player != null &&
			player.pos != null &&
			message.location.z != player.pos.z) || 
			(message.location != null && !insideViewPort(getAbsolutePosition(message.location))))
		{
			Debug.exitMethod();
			return;
		}
		messageHistory.add(message.text);
		if (messageHistory.size()>100)
			messageHistory.removeElementAt(0);
		messageBox.addText(message.text);
		
		messageBox.draw();
		Debug.exitMethod();
		
	}


	private void drawPlayerStatus() {
		Debug.enterMethod(this, "drawPlayerStatus");
		int foreColor;
		int backColor;
		switch (((player.getHP()-1) / 20) + 1) {
		case 1:
			foreColor = RED;
			backColor = WHITE;
			break;
		case 2:
			foreColor = DARK_RED;
			backColor = RED;
			break;
		default:
			foreColor = MAGENTA;
			backColor = DARK_RED;
			break;
		}
		String timeTile = "";
		int timeColor = YELLOW;
		switch (level.getDayTime()){
		case Level.MORNING:
			timeTile = "O__";
			timeColor = BROWN;
			break;
		case Level.NOON:
			timeTile = "_O_";
			timeColor = YELLOW;
			break;
		case Level.AFTERNOON:
			timeTile = "__O";
			timeColor = RED;
			break;
		case Level.DUSK:
			timeTile = "(__";
			timeColor = BLUE;
			break;
		case Level.NIGHT:
			timeTile = "_O_";
			timeColor = BLUE;
			break;
		case Level.DAWN:
			timeTile = "__)";
			timeColor = BLUE;
			break;
		}
		
		String shot ="   ";
		if (player.getShotLevel() == 1) {
			shot = "II ";
		} else if (player.getShotLevel() == 2) {
			shot = "III";
		}
		int rest = ((player.getHP()-1) % 20) + 1;
		
		si.print(0,0,"SCORE    "+player.score);
		si.print(0,1,"PLAYER   ");
		
		for (int i = 0; i < 20; i++) {
			if (i+1 <= rest) {
				si.print(i+9,1,'I', foreColor);
			} else {
				si.print(i+9,1,'I', backColor);
			}
		}
		
		si.print(0,2,"ENEMY    ");
		if (player.level.boss != null) {	// TODO && bossSeen!
			// duplicate logic! see GFXUserInterface.drawBossHealthBar()!
			int sixthiedBossHits = (int)Math.ceil((player.level.boss.hp * 60.0)/(double)player.level.boss.getMaxHP());
			int foreColorB = 0;
			int backColorB = 0;
			switch (((sixthiedBossHits-1) / 20) + 1) {
			case 1:
				foreColorB = YELLOW;
				backColorB = WHITE;
				break;
			case 2:
				foreColorB = BROWN;
				backColorB = YELLOW;
				break;
			default:
				foreColorB = PURPLE;
				backColorB = BROWN;
				break;
			}
			
			int restB = ((sixthiedBossHits-1) % 20) + 1;
			
			for (int i = 0; i < 20; i++) {
				if (i+1 <= restB) {
					si.print(i+9,2,'I', foreColorB);
				} else {
					si.print(i+9,2,'I', backColorB);
				}
			}
		} else {
			si.print(9,2,"IIIIIIIIIIIIIIIIIIII", WHITE);
		}
		
		si.print(31,2,fill(player.getWeaponDescription()+" "+shot,40));

		if (player.level.levelNumber == -1) {
			si.print(43,0,fill(player.level.getDescription(), 35));
		} else {
			si.print(43,0,fill("STAGE   "+player.level.levelNumber+" "+player.level.getDescription(), 35));
		}
		
		//si.print(43+"STAGE ".length(),0);

		si.print(31,1,"v       ", RED);
		si.print(33,1,"- "+player.getHearts());
		si.print(39,1,"k     ", YELLOW);
		si.print(41,1,"- "+player.getKeys());
		
		si.print(47,1,"$            ", YELLOW);
		si.print(49,1,"- "+player.getGold());
		
		si.print(60,1,"TIME - ");
		si.print(67,1,timeTile,timeColor);
		
		si.print(71,1,"     ",WHITE);
		if (player.getFlag(Consts.ENV_FOG))
			si.print(71,1,"FOG",TEAL);
		if (player.getFlag(Consts.ENV_RAIN))
			si.print (71,1,"RAIN",BLUE);
		if (player.getFlag(Consts.ENV_SUNNY))
			si.print (71,1,"SUNNY",YELLOW);
		if (player.getFlag(Consts.ENV_THUNDERSTORM))
			si.print (71,1,"STORM",WHITE);
		//si.print (71,2,"P  - 0");


		//si.print(1,24, "                                                                     ");
		si.print(1,24, fill(player.getName()+", the Lv"+player.getPlayerLevel()+" "+player.getClassString()+" "+player.getStatusString(),70));

		Debug.exitMethod();
	}
	
	private String fill(String str, int limit) {
		if (str.length() > limit) {
			return str.substring(0,limit);
		} else {
			return str+spaces(limit-str.length());
		}
	}

	private Hashtable<String, String> hashSpaces =  new Hashtable<>();
	private String spaces(int n) {
		String ret = hashSpaces.get(n+"");
		if (ret != null) {
			return ret;
		}
		ret = "";
		for (int i = 0; i < n; i++) {
			ret += " ";
		}
		hashSpaces.put(n+"", ret);
		return ret;
	}
	
	private Action target;
	
	public void init(ConsoleSystemInterface psi, UserCommand[] gameCommands, Action target) {
		Debug.enterMethod(this, "init");
		this.target = target;
		super.init(gameCommands);
		messageBox = new TextInformBox(psi);
		idList = new ListBox(psi);
		
		messageBox.setPosition(1,22);
		messageBox.setWidth(78);
		messageBox.setHeight(2);
		messageBox.setForeColor(RED);
		
		persistantMessageBox = new TextBox(psi);
		persistantMessageBox.setBounds(40, 5, 38, 14);
		persistantMessageBox.setBorder(true);
		persistantMessageBox.setBorderColor(RED);
		
		persistantMessageBox.setForeColor(WHITE);
		persistantMessageBox.setTitle("Tutorial");

		/*monstersList.setPosition(2, 4);
		monstersList.setWidth(27);
		monstersList.setHeight(10);*/
		
		idList.setPosition(52,4);
		idList.setWidth(27);
		idList.setHeight(18);
		si = psi;
		FOVMask = new boolean[80][25];
		Debug.exitMethod();
	}

	/** 
	 * Checks if the point, relative to the console coordinates, is inside the
	 * ViewPort 
	 */
	public boolean insideViewPort(int x, int y) {
		//return (x>=VP_START.x && x <= VP_END.x && y >= VP_START.y && y <= VP_END.y);
		return (x>=0 && x < FOVMask.length && y >= 0 && y < FOVMask[0].length) && FOVMask[x][y];
	}

	public boolean insideViewPort(Position what){
		return insideViewPort(what.x, what.y);
	}

	public boolean isDisplaying(Actor who){
		return insideViewPort(getAbsolutePosition(who.pos));
	}

	private Position pickPosition(String prompt, int fireKeyCode) throws ActionCancelException {
		Debug.enterMethod(this, "pickPosition");
		messageBox.setForeColor(BLUE);
		messageBox.setText(prompt);
		messageBox.draw();
		si.refresh();
		si.saveBuffer();
		
		Position defaultTarget = null; 
		Position nearest = getNearestMonsterPosition();
		if (nearest != null) {
			defaultTarget = nearest;
		} else {
			defaultTarget = null;
		}
		
		Position browser = null;
		Position offset = new Position (0,0);
		if (lockedMonster != null) {
			if (!player.sees(lockedMonster)  || lockedMonster.isDead()) {
				lockedMonster = null;
			} else {
				defaultTarget = new Position(lockedMonster.pos);
			}
		}
		if (!insideViewPort(PC_POS.x + offset.x,PC_POS.y + offset.y)) {
			offset = new Position (0,0);
		}
		
		if (defaultTarget == null) {
			offset = new Position (0,0);
		} else {
			offset = new Position(defaultTarget.x - player.pos.x, defaultTarget.y - player.pos.y);
		}
		while (true) {
			si.restore();
			String looked = "";
			browser = Position.add(player.pos, offset);
			
			/*if (PC_POS.x + offset.x < 0 || PC_POS.x + offset.x >= FOVMask.length || PC_POS.y + offset.y < 0 || PC_POS.y + offset.y >=FOVMask[0].length){
				offset = new Position (0,0);
				browser = Position.add(player.pos, offset);
			}*/
				
			if (FOVMask[PC_POS.x + offset.x][PC_POS.y + offset.y]){
				Cell choosen = level.getMapCell(browser);
				Feature feat = level.getFeatureAt(browser);
				Vector<Item> items = level.getItemsAt(browser);
				Item item = null;
				if (items != null) {
					item = (Item)items.elementAt(0);
				}
				Actor actor = level.getActorAt(browser);
				si.restore();
				
				if (choosen != null)
					looked += choosen.getDescription();
				if (level.getBloodAt(browser) != null)
				    looked += "{bloody}";
				if (feat != null)
					looked += ", "+ feat.getDescription();
				if (actor != null)
					looked += ", "+ actor.getDescription();
				if (item != null)
					looked += ", "+ item.getDescription();
			}
			messageBox.setText(prompt+" "+looked);
			messageBox.draw();
			//si.print(PC_POS.x + offset.x, PC_POS.y + offset.y, '_', BLUE);
			drawLineTo(PC_POS.x + offset.x, PC_POS.y + offset.y, '*', DARK_BLUE);
			si.print(PC_POS.x + offset.x, PC_POS.y + offset.y, 'X', BLUE);
			si.refresh();
			CharKey x = new CharKey(CharKey.NONE);
			while (x.code != CharKey.SPACE && x.code != CharKey.ESC && x.code != fireKeyCode &&
				   ! x.isArrow())
				x = si.inkey();
			if (x.code == CharKey.ESC){
				si.restore();
				throw new ActionCancelException();
			} 
			if (x.code == CharKey.SPACE || x.code == fireKeyCode){
				si.restore();
				if (level.getMonsterAt(browser) != null)
					lockedMonster = level.getMonsterAt(browser);
				return browser;
			}
			offset.add(Action.directionToVariation(Action.toIntDirection(x)));

			if (offset.x >= xrange) offset.x = xrange;
			if (offset.x <= -xrange) offset.x = -xrange;
			if (offset.y >= yrange) offset.y = yrange;
			if (offset.y <= -yrange) offset.y = -yrange;
		}
	}


	private int pickDirection(String prompt) throws ActionCancelException {
		Debug.enterMethod(this, "pickDirection");
		//refresh();
		messageBox.setText(prompt);
		messageBox.draw();
		si.refresh();
		//refresh();

		CharKey x = new CharKey(CharKey.NONE);
		while (x.code == CharKey.NONE)
			x = si.inkey();
		int ret = ConsoleUISelector.toIntDirection(x);
		if (ret != -1) {
			Debug.exitMethod(ret);
			return ret;
		} else {
			ActionCancelException ace = new ActionCancelException();
			Debug.exitExceptionally(ace);
			si.refresh();
			throw ace;
		}
	}

	private Item pickEquipedItem(String prompt) throws ActionCancelException {
		Debug.enterMethod(this, "pickEquipedItem");
		Vector<Item> equipped = new Vector<>();
		if (player.armor != null)
			equipped.add(player.armor);
		if (player.weapon != null)
			equipped.add(player.weapon);
		if (player.shield != null)
			equipped.add(player.shield);
		MenuBox menuBox = new MenuBox(si);
		//menuBox.setBounds(26,6,30,11);
		menuBox.setBounds(10,3,60,18);
		menuBox.setPromptSize(2);
		menuBox.setMenuItems(equipped);
		menuBox.setPrompt(prompt);
		menuBox.setForeColor(RED);
		menuBox.setBorder(true);
		si.saveBuffer();
		menuBox.draw();
		Item equiped = (Item)menuBox.getSelection();
		si.restore();
		if (equiped == null){
			ActionCancelException ret = new ActionCancelException();
			Debug.exitExceptionally(ret);
			throw ret;
		}
		return equiped;
	}
	
	private Item pickItem(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickItem");
		Vector<Equipment> inventory = player.getInventory();
		MenuBox menuBox = new MenuBox(si);
		menuBox.setBounds(10,3,60,18);
		menuBox.setPromptSize(2);
		menuBox.setMenuItems(inventory);
		menuBox.setPrompt(prompt);
		menuBox.setForeColor(RED);
		menuBox.setBorder(true);
		si.saveBuffer();
		menuBox.draw();
		Equipment equipment = (Equipment)menuBox.getSelection();
		si.restore();
		if (equipment == null){
			ActionCancelException ret = new ActionCancelException();
			Debug.exitExceptionally(ret);
			throw ret;
		}
		return equipment.getItem();
	}
	
	private Item pickUnderlyingItem(String prompt) throws ActionCancelException{
		Debug.enterMethod(this, "pickUnderlyingItem");
		Vector<Item> items = level.getItemsAt(player.pos);
  		if (items == null)
  			return null;
  		if (items.size() == 1)
  			return (Item) items.elementAt(0);
  		MenuBox menuBox = new MenuBox(si);
  		menuBox.setBounds(10,3,60,18);
  		menuBox.setPromptSize(2);
  		menuBox.setMenuItems(items);
  		menuBox.setPrompt(prompt);
  		menuBox.setForeColor(RED);
  		menuBox.setBorder(true);
  		si.saveBuffer();
  		menuBox.draw();
		Item item = (Item)menuBox.getSelection();
		si.restore();
		if (item == null){
			ActionCancelException ret = new ActionCancelException();
			Debug.exitExceptionally(ret);
			throw ret;
		}
		return item;
	}
	
	private Vector<Item> pickMultiItems(String prompt) {
		Equipment.eqMode = true;
		Vector<Equipment> inventory = player.getInventory();
		MenuBox menuBox = new MenuBox(si);
		menuBox.setBounds(25,3,40,18);
		menuBox.setPromptSize(2);
		menuBox.setMenuItems(inventory);
		menuBox.setPrompt(prompt);
		menuBox.setForeColor(RED);
		menuBox.setBorder(true);
		Vector<Item> ret = new Vector<>();
		MenuBox selectedBox = new MenuBox(si);
		selectedBox.setBounds(5,3,20,18);
		selectedBox.setPromptSize(2);
		selectedBox.setPrompt("Selected Items");
		selectedBox.setMenuItems(ret);
		selectedBox.setForeColor(RED);
		selectedBox.setBorder(true);
		
		si.saveBuffer();
		
		while (true) {
			selectedBox.draw();
			menuBox.draw();
			
			Equipment equipment = (Equipment)menuBox.getSelection();
			if (equipment == null) {
				break;
			}
			if (!ret.contains(equipment.getItem()))
				ret.add(equipment.getItem());
		}
		si.restore();
		Equipment.eqMode = false;
		return ret;
	}
	
	private Vector<Item> pickSpirits() {
		Vector<Equipment> originalInventory = player.getInventory();
		Vector<Item> inventory = new Vector<>();
		for (int i = 0; i < originalInventory.size(); i++) {
			Equipment testEq = originalInventory.elementAt(i);
			if (testEq.getItem().getDefinition().getID().endsWith("_SPIRIT")) {
				inventory.add(testEq.getItem());
			}
		}
		
		MenuBox menuBox = new MenuBox(si);
		menuBox.setBounds(25,3,40,18);
		menuBox.setPromptSize(2);
		menuBox.setMenuItems(inventory);
		menuBox.setPrompt("Select the spirits to fusion");
		menuBox.setForeColor(RED);
		menuBox.setBorder(true);

		Vector<Item> ret = new Vector<>();
		MenuBox selectedBox = new MenuBox(si);
		selectedBox.setBounds(5,3,20,18);
		selectedBox.setPromptSize(2);
		selectedBox.setPrompt("Selected Spirits");
		selectedBox.setMenuItems(ret);
		selectedBox.setForeColor(RED);
		selectedBox.setBorder(true);

		si.saveBuffer();

		while (true) {
			selectedBox.draw();
			menuBox.draw();
			
			Equipment equipment = (Equipment)menuBox.getSelection();
			if (equipment == null)
				break;
			if (!ret.contains(equipment.getItem()))
				ret.add(equipment.getItem());
		}
		si.restore();
		return ret;
	}

	public void processQuit(){
		messageBox.setForeColor(RED);
		messageBox.setText(Util.pick(Text.QUIT_MESSAGES)+" (y/n)");
		messageBox.draw();
		si.refresh();
		if (prompt()){
			messageBox.setText("Go away, and let the world flood in darkness... [Press Space to continue]");
			messageBox.draw();
			si.refresh();
			si.waitKey(CharKey.SPACE);
			player.getGameSessionInfo().setDeathCause(GameSessionInfo.QUIT);
			player.getGameSessionInfo().deathLevel = level.levelNumber;
			informPlayerCommand(CommandListener.QUIT);
		}
		messageBox.draw();
		messageBox.clear();
		si.refresh();
	}
	
	public void processSave(){
		if (!player.getGame().canSave()){
			level.addMessage("You cannot save your game here!");
			return;
		}
		messageBox.setForeColor(RED);
		messageBox.setText("Save your game? (y/n)");
		messageBox.draw();
		si.refresh();
		if (prompt()){
			messageBox.setText("Saving... I will await your return.. [Press Space to continue]");
			messageBox.draw();
			si.refresh();
			si.waitKey(CharKey.SPACE);
			informPlayerCommand(CommandListener.SAVE);
		}
		messageBox.draw();
		messageBox.clear();
		si.refresh();
	}

	public boolean prompt (){
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code != CharKey.Y && x.code != CharKey.y && x.code != CharKey.N && x.code != CharKey.n)
			x = si.inkey();
		return (x.code == CharKey.Y || x.code == CharKey.y);
	}

	@Override
	public void showVersionDialog(String description, boolean stop) {
		si.print(2,20, description, WHITE);
		si.refresh();
		if (stop) {
			si.waitKey(CharKey.SPACE);
		}
	}
	
	@Override
	public void showCriticalError(String description) {
		si.print(2,20, description, WHITE);
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}

	public void safeRefresh(){
		// For the Console UI we just do a normal refresh
		refresh();
	}

	public void refresh(){
		//cleanViewPort();
		drawPlayerStatus();
	 	drawLevel();
		if (showPersistantMessageBox){
			persistantMessageBox.draw();
		} else {
			idList.draw();
		}
		
		si.refresh();
		messageBox.draw();
	  	messageBox.setForeColor(DARK_RED);
	  	if (!player.getFlag("KEEPMESSAGES"))
	  		eraseOnArrival = true;
	  	
    }

	public void setTargets(Action a) throws ActionCancelException{
		if (a.needsItem())
			a.setItem(pickItem(a.getPromptItem()));
		if (a.needsDirection()){
			a.setDirection(pickDirection(a.getPromptDirection()));
		}
		if (a.needsPosition()){
			if (a == target){
				a.setPosition(pickPosition(a.getPromptPosition(), CharKey.f));
			} else {
				a.setPosition(pickPosition(a.getPromptPosition(), CharKey.SPACE));
			}
		}
		if (a.needsEquipedItem())
			a.setEquipedItem(pickEquipedItem(a.getPromptEquipedItem()));
		if (a.needsMultiItems()){
			a.setMultiItems(pickMultiItems(a.getPromptMultiItems()));
		}
		if (a.needsSpirits()){
			a.setMultiItems(pickSpirits());
		}
		if (a.needsUnderlyingItem()){
			a.setItem(pickUnderlyingItem(a.getPrompUnderlyingItem()));
		}
	}

	private Vector vecItemUsageChoices = new Vector();
	{
		vecItemUsageChoices.add(new SimpleMenuItem('*', "(u)se", 1));
		vecItemUsageChoices.add(new SimpleMenuItem('*', "(e)quip", 2));
		vecItemUsageChoices.add(new SimpleMenuItem('*', "(d)rop", 3));
		vecItemUsageChoices.add(new SimpleMenuItem('*', "(t)hrow",4 ));
		vecItemUsageChoices.add(new SimpleMenuItem('*', "( ) Cancel",5));
		
	}
	
	private int [] additionalKeys = new int[]{
				CharKey.N1, CharKey.N2, CharKey.N3, CharKey.N4,
		};
	
	private int [] itemUsageAdditionalKeys = new int[]{
				CharKey.u, CharKey.e, CharKey.d, CharKey.t,
		};
	
	public Action showInventory() throws ActionCancelException {
		Equipment.eqMode = true;
		Vector<Equipment> inventory = player.getInventory();
		int xpos = 1, ypos = 0;
		MenuBox menuBox = new MenuBox(si);
		menuBox.setHeight(11);
		menuBox.setWidth(50);
		menuBox.setPosition(1,8);
		menuBox.setBorder(false);
		menuBox.setMenuItems(inventory);

		MenuBox itemUsageChoices = new MenuBox(si);
		itemUsageChoices.setHeight(9);
		itemUsageChoices.setWidth(20);
		itemUsageChoices.setPosition(52,15);
		itemUsageChoices.setBorder(false);
		itemUsageChoices.setMenuItems(vecItemUsageChoices);
		itemUsageChoices.clearBox();

		TextBox itemDescription = new TextBox(si);
		itemDescription.setBounds(52,9,25,5);
		si.saveBuffer();
		si.cls();
		si.print(xpos,ypos,  "------------------------------------------------------------------------", DARK_RED);
		si.print(xpos,ypos+1,  "Inventory", RED);
		si.print(xpos,ypos+2,  "------------------------------------------------------------------------", DARK_RED);
		si.print(xpos+2,ypos+3,  "1. Weapon:    "+player.getEquipedWeaponDescription());
		si.print(xpos+2,ypos+4,  "2. Readied:   "+player.getSecondaryWeaponDescription());
		si.print(xpos+2,ypos+5,  "3. Armor:     "+player.getArmorDescription());
		si.print(xpos+2,ypos+6,  "4. Shield:    "+player.getAccDescription());
		si.print(xpos,ypos+7,  "------------------------------------------------------------------------", DARK_RED);
		menuBox.draw();
		si.print(xpos,24,  "[Space] to continue, Up and Down to browse");
		si.refresh();
		Item selected = null;

		Action selectedAction = null;
		do {
			try {
				Equipment eqs = (Equipment) menuBox.getSelectionAKS(additionalKeys);
				if (eqs == null)
					break;
				selected = eqs.getItem();
			} catch (AdditionalKeysSignal aks){
				switch (aks.getKeyCode()){
				case CharKey.N1:
					//Unequip Weapon
					if (player.weapon != null) {
						selectedAction = new Unequip();
						selectedAction.setPerformer(player);
						selectedAction.setEquipedItem(player.weapon);
						si.restore();
						return selectedAction;
					} else {
						continue;
					}
				case CharKey.N2:
					//Unequip Secondary Weapon
					if (player.secondaryWeapon != null){
						selectedAction = new Unequip();
						selectedAction.setPerformer(player);
						selectedAction.setEquipedItem(player.secondaryWeapon);
						si.restore();
						return selectedAction;
					} else {
						continue;
					}
				case CharKey.N3:
					// Unequip Armor
					if (player.armor != null) {
						selectedAction = new Unequip();
						selectedAction.setPerformer(player);
						selectedAction.setEquipedItem(player.armor);
						si.restore();
						return selectedAction;
					} else {
						continue;
					}
				case CharKey.N4:
					// Unequip Shield
					if (player.shield != null) {
						selectedAction = new Unequip();
						selectedAction.setPerformer(player);
						selectedAction.setEquipedItem(player.shield);
						si.restore();
						return selectedAction;
					} else {
						continue;
					}
				}
			}
			if (selected == null){
				break;
			}
			si.print(52, 8, fill(selected.getDescription(), 25), RED);
			itemDescription.clear();
			itemDescription.setText(selected.getDefinition().menuDescription);

			itemUsageChoices.draw();
			itemDescription.draw();
			SimpleMenuItem choice = null;
			try {
				choice = (SimpleMenuItem)itemUsageChoices.getNullifiedSelection(itemUsageAdditionalKeys);
			} catch (AdditionalKeysSignal aks){
				switch (aks.getKeyCode()){
				case CharKey.u:
					choice = (SimpleMenuItem)vecItemUsageChoices.elementAt(0);
					break;
				case CharKey.e:
					choice = (SimpleMenuItem)vecItemUsageChoices.elementAt(1);
					break;
				case CharKey.d:
					choice = (SimpleMenuItem)vecItemUsageChoices.elementAt(2);
					break;
				case CharKey.t:
					choice = (SimpleMenuItem)vecItemUsageChoices.elementAt(3);
					break;
				}
			}
			
			if (choice != null) {
				switch (choice.getValue()){
				case 1: // Use
					Use use = new Use();
					use.setPerformer(player);
					use.setItem(selected);
					si.restore();
					return use;
				case 2: //Equip
					Equip equip = new Equip();
					equip.setPerformer(player);
					equip.setItem(selected);
					si.restore();
					return equip;
				case 3: //Drop
					Drop drop = new Drop();
					drop.setPerformer(player);
					drop.setItem(selected);
					si.restore();
					return drop;
				case 4: // Throw
					Throw throwx = new Throw();
					throwx.setPerformer(player);
					throwx.setItem(selected);
					si.restore();
					throwx.setPosition(pickPosition("Throw where?", CharKey.SPACE));
					return throwx;
				case 5: // Cancel

					break;
				}
			}
			si.print(52, 8, fill("",25));
			itemUsageChoices.clearBox();
			itemDescription.clearBox();

		} while (selected != null);
		//		si.waitKey(CharKey.SPACE);
		si.restore();
		Equipment.eqMode = false;
		return null;
	}


	/**
	 * Shows a message inmediately; useful for system
	 * messages.
	 *  
	 * @param x the message to be shown
	 */
	public void showMessage(String x){
		messageBox.setForeColor(RED);
		messageBox.addText(x);
		messageBox.draw();
		si.refresh();
	}
	
	public void showImportantMessage(String x){
		showMessage(x);
		si.waitKey(CharKey.SPACE);
	}
	
	public void showSystemMessage(String x){
		messageBox.setForeColor(RED);
		messageBox.setText(x);
		messageBox.draw();
		si.refresh();
		si.waitKey(CharKey.SPACE);
	}
	
	
	public void showMessageHistory() {
		si.saveBuffer();
		si.cls();
		si.print(1, 0, "Message Buffer", Colors.DARK_RED);
		for (int i = 0; i < 22; i++) {
			if (i >= messageHistory.size()) {
				break;
			}
			si.print(1,i+2, (String)messageHistory.elementAt(messageHistory.size()-1-i), Colors.RED);
		}
		si.print(55, 24, "[ Space to Continue ]");
		si.waitKey(CharKey.SPACE);
		si.restore();
	}


	public void showPlayerStats () {
		si.saveBuffer();
		si.cls();
		si.print(1,0, player.getName()+" the level "+ player.getPlayerLevel()+" "+player.getClassString() + " "+player.getStatusString(), RED);
		si.print(1,1, "Sex: "+ (player.sex == Player.MALE ? "M" : "F"));
		si.print(1,2, "Hits: "+player.getHP()+ "/"+player.getHPMax()+" Hearts: " + player.getHearts() +"/"+player.getHeartsMax()+
					  " Gold: "+player.getGold()+ " Keys: "+player.getKeys());
		si.print(1,3, "Carrying: "+player.getItemCount()+"/"+player.getCarryMax());
		si.print(1,5, "Attack      +"+player.getAttack());
		si.print(1,6, "Soul Power  +"+player.getSoulPower());
		si.print(1,7, "Evade       "+player.getEvadeChance()+"%");
		si.print(1,8, "Combat      "+(50-player.getAttackCost()));
		si.print(1,9, "Invocation  "+(50-player.getCastCost()));
		si.print(1,10,"Movement    "+(50-player.getWalkCost()));
		
		si.print(1,11,"Experience  "+player.getXp()+"/"+player.getNextXP());
		
		/*si.print(1,2, "Skills", RED);
		Vector skills = player.getAvailableSkills();
		int cont = 0;
		for (int i = 0; i < skills.size(); i++){
			if (i % 10 == 0)
				cont++;
			si.print((cont-1) * 25 + 1, 3 + i - ((cont-1) * 10), ((Skill)skills.elementAt(i)).getMenuDescription());
		}*/
		
		si.print(1,13, "Weapon Proficiencies", RED);
		si.print(1,14, "Hand to hand             Whips                    Projectiles", RED);
		si.print(1,15, "Daggers                  Maces                    Bows/Xbows", RED);
		si.print(1,16, "Swords                   Pole                     Machinery", RED);
		si.print(1,17, "Spears                   Rings                    Shields", RED);

		String[] wskills = ItemDefinition.CATS;
		int cont = 0;
		for (int i = 0; i < wskills.length; i++) {
			if (i % 4 == 0) {
				cont++;
			}
			si.print((cont-1) * 25 + 14,
					14 + i - ((cont-1) * 4),
					Text.VERBOSE_SKILLS[player.weaponSkill(wskills[i])]);
		}

		si.print(1,19, "Attack Damage  ", RED);
		si.print(1,20, "Actual Defense ", RED);
		si.print(1,21, "Shield Rates   ", RED);
		
		si.print(16,19, ""+player.getWeaponAttack(), WHITE);
		si.print(16,20, player.getArmorDefense()+(player.getDefenseBonus()!=0?"+"+player.getDefenseBonus():""), WHITE);
		si.print(16,21, "Block "+player.getShieldBlockChance()+"% Coverage "+player.getShieldCoverageChance()+"%", WHITE);
		
		si.print(1,23, "[ Press Space to continue ]");
		si.refresh();
		si.waitKey(CharKey.SPACE);
		si.restore();
	}

	public Action showSkills() throws ActionCancelException {
		Debug.enterMethod(this, "showSkills");
		si.saveBuffer();
		Vector<Skill> skills = player.getAvailableSkills();
		MenuBox menuBox = new MenuBox(si);
		menuBox.setHeight(14);
		menuBox.setWidth(33);
		menuBox.setBorder(true);
		menuBox.setForeColor(RED);
		menuBox.setPosition(24,4);
		menuBox.setMenuItems(skills);
		menuBox.setTitle("Skills");
		menuBox.setPromptSize(0);
		menuBox.draw();
		si.refresh();
		Skill selectedSkill = (Skill)menuBox.getSelection();
		if (selectedSkill == null) {
			si.restore();
			Debug.exitMethod("null");
			return null;
		}
		si.restore();
		if (selectedSkill.isSymbolic()) {
			Debug.exitMethod("null");
			return null;
		}
		
		Action selectedAction = selectedSkill.getAction();
		selectedAction.setPerformer(player);
		if (selectedAction.canPerform(player))
			setTargets(selectedAction);
		else
			level.addMessage(selectedAction.getInvalidationMessage());
		
		//si.refresh();
		Debug.exitMethod(selectedAction);
		return selectedAction;
	}

	public void levelUp() {
		showMessage("You gained a level!, [Press Space to continue]");
		si.waitKey(CharKey.SPACE);
		if (player.deservesAdvancement(player.getPlayerLevel())) {
			Vector advancements = player.getAvailableAdvancements();
			if (advancements.size() != 0) {
				Advancement playerChoice = Display.thus.showLevelUp(advancements);
				playerChoice.advance(player);
				player.getGameSessionInfo().addHistoryItem("went for "+playerChoice.getName());
			}
		}
		if (player.deservesStatAdvancement(player.getPlayerLevel())) {
			Vector advancements = player.getAvailableStatAdvancements();
			if (advancements.size() != 0) {
				Advancement playerChoice = Display.thus.showLevelUp(advancements);
				playerChoice.advance(player);
				player.getGameSessionInfo().addHistoryItem("went for "+playerChoice.getName());
			}
		}
		si.saveBuffer();
		((CharDisplay)Display.thus).showBoxedMessage("LEVEL UP!",player.getLastIncrementString(), 3,4,30,10);
		player.resetLastIncrements();
		si.restore();
		si.refresh();
		
		/*
		showMessage("You gained a level!, [Press Space to continue]");
		si.waitKey(CharKey.SPACE);
    	int soulOptions = 5;
    	Vector soulIds = getLevelUpSouls();
    	int playerChoice = Display.thus.showLevelUp(soulIds);
    	Item soul = ItemFactory.getItemFactory().createItem((String)soulIds.elementAt(playerChoice));
    	if (player.canCarry()){
    		player.addItem(soul);
    	} else {
    		player.level.addItem(player.pos, soul);
    	}
    	showMessage("You acquired a "+soul.getDescription());*/
	}


	public Action selectCommand(CharKey input) {
		Debug.enterMethod(this, "selectCommand", input);
		int com = getRelatedCommand(input.code);
		informPlayerCommand(com);
		Action ret = actionSelectedByCommand;
		actionSelectedByCommand = null;
		Debug.exitMethod(ret);
		return ret;
	}
	
	public void commandSelected(int commandCode) {
		switch (commandCode) {
			case CommandListener.PROMPTQUIT:
				processQuit();
				break;
			case CommandListener.PROMPTSAVE:
				processSave();
				break;
			case CommandListener.HELP:
				si.saveBuffer();
				Display.thus.showHelp();
				si.restore();
				break;
			case CommandListener.LOOK:
				doLook();
				break;
			case CommandListener.SHOWSTATS:
				showPlayerStats();
				break;
			case CommandListener.SHOWINVEN:
				try {
					actionSelectedByCommand = showInventory();
				} catch (ActionCancelException ace){

				}
				break;
			case CommandListener.SHOWSKILLS:
				try {
					if (!player.isSwimming()) {
						actionSelectedByCommand = showSkills();
					} else {
						player.level.addMessage("You can't do that!");
						throw new ActionCancelException();
					}
				} catch (ActionCancelException ace){
					addMessage(new Message("- Cancelled", player.pos));
					eraseOnArrival = true;si.refresh();
					actionSelectedByCommand = null;
				}
				break;
			case CommandListener.SHOWMESSAGEHISTORY:
				showMessageHistory();
				break;
			case CommandListener.SHOWMAP:
				Display.thus.showMap(level.getMapLocationKey(), level.getDescription());
				break;
			case CommandListener.SWITCHMUSIC:
				boolean enabled = Main.music.enabled;
				if (enabled) {
					showMessage("Turn off music");
					Main.music.stopMusic();
					Main.music.enabled = false;
				} else {
					showMessage("Turn on music");
					Main.music.enabled = true;
					if (!level.isDay() && level.hasNoonMusic()) {
						Main.music.playKey(level.getMusicKeyNoon());
					} else {
						Main.music.playKey(level.getMusicKeyMorning());
					}
				}
				break;
			case EXAMINELEVELMAP:
				examineLevelMap();
				break;
			case CommandListener.CHARDUMP:
				GameFiles.saveChardump(player);
				showMessage("Character File Dumped.");
				break;
		}
	}

	/*private boolean cheatConsole(CharKey input){
		switch (input.code){
		case CharKey.F2:
			player.addHearts(5);
			player.informPlayerEvent(Player.EVT_LEVELUP);
			break;
		case CharKey.F3:
			player.setInvincible(250);
			player.setInvisible(20);
			
			break;
		case CharKey.F4:
			String nextLevel = level.getMetaData().getExit("_NEXT");
			player.informPlayerEvent(Player.EVT_GOTO_LEVEL, nextLevel);
			break;
		case CharKey.F5:
			player.heal();
			break;
		case CharKey.F6:
			if (player.level.getBoss() != null)
				player.level.getBoss().damage(15);
			break;
		case CharKey.F7:
			player.level.setIsDay(!player.level.isDay());
			break;
		case CharKey.F8:
			//player.informPlayerEvent(Player.EVT_BACK_LEVEL);
			break;
		default:
			return false;
		}
		return true;
	}*/
	
//	Runnable interface
	public void run (){}
	
//	IO Utility    
	public void waitKey (){
		CharKey x = new CharKey(CharKey.NONE);
		while (x.code == CharKey.NONE)
			x = si.inkey();
	}

	private void cleanViewPort(){
	    Debug.enterMethod(this, "cleanViewPort");
    	String spaces = "";
    	for (int i= 0; i<= VP_END.x - VP_START.x; i++){
    		spaces+=" ";
    	}
    	for (int i= VP_START.y; i<= VP_END.y; i++){
    		si.print(VP_START.x, i,spaces);
    	}
    	Debug.exitMethod();
	}

	private void drawLineTo(int x, int y, char icon, int color){
		Position target = new Position(x,y);
		Line line = new Line(PC_POS, target);
		Position tmp = line.next();
		while (!tmp.equals(target)){
			tmp = line.next();
			si.print(tmp.x, tmp.y, icon, color);
		}
		
	}
	
	private Position getNearestMonsterPosition(){
		VMonster monsters = level.getMonsters();
		Monster nearMonster = null;
		int minDist = 150;
		int maxDist = 15;
		for (int i = 0; i < monsters.size(); i++){
			Monster monster = (Monster) monsters.elementAt(i);
			if (monster.pos.z != level.getPlayer().pos.z) {
				continue;
			}
			int distance = Position.flatDistance(level.getPlayer().pos, monster.pos);
			if (distance < maxDist && distance< minDist && player.sees(monster)){
				minDist = distance;
				nearMonster = monster;
			}
		}
		if (nearMonster != null)
			return nearMonster.pos;
		else
			return null;
	}

	public Vector<String> getMessageBuffer() {
		if (messageHistory.size() > 20) {
			return new Vector<>(messageHistory.subList(messageHistory.size()-21,messageHistory.size()));
		} else {
			return messageHistory;
		}
	}
	
	private void examineLevelMap() {
		si.saveBuffer();
		si.cls();
		int lw = level.getWidth();
		int lh = level.getHeight();
		int remnantx = (int)((80 - (lw))/2.0d); 
		//int remnanty = (int)((25 - (lh))/2.0d);
		int pages = (int)((lh-1) / 23)+1;
		int cellColor = 0;
		Position runner = new Position(0,0,player.pos.z);
		for (int i = 1; i <= pages; i++){
			si.cls();
			for (int ii = 0; ii < 23; ii++){
				int y = (i-1)*23+ii;
				if (y >= level.getHeight())
					break;
				runner.y = y;
				runner.x = 0;
				for (int x = 0; x < level.getWidth(); x++, runner.x++){
					if (!level.remembers(x,y))
						cellColor = BLACK;
					else {
						Cell current = level.getMapCell(x, y, player.pos.z);
						Feature currentF = level.getFeatureAt(x,y,player.pos.z);
						if (level.isVisible(x,y)){
							if (current == null)
								cellColor = BLACK;
							else if (level.getExitOn(runner) != null)
								cellColor = RED;
							else
							if (current.isSolid() || (currentF != null && currentF.isSolid()))
								cellColor = BROWN;
							else 
								cellColor = LIGHT_GRAY;
							
						} else {
							if (current == null)
								cellColor = BLACK;
							else if (level.getExitOn(runner) != null)
								cellColor = RED;
							else if (current.isSolid()|| (currentF != null && currentF.isSolid()))
								cellColor = BROWN;
							else  
								cellColor = GRAY;
						}
						if (player.pos.x == x && player.pos.y == y)
							cellColor = RED;
					}
					si.safeprint(remnantx+x, ii, '.', cellColor);
					
				}
				
			}
			si.print(5, 24, "Page "+i, RED);
			si.refresh();
			si.waitKey(CharKey.SPACE);
		}
		
		si.restore();
		si.refresh();
	}
}