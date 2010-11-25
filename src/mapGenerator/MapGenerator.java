package mapGenerator;

import java.awt.Point;
import java.util.ArrayList;

import exception.InvalidAlgorithmException;

import vacuumAgent.VAFloor;
import vacuumAgent.VATile;
import vacuumAgent.VATile.VATileStatus;

/**
 * Class for the automatic generation of a map. It's possible to generate map
 * without unreachable areas, or with casual ones. It's also possible to choose
 * the algorithm to use for generating the map, between ProgressiveCreation,
 * CoinCreation, FullRandom.
 * 
 * @author falkor
 * 
 */
public class MapGenerator {

	/**
	 * @param size
	 *            the size of a side of the room
	 * @param blockPercentage
	 *            the percentage of blocks in the room
	 * @param dirtyPercentage
	 *            the percentage of dirty cells in the room
	 * @param fullyReachable
	 *            true if all free cells have to be reachable
	 * @param algorithm
	 *            the algorithm to use for the floor generation: 0 for
	 *            ProgressiveCreation, 1 for CoinCreation or 2 for FullRandom
	 * @return
	 */
	public static VAFloor generateFloor(int size, float blockPercentage,
			float dirtyPercentage, boolean fullyReachable, int algorithm) {

		int matrix[][] = new int[size][size];

		int blocks = (int) (size * size * blockPercentage / 100);

		switch (algorithm) {
		case 0:
			progressiveGenerator(matrix, blocks);
			break;
		case 1:
			coinCreation(matrix, blockPercentage);
			break;
		case 2:
			fullRandom(matrix, blocks);
			break;
		default:
			try {
				throw new InvalidAlgorithmException();
			} catch (InvalidAlgorithmException e) {
				e.printStackTrace();
			}
			break;
		}

		if (fullyReachable && algorithm != 0)
			makeAllReachable(matrix);

		dirtyCreation(matrix, dirtyPercentage);

		VATile floor [][] = new VATile [size][size];
		assert( size == matrix.length );

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] == 0)
				{
					floor[i][j] = new VATile( VATileStatus.CLEAN );					
				}
				else if (matrix[i][j] == 1)
				{
					floor[i][j] = new VATile( VATileStatus.BLOCK );
				}
				else if (matrix[i][j] == 2)
				{
					floor[i][j] = new VATile( VATileStatus.DIRTY );
				}
			}
		}
		VAFloor vaFloor = new VAFloor();
		vaFloor.setFloor(floor);
		return vaFloor;
	}


	private static void progressiveGenerator(int[][] matrix, int blocks) {

		int free = (matrix.length * matrix.length - blocks);
		System.out.println("blocks: " + blocks + "\tfree: " + free);

		// initialize the matrix at 1 (all cells are a block)
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j] = 1;
			}
		}
		// determine a start point
		int exp = ((int) Math.log10(matrix.length)) + 1;
		int x = (int) (Math.random() * (Math.pow(10.0, (double) exp)))
		% matrix.length;
		int y = (int) (Math.random() * (Math.pow(10.0, (double) exp)))
		% matrix.length;
		Point initialPoint = new Point(x, y);

		// create an array of point and other utility variables
		ArrayList<Point> toVisit = new ArrayList<Point>();
		int setted = 0;
		Point tmp = null, current = null;
		boolean random = false;

		toVisit.add(initialPoint);
		matrix[initialPoint.x][initialPoint.y] = 0;
		setted++;

		while (setted < free) {
			// choose a point that has been already visited as new start point
			if (random) {
				toVisit.remove(current);
				exp = ((int) Math.log10(toVisit.size())) + 1;
				current = toVisit.get(((int) (Math.random() * (Math.pow(10.0,
						(double) exp))) % toVisit.size()));
				random = false;
			} else
				current = toVisit.get(toVisit.size() - 1);

			tmp = nextPoint(current, matrix);

			if (tmp != null) {
				matrix[tmp.x][tmp.y] = 0;
				setted++;
				toVisit.add(tmp);
			} else {
				random = true;
			}
		}

	}

	/**
	 * Method for choosing a point that is adjacent to the passed one, and that
	 * is free (matrix contains 0 in that point)
	 * 
	 * @param current
	 * @param matrix
	 * @return
	 */
	private static Point nextPoint(Point point, int[][] matrix) {
		// chose a direction
		int dir = (int) (Math.random() * 10) % 4;
		int attempts = 0;

		while (attempts < 4) {
			switch (dir) {
			// north
			case 0:
				if (point.x > 0 && matrix[point.x - 1][point.y] == 1) {
					// matrix[point.x - 1][point.y] = 5;
					return new Point(point.x - 1, point.y);
				} else {
					dir++;
					attempts++;
				}
				break;
				// est
			case 1:
				if (point.y != matrix.length - 1
						&& matrix[point.x][point.y + 1] == 1) {
					// matrix[point.x][point.y + 1] = 6;
					return new Point(point.x, point.y + 1);
				} else {
					dir++;
					attempts++;
				}
				break;
				// south
			case 2:
				if (point.x != matrix.length - 1
						&& matrix[point.x + 1][point.y] == 1) {
					// matrix[point.x + 1][point.y] = 7;
					return new Point(point.x + 1, point.y);
				} else {
					dir++;
					attempts++;
				}
				break;
				// west
			case 3:
				if (point.y > 0 && matrix[point.x][point.y - 1] == 1) {
					// matrix[point.x][point.y - 1] = 8;
					return new Point(point.x, point.y - 1);
				} else {
					dir = 0;
					attempts++;
				}
				break;
			}
		}
		return null;
	}

	private static void coinCreation(int[][] matrix, float percentage) {
		float coin;
		int cont = 0;
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				coin = ((int) (Math.random() * 1000) % 100);
				if (coin < percentage) {
					matrix[i][j] = 1;
					cont++;
				} else
					matrix[i][j] = 0;
			}
		}
		System.out.println("settate " + cont + " celle come occupate.");

	}

	private static void fullRandom(int[][] matrix, int blocks) {
		int setted = 0;
		int exp = ((int) Math.log10(matrix.length)) + 1;
		exp = (int) (Math.pow(10.0, (double) exp));
		int x, y;
		int free, block;

		if ((matrix.length * matrix.length / blocks) > 0.5) {
			free = 0;
			block = 1;
		} else {
			free = 1;
			block = 0;
		}

		// initialize the matrix at 0 (all cells are free (freecell))
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j] = free;
			}
		}

		while (setted < blocks) {
			x = (int) (Math.random() * exp) % matrix.length;
			y = (int) (Math.random() * exp) % matrix.length;

			if (matrix[x][y] == free) {
				matrix[x][y] = block;
				setted++;
			}

		}

	}

	private static void dirtyCreation(int[][] matrix, float percentage) {
		float coin;
		int cont = 0;

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++)
				if (matrix[i][j] == 0) {
					coin = ((int) (Math.random() * 1000) % 100);
					if (coin < percentage) {
						matrix[i][j] = 2;
						cont++;
					}
				}

		}
		System.out.println("settate " + cont + " celle come sporche.");
	}

	/**
	 * Method for making reachable all areas of the floor.
	 * @param matrix
	 */
	private static void makeAllReachable(int[][] matrix) {
		int matrixTmp [][] = new int [matrix.length][matrix.length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				if (matrix[i][j] == 1)
					matrixTmp[i][j] = -1;
				else
					matrixTmp[i][j] = 0;
			}
		}

		int num = 1;
		ArrayList<Point> couples = new ArrayList<Point>();
		ArrayList<Point> blocksStart = new ArrayList<Point>();
		int min, mag;

		for (int i = 0; i < matrixTmp.length; i++) {
			for (int j = 0; j < matrixTmp.length; j++) {
				if (matrixTmp[i][j] != -1) {
					//prima riga
					if (i == 0) {
						//prima cella
						if (j == 0) {
							matrixTmp[i][j] = num++;
							blocksStart.add(new Point(i, j));
						}
						else
							if (matrixTmp[i][j - 1] != -1)
								//continuo con un vecchio blocco
								matrixTmp[i][j] = matrixTmp[i][j - 1];
							else {
								//inizio un nuovo blocco
								matrixTmp[i][j] = num++;
								blocksStart.add(new Point(i, j));
							}
					}
					//riga generica
					else {
						if (j > 0 && matrixTmp[i][j - 1] != -1){
							matrixTmp[i][j] = matrixTmp[i][j - 1];
							//rilevo corrispondenza tra blocchi.. la metto da parte.
							if (matrixTmp[i -1][j] != -1 && matrixTmp[i][j - 1] != matrixTmp[i - 1][j]) {
								if (matrixTmp[i][j - 1] < matrixTmp[i - 1][j]) {
									min = matrixTmp[i][j - 1];
									mag = matrixTmp[i - 1][j];
								}
								else {
									min = matrixTmp[i - 1][j];
									mag = matrixTmp[i][j - 1];
								}
								if (!couples.contains(new Point(min, mag)))
									couples.add(new Point(min, mag));
							}
						}
						else if (matrixTmp[i -1][j] != -1)
							matrixTmp[i][j] = matrixTmp[i - 1][j];
						else{
							//inizio un nuovo blocco
							matrixTmp[i][j] = num++;
							blocksStart.add(new Point(i, j));
						}
					}

				}
			}
		}//for

		boolean blocchi [] = new boolean [num - 1];
		for (int i = 0; i < blocchi.length; i++) {
			blocchi[i] = true;
		}

		Point point;
		while (!couples.isEmpty()) {
			point = couples.get(couples.size() - 1);

			for (int i = 0; i < matrixTmp.length; i++) {
				for (int j = 0; j < matrixTmp.length; j++) {
					if (matrixTmp[i][j] == point.y)
						matrixTmp[i][j] = point.x;
				}
			}
			for (Point current : couples) {
				if (current.x == point.y)
					current.x = point.x;
				if (current.y == point.y)
					current.x = point.x;
			}

			blocchi[point.y - 1] = false;			
			couples.remove(couples.size() - 1);
		}

		for (int i = 0; i < matrixTmp.length; i++) {
			for (int j = 0; j < matrixTmp.length; j++) {
				switch (matrixTmp[i][j]) {
				case -1:
					System.out.print("B  ");
					break;
				default:
					System.err.print(matrixTmp[i][j]);
					if(matrixTmp[i][j] < 10)
						System.err.print("  ");
					else
						System.err.print(" ");
					break;
				}
			}
			System.out.println();
		}

		System.out.println("finito di stampare");

		int iMin, iMax, jMin, jMax, iStart, jEnd;
		for (int k = 1; k < blocchi.length; k++) {
			if (blocchi[k]) {
				if (blocksStart.get(0).x < blocksStart.get(k).x) {
					iMin = blocksStart.get(0).x;
					iMax = blocksStart.get(k).x;
				}
				else {
					iMin = blocksStart.get(k).x;
					iMax = blocksStart.get(0).x;
				}
				if (blocksStart.get(0).y < blocksStart.get(k).y) {
					jMin = blocksStart.get(0).y;
					jMax = blocksStart.get(k).y;
				}
				else {
					jMin = blocksStart.get(k).y;
					jMax = blocksStart.get(0).y;
				}
				iStart = blocksStart.get(0).x;
				jEnd = blocksStart.get(k).y;

				for (int j = jMin; j <= jMax; j++) {
					matrixTmp[iStart][j] = 1;
				}
				for (int i = iMin; i <= iMax; i++) {
					matrixTmp[i][jEnd] = 1;
				}
			}
		}

		for (int i = 0; i < matrixTmp.length; i++) {
			for (int j = 0; j < matrixTmp.length; j++) {
				if (matrixTmp[i][j] >= 0)
					matrix[i][j] = 0;
				else
					matrix[i][j] = 1;
			}
		}


	}

//	private void print(int[][] matrix) {
//		for (int i = 0; i < matrix.length; i++) {
//			for (int j = 0; j < matrix.length; j++) {
//				switch (matrix[i][j]) {
//				case 0:
//					System.err.print("F ");
//					break;
//				case 1:
//					System.out.print("B ");
//					break;
//				case 2:
//					System.err.print("D ");
//					break;
//				}
//			}
//			System.out.println();
//		}
//	}
//
//	public static void main(String[] args) {
//		MapGenerator g = new MapGenerator();
//		g.print(g.generateFloor(15, (float) 40.0, (float) 30.0, true, 1)).getFloor();
//	}

}
