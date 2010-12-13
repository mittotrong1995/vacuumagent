package exception;

public class NegativeNumberException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param e
	 */
	public NegativeNumberException(Exception e) {
		super(e);
	}

	public NegativeNumberException(String message) {
		super(message);
	}
}
