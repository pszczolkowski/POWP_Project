package commandsFactory;

/**
 * Oznacza, że kategoria zawiera polecenia. Rzucany w przypadku próby usunięcia
 * takiej kategorii.
 */
public class CategoryNotEmptyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CategoryNotEmptyException(String msg) {
		super( msg );
	}

}
