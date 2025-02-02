package crl.item;

import java.util.Hashtable;

import crl.Main;
import crl.ui.*;

public class ItemDefinition {
	
	// Weapon Categories
	public final static String
		CAT_UNARMED = "HAND_TO_HAND",
		CAT_DAGGERS = "DAGGERS",
		CAT_SWORDS = "SWORDS",
		CAT_SPEARS = "SPEARS",
		CAT_WHIPS = "WHIPS",
		CAT_MACES = "MACES",
		CAT_STAVES = "POLE_WEAPONS",
		CAT_RINGS = "RINGS",
		CAT_PROJECTILES = "THROWN",
		CAT_BOWS = "BOWS",
		CAT_PISTOLS = "MISSILE_CRAFT",
		CAT_SHIELD = "SHIELD";
	
	public final static String[] CATS = {
		ItemDefinition.CAT_UNARMED,
		ItemDefinition.CAT_DAGGERS,
		ItemDefinition.CAT_SWORDS,
		ItemDefinition.CAT_SPEARS,
		ItemDefinition.CAT_WHIPS,
		ItemDefinition.CAT_MACES,
		ItemDefinition.CAT_STAVES,
		ItemDefinition.CAT_RINGS,
		ItemDefinition.CAT_PROJECTILES,
		ItemDefinition.CAT_BOWS,
		ItemDefinition.CAT_PISTOLS,
		ItemDefinition.CAT_SHIELD
	};
	
	
	public static final byte
		EQUIPTYPE_NONEQUIP = 0,
		EQUIPTYPE_ARMOR = 1,
		EQUIPTYPE_WEAPON = 2,
		EQUIPTYPE_SHIELD = 3;
	
	// Shop Categories
	public static final byte
	// NONE = 0? Or could 'crafts' be 0?
		SHOPTYPE_CRAFTS = 1,
		SHOPTYPE_MAGIC = 2,
		SHOPTYPE_WEAPONS = 3,
		SHOPTYPE_ARMOR = 4;
	
	public static String getCategoryDescription(String catID) {
		return HASH_DESCRIPTIONS.get(catID);
	}
	
	public static final Hashtable<String, String> HASH_DESCRIPTIONS = new Hashtable<>();
	static {
		HASH_DESCRIPTIONS.put(CAT_UNARMED,"hand to hand combat");
		HASH_DESCRIPTIONS.put(CAT_DAGGERS,"daggers");
		HASH_DESCRIPTIONS.put(CAT_SWORDS,"swords");
		HASH_DESCRIPTIONS.put(CAT_SPEARS,"spears");
		HASH_DESCRIPTIONS.put(CAT_WHIPS,"whips");
		HASH_DESCRIPTIONS.put(CAT_STAVES,"pole weapons");
		HASH_DESCRIPTIONS.put(CAT_RINGS,"rings");
		HASH_DESCRIPTIONS.put(CAT_PISTOLS,"missile craft");
		HASH_DESCRIPTIONS.put(CAT_PROJECTILES,"thrown weapons");
		HASH_DESCRIPTIONS.put(CAT_BOWS,"bows");
		HASH_DESCRIPTIONS.put(CAT_MACES,"maces");
		HASH_DESCRIPTIONS.put(CAT_SHIELD,"shields");
	};


	private String ID;
	public String description;
	public String effectOnUse;
	public String effectOnAcquire;
	public String effectOnStep;
	public String attackSound;
	public String shopDescription;
	public String attackSFX;		// TODO: Clarify: attackSFX != attackSound??!
	public String menuDescription;

	
	public Appearance appearance;
	public String useMessage;
	//private String throwMessage;
	public int featureTurns;
	public String placedSmartFeature;
	
	public int
		attack,
		defense,
		range,
		verticalRange,
		
		throwRange,
		pinLevel,
		
		goldPrice,
		rarity,
		
		reloadTurns,
		
		coolness,
		coverage,
		
		shopCategory,
		shopChance,
		
		attackCost,
		reloadCostGold;

	public boolean
		harmsUndead,
		slicesThrough,
		isTwoHanded,
		isUnique,
		isSingleUse,
		isFixedMaterial;

	public byte equipType;
	
	public String weaponCategory;	// WHY *STRING*!?


	/*private ListItem sightListItem;
	 * Debe ser calculado por la UI, y guardado en esta
	 * (would need to be calculated by the UI, and saved in here) */

	public ItemDefinition(String pID, String pDescription, String pAppearance, int pEquipCategory,
			String pMenuDescription, int pPinLevel,
			int pShopChance, String pShopDescription, int pGoldPrice, int pShopCategory,
			int pAttack, int pRange, int pReloadTurns, String pWeaponCategory, boolean pHarmsUndead, boolean pSlicesThrough,
			int pDefense,  int pCoverage, int pVerticalRange, int pAttackCost, int pReloadGoldCost, boolean pTwoHanded,
			String pEffectOnUse, String pEffectOnAcquire, int pThrowRange, String pPlacedSmartFeature, boolean pSingleUse,
			int pFeatureTurns, String pUseMessage, boolean pUnique, boolean pFixedMaterial, String pAttackSFX, String pAttackSound, int pCoolness, int pRarity 
		) {
		ID = pID;
		rarity = pRarity;
		coolness = pCoolness;
		description = pDescription;
		appearance = Main.appearances.get(pAppearance);	// obj ref, when could get by ID any time...?
		goldPrice = pGoldPrice;
		effectOnUse = pEffectOnUse;
		effectOnAcquire = pEffectOnAcquire;
		throwRange = pThrowRange;
		placedSmartFeature = pPlacedSmartFeature;
		isSingleUse = pSingleUse;
		isTwoHanded = pTwoHanded;
		coverage = pCoverage;
		featureTurns = pFeatureTurns;
		attack = pAttack;
		range = pRange;
		attackSound = pAttackSound;
		reloadTurns = pReloadTurns;
		harmsUndead = pHarmsUndead;
		slicesThrough = pSlicesThrough;
		useMessage = pUseMessage;
		shopChance = pShopChance;
		shopDescription = pShopDescription;
		defense=pDefense;
		equipType = (byte)pEquipCategory;
		attackSFX = pAttackSFX;
		weaponCategory = pWeaponCategory;
		shopCategory = pShopCategory;
		verticalRange = pVerticalRange;
		attackCost = pAttackCost;
		menuDescription = pMenuDescription;
		reloadCostGold = pReloadGoldCost;
		isUnique = pUnique;
		isFixedMaterial = pFixedMaterial;
		pinLevel = pPinLevel;
	}


	public String getID() {
		return ID;
	}

	/*
	 * public String getPlacedSmartFeature() { return placedSmartFeature; }
	 */

	public String getAttributesDescription() {
		String base = ""+description;
		boolean hasStats = attack > 0 || defense > 0 || 
				range > 1 || verticalRange > 0;
		if (hasStats) {
			base += " (";
		}
		if (attack > 0) {
			base += "ATK:"+attack+" ";
		}
		if (defense > 0) {
			base += "DEF:"+defense+" ";
		}
		if (range > 1 || verticalRange > 0) {
			if (verticalRange > 0) {
				base += "RNG:"+range+","+verticalRange;
			} else {
				base += "RNG:"+range;
			}
		}
		if (reloadCostGold > 0) {
			base += " RLD:"+reloadCostGold;
		}
		if (hasStats) {
			base += ")";
		}
		return base;
	}

}