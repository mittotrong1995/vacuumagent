import view.FloorView;

import model.Floor;
import model.Tile;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.gui.QApplication;

public class Dummy {
	public static void main(String args[]) {
		QApplication.initialize(args);

		// QPushButton hello = new QPushButton("Mi Piacciono i treni");
		// hello.resize(120, 40);
		// hello.setWindowTitle("WRUUMMMMMM");
		// hello.show();

		Floor vomito = new Floor();
		

		//TODO RANDOM
		

		FloorView pornoStarRoberto = new FloorView(vomito);
		pornoStarRoberto.show();

		QApplication.exec();
	}
}