package eventNotifier;

public abstract class Event {

	protected Object publisher;
	
	protected Event( Object publisher ){
		this.publisher = publisher;
	}
	
	public Object getPublisher(){
		return publisher;
	}
	
}
