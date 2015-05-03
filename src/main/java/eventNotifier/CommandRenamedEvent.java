package eventNotifier;

public class CommandRenamedEvent extends CommandsListEvent {

	private String commandOldName;
	private String commandCurrentName;

	public CommandRenamedEvent(Object publisher , String commandOldName , String commandCurrentName ) {
		super(publisher);
		this.commandOldName = commandOldName;
		this.commandCurrentName = commandCurrentName;
	}

	public String getCommandOldName() {
		return commandOldName;
	}

	public String getCommandCurrentName() {
		return commandCurrentName;
	}

}
