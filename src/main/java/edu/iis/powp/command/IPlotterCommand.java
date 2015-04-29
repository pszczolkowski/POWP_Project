package edu.iis.powp.command;

import java.io.Serializable;

import edu.iis.client.plottermagic.IPlotter;

/**
 * PlotterCommand interface.
 */
public interface IPlotterCommand extends Serializable {

    /**
     * Execute command on plotter.
     * 
     * @param plotter plotter.
     */
	public void execute(IPlotter plotter);
	
}
