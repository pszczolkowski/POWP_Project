package commandsListWindow;

import javax.swing.tree.DefaultMutableTreeNode;

public class CategoryTreeNode extends DefaultMutableTreeNode {

	private static final long serialVersionUID = 1L;

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
