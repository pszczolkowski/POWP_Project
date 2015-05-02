package commandsFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandCategory implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private List< CommandCategory > subcategories;
	
	CommandCategory(String name) {
		this.name = name;
		this.subcategories = new ArrayList<>();
	}
	
	public String getName(){
		return name;
	}

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
	
	public List< CommandCategory > getSubcategories(){
		return new ArrayList<>( subcategories );
	}
	
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

	
	boolean addSubcategory( CommandCategory category ){
		if( subcategories.contains( category ) )
			return false;
		
		subcategories.add( category );
		
		return true;
	}

	public List<CommandCategory> getAllSubcategories() {
		List< CommandCategory > allSubcategories = new ArrayList<>();
		
		allSubcategories.addAll( subcategories );
		for( CommandCategory subcategory : subcategories )
			allSubcategories.addAll( subcategory.getAllSubcategories() );
		
		return allSubcategories;
	}

}
