package commandsFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iis.powp.command.IPlotterCommand;

public class CommandFactory {
	
	private static final String DEFAULT_CATEGORY = "general";
	
	private Map< String , Map< String , IPlotterCommand > > commands = new HashMap<>();

	public CommandFactory() {
		read();
	}
	
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
		
		save();
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
	
	
	private void save(){
		try( ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( "commands" ) ) ) {
			out.writeObject( commands );
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void read(){
		try( ObjectInputStream in = new ObjectInputStream( new FileInputStream( "commands" ) ) ) {
			commands = (Map<String, Map<String, IPlotterCommand>>) in.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {}
	}
	
}
