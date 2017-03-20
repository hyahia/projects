package com.m800.log.management.actor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.m800.log.management.Main.Done;
import com.m800.log.management.Main.Parse;
import com.m800.log.management.Main.Scan;

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
    private String path;
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    public void onReceive(Object message) {
        if (message instanceof Scan){
        	path = ((Scan) message).path;
            log.info("{} is being scanned", path);
            final ActorRef parser = this.getContext().actorOf(Props.create(FileParser.class), "parser");

            try(Stream<Path> paths = Files.list(Paths.get(path))) {
                paths.filter(Files::isRegularFile).forEach(filePath -> {

                    // Tell the 'parser' to parse the file at 'filePath'.
                    parser.tell(new Parse(filePath.toString()), self());
                    
                });
            }catch (IOException e) {
    			e.printStackTrace();
    		}
            
            // Tell 'parser' to terminate.
            parser.tell(new Done(), self());
            
            log.info("{} is scanned successfully", path);

        }else if(message instanceof Done){
        	this.getContext().stop(self());
        }
    }

	public String getPath() {
		return path;
	}
}