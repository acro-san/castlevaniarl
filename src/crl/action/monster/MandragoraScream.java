package crl.action.monster;

import java.util.Vector;

import sz.util.Position;
import crl.Main;
import crl.action.Action;
import crl.game.SFXManager;
import crl.monster.Monster;
import crl.monster.VMonster;
import crl.npc.Hostage;
import crl.npc.NPC;
import crl.player.Damage;

public class MandragoraScream extends Action {
	
	public final static int
		SCREAM_RANGE = 8,
		SCREAM_DAMAGE = 20;
	
	public final static String
		SCREAM_WAV = "wav/scream.wav";

	@Override
	public String getID() {
		return "MANDRAGORA_SCREAM";
	}
	
	@Override
	public void execute() {
		if (!performer.getFlag("MANDRAGORA_PULLED")) {
			performer.level.addMessage("* A skeleton pulls out a mandragora root!!! *");
			performer.setFlag("MANDRAGORA_PULLED", true);
			return;
		}
		
		SFXManager.play(SCREAM_WAV);
		performer.level.addMessage("* The Mandragora emits an earth-shattering scream!!! *");
		Main.ui.drawEffect(Main.efx.createLocatedEffect(performer.pos, "SFX_MANDRAGORA_SCREAM"));
		
		VMonster monsters = performer.level.getMonsters();
		Vector<Monster> removables = new Vector<>();
		for (int i = 0; i < monsters.size(); i++) {
			Monster monster = monsters.elementAt(i);
			if (monster == performer) {
				continue;
			}
			if (Position.flatDistance(performer.pos, monster.pos) < SCREAM_RANGE) {
				if (monster instanceof NPC || monster instanceof Hostage) {
					
				} else {
					StringBuffer messages = new StringBuffer("The "+monster.getDescription()+" hears the mandragora scream!");
					//targetMonster.damage(player.getWhipLevel());
					monster.damage(messages, SCREAM_DAMAGE);
					if (monster.wasSeen()) {
						performer.level.addMessage(messages.toString());
					}
					if (monster.isDead()) {
						if (monster.wasSeen()) {
							performer.level.addMessage("The "+monster.getDescription()+" explodes in pain!");
						}
						removables.add(monster);
					}
				}
			}
		}
		monsters.removeAll(removables);
		
		if (Position.flatDistance(performer.pos, performer.level.getPlayer().pos) < SCREAM_RANGE) {
			performer.level.getPlayer().damage("You hear the mandragora scream!", (Monster)performer, new Damage(SCREAM_DAMAGE, true));
		}
		performer.die();
	}
	
}
