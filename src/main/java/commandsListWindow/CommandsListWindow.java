package commandsListWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import commandsFactory.CategoryManager;
import commandsFactory.CategoryNotEmptyException;
import commandsFactory.CommandCategory;
import commandsFactory.CommandStore;
import edu.iis.client.plottermagic.IPlotter;
import edu.iis.powp.app.Application;
import edu.iis.powp.app.DriverManager;
import edu.iis.powp.command.IPlotterCommand;
import eventNotifier.CategoryListEvent;
import eventNotifier.CommandAddedEvent;
import eventNotifier.CommandRemovedEvent;
import eventNotifier.CommandsListEvent;
import eventNotifier.Event;
import eventNotifier.EventService;
import eventNotifier.Subscriber;

public class CommandsListWindow extends JFrame implements TreeSelectionListener, ActionListener, ListSelectionListener, ChangeListener, DocumentListener, Subscriber {

	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode rootNode;
	private JTree tree;
	private JButton executeButton;
	private CommandStore store;
	private JList< String > searchResultsList;

	private String selectedCommandName = null;
	private JTabbedPane tabbedPane;
	private JTextField searchField;
	private DefaultTreeModel treeModel;
	private JPopupMenu categoryPopupMenu;
	private JMenuItem addSubcategoryMenuItem;
	private JMenuItem deleteCategoryMenuItem;
	private JPopupMenu commandPopupMenu;
	private JMenuItem deleteCommandMenuItem;
	
	public CommandsListWindow() {
		super();
		
		store = CommandStore.getInstance();
		
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				initUI();
			}
		});
		
		EventService.getInstance()
			.subscribe( CommandsListEvent.class , this )
			.subscribe( CategoryListEvent.class , this );
	}


	private void displayCommandsTree() {
		rootNode.removeAllChildren();
		displayCommandsOfCategory( rootNode , store.getCategoryManager().getRootCategory() );
		tree.repaint();
	}

	
	private void displayCommandsOfCategory( DefaultMutableTreeNode node , CommandCategory category ) {
		CommandStore store = CommandStore.getInstance();
		
		for( String name : store.getCommandsNamesOfCategory( category ) ){
				node.add( new DefaultMutableTreeNode( name ) );
			}
		
		for( CommandCategory subCategory : category.getSubcategories() ){
			DefaultMutableTreeNode subNode = new CategoryTreeNode( subCategory.getName() );
			node.add( subNode );
			
			displayCommandsOfCategory(subNode, subCategory);
		}
	}


	private void initUI() {
		setVisible( true );
		
		// MAIN WINDOW
		getContentPane().setLayout( new BorderLayout() );
		
		executeButton = new JButton( "execute" );
		executeButton.setEnabled( false );
		executeButton.addActionListener( this );
		add( executeButton , BorderLayout.SOUTH );
		
		setTitle( "Commands" );
		setSize( new Dimension( 210 , 400 ) );
		setMinimumSize( new Dimension( 210 , 300 ) );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );		
		setResizable( true );
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addChangeListener( this );
		add( tabbedPane );
		
		// ALL COMMANDS PANEL
		JPanel allPanel = new JPanel();
		allPanel.setLayout( new BorderLayout() );
		tabbedPane.addTab( "All" , new JScrollPane( allPanel ) );
		
		rootNode = new DefaultMutableTreeNode( "all" );
		treeModel = new DefaultTreeModel( rootNode );
		tree = new JTree( rootNode ) ; 
		tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
		tree.addTreeSelectionListener( this );
		tree.setModel( treeModel );
		tree.addMouseListener( mouseListener );
		allPanel.add( tree , BorderLayout.CENTER );
		
		categoryPopupMenu = new JPopupMenu();
		addSubcategoryMenuItem = new JMenuItem( "add subcategory" );
		addSubcategoryMenuItem.addMouseListener( mouseListener );
		deleteCategoryMenuItem = new JMenuItem( "delete" );
		deleteCategoryMenuItem.addMouseListener( mouseListener );
		categoryPopupMenu.add( addSubcategoryMenuItem );
		categoryPopupMenu.add( deleteCategoryMenuItem );
		
		commandPopupMenu = new JPopupMenu();
		deleteCommandMenuItem = new JMenuItem( "delete" );
		deleteCommandMenuItem.addMouseListener( mouseListener );
		commandPopupMenu.add( deleteCommandMenuItem );
		
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
		
		displayCommandsTree();
		tree.expandRow(0);
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

	@Override
	public void inform(Event event) {
		if( event instanceof CommandAddedEvent ){
			CommandAddedEvent specificEvent = (CommandAddedEvent) event;
			CommandCategory category = specificEvent.getCategory();
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode( specificEvent.getCommandName() );
			
			DefaultMutableTreeNode node = findCategoryNode(category);
			treeModel.insertNodeInto( newNode , node, node.getChildCount() );
			
			searchCommands();
			
			selectedCommandName = null;
			executeButton.setEnabled( false );
		}else if( event instanceof CommandsListEvent || event instanceof CategoryListEvent ){
			displayCommandsTree();
			treeModel.reload();
			
			searchCommands();
			
			selectedCommandName = null;
			executeButton.setEnabled( false );
		}
	}

	private DefaultMutableTreeNode findCategoryNode( CommandCategory category ){
		@SuppressWarnings("rawtypes")
		Enumeration enumeration = rootNode.depthFirstEnumeration();
		while( enumeration.hasMoreElements() ) {
		    DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
		    if ( !node.isLeaf() && ((String)node.getUserObject()).equals( category.getName() )) {
		    	return node;
		    }
		}
		
		return null;
	}
	
	private DefaultMutableTreeNode findCommandNode( String commandName ){
		@SuppressWarnings("rawtypes")
		Enumeration enumeration = rootNode.depthFirstEnumeration();
		while( enumeration.hasMoreElements() ) {
		    DefaultMutableTreeNode node = (DefaultMutableTreeNode) enumeration.nextElement();
		    if ( node.isLeaf() && ((String)node.getUserObject()).equals( commandName )) {
		    	return node;
		    }
		}
		
		return null;
	}
	
	
	private MouseListener mouseListener = new MouseAdapter() {
		@Override
		public void mouseReleased(MouseEvent e) {
			if( e.getSource() == tree && e.isPopupTrigger() ){
				int row = tree.getClosestRowForLocation(e.getX(), e.getY());
		        tree.setSelectionRow(row);
		        
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		        if( ! node.isLeaf() ){
		        	categoryPopupMenu.show(e.getComponent(), e.getX(), e.getY());
		        }else
		        	commandPopupMenu.show(e.getComponent(), e.getX(), e.getY());
			}else if( e.getSource() == addSubcategoryMenuItem ){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if( node.isLeaf() ){
					JOptionPane.showMessageDialog( CommandsListWindow.this , "No category selected" );
				}else{
					CommandCategory parentCategory = store.getCategoryManager().find( (String) node.getUserObject() );
					addSubcategory(parentCategory);
				}
			}else if( e.getSource() == deleteCategoryMenuItem ){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if( node.isLeaf() ){
					JOptionPane.showMessageDialog( CommandsListWindow.this , "No category selected" );
				}else{
					deleteCategory( (String) node.getUserObject() );
				}
			}else if( e.getSource() == deleteCommandMenuItem ){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if( ! node.isLeaf() ){
					JOptionPane.showMessageDialog( CommandsListWindow.this , "No command selected" );
				}else{
					deleteCommand( (String) node.getUserObject() );
				}
			}
			
		}
	};
	
	private void addSubcategory( CommandCategory parentCategory ){
		String name = JOptionPane.showInputDialog( this , "Enter subcategory name:" , "New subcategory" , JOptionPane.QUESTION_MESSAGE);
		if( name != null ){
			if( store.getCategoryManager().find( name ) != null ){
				JOptionPane.showMessageDialog( CommandsListWindow.this , "Category with that name already exists");
				return;
			}
			CommandCategory addedCategory = store.getCategoryManager().add( name );
			// aktualizacja drzewa
			DefaultMutableTreeNode newNode = new CategoryTreeNode( addedCategory.getName() );
			DefaultMutableTreeNode node = findCategoryNode( parentCategory );
			treeModel.insertNodeInto( newNode , node, node.getChildCount() );
			
			Event event = new CategoryListEvent( this );
			EventService.getInstance().publish(event);
		}
	}
	
	private void deleteCategory( String categoryName ){
		CategoryManager categoryManager = store.getCategoryManager();
		
		int answer = JOptionPane.showConfirmDialog( CommandsListWindow.this , "Are you sure you want to delete category " + categoryName + "?" , "Delete category" , JOptionPane.YES_NO_OPTION);
		if( answer == 1 )
			return;
		
		CommandCategory category = categoryManager.find( categoryName ); 
		if( category == null ){
			JOptionPane.showMessageDialog( CommandsListWindow.this , "Selected category doesn't exist");
			return;
		}
		
		try{
			if( categoryManager.remove( category ) ){
				// aktualizacja drzewa
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) findCategoryNode( category );
				treeModel.removeNodeFromParent( node );
				
				Event event = new CategoryListEvent( this );
				EventService.getInstance().publish(event);
			}else
				JOptionPane.showMessageDialog( CommandsListWindow.this , "This category cannot be deleted" );
		}catch( CategoryNotEmptyException e ){
			JOptionPane.showMessageDialog( CommandsListWindow.this , "Category cannot be deleted because it is not empty" );
		}
	}

	private void deleteCommand( String commandName ){
		int answer = JOptionPane.showConfirmDialog( CommandsListWindow.this , "Are you sure you want to delete command " + commandName + "?" , "Delete command" , JOptionPane.YES_NO_OPTION);
		if( answer == 1 )
			return;
		
		if( store.remove( commandName ) != null ){
			DefaultMutableTreeNode node = findCommandNode(commandName);
			treeModel.removeNodeFromParent( node );
			
			Event event = new CommandRemovedEvent( this , commandName );
			EventService.getInstance().publish(event);
		}else{
			JOptionPane.showMessageDialog( CommandsListWindow.this , "The command doesn't exist" );
			
		}
	}
	
}
