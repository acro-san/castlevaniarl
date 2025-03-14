package crl.action.vkiller;

import crl.action.AT;
import crl.action.ProjectileSkill;
import crl.game.SFXManager;
import crl.player.Player;

public class ItemBreakDagger extends ProjectileSkill {
	
	public AT getID() {
		return AT.ItemBreak_Dagger;//"DaggerBreak";
	}

	public String getPromptPosition(){
		return "Where?";
	}

	public String getSFX(){
		return "wav/dagger.wav";
	}
	
	public int getCost() {
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}

	public int getDamage() {
		return 5 + 
		getPlayer().getShotLevel()*5+ 
		getPlayer().getSoulPower()+
		(getPlayer().getDaggerLevel() == 1 ? 6 : 0)+
		(getPlayer().getDaggerLevel() == 2 ? 12 : 0);
	}

	public int getHit() {
		return 100;
	}
	
	public boolean showThrowMessage(){
		return false;
	}
	
	public int getPathType() {
		return ProjectileSkill.PATH_LINEAR;
	}

	public int getRange() {
		return 15;
	}

	public String getSelfTargettedMessage() {
		return "The dagger flies away to the heavens";
	}

	public String getSFXID() {
		switch (getPlayer().getDaggerLevel()){
			case 0:
		        return "SFX_WHITE_DAGGER";
		    case 1:
		    	return "SFX_SILVER_DAGGER";
		    default:
		        return "SFX_GOLD_DAGGER";
		}
	}

	public String getShootMessage() {
		switch (getPlayer().getDaggerLevel()){
			case 0:
		        return "You throw a dagger!";
		    case 1:
		    	return "You throw a silver dagger!";
		    default:
		        return "You throw a glowing dagger!";
		}
	}
		
	public String getSpellAttackDesc() {
		return "dagger";
	}
	
	private boolean executing = false;
	public int getHeartCost() {
		if (executing)
			return 0;
		else
			return 5;
	}
	
	public void execute(){
		getPlayer().reduceHearts(getHeartCost());
		executing = true;
		for (int i = 0; i < 10; i++){
			SFXManager.play(getSFX());
			super.execute();
		}
		executing = false;
	}
}