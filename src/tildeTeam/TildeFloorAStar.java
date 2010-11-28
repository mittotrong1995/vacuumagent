package tildeTeam;

import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import vacuumAgent.VAFloor;
import vacuumAgent.VATile;
import vacuumAgent.VATile.VATileStatus;

public class TildeFloorAStar extends VAFloor implements TileBasedMap{

	/** Indicator if a given tile has been visited during the search */
	private boolean[][] visited;
	
	public TildeFloorAStar(VATile[][] floor) {
		this.setFloor(floor);
		visited = new boolean[this.getSize()][this.getSize()];
	}

	@Override
	public int getWidthInTiles() {
		return this.getSize();
	}

	@Override
	public int getHeightInTiles() {
		return this.getSize();
	}

	@Override
	public void pathFinderVisited(int x, int y) {
		visited[x][y] = true;
	}

	@Override
	public boolean blocked(Mover mover, int x, int y) {
		if(this.floor[x][y].getStatus() == VATileStatus.BLOCK)
			return true;
		return false;
	}

	@Override
	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {
		return 0;
	}

}
