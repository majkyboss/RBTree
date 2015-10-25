package sk.banik.fri.dataStructures.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Iterator;

import org.junit.Test;

import sk.banik.fri.dataStructures.RBTree;
import sk.banik.fri.dataStructures.RBTree.RBNode;

public class TestRBTree {

	@Test
	public void test() {
		String[] args = new String[]{"S"
				, "E"
				, "A"
				, "R"
				, "C"
				, "H"
				, "E"
				, "X"
				, "A"
				, "M"
				, "P"
				, "L"
				, "E"};
        RBTree<String, Integer> tree = new RBTree<String, Integer>();
        for (int i = 0; i < args.length; i++) {
            String key = args[i];
            tree.insert(key, i);
        }
        tree.printBetter();
        boolean checkStatus = tree.check();
        System.out.println("is RB Tree? : " + checkStatus);
        assertTrue(checkStatus);
	}

	@Test
	public void testInsert() throws Exception {
		RBTree<Integer, String> rbTree = new RBTree<Integer, String>();
		HashMap<Integer, String> backupStructure = new HashMap<Integer, String>();
		int maxTestedCount = 10*10;
		
		do {
			// insert pair <key, value>
			
			Double dKey = (Math.random() * maxTestedCount * maxTestedCount);
			while (backupStructure.containsKey(dKey.intValue())) {
				dKey = Math.random();
			}
			String val = "" + dKey.intValue();
			rbTree.insert(dKey.intValue(), val);
			backupStructure.put(dKey.intValue(), val);
			// check if tree contains inserted node
			String foundValue = rbTree.find(dKey.intValue());
			assertNotNull(foundValue);
			assertTrue(foundValue.equals(val));
			// check the consistency - if children is bigger and less and if parent is
			Method findNodeMethod = rbTree.getClass().getDeclaredMethod(
					"findNode", Comparable.class);
			findNodeMethod.setAccessible(true);
			Object returnedValue = findNodeMethod.invoke(rbTree,
					dKey.intValue());
			RBNode foundNode = (RBNode) returnedValue;
			if (foundNode.leftChild != null)
				assertTrue(foundNode.key.compareTo(foundNode.leftChild.key) > 0);
			if (foundNode.rightChild != null)
				assertTrue(foundNode.key.compareTo(foundNode.rightChild.key) < 0);
			// check if size is good
			assertEquals(rbTree.size(), backupStructure.size());
			// general check
			assertTrue(rbTree.check());
			
			// do this many times for random values
			// and remember generated values
		} while (backupStructure.size() < maxTestedCount);
	}
	
	@Test
	public void testBstDelete() throws Exception {
		RBTree<Integer, String> rbTree = new RBTree<Integer, String>();
		HashMap<Integer, String> backupStructure = new HashMap<Integer, String>();
		int maxTestedCount = 5;
		
		do {
			// insert pair <key, value>
			Double dKey = (Math.random() * maxTestedCount * maxTestedCount);
			while (backupStructure.containsKey(dKey.intValue())) {
				dKey = Math.random();
			}
			String val = "" + dKey.intValue();
			rbTree.insert(dKey.intValue(), val);
			backupStructure.put(dKey.intValue(), val);
		} while (backupStructure.size() < maxTestedCount);

		Object[] array = backupStructure.keySet().toArray();
		for (int i = 0; i < array.length; i++) {
			int key = (int) array[i];
			// try to remove node with key
			String deletedValue= rbTree.delete(key);
			String expectedValue = backupStructure.remove(key);
			assertEquals(expectedValue, deletedValue);
			
			// check if tree contains inserted node
			String foundValue = rbTree.find(key);
			assertNull(foundValue);
			
			// check if size is good
			assertEquals(rbTree.size(), backupStructure.size());
			// general check
			assertTrue(rbTree.check());
			
			// do this that much how much values are in backup structure
		}
	}
}
