package il.ac.tau.cs.sw1.ex8.histogram;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class HashMapHistogramComparator<T extends Comparable<T>> implements Comparator<Map.Entry<T, Integer>> 
{
	HashMapHistogram<T> hmh;
	
	public HashMapHistogramComparator(HashMapHistogram<T> h) 
	{
		this.hmh = h;
	}
	
	@Override
	public int compare(Entry<T, Integer> e1, Entry<T, Integer> e2) 
	{
		return e1.getKey().compareTo(e2.getKey());
	}
}