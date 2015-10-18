package sk.banik.fri.dataStructures.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import sk.banik.fri.dataStructures.RBTree;

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
        RBTree<String, Integer> st = new RBTree<String, Integer>();
        for (int i = 0; i < args.length; i++) {
            String key = args[i];
            st.insert(key, i);
        }
        st.printBetter();
        boolean checkStatus = st.check();
        System.out.println("is RB Tree? : " + checkStatus);
        assertTrue(checkStatus);
	}

	@Test
	public void bigTest() throws Exception {
		// insert pair <key, value>
		// check if tree contains inserted node
		// check the consistency - if children is bigger and less and if parent is
		// check if size is good
		
		// do this many times for random values
		// and remember generated values
	}
}
