package eventNotifier;

public class CommandRemovedEvent extends CommandsListEvent {
	
	private String commandName;

	public CommandRemovedEvent(Object publisher , String commandName ) {
		super(publisher);
		this.commandName = commandName;
	}
	
	public String getCommandName(){
		return this.commandName;
	}

}
