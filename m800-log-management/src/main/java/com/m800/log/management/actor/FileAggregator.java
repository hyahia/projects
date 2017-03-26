package com.m800.log.management.actor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.m800.log.management.actor.message.Done;
import com.m800.log.management.actor.message.EndOfFile;
import com.m800.log.management.actor.message.Line;
import com.m800.log.management.actor.message.StartOfFile;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 *	The line word counter actor class.
 * 	@author Hossam Yahya
 */
public class FileAggregator extends UntypedActor {

	/**
	 * The word count for the current file.
	 */
	private int wordCount = 0;
	
	
	/**
	 * The path of the current file being processed.
	 */
	private String filePath;
	
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	public void onReceive(Object message) {
		// Handle 'StartOfFile' message.
		if (message instanceof StartOfFile) {
			filePath = ((StartOfFile) message).message;
            log.debug("<{}> is being started", filePath);
			wordCount = 0;
		} else if (message instanceof Line) { // Handle 'Line' message.

			// Split line into words (delimited by space character) count them.
			String line = ((Line) message).message;
			int count = line.split(" ").length;
			wordCount += count;
			
            log.debug("<{}> file, <{}> line, has <{}> words", filePath, line, count);

			// TODO I suggest counting words with the word counter method below
			//wordCount += countWords(((Line) message).message);
            
		} else if (message instanceof EndOfFile) { // Handle 'EndOfFile' message.
			System.out.println(filePath + ":" + wordCount);
            log.debug("<{}> file, has <{}> words", filePath, wordCount);

		} else if (message instanceof Done) { // Handle 'Done' message.
			this.getContext().stop(self());
		} else {
			unhandled(message);
		}
	}
	
	/**
	 * Local helper method to count words in a line.
	 * @param line the target string to count it's words.
	 * @return the number of words in the line
	 */
	private int countWords(String line){
		int count = 0;
		Pattern p = Pattern.compile("\\b\\w+\\b");
        Matcher m = p.matcher(line);
        
        while(m.find()){
            count++;
        }
        
        return count;
	}
	
	/**
	 * @return the wordCount
	 */
	public int getWordCount() {
		return wordCount;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
}