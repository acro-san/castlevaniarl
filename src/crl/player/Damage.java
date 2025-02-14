package crl.player;

/**
 * This class represents damage taken in combat
 * 
 * @author Tuukka Turto
 */
public class Damage {

	//element type, perhaps?
	public int damage;
	public boolean ignoresArmor = false;	// reasonable default.
	
	public Damage(int damage, boolean ignoreArmor) {
		this.damage = damage;
		this.ignoresArmor = ignoreArmor;
	}

	/**
	 * Reduce damage
	 * 
	 * @param reduction amount to subtract from damage
	 */
	public void reduceDamage(int reduction) {
		assert reduction >= 0;
		
		this.damage -= reduction;
		if (this.damage < 0) {
			this.damage = 0;
		}
	}
	
	/**
	 * Boost damage
	 * @param boost amount to add to damage
	 */
	public void boostDamage(int boost) {
		assert boost >= 0;
		
		this.damage = this.damage + boost;
	}

}
