package edu.iis.powp.commandsFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import commandsFactory.CommandBuilder;
import commandsFactory.CommandCategory;
import commandsFactory.CommandStore;
import edu.iis.powp.command.IPlotterCommand;

public class CommandStoreTest {

	@Test
	public void addSingleCommand_storeShouldContainThatCommand_expectedClone() {
		CommandStore store = CommandStore.getInstance();
		
		IPlotterCommand command = new CommandBuilder()
			.setPosition(0, 0)
			.drawLineTo(100, 50)
			.build();
		
		store.add( "test" , command);
		
		assertTrue( "store should contain added command" , store.contains( "test" ) );
		assertThat( store.get( "test" ) , is( equalTo( command ) ) );
		assertThat( store.get( "test" ) , is( not( sameInstance( command ) ) ) );
	}
	
	@Test
	public void addSingleCategory_storeShouldContainThatCategory(){
		CommandStore store = CommandStore.getInstance();
		
		CommandCategory addedCategory = store.addCategory( "testowa" , null );
		
		assertThat( addedCategory , is( notNullValue() ) );
		assertThat( store.findCategory( "testowa" ) , is( notNullValue() ) );
	}
	
	@Test
	public void addCategoriesTree_shouldContainAllOfThem(){
		CommandStore store = CommandStore.getInstance();
		
		CommandCategory addedCategory = store.addCategory( "testowa2" , null );
		CommandCategory addedSubcategory = store.addCategory( "subtestowa2" , addedCategory );
		
		assertThat( store.findCategory( "testowa2" ) , is( equalTo( addedCategory ) ) );
		assertThat( store.findCategory( "subtestowa2" ) , is( equalTo( addedSubcategory ) ) );
		assertThat( addedCategory.findSubcategory( "subtestowa2" ) , is( equalTo( addedSubcategory ) ) );
	}
	
	@Test
	public void addCategoriesTree_andOneCommand_shouldFindThatCommand(){
		CommandStore store = CommandStore.getInstance();
		
		IPlotterCommand command = new CommandBuilder()
		.setPosition(0, 0)
		.drawLineTo(100, 50)
		.build();
		
		CommandCategory addedCategory = store.addCategory( "testowa3" , null );
		CommandCategory addedSubcategory = store.addCategory( "subtestowa3" , addedCategory );
		
		store.add( "test3" , command, addedSubcategory);
		
		assertThat( store.get("test3") , is( equalTo( command ) ));
		assertThat( store.get("test3") , is( not( sameInstance( command ) ) ));
	}

}
