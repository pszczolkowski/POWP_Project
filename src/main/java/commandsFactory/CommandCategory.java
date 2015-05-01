package commandsFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import edu.iis.powp.command.IPlotterCommand;

public class CommandCategory {

	private String name;
	private Map< String , IPlotterCommand > commands;
	private List< CommandCategory > subcategories;
	
	CommandCategory(String name) {
		this.name = name;
		this.commands = new HashMap<>();
		this.subcategories = new ArrayList<>();
	}
	
	public String getName(){
		return name;
	}

	IPlotterCommand findCommand( String name ){
		IPlotterCommand foundCommand = commands.get( name );
		
		if( foundCommand == null ){
			for( CommandCategory category : subcategories ){
				foundCommand = category.findCommand( name );
				
				if( foundCommand != null )
					break;
			}
		}
		
		return foundCommand;
	}
	
	List< IPlotterCommand > getCommands(){
		List< IPlotterCommand > result = new ArrayList<>();
		
		try {
			for( IPlotterCommand command : commands.values() )
				result.add( command.clone() );
		} catch (CloneNotSupportedException e) {}
		
		return result;
	}
	
	public List< String > getCommandsNames(){
		return new ArrayList< String >( commands.keySet() );
	}
	
	public CommandCategory findSubcategory( String name ){
		CommandCategory foundSubcategory = null;
		
		if( this.name.equals( name ) )
			return this;
		
		for( CommandCategory category : subcategories ){
			foundSubcategory = category.findSubcategory( name );
			
			if( foundSubcategory != null )
				break;
		}
		
		return foundSubcategory;
	}
	
	public List< CommandCategory > getSubcategories(){
		return new ArrayList<>( subcategories );
	}
	
	boolean addCommand( String name , IPlotterCommand command ){
		if( commands.containsKey( name ) )
			return false;
		
		commands.put( name , command );
		
		return true;
	}
	
	boolean addSubcategory( CommandCategory category ){
		if( subcategories.contains( category ) )
			return false;
		
		subcategories.add( category );
		
		return true;
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
