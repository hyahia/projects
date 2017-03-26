package com.m800.log.management.actor;

import org.junit.*;
import static org.junit.Assert.*;

import com.m800.log.management.actor.message.Done;
import com.m800.log.management.actor.message.EndOfFile;
import com.m800.log.management.actor.message.Line;
import com.m800.log.management.actor.message.Scan;
import com.m800.log.management.actor.message.StartOfFile;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import scala.concurrent.duration.Duration;

/**
 * The class <code>FileScannerTest</code> contains tests for the class <code>{@link FileScanner}</code>.
 *
 * @author Hossam Yahya
 */
public class FileScannerTest {
	
    /**
     * The {@link akka.actor.ActorSystem} used to create different {@link akka.actor.Actor}.
     */
    static ActorSystem system;
    
	/**
	 * Run the void onReceive(Scan) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testOnReceive_Scan() throws Exception {
		// Create the scanner actor.
		TestActorRef<FileScanner> scanner = TestActorRef.create(system, Props.create(FileScanner.class), "scanner_testOnReceive_Scan");
				
		// Send Scan
		scanner.tell(new Scan("./../testing/"), ActorRef.noSender());

		// Assert that the test path is set correctly.
		Assert.assertEquals("./../testing/", scanner.underlyingActor().getPath());
	}

	/**
	 * Run the void onReceive(Done) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testOnReceive_Done() throws Exception {
		// Create the scanner actor.
		TestActorRef<FileScanner> scanner = TestActorRef.create(system, Props.create(FileScanner.class), "scanner_testOnReceive_Done");
		
		// Send Done
		scanner.tell(new Done(), ActorRef.noSender());

		// Assert that the actor is terminated upon receiving Done message.
		Assert.assertEquals(true, scanner.isTerminated());
	}
	
	/**
	 * Perform pre-test initialization by preparing the testing files for testing.
	 *
	 * @throws Exception
	 *         if the initialization fails for some reason
	 *
	 */
    @BeforeClass
	public static void setUp() throws Exception {
    	// Create the actor system.
		system = ActorSystem.create();
    }

	/**
	 * Perform post-test clean-up by closing the actor system after being done with testing..
	 *
	 * @throws Exception
	 *         if the clean-up fails for some reason
	 *
	 */
    @AfterClass
    public static void teardown() throws Exception{
    	// Shut down the actor system.
        system.shutdown();
        system.awaitTermination(Duration.create("10 seconds"));
    }

	/**
	 * Launch the test.
	 *
	 * @param args the command line arguments
	 *
	 */
	public static void main(String[] args) {
		new org.junit.runner.JUnitCore().run(FileScannerTest.class);
	}
}