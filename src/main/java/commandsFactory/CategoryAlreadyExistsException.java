package commandsFactory;

/**
 * Oznacza, że dana kategoria już istnieje
 */
public class CategoryAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public CategoryAlreadyExistsException(String msg) {
		super( msg );
	}

}
