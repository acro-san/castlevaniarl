package crl.action.vkiller;

import sz.util.Line;
import sz.util.Position;
import crl.action.BeamProjectileSkill;
import crl.level.Cell;
import crl.level.Level;
import crl.player.Player;
import crl.ui.UserInterface;
import crl.ui.effects.EffectFactory;

public class ItemBreakSacredFist extends BeamProjectileSkill{
	public String getID(){
		return "ItemBreakSacredFist";
	}
	
	public int getCost(){
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}
	
	public int getDamage() {
		return 25 +
		 2*getPlayer().getSoulPower();
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
		Line line = new Line(getPlayer().getPosition(), targetPosition);
		Player player = getPlayer();
		Level level = getPlayer().getLevel();
		Position runner = line.next();
		int i = 0;
		for (; i < 5; i++){
			runner = line.next();
        	Cell destinationCell = performer.getLevel().getMapCell(runner);
			if (
				level.isWalkable(runner) &&	
				destinationCell.getHeight() == level.getMapCell(player.getPosition()).getHeight() 
			)
				;
			else
				break;
		}
		drawEffect(EffectFactory.getSingleton().createDirectedEffect(player.getPosition(), targetPosition, "SFX_TELEPORT", i));
		
		player.setPosition(new Position(runner));
		player.see();
		UserInterface.getUI().refresh();
		
	}
	
	
}