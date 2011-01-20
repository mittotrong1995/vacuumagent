package vacuumAgent;

// TODO: Auto-generated Javadoc
/**
 * The Class VANeighborhood.
 */
public class VANeighborhood {

	boolean north;
	
	boolean south;
	
	boolean east;
	
	boolean west;

	/**
	 * Instantiates a new vA neighborhood.
	 */
	public VANeighborhood() {
		super();
		this.north = true;
		this.south = true;
		this.east = true;
		this.west = true;

	}

	/**
	 * Instantiates a new vA neighborhood.
	 *
	 * @param north the north
	 * @param south the south
	 * @param east the east
	 * @param west the west
	 */
	public VANeighborhood(boolean north, boolean south, boolean east,
			boolean west) {
		super();
		this.north = north;
		this.south = south;
		this.east = east;
		this.west = west;
	}

	/**
	 * North is free.
	 *
	 * @return true, if successful
	 */
	public boolean northIsFree() {
		return north;
	}

	/**
	 * Sets the north.
	 *
	 * @param north the new north
	 */
	public void setNorth(boolean north) {
		this.north = north;
	}

	/**
	 * South is free.
	 *
	 * @return true, if successful
	 */
	public boolean southIsFree() {
		return south;
	}

	/**
	 * Sets the south.
	 *
	 * @param south the new south
	 */
	public void setSouth(boolean south) {
		this.south = south;
	}

	/**
	 * East is free.
	 *
	 * @return true, if successful
	 */
	public boolean eastIsFree() {
		return east;
	}

	/**
	 * Sets the east.
	 *
	 * @param east the new east
	 */
	public void setEast(boolean east) {
		this.east = east;
	}

	/**
	 * West is free.
	 *
	 * @return true, if successful
	 */
	public boolean westIsFree() {
		return west;
	}

	/**
	 * Sets the west.
	 *
	 * @param west the new west
	 */
	public void setWest(boolean west) {
		this.west = west;
	}

}
