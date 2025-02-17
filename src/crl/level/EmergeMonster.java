package crl.level;

import sz.util.*;
import crl.action.*;
import crl.monster.*;

public class EmergeMonster extends Action {
	// WTF?!
////	private static EmergeMonster singleton = new EmergeMonster();
	
	public AT getID() {
		return AT.EmergeMonster;
	}
	
	public void execute() {
		Level level = performer.level;
		Emerger em = (Emerger)performer;
		Monster monster = em.getMonster();
		monster.pos = new Position(em.getPoint());
		level.addMonster(monster);
		if (em.getMound() != null)
			level.destroyFeature(em.getMound());
	}

	// It's almost as if... these shouldn't even be classes in the first place!
//	public static EmergeMonster getAction() {
//		return singleton;
//	}
}