package commandsListWindow;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
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

public class CommandsListWindow extends JFrame implements TreeSelectionListener, ActionListener {

	private static final long serialVersionUID = 1L;
	private DefaultMutableTreeNode rootNode;
	private JTree tree;
	private JButton drawButton;
	private CommandStore store;

	
	public CommandsListWindow() {
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
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout() );
		add( panel );
		
		rootNode = new DefaultMutableTreeNode( "all" );
		tree =new JTree( rootNode ) ; 
		tree.getSelectionModel().setSelectionMode( TreeSelectionModel.SINGLE_TREE_SELECTION );
		tree.addTreeSelectionListener( this );
		panel.add( tree , BorderLayout.CENTER );
		
		drawButton = new JButton( "draw" );
		drawButton.setEnabled( false );
		drawButton.addActionListener( this );
		panel.add( drawButton , BorderLayout.SOUTH );
		
		setTitle( "Commands list" );
		setSize( new Dimension( 200 , 400 ) );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setResizable( true );
	}


	@Override
	public void valueChanged(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
		
		if( node == null || ! node.isLeaf() )
			drawButton.setEnabled( false );
		else
			drawButton.setEnabled( true );
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == drawButton ){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if( node != null ){
				IPlotterCommand command = store.get( (String) node.getUserObject() );
				IPlotter plotter = Application.getComponent( DriverManager.class ).getCurrentPlotter();
				command.execute( plotter );
			}
		}
	}
	
}
