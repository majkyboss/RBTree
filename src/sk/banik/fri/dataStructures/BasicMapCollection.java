package sk.banik.fri.dataStructures;

public interface BasicMapCollection<Key extends Comparable<Key>, Value>{
	void insert(Key key , Value value);
	Value delete(Key key);
	Value find(Key key);
	int size();
	boolean check();
};
