package crl.action.renegade;

import java.util.Vector;

import sz.util.Util;
import crl.action.AT;
import crl.action.ProjectileSkill;
import crl.monster.Monster;
import crl.player.Player;

public class SoulSteal extends ProjectileSkill{
	
	public int getDamage() {
		return 1+getPlayer().getSoulPower();
	}

	public int getHit() {
		return 100;
	}

	public int getPathType() {
		return PATH_LINEAR;
	}

	public int getRange() {
		return 15;
	}

	public String getSelfTargettedMessage() {
		return "";
	}
	
	public boolean allowsSelfTarget(){
		return false;
	}

	public String getSFXID() {
		return "SFX_SOUL_STEAL";
	}

	public String getShootMessage() {
		return "Soul Steal!";
	}

	public String getSpellAttackDesc() {
		return "soul";
	}

	public int getHeartCost() {
		return 5;
	}

	public AT getID() {
		return AT.SoulSteal;//"Soul Steal";
	}
	
	public String getSFX() {
		return "wav/fire.wav";
	}

	public int getCost() {
		Player p = (Player) performer;
		return (int)(p.getCastCost() * 1.1);
	}
	
	public String getPromptPosition() {
		return "Where do you want to invoke the soul?";
	}
	
	public void execute() {
		super.execute();
		Vector<Monster> monsters = getHitMonsters();
		for (int i = 0; i < monsters.size(); i++){
			Monster m = monsters.elementAt(i);
			// TODO: Make this relative to the monster's soul or something
			if (Util.chance(70)) {
				Player p = getPlayer();
				p.level.addMessage("You steal the "+m.getDescription()+" soul!");
				p.healHPPercentage( 5 + p.getSoulPower() );
			}
		}
	}
}