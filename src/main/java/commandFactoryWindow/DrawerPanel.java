/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandFactoryWindow;

import commandsFactory.CommandAlreadyExistsException;
import commandsFactory.CommandCategory;
import commandsFactory.CommandStore;
import edu.iis.powp.command.IPlotterCommand;
import eventNotifier.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

/**
 *
 * @author Godzio
 */
// TODO disable przycisku load Command jesli na liscie nie ma nic
public class DrawerPanel extends JPanel implements Subscriber {

    private final JRadioButton setPositionButton = new JRadioButton( "Set Position" );
    private final JRadioButton drawLineButton = new JRadioButton( "Draw Line" );
    private final JButton clearButton = new JButton( "Clear Panel" );
    private final JLabel commandNameLabel = new JLabel( "Command name", SwingConstants.CENTER );
    private final JLabel commandCategoryLabel = new JLabel( "Command category", SwingConstants.CENTER );
    private final JTextField commandName = new JTextField();
    private final JComboBox commandCategory = new JComboBox();
    private final JButton saveButton = new JButton( "Save Command" );
    private final JButton useCommandButton = new JButton( "Use Command" );
    private JList commandsList;
    private DefaultListModel listModel;
    private CommandStore store;

    private final CommandDrawer drawer = new CommandDrawer();

    public DrawerPanel() {
        super();
        store = CommandStore.getInstance();
        initUI();

        EventService.getInstance()
                .subscribe( CategoryListEvent.class, this )
                .subscribe( CommandsListEvent.class, this );
    }

    private void initUI() {
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
        JScrollPane scrollPane = new JScrollPane( commandListPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );

        JPanel lowerPanel = new JPanel( new BorderLayout() );
        lowerPanel.add( new JLabel( "Commands" ), BorderLayout.NORTH );
        lowerPanel.add( scrollPane, BorderLayout.CENTER );
        lowerPanel.add( useCommandButton, BorderLayout.SOUTH );

        loadAllCategories();
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout( new BoxLayout( sidePanel, BoxLayout.Y_AXIS ) );
        sidePanel.add( inputs );
        sidePanel.add( lowerPanel );

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
                String name = commandName.getText();
                if ( name != null && !name.equals( "" ) ) {
                    try {
                        IPlotterCommand command = drawer.getBuilder().build();

                        String categoryName = commandCategory.getSelectedItem().toString();
                        CommandCategory category = store.getCategoryManager().find( categoryName );

                        store.add( name, command, category );
                        Event event = new CommandAddedEvent( this, name, category );
                        EventService.getInstance().publish( event );
                        drawer.clear();
                        commandName.setText( "" );
                        setPositionButton.doClick();
                        loadAllCommandNames();
                    } catch ( CommandAlreadyExistsException exception ) {
                        JOptionPane.showMessageDialog( DrawerPanel.this, "Command with that name already exists" );
                    }
                }
            }
        } );
        useCommandButton.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                if ( commandsList.getModel().getSize() > 0 && commandsList.getSelectedIndex() != -1 ) {
                    String commandName = commandsList.getSelectedValue().toString();
                    IPlotterCommand command = store.get( commandName );
                    command.execute( new CommandDrawPlotterAdapter( drawer ) );
                }
            }
        } );

    }

    private void loadAllCommandNames() {
        List<String> commands = store.getCommandsNames();
        Collections.sort( commands );
        listModel.clear();
        for ( String command : commands ) {
            listModel.addElement( command );
        }
    }

    private void loadAllCategories() {
        commandCategory.removeAllItems();
        List<CommandCategory> categories = store.getCategoryManager().getRootCategory().getAllSubcategories();
        for ( CommandCategory category : categories ) {
            commandCategory.addItem( category.getName() );

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
