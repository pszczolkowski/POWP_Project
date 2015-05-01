package commandsFactory;

import java.io.Serializable;
import java.util.List;

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
	
	public CommandCategory add( String name ){
		return add( name , getRootCategory() );
	}

	public CommandCategory add( String name , CommandCategory parent ){
		if( find( name ) != null )
			throw new CategoryAlreadyExistsException( "category " + name + " already exists" );
		
		CommandCategory createdCategory = new CommandCategory( name );
		if( parent != null )
			parent.addSubcategory( createdCategory );
		else
			rootCategory.addSubcategory( createdCategory );
		
		return createdCategory;
	}
	
	public CommandCategory find( String categoryName ){
		return rootCategory.findSubcategory( categoryName );
	}
	
	public CommandCategory getDefaultCategory() {
		return defaultCategory;
	}
	
	public List< CommandCategory > getRootCategories(){
		return rootCategory.getSubcategories();
	}
	
	public boolean contains( CommandCategory category ){
		return rootCategory.containsSubcategory( category );
	}
	

	void clear() {
		rootCategory = new CommandCategory( ROOT_CATEGORY_NAME );
		defaultCategory = add( DEFAULT_CATEGORY_NAME , null );
	}
	
	CommandCategory getRootCategory(){
		return rootCategory;
	}
	
}
