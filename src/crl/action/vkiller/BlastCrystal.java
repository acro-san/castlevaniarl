package crl.action.vkiller;

import crl.action.AT;
import crl.action.HeartAction;
import crl.level.Level;
import crl.player.Player;

public class BlastCrystal extends HeartAction {
	
	public int getHeartCost() {
		return 5;
	}
	
	public AT getID() {
		return AT.MW_BlastCrystal;// "BLAST_CRYSTAL";
	}
	
	public void execute() {
		super.execute();
//		Player aPlayer = (Player)performer;
		Level aLevel = performer.level;
		aLevel.addMessage("You release a mystic crystal!");
		aLevel.addSmartFeature("BLAST_CRYSTAL", performer.pos);
	}
	
	public int getCost() {
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}
	
}