package sk.banik.fri.dataStructures;

import java.util.LinkedList;
import java.util.NoSuchElementException;


public class RBTreeOld<Key extends Comparable<Key>, Value> implements BasicMapCollection<Key, Value>{

    private static final boolean RED   = true;
    private static final boolean BLACK = false;

    private Node root;

    private class Node extends AbstractNode{
        private Key key;
        private Value value;
        private Node leftChild, rightChild;
        private boolean color;
        private int N; // subtree count

        public Node(Key key, Value val, boolean color, int N) {
            this.key = key;
            this.value = val;
            this.color = color;
            this.N = N;
        }
    }
    
    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.N;
    } 

    public int size() {
        return size(root);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public boolean contains(Key key) {
        return find(key) != null;
    }

    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.leftChild) && !isRed(root.rightChild))
            root.color = RED;

        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK;
        // assert check();
    }

    // delete the key-value pair with the minimum key rooted at h
    private Node deleteMin(Node h) { 
        if (h.leftChild == null)
            return null;

        if (!isRed(h.leftChild) && !isRed(h.leftChild.leftChild))
            h = moveRedLeft(h);

        h.leftChild = deleteMin(h.leftChild);
        return balance(h);
    }

    public void deleteMax() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.leftChild) && !isRed(root.rightChild))
            root.color = RED;

        root = deleteMax(root);
        if (!isEmpty()) root.color = BLACK;
        // assert check();
    }

    // delete the key-value pair with the maximum key rooted at h
    private Node deleteMax(Node h) { 
        if (isRed(h.leftChild))
            h = rotateRight(h);

        if (h.rightChild == null)
            return null;

        if (!isRed(h.rightChild) && !isRed(h.rightChild.leftChild))
            h = moveRedRight(h);

        h.rightChild = deleteMax(h.rightChild);

        return balance(h);
    }

    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        // assert (h != null) && isRed(h.left);
        Node x = h.leftChild;
        h.leftChild = x.rightChild;
        x.rightChild = h;
        x.color = x.rightChild.color;
        x.rightChild.color = RED;
        x.N = h.N;
        h.N = size(h.leftChild) + size(h.rightChild) + 1;
        return x;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        // assert (h != null) && isRed(h.right);
        Node x = h.rightChild;
        h.rightChild = x.leftChild;
        x.leftChild = h;
        x.color = x.leftChild.color;
        x.leftChild.color = RED;
        x.N = h.N;
        h.N = size(h.leftChild) + size(h.rightChild) + 1;
        return x;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        // h must have opposite color of its two children
        // assert (h != null) && (h.left != null) && (h.right != null);
        // assert (!isRed(h) &&  isRed(h.left) &&  isRed(h.right))
        //    || (isRed(h)  && !isRed(h.left) && !isRed(h.right));
        h.color = !h.color;
        h.leftChild.color = !h.leftChild.color;
        h.rightChild.color = !h.rightChild.color;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

        flipColors(h);
        if (isRed(h.rightChild.leftChild)) { 
            h.rightChild = rotateRight(h.rightChild);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
        flipColors(h);
        if (isRed(h.leftChild.leftChild)) { 
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    // restore red-black tree invariant
    private Node balance(Node h) {
        // assert (h != null);

        if (isRed(h.rightChild))                      h = rotateLeft(h);
        if (isRed(h.leftChild) && isRed(h.leftChild.leftChild)) h = rotateRight(h);
        if (isRed(h.leftChild) && isRed(h.rightChild))     flipColors(h);

        h.N = size(h.leftChild) + size(h.rightChild) + 1;
        return h;
    }

    public int height() {
        return height(root);
    }
    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.leftChild), height(x.rightChild));
    }

    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("called min() with empty symbol table");
        return min(root).key;
    } 

    // the smallest key in subtree rooted at x; null if no such key
    private Node min(Node x) { 
        // assert x != null;
        if (x.leftChild == null) return x; 
        else                return min(x.leftChild); 
    } 

    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("called max() with empty symbol table");
        return max(root).key;
    } 

    // the largest key in the subtree rooted at x; null if no such key
    private Node max(Node x) { 
        // assert x != null;
        if (x.rightChild == null) return x; 
        else                 return max(x.rightChild); 
    } 


    /**
     * Returns the largest key in the symbol table less than or equal to <tt>key</tt>.
     * @param key the key
     * @return the largest key in the symbol table less than or equal to <tt>key</tt>
     * @throws NoSuchElementException if there is no such key
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public Key floor(Key key) {
        if (isEmpty()) throw new NoSuchElementException("called floor() with empty symbol table");
        Node x = floor(root, key);
        if (x == null) return null;
        else           return x.key;
    }    

    // the largest key in the subtree rooted at x less than or equal to the given key
    private Node floor(Node x, Key key) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp < 0)  return floor(x.leftChild, key);
        Node t = floor(x.rightChild, key);
        if (t != null) return t; 
        else           return x;
    }

    /**
     * Returns the smallest key in the symbol table greater than or equal to <tt>key</tt>.
     * @param key the key
     * @return the smallest key in the symbol table greater than or equal to <tt>key</tt>
     * @throws NoSuchElementException if there is no such key
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public Key ceiling(Key key) {  
        if (isEmpty()) throw new NoSuchElementException("called ceiling() with empty symbol table");
        Node x = ceiling(root, key);
        if (x == null) return null;
        else           return x.key;  
    }

    // the smallest key in the subtree rooted at x greater than or equal to the given key
    private Node ceiling(Node x, Key key) {  
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if (cmp == 0) return x;
        if (cmp > 0)  return ceiling(x.rightChild, key);
        Node t = ceiling(x.leftChild, key);
        if (t != null) return t; 
        else           return x;
    }

    /**
     * Return the kth smallest key in the symbol table.
     * @param k the order statistic
     * @return the kth smallest key in the symbol table
     * @throws IllegalArgumentException unless <tt>k</tt> is between 0 and
     *     <em>N</em> &minus; 1
     */
    public Key select(int k) {
        if (k < 0 || k >= size()) throw new IllegalArgumentException();
        Node x = select(root, k);
        return x.key;
    }

    // the key of rank k in the subtree rooted at x
    private Node select(Node x, int k) {
        // assert x != null;
        // assert k >= 0 && k < size(x);
        int t = size(x.leftChild); 
        if      (t > k) return select(x.leftChild,  k); 
        else if (t < k) return select(x.rightChild, k-t-1); 
        else            return x; 
    } 

    /**
     * Return the number of keys in the symbol table strictly less than <tt>key</tt>.
     * @param key the key
     * @return the number of keys in the symbol table strictly less than <tt>key</tt>
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public int rank(Key key) {
        return rank(key, root);
    } 

    // number of keys less than key in the subtree rooted at x
    private int rank(Key key, Node x) {
        if (x == null) return 0; 
        int cmp = key.compareTo(x.key); 
        if      (cmp < 0) return rank(key, x.leftChild); 
        else if (cmp > 0) return 1 + size(x.leftChild) + rank(key, x.rightChild); 
        else              return size(x.leftChild); 
    } 

    public Iterable<Key> keys() {
        return keys(min(), max());
    }

    public Iterable<Key> keys(Key lo, Key hi) {
        LinkedList<Key> queue = new LinkedList<Key>();
        // if (isEmpty() || lo.compareTo(hi) > 0) return queue;
        keys(root, queue, lo, hi);
        return queue;
    } 

    // add the keys between lo and hi in the subtree rooted at x
    // to the queue
    private void keys(Node x, LinkedList<Key> queue, Key lo, Key hi) { 
        if (x == null) return; 
        int cmplo = lo.compareTo(x.key); 
        int cmphi = hi.compareTo(x.key); 
        if (cmplo < 0) keys(x.leftChild, queue, lo, hi); 
        if (cmplo <= 0 && cmphi >= 0) queue.add(x.key); 
        if (cmphi > 0) keys(x.rightChild, queue, lo, hi); 
    } 

    public int size(Key lo, Key hi) {
        if (lo.compareTo(hi) > 0) return 0;
        if (contains(hi)) return rank(hi) - rank(lo) + 1;
        else              return rank(hi) - rank(lo);
    }


   /***************************************************************************
    *  Check the RB tree
    ***************************************************************************/
    public boolean check() {
        if (!isBST())            System.out.println("Not in symmetric order");
        if (!isSizeConsistent()) System.out.println("Subtree counts not consistent");
        if (!isRankConsistent()) System.out.println("Ranks not consistent");
        if (!is23())             System.out.println("Not a 2-3 tree");
        if (!isBalanced())       System.out.println("Not balanced");
        return isBST() && isSizeConsistent() && isRankConsistent() && is23() && isBalanced();
    }

    private boolean isBST() {
        return isBST(root, null, null);
    }

    private boolean isBST(Node x, Key min, Key max) {
        if (x == null) return true;
        if (min != null && x.key.compareTo(min) <= 0) return false;
        if (max != null && x.key.compareTo(max) >= 0) return false;
        return isBST(x.leftChild, min, x.key) && isBST(x.rightChild, x.key, max);
    } 

    private boolean isSizeConsistent() { return isSizeConsistent(root); }
    private boolean isSizeConsistent(Node x) {
        if (x == null) return true;
        if (x.N != size(x.leftChild) + size(x.rightChild) + 1) return false;
        return isSizeConsistent(x.leftChild) && isSizeConsistent(x.rightChild);
    } 

    private boolean isRankConsistent() {
        for (int i = 0; i < size(); i++)
            if (i != rank(select(i))) return false;
        for (Key key : keys())
            if (key.compareTo(select(rank(key))) != 0) return false;
        return true;
    }

    private boolean is23() { return is23(root); }
    private boolean is23(Node x) {
        if (x == null) return true;
        if (isRed(x.rightChild)) return false;
        if (x != root && isRed(x) && isRed(x.leftChild))
            return false;
        return is23(x.leftChild) && is23(x.rightChild);
    } 

    private boolean isBalanced() { 
        int black = 0;
        Node x = root;
        while (x != null) {
            if (!isRed(x)) black++;
            x = x.leftChild;
        }
        return isBalanced(root, black);
    }

    private boolean isBalanced(Node x, int black) {
        if (x == null) return black == 0;
        if (!isRed(x)) black--;
        return isBalanced(x.leftChild, black) && isBalanced(x.rightChild, black);
    } 
    
    public void printBetter() {
		printHelper(root, 0);
	}
    
    protected static final int INDENT_STEP = 6;

	private void printHelper(Node n, int indent) {
		if (n == null) {
			System.out.print("<empty tree>");
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

	@Override
	public void insert(Key key, Value val) {
        root = insert(root, key, val);
        root.color = BLACK;
        // assert check();
    }
	
    private Node insert(Node h, Key key, Value val) { 
        if (h == null) return new Node(key, val, RED, 1);

        int cmp = key.compareTo(h.key);
        if      (cmp < 0) h.leftChild  = insert(h.leftChild,  key, val); 
        else if (cmp > 0) h.rightChild = insert(h.rightChild, key, val); 
        else              h.value   = val;

        // fix-up any right-leaning links
        if (isRed(h.rightChild) && !isRed(h.leftChild))      h = rotateLeft(h);
        if (isRed(h.leftChild)  &&  isRed(h.leftChild.leftChild)) h = rotateRight(h);
        if (isRed(h.leftChild)  &&  isRed(h.rightChild))     flipColors(h);
        h.N = size(h.leftChild) + size(h.rightChild) + 1;

        return h;
    }

	@Override
	public Value delete(Key key) {
		if (!contains(key)) {
            System.err.println("symbol table does not contain " + key);
            return null;
        }
		
		Value valueToDel = find(key);

        // if both children of root are black, set root to red
        if (!isRed(root.leftChild) && !isRed(root.rightChild))
            root.color = RED;

        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;
        // assert check();
        
        if (contains(key)) {
			return valueToDel;
		}
        
        return null;
	}
	
	private Node delete(Node h, Key key) { 
        // assert get(h, key) != null;

        if (key.compareTo(h.key) < 0)  {
            if (!isRed(h.leftChild) && !isRed(h.leftChild.leftChild))
                h = moveRedLeft(h);
            h.leftChild = delete(h.leftChild, key);
        }
        else {
            if (isRed(h.leftChild))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.rightChild == null))
                return null;
            if (!isRed(h.rightChild) && !isRed(h.rightChild.leftChild))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.rightChild);
                h.key = x.key;
                h.value = x.value;
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.rightChild = deleteMin(h.rightChild);
            }
            else h.rightChild = delete(h.rightChild, key);
        }
        return balance(h);
    }

	@Override
	public Value find(Key key) {
		Node node = this.findNode(key);
		if (node != null) return node.value;
		else return null;
	}
	
	private Node findNode(Key key) {
		if (key == null) {
			throw new NullPointerException();
		}
		
		Node node = root;
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
}

