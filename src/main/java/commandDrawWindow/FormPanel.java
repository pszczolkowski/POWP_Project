/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commandDrawWindow;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

/**
 *
 * @author Godzio
 */
public class FormPanel extends JPanel {

    private final JLabel commandTypeLabel = new JLabel( "Command Type" );
    private final JLabel xPositionLabel = new JLabel( "X" );
    private final JLabel yPositionLabel = new JLabel( "Y" );

    private final JComboBox commandTypePicker = new JComboBox();
    private final JSpinner xPositionSpinner = new JSpinner( new SpinnerNumberModel( 0, MIN_VALUE, MAX_VALUE, 1 ) );
    private final JSpinner yPositionSpinner = new JSpinner( new SpinnerNumberModel( 0, MIN_VALUE, MAX_VALUE, 1 ) );

    private final JButton addButton = new JButton( "Add" );
    private final JButton saveButton = new JButton( "Save" );
    private final JButton resetButton = new JButton( "Reset" );

    private JTable commandsTable;
    private DefaultTableModel commandsTableModel;
    private final String[] header = { "Command Type", "X", "Y" };

    public FormPanel() {
        super();
        initUi();
    }

    private void initUi() {
        setLayout( new BorderLayout() );
        JPanel upperSide = new JPanel();
        JPanel inputs = new JPanel( new GridLayout( 3, 3 ) );
        inputs.add( commandTypeLabel );
        inputs.add( xPositionLabel );
        inputs.add( yPositionLabel );
        inputs.add( commandTypePicker );
        inputs.add( xPositionSpinner );
        inputs.add( yPositionSpinner );
        inputs.add( addButton );
        inputs.add( saveButton );
        inputs.add( resetButton );

        upperSide.add( inputs );

        JPanel commandsTablePanel = new JPanel( new GridLayout( 1, 1 ) );
        commandsTableModel = new DefaultTableModel( header, 0 );
        commandsTableModel.addRow( header );
        commandsTable = new JTable( commandsTableModel );
        JScrollPane scrollPanel = new JScrollPane( commandsTablePanel );
        scrollPanel.setLayout( new ScrollPaneLayout() );

        add( upperSide, BorderLayout.NORTH );
        add( scrollPanel, BorderLayout.CENTER );
    }

}
