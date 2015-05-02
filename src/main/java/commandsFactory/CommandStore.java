package commandsFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iis.powp.command.IPlotterCommand;

public class CommandStore implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String COMMANDS_PARENT_PATH = "commands";
	
	private static CommandStore instance = null;
	
	private CategoryManager categoryManager;
	private Map< CommandCategory , Map< String , IPlotterCommand > > commands;
	
	private CommandStore(){
		categoryManager = new CategoryManager();
		commands = new HashMap<>();
	}

	public static CommandStore getInstance(){
		if( instance == null )
			instance = new CommandStore();
		
		return instance; 
	}
	
	public void add( String name , IPlotterCommand command ){
		add( name , command , categoryManager.getDefaultCategory() );
	}
	
	public void add( String name , IPlotterCommand command , CommandCategory category ){
		if( command == null )
			throw new IllegalArgumentException( "command cannot be null" );
		
		if( ! categoryManager.contains( category ) )
			throw new CategoryDoesntExistException( "category " + category.getName() + " doesn't exist" );
		
		if( contains( name ) )
			throw new CommandAlreadyExistsException( "command " + name + " already exists" );
		
		Map< String , IPlotterCommand > categoryCommands = commands.get( category );
		if( categoryCommands == null ){
			categoryCommands = new HashMap<>();
			commands.put( category , categoryCommands);
		}
		
		categoryCommands.put( name , command );
	}

	public IPlotterCommand get( String commandName ){
		IPlotterCommand foundCommand = null;
		
		for( Map< String , IPlotterCommand > categoryCommands : commands.values() ){
			if( categoryCommands.containsKey( commandName ) ){
				foundCommand = categoryCommands.get( commandName );
				break;
			}
		}
		
		try {
			if( foundCommand != null )
				foundCommand = foundCommand.clone();
		} catch (CloneNotSupportedException e) {}
		
		return foundCommand;
	}
	
	public List< IPlotterCommand > getNamedLike( String nameLike ){
		List< IPlotterCommand > foundCommands = new ArrayList<>();
		
		try {
			for( Map< String , IPlotterCommand > categoryCommands : commands.values() ){
				for( String name : categoryCommands.keySet() ){
					if( name.contains( nameLike ) ){
						foundCommands.add( categoryCommands.get( name ).clone() );
					}
				}
			}
		} catch (CloneNotSupportedException e) {}
		
		return foundCommands;
	}
	
	public List< IPlotterCommand > getCommandsOfCategory( CommandCategory category ){
		if( ! categoryManager.contains( category ) )
			throw new CategoryDoesntExistException( "category " + category.getName() + " doesn't exist" );
		
		List< IPlotterCommand > result = new ArrayList<>();
		
		Map<String, IPlotterCommand> categoryCommands = commands.get( category );
		if( categoryCommands != null ){
			try {
				for( IPlotterCommand command : categoryCommands.values() ){
					result.add( command.clone() );
				}
			} catch (CloneNotSupportedException e) {}
		}
		
		return result;
	}
	
	public List< String > getCommandsNames(){
		List< String > names = new ArrayList<>();
		
		for( Map< String , IPlotterCommand > categoryCommands : commands.values() ){
			names.addAll( categoryCommands.keySet() );
		}
		
		return names;
	}
	
	public List< String > getCommandsNamesOfCategory( CommandCategory category ){
		if( ! categoryManager.contains( category ) )
			throw new CategoryDoesntExistException( "category " + category.getName() + " doeasn't exist" );
		
		List< String > names = new ArrayList<>();
		
		Map<String, IPlotterCommand> categoryCommands = commands.get( category );
		if( categoryCommands != null )
			names.addAll( categoryCommands.keySet() );
		
		return names;
	}
	
	public List< String > getCommandsNamesLike( String nameLike ){
		List< String > foundNAmes = new ArrayList<>();
		
		for( Map< String , IPlotterCommand > categoryCommands : commands.values() ){
			for( String name : categoryCommands.keySet() ){
				if( name.contains( nameLike ) ){
					foundNAmes.add( name );
				}
			}
		}
		
		return foundNAmes;
	}
	
	public boolean contains( String commandName ){
		return get( commandName ) != null;
	}

	public CategoryManager getCategoryManager() {
		return categoryManager;
	}
	
	public void clear(){
		categoryManager.clear();
		commands.clear();
	}
	
	public void exportToFile( String fileName ){
		File folder = new File( COMMANDS_PARENT_PATH );
		folder.mkdirs();
		
		try( ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( new File( folder , fileName ) ) ) ) {
			out.writeObject( this );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void importFromFile( String fileName ) throws FileNotFoundException {		
		try( ObjectInputStream in = new ObjectInputStream( new FileInputStream( new File( COMMANDS_PARENT_PATH , fileName ) ) ) ) {
			CommandStore readStore = (CommandStore) in.readObject();
			categoryManager = readStore.categoryManager;
			commands = readStore.commands;
		} catch( FileNotFoundException e ){
			throw e;
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
