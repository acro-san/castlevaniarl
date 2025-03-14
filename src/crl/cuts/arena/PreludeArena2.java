package crl.cuts.arena;

import crl.Main;
import crl.cuts.Unleasher;
import crl.game.Game;
import crl.game.STMusicManagerNew;
import crl.level.Level;
import crl.levelgen.MonsterSpawnInfo;

public class PreludeArena2 extends Unleasher {
	int counter;
	int superCounter;
	
	private MonsterSpawnInfo[] doInhabitants(String[] ids){
		MonsterSpawnInfo[] ret = new MonsterSpawnInfo[ids.length];
		for (int i = 0; i < ids.length; i++){
			ret[i] = new MonsterSpawnInfo(ids[i], MonsterSpawnInfo.UNDERGROUND, 100);
		}
		return ret;
	}
	private int counterLimit = 400;
	public void unleash(Level level, Game game) {
		counter++;
		if (counter > counterLimit) {
			counterLimit = counter*2;
			counter = 0;
			superCounter++;
			switch (superCounter) {
			case 1:
				level.setInhabitants(new MonsterSpawnInfo[] {
					new MonsterSpawnInfo("SKELETON_PANTHER", MonsterSpawnInfo.UNDERGROUND, 100),
					new MonsterSpawnInfo("BLACK_KNIGHT", MonsterSpawnInfo.UNDERGROUND, 100),
					new MonsterSpawnInfo("SKULL_HEAD", MonsterSpawnInfo.UNDERGROUND, 100)
				});
				level.levelNumber = 2;
				level.setMusicKeyMorning("ARENA2");
				level.setMusicKeyNoon("ARENA2");
				Main.music.playKey("ARENA2");
				break;
			case 2:
				level.setInhabitants(new MonsterSpawnInfo[]{
					new MonsterSpawnInfo("SPEAR_KNIGHT", MonsterSpawnInfo.UNDERGROUND, 100),
					new MonsterSpawnInfo("WEREBEAR", MonsterSpawnInfo.UNDERGROUND, 100),
					new MonsterSpawnInfo("BONE_ARCHER", MonsterSpawnInfo.UNDERGROUND, 100),
					new MonsterSpawnInfo("SKELETON_PANTHER", MonsterSpawnInfo.UNDERGROUND, 100),
					new MonsterSpawnInfo("AXE_KNIGHT", MonsterSpawnInfo.UNDERGROUND, 100)
				});
				level.levelNumber = 3;
				level.setMusicKeyMorning("ARENA3");
				level.setMusicKeyNoon("ARENA3");
				Main.music.playKey("ARENA3");
				break;
			case 3:
				level.setInhabitants(doInhabitants(new String[]{"DURGA","BLADE_SOLDIER","BONE_HALBERD","CROW"}));
				level.levelNumber = 4;
				level.setMusicKeyMorning("ARENA4");
				level.setMusicKeyNoon("ARENA4");
				Main.music.playKey("ARENA4");
				break;
			case 4:
				level.setInhabitants(doInhabitants(new String[]{"COCKATRICE","COOPER_ARMOR","SALOME"}));
				level.levelNumber = 5;
				level.setMusicKeyMorning("ARENA5");
				level.setMusicKeyNoon("ARENA5");
				Main.music.playKey("ARENA5");
				break;
			case 5:
				level.setInhabitants(doInhabitants(new String[]{"LILITH","BONE_MUSKET","VAMPIRE_BAT"}));
				level.levelNumber = 6;
				level.setMusicKeyMorning("ARENA6");
				level.setMusicKeyNoon("ARENA6");
				Main.music.playKey("ARENA6");
				break;
			case 6:
				level.setInhabitants(doInhabitants(new String[]{"ZELDO","CAGNAZOO","KNIFE_MERMAN"}));
				level.levelNumber = 7;
				level.setMusicKeyMorning("ARENA7");
				level.setMusicKeyNoon("ARENA7");
				Main.music.playKey("ARENA7");
				break;
			case 7:
				level.setInhabitants(doInhabitants(new String[]{"GIANTBAT"}));
				level.levelNumber = 8;
				level.setMusicKeyMorning("ARENA8");
				level.setMusicKeyNoon("ARENA8");
				Main.music.playKey("ARENA8");
				break;
			case 8:
				level.addMessage("Congratulations! You survived the arena!! But you must keep fighting...");
			}
		}
	}
}