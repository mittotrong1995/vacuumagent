package view.EnvironmentDrawPanel;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import vacuumAgent.environment.VAEnvironment;

public class FloorPanel extends JPanel{

	/**
	 * @author Claudia, Brunino
	 * La classe crea il pannello su cui viene disegnato l'ambiente
	 */
	private static final long serialVersionUID = 1L;
	
	final private ToolBar toolbar;
	final private DrawPanel drawPanel;
	
	private boolean editable;
	
	/**
	 * Costruttore. Crea il pannello.
	 * @param environment VAEnvironment
	 */
	public FloorPanel(final VAEnvironment environment) {
		super(new BorderLayout());
		
		drawPanel = new DrawPanel(environment);
		toolbar = new ToolBar(drawPanel);
		editable = true;
		this.addComponent();
		this.setVisible(true);
	}
	
	private void addComponent() {
		this.add(drawPanel, BorderLayout.CENTER);
		this.add(toolbar,BorderLayout.NORTH);
	}

	/**
	 * Restituisce true se il pannello è editabile, false altrimenti.
	 * @return boolean
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Setta il pannello editabile.
	 * @param editable boolean
	 */
	public void setEditable(boolean editable) {
		if(editable){
			this.editable = editable;
			drawPanel.addMouseListener(drawPanel);
			drawPanel.addMouseMotionListener(drawPanel);
			drawPanel.addKeyListener(drawPanel);
			
			drawPanel.setFocusable(true);
			drawPanel.setElementToAdd(null);
		}else{
			toolbar.setVisible(false);
			drawPanel.removeMouseListener(drawPanel);
			drawPanel.removeMouseMotionListener(drawPanel);
			drawPanel.setElementToAdd(null);
		}
	}
	
	
}
