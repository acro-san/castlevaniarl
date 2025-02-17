package crl.action.vkiller;

import crl.Main;
import crl.action.AT;
import crl.action.HeartAction;
import crl.feature.Feature;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Player;
import sz.util.Position;

public class Bible extends HeartAction {
	
	public static final int[] BIBLE_COORDS = {
		 1,0,	2,-1,	1,-2,	0,-2,	-1,-2,	-2,-1,	-2,0,	-2,1,
		-1,2,	0 ,2,	1, 2,	2,2,	3,1,	4,0,	4,-1,	4,-2,
		4,-3,	3,-4,	2,-4,	1,-4,	0,-4,	-1,-4,	-2,-4,	-3,-3,
		-4,-2,	-4,-1,	-4,0,	-4,1,	-4,2,	-3,3,	-2,4,	-1,4,
		0,4,	1,4,	2,4,	3,4,	4,3,	5,2,	6,1,	6,0,
		6,-1,	6,-2,	6,-3,	6,-4,	5,-5,	4,-6,	3,-7,	2,-8,
		1,-9,	0,-10,	-1,-11,	-2,-12,	-3,-13,	-4,-14,	-5,-15,	-6,-16
	};
	
	// it doesn't change length a lot so should just be a simple array...!
	public static final Position[] BIBLE_STEPS = new Position[BIBLE_COORDS.length/2];
	static {
		for (int i=0; i<BIBLE_STEPS.length; i+=2) {
			int j = i / 2;
			BIBLE_STEPS[j] = new Position(BIBLE_COORDS[i], BIBLE_COORDS[i+1]);
		}
	}
	
	
	public int getHeartCost() {
		return 2;
	}
	
	public AT getID(){
		return AT.MW_Bible;
	}

	private int getDamage() {
		return 7 + 
			getPlayer().getShotLevel() + 
			getPlayer().getSoulPower()*2;
	}
	
	public void execute(){
		super.execute();
		Level aLevel = performer.level;
		Player aPlayer = (Player) performer;
		aPlayer.level.addMessage("You open the bible!");
		//drawEffect(new SequentialEffect(performer.pos, steps, "?�", Appearance.CYAN, 10));
		drawEffect(Main.efx.createLocatedEffect(performer.pos, "SFX_BIBLE"));

		int damage = getDamage();
		for (int i = 0; i < BIBLE_STEPS.length; i++) {
			Position dest = Position.add(performer.pos, BIBLE_STEPS[i]);
			StringBuffer message = new StringBuffer();
			Feature destFeature = aLevel.getFeatureAt(dest);
			if (destFeature != null && destFeature.isDestroyable()) {
				message.append("The "+destFeature.getDescription()+" is slashed");
				Feature prize = destFeature.damage(aLevel.getPlayer(), damage);
				if (prize != null) {
					message.append(" and torn apart!");
				} else {
					message.append(".");
				}
				aLevel.addMessage(message.toString());
			}
			
			Monster targetMonster = performer.level.getMonsterAt(dest);
			message = new StringBuffer();
			if (targetMonster != null) {
				message.append("The "+targetMonster.getDescription()+" is slashed");
				//targetMonster.damage(player.getWhipLevel());
				targetMonster.damage(message, damage);
				if (targetMonster.isDead()) {
					message.append(" apart!");
					performer.level.removeMonster(targetMonster);
				} else {
					message.append(".");
				}
				if (targetMonster.wasSeen()) {
					aLevel.addMessage(message.toString());
				}
			}
		}
	}
	
	public int getCost() {
		Player p = (Player) performer;
		return (int)(25 / (p.getShotLevel()+1));
	}
	
	public String getPromptDirection() {
		return "Where do you want to throw the Cross?";
	}


	public String getSFX() {
		return "wav/loutwarp.wav";
	}
	
}