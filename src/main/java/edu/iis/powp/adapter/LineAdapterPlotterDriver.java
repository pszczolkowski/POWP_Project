package edu.iis.powp.adapter;

import edu.iis.client.plottermagic.IPlotter;
import edu.iis.powp.app.Application;
import edu.kis.powp.drawer.panel.DrawPanelController;
import edu.kis.powp.drawer.shape.ILine;
import edu.kis.powp.drawer.shape.LineFactory;


/**
 * Line adapter - IPlotter with DrawPanelController object.
 */
public class LineAdapterPlotterDriver implements IPlotter
{
	private ILine line; 
	private int startX = 0, startY = 0;
	private String name;
	
    private DrawPanelController drawController;
    
    public LineAdapterPlotterDriver(ILine line, String name) {
		super();
		drawController = Application.getComponent(DrawPanelController.class);
		this.line = line;
		this.name = name;
	}
    
    

    public static LineAdapterPlotterDriver getSpecialLineAdapter() {
    	return new LineAdapterPlotterDriver(LineFactory.getSpecialLine(), "special");
    }
    
	@Override
    public void setPosition(int x, int y)
    {
        this.startX = x;
        this.startY = y;
    }

    @Override
    public void drawTo(int x, int y)
    {
        line.setStartCoordinates(this.startX, this.startY);
        this.setPosition(x, y);
        line.setEndCoordinates(x, y);
        
        try {
			drawController.drawLine((ILine) line.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public String toString()
    {
        return "Plotter Simulator - " + name;
    }
}
