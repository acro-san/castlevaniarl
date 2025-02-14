package crl.actor;

import java.util.Enumeration;
import java.util.Hashtable;

import sz.util.Debug;
import sz.util.Position;
import sz.util.PriorityEnqueable;
import crl.action.Action;
import crl.ai.ActionSelector;
import crl.game.SFXManager;
import crl.level.Level;
import crl.ui.Appearance;

public class Actor implements Cloneable, java.io.Serializable, PriorityEnqueable {
	
	protected /*transient*/ int positionx, positiony, positionz;
	protected transient Appearance appearance;
	public ActionSelector selector;
	private /*transient*/ Position position = new Position(0,0,0);
	private int hoverHeight;
	private /*transient*/ int nextTime = 10;
	
	public Level level;	// not combat level. a ref to the MAP this is in.
	
	private boolean isJumping;
	private int startingJumpingHeight;



	public int getCost(){
		//Debug.say("Cost of "+getDescription()+" "+ nextTime);
		return nextTime;
	}
	
	public void reduceCost(int value){
		//Debug.say("Reducing cost of "+getDescription()+"by"+value+" (from "+nextTime+")");
		nextTime -= value;
	}
	
	public void setNextTime(int value) {
		//Debug.say("Next time for "+getDescription()+" "+ value);
		nextTime = value;
	}


	public void updateStatus() {
		Enumeration<String> countersKeys = hashCounters.keys();
		while (countersKeys.hasMoreElements()){
			String key = countersKeys.nextElement();
			Integer counter = (Integer)hashCounters.get(key);
			if (counter.intValue() == 0){
				hashCounters.remove(key);
			} else {
				hashCounters.put(key, Integer.valueOf(counter.intValue()-1));
			}
		}
	}

	public String getDescription() {
		return "";
	}

	public void execute(Action x) {
		if (x != null){
			x.setPerformer(this);
			if (x.canPerform(this)){
				if (x.getSFX() != null)
					SFXManager.play(x.getSFX());
				x.execute();
				//Debug.say("("+x.getCost()+")");
				setNextTime(x.getCost());
			}
		} else {
			setNextTime(50);
		}
		updateStatus();
	}
	
	public void act() {
		Action x = selector.selectAction(this);
		execute(x);
	}

	public void setPosition(int x, int y, int z){
		position.x = x;
		position.y = y;
		position.z = z;
	}

	public void die() {
		/** Request to be removed from any dispatcher or structure */
		aWannaDie = true;
	}

	public boolean wannaDie() {
		return aWannaDie;
	}

	private boolean aWannaDie;


	public void setPosition(Position p) {
		position = p;
	}

	public Position getPosition() {
		return position;
	}

	//Player has a complex override of getAppearance for form-changes etc!
	public Appearance getAppearance() { return appearance; }
	public void setAppearance(Appearance value) { appearance = value; }

	public Object clone(){
		try {
			Actor x = (Actor) super.clone();
			if (position != null)
				x.setPosition(new Position(position.x, position.y, position.z));
			return x;
		} catch (CloneNotSupportedException cnse){
			Debug.doAssert(false, "failed class cast, Feature.clone()");
		}
		return null;
	}


	public void message(String mess){
	}
	
	protected Hashtable<String,Integer> hashCounters = new Hashtable<>();
	public void setCounter(String counterID, int turns) {
		hashCounters.put(counterID, Integer.valueOf(turns));
	}
	
	public int getCounter(String counterID) {
		Integer val = hashCounters.get(counterID);
		if (val == null) {
			return -1;
		} else {
			return val.intValue();
		}
	}
	
	public boolean hasCounter(String counterID) {
		return getCounter(counterID) > 0;
	}

	
	private Hashtable<String, Boolean> hashFlags = new Hashtable<>();
	public void setFlag(String flagID, boolean value){
		hashFlags.put(flagID, Boolean.valueOf(value));
	}
	
	public boolean getFlag(String flagID) {
		Boolean val = hashFlags.get(flagID);
		return val != null && val.booleanValue();
	}

	public int getHoverHeight() {
		return hoverHeight;
	}

	public void setHoverHeight(int hoverHeight) {
		if (hoverHeight > 0) {
			this.hoverHeight = hoverHeight;
		} else {
			this.hoverHeight = 0;
		}
	}
	
	public int getStandingHeight() {
		if (isJumping) {
			return startingJumpingHeight+2;
		}
		if (level.getMapCell(getPosition()) != null) {
			return level.getMapCell(getPosition()).getHeight()+getHoverHeight();
		} else {
			return getHoverHeight();
		}
	}
	

	public boolean isJumping() {
		return isJumping;
	}

	public void doJump(int startingJumpingHeight) {
		this.isJumping = true;
		this.startingJumpingHeight = startingJumpingHeight;
	}
	
	public void stopJump(){
		this.isJumping = false;
	}

}