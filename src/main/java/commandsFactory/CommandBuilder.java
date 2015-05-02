package commandsFactory;

import java.util.ArrayList;
import java.util.List;

import edu.iis.powp.command.DrawToCommand;
import edu.iis.powp.command.IPlotterCommand;
import edu.iis.powp.command.SetPositionCommand;

/**
 * Służy do budowania złożonych poleceń plotera {@link CompoundCommand}. JEst to
 * implementacja wzorca projektowego Builder.
 */
public class CommandBuilder {
	
	private List< IPlotterCommand > commands = new ArrayList<>();

	/**
	 * Dodaje polecenie zmiany pozycji plotera do listy poleceń
	 * 
	 * @param x
	 * @param y
	 */
	public CommandBuilder setPosition( int x , int y ){
		commands.add( new SetPositionCommand( x , y ) );
		return this;
	}
	
	/**
	 * Dodanie polecenie narysowania linii do listy poleceń
	 * 
	 * @param x
	 * @param y
	 */
	public CommandBuilder drawLineTo( int x , int y ){
		commands.add( new DrawToCommand( x , y ) );
		return this;
	}
	
	/**
	 * Dodaje istniejące złożone polecenie do listy poleceń. Poprzez tę metodę
	 * można rozbudowywać istniejące polecenia.
	 * 
	 * @param command
	 *            istniejące polecenie
	 */
	public CommandBuilder addCommand( IPlotterCommand command ){
		commands.add( command );
		return this;
	}
	
	/**
	 * Tworzy obiekt złożonego polecenia {@link CompoundCommand} na podstawie
	 * dodanych wcześniej poleceń.
	 * 
	 * @return utworzone złożone polecenie {@link CompoundCommand}
	 */
	public CompoundCommand build(){
		return new CompoundCommand( commands );
	}
	
}
