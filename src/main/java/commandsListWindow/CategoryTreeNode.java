package commandsListWindow;

import javax.swing.tree.DefaultMutableTreeNode;

import edu.iis.powp.command.IPlotterCommand;

public class CategoryTreeNode extends DefaultMutableTreeNode {

	public CategoryTreeNode() {
		super();
	}

	public CategoryTreeNode(Object userObject, boolean allowsChildren) {
		super(userObject, allowsChildren);
	}

	public CategoryTreeNode(Object userObject) {
		super(userObject);
	}

	@Override
	public boolean isLeaf() {
		return false;
	}
}
