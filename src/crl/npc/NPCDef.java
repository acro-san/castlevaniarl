package crl.npc;

import sz.csi.textcomponents.ListItem;
import crl.ui.*;
import crl.Main;
import crl.ai.*;

public class NPCDef {
	
	private String ID;
	public String description;
	
	private String talkMessage;		// dialogue. TODO: TxtTpl'd based on player status checks, prior meetings etc?
	private int attack;
	private int hits;
	private String angryMessage;
	private String dangerMessage;
	public boolean isHostage = false;
	public boolean isPriest = false;
	private Appearance appearance;
	private ActionSelector defaultSelector;	// ?
	private ListItem sightListItem;	// ?

	public NPCDef (String pID, String pDescription, String pAppearance,
	String pDefaultSelectorID, String pTalkMessage, int pAttack, int pHits,
	String pAngryMessage, String pDangerMessage, boolean pHostage, boolean pPriest) {
		ID = pID;
		description = pDescription;
		appearance = Main.appearances.get(pAppearance);
		
		// derive a new action selector from existing by fetching existing by ID
		ActionSelector baseAI = Main.selectors.get(pDefaultSelectorID);
		defaultSelector = baseAI.derive();
		
		//sightListItem = new BasicListItem(appearance.getChar(), appearance.getColor(), description);
		talkMessage = pTalkMessage;
		
		attack = pAttack;
		hits = pHits;
		angryMessage = pAngryMessage;
		dangerMessage = pDangerMessage;
		isHostage = pHostage;
		isPriest = pPriest;
	}

//	public String getDescription() {
//		return description;
//	}

	public Appearance getAppearance() {
		return appearance;
	}

	public ActionSelector getDefaultSelector() {
		return defaultSelector;
	}

	public String getTalkMessage() {
		return talkMessage;
	}

	public String getID() {
		return ID;
	}

	public int getAttack() {
		return attack;
	}

	public int getHits() {
		return hits;
	}
	
	public ListItem getSightListItem(){
		return sightListItem;
	}

	public String getAngryMessage(){
		return angryMessage;
	}
	
	public String getDangerMessage(){
		return dangerMessage;
	}
/*
	public boolean isHostage() {
		return isHostage;
	}

	public void setHostage(boolean isHostage) {
		this.isHostage = isHostage;
	}
*/
	/*
	public boolean isPriest() {
		return isPriest;
	}

	public void setPriest(boolean isPriest) {
		this.isPriest = isPriest;
	}
	*/
	
}


