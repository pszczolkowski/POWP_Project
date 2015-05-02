package commandsFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Zarządca kategorii poleceń. Zarządca zostaje utworzony przez magazyn poleceń
 * i jest z nim związany. Umożliwia tworzenie nowych kategorii oraz zarządzanie
 * istniejącymi. Wszystkie operacje na kategoriach zostają odzwierciedlone na
 * magazynie poleceń. Każda kategoria posiada kategorię rodzica. Kategoria
 * najwyższego poziomu, która nie posiada rodzica, nazywa się kategorią
 * korzenia.
 */
public class CategoryManager implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String ROOT_CATEGORY_NAME = "all";
	private static final String DEFAULT_CATEGORY_NAME = "general";
	
	private CommandCategory rootCategory;
	private CommandCategory defaultCategory;
	
	CategoryManager(){
		rootCategory = new CommandCategory( ROOT_CATEGORY_NAME );
		defaultCategory = add( DEFAULT_CATEGORY_NAME , null );
	}
	
	/**
	 * Dodaje nową kategorię o podanej nazwie. Nazwa musi być unikalna wśród
	 * wszystkich kategorii, niezależnie na jakim poziomie zagnieżdżenia się
	 * znajdują. Kategoria zostaje dodana bezpośrednio do kategorii korzenia.
	 * 
	 * @param name
	 *            nazwa kategorii do dodania.
	 * @return dodana kategoria
	 * @throws CategoryAlreadyExistsException
	 *             jeżeli kategoria o podanej nazwie już istnieje
	 */
	public CommandCategory add( String name ){
		return add( name , getRootCategory() );
	}

	/**
	 * Dodaje nową kategorię o podanej nazwie. Nazwa musi być unikalna wśród
	 * wszystkich kategorii, niezależnie na jakim poziomie zagnieżdżenia się
	 * znajdują. Kategoria zostaje dodana jako podkategoria podanej kategorii
	 * rodzica.
	 * 
	 * @param name
	 *            nazwa kategorii do dodania.
	 * @return dodana kategoria
	 * @throws CategoryAlreadyExistsException
	 *             jeżeli kategoria o podanej nazwie już istnieje
	 * @throws CategoryDoesntExistException
	 *             jeżeli podana kategoria rodzica nie istnieje w magazynie
	 */
	public CommandCategory add( String name , CommandCategory parent ){
		if( find( name ) != null )
			throw new CategoryAlreadyExistsException( "category " + name + " already exists" );
		
		CommandCategory createdCategory = new CommandCategory( name );
		if( parent != null ){
			if( ! exists( parent ) )
				throw new CategoryDoesntExistException( "parent category " + parent.getName() + " doeasn't exist" );
				
			parent.addSubcategory( createdCategory );
		}
		else
			rootCategory.addSubcategory( createdCategory );
		
		return createdCategory;
	}
	
	/**
	 * Odnajduje kategorię o podanej nazwie w drzewie kategorii, niezależnie od
	 * poziomu zagnieżdżenia, na jakim może się znaleźć. Jeżeli kategoria o
	 * podanej nazwie istnieje w magazynie, na pewno zostanie odnaleziona.
	 * Jeżeli nie istnieje, zostanie zwrócony null.
	 * 
	 * @param categoryName
	 *            nazwa kategorii do odnalezienia
	 * @return kategoria o podanej nazwie albo null, jeżeli taka kategoria nie
	 *         istnieje
	 */
	public CommandCategory find( String categoryName ){
		if( rootCategory.getName() == categoryName )
			return rootCategory;
		else
			return rootCategory.findSubcategory( categoryName );
	}

	/**
	 * Pobiera listę wszystkich kategorii, niezależnie od poziomu zagnieżdżenia
	 * na jakim się znajdują. Zawiera również kategorię korzenia
	 * 
	 * @return lista wszystkich kategorii
	 */
	public List< CommandCategory > getAllCategories(){
		List< CommandCategory > allCategories = new ArrayList<>();
		
		allCategories.add( rootCategory );
		allCategories.addAll( rootCategory.getAllSubcategories() );
		
		return allCategories;
	}

	/**
	 * Pobiera domyślną kategorię
	 * 
	 * @return domyślna kategoria
	 */
	public CommandCategory getDefaultCategory() {
		return defaultCategory;
	}
	
	/**
	 * Pobiera listę bezpośrednich podkategorii kategorii korzenia.
	 * 
	 * @return lista bezpośrednich podkategorii kategorii korzenia
	 */
	public List< CommandCategory > getRootCategories(){
		return rootCategory.getSubcategories();
	}
	
	/**
	 * Sprawdza czy istnieje podana kategoria. Sprawdzenie jest niezależne od
	 * poziomu zagnieżdżenia na jakim podana kategoria się znajduje.
	 * 
	 * @param category
	 *            kategoria
	 * @return true jeżeli podana kategoria istnieje, false jeżeli nie
	 */
	public boolean exists( CommandCategory category ){
		if( category.equals( rootCategory ) )
			return true;
		else
			return rootCategory.containsSubcategory( category );
	}
	
	/**
	 * Usuwa wszystkie kategorie. Operacja ta jest nieodwracalna.
	 */
	void clear() {
		rootCategory = new CommandCategory( ROOT_CATEGORY_NAME );
		defaultCategory = add( DEFAULT_CATEGORY_NAME , null );
	}
	
	/**
	 * Pobiera kategorię korzenia.
	 * 
	 * @return kategoria korzenia
	 */
	public CommandCategory getRootCategory(){
		return rootCategory;
	}
	
}
