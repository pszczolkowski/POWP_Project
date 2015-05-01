package edu.iis.powp.commandsFactory;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.*;

import java.io.FileNotFoundException;

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
		CommandCategory addedCategory = categoryManager.add( "testowa2" , null );
		CommandCategory addedSubcategory = categoryManager.add( "subtestowa2" , addedCategory );
		
		assertThat( categoryManager.find( "testowa2" ) , is( equalTo( addedCategory ) ) );
		assertThat( categoryManager.find( "subtestowa2" ) , is( equalTo( addedSubcategory ) ) );
		assertThat( addedCategory.findSubcategory( "subtestowa2" ) , is( equalTo( addedSubcategory ) ) );
	}
	
	@Test
	public void addCategoriesTree_andOneCommand_shouldFindThatCommand(){
		IPlotterCommand command = new CommandBuilder()
		.setPosition(0, 0)
		.drawLineTo(100, 50)
		.build();
		
		CommandCategory addedCategory = categoryManager.add( "testowa3" , null );
		CommandCategory addedSubcategory = categoryManager.add( "subtestowa3" , addedCategory );
		
		store.add( "test3" , command, addedSubcategory);
		
		assertThat( store.get("test3") , is( equalTo( command ) ));
		assertThat( store.get("test3") , is( not( sameInstance( command ) ) ));
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

}
