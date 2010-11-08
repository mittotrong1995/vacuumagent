package view;

import model.Floor;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QWidget;

public class FloorView extends QWidget{
	final static int TILESIZE = 30;
	
	Floor floor;
	
	public FloorView(Floor floor){
		this.floor = floor;
	}
	

	@Override
    protected void paintEvent(QPaintEvent event) {

        QPainter painter = new QPainter(this);
        
        for (QPoint p: floor.getTiles().keySet()) {
        	int x = p.x() * TILESIZE;
        	int y = p.y() * TILESIZE;
			painter.drawRect(new QRect(p,new QPoint(x+TILESIZE, y+TILESIZE)));
		}
        
    }
	
	
	
	
}
