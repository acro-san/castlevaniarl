package crl.player;

import sz.fov.FOV;
import sz.util.*;
import java.util.*;

import crl.item.*;
import crl.Main;
import crl.action.*;
import crl.action.renegade.*;
import crl.action.renegade.Teleport;
import crl.action.weapon.*;
import crl.action.vanquisher.*;
import crl.action.invoker.*;
import crl.action.knight.Defend;
import crl.action.manbeast.*;
import crl.action.vkiller.*;
import crl.actor.*;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.monster.*;
import crl.ui.*;
import crl.level.*;
import crl.feature.*;
import crl.game.Game;
import crl.game.PlayerGenerator;
import crl.game.SFXManager;
import crl.npc.*;
import crl.player.advancements.AdvDodge;
import crl.player.advancements.AdvDodge2;
import crl.player.advancements.Advancement;
import crl.player.advancements.renegade.*;
import crl.player.advancements.stats.*;
import crl.player.advancements.vkiller.*;
import crl.player.advancements.vanquisher.*;
import crl.player.advancements.invoker.*;
import crl.player.advancements.manbeast.*;

public class Player extends Actor {
	
	private Game game;
	
	private boolean doNotRecordScore = false;
	
	private static final int
		MAX_HPMAX = 60;		// MAXIMUM max-hp
	
	public static final Advancement
		ADV_MERCURY = new AdvMercury(),
		ADV_VENUS = new AdvVenus(),
		ADV_TERRA = new AdvTerra(),
		ADV_MARS = new AdvMars(),
		ADV_JUPITER = new AdvJupiter(),
		ADV_SATURN = new AdvSaturn(),
		ADV_URANUS = new AdvUranus(),
		ADV_NEPTUNE = new AdvNeptune(),
		ADV_PLUTO = new AdvPluto();
	
	public static final Advancement[] ALL_ADVANCEMENTS = {
		ADV_MERCURY,
		ADV_VENUS,
		ADV_TERRA,
		ADV_MARS,
		ADV_JUPITER,
		ADV_SATURN,
		ADV_URANUS,
		ADV_NEPTUNE,
		ADV_PLUTO
	};
	
	
	// Attributes
	private String name;
	public byte sex;
	public byte playerClass;	// damn java keyword!
	
	private String plot;
	private String plot2;
	private String description;

	// Status
	private int playerLevel = 1;
	private int xp;
	private int nextLevelXP = 1000; //5000
	private int hearts;
	private int heartMax;
	public int score;
	private int keys;
	private int carryMax;
	
	private int hp;
	private int hpMax;
	
	private int baseSightRange;
	private int breathing = 25;
	private int gold;
	private int soulPower;
	private Hashtable<String,Counter> weaponSkillsCounters = new Hashtable<>();
	private Hashtable<String,Counter> weaponSkills = new Hashtable<>();
	private Hashtable<String,String> customMessages = new Hashtable<>(); 
	/*private int[] weaponSkillsCounters = new int[13];
	private int[] weaponSkills = new int[13];*/
	private boolean justJumped = false;
	private Position previousPosition;

	private int walkCost = 50;
	private int attackCost = 50;
	private int castCost = 50;
	private int evadeChance;
	private int attack;
	// there's a 'defense' stat way further down but which was never read back/used anywhere!
	public int coolness;
	
	//Vampire Killer
	private int daggerLevel;
	private int currentMysticWeapon;
	private int whipLevel;
	private int shotLevel;
	private int minorHeartCount;
	private int mUpgradeCount;
	
	private GameSessionInfo gameSessionInfo;
	
	//Status Auxiliars
	private int invisibleCount;
	private int poisonCount;
	private int stunCount;
	private int petrifyCount;
	private int faintCount;
	
	//Relationships
	private transient PlayerEventListener playerEventListener;

	private Hostage currentHostage;
	private Monster enemy;
	
	private HashMap<String, Equipment> inventory = new HashMap<>();
	
	
	public void setAdvancementLevels(int[] advancementLevels) {
		this.advancementLevels = advancementLevels;
		statAdvancementLevels = new int[advancementLevels.length-1];
		for (int i = 0; i < advancementLevels.length-1; i++){
			statAdvancementLevels[i]= (int)Math.ceil((advancementLevels[i]+advancementLevels[i+1])/2.0D);
		}
	}

	public Monster getEnemy() {
		return enemy;
	}

	public void setEnemy(Monster enemy) {
		this.enemy = enemy;
	}

	public void addHistoricEvent(String description){
		gameSessionInfo.addHistoryItem(description);
	}
	
	public void setHostage(Hostage who){
		currentHostage = who;
	}
	
	public boolean hasHostage(){
		return currentHostage != null;
	}
	
	public Hostage getHostage(){
		return currentHostage;
	}


	public void addKeys(int x) {
		keys += x;
	}

	public void addGold(int x) {
		gold += x;
		score += x;
		gameSessionInfo.goldCount += x;	// why's it stored in 2 places though??
	}
	
	
	public void setGold(int x) {
		gold = x;
	}

	public int getGold() {
		return gold;
	}

	public void reduceGold(int q){
		gold -= q;
	}
/*
	public void addScore(int x) {
		score += x;
	}
	*/
	private int[] advancementLevels;
	public boolean deservesAdvancement(int level){
		for (int i = 0; i < advancementLevels.length; i++){
			if (advancementLevels[i] == level)
				return true;
		}
		return false;
	}
	
	private int[] statAdvancementLevels;
	public boolean deservesStatAdvancement(int level){
		for (int i = 0; i < statAdvancementLevels.length; i++){
			if (statAdvancementLevels[i] == level)
				return true;
		}
		return false;
	}
	
	private boolean deservesLevelUp = false;
	public void addXP(int x) {
		xp += x;
		if (xp >= nextLevelXP) {
			deservesLevelUp = true;
		}
	}
	
	public static final String
		INCREMENT_HITS = "hits",	// FIXME Rename INCREMENT_HP !!!
		INCREMENT_HEARTS = "hearts",
		INCREMENT_ATTACK = "attack",
		INCREMENT_COMBAT = "combat",
		INCREMENT_INVOKATION = "invok",
		INCREMENT_SPEED = "speed",
		INCREMENT_SOUL = "soul",
		INCREMENT_CARRYING = "carry",
		INCREMENT_DEFENSE = "defense",
		INCREMENT_EVADE = "evade";
	
	
	public String getLastIncrementString() {
		int temp = 0;
		String tempStr = "";
		temp = getLastIncrement(INCREMENT_HITS);
		if (temp > 0){
			tempStr+=" Hits+"+temp;
		}
		temp = getLastIncrement(INCREMENT_HEARTS);
		if (temp > 0){
			tempStr+=" Hearts+"+temp;
		}
		temp = getLastIncrement(INCREMENT_ATTACK);
		if (temp > 0){
			tempStr+=" Atk+"+temp;
		}
		temp = getLastIncrement(INCREMENT_COMBAT);
		if (temp > 0){
			tempStr+=" Combat+"+temp;
		}
		temp = getLastIncrement(INCREMENT_INVOKATION);
		if (temp > 0){
			tempStr+=" Invoke+"+temp;
		}
		temp = getLastIncrement(INCREMENT_SPEED);
		if (temp > 0){
			tempStr+=" Speed+"+temp;
		}
		temp = getLastIncrement(INCREMENT_SOUL);
		if (temp > 0){
			tempStr+=" Soul+"+temp;
		}
		temp = getLastIncrement(INCREMENT_CARRYING);
		if (temp > 0){
			tempStr+=" Carrying+"+temp;
		}
		temp = getLastIncrement(INCREMENT_DEFENSE);
		if (temp > 0){
			tempStr+=" Defense+"+temp;
		}
		temp = getLastIncrement(INCREMENT_EVADE);
		if (temp > 0){
			tempStr+=" Evade+"+temp;
		}
		return tempStr;
	}
	
	private int getNeededXP(int level) {
		return getAvgEnemies(level) * getAvgXP(level);
	}
	
	private int getAvgEnemies(int level){
		if (level == 1)
			return 25;
		else return getAvgEnemies(level-1)+getIncreaseOnEnemies(level);
	}
	
	private int getIncreaseOnEnemies(int level){
		if (level == 1)
			return 0;
		/* 0000029: Change level up schema to acquire skills quickly 
		 * if (level == 2)
			return 20;
		else return getIncreaseOnEnemies(level-1)+5;*/
		return 25;
	}
	
	private int getAvgXP(int level){
		if (level == 1)
			return 100;
		else return getAvgXP(level-1)+50;
	}

	/*public void finishLevel(){
		playerEventListener.informEvent(EVT_FORWARD);
	}*/
/*
	public int getScore(){
		return score;
	}
	*/

	public Player () {
		hpMax = 20;
		hp = hpMax;
		heartMax = 20;
		hearts = 5;
		carryMax = 15;
		gold = 0;
		currentMysticWeapon = -1;
		for (int i = 0; i < ItemDefinition.CATS.length; i++) {
			resetWeaponSkillLevel(ItemDefinition.CATS[i]);
		}
	}
	
	
	public static void initializeWhips(String leatherWhip,
			String chainWhip,
			String vampireKiller,
			String thornWhip,
			String flameWhip,
			String litWhip)
	{
		ItemDataTable it = Main.itemData;
		LEATHER_WHIP = it.createWeapon(leatherWhip,"");
		CHAIN_WHIP = it.createWeapon(chainWhip,"");
		VAMPIRE_WHIP = it.createWeapon(vampireKiller,"");
		THORN_WHIP = it.createWeapon(thornWhip,"");
		FLAME_WHIP = it.createWeapon(flameWhip,"");
		LIT_WHIP = it.createWeapon(litWhip,"");
	}
	
	
	public static Item
		LEATHER_WHIP,
		CHAIN_WHIP,
		VAMPIRE_WHIP,	//vkiller (fully upgraded normal whip ?)
		THORN_WHIP,
		FLAME_WHIP,
		LIT_WHIP;


	public int getMysticWeapon() {
		return currentMysticWeapon;
	}

	public void informPlayerEvent(int code){
		Debug.enterMethod(this, "informPlayerEvent", code+"");
		if (playerEventListener != null)
			playerEventListener.informEvent(code);
		Debug.exitMethod();
	}

	public void informPlayerEvent(int code, Object param){
		playerEventListener.informEvent(code, param);
	}

	//static?
	public int getMorphDefense() {
		if (hasCounter(Consts.C_MYSTMORPH) || hasCounter(Consts.C_MYSTMORPH2)){
			return 1;
		} else if (hasCounter(Consts.C_LUPINEMORPH))
			return 1;
		else if (hasCounter(Consts.C_BEARMORPH))
			return 1;
		else if (hasCounter(Consts.C_BEASTMORPH))
			return 2;
		else if (hasCounter(Consts.C_DEMONMORPH))
			return 3;
		else if (hasCounter(Consts.C_WEREWOLFMORPH))
			return 4;
		return 0;
	}
	
	private void damage(String damageSource, Damage dam) {
		if (!level.isDay()) {
			dam.boostDamage(1);
		}
		if (getFlag(Consts.ENV_THUNDERSTORM)) {
			dam.boostDamage(2);
		}
		if (getFlag(Consts.ENV_SUNNY)) {
			if (dam.damage > 2) {
				dam.reduceDamage(2);
			}
		}
		
		if (hasCounter(Consts.C_ENERGYSHIELD)) {
			level.addMessage("The energy shield covers you!");
			dam.damage = (int)Math.ceil(dam.damage * 2.0/3.0);
		}
		
		if (hasCounter(Consts.C_TURTLESHELL)) {
			level.addMessage("The turtle soul covers you!");
			dam.damage = (int)Math.ceil(dam.damage * 2.0d/3.0d);
		}
		
		if (!dam.ignoresArmor) {
			dam.reduceDamage(getArmorDefense());
		}
		dam.reduceDamage(getDefenseBonus());
		
		if (hasCounter("REGAIN_SHAPE")) {
			selector = originalSelector;
			setFlag("KEEPMESSAGES", false);
			setCounter("REGAIN_SHAPE", 0);
			level.addMessage("You recover your shape!");
			deMorph();
			land();
		}
		
		if (dam.damage <= 0) {
			if (Util.chance(70)) {
				level.addMessage("You withstand the attack.");
				return;
			} 
			dam.damage = 1;
		}
		
		if (isInvincible()) {
			level.addMessage("You are invincible!");
			return;
		}
		if (sex == MALE) {
			if (Util.chance(50)) {
				SFXManager.play("wav/hurt_male.wav");
			} else {
				SFXManager.play("wav/hurt_male2.wav");
			}
		} else {
			if (Util.chance(50)) {
				SFXManager.play("wav/hurt_female.wav");
			} else {
				SFXManager.play("wav/hurt_female2.wav");
			}
		}
		
		hp -= dam.damage;
		level.addMessage(damageSource + " {"+dam.damage+"}");
		if (Util.chance(50)) {
			decreaseWhip();
		}
		if (Util.chance(40)) {
			level.addBlood(pos, Util.rand(0,1));
		}
	}


	public void selfDamage(String damageSource, int damageType, Damage dam) {
		damage(damageSource, dam);
		if (hp > 0) {
			return;	// still alive.
		}
		
		// hp is 0 or lower. DEAD.
		switch (damageType) {
			case Player.DAMAGE_MORPHED_WITH_STRONG_ARMOR:
				gameSessionInfo.setDeathCause(GameSessionInfo.STRANGLED_BY_ARMOR);
				break;
			case Player.DAMAGE_WALKED_ON_LAVA:
				gameSessionInfo.setDeathCause(GameSessionInfo.BURNED_BY_LAVA);
				break;
		}
	}


	public void increaseWeaponSkill(String category) {
		Counter c = ((Counter)weaponSkillsCounters.get(category));
		Counter s = ((Counter)weaponSkills.get(category));
		
		c.increase();
		if (c.getCount() > s.getCount()*80+10){
			c.reset();
			if (s.getCount() == 9){
				if (getFlag("WEAPON_MASTER") && playerClass != CLASS_KNIGHT){
					
				} else {
					Main.ui.showImportantMessage("You have become a master with "+ItemDefinition.getCategoryDescription(category)+"!");
					s.increase();
					setFlag("WEAPON_MASTER", true);
				}
			} else if (s.getCount() < 10){
				Main.ui.showImportantMessage("You become better with "+ItemDefinition.getCategoryDescription(category)+". Press Space to continue.");
				s.increase();
			}
		}
	}

	public void increaseWeaponSkillLevel(String category) {
		Counter c = ((Counter)weaponSkillsCounters.get(category));
		Counter s = ((Counter)weaponSkills.get(category));
		c.reset();
		if (s.getCount() < 10) {
			s.increase();
		}
		// else display/log 'Weapon Skill Cap hit for '+category ?
	}
	
	public void resetWeaponSkillLevel(String category) {
		weaponSkills.put(category, new Counter(0));
		weaponSkillsCounters.put(category, new Counter(0));
	}

	private int getBackFlipChance() {
		return 20 + getAttack();
	}
	
	private int blockDirection, blockDirection1, blockDirection2;
	
	public void setShieldGuard(int direction, int turns){
		setCounter("SHIELD_GUARD", turns);
		switch (direction){
		case Action.UP:
			blockDirection = Action.DOWN;
			blockDirection1 = Action.DOWNLEFT;
			blockDirection2 = Action.DOWNRIGHT;
			break;
		case Action.DOWN:
			blockDirection = Action.UP;
			blockDirection1 = Action.UPLEFT;
			blockDirection2 = Action.UPRIGHT;
			break;
		case Action.LEFT:
			blockDirection = Action.RIGHT;
			blockDirection1 = Action.UPRIGHT;
			blockDirection2 = Action.DOWNRIGHT;
			break;
		case Action.RIGHT:
			blockDirection = Action.LEFT;
			blockDirection1 = Action.UPLEFT;
			blockDirection2 = Action.DOWNLEFT;
			break;
		case Action.UPLEFT:
			blockDirection = Action.DOWNRIGHT;
			blockDirection1 = Action.RIGHT;
			blockDirection2 = Action.DOWN;
			break;
		case Action.UPRIGHT:
			blockDirection = Action.DOWNLEFT;
			blockDirection1 = Action.LEFT;
			blockDirection2 = Action.DOWN;
			break;
		case Action.DOWNRIGHT:
			blockDirection = Action.UPLEFT;
			blockDirection1 = Action.LEFT;
			blockDirection2 = Action.UP;
			break;
		case Action.DOWNLEFT:
			blockDirection = Action.UPRIGHT;
			blockDirection1 = Action.RIGHT;
			blockDirection2 = Action.UP;
			break;
		}
	}
	
	public boolean damage (String mxessage, Monster who, Damage dam){
		int attackDirection = Action.getGeneralDirection(who.pos, pos);
		if (hasEnergyField()){
			StringBuffer buff = new StringBuffer("The "+who.getDescription()+" is shocked!");
			who.damage(buff, 1);
			level.addMessage(buff.toString());
			return false;
		}
		
		if (Util.chance(getEvadeChance())) {
			level.addMessage("You jump and avoid the "+who.getDescription()+" attack");
			return false;
		}
		
		if (getFlag("PASIVE_BACKFLIP") && Util.chance(getBackFlipChance()) && Util.chance(evadeChance)){
			level.addMessage("You backflip and avoid the "+who.getDescription()+" attack!");
			return false;
		}

		
		if (weapon != null && Util.chance(weapon.getCoverage())) {
			level.addMessage("You parry the attack with your "+weapon.getDescription());
			return false;
		}
		
		if (shield != null &&
			(weapon == null || (weapon != null && !weapon.isTwoHanded()))
			)
		{
			int blockChance = getShieldBlockChance();
			int coverageChance = getShieldCoverageChance();
			
			if (hasCounter("SHIELD_GUARD")) {
				if (attackDirection == blockDirection ||
					attackDirection == blockDirection1 ||
					attackDirection == blockDirection2){
					level.addMessage("You withstand the attack!");
					blockChance *= 3;
					coverageChance = 100;
				}
			}
			
			if (Util.chance(blockChance)) {
				level.addMessage("You completely block the attack with your "+shield.getDescription());
				increaseWeaponSkill(ItemDefinition.CAT_SHIELD);
				return false;
			}
			
			if (Util.chance(coverageChance)) {
				level.addMessage("Your "+shield.getDescription()+" is hit.");
				dam.reduceDamage(shield.getDefense());
			}
		}
		damage("The "+who.getDescription()+" hits you.", dam);
		Main.ui.drawEffect(Main.efx.createLocatedEffect(pos, "SFX_QUICK_WHITE_HIT"));
		if (hp < 0) {
			if (sex == MALE)
				SFXManager.play("wav/die_male.wav");
			else
				SFXManager.play("wav/die_female.wav");
			gameSessionInfo.setDeathCause(GameSessionInfo.KILLED);
			gameSessionInfo.setKillerMonster(who);
			gameSessionInfo.deathLevel = level.levelNumber;
		}
		return true;
	}

	public void checkDeath() {
		if (hp < 0) {
			level.addMessage("You are dead..");
			informPlayerEvent(DEATH);
		}
	}

	public void setMysticWeapon(int value) {
		currentMysticWeapon = value;
	}


	public String getSecondaryWeaponDescription() {
		if (playerClass == CLASS_VAMPIREKILLER) {
			if (getMysticWeapon() != -1)
				return weaponName(getMysticWeapon());
			else
				return "None";
		} else {
			if (secondaryWeapon != null)
				return secondaryWeapon.getAttributesDescription();
			else
				return "";
		}
	}
	
	public String getEquipedWeaponDescription() {
		if (weapon != null)
			return (weapon.hasCounter(Consts.C_WEAPON_ENCHANTMENT) ? "Enchanted ":"") + weapon.getAttributesDescription();
		else
			return "Nothing";
	}

	public String getArmorDescription() {
		if (armor != null)
			return armor.getAttributesDescription();
		else
			return "Nothing";
	}


	public String getAccDescription() {
		if (shield == null)
			return "Nothing";
		else
			return shield.getAttributesDescription();
	}


	public void addItem(Item toAdd){
		if (!canCarry()){
			if (level != null)
				level.addMessage("You can't carry anything more");
			return;
		}
		String[] effectOnAcquire = toAdd.getEffectOnAcquire().split(" ");
		if (effectOnAcquire[0].equals("KEYS"))
			addKeys(Integer.parseInt(effectOnAcquire[1]));
		else
		if (effectOnAcquire[0].equals("HEARTMAX"))
			increaseHeartMax(Integer.parseInt(effectOnAcquire[1]));
		else
		if (effectOnAcquire[0].equals("HITSMAX"))
			increaseHPMax(Integer.parseInt(effectOnAcquire[1]));
		else
		if (effectOnAcquire[0].equals("ENABLE")){
			if (effectOnAcquire[1].equals("LITSPELL"))
				setFlag(Consts.C_SPELL_LIT, true);
			else
			if (effectOnAcquire[1].equals("FLAMESPELL"))
				setFlag(Consts.C_SPELL_FIRE, true);
			else
			if (effectOnAcquire[1].equals("ICESPELL"))
				setFlag(Consts.C_SPELL_ICE, true);
			if (effectOnAcquire[1].equals("SILVERDAGGER")){
				if (daggerLevel == 0)
					daggerLevel = 1;
			} else
			if (effectOnAcquire[1].equals("GOLDDAGGER")){
				daggerLevel = 2;
			}
		}else
		if (effectOnAcquire[0].equals("CARRY"))
			setCarryMax (Integer.parseInt(effectOnAcquire[1]));

		if (!effectOnAcquire[0].equals("") && toAdd.getDefinition().isSingleUse) ;

		else {
			if (canCarry()){
				String toAddID = toAdd.getFullID();
				Equipment equipmentx = (Equipment)inventory.get(toAddID);
				if (equipmentx == null)
					inventory.put(toAddID, new Equipment(toAdd, 1));
				else
					equipmentx.increaseQuantity();
			}
		}
	}

	public Item
		weapon,
		secondaryWeapon,
		armor,
		shield;
	
	public int getItemCount() {
		int eqCount = 0;
		for (String i: inventory.keySet()) {
			eqCount += inventory.get(i).getQuantity();
		}
		return eqCount;
	}
	
	public boolean canCarry() {
		return getItemCount() < carryMax;
	}
	
	public boolean canCarry(int quantity) {
		return getItemCount() + quantity <= carryMax;
	}

	private void removeItem(Equipment toRemove) {
		inventory.remove(toRemove.getItem().getFullID());
	}
	
	public boolean hasItem(Item item){
		return inventory.containsKey(item.getFullID());
	}
	
	public boolean hasItemByID(String itemID){
		return inventory.containsKey(itemID);
	}

	/*public void removeItem(Item toRemove){
		inventory.remove(toRemove.getDefinition().getID());
	}*/

	public Vector<Equipment> getInventory() {
		Vector<Equipment> ret = new Vector<>(10);
		for (String i: inventory.keySet()) {
			ret.add(inventory.get(i));
		}
		return ret;
	}


	public void addHearts(int howMuch) {
		minorHeartCount++;
		hearts += howMuch;
		if (hearts > heartMax) {
			hearts = heartMax;
		}
	}
	
	public int getHearts() {
		return hearts;
	}

	public void setHearts(int value) {
		hearts = value;
	}

	// === HP Functions ===
	
	public int getHP() {
		return hp;
	}

	public void setHP(int value) {
		hp = value;
		if (hp > hpMax) {
			hp = hpMax;
		}
	}
	
	public int getHPMax() {
		return hpMax;
	}

	public void setHPMax(int max) {
		hpMax = max;
	}
	
	public void increaseHPMax(int amount) {
		assert(amount > 0);
		hpMax += amount;
		// TODO: What if: HP was FULL, and you just increased max amount?
		// fill up current amount also? Or no?
		if (hpMax > MAX_HPMAX) {
			hpMax = MAX_HPMAX;
		}
	}
	
	
	public void heal() {	// FULL heal. TODO Rename 'fullHeal' or 'healFully'
		hp = hpMax;
	}


	public void heal(int i) {	// heal()	was called recoverHP().
		hp += i;
		if (hp > hpMax) {
			hp = hpMax;
		}
	}
	
	/** Recover p% of your max HP! */
	public void healHPPercentage(int p) {	// previously known as "recoverHitsP"
		int recovery = (int)Math.round((double)hpMax * (p/100.0));
		heal(recovery);
	}
	
	
	/*public void regen() {
		if (regenRate > 0)
			regenCont++;
		if (regenCont > regenRate) {
			regenCont = 0;
			recoverHits(1);
		}
	}*/
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String value) {
		name = value;
	}

	private String classString;		// ....?!?! WHY WHAT WHY STORE STR OF IT?
	public String getClassString() {
		return classString;
	}

	public PlayerEventListener getPlayerEventListener() {
		return playerEventListener;
	}

	public void setPlayerEventListener(PlayerEventListener value) {
		playerEventListener = value;
	}

	public void reduceHearts(int amount) {
		hearts -= amount;
	}

	// *Mystic*WeaponName?
	public static String weaponName(int code) {	//playerWeaponID?
		switch (code){
			case AXE:
				return "Axe";
			case CROSS:
				return "Cross";
			case DAGGER:
				return "Mystic Dagger";
			case HOLY:
				return "Holy water";
			case STOPWATCH:
				return "Stopwatch";
			case BIBLE:
				return "Holy Bible";
			case SACRED_CRYSTAL:
				return "Crystal";
			case SACRED_FIST:
				return "Sacred Fist";
			case SACRED_REBOUND:
				return "Rebound Crystal";
			default:
				return "No Weapon";
		}
	}

	public void bounceBack(Position var, int dep){
		Debug.enterMethod(this, "bounceBack", var +","+dep);
		int startingHeight = level.getMapCell(pos).getHeight();
		out: for (int i = 1; i < dep; i++) {
			Position destinationPoint = Position.add(pos, var);
			Cell destinationCell = level.getMapCell(destinationPoint);
			/*if (destinationCell == null)
        		break out;*/
			if (destinationCell == null) {
				if (!level.isValidCoordinate(destinationPoint)) {
					destinationPoint = Position.subs(destinationPoint, var);
					landOn(destinationPoint);
					break out;
				}
				if (i < dep-1) {
					pos = destinationPoint;
					continue out;
				} else {
					landOn(destinationPoint);
					break out;
				}
			}
			Feature destinationFeature = level.getFeatureAt(destinationPoint);
			if (destinationFeature != null && destinationFeature.getKeyCost() > getKeys()) {
				land();
				break out;
			}
			if (destinationCell.getHeight() > startingHeight+2) {
				land();
				break out;
			} else {
				if (!destinationCell.isSolid()) {
					if (i < dep-1)
						pos = destinationPoint;
					else
						landOn(destinationPoint);
				} else {
					level.addMessage("You bump into the "+destinationCell.getShortDescription());
					land();
					break out;
				}
			}
		}
		Debug.exitMethod();
	}
	
	
	public boolean isSwimming() {
		Cell mapcell = level.getMapCell(pos);
		return mapcell != null && (mapcell.isWater() || mapcell.isShallowWater());
	}

	public GameSessionInfo getGameSessionInfo() {
		return gameSessionInfo;
	}

	public void setGameSessionInfo(GameSessionInfo value) {
		gameSessionInfo = value;
	}

	public int getKeys() {
		return keys;
	}

	public void increaseKeys(){
		keys++;
	}
/*
	public int getSex() {
		return sex;
	}

	public void setSex(byte value) {
		sex = value;
	}
*/
	
	public void updateStatus() {
		if (getCounter(Consts.C_BATMORPH) == 1 || getCounter(Consts.C_BATMORPH2) == 1) {
			level.addMessage("You regain your human shape!");
			land();
		}
		
		if (getCounter(Consts.C_MYSTMORPH) == 1 || 
			getCounter(Consts.C_MYSTMORPH2) == 1 ||
			getCounter(Consts.C_BEARMORPH) == 1 ||
			getCounter(Consts.C_LUPINEMORPH) == 1 ||
			getCounter(Consts.C_BEASTMORPH) == 1 ||
			getCounter(Consts.C_DEMONMORPH) == 1 ||
			getCounter(Consts.C_WEREWOLFMORPH) == 1){
			level.addMessage("You regain your human shape!");
			land();
		}
		
		if (getCounter("REGAIN_SHAPE") == 1){
			selector = originalSelector;
			setFlag("KEEPMESSAGES", false);
		}
		
		for (int i = 0; i < counteredItems.size(); i++){
			Item item = (Item)counteredItems.elementAt(i);
			item.reduceCounters(this);
			if (!item.hasCounters()){
				counteredItems.remove(item);
			}
		}
			
		super.updateStatus();
		
		if (hasIncreasedDefense()) defenseCounter--;
		if (isInvisible()) invisibleCount--;
		if (hasIncreasedJumping()) jumpingCounter--;
		if (isInvincible()) invincibleCount--;
		if (hasEnergyField()) energyFieldCounter--;
		
		if (isPoisoned()) {
			poisonCount--;
			if (!isPoisoned()) {
				level.addMessage("The poison leaves your blood.");
			}
		}
		if (isStunned()) stunCount--;
		if (isPetrified()) petrifyCount--;
		if (isFainted()) faintCount--;
		
		if (isPoisoned()) {
			if (Util.chance(40)) {
				selfDamage("You feel the poison coursing through your veins!", Player.DAMAGE_POISON, new Damage(3, true));
			}
		}
		if (getHoverHeight() > 0) {
			if (hasCounter(Consts.C_BATMORPH) || hasCounter(Consts.C_BATMORPH2)) {
				;
			} else {
				setHoverHeight(getHoverHeight()-4);
			}
		}
		if (level.getMapCell(pos) != null && level.getMapCell(pos).isWater()) {
			if (getFlag("PLAYER_SWIMMING")) {
				if (getCounter("OXYGEN") == 0) {
					drown();
				} else if (getCounter("OXYGEN") == 5) {
					level.addMessage("You are almost drown!");
				} else if (getCounter("OXYGEN") == 15) {
					level.addMessage("You are drowning!");
				}
			} else {
				setCounter("OXYGEN", getBreathing());
				level.addMessage("You start swimming!");
				setFlag("PLAYER_SWIMMING", true);
			}
		} else {
			setFlag("PLAYER_SWIMMING", false);
		}
		// regen();
	}


	private void levelUp() {
		nextLevelXP += getNeededXP(playerLevel);
		if (playerLevel % 2 == 0) {
			hpMax++;
			addLastIncrement(INCREMENT_HITS,1);
		}
		if (playerLevel % 3 == 0){
			soulPower++;
			addLastIncrement(INCREMENT_SOUL,1);
		}
		if (playerLevel % 3 == 0){
			attack++;
			addLastIncrement(INCREMENT_ATTACK,1);
		}
		if (playerLevel % 5 == 0){
///			defense++;
			addLastIncrement(INCREMENT_DEFENSE,1);
		}
		heartMax += 1;
		addLastIncrement(INCREMENT_HEARTS,1);
		SFXManager.play("wav/levelup.wav");
		informPlayerEvent(EVT_LEVELUP);
		coolness += 20;
		playerLevel++;
		deservesLevelUp = false;
	}
	
	public void act(){
		setPreviousPosition();
		if (deservesLevelUp){
			levelUp();
		}
		if (justJumped()) {
			setJustJumped(false);
		} else if (isStunned()){
			if (Util.chance(40)){
				level.addMessage("You cannot move!");
				updateStatus();
			}
			else
				super.act();
		} else if (isPetrified()){
			level.addMessage("You are petrified!");
			updateStatus();
			see();
			Main.ui.refresh();
		} else if (isFainted()){
			updateStatus();
			see();
			Main.ui.refresh();
		} else {
			super.act();
		}
	}

	public void land(){
		Debug.enterMethod(this, "land");
		landOn (pos);
		Debug.exitMethod();
	}

	private Position getFreeSquareAround(Position destinationPoint){
		Position tryP = Position.add(destinationPoint, Action.directionToVariation(Action.UP));
		if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
			return tryP;
		} 
		
		tryP = Position.add(destinationPoint, Action.directionToVariation(Action.DOWN));
		if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
			return tryP;
		}
		
		tryP = Position.add(destinationPoint, Action.directionToVariation(Action.LEFT));
		if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
			return tryP;
		}
					
		tryP = Position.add(destinationPoint, Action.directionToVariation(Action.RIGHT));
		if (level.getMapCell(tryP) != null && !level.getMapCell(tryP).isSolid()){
			return tryP;
		}
		return null;
	}
	
	/**
	 * Lands on the destination point, stepping on height changing triggers
	 * @param destinationPoint
	 * @param step
	 */
	public void landOn(Position destinationPoint) {
		landOn(destinationPoint, true);
	}


	/**
	 * Lands on the destination point
	 * @param destinationPoint
	 * @param step If true, step on height changing triggers
	 */
	public void landOn(Position destinationPoint, boolean step) {
		Debug.enterMethod(this, "landOn", destinationPoint);
		Cell destinationCell = level.getMapCell(destinationPoint);
		if (destinationCell == null || destinationCell.isEthereal()) {
			destinationPoint = level.getDeepPosition(destinationPoint);
			if (destinationPoint == null) {
				level.addMessage("You fall into a endless pit!");
				gameSessionInfo.setDeathCause(GameSessionInfo.ENDLESS_PIT);
				hp = -1;	// 0, surely?
				informPlayerEvent(Player.DEATH);
				Debug.exitMethod();
				return;
			} else {
				destinationCell = level.getMapCell(destinationPoint);
			}
		}
		
		pos = destinationPoint;
		
		
		if (destinationCell.isSolid() && !isEthereal()) {
			// Tries to land on a freesquare around
			Position tryp = getFreeSquareAround(destinationPoint);
			if (tryp == null) {
				level.addMessage("You are smashed inside the "+destinationCell.getShortDescription()+"!");
				gameSessionInfo.setDeathCause(GameSessionInfo.SMASHED);
				hp = -1;	// 0, surely?
				informPlayerEvent(Player.EVT_SMASHED);
				Debug.exitMethod();
				return;
			} else {
				landOn(tryp);	// RECURSIVELY CALLING landOn(tryp, true);	!!?!?!?!?? WHYYYYY!?
				Debug.exitMethod();
				return;
			}
			
		}
		if (destinationCell.getDamageOnStep() > 0) {
			if (!isInvincible()){
				selfDamage("You are injured by the "+destinationCell.getShortDescription(),
					Player.DAMAGE_WALKED_ON_LAVA, new Damage(2, false));
			}
		}

		if (step && destinationCell.getHeightMod() != 0) {
			pos = Position.add(destinationPoint, new Position(0,0, destinationCell.getHeightMod()));
		}
		
		if (destinationCell.isShallowWater()) {
			level.addMessage("You swim in the "+destinationCell.getShortDescription()+"!");
		}
		Vector<Item> destinationItems = level.getItemsAt(destinationPoint);
		if (destinationItems != null) {
			if (destinationItems.size() == 1) {
				level.addMessage("There is a "+(destinationItems.elementAt(0)).getDescription()+" here");
			} else {
				level.addMessage("There are several items here");
			}
		}
		
		Actor aActor = level.getActorAt(destinationPoint);
		if (aActor instanceof Hostage) {
			if (!hasHostage() && !((Hostage)aActor).isRescued()){
				setHostage((Hostage)aActor);
				addHistoricEvent("rescued "+aActor.getDescription()+" from the "+level.getDescription());
				level.removeMonster((Monster)aActor);
			}
		}
		
		Feature[] destinationFeatures = level.getFeaturesAt(destinationPoint);
		Feature destinationFeature = null;
		boolean played = false;
		if (destinationFeatures != null) {
			for (int i = 0; i < destinationFeatures.length; i++){
				destinationFeature = destinationFeatures[i];
				if (destinationFeature.getKeyCost() > 0){
					reduceKeys(destinationFeature.getKeyCost());
					//Debug.say("I destroy the "+destinationFeature);
					level.destroyFeature(destinationFeature);
				}
				
				if (destinationFeature.getID().equals("TELEPORT")) {
					if (getGold() > 1000) {
						UserInterface ui = Main.ui;
						ui.showMessage("Drop a thousand in gold to return to Petra? [Y/N]");
						if (ui.prompt()) {
							if (getHostage() != null) {
								ui.showMessage("Abandon "+getHostage().getDescription()+"? [Y/N]");
								if (ui.prompt()) {
									abandonHostage();
								} else {
									return;
								}
							}
							SFXManager.play("wav/loutwarp.wav");
							informPlayerEvent(Player.EVT_GOTO_LEVEL, "TOWN0");
							level.levelNumber = 0;
							landOn(Position.add(level.getExitFor("FOREST0"), new Position(-1,0,0)));
							reduceGold(1000);
							
							return;
						}
					} else {
						level.addMessage("There is something engraved here: \"Of Gold A Thousand Put Here And Be Gone\"");
					}
				}

				if (destinationFeature.getHeightMod() != 0) {
					pos = Position.add(destinationPoint, new Position(0,0, destinationFeature.getHeightMod()));
				}
				if (destinationFeature.getHeartPrize() > 0) {
					level.addMessage("You get "+destinationFeature.getHeartPrize()+" hearts");
					addHearts(destinationFeature.getHeartPrize());
					level.destroyFeature(destinationFeature);
					if (!played) {
						played = true;
						SFXManager.play("wav/pickup.wav");
					}
				}
				
				if (destinationFeature.getScorePrize() > 0) {
					level.addMessage("You pick up the "+destinationFeature.getDescription()+".");
					addGold(destinationFeature.getScorePrize());
					level.destroyFeature(destinationFeature);
					if (!played) {
						played = true;
						SFXManager.play("wav/bonusblp.wav");
					}
				}

				if (destinationFeature.getKeyPrize() > 0){
					level.addMessage("You find "+destinationFeature.getKeyPrize()+" castle key!");
					addKeys(destinationFeature.getKeyPrize());
					level.destroyFeature(destinationFeature);
					if (!played){
						played = true;
						SFXManager.play("wav/bonusblp.wav");
					}
				}
				if (destinationFeature.getUpgradePrize() > 0){
					if (whipLevel < 2)
						increaseWhip();
					level.destroyFeature(destinationFeature);
					if (!played){
						played = true;
						SFXManager.play("wav/bonusblp.wav");
					}
				}

				if (destinationFeature.getMysticWeaponPrize() != -1){
					if (getMysticWeapon() != -1){
						Position tryp = getFreeSquareAround(destinationPoint);
						if (tryp != null){
							level.addFeature(getFeatureNameForMystic(getMysticWeapon()), tryp);
						}
					}
					level.addMessage("You get the "+ Player.weaponName(destinationFeature.getMysticWeaponPrize()) +"!");
					setMysticWeapon(destinationFeature.getMysticWeaponPrize());
					level.destroyFeature(destinationFeature);
					if (!played){
						played = true;
						SFXManager.play("wav/bonusblp.wav");
					}
				}

				if (destinationFeature.getHealPrize() > 0){
					level.addMessage("You eat the "+ destinationFeature.getDescription() +"!");
					setHP(hp + destinationFeature.getHealPrize());
					level.destroyFeature(destinationFeature);
					if (!played){
						played = true;
						SFXManager.play("wav/bonusblp.wav");
					}
				}

				if (destinationFeature.getEffect() != null)
					if (destinationFeature.getEffect().equals("ROSARY")){
						invokeRosary();
						level.destroyFeature(destinationFeature);
					} else
					if (destinationFeature.getEffect().equals("SPAWN_TREASURE")){
						level.addMessage("A treasure rises from the ground!");
						level.spawnTreasure();
						level.destroyFeature(destinationFeature);
					} else
					if (destinationFeature.getEffect().equals("MUPGRADE")){
						level.addMessage("Your "+getWeaponDescription()+" gets stronger");
						increaseShot();
						level.destroyFeature(destinationFeature);
						if (!played){
							played = true;
							SFXManager.play("wav/bonusblp.wav");
						}
					} else
					if (destinationFeature.getEffect().equals("INVISIBILITY")){
						level.addMessage("You drink the potion of invisibility!");
						setInvisible(30);
						level.destroyFeature(destinationFeature);
						if (!played){
							played = true;
							SFXManager.play("wav/bonusblp.wav");
						}
					}

				/*if (destinationFeature.getTrigger() != null)
					if (destinationFeature.getTrigger().equals("ENDGAME"))
						;
						/*if (aPlayer.getKeys() == 10)
							aPlayer.informPlayerEvent(Player.OPENEDCASTLE);*/
				Feature pred = destinationFeature;
				destinationFeature = level.getFeatureAt(destinationPoint);
				if (destinationFeature == pred) {
					destinationFeature = null;
				}
			}
		}
		
		if (level.isExit(pos)) {
			String exit = level.getExitOn(pos);
			if (exit.equals("_START") || exit.startsWith("#")) {
				//Do nothing. This must be changed with startsWith("_");
			} /*else if (exit.equals("_NEXT")){
				informPlayerEvent(Player.EVT_NEXT_LEVEL);
			} else if (exit.equals("_BACK")){
				informPlayerEvent(Player.EVT_BACK_LEVEL);
			} */else {
				informPlayerEvent(Player.EVT_GOTO_LEVEL, exit);
			}
		}
		Debug.exitMethod();
	}


	private void drown() {
		gameSessionInfo.setDeathCause(GameSessionInfo.DROWNED);
		gameSessionInfo.deathLevel = level.levelNumber;
		hp = -1;	//0, surely!?
		informPlayerEvent(Player.DROWNED);
	}
	
	
	public boolean deservesUpgrade() {
		if (playerClass != CLASS_VAMPIREKILLER) {
			return false;
		}
		if (weapon == VAMPIRE_WHIP) {
			return false;
		}
		if (minorHeartCount > 5) {
			minorHeartCount = 0;
			return true;
		}
		return false;
	}
	
	public boolean deservesMUpgrade() {
		if (playerClass != CLASS_VAMPIREKILLER) {
			return false;
		}
		//Debug.say(mUpgradeCount);
		if (shotLevel > 1) {
			return false;
		}
		if (mUpgradeCount > 50) {
			mUpgradeCount = 0;
			return true;
		}
		return false;
	}
	
	

	private void invokeRosary() {
		level.addMessage("A blast of holy light surrounds you!");
		//level.addEffect(new SplashEffect(pos, "****~~~~,,,,....", Appearance.WHITE));
		SFXManager.play("wav/lazrshot.wav");
		Main.ui.drawEffect(Main.efx.createLocatedEffect(pos, "SFX_ROSARY_BLAST"));
		
		String message = "";

		Vector<Monster> monsters = (Vector<Monster>)level.getMonsters().getVector().clone();
		Vector<Monster> removables = new Vector<>();
		for (int i = 0; i < monsters.size(); i++) {
			Monster monster = monsters.elementAt(i);
			if (Position.flatDistance(pos, monster.pos) < 16){
				if (monster instanceof NPC || monster instanceof Hostage) {
					
				} else {
					//targetMonster.damage(player.getWhipLevel());
					
					monster.damage(new StringBuffer(), 10);
					if (monster.isDead()) {
						message = "The "+monster.getDescription()+" is shredded by the holy light!";
						removables.add(monster);
					} else {
						message = "The "+monster.getDescription()+" is purified by the holy light!";
					}
					if (monster.wasSeen()) {
						level.addMessage(message);
					}
				}
			}
		}
		monsters.removeAll(removables);
		//level.removeMonster(monster);
	}


	public boolean isFainted() {
		return faintCount > 0;
	}

	public void setFainted(int counter) {
		faintCount = counter;
	}
	
	public boolean isPetrified() {
		return petrifyCount > 0;
	}

	public void setPetrify(int counter) {
		petrifyCount = counter;
	}
	
	public boolean isInvisible() {
		return invisibleCount > 0;
	}

	public void setInvisible(int counter) {
		invisibleCount = counter;
	}

	public boolean isPoisoned(){
		return poisonCount > 0;
	}
	
	public void setPoison(int counter){
		poisonCount = counter;
	}
	
	public boolean isStunned(){
		return stunCount > 0;
	}
	
	public void setStun(int counter){
		stunCount = counter;
	}
	
	private int invincibleCount;

	public boolean isInvincible(){
		return invincibleCount > 0;
	}

	public void setInvincible(int counter) {
		invincibleCount = counter;
	}

	private int jumpingCounter;

	public void increaseJumping(int counter) {
		jumpingCounter = counter;
	}

	public boolean hasIncreasedJumping(){
		return jumpingCounter > 0;
	}

	public boolean isThornWhip(){
		return weapon == THORN_WHIP;
	}

	public boolean isFireWhip(){
		return weapon == FLAME_WHIP;
	}

	public boolean isLightingWhip(){
		return weapon == LIT_WHIP;
	}

	public void setFireWhip(){
		if (playerClass == CLASS_VAMPIREKILLER) {
			weapon = FLAME_WHIP;
		}
	}
	
	public void setLitWhip() {
		if (playerClass == CLASS_VAMPIREKILLER) {
			weapon = LIT_WHIP;
		}
	}
	
	public void setThornWhip() {
		if (playerClass == CLASS_VAMPIREKILLER) {
			weapon = THORN_WHIP;
		}
	}
	
	public void increaseShot() {
		// FIXME Shouldn't this be NOT vampirekiller? what's 'shotlevel'?
		if (playerClass == CLASS_VAMPIREKILLER && shotLevel < 2) {
			shotLevel++;
		}
	}
	
	public int getShotLevel() {
		return shotLevel;
	}
	
	public void increaseWhip() {
		if (playerClass != CLASS_VAMPIREKILLER) {
			return;
		}
		if (whipLevel < 2) {
			whipLevel++;
		} else {
			return;
		}
		switch (whipLevel) {
		case 1:
			level.addMessage("Your leather whip turns into a chain whip!");
			weapon = CHAIN_WHIP;
			break;
		case 2:
			level.addMessage("Your chain whip turns into the Vampire Killer!");
			weapon = VAMPIRE_WHIP;
		}
	}

	public void decreaseWhip() {
		if (playerClass != CLASS_VAMPIREKILLER) {
			return;
		}
		if (shotLevel > 0)
			shotLevel--;
		if (whipLevel > 0)
			whipLevel--;
		else
			return;
		switch (whipLevel){
		case 1:
			level.addMessage("Your Vampire Killer turns into a chain whip!");
			weapon = CHAIN_WHIP;
			break;
		case 0:
			level.addMessage("Your chain whip turns into a leather whip!");
			weapon = LEATHER_WHIP;
		}
	}

	/// private int defense; //Temporary stat
	private int defenseCounter;

	public void increaseDefense(int counter) {
///		defense++;
		defenseCounter = counter;
	}

	public boolean hasIncreasedDefense() {
		return defenseCounter > 0;
	}

	private int energyFieldCounter;

	public void setEnergyField(int counter){
		energyFieldCounter = counter;
	}

	public boolean hasEnergyField(){
		return energyFieldCounter > 0;
	}


	public void reduceQuantityOf(Item what) {
		String toAddID = what.getFullID();
		Equipment equipment = (Equipment)inventory.get(toAddID);
		equipment.reduceQuantity();
		if (equipment.isEmpty())
			removeItem(equipment);
	}

	public int getDefenseCounter() {
		return defenseCounter;
	}

	public void setHeartMax(int value) {
		heartMax = value;
	}

	public void setCarryMax(int value) {
		carryMax = value;
	}
/*
	public int getPlayerClass() {
		return playerClass;
	}
*/
	public boolean isEthereal(){
		return hasCounter(Consts.C_MYSTMORPH) || hasCounter(Consts.C_MYSTMORPH2);
	}
	
	public boolean isFlying(){
		return hasCounter(Consts.C_BATMORPH) || hasCounter(Consts.C_BATMORPH2) || isEthereal();
	}

	public void reduceKeys(int k) {
		keys -= k;
	}


	// FIXME We have these values in Text, fixed, already, do we not!?
	// 
	public void setPlayerClass(byte value) {
		playerClass = value;
		switch (playerClass) {
			case CLASS_VAMPIREKILLER:
				classString = "Vampire Killer";
				break;
			case CLASS_INVOKER:
				classString = "Soul Master";
				break;
			case CLASS_KNIGHT:
				classString = "Knight";
				break;
			case CLASS_MANBEAST:
				if (sex == MALE)
					classString = "Beast-Man";
				else
					classString = "Beast-Woman";
				break;
			case CLASS_RENEGADE:
				classString = "Renegade";
				break;
			case CLASS_VANQUISHER:
				classString = "Vanquisher";
				break;
		}
	}

	public Appearance getAppearance() {
		if (hasCounter(Consts.C_BATMORPH))
			return Main.appearances.get("MORPHED_BAT");
		else if (hasCounter(Consts.C_BATMORPH2))
			return Main.appearances.get("MORPHED_BAT2");
		else if (hasCounter(Consts.C_LUPINEMORPH))
			return Main.appearances.get("MORPHED_LUPINE");
		else if (hasCounter(Consts.C_BEARMORPH))
			return Main.appearances.get("MORPHED_WEREBEAR");
		else if (hasCounter(Consts.C_BEASTMORPH))
			return Main.appearances.get("MORPHED_WEREBEAST");
		else if (hasCounter(Consts.C_DEMONMORPH))
			return Main.appearances.get("MORPHED_WEREDEMON");
		else if (hasCounter(Consts.C_WEREWOLFMORPH))
			return Main.appearances.get("MORPHED_WEREWOLF");
		else if (hasCounter(Consts.C_WOLFMORPH))
			return Main.appearances.get("MORPHED_WOLF");
		else if (hasCounter(Consts.C_WOLFMORPH2))
			return Main.appearances.get("MORPHED_WOLF2");
		else if (hasCounter(Consts.C_MYSTMORPH))
			return Main.appearances.get("MORPHED_MYST");
		else if (hasCounter(Consts.C_MYSTMORPH2))
			return Main.appearances.get("MORPHED_MYST2");
		else {
			Appearance ret = super.appearance;
			if (ret == null) {
				if (sex == Player.MALE) {
					setAppearance(Main.appearances.get(PlayerGenerator.getClassID(playerClass)));
				} else {
					setAppearance(Main.appearances.get(PlayerGenerator.getClassID(playerClass)+"_W"));
				}
				ret = super.getAppearance();
			}
			return ret; 
		}
	}

	private Vector<Skill> availableSkills = new Vector<>(10);

	
	public Vector<Skill> getAvailableSkills() {
		availableSkills.removeAllElements();
		if (getFlag("PASIVE_DODGE"))
			availableSkills.add(skills.get("DODGE"));
		if (getFlag("PASIVE_DODGE2"))
			availableSkills.add(skills.get("DODGE2"));
		if (playerClass == CLASS_VAMPIREKILLER){
			if (getFlag("SKILL_WARP_DASH"))
				availableSkills.add(skills.get("WARP_DASH"));
			if (getFlag("SKILL_AIR_DASH"))
				availableSkills.add(skills.get("AIR_DASH"));
			if (getFlag("SKILL_SLIDEKICK"))
				availableSkills.add(skills.get("SLIDE_KICK"));
			if (getFlag("SKILL_ITEM_BREAK"))
				availableSkills.add(skills.get("ITEM_BREAK"));
			
			if (getFlag("SKILL_SOULBLAST"))
				availableSkills.add(skills.get("SKILL_SOULBLAST"));
			if (getFlag("SKILL_SOULFLAME"))
				availableSkills.add(skills.get("SKILL_SOULFLAME"));
			if (getFlag("SKILL_SOULICE"))
				availableSkills.add(skills.get("SKILL_SOULICE"));
			if (getFlag("SKILL_SOULSAINT"))
				availableSkills.add(skills.get("SKILL_SOULSAINT"));
			if (getFlag("SKILL_SOULWIND"))
				availableSkills.add(skills.get("SKILL_SOULWIND"));
			
			availableSkills.add(skills.get("MYSTIC_DAGGER"));
			availableSkills.add(skills.get("MYSTIC_AXE"));
			if (getFlag("MYSTIC_HOLY_WATER"))
				availableSkills.add(skills.get("MYSTIC_HOLYWATER"));
			if (getFlag("MYSTIC_HOLY_BIBLE"))
				availableSkills.add(skills.get("MYSTIC_BIBLE"));
			if (getFlag("MYSTIC_STOPWATCH"))
				availableSkills.add(skills.get("MYSTIC_STOPWATCH"));
			if (getFlag("MYSTIC_CROSS"))
				availableSkills.add(skills.get("MYSTIC_CROSS"));
			if (getFlag("MYSTIC_FIST"))
				availableSkills.add(skills.get("MYSTIC_FIST"));
			if (getFlag("MYSTIC_CRYSTAL"))
				availableSkills.add(skills.get("MYSTIC_CRYSTAL"));
			if (getFlag("PASIVE_BACKFLIP"))
				availableSkills.add(skills.get("BACKFLIP"));
			
		} else if (playerClass == CLASS_RENEGADE) {
			availableSkills.add(skills.get("FIREBALL"));
			availableSkills.add(skills.get("SOULSTEAL"));
			if (getFlag("SKILL_SUMMONSOUL"))
				availableSkills.add(skills.get("SUMMON_SPIRIT"));
			if (getFlag("SKILL_SOULSSTRIKE"))
				availableSkills.add(skills.get("SKILL_SOULSSTRIKE"));
			if (getFlag("SKILL_FLAMESSHOOT"))
				availableSkills.add(skills.get("SKILL_FLAMESSHOOT"));
			if (getFlag("SKILL_HELLFIRE"))
				availableSkills.add(skills.get("SKILL_HELLFIRE"));
			
			availableSkills.add(skills.get("MINOR_JINX"));
			if (getFlag("SKILL_DARKMETAMORPHOSIS"))
				availableSkills.add(skills.get("BLOOD"));
			if (getFlag("SKILL_SHADETELEPORT"))
				availableSkills.add(skills.get("TELEPORT"));
			
			if (getFlag("SKILL_WOLFMORPH2"))
				availableSkills.add(skills.get("SKILL_WOLFMORPH2"));
			else if (getFlag("SKILL_WOLFMORPH"))
				availableSkills.add(skills.get("SKILL_WOLFMORPH"));
			if (getFlag("SKILL_MYSTMORPH2"))
				availableSkills.add(skills.get("SKILL_MYSTMORPH2"));
			else if (getFlag("SKILL_MYSTMORPH"))
				availableSkills.add(skills.get("SKILL_MYSTMORPH"));
			if (getFlag("SKILL_BATMORPH2"))
				availableSkills.add(skills.get("SKILL_BATMORPH2"));
			else if (getFlag("SKILL_BATMORPH"))
				availableSkills.add(skills.get("SKILL_BATMORPH"));
		} else if (playerClass == CLASS_INVOKER) {
			availableSkills.add(skills.get("INVOKE_BIRD"));
			availableSkills.add(skills.get("INVOKE_TURTLE"));
			if (getFlag("SKILL_CATSOUL"))
				availableSkills.add(skills.get("INVOKE_CAT"));
			if (getFlag("SKILL_BIRDSEGG"))
				availableSkills.add(skills.get("THROW_EGG"));
			if (getFlag("SKILL_MANIPULATE"))
				availableSkills.add(skills.get("MANIPULATE"));
			if (getFlag("SKILL_DRAGONFIRE"))
				availableSkills.add(skills.get("INVOKE_DRAGON"));
			if (getFlag("SKILL_INVOKEBIRD"))
				availableSkills.add(skills.get("SUMMON_BIRD"));
			if (getFlag("SKILL_INVOKETURTLE"))
				availableSkills.add(skills.get("SUMMON_TURTLE"));
			if (getFlag("SKILL_INVOKECAT"))
				availableSkills.add(skills.get("SUMMON_CAT"));
			if (getFlag("SKILL_INVOKEEAGLE"))
				availableSkills.add(skills.get("SUMMON_EAGLE"));
			if (getFlag("SKILL_INVOKETORTOISE"))
				availableSkills.add(skills.get("SUMMON_TORTOISE"));
			if (getFlag("SKILL_INVOKETIGER"))
				availableSkills.add(skills.get("SUMMON_TIGER"));
			if (getFlag("SKILL_INVOKEDRAGON"))
				availableSkills.add(skills.get("SUMMON_DRAGON"));
			
			//availableSkills.add(skills.get("MAJOR_JINX"));
			
		} else if (playerClass == CLASS_MANBEAST) {
			if (getFlag("SKILL_POWERBLOW3"))
				availableSkills.add(skills.get("IMPACT_BLOW3"));
			else if (getFlag("SKILL_POWERBLOW2"))
				availableSkills.add(skills.get("IMPACT_BLOW2"));
			else if (getFlag("SKILL_POWERBLOW"))
				availableSkills.add(skills.get("IMPACT_BLOW"));
			availableSkills.add(skills.get("CLAW_SWIPE"));
			if (getFlag("SKILL_ENERGYSCYTHE"))
				availableSkills.add(skills.get("ENERGY_SCYTHE"));
			if (getFlag("SKILL_CLAWASSAULT"))
				availableSkills.add(skills.get("CLAW_ASSAULT"));
			availableSkills.add(skills.get("LUPINE_MORPH"));
			if (getFlag("SKILL_BEARMORPH"))
				availableSkills.add(skills.get("BEAR_MORPH"));
			if (getFlag("SKILL_BEASTMORPH"))
				availableSkills.add(skills.get("BEAST_MORPH"));
			if (getFlag("SKILL_DEMONMORPH"))
				availableSkills.add(skills.get("DEMON_MORPH"));
			if (getFlag("SKILL_WEREWOLFMORPH"))
				availableSkills.add(skills.get("WEREWOLF_MORPH"));
			if (getFlag(Consts.F_COMPLETECONTROL))
				availableSkills.add(skills.get("COMPLETECONTROL"));
			else if (getFlag(Consts.F_SELFCONTROL))
				availableSkills.add(skills.get("SELFCONTROL"));
			if (getFlag("HEALTH_REGENERATION"))
				availableSkills.add(skills.get("REGEN"));
		} else if (playerClass == CLASS_VANQUISHER) {
			availableSkills.add(skills.get("HOMING_BALL"));
			availableSkills.add(skills.get("CHARGE_BALL"));
			if (getFlag(Consts.C_SPELL_FIRE))
				availableSkills.add(skills.get("FLAME_SPELL"));
			if (getFlag(Consts.C_SPELL_ICE))
				availableSkills.add(skills.get("ICE_SPELL"));
			if (getFlag(Consts.C_SPELL_LIT))
				availableSkills.add(skills.get("LIT_SPELL"));
			if (getFlag("SKILL_MINDBLAST"))
				availableSkills.add(skills.get("MINDBLAST"));
			if (getFlag("SKILL_TELEPORT"))
				availableSkills.add(skills.get("TELEPORT"));
			if (getFlag("SKILL_RECOVER"))
				availableSkills.add(skills.get("RECOVER"));
			if (getFlag("SKILL_CURE"))
				availableSkills.add(skills.get("CURE"));
			if (getFlag("SKILL_ENCHANT"))
				availableSkills.add(skills.get("ENCHANT"));
			if (getFlag("SKILL_ENERGYSHIELD"))
				availableSkills.add(skills.get("ENERGYSHIELD"));
			if (getFlag("SKILL_LIGHT"))
				availableSkills.add(skills.get("LIGHT"));
			if (getFlag("SKILL_MINDLOCK"))
				availableSkills.add(skills.get("MINDLOCK"));
			if (getFlag("SKILL_MAJORJINX"))
				availableSkills.add(skills.get("MAJOR_JINX"));
			
		}else if (playerClass == CLASS_KNIGHT) {
			availableSkills.add(skills.get("SHIELD_GUARD"));
		}
		if (weaponSkill(ItemDefinition.CAT_RINGS) > 1) {
			availableSkills.add(skills.get("DIVING_SLIDE"));
		}
		
		if (weapon != null) {
			final String wpnType = weapon.getWeaponCategory();
			if (wpnType.equals(ItemDefinition.CAT_RINGS) && weaponSkill(ItemDefinition.CAT_RINGS) >= 10) {
				availableSkills.add(skills.get("SPINNING_SLICE"));
			} else if (wpnType.equals(ItemDefinition.CAT_WHIPS) && weaponSkill(ItemDefinition.CAT_WHIPS) >= 10) {
				availableSkills.add(skills.get("WHIRLWIND_WHIP"));
			} else if (wpnType.equals(ItemDefinition.CAT_STAVES) && weaponSkill(ItemDefinition.CAT_STAVES) >= 10) {
				availableSkills.add(skills.get("ENERGY_BEAM"));
			} else if (wpnType.equals(ItemDefinition.CAT_SWORDS) && weaponSkill(ItemDefinition.CAT_SWORDS) >= 10) {
				availableSkills.add(skills.get("FINAL_SLASH"));
			} else if (wpnType.equals(ItemDefinition.CAT_PISTOLS) && weaponSkill(ItemDefinition.CAT_PISTOLS) >= 10) {
				availableSkills.add(skills.get("ENERGY_BURST"));
			}
		}
		if ((weapon == null || weapon.getWeaponCategory().equals(ItemDefinition.CAT_UNARMED)) && weaponSkill(ItemDefinition.CAT_UNARMED) >= 10) {
			availableSkills.add(skills.get("TIGER_CLAW"));
		}
		
		return availableSkills;
	}
	
	public int weaponSkill(String catID){
		return ((Counter)weaponSkills.get(catID)).getCount();
	}

	public String getWeaponDescription() {
		if (playerClass == CLASS_VAMPIREKILLER) {
			if (getMysticWeapon() != -1) {
				return weaponName(getMysticWeapon());
			} else {
				return "None";
			}
		}
		// All non-VK classes:
		if (weapon == null) {
			return "None";
		}
		if (weapon.getReloadTurns() > 0) {
			return weapon.getDescription()+"("+weapon.getRemainingTurnsToReload()+")";
		} else {
			return weapon.getDescription();
		}
	}


	// Skill *DEFINITIONS*.
	// TODO Just make this an ARRAY, indexed by integer ordinal of skill enum...?
	private final static HashMap<String, Skill> skills = new HashMap<>();
	static {
		skills.put("DIVING_SLIDE", new Skill("Diving Slide", new DivingSlide(), 8));
		skills.put("SPINNING_SLICE", new Skill("Spinning Slice", new SpinningSlice(), 8));
		skills.put("WHIRLWIND_WHIP", new Skill("Whirlwind Whip", new WhirlwindWhip(), 5));
		skills.put("ENERGY_BEAM", new Skill("Energy Beam", new EnergyBeam(), 10));
		skills.put("FINAL_SLASH", new Skill("Final Slash!", new FinalSlash(), 10));
		skills.put("TIGER_CLAW", new Skill("Tiger Claw", new TigerClaw(), 10));
		skills.put("ENERGY_BURST", new Skill("Energy Burst", new EnergyBurst(), 10));
		skills.put("REGEN", new Skill("Regeneration"));
		
		skills.put("DODGE", new Skill("Dodge"));
		skills.put("DODGE2", new Skill("Mirror Dodge"));
		
		/*skills.put("MYSTIC_WEAPON", new Skill("Mystic Weapons"));
		skills.put("HMYSTIC_WEAPON", new Skill("Sacred Mystics"));*/

		// Vampire Killer Skills
		skills.put("MYSTIC_DAGGER", new Skill("Mystic Dagger"));
		skills.put("MYSTIC_AXE", new Skill("Mystic Axe"));
		skills.put("MYSTIC_HOLYWATER", new Skill("Mystic Holy Water"));
		skills.put("MYSTIC_BIBLE", new Skill("Mystic Bible"));
		skills.put("MYSTIC_STOPWATCH", new Skill("Mystic Stopwatch"));
		skills.put("MYSTIC_CROSS", new Skill("Mystic Cross"));
		skills.put("MYSTIC_FIST", new Skill("Sacred Fist"));
		skills.put("MYSTIC_CRYSTAL", new Skill("Blast Crystal"));
		skills.put("WARP_DASH", new Skill("Warp Dash", new WarpDash(), 3));
		skills.put("AIR_DASH", new Skill("Air Dash", new AirDash(), 5));
		skills.put("ITEM_BREAK", new Skill("Item Break", new ItemBreak(), 0));
		skills.put("BACKFLIP", new Skill("Backflip"));
		skills.put("SKILL_SOULWIND", new Skill("Soul Wind", new SoulWind(), 10));
		// y'know... this structure is just CRAZY!
		// skills put str new Skill, str, new SoulWind, 10? WTF!???
		skills.put("SKILL_SOULFLAME", new Skill("Soul Flame", new SoulFlame(), 10));
		skills.put("SKILL_SOULICE", new Skill("Soul Ice", new SoulIce(), 20));
		skills.put("SKILL_SOULSAINT", new Skill("Soul Saint", new SoulSaint(), 15));
		skills.put("SKILL_SOULBLAST", new Skill("Soul Blast", new SoulBlast(), 20));
		skills.put("SLIDE_KICK", new Skill("Slide Kick", new SlideKick(), 2));
		
		// Renegade Skills
		skills.put("FIREBALL", new Skill("Fireball", new Fireball(), 2));
		skills.put("SOULSTEAL", new Skill("Soul Steal", new SoulSteal(), 5));
		skills.put("SUMMON_SPIRIT", new Skill("Summon Spirit", new SummonSpirit(), 4));
		skills.put("SKILL_SOULSSTRIKE", new Skill("Soul's Strike", new SoulsStrike(), 8));
		skills.put("SKILL_FLAMESSHOOT", new Skill("Flame Shoot", new FlamesShoot(), 10));
		skills.put("SKILL_HELLFIRE", new Skill("Hellfire", new HellFire(), 15));
		skills.put("MINOR_JINX", new Skill("Minor Jinx", new crl.action.renegade.MinorJinx(), 0));
		skills.put("BLOOD", new Skill("Dark Metamorphosis", new BloodThirst(), 10));
		skills.put("TELEPORT", new Skill("Shade Teleport", new Teleport(), 5));
		skills.put("SKILL_WOLFMORPH", new Skill("Lupine Metamorphosis", new WolfMorph(), 10));
		skills.put("SKILL_MYSTMORPH", new Skill("Ethereal Metamorphosis", new MystMorph(), 10));
		skills.put("SKILL_BATMORPH", new Skill("Chiroptera Metamorphosis", new BatMorph(), 10));
		skills.put("SKILL_WOLFMORPH2", new Skill("Adv. Lupine Metamorphosis", new WolfMorph2(), 10));
		skills.put("SKILL_MYSTMORPH2", new Skill("Adv. Ethereal Metamorphosis", new MystMorph2(), 10));
		skills.put("SKILL_BATMORPH2", new Skill("Adv. Chiroptera Metamorphosis", new BatMorph2(), 10));
		
		// Vanquisher Skills
		skills.put("HOMING_BALL", new Skill("Homing Ball", new HomingBall(), 2));
		skills.put("CHARGE_BALL", new Skill("Charge Ball", new ChargeBall(), 6));
		skills.put("FLAME_SPELL", new Skill("Flame Spell", new FlameSpell(), 8));
		skills.put("ICE_SPELL", new Skill("Ice Spell", new IceSpell(), 8));
		skills.put("LIT_SPELL", new Skill("Lighting Spell", new LitSpell(), 8));
		skills.put("MINDBLAST", new Skill("MindBlast", new Mindblast(), 15));
		skills.put("TELEPORT", new Skill("Teleport", new crl.action.vanquisher.Teleport(), 5));
		skills.put("RECOVER", new Skill("Recover", new Recover(), 15));
		skills.put("CURE", new Skill("Cure", new Cure(), 5));
		skills.put("ENCHANT", new Skill("Enchant", new Enchant(), 5));
		skills.put("ENERGYSHIELD", new Skill("Energy Shield", new EnergyShield(), 15));
		skills.put("LIGHT", new Skill("Light", new Light(), 15));
		skills.put("MINDLOCK", new Skill("Mindlock", new MindLock(), 7));
		skills.put("MAJOR_JINX", new Skill("Major Jinx", new MajorJinx(), 0));
		
		// Invoker Skills
		skills.put("INVOKE_CAT", new Skill("Cat Soul", new Cat(), 3));
		skills.put("INVOKE_TURTLE", new Skill("Turtle Soul", new Turtle(), 5));
		skills.put("INVOKE_BIRD", new Skill("Birds' Soul", new Bird(), 2));
		skills.put("INVOKE_DRAGON", new Skill("Dragonfire", new DrakeSoul(), 8));
		skills.put("THROW_EGG", new Skill("Throw Egg", new Egg(), 1));
		skills.put("MANIPULATE", new Skill("Manipulate", new Charm(), 5));
		skills.put("TAME", new Skill("Tame", new Tame(), 5));
		// FIXME This has a lot of duplicated information. str 'invoke cat' is
		// defined in the summon skill already, as is the heart cost... etc.
		skills.put("SUMMON_CAT", new Skill("Invoke Cat", new SummonSkill(SummonSkill.CAT), 5));
		skills.put("SUMMON_TURTLE", new Skill("Invoke Turtle", new SummonSkill(SummonSkill.TURTLE), 5));
		skills.put("SUMMON_BIRD", new Skill("Invoke Bird", new SummonSkill(SummonSkill.BIRD), 5));
		skills.put("SUMMON_TIGER", new Skill("Invoke Tiger", new SummonSkill(SummonSkill.TIGER), 5));
		skills.put("SUMMON_TORTOISE", new Skill("Invoke Tortoise", new SummonSkill(SummonSkill.TORTOISE), 5));
		skills.put("SUMMON_EAGLE", new Skill("Invoke Eagle", new SummonSkill(SummonSkill.EAGLE), 5));
		skills.put("SUMMON_DRAGON", new Skill("Invoke Dragon", new SummonSkill(SummonSkill.DRAGON), 8));
		
		// BeastMan
		skills.put("CLAW_SWIPE", new Skill("Claw Swipe", new ClawSwipe(), 3));
		skills.put("LUPINE_MORPH", new Skill("Lupine Morph", new LupineMorph(), 15));
		skills.put("IMPACT_BLOW", new Skill("Power Blow", new PowerBlow(), 3));
		skills.put("ENERGY_SCYTHE", new Skill("Energy Scythe", new EnergyScythe(), 5));
		skills.put("BEAR_MORPH", new Skill("Ursinae Morph", new BearMorph(), 15));
		skills.put("IMPACT_BLOW2", new Skill("Power Strike", new PowerBlow2(), 3));
		skills.put("CLAW_ASSAULT", new Skill("Claw Assault", new ClawAssault(), 5));
		skills.put("SELFCONTROL", new Skill("SelfControl"));
		skills.put("REGEN", new Skill("Regeneration"));
		skills.put("BEAST_MORPH", new Skill("BeaSt MoRPh", new BeastMorph(), 15));
		skills.put("IMPACT_BLOW3", new Skill("Power Crash", new PowerBlow3(), 3));
		skills.put("DEMON_MORPH", new Skill("Demon Morph", new DemonMorph(), 15));
		skills.put("COMPLETECONTROL", new Skill("Complete Control"));
		skills.put("WEREWOLF_MORPH", new Skill("Legendary Werewolf", new WereWolfMorph(), 20));
		
		// Knight
		skills.put("SHIELD_GUARD", new Skill("Shield Guard", new Defend(), 1));
		
	}


	public final static int
		DEATH = 0,
		WIN = 1,
		DROWNED = 2,
		KEYINMINENT = 3;

	public final static int
		EVT_FORWARD = 7,
		EVT_RETURN = 8,
		EVT_MERCHANT = 9,
		EVT_SMASHED = 10,
		EVT_CHAT = 11,
		EVT_LEVELUP = 12,
		EVT_NEXT_LEVEL_DEPRECATED = 13,
		EVT_BACK_LEVEL_DEPRECATED = 14,
		EVT_GOTO_LEVEL = 15,
		EVT_FORWARDTIME = 16,
		EVT_INN = 17;
	
	public final static byte
		MALE = 1,
		FEMALE = 2;

	public final static byte
		CLASS_VAMPIREKILLER = 0,
		CLASS_RENEGADE = 1,
		CLASS_VANQUISHER = 2,
		CLASS_INVOKER = 3,
		CLASS_MANBEAST = 4,
		CLASS_KNIGHT = 5;

	public final static int
		DAMAGE_MORPHED_WITH_STRONG_ARMOR = 0,
		DAMAGE_WALKED_ON_LAVA = 1,
		DAMAGE_USING_ITEM = 2,
		DAMAGE_POISON = 3,
		DAMAGE_JINX = 4;

	public final static String
		STATUS_STUN = "STUN",
		STATUS_POISON = "POISON",
		STATUS_PETRIFY = "PETRIFY",
		STATUS_FAINTED = "FAINTED";

	/*
	public Item getWeapon() {
		return weapon;
	}

	public void setWeapon(Item value) {
		weapon = value;
	}
	
	public Item getSecondaryWeapon() {
		return secondaryWeapon;
	}

	public void setSecondaryWeapon(Item value) {
		secondaryWeapon = value;
	}

	public Item getArmor() {
		return armor;
	}

	public void setArmor(Item value) {
		armor = value;
	}
	
	public Item getShield() {
		return shield;
	}

	public void setShield(Item value) {
		shield = value;
	}
*/
	public String getStatusString() {
		String status = "";
		if (isInvisible())
			status +="Invisible ";
		if (hasEnergyField())
			status +="EnergyField ";
		if (hasIncreasedDefense())
			status +="Protected ";
		if (hasIncreasedJumping())
			status +="Spring ";
		if (isInvincible())
			status +="Invincible ";
		if (hasCounter(Consts.C_BLOOD_THIRST))
			status +="Vampiric ";
		if (hasCounter(Consts.C_BATMORPH))
			status +="Bat ";
		if (hasCounter(Consts.C_BATMORPH2))
			status +="HBat ";
		if (hasCounter(Consts.C_MYSTMORPH))
			status +="Myst ";
		if (hasCounter(Consts.C_MYSTMORPH2))
			status +="HMyst ";
		if (hasCounter(Consts.C_WOLFMORPH))
			status +="Wolf ";
		if (hasCounter(Consts.C_WOLFMORPH2))
			status +="HWolf ";
		if (hasCounter(Consts.C_LUPINEMORPH))
			status +="Wolvish ";
		if (hasCounter(Consts.C_BEARMORPH))
			status +="Bear ";
		if (hasCounter(Consts.C_BEASTMORPH))
			status +="Beast ";
		if (hasCounter(Consts.C_DEMONMORPH))
			status +="Demon ";
		if (hasCounter(Consts.C_WEREWOLFMORPH))
			status +="WereWolf ";
		if (hasCounter(Consts.C_TURTLESHELL)){
			status +="TurtleSoul ";
		}
		
		if (hasCounter("SHIELD_GUARD"))
			status +="Guarding ";

		if (isPoisoned())
			status +="Poison ";
		if (isStunned())
			status +="Stun ";
		if (isPetrified())
			status +="Stone ";
		if (getHoverHeight() > 0) {
			status +="Fly("+getHoverHeight()+")";
		}
		if (hasCounter(Consts.C_FIREBALL_WHIP))
			status +="EnchantWhip ";
		if (hasCounter(Consts.C_WEAPON_ENCHANTMENT))
			status +="EnchantWeapon ";
		if (hasCounter(Consts.C_ENERGYSHIELD))
			status +="EnergyShield ";
		if (hasCounter(Consts.C_MAGICLIGHT))
			status +="MagicLight ";
		if (getFlag("PLAYER_SWIMMING"))
			status +="Swimming (O2="+getCounter("OXYGEN")+") ";
		return status;
	}


	public int getDaggerLevel(){
		return daggerLevel;
	}


	public final static int
		DAGGER = 0,
		AXE = 1,
		HOLY = 2,
		STOPWATCH = 3,
		CROSS = 4,
		BIBLE = 5,
		SACRED_CRYSTAL = 6,
		SACRED_FIST = 7 ,
		SACRED_REBOUND = 8;

	private final static Action[] MYSTIC_ACTIONS = {
		new Dagger(),
		new Axe(),
		new Holy(),
		new Stopwatch(),
		new Cross(),
		new Bible(),
		new BlastCrystal(),
		new SacredFist(),
		new Rebound()
	};

	public Action getMysticAction() {
		if (getMysticWeapon() == -1)
			return null;
		else
			return MYSTIC_ACTIONS[getMysticWeapon()];
	}

	/*public boolean deservesHighMystics(){
		return score > 20000 && playerLevel > 6;
	}*/
	
		public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
	public String getPlot() {
		return plot;
	}
	
	public String getPlot2() {
		return plot2;
	}

	public void setPlot(String plot, String plot2) {
		this.plot = plot;
		this.plot2 = plot2;
	}


	public void increaseHeartMax(int how){
		heartMax += how;
	}


	public void increaseMUpgradeCount() {
		mUpgradeCount++;
	}
	
	public boolean justJumped( ){
		return justJumped;
	}
	
	public void setJustJumped(boolean val){
		justJumped = val;
	}
	
	public int getHeartsMax(){
		return heartMax;
	}
	
	public int getSightRange() {
		int base = baseSightRange + 
			(level.isDay()?3:0)+
			(level.getMapCell(pos) != null ? level.getMapCell(pos).getHeight()>0?1:0:0)+
			(hasCounter(Consts.C_MAGICLIGHT) ? 3 : 0) +
			(hasCounter("LIGHT") ? 3 : 0);
		if (getFlag(Consts.ENV_FOG)) {
			base -= 2;
		}
		if (getFlag(Consts.ENV_RAIN) || getFlag(Consts.ENV_THUNDERSTORM)) {
			base -= 1;
		}
		if (base < 1) {
			base = 1;
		}
		return base;
	}
	
	public int getDarkSightRange(){
		int base = baseSightRange + 7 +(level.getMapCell(pos) != null && level.getMapCell(pos).getHeight()>0?1:0);
		if (getFlag(Consts.ENV_FOG))
			base -= 6;
		if (base < 1)
			base = 1;
		return base;
	}

	public int getBaseSightRange() {
		return baseSightRange;
	}

	public void setBaseSightRange(int baseSightRange) {
		this.baseSightRange = baseSightRange;
	}


	public void setFOV(FOV fov){
		this.fov = fov;
	}
	
	private transient FOV fov;
	
	public void see(){
		//fov.startCircle(getLevel(), pos.x, pos.y, getSightRange());
		fov.startCircle(level, pos.x, pos.y, getDarkSightRange());
	}
	
	public void darken(){
		level.darken();
	}
	
	public Position getNearestMonsterPosition(){
		Monster nearMonster = getNearestMonster();
		if (nearMonster != null)
			return nearMonster.pos;
		else
			return null;
	}
	
	public Monster getNearestMonster(){
		VMonster monsters = level.getMonsters();
		Monster nearMonster = null;
		int minDist = 150;
		for (int i = 0; i < monsters.size(); i++){
			Monster monster = (Monster) monsters.elementAt(i);
			if (monster instanceof NPC)
				continue;
			if (monster.pos.z != pos.z)
				continue;
			int distance = Position.flatDistance(level.getPlayer().pos, monster.pos);
			if (distance < minDist){
				minDist = distance;
				nearMonster = monster;
			}
		}
		return nearMonster;
	}

	public int getAttackCost() {
		return attackCost;
	}

	public void setAttackCost(int attackCost) {
		this.attackCost = attackCost;
	}

	public int getCastCost() {
		return castCost;
	}

	public void setCastCost(int castCost) {
		this.castCost = castCost;
	}

	public int getWalkCost() {
		int walkCostBonus = 0;
		if (hasCounter(Consts.C_WOLFMORPH2))
			walkCostBonus = -25;
		else if (hasCounter(Consts.C_WOLFMORPH))
			walkCostBonus = -20;
		if (hasCounter(Consts.C_BATMORPH2))
			walkCostBonus = -25;
		else if (hasCounter(Consts.C_BATMORPH))
			walkCostBonus = -20;
		
		return walkCost + walkCostBonus > 0 ? walkCost + walkCostBonus : 1;
	}

	public void setWalkCost(int walkCost) {
		this.walkCost = walkCost;
	}
	
	public int getPlayerLevel(){
		return playerLevel;
	}
	
	public void reduceCastCost(int param){
		this.castCost -= param;
	}

	public void increaseSoulPower(int param){
		this.soulPower += param;
	}
	
	public void reduceWalkCost(int param){
		this.walkCost -= param;
	}
	
	public void increaseCarryMax(int param){
		this.carryMax += param;
	}
	
	public void increaseEvadeChance(int param){
		this.evadeChance += param;
	}
	
	public void reduceAttackCost(int param){
		this.attackCost -= param;
	}
	
	public void increaseAttack(int param){
		this.attack += param;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getEvadeChance(){
		int base = getBaseEvadeChance() + (getFlag("PASIVE_DODGE") ? 10 : 0) + (getFlag("PASIVE_DODGE2") ? 10 : 0);
		if (base > 90)
			base = 90;
		return base;
	}
	
	public int getBaseEvadeChance() {
		return evadeChance;
	}

	public void setBaseEvadeChance(int evadeChance) {
		this.evadeChance = evadeChance;
	}

	public int getCarryMax() {
		return carryMax;
	}
	
	public int getSoulPower() {
		return soulPower;
	}
	
	public int getUnarmedAttack(){
		return weaponSkill(ItemDefinition.CAT_UNARMED) + 1;
	}
	
	public int getXp() {
		return xp;
	}
	
	public int getNextXP(){
		return nextLevelXP;
	}
	
	public boolean sees(Position p) {
		return level.isVisible(p.x, p.y);
	}
	
	public boolean sees(Monster m){
		return sees(m.pos);
	}
	
	public void setSoulPower(int sp){
		this.soulPower = sp;
	}

	public boolean isDoNotRecordScore() {
		return doNotRecordScore;
	}

	public void setDoNotRecordScore(boolean doNotRecordScore) {
		this.doNotRecordScore = doNotRecordScore;
	}
	
	public void setPlayerLevel(int level){
		playerLevel = level;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	public static String getFeatureNameForMystic(int mysticID){
		switch (mysticID){
		case AXE:
			return "AXEWP";
		case DAGGER:
			return "DAGGERWP";
		case HOLY:
			return "HOLYWP";
		case CROSS:
			return "CROSSWP";
		case STOPWATCH:
			return "STOPWATCHWP";
		case BIBLE:
			return "BIBLEWP";
		case SACRED_FIST:
			return "FISTWP";
		case SACRED_CRYSTAL:
			return "CRYSTALWP";
		case SACRED_REBOUND:
			return "REBOUNDWP";
		}
		return "DAGGERWP";
	}
	
	public static Advancement[][] STATADVANCEMENTS = new Advancement[][]{
		{ ADV_VENUS, ADV_PLUTO, ADV_SATURN },
		{ ADV_MARS, ADV_MERCURY, ADV_URANUS },
		{ ADV_TERRA, ADV_URANUS, ADV_SATURN },
		{ ADV_VENUS, ADV_TERRA, ADV_MERCURY },
		{ ADV_JUPITER, ADV_NEPTUNE, ADV_PLUTO },
		{ ADV_JUPITER, ADV_NEPTUNE, ADV_MARS }
	};
	
	public static Advancement[][] ADVANCEMENTS;
	static {
		ADVANCEMENTS = new Advancement[][] {
			{ // Vampire Killer
				new AdvDodge(),
				new AdvDodge2(),
				
				new AdvHolyBible(),
				new AdvHolyWater(),
				new AdvStopwatch(),
				new AdvCross(),
				new AdvFist(),
				new AdvCrystal(),
				new AdvAirDash(),
				new AdvBackflip(),
				new AdvSlideKick(),
				new AdvWarpDash(),
				
				new AdvItemBreak(),
				new AdvSoulBlast(),
				new AdvSoulFlame(),
				new AdvSoulIce(),
				new AdvSoulSaint(),
				new AdvSoulWind()
			},
			{
				new AdvBatMorph(),
				new AdvBatMorph2(),
				new AdvDarkMetamorphosis(),
				new AdvFlamesShot(),
				new AdvLavaShot(),
				new AdvMystMorph(),
				new AdvMystMorph2(),
				new AdvShadeTeleport(),
				new AdvSoulsStrike(),
				new AdvSummonSoul(),
				new AdvWolfMorph(),
				new AdvWolfMorph2(),
				new AdvMorph()
			}, {
				new AdvDodge(),
				new AdvDodge2(),
				new AdvCure(),
				new AdvEnchant(),
				new AdvEnergyShield(),
				new AdvLight(),
				new AdvMindblast(),
				new AdvMindlock(),
				new AdvRecover(),
				new AdvTeleport(),
				new AdvMajorJinx()
			},{
				new AdvDodge(),
				new AdvDodge2(),
				new AdvBirdsEgg(),
				new AdvCatSoul(),
				new AdvDragonFire(),
				new AdvInvokeBird(),
				new AdvInvokeCat(),
				new AdvInvokeDragon(),
				new AdvInvokeEagle(),
				new AdvInvokeTiger(),
				new AdvInvokeTortoise(),
				new AdvInvokeTurtle(),
				new AdvManipulate(),
				new AdvTame(),
				new AdvConfidence(),
				new AdvSoulForge(),
				new AdvKindSoul()
			},{
				new AdvDodge(),
				new AdvDodge2(),
				new AdvBearMorph(),
				new AdvBeastMorph(),
				new AdvClawAssault(),
				new AdvCompleteControl(),
				new AdvDemonMorph(),
				new AdvEnergyScythe(),
				new AdvImpactBlow(),
				new AdvPowerBlow2(),
				new AdvImpactBlow3(),
				new AdvRegen(),
				new AdvSelfControl()
			},{
				
			}
		};
	};
	
	private Vector<Advancement> tmpAvailableAdvancements = new Vector<>();
	public Vector<Advancement> getAvailableAdvancements() {
		tmpAvailableAdvancements.clear();
		out: for (int i = 0; i < ADVANCEMENTS[playerClass].length; i++) {
			if (getFlag(ADVANCEMENTS[playerClass][i].getID())){
				//Already has the advancement
				continue;
			}
			String[] requirements = ADVANCEMENTS[playerClass][i].getRequirements();
			for (int j = 0; j < requirements.length; j++) {
				if (!getFlag(requirements[j])){
					//Misses a requirement
					continue out;
				}
			}
			String[] bans = ADVANCEMENTS[playerClass][i].getBans();
			for (int j = 0; j < bans.length; j++) {
				if (getFlag(bans[j])) {
					//Has a ban
					continue out;
				}
			}
			tmpAvailableAdvancements.add(ADVANCEMENTS[playerClass][i]);
		}
		return tmpAvailableAdvancements;
	}
	
	
	public Vector<Advancement> getAvailableStatAdvancements() {
		tmpAvailableAdvancements.clear();
		for (int i = 0; i < STATADVANCEMENTS[playerClass].length; i++) {
			tmpAvailableAdvancements.add(STATADVANCEMENTS[playerClass][i]);
		}
		int rand = Util.rand(2,3);
		for (int i = 0; i < rand; i++){
			Advancement adv = Util.pick(ALL_ADVANCEMENTS);
			if (!tmpAvailableAdvancements.contains(adv))
				tmpAvailableAdvancements.add(adv);
		}
	
		return tmpAvailableAdvancements;
	}
	
	
	public boolean canAttack() {
		if (isSwimming()) {
			if (weapon == null ||
				weapon.getWeaponCategory().equals(ItemDefinition.CAT_UNARMED) ||
				weapon.getWeaponCategory().equals(ItemDefinition.CAT_DAGGERS) ||
				weapon.getWeaponCategory().equals(ItemDefinition.CAT_SPEARS) ||
				weapon.getWeaponCategory().equals(ItemDefinition.CAT_RINGS) )
			{
				// So those 4 are the only weapontypes usable under water? Or
				// IN water?
				return true;
			} else {
				return false;
			}
		}
		return !(hasCounter(Consts.C_BATMORPH) ||
				hasCounter(Consts.C_BATMORPH2) ||
				hasCounter(Consts.C_MYSTMORPH) ||
				hasCounter(Consts.C_MYSTMORPH2));
	}
	
	
	public void cure() {	// TODO Rename as curePoison()
		if (isPoisoned()) {
			level.addMessage("The poison leaves your veins");
			setPoison(0);
		} else {
			level.addMessage("Nothing happens");
		}
	}
	
	
	public void deMorph() {
		if (hasCounter(Consts.C_BATMORPH))
			setCounter(Consts.C_BATMORPH, 0);
		if (hasCounter(Consts.C_BATMORPH2))
			setCounter(Consts.C_BATMORPH2, 0);
		if (hasCounter(Consts.C_MYSTMORPH))
			setCounter(Consts.C_MYSTMORPH, 0);
		if (hasCounter(Consts.C_MYSTMORPH2))
			setCounter(Consts.C_MYSTMORPH2, 0);
		if (hasCounter(Consts.C_WOLFMORPH))
			setCounter(Consts.C_WOLFMORPH, 0);
		if (hasCounter(Consts.C_WOLFMORPH2))
			setCounter(Consts.C_WOLFMORPH2, 0);
		if (hasCounter(Consts.C_BEARMORPH))
			setCounter(Consts.C_BEARMORPH, 0);
		if (hasCounter(Consts.C_BEASTMORPH))
			setCounter(Consts.C_BEASTMORPH, 0);
		if (hasCounter(Consts.C_DEMONMORPH))
			setCounter(Consts.C_DEMONMORPH, 0);
		if (hasCounter(Consts.C_LUPINEMORPH))
			setCounter(Consts.C_LUPINEMORPH, 0);
		if (hasCounter(Consts.C_WEREWOLFMORPH))
			setCounter(Consts.C_WEREWOLFMORPH, 0);
	}
	
	private ActionSelector originalSelector;
	
	private void carryOrDrop(Item item) {
		if (canCarry()) {
			addItem(item);
		} else {
			level.addItem(pos, item);
			//TODO it's not removing it from your possession, explicitly, is it?
		}
	}
	
	public void morph(String morphID, int count, boolean smallMorph,
			boolean bigMorph, int morphStrength, int loseMindChance) {
		deMorph();
		
		// FIXME There's no way for the player to know this is a CHANCE!
		// using the Manbeast's 15-heart [P]ower ability just wasted resources/unequipped a weapon,
		// without any way to know there could have been some other outcome...
		if (getFlag(Consts.F_SELFCONTROL)) {	
			loseMindChance = (int)Math.floor(loseMindChance / 2.0);
		}
		if (getFlag(Consts.F_COMPLETECONTROL)) {
			loseMindChance = 0;
		}
		
		if (Util.chance(loseMindChance)) {
			level.addMessage("You lose your mind!");
			setFlag("KEEPMESSAGES", true);
			originalSelector = selector;
			setCounter("REGAIN_SHAPE", count);
			selector = Main.selectors.get(AIT.WILD_MORPH_AI);
		}
		
		// Drop items
		if (weapon != null) {
			level.addMessage("You drop your "+weapon.getDescription());
			carryOrDrop(weapon);	// FIXME SURELY explicit drop, here!??
			weapon = null;
		}
		
		if (secondaryWeapon != null) {
			level.addMessage("You drop your "+secondaryWeapon.getDescription());
			carryOrDrop(secondaryWeapon);
			secondaryWeapon = null;
		}
		
		if (shield != null) {
			level.addMessage("You drop your "+shield.getDescription());
			carryOrDrop(shield);
			shield = null;
		}
		
		//Item armor = getArmor();
		if (armor != null) {
			if (bigMorph) {
				if (armor.getDefense() > morphStrength) {
					selfDamage("Your armor is too strong! You feel trapped! You are injured!",
						Player.DAMAGE_MORPHED_WITH_STRONG_ARMOR, new Damage(10, true));
					return;
				}
				level.addMessage("You destroy your "+armor.getDescription()+"!");
				armor = null;
			} else if (smallMorph) {
				level.addMessage("Your "+armor.getDescription()+" falls.");
				carryOrDrop(armor);
				armor = null;
			}
		}
		setCounter(morphID, count);
	}
	
	
	public boolean canWield() {
		return !isMorphed();
	}
	
	public boolean isMorphed() {
		return
			hasCounter(Consts.C_LUPINEMORPH) ||
			hasCounter(Consts.C_BEARMORPH) ||
			hasCounter(Consts.C_BEASTMORPH)||
			hasCounter(Consts.C_DEMONMORPH) ||
			hasCounter(Consts.C_WEREWOLFMORPH) ||
			hasCounter(Consts.C_BATMORPH) ||
			hasCounter(Consts.C_BATMORPH2) ||
			hasCounter(Consts.C_WOLFMORPH) ||
			hasCounter(Consts.C_WOLFMORPH2) ||
			hasCounter(Consts.C_MYSTMORPH) ||
			hasCounter(Consts.C_MYSTMORPH2);
	}


	public int stareMonster(Monster who) {
		if (who.pos.z != pos.z) {
			return -1;
		}
		if (!who.wasSeen()) {
			return -1;
		}
		Position pp = who.pos;
		if (pp.x == pos.x){
			if (pp.y > pos.y){
				return Action.DOWN;
			} else {
				return Action.UP;
			}
		} else
		if (pp.y == pos.y){
			if (pp.x > pos.x){
				return Action.RIGHT;
			} else {
				return Action.LEFT;
			}
		} else
		if (pp.x < pos.x){
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
	
	
	public int stareMonster() {
		Monster nearest = getNearestMonster();
		if (nearest == null)
			return -1;
		else
			return stareMonster(nearest);
	}
	
	private String[] bannedArmors;

	public String[] getBannedArmors() {
		return bannedArmors;
	}

	public void setBannedArmors(String[] bannedArmors) {
		this.bannedArmors = bannedArmors;
	}


	private Hashtable<String, Integer> lastIncrements = new Hashtable<>();
	
	public void addLastIncrement(String key, int value){
		Integer current = (Integer) lastIncrements.get(key);
		if (current == null){
			current = Integer.valueOf(value);
		} else {
			current = Integer.valueOf(current.intValue()+value);
		}
		lastIncrements.put(key, current);
	}
	
	private int  getLastIncrement(String key){
		Integer current = (Integer) lastIncrements.get(key);
		if (current == null){
			return 0;
		} else {
			return current.intValue();
		}
	}
	
	public void resetLastIncrements(){
		lastIncrements.clear();
	}

	public int getBreathing() {
		return breathing;
	}

	public void setBreathing(int breathing) {
		this.breathing = breathing;
	}
	
	private Vector<Item> counteredItems = new Vector<>();
	public void addCounteredItem(Item i) {
		counteredItems.add(i);
	}
	
	public int getPunchDamage(){
		int punchDamage = (int)Math.floor (1.5 * weaponSkill(ItemDefinition.CAT_UNARMED))+1;
		if (hasCounter(Consts.C_BEASTMORPH)){
			punchDamage = 2*punchDamage+getAttack();
		} else if (hasCounter(Consts.C_BEARMORPH)){
			punchDamage = (int)Math.ceil(1.7*punchDamage);
		} else if (hasCounter(Consts.C_DEMONMORPH)){
			punchDamage = (int)Math.ceil(2*punchDamage);
		} else if (hasCounter(Consts.C_LUPINEMORPH)){
			punchDamage = (int)Math.ceil(1.5d*punchDamage);
		} else if (hasCounter(Consts.C_WEREWOLFMORPH)){
			punchDamage = (int)Math.ceil(3*punchDamage);
		} else if (hasCounter(Consts.C_WOLFMORPH) || hasCounter(Consts.C_WOLFMORPH2)){
			punchDamage = getAttack()+1+Util.rand(0,2);
		} else if (hasCounter(Consts.C_POWERBLOW)){
			punchDamage = 10+2*getAttack()+(int)Math.ceil(1.3d*punchDamage);
		} else if (hasCounter(Consts.C_POWERBLOW2)){ 
			punchDamage = 16+2*getAttack()+(int)Math.ceil(1.5d*punchDamage);
		} else if (hasCounter(Consts.C_POWERBLOW3)){ 
			punchDamage = 25+3*getAttack()+(int)Math.ceil(1.7d*punchDamage);
		} else if (playerClass == Player.CLASS_MANBEAST) {
			punchDamage = (int)Math.ceil(1.3d*punchDamage);
		}
		return getAttack() + punchDamage;
	}
	
	
	public int getPunchPush() {
		int push = 0;
		if (hasCounter(Consts.C_BEASTMORPH)) {
			push = 3;
		} else if (hasCounter(Consts.C_BEARMORPH)) {
			push = 4;
		} else if (hasCounter(Consts.C_LUPINEMORPH)) {
			push = 2;
		} else if (hasCounter(Consts.C_WEREWOLFMORPH)) {
			push = 3;
		} else if (hasCounter(Consts.C_POWERBLOW)) {
			push = 2;
		} else if (hasCounter(Consts.C_POWERBLOW2)) {
			push = 3;
		} else if (hasCounter(Consts.C_POWERBLOW3)) {
			push = 4;
		}
		return push;
	}
	
	
	public String getPunchDescription() {
		String attackDescription = "punch";
		if (hasCounter(Consts.C_BEASTMORPH)) {
			attackDescription = "claw";
		} else if (hasCounter(Consts.C_BEARMORPH)) {
			attackDescription = "bash";
		} else if (hasCounter(Consts.C_DEMONMORPH)) {
			attackDescription = "claw";
		} else if (hasCounter(Consts.C_LUPINEMORPH)) {
			attackDescription = "slash at";
		} else if (hasCounter(Consts.C_WEREWOLFMORPH)) {
			attackDescription = "slash through";
		} else if (hasCounter(Consts.C_WOLFMORPH) || hasCounter(Consts.C_WOLFMORPH2)) {
			attackDescription = "bite";
		} else if (hasCounter(Consts.C_POWERBLOW)) {
			attackDescription = "charge against";
		} else if (hasCounter(Consts.C_POWERBLOW2)) {
			attackDescription = "charge against";
		} else if (hasCounter(Consts.C_POWERBLOW3)) {
			attackDescription = "charge against";
		} else if (playerClass == Player.CLASS_MANBEAST) {
			attackDescription = "slash at";
		}
		return attackDescription;
	}
	
	
	public int getWeaponAttack() {
		double multiplier = 1;
		if (isSwimming()) {
			multiplier = 0.5;
		}
		
		if (weapon == null) {
			return (int)(multiplier * getPunchDamage());
		}
		
		if (playerClass == Player.CLASS_VAMPIREKILLER) {
			return (int)(multiplier * (weaponSkill(weapon.getDefinition().weaponCategory) +
					(int)Math.round(getAttack() * (weapon.getAttack()/2.0))));
		} else {
			return (int)(multiplier * (weapon.getAttack() +
				weaponSkill(weapon.getDefinition().weaponCategory) +
				getAttack() +
				(weapon.hasCounter(Consts.C_WEAPON_ENCHANTMENT) ? 2 : 0)));
		}
	}
	
	
	public int getArmorDefense() {
		if (armor == null) {
			return 0;
		}
		
		if (playerClass == CLASS_VAMPIREKILLER) {
			// TODO VampireKiller perk to surface on the class-select screen!
			return (int)(armor.getDefense() + Math.ceil(getPlayerLevel()/3.0));
		} else {
			return armor.getDefense();
		}
	}
	
	
	public int getDefenseBonus() {
		int ret = 0;
		if (playerClass == Player.CLASS_MANBEAST) {
			ret+= Math.ceil((double)getPlayerLevel() / 2.5);
		}
		if (hasIncreasedDefense())
			ret++;
		ret += getMorphDefense();
		return ret;
	}
	
	
	public int getShieldBlockChance() {
		if (shield == null) {
			return 0;
		}
		boolean wieldingTwoHander = weapon != null && weapon.isTwoHanded();
		if (wieldingTwoHander) {
			return 0;
		}
		int blockChance = 0;
		// TODO Another class perk that ought to be explained on class-sel screen?
		if (playerClass == CLASS_KNIGHT) {
			blockChance = shield.getCoverage();
		} else {
			blockChance = (int)(shield.getCoverage() / 2.0);
		}
		blockChance += 2 * weaponSkill(ItemDefinition.CAT_SHIELD);
		if (blockChance > 70) {
			return 70;
		}
		return blockChance;
	}

	
	public int getShieldCoverageChance() {
		if (shield == null) {
			return 0;
		}
		boolean wieldingTwoHander = weapon != null && weapon.isTwoHanded();
		if (wieldingTwoHander) {
			return 0;
		}
		
		int coverageChance = 0;
		if (playerClass == CLASS_KNIGHT) {
			coverageChance = 70;
		} else {
			coverageChance = shield.getCoverage();
		}
		return coverageChance;
	}


	public void abandonHostage() {
		getHostage().pos = new Position(pos);	// TODO(testing): this was previously just hostage.pos = pos. is the deepcopy needed?
		level.addMonster(getHostage());
		addHistoricEvent("abandoned "+getHostage().getDescription()+" at the "+level.getDescription());
		setHostage(null);
	}
	
	
	public void putCustomMessage(String messageID, String text) {
		customMessages.put(messageID, text);
	}
	
	public String getCustomMessage(String messageID){
		return (String) customMessages.get(messageID);
	}

	
	public Position getPreviousPosition() {
		if (previousPosition == null)
			return pos;
		else
			return previousPosition;
	}
	
	public void setPreviousPosition() {
		previousPosition = pos;
	}
}