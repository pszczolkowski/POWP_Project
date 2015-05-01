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
	private static final String ROOT_CATEGORY_NAME = "all";
	private static final String DEFAULT_CATEGORY_NAME = "general";
	
	private static CommandStore instance = null;
	private CommandCategory defaultCategory;
	private CommandCategory rootCategory;
	
	private CommandStore(){
		rootCategory = new CommandCategory( ROOT_CATEGORY_NAME );
		defaultCategory = addCategory( DEFAULT_CATEGORY_NAME , null );
	}

	public static CommandStore getInstance(){
		if( instance == null )
			instance = new CommandStore();
		
		return instance; 
	}
	
	public void add( String name , IPlotterCommand command ){
		add( name , command , defaultCategory );
	}
	
	public void add( String name , IPlotterCommand command , CommandCategory category ){
		if( command == null )
			throw new IllegalArgumentException( "command cannot be null" );
		
		if( ! categoryExists( category ) )
			throw new CategoryDoesntExistException( "category " + category.getName() + " doesn't exist" );
		
		if( ! category.addCommand( name , command ) )
			throw new CommandAlreadyExistsException( "command " + name + " already exists" );
	}
	
	public IPlotterCommand get( String commandName ){
		return rootCategory.findCommand( commandName );
	}
	
	public List< IPlotterCommand > getCommandsOfCategory( CommandCategory category ){
		return category.getCommands();
	}
	
	public boolean contains( String commandName ){
		return rootCategory.findCommand( commandName ) != null;
	}
	
	public CommandCategory addCategory(String categoryName, CommandCategory parent) {
		if( findCategory( categoryName ) != null )
			throw new CategoryAlreadyExistsException( "category " + categoryName + " already exists" );
		
		CommandCategory createdCategory = new CommandCategory( categoryName );
		if( parent != null )
			parent.addSubcategory( createdCategory );
		else
			rootCategory.addSubcategory( createdCategory );
		
		return createdCategory;
	}
	
	public CommandCategory findCategory( String name ){
		return rootCategory.findSubcategory( name );
	}
	
	public boolean categoryExists( CommandCategory category ){
		return rootCategory.findSubcategory( category ) != null;
	}
	
	public List< CommandCategory > getRootCategories(){
		return rootCategory.getSubcategories();
	}

	public void clear(){
		rootCategory = new CommandCategory( ROOT_CATEGORY_NAME );
		defaultCategory = addCategory( DEFAULT_CATEGORY_NAME , null );
	}
	
	public void exportToFile( String fileName ){
		File folder = new File( COMMANDS_PARENT_PATH );
		folder.mkdirs();
		
		try( ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( new File( folder , fileName ) ) ) ) {
			out.writeObject( rootCategory );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void importFromFile( String fileName ) throws FileNotFoundException {		
		try( ObjectInputStream in = new ObjectInputStream( new FileInputStream( new File( COMMANDS_PARENT_PATH , fileName ) ) ) ) {
			rootCategory = (CommandCategory) in.readObject();
			defaultCategory = rootCategory.findSubcategory( DEFAULT_CATEGORY_NAME );
			if( defaultCategory == null )
				defaultCategory = addCategory( DEFAULT_CATEGORY_NAME , null );
		} catch( FileNotFoundException e ){
			throw e;
		}catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
