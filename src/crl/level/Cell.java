package crl.level;

import sz.util.*;
import crl.Main;
import crl.ui.*;

public class Cell implements Cloneable, java.io.Serializable {
	
	// Is this a tile/terrain *definition* ... or an instance of such?
	// one should only require an index. unless there's STATE? (key/door unlock state for example)?
	
	private boolean isStair, isSolid, isWater, isOpaque, shallowWater;
	private boolean ethereal;
	private transient Appearance appearance;
	private String appearanceID;	// sprite index??
	private String ID;
	private String description, shortDescription;
	private int height;
	private int heightMod;

	private int damageOnStep;
	private int keyCost;


	public final static int DOOR = 0;

	// FIXME TILE_TYPEs. Tile IDs. DEFINITELY should *NOT* be strings!!
	public String getID() {
		return ID;
	}

	public Cell(String pID, String pShortDescription, String pDescription, Appearance pApp) {
		ID = pID;
		appearance = pApp;
		appearanceID = pApp.getID();
		description = pDescription;
		shortDescription = pShortDescription;
		Debug.doAssert(pApp != null, "No se especifico apariencia pa la celda");
	}

	public String getShortDescription(){
		return shortDescription;
	}

	public Cell (String pID, String sdes, String pDescription, Appearance pApp, boolean isSolid, boolean isOpaque){
		this(pID, sdes, pDescription, pApp);
		this.isSolid = isSolid;
		this.isOpaque = isOpaque;
	}

	public void setAppearance(Appearance what){
		appearanceID = what.getID();
		appearance = what;
	}
	
	public Appearance getAppearance(){
		if (appearance == null){
			if (appearanceID != null)
				appearance = Main.appearances.get(appearanceID);
		}
		return appearance;
	}

	public boolean isSolid(){
		return isSolid;
	}

	public boolean isWater(){
		return isWater;
	}

	public void setWater(boolean what){
		isWater = what;
	}

	public String getDescription() {
		return description;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int value) {
		height = value;
	}

	public boolean isStair() {
		return isStair;
	}

	public void setIsStair(boolean value) {
		isStair = value;
	}

	public int getKeyCost() {
		return keyCost;
	}

	public void setKeyCost(int value) {
		keyCost = value;
	}

	public int getDamageOnStep() {
		return damageOnStep;
	}

	public void setDamageOnStep(int damageOnStep) {
		this.damageOnStep = damageOnStep;
	}

	public int getHeightMod() {
		return heightMod;
	}

	public void setHeightMod(int heightMod) {
		this.heightMod = heightMod;
	}

	public boolean isOpaque() {
		return isOpaque;
	}

	public boolean isEthereal() {
		return ethereal;
	}

	public void setEthereal(boolean ethereal) {
		this.ethereal = ethereal;
	}

	public boolean isShallowWater() {
		return shallowWater;
	}

	public void setShallowWater(boolean shallowWater) {
		this.shallowWater = shallowWater;
	}
}