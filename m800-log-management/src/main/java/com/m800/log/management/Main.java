package com.m800.log.management;

import java.io.Serializable;

import com.m800.log.management.actor.FileScanner;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;


/**
 * @author Hossam Yahya
 *	The main application class.
 */
public class Main {

	public static class Scan implements Serializable {
		private static final long serialVersionUID = 1L;
		public final String path;
        public Scan(String path) {
            this.path = path;
        }
    }
    
    public static class StartOfFile implements Serializable {
		private static final long serialVersionUID = 1L;
		public final String message;
        public StartOfFile(String message) {
            this.message = message;
        }
    }
    
    public static class Line implements Serializable {
		private static final long serialVersionUID = 1L;
		public final String message;
        public Line(String message) {
            this.message = message;
        }
    }
    
    public static class EndOfFile implements Serializable {
		private static final long serialVersionUID = 1L;
		public final String message;
        public EndOfFile(String message) {
            this.message = message;
        }
    }
    
    public static class Parse implements Serializable {
		private static final long serialVersionUID = 1L;
		public final String filePath;
        public Parse(String filePath) {
            this.filePath = filePath;
        }
    }
    
    public static class Done implements Serializable {
		private static final long serialVersionUID = 1L;
    }
    
    public static void main(String[] args) {
        try {
            // Create the 'log-management' actor system.
            final ActorSystem system = ActorSystem.create("log-management");

            // Create the 'scanner' actor.
            final ActorRef scanner = system.actorOf(Props.create(FileScanner.class), "scanner");
            scanner.tell(new Scan(args[0]), ActorRef.noSender());

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