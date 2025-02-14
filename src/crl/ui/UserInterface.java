package crl.ui;

import java.util.*;

import sz.csi.textcomponents.BasicListItem;
import sz.util.*;

import crl.action.*;
//import crl.action.vkiller.Whip;
import crl.ui.effects.*;
import crl.player.*;
import crl.item.*;
import crl.level.*;
import crl.npc.*;
import crl.actor.*;


/** 
 *  Shows the level
 *  Informs the Actions and Commands of the player.
 * 	Must be listening to a System Interface
 */

public abstract class UserInterface implements CommandListener {

	protected Vector monstersOnSight = new Vector<>();
	protected Vector <BasicListItem> featuresOnSight = new Vector<>();
	protected Vector itemsOnSight = new Vector<>();
	protected Action actionSelectedByCommand;
	
	protected boolean eraseOnArrival; // Erase the buffer upon the arrival of a new msg
	
	protected String lastMessage;
	protected Level level;
	
	protected Player player;

	public Player getPlayer() {
		return player;
	}

	private boolean gameOver = false;
	public void setGameOver(boolean b) {
		gameOver = b;
	}
	
	public boolean gameOver( ){
		return gameOver;
	}
	
	protected boolean[][] FOVMask;
	//Interactive Methods
	public abstract void doLook();
	
	public abstract void launchMerchant(Merchant who);
	
	public abstract void chat(NPC who);
	
	public abstract boolean promptChat(NPC who);

	// Drawing Methods
	public abstract void drawEffect(Effect what);
	
	public boolean isOnFOVMask(int x, int y){
		return FOVMask[x][y];
	}

	public abstract void addMessage(Message message);
	public abstract Vector<String> getMessageBuffer();

	// *WHY*!?
	public void setPlayer(Player pPlayer) {
		player = pPlayer;
		level = player.level;
	}

	public void init(UserCommand[] gameCommands) {
		//uiSelector = selector;
		FOVMask = new boolean[80][25];
		for (int i = 0; i < gameCommands.length; i++) {
			this.gameCommands.put(gameCommands[i].getKeyCode()+"", gameCommands[i]);
		}
		addCommandListener(this);
	}

	protected int getRelatedCommand(int keyCode) {
		Debug.enterMethod(this, "getRelatedCommand", keyCode+"");
		UserCommand uc = (UserCommand ) gameCommands.get(keyCode+"");
		if (uc == null){
			Debug.exitMethod(CommandListener.NONE);
			return CommandListener.NONE;
		}

		int ret = uc.getCommand();
		Debug.exitMethod(ret+"");
		return ret;
	}
	
	
	public abstract boolean isDisplaying(Actor who);

	
	public void levelChange() {
		level = player.level;
	}
	
	protected void informPlayerCommand(int command) {
		Debug.enterMethod(this, "informPlayerCommand", command+"");
		for (int i =0; i < commandListeners.size(); i++){
			(commandListeners.elementAt(i)).commandSelected(command);
		}
		Debug.exitMethod();
	}
	
	public void addCommandListener(CommandListener pCl) {
		commandListeners.add(pCl);
	}
	
	public void removeCommandListener(CommandListener pCl) {
		commandListeners.remove(pCl);
	}
	
	protected Hashtable<String,UserCommand> gameCommands = new Hashtable<>();
	private Vector<CommandListener> commandListeners = new Vector<>(5);

	
	/**
	 * Prompts for Yes or NO
	 */
	public abstract boolean prompt();

	public abstract void refresh();

	// This method can be invoked from any thread and won't cause rendering issues.
	// Provided mostly so that the Swing implementation can refresh the UI from
	// non UI threads safely.
	public abstract void safeRefresh();

 	/**
     * Shows a message inmediately; useful for system
     * messages.
     *  
     * @param x the message to be shown
     */
	public abstract void showMessage(String x);

	public abstract void showImportantMessage(String x);
	/**
     * Shows a message inmediately; useful for system
     * messages. Waits for a key press or something.
     *  
     * @param x the message to be shown
     */
	public abstract void showSystemMessage(String x);
	
	/* Shows a level was won, lets pick a random spirit */
	public abstract void levelUp();
	
	public abstract void processQuit();
	
	public abstract void processSave();
	
	public abstract void showPlayerStats();
	
	public abstract Action showInventory() throws ActionCancelException;
	
	public abstract Action showSkills() throws ActionCancelException;
	
	public void commandSelected (int commandCode){
		switch (commandCode){
			case CommandListener.PROMPTQUIT:
				processQuit();
				break;
			case CommandListener.PROMPTSAVE:
				processSave();
				break;
			case CommandListener.HELP:
				Display.thus.showHelp();
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
					if (!player.isSwimming()){
						actionSelectedByCommand = showSkills();
					} else {
						player.level.addMessage("You can't do that!");
					}
				} catch (ActionCancelException ace){

				}
				break;
		}
	}
	
	
	public abstract void setTargets(Action a) throws ActionCancelException;
	public abstract void showMessageHistory();

	public abstract void setPersistantMessage(String description) ;

	public abstract void showVersionDialog(String description, boolean stop);
	public abstract void showCriticalError(String description);
}