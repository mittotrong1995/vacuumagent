package view.principal;


import javax.swing.JButton;
import javax.swing.JPanel;

import main.Main;
import util.constants.Constants;

public class PrincipalPanel extends JPanel {

  /**
   * 
   */
	private static final long serialVersionUID = 1L;
	
	Main frame;
	
	public PrincipalPanel( Main frame ) 
	{
		super();
		this.frame = frame;
		createPanel();		
	}

	private void createPanel() 
	{		
		JButton generateMapRandomly = new JButton( Constants.GENERATERANDOMLY );
		JButton generateMap = new JButton( Constants.GENERATEMAP );
		
		generateMapRandomly.addActionListener( new GenerateRandomlyActionListener( frame ) );
		generateMap.addActionListener( new GenerateMap( frame ) );			
		
		this.add( generateMapRandomly );
		this.add( generateMap );
	}	
}