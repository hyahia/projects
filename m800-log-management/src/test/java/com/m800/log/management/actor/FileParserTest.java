package com.m800.log.management.actor;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.m800.log.management.actor.message.Done;
import com.m800.log.management.actor.message.Parse;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import scala.concurrent.duration.Duration;

/**
 * The class <code>FileParserTest</code> contains tests for the class <code>{@link FileParser}</code>.
 *
 * @author Hossam Yahya
 */
public class FileParserTest {
	
    /**
     * The {@link akka.actor.ActorSystem} used to create different {@link akka.actor.Actor}.
     */
    static ActorSystem system;
    
	/**
	 * Run the void onReceive(Parse) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testOnReceive_Parse() throws Exception {
		// Create the parser actor.
		TestActorRef<FileParser> parser = TestActorRef.create(system, Props.create(FileParser.class), "parser_testOnReceive_Parse");
		
		// Send Parse
		parser.tell(new Parse("./../testing/testFile.txt"), ActorRef.noSender());

		// Assert that the test path is set correctly.
		Assert.assertEquals("./../testing/testFile.txt", parser.underlyingActor().getFilePath());
	}

	/**
	 * Run the void onReceive(Done) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testOnReceive_Done() throws Exception {
		// Create the parser actor.
		TestActorRef<FileParser> parser = TestActorRef.create(system, Props.create(FileParser.class), "parser_testOnReceive_Done");
			
		// Send Done
		parser.tell(new Done(), ActorRef.noSender());

		// Assert that the actor is terminated upon receiving Done message.
		Assert.assertEquals(true, parser.isTerminated());
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
		new org.junit.runner.JUnitCore().run(FileParserTest.class);
	}
}