package commandsFactory;

import java.util.ArrayList;
import java.util.List;

import edu.iis.powp.command.DrawToCommand;
import edu.iis.powp.command.IPlotterCommand;
import edu.iis.powp.command.SetPositionCommand;

public class CommandBuilder {
	
	private List< IPlotterCommand > commands = new ArrayList<>();

	/**
	 * Dodoaje polecenie zmiany pozycji plotera na podaną
	 * @param x
	 * @param y
	 */
	public CommandBuilder setPosition( int x , int y ){
		commands.add( new SetPositionCommand( x , y ) );
		return this;
	}
	
	/**
	 * Dodanie polecenie narysowania linii do podanej pozycji
	 * @param x
	 * @param y
	 */
	public CommandBuilder drawLineTo( int x , int y ){
		commands.add( new DrawToCommand( x , y ) );
		return this;
	}
	
	/**
	 * Dodaje istniejące polecenie
	 * @param command
	 * @return
	 */
	public CommandBuilder addCommand( IPlotterCommand command ){
		commands.add( command );
		return this;
	}
	
	/**
	 * Tworzy obiekt złożonego polecenia na podstawie dodanych
	 * do listy poleceń 
	 * @return utworzone złożone polecenie
	 */
	public CompoundCommand build(){
		return new CompoundCommand( commands );
	}
	
}
