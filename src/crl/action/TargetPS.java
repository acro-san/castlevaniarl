package crl.action;

import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.actor.Actor;
import crl.item.Item;
import crl.item.ItemDefinition;
import crl.monster.Monster;
import crl.player.Player;

public class TargetPS extends ProjectileSkill {

	private Player player;
	private Item weapon;
	private int reloadTime;
	
	public AT getID() {
		return AT.Target;
	}

	public void execute() {
		player = null;
		reloadTime = 0;
		try {
			player = (Player) performer;
		} catch (ClassCastException cce) {
			return;
		}
		weapon = player.weapon;
		
		ItemDefinition weaponDef = weapon.getDefinition();

		if (weapon.getReloadTurns() > 0) {
			if (weapon.getRemainingTurnsToReload() == 0) {
				/*
				aLevel.addMessage("You must reload the "+weapon.getDescription()+"!");
				return;*/
				if (!reload(weapon, player))
					return;
			}
		}

		super.execute();
		
		if (weapon.getReloadTurns() > 0 &&
			weapon.getRemainingTurnsToReload() > 0)
			weapon.setRemainingTurnsToReload(weapon.getRemainingTurnsToReload()-1);
		if (weaponDef.isSingleUse) {
			if (weapon.getReloadTurns() > 0) {
				if (weapon.getRemainingTurnsToReload() == 0) {
					player.weapon = null;
				}
			} else {
				if (player.hasItem(weapon))
					player.reduceQuantityOf(weapon);
				else
					player.weapon = null;
			}
		}
	}

	public String getPromptPosition() {
		return "Where do you want to attack?";
	}

	public Position getPosition() {
		return targetPosition;
	}

	public String getSFX() {
		Player p = (Player)performer;
		weapon = p.weapon;
		if (weapon != null && !weapon.getAttackSound().equals("DEFAULT")) {
			return weapon.getAttackSound();
		} else {
			if (((Player)performer).sex == Player.MALE)
				return "wav/punch_male.wav";
			else
				return "wav/punch_female.wav";
		}
	}

	public int getCost() {
		return player.getAttackCost()+weapon.getAttackCost()+reloadTime;
	}
	
	private boolean reload(Item weapon, Player aPlayer) {
		// TODO See/dedupe 'Reload' skill? Already have this logic, dont we?
		
		if (weapon != null) {
			if (aPlayer.getGold() < weapon.getDefinition().reloadCostGold) {
				aPlayer.level.addMessage("You can't reload the " + weapon.getDescription());
				return false;
			}else if (aPlayer.getHearts() < 1){
				aPlayer.level.addMessage("You can't reload the " + weapon.getDescription());
				return false;
			} else {
				weapon.reload();
				aPlayer.reduceGold(weapon.getDefinition().reloadCostGold);
				aPlayer.reduceHearts(1);
				aPlayer.level.addMessage("You reload the " + 
				weapon.getDescription()+" ("+
					weapon.getDefinition().reloadCostGold+" gold)");
				reloadTime = 10*weapon.getDefinition().reloadTurns;
				return true;
			}
		} else {
			aPlayer.level.addMessage("You can't reload yourself");
		}
		return false;
	}
	
	
	public boolean canPerform(Actor a) {
		player = null;
		try {
			player = (Player) a;
		} catch (ClassCastException cce){
			return false;
		}
		
		//Item 
		weapon = player.weapon;
		
		if (!player.canAttack()) {
			invalidationMessage = "You can't attack!";
			return false;
		}

		if (weapon == null) {
			invalidationMessage = "It is useless to target your own blows...";
			return false;
		}

		// wtf specific string naming heck.:!?
		String wfxID = "SFX_WP_" + weapon.getDefinition().getID();
		// this seems a bad way to go about querying if a targeted projectile
		// can be directed. surely that should be a simple field on the skill?
		if (!Main.efx.isDirectedEffect(wfxID)) {
			invalidationMessage = "You cannot target your " + weapon.getDescription() + ". Attack on a direction instead!";
			return false;
		}
		
		if (weapon.getRange() < 2) {
			invalidationMessage = "You can't target your "+weapon.getDescription();
			return false;
		}
		
		if (player.weapon != null && 
			player.weapon.getWeaponCategory() == ItemDefinition.CAT_BOWS) {
			Monster nearest = player.getNearestMonster();
			if (nearest != null) {
				if (Position.flatDistance(nearest.pos, player.pos) < 2){
					invalidationMessage = "You can't aim your "+player.weapon.getDescription()+" this close to the enemy, get away!";
					return false;
				}
			}
		}

		return true;
	}
	


	public int getDamage() {
		return player.getWeaponAttack()+Util.rand(0,2);
	}
	
	public boolean isWeaponAttack() {
		return true;
	}

	public int getHit() {
		return 100;
	}


	public int getPathType() {
		if (weapon.getVerticalRange() > 0)
			return PATH_DIRECT;
		else
			return PATH_LINEAR;
	}


	public int getRange() {
		return weapon.getDefinition().range;
	}

	public String getSelfTargettedMessage() {
		if (weapon.getRange() > 5)
			return "You fire your "+weapon.getDescription()+" upward";
		else
			return "You attack with your "+weapon.getDescription();
	}

	public String getSFXID() {
		return "SFX_WP_"+weapon.getDefinition().getID();
	}


	public String getShootMessage() {
		if (weapon.getRange() > 5)
			return "You fire your "+weapon.getDescription();
		else
			return "You attack with your "+weapon.getDescription();
	}


	public String getSpellAttackDesc() {
		return "attack";
	}


	public boolean piercesThru() {
		return weapon.isSlicesThrough();
	}


	public int getHeartCost() {
		return 0;
	}
}