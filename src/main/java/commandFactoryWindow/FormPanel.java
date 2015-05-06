/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandFactoryWindow;

import commandsFactory.CommandAlreadyExistsException;
import commandsFactory.CommandBuilder;
import commandsFactory.CommandCategory;
import commandsFactory.CommandStore;
import edu.iis.powp.command.IPlotterCommand;
import eventNotifier.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

import static commandsFactory.CommandStore.getInstance;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

/**
 *
 * @author Godzio
 */
public class FormPanel extends JPanel implements Subscriber {

    private final JLabel commandTypeLabel = new JLabel( "Command Type", SwingConstants.CENTER );
    private final JLabel xPositionLabel = new JLabel( "X", SwingConstants.CENTER );
    private final JLabel yPositionLabel = new JLabel( "Y", SwingConstants.CENTER );
    private final JLabel commandNameLabel = new JLabel( "Command Name", SwingConstants.CENTER );
    private final JLabel commandCategoryLabel = new JLabel( "Command Category", SwingConstants.CENTER );

    private final JComboBox commandTypePicker = new JComboBox();
    private final JSpinner xPositionSpinner = new JSpinner( new SpinnerNumberModel( 0, MIN_VALUE, MAX_VALUE, 1 ) );
    private final JSpinner yPositionSpinner = new JSpinner( new SpinnerNumberModel( 0, MIN_VALUE, MAX_VALUE, 1 ) );
    private final JComboBox commandCategoryPicker = new JComboBox();
    private final JTextField commandNameChooser = new JTextField();

    private final JButton addButton = new JButton( "Add" );
    private final JButton saveButton = new JButton( "Save" );
    private final JButton resetButton = new JButton( "Reset" );

    private final DefaultListModel commandListModel = new DefaultListModel();

    private CommandStore store;
    private CommandBuilder builder;

    public FormPanel() {
        super();
        store = getInstance();
        builder = new CommandBuilder();
        initUi();
        assaignListeners();

        EventService.getInstance()
                .subscribe( CategoryListEvent.class, this )
                .subscribe( CommandsListEvent.class, this );
    }

    private void initUi() {
        setLayout( new BorderLayout() );
        JPanel upperSide = new JPanel( new GridLayout( 2, 1, 0, 10 ) );
        JPanel lowerSide = new JPanel( new BorderLayout( 20, 0 ) );
        JPanel controls = new JPanel( new GridLayout( 8, 1, 0, 10 ) );

        JPanel firstRow = new JPanel( new GridLayout( 1, 4, 5, 0 ) );
        firstRow.add( commandTypeLabel );
        firstRow.add( xPositionLabel );
        firstRow.add( yPositionLabel );
        firstRow.add( new JPanel() );
        JPanel secondRow = new JPanel( new GridLayout( 1, 4, 5, 0 ) );
        secondRow.add( commandTypePicker );
        secondRow.add( xPositionSpinner );
        secondRow.add( yPositionSpinner );
        secondRow.add( addButton );

        JPanel thirdRow = new JPanel( new GridLayout( 1, 2, 5, 0 ) );
        thirdRow.add( commandNameLabel );
        thirdRow.add( commandCategoryLabel );
        JPanel fourthRow = new JPanel( new GridLayout( 1, 2, 5, 0 ) );
        fourthRow.add( commandNameChooser );
        fourthRow.add( commandCategoryPicker );

        JPanel fifthRow = new JPanel( new GridLayout( 1, 2, 5, 0 ) );
        fifthRow.add( saveButton );
        fifthRow.add( resetButton );

        upperSide.add( firstRow );
        upperSide.add( secondRow );
        controls.add( thirdRow );
        controls.add( fourthRow );
        controls.add( fifthRow );
        controls.add( new JPanel() );
        controls.add( new JPanel() );
        controls.add( new JPanel() );
        controls.add( new JPanel() );
        controls.add( new JPanel() );

        JPanel commandListPanel = new JPanel( new GridLayout( 1, 1 ) );
        JList commandList = new JList( commandListModel );
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) commandList.getCellRenderer();
        renderer.setHorizontalAlignment( JLabel.CENTER );
        commandList.setEnabled( false );
        commandListPanel.add( commandList );
        JScrollPane scrollPanel = new JScrollPane( commandListPanel );

        lowerSide.add( controls, BorderLayout.WEST );
        lowerSide.add( scrollPanel, BorderLayout.CENTER );

        add( upperSide, BorderLayout.NORTH );
        add( lowerSide, BorderLayout.CENTER );

        loadAllCategories();
        loadAllCommandNames();
    }

    private void assaignListeners() {

        addButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                String commandType = (String) commandTypePicker.getSelectedItem();
                if ( commandType.equals( "Set Position" ) || commandType.equals( "Draw line" ) ) {
                    int xPosition = (int) xPositionSpinner.getValue();
                    int yPosition = (int) yPositionSpinner.getValue();
                    StringBuilder element = new StringBuilder( "" );
                    element.append( commandType ).append( "(" ).append( xPosition ).append( ", " ).append( yPosition ).append( ")" );
                    commandListModel.addElement( element.toString() );
                    if ( commandType.equals( "Set Position" ) ) {
                        builder.setPosition( xPosition, yPosition );
                    } else {
                        builder.drawLineTo( xPosition, yPosition );
                    }
                } else {
                    commandListModel.addElement( commandType );
                    builder.addCommand( store.get( commandType ) );
                }
            }
        } );

        saveButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                String commandName = commandNameChooser.getText();
                if ( commandName != null && !commandName.equals( "" ) ) {
                    try {
                        String categoryName = commandCategoryPicker.getSelectedItem().toString();
                        CommandCategory category = store.getCategoryManager().find( categoryName );
                        IPlotterCommand command = builder.build();

                        store.add( commandName, command, category );
                        Event event = new CommandAddedEvent( this, commandName, category );
                        EventService.getInstance().publish( event );
                        resetButton.doClick();
                        builder = new CommandBuilder();
                    } catch ( CommandAlreadyExistsException exception ) {
                        JOptionPane.showMessageDialog( FormPanel.this, "Command with that name already exists" );
                    }
                }

            }
        }
        );

        resetButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed( ActionEvent e
                    ) {
                        commandNameChooser.setText( "" );
                        commandListModel.clear();
                        builder = new CommandBuilder();
                    }
                }
        );

        commandTypePicker.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged( ItemEvent e
                    ) {
                        if ( commandTypePicker.getSelectedItem() != null ) {
                            String commandType = commandTypePicker.getSelectedItem().toString();
                            if ( commandType.equals( "Set Position" ) || commandType.equals( "Draw line" ) ) {
                                xPositionSpinner.setEnabled( true );
                                yPositionSpinner.setEnabled( true );
                            } else {
                                xPositionSpinner.setEnabled( false );
                                yPositionSpinner.setEnabled( false );
                            }
                        }
                    }
                }
        );

    }

    private void loadAllCommandNames() {
        commandTypePicker.removeAllItems();
        commandTypePicker.addItem( "Set Position" );
        commandTypePicker.addItem( "Draw line" );
        List<String> commands = store.getCommandsNames();
        Collections.sort( commands );
        for ( String command : commands ) {
            commandTypePicker.addItem( command );
        }
    }

    private void loadAllCategories() {
        commandCategoryPicker.removeAllItems();
        List<CommandCategory> categories = store.getCategoryManager().getRootCategory().getAllSubcategories();
        for ( CommandCategory category : categories ) {
            commandCategoryPicker.addItem( category.getName() );

        }
    }

    @Override
    public void inform( Event event ) {
        if ( event instanceof CategoryListEvent ) {
            loadAllCategories();
        } else if ( event instanceof CommandsListEvent ) {
            loadAllCommandNames();
        }
    }

}
