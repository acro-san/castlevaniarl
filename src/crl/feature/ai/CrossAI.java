package crl.feature.ai;

import crl.Main;
import crl.action.Action;
import crl.actor.Actor;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.feature.SmartFeature;
import crl.feature.action.CrossBack;
import sz.util.Position;

public class CrossAI extends ActionSelector implements Cloneable {
	
	private Position targetPosition;
	
	public void setTargetPosition(Position value) {
		targetPosition = value;
	}

	public AIT getID() {
		return AIT.CROSS_SELECTOR;
	}

	public Action selectAction(Actor who) {
		Action ret = new CrossBack();
		ret.setPosition(targetPosition);
		who.die();
		who.level.removeSmartFeature((SmartFeature)who);
		Main.ui.getPlayer().see();
		Main.ui.refresh();
		return ret;
	}

}