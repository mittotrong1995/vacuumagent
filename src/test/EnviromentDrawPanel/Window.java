package test.EnviromentDrawPanel;



import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.UIManager;

import vacuumAgent.VAFloor;
import vacuumAgent.environment.VAEnvObservable;
import view.EnvironmentDrawPanel.FloorPanel;



public class Window extends JFrame{

	private static final long serialVersionUID = 1L;
	final FloorPanel floorPanel;

	
	public Window() {
		super("Hoover");
		setOptionWindow();
		VAEnvObservable state = new VAEnvObservable(null, new Point(0,2), new VAFloor(100));
		floorPanel = new FloorPanel(state);
		floorPanel.setEditable(true);
		this.add(floorPanel, BorderLayout.CENTER);
		
		this.setPreferredSize(new Dimension(300,300));
		this.pack();
		this.setVisible(true);
		
	}

	private void setOptionWindow() {
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
