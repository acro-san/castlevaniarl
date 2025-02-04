package crl.data;

import java.util.HashMap;

import crl.Main;
import crl.ai.ActionSelector;
import crl.feature.SmartFeature;
import crl.ui.Appearance;

public class SmartFeatures {
	
	public static SmartFeature[] getSmartFeatures(HashMap<String, ActionSelector> acs) {
		HashMap<String, Appearance> aps = Main.appearances;
		SmartFeature [] ret = new SmartFeature[6];
		ret[0] = new SmartFeature("CROSS", "Holy cross", aps.get("CROSS"));
		ret[0].selector = acs.get("CROSS_SELECTOR");
		
		ret[1] = new SmartFeature("BURNING_FLAME", "Burning Flame", aps.get("FLAME"));
		ret[1].selector = acs.get("FLAME_SELECTOR");
		ret[1].damageOnStep = 1;
		
		ret[2] = new SmartFeature("GARLIC", "Garlic", aps.get("GARLIC"));
		ret[2].selector = acs.get("COUNTDOWN");
		ret[2].setEffectOnStep("TRAP");
		
		ret[3] = new SmartFeature("BIBUTI", "Bibuti Powder", aps.get("BIBUTI"));
		ret[3].selector = acs.get("COUNTDOWN");
		ret[3].damageOnStep = 3;
		
		ret[4] = new SmartFeature("FLAME", "Magic Flame", aps.get("FLAME"));
		ret[4].selector = acs.get("COUNTDOWN");
		ret[4].damageOnStep = 4;
		
		ret[5] = new SmartFeature("BLAST_CRYSTAL", "Blast Crystal", aps.get("BLAST_CRYSTAL"));
		ret[5].selector = acs.get("CRYSTAL_SELECTOR");
		
		return ret;
	}
}
