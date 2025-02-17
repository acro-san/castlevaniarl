package crl.level;

import crl.*;
import crl.action.*;
import sz.util.*;
import crl.ai.*;
import crl.actor.*;

public class RespawnAI implements ActionSelector {
	
	private static final Action
		SPAWN_ACTION = new SpawnMonster();	//type?
	
	private int counter;

	//AIT?
	public String getID() {
		return "Respawn";
	}

	public Action selectAction(Actor who) {
		Debug.enterMethod(this, "selectAction", who);
		Respawner x = (Respawner) who;
		counter++;
		if (x.getFreq() < counter) {
			counter = 0;
			//Action ret = SpawnMonster.getAction();
			Debug.exitMethod(SPAWN_ACTION);
			return SPAWN_ACTION;
		}
		Debug.exitMethod("null");
		return null;
	}

	public ActionSelector derive() {
		try {
			return (ActionSelector) clone();
		} catch (CloneNotSupportedException cnse) {
			return null;
		}
	}
}