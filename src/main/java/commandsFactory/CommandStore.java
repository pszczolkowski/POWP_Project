package commandsFactory;

import java.util.List;

import edu.iis.powp.command.IPlotterCommand;

public class CommandStore {

	private static final String ROOT_CATEGORY_NAME = "all";
	private static final String DEFAULT_CATEGORY_NAME = "general";
	
	private CommandStore instance = null;
	private CommandCategory defaultCategory;
	private CommandCategory rootCategory;
	
	private CommandStore(){
		// TODO
		// odczyt pliku albo oddelegować go na potem
		
		// TODO po odczycie z pliku zmodyfkować linię niżej
		rootCategory = new CommandCategory( ROOT_CATEGORY_NAME );
		
		defaultCategory = findCategory( DEFAULT_CATEGORY_NAME );
		if( defaultCategory == null )
			defaultCategory = addCategory( DEFAULT_CATEGORY_NAME , null );
	}

	public CommandStore getInstance(){
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
		
		if( category.addCommand( name , command ) )
			throw new CommandAlreadyExistsException( "command " + name + " already exists" );
	}
	
	public IPlotterCommand get( String commandName ){
		IPlotterCommand foundCommand = rootCategory.findCommand( commandName );
		
		try {
			if( foundCommand != null )
			foundCommand = foundCommand.clone();
		} catch (CloneNotSupportedException e) {}
		
		return foundCommand;
	}
	
	public CommandCategory addCategory(String categoryName, CommandCategory parent) {
		if( findCategory( categoryName ) != null )
			throw new CategoryAlreadyExistsException( "category " + categoryName + " already exists" );
		
		CommandCategory createdCategory = new CommandCategory( categoryName );
		if( parent != null )
			parent.addSubcategory( createdCategory );
		
		return createdCategory;
	}
	
	public List< CommandCategory > getRootCategories(){
		return rootCategory.getSubcategories();
	}
	}

}
