package crl.data;

import crl.Main;
import crl.ai.ActionSelector;
import crl.ai.monster.BasicMonsterAI;
import crl.ai.monster.MonsterAI;
import crl.ai.monster.RangedAI;
import crl.ai.monster.RangedAttack;
import crl.ai.monster.StationaryAI;
import crl.ai.monster.UnderwaterAI;
import crl.ai.monster.WanderToPlayerAI;
import crl.ai.monster.boss.DeathAI;
import crl.ai.monster.boss.DemonDraculaAI;
import crl.ai.monster.boss.DraculaAI;
import crl.ai.monster.boss.FrankAI;
import crl.ai.monster.boss.MedusaAI;
import crl.game.CRLException;
import crl.monster.*;

import nox.NoX;
import nox.XMLTag;
import nox.Att;

import java.util.*;
import java.io.*;

import sz.crypt.*;

public class MonsterLoader {
	
	public static MonsterDefinition[] getBaseMonsters(String monsterFile) throws CRLException {
		BufferedReader br = null;
		try {
			Vector<MonsterDefinition> vecMonsters = new Vector<>(10);
			DESEncrypter encrypter = new DESEncrypter("65csvlk3489585f9rjh");
			br = new BufferedReader(
				new InputStreamReader(
					encrypter.decrypt(new FileInputStream(monsterFile))));
			String line = br.readLine();
			line = br.readLine();
			while (line != null) {
				String[] data = line.split(";");
				MonsterDefinition def = new MonsterDefinition(data[0]);
				def.appearance = Main.appearances.get(data[1]);
				def.description = data[2];
				def.longDescription = data[3];
				def.wavOnHit = data[4];
				def.bloodContent = Integer.parseInt(data[5]);
				def.isUndead = Boolean.valueOf(data[6]);//.equals("true");
				def.isEthereal = Boolean.valueOf(data[7]);//.equals("true"));
				def.canSwim = Boolean.valueOf(data[8]);//.equals("true"));
				def.canFly = Boolean.valueOf(data[9]);//.equals("true"));
				def.score = Integer.parseInt(data[10]);
				def.sightRange = Integer.parseInt(data[11]);
				def.maxHP = Integer.parseInt(data[12]);
				def.attack = Integer.parseInt(data[13]);
				def.walkCost = Integer.parseInt(data[14]);
				def.attackCost = Integer.parseInt(data[15]);
				def.evadeChance = Integer.parseInt(data[16]);
				def.evadeMessage = data[17];
				def.autorespawnCount = Integer.parseInt(data[18]);
				
				vecMonsters.add(def);
				line = br.readLine();
			}
			return (MonsterDefinition[])vecMonsters.toArray(new MonsterDefinition[vecMonsters.size()]);
		} catch (IOException ioe){
			throw new CRLException("Error while loading data from monster file");
		} finally {
			try {
				br.close();
			} catch (IOException ioe){
				throw new CRLException("Error while loading data from monster file");
			}
		}
	}
	
	public static MonsterDefinition[] getMonsterDefinitions(
			String monsterDefFile, String monsterAI_XMLFile) throws CRLException {
		try {
			// from ecsv:
			MonsterDefinition[] monsters = getBaseMonsters(monsterDefFile);
			HashMap<String, MonsterDefinition> monsterTable = new HashMap<>();
			for (int i = 0; i < monsters.length; i++) {
				monsterTable.put(monsters[i].ID, monsters[i]);
			}
			
			DESEncrypter encrypter = new DESEncrypter("65csvlk3489585f9rjh");
			InputStream decryptStream = encrypter.decrypt(new FileInputStream(monsterAI_XMLFile));
			XMLTag xml = NoX.xmlFromStream(decryptStream);
			// 'parse' (convert xml struct into game's internal data)
			readMonsterAIFromXML(xml, monsterTable);
			// That populates the 'monsters' def objects with their
			// range-attack/movement AI settings objects by reading the XML.
			
			return monsters;
			
			/* Print Some data to a CSV File, yeah I am evil
			BufferedWriter write = FileUtil.getWriter("monsterStats.csv");
			for (int i = 0; i < ret.length; i++){
				write.write(ret[i].getID()+","+ret[i].getAppearance().getID()+","+ret[i].getDescription()+",,"+
						ret[i].getWavOnHit()+","+ret[i].getBloodContent()+","+
						ret[i].isUndead()+","+ret[i].isEthereal()+","+
						ret[i].isCanSwim()+",false,"+ret[i].getScore()+","+
						ret[i].getSightRange()+","+ret[i].getMaxHits()+","+
						ret[i].getAttack()+","+ret[i].getWalkCost()+","+ret[i].getAttackCost()+","+
						ret[i].getEvadeChance()+","+ret[i].getEvadeMessage()+","+ret[i].getAutorespawnCount()
						);
				write.write("\n");
			}
			write.close();*/
			// End Print Some data to a CSV File, yeah I am evil
			
		} catch (IOException ioe) {
			throw new CRLException("Error while loading data from monster file");
		}
	}


	private static void readMonsterAIFromXML(XMLTag xmlRoot, HashMap<String, MonsterDefinition> monsterDefs) {
		// returns a list of monster defs in program's format. probably. xml.
		// flesh out/augment a MonsterDefinition object with its AI patterns.
		XMLTag monsters = xmlRoot.children[0];
		assert("monsters".equals(monsters.name));
		XMLTag[] monTags = monsters.children;
		
		for (XMLTag monTag: monTags) {
			if (monTag.tagtype == XMLTag.COMMENT) {
				continue;
			}
			
			String mid = monTag.getStrAttr("id");
			MonsterDefinition md = (MonsterDefinition)monsterDefs.get(mid);
			
			XMLTag selectorTag = monTag.children[0];
			assert("selector".equals(selectorTag.name));
			
			assert(selectorTag.children.length == 1);
			XMLTag sel = selectorTag.children[0];
			
			MonsterAI sai = null;
			switch (sel.name) {
			case "sel_wander":
				sai = new WanderToPlayerAI();
				//TODO add it to the md!
				break;
			case "sel_underwater":
				sai = new UnderwaterAI();
				break;
			case "sel_sickle":
				sai = new crl.action.monster.boss.SickleAI();
				break;
			case "sel_death":
				sai = new DeathAI();
				break;
			case "sel_dracula":
				sai = new DraculaAI();
				break;
			case "sel_demondracula":
				sai = new DemonDraculaAI();
				break;
			case "sel_medusa":
				sai = new MedusaAI();
				break;
			case "sel_frank":
				sai = new FrankAI();
				break;
			case "sel_stationary":
				sai = new StationaryAI();
				break;
			case "sel_basic":
				sai = new BasicMonsterAI();	// The most complex one!?
				
				if (sel.atts.length > 0) {
					for (Att at: sel.atts) {
					//	System.err.format("BasicMonsterAI for %s has attr: %s (%s)\n",
					//		mid, at.name, at.value);
						switch (at.name) {
						case "waitPlayerRange":
							int range = sel.getIntAttr("waitPlayerRange");
							((BasicMonsterAI)sai).setWaitPlayerRange(range);
							break;
						case "stationary":
							((BasicMonsterAI)sai).setStationary("true".equalsIgnoreCase(at.value));
							break;
						case "approachLimit":
							int lim = sel.getIntAttr("approachLimit");
							((BasicMonsterAI)sai).setApproachLimit(lim);
							break;
						case "patrolRange":
							int prange = sel.getIntAttr("patrolRange");
							((BasicMonsterAI)sai).setPatrolRange(prange);
							break;
						default:
							System.err.format("Attribute error for %s: %s\n", mid, at.name);
						}
					}
				}
				break;
			case "sel_ranged":
				sai = new RangedAI();
				int apprLimit = sel.getIntAttr("approachLimit");
				((RangedAI)sai).setApproachLimit(apprLimit);
				break;
			}
			
			if (sel.children != null && sel.children.length > 0) {
				assert(sel.children.length == 1);
				XMLTag ratks = sel.children[0];
				assert(ratks.name.equals("rangedAttacks"));
				parseRangeAttacks(sai, ratks);
			}
			
			md.defaultSelector = sai;
		}
	}
	
	
	private static void parseRangeAttacks(MonsterAI sai, XMLTag ratks) {
		Vector<RangedAttack> rangedAttacks = new Vector<>(3);
		XMLTag[] raDefs = ratks.children;
		for (XMLTag raTag: raDefs) {
			int damage = raTag.getIntAttr("damage");	// yields 0 if no attr.
			RangedAttack ra = new RangedAttack(
				raTag.getStrAttr("id"),
				raTag.getStrAttr("type"),
				raTag.getStrAttr("status_effect"),
				raTag.getIntAttr("range"),
				raTag.getIntAttr("frequency"),
				raTag.getStrAttr("message"),
				raTag.getStrAttr("effectType"),
				raTag.getStrAttr("effectID"),
				damage
				// color
			);
			String fxWav = raTag.getStrAttrOrDefault("effectWav", null);
			ra.setEffectWav(fxWav);

			String sumID = raTag.getStrAttrOrDefault("summonMonsterId", null);
			ra.setSummonMonsterId(sumID);
			
			String charge = raTag.getStrAttrOrDefault("charge", null);
			if (charge != null) {
				int chvalue = Integer.parseInt(charge);
				ra.setChargeCounter(chvalue);
			}
			rangedAttacks.add(ra);
		}
		sai.setRangedAttacks(rangedAttacks);
	}

}