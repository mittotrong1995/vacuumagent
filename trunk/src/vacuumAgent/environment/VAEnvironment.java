package vacuumAgent.environment;

import java.awt.Point;

import vacuumAgent.VAAction;
import vacuumAgent.VAAgent;
import vacuumAgent.VAFloor;
import vacuumAgent.VANeighborhood;
import vacuumAgent.VAPercept;
import vacuumAgent.VAAction.VAActionType;
import vacuumAgent.VATile.VATileStatus;

import exception.VAIllegalMove;
import framework.Agent;
import framework.Environment;

/**
 * The Class VAEnvironment.
 * This Environment is properly developed for a Vacuum Agent.
 */
public abstract class VAEnvironment implements Environment {

	protected VAAgent vacuumAgent;
	protected Point vacuumAgentPosition;
	protected VAFloor floor;
	protected int stepCount;
	protected Point startPosition;

	
	/**
	 * Instantiates a new VAenvironmet.
	 * 
	 * @param vacuumAgent
	 * 				Is the unique agent on this environment.
	 * @param vacuumAgentPosition
	 * 				Position of the agent on the environment
	 * @param floor
	 * 				Indicates the floor that will be cleaned by the agent
	 */
	public VAEnvironment(VAAgent vacuumAgent, Point vacuumAgentPosition,
			VAFloor floor) {
		super();
		this.vacuumAgent = vacuumAgent;
		this.vacuumAgentPosition = vacuumAgentPosition;
		this.floor = floor;
		this.stepCount = 0;
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
	 * Gets the vacuum agent.
	 *
	 * @return the vacuum agent
	 */
	public VAAgent getVacuumAgent() {
		return vacuumAgent;
	}

	/**
	 * Sets the vacuum agent.
	 *
	 * @param vacuumAgent the new vacuum agent
	 */
	public void setVacuumAgent(VAAgent vacuumAgent) {
		this.vacuumAgent = vacuumAgent;
	}

	/**
	 * Generate a perception for the agent according to the environment type.
	 * 
	 * @return A perception for the agent according to the environment type.
	 */
	protected abstract VAPercept genPerception();

	/**
	 * The environment goes on by a step:
	 * The vacuum agent does an action.
	 * The environment evolves consequently.
	 *
	 * @throws VAIllegalMove the vA illegal move
	 */
	@Override
	public void step() throws VAIllegalMove {
		
		if(isDone()){
			return;
		}
		
		if(stepCount == 0){
			this.startPosition = new Point(vacuumAgentPosition.x, vacuumAgentPosition.y);
		}
		
		VAPercept percept = this.genPerception();
		VAAction action = (VAAction) vacuumAgent.execute(percept);
		VANeighborhood neighborhood = this.getNeighborhood(vacuumAgentPosition);

		
		
		
		if (action.getActionType() == VAActionType.SUCK) {
			floor.getTile(vacuumAgentPosition).setStatus(VATileStatus.CLEAN);
			vacuumAgent.spendEnergy();
		}
		if (action.getActionType() == VAActionType.MOVENORTH) {
			if (!neighborhood.northIsFree()) {
				throw new VAIllegalMove("Illegal Move!");
			}
			vacuumAgentPosition.x++;
			vacuumAgent.spendEnergy();
		}
		if (action.getActionType() == VAActionType.MOVESOUTH) {
			if (!neighborhood.southIsFree()) {
				throw new VAIllegalMove("Illegal Move!");
			}
			vacuumAgentPosition.x--;
			vacuumAgent.spendEnergy();
		}
		if (action.getActionType() == VAActionType.MOVEWEST) {
			if (!neighborhood.westIsFree()) {
				throw new VAIllegalMove("Illegal Move!");
			}
			vacuumAgentPosition.y--;
			vacuumAgent.spendEnergy();
		}
		if (action.getActionType() == VAActionType.MOVEEAST) {
			if (!neighborhood.eastIsFree()) {
				throw new VAIllegalMove("Illegal Move!");
			}
			vacuumAgentPosition.y++;
			vacuumAgent.spendEnergy();
		}
		this.stepCount++;
		System.out.println("MISURA DI PRESTAZIONE >>>> "+this.getPerformanceMeasure(this.vacuumAgent));

	}

	/**
	 * Evolves the environment by n steps.
	 *
	 * @param n 
	 * 		numbers of step to go on.
	 * @throws VAIllegalMove the vA illegal move
	 */
	@Override
	public void step(int n) throws VAIllegalMove {
		for (int i = 0; i < n; i++) {
			this.step();
		}

	}
	
	/**
	 *  Evolves the environment until the end.
	 * 
	 * @throw VAIllegalMove
	 */
	@Override
	public void stepUntilDone() throws VAIllegalMove {
		while (!isDone()) {
			this.step();
		}

	}

	/* (non-Javadoc)
	 * @see framework.Environment#isDone()
	 */
	@Override
	public boolean isDone() {
		if(vacuumAgent.getEnergy() <= 0 || !vacuumAgent.isAlive()){
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see framework.Environment#getPerformanceMeasure(framework.Agent)
	 */
	@Override
	public double getPerformanceMeasure(Agent forAgent) {
		int clean = floor.countClean();
		System.out.println("Clean ->"+clean);
		int reachable = floor.countReachable();
		System.out.println("Reachable ->"+reachable);
		System.out.println(vacuumAgentPosition);
		System.out.println(startPosition);
		int distanceFromStart = floor.distanceBetween(vacuumAgentPosition, startPosition);
		System.out.println("distanceFromStart->"+distanceFromStart);
		
		double p1 = Math.pow(clean, 2) / Math.pow(reachable, 2);
		System.out.println("p1->"+p1);
		double p2 = Math.log10(distanceFromStart / (Math.sqrt(reachable)*2) + 1)/2;
		System.out.println("p2->"+p2);
		double p3 = Math.log10(stepCount / (reachable*3) + 1)/3;
		System.out.println("p3->"+p3);
			
		return p1/(1+p2+p3);
	}

	/**
	 * Given a position, gets the status of adjacent tiles.
	 * (if they're accessible or not)
	 *
	 * @param p a position in the map.
	 * 
	 * @return the neighborhood state.
	 */
	public VANeighborhood getNeighborhood(Point p) {

		boolean north = true;
		boolean south = true;
		boolean east = true;
		boolean west = true;

		if (floor.getTile(new Point(p.x + 1, p.y)).getStatus() == VATileStatus.BLOCK) {
			north = false;
		}
		if (floor.getTile(new Point(p.x - 1, p.y)).getStatus() == VATileStatus.BLOCK) {
			south = false;
		}
		if (floor.getTile(new Point(p.x, p.y + 1)).getStatus() == VATileStatus.BLOCK) {
			east = false;
		}
		if (floor.getTile(new Point(p.x, p.y - 1)).getStatus() == VATileStatus.BLOCK) {
			west = false;
		}

		return new VANeighborhood(north, south, east, west);
	}

}
