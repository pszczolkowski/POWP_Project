package commandsFactory;

import java.util.ArrayList;
import java.util.List;

import edu.iis.powp.command.DrawToCommand;
import edu.iis.powp.command.IPlotterCommand;
import edu.iis.powp.command.SetPositionCommand;

/**
 * Służy do budowania złożonych poleceń {@link CompoundCommand}. Kolejne polecenia
 * dodaje się używając metod: setPosition, drawLineTo oraz addCommand.
 * Po zaprojektowaniu listy kolejno wykonywanych poleceń, należy użyć 
 * metody build, aby utworzyć obiekt {@link CompoundCommand}
 *
 */
public class CommandBuilder {
	
	private List< IPlotterCommand > commands = new ArrayList<>();

	/**
	 * Dodaje polecenie zmiany pozycji plotera do listy poleceń
	 * @param x
	 * @param y
	 */
	public CommandBuilder setPosition( int x , int y ){
		commands.add( new SetPositionCommand( x , y ) );
		return this;
	}
	
	/**
	 * Dodanie polecenie narysowania linii do listy poleceń
	 * @param x
	 * @param y
	 */
	public CommandBuilder drawLineTo( int x , int y ){
		commands.add( new DrawToCommand( x , y ) );
		return this;
	}
	
	/**
	 * Dodaje istniejące złożone polecenie do listy poleceń. 
	 * Poprzez tę metodę można rozbudowywać istniejące polecenia.
	 * @param command istniejące polecenie
	 * @return
	 */
	public CommandBuilder addCommand( IPlotterCommand command ){
		commands.add( command );
		return this;
	}
	
	/**
	 * Tworzy obiekt złożonego polecenia {@link CompoundCommand} na podstawie dodanych
	 * do listy poleceń 
	 * @return utworzone złożone polecenie {@link CompoundCommand}
	 */
	public CompoundCommand build(){
		return new CompoundCommand( commands );
	}
	
}
