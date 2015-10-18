package sk.banik.fri.dataStructures;


public class RBTree<Key extends Comparable<Key>, Value>  implements BasicMapCollection<Key, Value>{
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	
	private RBNode root = null;

	public class RBNode{
		public Key key;
		public Value value;
		public boolean color;
		public RBNode leftChild;
		public RBNode rightChild;
		public RBNode parent;

		public RBNode(Key key, Value value, boolean color, RBNode parent) {
			this.key = key;
			this.value = value;
			this.color = color;
			
		}
		
		/**
		 * Returns the node with less key from this node and entered.
		 * @param node node for key comparison
		 * @return <b>this</b> if key(this)<=key(node)<br>
		 * <b>node</b> if key(this)>key(node)
		 */
		public RBNode min(RBNode node){
			int cmp = this.key.compareTo(node.key);
			if (cmp > 0) return node;
			else return this;
		}
	}

	@Override
	public void insert(Key key, Value value) {
		if (root == null) {
			root = new RBNode(key, value, BLACK, null);
			return;
		}
		
		RBNode node = root;
		do {
			int cmp = key.compareTo(node.key);
			if (cmp < 0) {
				// go to left child
				if (node.leftChild == null){
					node.leftChild = new RBNode(key, value, RED, node);
					node.leftChild.parent = node;
					node = node.leftChild;
					break;
				} else {
					node = node.leftChild;
				}
			} else if (cmp > 0) {
				// go to right child
				if (node.rightChild == null) {
					node.rightChild = new RBNode(key, value, RED, node);
					node.rightChild.parent = node;
					node = node.rightChild;
					break;
				} else{
					node = node.rightChild;
				}
			} else {
				// found already inserted value
				boolean wasSet = (node.value != null);
				node.value = value;
				if (!wasSet) {
					if (node.equals(root))
						node.color = BLACK;
					else
						node.color = RED;
				}
				break;
			}
		} while (true);

		fixInsert(node);

	}

	private void fixInsert(RBNode node) {
		// fix irregularity
		if (isRed(node.parent)) {
			if (node.parent == null || node.parent.parent == null) {
				return;
			}
			RBNode uncle = uncle(node);
			
			if (!isRed(uncle)) {
				RBNode grandParent = node.parent.parent;
				RBNode parent = node.parent;
				
				RBNode head = node.parent;
				if (parent.equals(grandParent.rightChild)) {
					// GP.r == P
					
					if (node.equals(node.parent.leftChild)) {
						rightRotation(node);
						head = node;
					}
					// do second rotation or if the n.p.r==n this will be first
					leftRotation(head);
				} else {
					// GP.l == P
					
					if (node.equals(node.parent.rightChild)) {
						leftRotation(node);
						head = node;
					}
					rightRotation(head);
				}
				// re-coloring
				head.color = BLACK;
				if (head.leftChild != null) head.leftChild.color = RED;
				if (head.rightChild != null) head.rightChild.color = RED;				
				
				
//				if (node.equals(node.parent.leftChild)) {
//					// case 1.2
//					rightRotation(node.parent);
//					if (brother(node).equals(uncle)) {
//						// case 1.4
//						leftRotation(node);
//					}
//				} else {
//					// case 1.1
//					leftRotation(node.parent);
//					if (brother(node).equals(uncle)) {
//						// case 1.3
//						rightRotation(node);
//					}
//				}
//				// re-coloring
//				if (node.parent != null) {
//					node.parent.color = BLACK;
//					if (node.parent.rightChild != null) node.parent.rightChild.color = RED;
//					if (node.parent.leftChild != null) node.parent.leftChild.color = RED;
//				}
			} else {
				// case 2
				RBNode grandParent = node.parent.parent;
				if (grandParent.leftChild != null) grandParent.leftChild.color = BLACK;
				if (grandParent.rightChild != null) grandParent.rightChild.color = BLACK;
				if (!grandParent.equals(root))
					grandParent.color = RED;
				else 
					grandParent.color = BLACK;
				fixInsert(grandParent);
			}
		}
	}

	private Object brother(RBNode node) {
		if (node == null || node.parent == null) {
			return null;
		}
		
		if (node.parent.leftChild.equals(node)) {
			return node.parent.rightChild;
		} else {
			return node.parent.leftChild;
		}
	}

	private void leftRotation(RBNode node) {
		if (node.parent == null) {
			return;
		}
		
		RBNode grandParent = node.parent.parent;
		RBNode parent = node.parent;
		
		parent.rightChild = node.leftChild;
		if (node.leftChild != null) node.leftChild.parent = parent;
		
		parent.parent = node;
		node.leftChild = parent;
		
		node.parent = grandParent;
		if (grandParent == null) root = node;
		else if (grandParent.leftChild.equals(parent))
			grandParent.leftChild = node;
		else
			grandParent.rightChild = node;
		
//		// move node's left child to parent as child (on the place where the node was under node's parent)
//		if (node.leftChild != null) node.leftChild.parent = node.parent;
//		node.parent.rightChild = node.leftChild;
//		
//		// move parent of node to left child of node
//		node.parent.parent = node;
//		node.leftChild = node.parent;
//		
//		// update refs from grand parent
//		node.parent = grandParent;
//		if (node.parent == null) root = node;
//		else if (node.leftChild.equals(grandParent.leftChild))
//			grandParent.leftChild = node;
//		else {
//			grandParent.rightChild = node;
//		}
	}

	private void rightRotation(RBNode node) {
		if (node.parent == null) {
			return;
		}
		
		RBNode grandParent = node.parent.parent;
		RBNode parent = node.parent;
		
		parent.leftChild = node.rightChild;
		if (node.rightChild != null) node.rightChild.parent = parent;
		
		parent.parent = node;
		node.rightChild = parent;
		
		node.parent = grandParent;
		if (grandParent == null) root = node;
		else if (grandParent.leftChild.equals(parent))
			grandParent.leftChild = node;
		else
			grandParent.rightChild = node;
//		
//		// move node's right child to parent as child (on the place where the node was under node's parent)
//		if (node.rightChild != null) node.rightChild.parent = node.parent;
//		node.parent.leftChild = node.rightChild;
//		
//		// move parent of node to right child of node
//		node.parent.parent = node;
//		node.rightChild = node.parent;
//		
//		// update refs from grand parent
//		node.parent = grandParent;
//		if (node.parent == null) root = node;
//		else if (node.rightChild.equals(grandParent.leftChild))
//			grandParent.leftChild = node;
//		else {
//			grandParent.rightChild = node;
//		}
	}

	private RBNode uncle(RBNode node) {
		if (node == null || node.parent == null || node.parent.parent == null) {
			return null;
		}
		RBNode grandParent = node.parent.parent;
		
		if (node.parent.equals(grandParent.leftChild)) {
			return grandParent.rightChild;
		} else {
			return grandParent.leftChild;
		}
	}

	private boolean isRed(RBNode node) {
		if (node == null) {
			return false;
		}
		return node.color == RED;
	}
	
	@Override
	public Value delete(Key key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Value find(Key key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*********************************************************************
    *  Check the RB tree
    **********************************************************************/
    public boolean check() {
        if (!isBST())            System.out.println("Not in symmetric order");
//	        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
//	        if (!isRankConsistent()) System.out.println("Ranks not consistent");
        if (!is23())             System.out.println("Not a 2-3 tree");
        if (!isBalanced())       System.out.println("Not balanced");
        return isBST() &&/* isSizeConsistent() && isRankConsistent() &&*/ is23() && isBalanced();
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return isBST(root, null, null);
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    // Credit: Bob Dondero's elegant solution
    private boolean isBST(RBNode x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.leftChild, min, x.key) && isBST(x.rightChild, x.key, max);
    } 

    // are the size fields correct?
//	    private boolean isSizeConsistent() { return isSizeConsistent(root); }
//	    private boolean isSizeConsistent(RBNode x) {
//	        if (x == null) return true;
//	        if (x.N != size(x.leftChild) + size(x.rightChild) + 1) return false;
//	        return isSizeConsistent(x.leftChild) && isSizeConsistent(x.rightChild);
//	    } 

    // check that ranks are consistent
//	    private boolean isRankConsistent() {
//	        for (int i = 0; i < size(); i++)
//	            if (i != rank(select(i))) return false;
//	        for (Key key : keys())
//	            if (key.compareTo(select(rank(key))) != 0) return false;
//	        return true;
//	    }

    // Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
    private boolean is23() { return is23(root); }
    private boolean is23(RBNode x) {
        if (x == null) return true;
//        if (isRed(x.rightChild)) return false;
        if (x != root && isRed(x) && isRed(x.leftChild))
            return false;
        return is23(x.leftChild) && is23(x.rightChild);
    } 

    // do all paths from root to leaf have same number of black edges?
    private boolean isBalanced() { 
        int black = 0;     // number of black links on path from root to min
        RBNode x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.leftChild;
        }
        return isBalanced(root, black);
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(RBNode x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.leftChild, black) && isBalanced(x.rightChild, black);
    } 
    
    /*********************************************************************
     *  Printing to the terminal
     *********************************************************************/
    
    public void printBetter() {
    	System.out.println("----------------------------------------");
		printHelper(root, 0);
	}
    
    protected static final int INDENT_STEP = 6;

	private void printHelper(RBNode n, int indent) {
		if (n == null) {
			System.out.print("<empty tree>\n");
			return;
		}
		if (n.rightChild != null) {
			printHelper(n.rightChild, indent + INDENT_STEP);
		}
		for (int i = 0; i < indent; i++)
			System.out.print(" ");
		if (n.color == BLACK)
			System.out.println(n.key);
		else
			System.out.println("<" + n.key + ">");
		if (n.leftChild != null) {
			printHelper(n.leftChild, indent + INDENT_STEP);
		}
	}
}
