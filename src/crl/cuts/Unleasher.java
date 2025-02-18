package crl.cuts;

import java.io.Serializable;

import crl.game.Game;
import crl.level.Level;

public abstract class Unleasher implements Serializable {
	
	public boolean enabled = true;
	
	public abstract void unleash(Level level, Game game);
	/*This must check condition first*/
	
}
