package crl.monster;

import java.util.*;

import crl.Main;
import crl.level.Level;
import crl.levelgen.MonsterSpawnInfo;

import sz.util.*;

public class MonsterData {

	private static Hashtable<String, MonsterDefinition> definitions = new Hashtable<>(40);
	private static Vector<MonsterDefinition> vDefinitions = new Vector<>(50);
	private static int lastSpawnLocation;

	public static Monster buildMonster(String id) {
		return new Monster(definitions.get(id));
	}

	public static MonsterDefinition getDefinition(String id) {
		return definitions.get(id);
	}
	
	public static void init(MonsterDefinition[] defs) {
		for (int i = 0; i < defs.length; i++) {
			defs[i].appearance = Main.appearances.get(defs[i].ID);	// ???
			definitions.put(defs[i].ID, defs[i]);
			vDefinitions.add(defs[i]);
			
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
				return new Monster(definitions.get(spawnIDs[rand].getMonsterID()));
			}
		}
	}

	public static void printAppearances() {
		Enumeration<String> x = definitions.keys();
		while (x.hasMoreElements()) {
			MonsterDefinition d = definitions.get(x.nextElement());
			Debug.say("Monstero "+ d.description+" app "+d.appearance);
		}
	}
}