package main.tildeTeam;

import java.awt.Point;
import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import vacuumAgent.VAAction;
import vacuumAgent.VAPercept;
import vacuumAgent.VAAction.VAActionType;
import vacuumAgent.VATile.VATileStatus;


public class TLDAgentObservableCase extends TDLAgent {

	public TLDAgentObservableCase(int energy) {
		super(energy);
	}

	@Override
	protected VAAction run(VAPercept percept) {

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
}
