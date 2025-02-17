package crl.level;

import sz.util.*;
import crl.action.*;

public class SpawnMonster extends Action {
	
	public AT getID() {
		return AT.SpawnMonster;
	}

	public void execute() {
		Level level = performer.level;
		Respawner perf = (Respawner)performer;
		if (Util.chance(perf.getProb())) {
			level.respawn();
		}
	}

}