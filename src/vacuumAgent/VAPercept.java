package vacuumAgent;

import java.awt.Point;

import vacuumAgent.VATile.VATileStatus;

import framework.Percept;

//TODO: Auto-generated Javadoc

/**
 * The Class VAPercept.
 */
public class VAPercept implements Percept {

	/** The floor. */
	VAFloor floor;
	
	/** The vacuum agent position. */
	Point vacuumAgentPosition;
	
	/** The current tile status. */
	VATileStatus currentTileStatus;
	
	/** The neighborhood. */
	VANeighborhood neighborhood;

	/**
	 * Instantiates a new vA percept.
	 *
	 * @param floor the floor
	 * @param vacuumAgentPosition the vacuum agent position
	 * @param currentTileStatus the current tile status
	 * @param neighborhood the neighborhood
	 */
	public VAPercept(VAFloor floor, Point vacuumAgentPosition,
			VATileStatus currentTileStatus, VANeighborhood neighborhood) {
		super();
		this.floor = floor;
		this.vacuumAgentPosition = vacuumAgentPosition;
		this.currentTileStatus = currentTileStatus;
		this.neighborhood = neighborhood;
	}

	/**
	 * Gets the floor.
	 *
	 * @return the floor
	 */
	public VAFloor getFloor() {
		return floor;
	}

	/**
	 * Sets the floor.
	 *
	 * @param floor the new floor
	 */
	public void setFloor(VAFloor floor) {
		this.floor = floor;
	}

	/**
	 * Gets the vacuum agent position.
	 *
	 * @return the vacuum agent position
	 */
	public Point getVacuumAgentPosition() {
		return vacuumAgentPosition;
	}

	/**
	 * Sets the vacuum agent position.
	 *
	 * @param vacuumAgentPosition the new vacuum agent position
	 */
	public void setVacuumAgentPosition(Point vacuumAgentPosition) {
		this.vacuumAgentPosition = vacuumAgentPosition;
	}

	/**
	 * Gets the current tile status.
	 *
	 * @return the current tile status
	 */
	public VATileStatus getCurrentTileStatus() {
		return currentTileStatus;
	}

	/**
	 * Sets the current tile status.
	 *
	 * @param currentTileStatus the new current tile status
	 */
	public void setCurrentTileStatus(VATileStatus currentTileStatus) {
		this.currentTileStatus = currentTileStatus;
	}

	/**
	 * Gets the neighborhood.
	 *
	 * @return the neighborhood
	 */
	public VANeighborhood getNeighborhood() {
		return neighborhood;
	}

	/**
	 * Sets the neighborhood.
	 *
	 * @param neighborhood the new neighborhood
	 */
	public void setNeighborhood(VANeighborhood neighborhood) {
		this.neighborhood = neighborhood;
	}

}
