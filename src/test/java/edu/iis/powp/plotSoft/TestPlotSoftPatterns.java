package edu.iis.powp.plotSoft;

import commandsFactory.CommandFactory;
import commandsFactoryWindow.CommandFactoryPanel;
import commandsFactoryWindow.CommandFactoryWindow;
import edu.iis.client.plottermagic.IPlotter;
import edu.iis.client.plottermagic.preset.FiguresJoe;
import edu.iis.powp.adapter.LineAdapterPlotterDriver;
import edu.iis.powp.app.Application;
import edu.iis.powp.app.ApplicationWithDrawer;
import edu.iis.powp.app.Context;
import edu.iis.powp.app.DriverManager;
import edu.iis.powp.command.DrawToCommand;
import edu.iis.powp.command.IPlotterCommand;
import edu.iis.powp.command.SetPositionCommand;
import edu.iis.powp.gui.event.predefine.SelectTestFigureOptionListener;
import edu.kis.powp.drawer.shape.line.BasicLine;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static commandsFactoryWindow.CommandFactoryWindowSetup.setupCommandFactoryWindow;

public class TestPlotSoftPatterns {

    /**
     * Launch the application.
     */
    public static void main( String[] args ) {
        ApplicationWithDrawer.configureApplication();

        final Context context = Application.getComponent( Context.class );

        setupCommandFactoryWindow( context );
        CommandFactoryWindow.getPanel().addOnCommandAddedListener( new CommandFactoryPanel.OnCommandAddedListener() {
			
			@Override
			public void onCommandAdded(String name, IPlotterCommand command) {
				setupTest( context , name , command );				
			}
		});

        setupDrivers( context );

        setupPresetTests( context );

        setupSecretCommandTest( context );
        
        loadAllTests( context );
    }

    /**
     * Sets plotter simulators
     *
     * @param context Application context.
     */
    private static void setupDrivers( Context context ) {

        IPlotter plotter = new LineAdapterPlotterDriver( new BasicLine(), "basic" );
        context.addDriver( "Line Simulator", plotter );
        Application.getComponent( DriverManager.class ).setCurrentPlotter( plotter );
        context.updateDriverInfo();

        plotter = LineAdapterPlotterDriver.getSpecialLineAdapter();
        context.addDriver( "Special line Simulator", plotter );
    }

    /**
     * Setup test in context.
     *
     * @param context Application context.
     */
    private static void setupPresetTests( Context context ) {
        SelectTestFigureOptionListener selectTestFigureOptionListener = new SelectTestFigureOptionListener();

        context.addTest( "Figure Joe 1", selectTestFigureOptionListener );

        context.addTest( "Figure Joe 2", new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                FiguresJoe.figureScript2( Application.getComponent( DriverManager.class ).getCurrentPlotter() );
            }
        } );
    }

    private static void loadAllTests( Context context ) {
        CommandFactory commandFactory = new CommandFactory();
        Map< String, IPlotterCommand> mapa = commandFactory.getAllWithNames();

        for ( String name : mapa.keySet() ) {
            setupTest( context, name, mapa.get( name ) );
        }

    }

    private static void setupTest( Context context, String name, final IPlotterCommand command ) {
        context.addTest( name, new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                command.execute( Application.getComponent( DriverManager.class ).getCurrentPlotter() );
            }
        } );
    }

    /**
     * Simple test of basic commands
     *
     * @param context Application context.
     */
    private static void setupSecretCommandTest( Context context ) {

        context.addTest( "Secret command", new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent e ) {
                List<IPlotterCommand> commands = new ArrayList<IPlotterCommand>();
                commands.add( new SetPositionCommand( -20, -50 ) );
                commands.add( new DrawToCommand( -20, -50 ) );
                commands.add( new SetPositionCommand( -20, -40 ) );
                commands.add( new DrawToCommand( -20, 50 ) );
                commands.add( new SetPositionCommand( 0, -50 ) );
                commands.add( new DrawToCommand( 0, -50 ) );
                commands.add( new SetPositionCommand( 0, -40 ) );
                commands.add( new DrawToCommand( 0, 50 ) );
                commands.add( new SetPositionCommand( 70, -50 ) );
                commands.add( new DrawToCommand( 20, -50 ) );
                commands.add( new DrawToCommand( 20, 0 ) );
                commands.add( new DrawToCommand( 70, 0 ) );
                commands.add( new DrawToCommand( 70, 50 ) );
                commands.add( new DrawToCommand( 20, 50 ) );

                for ( IPlotterCommand c : commands ) {
                    c.execute( Application.getComponent( DriverManager.class ).getCurrentPlotter() );
                }
            }
        } );

    }
}
