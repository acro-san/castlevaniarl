package crl.ai.monster;

import java.util.Vector;

import crl.ai.ActionSelector;
//import sz.util.Debug;

public abstract class MonsterAI extends ActionSelector implements Cloneable {
	
	protected Vector<RangedAttack> rangedAttacks;	// public? non-null?
	// "attacks". ALL are declared as such? not just ranged?
	
	public void setRangedAttacks(Vector<RangedAttack> pRangedAttacks) {
		rangedAttacks = pRangedAttacks;
	}


}
