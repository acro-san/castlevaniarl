package crl.action;

public class WhipFireball extends ProjectileSkill {
	
	public int getDamage() {
		return 4;
	}
	
	public int getHeartCost() {
		return 0;
	}

	public int getHit() {
		return 100;
	}

	public int getPathType() {
		return PATH_LINEAR;
	}

	public String getPromptPosition() {
		return null;
	}

	public int getRange() {
		return 15;
	}

	public String getSelfTargettedMessage() {
		return null;
	}

	public String getSFXID() {
		return "SFX_WHIP_FIREBALL";
	}

	public String getShootMessage() {
		return "A fireball flies out from your whip";
	}

	public String getSpellAttackDesc() {
		return "fireball";
	}

	public boolean needsPosition() {
		return false;
	}

	public AT getID() {
		return AT.WhipFireball;
	}
}