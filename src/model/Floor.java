package model;


import java.util.HashMap;

import com.trolltech.qt.core.QPoint;


public class Floor {

	protected HashMap<QPoint, Tile> tiles;
	protected int maxX = 0;
	protected int maxY = 0;
	protected int minX = 0;
	protected int minY = 0;
	

	public Floor()
	{
		tiles = new HashMap<QPoint, Tile>();
	}
	
	public Tile getTile(QPoint p)
	{
		return tiles.get(p);
	}
	
	public void putTile (QPoint p, Tile t)
	{
		int x = p.x();
		int y = p.y();
		
	    if(x < minX)
	        minX = x;
	    if(x > maxX)
	        maxX = x;
	    if(y < minY)
	        minY = y;
	    if(y > maxY)
	        maxY = y;
		
		tiles.put(p, t);
	}

	public HashMap<QPoint, Tile> getTiles() {
		return tiles;
	}

	public void setTiles(HashMap<QPoint, Tile> tiles) {
		this.tiles = tiles;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}

	public int getMinX() {
		return minX;
	}

	public void setMinX(int minX) {
		this.minX = minX;
	}

	public int getMinY() {
		return minY;
	}

	public void setMinY(int minY) {
		this.minY = minY;
	}
	
	
	
}
