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

import exception.InvalidValuesException;

import main.Main;
import util.constants.Constants;
import vacuumAgent.environment.VAEnvNonObservable;
import vacuumAgent.environment.VAEnvObservable;
import vacuumAgent.environment.VAEnvSemiObservable;

public class DefineAgentActionListener implements ActionListener {

	Main principalFrame;
	
	public DefineAgentActionListener( Main principalFrame ) {
		super();
		this.principalFrame = principalFrame;
	}

	@Override
	public void actionPerformed( ActionEvent arg0 ) {
		// TODO Auto-generated method stub
		final JFrame frame = new JFrame( Constants.TITLE );
		frame.setResizable( false);
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		frame.setSize( 230, 150 );
		frame.setLocation( 300, 300 );
		
		JPanel panel = new JPanel();
		
		panel.setLayout( new GridLayout( 3, 2 ) );			
		
		JLabel energyLabel = new JLabel( Constants.ENERGYLABEL );		
		panel.add( energyLabel );
	
		final JTextField energy = new JTextField();		
		panel.add( energy );
		
		JLabel environmentTypeLabel = new JLabel( Constants.TAILSNUMBER );		
		panel.add( environmentTypeLabel );
		
		final JComboBox environmentType = new JComboBox();
		environmentType.addItem("Full Observable");
		environmentType.addItem("Semi Observable");
		environmentType.addItem("Non Observable");
		panel.add( environmentType );
		
		JButton confirm = new JButton( Constants.OK );
		confirm.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {						
				// TODO Auto-generated method stub
				String energyText = energy.getText();
				try
				{
					int energyNumber = Integer.parseInt( energyText );					
					
					int index = environmentType.getSelectedIndex();
					
					if( energyNumber < 1 || energyNumber > 10000 )
					{
						throw new InvalidValuesException();						
					}
					
					principalFrame.getEnvironment().getVacuumAgent().setEnergy( energyNumber );
					
					switch( index )
					{
						case 0:
							VAEnvObservable stateObservable = new VAEnvObservable( 
									principalFrame.getEnvironment().getVacuumAgent(),									
									principalFrame.getEnvironment().getVacuumAgentPosition(),
									principalFrame.getEnvironment().getFloor() );
							principalFrame.setEnvironment( stateObservable );
							break;
						case 1:
							VAEnvSemiObservable stateSemiObservable = new VAEnvSemiObservable( 
									 principalFrame.getEnvironment().getVacuumAgent(),
									 principalFrame.getEnvironment().getVacuumAgentPosition(),
									 principalFrame.getEnvironment().getFloor() );
							principalFrame.setEnvironment( stateSemiObservable );
							break;
						case 2:
							VAEnvNonObservable stateNonObservable = new VAEnvNonObservable( 
									 principalFrame.getEnvironment().getVacuumAgent(),
									 principalFrame.getEnvironment().getVacuumAgentPosition(),
									 principalFrame.getEnvironment().getFloor() );
							principalFrame.setEnvironment( stateNonObservable );
							break;
						default:
							throw new Exception();
					}
					
					frame.dispose();	
				}
				catch( NumberFormatException ex )
				{							
					JOptionPane.showMessageDialog( null, Constants.ERRORONNUMBER, Constants.ERROR, JOptionPane.ERROR_MESSAGE );
				}
				catch( InvalidValuesException ex )
				{
					JOptionPane.showMessageDialog( null, Constants.ERRORPARAMETERENERGY, Constants.ERROR, JOptionPane.ERROR_MESSAGE );
				}
				catch( Exception ex )
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog( null, ex.toString(), Constants.ERROR, JOptionPane.ERROR_MESSAGE );					
				}				
			}
		});
		
		panel.add( confirm );	
		
		frame.setContentPane( panel );
	}

}
