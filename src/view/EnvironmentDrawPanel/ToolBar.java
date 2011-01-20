package view.EnvironmentDrawPanel;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;

import vacuumAgent.VATile.VATileStatus;
/**
 * @author Claudia, Brunino
 * 
 */
@SuppressWarnings("serial")
class ToolBar extends JToolBar implements ActionListener {

	private final JButton[] buttons;
	private DrawPanel drawPanel;
	private JLabel energy;
	private JLabel ms;
	private JSeparator sepLabel;

	public ToolBar(DrawPanel drawPanel) {
		super("Toolbar");
		this.drawPanel = drawPanel;
		buttons = new JButton[3];
		
		energy = new JLabel(" Energy: 0");
		ms = new JLabel(" Performance measurement: 0");
		energy.setBorder(LineBorder.createBlackLineBorder());
		ms.setBorder(LineBorder.createBlackLineBorder());
		sepLabel = new JSeparator(JSeparator.HORIZONTAL);
		
		// DRAW dust
		buttons[0] = new JButton("Add dust");
		buttons[0].setActionCommand("addDust");
		buttons[0].setMargin(new Insets(0, 10, 0, 10));
		buttons[0].addActionListener(this);
		this.setFocusable(true);

		// DRAW Obstacle
		buttons[1] = new JButton("Add obstacle");
		buttons[1].setActionCommand("addObstacle");
		buttons[1].setMargin(new Insets(0, 10, 0, 10));
		buttons[1].addActionListener(this);
		this.setFocusable(true);

		

		// Move agent
		buttons[2] = new JButton("Move Agent");
		buttons[2].setActionCommand("moveAgent");
		buttons[2].setMargin(new Insets(0, 10, 0, 10));
		buttons[2].addActionListener(this);
		this.setFocusable(true);
		
		this.add(buttons[0]);
		this.add(buttons[1]);
		this.add(buttons[2]);
		this.add(energy);
		this.add(sepLabel);
		this.add(ms);
		
		this.setVisible(true);
		
		energy.setVisible(false);
		sepLabel.setVisible(false);
		ms.setVisible(false);
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if ("addDust".equals(e.getActionCommand())) {
			this.drawPanel.setElementToAdd(VATileStatus.DIRTY);
		}else if ("addObstacle".equals(e.getActionCommand())) {
			this.drawPanel.setElementToAdd(VATileStatus.BLOCK);
		}else if ("moveAgent".equals(e.getActionCommand())) {
			this.drawPanel.setElementToAdd(VATileStatus.UNDEFINED);
		}
	}
	
	public void hideButton(){
		for (JButton b : buttons) {
			b.setVisible(false);
		}
		energy.setVisible(true);
		sepLabel.setVisible(true);
		ms.setVisible(true);
	}
	public void showButton(){
		for (JButton b : buttons) {
			b.setVisible(true);
		}
		energy.setVisible(false);
		sepLabel.setVisible(false);
		ms.setVisible(false);
	}

	public void setEnPerformance(int energy, double performancem) {
		this.energy.setText(" Energy: " + Integer.toString(energy));
		this.ms.setText(" Performance measurement: "+Double.toString(performancem));
	}
	
	

}
