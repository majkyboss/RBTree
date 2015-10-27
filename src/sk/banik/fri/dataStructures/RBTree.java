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
		
		@Override
		public String toString() {
			return "RBNode: key="+key+", value="+value;
		}

		public RBNode brother() {
			if (parent == null) {
				return null;
			}
			
			if (this.equals(parent.leftChild)) {
				return parent.rightChild;
			} else {
				return parent.leftChild;
			}
		}
		
		public RBNode grandparent() {
		    assert parent != null;
		    assert parent.parent != null;
		    return parent.parent;
		}
		
		public RBNode uncle() {
		    assert parent != null;
		    assert parent.parent != null;
		    return parent.brother();
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
		else if (grandParent.leftChild != null && grandParent.leftChild.equals(parent))
			grandParent.leftChild = node;
		else
			grandParent.rightChild = node;
		
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
		else if (grandParent.leftChild != null && grandParent.leftChild.equals(parent))
			grandParent.leftChild = node;
		else
			grandParent.rightChild = node;
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
		RBNode deleted = deleteNode(key);
		if (deleted != null) {
			return deleted.value;
		}
		return null;
	}
	
	public RBNode deleteNode(Key key) {
		// find the node
		RBNode toDel = findNode(key);
		if (toDel == null) {
			// tree not contains key
			return null;
		}
		
		// actually toDel replaced by this and toDel deleted (toDel not connected to tree)
		RBNode deleted = null;
		RBNode brother = null;
		int LEFT_CHILD = 1;
		int RIGHT_CHILD = 2;
		int NO_PARENT = -1;
		int deletedChildhood = NO_PARENT;
		
		// BST delete ----------------------------
		{
			if (toDel.rightChild == null && toDel.leftChild == null) {
				// toDel is leaf - basic delete - cut the leaf
				deleted = toDel;
				
				if (toDel.equals(root)) {
					root = null;
				} else {
					if (deleted.equals(deleted.parent.leftChild)){
						brother = deleted.parent.rightChild;
						deletedChildhood = LEFT_CHILD;
					} else {
						brother = deleted.parent.leftChild;
						deletedChildhood = RIGHT_CHILD;
					}
					
					// disconnect node from parent
					if (toDel.parent.leftChild != null && toDel.parent.leftChild.equals(toDel)) {
						toDel.parent.leftChild = null;
					} else {
						toDel.parent.rightChild = null;
					}
				}
			} else if (toDel.rightChild == null) {
				// replace by left subtree
				deleted = toDel.leftChild;
				if (deleted.equals(deleted.parent.leftChild)){
					brother = deleted.parent.rightChild;
					deletedChildhood = LEFT_CHILD;
				} else {
					brother = deleted.parent.leftChild;
					deletedChildhood = RIGHT_CHILD;
				}
				
				if (toDel.equals(root)) {
					root = toDel.leftChild;
					toDel.leftChild.parent = null;
				} else {
					if (toDel.parent.leftChild != null && toDel.parent.leftChild.equals(toDel))
						toDel.parent.leftChild = toDel.leftChild;
					else {
						toDel.parent.rightChild = toDel.leftChild;
					}
					toDel.leftChild.parent = toDel.parent;
				}

			} else if (toDel.leftChild == null) {
				// replace by right subtree
				deleted = toDel.rightChild;
				if (deleted.equals(deleted.parent.leftChild)) brother = deleted.parent.rightChild;
				else brother = deleted.parent.leftChild;
				
				if (toDel.equals(root)) {
					root = toDel.rightChild;
					toDel.rightChild.parent = null;
				} else {
					toDel.rightChild.parent = toDel.parent;
					if (toDel.parent.leftChild != null && toDel.parent.leftChild.equals(toDel))
						toDel.parent.leftChild = toDel.rightChild;
					else {
						toDel.parent.rightChild = toDel.rightChild;
					}
					toDel.rightChild.parent = toDel.parent;
				}
			} else {
				// left and righ != null
				// replace with min of right subtree

				// get min of right
				RBNode minRight = min(toDel.rightChild);
				deleted = minRight;
				if (deleted.equals(deleted.parent.leftChild)) brother = deleted.parent.rightChild;
				else brother = deleted.parent.leftChild;
				
				if (minRight.equals(toDel.rightChild)) {
					// do not disconnect if minRight is first right child
					// only to replace it
					// assert that there is no left child of minRight because it should be minimum
					
					// TODO if else is same like in next else, also check other occurrences
					if (toDel.equals(root)) {
						root = minRight;
					} else {
						if (toDel.parent.leftChild != null && toDel.parent.leftChild.equals(toDel))
							toDel.parent.leftChild = minRight;
						else
							toDel.parent.rightChild = minRight;
					}
					
					minRight.parent = toDel.parent;
					minRight.leftChild = toDel.leftChild;
					toDel.leftChild.parent = minRight;
					// right child will be same
				} else {
					// disconnect min from parent reconnect child of min to parent
					minRight.parent.leftChild = minRight.rightChild;
					if (minRight.rightChild != null)
						minRight.rightChild.parent = minRight.parent;
						
					// replace toDel with min of right
					minRight.rightChild = toDel.rightChild;
					toDel.rightChild.parent = minRight;
					minRight.leftChild = toDel.leftChild;
					toDel.leftChild.parent = minRight;
					minRight.parent = toDel.parent;
					if (toDel.equals(root)) {
						root = minRight;
					} else {
						if (toDel.parent.leftChild != null && toDel.parent.leftChild.equals(toDel))
							toDel.parent.leftChild = minRight;
						else
							toDel.parent.rightChild = minRight;
					}
				}
			}
		}
		// ---------------------------------------
		
		if (isRed(deleted))
			return toDel;
		
		// RB delete fix
		// TODO implements RB delete
//		RBNode n = deleted;
		
		
		
		return toDel;
	}
	
	private RBNode min(RBNode node) {
		if (node == null) {
			throw new NullPointerException();
		}
		
		RBNode min = node;
		while (min.leftChild != null){
			min = min.leftChild;
		}

		return min;
	}
	
	private RBNode max(RBNode node) {
		if (node == null) {
			throw new NullPointerException();
		}
		
		RBNode max = node;
		while (max.rightChild != null){
			max = max.rightChild;
		}

		return max;
	}

	@Override
	public Value find(Key key) {
		RBNode node = this.findNode(key);
		if (node != null) return node.value;
		else return null;
	}
	
	private RBNode findNode(Key key) {
		if (key == null) {
			throw new NullPointerException();
		}
		
		RBNode node = root;
		while (node != null && !node.key.equals(key)){
			int cmp = node.key.compareTo(key) * (-1);
			// make cmp opposite because node is compared with key,
			// not key compared with node
			if (cmp < 0) {
				node = node.leftChild;
			} else if (cmp > 0) {
				node = node.rightChild;
			}
		}
		
		return node;
	}
	
	@Override
	public int size() {
		return size(root);
	}
	
	private int size(RBNode node){
		if (node == null) {
			return 0;
		}
		
		int i = 1;
		i += size(node.leftChild);
		i += size(node.rightChild);
		
		return i;
	}
	
	/*********************************************************************
    *  Check the RB tree
    **********************************************************************/
	
    public boolean check() {
		if (!isBST())                    System.out.println("Not in symmetric order");
		// is root RED
        if (isRed(root))                System.out.println("The root is not BLACK");
		// every RED node has only BLACK nodes
        if (!redsHaveOnlyBLACKs(root))   System.out.println("Two red nodes in a row");
		// balanced - the count of BLACK nodes on the path to the list is same in whole tree
    	if (!isBalanced())               System.out.println("Not balanced");
    	
    	return isBST() && !isRed(root) && redsHaveOnlyBLACKs(root) && isBalanced();
    }

    private boolean redsHaveOnlyBLACKs(RBNode node) {
		if (node == null)
			return true;
		if (node != root && isRed(node) && isRed(node.leftChild) ||
				node != root && isRed(node) && isRed(node.rightChild))
			return false;
		return redsHaveOnlyBLACKs(node.leftChild) && redsHaveOnlyBLACKs(node.rightChild);
	}

    private boolean isBST() {
        return isBST(root, null, null);
    }

    private boolean isBST(RBNode x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.leftChild, min, x.key) && isBST(x.rightChild, x.key, max);
    }

    // check that ranks are consistent
//	    private boolean isRankConsistent() {
//	        for (int i = 0; i < size(); i++)
//	            if (i != rank(select(i))) return false;
//	        for (Key key : keys())
//	            if (key.compareTo(select(rank(key))) != 0) return false;
//	        return true;
//	    }

    private boolean isBalanced() { 
        int black = 0;     // number of black links on path from root to min
        RBNode x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.leftChild;
        }
        return isBalanced(root, black);
    }
    
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
