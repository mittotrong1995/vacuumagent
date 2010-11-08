import java.awt.geom.Point2D;

import javax.swing.text.FlowView;

import view.FloorView;

import model.Floor;
import model.Tile;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QPushButton;





public class Dummy
{
    public static void main(String args[])
    {
        QApplication.initialize(args);

        QPushButton hello = new QPushButton("Mi Piacciono i treni");
        hello.resize(120, 40);
        hello.setWindowTitle("WRUUMMMMMM");
        hello.show();
        
        Floor vomito = new Floor();
        vomito.putTile(new QPoint(1,1), new Tile(69));
        vomito.putTile(new QPoint(1,3), new Tile(69));
        vomito.putTile(new QPoint(1,6), new Tile(69));
        vomito.putTile(new QPoint(3,1), new Tile(69));
        FloorView robertino = new FloorView(vomito);
        robertino.show();
        
        
        
        

        QApplication.exec();
    }
}