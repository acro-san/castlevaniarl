package crl.levelgen.featureCarve;

import java.util.ArrayList;

import sz.util.Position;
import sz.util.Util;
import crl.action.Action;
import crl.game.CRLException;
import crl.level.Cell;
import crl.level.Level;
import crl.level.MapCellFactory;
import crl.levelgen.LevelGenerator;

public class FeatureCarveGenerator extends LevelGenerator {
	private String[][] preLevel;
	private boolean[][] mask;
	private String[][] preLevelB;
	private boolean[][] maskB;
	private ArrayList<Position> hotspots = new ArrayList<>();
	private ArrayList<Position> roomHotspots = new ArrayList<>();
	private String solidCell;
	private String corridor;
	private ArrayList<Feature> levelFeatures;
	private String backExit,nextExit;
	
	public void initialize(ArrayList<Feature> levelFeatures, String solidCell, 
		int xdim, int ydim, String corridor, String backExit, String nextExit) {
		preLevel = new String[xdim][ydim];
		mask = new boolean[xdim][ydim];
		preLevelB = new String[xdim][ydim];
		maskB = new boolean[xdim][ydim];
		this.solidCell = solidCell;
		this.corridor = corridor;
		this.levelFeatures = levelFeatures;
		this.backExit = backExit;
		this.nextExit = nextExit;
	}
	
	boolean checkCorridor = true;
	public void setCheckCorridor(boolean val){
		checkCorridor = val;
	}
	
	private void save(){
		for (int i = 0; i < mask.length; i++){
			System.arraycopy(mask[i], 0, maskB[i], 0, mask[i].length);
			System.arraycopy(preLevel[i], 0, preLevelB[i], 0, preLevel[i].length);
		}
	}
	
	private void rollBack(){
		for (int i = 0; i < mask.length; i++){
			System.arraycopy(maskB[i], 0, mask[i], 0, mask[i].length);
			System.arraycopy(preLevelB[i], 0, preLevel[i], 0, preLevel[i].length);
		}
	}
	
	public Level generateLevel() throws CRLException {
		boolean checked = false;
		boolean placed = false;
		int i = 0;
		go: while (!checked) {
			ArrayList<Feature> pendingFeatures = new ArrayList<>(levelFeatures);
			hotspots.clear();
			roomHotspots.clear();
			//Fill the level with solid element
			for (int x = 0; x < getLevelWidth(); x++){
				for (int y = 0; y < getLevelHeight(); y++){
					preLevel[x][y] = solidCell;
					mask[x][y] = false;
				}
			}
			
			//Dig out a single room or a feature in the center of the map
			Position pos = new Position(getLevelWidth() / 2, getLevelHeight() / 2);
			Feature room = null;
			int direction = 0;
			boolean finished = false;
			
			while (!placed){
 				room = Util.pick(pendingFeatures);
 				switch (Util.rand(1,4)){
					case 1:
						
						direction = Action.UP;
						break;
					case 2:
						
						direction = Action.DOWN;
						break;
					case 3:
						direction = Action.LEFT;
						break;
					case 4:
						direction = Action.RIGHT;
						break;
					}
				if (room.drawOverCanvas(preLevel, pos, direction, mask, hotspots)){
					pendingFeatures.remove(room);
					if (pendingFeatures.isEmpty()){
						finished = true;
						checked = true;
					}
					placed = true;
				} else {
					i++;
					if (i > 50000) {
						i = 0;
						//System.exit(0);
						continue go;
					}
				}
			}
			
			placed = false;
			save();
			//boolean placeRoom = true;
			boolean letsRollBack = false;
			while (!finished){
				pos = Util.pick(hotspots);
				// Try to make a branch (corridor + room)
				int corridors = Util.rand(1,3);
				int j = 0;
				//corridors = 1; //TEST
				while (j<corridors && !letsRollBack){
					CorridotFeature corridorF = new CorridotFeature(Util.rand(4,5), corridor);
					switch (Util.rand(1,4)) {
 					case 1:
 						direction = Action.UP;
 						break;
 					case 2:
 						direction = Action.DOWN;
 						break;
 					case 3:
 						direction = Action.LEFT;
 						break;
 					case 4:
 						direction = Action.RIGHT;
 						break;
 					}
					if (corridorF.drawOverCanvas(preLevel, pos, direction, mask, roomHotspots)) {
						j++;
						pos = corridorF.getTip();
					} else {
						letsRollBack = true;
					}
				}
				if (letsRollBack){
					rollBack();
					letsRollBack = false;
					continue;
				}
				
 				room = Util.pick(pendingFeatures);
 				// direction is kept from the last corridor
				if (room.drawOverCanvas(preLevel, pos, direction, mask, hotspots)){
					pendingFeatures.remove(room);
					save();
					if (pendingFeatures.isEmpty()){
						finished = true;
						checked = true;
					}
					placed = true;
				} else {
					rollBack();
				}
 				placed = false;
	 			
			}
		}
		
		Level ret = new Level();
		Cell[][][] cells = new Cell[1][][];
		cells [0] = renderLevel(preLevel);
		ret.setCells(cells);
		renderLevelFeatures(ret, preLevel);
		
		Position entrance = new Position(0,0);
		Position exit = new Position(0,0);
		while (true){
			entrance.x = Util.rand(1,getLevelWidth()-2);
			entrance.y = Util.rand(1,getLevelHeight()-2);
			if (ret.isExitPlaceable(entrance) && 
					(!checkCorridor || !preLevel[entrance.x][entrance.y].equals(corridor)
							)
							){
				ret.addExit(entrance, "_BACK");
				ret.getCells()[entrance.z][entrance.x][entrance.y] = MapCellFactory.getMapCellFactory().getMapCell(backExit);
				break;
			}
		}
		
		while (true){
			exit.x = Util.rand(1,getLevelWidth()-2);
			exit.y = Util.rand(1,getLevelHeight()-2);
			if (ret.isExitPlaceable(exit) && (!checkCorridor || !preLevel[exit.x][exit.y].equals(corridor))){
				ret.addExit(exit, "_NEXT");
				ret.getCells()[exit.z][exit.x][exit.y] = MapCellFactory.getMapCellFactory().getMapCell(nextExit);
				break;
			}
		}
		
		return ret;
	}
	
	private int getLevelWidth(){
		return preLevel.length;
	}
	
	
	private int getLevelHeight(){
		return preLevel[0].length;
	}
	
	

}
