package commandsFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.iis.client.plottermagic.IPlotter;
import edu.iis.powp.command.ICompoundCommand;
import edu.iis.powp.command.IPlotterCommand;

public class CompoundCommand implements ICompoundCommand {

	private List< IPlotterCommand > commands;
	
	public CompoundCommand( List<IPlotterCommand> commands ) {
		this.commands = commands;
	}
	
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

}
