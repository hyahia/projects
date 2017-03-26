package com.m800.log.management;

import java.io.File;

import com.m800.log.management.actor.FileScanner;
import com.m800.log.management.actor.message.Scan;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;


/**
 * @author Hossam Yahya
 *	The main application class.
 */
public class LogManagementMain {

	/**
     * The application main method to start scanning the target directory passed as application argument.
     * @param args application arguments containing the path required to be scanned at args[0].
     * @throws IllegalArgumentException when no value is passed for args[].
     */
    public static void main(String[] args) throws IllegalArgumentException {
    	// Validate that the path parameter is passed.
    	if(args == null || args.length == 0){
    		throw new IllegalArgumentException("Path parameter should be passed");
    	}
    	
    	String path = args[0];
    	// Validate that if path is not a directory or does not exist, throw exception.
		File targetDir = new File(path);
		if (!targetDir.exists() || !targetDir.isDirectory())
    		throw new IllegalArgumentException("Path parameter should be a valid existing dirctory path.");
    	
        try {
            // Create the 'log-management' actor system.
            final ActorSystem system = ActorSystem.create("log-management");
            
            // Create the 'scanner' actor.
            final ActorRef scanner = system.actorOf(Props.create(FileScanner.class), "scanner");
            scanner.tell(new Scan(path), ActorRef.noSender());

            // Keep waiting till all files being parsed successfully.
            try {
				while(!scanner.isTerminated()){
					Thread.sleep(500);
				}
				system.shutdown();
			} catch (Exception e) {
				e.printStackTrace();
			}
            
        } catch (Exception ex) {
            System.out.println("Unknown error happened!");
            ex.printStackTrace();
        }
    }

}