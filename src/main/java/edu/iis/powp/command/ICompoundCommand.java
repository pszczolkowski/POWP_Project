package edu.iis.powp.command;

import java.util.Iterator;

/**
 * Interface extending IPlotterCommand to execute more than one command.
 */
public interface ICompoundCommand extends IPlotterCommand {

	public Iterator<IPlotterCommand> iterator();
	
}
