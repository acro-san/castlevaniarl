package crl.action.monster;

import sz.util.Debug;
import sz.util.Position;
import crl.Main;
import crl.action.AT;
import crl.action.Action;
import crl.game.SFXManager;
import crl.level.Cell;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Damage;
import crl.player.Player;

public class MonsterCharge extends Action {
	
	private int range;
	private String message;
	private String effectWav;

	private int damage;
	
	public AT getID() {
		return AT.MonsterCharge;
	}
	
	public boolean needsDirection() {
		return true;
	}

	public void set(int pRange, String pMessage, int pDamage, String pEffectWav){
		message = pMessage;
		range = pRange;
		damage = pDamage;
		effectWav = pEffectWav;
	}
	
	public void execute(){
		Debug.doAssert(performer instanceof Monster, "Someone not a monster tried to JumpOver");
		Monster aMonster = (Monster) performer;
		targetDirection = aMonster.starePlayer();
		Position var = directionToVariation(targetDirection);
		Level aLevel = performer.level;
		Player aPlayer = aLevel.getPlayer();
		aLevel.addMessage("The "+aMonster.getDescription()+" "+message+".");
		Cell currentCell = aLevel.getMapCell(performer.pos);
		Position destinationPoint = null;
		if (effectWav != null) {
			SFXManager.play(effectWav);
		}
		for (int i=0; i<range; i++) {
			destinationPoint = Position.add(aMonster.pos, var);
			if (!aLevel.isValidCoordinate(destinationPoint))
				break;
			Position deepPoint  = aLevel.getDeepPosition(destinationPoint);
			if (deepPoint == null){
				aLevel.addMessage("The "+aMonster.getDescription()+ " falls into an endless pit!");
				performer.die();
				performer.level.removeMonster(aMonster);
				break;
			}
			Cell destinationCell = aLevel.getMapCell(deepPoint);
			if (!aMonster.isEthereal() &&
					( destinationCell.isSolid() || destinationCell.getHeight() > currentCell.getHeight()+aMonster.getLeaping() +1
					)
				) {
				aLevel.addMessage("The "+aMonster.getDescription()+ " bumps into the "+destinationCell.getShortDescription());
				break;
			}

			if (!aMonster.isEthereal() && !aMonster.canSwim() && destinationCell.isShallowWater()){
				aLevel.addMessage("The "+aMonster.getDescription()+ " falls into the "+ destinationCell.getShortDescription() + "!");
				performer.die();
				performer.level.removeMonster(aMonster);
				break;
			}
			if (aPlayer.pos.equals(destinationPoint)){
				if (aPlayer.getStandingHeight() == aMonster.getStandingHeight()){
					aPlayer.damage("The "+aMonster.getDescription()+ " hits you!", aMonster, new Damage((damage==0?aMonster.getAttack():damage), false));
				} else if (aPlayer.getStandingHeight() > aMonster.getStandingHeight()){
					aLevel.addMessage("The "+aMonster.getDescription()+ "'s attack passes beneath you!");
				} else {
					aLevel.addMessage("The "+aMonster.getDescription()+ "'s attack passes over you!");
				}
				if (i == range - 1) {
					// Prevent enemy from landing over player
					break;
				}
			}
			aMonster.pos = destinationPoint;
			if (i < range - 1) {
				Main.ui.safeRefresh(); // We need to show the player the enemy in the updated position
				actionAnimationPause();
			}
		}
	}

	public String getPromptDirection() {
		return "Where do you want to whip?";
	}

	public int getCost() {
		Monster m = (Monster) performer;
		if (m.getAttackCost() == 0) {
			Debug.say("quickie monster");
			return 10;
		}
		return m.getAttackCost();
	}
	
}