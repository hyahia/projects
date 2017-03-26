package com.m800.log.management.actor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.m800.log.management.actor.message.Done;
import com.m800.log.management.actor.message.Parse;
import com.m800.log.management.actor.message.Scan;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * The path scanning actor class.
 * @author Hossam Yahya
 */
public class FileScanner extends UntypedActor {
	/**
	 * The path of the current directory being scanned.
	 */
    private String path;
    
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public void onReceive(Object message) {
    	// Handle scan messages.
        if (message instanceof Scan){
        	path = ((Scan) message).path;
            log.info("<{}> is being scanned", path);
            
            // Create the parser actor
            final ActorRef parser = this.getContext().actorOf(Props.create(FileParser.class), "parser");

            // Loop through all files at this location 'path'.
            try(Stream<Path> paths = Files.list(Paths.get(path))) {
                paths.filter(Files::isRegularFile).forEach(filePath -> {

                    // Tell the 'parser' to parse the file at 'filePath'.
                    parser.tell(new Parse(filePath.toString()), self());
                    
                });
            }catch (IOException e) {
    			log.error(e, String.format("Error scanning directory at <{}>.", path));
    		}
            
            // Tell 'parser' to terminate.
            parser.tell(new Done(), self());
            
            log.info("<{}> is scanned successfully", path);

        }else if(message instanceof Done){ // Stop this actor upon receiving a 'Done' message.
        	this.getContext().stop(self());
        }else {
			unhandled(message);
		}
    }

	/**
	 * @return the value of the path field.
	 */
	public String getPath() {
		return path;
	}
}