package vacuumAgent;

import framework.EnvironmentObject;

/**
 * A floor's tile.
 */
public class VATile implements EnvironmentObject {

	/**
	 * The Enum VATileStatus.
	 */
	public enum VATileStatus {

		CLEAN, DIRTY, BLOCK, UNDEFINED
	}

	/** The tile's status. */
	VATileStatus status;

	/**
	 * Instantiates a new vA tile.
	 */
	public VATile() {
		super();
		status = VATileStatus.UNDEFINED;
	}

	/**
	 * Instantiates a new vA tile.
	 * 
	 * @param status
	 *            the tile's status
	 */
	public VATile(VATileStatus status) {
		super();
		this.status = status;
	}

	/**
	 * Gets the tile's status.
	 * 
	 * @return the status
	 */
	public VATileStatus getStatus() {
		return status;
	}

	/**
	 * Sets the tile's status.
	 * 
	 * @param status
	 *            the new tile's status
	 */
	public void setStatus(VATileStatus status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VATile other = (VATile) obj;
		if (status != other.status)
			return false;
		return true;
	}

}
