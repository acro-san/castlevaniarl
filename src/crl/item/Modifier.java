package crl.item;

import java.io.Serializable;

public class Modifier implements Serializable {

	
	public String
		id,			// FIXME -> short. or int. then use as def array indices.
		description;

	public double
		priceModifier;
	public int
		chance,
		atkBonus,
		rangeBonus,
		defenseBonus,
		atkCostBonus;
	public boolean
		harmsUndead,
		slicesThru;

	public Modifier(String ID, String description, int chance) {
		this.id = ID;
		this.description = description;
		this.chance = chance;
	}
	
	/*
	public int getAtkBonus() {
		return atkBonus;
	}
	public void setAtkBonus(int atkBonus) {
		this.atkBonus = atkBonus;
	}
	public int getAtkCostBonus() {
		return atkCostBonus;
	}
	public void setAtkCostBonus(int atkCostBonus) {
		this.atkCostBonus = atkCostBonus;
	}
	public int getDefenseBonus() {
		return defenseBonus;
	}
	public void setDefenseBonus(int defenseBonus) {
		this.defenseBonus = defenseBonus;
	}
	public boolean isHarmsUndead() {
		return harmsUndead;
	}
	public void setHarmsUndead(boolean harmsUndead) {
		this.harmsUndead = harmsUndead;
	}
	public int getRangeBonus() {
		return rangeBonus;
	}
	public void setRangeBonus(int rangeBonus) {
		this.rangeBonus = rangeBonus;
	}
	public boolean isSlicesThru() {
		return slicesThru;
	}
	public void setSlicesThru(boolean slicesThru) {
		this.slicesThru = slicesThru;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPriceModifier() {
		return priceModifier;
	}
	public void setPriceModifier(double priceModifier) {
		this.priceModifier = priceModifier;
	}
	*/
}
