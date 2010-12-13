package vacuumAgent;

import framework.Action;
import framework.Agent;
import framework.Percept;

public abstract class VAAgent implements Agent{
	
	protected int energy;
	boolean alive;
	
	public VAAgent(int energy) {
		super();
		this.energy = energy;
		this.alive = true;
	}
	
	

	public int getEnergy() {
		return energy;
	}



	public void setEnergy(int energy) {
		this.energy = energy;
	}
	
	public void spendEnergy()
	{
		this.energy--;
	}



	@Override
	public abstract Action execute(Percept percept);	

	@Override
	public boolean isAlive(){
		return this.alive;
	}

	@Override
	public void setAlive(boolean alive){
		this.alive = alive;
	}

}
