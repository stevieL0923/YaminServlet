package exception;

public class DataBaseInsertException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataBaseInsertException() {
		super();
	}
	
	public DataBaseInsertException(String message) {
		super(message);
		
		System.out.println("Problem adding data to database:");
		System.out.println(message);
	}
}

