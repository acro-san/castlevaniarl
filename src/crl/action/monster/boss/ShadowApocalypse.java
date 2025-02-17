package crl.action.monster.boss;

import sz.util.Position;
import sz.util.Util;
import crl.Main;
import crl.action.AT;
import crl.action.Action;
import crl.level.Level;
import crl.monster.Monster;
import crl.player.Damage;
import crl.player.Player;

public class ShadowApocalypse extends Action {
	
	public AT getID() {
		return AT.ShadowApocalypse;
	}
	
	public void execute() {
		Level aLevel = performer.level;
		aLevel.addMessage("A voice thunders! 'SHADOW APOCALYPSE!'");
		int sickles = Util.rand(4,8);
		for (int i=0; i<sickles; i++) {
			int xvar = Util.rand(-10,10);
			int yvar = Util.rand(-10,10);
			int xgo = performer.pos.x + xvar - 4;
			int ygo = performer.pos.y + yvar - 4;
			//UserInterface.getUI().drawEffect(new SplashEffect(new Position(xvar+performer.pos.x, yvar+performer.pos.y), "Oo*'.", Appearance.CYAN));
			Position pp = performer.pos;
			drawEffect(Main.efx.createLocatedEffect(new Position(xvar+pp.x, yvar+pp.y, pp.z), "SFX_SHADOW_APOCALYPSE"));
			for (int jx = xgo; jx <= xgo+4; jx++) {
				for (int jy = ygo; jy <= ygo+4; jy++) {
					hit(jx, jy, performer.pos.z);
				}
			}
		}
	}
	
	public int getCost() {
		return 40;
	}
	
	private void hit(int x, int y, int z) {
		Level aLevel = performer.level;
		Player aPlayer = aLevel.getPlayer();
		Position destinationPoint = new Position(x,y,z);
		if (destinationPoint.equals(aPlayer.pos)) {
			aPlayer.damage("You feel pain all over your body!", (Monster)performer, new Damage(4, false));
		}
	}
}