package crl.action.vanquisher;

import java.util.Vector;

import crl.action.AT;
import crl.action.ProjectileSkill;
import crl.monster.Monster;
import crl.player.Player;

public class IceSpell extends ProjectileSkill {
	
	public int getDamage() {
		return 15+2*getPlayer().getSoulPower();
	}

	public int getHit() {
		return 100;
	}

	public int getPathType() {
		return PATH_LINEAR;
	}

	public int getRange() {
		return 5;
	}

	public String getSelfTargettedMessage() {
		return "You feel chilly";
	}

	public String getSFXID() {
		return "SFX_ICE_SPELL";
	}

	public String getShootMessage() {
		return "You invoke the spell of Ice!";
	}

	public String getSpellAttackDesc() {
		return "chilly wind";
	}

	public boolean piercesThru() {
		return true;
	}

	public int getHeartCost() {
		return 8;
	}

	public AT getID() {
		return AT.IceSpell;
	}
	
	public void execute() {
		super.execute();
		Vector<Monster> hitMonsters = getHitMonsters();
		for (int i = 0; i < hitMonsters.size(); i++) {
			Monster targetMonster = hitMonsters.elementAt(i);
			int friz = 10 + getPlayer().getSoulPower() - targetMonster.getFreezeResistance();
			if (friz > 0) {
				if (targetMonster.wasSeen()) {
					targetMonster.level.addMessage("The "+targetMonster.getDescription()+ " is frozen!");
				}
				targetMonster.freeze(friz);
			} else {
				if (targetMonster.wasSeen()) {
					targetMonster.level.addMessage("The "+targetMonster.getDescription()+ " is too hot!");
				}
			}
		}
	}

	public int getCost() {
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.3);
	}
	
	public String getPromptPosition() {
		return "Where do you want to invoke the frost?";
	}
	
}