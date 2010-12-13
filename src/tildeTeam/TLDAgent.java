package tildeTeam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.TargetDataLine;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.sun.jndi.ldap.PersistentSearchControl;

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
	
	
	/**The visited. */
//	ArrayList<Point> visited; TODO
	TLDInnerWorld innerWorld;
	
	Point innerWorldPosition;
	
	
	/**
	 * Instantiates a new tLD agent.
	 *
	 * @param energy the energy
	 */
	public TLDAgent(int energy) {		
		
		super(energy);		
		firtsStep = true;
		innerWorld = new TLDInnerWorld();
		path = new ArrayList<Point>();
		history = new ArrayList<Point>();
		
	}

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
		
		if (vap.getCurrentTileStatus() == VATileStatus.DIRTY)	
			return new VAAction(VAActionType.SUCK);
		
		
		return observableCase(vap);
//		return semiObservableCase(percept);
//		return nonObservableCase(vap);

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
			
//			SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> floorDir = TLDConvertToGraph.toGraph(percept.getFloor());
//			TLDPathFinder pathFinder = new TLDPathFinder(floorDir);
//			this.path = pathFinder.findCycle(currPoint, dirtyNodes);
			
			
			this.path = this.findGoodCycle(percept, currPoint, dirtyNodes, this.energy);
			
			this.firtsStep = false;
			
		} // not 1° step

		if (path.size() == 0) {
			this.die();
			return new VAAction(VAActionType.NOOP);
		}
		
		Point nextP = path.remove(0);
		return moveAgent(currPoint,nextP);

	}
	
	
	private ArrayList<Point> findGoodCycle (VAPercept percept, Point start, ArrayList<Point> dirtyNodes, int energy){
		
		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> floorDir = TLDConvertToGraph.toGraph(percept.getFloor());
		TLDPathFinder pathFinder = new TLDPathFinder(floorDir);
		ArrayList<Point> goodCycle = pathFinder.findCycle(start, dirtyNodes);
		
		if(goodCycle.size()+dirtyNodes.size()<= energy){
			
			System.out.println("SUKA");
			return goodCycle;
	    }
//		
//		int maxVisited = this.dirtyVisited(goodCycle, dirtyNodes, energy);
//		int n = dirtyNodes.size();
//		int k =  maxVisited;
//		
//		
//		for(int i = n; i > k; i--){
//			dirtyNodes.remove(pathFinder.furthestNodeFrom(start, dirtyNodes));
//			ArrayList<Point> tempCycle = pathFinder.findCycle(start, dirtyNodes);
//			int tempVisited = dirtyVisited(tempCycle, dirtyNodes, energy);
//			
//			if(tempVisited > maxVisited){
//				maxVisited = tempVisited;
//				goodCycle = tempCycle;
//			}
//		}
		
		return goodCycle;
	}
	
	private int dirtyVisited(ArrayList<Point> path, ArrayList<Point> dirtyNodes, int energy){
		int cont = 0;
		
		for(int i = 1; i <= energy; i++){
			if(dirtyNodes.contains(path.get(i))){
				if(i < energy){
					i++;
					cont++;
				}
			}
		}
		return cont;
	}
	
	
	private Action semiObservableCase(VAPercept percept){
		return nonObservableCase(percept);		
	}
	
	private Action nonObservableCase(VAPercept percept){
		if(firtsStep){  
			history.add(new Point(0,0));
			this.innerWorldPosition = new Point(0,0);
			firtsStep = false;
			
		}

		if(!this.path.isEmpty()){
			for(Point p: this.path){
				System.out.println(p);
			}
			System.out.println("Current Position"+innerWorldPosition);
			Point nextPoint = path.remove(0);
			System.out.println("NextPoint"+nextPoint);
			System.out.println("<___________>");
			Point prePoint = innerWorldPosition;
			innerWorldPosition = nextPoint;
			return moveAgent(prePoint,nextPoint);
		}

		
		innerWorldPosition = history.get(history.size()-1);
		
		VANeighborhood neighborhood = percept.getNeighborhood();
		
		innerWorld.updateWorld(innerWorldPosition, percept.getNeighborhood());
		
		ArrayList<Point> freeStage = new ArrayList<Point>();
		
		if(neighborhood.northIsFree()){
			Point point = new Point(innerWorldPosition.x+1, innerWorldPosition.y);
			if(!innerWorld.visited(point))
				freeStage.add(point);
		}
		
		if(neighborhood.southIsFree()){
			Point point = new Point(innerWorldPosition.x-1, innerWorldPosition.y);
			if(!innerWorld.visited(point))
				freeStage.add(point);
		}
		
		if(neighborhood.eastIsFree()){
			Point point = new Point(innerWorldPosition.x, innerWorldPosition.y+1);
			if(!innerWorld.visited(point))
				freeStage.add(point);
		}
		
		if(neighborhood.westIsFree()){
			Point point = new Point(innerWorldPosition.x, innerWorldPosition.y-1);
			if(!innerWorld.visited(point))
				freeStage.add(point);
		}
		
		Point nextPoint;
		
		if(freeStage.isEmpty()){
			if(innerWorldPosition.x == 0 && innerWorldPosition.y == 0){
				this.die();
				return new VAAction(VAActionType.SUCK);
			}
		
			
			
			history.remove(history.size()-1);
			while(innerWorld.deadEnd(history.get(history.size()-1)) && 
					!history.get(history.size()-1).equals(new Point(0,0))){
				history.remove(history.get(history.size()-1));
			}
			nextPoint = history.get(history.size()-1);
			ArrayList<Point> pathFinded = innerWorld.findPath(innerWorldPosition, nextPoint);
			this.path.addAll(pathFinded);
			for(Point p: this.path){
				System.out.println(p);
			}
			nextPoint = this.path.remove(0);
			System.out.println("NextPoint"+nextPoint);
			System.out.println("<___________>");
			
		}
		else{
			System.out.println("Current Position"+innerWorldPosition);
			nextPoint = freeStage.get(0);
			history.add(nextPoint);
			System.out.println("NextPoint"+nextPoint);
		}
		
		Point prePoint = innerWorldPosition;
		innerWorldPosition = nextPoint;
		return moveAgent(prePoint,nextPoint);
	}
	
	private VAAction moveAgent(Point currentPosition ,Point nextPoint){
		
		
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
		return new VAAction(VAActionType.MOVENORTH);		
	}

}
