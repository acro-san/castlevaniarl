package crl.feature;

import crl.Main;
import crl.actor.*;
import crl.ui.*;

public class SmartFeature extends Actor implements Cloneable {
	
	public int height;
	public boolean destroyable;
	public int damageOnStep;
	
	private transient Appearance appearance;
	
	public String
		ID,
		description,
		appearanceID,
		effectOnStep;

	public SmartFeature (String pID, String pDescription, Appearance pAppearance) {
		ID = pID;
		description = pDescription;
		appearance = pAppearance;
		appearanceID = pAppearance.getID();
	}


	public Object clone() {
	//	try {
			SmartFeature x = (SmartFeature)super.clone();
			//x.setSelector(selector.derive());
			x.selector = selector.derive();
			return x;
/*		} catch (CloneNotSupportedException cnse){
			Debug.doAssert(false, "failed class cast, Feature.clone()");*/
//		}                        */
//		return null;
	}

	public Appearance getAppearance() {
		if (appearance == null) {
			if (appearanceID != null) {
				appearance = Main.appearances.get(appearanceID);
			}
		}
		return appearance;
	}

	public void setAppearance(Appearance value) {	// by ID, though?
		appearance = value;
	}


	public boolean isVisible() {
		return !getAppearance().getID().equals("VOID");
	}
}