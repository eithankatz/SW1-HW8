package il.ac.tau.cs.sw1.ex8.histogram;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**************************************
 *  Add your code to this class !!!   *
 **************************************/
public class HashMapHistogram<T extends Comparable<T>> implements IHistogram<T>
{
	//HashSet<T> items;				//List containing all items 
	Map<T, Integer> counts; //Key is item, value is counter of key

	public HashMapHistogram()
    {
		counts = new HashMap<T, Integer>();
    }
	
	@Override
	public void addItem(T item) 
	{
		if(counts.containsKey(item))	//Update existing item
		{
			counts.put(item, counts.get(item) + 1);
		}
		
		else	//Add new item
		{
			counts.put(item, 1);
		}
	}
	
	@Override
	public boolean removeItem(T item)  
	{
		if(this.counts.containsKey(item))	//Item removed
		{
			if(this.counts.get(item) == 1)
			{
				this.counts.remove(item);
			}
			else
			{
				this.counts.put(item, this.counts.get(item) - 1);
			}
			return true;
		}
		else
			return false;	//Item wasn't in histogram
	}
	
	@Override
	public void addAll(Collection<T> items) 
	{
		for(T item : items) 
		{
			addItem(item);
		}
	}

	@Override
	public int getCountForItem(T item) 
	{
		if(counts.containsKey(item))
		{
			return counts.get(item);
		}
		else
		{
			return 0; 
		}
	}

	@Override
	public void clear() 
	{
		counts = new HashMap<T, Integer>();
	}

	@Override
	public Set<T> getItemsSet() 
	{
		Set<T> items_set = new HashSet<T> (); 
		for(T item : this.counts.keySet()) 
		{
			items_set.add(item);
		}
		return items_set;
	}
	
	@Override
	public int getCountsSum() 
	{
		int sum = 0;
		for(Integer count : counts.values()) 
		{
			sum += count;
		}
		return sum;
	}
	
	public int getSize() 
	{
		return counts.size();
	}
	
	public int getValue(T key) 
	{
		if(counts.containsKey(key))
		{
			return counts.get(key);
		}
		else
		{
			return 0;
		}
	}
	
	@Override
	public Iterator<Entry<T, Integer>> iterator() 
	{
		return new HashMapHistogramIterator<>(this);
	}
	//add private methods here, if needed

	public boolean containsKey(T key) 
	{
		return this.counts.containsKey(key);
	}
}
