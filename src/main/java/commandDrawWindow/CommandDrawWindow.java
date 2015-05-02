/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandDrawWindow;

import javax.swing.*;

/**
 *
 * @author Godzio
 */
public class CommandDrawWindow extends JFrame {

    private DrawerPanel drawingPanel;
    private FormPanel formPanel;

    public CommandDrawWindow() {
        super();
        setTitle( "Command Drawer" );
        setSize( 600, 600 );
        setResizable( true );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
//        EventQueue.invokeLater( new Runnable() {
//
//            @Override
//            public void run() {
//                initUI();
//            }
//        } );
        initUI();
        setVisible( true );
    }

    private void initUI() {
        
        JTabbedPane tabbedMenu = new JTabbedPane();
        drawingPanel = new DrawerPanel();
        tabbedMenu.add( "Drawer", drawingPanel );
        tabbedMenu.add( "Form", formPanel);
        add( tabbedMenu );
    }

}
