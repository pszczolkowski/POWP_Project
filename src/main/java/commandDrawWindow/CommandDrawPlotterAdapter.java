/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandDrawWindow;

import edu.iis.client.plottermagic.IPlotter;

/**
 *
 * @author Godzio
 */
public class CommandDrawPlotterAdapter implements IPlotter {

    private final DrawerPanel drawer;

    public CommandDrawPlotterAdapter( DrawerPanel drawer ) {
        this.drawer = drawer;
    }

    @Override
    public void setPosition( int x, int y ) {
        drawer.setPosition( x, y );
    }

    @Override
    public void drawTo( int x, int y ) {
        drawer.drawLine( x, y );
    }

}
