package view;

import model.Floor;

import com.trolltech.qt.core.QPoint;
import com.trolltech.qt.core.QRect;
import com.trolltech.qt.core.Qt;
import com.trolltech.qt.gui.QColor;
import com.trolltech.qt.gui.QImage;
import com.trolltech.qt.gui.QKeyEvent;
import com.trolltech.qt.gui.QMouseEvent;
import com.trolltech.qt.gui.QPaintEvent;
import com.trolltech.qt.gui.QPainter;
import com.trolltech.qt.gui.QWheelEvent;
import com.trolltech.qt.gui.QWidget;

import controller.FloorController;

public class FloorView extends QWidget{
	
	final static String TILE = "./resources/tile.jpg";
	final static int TILESIZE = 50;
	final static int MOVESPEED = 15;	
	
	Floor floor;
	FloorController floorController;
	int viewX,viewY;
	QImage tileTexture;
	boolean addingDust;
	boolean leftButton = false;
	boolean rightButton = false;
	double zoom = 1;
	
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
		if(keyEvent.key() == Qt.Key.Key_W.value()){ viewY+=MOVESPEED*zoom;}
		if(keyEvent.key() == Qt.Key.Key_S.value()){ viewY-=MOVESPEED*zoom;}
		if(keyEvent.key() == Qt.Key.Key_A.value()){ viewX+=MOVESPEED*zoom;}
		if(keyEvent.key() == Qt.Key.Key_D.value()){ viewX-=MOVESPEED*zoom;}		
		if(keyEvent.key() == Qt.Key.Key_E.value()){ addingDust = true;}		
		repaint();
	}
	
	@Override
	protected void keyReleaseEvent(QKeyEvent keyEvent) {
		super.keyReleaseEvent(keyEvent);
		if(keyEvent.key() == Qt.Key.Key_E.value()){ addingDust =  false;}
	}
	
	@Override
	protected void mouseMoveEvent(QMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		super.mouseMoveEvent(mouseEvent);
		manageMouseEvent(mouseEvent);
	}
	
	@Override
	protected void mousePressEvent(QMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		super.mousePressEvent(mouseEvent);
		if (mouseEvent.button()== Qt.MouseButton.LeftButton){leftButton = true;}
		if (mouseEvent.button()== Qt.MouseButton.RightButton){rightButton = true;}
		manageMouseEvent(mouseEvent);
		
	}
	
	@Override
	protected void mouseReleaseEvent(QMouseEvent mouseEvent) {
		// TODO Auto-generated method stub
		super.mouseReleaseEvent(mouseEvent);
		if (mouseEvent.button()== Qt.MouseButton.LeftButton){leftButton = false;}
		if (mouseEvent.button()== Qt.MouseButton.RightButton){rightButton = false;}
		manageMouseEvent(mouseEvent);
		
	}
	
	@Override
	protected void wheelEvent(QWheelEvent event) {
		// TODO Auto-generated method stub
		super.wheelEvent(event);	
		double delta = event.delta() * 0.001;
		zoom += delta;	
		if(zoom + delta <= 0.3){ delta=0; zoom = 0.3;}
		else if(zoom + delta >= 10){ delta=0; zoom = 10; }
		
		System.out.println(delta);
		viewX -= delta*TILESIZE;
		viewY -= delta*TILESIZE;
		repaint();
	}
	
	protected void manageMouseEvent(QMouseEvent mouseEvent)
	{
		int x = (int) ((mouseEvent.x() - viewX)/(TILESIZE * zoom)) ;
		int y = (int) ((mouseEvent.y() - viewY)/(TILESIZE * zoom)) ;
		
		if(mouseEvent.x() - viewX < 0){x--;}
		if(mouseEvent.y() - viewY < 0){y--;}
				
		if(!addingDust){
			if (leftButton){floorController.addTile(x, y);}
			if (rightButton){floorController.removeTile(x, y);}
		}
		else
		{
			if (leftButton){floorController.addDust(x, y);}
			if (rightButton){floorController.removeDust(x, y);}
		}
			
		repaint();
	}

	
	@Override
    protected void paintEvent(QPaintEvent event) {

        QPainter painter = new QPainter(this);
        painter.translate(viewX, viewY);
        painter.scale(zoom,zoom);
        
        for (QPoint p: floor.getTiles().keySet()) {
        	int x = p.x() * TILESIZE;
        	int y = p.y() * TILESIZE;
        	QRect rect = new QRect(new QPoint(x, y),new QPoint(x+TILESIZE, y+TILESIZE));
			painter.drawImage(rect, tileTexture);
			painter.fillRect(rect,new QColor(0, 0, 0,(int)floor.getTile(p).getDust()));
		}
        
    }
	
	
	
	
}
