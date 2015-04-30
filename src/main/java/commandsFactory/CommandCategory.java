package commandsFactory;

import java.util.List;
import java.util.Objects;

import edu.iis.powp.command.IPlotterCommand;

public class CommandCategory {

	private String name;
	
	CommandCategory(String name) {
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	IPlotterCommand findCommand( String name ){
		// TODO
		return null;
	}
	
	List< IPlotterCommand > getCommands(){
		// TODO
		return null;
	}
	
	List< String > getCommandNames(){
		// TODO
		return null;
	}
	
	public CommandCategory findSubcategory( String name ){
		// TODO
		return null;
	}
	
	public List< CommandCategory > getSubcategories(){
		// TODO 
		return null;
	}
	
	boolean addCommand( String name , IPlotterCommand command ){
		// TODO
		// tutaj następuje sprawdzanie, czy dana komenda istnieje
		// i zwrócone zostanie true / false
		// wyjątek należy rzucać poziom abstrakcji wyżej
		return false;
	}
	
	boolean addSubcategory( CommandCategory category ){
		// TODO
		// ma sprawdzać czy dana kategoria tam już czasem nie istnieje
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash( name );
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if( obj instanceof CommandCategory ){
			CommandCategory other = (CommandCategory) obj;
			
			return Objects.equals( name , other.name );
		}else
			return false;
	}
	
	

}
