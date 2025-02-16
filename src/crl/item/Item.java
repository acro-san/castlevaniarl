package crl.item;

import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import sz.csi.textcomponents.MenuItem;
import sz.gadgets.GFXMenuItem;
import crl.Main;
import crl.player.Consts;
import crl.player.Player;
import crl.ui.*;
import crl.ui.consoleUI.CharAppearance;
import crl.ui.graphicsUI.GFXAppearance;


public class Item implements Serializable, MenuItem, GFXMenuItem {
	private transient ItemDefinition definition;
	private String defID;
	//Status
	private int remainingTurnsToReload;
	private ArrayList<Modifier> premodifiers = new ArrayList<>(10);
	private ArrayList<Modifier> postmodifiers = new ArrayList<>(10);
	public static boolean shopMode = false;
	
	public Item(ItemDefinition itemDef) {
		definition = itemDef;
		defID = definition.getID();
		reload();
	}
	
	public ItemDefinition getDefinition() {
		if (definition == null){
			definition = Main.itemData.get(defID);
		}
		return definition;
	}

	public int getRemainingTurnsToReload() {
		return remainingTurnsToReload;
	}

	public void setRemainingTurnsToReload(int value) {
		remainingTurnsToReload = value;
	}

	public void reload(){
		setRemainingTurnsToReload(getDefinition().reloadTurns);
	}

	public boolean isVisible(){
		return !getDefinition().appearance.getID().equals("VOID");
	}

	/*Unsafe, Coupled*/
	public char getMenuChar() {
		return ((CharAppearance)getDefinition().appearance).getChar();
	}
	
	/*Unsafe, Coupled*/
	public int getMenuColor() {
		return ((CharAppearance)getDefinition().appearance).getColor();
	}

	public String getMenuDescription() {
		if (shopMode)
			return getAttributesDescription()+ " ["+getDefinition().menuDescription+"] ($"+getGoldPrice()+")";
		else
			return getAttributesDescription();
	}
	
	public String getDescription(){
		String description = "";
		for (int i = 0; i < premodifiers.size(); i++) {
			description += premodifiers.get(i).description;
		}
		description += getDefinition().description;
		for (int i = 0; i < postmodifiers.size(); i++) {
			description += postmodifiers.get(i).description;
		}
		return description;
	}
	
	
	public Appearance getAppearance() {
		return getDefinition().appearance;
	}


	private int getModifiersAttackBonus() {
		int ret = 0;
		for (int i = 0; i < premodifiers.size(); i++) {
			ret += ((Modifier)premodifiers.get(i)).atkBonus;
		}
		for (int i = 0; i < postmodifiers.size(); i++) {
			ret += ((Modifier)postmodifiers.get(i)).atkBonus;
		}
		return ret;
	}
	
	private int getModifiersAttackCost() {
		int ret = 0;
		for (int i = 0; i < premodifiers.size(); i++) {
			ret += premodifiers.get(i).atkCostBonus;
		}
		for (int i = 0; i < postmodifiers.size(); i++){
			ret += postmodifiers.get(i).atkCostBonus;
		}
		return ret;
	}
	
	private double getModifiersGoldMod() {
		double ret = 0;
		for (int i = 0; i < premodifiers.size(); i++) {
			ret += premodifiers.get(i).priceModifier / 100.0;
		}
		for (int i = 0; i < postmodifiers.size(); i++) {
			ret += postmodifiers.get(i).priceModifier / 100.0;
		}
		return ret;
	}
	
	private int getModifiersDefenseBonus() {
		int ret = 0;
		for (int i = 0; i < premodifiers.size(); i++) {
			ret += premodifiers.get(i).defenseBonus;
		}
		for (int i = 0; i < postmodifiers.size(); i++) {
			ret += postmodifiers.get(i).defenseBonus;
		}
		return ret;
	}
	
	private int getModifiersRangeBonus() {
		int ret = 0;
		for (int i = 0; i < premodifiers.size(); i++) {
			ret += premodifiers.get(i).rangeBonus;
		}
		for (int i = 0; i < postmodifiers.size(); i++) {
			ret += postmodifiers.get(i).rangeBonus;
		}
		return ret;
	}
	
	private boolean modifiersHarmUndead() {
		for (int i = 0; i < premodifiers.size(); i++) {
			if (premodifiers.get(i).harmsUndead) {
				return true;
			}
		}
		for (int i = 0; i < postmodifiers.size(); i++) {
			if (postmodifiers.get(i).harmsUndead) {
				return true;
			}
		}
		return false;
	}
	
	private boolean modifiersSliceThru() {
		for (int i = 0; i < premodifiers.size(); i++) {
			if (premodifiers.get(i).slicesThru) {
				return true;
			}
		}
		for (int i = 0; i < postmodifiers.size(); i++) {
			if (postmodifiers.get(i).slicesThru) {
				return true;
			}
		}
		return false;
	}
	
	
	public int getAttack() {
		int ret = getDefinition().attack + getModifiersAttackBonus();
		if (getDefinition().attack > 0 && ret <= 0) {
			return 1;
		}
		return ret;
	}

	public int getAttackCost() {
		return getDefinition().attackCost + getModifiersAttackCost();
	}

	public int getDefense() {
		return getDefinition().defense + getModifiersDefenseBonus();
	}

	public String getEffectOnAcquire() {
		return getDefinition().effectOnAcquire;
	}

	/*public String getEffectOnStep() {
		return getDefinition().getEffectOnStep();
	}*/

	public String getEffectOnUse() {
		return getDefinition().effectOnUse;
	}

	public int getFeatureTurns() {
		return getDefinition().featureTurns;
	}

	public boolean isHarmsUndead() {
		return getDefinition().harmsUndead || modifiersHarmUndead();
	}

	public String getPlacedSmartFeature() {
		return getDefinition().placedSmartFeature;
	}

	public int getRange() {
		return getDefinition().range + getModifiersRangeBonus();
	}

	public int getReloadTurns() {
		return getDefinition().reloadTurns;
	}

	public boolean isSlicesThrough() {
		return getDefinition().slicesThrough || modifiersSliceThru();
	}

	/*public String getThrowMessage() {
		return getDefinition().getThrowMessage();
	}*/

	public int getThrowRange() {
		return getDefinition().throwRange;
	}

	public String getUseMessage() {
		return getDefinition().useMessage;
	}

	public int getVerticalRange() {
		return getDefinition().verticalRange;
	}
	
	public String getAttributesDescription(){
		String base = getDescription();
		if (getRemainingTurnsToReload()>0){
			base += " {"+getRemainingTurnsToReload()+"}";
		}
		if (getAttack() > 0 || getDefense() > 0 || getRange() > 1 || getVerticalRange() > 0)
			base+= " (";
		if (getAttack() > 0){
			if (hasCounter(Consts.C_WEAPON_ENCHANTMENT)){
				base+= "ATK:"+getAttack()+"+2 ";
			} else {
				base+= "ATK:"+getAttack()+" ";
			}
		}
			
		if (getDefense() > 0)
			base+= "DEF:"+getDefense()+" ";
		if (getCoverage() > 0)
			base+= "COV:"+getCoverage()+"% ";
		if (getRange() > 1 || getVerticalRange() > 0)
			if (getVerticalRange() > 0)
				base+= "RNG:"+getRange()+","+getVerticalRange();
			else
				base+= "RNG:"+getRange();
		if (definition.reloadCostGold > 0){
			base += " RLD:"+definition.reloadCostGold+"$";
		}
		if (getAttack() > 0 || getDefense() > 0 || getRange() > 1 || getVerticalRange() > 0)
			base+= ")";
		if (isTwoHanded()){
			base+= "(2H)";	
		}
		return base;
	}
	
	
	public String getFullID() {	// FIXME Uh oh!!!! Why're we concatenating IDs, eh??
		String toAddID = getDefinition().getID();
		for (int i = 0; i < premodifiers.size(); i++) {
			toAddID += (premodifiers.get(i)).id;
		}
		for (int i = 0; i < postmodifiers.size(); i++) {
			toAddID += postmodifiers.get(i).id;
		}
		return toAddID;
	}
	
	public String getMenuDetail() {
		return null;
	}

	public Image getMenuImage() {
		return ((GFXAppearance)getAppearance()).getIconImage();
	}
	
	public void addPreModifier(Modifier pre){
		premodifiers.add(pre);
	}
	
	public void addPostModifier(Modifier post){
		postmodifiers.add(post);
	}
	
	public String getShopDescription(){
		return getDefinition().shopDescription;
	}
	
	public int getGoldPrice(){
		double modifiersGold = 1 + getModifiersGoldMod();
		if (modifiersGold == 1)
			return getDefinition().goldPrice;
		else
			return (int)(Math.round(getDefinition().goldPrice * getModifiersGoldMod()));
	}
	
	public String getWeaponCategory(){
		return getDefinition().weaponCategory;
	}
	
	protected Hashtable<String,Integer> hashCounters = new Hashtable<>();
	public void setCounter(String counterID, int turns) {
		hashCounters.put(counterID, Integer.valueOf(turns));
	}
	
	public int getCounter(String counterID) {
		Integer val = hashCounters.get(counterID);
		if (val == null) {
			return -1;
		} else {
			return val.intValue();
		}
	}
	
	public boolean hasCounter(String counterID) {
		return getCounter(counterID) > 0;
	}
	
	public void reduceCounters(Player p){
		Enumeration<String> countersKeys = hashCounters.keys();
		while (countersKeys.hasMoreElements()) {
			String key = countersKeys.nextElement();
			Integer counter = hashCounters.get(key);
			if (counter.intValue() == 0) {
				if (key.equals(Consts.C_WEAPON_ENCHANTMENT)) {
					p.level.addMessage("Your "+getDescription()+" stops glowing.");
				}
				hashCounters.remove(key);
			} else {
				hashCounters.put(key, Integer.valueOf(counter.intValue()-1));
			}
		}
	}
	
	public boolean hasCounters(){
		return hashCounters.size() > 0;
	}
	
	public boolean isTwoHanded(){
		return getDefinition().isTwoHanded;
	}
	
	public int getCoverage(){
		return getDefinition().coverage;
	}
	
	public String getAttackSound(){
		return getDefinition().attackSound;
	}
	
	
}