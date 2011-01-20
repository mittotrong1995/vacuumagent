package view.EnvironmentDrawPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import vacuumAgent.VATile;
import vacuumAgent.VATile.VATileStatus;
import vacuumAgent.environment.VAEnvironment;

/**
 * @author Claudia, Brunino
 * 
 */
@SuppressWarnings("serial")
class DrawPanel extends JPanel implements MouseListener, KeyListener,
		MouseMotionListener {
	private VAEnvironment environment;

	private BufferedImage buffer;
	private Graphics2D gbuffer;
	private boolean clickButton1;

	// IMAGE
	static private BufferedImage tileTexture;
	static private BufferedImage wall;
	static private BufferedImage powder;
	static private BufferedImage robotTexture;

	private VATileStatus elementToAdd;
	private Point mousePosition;
	private ArrayList<Point> elementsToAdd;

	private int camXpos;
	private int camYpos;
	private int dimCell;

	public DrawPanel(VAEnvironment environment) {
		super();
		loadImage();
		this.environment = environment;

		elementsToAdd = new ArrayList<Point>();
		this.addMouseListener(this);
		this.addKeyListener(this);
		this.addMouseMotionListener(this);
		this.setFocusable(true);
		this.camXpos = 0;
		this.camYpos = 0;
		this.dimCell = 60;
		this.clickButton1 = false;
		elementToAdd = null;
		mousePosition = null;
	}

	private void loadImage() {
		try {
			tileTexture = ImageIO.read(new File("img/tile.jpg"));
			wall = ImageIO.read(new File("img/crate.png"));
			powder = ImageIO.read(new File("img/dust.png"));
			robotTexture = ImageIO.read(new File("img/robot.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		buffer = new BufferedImage(this.getWidth(), this.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		gbuffer = buffer.createGraphics();
		this.draw(gbuffer, camYpos / dimCell, camXpos / dimCell,
				this.getWidth() / dimCell, this.getHeight() / dimCell);
		g.drawImage(buffer, 0, 0, null);
	}

	public void draw(Graphics2D g, int rowinit, int colinit, int w, int h) {
		int size = this.environment.getFloor().getSize();
		int endCol = colinit + w;
		int endRow = rowinit + h;
		if (endCol >= size)
			endCol = size - 1;
		if (endRow >= size)
			endRow = size - 1;

		for (int i = rowinit; i <= endRow; ++i) {
			for (int j = colinit; j <= endCol; ++j) {
				Point matrixPos = new Point(i, j);
				VATile tile = environment.getFloor().getTile(matrixPos);
				int r = (i - rowinit) * dimCell;
				int c = (j - colinit) * dimCell;

				g.drawImage(tileTexture, c, r, dimCell, dimCell, null);
				if (tile.getStatus() == VATileStatus.BLOCK)
					g.drawImage(wall, c, r, dimCell, dimCell, null);
				else if (tile.getStatus() == VATileStatus.DIRTY)
					g.drawImage(powder, c, r, dimCell, dimCell, null);
			}
		}
		Point agentPosition = this.environment.getVacuumAgentPosition();

		if (elementToAdd == null || elementToAdd != VATileStatus.UNDEFINED)
			g.drawImage(robotTexture, (agentPosition.y - colinit) * dimCell,
					(agentPosition.x - rowinit) * dimCell, dimCell, dimCell,
					null);

		if (elementToAdd != null && mousePosition != null) {
			if (elementToAdd == VATileStatus.BLOCK)
				g.drawImage(wall, mousePosition.x + 10, mousePosition.y + 15,
						50, 50, null);
			else if (elementToAdd == VATileStatus.DIRTY)
				g.drawImage(powder, mousePosition.x + 10, mousePosition.y + 15,
						50, 50, null);
			else if (elementToAdd == VATileStatus.UNDEFINED)
				g.drawImage(robotTexture, mousePosition.x + 10,
						mousePosition.y + 15, 50, 50, null);
		}
	}

	public VATileStatus getElementToAdd() {
		return elementToAdd;
	}

	public void setElementToAdd(VATileStatus elementToAdd) {
		this.elementToAdd = elementToAdd;
	}

	public void addToFloor(int row, int col) {
		if (elementToAdd != null) {
			int sizefloor = this.environment.getFloor().getSize();

			Point pos = new Point(row, col);
			if (row >= 0 && row < sizefloor && col >= 0 && col < sizefloor) {
				if (elementToAdd != VATileStatus.UNDEFINED && !environment.getVacuumAgentPosition().equals(pos))
					this.environment.getFloor().getTile(pos)
							.setStatus(elementToAdd);
				else if (elementToAdd == VATileStatus.UNDEFINED
						&& environment.getFloor().getTile(pos).getStatus() != VATileStatus.BLOCK) {
					this.environment.setVacuumAgentPosition(pos);
					elementToAdd = null;
				}
				this.repaint();
			}

		}

	}

	public void removeElement(int row, int col) {
		int sizefloor = this.environment.getFloor().getSize();
		if (row >= 0 && row < sizefloor && col >= 0 && col < sizefloor) {
			this.environment.getFloor().getTile(new Point(row, col))
					.setStatus(VATileStatus.CLEAN);
			this.repaint();
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		this.requestFocus();
		switch (e.getKeyCode()) {

		case KeyEvent.VK_UP:
			if (camYpos - dimCell >= 0) {
				camYpos -= dimCell;
				this.repaint();
			} else {
				camYpos = 0;
				this.repaint();
			}
			break;
		case KeyEvent.VK_DOWN:
			camYpos += dimCell;
			this.repaint();
			break;
		case KeyEvent.VK_RIGHT:
			camXpos += dimCell;
			this.repaint();
			break;
		case KeyEvent.VK_LEFT:

			if (camXpos - dimCell >= 0) {
				camXpos -= dimCell;
				this.repaint();
			} else {
				camXpos = 0;
				this.repaint();
			}
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int col = (e.getPoint().x + camXpos) / dimCell;
		int row = (e.getPoint().y + camYpos) / dimCell;
		// System.out.println("col = "+ col + " row = " + row + " " +
		// e.getPoint().toString() + " camX " + camXpos + " camy " + camYpos +
		// " zoom " + dimCell + "c " + (double)(e.getPoint().x+camXpos) /
		// dimCell + " r " + (e.getPoint().y+camYpos) / dimCell);
		if (e.getButton() == MouseEvent.BUTTON1) {
			this.addToFloor(row, col);
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			this.removeElement(row, col);
		}

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		this.requestFocus();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getButton() == MouseEvent.BUTTON1)
			clickButton1 = true;

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)
			clickButton1 = false;
			for(Point p: elementsToAdd){
				this.addToFloor(p.x, p.y);
				
			}
		
		elementsToAdd.clear();

	}

	@Override
	public void mouseDragged(MouseEvent e) {
			if(clickButton1){
			int col = (e.getPoint().x + camXpos) / dimCell;
			int row = (e.getPoint().y + camYpos) / dimCell;
			int sizefloor = this.environment.getFloor().getSize();
			if (row >= 0 && row < sizefloor && col >= 0 && col < sizefloor) {
				if (elementToAdd != VATileStatus.UNDEFINED) {
					elementsToAdd.add(new Point(row, col));
				} else if (elementToAdd == VATileStatus.UNDEFINED)
					elementsToAdd.clear();
			}
			}
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (elementToAdd != null) {
			this.mousePosition = e.getPoint();
			this.repaint();
		}
	}

}
