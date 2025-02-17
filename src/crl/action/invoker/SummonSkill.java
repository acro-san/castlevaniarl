package crl.action.invoker;

import sz.util.Position;
import sz.util.Util;
import crl.action.AT;
import crl.action.HeartAction;
import crl.level.Level;
import crl.monster.Monster;
import crl.monster.MonsterData;
import crl.player.Consts;

//SummonSkill? aka 'Invoke'?
public class SummonSkill extends HeartAction {
	
	public static final byte	// enum?
		BIRD = 0,
		CAT = 1,
		DRAGON = 2,
		EAGLE = 3,
		TIGER = 4,
		TORTOISE = 5,
		TURTLE = 6;
	// actual 'IDs' are just 'what summon is it?' as an index into these data
	// arrays:
	public byte sType = BIRD;
	public SummonSkill(byte summonType) {
		sType = summonType;
	}
	
	public static final String[]
		SUMMON_MONSTER_IDS = {
			"S_BIRD", "S_CAT", "S_DRAGON", "S_EAGLE", "S_TIGER", "S_TORTOISE", "S_TURTLE"
		},
		
		SUMMON_SFX = {
			null, "wav/kitty.wav", null, null, "wav/tigerGrowl.wav",
			"wav/turtleCry.wav", "wav/turtleCry.wav"
			// ^tortoise (uses turtleCry also).
		};
	
	
	public static final AT[]
		SUMMON_IDS = {	// I HIGHLY doubt these phrases are 'IDs'. spell ids?
			AT.SummonBird, AT.SummonCat, AT.SummonDragon, AT.SummonEagle,
			AT.SummonTiger, AT.SummonTortoise, AT.SummonTurtle
			// these seem more like human-readable synopses / text-descriptions?
//			"Invoke Bird", "Invoke Cat", "Invoke Dragon", "Invoke Eagle",
//			"Invoke Tiger", "Invoke Tortoise", "Invoke Turtle"
	};
	
	public static final int[]		//   Tiger only costs 5!?
		SUMMON_HEARTCOSTS = {5, 5, 8, 5, 5, 5, 5},	// only dragon is 8.
		SUMMON_HPBONUS_SOUL_MULTIPLIER = {2, 2, 5, 3, 3, 3, 2};
	
	
	private int getHPBonus() {
		// based on the SUMMON_ID that it is...
		if (sType < 0 || sType > TURTLE) {
			System.err.println("Undefined Summon Type: "+sType);
			return 0;
		}
		int soulpower = getPlayer().getSoulPower();
		return soulpower * SUMMON_HPBONUS_SOUL_MULTIPLIER[sType];
	}
	
	@Override
	public AT getID() {
		return SUMMON_IDS[sType];
	}
	
	@Override
	public int getHeartCost() {
		return SUMMON_HEARTCOSTS[sType];
	}
	
	@Override
	public String getSFX() {
		return SUMMON_SFX[sType];
	}
	
	
	// gets the (monster)ID of the SUMMONED MONSTER.
	public final String getMonsterID() {
		return SUMMON_MONSTER_IDS[sType];	// actual MonsterIDs (enum)! see global list!
	}
	
	
	@Override
	public void execute() {
		super.execute();
		Level aLevel = performer.level;
		Position randPos = null;
		if (Util.chance(100)){ //TODO: This is related to soul
			int count = 20;
			out: while (count > 0){
				randPos = Position.add(performer.pos,
					new Position(Util.rand(-1,1), Util.rand(-1,1),0));
				if (aLevel.isWalkable(randPos)){
					break out;
				}
				count--;
			}
			if (count > 0) {
				Monster m = MonsterData.buildMonster(getMonsterID());
				aLevel.addMessage("A "+m.getDescription()+" rises from the floor!");
				m.setCounter(Consts.C_MONSTER_CHARM, 9999999);
				m.pos = randPos;
				m.hp += getHPBonus();
				aLevel.addMonster(m);
			}
		} else {
			aLevel.addMessage("Nothing happens.");
		}
	}


}
