package edu.iis.powp.command;

import java.util.Objects;

import edu.iis.client.plottermagic.IPlotter;

/**
 * Implementation of IPlotterCommand for setPosition command functionality.
 */
public class SetPositionCommand implements IPlotterCommand {

	private int posX, posY;
	
	public SetPositionCommand(int posX, int posY) {
		super();
		this.posX = posX;
		this.posY = posY;
	}

	@Override
	public void execute(IPlotter plotter) {
		plotter.setPosition(posX, posY);
	}

	@Override
	public SetPositionCommand clone() throws CloneNotSupportedException {
		return (SetPositionCommand) super.clone();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash( posX , posY );
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if( obj instanceof SetPositionCommand ){
			SetPositionCommand other = (SetPositionCommand) obj;
			return Objects.equals( posX , other.posX ) && Objects.equals( posY , other.posY );
		}else
			return false;
	}

}
