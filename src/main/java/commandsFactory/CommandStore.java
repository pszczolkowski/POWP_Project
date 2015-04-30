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
	
	public void add( IPlotterCommand command ){
		// TODO
		// dodaje do domuślnej kategori general
		// rzuca wyjątek, jak polecenie o podanej nazwie istnieje
		// sprawdzanie czy podano nulla
	}
	
	public void add( IPlotterCommand command , CommandCategory category ){
		// TODO
		// uwagi jak wyżej
		// kategoria nie może być nullem
	}
	
	public IPlotterCommand get( String commandName ){
		// TODO
		return null;
	}
	
	public List< CommandCategory > getRootCategories(){
		// TODO
		return null;
	}

}
