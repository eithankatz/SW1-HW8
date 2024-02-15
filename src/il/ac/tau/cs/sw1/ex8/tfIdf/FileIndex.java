package il.ac.tau.cs.sw1.ex8.tfIdf;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import il.ac.tau.cs.sw1.ex8.histogram.HashMapHistogram;



/**************************************
 *  Add your code to this class !!!   *
 **************************************/

public class FileIndex 
{
	
	private boolean isInitialized = false;
	private HashMap<String, HashMapHistogram<String>> file2hmh;
	private HashMap<String, LinkedList<Map.Entry<String, Double>>> file2sorted;
	private HashMapHistogram<String> allWords;

	
	public FileIndex() 
	{
		this.file2hmh = new HashMap<String, HashMapHistogram<String>>();
		this.file2sorted = new HashMap<String, LinkedList<Map.Entry<String, Double>>>();
		this.allWords = new HashMapHistogram<>();
	}

	//Add members here

	/*
	 * @pre: the directory is no empty, and contains only readable text files
	 * @pre: isInitialized() == false;
	 */
  	public void indexDirectory(String folderPath) 
  	{ //Q1
		File folder = new File(folderPath);
		File[] listFiles = folder.listFiles();
		
		for (File file : listFiles) 
		{
			// for every file in the folder
			if (file.isFile()) 
			{
				/*******************/
				try 
				{
					List<String> allTokens = FileUtils.readAllTokens(file);
					HashMapHistogram<String> hmh = new HashMapHistogram<String>();
					hmh.addAll(allTokens);
					this.allWords.addAll(allTokens);
					this.file2hmh.put(file.getName(), hmh);
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
				/*******************/
			}
		}
		
		for (File file : listFiles) 
		{
			// for every file in the folder
			if (file.isFile()) 
			{
				LinkedList<Map.Entry<String, Double>> lst = new LinkedList<Map.Entry<String, Double>>();
				double tfidf = 0;
				for(Entry<String, Integer> item : file2hmh.get(file.getName()))
				{
					// for every word in document histogram
					String word = item.getKey();
					try 
					{
						tfidf = getTFIDF(word, file.getName());
					} 
					catch (FileIndexException e) 
					{
						// TODO Auto-generated catch block
						// e.printStackTrace();
					}
					Map.Entry<String, Double> tfidfItem = new AbstractMap.SimpleEntry<String, Double>(word, tfidf);
					lst.add(tfidfItem);
				}

				Collections.sort(lst,new Comparator<Map.Entry<String, Double>>()
				{
					public int compare(Entry<String, Double> e1, Entry<String, Double> e2) 
					{
						return e1.getValue().compareTo(e2.getValue()) * -1;
					}
                    
				});
				this.file2sorted.put(file.getName(), lst);
			}
		}
		/*******************/		
		isInitialized = true;
  	}
	
	// Q2
  	
	/* @pre: isInitialized() */
	public int getCountInFile(String word, String fileName) throws FileIndexException
	{ 
		if (!file2hmh.containsKey(fileName)) 
		{
			throw new FileIndexException("File does not exist");
		}
		else 
		{
			word = word.toLowerCase();
			int count = 0;
			if(file2hmh.get(fileName).containsKey(word))
			{
				count = file2hmh.get(fileName).getCountForItem(word);
			}
			return count;
		}
	}
	
	/* @pre: isInitialized() */
	public int getNumOfUniqueWordsInFile(String fileName) throws FileIndexException
	{ 
		if (!file2hmh.containsKey(fileName)) 
		{
			throw new FileIndexException("File does not exist");
		}
		else 
		{
			return file2hmh.get(fileName).getSize();
		}
	}
	
	/* @pre: isInitialized() */
	public int getNumOfFilesInIndex()
	{
		return file2hmh.size();
	}

	
	/* @pre: isInitialized() */
	public double getTF(String word, String fileName) throws FileIndexException
	{ // Q3
		if (!file2hmh.containsKey(fileName)) 
		{
			throw new FileIndexException("File does not exist");
		}
		else 
		{
			word = word.toLowerCase();
			int wordReps = file2hmh.get(fileName).getValue(word);
			int wordsInDoc = file2hmh.get(fileName).getCountsSum();
			return calcTF(wordReps, wordsInDoc);
		}
	}
	
	/* @pre: isInitialized() 
	 * @pre: exist fileName such that getCountInFile(word) > 0*/
	public double getIDF(String word)
	{ //Q4
		word = word.toLowerCase();
		int docNum = file2hmh.size();
		int docsWithWord = getDocsWithWord(word);			
		return calcIDF(docNum, docsWithWord);
	}
	
	//returns how many docs contains the word
	private int getDocsWithWord(String word)
	{
		word = word.toLowerCase();
		int cnt = 0;
		for( HashMapHistogram<String> doc : file2hmh.values())
		{
			if(doc.containsKey(word))
			{
				cnt++;
			}
		}
		return cnt;
	}
	
	
	
	/*
	 * @pre: isInitialized()
	 * @pre: 0 < k <= getNumOfUniqueWordsInFile(fileName)
	 * @post: $ret.size() = k
	 * @post for i in (0,k-2):
	 * 		$ret[i].value >= $ret[i+1].value
	 */
	public List<Map.Entry<String, Double>> getTopKMostSignificantWords(String fileName, int k) throws FileIndexException
	{ //Q5
		if (!file2hmh.containsKey(fileName)) 
		{
			throw new FileIndexException("File does not exist");
		}
		else 
		{
			LinkedList<Map.Entry<String, Double>> ret = new LinkedList<Map.Entry<String, Double>>();
			for(int i=0; i<k; i++)
			{
				ret.add(file2sorted.get(fileName).get(i));
			}
			return ret;
		}
	}
	
	
	/* @pre: isInitialized() */
	public double getCosineSimilarity(String fileName1, String fileName2) throws FileIndexException
	{ //Q6
		if(!file2hmh.containsKey(fileName1) || !file2hmh.containsKey(fileName2)) 
		{
			throw new FileIndexException("File does not exist");
		}
		else 
		{			
			double Ai = 0;
			double Bi = 0;
			double AB = 0;
			for(String word : this.allWords.getItemsSet())
			{
				Ai += Math.pow(getTFIDF(word, fileName1), 2);
				Bi += Math.pow(getTFIDF(word, fileName2), 2);
				AB += (getTFIDF(word, fileName1) * getTFIDF(word, fileName2));			
			}
			return (AB / (Math.sqrt(Ai*Bi)));
		}
	}
	
	/*
	 * @pre: isInitialized()
	 * @pre: 0 < k <= getNumOfFilesInIndex()-1
	 * @post: $ret.size() = k
	 * @post for i in (0,k-2):
	 * 		$ret[i].value >= $ret[i+1].value
	 */
	public List<Map.Entry<String, Double>> getTopKClosestDocuments(String fileName, int k) throws FileIndexException
	{ //Q7
		if (!file2hmh.containsKey(fileName)) 
		{
			throw new FileIndexException("File does not exist");
		}
		else 
		{
			List<Map.Entry<String, Double>> docList = new LinkedList<Map.Entry<String, Double>>();
			for (String file : file2hmh.keySet()) 
			{
				// for every file Histogram in the map
				if (!file.equals(fileName)) //don't compare the file with itself
				{
					double cosSim = getCosineSimilarity(fileName, file);
					Map.Entry<String, Double> cosSimEntry =  new AbstractMap.SimpleEntry<String, Double>(file, cosSim);
					docList.add(cosSimEntry);
				}
			}
			Collections.sort(docList,new Comparator<Map.Entry<String, Double>>()
			{
				public int compare(Entry<String, Double> e1, Entry<String, Double> e2) 
				{
					return e1.getValue().compareTo(e2.getValue()) * -1;
				}
	            
			});
			return docList.subList(0, k);
		}
	}

	
	
	//add private methods here, if needed

	
	/*************************************************************/
	/********************* Don't change this ********************/
	/*************************************************************/
	
	public boolean isInitialized(){
		return this.isInitialized;
	}
	
	/* @pre: exist fileName such that getCountInFile(word) > 0*/
	public double getTFIDF(String word, String fileName) throws FileIndexException{
		return this.getTF(word, fileName)*this.getIDF(word);
	}
	
	private static double calcTF(int repetitionsForWord, int numOfWordsInDoc){
		return (double)repetitionsForWord/numOfWordsInDoc;
	}
	
	private static double calcIDF(int numOfDocs, int numOfDocsContainingWord){
		return Math.log((double)numOfDocs/numOfDocsContainingWord);
	}
	
}
