package commandsFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iis.powp.command.IPlotterCommand;

public class CommandFactory {
	
	private static final String DEFAULT_CATEGORY = "general";
	
	private Map< String , Map< String , IPlotterCommand > > commands = new HashMap<>();

	public void add( String name , IPlotterCommand command ){
		add( name , command, DEFAULT_CATEGORY );
	}
	
	public void add( String name , IPlotterCommand command , String category ){
		if ( name == null )
			throw new IllegalArgumentException( "command name cannot be null" );
		if ( command == null )
			throw new IllegalArgumentException( "command cannot be null" );
		if ( category == null )
			throw new IllegalArgumentException( "category name cannot be null" );
		
		IPlotterCommand existingCommand = get( name );
		if( existingCommand != null )
			throw new CommandAlreadyExistsException( "Command \"" + name + "\" is already added" );
		
		Map<String, IPlotterCommand> categoryCommands = commands.get( category );
		if( categoryCommands == null ){
			categoryCommands = new HashMap<>();
			commands.put( category , categoryCommands );
		}
		
		categoryCommands.put( name , command );
	}
	
	public IPlotterCommand get( String name ){
		for (Map< String , IPlotterCommand > categoryCommands : commands.values()) {
			for (String existingName : categoryCommands.keySet()) {
				if( existingName.equalsIgnoreCase( name ) ){
					return categoryCommands.get( existingName );
				}
			}
		}

		return null;
	}
	
	public List< IPlotterCommand > getCommandsOf( String category ){
		return new ArrayList< IPlotterCommand >( commands.get( category ).values() );
	}
	
}
