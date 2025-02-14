package crl.action.monster;

import sz.util.Debug;
import sz.util.Position;
import crl.action.Action;
import crl.ai.monster.BasicMonsterAI;
import crl.feature.Feature;
import crl.feature.SmartFeature;
import crl.game.SFXManager;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Consts;
import crl.player.Damage;

public class MonsterWalk extends Action {
	
	public String getID() {
		return "MonsterWalk";
	}
	
	public boolean needsDirection() {
		return true;
	}

	public void execute() {
		Debug.doAssert(performer instanceof Monster, "The player tried to MonsterWalk...");
		Monster mon = (Monster)performer;
		Position var = directionToVariation(targetDirection);
		Position destinationPoint = Position.add(performer.pos, var);
		Level aLevel = performer.level;
		if (!aLevel.isValidCoordinate(destinationPoint)) {
			return;
		}
		Cell destinationCell = aLevel.getMapCell(destinationPoint);
		Cell currentCell = aLevel.getMapCell(performer.pos);
		
		Monster destinationMonster = aLevel.getMonsterAt(destinationPoint);
		Feature destinationFeature = aLevel.getFeatureAt(destinationPoint);
		SmartFeature standing = aLevel.getSmartFeature(performer.pos);
		if (standing != null) {
			if (standing.getEffectOnStep() != null) {
				String[] effects = standing.getEffectOnStep().split(" ");
				if (effects[0].equals("TRAP") && mon != aLevel.boss) {
					aLevel.addMessage("The "+mon.getDescription()+" is trapped!");
					return;
				}
			}
		}
		
		if (destinationFeature != null && destinationFeature.isSolid()) {
			//if (Util.chance(50)) {
			if (mon.wasSeen())
				aLevel.addMessage("The "+mon.getDescription()+" hits the "+destinationFeature.getDescription());
			destinationFeature.damage(mon);
			//}
		}
		if (destinationCell == null ||
				destinationCell.isSolid() ||
				(destinationCell.isEthereal() && !mon.isFlying())
			)
			if (mon.selector instanceof BasicMonsterAI) {
				if (((BasicMonsterAI)mon.selector).getPatrolRange() > 0) {
					((BasicMonsterAI)mon.selector).setChangeDirection(true);
				}
			}
		if (destinationCell != null &&
				(!destinationCell.isEthereal() || (destinationCell.isEthereal() && (mon.isEthereal() || mon.isFlying())))
			) {
			if (destinationFeature != null && destinationFeature.isSolid()) {
				;
			} else {
				if (mon.isEthereal() || !destinationCell.isSolid()) {
					if (destinationMonster == null) {
						if (mon.isEthereal() || 
							mon.isFlying() || 
							currentCell == null || 
							destinationCell.getHeight() <= currentCell.getHeight()+mon.getLeaping() + 1)
						{
							if (destinationCell.isShallowWater()) {
								if (mon.canSwim() || mon.isEthereal()) {
									performer.pos = destinationPoint;
								}
							} else if (destinationCell.isWater()) {
								if (mon.canSwim() || mon.isEthereal()) {
									performer.pos = destinationPoint;
								}
							} else {
								if (aLevel.getPlayer().pos.equals(destinationPoint)) {
									if (aLevel.getPlayer().getStandingHeight() == mon.getStandingHeight()) {
										if (mon.getWavOnHit() != null) {
											SFXManager.play(mon.getWavOnHit());
										}
										aLevel.getPlayer().damage("You are hit by the "+mon.getDescription()+"!", mon, new Damage(mon.getAttack(), false));
									} else if (aLevel.getPlayer().getStandingHeight() > mon.getStandingHeight()){
										aLevel.addMessage("The "+mon.getDescription()+ " walks beneath you");
										performer.pos = destinationPoint;
										/*Esto deberia estar en un landing*/
										if (aLevel.getSmartFeature(destinationPoint) != null) {
											SmartFeature sf = aLevel.getSmartFeature(destinationPoint);
											if (sf.damageOnStep > 0) {
												StringBuffer buff = new StringBuffer("The "+mon.getDescription()+" is injured by the " + sf.getDescription());
												mon.damage(buff, sf.damageOnStep);
												aLevel.addMessage(buff.toString());
											}
										}
									} else {
										aLevel.addMessage("The "+mon.getDescription()+ " hovers over you!");
										performer.pos = destinationPoint;
										if (aLevel.getSmartFeature(destinationPoint) != null) {
											SmartFeature sf = aLevel.getSmartFeature(destinationPoint);
											if (sf.damageOnStep > 0) {
												StringBuffer buff = new StringBuffer("The "+mon.getDescription()+" is injured by the " + sf.getDescription());
												mon.damage(buff, sf.damageOnStep);
												aLevel.addMessage(buff.toString());
											}
										}
									}
								} else {
									performer.pos = destinationPoint;
									if (aLevel.getSmartFeature(destinationPoint) != null) {
										SmartFeature sf = aLevel.getSmartFeature(destinationPoint);
										if (sf.damageOnStep > 0) {
											StringBuffer buff = new StringBuffer("The "+mon.getDescription()+" is injured by the " + sf.getDescription());
											mon.damage(buff, sf.damageOnStep);
											aLevel.addMessage(buff.toString());
										}
									}
								}
							}
						}
					} else {
						if (mon.hasCounter(Consts.C_MONSTER_CHARM) || mon.enemy == destinationMonster) {
							destinationMonster.tryHit((Monster)performer);
						}
					}
				}
			}
		}
	}


	public int getCost() {
		Monster m = (Monster)performer;
		if (m.getWalkCost() == 0) {
			Debug.say("quickie monster");
			return 10;
		}
		return m.getWalkCost();
	}

}