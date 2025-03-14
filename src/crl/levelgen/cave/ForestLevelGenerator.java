package crl.levelgen.cave;

import sz.ca.CARandomInitializer;
import sz.ca.CARule;
import sz.ca.Matrix;
import sz.ca.SZCA;
import sz.util.Position;
import sz.util.Util;
import crl.feature.Feature;
import crl.feature.FeatureFactory;
import crl.game.CRLException;
import crl.level.Cell;
import crl.level.Level;
import crl.level.MapCellFactory;
import crl.levelgen.LevelGenerator;

public class ForestLevelGenerator  extends LevelGenerator{
	private String baseWall, baseFloor, baseLava;
	
	public void init(String baseWall, String baseFloor, String baseLava){
		this.baseWall = baseWall;
		this.baseFloor = baseFloor;
		this.baseLava = baseLava;
	}
	
	public Level generateLevel(int xdim, int ydim, boolean locked) throws CRLException{
		/** Uses Cave Cellular automata, by SantiagoZ
		 * Init (1) 30%
		 * If 0 and more than 3 1 around, 1
		 * If 1 and less than 2 1 around, 0
		 * If 0 and more than 7 0 around, 2
		 * If 2 and more than 2 0 around, 0
		 * 
		 * Run for 5 turns
		 */
		
		CARandomInitializer vInit = new CARandomInitializer(new double [] {0.3}, true);
		CARule [] vRules = new CARule []{
			new CARule(0, CARule.MORE_THAN, 3, 1, 1),
			new CARule(1, CARule.LESS_THAN, 2, 1, 0),
			new CARule(0, CARule.MORE_THAN, 7, 0, 2),
			new CARule(2, CARule.MORE_THAN, 2, 0, 0),
		};

		int[][] intMap = null;
		int yTown = 0;
		int yExit = 0;
		int xEntrance = 0; 
		
		Matrix map = new Matrix(xdim,ydim);
		vInit.init(map);
		SZCA.runCA(map, vRules, 5, false);
		intMap = map.getArrays();
		//Carve the exit to town
		yTown = Util.rand(5, intMap[0].length-5);
		intMap[0][yTown] = 0;
		for (int i = 1; i < 9; i++){
			intMap[i][yTown-1] = 0;
			intMap[i][yTown] = 0;
			intMap[i+1][yTown] = 0;
			intMap[i][yTown+1] = 0;
		}
		//Carve the exit to wreckage
		xEntrance = Util.rand(5, intMap.length-5);
		intMap[xEntrance][intMap[0].length-1] = 0;
		for (int i = 1; i < 4; i++){
			intMap[xEntrance-1][intMap[0].length-1-i] = 0;
			intMap[xEntrance][intMap[0].length-1-i] = 0;
			intMap[xEntrance+1][intMap[0].length-1-i] = 0;
		}

		//Carve the exit
		yExit = Util.rand(5, intMap[0].length-5);
		intMap[intMap.length-1][yExit] = 0;
		for (int i = 1; i < 9; i++){
			intMap[intMap.length-1-i][yExit-1] = 0;
			intMap[intMap.length-1-i][yExit] = 0;
			intMap[intMap.length-2-i][yExit] = 0;
			intMap[intMap.length-1-i][yExit+1] = 0;
		}
		
		
		Position start = new Position(0,yTown);
		Position end = new Position(intMap.length-1,yExit);
		Position charriot = new Position(xEntrance, intMap[0].length-1);
		
		//Run the wisps
		WispSim.setWisps(new Wisp(start, 10,40,5),new Wisp(end, 10,40,5));
		WispSim.run(intMap);
		WispSim.setWisps(new Wisp(charriot, 40,30,3),new Wisp(end, 20,30,3));
		WispSim.run(intMap);
		Position key1 = null;
		Position key2 = null;
		if (locked){
			//Put the keys
			while (key1 == null){
				int xpos = Util.rand(0,intMap.length-1);
				int ypos = Util.rand(0,intMap[0].length-1);
				if (intMap[xpos][ypos] == 0)
					key1 = new Position(xpos, ypos);
			}
			while (key2 == null){
				int xpos = Util.rand(0,intMap.length-1);
				int ypos = Util.rand(0,intMap[0].length-1);
				if (intMap[xpos][ypos] == 0)
					key2 = new Position(xpos, ypos);
			}
			
			//Run the wisps for the keys
			WispSim.setWisps(new Wisp(key1, 40,30,3),new Wisp(key2, 20,30,3));
			WispSim.run(intMap);
		}
		
		String[][] tiles = new String[intMap.length][intMap[0].length];
		Level ret = new Level();
		for (int x = 0; x < intMap.length; x++)
			for (int y = 0; y < intMap[0].length; y++)
				if (intMap[x][y] == 0)
					tiles[x][y] = baseFloor;
				else if (intMap[x][y] == 1)
					tiles[x][y] = baseWall;
				else if (intMap[x][y] == 2)
					tiles[x][y] = baseLava;
				else if (intMap[x][y] == 4){
					tiles[x][y] = baseFloor;
					//ret.addBlood(new Position(x,y,0), 8);
				}

		Cell[][] cells = renderLevel(tiles);
		
		Cell[][][] levelCells = new Cell[1][][];
		levelCells[0] = cells;
		ret.setCells(levelCells);
		if (locked){
			//Place the keys
			Feature keyf = FeatureFactory.buildFeature("KEY");
			keyf.setPosition(key1.x, key1.y, key1.z);
			ret.addFeature(keyf);
			keyf = FeatureFactory.buildFeature("KEY");
			keyf.setPosition(key2.x, key2.y, key2.z);
			ret.addFeature(keyf);
			
			//Place the door
			Feature door = FeatureFactory.buildFeature("MAGIC_DOOR");
			door.setPosition(ret.getWidth()-1,yExit,0);
			door.setKeyCost(2);
			ret.addFeature(door);
		}
		
		//Place candles
		lightCandles(ret);
		
		//Place trees
		int trees = (ret.getHeight() * ret.getWidth())/10;
		for (int i = 0; i < trees; i++){
			int xrnd = Util.rand(1, ret.getWidth() -1);
			int yrnd = Util.rand(1, ret.getHeight() -1);
			if (ret.getMapCell(xrnd, yrnd, 0).isSolid()){
				i--;
				continue;
			}
			if (Util.chance(50))
				ret.getCells()[0][xrnd][yrnd] = MapCellFactory.getMapCellFactory().getMapCell("FOREST_TREE_1");
			else
				ret.getCells()[0][xrnd][yrnd] = MapCellFactory.getMapCellFactory().getMapCell("FOREST_TREE_2");
		}
		
		ret.addExit(start, "_BACK");
		ret.addExit(end, "_NEXT");
		ret.addExit(charriot, "CHARRIOT_W");
		return ret;
	}
	
	private void lightCandles(Level l){
		int candles = (l.getHeight() * l.getWidth())/400;
		for (int i = 0; i < candles; i++){
			int xrnd = Util.rand(1, l.getWidth() -1);
			int yrnd = Util.rand(1, l.getHeight() -1);
			if (l.getMapCell(xrnd, yrnd, 0).isSolid()){
				i--;
				continue;
			}
				
			Feature vFeature = FeatureFactory.buildFeature("CANDLE");
			vFeature.setPosition(xrnd,yrnd,0);
			l.addFeature(vFeature);
		}
	}
}

