package tildeTeam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
	
	/**The visited. */
//	ArrayList<Point> visited; TODO
	TLDInnerWorld world;
	
	/**
	 * Instantiates a new tLD agent.
	 *
	 * @param energy the energy
	 */
	public TLDAgent(int energy) {		super(energy);		firtsStep = true;	}

	/**	 * Die.	 */
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
		VAPercept vap = (VAPercept) percept;
		return nonObservableCase(vap);

	}
	
	private Action observableCase(VAPercept percept){
		
		
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
			SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> floorDir = TLDConvertToGraph.toGraph(percept.getFloor());

			TLDPathFinder pathFinder = new TLDPathFinder(floorDir);

			this.path = pathFinder.findPath(currPoint, dirtyNodes);

			this.firtsStep = false;
			
		} // not 1° step

		if (percept.getCurrentTileStatus() == VATileStatus.DIRTY)	return new VAAction(VAActionType.SUCK);
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
	
	private Action semiObservableCase(Percept percept){
		
		return null;		
	}
	
	private Action nonObservableCase(VAPercept percept){
		if(firtsStep){
			world = new TLDInnerWorld();
			path = new ArrayList<Point>();    
			path.add(new Point(0,0));
			firtsStep = false;
		}
		
		if (percept.getCurrentTileStatus() == VATileStatus.DIRTY){
			return new VAAction(VAActionType.SUCK);
		}
		
		VANeighborhood neighborhood = percept.getNeighborhood();
		
		
		Point currentPosition = path.get(path.size()-1);
		world.updateWorld(currentPosition, percept.getNeighborhood());
		
		
//		System.out.println("curr "+currentPosition);
		
		ArrayList<Point> freeStage = new ArrayList<Point>();
		
		if(neighborhood.northIsFree()){
			Point point = new Point(currentPosition.x+1, currentPosition.y);
			if(!world.visited(point))
				freeStage.add(point);
		}
		
		if(neighborhood.southIsFree()){
			Point point = new Point(currentPosition.x-1, currentPosition.y);
			if(!world.visited(point))
				freeStage.add(point);
		}
		
		if(neighborhood.eastIsFree()){
			Point point = new Point(currentPosition.x, currentPosition.y+1);
			if(!world.visited(point))
				freeStage.add(point);
		}
		
		if(neighborhood.westIsFree()){
			Point point = new Point(currentPosition.x, currentPosition.y-1);
			if(!world.visited(point))
				freeStage.add(point);
		}
		
		Point nextPoint;
		
		if(freeStage.isEmpty()){
			if(currentPosition.x == 0 && currentPosition.y == 0){
				this.die();
				return new VAAction(VAActionType.SUCK);
			}
			path.remove(path.size()-1);
			nextPoint = path.get(path.size()-1);
		}
		else{
			nextPoint = freeStage.get(0);
			path.add(nextPoint);
		}
		
//		System.out.println("nextpoint: "+nextPoint.toString());
//		
//
//		System.out.println("--------------------------------------------");
//		System.out.println("--------------------------------------------");
		
		int cy = currentPosition.y, cx = currentPosition.x, nx = nextPoint.x, ny = nextPoint.y;
		
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
