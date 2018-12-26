/**
 * @author Ross Grogan-Kaylor
 * 2/27/18
 */

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class WordCounter
{
	//NOTE: text files used for testing this program must be in the same directory as the Java files
	public static void main(String[] args) throws FileNotFoundException
	{
		String arg0 = args[0];
		boolean alphabetical = arg0.equals("alphabetical"), frequency = arg0.equals("frequency");
		if (alphabetical || frequency)
		{
			//Read into map from file
			String file = args[1];
			WordCountMap map = WordCountMap.readFromFileNoStopwords(file);
					
			//Get list filled with words by count or counts by word, depending the first command line argument
			ArrayList<WordCount> list;
			if (alphabetical)
			{
				list = map.getWordCountsByWord();
			}
			else
			{
				list = map.getWordCountsByCount();
			}
						
			//Print the list
			StringJoiner sj = new StringJoiner("\n");
			for (WordCount wc : list)
			{
				sj.add(wc.toString()); //The toString() method of the WordCount class has been written to provide the desired output
			}
			System.out.print(sj.toString());
		} 
		else if (arg0.equals("cloud"))
		{
			//Parse some command line arguments
			String file = args[1];
			int numWords = Integer.parseInt(args[2]);
			
			//Read into map from file
			WordCountMap map = WordCountMap.readFromFileNoStopwords(file);
			
			//Get list filled with words by count, and then select the portion of this list specified in the command line
			ArrayList<WordCount> list = map.getWordCountsByCount();
			List<WordCount> wcToUse = list.subList(0, numWords); //only use first "numWords" WordCount objectst	
			
			//Pass this list to the WordCloudMaker
			String title = file.replaceAll(".txt", "");
			WordCloudMaker.createWordCloudHTML(title, wcToUse, title + ".html");
		}
	}
}
