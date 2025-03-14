package crl.monster;

import sz.util.*;
import crl.Main;
import crl.action.*;
import crl.item.*;
import crl.level.Emerger;
import crl.level.EmergerAI;
import crl.npc.NPC;
import crl.feature.*;
import crl.ui.*;
import crl.ai.monster.boss.DraculaAI;
import crl.player.Consts;
import crl.player.Player;
import crl.actor.*;

public class Monster extends Actor implements Cloneable {

	protected String defID;	// monsterType? enum? also used in 'NPC' subclass.
	private transient MonsterDef definition;
	
	public int hp;
	
	public String featurePrize;
	public boolean isVisible = true;

	private boolean wasSeen = false;
	
	public Monster enemy;
	

	public Monster(MonsterDef md) {
		definition = md;
		defID = md.ID;
		if (md.defaultSelector == null) {
			boolean debuggery = true;
			// what monster def has null AI def???!
		}
		selector = md.defaultSelector.derive();
		hp = md.maxHP;
	}
	
	
	public int getMaxHP() {	// where's this being called and why's it not
		// just using hpMax value of THIS MONSTER!?
		return getDefinition().maxHP;
	}
	
	public void recoverAllHP() {
		hp = getDefinition().maxHP;
	}

	public boolean isDead() {
		return hp <= 0;
	}
	
	public String getWavOnHit() {
		return getDefinition().wavOnHit;
	}
	
	public void setWasSeen(boolean value) {
		wasSeen = true;
	}
	
	public boolean wasSeen() {
		return wasSeen;
	}


	public void act() {
		if (hasCounter(Consts.C_MONSTER_FREEZE) || hasCounter(Consts.C_MONSTER_SLEEP)) {
			setNextTime(50);
			updateStatus();
			return;
		}
		super.act();
		wasSeen = false;
	}


	public boolean isInWater() {
		if (level.getMapCell(pos) != null) {
			return level.getMapCell(pos).isShallowWater();
		}
		return false;
	}


	public void freeze(int cont) {
		setCounter(Consts.C_MONSTER_FREEZE, cont);
	}


	public int getFreezeResistance() {
		return 0; //placeholder
	}

	public Appearance getAppearance() {
		return getDefinition().appearance;
	}


	public Object clone() {
		try {
			return super.clone();
		} catch (Exception x) {
			return null;
		}
	}


	/*public boolean playerInRow(){
		Position pp = level.getPlayer().pos;
		/*if (!playerInRange())
			return false;
		//Debug.say("pp"+pp);
		//Debug.say(pos);
		if (pp.x == pos.x || pp.y == pos.y)
			return true;
		if (pp.x - pos.x == pp.y - pos.y)
			return true;
		return false;
	}*/

	/** returns the direction in which the player is seen */
	public int starePlayer() {
		Player pl = level.getPlayer();
		if (pl == null || pl.isInvisible() || pl.pos.z != pos.z) {
			return -1;
		}
		Position pp = level.getPlayer().pos;
		//mp = my/monster pos...
		if (Position.flatDistance(pp, pos) <= getDefinition().sightRange) {
			if (pp.x == pos.x) {
				if (pp.y > pos.y) {
					return Action.DOWN;
				} else {
					return Action.UP;
				}
			} else
			if (pp.y == pos.y) {
				if (pp.x > pos.x) {
					return Action.RIGHT;
				} else {
					return Action.LEFT;
				}
			} else
			if (pp.x < pos.x) {
				if (pp.y > pos.y)
					return Action.DOWNLEFT;
				else
					return Action.UPLEFT;
			} else {
				if (pp.y > pos.y)
					return Action.DOWNRIGHT;
				else
					return Action.UPRIGHT;
			}
		}
		return -1;
	}


	public void damageWithWeapon(StringBuffer message, int dam) {
		Player pl = level.getPlayer();
		Item wep = pl.weapon;
		if (wep == null) {
			pl.increaseWeaponSkill(ItemDefinition.CAT_UNARMED);
		} else {
			pl.increaseWeaponSkill(wep.getDefinition().weaponCategory);
		}
		damage(message, dam);
	}
	
	
	public void damage(StringBuffer message, int dam) {
		if (selector instanceof DraculaAI) {
			((DraculaAI)selector).inBattle = true;
		}
		if (Util.chance(getEvadeChance())) {
			if (wasSeen())
				level.addMessage("The "+getDescription()+" "+getEvadeMessage());
			return;
		}
		if (hasCounter(Consts.C_MONSTER_FREEZE)) {
			dam *= 2;
		}
		message.append(" ("+dam+")");
		hp -= dam;
		Main.ui.drawEffect(Main.efx.createLocatedEffect(pos, "SFX_QUICK_WHITE_HIT"));
		Player pl = level.getPlayer();
		if (getDefinition().isBleedable()) {
			if (pl.hasCounter(Consts.C_BLOOD_THIRST) &&
					Position.flatDistance(pos, pl.pos) < 3) {
				int recover = (int)Math.ceil(getDefinition().bloodContent / 30);	// why 30?
				level.addMessage("You drink some of the "+getDefinition().description+" blood! (+"+recover+")");
				pl.heal(recover);
			}
			if (Util.chance(40)) {
				level.addBlood(pos, Util.rand(0,1));
			}
		}
		if (pl.getFlag("HEALTH_REGENERATION") && Util.chance(30)) {	// *RANDOM* regen?! Why not slow tickrate?
			pl.heal(1);
		}

		if (isDead()) {
			if (this == level.boss) {
				//if (!level.isWalkable(pos)){
					//level.addMessage("You get a castle key!");
					pl.addKeys(1);
				/*} else
					setFeaturePrize("KEY");*/
				//level.addEffect(new DoubleSplashEffect(pos, "O....,,..,.,.,,......", Appearance.RED, ".,,,,..,,.,.,..,,,,,,", Appearance.WHITE));
				Main.ui.drawEffect(Main.efx.createLocatedEffect(pos, "SFX_BOSS_DEATH"));
				level.addMessage("The whole level trembles with holy energy!");
				level.removeBoss();
				pl.addHistoricEvent("vanquished the "+this.getDescription()+" on the "+level.getDescription());
				level.anihilate();
				level.removeRespawner();
				//level.getPlayer().addSoulPower(Util.rand(10,20)*level.getLevelNumber());
			} else {
				pl.increaseMUpgradeCount();
				setPrize();
			}
			if (featurePrize != null && !level.getMapCell(pos).isSolid())
				if (level.getMapCell(pos).isShallowWater()) {
					level.addMessage("A "+FeatureFactory.getDescriptionForID(featurePrize) +" falls into the " + level.getMapCell(pos).getDescription());
					level.addFeature(featurePrize, pos);
				} else {
					level.addFeature(featurePrize, pos);
				}
			
			if (getDefinition().isBleedable()) {
				Position runner = new Position(-1,-1,pos.z);
				for (runner.x = -1; runner.x <= 1; runner.x++) {
					for (runner.y = -1; runner.y <= 1; runner.y++) {
						if (Util.chance(70)) {
							level.addBlood(Position.add(pos, runner), Util.rand(0,1));
						}
					}
				}
			}
			die();
			pl.score += getDefinition().score;
			pl.addXP(getDefinition().score);
			//pl.addSoulPower(Util.rand(0,3));
			pl.getGameSessionInfo().addDeath(getDefinition());
		}
	}


	public int getScore() {
		return getDefinition().score;
	}


	public String getDescription() {
		// This may be flavored with specific monster daya
		return getDefinition().description + (hasCounter(Consts.C_MONSTER_CHARM) ? " C ":"");
	}


	private MonsterDef getDefinition() {
		// last minute decision making!? shouldn't this already be set on init?
		if (definition == null) {
			if (this instanceof NPC)
				definition = NPC.NPC_MONSTER_DEFINITION;
			else
				definition = MonsterData.getDefinition(defID);
		}
		return definition;
	}


	//swimming/ethereal/undead NPCs are the whole reason this has
	// 'getDefinition()' as a method inside!?
	public boolean canSwim() {
		return getDefinition().canSwim;
	}

	public boolean isUndead() {
		return getDefinition().isUndead;
	}

	public boolean isEthereal() {
		return getDefinition().isEthereal;
	}


	public int getAttack() {
		return getDefinition().attack;
	}

	public int getLeaping(){
		return getDefinition().leaping;
	}
	
	
	public boolean waitsPlayer() {
		return false;
	}


	// FIXME Convoluted crap code, obscuring what actual chances there are of 
	// any of the given branches being taken. also: terrible for branch predictor.
	// solution: why not just store the chances in a droptable and roll once!???
	private void setPrize() {	// OUTCOME == SIDE EFFECT!! When is this called? ONCE?!
		Player p = level.getPlayer();
		String[] prizeList = null;
		
		if (p.deservesMUpgrade()) {	// this implies it's not set at mapgen time?
			featurePrize = "MUPGRADE";
			return;
		}
		
		if (p.deservesUpgrade() && Util.chance(50)) {
			featurePrize = "UPGRADE";
		}
		
		if (Util.chance(60)) {
			return;
		}
		
		// FIXME See 'Feature.java' logic. is there overlap with this??
		if (p.playerClass == Player.CLASS_VAMPIREKILLER) {
			if (Util.chance(20)) {
				// Will get a mystic weapon
				if (p.getFlag("MYSTIC_CRYSTAL") && Util.chance(20)) {
					prizeList = new String[]{"CRYSTALWP"};
				} else if (p.getFlag("MYSTIC_FIST") && Util.chance(20)) {
					prizeList = new String[]{"FISTWP"};
				} else if (p.getFlag("MYSTIC_CROSS") && Util.chance(20)) {
					prizeList = new String[]{"CROSSWP"};
				} else if (p.getFlag("MYSTIC_STOPWATCH") && Util.chance(20)) {
					prizeList = new String[]{"STOPWATCHWP"};
				} else if (p.getFlag("MYSTIC_HOLY_WATER") && Util.chance(20)) {
					prizeList = new String[]{"HOLYWP"};
				} else if (p.getFlag("MYSTIC_HOLY_BIBLE") && Util.chance(20)) {
					prizeList = new String[]{"BIBLEWP"};
				} else {
					prizeList = new String[]{"AXEWP", "DAGGERWP"};
				}
			} else {
				if (Util.chance(50)) {
					if (Util.chance(40)) {
						if (Util.chance(10)) {
							if (Util.chance(10)) {
								if (Util.chance(10)) {
									if (Util.chance(10)) {
										prizeList = new String[]{"WHITE_MONEY_BAG"};
									} else {
										prizeList = new String[]{"POT_ROAST"};
									}
								} else {
									prizeList = new String[]{"INVISIBILITY_POTION", "ROSARY", "BLUE_MONEY_BAG"};
								}
							} else {
								prizeList = new String[]{"RED_MONEY_BAG"};
							}
						} else {
							prizeList = new String[]{"BIGHEART"};
						}
					} else {
						prizeList = new String[]{"SMALLHEART", "COIN"};
					}
				}
			}
		} else {
			if (Util.chance(50)) {
				if (Util.chance(50)) {
					if (Util.chance(10)) {
						if (Util.chance(10)) {
							if (Util.chance(10)) {
								prizeList = new String[]{"WHITE_MONEY_BAG"};
							} else {
								prizeList = new String[]{"POT_ROAST"};
							}
						} else {
							prizeList = new String[]{"INVISIBILITY_POTION", "ROSARY", "BLUE_MONEY_BAG"};
						}
					} else {
						prizeList = new String[]{"RED_MONEY_BAG"};
					}
				} else {
					prizeList = new String[]{"BIGHEART"};
				}
			} else {
				prizeList = new String[]{"SMALLHEART", "COIN"};
			}
		}
		
		if (prizeList != null) {
			featurePrize = Util.pick(prizeList);
		}
	}
	
	
	public void die() {
		super.die();
		level.removeMonster(this);
		if (getAutorespawncount() > 0) {
			Emerger em = new Emerger(MonsterData.buildMonster(getDefinition().ID), pos, getAutorespawncount());
			level.addActor(em);
			em.selector = new EmergerAI();
			em.level = level;
		}
	}


	public int getAttackCost() {
		return getDefinition().attackCost;
	}

	public int getWalkCost() {
		return getDefinition().walkCost;
	}
	
	public String getID() {
		return getDefinition().ID;
	}
	
	public int getEvadeChance() {
		return getDefinition().evadeChance;
	}
	
	public String getEvadeMessage(){
		return getDefinition().evadeMessage;
	}
	
	public int getAutorespawncount() {
		return getDefinition().autorespawnCount;
	}
	
	public boolean tryMagicHit(Player attacker, int magicalDamage,
			int magicalHit, boolean showMsg, String attackDesc, 
			boolean isWeaponAttack, Position attackOrigin)
	{
		int hitChance = 100 - getEvadeChance();
		hitChance = (int)Math.round((hitChance + magicalHit)/2.0d);
		int penalty = 0;
		if (isWeaponAttack) {
			penalty = (int)(Position.distance(pos, attackOrigin)/4);
			if (attacker.weapon.isHarmsUndead() && isUndead()) {
				magicalDamage *= 2;
			}
			attacker.increaseWeaponSkill(attacker.weapon.getDefinition().weaponCategory);
			
		}
		
		magicalDamage -= penalty;
		int evasion = 100 - hitChance;
		
		if (evasion < 0)
			evasion = 0;
		
		if (hasCounter(Consts.C_MONSTER_CHARM))
			setCounter(Consts.C_MONSTER_CHARM, 0);
		if (hasCounter("SLEEP"))
			evasion = 0;
		//see if evades it
		if (Util.chance(evasion)){
			if (showMsg)
				level.addMessage("The "+getDescription()+" evades the "+attackDesc+"!");
			//moveRandomly();
			return false;
		} else {
			if (hasCounter("SLEEP")){
				level.addMessage("You wake up the "+getDescription()+ "!");
				setCounter("SLEEP", 0);
			}
			int baseDamage = magicalDamage;
			double damageMod = 1;
			StringBuffer hitDesc = new StringBuffer();
			int damage = (int)(baseDamage * damageMod);
			double percent = (double)damage / getDefinition().maxHP;
			if (percent > 1.0)
				hitDesc.append("The "+attackDesc+ " whacks the "+getDescription()+ " apart!!");
			else if (percent > 0.7)
				hitDesc.append("The "+attackDesc+ " smashes the "+getDescription()+ "!");
			else if (percent > 0.5)
				hitDesc.append("The "+attackDesc+ " grievously hits the "+getDescription()+ "!");
			else if (percent> 0.3)
				hitDesc.append("The "+attackDesc+ " hits the "+getDescription()+ ".");
			else
				hitDesc.append("The "+attackDesc+ " barely scratches the "+getDescription()+ "...");
			
			damage(hitDesc, (int)(baseDamage*damageMod));
			if (showMsg)
				level.addMessage(hitDesc.toString() );
			//attacker.setLastWalkingDirection(Action.SELF);
			return true;
		}
	}
	
	public String getLongDescription() {
		return getDefinition().longDescription;
	}

	/*
	public Monster getEnemy() {
		return enemy;
	}

	public void setEnemy(Monster enemy) {
		this.enemy = enemy;
	}
	*/
	
	/** Returns the direction in which the nearest monster is seen */
	public int stareMonster(){
		Monster nearest = getNearestMonster();
		if (nearest == null)
			return -1;
		else
			return stareMonster(getNearestMonster());
	}
	
	public Monster getNearestMonster(){
		VMonster monsters = level.getMonsters();
		Monster nearMonster = null;
		int minDist = 150;
		for (int i = 0; i < monsters.size(); i++){
			Monster monster = (Monster) monsters.elementAt(i);
			int distance = Position.flatDistance(pos, monster.pos);
			if (monster != this && distance < minDist){
				minDist = distance;
				nearMonster = monster;
			}
		}
		return nearMonster;
	}
	
	
	public int stareMonster(Monster who) {
		if (who.pos.z != pos.z) {
			return -1;
		}
		if (Position.flatDistance(who.pos, pos) <= getDefinition().sightRange) {
			Position pp = who.pos;
			if (pp.x == pos.x) {
				if (pp.y > pos.y) {
					return Action.DOWN;
				} else {
					return Action.UP;
				}
			} else if (pp.y == pos.y) {
				if (pp.x > pos.x){
					return Action.RIGHT;
				} else {
					return Action.LEFT;
				}
			} else if (pp.x < pos.x) {
				if (pp.y > pos.y)
					return Action.DOWNLEFT;
				else
					return Action.UPLEFT;
			} else {
				if (pp.y > pos.y)
					return Action.DOWNRIGHT;
				else
					return Action.UPRIGHT;
			}
		}
		return -1;
	}
	
	
	public boolean seesPlayer() {
		if (!wasSeen()) {	// so if player can't see enemy, enemy can't see player?
			// this seems very limiting. nothing can get the jump on you if you FoW-isolate them?
			return false;
		}
		Line sight = new Line(pos, level.getPlayer().pos);
		Position point = sight.next();
		while (!point.equals(level.getPlayer().pos)) {
			if (level.getMapCell(point)!= null && level.getMapCell(point).isOpaque()){
				return false;
			}
			point = sight.next();
			if (!level.isValidCoordinate(point))
				return false;
		}
		return true;
	}
	
	
	public boolean tryHit(Monster attacker) {
		enemy = attacker;
		int evasion = getEvadeChance();
		//level.addMessage("Evasion "+evasion);
		if (hasCounter("SLEEP")) {
			evasion = 0;
		}
		//level.addMessage("Evasion "+evasion);
		//see if evades it
		int weaponAttack = attacker.getDefinition().attack;
		if (Util.chance(evasion)) {
			level.addMessage("The "+getDescription()+ " dodges the "+attacker.getDescription()+" attack!");
			return false;
		}
		
		if (hasCounter(Consts.C_MONSTER_SLEEP)) {
			level.addMessage("The "+attacker.getDescription()+" wakes up the "+getDescription()+ "!");
			setCounter(Consts.C_MONSTER_SLEEP, 0);
		}
		int baseDamage = weaponAttack;
		double damageMod = 1;
		StringBuffer hitDesc = new StringBuffer();
		int damage = (int)(baseDamage * damageMod);
		double percent = (double)damage / getDefinition().maxHP;
		if (percent > 1.0d)
			hitDesc.append("The "+attacker.getDescription()+" whacks the "+getDescription()+ " apart!!");
		else if (percent > 0.7d)
			hitDesc.append("The "+attacker.getDescription()+" smashes the "+getDescription()+ "!");
		else if (percent > 0.5d)
			hitDesc.append("The "+attacker.getDescription()+" grievously hits the "+getDescription()+ "!");
		else if (percent> 0.3d)
			hitDesc.append("The "+attacker.getDescription()+" hits the "+getDescription()+ ".");
		else
			hitDesc.append("The "+attacker.getDescription()+" barely scratches the "+getDescription()+ "...");
		
		damage(hitDesc, (int)(baseDamage*damageMod));
		level.addMessage(hitDesc.toString());
		return true;
	
	}
	
	
	public boolean isFlying() {
		return getDefinition().canFly;
	}
	
	
}