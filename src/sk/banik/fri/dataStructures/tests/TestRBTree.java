package sk.banik.fri.dataStructures.tests;

import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
				dKey = (Math.random() * maxTestedCount * maxTestedCount);
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
		int maxTestedCount = 100;
		
		// DEBIG
//		LinkedList<Integer> inputs = new LinkedList<Integer>();
//		inputs.addAll(Arrays.asList(0,1285,4622,1812,1557,7966,1826,6947,5156,6180,6438,9510,5927,42,2348,5423,5938,1591,9015,1849,7231,4416,6464,3392,4673,68,5956,9552,1617,2388,9049,1113,5978,5979,9308,5982,2146,6754,3430,3688,4972,621,8046,6003,3702,7031,6268,4989,2688,6784,641,9346,5252,2437,2949,2951,5768,3982,4750,3983,3984,8595,3989,3228,413,5022,8862,2975,7071,3244,8620,8111,5554,5042,703,3264,2756,4037,9413,2501,4807,4296,2249,1482,7627,8909,3533,3535,3792,988,6623,8672,4321,6371,230,9193,5866,8436,4598,9466));
		//
		
		do {
			// insert pair <key, value>
			Double dKey = (Math.random() * maxTestedCount * maxTestedCount);
			while (backupStructure.containsKey(dKey.intValue())) {
				dKey = (Math.random() * maxTestedCount * maxTestedCount);
			}
			
			// DEBIG
//			dKey = new Double(inputs.remove(0));
			//
			
			String val = "" + dKey.intValue();
			rbTree.insert(dKey.intValue(), val);
			backupStructure.put(dKey.intValue(), val);
		} while (backupStructure.size() < maxTestedCount);
		
		// DEBIG
//		rbTree.printBetter();
//		List<Integer> list = new LinkedList<Integer>();
//		list.addAll(backupStructure.keySet());
////		printList(list);
//		int lastKey = -1;
		//
		
		Object[] array = backupStructure.keySet().toArray();
//		try {
			for (int i = 0; i < array.length; i++) {
				int key = (int) array[i];
				
				// DEBIG
//				lastKey = key;
//				if (key == 2756) {
//					System.out.println();
//				}
				//
				
				// try to remove node with key
				String deletedValue= rbTree.delete(key);
//				rbTree.printBetter();
				String expectedValue = backupStructure.remove(key);
				assertEquals(expectedValue, deletedValue);
				
				// check if tree contains inserted node
				String foundValue = rbTree.find(key);
				
				// DEBIG
//				if (foundValue != null){
//					System.out.println("not null");
//					printList(list);
//					System.out.println(lastKey);
//				}
				//
				
				assertNull(foundValue);
				
				// check if size is good
//				try {
					rbTree.size();
//				} catch (Exception e) {
//					System.out.println("size");
//					printList(list);
//					System.out.println(lastKey);
//					throw e;
//				}
				assertEquals(rbTree.size(), backupStructure.size());
				// general check
				// assertTrue(rbTree.check());
				
				// do this that much how much values are in backup structure
			}
//		} catch (Exception e) {
//			System.out.println("last catch");
//			printList(list);
//			System.out.println(lastKey);
//			throw e;
//		}
	}
	
	private void printList(List<Integer> list) {
		if (list == null)
			return;
		for (int i = 0; i < list.size(); i++) {
			System.out.print(list.get(i) + " ");
		}
		System.out.println();
	}
	
	@Test
	public void testDeleteIntensive() throws Exception {
		for (int i = 0; i < 100000; i++) {
			testBstDelete();
			if ((i % 10) == 0)
				System.out.println(i);
		}
	}
}
