package crl.monster;

import crl.ui.*;
import crl.ai.*;

public class MonsterDefinition {
	
	public String
		ID,
		description,
		longDescription,
		
		evadeMessage,
		wavOnHit;	// would this be clearer if named onHitWav (soundeffectpath?)
	
	public /*transient*/ Appearance appearance;	// why marked transient? Ah! no serialize needed on this ref.
	// or look it up by ID always? then there IS NO REF, and int ids are smaller than object refs.
	
	public ActionSelector defaultSelector;	// AI?
	
	public int
		score,
		maxHits,
		attack = 1,
		sightRange = 10,
		
		leaping, //Capability of moving to a higher cell
	
		bloodContent,
		minLevel,
		maxLevel,
		attackCost = 50,
		walkCost = 50,
		evadeChance,
		
		autorespawnCount;

	public boolean
		canFly,
		canSwim,
		isEthereal,	// Walks thru solid
		isUndead;

	public MonsterDefinition(String pID) {
		ID = pID;
	}

	public boolean isBleedable() {
		return bloodContent > 0;
	}

}