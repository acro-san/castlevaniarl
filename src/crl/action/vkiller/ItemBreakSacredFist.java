package crl.action.vkiller;

import sz.util.Line;
import sz.util.Position;
import crl.Main;
import crl.action.AT;
import crl.action.BeamProjectileSkill;
import crl.level.Cell;
import crl.level.Level;
import crl.player.Player;

public class ItemBreakSacredFist extends BeamProjectileSkill {
	
	public AT getID() {
		return AT.ItemBreak_SacredFist;	//"ItemBreakSacredFist";
	}
	
	public int getCost() {
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}
	
	public int getDamage() {
		return 25 + 2*getPlayer().getSoulPower();
	}
	
	public int getHeartCost() {
		return 10;
	}
	
	public int getHit() {
		return 100;
	}
	
	public int getPathType() {
		return PATH_LINEAR;
	}
	
	public String getPromptPosition() {
		return "Where do you want to punch?";
	}
	
	public int getRange() {
		return 6;
	}
	
	public String getSelfTargettedMessage() {
		return null;
	}
	
	public String getSFXID() {
		return "NONE";
	}
	
	public String getShootMessage() {
		return "You punch with all your mystic power!!!";
	}
	
	public String getSpellAttackDesc() {
		return "holy blow";
	}
	
	public boolean allowsSelfTarget() {
		return false;
	}
	
	public String plottedLocatedEffect() {
		return null;
	}
	
	public boolean piercesThru() {
		return true;
	}
	
	public void execute() {
		super.execute();
		Line line = new Line(getPlayer().pos, targetPosition);
		Player player = getPlayer();
		Level level = getPlayer().level;
		Position runner = line.next();
		int i = 0;
		for (; i < 5; i++) {
			runner = line.next();
			Cell destinationCell = performer.level.getMapCell(runner);
			if (level.isWalkable(runner) &&
				destinationCell.getHeight() == level.getMapCell(player.pos).getHeight()) {
				;
			} else {
				break;
			}
		}
		drawEffect(Main.efx.createDirectedEffect(player.pos, targetPosition, "SFX_TELEPORT", i));
		
		player.pos = new Position(runner);
		player.see();
		Main.ui.refresh();
	}
	
}