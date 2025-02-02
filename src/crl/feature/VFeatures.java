package crl.feature;

import java.io.Serializable;
import java.util.*;

import sz.util.*;

public class VFeatures implements Serializable {
	
	private Vector<Feature> features;
	private Hashtable<Integer, Feature> mLocs;	// ? How many features can be on one Cell?

	private Vector<Feature> temp = new Vector<>();
	private Vector<Feature> tempVector = new Vector<>();	// ??
	
	public VFeatures(int size) {
		features = new Vector<>(size);
		mLocs = new Hashtable<>(size);
	}
	
	public void addFeature(Feature what) {
		features.add(what);
		//mLocs.put(what, what.getPosition());
		mLocs.put(what.getPosition().ihash(), what);
	}

	public Feature[] getFeaturesAt(Position p) {
		temp.clear();
		for (int i=0; i<features.size(); i++){
			if ((features.elementAt(i)).getPosition().equals(p)){
				temp.add(features.elementAt(i));
			}
		}
		if (temp.size() == 0){
			return null;
		} else {
			return (Feature[])temp.toArray(new Feature[temp.size()]);
		}
	}
	
	public Feature getFeatureAt(int x, int y) {
		int poshash = Position.ihash(x, y, 0);
		// array of? features at a given cell...?
		if (!mLocs.containsKey(poshash)) {
			return null;
		}
		return mLocs.get(poshash);
	}
	
	public Feature getFeatureAt(Position p){
		//return (Feature) mLocs.get(p);
		for (int i=0; i<features.size(); i++){
			if ((features.elementAt(i)).getPosition().equals(p)){
				return features.elementAt(i);
			}
		}
		//Debug.byebye("Feature not found! "+p);
		return null;
	}


	public void removeFeature(Feature o) {
		features.remove(o);
		// remove location-hashed entry also
		if (mLocs.containsValue(o)) {
			mLocs.remove(o);	// FIXME This looks wrong
		}
	}
	
	
	// This is used solely by Level class, to getAllOf "MOUND" and "CANDLE".
	public Vector<Feature> getAllOf(String featureID) {
		tempVector.removeAllElements();
		for (int i = 0; i < features.size(); i++) {
			Feature f = features.elementAt(i);
			if (f.getID().equals(featureID)) {
				tempVector.add(f);
			}
		}
		return tempVector;
	}

}