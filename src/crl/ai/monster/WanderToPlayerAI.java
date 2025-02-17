package crl.ai.monster;

import sz.util.Debug;
import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.action.Action;
import crl.action.monster.MonsterWalk;
import crl.actor.Actor;
import crl.ai.ActionSelector;
import crl.level.Cell;
import crl.monster.Monster;
import crl.player.Consts;


public class WanderToPlayerAI extends MonsterAI {
	/** This AI is used by those enemies that just walk until they find
	 * the player, optionally performing a ranged attack 
	 * him when he is in range */
	
	
	public Action selectAction(Actor who) {
		Debug.doAssert(who instanceof Monster, "WanderToPlayerAI selectAction");
		Monster aMonster = (Monster) who;
		
		if (aMonster.enemy != null) {
			if (!aMonster.level.getMonsters().contains(aMonster.enemy)){
				aMonster.enemy = null;
			}
		}

		if (aMonster.enemy != null || aMonster.hasCounter(Consts.C_MONSTER_CHARM)) {
			int directionToMonster = -1;
			if (aMonster.enemy != null) {
				directionToMonster = aMonster.stareMonster(aMonster.enemy);
			} else {
				directionToMonster = aMonster.stareMonster();
			}
			
			if (directionToMonster == -1) {
				//Walk TO player except if will bump him
				directionToMonster = aMonster.starePlayer();
				if (directionToMonster == -1) {
					return null;
				} else {
					Position targetPositionX = Position.add(who.pos, Action.directionToVariation(directionToMonster));
					if (!who.level.isWalkable(targetPositionX)){
						return null;
					} else {
						if (who.level.getPlayer().pos.equals(targetPositionX)) {
							return null;
						} else {
							Action ret = new MonsterWalk();
							ret.setDirection(directionToMonster);
							return ret;
						}
					}
				}
			} else {
				Action ret = new MonsterWalk();
				if (!who.level.isWalkable(Position.add(who.pos, Action.directionToVariation(directionToMonster)))){
					directionToMonster = Util.rand(0,7); 
					while (true){
						if (!Position.add(who.pos, Action.directionToVariation(directionToMonster)).equals(who.level.getPlayer().pos))
								break;
					}
					ret.setDirection(directionToMonster);
				} else {
					ret.setDirection(directionToMonster);
				}
				return ret;
			}
		}
		
		int directionToPlayer = aMonster.starePlayer();
		if (directionToPlayer == -1){
			//Wander aimlessly 
			int direction = Util.rand(0,7);
			Action ret = new MonsterWalk();
			ret.setDirection(direction);
			return ret;
		} else {
			int distanceToPlayer = Position.flatDistance(aMonster.pos, aMonster.level.getPlayer().pos);
			//Decide if will attack the player or walk to him
			if (Util.chance(50)){
				//Will try to attack the player
				if (rangedAttacks != null ){
					//Try
					for (int i = 0; i < rangedAttacks.size(); i++){
						RangedAttack ra = (RangedAttack)rangedAttacks.elementAt(i);
						if (distanceToPlayer <= ra.getRange())
							if (Util.chance(ra.getFrequency())){
								Action ret = Main.getAction(ra.getAttackId());
								ret.setDirection(directionToPlayer);
								return ret;
							}
					}
				}
			}
			// Couldnt attack the player, move to him
			Action ret = new MonsterWalk();
			ret.setDirection(directionToPlayer);
			Cell currentCell = aMonster.level.getMapCell(aMonster.pos);
			Cell destinationCell = aMonster.level.getMapCell(
				Position.add(
					aMonster.pos,
					Action.directionToVariation(directionToPlayer)
				));

			if (destinationCell == null || currentCell == null){
				ret.setDirection(Util.rand(0,7));
				return ret;
			}
			if ((destinationCell.isSolid() && !aMonster.isEthereal()) ||
				destinationCell.getHeight() > currentCell.getHeight()+aMonster.getLeaping() + 1 )
				ret.setDirection(Util.rand(0,7));
			return ret;
		}
	}


	public String getID(){
		return "WANDER";
	}


	public ActionSelector derive() {
		try {
			return (ActionSelector)clone();
		} catch (CloneNotSupportedException cnse) {
			return null;
		}
	}


}