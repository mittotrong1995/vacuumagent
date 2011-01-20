package main.tildeTeam;

import java.awt.Point;
import java.util.ArrayList;

import vacuumAgent.VAAction;
import vacuumAgent.VANeighborhood;
import vacuumAgent.VAPercept;
import vacuumAgent.VAAction.VAActionType;

public class TLDAgentSemiObservableCase extends TDLAgent{
	
	
	protected ArrayList<Point> history;
	
	protected TLDInnerWorld innerWorld;
	protected Point innerWorldPosition;

	public TLDAgentSemiObservableCase(int energy) {
		super(energy);
		history = new ArrayList<Point>();
		innerWorld = new TLDInnerWorld();
	}

	@Override
	protected VAAction run(VAPercept percept) {
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

}
