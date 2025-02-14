package crl.action;

import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.item.Item;
import crl.item.ItemDefinition;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Consts;
import crl.player.Player;
import crl.ui.effects.Effect;

public class Attack extends Action {
	
	private int reloadTime;
	private Item weapon;
	
	public String getID() {
		return "Attack";
	}
	
	public boolean needsDirection() {
		return true;
	}


	public void execute() {
		Position var = directionToVariation(targetDirection);
		Player player = null;
		reloadTime = 0;
		try {
			player = (Player)performer;
		} catch (ClassCastException cce){
			return;
		}
		
		weapon = player.getWeapon();
		Level aLevel = performer.level;
		if (!player.canAttack()) {
			aLevel.addMessage("You can't attack!");
			return;
		}
		
		/*if (player.hasCounter(Consts.C_WOLFMORPH) || player.hasCounter(Consts.C_WOLFMORPH2))
			weapon = null;*/
		
		if (weapon == null || weapon.getDefinition().weaponCategory.equals(ItemDefinition.CAT_UNARMED)) {
			if (targetDirection == Action.SELF && aLevel.getMonsterAt(player.pos) == null){
				aLevel.addMessage("Don't hit yourself");
				return;
			}
			Position targetPosition = Position.add(player.pos, Action.directionToVariation(targetDirection)); 
			Monster targetMonster = aLevel.getMonsterAt(targetPosition);
			String attackDescription = player.getPunchDescription();
			int punchDamage = player.getPunchDamage();
			int push = player.getPunchPush();
			
			if (targetMonster != null && targetMonster.getStandingHeight() == player.getStandingHeight()){
				StringBuffer buff = new StringBuffer("You "+attackDescription+" the "+targetMonster.getDescription()+"!");
				targetMonster.damageWithWeapon(buff, punchDamage);
				aLevel.addMessage(buff.toString());
				if (push != 0)
					pushMonster(targetMonster, aLevel,push);

			}
			Feature targetFeature = aLevel.getFeatureAt(targetPosition);
			if (targetFeature != null && targetFeature.isDestroyable()){
				aLevel.addMessage("You "+attackDescription+" the "+targetFeature.getDescription());
				targetFeature.damage(player, punchDamage);
			}
			
			Cell targetMapCell = aLevel.getMapCell(targetPosition);
			if (targetMapCell != null && targetMapCell.isSolid()){
				aLevel.addMessage("You "+attackDescription+" the "+targetMapCell.getShortDescription());
			}
			return;
		}

		if (targetDirection == Action.SELF && aLevel.getMonsterAt(player.pos) == null) {
			aLevel.addMessage("That's a coward way to give up!");
			return;
		}
		
		targetPosition = Position.add(performer.pos, Action.directionToVariation(targetDirection));
		int startHeight = aLevel.getMapCell(player.pos).getHeight() + player.getHoverHeight();
		ItemDefinition weaponDef = weapon.getDefinition();

		if (weapon.getReloadTurns() > 0)
			if (weapon.getRemainingTurnsToReload() == 0) {
				if (!reload(weapon, player))
					return;
			}

		String [] sfx = weaponDef.attackSFX.split(" ");
		if (sfx.length > 0) {
			if (sfx[0].equals("MELEE")) {
				Effect me = Main.efx.createDirectionalEffect(performer.pos, targetDirection, weapon.getRange(), "SFX_WP_"+weaponDef.getID());
				Main.ui.drawEffect(me);
			} else if (sfx[0].equals("BEAM")) {
				Effect me = Main.efx.createDirectedEffect(performer.pos, targetPosition, "SFX_WP_"+weaponDef.getID(), weapon.getRange());
				Main.ui.drawEffect(me);
			} else if (sfx[0].equals("MISSILE")) {
				Effect me = Main.efx.createDirectedEffect(performer.pos, targetPosition, "SFX_WP_"+weaponDef.getID(), weapon.getRange());
				if (!weapon.isSlicesThrough()) {
					int i = 0;
					for (i=0; i<weapon.getRange(); i++) {
						Position destinationPoint = Position.add(performer.pos,
							Position.mul(var, i+1));
						Cell destinationCell = aLevel.getMapCell(destinationPoint);
						Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
						if (destinationFeature != null && destinationFeature.isDestroyable()) {
							break;
						}
						Monster targetMonster = performer.level.getMonsterAt(destinationPoint);
						if (
							targetMonster != null &&
							!(targetMonster.isInWater() && targetMonster.canSwim()) &&
							(destinationCell.getHeight() == aLevel.getMapCell(player.pos).getHeight() ||
							destinationCell.getHeight() -1  == aLevel.getMapCell(player.pos).getHeight() ||
							destinationCell.getHeight() == aLevel.getMapCell(player.pos).getHeight()-1)
						)
							break;
					}
					me = Main.efx.createDirectedEffect(performer.pos, targetPosition, "SFX_WP_"+weaponDef.getID(), i);
				}
				Main.ui.drawEffect(me);
			}
		}
		
		boolean hitsSomebody = false;
		boolean hits = false;
		for (int i=0; i<weapon.getRange(); i++) {
			Position destinationPoint = Position.add(performer.pos,
				Position.mul(var, i+1));
			Cell destinationCell = aLevel.getMapCell(destinationPoint);
			/*aLevel.addMessage("You hit the "+destinationCell.getID());*/
			
			String message = "";
			
			Monster targetMonster = performer.level.getMonsterAt(destinationPoint);
			message = "";

			if (targetMonster != null) {
				if ((targetMonster.isInWater() && targetMonster.canSwim())|| destinationCell.getHeight() < startHeight-1) {
					if (targetMonster.wasSeen())
						aLevel.addMessage("The attack passes over the "+targetMonster.getDescription());
				} else {
					if (destinationCell.getHeight() > startHeight + 1){
						if (weapon.getVerticalRange()>0){
							hits = true;
						} else {
							if (targetMonster.wasSeen()){
								aLevel.addMessage("The attack passes under the "+targetMonster.getDescription());
							}
						}
					} else {
						hits = true;
					}
				}
			}
			
			if (hits){
				hits = false;
				hitsSomebody = true;
				int penalty = (int)(Position.distance(targetMonster.pos, player.pos)/4);
				int attack = player.getWeaponAttack()+Util.rand(0,2);
				attack -= penalty;
				if (attack < 1)
					attack = 1;
				StringBuffer hitMsg = new StringBuffer();
				if (weapon.isHarmsUndead() && targetMonster.isUndead()){
					attack *= 2;
					if (targetMonster.wasSeen())
						hitMsg.append("You critically damage the "+targetMonster.getDescription()+"!");
					else
						hitMsg.append("You hit something!");
				} else {
					if (targetMonster.wasSeen())
						hitMsg.append("You hit the "+targetMonster.getDescription());
					else
						hitMsg.append("You hit something!");
				}
					
				//targetMonster.damage(player.getWhipLevel());
				
				targetMonster.damageWithWeapon(hitMsg, attack);
				aLevel.addMessage(hitMsg.toString());
			}
			
			Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
			if (destinationFeature != null && destinationFeature.isDestroyable()) {
				hitsSomebody = true;
				if (player.sees(destinationPoint)) {
					message = "You hit the "+destinationFeature.getDescription();
				} else {
					message = "You hit something";
				}
				destinationFeature.damage(player, weapon.getAttack());
				aLevel.addMessage(message);
			}
			
			Cell targetMapCell = aLevel.getMapCell(destinationPoint);
			if (targetMapCell != null && targetMapCell.isSolid()) {
				hitsSomebody = true;
				//aLevel.addMessage("You hit the "+targetMapCell.getShortDescription());
			}
			
			if (hitsSomebody && !weapon.isSlicesThrough())
				break;
		}
/*		if (!hitsSomebody)
			aLevel.addMessage("You swing at the air!");*/
		if (!hitsSomebody && player.hasCounter(Consts.C_FIREBALL_WHIP)) {
			Action fireball = new WhipFireball();
			fireball.setPerformer(performer);
			fireball.setPosition(targetPosition);
			fireball.execute();
		}
		if (weapon.getReloadTurns() > 0 &&
			weapon.getRemainingTurnsToReload() > 0)
			weapon.setRemainingTurnsToReload(weapon.getRemainingTurnsToReload()-1);
		if (weaponDef.isSingleUse) {
			if (weapon.getReloadTurns() > 0) {
				if (weapon.getRemainingTurnsToReload() == 0) {
					player.setWeapon(null);
				}
			} else {
				if (player.hasItem(weapon))
					player.reduceQuantityOf(weapon);
				else
					player.setWeapon(null);
			}
		}
	}


	public String getPromptDirection() {
		return "Where do you want to attack?";
	}


	public int getDirection(){
		return targetDirection;
	}


	private boolean reload(Item weapon, Player aPlayer) {
		if (weapon == null) {
			aPlayer.level.addMessage("You can't reload yourself");
			return false;
		}
		
		if (aPlayer.getGold() < weapon.getDefinition().reloadCostGold) {
			aPlayer.level.addMessage("You can't afford to reload the " + weapon.getDescription());
			return false;
		}
		if (aPlayer.getHearts() < 2) {
			aPlayer.level.addMessage("You can't reload the " + weapon.getDescription());
			return false;
		}
		
		weapon.reload();
		aPlayer.reduceGold(weapon.getDefinition().reloadCostGold);
		aPlayer.reduceHearts(2);
		aPlayer.level.addMessage("You reload the " + weapon.getDescription()+" ("+weapon.getDefinition().reloadCostGold+" gold)");
		reloadTime = 10*weapon.getDefinition().reloadTurns;
		return true;
	}
	
	
	public int getCost() {
		Player player = (Player) performer;
		if (weapon != null){
			return player.getAttackCost() + weapon.getAttackCost()+reloadTime;
		} else {
			return (int)(player.getAttackCost() * 1.5);
		}
	}


	public String getSFX() {
		Player p = (Player)performer;
		weapon = p.getWeapon();
		if (weapon != null && !weapon.getAttackSound().equals("DEFAULT")) {
			return weapon.getAttackSound();
		} else {
			if (p.sex == Player.MALE)
				return "wav/punch_male.wav";
			else
				return "wav/punch_female.wav";
		}
	}

	private void pushMonster(Monster targetMonster, Level aLevel, int spaces) {
		Position varP = Action.directionToVariation(targetDirection);
		Position runner = Position.add(targetMonster.pos, varP);
		for (int i = 0; i < spaces; i++) {
			Cell fly = aLevel.getMapCell(runner);
			if (fly == null)
				return;
			if (!fly.isSolid()) {
				if (fly.isWater() || fly.isShallowWater()) {
					if (targetMonster.canSwim()) {
						if (i == spaces-1 ) aLevel.addMessage("You throw the "+targetMonster.getDescription()+" into the water!");
						targetMonster.pos = runner;
					} else if (targetMonster.isEthereal()) {
						targetMonster.pos = runner;
					} else {
						aLevel.addMessage("You throw the "+targetMonster.getDescription()+" into the water!");
						aLevel.addMessage("The "+targetMonster.getDescription()+" drowns!");
						targetMonster.die();
						aLevel.removeMonster(targetMonster);
						return;
					}
				} else {
					targetMonster.pos = runner;
				}
			} else {
				StringBuffer buff = new StringBuffer("You smash the "+targetMonster.getDescription()+" against the "+fly.getDescription()+"!");
				targetMonster.damage(buff, 2);
				aLevel.addMessage(buff.toString());
			}
			runner.add(varP);
		}

	}
	
	public boolean canPerform(Actor a) {
		Player player = getPlayer(a);
		if (!player.canAttack()){
			invalidationMessage = "You can't attack";
			return false;
		}
		if (player.getWeapon() != null && player.getWeapon().getWeaponCategory() == ItemDefinition.CAT_BOWS){
			Monster nearest = player.getNearestMonster();
			if (nearest != null){
				if (Position.distance(nearest.pos, player.pos) < 2){
					invalidationMessage = "You can't aim your "+player.getWeapon().getDescription()+" this close to the enemy, get away!";
					return false;
				}
			}
		}
		return true;
	}
	
	
}