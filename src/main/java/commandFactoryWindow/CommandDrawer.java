/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandFactoryWindow;

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
    int halfOfPanelWidth;
    int halfOfPanelHeigth;

    public CommandDrawer() {
        builder = new CommandBuilder();
        this.setBackground( Color.white );
        halfOfPanelWidth = getWidth() / 2;
        halfOfPanelHeigth = getHeight() / 2;
        this.addMouseListener(
                new MouseListener() {

                    @Override
                    public void mouseClicked( MouseEvent e ) {

                    }

                    @Override
                    public void mousePressed( MouseEvent e ) {
                        if ( !draw || startPoint == null ) {
                            startPoint = new Point( e.getX(), e.getY() );
                            builder.setPosition( e.getX() - halfOfPanelWidth, e.getY() - halfOfPanelHeigth );

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
        halfOfPanelWidth = getWidth() / 2;
        halfOfPanelHeigth = getHeight() / 2;
        g.setColor( Color.red );

        if ( startPoint != null ) {
            if ( draw && mouseInWindow && endPoint != null ) {
                if ( !preset ) {
                    lines.add( new Line( startPoint, endPoint ) );
                    builder.drawLineTo( endPoint.x - halfOfPanelWidth, endPoint.y - halfOfPanelHeigth );
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
        startPoint = null;
    }

    void setPosition( int x, int y ) {
        halfOfPanelWidth = getWidth() / 2;
        halfOfPanelHeigth = getHeight() / 2;
        startPoint = new Point( x + halfOfPanelWidth, y + halfOfPanelHeigth );
        builder.setPosition( x, y );
    }

    void drawLine( int x, int y ) {
        halfOfPanelWidth = getWidth() / 2;
        halfOfPanelHeigth = getHeight() / 2;
        endPoint = new Point( x + halfOfPanelWidth, y + halfOfPanelHeigth );
        lines.add( new Line( startPoint, endPoint ) );
        builder.drawLineTo( x, y );
        startPoint = endPoint;
        repaint();
    }

}
