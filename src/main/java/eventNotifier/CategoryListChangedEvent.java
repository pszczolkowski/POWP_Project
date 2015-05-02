package eventNotifier;

public class CategoryListChangedEvent implements Event {

	private Object publisher;
	
	public CategoryListChangedEvent(Object publisher) {
		this.publisher = publisher;
	}

	@Override
	public Class<? extends Event> getType() {
		return getClass();
	}

	@Override
	public Object getPublisher() {
		return publisher;
	}

}
