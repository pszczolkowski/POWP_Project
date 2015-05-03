package eventNotifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		Set<Subscriber> subscibers = findSubscribersFor( event );
		inform(subscibers , event);
	}
	
	public EventService subscribe( Class< ? extends Event > event , Subscriber subscriber ){
		List<Subscriber> subscibers = subscriptions.get( event );
		
		if( subscibers == null ){
			subscibers = new ArrayList<>();
			subscriptions.put( event , subscibers );
		}
		
		subscibers.add( subscriber );
		
		return this;
	}
	
	
	private Set<Subscriber> findSubscribersFor(Event event) {
		Set<Subscriber> subscibers = new HashSet<>();
		for( Class< ? extends Event > eventType : subscriptions.keySet() ){
			if( eventType.isInstance( event ) ){
				subscibers.addAll( subscriptions.get( eventType ) );
			}
		}
		return subscibers;
	}
	
	private void inform( Set<Subscriber> subscibers , Event event ) {
		for( Subscriber subscriber : subscibers ){
			if( subscriber == event.getPublisher() )
				continue;
				
			subscriber.inform( event );
		}
	}
	
}
