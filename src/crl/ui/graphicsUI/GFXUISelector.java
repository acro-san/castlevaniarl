package crl.ui.graphicsUI;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.Serializable;
import java.util.Properties;

import sz.csi.CharKey;
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

public class GFXUISelector extends UISelector
	implements MouseListener, MouseMotionListener, Serializable {
	
	private transient SwingSystemInterface si;
	private boolean useMouse = false;
	
	public void init(SwingSystemInterface psi, UserAction[] gameActions, Properties UIProperties,
			Action advance, Action target, Action attack, Properties keyBindings) {
		super.init(gameActions, advance, target, attack, keyBindings);
		this.si = psi;
		if (UIProperties.getProperty("useMouse").equals("true")) {
			psi.addMouseListener(this);
			psi.addMouseMotionListener(this);
			useMouse = true;
		}
	}
	
	
	public GFXUserInterface ui() {
		return (GFXUserInterface)Main.ui;
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
			if (input.code == CharKey.NONE && !useMouse)
				continue;
			ret = ui().selectCommand(input);
			if (ret != null){
				if (ret.canPerform(player)) {
					return ret;
				} else {
					return null;
				}
			}
			if (input.code == DONOTHING1_KEY) {
				Debug.exitMethod("null");
				return null;
			}
			if (input.code == DONOTHING2_KEY) {
				Debug.exitMethod("null");
				return null;
			}
			
			if (Cheat.cheatConsole(player, input.code)) {
				continue;
			}
			
			if (useMouse && mousePosition != null) {
				mouseDirection = -1;
				if (level.isValidCoordinate(mousePosition)) {
					//if (level.getMonsterAt(mousePosition) != null){
					if (player.playerClass == Player.CLASS_VAMPIREKILLER) {
						ret = player.getMysticAction();
						try {
							if (ret != null) {
								ret.setPerformer(player);
								if (ret.canPerform(player))
									ret.setPosition(mousePosition);
								else {
									level.addMessage(ret.getInvalidationMessage());
									throw new ActionCancelException();
								}
								Debug.exitMethod(ret);
								mousePosition = null;
								return ret;
							}
						} catch (ActionCancelException ace) {
							ui().addMessage(new Message("- Cancelled", player.pos));
							si.refresh();
							ret = null;
						}
					} else {
						ret = target;
						try {
							ret.setPerformer(player);
							if (ret.canPerform(player))
								ret.setPosition(mousePosition);
							else {
								level.addMessage(ret.getInvalidationMessage());
								throw new ActionCancelException();
							}
							Debug.exitMethod(ret);
							mousePosition = null;
							return ret;
						}
						catch (ActionCancelException ace){
							ui().addMessage(new Message("- Cancelled", player.pos));
							si.refresh();
							ret = null;
						}
					}
				}
				mousePosition = null;
			}
			
			if (isArrow(input) || (useMouse && mousePosition == null && mouseDirection != -1)){
				int direction = -1;
				if (useMouse && mouseDirection != -1){
					direction = mouseDirection;
					mouseDirection = -1;
				} else {
					direction = toIntDirection(input);
				}
				
				Monster vMonster = player.level.getMonsterAt(Position.add(player.pos, Action.directionToVariation(direction)));
				if (vMonster != null && vMonster.getStandingHeight() == player.getStandingHeight() &&
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
					switch (direction){
					case Action.UPLEFT:
					case Action.LEFT:
					case Action.DOWNLEFT:
						ui().setFlipFacing(true);
						break;
					case Action.UPRIGHT:
					case Action.RIGHT:
					case Action.DOWNRIGHT:
						ui().setFlipFacing(false);
						break;
					}
					return advance;
				}
			} else
			if (input.code == WEAPON_KEY) {
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
						ui().addMessage(new Message("- Cancelled", player.pos));
						si.refresh();
						//si.cls();
						//refresh();
						ret = null;
					}
				} else {
					ret = target;
					try {
						ret.setPerformer(player);
						if (ret.canPerform(player)) {
							ui().setTargets(ret);
						} else {
							level.addMessage(ret.getInvalidationMessage());
							throw new ActionCancelException();
						}
						Debug.exitMethod(ret);
						return ret;
					} catch (ActionCancelException ace) {
						ui().addMessage(new Message("- Cancelled", player.pos));
						si.refresh();
						ret = null;
					}
				}
			} else {
				ret = getRelatedAction(input.code);
				/*if (ret == target) {
					defaultTarget = player.getNearestMonsterPosition();
				}*/
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
					ui().addMessage(new Message("- Cancelled", player.pos));
					ret = null;
				}
				//refresh();
			}
		}
		Debug.exitMethod("null");
		return null;
	}
	

	public AIT getID() {
		return AIT.UI;
	}

//	public ActionSelector derive() {
//		return null;
//	}
	
	
	public void mouseClicked(MouseEvent e) { }

	public void mouseEntered(MouseEvent e) { }

	public void mouseExited(MouseEvent e) { }

	int[] QDIRECTIONS = {
		Action.UPLEFT,
		Action.UP,
		Action.UPRIGHT,
		Action.LEFT,
		Action.SELF,
		Action.RIGHT,
		Action.DOWNLEFT,
		Action.DOWN,
		Action.DOWNRIGHT
	};

	private int mouseDirection = -1;
	private Position mousePosition;
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1){
			int quadrant = defineQuadrant(e.getPoint().x, e.getPoint().y);
			mouseDirection = QDIRECTIONS[quadrant-1];
		} else if (e.getButton() == MouseEvent.BUTTON3){
			translatePosition(e.getPoint().x, e.getPoint().y);
		}
	}

	public void mouseReleased(MouseEvent e) { }

	public void mouseDragged(MouseEvent e) { }

	public void mouseMoved(MouseEvent e) {
		switch (defineQuadrant(e.getX(), e.getY())) {
		case 9:
			si.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
			break;
		case 6:
			si.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
			break;
		case 3:
			si.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
			break;
		case 8:
			si.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
			break;
		case 5:
			si.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			break;
		case 2:
			si.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
			break;
		case 7:
			si.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
			break;
		case 4:
			si.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
			break;
		case 1:
			si.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
			break;
		}
		/*if (isCursorEnabled && updateCursorPosition(e.getPoint().x, e.getPoint().y))
			drawCursor();*/
	}
	

	
	// ??????? 800 x 600 !?? What about the other screen size(s)?!
	int x1 = (int)Math.round((800.0/9.0)*4.0);
	int x2 = (int)Math.round((800.0/9.0)*5.0);
	
	int y1 = (int)Math.round((600.0/9.0)*4.0);
	int y2 = (int)Math.round((600.0/9.0)*5.0);


	private int defineQuadrant(int x, int y) {
		if (x > x2)
			if (y > y2)
				return 9;
			else if (y>y1)
				return 6;
			else
				return 3;
		else if (x > x1)
			if (y > y2)
				return 8;
			else if (y>y1)
				return 5;
			else
				return 2;
		else
			if (y > y2)
				return 7;
			else if (y>y1)
				return 4;
			else
				return 1;
	}
	
	private Position tempRel = new Position(0,0);
	private void translatePosition(int x, int y) {
		int bigx = (int)Math.ceil(x/32.0);
		int bigy = (int)Math.ceil(y/32.0);
		tempRel.x = bigx-ui().PC_POS.x-1;
		tempRel.y = bigy-ui().PC_POS.y-1;
		mousePosition = Position.add(player.pos, tempRel);
	}


	public static int toIntDirection(Position what) {
		switch (what.x) {
			case 1:
				switch (what.y) {
					case 1:
						return Action.DOWNRIGHT;
					case 0:
						return Action.RIGHT;
					case -1:
						return Action.UPRIGHT;
				}
			case 0:
				switch (what.y){
					case 1:
						return Action.DOWN;
					case -1:
						return Action.UP;
				}
			case -1:
				switch (what.y) {
					case 1:
						return Action.DOWNLEFT;
					case 0:
						return Action.LEFT;
					case -1:
						return Action.UPLEFT;
				}
		}
		return -1;
	}


}