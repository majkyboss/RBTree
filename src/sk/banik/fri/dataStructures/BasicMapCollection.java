package sk.banik.fri.dataStructures;

import java.util.List;

public interface BasicMapCollection<Key extends Comparable<Key>, Value>{
	void insert(Key key , Value value);
	Value delete(Key key);
	Value find(Key key);
	int size();
	boolean check();

	List<Value> getSortedList();

	Iterable<Value> getValues();
};
