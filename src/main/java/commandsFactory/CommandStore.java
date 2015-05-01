package commandsFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import edu.iis.powp.command.IPlotterCommand;

public class CommandStore {

	private static final String COMMANDS_PARENT_PATH = "commands";
	
	private static CommandStore instance = null;
	private CategoryManager categoryManager;
	
	private CommandStore(){
		categoryManager = new CategoryManager();
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
		
		if( ! category.addCommand( name , command ) )
			throw new CommandAlreadyExistsException( "command " + name + " already exists" );
	}
	
	public IPlotterCommand get( String commandName ){
		return categoryManager.getRootCategory().findCommand( commandName );
	}
	
	public List< IPlotterCommand > getCommandsOfCategory( CommandCategory category ){
		return category.getCommands();
	}
	
	public boolean contains( String commandName ){
		return categoryManager.getRootCategory().findCommand( commandName ) != null;
	}

	public CategoryManager getCategoryManager() {
		return categoryManager;
	}
	
	public void clear(){
		categoryManager.clear();
	}
	
	public void exportToFile( String fileName ){
		File folder = new File( COMMANDS_PARENT_PATH );
		folder.mkdirs();
		
		try( ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( new File( folder , fileName ) ) ) ) {
			out.writeObject( categoryManager );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void importFromFile( String fileName ) throws FileNotFoundException {		
		try( ObjectInputStream in = new ObjectInputStream( new FileInputStream( new File( COMMANDS_PARENT_PATH , fileName ) ) ) ) {
			categoryManager = (CategoryManager) in.readObject();
		} catch( FileNotFoundException e ){
			throw e;
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
