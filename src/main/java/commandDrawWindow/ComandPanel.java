/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandDrawWindow;

import commandsFactory.CommandCategory;
import commandsFactory.CommandStore;
import edu.iis.powp.command.IPlotterCommand;
import eventNotifier.CommandAddedEvent;
import eventNotifier.Event;
import eventNotifier.EventService;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author Godzio
 */
// TODO disable przycisku load Command jesli na liscie nie ma nic
public class ComandPanel extends JPanel {

    private final JRadioButton setPositionButton = new JRadioButton( "Set Position" );
    private final JRadioButton drawLineButton = new JRadioButton( "Draw Line" );
    private final JButton clearButton = new JButton( "Clear Panel" );
    private final JLabel commandNameLabel = new JLabel( "Command name" );
    private final JLabel commandCategoryLabel = new JLabel( "Command category" );
    private final JTextField commandName = new JTextField();
    private final JComboBox commandCategory = new JComboBox();
    private final JButton saveButton = new JButton( "Save Command" );
    private final JButton useCommandButton = new JButton( "Use Command" );
    private final JList commandsList;
    private final DefaultListModel listModel;
    private final CommandStore store;

    private final DrawerPanel drawer = new DrawerPanel();

    ;

    public ComandPanel() {
        super();
        store = CommandStore.getInstance();
        setLayout( new BorderLayout() );

        JPanel inputs = new JPanel( new GridLayout( 8, 1, 2, 2 ) );
        ButtonGroup group = new ButtonGroup();
        group.add( setPositionButton );
        inputs.add( setPositionButton );
        setPositionButton.setSelected( true );
        group.add( drawLineButton );
        inputs.add( drawLineButton );
        inputs.add( clearButton );
        inputs.add( commandNameLabel );
        inputs.add( commandName );
        inputs.add( commandCategoryLabel );
        inputs.add( commandCategory );
        inputs.add( saveButton );

        JPanel commandListPanel = new JPanel( new GridLayout( 1, 1 ) );
        listModel = new DefaultListModel();
        loadAllCommandNames();
        commandsList = new JList( listModel );
        commandListPanel.add( commandsList );
        JScrollPane scrollPane = new JScrollPane( commandListPanel );

        loadAllCategories();
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout( new BoxLayout( sidePanel, BoxLayout.Y_AXIS ) );
        sidePanel.add( inputs );
        sidePanel.add( new JLabel( "Commands" ) );
        sidePanel.add( scrollPane );
        sidePanel.add( useCommandButton );

        setListeners();

        this.add( sidePanel, BorderLayout.EAST );
        this.add( drawer );

    }

    private void setListeners() {

        setPositionButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                drawer.setDraw( false );
            }
        } );
        drawLineButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                drawer.setDraw( true );
            }
        } );
        clearButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                drawer.clear();
            }
        } );
        saveButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                IPlotterCommand command = drawer.getBuilder().build();
                String name = commandName.getText();
                String categoryName = commandCategory.getSelectedItem().toString();
                CommandCategory category = store.getCategoryManager().find( categoryName );

                store.add( name, command, category );
                Event event = new CommandAddedEvent( this, name, category );
                EventService.getInstance().publish( event );
                drawer.newBuilder();
                loadAllCommandNames();
            }
        } );
        useCommandButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                String commandName = commandsList.getSelectedValue().toString();
                IPlotterCommand command = store.get( commandName );
                command.execute( new CommandDrawPlotterAdapter( drawer ) );
            }
        } );

    }

    private void loadAllCommandNames() {
        List<String> commands = store.getCommandsNames();
        listModel.clear();
        for ( String command : commands ) {
            listModel.addElement( command );
        }
    }

    private void loadAllCategories() {
        List<CommandCategory> categories = store.getCategoryManager().getRootCategory().getAllSubcategories();
        for ( CommandCategory category : categories ) {
            commandCategory.addItem( category.getName() );

        }
    }
}