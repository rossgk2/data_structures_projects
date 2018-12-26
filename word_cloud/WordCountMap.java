/**
 * @author Ross Grogan-Kaylor
 * 2/27/18
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

public class WordCountMap
{
	private Node root;
	public static final WordCountMap stopwords = initStopwords("StopWords.txt"); //map that contains all the words that should not be included
																 				 //in word clouds
	
	/**
	 * Node class used for the internal tree structure of the map.
	 */
	private static class Node
	{
		private WordCount wordCount;
		private Node left;
		private Node right;

		private Node(WordCount wc, Node left, Node right)
		{
			this.wordCount = wc;
			this.left = left;
			this.right = right;
		}

		private Node(String word, int count, Node left, Node right)
		{
			this(new WordCount(word, count), left, right);
		}

		private Node(String word, int count)
		{
			this(new WordCount(word, count), null, null);
		}
		
		//toString() method used for testing and debugging
		public String toString()
		{
			return wordCount.toString();
		}
	}
	
	public WordCountMap()
	{
		root = null;
	}
	
	/**
	 * Initializes the static {@code stopwords} variable to a map containing the words specified by the
	 * file at {@code filepath}. 
	 */
	public static WordCountMap initStopwords(String filepath)
	{
		try
		{
			return readFromFile(filepath);
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}	
	
	/**
	 * If the specified word is already in this WordCountMap, then its count is
	 * increased by one. Otherwise, the word is added to this map with a count of 1.
	 */
	public void incrementCount(String word)
	{
		root = add(root, word);
	}

	/**
	 * Recursively adds word to the appropriate subtree of rt. If a wordCount object
	 * containing the specified word is already in the tree, its "count" field is
	 * increased by one.
	 * @param localRoot local root of the tree to traverse
	 * @return root of the tree after the adding process has finished
	 */
	private Node add(Node localRoot, String word)
	{
		if (localRoot == null)
		{
			return new Node(word, 1);
		}

		int compare = word.compareTo(localRoot.wordCount.word); //tree is organized by alphabetical categorization of words, not frequency of words

		//word is already in tree
		if (compare == 0)
		{
			localRoot.wordCount.count++;
			return localRoot;
		} 
		else if (compare < 0)
		{
			localRoot.left = add(localRoot.left, word);
			return localRoot;
		} 
		else //compare > 0
		{
			localRoot.right = add(localRoot.right, word);
			return localRoot;
		}	
	}

	/**
	 * Returns an array list of WordCount objects, one per word stored in this
	 * WordCountMap, sorted in decreasing order by count.
	 */
	public ArrayList<WordCount> getWordCountsByCount()
	{
		//write tree into array list
		ArrayList<WordCount> list = getWordCountsByWord();
			
		//sort array list by count
		list.sort(new Comparator<WordCount>()
		{
		   @Override
		   public int compare(WordCount wc1, WordCount wc2)
		   {
			   return -wc1.compareTo(wc2, WordCount.CompareOption.COUNT_COMPARE);
			   //Why the negative sign?
			   //The compareTo() method in the wordCount class treats WordCount objects with larger "count"s 
			   //as larger than ones with smaller counts. The sort() method called here will sort elements in
			   //nondecreasing order, which means we must signify to the method that an object is "smaller" 
			   //(should precede other objects) when its "count" variable is in fact larger.
		   }
		 });
		
		return list;
	}

	/**
	 * Returns a list of WordCount objects, one per word stored in this
	 * WordCountMap, sorted alphabetically by word.
	 */
	public ArrayList<WordCount> getWordCountsByWord()
	{
		ArrayList<WordCount> list = new ArrayList<WordCount>();
		inorder(root, list);
		return list;
	}
	
	/** Performs an inorder traversal of the tree (so as to visit each element in the tree in nondecreasing order)
	 * and writes the results of the traversal to {@code writeTo}.
	 * @param localRoot local root of the tree to traverse
	 * @param writeTo the {@code ArrayList<WordCount>} object to which the results of the traversal will be written
	 */
	private void inorder(Node localRoot, ArrayList<WordCount> writeTo)
	{		
		if (localRoot == null) return;
		
		inorder(localRoot.left, writeTo);
		writeTo.add(localRoot.wordCount);
		inorder(localRoot.right, writeTo);
	}

	/**
	 * @param word word to check
	 * @return Whether the word is a stopword or not. Case of word is ignored.
	 */
	public static boolean wordIsStopword(String word)
	{
		return stopwords.contains(word.toLowerCase());
	}
	
	/**
	 * @param word the word to search for
	 * @return whether the map contains the specified word or not
	 */
	public boolean contains(String word)
	{
		return search(word) != null;
	}
	
	/**
	 * Finds the Node in the tree that has the specified {@code word} parameter.
	 * @ return Node that has the specified {@code word} parameter
	 */
	public Node search(String word)
	{
		return search(root, word);
	}
	
	/** Helper method for {@code search(WordCount wc)}
	 */
	private Node search(Node localRoot, String word)
	{
		if (localRoot == null) return localRoot;
		
		int compare = word.compareTo(localRoot.wordCount.word);
		if (compare < 0)
		{
			return search(localRoot.left, word);
		}
		else if (compare > 0)
		{
			return search(localRoot.right, word);
		}
		else //compare == 0
		{
			return localRoot;
		}
	}
	
	public void remove(String word)
	{
		root = remove(root, word);
	}
	
	/**
	 * Helper method for {@code remove(String word)}.
	 * @return root of the subtree with initial root {@code localRoot} after the {@code WordCount} object
	 * with the specified {@code word} field has been removed
	 */
	private Node remove(Node localRoot, String word)
	{
		if (localRoot == null) return null;
		
		int compare = word.compareTo(localRoot.wordCount.word);
		if (compare < 0)
		{
			localRoot.left = remove(localRoot.left, word);
			return localRoot;
		}
		else if (compare > 0)
		{
			localRoot.right = remove(localRoot.right, word);
			return localRoot;
		}
		else
		{
			if (localRoot.left == null)
			{ 
	            return localRoot.right; 
	        }
			else if (localRoot.right == null)
			{ 
	            return localRoot.left;
			}
			else
			{
				localRoot = findInorderPredAndReplace(localRoot);
				return localRoot;
			}
		}
	}
	
	/** 
	 * Returns the root of the subtree with root {@code localRoot} after {@code localRoot} has been overwritten with its inorder predecessor.
	 * (The inorder predecessor is removed from its old spot in the subtree.)
	 */
	private Node findInorderPredAndReplace(Node localRoot)
	{
		if (localRoot.left.right == null)
		{ 
			//localRoot.left is the ip, so replace the value in localRoot with localRoot.left.wordCount.
            localRoot.wordCount = localRoot.left.wordCount;
            //Shift the left-left branch up to fill the space left by the left branch.
            localRoot.left = localRoot.left.left; 
        } 
		else 
        { 
            localRoot.wordCount = findLargestChildAndReplace(localRoot.left); 
        }
		return localRoot;
	}
	
	/**
	 * Returns the greatest value from the subtree with root {@code localRoot} after removing it
	 * from the subtree.
	 */
	private WordCount findLargestChildAndReplace(Node localRoot)
	{
		if (localRoot.right.right == null)
		{
			WordCount ret = localRoot.right.wordCount;
			localRoot.right = localRoot.right.left;
			return ret;
		}
		else
		{
			return findLargestChildAndReplace(localRoot.right);
		}
	}

	/**
	 * @param file The file to read from
	 * @return a {@code WordCountMap} object containing the contents of the specified file
	 */
	public static WordCountMap readFromFile(String file) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(new File(file));
		scanner.useDelimiter("[^a-zA-Z]"); //regex used to ignore punctuation

		WordCountMap map = new WordCountMap();
		while (scanner.hasNext())
		{
			map.incrementCount(scanner.next());
		}
		scanner.close();
		
		map.remove(""); //for some reason, the regex above causes empty Strings to be inserted into the tree
		return map;
	}
	
	/**
	 * @param file The file to read from
	 * @return a {@code WordCountMap} object containing the contents of the specified file with stopwords excluded
	 */
	public static WordCountMap readFromFileNoStopwords(String file) throws FileNotFoundException
	{
		Scanner scanner = new Scanner(new File(file));
		scanner.useDelimiter("[^a-zA-Z]"); //regex used to ignore punctuation
		
		WordCountMap map = new WordCountMap();
		String next;
		while (scanner.hasNext())
		{
			next = scanner.next();

			if (!wordIsStopword(next))
			{
				map.incrementCount(next);
			}
		}
		scanner.close();
		
		map.remove(""); //for some reason, the regex above causes empty Strings to be inserted into the tree
		return map;
	}
}
