package crl.action;

import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.actor.Actor;
import crl.data.Text;
import crl.game.SFXManager;
import crl.item.ItemDefinition;
import crl.level.Level;
import crl.player.Consts;
import crl.player.Damage;
import crl.player.Player;


public class Use extends Action {
	
	public AT getID() {
		return AT.Use;
	}
	
	public boolean needsItem() {
		return true;
	}

	public String getPromptItem() {
		return "What do you want to use?";
	}

	public void execute() {
		Player p = (Player)performer;
		Level pLvl = p.level;
		ItemDefinition def = targetItem.getDefinition();
		String[] effect = def.effectOnUse.split(" ");
		
		if (def.getID().equals("SOUL_RECALL")) {
			if (p.getHostage() != null) {
				Main.ui.showMessage("Abandon "+p.getHostage().getDescription()+"? [Y/N]");
				if (Main.ui.prompt()) {
					p.abandonHostage();
				} else {
					return;
				}
			}
			p.informPlayerEvent(Player.EVT_GOTO_LEVEL, "TOWN0");
			// Check if we actually managed to go to level!
			if (!pLvl.getID().equals("TOWN0")) {
				pLvl.addMessage("An evil energy prevents the orb from working!");
				return;
			}
			SFXManager.play("wav/loutwarp.wav");
			// We need to properly relocate the player
			Position exit = pLvl.getExitFor("FOREST0");
			p.level.levelNumber = 0;
			p.landOn(Position.add(exit, new Position(-1,0,0)));
			p.reduceQuantityOf(targetItem);
			return;
		}
		
		if (def.getID().equals("OXY_HERB")) {
			p.level.addMessage("You bite the oxyherb. Air fills your breast!");
			if (p.isSwimming()) {
				p.setCounter("OXYGEN", p.getBreathing());
			}
			p.reduceQuantityOf(targetItem);
			return;
		}
		
		if (def.getID().startsWith("ART_CARD_")) {
			if (pLvl.getMapCell(p.pos).getID().equals("WEIRD_MACHINE")) {
				pLvl.addMessage("You insert the card into the machine!");
				p.setFlag("HAS_"+def.getID(), true);
				if (p.getFlag("HAS_ART_CARD_SOL") &&
					p.getFlag("HAS_ART_CARD_MOONS") &&
					p.getFlag("HAS_ART_CARD_DEATH") &&
					p.getFlag("HAS_ART_CARD_LOVE"))
				{
					pLvl.addMessage("The machine opens. A wooden music box plays a mellow melody. You take the music box");
					p.addItem(Main.itemData.createItem("JUKEBOX"));
				}
				p.reduceQuantityOf(targetItem);
				return;
			} else {
				performer.level.addMessage("You raise the "+targetItem.getDescription()+" up high! Nothing happens...");
				return;
			}
		}
		
		if (effect[0].equals("")) {
			performer.level.addMessage("You don't find a use for the "+targetItem.getDescription());
			//aPlayer.addItem(targetItem);
			return;
		}
		for (int cmd = 0; cmd < effect.length; cmd+=2) {
			String message = targetItem.getUseMessage();
			if (message.equals("")) {
				message = "You use the "+targetItem.getDescription();
			}
			if (effect[cmd].equals("DAYLIGHT")) {
				if (!pLvl.isDay()) {
					pLvl.addMessage("The card fizzles in a blast of light!");
					p.informPlayerEvent(Player.EVT_FORWARDTIME);
				} else {
					pLvl.addMessage("Nothing happens.");
				}
			} else if (effect[cmd].equals("MOONLIGHT")) {
				if (pLvl.isDay()) {
					pLvl.addMessage("The card fizzles in a puff of smoke!");
					p.informPlayerEvent(Player.EVT_FORWARDTIME);
				} else {
					pLvl.addMessage("Nothing happens.");
				}
			} else if (effect[cmd].equals("INCREASE_DEFENSE"))
				p.increaseDefense(Integer.parseInt(effect[cmd+1]));
			else if (effect[cmd].equals("INVINCIBILITY"))
				p.setInvincible(Integer.parseInt(effect[cmd+1]));
			else if (effect[cmd].equals("ENERGY_FIELD"))
				p.setEnergyField(Integer.parseInt(effect[cmd+1]));
			else if (effect[cmd].equals("READ_CLUE"))
				readClue(Integer.parseInt(effect[cmd+1]));
			
			/*else if (effect[cmd].equals("LIGHT"))
				aPlayer.setCounter("LIGHT",Integer.parseInt(effect[cmd+1]));
			*/
			else if (effect[cmd].equals("INCREASE_JUMPING"))
				p.increaseJumping(Integer.parseInt(effect[cmd+1]));
			else if (effect[cmd].equals("SETWHIP")) {
				if (effect[cmd+1].equals("LIT"))
					p.setLitWhip();
				else if (effect[cmd+1].equals("FLAME"))
					p.setFireWhip();
				else if (effect[cmd+1].equals("THORN"))
					p.setThornWhip();
			} else if (effect[cmd].equals("HEAL")) {
				if (effect[cmd+1].equals("NP"))
					p.heal();
				else
					p.heal(Integer.parseInt(effect[cmd+1]));
			} else if (effect[cmd].equals("FIREBALL"))
				//aPlayer.setFireballWhip(Integer.parseInt(effect[cmd+1]));
				p.setCounter(Consts.C_FIREBALL_WHIP, Integer.parseInt(effect[cmd+1]));
			else if (effect[cmd].equals("RECOVER"))
				p.heal(Integer.parseInt(effect[cmd+1]));
			else if (effect[cmd].equals("DAMAGE")) {
				if (p.isInvincible())
					pLvl.addMessage("The damage is repelled!");
				else
					p.selfDamage(message, Player.DAMAGE_USING_ITEM, new Damage(Integer.parseInt(effect[cmd+1]), false));
			} else if (effect[cmd].equals("LIGHT")) {
				p.setCounter("LIGHT", 200);
			}
			performer.level.addMessage(message.toString());
		}
		if (def.isSingleUse) {
			p.reduceQuantityOf(targetItem);
		}

	}

	private void readClue(int level){
		performer.level.addMessage("The page reads: \""+Text.USE_CLUES[level][Util.rand(0, Text.USE_CLUES.length-1)]+"\"");
	}


	@Override
	public boolean canPerform(Actor a) {
		return !((Player)a).isMorphed();
	}
	
	@Override
	public String getInvalidationMessage() {
		return "You can't use your items right now!";
	}
}