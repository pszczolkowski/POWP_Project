package edu.iis.powp.command;

import java.util.Objects;

import edu.iis.client.plottermagic.IPlotter;

/**
 * Implementation of IPlotterCommand for drawTo command functionality.
 */
public class DrawToCommand implements IPlotterCommand {

	private int posX, posY;
	
	public DrawToCommand(int posX, int posY) {
		super();
		this.posX = posX;
		this.posY = posY;
	}

	@Override
	public void execute(IPlotter plotter) {
		plotter.drawTo(posX, posY);
	}

	@Override
	public DrawToCommand clone() throws CloneNotSupportedException {
		return (DrawToCommand) super.clone();
	}

	@Override
	public int hashCode() {
		return Objects.hash( posX , posY );
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if( obj instanceof DrawToCommand ){
			DrawToCommand other = (DrawToCommand) obj;
			return Objects.equals( posX , other.posX ) && Objects.equals( posY , other.posY );
		}else
			return false;
	}

}
