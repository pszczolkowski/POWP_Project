/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandsFactoryWindow;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import commandsFactory.CommandAlreadyExistsException;
import commandsFactory.CommandBuilder;
import commandsFactory.CommandFactory;

import edu.iis.powp.command.IPlotterCommand;

/**
 *
 * @author Godzio
 */
public class CommandFactoryPanel extends JPanel {

    CommandBuilder builder = new CommandBuilder();
    CommandFactory factory = new CommandFactory();

    private final JSpinner xPosition;
    private final JSpinner yPosition;
    private final JComboBox commandType;
    private final JButton addCommandButton;

    private final String[] header = { "Command", "x", "y" };
    private final JTable commandsTable;
    private final DefaultTableModel tableModel;

    private final JButton saveCommandButton;
    private final JButton resetButton;
    private final JTextField commandName;
    private final JTextField commandCategory;
    
    private List< OnCommandAddedListener > listeners = new ArrayList<>();

    public void addOnCommandAddedListener( OnCommandAddedListener listener ){
    	listeners.add( listener );
    }
    private void commandAdded( String name , IPlotterCommand command ){
    	for( OnCommandAddedListener listener : listeners )
    		listener.onCommandAdded(name, command);
    }
    
    public CommandFactoryPanel() {
        setBackground( Color.white );
        setLayout( new GridLayout( 3, 1 ) );

        JPanel newCommandPanel = new JPanel( new GridLayout( 3, 3 ) );
        xPosition = new JSpinner( new SpinnerNumberModel( 0, MIN_VALUE, MAX_VALUE, 1 ) );
        yPosition = new JSpinner( new SpinnerNumberModel( 0, MIN_VALUE, MAX_VALUE, 1 ) );
        commandType = new JComboBox();
        commandType.addItem( "Set Position" );
        commandType.addItem( "Draw Line" );
        commandType.setEditable( false );
        addCommandButton = new JButton( "Add command" );
        addCommandButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                String selectedCommand = commandType.getSelectedItem().toString();
                int x = (int) xPosition.getValue();
                int y = (int) yPosition.getValue();
                tableModel.addRow( new Object[]{ selectedCommand, x, y } );
                if ( selectedCommand.equals( "Set Position" ) ) {
                    builder.setPosition( x, y );
                } else {
                    builder.drawLineTo( x, y );
                }
            }
        } );
        newCommandPanel.add( new JLabel( "Command type" ) );
        newCommandPanel.add( new JLabel( "X" ) );
        newCommandPanel.add( new JLabel( "Y" ) );
        newCommandPanel.add( commandType );
        newCommandPanel.add( xPosition );
        newCommandPanel.add( yPosition );
        newCommandPanel.add( new JPanel() );
        newCommandPanel.add( addCommandButton );
        newCommandPanel.add( new JPanel() );

        JPanel inputPanel = new JPanel( new GridLayout( 2, 2 ) );
        JLabel nameLabel = new JLabel( "Command Name" );
        commandName = new JTextField();
        JLabel categoryLabel = new JLabel( "Command Category" );
        commandCategory = new JTextField();
        inputPanel.add( nameLabel );
        inputPanel.add( commandName );
        inputPanel.add( categoryLabel );
        inputPanel.add( commandCategory );

        JPanel buttonPanel = new JPanel( new GridLayout( 2, 1 ) );
        saveCommandButton = new JButton( "Save" );
        saveCommandButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                String name = commandName.getText();
                String category = commandCategory.getText();
                if ( !( name.equals( "" ) || category.equals( "" ) ) ) {
                    try {
                    	IPlotterCommand command =builder.build(); 
                        factory.add( name, command, category );
                        commandAdded(name, command);
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

        JPanel commandTablePanel = new JPanel();
        tableModel = new DefaultTableModel( header, 0 );
        tableModel.addRow( header );
        commandsTable = new JTable( tableModel );
        
        commandTablePanel.add( commandsTable );
        JScrollPane scrollPanel = new JScrollPane( commandTablePanel );

        add( newCommandPanel );
        add( menuPanel );
        add( scrollPanel );

    }

    private void clearInputsAndTable() {
        commandCategory.setText( "" );
        commandName.setText( "" );
        xPosition.setValue( 0 );
        yPosition.setValue( 0 );
        tableModel.setRowCount( 0 );
        tableModel.addRow( header );

    }
    
    public interface OnCommandAddedListener{
    	void onCommandAdded( String name , IPlotterCommand command );
    }
}
