package commandsFactory;

public class CategoryNotEmptyException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CategoryNotEmptyException(String msg) {
		super( msg );
	}

}
