package commandsFactory;

public class CommandAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CommandAlreadyExistsException( String msg ) {
		super( msg );
	}

}
