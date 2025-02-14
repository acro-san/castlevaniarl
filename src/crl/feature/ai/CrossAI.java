package crl.feature.ai;

import crl.Main;
import crl.action.*;
import crl.action.vkiller.Cross;
import crl.ai.*;
import crl.actor.*;
import crl.feature.SmartFeature;
import crl.feature.action.CrossBack;
import crl.ui.UserInterface;
import sz.util.*;

public class CrossAI implements ActionSelector, Cloneable {
	
	public String getID() {
		return "CROSS_SELECTOR";
	}

	private Position targetPosition;


	public Action selectAction(Actor who) {
		Action ret = new CrossBack();
		ret.setPosition(targetPosition);
		who.die();
		who.level.removeSmartFeature((SmartFeature)who);
		Main.ui.getPlayer().see();
		Main.ui.refresh();
		return ret;
	}

	public ActionSelector derive() {
		try {
			return (ActionSelector)clone();
		} catch (CloneNotSupportedException cnse){
			return null;
		}
	}

	public void setTargetPosition(Position value) {
		targetPosition = value;
	}
}