package commandsFactory;

import java.util.ArrayList;
import java.util.List;

import edu.iis.powp.command.DrawToCommand;
import edu.iis.powp.command.IPlotterCommand;
import edu.iis.powp.command.SetPositionCommand;

public class CommandBuilder {
	
	private List< IPlotterCommand > commands = new ArrayList<>();

	public CommandBuilder setPosition( int x , int y ){
		commands.add( new SetPositionCommand( x , y ) );
		return this;
	}
	
	public CommandBuilder drawLineTo( int x , int y ){
		commands.add( new DrawToCommand( x , y ) );
		return this;
	}
	
	public CommandBuilder addCommand( IPlotterCommand command ){
		commands.add( command );
		return this;
	}
	
	public CompoundCommand build(){
		return new CompoundCommand( commands );
	}
	
}
