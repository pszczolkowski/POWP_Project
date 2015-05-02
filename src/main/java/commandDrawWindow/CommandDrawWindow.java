/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandDrawWindow;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
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
    private Point startPoint;
    private Point endPoint;
    private boolean draw = false;
    private boolean preset = true;
    private List<Line> lines = new ArrayList<>();

    public CommandDrawWindow() {
        super();
        setTitle( "Command Draw" );
        setSize( 600, 600 );
        setResizable( true );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        initUI();
        setVisible( true );
    }

    private void initUI() {
        menuBar = new JMenuBar();
        JMenuItem setPosition = new JMenuItem( "Set position" );
        setPosition.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                draw = false;
            }
        } );
        JMenuItem drawLine = new JMenuItem( "Draw line" );
        drawLine.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                draw = true;
            }
        } );
        menuBar.add( setPosition );
        menuBar.add( drawLine );

        JPanel drawer = new JPanel();
        drawer.setBackground( Color.white );
        drawer.addMouseListener( new MouseListener() {

            @Override
            public void mouseClicked( MouseEvent e ) {
                if ( !draw || startPoint == null ) {
                    startPoint = new Point( e.getX() + 5, e.getY() + 50 );
                } else {
                    endPoint = new Point( e.getX() + 5, e.getY() + 50 );
                }
                preset = false;
                repaint();
            }

            @Override
            public void mousePressed( MouseEvent e ) {

            }

            @Override
            public void mouseReleased( MouseEvent e ) {
            }

            @Override
            public void mouseEntered( MouseEvent e ) {
            }

            @Override
            public void mouseExited( MouseEvent e ) {
            }
        } );
        drawer.addMouseMotionListener( new MouseMotionListener() {

            @Override
            public void mouseDragged( MouseEvent e ) {

            }

            @Override
            public void mouseMoved( MouseEvent e ) {
                if ( draw ) {
                    preset = true;
                    endPoint = new Point( e.getX() + 5, e.getY() + 50 );
                    repaint();
                }
            }
        } );
        setJMenuBar( menuBar );
        add( drawer );
    }

    @Override
    public void paint( Graphics g ) {
        super.paint( g );
        g.setColor( Color.red );
        if ( startPoint != null ) {
            if ( draw ) {
                if ( !preset ) {
                    lines.add( new Line( startPoint, endPoint ) );
                    startPoint = endPoint;
                } else {
                    g.drawLine( startPoint.x, startPoint.y, endPoint.x, endPoint.y );
                }
                for ( Line line : lines ) {
                    g.drawLine( line.startPoint.x, line.startPoint.y, line.endPoint.x, line.endPoint.y );
                }

            } else {
                endPoint = startPoint;
                g.drawOval( endPoint.x, endPoint.y, 5, 5 );
            }
        }
        preset = true;
    }

}
