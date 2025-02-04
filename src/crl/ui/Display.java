package crl.ui;

import java.io.File;
import java.util.Properties;
import java.util.Vector;

import crl.game.Game;
import crl.monster.Monster;
import crl.npc.Hostage;
import crl.player.HiScore;
import crl.player.Player;
import crl.player.advancements.Advancement;

public abstract class Display {
	
	public static Display thus;
	
	public static Properties keyBindings;	// WHY have this in Display!?
	
	public abstract int showTitleScreen();
	public abstract void showIntro(Player player);
	public abstract boolean showResumeScreen(Player player);
	public abstract void showEndgame(Player player);
	public abstract void showHiscores(HiScore[] scores);
	public abstract void showHelp();
	public abstract void showDraculaSequence();
	
	// showMessageBox! (currently: this is polymorphism as flow control)
	public abstract void showTimeChange(boolean day, boolean fog, boolean rain, boolean thunderstorm, boolean sunnyDay);
	
	public abstract void showTextBox(String text, int x, int y, int w, int h);

	public abstract int showSavedGames(File[] saveFiles);
	public abstract void showHostageRescue(Hostage h);
	public abstract Advancement showLevelUp(Vector<Advancement> soulIds);
	public abstract void showChat(String chatID, Game game);
	public abstract void showScreen(Object screenID);
	public abstract void showMap(String locationKey, String locationDescription);
	public abstract void showMonsterScreen(Monster who, Player player);
	
	/*
	public static final void setKeyBindings(Properties keyBindings) {
		Display.keyBindings = keyBindings;
	}
	
	public static final Properties getKeyBindings() {
		return keyBindings;				// WAIT. I'm sorry, bug??
	}
	*/
}
