/**
 * @author Ross Grogan-Kaylor
 * 2/27/18
 */

public class WordCount implements Comparable<WordCount>
{
	public String word;
	public int count;
	
	public WordCount(String word, int count)
	{
		this.word = word;
		this.count = count;
	}
	
	// enum for specifying whether compareTo compares WordCount objects using the "word" or "count" field
	public enum CompareOption
	{
		WORD_COMPARE, COUNT_COMPARE
	};
	
	/**
	 * @param other object to compare {@code this} to
	 * @param option the basis on which to compare the {@code WordCount} objects. Use {@code CompareOption.WORD_COMPARE}
	 * {@code CompareOption.COUNT_COMPARE}
	 * @return when compared using the specified {@code CompareOption}: -1 if {@code this} is less than {@code other},
	 * 0 if {@code this} is greater than {@code other}, and 1 if {@code this} is greater than {@code other}.
	 */
	public int compareTo(WordCount other, CompareOption option)
	{
		if (option == CompareOption.WORD_COMPARE) return this.word.compareTo(other.word);
		else //option == CompareOption.COUNT_COMPARE
		{
			if (this.count < other.count)
			{
				return -1;
			}
			else if (this.count > other.count)
			{
				return 1;
			}
			else
			{
				return 0;
			}
		}
	}
	
	/**
	 * Returns the result of comparing the wordCount object's {@code count} parameter.
	 * @param other object to compare {@code this} to
	 * @return -1 if {@code this.word} is less than {@code other.word},
	 * 0 if {@code this.word} is greater than {@code other.word}, and 1 if {@code this.word} is greater than {@code other.word}.
	 */
	public int compareTo(WordCount wc)
	{
		return this.compareTo(wc, CompareOption.COUNT_COMPARE);
	}
	
	/**
	 * @return Returns a String of the form "{word}: {count}"
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(word).append(": ").append(count);
		return sb.toString();
	}
}
