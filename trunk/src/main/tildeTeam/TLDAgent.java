package main.tildeTeam;

import java.awt.Point;
import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import framework.Action;
import framework.Percept;
import vacuumAgent.VAAction;
import vacuumAgent.VAAction.VAActionType;
import vacuumAgent.VAAgent;
import vacuumAgent.VANeighborhood;
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
	ArrayList<Point> history;

	/** The visited. */
	// ArrayList<Point> visited; TODO
	TLDInnerWorld innerWorld;

	Point innerWorldPosition;

	/**
	 * Instantiates a new tLD agent.
	 * 
	 * @param energy
	 *            the energy
	 */
	public TLDAgent(int energy) {

		super(energy);
		firtsStep = true;
		innerWorld = new TLDInnerWorld();
		path = new ArrayList<Point>();
		history = new ArrayList<Point>();

	}

	/** * Die. */
	private void die() {
		this.setAlive(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vacuumAgent.VAAgent#execute(framework.Percept)
	 */
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

//		return observableCase(vap);
//		 return semiObservableCase(percept);
		 return nonObservableCase(vap);

	}

	private Action observableCase(VAPercept percept) {

		Point currPoint = percept.getVacuumAgentPosition();

		if (this.firtsStep) {
			int n = percept.getFloor().getSize();
			ArrayList<Point> dirtyNodes = new ArrayList<Point>();

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					if (percept.getFloor().getTile(new Point(i, j)).getStatus() == VATileStatus.DIRTY)
						dirtyNodes.add(new Point(i, j));// dirtyNodes
				}
			}
			SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> floorDir = TLDConvertToGraph
					.toGraph(percept.getFloor());

			TLDObservableCaseResolver resolver = new TLDObservableCaseResolver(
					floorDir);

			this.path = resolver.findGoodCycle(currPoint, dirtyNodes,
					this.energy);

			this.firtsStep = false;

		} // not 1° step

		if (path.size() == 0) {
			this.die();
			return new VAAction(VAActionType.NOOP);
		}

		Point nextP = path.remove(0);
		return moveAgent(currPoint, nextP);

	}

	private Action semiObservableCase(VAPercept percept) {
		return nonObservableCase(percept);
	}

	private Action nonObservableCase(VAPercept percept) {
		if (firtsStep) {
			history.add(new Point(0, 0));
			this.innerWorldPosition = new Point(0, 0);
			firtsStep = false;

		}
		
		innerWorld.updateWorld(innerWorldPosition, percept.getNeighborhood());
		if(!history.contains(innerWorldPosition))
			history.add(innerWorldPosition);

		if (!this.path.isEmpty()) {

			for (Point p : this.path) {
				System.out.println(p);
			}
			System.out.println("Current Position" + innerWorldPosition);
			Point nextPoint = path.remove(0);
			System.out.println("NextPoint" + nextPoint);
			System.out.println("<___________>");
			Point precPoint = innerWorldPosition;
			innerWorldPosition = nextPoint;
			return moveAgent(precPoint, nextPoint);
		}


		VANeighborhood neighborhood = percept.getNeighborhood();

		ArrayList<Point> freeStage = new ArrayList<Point>();

		if (neighborhood.northIsFree()) {
			Point point = new Point(innerWorldPosition.x + 1,
					innerWorldPosition.y);
			if (!innerWorld.visited(point))
				freeStage.add(point);
		}

		if (neighborhood.southIsFree()) {
			Point point = new Point(innerWorldPosition.x - 1,
					innerWorldPosition.y);
			if (!innerWorld.visited(point))
				freeStage.add(point);
		}

		if (neighborhood.eastIsFree()) {
			Point point = new Point(innerWorldPosition.x,
					innerWorldPosition.y + 1);
			if (!innerWorld.visited(point))
				freeStage.add(point);
		}

		if (neighborhood.westIsFree()) {
			Point point = new Point(innerWorldPosition.x,
					innerWorldPosition.y - 1);
			if (!innerWorld.visited(point))
				freeStage.add(point);
		}

		Point nextPoint;
		
		
		if(!freeStage.isEmpty()){
			System.out.println("FreStageCurrent Position" + innerWorldPosition);
			nextPoint = freeStage.get(0);
			System.out.println("NextPoint" + nextPoint);
			Point precPoint = innerWorldPosition;
			innerWorldPosition = nextPoint;
			return moveAgent(precPoint, nextPoint);
		}
		
		
		
		boolean firstFinded = false;
		ArrayList<Point> minPath = new ArrayList<Point>();
		
		for(Point p: history){
			if(!innerWorld.deadEnd(p) && !p.equals(innerWorldPosition)){
				if(!firstFinded){
					minPath = innerWorld.findPath(innerWorldPosition, p);
					firstFinded = true;
				}
				else {
					ArrayList<Point> tempPath = innerWorld.findPath(innerWorldPosition, p);
					if(tempPath.size() < minPath.size()){
						minPath = tempPath;
					}
				}
			}
		}
		
		if(firstFinded == false || minPath.size() + 2 > this.getEnergy()){
			if(innerWorld.equals(new Point(0,0))){
				this.die();
				return new VAAction(VAActionType.NOOP);
			}
			minPath = innerWorld.findPath(innerWorldPosition, new Point(0,0));
		}
		

		
		this.path = minPath;
		return new VAAction(VAActionType.NOOP);
	}

	private VAAction moveAgent(Point currentPosition, Point nextPoint) {

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
