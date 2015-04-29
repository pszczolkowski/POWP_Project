/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandsFactoryWindow;

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author Godzio
 */
public class CommandFactoryWindow extends JFrame {

    private static CommandFactoryWindow window = null;
    private static CommandFactoryPanel panel = null;

    public CommandFactoryWindow() {
        super();
        initializeWindow();
    }

    private void initializeWindow() {
        setTitle( "Command Factory" );
        this.setLayout( new BorderLayout() );
        setBounds( 50, 600, 650, 300 );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setResizable( true );
        setVisible( true );
    }

    public static synchronized CommandFactoryWindow getCommandFactoryWindow() {
        if ( window == null ) {
            window = new CommandFactoryWindow();
            panel = new CommandFactoryPanel();
            window.add( panel );
        }
        return window;

    }

}
