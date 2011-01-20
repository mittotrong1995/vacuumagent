package main.tildeTeam;

import java.awt.Point;
import java.util.ArrayList;

import framework.Action;
import framework.Percept;
import vacuumAgent.VAAction;
import vacuumAgent.VAAction.VAActionType;
import vacuumAgent.VAAgent;
import vacuumAgent.VAPercept;
import vacuumAgent.VATile.VATileStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class TLDAgent.
 */
public abstract class TDLAgent extends VAAgent {


	protected boolean firtsStep;

	protected ArrayList<Point> path;
	

	/**
	 * Instantiates a new tLD agent.
	 * 
	 * @param energy
	 *            the energy
	 */
	public TDLAgent(int energy) {

		super(energy);
		firtsStep = true;
		
		path = new ArrayList<Point>();


	}

	protected void die() {
		this.setAlive(false);
	}

	@Override
	public Action execute(Percept percept) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		VAPercept vap = (VAPercept) percept;

		if (vap.getCurrentTileStatus() == VATileStatus.DIRTY)
			return new VAAction(VAActionType.SUCK);

		return run(vap);

	}
	
	protected abstract VAAction run(VAPercept vap);

	protected VAAction moveAgent(Point currentPosition, Point nextPoint) {

		int cy = currentPosition.y, cx = currentPosition.x, nx = nextPoint.x, ny = nextPoint.y;

		if (cy == ny && cx + 1 == nx) {
			return new VAAction(VAActionType.MOVENORTH);
		}// north e south sono invertiti
		if (cy == ny && cx - 1 == nx) {
			return new VAAction(VAActionType.MOVESOUTH);
		}// north e south sono invertiti
		if (cy + 1 == ny && cx == nx) {
			return new VAAction(VAActionType.MOVEEAST);
		}// north e south sono invertiti
		if (cy - 1 == ny && cx == nx) {
			return new VAAction(VAActionType.MOVEWEST);
		}
		return new VAAction(VAActionType.MOVENORTH);
	}

}
