package eventNotifier;

import commandsFactory.CommandCategory;

public class CommandAddedEvent extends CommandsListEvent {

	private String commandName;
	private CommandCategory category;
	
	public CommandAddedEvent(Object publisher, String commandName,
			CommandCategory category) {
		super( publisher );
		this.commandName = commandName;
		this.category = category;
	}

	public String getCommandName() {
		return commandName;
	}

	public CommandCategory getCategory() {
		return category;
	}

}
