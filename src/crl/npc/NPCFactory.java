package crl.npc;

import java.util.*;
import crl.ui.*;
import crl.player.*;
import crl.Main;
import crl.item.*;
import sz.util.*;

public class NPCFactory {
	private final static NPCFactory singleton = new NPCFactory();
	private Hashtable<String,NPCDefinition> definitions;
	private Vector<String> hostages = new Vector<>();

	@Deprecated
	public static NPCFactory getFactory() {
		return singleton;
	}

	public NPC buildNPC(String id) {
		return new NPC((NPCDefinition)definitions.get(id));
	}
	
	public Merchant buildMerchant(int merchandiseType){
		return new Merchant(definitions.get("MERCHANT"), merchandiseType);
	}
	
	public Hostage buildHostage() {
		Hostage ret = new Hostage((NPCDefinition)definitions.get( Util.pick(hostages) ));
		Player p = Main.ui.getPlayer();	//FIXME: *SURELY* the UI isn't where the player's stored though?
		if (p.getPlayerClass() != Player.CLASS_VAMPIREKILLER) {
			int artifactCategory = ((int) (p.getPlayerLevel() / 6.0));
			ret.setItemReward(Main.itemData.createWeapon(Util.pick(hostageArtifacts[artifactCategory]),""));
		}
		return ret;
	}

	public NPCDefinition getDefinition(String id) {
		return (NPCDefinition)definitions.get(id);
	}

	public void addDefinition(NPCDefinition definition){
		definitions.put(definition.getID(), definition);
		if (definition.isHostage()) {
			hostages.add(definition.getID());
		}
	}

	public NPCFactory() {
		definitions = new Hashtable<>(40);
	}
	
	private static String[][] hostageArtifacts = {
		{"HOLBEIN_DAGGER", "SHOTEL"},
		{"WEREBANE", "ALCARDE_SPEAR", "ETHANOS_BLADE"},
		{"FIREBRAND", "GURTHANG", "HADOR"},
		{"ICEBRAND", "MORMEGIL", "VORPAL_BLADE"},
		{"GRAM", "CRISSAEGRIM"},
		{"KAISER_KNUCKLE", "OSAFUNE", "MASAMUNE"}
	};
}