package com.m800.log.management.actor;

import com.m800.log.management.actor.message.Scan;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;


/**
 * @author Hossam Yahya
 *	The main application class.
 */
public class Main {

	/**
     * @param args application arguments containing the path required to be scanned at args[0].
     * @throws IllegalArgumentException when no value is passed for args[].
     */
    public static void main(String[] args) throws IllegalArgumentException {
    	if(args == null || args.length == 0){
    		throw new IllegalArgumentException("Path parameter should be passed");
    	}
    	
    	String path = args[0];
    	
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
			}
            
        } catch (Exception ex) {
            System.out.println("Unknown error happened!");
            ex.printStackTrace();
        }
    }

}