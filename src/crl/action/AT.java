package crl.action;

// ActionType. numerical ids for every Action.
public enum AT {
	
	Attack,
	
	// VKILLER Actions:
	AirDash,
	WarpDash,
	// these are also enumerated as Player.AXE, etc!
	MW_Axe,	//MysticWeaponAxe? (aka SubWpn)? ST? SubweaponThrow?
	MW_Bible,
	MW_BlastCrystal,
	MW_Cross,
	MW_Dagger,
	MW_HolyWater,
	MW_SacredFist,
	MW_Stopwatch,
	//these all also have an 'ItemBreak'..action?

	ItemBreak,	//heartaction..? What?
	ItemBreak_Axe,
	ItemBreak_Bible,
	ItemBreak_BlastCrystal,
	ItemBreak_Cross,
	ItemBreak_Dagger,
	ItemBreak_HolyWater,
	ItemBreak_SacredFist,
	ItemBreak_Stopwatch,
	
	Rebound,
	SlideKick,
	SoulBlast,
	SoulFlame,
	SoulIce,
	SoulSaint,
	SoulWind,
	
	// RENEGADE Actions:
	BallOfDestruction,
	BatMorph,
	BatMorph2,	// turn into a White Bat...?! (how's this accessed? Action is otherwise IDENTICAL)
	BloodThirst,
	Fireball,
	FlamesShoot,
	HellFire,
	MinorJinx,	// was 'MINOR_JINX' styled.
	
	MystMorph,	// was 'BatMorph' - BUG!?
	MystMorph2,	// WAS correctly this. 'you turn into Thick Myst' !???! (Hah, ok!?)
	ShadeTeleport,
	SoulsStrike,
	SoulSteal,
	SummonSpirit,
	Teleport_RengBlink,	// RenegadeTP!? aimprompt for this says 'Where do you want to blink?'
	// also txt descrip says 'you wrap in your cape and disappear'
	// so it's renegadeBlink. Oldtxt was just 'Teleport'.
	// Wtf is this id... FIXME CHECK IT.
	WolfMorph,	// you turn into a wolf
	WolfMorph2,		// you turn into a grey wolf
	
	// VANQUISHER Actions:
	ChargeBall,
	Cure,	// "CURE" was the str.
	Enchant,	// "ENCHANT"
	EnergyShield,
	FlameSpell,
	HomingBall,
	IceSpell,
	Light,	// LightSpell
	LitSpell,	//lightning?
	LitStorm,
	MajorJinx,	// txt was MAJOR_JINX. CHECK for this.
	MindBlast,	// txt was 'Mindblast' (lowercase b)
	MindLock,	//oldtxt: "Mindlock"
	MinorJinxVanq,	//"MINOR_JINX"
	// wait. *what*?
	Recover,
	SleepSpell,	// TODO Investigate if any bugs due to old txt being 'SpellSpell'
	Teleport_Vanq,	// old txt was 'Teleport2'
	
	// INVOKER actions:
	//Action>HeartAction>ProjectileSkill>Bird
	Bird,
	//Action>HeartAction>ProjectileSkill>Cat
	Cat,
	//Action>HeartAction>Charm
	Charm,
	//Action>HeartAction>Dragon
	Dragon,
	//Action>HeartAction>ProjectileSkill>BeamProjectileSkill
	DrakeSoul,
	//Action>HeartAction>Egg
	Egg,
	//Action>HeartAction>Tame
	Tame,
	//Action>HeartAction>Turtle
	Turtle,
	SummonBird,
	SummonCat,
	SummonDragon,//Ch
	SummonEagle,
	SummonTiger,//Ch
	SummonTortoise,
	SummonTurtle,
	// what are the other Chinese signs?
	// Rat, Ox, Tiger, Rabbit,
	// Dragon, Snake, Horse,
	// Goat, Monkey, Rooster
	// Dog, Pig.
	// OH. So most ARENT.
	
	// Beastman Actions:
	//Action>HeartAction>MorphAction>..
	BearMorph,
	BeastMorph,
	//Action>HeartAction>ProjectileSkill
	ClawAssault,
	//Action>HeartAction
	ClawSwipe,
	//Action>HeartAction>MorphAction>..
	DemonMorph,
	EnergyScythe,
	LupineMorph,	// (Alucard style wolf transform)?
	PowerBlow,
	PowerBlow2,
	PowerBlow3,
	WerewolfMorph,
	
	//KNIGHT Action ..
	//Action>HeartAction>Defend
	Defend,
	//NB: As String was 'DEFEND'.
	
	// Weapon Actions:
	DivingSlide,
	EnergyBeam,
	EnergyBurst,	// oldtxt: ENERGY_BURST
	FinalSlash,		// oldtxt: FINAL_SLASH
	SpinningSlice,
	TigerClaw,		// oldtxt: TIGER_CLAW
	WhirlwindWhip,
	
	// Monster Actions: (See data XML)
	SliceDive,	//"Dash" class!? "SLICE_DIVE"
	//^GiantBat & Igor get this in monsters.xml
	// most common in monsters.xml:
	MonsterMissile,// MONSTER_MISSILE
	MonsterCharge,// MONSTER_CHARGE
	SummonMonster,	// oldtxt:SUMMON_MONSTER
	MandragoraScream,// MANDRAGORA_SCREAM
	MonsterWalk,

	Swim,	// is monsterswim?
	// BossMonster Actions:
	Materialize,	// Drac. MATERIALIZE
	MummyStrangle,	// MUMMY_STRANGLE
	MummyTeleport,	// MUMMY_TELEPORT
	ShadowApocalypse,// SHADOW_APOCALYPSE
	ShadowExtinction,// SHADOW_EXTINCTION
	ShadowFlare,	// SHADOWFLARE
	//SickleAI?
	SummonSnakes,	// SUMMON_SNAKES
	BossTeleport,	// oldtxt: "TELEPORT" (allcaps) (Drac)
	UnleashSickles,	// UNLEASH_SICKLES (Death)
	Vanish,	// Dracula disappears. (VANISH)
	
	
	// NPC Actions
	PeaceWalk,	// move without bump attack!
	
	// Prelude Intro 'cutscene' Drac Action:
	PreludeKillChris,//oldtxt:PRELUDE_KILL_CHRIS
	
	// General/Player Actions?
	Dive,	// oldtxt: DIVE
	Drop,
	Equip,
	FusionSpirits,	//"Fusion Spirits"
	Get,	// pickupitem? idk.
	Jump,
	Reload,
	SwitchWeapons,
	Target,	// TargetProjectileSkill.
	Throw,		
	Unequip,
	Use,
	Walk,
	WhipFireball,	// was NULL ID!!!
	
	// 'Feature' Actions
	Blast,
	CrossBack,	// *shit*. It's MW_Cross, but the 'ping back' version. Not unique-id...
	Shine,
	
	// 'Level' (map) Action:
	EmergeMonster,
	Preemerge,
	SpawnMonster,
	SFX,		// a (disued?) thunder action?
	
	
	
}
