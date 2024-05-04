package exception;

public class DataBaseDeleteException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataBaseDeleteException() {
		super();
	}
	
	public DataBaseDeleteException(String message) {
		super(message);
		
		System.out.println("Problem deleting data in database:");
		System.out.println(message);
	}
}

