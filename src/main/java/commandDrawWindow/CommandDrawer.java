/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandDrawWindow;

import commandsFactory.CommandBuilder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author Godzio
 */
public class CommandDrawer extends JPanel {

    private Point startPoint;
    private Point endPoint;
    private boolean draw = false;
    private boolean preset = true;
    private boolean mouseInWindow = false;
    private final List<Line> lines = new ArrayList<>();
    private CommandBuilder builder;

    public CommandDrawer() {
        builder = new CommandBuilder();
        this.setBackground( Color.white );

        this.addMouseListener(
                new MouseListener() {

                    @Override
                    public void mouseClicked( MouseEvent e ) {

                    }

                    @Override
                    public void mousePressed( MouseEvent e ) {
                        if ( !draw || startPoint == null ) {
                            startPoint = new Point( e.getX(), e.getY() );
                        } else {
                            endPoint = new Point( e.getX(), e.getY() );
                        }
                        preset = false;
                        repaint();
                    }

                    @Override
                    public void mouseReleased( MouseEvent e ) {
                    }

                    @Override
                    public void mouseEntered( MouseEvent e ) {
                        mouseInWindow = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited( MouseEvent e ) {
                        mouseInWindow = false;
                        repaint();
                    }
                } );
        this.addMouseMotionListener(
                new MouseMotionListener() {

                    @Override
                    public void mouseDragged( MouseEvent e ) {

                    }

                    @Override
                    public void mouseMoved( MouseEvent e ) {
                        if ( draw ) {
                            preset = true;
                            endPoint = new Point( e.getX(), e.getY() );
                            repaint();
                        }
                    }
                }
        );
    }

    @Override
    public void paint( Graphics g ) {
        super.paint( g );
//        RenderingHints rh = new RenderingHints( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
//        rh.put( RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY );
//
//        Graphics2D g = (Graphics2D) g;
//        g.setRenderingHints( rh );
        g.setColor( Color.red );
        if ( startPoint != null ) {
            if ( draw && mouseInWindow ) {
                if ( !preset ) {
                    lines.add( new Line( startPoint, endPoint ) );
                    builder.drawLineTo( endPoint.x, endPoint.y );
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
                builder.setPosition( endPoint.x, endPoint.y );
                for ( Line line : lines ) {
                    g.drawLine( line.startPoint.x, line.startPoint.y, line.endPoint.x, line.endPoint.y );
                }
            }
        }
        preset = true;
    }

    public void setDraw( boolean draw ) {
        this.draw = draw;
    }

    public CommandBuilder getBuilder() {
        return builder;
    }

    public void clear() {
        lines.clear();
        builder = new CommandBuilder();
        repaint();
    }

    void setPosition( int x, int y ) {
        startPoint = new Point( x, y );
        builder.setPosition( x, y );
    }

    void drawLine( int x, int y ) {
        endPoint = new Point( x, y );
        lines.add( new Line( startPoint, endPoint ) );
        builder.drawLineTo( x, y );
        startPoint = endPoint;
        repaint();
    }

}
