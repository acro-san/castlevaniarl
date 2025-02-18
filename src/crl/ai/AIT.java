package crl.ai;

public enum AIT {
	VILLAGER,	// Villager AI
	HEALPLAYER,	// PriestAI
	//SickleAI? (monster.boss.SickleAI seems like an AI type?)
	// 'sickleAI' is currently returning ID of: "WANDER"
	
	NULL_SELECTOR,	// None/nothing at all. selected in DeathHall1...
	
	WILD_MORPH_AI,	// Player's wildmorph, for when they lose control i assume
	
	STATIONARY_AI,
	UNDERWATER,
	
	//wander-to-player AI:
	WANDER,	//Uhhh
	
	
	Emerge,// the ai used by 'level.EmergerAI'. ...?
	Respawn,// the "RespawnAI" id.
	
	CROSS_SELECTOR,	// CROSS_AI -- movement pattern for VKiller's Cross thrown weapon. i think?
	FLAME_SELECTOR,
	COUNTDOWN,
	CRYSTAL_SELECTOR,
	
	UI,	// the 'action selector ID for ConsoleUISelector and GFXUISelector...
	
	BASIC_MONSTER_AI,	// big/complex/common/parametric!
	
	RANGED_AI,	// all the xml ranged atks
	
	SICKLE_AI,	// SickleAI *was* returning 'WANDER'. But SickleAI != WanderToPlayerAI. VERY different classes.
	DRACULA_AI,
	DEMON_DRACULA_AI,
	DEATH_AI,
	FRANK_AI,
	MEDUSA_AI,
	
	
	
}
