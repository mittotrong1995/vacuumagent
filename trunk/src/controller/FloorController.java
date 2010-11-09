package controller;

import com.trolltech.qt.core.QPoint;

import model.Floor;
import model.Tile;

public class FloorController {
	static final int DUSTTOADD = 25;

	protected Floor floor;

	public FloorController(Floor floor) {
		this.floor = floor;
	}

	public void addTile(int x, int y) {
		if (floor.getTiles().containsKey(new QPoint(x, y)))
			return;
		floor.getTiles().put(new QPoint(x, y), new Tile(0));
	}

	public void removeTile(int x, int y) {
		floor.getTiles().remove((new QPoint(x, y)));
	}

	public void addDust(int x, int y) {
		if (!floor.getTiles().containsKey(new QPoint(x, y))) {
			return;
		}

		if (floor.getTile(new QPoint(x, y)).getDust() + DUSTTOADD > 255)
			floor.getTile(new QPoint(x, y)).setDust(255);
		else
			floor.getTile(new QPoint(x, y)).addDust(DUSTTOADD);
	}

	public void removeDust(int x, int y) {

		if (!floor.getTiles().containsKey(new QPoint(x, y))) {
			return;
		}
		if (floor.getTile(new QPoint(x, y)).getDust() - DUSTTOADD < 0)
			floor.getTile(new QPoint(x, y)).setDust(0);
		else
			floor.getTile(new QPoint(x, y)).removeDust(DUSTTOADD);
	}

}
