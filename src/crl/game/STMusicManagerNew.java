package crl.game;

import java.util.HashMap;

//import sz.midi.MidisLoader;
import sz.midi.STMidiPlayer;
import sz.mp3.JLayerMP3Player;

public class STMusicManagerNew {
	private Thread currentMidiThread;
	private Thread currentMP3Thread;
	private HashMap<String, String> musics = new HashMap<>();
	public boolean enabled = true;
	// TODO When this is set to a different value, might we want to persist that
	// setting to a prefs file on disk?
	private String playing = "__nuthin";


	public STMusicManagerNew() {
		STMidiPlayer midiPlayer = new STMidiPlayer();
		JLayerMP3Player mp3Player = new JLayerMP3Player();
		currentMidiThread = new Thread(midiPlayer);
		currentMP3Thread = new Thread(mp3Player);
		currentMP3Thread.start();
		currentMidiThread.start();
	}


	public void stopMusic() {
		if (playing.endsWith("mp3")) {
			JLayerMP3Player.setInstruction(JLayerMP3Player.INS_STOP);
			if (currentMP3Thread != null){
				currentMP3Thread.interrupt();
			}
		} else {
			STMidiPlayer.setInstruction(STMidiPlayer.INS_STOP);
			if (currentMidiThread != null)
				currentMidiThread.interrupt();
		}
		playing = "__nuthin";
	}
	
	
	public void die() {
		STMidiPlayer.setInstruction(STMidiPlayer.INS_DIE);
		if (currentMidiThread != null){
			currentMidiThread.interrupt();
		}
		JLayerMP3Player.setInstruction(JLayerMP3Player.INS_DIE);
		if (currentMP3Thread != null){
			currentMP3Thread.interrupt();
		}
	}
	
	
	public void play(String fileName) {
		if (!enabled || playing.equals(fileName)) {
			return;
		}
		stopMusic();
		try {
			playing = fileName;
			if (fileName.endsWith("mp3")){
				JLayerMP3Player.setMP3(fileName);
				JLayerMP3Player.setInstruction(JLayerMP3Player.INS_LOAD);
				if (currentMP3Thread != null) {
					currentMP3Thread.interrupt();
				}
			} else {
				STMidiPlayer.setMidi(fileName);
				STMidiPlayer.setInstruction(STMidiPlayer.INS_LOAD);
				if (currentMidiThread != null){
					currentMidiThread.interrupt();
				}
			}
		} catch (Exception e) {
			Game.crash("Error trying to play "+fileName,e);
		}
	}
	
	
	public void playOnce(String fileName) {
		if (!enabled || playing.equals(fileName)) {
			return;
		}
		stopMusic();
		try {
			playing = fileName;
			if (fileName.endsWith("mp3")){
				JLayerMP3Player.setMP3(fileName);
				JLayerMP3Player.setInstruction(JLayerMP3Player.INS_LOAD);
				if (currentMP3Thread != null){
					currentMP3Thread.interrupt();
				}
			} else {
				STMidiPlayer.setMidi(fileName);
				STMidiPlayer.setInstruction(STMidiPlayer.INS_LOAD_ONCE);
				if (currentMidiThread != null){
					currentMidiThread.interrupt();
				}
			}
			
		} catch (Exception e) {
			Game.crash("Error trying to play "+fileName,e);
		}
	}
	
	
	public void addMusic(String levelType, String fileName) {
		musics.put(levelType, fileName);
	}
	
	
	public void playForLevel(String levelType) {
		String bgMusic = (String) musics.get(levelType);
		if (bgMusic != null) {
			play(bgMusic);
		} else {
			stopMusic();
		}
	}
	
	public void playKey(String key) {
		String bgMusic = (String) musics.get(key);
		if (bgMusic != null){
			play(bgMusic);
		} else {
			stopMusic();
		}
	}
	
	public void playKeyOnce(String key) {
		String bgMusic = (String) musics.get(key);
		if (bgMusic != null) {
			playOnce(bgMusic);
		} else {
			stopMusic();
		}
	}
	
	/*public void playNight() {
		String niteMusic = (String) musics.get(NITE_MUSIC);
		if (niteMusic != null)
			play(niteMusic); 
	}
	
	private final static String NITE_MUSIC = "NITE_MUSIC";*/
}

