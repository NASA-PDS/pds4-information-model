package cucumber;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * This class connects the feature files with the Cucumber test code
 */
public class StepDefs {
    // The values of these variables should come from a row in the table in the
    // feature file.
    private String resourceDirectory;
    private String testDirectory;
    private String commandArgs;
    private String actualResponse;

    /**
     * This method resolves the command arguments by replacing placeholders with actual values
     * @return a String array containing the resolved command arguments
     */
    private String[] resolveArgumentStrings() {
        String array1[] = this.commandArgs.split("\\s+");  // split the commandArgs into a String array
        String[] args = new String[array1.length];
        int argIndex = 0;
        String resolvedToken = "";
        for (String temp : array1) {
            // Replace every occurence of "{resourceDirectory}" with actual value
            resolvedToken = temp.replace("{resourceDirectory}", this.resourceDirectory);
            // Replace every occurence of "{testDirectory}" with actual value.
            resolvedToken = resolvedToken.replace("{testDirectory}", this.testDirectory);
            // Replace every occurence of "%20" with a space
            resolvedToken = resolvedToken.replace("%20", " ");
            args[argIndex++] = resolvedToken;  // add the resolved token to the args array
        }

        return args;
    }

    /**
     * This method moves generated files from the source directory to the target directory
     * @param sourceDir the source directory
     * @param targetDir the target directory
     * @param pattern the pattern to match the files to move
     * @throws IOException if an I/O error occurs
     */
    private void moveGeneratedFiles(String sourceDir, String targetDir, String pattern) throws IOException {
        File source = new File(sourceDir);
        File target = new File(targetDir);

        // Create the target directory if it does not exist
        if (!target.exists()) {
            if (!target.mkdirs()) {
                throw new IOException("Failed to create directory: " + targetDir);
            }
        }

        // Save the files that match the pattern to an array
        File[] filesToMove = source.listFiles((dir, name) -> name.startsWith(pattern));

        if (filesToMove != null) {
            for (File file : filesToMove) {
                Path sourcePath = file.toPath(); // source file path
                Path targePath = target.toPath().resolve(file.getName()); // target file path
                
                // Move the file to the target directory
                try {
                    Files.move(sourcePath, targePath, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    throw new IOException("Failed to move file: " + file.getName(), ex);
                
                }
            }
        }
    }

    /**
     * This method creates an output file with the specified content
     * @param output the content of the output file
     * @param fileName the name of the output file
     * @throws IOException if an I/O error occurs
     */
    private void createOutputFile(String output, String fileName) throws IOException {
        File file = new File(fileName);
        Path filePath = file.toPath();

        File parentDir = file.getParentFile();

        // Create the parent directories if they do not exist
        if (parentDir != null && !parentDir.exists()) {
            if (!parentDir.mkdirs()) {
                throw new IOException("Failed to create directory: " + parentDir);
            }
        }

        try {
            // Create the output file with the specified content
            Files.write(filePath, output.getBytes(StandardCharsets.UTF_8));
        } catch (IOException ex) {
            throw new IOException("Failed to write to file: " + fileName, ex);
        }
    }

    /**
     * This method is called before each test case to set up the test environment
     */
    @Before
    public void setUp() {
        this.resourceDirectory = null;
        this.testDirectory = null;
        this.commandArgs = null;
        this.actualResponse = null;
    }

    /**
     * This method is called before each test case to set up the test environment
     * @param resourceDirectory the resource directory
     * @param testDirectory the test directory
     * @param commandArgs the command arguments
     */
    @Given("the test directories {string} and {string} and command arguments {string}")
    public void test_directories_and_command_arguments(String resourceDirectory, String testDirectory, String commandArgs) {
        this.resourceDirectory = resourceDirectory;
        this.testDirectory = testDirectory;
        this.commandArgs = commandArgs;
    }

    /**
     * This method is called before each test case to set up the test environment
     * @throws IOException if an I/O error occurs
     */
    @When("lddtool is run")
    public void run_lddtool() {
        String[] args = this.resolveArgumentStrings();  // resolve the commandArgs into a String array
        try {
            // run lddtool with the commandArgs and capture the output
            this.actualResponse = LddToolRunner.runLddTool(args);

            // create an output file from the lddtool output and save it to target/generated-files directory
            this.createOutputFile(this.actualResponse, "target/generated-files/" + this.testDirectory + "/lddtool-output.txt");

            // move lddtool-generated files to target/generated-files directory
            this.moveGeneratedFiles(".", "target/generated-files/" + this.testDirectory + "/", "PDS4_");
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new RuntimeException("DMDocument error", ex);
        }
    }

    /**
     * This method is called after each test case to compare the actual output with the expected output
     * @param assertType the type of assertion to perform
     * @param output the expected output
     */
    @Then("the produced output from lddtool command should {string} {string}")
    public void output_should_match_expected_response(String assertType, String output) {
        switch (assertType) {
            case "contain":
                assertTrue(this.actualResponse.contains(output),
                "Output: " + output + "\nActual response: " + this.actualResponse);
                break;
            case "not contain":
                assertFalse(this.actualResponse.contains(output),
                "Output: " + output + "\nActual response: " + this.actualResponse);
                break;
            default:
                throw new RuntimeException("Invalid assert type: " + assertType);
        }
    }

    /*
     * This method is called after each test case to clean up the test environment
     */
    @After
    public void tearDown() {
        this.resourceDirectory = null;
        this.testDirectory = null;
        this.commandArgs = null;
        this.actualResponse = null;
        LddToolRunner.clearStreams();
    }
}
