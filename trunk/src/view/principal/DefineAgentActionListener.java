package view.principal;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;
import util.constants.Constants;
import vacuumAgent.environment.VAEnvNonObservable;
import vacuumAgent.environment.VAEnvObservable;
import vacuumAgent.environment.VAEnvSemiObservable;
import exception.InvalidValuesException;

public class DefineAgentActionListener implements ActionListener {

    Main principalFrame;

    public DefineAgentActionListener(Main principalFrame) {
	super();
	this.principalFrame = principalFrame;
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	final JFrame frame = new JFrame(Constants.TITLE);
	frame.setResizable(false);
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.setSize(230, 150);
	frame.setLocation(300, 300);

	final JPanel panel = new JPanel();

	panel.setLayout(new GridLayout(3, 2));

	final JLabel energyLabel = new JLabel(Constants.ENERGYLABEL);
	panel.add(energyLabel);

	final JTextField energy = new JTextField();
	panel.add(energy);

	final JLabel environmentTypeLabel = new JLabel(Constants.TAILSNUMBER);
	panel.add(environmentTypeLabel);

	final JComboBox environmentType = new JComboBox();
	environmentType.addItem("Full Observable");
	environmentType.addItem("Semi Observable");
	environmentType.addItem("Non Observable");
	panel.add(environmentType);

	final JButton confirm = new JButton(Constants.OK);
	confirm.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		final String energyText = energy.getText();
		try {
		    final int energyNumber = Integer.parseInt(energyText);
		    final int index = environmentType.getSelectedIndex();
		    setAgentAction(energyNumber, index);
		    frame.dispose();
		} catch (final NumberFormatException ex) {
		    JOptionPane.showMessageDialog(null, Constants.ERRORONNUMBER, Constants.ERROR, JOptionPane.ERROR_MESSAGE);
		} catch (final Exception ex) {
		    ex.printStackTrace();
		    JOptionPane.showMessageDialog(null, ex.toString(), Constants.ERROR, JOptionPane.ERROR_MESSAGE);
		}
	    }
	});

	panel.add(confirm);

	frame.setContentPane(panel);
    }

    public void setAgentAction(int energyNumber, int index) throws Exception {
	if (energyNumber < 1 || energyNumber > 10000) {
	    throw new InvalidValuesException();
	}

	principalFrame.getEnvironment().getVacuumAgent().setEnergy(energyNumber);

	switch (index) {
	case 0:
	    final VAEnvObservable stateObservable = new VAEnvObservable(principalFrame.getEnvironment().getVacuumAgent(), principalFrame.getEnvironment()
		    .getVacuumAgentPosition(), principalFrame.getEnvironment().getFloor());
	    principalFrame.setEnvironment(stateObservable);
	    break;
	case 1:
	    final VAEnvSemiObservable stateSemiObservable = new VAEnvSemiObservable(principalFrame.getEnvironment().getVacuumAgent(), principalFrame
		    .getEnvironment().getVacuumAgentPosition(), principalFrame.getEnvironment().getFloor());
	    principalFrame.setEnvironment(stateSemiObservable);
	    break;
	case 2:
	    final VAEnvNonObservable stateNonObservable = new VAEnvNonObservable(principalFrame.getEnvironment().getVacuumAgent(), principalFrame
		    .getEnvironment().getVacuumAgentPosition(), principalFrame.getEnvironment().getFloor());
	    principalFrame.setEnvironment(stateNonObservable);
	    break;
	default:
	    throw new Exception();
	}

    }

}
