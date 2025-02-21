package crl.monster;

import java.util.*;

import crl.Main;
import crl.level.Level;
import crl.levelgen.MonsterSpawnInfo;

import sz.util.*;

public class MonsterData {

	public static HashMap<String, MonsterDef> MONSTER_DEFS = new HashMap<>(40);
	public static Vector<MonsterDef> DEFS_ORDERED = new Vector<>(50);
	private static int lastSpawnLocation;

	public static Monster buildMonster(String id) {
		return new Monster(MONSTER_DEFS.get(id));
	}

	public static MonsterDef getDefinition(String id) {
		return MONSTER_DEFS.get(id);
	}
	
	public static void init(MonsterDef[] defs) {
		for (MonsterDef md: defs) {
			md.appearance = Main.appearances.get(md.ID);	// ???
			MONSTER_DEFS.put(md.ID, md);
			DEFS_ORDERED.add(md);
		}
	}
	
	public static int getLastSpawnPosition() {
		return lastSpawnLocation;
	}
	
	public static Monster getMonsterForLevel(Level level) {
		MonsterSpawnInfo[] spawnIDs = level.getSpawnInfo();
		if (spawnIDs == null || spawnIDs.length == 0) {
			return null;
		}
		while (true) {
			int rand = Util.rand(0, spawnIDs.length-1);
			if (Util.chance(spawnIDs[rand].getFrequency())) {
				lastSpawnLocation = spawnIDs[rand].getSpawnLocation();
				return new Monster(MONSTER_DEFS.get(spawnIDs[rand].getMonsterID()));
			}
		}
	}

	public static void printAppearances() {
		for (MonsterDef d: DEFS_ORDERED) {
			Debug.say("Monstero "+ d.description+" app "+d.appearance);
		}
		/*
		Set<String> mkeys = MONSTER_DEFS.keySet();
		// sorted? or no? That's what the linear array/list should be for...?
		for (String k: mkeys) {
		//Enumeration<String> x = MONSTER_DEFS.keys();
		//while (x.hasMoreElements()) {
			MonsterDefinition d = MONSTER_DEFS.get(k);	//x.nextElement()
			Debug.say("Monstero "+ d.description+" app "+d.appearance);
		}
		*/
	}
}