package crl.npc;

import sz.csi.textcomponents.ListItem;
import crl.Main;
import crl.ai.*;
import crl.ai.npc.VillagerAI;
import crl.ui.*;
import crl.monster.*;

public class NPC extends Monster {
	
	// isnt an NPCDef the same as a monster def?
	private transient NPCDef definition;	// only need set on init and reload. right?
	//private String defID;	// allows fetching definition from global def table
	// already declared in monster. right?
	
	private ActionSelector selector;
	private String npcID;
	private String talkMessage = null;

//	private final static MonsterDefinition NPC_MONSTER_DEFINITION = new MonsterDefinition("NPC", "NPC", "VOID", "NULL_SELECTOR", 0, 2, 0, 5, 0, false, false, true, false, 0, 0, 0, 0);
	
	public final static MonsterDefinition
		NPC_MONSTER_DEFINITION = new MonsterDefinition("NPC");
	static {
		NPC_MONSTER_DEFINITION.description = "Innocent Being";
		NPC_MONSTER_DEFINITION.appearance = Main.appearances.get("VOID");
		NPC_MONSTER_DEFINITION.defaultSelector = Main.selectors.get("NULL_SELECTOR");
		NPC_MONSTER_DEFINITION.score = -100;
		NPC_MONSTER_DEFINITION.maxHP = 2;
		NPC_MONSTER_DEFINITION.attack = 0;
		NPC_MONSTER_DEFINITION.sightRange = 5;
		NPC_MONSTER_DEFINITION.bloodContent = 30;
	}

	public NPC(NPCDef def) {
		super(NPC_MONSTER_DEFINITION);
		definition = def;
		defID = def.getID();
		npcID = def.getID();
		selector = getNDefinition().getDefaultSelector().derive();
		hp = def.hpMax;
	}
	
	public Appearance getAppearance() {
		return getNDefinition().getAppearance();
	}

	public ActionSelector getSelector() {
		return selector;
	}

	public String getDescription() {
		return getNDefinition().description;
	}
	
	private NPCDef getNDefinition() {
		if (definition == null) {
			definition = NPCData.getDefinition(defID);
			// why not just get it out of the global defs array though?
			// by npcID?
		}
		return definition;
	}


	public String getTalkMessage() {
		if (talkMessage == null) {
			return getNDefinition().getTalkMessage();
		} else {
			return talkMessage;
		}
	}


	@Override
	public void message(String msg) {
		try {
			if (msg.equals("ATTACK_PLAYER")) {
				((VillagerAI)getSelector()).setAttackPlayer(true);
			} else if (msg.equals("EVT_MURDERER")) {
				if (hp > 1) {
					((VillagerAI)getSelector()).setAttackPlayer(true);
				} else {
					((VillagerAI)getSelector()).setOnDanger(true);
				}
			}
		} catch (ClassCastException cce) {
			
		}
	}

	@Override
	public void damage(StringBuffer buff, int dam) {
		try {
			((VillagerAI)getSelector()).setOnDanger(true);
			if (hp > 1) {
				((VillagerAI)getSelector()).setAttackPlayer(true);
			}
			level.signal(pos, 8, "EVT_MURDERER");
		} catch (ClassCastException cce) {
			
		}
		super.damage(buff, dam);
	}

	public int getAttack() {
		return getNDefinition().getAttack();
	}
	
	public ListItem getSightListItem(){
		return getNDefinition().getSightListItem();
	}
	
	public String getAngryMessage(){
		return getNDefinition().getAngryMessage();
	}
	
	public String getDangerMessage(){
		return getNDefinition().getDangerMessage();
	}
	
	public boolean isHostile(){
		return ((VillagerAI)getSelector()).isHostile(); 
	}
	
	public boolean isPriest(){
		return getNDefinition().isPriest;
	}
	

	public String getNPCID() {
		return npcID;
	}
	
	public void setTalkMessage(String message) {
		talkMessage = message;
	}
}
