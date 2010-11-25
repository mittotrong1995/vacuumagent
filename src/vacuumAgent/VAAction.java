package vacuumAgent;

import framework.Action;

// TODO: Auto-generated Javadoc
/**
 * The Class VAAction.
 */
public class VAAction implements Action {
	
	/**
	 * The Enum VAActionType.
	 */
	public enum VAActionType {
		
		SUCK, 
 MOVENORTH, 
 MOVESOUTH, 
 MOVEWEST, 
 MOVEEAST
	}

	/** The action type. */
	VAActionType actionType;

	/**
	 * Instantiates a new vA action.
	 *
	 * @param actionType the action type
	 */
	public VAAction(VAActionType actionType) {
		super();
		this.actionType = actionType;
	}

	/**
	 * Gets the action type.
	 *
	 * @return the action type
	 */
	public VAActionType getActionType() {
		return actionType;
	}

	/**
	 * Sets the action type.
	 *
	 * @param actionType the new action type
	 */
	public void setActionType(VAActionType actionType) {
		this.actionType = actionType;
	}

	/* (non-Javadoc)
	 * @see framework.Action#isNoOp()
	 */
	@Override
	public boolean isNoOp() {
		return false;
	}

}
