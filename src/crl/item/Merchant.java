package crl.item;

import crl.npc.*;

import java.util.*;

import sz.util.*;
import crl.player.*;
import crl.Main;
import crl.data.*;

public class Merchant extends NPC {
	
	private String merchantName;
	private int refreshTurns = -1;
	private byte merchandiseType;
	
	private final static String[] MERCHANT_NAMES = {
		"Kaleth",
		"Adam",
		"Invenior",
		"Dimitri",
		"Merdotios",
		"Richard",
		"Tommy",
		"Valentina",
		"Astrith",
		"Julieth",
		"Jazeth",
		"Juran",
		"Camilla",
		"Emer"
	};

	public String getMerchandiseTypeDesc() {
		switch (merchandiseType) {
		case ItemDefinition.SHOPTYPE_ARMOR:
			return "armor";
		case ItemDefinition.SHOPTYPE_CRAFTS:
			return "general goods";
		case ItemDefinition.SHOPTYPE_MAGIC:
			return "magic goods";
		case ItemDefinition.SHOPTYPE_WEAPONS:
			return "weapons";
		}
		return "";
	}
	
	public String getName(){
		return merchantName;
	}

	public Merchant(NPCDefinition def, int pMerchandiseType) {
		super(def);
		merchandiseType = (byte)pMerchandiseType;
		merchantName = MERCHANT_NAMES[Util.rand(0, MERCHANT_NAMES.length-1)];
	}
	
	public String getDescription() {
		return merchantName;
	}
	
	public Vector<Item> getMerchandiseFor(Player player) {
		int gameTurns = player.getGameSessionInfo().turns;
		if (refreshTurns == -1 || gameTurns - refreshTurns > 1000) {
			if (player.getPlayerClass() == Player.CLASS_VAMPIREKILLER && (
					merchandiseType == ItemDefinition.SHOPTYPE_WEAPONS ||
					merchandiseType == ItemDefinition.SHOPTYPE_ARMOR)
					)
			{
				;	// no refresh 
			} else {
				refreshMerchandise(player);
			}
			refreshTurns = gameTurns;
		}
		return inventory;
	}

	private Vector<Item> inventory;

	/*private void refreshMerchandise(Player player){
		merchandises = new Vector();
		ItemDefinition[] defs = Items.getItemDefinitions();
		//int itemNumber = Util.rand(6,12);
		int itemNumber = Util.rand(6,12);
		int items = 0;
		int tries = 0;
		while (items < itemNumber){
			tries++;
			if (tries > 200)
				break;
			ItemDefinition trying = defs[Util.rand(0, defs.length - 1)];
			if (trying.getShopCategory() != merchandiseType)
				continue;
			if (merchandises.contains(trying.getShopMenuItem()))
				continue;
			if (Util.chance(trying.getRarity())){
				merchandises.add(trying.getShopMenuItem());
				items++;
			}
		}
	}*/
	
	public void refreshMerchandise(Player player) {
		if (player.getPlayerClass() == Player.CLASS_VAMPIREKILLER && (
				merchandiseType == ItemDefinition.SHOPTYPE_WEAPONS ||
				merchandiseType == ItemDefinition.SHOPTYPE_ARMOR)
			)
		{
			return;
		}
		
		inventory = new Vector<>();
		Vector<String> vectorIDs = new Vector<>();
		ItemDefinition[] defs = Items.defs;
		int itemNumber = Util.rand(6,12);
		int items = 0;
		int tries = 0;
		while (items < itemNumber) {
			tries++;
			if (tries > 200)
				break;
			ItemDefinition def = defs[Util.rand(0, defs.length - 1)];
			if (def.shopCategory != merchandiseType) {
				continue;
			}
			if (!Util.chance(def.shopChance)) {
				continue;
			}
			if (vectorIDs.contains(def.getID())) {
				continue;
			}
			items++;
			Item item = new Item(def);
			vectorIDs.add(def.getID());
			if (def.isUnique) {
				inventory.add(item);
			} else if (def.attack > 0) {
				if (!def.isFixedMaterial) {
					Main.itemData.setMaterial(item, level.getLevelNumber(), ItemDataTable.MOD_MATERIAL);
				}
				if (Util.chance(20)) {
					Main.itemData.setWeaponModifiers(item, level.getLevelNumber());
				}
				inventory.add(item);
			} else if (def.defense > 0) {
				if (!def.isFixedMaterial) {
					Main.itemData.setMaterial(item, level.getLevelNumber(),ItemDataTable.MOD_ARMOR_MATERIAL);
				}
				if (Util.chance(10)) {
					Main.itemData.setArmorModifiers(item, level.getLevelNumber());
				}
				inventory.add(item);
			} else {
				inventory.add(item);
			}
		}
	}


	public int getAttack() {
		return 4;
	}
}