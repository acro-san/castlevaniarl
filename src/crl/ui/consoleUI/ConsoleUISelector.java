package crl.ui.consoleUI;

import java.util.Properties;

import sz.csi.CharKey;
import sz.csi.ConsoleSystemInterface;
import sz.util.Debug;
import sz.util.Position;
import crl.Main;
import crl.action.Action;
import crl.actor.Actor;
import crl.actor.Message;
import crl.ai.AIT;
import crl.ai.ActionSelector;
import crl.game.Cheat;
import crl.monster.Monster;
import crl.npc.NPC;
import crl.player.Player;
import crl.ui.ActionCancelException;
import crl.ui.UISelector;
import crl.ui.UserAction;

public class ConsoleUISelector extends UISelector {
	
	private ConsoleSystemInterface si;	//transient?
	
	public ConsoleUserInterface ui() {
		return (ConsoleUserInterface)Main.ui;
	}
	
	public void init(ConsoleSystemInterface csi, UserAction[] gameActions, Action advance, Action target, Action attack, Properties keyBindings) {
		super.init(gameActions, advance, target, attack, keyBindings);
		this.si = csi;
	}
	
	/** 
	 * Returns the Action that the player wants to perform.
	 * It may also forward a command instead
	 */
	public Action selectAction(Actor who) {
		Debug.enterMethod(this, "selectAction", who);
		CharKey input = null;
		Action ret = null;
		while (ret == null) {
			if (ui().gameOver()) {
				return null;
			}
			input = si.inkey();
			ret = ui().selectCommand(input);
			if (ret != null)
				return ret;
			if (input.code == DONOTHING1_KEY) {
				Debug.exitMethod("null");
				return null;
			}
			if (input.code == DONOTHING2_KEY) {
				Debug.exitMethod("null");
				return null;
			}
			if (Cheat.cheatConsole(player, input.code)){
				continue;
			}
			if (isArrow(input)){
				int direction = toIntDirection(input);
				Monster vMonster = player.level.getMonsterAt(Position.add(player.pos, Action.directionToVariation(direction)));
				if (vMonster != null && 
					(!(vMonster instanceof NPC) || (vMonster instanceof NPC && ((NPC)vMonster).isHostile()))) {
					if (attack.canPerform(player)){
						attack.setDirection(direction);
						Debug.exitMethod(attack);
						return attack;
					} else {
						level.addMessage(attack.getInvalidationMessage());
						si.refresh();
					}
				} else {
					advance.setDirection(direction);
					Debug.exitMethod(advance);
					return advance;
				}
			} else
			if (input.code == WEAPON_KEY){
				if (player.playerClass == Player.CLASS_VAMPIREKILLER) {
					ret = player.getMysticAction();
					try {
						if (ret != null) {
							ret.setPerformer(player);
							if (ret.canPerform(player)) {
								ui().setTargets(ret);
							} else {
								level.addMessage(ret.getInvalidationMessage());
								throw new ActionCancelException();
							}
							Debug.exitMethod(ret);
							return ret;
						}
					} catch (ActionCancelException ace) {
						ret = null;
					}
				}
			} else {
				ret = getRelatedAction(input.code);
				try {
					if (ret != null) {
						ret.setPerformer(player);
						if (ret.canPerform(player)) {
							ui().setTargets(ret);
						} else {
							level.addMessage(ret.getInvalidationMessage());
							throw new ActionCancelException();
						}
						
						Debug.exitMethod(ret);
						return ret;
					}
				} catch (ActionCancelException ace) {
					//si.cls();
	 				//refresh();
					ui().addMessage(new Message("Cancelled", player.pos));
					ret = null;
				}
				//refresh();
			}
		}
		Debug.exitMethod("null");
		return null;
	}
	
	// This class rly seems like it ought not to be an 'AI' function.
	public AIT getID() {
		return AIT.UI;
	}
	
//	public ActionSelector derive() {
//		return null;
//	}
	
}
