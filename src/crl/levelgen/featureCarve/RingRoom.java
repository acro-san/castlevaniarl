package crl.levelgen.featureCarve;

import java.util.ArrayList;

import crl.action.Action;

import sz.util.Circle;
import sz.util.Position;

public class RingRoom extends Feature {
	protected int width, height;
	private String floor, wall;
	protected Position start;
	
	public RingRoom(int width, int height, String floor, String wall) {
		start = new Position(0,0);
		this.width = width;
		this.height = height;
		this.floor = floor;
		this.wall = wall;
	}
	
	@Override
	public boolean drawOverCanvas(String[][] canvas, Position where, int direction, boolean [][] mask, ArrayList<Position> hotspots){
		int rndPin = 0;
		if (width > height)
			height = width;
		else
			width = height;
		if (width % 2 == 0)
			width++;
		if (height % 2 == 0)
			height++;
		int midWidth = (int)(Math.floor(width / 2.0));
		int midHeight = (int)(Math.floor(height / 2.0));
		switch (direction){
		case Action.UP:
			rndPin = midWidth;
			start.x = where.x - rndPin;
			start.y = where.y - height + 1;
			break;
		case Action.DOWN:
			rndPin = midWidth;
			start.x = where.x - rndPin;
			start.y = where.y;
			break;
		case Action.LEFT:
			rndPin = midHeight;
			start.x = where.x - width + 1;
			start.y = where.y - rndPin;
			break;
		case Action.RIGHT:
			rndPin = midHeight;
			start.x = where.x;
			start.y = where.y - rndPin;
			break;
		}
		
		
		
		//Check the mask
		
		for (int x = start.x; x < start.x + width; x++){
			for (int y = start.y; y < start.y + height; y++){
				if (!isValid(x,y,canvas) || mask[x][y]){
					return false;
				}
			}
		}
		
		hotspots.add(new Position(start.x,start.y+midHeight));
		hotspots.add(new Position(start.x+width-1,start.y+midHeight));
		hotspots.add(new Position(start.x+midWidth,start.y));
		hotspots.add(new Position(start.x+midWidth,start.y+height-1));
		Circle circle = new Circle(new Position(start.x+midWidth, start.y+midHeight), midHeight);
		Circle inner = new Circle(new Position(start.x+midWidth, start.y+midHeight), midHeight-1);
		Circle innerie = new Circle(new Position(start.x+midWidth, start.y+midHeight), midHeight-2);
		
		
		//Carve
		ArrayList circlePoints = circle.getPoints();
		for (int i = 0; i < circlePoints.size(); i++){
			Position p = (Position)circlePoints.get(i);
			//canvas[p.x][p.y]=floor;
			mask[p.x][p.y]=true;
		}
		circlePoints = inner.getPoints();
		for (int i = 0; i < circlePoints.size(); i++){
			Position p = (Position)circlePoints.get(i);
			canvas[p.x][p.y]=floor;
			mask[p.x][p.y]=true;
		}
		circlePoints = innerie.getPoints();
		for (int i = 0; i < circlePoints.size(); i++){
			Position p = (Position)circlePoints.get(i);
			canvas[p.x][p.y]=floor;
			mask[p.x][p.y]=true;
		}
		
		mask[start.x][start.y+midHeight] = false;
		mask[start.x+width-1][start.y+midHeight] = false;
		mask[start.x+midWidth][start.y] = false;
		mask[start.x+midWidth][start.y+height-1] = false;
		return true;
	}
}
