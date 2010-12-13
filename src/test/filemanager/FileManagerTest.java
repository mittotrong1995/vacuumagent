package test.filemanager;

import vacuumAgent.VATile;
import vacuumAgent.environment.VAEnvironment;
import exception.FileManagerException;
import file.FileManager;


public class FileManagerTest {

	
	public static void main(String[] args) throws FileManagerException {
		VAEnvironment load = FileManager.load("esempiosave.map");
		System.out.println("Agent position = "+load.getVacuumAgentPosition().toString());
		System.out.println("Size = "+load.getFloor().getSize());
		VATile[][] map = load.getFloor().getFloor();
		for (VATile[] tiles : map) {
			for (VATile tile : tiles) {
				System.out.print(tile.getStatus().ordinal());
			}
			System.out.println();
		}
		FileManager.save(load, "esempiosave2.map");
	}
}
