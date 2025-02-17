package crl.action.monster.boss;

import crl.action.AT;
import crl.action.Action;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Damage;

public class MummyStrangle extends Action {
	
	public AT getID() {
		return AT.MummyStrangle;
	}
	
	public void execute() {
		Level aLevel = performer.level;
		aLevel.getPlayer().damage("Akmodan strangles you!", (Monster)performer, new Damage(6, false));
	}

}