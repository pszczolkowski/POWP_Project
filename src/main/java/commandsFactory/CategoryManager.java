package commandsFactory;

import java.io.Serializable;
import java.util.ArrayList;
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
		if( parent != null ){
			if( ! contains( parent ) )
				throw new CategoryDoesntExistException( "parent category " + parent.getName() + " doeasn't exist" );
				
			parent.addSubcategory( createdCategory );
		}
		else
			rootCategory.addSubcategory( createdCategory );
		
		return createdCategory;
	}
	
	public CommandCategory find( String categoryName ){
		if( rootCategory.getName() == categoryName )
			return rootCategory;
		else
			return rootCategory.findSubcategory( categoryName );
	}
	
	public List< CommandCategory > getAllCategories(){
		List< CommandCategory > allCategories = new ArrayList<>();
		
		allCategories.add( rootCategory );
		allCategories.addAll( rootCategory.getAllSubcategories() );
		
		return allCategories;
	}
	
	public CommandCategory getDefaultCategory() {
		return defaultCategory;
	}
	
	public List< CommandCategory > getRootCategories(){
		return rootCategory.getSubcategories();
	}
	
	public boolean contains( CommandCategory category ){
		if( category.equals( rootCategory ) )
			return true;
		else
			return rootCategory.containsSubcategory( category );
	}
	

	void clear() {
		rootCategory = new CommandCategory( ROOT_CATEGORY_NAME );
		defaultCategory = add( DEFAULT_CATEGORY_NAME , null );
	}
	
	public CommandCategory getRootCategory(){
		return rootCategory;
	}
	
}
