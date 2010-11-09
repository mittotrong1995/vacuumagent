package view;

import model.Floor;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QBrush;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QWidget;

import controller.FloorController;

public class FloorView extends QWidget{
	
	final static String TILE = "./resources/tile.jpg";
	final static int TILESIZE = 50;
	final static int MOVESPEED = 5;	
	
	Floor floor;
	FloorController floorController;
	int viewX,viewY;
	QImage tileTexture;
	boolean addingDust;
	
	public FloorView(Floor floor){
		this.floor = floor;
		floorController = new FloorController(floor);
		viewX =0 ;
		viewY =0 ;
		tileTexture = new QImage(TILE);
		addingDust = false;
	}
	
	@Override
	protected void keyPressEvent(QKeyEvent keyEvent) {
		super.keyPressEvent(keyEvent);
		if(keyEvent.key() == Qt.Key.Key_W.value()){ viewY+=MOVESPEED;}
		if(keyEvent.key() == Qt.Key.Key_S.value()){ viewY-=MOVESPEED;}
		if(keyEvent.key() == Qt.Key.Key_A.value()){ viewX+=MOVESPEED;}
		if(keyEvent.key() == Qt.Key.Key_D.value()){ viewX-=MOVESPEED;}		
		if(keyEvent.key() == Qt.Key.Key_E.value()){ addingDust = true;}		
		repaint();
	}
	
	@Override
	protected void keyReleaseEvent(QKeyEvent keyEvent) {
		super.keyReleaseEvent(keyEvent);
		if(keyEvent.key() == Qt.Key.Key_E.value()){ addingDust =  false;}
	}
	
	@Override
	protected void mouseMoveEvent(QMouseEvent arg__1) {
		// TODO Auto-generated method stub
		super.mouseMoveEvent(arg__1);
	}
	
	@Override
	protected void mousePressEvent(QMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		super.mousePressEvent(mouseEvent);
		
		int x = (mouseEvent.x() - viewX)/TILESIZE ;
		int y = (mouseEvent.y() - viewY)/TILESIZE ;
		if(!addingDust){
			if (mouseEvent.button()== Qt.MouseButton.LeftButton){floorController.addTile(x, y);}
			if (mouseEvent.button()== Qt.MouseButton.RightButton){floorController.removeTile(x, y);}
		}
		else
		{
			if (mouseEvent.button()== Qt.MouseButton.LeftButton){floorController.addDust(x, y);}
			if (mouseEvent.button()== Qt.MouseButton.RightButton){floorController.removeDust(x, y);}
		}
			
		repaint();
		
	}

	
	@Override
    protected void paintEvent(QPaintEvent event) {

        QPainter painter = new QPainter(this);
        painter.translate(viewX, viewY);
        
        for (QPoint p: floor.getTiles().keySet()) {
        	int x = p.x() * TILESIZE;
        	int y = p.y() * TILESIZE;	
        	QRect rect = new QRect(new QPoint(x, y),new QPoint(x+TILESIZE, y+TILESIZE));
			painter.drawImage(rect, tileTexture);
			painter.fillRect(rect,new QColor(0, 0, 0,(int)floor.getTile(p).getDust()));
		}
        
    }
	
	
	
	
}
