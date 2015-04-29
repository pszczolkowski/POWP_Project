package commandsFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.iis.powp.command.IPlotterCommand;

/**
 * Służy do przechowywania utworzonych poleceń. Każde polecenie posiada
 * przypisaną nazwę i kategorię. Dodając nowe polecenia podaje się ich
 * nazwę oraz kategorię. Aby pobrać dane polecenie, należy podać jego nazwę.
 * Można także pobrać całą listę poleceń danej kategorii, poprzez podanie
 * jej nazwy.
 */
public class CommandFactory {
	
	private static final String DEFAULT_CATEGORY = "general";
	
	private Map< String , Map< String , IPlotterCommand > > commands = new HashMap<>();

	/**
	 * Podczas tworzenia obiektu zostają wczytane zapisane w pliku polecenia
	 */
	public CommandFactory() {
		read();
	}
	
	/**
	 * Dodaje polecenie o podanej nazwie. Może istnieć tylko jedno polecenie o podanej nazwie
	 * (be rozróżniania wielkości liter). Ustawia kategorię polecenia na "general"
	 * @param name nazwa polecenia
	 * @param command polecenie do dodania
	 * @throws CommandAlreadyExistsException jeżeli polecenie o podanej nazwie już istnieje
	 * @throws IllegalArgumentException jeżeli podana nazwa lub polecenie są null
	 */
	public void add( String name , IPlotterCommand command ){
		add( name , command, DEFAULT_CATEGORY );
	}
	
	/**
	 * Dodaje polecenie o podanej nazwie. Może istnieć tylko jedno polecenie o podanej nazwie
	 * (be rozróżniania wielkości liter). 
	 * @param name nazwa polecenia
	 * @param command polecenie do dodania
	 * @param category nazwa kategorii
	 * @throws CommandAlreadyExistsException jeżeli polecenie o podanej nazwie już istnieje
	 * @throws IllegalArgumentException jeżeli podana nazwa, polecenie lub nazwa kategorii są null
	 */
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
	
	/**
	 * Pobiera polecenie o podanej nazwie. Jeżeli polecenie o takiej nazwie nie istnieje,
	 * zwraca null.
	 * @param name nazwa polecenia
	 * @return polecenie o podanej nazwie lub null, jeżeli nie ma takiego polecenia
	 */
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
	
	/**
	 * Pobiera listę wszystkich przechowywanych poleceń.
	 * @return lista przechowywanych poleceń
	 */
	public List< IPlotterCommand > getAll(){
		List< IPlotterCommand > allCommands = new ArrayList<>();
		
		for( Map< String , IPlotterCommand > categoryCommands : commands.values() ){
			allCommands.addAll( categoryCommands.values() );
		}
		
		return allCommands;
	}
	
	/**
	 * Usuwa polecenie o podanej nazwie
	 * @param name nazwa polecenia
	 * @return true jeżeli polecenie zostało usunięte, false jeżeli nie istniało
	 */
	public boolean remove( String name ){
		for (Map< String , IPlotterCommand > categoryCommands : commands.values()) {
			if( categoryCommands.remove( name ) != null ){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Sprawdza czy istnieje polecenie o podanej nazwie
	 * @param name nazwa polecenia
	 * @return true jeżeli polecenie o podanej nazwie istnieje, false jeżeli nie istnieje
	 */
	public boolean contains( String name ){
		for (Map< String , IPlotterCommand > categoryCommands : commands.values()) {
			if( categoryCommands.containsKey( name ) ){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Pobiera listę poleceń o podanej kategorii
	 * @param category nazwa kategorii
	 * @return lista poleceń z danej kategorii. Jeżeli kategoria nie istnieje, lista jest pusta
	 */
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
		} catch ( FileNotFoundException e ) {
			// ignore 
		} 
		catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {}
	}
	
}
