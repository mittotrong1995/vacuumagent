package vacuumAgent;

import java.awt.Point;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import vacuumAgent.VATile.VATileStatus;

// TODO: Auto-generated Javadoc
/**
 * The Class VAFloor.
 */
public class VAFloor {

//	/** The size. */
//	private int size;

	/** The floor. */
	protected VATile floor[][];

	/**
	 * Instantiates a new vA floor.
	 */
	public VAFloor() {
		super();
//		size = 0;
		this.floor = null;
	}

	/**
	 * Instantiates a new vA floor.
	 * 
	 * @param size
	 *            the size
	 */
	public VAFloor(int size) {
		super();
//		size=this.size;
		this.floor = new VATile[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				this.floor[i][j] = new VATile();
			}
		}
	}
	
	public VAFloor(int size, VATileStatus defaultStatus) {
		super();
//		size=this.size;
		this.floor = new VATile[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				this.floor[i][j] = new VATile();
				this.floor[i][j].setStatus(defaultStatus);
			}
		}
	}

	/**
	 * Gets the tile.
	 * 
	 * @param p
	 *            the p
	 * @return the tile
	 */
	public VATile getTile(Point p) {

		if (p.x >= getSize() || p.y >= getSize() || p.x < 0 || p.y < 0) {
			return new VATile(VATileStatus.BLOCK);
		}

		return floor[p.x][p.y];
	}

	/**
	 * Gets the size.
	 * 
	 * @return the size
	 */
	public int getSize() {
		return (floor==null?0:floor.length);
	}

	/**
	 * Gets the floor.
	 * 
	 * @return the floor
	 */
	public VATile[][] getFloor() {
		return floor;
	}

	/**
	 * Sets the floor.
	 * 
	 * @param floor
	 *            the new floor
	 */
	public void setFloor(VATile[][] floor) {

//		size = floor.length; 
		this.floor = floor;
	}

	// Ho commentato questo meto in quanto penso che non sia possibile
	// modificare la size del pavimento lasciando la matrice di un altra
	// dimensione.. Giovanna
	// /**
	// * Sets the size.
	// *
	// * @param size the new size
	// */
	// public void setSize(int size) {
	// this.size = size;
	//
	// }
	
	
	public int countReachable(){
		int cont = 0;
		int size = this.getSize();
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if(this.getTile(new Point(i,j)).getStatus() == VATileStatus.CLEAN){
					cont++;
				}
				if(this.getTile(new Point(i,j)).getStatus() == VATileStatus.DIRTY){
					cont++;
				}
			}
		}
		return cont;
	}
	
	public int countClean(){
		int cont = 0;
		int size = this.getSize();
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if(this.getTile(new Point(i,j)).getStatus() == VATileStatus.CLEAN){
					cont++;
				}
			}
		}
		return cont;
	}
	
	public int distanceBetween(Point p1, Point p2){
		SimpleDirectedWeightedGraph<Point, DefaultWeightedEdge> graph = VAConvertToGraph.toGraph(this);
		
//		DepthFirstIterator<Point, DefaultWeightedEdge> iter = new DepthFirstIterator<Point, DefaultWeightedEdge>(
//				graph);
//		Point vertex;
//		while (iter.hasNext()) {
//			vertex = iter.next();
//			System.out.println("Vertex " + vertex.toString()
//					+ " is connected to: "
//					+ graph.edgesOf(vertex).toString());
//		}

		return DijkstraShortestPath.findPathBetween(graph, p1, p2).size();
	}
	
	
	

}
