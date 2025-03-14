package crl.item;

import java.util.*;

import crl.game.Game;
import crl.level.Level;
import crl.player.Player;
import sz.util.*;

public class ItemDataTable {
	
	// itemdefs by id
	private Hashtable<String, ItemDefinition> definitions = new Hashtable<>();

	// used for...? Just for looping contents of hashtable? ARRAY, then?
///	private Vector<ItemDefinition> vDefinitions;

	private Vector<ItemDefinition>
		generalItemDefinitions = new Vector<>(),
		weaponDefinitions = new Vector<>(),
		armorDefinitions = new Vector<>();


	public void init(ItemDefinition[] itemDefs) {
///		vDefinitions = new Vector<>();
		for (ItemDefinition def: itemDefs) {//int i = 0; i < defs.length; i++) {
			definitions.put(def.getID(), def);
///			vDefinitions.add(def);	// just a list to iterate ALL the defs? in order...???
			switch (def.equipType) {	// should be a BYTE defined in ItemDef statically.
			case ItemDefinition.EQUIPTYPE_NONEQUIP:
				generalItemDefinitions.add(def);
				break;
			case ItemDefinition.EQUIPTYPE_ARMOR:// 1:
				armorDefinitions.add(def);
				break;
			case ItemDefinition.EQUIPTYPE_WEAPON://2:
				weaponDefinitions.add(def);
				break;
			case ItemDefinition.EQUIPTYPE_SHIELD://3!! BUG solved. 4:	// *WHY* was this 4?
				armorDefinitions.add(def);
				break;
			//default:
				//if (DEBUG_MODE) {
				//System.err.println("Incorrect equip-type flag in Item Definition: "+def);
				//
			}
		}
	}


	public ItemDefinition get(String id) {
		ItemDefinition def = definitions.get(id);
		assert(def != null);
		if (def == null) {
			Debug.doAssert(false, "Invalid Item ID " + id);
		}
		return def;
	}


	public Item createWeapon(String ID, String material) {
		Modifier modMaterial = null;
		for (int i = 0; i < MOD_MATERIAL.length; i++) {
			if (MOD_MATERIAL[i].id.equals(material)) {
				modMaterial = MOD_MATERIAL[i];
			}
		}
		ItemDefinition def = get(ID);
		Item item = new Item(def);
		if (!def.isFixedMaterial) {
			Debug.doAssert(modMaterial != null, "Material " + material
					+ " not found at create weapon");
			item.addPreModifier(modMaterial);
		}
		return item;
	}

	public Item createShield(String ID, String material) {
		Modifier modMaterial = null;
		for (int i = 0; i < MOD_ARMOR_MATERIAL.length; i++) {
			if (MOD_ARMOR_MATERIAL[i].id.equals(material)) {
				modMaterial = MOD_ARMOR_MATERIAL[i];
			}
		}
		ItemDefinition def = get(ID);
		Item item = new Item(def);
		if (!def.isFixedMaterial) {
			Debug.doAssert(modMaterial != null, "Material " + material
					+ " not found at create shield");
			item.addPreModifier(modMaterial);
		}
		return item;
	}

	public Item createArmor(String ID, String material) {
		Modifier modMaterial = null;
		for (int i = 0; i < MOD_ARMOR_MATERIAL.length; i++) {
			if (MOD_ARMOR_MATERIAL[i].id.equals(material)) {
				modMaterial = MOD_ARMOR_MATERIAL[i];
			}
		}

		ItemDefinition def = get(ID);
		Item item = new Item(def);
		if (!def.isFixedMaterial) {
			item.addPreModifier(modMaterial);
		}
		return item;
	}

	public Item createItem(String itemDefID) {
		ItemDefinition def = (ItemDefinition)definitions.get(itemDefID);
		assert(def != null);
		if (def == null) {
			Debug.doAssert(false, "Invalid Item ID " + itemDefID);
		}
		return new Item(def);
	}

	public Item createItemForLevel(Level level, Player player) {
		/* 
		 * String[] itemIDs = level.getSpawnItemsIDs(); String itemID =
		 * Util.randomElementOf(itemIDs); ItemDefinition def =
		 * getDefinition(itemID);
		 */
		if (level.levelNumber == -1) {
			return null;
		}
		int tries = 150;	// FIXME ...?
		int i = 0;
		ItemDefinition def = null;
		out: while (i < tries) {
			
			int pin = Util.rand(0, 100);
			if (pin > 15) {		// 85%
				def = Util.pick(generalItemDefinitions);
			} else if (pin > 5) {	//10%
				def = Util.pick(weaponDefinitions);
			} else {		// 5%
				def = Util.pick(armorDefinitions);
			}
			
			int pinLevel = def.pinLevel;
			if (pinLevel == 0) {
				continue;
			}
			int diff = Math.abs(level.levelNumber - pinLevel);
			if (diff > 5) {
				i++;
				def = null;
				continue;
			}
			int prob = 80;
			for (i = 0; i < diff; i++) {
				prob = (int) Math.round(prob / 2.0);
			}

			if (def.isUnique) {
				if (Game.wasUniqueGenerated(def.getID())) {
					i++;
					continue;
				}
				prob = (int) Math.round(prob / 5.0);
			}

			if (def.coolness > player.coolness) {
				i++;
				continue;
			}

			if (Util.chance(prob) && Util.chance(def.rarity)) {
				if (def.isUnique) {
					Game.registerUniqueGenerated(def.getID());
				}
				break out;
			}

			def = null;
			i++;
		}

		if (def == null) {
			return null;
		}
		Item item = new Item(def);
		player.coolness -= def.coolness;
		if (def.isUnique) {
			return item;
		}
		if (def.equipType == ItemDefinition.EQUIPTYPE_WEAPON) {
			if (!def.isFixedMaterial)
				setMaterial(item, level.levelNumber, MOD_MATERIAL);
			if (Util.chance(20))
				setWeaponModifiers(item, level.levelNumber);
			return item;
		} else if (def.equipType == ItemDefinition.EQUIPTYPE_ARMOR) {
			if (!def.isFixedMaterial)
				setMaterial(item, level.levelNumber, MOD_ARMOR_MATERIAL);
			if (Util.chance(10))
				setArmorModifiers(item, level.levelNumber);
			return item;
		} else if (def.equipType == ItemDefinition.EQUIPTYPE_SHIELD) {
			if (!def.isFixedMaterial)
				setMaterial(item, level.levelNumber, MOD_MATERIAL);
			if (Util.chance(20))
				setWeaponModifiers(item, level.levelNumber);
			return item;
		} else {
			return item;
		}

	}

	public static final Modifier[]
		MOD_ARMOR_STRENGTH = {
			new Modifier("WEAKENED", "Weakened ", 10),
			new Modifier("REINFORCED", "Reinforced ", 20),
		},
		MOD_ARMOR_MAGIC = {
			new Modifier("HOLY", "Holy ", 5),
			new Modifier("SHIELDING", "Shielding ", 10),
			new Modifier("DARK", "Dark ", 5),
			new Modifier("STAR", "Star ", 5),
		},
		MOD_ARMOR_MATERIAL = {
			new Modifier("BRONZE", "Bronze ", 80),
			new Modifier("STEEL", "Steel ", 10),
			new Modifier("IRON", "Iron ", 5),
			new Modifier("SILVER", "Silver ", 2),
		},
		MOD_ARMOR_ADDITIONAL = {
			new Modifier("OFTHEMOON", " of the Moon", 5),
			new Modifier("OFTHESUN", " of the Sun", 5),
		},
		MOD_STRENGTH = {
			new Modifier("WEAKENED", "Weakened ", 20),
			new Modifier("REINFORCED", "Reinforced ", 20),
			new Modifier("ENHANCED", "Enhanced ", 15),
			new Modifier("DEADLY", "Deadly ", 10),
		},
		MOD_MAGIC = {
			new Modifier("PULSATING", "Pulsating ", 5),
			new Modifier("HOLY", "Holy ", 5),
			new Modifier("SHIELDING", "Shielding ", 5),
			new Modifier("DARK", "Dark ", 5),
			new Modifier("STAR", "Star ", 5),
		},
		MOD_MATERIAL = {
			new Modifier("STEEL", "Steel ", 80),
			new Modifier("WOODEN", "Wooden ", 5),
			new Modifier("IRON", "Iron ", 5),
			new Modifier("SILVER", "Silver ", 2),
			new Modifier("OBSIDIAN", "Obsidian ", 2),
		},
		MOD_ADDITIONAL = {
			new Modifier("OFHUNTING", " of Hunting", 2),
			new Modifier("OFDESTRUCTION", " of Destruction", 2),
			new Modifier("OFPUNISHMENT", " of Punishment", 2),
			new Modifier("OFTHEMOON", "  of the Moon", 2),
		};

	// FIXME data-as-code. Could load from csv, xml or ?? format datatable?
	static {
		MOD_ARMOR_STRENGTH[0].defenseBonus = -1;
		MOD_ARMOR_STRENGTH[1].defenseBonus = 1;
		MOD_ARMOR_MAGIC[1].defenseBonus = 1;
		MOD_ARMOR_MATERIAL[0].defenseBonus = 0;
		MOD_ARMOR_MATERIAL[1].defenseBonus = 1;
		MOD_ARMOR_MATERIAL[2].defenseBonus = 2;
		MOD_ARMOR_MATERIAL[3].defenseBonus = 3;

		MOD_ARMOR_STRENGTH[1].priceModifier = 100;
		MOD_ARMOR_MAGIC[1].priceModifier = 150;

		MOD_ARMOR_MATERIAL[0].priceModifier = 0;
		MOD_ARMOR_MATERIAL[1].priceModifier = 100;
		MOD_ARMOR_MATERIAL[2].priceModifier = 300;
		MOD_ARMOR_MATERIAL[3].priceModifier = 1000;

		MOD_STRENGTH[0].atkBonus = -3;
		MOD_STRENGTH[1].atkBonus = 1;
		MOD_STRENGTH[2].atkBonus = 3;
		MOD_STRENGTH[3].atkBonus = 5;
		MOD_STRENGTH[3].slicesThru = true;

		MOD_STRENGTH[0].priceModifier = 0;
		MOD_STRENGTH[1].priceModifier = 100;
		MOD_STRENGTH[2].priceModifier = 3000;
		MOD_STRENGTH[3].priceModifier = 5000;

		MOD_MAGIC[0].rangeBonus = 1;
		MOD_MAGIC[1].harmsUndead = true;
		MOD_MAGIC[2].defenseBonus = 5;
		MOD_MAGIC[4].harmsUndead = true;

		MOD_MAGIC[0].priceModifier = 2000;
		MOD_MAGIC[1].priceModifier = 3000;
		MOD_MAGIC[2].priceModifier = 1000;
		MOD_MAGIC[4].priceModifier = 3000;

		MOD_MATERIAL[0].atkCostBonus = 0;
		MOD_MATERIAL[1].atkCostBonus = 20;
		MOD_MATERIAL[1].atkBonus = -5;
		MOD_MATERIAL[2].atkBonus = 1;
		MOD_MATERIAL[3].atkBonus = 3;
		MOD_MATERIAL[3].harmsUndead = true;
		MOD_MATERIAL[4].atkBonus = 2;

		MOD_MATERIAL[0].priceModifier = 0;
		MOD_MATERIAL[1].priceModifier = 50;
		MOD_MATERIAL[2].priceModifier = 100;
		MOD_MATERIAL[3].priceModifier = 1000;
		MOD_MATERIAL[4].priceModifier = 500;

		MOD_ADDITIONAL[0].rangeBonus = 1;
		MOD_ADDITIONAL[1].atkBonus = 1;
		MOD_ADDITIONAL[2].atkBonus = 2;

		MOD_ADDITIONAL[0].priceModifier = 1500;
		MOD_ADDITIONAL[1].priceModifier = 1500;
		MOD_ADDITIONAL[2].priceModifier = 2000;

	}

	public void setMaterial(Item weapon, int levelNumber, Modifier[] materials) {
		Modifier material = materials[0];
		int tries = 3;
		while (tries > 0) {
			Modifier randMat = Util.pick(materials);
			int chance = randMat.chance + levelNumber;
			if (Util.chance(chance)) {
				material = randMat;
				break;
			}
			tries--;
		}
		tries = 3;
		weapon.addPreModifier(material);
	}

	public void setWeaponModifiers(Item weapon, int levelNumber) {
		Modifier strength = null;
		Modifier magic = null;
		Modifier additional = null;

		Modifier randMod = Util.pick(MOD_STRENGTH);
		int chance = randMod.chance + levelNumber;
		if (Util.chance(chance)) {
			strength = randMod;
		}
		randMod = Util.pick(MOD_MAGIC);
		chance = randMod.chance + levelNumber;
		if (Util.chance(chance)) {
			magic = randMod;
		}
		randMod = Util.pick(MOD_ADDITIONAL);
		chance = randMod.chance + levelNumber;
		if (Util.chance(chance)) {
			additional = randMod;
		}

		if (strength != null)
			weapon.addPreModifier(strength);
		if (magic != null)
			weapon.addPreModifier(magic);
		if (additional != null)
			weapon.addPostModifier(additional);
	}

	public void setArmorModifiers(Item weapon, int levelNumber) {
		Modifier strength = null;
		Modifier magic = null;
		Modifier additional = null;
		Modifier rand = Util.pick(MOD_ARMOR_STRENGTH);
		int chance = rand.chance + levelNumber;
		if (Util.chance(chance)) {
			strength = rand;
		}
		rand = Util.pick(MOD_ARMOR_MAGIC);
		chance = rand.chance + levelNumber;
		if (Util.chance(chance)) {
			magic = rand;
		}
		rand = Util.pick(MOD_ARMOR_ADDITIONAL);
		chance = rand.chance + levelNumber;
		if (Util.chance(chance)) {
			additional = rand;
		}
		if (strength != null)
			weapon.addPreModifier(strength);
		if (magic != null)
			weapon.addPreModifier(magic);
		if (additional != null)
			weapon.addPostModifier(additional);
	}

	public Item createWeapon(Modifier strength, Modifier magic,
			Modifier material, String baseID, Modifier additional) {
		Item weapon = createItem(baseID);
		if (weapon != null)
			weapon.addPreModifier(strength);
		if (magic != null)
			weapon.addPreModifier(magic);
		weapon.addPreModifier(material);
		if (additional != null)
			weapon.addPostModifier(additional);
		return weapon;
	}

	public Item createArmor(Modifier strength, Modifier magic,
			Modifier material, String baseID, Modifier additional) {
		Item weapon = createItem(baseID);
		if (weapon != null)
			weapon.addPreModifier(strength);
		if (magic != null)
			weapon.addPreModifier(magic);
		weapon.addPreModifier(material);
		if (additional != null)
			weapon.addPostModifier(additional);
		return weapon;
	}

}