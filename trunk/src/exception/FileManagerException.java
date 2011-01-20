package exception;

import java.io.IOException;

/**
 * @author Giovanna,Maria,Antonia
 *
 */
public class FileManagerException extends Exception {

	/**
	 * @param e
	 */
	public FileManagerException(IOException e) {
		super(e);
	}

	public FileManagerException(String message) {
		super(message);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
