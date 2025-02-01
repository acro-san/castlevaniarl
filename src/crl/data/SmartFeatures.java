package crl.data;

import java.util.HashMap;

import crl.Main;
//import crl.ai.SelectorFactory;
import crl.ai.ActionSelector;
import crl.feature.SmartFeature;
import crl.ui.Appearance;

public class SmartFeatures {
	public static SmartFeature[] getSmartFeatures(HashMap<String, ActionSelector> as) {
		HashMap<String, Appearance> aps = Main.appearances;
		SmartFeature [] ret = new SmartFeature [6];
		ret[0] = new SmartFeature("CROSS", "Holy cross", aps.get("CROSS"));
		ret[1] = new SmartFeature("BURNING_FLAME", "Burning Flame", aps.get("FLAME"));
		ret[2] = new SmartFeature("GARLIC", "Garlic", aps.get("GARLIC"));
		ret[3] = new SmartFeature("BIBUTI", "Bibuti Powder", aps.get("BIBUTI"));
		ret[4] = new SmartFeature("FLAME", "Magic Flame", aps.get("FLAME"));
		ret[5] = new SmartFeature("BLAST_CRYSTAL", "Blast Crystal", aps.get("BLAST_CRYSTAL"));
		
		ret[0].setSelector(as.get("CROSS_SELECTOR"));
		ret[1].setSelector(as.get("FLAME_SELECTOR"));
		ret[1].setDamageOnStep(1);
		ret[2].setSelector(as.get("COUNTDOWN"));
		ret[2].setEffectOnStep("TRAP");
		ret[3].setSelector(as.get("COUNTDOWN"));
		ret[3].setDamageOnStep(3);
		ret[4].setSelector(as.get("COUNTDOWN"));
		ret[4].setDamageOnStep(4);
		ret[5].setSelector(as.get("CRYSTAL_SELECTOR"));
		return ret;
	}
}
