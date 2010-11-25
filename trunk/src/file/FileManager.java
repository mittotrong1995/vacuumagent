package file;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;

import exception.FileManagerException;

import vacuumAgent.VAFloor;
import vacuumAgent.VAPercept;
import vacuumAgent.VATile;
import vacuumAgent.VATile.VATileStatus;
import vacuumAgent.environment.VAEnvironment;

/**
 * @author Giovanna,Maria,Antonia
 * 
 */
public class FileManager {

	static final int INDEX_FLOOR_SIZE = 1;
	static final int INDEX_AGENT_POSITION = 0;

	public static VAEnvironment load(String filePath)
			throws FileManagerException {
		File file = new File(filePath);
		VAEnvironment environment = createByStream(file);
		return environment;
	}

	private static VAEnvironment createByStream(File file)
			throws FileManagerException {

		List<String> lines = null;
		try {
			lines = FileUtils.readLines(file);
		} catch (IOException e) {
			throw new FileManagerException(e);
		}
		if (!lines.get(INDEX_AGENT_POSITION).trim()
				.matches("agentPosition=[0-9]+,[0-9]+"))
			throw new FileManagerException(
					"La mappa caricata non è corretta : posizione dell'agente non dichiarata correttamente");
		if (!lines.get(INDEX_FLOOR_SIZE).trim().matches("size=[0-9]+"))
			throw new FileManagerException(
					"La mappa caricata non è corretta : dimensione non dichiarata correttamente");

		Integer sizeMap = Integer.parseInt(lines.get(INDEX_FLOOR_SIZE).trim()
				.replace("size=", ""));
		if (lines.size() - 2 != sizeMap)
			throw new FileManagerException(
					"La mappa caricata non è corretta : il numero di righe della mappa non corrisponde alla dimensione dichiarata");
		
		VATile[][] tiles = initMap(sizeMap);
		for (int i = 2; i < lines.size(); i++) {
			String rowString = lines.get(i);
			if (rowString.length() != sizeMap)
				throw new FileManagerException(
						"La mappa caricata non è corretta : il numero di colonne della mappa non corrisponde alla dimensione dichiarata");
			for (int j = 0; j < rowString.length(); j++) {
				int ordinal = Integer.parseInt(String
						.copyValueOf(new char[] { rowString.charAt(j) }));
				VATileStatus type = VATileStatus.values()[ordinal];
				tiles[i - 2][j].setStatus(type);
			}
		}
		// Caricamento posizione agente
		String positionString = lines.get(INDEX_AGENT_POSITION).replace(
				"agentPosition=", "");
		int x_posAgent = Integer.parseInt(positionString.split(",")[0]);
		int y_posAgent = Integer.parseInt(positionString.split(",")[1]);
		Point positionAgent = new Point(x_posAgent, y_posAgent);
		if (x_posAgent >= sizeMap
				|| y_posAgent >= sizeMap
				|| tiles[x_posAgent][y_posAgent].getStatus().equals(
						VATileStatus.BLOCK))
			throw new FileManagerException(
					"La mappa caricata non è corretta : posizione dell'agente fuori mappa o in posizione occupata da un ostacolo!");

		VAFloor floor = new VAFloor(sizeMap);
		floor.setFloor(tiles);

		VAEnvironment env = new VAEnvironment(null, positionAgent,floor) {
			
			@Override
			protected VAPercept genPerception() {
				return null;
			}
		} ;
		return env;

	}

	private static VATile[][] initMap(Integer sizeMap) {
		VATile[][] tiles = new VATile[sizeMap][sizeMap];
		for (int i = 0; i < sizeMap; i++) {
			for (int j = 0; j < sizeMap; j++) {
				tiles[i][j] = new VATile();
			}
		}
		return tiles;
	}

	public static void save(VAEnvironment environment, String filePath)
			throws FileManagerException {
System.out.println("");
		Collection<String> lines = generateStream(environment);
		File file = new File(filePath);
		// CONTROLLO SE IL FILE ESISTE...
		// if (file.exists())
		// throw new FileManagerException("File esistente");
		try {
			FileUtils.writeLines(file, lines);
		} catch (IOException e) {
			throw new FileManagerException(e);
		}

	}

	private static Collection<String> generateStream(VAEnvironment environment) {
		List<String> map = new ArrayList<String>();
		Point vacuumAgentPosition = environment.getVacuumAgentPosition();
		map.add("agentPosition="+vacuumAgentPosition.x+","+vacuumAgentPosition.y);
		VAFloor env_floor=environment.getFloor();
		String sizeLines = "size=" + env_floor.getSize();
		map.add(sizeLines);
		VATile[][] tiles = env_floor.getFloor();
		for (VATile[] tilesRow : tiles) {
			String rowLine = "";
			for (VATile tileCol : tilesRow) {
				rowLine += tileCol.getStatus().ordinal();
			}
			map.add(rowLine);
		}
		return map;
	}

}
