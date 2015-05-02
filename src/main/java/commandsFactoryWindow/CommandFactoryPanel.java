/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandsFactoryWindow;

import commandsFactory.CommandAlreadyExistsException;
import commandsFactory.CommandBuilder;
import commandsFactory.CommandCategory;
import commandsFactory.CommandStore;
import edu.iis.powp.command.IPlotterCommand;
import eventNotifier.CommandAddedEvent;
import eventNotifier.Event;
import eventNotifier.EventService;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

/**
 *
 * @author Godzio
 */
public class CommandFactoryPanel extends JPanel {

    CommandBuilder builder = new CommandBuilder();
    //CommandFactory factory = new CommandFactory();

    private final JSpinner xPosition;
    private final JSpinner yPosition;
    private final JComboBox commandType;
    private final JButton addCommandButton;

    private final String[] header = { "Command", "x", "y" };
    private final JTable commandsTable;
    private final DefaultTableModel tableModel;

    private final JButton saveCommandButton;
    private final JButton resetButton;
    private final JTextField commandNameField;
    private final JTextField commandCategoryField;

    private List< String> commandList;
    private CommandStore store = CommandStore.getInstance();

    private List< OnCommandAddedListener> listeners = new ArrayList<>();

    public void addOnCommandAddedListener( OnCommandAddedListener listener ) {
        listeners.add( listener );
    }

    public CommandFactoryPanel() {
        setBackground( Color.white );
        setLayout( new GridLayout( 3, 1 ) );

        commandList = store.getCommandsNames();

        JPanel newCommandPanel = new JPanel( new GridLayout( 2, 4 ) );
        xPosition = new JSpinner( new SpinnerNumberModel( 0, MIN_VALUE, MAX_VALUE, 1 ) );
        yPosition = new JSpinner( new SpinnerNumberModel( 0, MIN_VALUE, MAX_VALUE, 1 ) );
        commandType = new JComboBox();
        commandType.addItem( "Set Position" );
        commandType.addItem( "Draw Line" );
        addAllCommands();
        commandType.setEditable( false );
        addCommandButton = new JButton( "Add command" );
        addCommandButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                String selectedCommand = commandType.getSelectedItem().toString();
                int x = (int) xPosition.getValue();
                int y = (int) yPosition.getValue();
                if ( selectedCommand.equals( "Set Position" ) ) {
                    tableModel.addRow( new Object[]{ selectedCommand, x, y } );
                    builder.setPosition( x, y );
                } else if ( selectedCommand.equals( "Draw Line" ) ) {
                    tableModel.addRow( new Object[]{ selectedCommand, x, y } );
                    builder.drawLineTo( x, y );
                } else {
                    for ( String name : commandList ) {
                        if ( selectedCommand.equals( name ) ) {
                            tableModel.addRow( new Object[]{ selectedCommand, null, null } );
                            builder.addCommand( store.get( name ) );
                            break;
                        }
                    }
                }
                clearInputs();
            }
        } );
        newCommandPanel.add( new JLabel( "Command type", SwingConstants.CENTER ) );
        newCommandPanel.add( new JLabel( "X", SwingConstants.CENTER ) );
        newCommandPanel.add( new JLabel( "Y", SwingConstants.CENTER ) );
        newCommandPanel.add( new JPanel() );
        newCommandPanel.add( commandType );
        newCommandPanel.add( xPosition );
        newCommandPanel.add( yPosition );
//        newCommandPanel.add( new JPanel() );
        newCommandPanel.add( addCommandButton );
        //newCommandPanel.add( new JPanel() );

        JPanel inputPanel = new JPanel( new GridLayout( 2, 2 ) );
        JLabel nameLabel = new JLabel( "Command Name", SwingConstants.CENTER );
        commandNameField = new JTextField();
        JLabel categoryLabel = new JLabel( "Command Category", SwingConstants.CENTER );
        commandCategoryField = new JTextField();
        inputPanel.add( nameLabel );
        inputPanel.add( commandNameField );
        inputPanel.add( categoryLabel );
        inputPanel.add( commandCategoryField );

        JPanel buttonPanel = new JPanel( new GridLayout( 2, 1 ) );
        saveCommandButton = new JButton( "Save" );
        saveCommandButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                String commandName = commandNameField.getText();
                String categoryName = commandCategoryField.getText();
                if ( !( commandName.equals( "" ) || categoryName.equals( "" ) ) ) {
                    try {
                        IPlotterCommand command = builder.build();
                        CommandCategory category = store.getCategoryManager().find( categoryName );
                        store.add( commandName, command, category );
                        Event event = new CommandAddedEvent( this, commandName, category );
                        EventService.getInstance().publish( event );
                        commandType.addItem( commandName );
                    } catch ( CommandAlreadyExistsException error ) {
                        JOptionPane.showMessageDialog( null, "Command Already Exists" );
                    }
                    builder = new CommandBuilder();
                    clearInputsAndTable();
                } else {
                    JOptionPane.showMessageDialog( null, "You have to type in both name and category" );
                }
            }
        } );
        resetButton = new JButton( "Reset" );
        resetButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                clearInputsAndTable();
                builder = new CommandBuilder();
            }
        } );
        buttonPanel.add( saveCommandButton );
        buttonPanel.add( resetButton );

        JPanel menuPanel = new JPanel( new GridLayout( 1, 2 ) );
        menuPanel.add( inputPanel );
        menuPanel.add( buttonPanel );

        JPanel commandTablePanel = new JPanel( new GridLayout( 1, 1 ) );
        tableModel = new DefaultTableModel( header, 0 );
        tableModel.addRow( header );
        commandsTable = new JTable( tableModel );
        commandTablePanel.add( commandsTable );
        JScrollPane scrollPanel = new JScrollPane( commandTablePanel );
        scrollPanel.getVerticalScrollBar().addAdjustmentListener( new AdjustmentListener() {
            public void adjustmentValueChanged( AdjustmentEvent e ) {
                e.getAdjustable().setValue( e.getAdjustable().getMaximum() );
            }
        } );

        add( newCommandPanel );
        add( menuPanel );
        add( scrollPanel );

    }

    private void clearInputsAndTable() {
        clearInputs();
        tableModel.setRowCount( 0 );
        tableModel.addRow( header );
    }

    private void clearInputs() {
        commandCategoryField.setText( "" );
        commandNameField.setText( "" );
        xPosition.setValue( 0 );
        yPosition.setValue( 0 );
    }

    private void addAllCommands() {
        for ( String name : commandList ) {
            commandType.addItem( name );
        }
    }

    public interface OnCommandAddedListener {

        void onCommandAdded( String name, IPlotterCommand command );
    }

}
