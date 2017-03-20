import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.m800.log.management.Main.Scan;
import com.m800.log.management.actor.FileScanner;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import akka.testkit.TestActorRef;
import scala.concurrent.duration.Duration;


/**
 * 	The application unit testing class
 * 	@author Hossam Yahya
 */
public class HelloAkkaTest {

    static ActorSystem system;

    @BeforeClass
	public static void setup() {
		system = ActorSystem.create();

		try {
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
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    // Close the system after being done with testing.
    @AfterClass
    public static void teardown() {
        system.shutdown();
        system.awaitTermination(Duration.create("10 seconds"));
    }

    // Test successful scan of a fixed file.
    @Test
    public void testSetPathAndScan() {
        new JavaTestKit(system) {{
            final TestActorRef<FileScanner> scanner =
                TestActorRef.create(system, Props.create(FileScanner.class), "scanner");

            scanner.tell(new Scan("./../testing/"), getTestActor());

            // Assert that the test path is set correctly.
            Assert.assertEquals("./../testing/", scanner.underlyingActor().getPath());
            
            // Wait for the actors to complete the test file scanning.
            try {
				Thread.sleep(1000);
			} catch (Exception e) {
			}
            
            // Assert that the test file was parsed successfully and the word count is correct.
            Assert.assertTrue(readFile("./../testing/result/result.txt").contains("./../testing/testFile.txt:2"));

        }};
    }

    // Local helper method to read a file.
    private String readFile(String filePath){
    	StringBuilder fileContent = new StringBuilder();
    	
    	try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
            
			stream.forEach(line -> {
				fileContent.append(line);
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
    	
		return fileContent.toString();
    }
}
