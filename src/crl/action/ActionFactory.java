package crl.action;

import java.util.Hashtable;

import sz.util.Debug;

public class ActionFactory {	// NON-CLASS!

	//public static Hashtable<AT, Action> definitions = new Hashtable<>(30);

	/*
	private final static ActionFactory singleton = new ActionFactory();
	public static ActionFactory getActionFactory(){
		return singleton;
	}
	*/
/*
	public Action getAction(AT id){
		Action ret = definitions.get(id);
		Debug.doAssert(ret != null, "Tried to get an invalid "+id+" Action");
		return ret;
	}
*/
	// this would be... the ONLY WAY in the entire codebase, for def.getID() to
	// be referenced, other than the one in init() in Main where about 7 actions
	// are added.
//	private void addDefinition(Action definition) {
//		definitions.put(definition.getID(), definition);
//	}

	
}
