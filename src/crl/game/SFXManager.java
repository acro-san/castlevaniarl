package crl.game;

import sz.wav.WavPlayer;

public class SFXManager {
	
	private static Thread currentThread;
	
	public static boolean enabled = true;
	
	/*
	public static void setEnabled(boolean value) {
		enabled = value;
	}
	*/
	
	public static synchronized void play(String fileName) {
		if (enabled == false || fileName == null || fileName.equals("")) {
			return;
		}
		
		if (currentThread != null) {
			currentThread.interrupt();
		}
		if (fileName.endsWith(".wav")) {
			WavPlayer wavPlayer = new WavPlayer(fileName);
			currentThread = new Thread(wavPlayer);
		}
		try {
			currentThread.start();
		} catch (IllegalThreadStateException itse) {
			Game.addReport("Illegal Thread State for "+fileName);
		}
	}
}
