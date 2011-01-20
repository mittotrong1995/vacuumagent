package view.principal;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Main;
import util.constants.Constants;
import util.utility.EnableDisableButtons;
import vacuumAgent.VAFloor;
import vacuumAgent.environment.VAEnvironment;
import view.EnvironmentDrawPanel.FloorPanel;
import exception.NegativeNumberException;

public class GenerateMap implements ActionListener {

	Main principalFrame;
		
	public GenerateMap( Main principalFrame ) 
	{
		super();
		this.principalFrame = principalFrame;		
	}


	@Override 
	public void actionPerformed(ActionEvent arg0) {
		
		final JFrame frame = new JFrame( Constants.TITLE );
		frame.setResizable( false);
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		frame.setSize( 230, 150 );
		frame.setLocation( 300, 300 );
		
		JPanel panel = new JPanel();
		
		panel.setLayout( new GridLayout( 2, 2 ) );			
		
		JLabel tailsNumberLabel = new JLabel( Constants.TAILSNUMBER );
		panel.add( tailsNumberLabel );
	
		final JTextField tailsNumber = new JTextField();
		panel.add( tailsNumber );
		
		JButton generate = new JButton( Constants.GENERATE );
		
		generate.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {						
				String tailsNumberText = tailsNumber.getText();				
				
				try
				{
					int tails = Integer.parseInt( tailsNumberText );
					
					if( tails <= 0 )
						throw new NegativeNumberException( Constants.ERRORNEGATIVENUMBERS);
					
					VAEnvironment environment = principalFrame.getEnvironment();
					environment.setFloor( new VAFloor( tails ) );
					
					FloorPanel floorPanel;
												
					floorPanel = new FloorPanel( environment );
					floorPanel.setEditable( true );
					
					principalFrame.setSize( 800, 600 );
					principalFrame.setContentPane( floorPanel );
					
					EnableDisableButtons.enableDisableButtons( principalFrame );
					
					frame.dispose();
				}
				catch( NumberFormatException ex )
				{							
					JOptionPane.showMessageDialog( null, Constants.ERRORONNUMBER, Constants.ERROR, JOptionPane.ERROR_MESSAGE );
				} catch ( NegativeNumberException e ) {
					JOptionPane.showMessageDialog( null, Constants.ERRORNEGATIVENUMBERS, Constants.ERROR, JOptionPane.ERROR_MESSAGE );
				}
				
			}
		});
		
		panel.add( generate );	
		
		frame.setContentPane( panel );	
	}
}