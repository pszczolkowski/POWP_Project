/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandsFactoryWindow;

import edu.iis.powp.app.Application;
import edu.iis.powp.app.Context;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Godzio
 */
public class CommandFactoryWindowSetup {

    private static boolean isAppCreated = false;

    public static void setupCommandFactoryWindow( Context context ) {
        Application.addComponent( CommandFactoryWindowController.class );
        context.addComponentMenu( CommandFactoryWindowController.class, "Command Factory", 3 );
        context.addComponentMenuElement( CommandFactoryWindowController.class, "Visibility", new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {

                CommandFactoryWindowController controller = Application.getComponent( CommandFactoryWindowController.class );
                if ( controller.isVisible() ) {
                    controller.setVisible( false );
                } else {
                    controller.setVisible( true );
                }
            }
        }, true );
        Application.getComponent( CommandFactoryWindowController.class ).setVisible( true );
    }

}
