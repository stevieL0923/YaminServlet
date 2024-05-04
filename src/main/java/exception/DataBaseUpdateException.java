package exception;

public class DataBaseUpdateException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataBaseUpdateException() {
		super();
	}
	
	public DataBaseUpdateException(String message) {
		super(message);
		
		System.out.println("Problem updating data in database:");
		System.out.println(message);
	}
}

