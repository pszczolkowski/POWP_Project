package eventNotifier;

public class CommandsListChangedEvent implements Event {

	private Object publisher;

	public CommandsListChangedEvent(Object publisher) {
		this.publisher = publisher;
	}

	@Override
	public Class< ? extends Event > getType() {
		return getClass();
	}

	@Override
	public Object getPublisher() {
		return publisher;
	}
	
}
