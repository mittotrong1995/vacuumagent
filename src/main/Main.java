package main;

import java.awt.Point;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import tildeTeam.TLDAgent;
import util.constants.Constants;
import vacuumAgent.VAAction;
import vacuumAgent.VAAgent;
import vacuumAgent.VAPercept;
import vacuumAgent.VAAction.VAActionType;
import vacuumAgent.VATile.VATileStatus;
import vacuumAgent.environment.VAEnvNonObservable;
import vacuumAgent.environment.VAEnvObservable;
import vacuumAgent.environment.VAEnvSemiObservable;
import vacuumAgent.environment.VAEnvironment;
import view.principal.GenerateMap;
import view.principal.GenerateRandomlyActionListener;
import view.principal.OpenFileChooserActionListener;
import view.principal.PrincipalPanel;
import framework.Action;
import framework.Percept;

public class Main extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JMenuItem load = new JMenuItem( Constants.OPEN );
	JMenuItem save = new JMenuItem( Constants.SAVE );
	JMenuItem generateRandomly = new JMenuItem( Constants.GENERATERANDOMLY );
	JMenuItem generateMap = new JMenuItem( Constants.GENERATEMAP );
	JMenuItem start = new JMenuItem( Constants.START );
	JMenuItem moveOneStep = new JMenuItem( Constants.MOVEONESTEP );
	
	VAEnvironment environment;
	
	public Main( VAEnvironment environment ) {
		// TODO Auto-generated constructor stub
		super( Constants.TITLE );
		
		this.environment = environment;

		PrincipalPanel panel = new PrincipalPanel( this );

		JMenuBar menuBar = new JMenuBar();
		JMenu file = new JMenu( "File" );
		
		load.addActionListener( new OpenFileChooserActionListener( this ) );
		save.setEnabled( false );
		
		file.add( load );
		file.add( save );

		JMenu generate = new JMenu( "Generate" );
				
		generateRandomly.addActionListener( new GenerateRandomlyActionListener( this ) );
		generateMap.addActionListener( new GenerateMap( this ) );
		
		generate.add( generateRandomly );
		generate.add( generateMap );		

		JMenu action = new JMenu( "Action" );
		
		start.setEnabled( false );
		moveOneStep.setEnabled( false );
		
		action.add( start );
		action.add( moveOneStep );
		
		menuBar.add( file );
		menuBar.add( generate );
		menuBar.add( action );
		this.setContentPane( panel );
		this.setResizable( false );
		this.setVisible( true );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		this.setJMenuBar( menuBar );
		this.setSize( 300, 200 );		
		this.setLocation( 300, 300 );
	}
	
	public JMenuItem getLoad() {
		return load;
	}

	public void setLoad( JMenuItem load ) {
		this.load = load;
	}

	public JMenuItem getSave() {
		return save;
	}

	public void setSave( JMenuItem save ) {
		this.save = save;
	}

	public JMenuItem getGenerateRandomly() {
		return generateRandomly;
	}

	public void setGenerateRandomly( JMenuItem generateRandomly ) {
		this.generateRandomly = generateRandomly;
	}

	public JMenuItem getGenerateMap() {
		return generateMap;
	}

	public void setGenerateMap( JMenuItem generateMap ) {
		this.generateMap = generateMap;
	}

	public VAEnvironment getEnvironment() {
		return environment;
	}

	public void setEnvironment( VAEnvironment environment ) {
		this.environment = environment;
	}

	public JMenuItem getStart() {
		return start;
	}

	public void setStart( JMenuItem start ) {
		this.start = start;
	}

	public JMenuItem getMoveOneStep() {
		return moveOneStep;
	}

	public void setMoveOneStep( JMenuItem moveOneStep ) {
		this.moveOneStep = moveOneStep;
	}

	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		Point point = new Point( 0, 0 );
		
		TLDAgent a = new TLDAgent(10000);
		
		/*VAAgent a = new VAAgent(1000) {
			@Override
			public Action execute(Percept percept) {
				// TODO Auto-generated method stub
				VAPercept p = (VAPercept) percept;
				VATileStatus status = p.getCurrentTileStatus();
				
				VAAction a = new VAAction(VAActionType.SUCK);
				if( status == VATileStatus.DIRTY )
				{					
					a = new VAAction(VAActionType.SUCK);
				}
				else
				{
					Random r = new Random();
					int i = r.nextInt(4);
					
					switch( i )
					{
						case 0:						
							if( p.getNeighborhood().eastIsFree() )
							{
								a = new VAAction(VAActionType.MOVEEAST);
							}
							break;
						case 1:
							if( p.getNeighborhood().northIsFree() )
							{
								a = new VAAction(VAActionType.MOVENORTH);
							}
							break;
						case 2:
							if( p.getNeighborhood().southIsFree())
							{
								a = new VAAction(VAActionType.MOVESOUTH);
							}
							break;
						case 3:
							if( p.getNeighborhood().westIsFree())
							{
								a = new VAAction(VAActionType.MOVEWEST);
							}
							break;
						default:
							break;
					}
				}				
				return a;
			}
		};*/
				
		VAEnvSemiObservable state = new VAEnvSemiObservable( a, point, null );
		
		new Main( state );
	}
}
