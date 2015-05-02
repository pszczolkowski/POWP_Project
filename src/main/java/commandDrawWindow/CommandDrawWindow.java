/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandDrawWindow;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 *
 * @author Godzio
 */
public class CommandDrawWindow extends JFrame {

    private JMenuBar menuBar;

    public CommandDrawWindow() {
        super();
        setTitle( "Command Draw" );
        setSize( 600, 600 );
        setResizable( true );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setVisible( true );
        initUI();
    }

    private void initUI() {
        menuBar = new JMenuBar();
        JMenuItem setPosition = new JMenuItem( "Set position" );
        JMenuItem drawLine = new JMenuItem( "Draw line" );
        menuBar.add( setPosition );
        menuBar.add( drawLine );

        JPanel drawer = new JPanel();
        drawer.setBackground( Color.white );
    }

}
