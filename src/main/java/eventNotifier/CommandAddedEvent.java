package eventNotifier;

import commandsFactory.CommandCategory;

public class CommandAddedEvent implements Event {

	private Object publisher;
	private String commandName;
	private CommandCategory category;
	
	public CommandAddedEvent(Object publisher, String commandName,
			CommandCategory category) {
		this.publisher = publisher;
		this.commandName = commandName;
		this.category = category;
	}

	@Override
	public Class<? extends Event> getType() {
		return getClass();
	}

	@Override
	public Object getPublisher() {
		return publisher;
	}

	public String getCommandName() {
		return commandName;
	}

	public CommandCategory getCategory() {
		return category;
	}

}
