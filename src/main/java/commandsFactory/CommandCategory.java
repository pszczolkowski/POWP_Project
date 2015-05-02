package commandsFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.iis.powp.command.IPlotterCommand;

/**
 * Reprezentuje kategorię polecenia plotera {@link IPlotterCommand} w magazynie
 * poleceń {@link CommandStore}. Do zarządzania kategoriami służy zarządca
 * kategorii {@link CategoryManager}. W magazynie poleceń każda kategoria musi
 * być unikalna (posiadać unikalną nazwę).
 * <p>
 * Obiekty {@link CommandCategory} są niemutowalne.
 */
public class CommandCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private List< CommandCategory > subcategories;
	
	CommandCategory(String name) {
		this.name = name;
		this.subcategories = new ArrayList<>();
	}
	
	/**
	 * Pobiera nazwę kategorii
	 * 
	 * @return nazwa kategorii
	 */
	public String getName(){
		return name;
	}

	/**
	 * Odnajduje podkategorię o podanej nazwie spośód wszystkich podkategorii,
	 * niezależnie od poziomu zagnieżdżenia. Jeżeli kategori o podanej nazwie
	 * istnieje wśród podkategorii, na pewno zostanie odnaleziona. Jeżeli nie
	 * istnieje, zwracany jest null.
	 * 
	 * @param name
	 *            nazwa kategorii do odnalezienia
	 * @return kategoria o podanej nazwie albo null, jeżeli taka kategoria nie
	 *         istnieje
	 */
	public CommandCategory findSubcategory( String name ){
		CommandCategory foundSubcategory = null;
		
		for( CommandCategory category : subcategories ){
			if( category.name.equals( name ) )
				foundSubcategory = category;
			else
				foundSubcategory = category.findSubcategory( name );
			
			if( foundSubcategory != null )
				break;
		}
		
		return foundSubcategory;
	}
	
	/**
	 * Pobiera listę bezpośrednich podkategorii.
	 * 
	 * @return lista bezpośrednich podkategorii
	 */
	public List< CommandCategory > getSubcategories(){
		return new ArrayList<>( subcategories );
	}
	
	/**
	 * Pobiera listę wszystkich podkategorii niezależnie od poziomu
	 * zagnieżdżenia.
	 * 
	 * @return lista wszystkich podkategorii
	 */
	public List<CommandCategory> getAllSubcategories() {
		List< CommandCategory > allSubcategories = new ArrayList<>();
		
		allSubcategories.addAll( subcategories );
		for( CommandCategory subcategory : subcategories )
			allSubcategories.addAll( subcategory.getAllSubcategories() );
		
		return allSubcategories;
	}
	
	/**
	 * Sprawdza czy kategoria posiada podaną podkategorię. Sprawdzenie następuje
	 * nieznależnie od poziomu zagnieżdżenia.
	 * 
	 * @param searchCategory
	 *            kategoria do srpawdzenia
	 * @return true jeżeli kategoria posiada podaną podkategorię albo false,
	 *         jeżeli nie posiada
	 */
	public boolean containsSubcategory(CommandCategory searchCategory) {
		for( CommandCategory category : subcategories ){
			if( category == searchCategory )
				return true;
			else if( category.containsSubcategory( searchCategory ) )
				return true;
		}
		
		return false;
	}	
	
	@Override
	public int hashCode() {
		return Objects.hash( name );
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if( obj instanceof CommandCategory ){
			CommandCategory other = (CommandCategory) obj;
			
			return Objects.equals( name , other.name );
		}else
			return false;
	}

	/**
	 * Dodaje podaną kategorię jako podkategorię.
	 * 
	 * @param category
	 *            podkategoria do dodania
	 * @return true jeżeli podkategoria zostanie dodana albo false, jeżeli
	 *         podana kategoria już istnieje
	 */
	boolean addSubcategory( CommandCategory category ){
		if( subcategories.contains( category ) )
			return false;
		
		subcategories.add( category );
		
		return true;
	}

}
