package crl.levelgen;

import java.util.*;

import crl.level.*;
import crl.monster.*;
import crl.game.*;
import crl.Main;
import crl.feature.*;
import crl.item.*;
import crl.npc.*;

import sz.util.*;

public class StaticGenerator {
	private static StaticGenerator singleton = new StaticGenerator();
	private Hashtable<String,String> charMap;
	private Hashtable<String,String> inhabitantsMap;	//id, space-separated-list-of-chars.
	private String[][] level;
	private String[][] inhabitants;
	//private Position startPosition, endPosition;

	public void reset(){
		charMap = null;
		level = null;
		inhabitantsMap = null;
		inhabitants = null;
	}
	
	public static StaticGenerator getGenerator(){
		return singleton;
	}

	public void renderOverLevel(Level l, String[] map, Hashtable<String,String> table, Position where) throws CRLException {
		Cell [][][] cmap = l.getCells();
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[0].length(); x++) {
				if (map[y].charAt(x) == ' ') {
					cmap[where.z][where.x+x][where.y+y] = MapCellFactory.getMapCellFactory().getMapCell("AIR");
					continue;
				}
				String iconic = table.get(map[y].charAt(x)+"");
				if (iconic == null) {
					Game.crash("renderOverLevel: "+map[y].charAt(x)+" not found on the level charMap", new Exception());
				}
				String[] cmds = iconic.split(" ");
				if (!cmds[0].equals("NOTHING")) {
					cmap[where.z][where.x+x][where.y+y] = MapCellFactory.getMapCellFactory().getMapCell(cmds[0]);
				}
				if (cmds.length < 2) {
					continue;
				}
				//(cmds.length > 1)
				if (cmds[1].equals("FEATURE")) {
					if (cmds.length < 4 || Util.chance(Integer.parseInt(cmds[3]))) {
						Feature vFeature = FeatureFactory.buildFeature(cmds[2]);
						vFeature.setPosition(where.x+x,where.y+y,where.z);
						if (cmds.length > 4) {
							if (cmds[4].equals("COST")) {
								vFeature.setKeyCost(Integer.parseInt(cmds[5]));
							}
						}
						l.addFeature(vFeature);
					}
				} else if (cmds[1].equals("ITEM")) {
					Item vItem = Main.itemData.createItem(cmds[2]);
					if (vItem != null) {
						l.addItem(new Position(where.x+x,where.y+y,where.z), vItem);
					}
				} else if (cmds[1].equals("MONSTER")) {
					Monster toAdd = MonsterData.buildMonster(cmds[2]);
					toAdd.setPosition(where.x+x,where.y+y,where.z);
					l.addMonster(toAdd);
				} else if (cmds[1].equals("EXIT")) {
					l.addExit(new Position(where.x+x,where.y+y,where.z), cmds[2]);
				} else if (cmds[1].equals("EXIT_FEATURE")) {
					l.addExit(new Position(where.x+x,where.y+y,where.z), cmds[2]);
					Feature vFeature = FeatureFactory.buildFeature(cmds[3]);
					vFeature.setPosition(where.x+x,where.y+y,where.z);
					if (cmds.length > 4){
						if (cmds[5].equals("COST")) {
							vFeature.setKeyCost(Integer.parseInt(cmds[6]));
						}
					}
					l.addFeature(vFeature);
				} else if (cmds[1].equals("EOL")) {
					l.addExit(new Position(where.x+x,where.y+y,where.z), "_NEXT");
					Feature endFeature = FeatureFactory.buildFeature(cmds[2]);
					endFeature.setPosition(where.x+x,where.y+y,where.z);
					if (cmds.length > 3){
						//Debug.say("Hi... i will set the cost");
						if (cmds[3].equals("COST")) {
							//Debug.say("Hi... i did it to "+vFeature);
							endFeature.setKeyCost(Integer.parseInt(cmds[4]));
						}
					}
					l.addFeature(endFeature);
				} else if (cmds[1].equals("NPC")) {
					NPC toAdd = NPCData.buildNPC(cmds[2]);
					toAdd.setPosition(where.x+x,where.y+y,where.z);
					toAdd.level = l;
					l.addActor(toAdd);
				}
			}
		}
	}
	
	
	public void renderOverLevel(Level l, String[][] map, 
		Hashtable<String,String> table, Position where) throws CRLException {
		Position runner = new Position(where);
		runner.z = 0;
		for (int i = 0; i < map.length; i++) {
			renderOverLevel(l, map[i], table, runner);
			runner.z++;
		}
	}
	
	
	public Level createLevel() throws CRLException {
		Debug.enterMethod(this, "createLevel");
		Level ret = new Level();
		ret.setDispatcher(new Dispatcher());
		Cell [][][] cmap = new Cell[level.length][level[0][0].length()][level[0].length];
		ret.setCells(cmap);
		for (int z=0; z < level.length; z++) {
			for (int y = 0; y < level[0].length; y++) {
				for (int x = 0; x < level[0][0].length(); x++) {
					if (level[z][y].charAt(x) == ' '){
						cmap[z][x][y] = MapCellFactory.getMapCellFactory().getMapCell("AIR");
						continue;
					}
					String iconic = (String)charMap.get(level[z][y].charAt(x)+"");
					if (iconic == null) {
						Game.crash("mapchar "+level[z][y].charAt(x)+" not found on the level charMap", new Exception());
					}
					String[] cmds = iconic.split(" ");
					
					if (!cmds[0].equals("NOTHING")) {
						cmap[z][x][y] = MapCellFactory.getMapCellFactory().getMapCell(cmds[0]);
					}
					
					if (cmds.length > 1) {
						if (cmds[1].equals("FEATURE")){
							if (cmds.length < 4 || Util.chance(Integer.parseInt(cmds[3]))){
								Feature vFeature = FeatureFactory.buildFeature(cmds[2]);
								vFeature.setPosition(x,y,z);
								if (cmds.length > 4){
									if (cmds[4].equals("COST")) {
										vFeature.setKeyCost(Integer.parseInt(cmds[5]));
									}
								}
								ret.addFeature(vFeature, false);
							}
						}else if (cmds[1].equals("ITEM")){
							Item vItem = Main.itemData.createItem(cmds[2]);
							ret.addItem(new Position(x,y,z), vItem);
						}else if (cmds[1].equals("WEAPON")){
							Item vItem = Main.itemData.createWeapon(cmds[2],cmds[3]);
							ret.addItem(new Position(x,y,z), vItem);
						}else if (cmds[1].equals("MONSTER")){
							Monster toAdd = MonsterData.buildMonster(cmds[2]);
							toAdd.setPosition(x,y,z);
							ret.addMonster(toAdd);
						}else if (cmds[1].equals("NPC")){
							NPC toAdd = NPCData.buildNPC(cmds[2]);
							toAdd.setPosition(x,y,z);
							toAdd.level = ret;
							ret.addActor(toAdd);
						} else if (cmds[1].equals("MERCHANT")){
							NPC toAdd = NPCData.buildMerchant(Integer.parseInt(cmds[2]));
							toAdd.level = ret;
							toAdd.setPosition(x,y,z);
							ret.addActor(toAdd);
						}else if (cmds[1].equals("EXIT")){
							ret.addExit(new Position(x,y,z), cmds[2]);
						} else if (cmds[1].equals("EXIT_FEATURE")) {
							ret.addExit(new Position(x,y,z), cmds[2]);
							Feature vFeature = FeatureFactory.buildFeature(cmds[3]);
							vFeature.setPosition(x,y,z);
							if (cmds.length > 4){
								if (cmds[5].equals("COST")) {
									vFeature.setKeyCost(Integer.parseInt(cmds[6]));
								}
							}
							ret.addFeature(vFeature);
						} else if (cmds[1].equals("EOL")){
							ret.addExit(new Position(x,y,z), "_NEXT");
							Feature endFeature = FeatureFactory.buildFeature(cmds[2]);
							endFeature.setPosition(x,y,z);
							if (cmds.length > 3){
								//Debug.say("Hi... i will set the cost");
								if (cmds[3].equals("COST")) {
									//Debug.say("Hi... i did it to "+vFeature);
									endFeature.setKeyCost(Integer.parseInt(cmds[4]));
								}
							}
							ret.addFeature(endFeature);
						}
					}
				}
			}
		}
		
		if (inhabitantsMap != null && inhabitants != null){
			for (int z=0; z < level.length; z++)
				for (int y = 0; y < level[0].length; y++)
					for (int x = 0; x < level[0][0].length(); x++) {
						if (level[z][y].charAt(x) == ' ')
							continue;
						if (inhabitantsMap.get(inhabitants[z][y].charAt(x)+"") == null)
							continue;
						String[] cmds = (inhabitantsMap.get(inhabitants[z][y].charAt(x)+"")).split(" ");
						if (cmds[0].equals("MONSTER")) {
							Monster toAdd = MonsterData.buildMonster(cmds[1]);
							toAdd.setPosition(x,y,z);
							ret.addMonster(toAdd);
						}else if (cmds[0].equals("NPC")) {
							NPC toAdd = NPCData.buildNPC(cmds[1]);
							toAdd.setPosition(x,y,z);
							toAdd.level = ret;
							ret.addActor(toAdd);
						} else if (cmds[0].equals("MERCHANT")) {
							NPC toAdd = NPCData.buildMerchant(Integer.parseInt(cmds[1]));
							toAdd.setPosition(x,y,z);
							ret.addActor(toAdd);
						}
					}
		}
		
		//ret.setCells(cmap);
		//ret.setPositions(startPosition, endPosition);
/*
		if (!hasBoss){
			int keysOnLevel = placeKeys(ret);
			if (endFeature != null)
				endFeature.setKeyCost(keysOnLevel);
		} else
			if (endFeature != null)
				endFeature.setKeyCost(1);*/
		Debug.exitMethod(ret);
		return ret;
	}

	public void setCharMap(Hashtable<String,String> value) {
		charMap = value;
	}
	
	public void setInhabitantsMap(Hashtable<String,String> value) {
		inhabitantsMap = value;
	}

	public void setLevel(String[][] value) {
		level = value;
	}
	
	public void setInhabitants(String[][] value) {
		inhabitants = value;
	}

	public void setFlatLevel(String[] value){
		level = new String [1][];
		level[0] = value;
	}

	protected int placeKeys(Level ret){
		Debug.enterMethod(this, "placeKeys");
		//Place the magic Keys
		int keys = Util.rand(1,4);
		Position tempPosition = new Position(0,0);
		for (int i = 0; i < keys; i++){
			int keyx = Util.rand(1,ret.getWidth()-1);
			int keyy = Util.rand(1,ret.getHeight()-1);
			int keyz = Util.rand(0, ret.getDepth()-1);
			tempPosition.x = keyx;
			tempPosition.y = keyy;
			tempPosition.z = keyz;
			if (ret.isWalkable(tempPosition) && !ret.getMapCell(tempPosition).isWater()){
				Feature keyf = FeatureFactory.buildFeature("KEY");
				keyf.setPosition(tempPosition.x, tempPosition.y, tempPosition.z);
				ret.addFeature(keyf);
			} else {
				i--;
			}
		}
		Debug.exitMethod(keys);
		return keys;
		
	}

}