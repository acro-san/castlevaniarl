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

	private transient MonsterDefinition definition;
	private String defID;

	protected int hits;	// HEALTHPOINTS / HP. 'hits' is ambiguous. like a question. 'hits'?
	private int maxHits;	// MAX HP!
	
	
	public String featurePrize;
	public boolean isVisible = true;

	private boolean wasSeen = false;
	
	public Monster enemy;
	
	public String getWavOnHit() {
		return getDefinition().wavOnHit;
	}
	
	public void setWasSeen(boolean value) {
		wasSeen = true;
	}
	
	public boolean wasSeen(){
		return wasSeen;
	}

	public void increaseHits(int i){
		hits += i;
	}

	public void act(){
		if (hasCounter(Consts.C_MONSTER_FREEZE) || hasCounter(Consts.C_MONSTER_SLEEP)){
			setNextTime(50);
			updateStatus();
			return;
		}
		super.act();
		wasSeen = false;
	}

	public boolean isInWater(){
		if (level.getMapCell(getPosition())!= null)
			return level.getMapCell(getPosition()).isShallowWater();
		else
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
		Position pp = level.getPlayer().getPosition();
		/*if (!playerInRange())
			return false;
		//Debug.say("pp"+pp);
		//Debug.say(getPosition());
		if (pp.x == getPosition().x || pp.y == getPosition().y)
			return true;
		if (pp.x - getPosition().x == pp.y - getPosition().y)
			return true;
		return false;
	}*/

	/** returns the direction in which the player is seen */
	public int starePlayer() {
		Player pl = level.getPlayer();
		if (pl == null || pl.isInvisible() || pl.getPosition().z != getPosition().z) {
			return -1;
		}
		Position pp = level.getPlayer().getPosition();
		//mp = my/monster pos...
		if (Position.flatDistance(pp, getPosition()) <= getDefinition().sightRange) {
			if (pp.x == getPosition().x) {
				if (pp.y > getPosition().y) {
					return Action.DOWN;
				} else {
					return Action.UP;
				}
			} else
			if (pp.y == getPosition().y) {
				if (pp.x > getPosition().x) {
					return Action.RIGHT;
				} else {
					return Action.LEFT;
				}
			} else
			if (pp.x < getPosition().x) {
				if (pp.y > getPosition().y)
					return Action.DOWNLEFT;
				else
					return Action.UPLEFT;
			} else {
				if (pp.y > getPosition().y)
					return Action.DOWNRIGHT;
				else
					return Action.UPRIGHT;
			}
		}
		return -1;
	}

	public void damageWithWeapon(StringBuffer message, int dam) {
		Player pl = level.getPlayer();
		Item wep = pl.getWeapon();
		if (wep == null) {
			pl.increaseWeaponSkill(ItemDefinition.CAT_UNARMED);
		} else {
			pl.increaseWeaponSkill(wep.getDefinition().weaponCategory);
		}
		damage(message, dam);
	}
	
	public void damage(StringBuffer message, int dam) {
		if (selector instanceof DraculaAI) {
			((DraculaAI)selector).setOnBattle(true);
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
		hits -= dam;
		Main.ui.drawEffect(Main.efx.createLocatedEffect(getPosition(), "SFX_QUICK_WHITE_HIT"));
		Player pl = level.getPlayer();
		if (getDefinition().isBleedable()) {
			if (pl.hasCounter(Consts.C_BLOOD_THIRST) &&
					Position.flatDistance(getPosition(), pl.getPosition()) < 3) {
				int recover = (int)Math.ceil(getDefinition().bloodContent / 30);	// why 30?
				level.addMessage("You drink some of the "+getDefinition().description+" blood! (+"+recover+")");
				pl.recoverHits(recover);
			}
			if (Util.chance(40)) {
				level.addBlood(getPosition(), Util.rand(0,1));
			}
		}
		if (pl.getFlag("HEALTH_REGENERATION") && Util.chance(30)) {	// *RANDOM* regen?! Why not slow tickrate?
			pl.recoverHits(1);
		}

		if (isDead()) {
			if (this == level.boss) {
				//if (!level.isWalkable(getPosition())){
					//level.addMessage("You get a castle key!");
					pl.addKeys(1);
				/*} else
					setFeaturePrize("KEY");*/
				//level.addEffect(new DoubleSplashEffect(getPosition(), "O....,,..,.,.,,......", Appearance.RED, ".,,,,..,,.,.,..,,,,,,", Appearance.WHITE));
				Main.ui.drawEffect(Main.efx.createLocatedEffect(getPosition(), "SFX_BOSS_DEATH"));
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
			if (featurePrize != null && !level.getMapCell(getPosition()).isSolid())
				if (level.getMapCell(getPosition()).isShallowWater()) {
					level.addMessage("A "+FeatureFactory.getDescriptionForID(featurePrize) +" falls into the " + level.getMapCell(getPosition()).getDescription());
					level.addFeature(featurePrize, getPosition());
				} else {
					level.addFeature(featurePrize, getPosition());
				}
			
			if (getDefinition().isBleedable()) {
				Position runner = new Position(-1,-1,getPosition().z);
				for (runner.x = -1; runner.x <= 1; runner.x++) {
					for (runner.y = -1; runner.y <= 1; runner.y++) {
						if (Util.chance(70)) {
							level.addBlood(Position.add(getPosition(), runner), Util.rand(0,1));
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
	
	public boolean isDead() {
		return hits <= 0;
	}

	public String getDescription() {
		// This may be flavored with specific monster daya
		return getDefinition().description + (hasCounter(Consts.C_MONSTER_CHARM) ? " C ":"");
	}

	private MonsterDefinition getDefinition() {
		if (definition == null) {
			if (this instanceof NPC)
				definition = NPC.NPC_MONSTER_DEFINITION;
			else
				definition = MonsterData.getDefinition(defID);
		}
		return definition;
	}
	
	//swimming/ethereal/undead NPCs are the whole reason this has 'getDefinition()' as a method inside!???
	public boolean canSwim() {
		return getDefinition().canSwim;
	}

	public boolean isUndead() {
		return getDefinition().isUndead;
	}

	public boolean isEthereal() {
		return getDefinition().isEthereal;
	}

	// FIXME Rename as HEALTHPOINTS.
	public int getHits() {
		return hits;
	}

	public Monster(MonsterDefinition md) {
		definition = md;
		defID = md.ID;
		//selector = md.getDefaultSelector();
		selector = md.defaultSelector.derive();
		
		hits = md.maxHits;
		maxHits = md.maxHits;
	}

/*
	public String getFeaturePrize() {
		return featurePrize;
	}

	public void setFeaturePrize(String value) {
		featurePrize = value;
	}
*/
	
	public int getAttack() {
		return getDefinition().attack;
	}

	public int getLeaping(){
		return getDefinition().leaping;
	}
	
	
	public boolean waitsPlayer() {
		return false;
	}


	private void setPrize() {
		Player p = level.getPlayer();
		String [] prizeList = null;
		
		
		if (p.deservesMUpgrade()) {
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
				if (p.getFlag("MYSTIC_CRYSTAL") && Util.chance(20))
        			prizeList = new String[]{"CRYSTALWP"};
        		else if (p.getFlag("MYSTIC_FIST") && Util.chance(20))
        			prizeList = new String[]{"FISTWP"};
        		else if (p.getFlag("MYSTIC_CROSS") && Util.chance(20))
        			prizeList = new String[]{"CROSSWP"};
        		else if (p.getFlag("MYSTIC_STOPWATCH") && Util.chance(20))
        			prizeList = new String[]{"STOPWATCHWP"};
        		else if (p.getFlag("MYSTIC_HOLY_WATER") && Util.chance(20))
        			prizeList = new String[]{"HOLYWP"};
        		else if (p.getFlag("MYSTIC_HOLY_BIBLE") && Util.chance(20))
        			prizeList = new String[]{"BIBLEWP"};
        		else 
        			prizeList = new String[]{"AXEWP", "DAGGERWP"};
        	} else
        	if (Util.chance(50))
	        if (Util.chance(40))
    	    if (Util.chance(10))
        	if (Util.chance(10))
	        if (Util.chance(10))
    	    if (Util.chance(10))
   	    		prizeList = new String[]{"WHITE_MONEY_BAG"};
			else
				prizeList = new String[]{"POT_ROAST"};
			else
				prizeList = new String[]{"INVISIBILITY_POTION", "ROSARY", "BLUE_MONEY_BAG"};
			else
				prizeList = new String[]{"RED_MONEY_BAG"};
			else
				prizeList = new String[]{"BIGHEART"};
			else
				prizeList = new String[]{"SMALLHEART", "COIN"};
    	} else {
	        if (Util.chance(50))
    	    if (Util.chance(50))
        	if (Util.chance(10))
	        if (Util.chance(10))
    	    if (Util.chance(10))
    	    	prizeList = new String[]{"WHITE_MONEY_BAG"};
			else
				prizeList = new String[]{"POT_ROAST"};
			else
				prizeList = new String[]{"INVISIBILITY_POTION", "ROSARY", "BLUE_MONEY_BAG"};
			else
				prizeList = new String[]{"RED_MONEY_BAG"};
			else
				prizeList = new String[]{"BIGHEART"};
			else
				prizeList = new String[]{"SMALLHEART", "COIN"};
    	}

		if (prizeList != null) {
			featurePrize = Util.pick(prizeList);
		}
	}
	
	public void die() {
		super.die();
		level.removeMonster(this);
		if (getAutorespawncount() > 0) {
			Emerger em = new Emerger(MonsterData.buildMonster(getDefinition().ID), getPosition(), getAutorespawncount());
			level.addActor(em);
			em.selector = new EmergerAI();
			em.level = level;
		}
	}
	
	
	/*
	public void setVisible(boolean value) {
		visible = value;
	}
	
	public boolean isVisible() {
		return visible;
	}
	*/
	
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
			penalty = (int)(Position.distance(getPosition(), attackOrigin)/4);
			if (attacker.getWeapon().isHarmsUndead() && isUndead()) {
				magicalDamage *= 2;
			}
			attacker.increaseWeaponSkill(attacker.getWeapon().getDefinition().weaponCategory);
			
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
			double percent = (double)damage / getDefinition().maxHits;
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
			int distance = Position.flatDistance(getPosition(), monster.getPosition());
			if (monster != this && distance < minDist){
				minDist = distance;
				nearMonster = monster;
			}
		}
		return nearMonster;
	}
	
	
	public int stareMonster(Monster who) {
		if (who.getPosition().z != getPosition().z) {
			return -1;
		}
		if (Position.flatDistance(who.getPosition(), getPosition()) <= getDefinition().sightRange) {
			Position pp = who.getPosition();
			if (pp.x == getPosition().x) {
				if (pp.y > getPosition().y) {
					return Action.DOWN;
				} else {
					return Action.UP;
				}
			} else if (pp.y == getPosition().y) {
				if (pp.x > getPosition().x){
					return Action.RIGHT;
				} else {
					return Action.LEFT;
				}
			} else if (pp.x < getPosition().x) {
				if (pp.y > getPosition().y)
					return Action.DOWNLEFT;
				else
					return Action.UPLEFT;
			} else {
				if (pp.y > getPosition().y)
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
		Line sight = new Line(getPosition(), level.getPlayer().getPosition());
		Position point = sight.next();
		while (!point.equals(level.getPlayer().getPosition())) {
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
		double percent = (double)damage / getDefinition().maxHits;
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
	
	// TODO Rename getMaxHP()
	public int getMaxHits() {
		return getDefinition().maxHits;
	}
	
	public boolean isFlying() {
		return getDefinition().canFly;
	}
	
	public void recoverHits() {
		hits = maxHits;
	}
	
	
}