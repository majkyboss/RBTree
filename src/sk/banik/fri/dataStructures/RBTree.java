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

		public RBNode(Key key, Value value, boolean color) {
			this.key = key;
			this.value = value;
			this.color = color;
		}
		
	}

	@Override
	public void insert(Key key, Value value) {
		if (root == null) {
			root = new RBNode(key, value, BLACK);
			return;
		}
		
		RBNode currentNode = root,
				parentNode = null,
				node = null,
				newNode = null;
		do {
			node = null;
			int cmp = key.compareTo(currentNode.key);
			if (cmp < 0) {
				// go to left child
				if (currentNode.leftChild == null){
					currentNode.leftChild = newNode = new RBNode(key, value, RED);
					break;
				} else {
					node = currentNode.leftChild;
				}
			} else if (cmp > 0) {
				// go to right child
				if (currentNode.rightChild == null) {
					currentNode.rightChild = newNode = new RBNode(key, value, RED);
					break;
				} else{
					node = currentNode.rightChild;
				}
			} else {
				// found already inserted value
				currentNode.value = value;
				// TODO test not sure if re-color when updating, also returning not sure
				// most probably because the value is only changing the structure is not chaged
				// currentNode.color = RED;
				return;
			}
			parentNode = currentNode;
			currentNode = node;
		} while (node != null);
		
		// current node is actually parent node (see got to right and left)
		if (parentNode != null && isRed(currentNode)) {
			// fix irregularity
			fixInsert(parentNode, currentNode, newNode);
		}
	}
	
	private void fixInsert(RBNode parentNode, RBNode currentNode, RBNode newNode) {
		if (parentNode.leftChild != null && parentNode.leftChild.equals(currentNode)) {
			// from materials 1.1 1.4
			RBNode currentBorther = parentNode.rightChild;
			if (!isRed(currentBorther)) {
				
			}
		} else if (parentNode.rightChild != null && parentNode.rightChild.equals(currentNode)) {
			// from materials 1.2 1.3
			
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

}
