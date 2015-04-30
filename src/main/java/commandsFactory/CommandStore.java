package commandsFactory;

import java.util.List;

import edu.iis.powp.command.IPlotterCommand;

public class CommandStore {

	private CommandStore instance = null;
	
	private CommandStore(){
		// TODO
		// odczyt pliku albo oddelegować go na potem
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
