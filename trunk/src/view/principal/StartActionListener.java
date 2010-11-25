package view.principal;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.Main;
import vacuumAgent.environment.VAEnvironment;
import view.EnvironmentDrawPanel.FloorPanel;
import exception.VAIllegalMove;

public class StartActionListener implements ActionListener, Runnable
{

	Main frame;
	int step;
	
	public StartActionListener( Main frame, int step ) {
		// TODO Auto-generated constructor stub
		this.frame = frame;
		this.step = step;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		FloorPanel floor = ( FloorPanel ) frame.getContentPane();
		floor.setEditable( false );
		frame.getSave().setEnabled( false );
		Thread t = new Thread( this );
		t.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		VAEnvironment environment = frame.getEnvironment();
		try 
		{
			if( step == 0 )
			{
				while( !environment.isDone() )
				{
					environment.step( 1 );
					frame.getContentPane().repaint();
				}
			}
			else
			{
				environment.step( step );
				frame.getContentPane().repaint();
			}
		} catch ( VAIllegalMove e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
}
