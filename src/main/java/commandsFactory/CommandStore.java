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

/**
 * Magazyn służący do przechowywania poleceń plotera {@link IPlotterCommand}.
 * Każde przechowywane polecenie posiada przyporządkowaną unikalną nazwę, dzięki
 * której możliwe jest odwołanie się do danego polecenia. Polecenia pogrupowane
 * są w kategorie, do zarządzania którymi służy zarządca kategorii
 * {@link CategoryManager}. Magazyn posiada związanego ze sobą zarządcę
 * kategorii. Zawartośc magazynu można wyeksportować do pliku oraz zaimportować
 * zapisane wcześniej polecenia.
 */
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

	/**
	 * Zwraca obiekt {@link CommandStore}. Może istnieć tylko jeden taki obiekt
	 * dlatego konstruktor jest prywatny i tworzeniem obiektów zajmuje się
	 * jedynie ta metoda.
	 * 
	 * @return istniejący obiekt lub nowy, jeżeli jeszcze żaden nie został
	 *         utworzony
	 */
	public static CommandStore getInstance(){
		if( instance == null )
			instance = new CommandStore();
		
		return instance; 
	}
	
	/**
	 * Dodaje polecenie z podaną nazwą. Może istnieć tylko jedno polecenie z
	 * daną nazwą. W przypadku próby dodania polecenia z istniejącą nazwą,
	 * zostaje rzucony wyjątek {@link CommandAlreadyExistsException}. Przypisuje
	 * polecenie do domyślnej kategorii .
	 * 
	 * @param name
	 *            nazwa polecenia, po której będzie rozpoznawane
	 * @param command
	 *            polecenie do dodania do magazynu
	 * @throws CommandAlreadyExistsException
	 *             jeżeli polecenie o podanej nazwie już istnieje
	 */
	public void add( String name , IPlotterCommand command ){
		add( name , command , categoryManager.getDefaultCategory() );
	}
	
	/**
	 * Dodaje polecenie z podaną nazwą. Może istnieć tylko jedno polecenie z
	 * daną nazwą. W przypadku próby dodania polecenia z istniejącą nazwą,
	 * zostaje rzucony wyjątek {@link CommandAlreadyExistsException}. Podana
	 * kategoria musi istnieć w magazynie. .
	 * 
	 * @param name
	 *            nazwa polecenia, po której będzie rozpoznawane
	 * @param command
	 *            polecenie do dodania do magazynu
	 * @param category
	 *            kategoria do której ma zostać przypisane polecenie
	 * @throws CommandAlreadyExistsException
	 *             jeżeli polecenie o podanej nazwie już istnieje
	 * @throws CategoryDoesntExistException
	 *             jeżeli podana kategoria nie istnieje w magazynie
	 */
	public void add( String name , IPlotterCommand command , CommandCategory category ){
		if( command == null )
			throw new IllegalArgumentException( "command cannot be null" );
		
		if( ! categoryManager.exists( category ) )
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

	/**
	 * Pobiera polecenie o podanej nazwie z magazynu. Polecenie pobierane jest
	 * niezależnie od kategorii, do której zostało przypisane. Jeżeli polecenie
	 * nie istnieje zwraca null. Zwrócone polecenie jest kopią przechowywanego,
	 * dzięki czemu dokonane na nim zmiany nie mają wpływu na polecenia wewnątrz
	 * magazynu.
	 * 
	 * @param commandName
	 *            nazwa polecenia do pobrania
	 * @return
	 */
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
	
	/**
	 * Pobiera listę poleceń, których nazwy zawierają podany ciąg znaków.
	 * Polecenia pobierane są niezależnie od kategorii, do których zostały
	 * przypisane.
	 * 
	 * @param nameLike
	 *            ciąg znaków do wyszukania w nazwach poleceń
	 * @return lista poleceń, których nazwy zawierają podany ciąg znaków
	 */
	public List< IPlotterCommand > getCommandsNamedLike( String nameLike ){
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
	
	/**
	 * Pobiera listę poleceń przypisaną do podanej kategorii. Lista zawiera
	 * kopie przechowywanych poleceń
	 * 
	 * @param category
	 *            kategoria, której polecenia mają zostać pobrane
	 * @return lista poleceń przypisanych do podanej kategorii
	 * @throws CategoryDoesntExistException
	 *             jeżeli podana kategoria nie istnieje w magazynie
	 */
	public List< IPlotterCommand > getCommandsOfCategory( CommandCategory category ){
		if( ! categoryManager.exists( category ) )
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
	
	/**
	 * Pobiera nazwy wszystkich przechowywnych poleceń, niezależnie od kategorii
	 * w jakich się znajdują. Dana nazwa występuje tylko raz.
	 * 
	 * @return lista nazw przechowywanych poleceń
	 */
	public List< String > getCommandsNames(){
		List< String > names = new ArrayList<>();
		
		for( Map< String , IPlotterCommand > categoryCommands : commands.values() ){
			names.addAll( categoryCommands.keySet() );
		}
		
		return names;
	}
	
	/**
	 * Pobiera listę nazw poleceń przypisanych do podanej kategorii. Dana nazwa
	 * występuje tylko raz.
	 * 
	 * @param category
	 *            kategoria, której nazwy poleceń mają zostać pobrane
	 * @return lista nazw poleceń przypisanych do podanej kategorii
	 * @throws CategoryDoesntExistException
	 *             jeżeli podana kategoria nie istnieje
	 */
	public List< String > getCommandsNamesOfCategory( CommandCategory category ){
		if( ! categoryManager.exists( category ) )
			throw new CategoryDoesntExistException( "category " + category.getName() + " doeasn't exist" );
		
		List< String > names = new ArrayList<>();
		
		Map<String, IPlotterCommand> categoryCommands = commands.get( category );
		if( categoryCommands != null )
			names.addAll( categoryCommands.keySet() );
		
		return names;
	}
	
	/**
	 * Pobiera listę nazw przechowywanych poleceń, które zawierają podany ciąg
	 * znaków. Nazwy poleceń pobierane są niezależnie od kategorii, do jakiej
	 * przypisane są polecenia. Dana nazwa występuje tylko raz.
	 * 
	 * @param nameLike
	 *            ciąg znaków do wyszukania w nazwach poleceń
	 * @return lista nazw poleceń zawierających podany ciąg znaków
	 */
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
	
	// TODO javadoc
	public void rename( String currentName , String newName ){
		if ( contains( currentName ) ){
			IPlotterCommand command = null;
			
			for( Map< String , IPlotterCommand > categoryCommands : commands.values() ){
				command = categoryCommands.get( currentName );
				
				if( command != null ){
					categoryCommands.remove( currentName );
					categoryCommands.put( newName , command );
					break;
				}
			}
		}
	}
	
	// TODO javadoc
	public IPlotterCommand remove( String name ){
		if( contains( name ) ){
			for( Map< String , IPlotterCommand > categoryCommands : commands.values() ){
				if( categoryCommands.containsKey( name ) ){
					return categoryCommands.remove( name );
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Sprawdza czy w magazynie znajduje się polecenie o podanej nazwie. Może
	 * istnieć tylko jedno polecenie o podanej nazwie.
	 * 
	 * @param commandName
	 *            nazwa polecenia do sprawdzenia
	 * @return true jeżeli polecenie o podanej nazwie istnieje, fale jeżeli nie
	 *         istnieje
	 */
	public boolean contains( String commandName ){
		return get( commandName ) != null;
	}

	/**
	 * Pobiera zarządcę kategorii {@link CategoryManager}, związanego z
	 * magazynem.
	 * 
	 * @return zarządca kategorii
	 */
	public CategoryManager getCategoryManager() {
		return categoryManager;
	}
	
	/**
	 * Czyści magazyn - usuwa wszystkie przechowywane polecenia oraz kategorie.
	 * Jest to niedowracalna operacja. Czyszczenie powinno być wykonywane
	 * jedynie w koniecznych sytuacjach, najlepiej po uprzednim wyeksportowaniu
	 * przechowywanych poleceń do pliku.
	 */
	public void clear(){
		categoryManager.clear();
		commands.clear();
	}
	
	/**
	 * Eksportuje przechowywane polecenia do pliku o podanej nazwie. Jeżeli plik
	 * z poleceniami o podanej nazwie już istnieje, zostaje nadpisany.
	 * 
	 * @param fileName
	 *            nazwa pliku, do którego zostaną wyeksportowane przechowywane
	 *            polecenia
	 */
	public void exportToFile( String fileName ){
		File folder = new File( COMMANDS_PARENT_PATH );
		folder.mkdirs();
		
		try( ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( new File( folder , fileName ) ) ) ) {
			out.writeObject( this );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Importuje zestaw poleceń z pliku o podanej nazwie. Obecnie przechowywane
	 * polecenia oraz kategorie zostają bezpowrotnie utracone.
	 * 
	 * @param fileName
	 *            nazwa pliku, z którego zostaną zaimportowane polecenia
	 * @throws FileNotFoundException
	 *             jeżeli plik o podanej nazwie nie istnieje
	 */
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
