package commandsFactory;

/**
 * Oznacza, że podana kategoria nie istnieje.
 */
public class CategoryDoesntExistException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CategoryDoesntExistException(String msg) {
		super( msg );
	}
	
	
}
