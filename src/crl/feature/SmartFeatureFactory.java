package crl.feature;

import java.util.*;

import sz.util.*;

// just put the table in main...
public class SmartFeatureFactory {	// > SmartFeatures or SmartFeatureData

	private static Hashtable<String, SmartFeature>
		definitions = new Hashtable<>(40);
	
	// Get 'SmartFeature' CLONED OBJECT from global table of SmartFeature defs.
	public static SmartFeature buildFeature(String id) {
		SmartFeature x = definitions.get(id);
		if (x != null) {
			return (SmartFeature)x.clone();
		}
		Debug.byebye("SmartFeature "+id+" not found");
		return null;
	}
	
	public static void init(SmartFeature[] defs) {
		for (int i = 0; i < defs.length; i++)
			definitions.put(defs[i].ID, defs[i]);
	}

}
