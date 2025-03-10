package crl.action.vkiller;

import crl.Main;
import crl.action.AT;
import crl.action.HeartAction;
import crl.player.Player;
import crl.ui.ActionCancelException;
import crl.ui.UserInterface;

public class ItemBreak extends HeartAction {
	
	private HeartAction
		AXEBREAK = new ItemBreakAxe(),
		BIBLEBREAK = new ItemBreakBible(),
		CRYSTALBREAK = new ItemBreakBlastCrystal(),
		CROSSBREAK = new ItemBreakCross(),
		DAGGERBREAK = new ItemBreakDagger(),
		HOLYBREAK = new ItemBreakHoly(),
		FISTBREAK = new ItemBreakSacredFist(),
		STOPBREAK = new ItemBreakStopwatch();
	
	public int getHeartCost() {
		//return ((HeartAction)(getPlayer().getMysticAction())).getHeartCost();
		HeartAction breakAction = getBreakAction();
		if (breakAction != null)
			return getBreakAction().getHeartCost();
		else return 0;
	}
	
	public AT getID() {
		return AT.ItemBreak;
	}
	
	public HeartAction getBreakAction() {
		switch (getPlayer().getMysticWeapon()) {
		case Player.AXE:
			return AXEBREAK;
		case Player.BIBLE:
			return BIBLEBREAK;
		case Player.SACRED_CRYSTAL:
			return CRYSTALBREAK;
		case Player.CROSS:
			return CROSSBREAK;
		case Player.DAGGER:
			return DAGGERBREAK;
		case Player.HOLY:
			return HOLYBREAK;
		case Player.SACRED_FIST:
			return FISTBREAK;
		case Player.STOPWATCH:
			return STOPBREAK;
		}
		return null;
	}
	
	public void execute() {
		super.execute();
		HeartAction breakAction = getBreakAction();
		if (breakAction == null){
			getPlayer().level.addMessage("??");
			return;
			
		}
		try {
			Main.ui.setTargets(breakAction);
			if (breakAction.canPerform(performer))
				breakAction.execute();
		} catch (ActionCancelException ace){
			getPlayer().level.addMessage("Cancelled. ");
		}
	}
}
