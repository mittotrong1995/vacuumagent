package view.principal;

import java.awt.GridLayout;
import java.awt.Point;
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
import mapGenerator.MapGenerator;
import util.constants.Constants;
import util.utility.EnableDisableButtons;
import vacuumAgent.VAFloor;
import vacuumAgent.VATile.VATileStatus;
import view.EnvironmentDrawPanel.FloorPanel;
import exception.InvalidValuesException;

public class GenerateRandomlyActionListener implements ActionListener {

	Main principalFrame;
		
	public GenerateRandomlyActionListener( Main principalFrame ) {
		super();
		this.principalFrame = principalFrame;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		final JFrame frame = new JFrame( Constants.TITLE );
		frame.setResizable( false);
		frame.setVisible( true );
		frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		frame.setSize( 270, 150 );
		frame.setLocation( 300, 300 );
		
		JPanel panel = new JPanel();
		
		panel.setLayout( new GridLayout( 5, 2 ) );			
		
		JLabel tailsNumberLabel = new JLabel( Constants.TAILSNUMBER );		
		panel.add( tailsNumberLabel );
	
		final JTextField tailsNumber = new JTextField();
		tailsNumber.setToolTipText( Constants.TOOTLTIPTAILSNUMBER );		
		panel.add( tailsNumber );
		
		JLabel dustPercentLabel = new JLabel( Constants.DUSTPERCENT );		
		panel.add( dustPercentLabel );
		
		final JTextField dustPercent = new JTextField();
		dustPercent.setToolTipText( Constants.TOOLTIPDUSTPERCENT );
		panel.add( dustPercent );
		
		JLabel wallPercentLabel = new JLabel( Constants.WALLPERCENT );
		panel.add( wallPercentLabel );				
		
		final JTextField wallPercent = new JTextField();
		wallPercent.setToolTipText( Constants.TOOLTIPWALLPERCENT );
		panel.add( wallPercent );
		
		JLabel algorithmTypeLabel = new JLabel( Constants.ALGORITHMTYPE );
		panel.add( algorithmTypeLabel );				
		
		final JComboBox algorithmType = new JComboBox();
		algorithmType.addItem( "ProgressiveCreation" );
		algorithmType.addItem( "CoinCreation" );
		algorithmType.addItem( "FullRandom" );
		panel.add( algorithmType );
		
		JButton generate = new JButton( Constants.GENERATE );
		generate.addActionListener( new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {						
				// TODO Auto-generated method stub
				String tailsNumberText = tailsNumber.getText();
				String dustPercentText = dustPercent.getText();
				String wallPercentText = wallPercent.getText();				
				try
				{
					int tails = Integer.parseInt( tailsNumberText );					
					float dust = Float.parseFloat( dustPercentText );
					float wall = Float.parseFloat( wallPercentText );
					int type = algorithmType.getSelectedIndex();
					
					if( dust > 60 || dust < 0 || wall < 0 || wall > 40 || tails <= 0 || tails > 1000 || dust+wall > 100 || type < 0 || type > 2 )
					{						
						throw new InvalidValuesException();
					}
					//MapGenerator mapGenerator = new MapGenerator();
					VAFloor floor = MapGenerator.generateFloor( tails, wall, dust, true, type );
					
					for( int i = floor.getSize() - 1; i >= 0; i-- )
					{
						for( int j = 0; j < floor.getSize(); j++ )
						{
							if( floor.getFloor()[ i ][ j ].getStatus() != VATileStatus.BLOCK )
							{
								principalFrame.getEnvironment().setVacuumAgentPosition( new Point( i, j ) );
								break;
							}
						}
					}
					
					principalFrame.getEnvironment().setFloor( floor );
					
					FloorPanel floorPanel;
										
					floorPanel = new FloorPanel( principalFrame.getEnvironment() );
					floorPanel.setEditable( true );
					principalFrame.setSize( 800, 600 );
					principalFrame.setContentPane( floorPanel );
					
					EnableDisableButtons.enableDisableButtons( principalFrame );
					
					frame.dispose();					
				}
				catch( NumberFormatException ex )
				{							
					JOptionPane.showMessageDialog( null, Constants.ERRORONNUMBER, Constants.ERROR, JOptionPane.ERROR_MESSAGE );
				}
				catch( InvalidValuesException ex )
				{
					JOptionPane.showMessageDialog( null, Constants.ERRORPARAMETERS, Constants.ERROR, JOptionPane.ERROR_MESSAGE );
				}
				catch( Exception ex )
				{
					ex.printStackTrace();
					JOptionPane.showMessageDialog( null, ex.toString(), Constants.ERROR, JOptionPane.ERROR_MESSAGE );					
				}				
			}
		});
		
		panel.add( generate );	
		
		frame.setContentPane( panel );
	}
}