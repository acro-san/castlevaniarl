package crl.action.vkiller;

import crl.Main;
import crl.action.AT;
import crl.action.HeartAction;
import crl.feature.Feature;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import sz.util.Line;
import sz.util.Position;

public class AirDash extends HeartAction {
	
	public int getHeartCost() {
		return 5;
	}

	public AT getID(){
		return AT.AirDash;
	}
	
	public boolean needsPosition(){
		return true;
	}

	public String getPromptPosition(){
		return "Where do you want to dash?";
	}

	private int getDamage(){
		return 10 +
		getPlayer().getAttack();
	}
	public void execute(){
		super.execute();
		Player aPlayer = (Player) performer;
		Level aLevel = aPlayer.level;
		aLevel.addMessage("You jump and dash forward!");
		if (targetPosition.equals(performer.pos)){
			aLevel.addMessage("You fall back.");
			return;
		}
		
		int damage = getDamage();

//		boolean hit = false;
		Line fireLine = new Line(performer.pos, targetPosition);
		
//		boolean curved = false;
//		int flyStart = 0, flyEnd = 0;
		Position previousPoint = aPlayer.pos;
		int projectileHeight = aLevel.getMapCell(aPlayer.pos).getHeight();
		for (int i=0; i<4; i++){
			Position destinationPoint = fireLine.next();
			if (aLevel.isSolid(destinationPoint)){
				drawEffect(Main.efx.createDirectedEffect(performer.pos, targetPosition, "SFX_AIRDASH", i));
				aPlayer.landOn(previousPoint);
				return;
			}
			
			String message = "";
			
			int destinationHeight = aLevel.getMapCell(destinationPoint).getHeight();

			if (destinationHeight == projectileHeight){
				Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
				if (destinationFeature != null && destinationFeature.isDestroyable()) {
					message = "You hit the "+destinationFeature.getDescription();
					drawEffect(Main.efx.createDirectedEffect(performer.pos, targetPosition, "SFX_AIRDASH", i));
					Feature prize = destinationFeature.damage(aPlayer, damage);
					if (prize != null) {
						message += " and destroys it.";
					}
					aLevel.addMessage(message);
					aPlayer.landOn(previousPoint);
					return;
				}
			}
			Monster targetMonster = performer.level.getMonsterAt(destinationPoint);
			
			if (targetMonster != null){
				//int monsterHeight = destinationHeight + (targetMonster.isFlying() ? 1 : 0);
				int monsterHeight = destinationHeight + targetMonster.getHoverHeight();
				if (projectileHeight == monsterHeight){
					if (targetMonster.tryMagicHit(aPlayer,damage, 100, targetMonster.wasSeen(), "dash", false, performer.pos)){
						drawEffect(Main.efx.createDirectedEffect(aPlayer.pos, targetPosition, "SFX_AIRDASH", i));
//						hit = true;
						Position runner = new Position(destinationPoint);
						outa: for (int ii = 0; ii < 2; ii++) {
							Cell fly = aLevel.getMapCell(runner);
							if (fly == null)
								break outa;		// FIXME: puzzle out: why is this not CONTINUE? is it important it's restarting loop?
							if (!fly.isSolid()) {
								targetMonster.pos = runner;
							} else {
								StringBuffer byff = new StringBuffer("You smash the "+targetMonster.getDescription()+" against the "+fly.getDescription()+"!");
								targetMonster.damage(byff, damage);
								aLevel.addMessage(byff.toString());
							}
							//runner.add(varP);
							runner = fireLine.next();
						}
						aPlayer.landOn(previousPoint);
						return;
					};
				} else if (projectileHeight < monsterHeight) {
					aLevel.addMessage("You dash under the "+targetMonster.getDescription());
				} else {
					aLevel.addMessage("You dash over the "+targetMonster.getDescription());
				}
			}
			previousPoint = destinationPoint;
		}
		
		drawEffect(Main.efx.createDirectedEffect(aPlayer.pos, targetPosition, "SFX_AIRDASH", 4));
		aPlayer.landOn(previousPoint);
	}

	public int getCost() {
		Player p = (Player) performer;
		return (int)(p.getWalkCost() * 1.4);
	}
	
	public String getSFX(){
		return "wav/scrch.wav";
	}
}