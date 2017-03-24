package com.m800.log.management.actor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.m800.log.management.actor.FileAggregator;
import com.m800.log.management.actor.message.Done;
import com.m800.log.management.actor.message.EndOfFile;
import com.m800.log.management.actor.message.Line;
import com.m800.log.management.actor.message.Parse;
import com.m800.log.management.actor.message.StartOfFile;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 *	The file parsing actor class.
 * 	@author Hossam Yahya
 */
public class FileParser extends UntypedActor {
	private String filePath;
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public void onReceive(Object message) {
        if (message instanceof Parse){
        	filePath = ((Parse)message).filePath;
            log.debug("{} file is being parsed", filePath);

        	final ActorRef aggregator = this.getContext().actorOf(Props.create(FileAggregator.class), "aggregator" + filePath.replaceAll("/", "_"));

		        try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
		        	
		        	// Tell the 'aggregator' to StartOfFile the file at 'filePath'.
		        	aggregator.tell(new StartOfFile(filePath), self());
                    
					stream.forEach(line -> {
						// Tell the 'aggregator' to count the words of 'line'.
						aggregator.tell(new Line(line), self());
					});
		
		        	// Tell the 'aggregator' to EndOfFile the file at 'filePath'.
					aggregator.tell(new EndOfFile(filePath), self());
					
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	
	            log.debug("{} file is parsed successfully", filePath);

		} else if (message instanceof Done) {
			// Tell the 'scanner' to terminate.
			getSender().tell(new Done(), self());
			//  Stop the 'parser'.
			this.getContext().stop(self());
		} else
			unhandled(message);
    }
}