package crl.action;

import sz.util.Debug;
import sz.util.Position;
import crl.Main;
import crl.actor.Actor;
import crl.feature.Feature;
import crl.item.Merchant;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.npc.NPC;
import crl.player.Consts;
import crl.player.Damage;
import crl.player.Player;

public class Jump extends Action {
	
	private Player aPlayer;
	
	public AT getID() {
		return AT.Jump;
	}
	
	public boolean needsDirection() {
		if (aPlayer.hasCounter(Consts.C_BATMORPH) || 
			aPlayer.hasCounter(Consts.C_BATMORPH2) || 
			aPlayer.isSwimming()) {
			return false;
		}
		return true;
	}


	public boolean canPerform(Actor a) {
		aPlayer = getPlayer(a);
		return super.canPerform(a);
	}


	public String getPromptDirection() {
		return "Where do you want to jump?";
	}


	public String getSFX() {
		Player p = (Player) performer;
		if (p.sex == Player.MALE) {
			return "wav/jump_male.wav";
		} else {
			return "wav/jump_female.wav";
		}
	}

	public int getCost() {
		return (int)(aPlayer.getWalkCost()*1.3);
	}

	public void execute() {
		Debug.doAssert(performer instanceof Player,
			"Walk action, tried for not player");
		aPlayer = (Player) performer;
		Position var = directionToVariation(targetDirection);
		Level aLevel = performer.level;
		if (aPlayer.isSwimming()) {
			if (aLevel.getMapCell(aPlayer.pos).isShallowWater()) {
				aLevel.addMessage("You are already floating");
			} else {
				if (aPlayer.pos.z != 0){
					Position deep = new Position(aPlayer.pos);
					deep.z--;
					if (aLevel.getMapCell(deep).isShallowWater()){
						aLevel.addMessage("You float to the surface");
						aPlayer.landOn(deep);
					} else {
						aLevel.addMessage("You can't float upward");
					}
				}else {
					aLevel.addMessage("You can't float upward");
				}
			}
			return;
		}
		if (aPlayer.hasCounter(Consts.C_BATMORPH) || aPlayer.hasCounter(Consts.C_BATMORPH2)) {
			if (aPlayer.getStandingHeight() > 3) {
				if (aPlayer.pos.z != 0) {
					Position deep = new Position(aPlayer.pos);
					deep.z--;
					if (aLevel.getMapCell(deep).getID().equals("AIR")) {
						aLevel.addMessage("You fly upward");
						aPlayer.pos = deep;
						aPlayer.setHoverHeight(0);
					} else {
						aLevel.addMessage("You can't fly upward");
					}
				} else {
					aLevel.addMessage("You can't fly upward");
				}
			} else {
				aLevel.addMessage("You fly upward.");
				aPlayer.setHoverHeight(aPlayer.getHoverHeight()+1);
			}
			return;
		}
		
		if (targetDirection == Action.SELF) {
			aLevel.addMessage("You jump upward");
			return;
		}
		int startingHeight = aLevel.getMapCell(performer.pos).getHeight();
		Position startingPosition = new Position(aPlayer.pos);
		int jumpingRange = 4;
		if (aPlayer.hasIncreasedJumping()) {
			jumpingRange++;
		}
		aLevel.addMessage("You jump.");
		boolean messaged = false;
		aPlayer.setJustJumped(true);
		Cell currentCell = aLevel.getMapCell(startingPosition);
		aPlayer.doJump(aPlayer.getStandingHeight());
		out: for (int i = 1; i < jumpingRange; i++) {
			Position destinationPoint = Position.add(startingPosition, Position.mul(var, i));
			Cell destinationCell = aLevel.getMapCell(destinationPoint);
			/*if (destinationCell == null)
				break out;*/
			if (destinationCell == null){
				if (!aLevel.isValidCoordinate(destinationPoint)) {
					destinationPoint = Position.subs(destinationPoint, var);
					aPlayer.landOn(destinationPoint);
					break out;
				}
				if (i < jumpingRange-1) {
					aPlayer.pos = destinationPoint;
					Main.ui.safeRefresh();
					actionAnimationPause();
					continue out;
				} else {
					aPlayer.landOn(destinationPoint);
					break out;
				}
			}
			Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
			if (destinationFeature != null && destinationFeature.getKeyCost() > aPlayer.getKeys()) {
				aPlayer.land();
				break out;
			}
			if (destinationCell.getHeight() > startingHeight+2) {
				aPlayer.land();
				break out;
			} else {
				if (!messaged && destinationCell.getHeight() < startingHeight) {
					aLevel.addMessage("You fly from the "+currentCell.getShortDescription()+ "!");
					messaged = true;
				}
				if (!destinationCell.isSolid()) {
					if (i < jumpingRange-1) {
						aPlayer.pos = destinationPoint;
						Main.ui.safeRefresh();
						actionAnimationPause();
					} else {
						aPlayer.landOn(destinationPoint);
					}
				} else {
					aLevel.addMessage("You bump into the "+destinationCell.getShortDescription());
					aPlayer.land();
					break out;
				}
			}
			Monster aMonster = aLevel.getMonsterAt(destinationPoint);
			if (aMonster != null && !(aMonster instanceof Merchant || aMonster instanceof NPC)) {
				// Damage the poor player and bounce him back
				if (aPlayer.damage("You are bounced back by the "+aMonster.getDescription()+"!", aMonster, new Damage(aMonster.getAttack(), false)))
					aLevel.getPlayer().bounceBack(Position.mul(var, -1), 3);
				break out;
			}
		}
		aPlayer.stopJump();
		aLevel.addMessage("You hold your breath.");		// FIXME: What? this is confusing for the player.
	}
}
