package vacuumAgent.environment;

import java.awt.Point;

import vacuumAgent.VAAgent;
import vacuumAgent.VAFloor;
import vacuumAgent.VANeighborhood;
import vacuumAgent.VAPercept;
import vacuumAgent.VATile.VATileStatus;

/**
 * The Class VAEnvNonObservable.
 * A non observable VAenvironment.
 */
public class VAEnvNonObservable extends VAEnvironment {

	/**
	 * 
	 * Instantiates a new vA env non observable.
	 * 
	 * @param vacuumAgent
	 * 				Is the unique agent on this environment.
	 * @param vacuumAgentPosition
	 * 				Position of the agent on the environment
	 * @param floor
	 * 				Indicates the floor that will be cleaned by the agent
	 */
	public VAEnvNonObservable(VAAgent vacuumAgent, Point vacuumAgentPosition, VAFloor floor) {
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
		VAFloor undefFloor = new VAFloor(floor.getSize());
		VAPercept percept = new VAPercept(undefFloor, null, currentTileStatus,
				neighborhood);
		return percept;
	}

}
