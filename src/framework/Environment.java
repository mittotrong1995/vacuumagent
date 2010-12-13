package framework;

import exception.VAIllegalMove;

/**
 * An abstract description of possible discrete Environments in which Agent(s) 
 * can perceive and act.
 *
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface Environment {
	/**
	 * Move the Environment one time step forward.
	 * 
	 * @throws VAIllegalMove
	 */
	void step() throws VAIllegalMove;

	/**
	 * Move the Environment n time steps forward.
	 * 
	 * @param n
	 *            the number of time steps to move the Environment forward.
	 * @throws VAIllegalMove
	 */
	void step(int n) throws VAIllegalMove;

	/**
	 * Step through time steps until the Environment has no more tasks.
	 * 
	 * @throws VAIllegalMove
	 */
	void stepUntilDone() throws VAIllegalMove;

	/**
	 * 
	 * @return if the Environment is finished with its current task(s).
	 */
	boolean isDone();

	/**
	 * Retrieve the performance measure associated with an Agent.
	 * 
	 * @param forAgent
	 *            the Agent for which a performance measure is to be retrieved.
	 * @return the performance measure associated with the Agent.
	 */
	double getPerformanceMeasure();
}
