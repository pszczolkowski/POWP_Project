/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandFactoryWindow;

import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.*;

/**
 *
 * @author Godzio
 */
public class CommandFactoryWindow extends JFrame {

    private DrawerPanel drawingPanel;
    private FormPanel formPanel;

    public CommandFactoryWindow() {
        super();

        EventQueue.invokeLater( new Runnable() {
            @Override
            public void run() {
                initUI();
            }
        } );
    }

    private void initUI() {
        setTitle( "Command Factory" );
        setSize( 600, 600 );
        setMinimumSize( new Dimension( 600, 600 ) );
        setResizable( true );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

        JTabbedPane tabbedMenu = new JTabbedPane();
        drawingPanel = new DrawerPanel();
        formPanel = new FormPanel();
        tabbedMenu.add( "Drawer", drawingPanel );
        tabbedMenu.add( "Form", formPanel );

        add( tabbedMenu );
    }

}
