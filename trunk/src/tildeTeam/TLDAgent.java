package tildeTeam;

import java.awt.Point;
import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

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
public class TLDAgent extends VAAgent {

	/** The firts step. */
	boolean firtsStep;
	
	/** The path. */
	ArrayList<Point> path;

	/**
	 * Instantiates a new tLD agent.
	 *
	 * @param energy the energy
	 */
	public TLDAgent(int energy) {		super(energy);		firtsStep = true;	}

	/**
	 * Die.
	 */
	private void die() {		this.setAlive(false);	}

	/* (non-Javadoc)
	 * @see vacuumAgent.VAAgent#execute(framework.Percept)
	 */
	@Override
	public Action execute(Percept percept) {		// creare la lista con il path
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return observableCase(percept);

	}
	
	private Action observableCase(Percept percept){
		
		VAPercept vap = (VAPercept) percept;
		Point currPoint = vap.getVacuumAgentPosition();

		if (this.firtsStep) {
			int n = vap.getFloor().getSize();
			ArrayList<Point> dirtyNodes = new ArrayList<Point>();

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (vap.getFloor().getTile(new Point(i, j)).getStatus() == VATileStatus.DIRTY)
						dirtyNodes.add(new Point(i, j));// dirtyNodes
				}
			}
			SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> floorDir = TLDConvertToGraph.toGraph(vap.getFloor());

			TLDPathFinder pathFinder = new TLDPathFinder(floorDir);

			this.path = pathFinder.findPath(currPoint, dirtyNodes);

			this.firtsStep = false;
			
		} // not 1° step

		if (vap.getCurrentTileStatus() == VATileStatus.DIRTY)	return new VAAction(VAActionType.SUCK);
		int cx, cy, nx, ny;

		cx = currPoint.x;		cy = currPoint.y;

		if (path.size() == 0) {
			this.die();
			return new VAAction(VAActionType.SUCK);
		}// TODO noop
		Point nextP = path.remove(0);
		nx = nextP.x;		ny = nextP.y;
		
		System.out.println("-----------"+cx+","+cy+" -> "+nx+","+ny);

		if (cy == ny && cx + 1 == nx) {
			return new VAAction(VAActionType.MOVENORTH);
		}// north e south sono invertiti
		if (cy == ny && cx - 1 == nx) {
			return new VAAction(VAActionType.MOVESOUTH);
		}// north e south sono invertiti
		if (cy+1 == ny && cx == nx) {
			return new VAAction(VAActionType.MOVEEAST);
		}// north e south sono invertiti
		if (cy -1 == ny  && cx == nx) {
			return new VAAction(VAActionType.MOVEWEST);
		}
		return null;
	}

}
