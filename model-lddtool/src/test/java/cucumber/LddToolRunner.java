package cucumber;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.Permission;
import gov.nasa.pds.model.plugin.DMDocument;

public class LddToolRunner {
    // ByteArrayOutputStreams to capture output and error streams
    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    
    // Original System.out and System.err streams to restore them after execution
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    // This method runs the Lddtool and captures its output
    public static String runLddTool(String[] args) throws Throwable {
        setupStreams(); // Redirect System.out and System.err to capture them
        try {
            SecurityManager originalSecurityManager = System.getSecurityManager();
            // Set a custom SecurityManager that throws an exception instead of exiting
            System.setSecurityManager(new NoExitSecurityManager());
            try {
                // Call lddtool's main method and capture its output
                DMDocument.main(args);
            } catch (ExitException e) {
                // Handle the exception thrown by our NoExitSecurityManager when System.exit() is called
                System.err.println("DMDocument attempted to exit with status: " + e.status);
            } finally {
                // Restore the original security manager
                System.setSecurityManager(originalSecurityManager);
            }
        } finally {
            restoreStreams(); // Restore the original System.out and System.err
        }
        return outContent.toString(); // Return the captured output
    }

    // Sets up custom PrintStreams to capture System.out and System.err output
    private static void setupStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    // Restores the original System.out and System.err streams
    private static void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    // A custom SecurityManager that prevents System.exit() from terminating the JVM
    private static class NoExitSecurityManager extends SecurityManager {
        @Override
        public void checkPermission(Permission perm) {
            // Allow most actions by default
            // This is where you could restrict permissions if necessary, but for now, it permits everything.
        }

        @Override
        public void checkPermission(Permission perm, Object context) {
            // Allow most actions by default
            // Similar to checkPermission(Permission perm), this version is used in context-restricted scenarios.
        }

        @Override
        public void checkExit(int status) {
            super.checkExit(status); // Call the original checkExit method
            // Throw a custom exception instead of exiting
            // This prevents the application from terminating the JVM
            throw new ExitException(status);
        }
    }

    // A custom SecurityException to handle the prevention of System.exit()
    private static class ExitException extends SecurityException {
        public final int status; // The exit status requested by the application

        public ExitException(int status) {
            super("Prevented System.exit");
            this.status = status;
        }
    }
}
