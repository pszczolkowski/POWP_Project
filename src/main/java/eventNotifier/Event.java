package eventNotifier;

public interface Event {

	Class< ? extends Event > getType();
	Object getPublisher();
	
}
