package commandsFactory;

public class CategoryAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CategoryAlreadyExistsException(String msg) {
		super( msg );
	}

}
