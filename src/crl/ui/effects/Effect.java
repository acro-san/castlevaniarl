package crl.ui.effects;

import sz.util.*;

public class Effect {	//abstract??
	
	private String id;
	private Position position;
	protected int animationDelay = 50;
	
	public String getID() {
		return id;
	}
	
	public void set(Position loc) {
		setPosition(loc);
	}
	
	public Effect(String id) {
		this.id = id;
	}

	public Effect(String id, int delay){
		this.id = id;
		animationDelay = delay;
	}


	protected final void animationPause() {
		try {Thread.sleep(animationDelay);} catch (InterruptedException e) {}
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position value) {
		position = value;
	}

	public void setAnimationDelay(int value) {
		animationDelay = value;
	}
	
	// there's nothing abstract??
}