package crl.action.vkiller;

import sz.util.Position;
import crl.action.AT;
import crl.action.Action;
import crl.action.HeartAction;
import crl.level.Level;
import crl.player.Player;

public class ItemBreakBlastCrystal extends HeartAction {
	
	public AT getID() {
		return AT.ItemBreak_BlastCrystal;	// "BLAST_CRYSTAL"; <- this looks buggy potentially!
	}
	
	public void execute() {
		super.execute();
		Level aLevel = performer.level;
		aLevel.addMessage("You release a handful of mystic crystals!");
		// *WHAT* is this STRING?! FIXME is 'BLAST_CRYSTAL' the ORIGINAL ACTION'S
		// ID, or... something else? SmartFeatures have different ID system? Or no?
		
		aLevel.addSmartFeature("BLAST_CRYSTAL", Position.add(performer.pos, Action.VARUP));
		aLevel.addSmartFeature("BLAST_CRYSTAL", Position.add(performer.pos, Action.VARDN));
		aLevel.addSmartFeature("BLAST_CRYSTAL", Position.add(performer.pos, Action.VARLF));
		aLevel.addSmartFeature("BLAST_CRYSTAL", Position.add(performer.pos, Action.VARRG));
	}
	
	public int getCost() {
		Player p = (Player)performer;
		return (int)(25 / (p.getShotLevel()+1));
	}
	
	public int getHeartCost() {
		return 10;
	}
	

}