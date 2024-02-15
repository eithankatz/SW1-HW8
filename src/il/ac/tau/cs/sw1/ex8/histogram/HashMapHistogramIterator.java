package il.ac.tau.cs.sw1.ex8.histogram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;


/**************************************
 *  Add your code to this class !!!   *
 **************************************/

public class HashMapHistogramIterator<T extends Comparable<T>> implements Iterator<Map.Entry<T, Integer>>
{
	
	//add members here
	HashMapHistogram<T> hmh;
	HashMapHistogramComparator<T> hmhc;
	int nextIndex;
	List<Map.Entry<T, Integer>> entryList;
	
	//add constructor here, if needed
	public HashMapHistogramIterator(HashMapHistogram<T> h) 
	{
		this.hmh = h;
		this.entryList = new ArrayList<>();
		this.hmhc = new HashMapHistogramComparator<T>(h);
		Set<Map.Entry<T, Integer>> entries = hmh.counts.entrySet();
		for (Map.Entry<T, Integer> item: entries) 
		{
			this.entryList.add(item);
		}
		Collections.sort(this.entryList, this.hmhc);
		this.nextIndex = 0;
	}
	
	@Override
	public boolean hasNext() 
	{
		return this.nextIndex < this.entryList.size();
	}

	@Override
	public Map.Entry<T, Integer> next() 
	{
		if (this.hasNext()) 
		{
			Entry<T, Integer> next = this.entryList.get(nextIndex);
			this.nextIndex++;
			return next;
		}
		else 
		{
			throw new NoSuchElementException();
		}
	}

	@Override
	public void remove() 
	{
		throw new UnsupportedOperationException();
	}
	
	//add private methods here, if needed
}
