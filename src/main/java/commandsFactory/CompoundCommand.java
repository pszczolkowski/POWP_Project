package commandsFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.iis.client.plottermagic.IPlotter;
import edu.iis.powp.command.ICompoundCommand;
import edu.iis.powp.command.IPlotterCommand;

public class CompoundCommand implements ICompoundCommand , Cloneable {

	private List< IPlotterCommand > commands;
	
	/**
	 * Tworzy złożone polecenie, składające się z podanej listy poleceń
	 * @param commands ista poleceń
	 */
	public CompoundCommand( List<IPlotterCommand> commands ) {
		this.commands = commands;
	}
	
	/**
	 * Tworzy złożone polecenie, składające się z podanej tablicy poleceń
	 * @param commands tablica poleceń
	 */
	public CompoundCommand( IPlotterCommand... commands ) {
		this.commands = new ArrayList< IPlotterCommand >( Arrays.asList( commands ));
	}

	@Override
	public void execute(IPlotter plotter) {
		for (IPlotterCommand command : commands) {
			command.execute( plotter );
		}
	}

	@Override
	public Iterator<IPlotterCommand> iterator() {
		return commands.iterator();
	}

	@Override
	protected CompoundCommand clone() throws CloneNotSupportedException {
		CompoundCommand result = (CompoundCommand) super.clone();
		result.commands = new ArrayList< IPlotterCommand >( commands );
		
		return result;
	}

}
