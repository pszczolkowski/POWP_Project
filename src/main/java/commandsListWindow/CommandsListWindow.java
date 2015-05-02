package commandsListWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import commandsFactory.CategoryManager;
import commandsFactory.CommandBuilder;
import commandsFactory.CommandCategory;
import commandsFactory.CommandStore;
import edu.iis.client.plottermagic.IPlotter;
import edu.iis.powp.app.Application;
import edu.iis.powp.app.DriverManager;
import edu.iis.powp.command.IPlotterCommand;
import edu.iis.powp.command.SetPositionCommand;

public class CommandsListWindow extends JFrame implements TreeSelectionListener, ActionListener, ListSelectionListener, ChangeListener, DocumentListener {

	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode rootNode;
	private JTree tree;
	private JButton executeButton;
	private CommandStore store;
	private JList< String > searchResultsList;

	private String selectedCommandName = null;
	private JTabbedPane tabbedPane;
	private JTextField searchField;
	
	public CommandsListWindow() {
		super();
		store = CommandStore.getInstance();
		CategoryManager categoryManager = store.getCategoryManager();
		CommandCategory figures = categoryManager.add( "figures" );
		IPlotterCommand cmd1 = new CommandBuilder()
			.setPosition( 200 , 200 )
			.drawLineTo( 400 , 100 )
			.build();
		store.add( "kwadrat" , cmd1 , figures );
		store.add( "kolo" , new SetPositionCommand(0,0) , figures );
		
		initUI();
		displayCommandsTree( rootNode , CommandStore.getInstance().getCategoryManager().getRootCategory() );
		tree.expandRow(0);
	}

	
	private void displayCommandsTree( DefaultMutableTreeNode node , CommandCategory category ) {
		CommandStore store = CommandStore.getInstance();
		
		List< String > names = store.getCommandsNamesOfCategory( category );
		List<CommandCategory> subCategories = category.getSubcategories();
		if( names.size() > 0 ){
			for( String name : names ){
				node.add( new DefaultMutableTreeNode( name ) );
			}
		}else if( subCategories.size() == 0 ){
			node.setAllowsChildren( true );
			// TODO wyświetlanie jako folderu a nie liścia
		}
		
		for( CommandCategory subCategory : subCategories ){
			DefaultMutableTreeNode subNode = new CategoryTreeNode( subCategory.getName() );
			node.add( subNode );
			
			displayCommandsTree(subNode, subCategory);
		}
	}


	private void initUI() {
		// MAIN WINDOW
		getContentPane().setLayout( new BorderLayout() );
		
		executeButton = new JButton( "execut" );
		executeButton.setEnabled( false );
		executeButton.addActionListener( this );
		add( executeButton , BorderLayout.SOUTH );
		
		setTitle( "Commands" );
		setSize( new Dimension( 210 , 400 ) );
		setMinimumSize( new Dimension( 210 , 300 ) );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setVisible( true );
		setResizable( true );
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener( this );
		add( tabbedPane );
		
		// ALL COMMANDS PANEL
		JPanel allPanel = new JPanel();
		allPanel.setLayout( new BorderLayout() );
		tabbedPane.addTab( "All" , new JScrollPane( allPanel ) );
		
		rootNode = new DefaultMutableTreeNode( "all" );
		tree = new JTree( rootNode ) ; 
		tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
		tree.addTreeSelectionListener( this );
		allPanel.add( tree , BorderLayout.CENTER );
		
		// SEARCH PANEL
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout( new BorderLayout() );
		tabbedPane.addTab( "Search" , searchPanel );
		
		searchResultsList = new JList<>();
		searchResultsList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		searchResultsList.addListSelectionListener( this );
		searchPanel.add( new JScrollPane( searchResultsList ) );
		
		JPanel searchFieldPanel = new JPanel();
		searchFieldPanel.setLayout( new BoxLayout( searchFieldPanel , BoxLayout.X_AXIS ) );
		searchPanel.add( searchFieldPanel , BorderLayout.SOUTH );
		
		searchFieldPanel.add( new JLabel( "Search: " ) );
		
		searchField = new JTextField();
		searchField.getDocument().addDocumentListener( this );
		searchFieldPanel.add( searchField );
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// WYBRANIE POLECENIA Z DRZEWA POLECEN
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		
		if( node == null || ! node.isLeaf() ){
			executeButton.setEnabled( false );
			selectedCommandName = null;
		}else{
			executeButton.setEnabled( true );
			selectedCommandName = (String) ((DefaultMutableTreeNode) tree.getLastSelectedPathComponent()).getUserObject();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// KLIKNIECIE PRZYCISKU "DRAW"
		if( e.getSource() == executeButton ){
			if( selectedCommandName != null ){
				IPlotterCommand command = store.get( selectedCommandName );
				IPlotter plotter = Application.getComponent( DriverManager.class ).getCurrentPlotter();
				command.execute( plotter );
			}else{
				JOptionPane.showMessageDialog( this , "No command selected");
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// WYBRANIE POLECENIA Z LISTY WYSZUKIWANIA
		/*ListSelectionModel lsm = searchResultsList.getSelectionModel();
        if ( ! lsm.isSelectionEmpty() ) {
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
                if (lsm.isSelectedIndex(i)) {
                	selectedCommandName = searchResultsList.getSelectedValue();
                    drawButton.setEnabled( true );
                }
            }
        }*/
		selectedCommandName = searchResultsList.getSelectedValue();
		if( selectedCommandName != null )
			executeButton.setEnabled( true );
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// PRZEŁACZENIE KARTY
		if( e.getSource() == tabbedPane ){
			selectedCommandName = null;
			executeButton.setEnabled( false );
			
			if( searchResultsList != null )
				searchResultsList.clearSelection();
			if( tree != null )
				tree.clearSelection();
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e ) {
		searchCommands();
	}
	@Override
	public void insertUpdate(DocumentEvent e) {
		searchCommands();
	}
	@Override
	public void removeUpdate(DocumentEvent e) {
		searchCommands();
	}
	
	private void searchCommands() {
		String searchName = searchField.getText();
		List<String> foundCommands = null;
		
		if( searchName.isEmpty() )
			foundCommands = new ArrayList<String>();
		else		
			foundCommands = store.getCommandsNamesLike( searchName );
		
		searchResultsList.setListData( foundCommands.toArray(new String[]{}) );
	}
	
}
