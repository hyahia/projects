package com.m800.log.management.actor;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.m800.log.management.actor.message.Done;
import com.m800.log.management.actor.message.EndOfFile;
import com.m800.log.management.actor.message.Line;
import com.m800.log.management.actor.message.StartOfFile;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import scala.concurrent.duration.Duration;

/**
 * The class <code>FileAggregatorTest</code> contains tests for the class <code>{@link FileAggregator}</code>.
 *
 * @author Hossam Yahya
 */
public class FileAggregatorTest {
	
    /**
     * The {@link akka.actor.ActorSystem} used to create different {@link akka.actor.Actor}.
     */
    static ActorSystem system;
    
	/**
	 * Run the void onReceive(StartOfFile) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testOnReceive_StartOfFile() throws Exception {
		// Create the aggregator actor.
		TestActorRef<FileAggregator> aggregator = TestActorRef.create(system, Props.create(FileAggregator.class), "aggregator_testOnReceive_StartOfFile");
		
		// Send StartOfFile
		aggregator.tell(new StartOfFile("./../testing/testFile.txt"), ActorRef.noSender());

		// Assert that the test path is set correctly.
		Assert.assertEquals("./../testing/testFile.txt", aggregator.underlyingActor().getFilePath());
	}

	/**
	 * Run the void onReceive(Line) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testOnReceive_Line() throws Exception {
		// Create the aggregator actor.
		TestActorRef<FileAggregator> aggregator = TestActorRef.create(system, Props.create(FileAggregator.class), "aggregator_testOnReceive_Line");

		// Send StartOfFile
		aggregator.tell(new StartOfFile("./../testing/testFile.txt"), ActorRef.noSender());
		
		// Send line 'Test data'
		aggregator.tell(new Line("Test data"), ActorRef.noSender());

		// Assert that the current word count is 2.
		Assert.assertEquals(2, aggregator.underlyingActor().getWordCount());
	}
	
	/**
	 * Run the void onReceive(EndOfFile) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testOnReceive_EndOfFile() throws Exception {
		// Create the aggregator actor.
		TestActorRef<FileAggregator> aggregator = TestActorRef.create(system, Props.create(FileAggregator.class), "aggregator_testOnReceive_EndOfFile");

		// Send StartOfFile
		aggregator.tell(new StartOfFile("./../testing/testFile.txt"), ActorRef.noSender());

		// Send line 'Test data'
		aggregator.tell(new Line("Test data"), ActorRef.noSender());
		// Send EndOfFile
		aggregator.tell(new EndOfFile("./../testing/testFile.txt"), ActorRef.noSender());

		// Assert that the final word count is 2.
		Assert.assertEquals(2, aggregator.underlyingActor().getWordCount());
	}
	
	/**
	 * Run the void onReceive(Done) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testOnReceive_Done() throws Exception {
		// Create the aggregator actor.
		TestActorRef<FileAggregator> aggregator = TestActorRef.create(system, Props.create(FileAggregator.class), "aggregator_testOnReceive_Done");
				
		// Send Done
		aggregator.tell(new Done(), ActorRef.noSender());

		// Assert that the actor is terminated upon receiving Done message.
		Assert.assertEquals(true, aggregator.isTerminated());
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
		new org.junit.runner.JUnitCore().run(FileAggregatorTest.class);
	}
}