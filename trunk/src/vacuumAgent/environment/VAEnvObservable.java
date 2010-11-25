package vacuumAgent.environment;

import java.awt.Point;

import vacuumAgent.VAAgent;
import vacuumAgent.VAFloor;
import vacuumAgent.VANeighborhood;
import vacuumAgent.VAPercept;
import vacuumAgent.VATile.VATileStatus;

/**
 * The Class VAEnvObservable.
 *  * An observable VAenvironment.

 */
public class VAEnvObservable extends VAEnvironment {

	/**
	 * Instantiates a new vA env observable.
	 *
	 * @param vacuumAgent
	 * 				Is the unique agent on this environment.
	 * @param vacuumAgentPosition
	 * 				Position of the agent on the environment
	 * @param floor
	 * 				Indicates the floor that will be cleaned by the agent
	 */
	public VAEnvObservable(VAAgent vacuumAgent, Point vacuumAgentPosition, VAFloor floor) {
		super(vacuumAgent, vacuumAgentPosition, floor);
	}

	/* (non-Javadoc)
	 * @see vacummAgent.environment.VAEnvironment#genPerception()
	 */
	@Override
	protected VAPercept genPerception() {
		VATileStatus currentTileStatus = floor.getTile(vacuumAgentPosition)
				.getStatus();
		VANeighborhood neighborhood = this.getNeighborhood(vacuumAgentPosition);
		VAPercept percept = new VAPercept(this.floor, this.vacuumAgentPosition,
				currentTileStatus, neighborhood);
		return percept;
	}

}
