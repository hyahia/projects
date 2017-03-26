package com.m800.log.management;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import akka.actor.ActorSystem;
import scala.concurrent.duration.Duration;

/**
 * The class <code>LogManagementMainTest</code> contains tests for the class <code>{@link LogManagementMain}</code>.
 *
 * @author Hossam Yahya
 */
public class LogManagementMainTest {
	
    /**
     * The {@link akka.actor.ActorSystem} used to create different {@link akka.actor.Actor}
     */
    static ActorSystem system;

	/**
	 * Run the void main(String[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testMain_1()
		throws Exception {

		LogManagementMain.main(null);
	}

	/**
	 * Run the void main(String[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testMain_2()
		throws Exception {
		String[] args = new String[] {};

		LogManagementMain.main(args);
	}

	/**
	 * Run the void main(String[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test(expected = java.lang.IllegalArgumentException.class)
	public void testMain_3()
		throws Exception {
		String[] args = new String[] {"not a & .\\/ % valid: path $"};

		LogManagementMain.main(args);

		// add additional test code here
	}

	/**
	 * Run the void main(String[]) method test.
	 *
	 * @throws Exception
	 *
	 */
	@Test
	public void testMain_4() throws Exception {
		String[] args = new String[] {"./../testing/"};

		LogManagementMain.main(args);

        // Wait for the actors to complete the test file scanning.
        try {
			Thread.sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();   
		}

        // Assert that the test file was parsed successfully and the word count is correct.
        Assert.assertTrue(readFile("./../testing/result/result.txt").contains("./../testing/testFile.txt:2"));
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
		
		// Initialize the testing files
		initTestingFiles();
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
		// Start executing the test case
		new org.junit.runner.JUnitCore().run(LogManagementMainTest.class);
	}
	
	/**
	 * Launch the test.
	 *
	 * @throws IOException
	 *         if the clean-up fails for some reason
	 */
	private static void initTestingFiles() throws IOException {
		File testDir = new File("./../testing/");
		File testResultDir = new File("./../testing/result/");

		// If the test directory exist, delete it
		if (testDir.exists()) {
			testDir.delete();
		}

		// Create test directories.
		testDir.mkdir();
		testResultDir.mkdir();

		// Create the test file.
		File file = new File("./../testing/testFile.txt");
		file.createNewFile();

		// Write test file content.
		FileWriter writer = new FileWriter(file);
		writer.write("Test data");
		writer.close();

		// Create the result file
		File outFile = new File("./../testing/result/result.txt");
		outFile.createNewFile();

		// Direct all 'System.out.println()' to the result file.
		System.setOut(new PrintStream(outFile));
	}
	
	/**
     * Local helper method to read a file.
     * @param filePath The file path to be read.
     * @return The file content as a single string.
     * @throws IOException 
     */
    private String readFile(String filePath) throws IOException{
    	return Files.lines(Paths.get(filePath)).collect(Collectors.joining());
    }
}