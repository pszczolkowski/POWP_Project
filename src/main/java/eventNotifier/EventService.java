package eventNotifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventService {

	private static EventService instance = null;
	
	private Map< Class< ? extends Event > , List< Subscriber > > subscriptions;
	
	private EventService(){
		subscriptions = new HashMap<>();
	}
	
	public static EventService getInstance(){
		if( instance == null )
			instance = new EventService();
		
		return instance;
	}
	
	public void publish( Event event ){
		List<Subscriber> subscibers = subscriptions.get( event.getClass() );
		
		if( subscibers != null ){
			for( Subscriber subscriber : subscibers )
				subscriber.inform( event );
		}
	}
	
	public void subscribe( Class< ? extends Event > event , Subscriber subscriber ){
		List<Subscriber> subscibers = subscriptions.get( event );
		
		if( subscibers == null ){
			subscibers = new ArrayList<>();
			subscriptions.put( event , subscibers );
		}
		
		subscibers.add( subscriber );
	}
	
}
