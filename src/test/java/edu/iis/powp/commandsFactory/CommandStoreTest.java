package edu.iis.powp.commandsFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Test;

import commandsFactory.CategoryManager;
import commandsFactory.CommandBuilder;
import commandsFactory.CommandCategory;
import commandsFactory.CommandStore;

import edu.iis.powp.command.IPlotterCommand;

public class CommandStoreTest {
	
	private CommandStore store = CommandStore.getInstance();
	private CategoryManager categoryManager = store.getCategoryManager();
	
	@After
	public void tearDown(){
		store.clear();
	}
	
	@Test
	public void addSingleCommand_storeShouldContainThatCommand_expectedClone() {
		IPlotterCommand command = new CommandBuilder()
			.setPosition(0, 0)
			.drawLineTo(100, 50)
			.build();
		
		store.add( "test" , command );
		
		assertTrue( "store should contain added command" , store.contains( "test" ) );
		assertThat( store.get( "test" ) , is( equalTo( command ) ) );
		assertThat( store.get( "test" ) , is( not( sameInstance( command ) ) ) );
	}
	
	@Test
	public void addSingleCategory_storeShouldContainThatCategory(){
		CommandCategory addedCategory = categoryManager.add( "testowa" , null );
		
		assertThat( addedCategory , is( notNullValue() ) );
		assertThat( categoryManager.find( "testowa" ) , is( notNullValue() ) );
	}
	
	@Test
	public void addCategoriesTree_shouldContainAllOfThem(){
		CommandCategory addedCategory = categoryManager.add( "testowa" , null );
		CommandCategory addedSubcategory = categoryManager.add( "subtestowa" , addedCategory );
		
		assertThat( categoryManager.find( "testowa" ) , is( equalTo( addedCategory ) ) );
		assertThat( categoryManager.find( "subtestowa" ) , is( equalTo( addedSubcategory ) ) );
		assertThat( addedCategory.findSubcategory( "subtestowa" ) , is( equalTo( addedSubcategory ) ) );
	}
	
	@Test
	public void addCategoriesTree_andOneCommand_shouldFindThatCommand(){
		IPlotterCommand command = new CommandBuilder()
		.setPosition(0, 0)
		.drawLineTo(100, 50)
		.build();
		
		CommandCategory addedCategory = categoryManager.add( "testowa" , null );
		CommandCategory addedSubcategory = categoryManager.add( "subtestowa" , addedCategory );
		
		store.add( "test" , command, addedSubcategory);
		
		assertThat( store.get("test") , is( equalTo( command ) ));
		assertThat( store.get("test") , is( not( sameInstance( command ) ) ));
	}
	
	@Test
	public void serializationTest(){
		IPlotterCommand command = new CommandBuilder()
		.setPosition(0, 0)
		.drawLineTo(100, 50)
		.build();
	
		store.add( "test" , command);
		
		store.exportToFile( "test" );
		store.clear();
		try {
			store.importFromFile( "test" );
		} catch (FileNotFoundException e) {
			fail( "file with exported commands doesn't exist" );
		}
		
		assertTrue( "store should contain added command" , store.contains( "test" ) );
		assertThat( store.get( "test" ) , is( equalTo( command ) ) );
		assertThat( store.get( "test" ) , is( not( sameInstance( command ) ) ) );
	}
	
	@Test
	public void addCategoriesWithSubcategories_getAllCategoriesShouldReturnThemAll(){
		CommandCategory category1 = categoryManager.add( "test1" );
		CommandCategory category2 = categoryManager.add( "test2" , category1 );
		CommandCategory category3 = categoryManager.add( "test3" , category1 );
		CommandCategory category4 = categoryManager.add( "test4" , category2 );
		
		Set< CommandCategory > addedCategories = new HashSet<>();
		addedCategories.add( categoryManager.getRootCategory() );
		addedCategories.add( categoryManager.getDefaultCategory() );
		addedCategories.add( category1 );
		addedCategories.add( category1 );
		addedCategories.add( category2 );
		addedCategories.add( category3 );
		addedCategories.add( category4 );
		
		Set< CommandCategory > loadedCategories = new HashSet<>( categoryManager.getAllCategories() );
		
		assertThat( loadedCategories , is( equalTo( addedCategories ) ));
	}

}
