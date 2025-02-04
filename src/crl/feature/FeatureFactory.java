package crl.feature;

import java.util.*;

import sz.util.*;

public class FeatureFactory {

	private static Hashtable<String,Feature> definitions = new Hashtable<>(40);
	
	public static Feature buildFeature(String id) {
		Feature x = (Feature)definitions.get(id);
		if (x != null) {
			return (Feature) x.clone();
		}
		Debug.byebye("Feature "+id+" not found");
		return null;
	}

	public static String getDescriptionForID(String id) {
		Feature x = definitions.get(id);
		if (x != null)
			return x.getDescription();
		else
		return "?";
	}

	public void addDefinition(Feature definition){
		definitions.put(definition.getID(), definition);
	}
	
	public static void init(Feature[] defs) {
		//definitions.clear();	//? If it gets reinited??
		assert(definitions.size() == 0);
		for (int i = 0; i < defs.length; i++) {
			definitions.put(defs[i].getID(), defs[i]);
		}
	}

}