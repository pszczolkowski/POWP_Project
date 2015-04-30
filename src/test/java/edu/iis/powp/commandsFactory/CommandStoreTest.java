package edu.iis.powp.commandsFactory;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import commandsFactory.CommandBuilder;
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

}
