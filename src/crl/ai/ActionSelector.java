package crl.ai;

import crl.action.Action;
import crl.actor.Actor;
import sz.util.Debug;

// TODO: investigate why a 'class'/object is needed for this. a function
// that performs a specific set of decisions/actions could suffice? Answer:
// some of these implementor AI objects (action selectors) contain STATE.
public abstract class ActionSelector implements Cloneable, java.io.Serializable {
	
	public abstract Action selectAction(Actor who);
	
	public abstract AIT getID();
	
	public ActionSelector derive() {
		try {
			return (ActionSelector)clone();
		} catch (CloneNotSupportedException cnse) {
			Debug.byebye("Failed to clone AI "+getID());
			return null;
		}
	}

}