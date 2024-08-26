package cucumber;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.model.plugin.DMDocument;

/**
 * This class is used to run lddtool and capture its output
 * It prevents lddtool from terminating the JVM by overriding System.exit()
 * It also captures the output of System.out and System.err for testing purposes
 */
public class LddToolRunner {
	private static final Logger LOG = LoggerFactory.getLogger(LddToolRunner.class);

    // ByteArrayOutputStreams to capture output stream
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    
    // Original System.out and System.err streams to restore them after execution
    private static final PrintStream originalOut = System.out;

    /**
     *  This method runs the Lddtool and captures its output
     * @param args the command line arguments to pass to lddtool
     * @return the output of lddtool
     * @throws Throwable if an error occurs while running lddtool
     */
    public static String runLddTool(String[] args, String outDirectory) throws Throwable {
        String output = null; // output of lddtool
        try {
        	setupStreams(); // Redirect System.out and System.err to capture them
            File f = new File(outDirectory);
            f.mkdirs();
            DMDocument.setOutputDirPath(outDirectory + File.separatorChar);
            DMDocument.run(args);
            output = outContent.toString();
        } catch (Exception e) {
            // Throw an exception if an error occurs while setting the SecurityManager
        	e.printStackTrace();
            throw new Exception("An error occurred while setting the SecurityManager in runLddTool", e);
        } finally {
            restoreStreams(); // Restore the original System.out and System.err
            clearStreams(); // Clear the captured output and error streams
        }
        return output; // Return the captured output
    }

    /**
     * Clears the content of output and error streams.
     */
    public static void clearStreams() {
        outContent.reset();
    }

    /**
     * Sets up custom PrintStreams to capture System.out and System.err output 
     */
    static void setupStreams() {
    	PrintStream stream = new PrintStream(outContent);
        System.setOut(stream);
    }

    /**  
     * Restores the original System.out and System.err streams
     */
    private static void restoreStreams() {
        System.setOut(originalOut);
    }
}
