package crl.action.monster;

import sz.util.Debug;
import sz.util.Line;
import sz.util.Position;
import crl.Main;
import crl.action.AT;
import crl.action.Action;
import crl.game.SFXManager;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Damage;
import crl.player.Player;

public class MonsterMissile extends Action {
	
	private int range;
	private String message;
	private String effectType;
	private String effectID;
	private String statusEffect;
	private String effectWav;
	private int damage;
	private String type;
	
	public String getEffectWav() {
		return effectWav;
	}
	
	public final static String
		TYPE_AXE = "AXE",
		TYPE_STRAIGHT = "STRAIGHT",
		TYPE_DIRECT = "DIRECT";
	
	public void set(String pType, String pStatusEffect, int pRange,
			String pMessage, String pEffectType, String pEffectID, int damage,
			String pEffectWav)
	{
		type = pType;
		range = pRange;
		statusEffect = pStatusEffect;
		message = pMessage;
		effectType = pEffectType;
		effectID = pEffectID;
		effectWav =  pEffectWav;
		this.damage = damage;
	}


	public AT getID() {
		return AT.MonsterMissile;
	}
	
	public boolean needsPosition() {
		return true;
	}

	public void execute() {
		Debug.doAssert(performer instanceof Monster, "Someone not a monster tried to throw a bone");
		Monster aMonster = (Monster)performer;
		Level aLevel = performer.level;
		Player aPlayer = aLevel.getPlayer();

		int targetDirection = solveDirection(performer.pos, targetPosition);
		if (effectWav != null) {
			SFXManager.play(effectWav);
		}
		if (effectType.equals("beam")) {
			drawEffect(Main.efx.createDirectedEffect(aMonster.pos, targetPosition, effectID,range));
		} else if (effectType.equals("melee")) {
			drawEffect(Main.efx.createDirectionalEffect(aMonster.pos, targetDirection, range, effectID));
		} else if (effectType.equals("missile")) {
			drawEffect(Main.efx.createDirectedEffect(aMonster.pos, targetPosition, effectID, range));
		} else if (effectType.equals("directionalmissile")) {
			drawEffect(Main.efx.createDirectedEffect(aMonster.pos, targetPosition, effectID , range));
		}
		Line line = new Line(aMonster.pos, targetPosition);
		boolean hits = false;
		for (int i=0; i<range; i++) {
			Position destinationPoint = line.next();
			if (aPlayer.pos.equals(destinationPoint)){
				int penalty = (int)(Position.distance(aMonster.pos, aPlayer.pos)/4);
				int attack = aMonster.getAttack();
				attack -= penalty;
				if (attack < 1)
					attack = 1;
				if (type.equals(TYPE_DIRECT)) {
					hits = true;
				} else if (type.equals(TYPE_STRAIGHT)) {
					//if (monsterCell.getHeight() == playerCell.getHeight()){
					if (aMonster.getStandingHeight() == aPlayer.getStandingHeight()) {
						hits = true;
					} else {
						hits = false;
					}
				} else if (type.equals(TYPE_AXE)) {
					if (i > (int)(range/4) && i < (int)(3*(range/4))){
						//if (playerCell.getHeight() == monsterCell.getHeight()+2 || playerCell.getHeight() == monsterCell.getHeight()+3){
						if (aMonster.getStandingHeight()+2 == aPlayer.getStandingHeight() || aMonster.getStandingHeight()+3 == aPlayer.getStandingHeight()){
							hits = true;
						} else {
							hits = false;
						}
					} else {
						if (aMonster.getStandingHeight() == aPlayer.getStandingHeight()){
							hits = true;
						} else {
							hits = false;
						}
					}
				}
				if (hits) {
					aLevel.addBlood(destinationPoint, 1);
					if (aPlayer.damage("The "+aMonster.getDescription()+ " hits you!", aMonster, new Damage((damage==0?aMonster.getAttack():damage), false))) {
						if (statusEffect != null){
							if (statusEffect.equals(Player.STATUS_STUN)){
								aLevel.addMessage("You are stunned!");
								aPlayer.setStun(8);
							} else if (statusEffect.equals(Player.STATUS_POISON)){
								aLevel.addMessage("Your blood is poisoned!");
								aPlayer.setPoison(15);
							} else if (statusEffect.equals(Player.STATUS_PETRIFY)){
								aLevel.addMessage("Your skin petrifies!");
								aPlayer.setPetrify(10);
							}
						}
					}
				}
				break;
			}
		}
		aLevel.addMessage("The "+aMonster.getDescription()+" "+message+".");
	}

	public String getPromptPosition() {
		return "";
	}


	public int getCost() {
		Monster m = (Monster)performer;
		if (m.getAttackCost() == 0){
			Debug.say("quickie monster");
			return 10;
		}
		return m.getAttackCost();
	}

}