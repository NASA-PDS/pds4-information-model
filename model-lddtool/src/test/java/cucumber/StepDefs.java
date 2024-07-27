package cucumber;

import static org.junit.jupiter.api.Assertions.*;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.File;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class connects the feature files with the Cucumber test code
 */
public class StepDefs {
    // The values of these variables should come from a row in the table in the
    // feature file.
    private String inputDirectory;
    private String outputDirectory;
    private String commandArgs;

    /**
     * This method resolves the command arguments by replacing placeholders with actual values
     * @return a String array containing the resolved command arguments
     */
    private String[] resolveArgumentStrings(String[] args) {
        String[] resolvedArgs = new String[args.length];
        int argIndex = 0;
        String resolvedToken = "";
        for (String temp : args) {
            // Replace every occurence of "{inputDirectory}" with actual value
            resolvedToken = temp.replace("{inputDirectory}", this.inputDirectory);
            // Replace every occurence of "{outputDirectory}" with actual value.
            resolvedToken = resolvedToken.replace("{outputDirectory}", this.outputDirectory);
            // Replace every occurence of "%20" with a space
            resolvedToken = resolvedToken.replace("%20", " ");
            resolvedArgs[argIndex++] = resolvedToken;  // add the resolved token to the args array
        }

        return resolvedArgs;
    }

    /**
     * This method moves generated files from the source directory to the target directory
     * @param sourceDir the source directory
     * @param targetDir the target directory
     * @param pattern the pattern to match the files to move
     * @throws IOException if an I/O error occurs
     */
    private static void moveGeneratedFiles(String sourceDir, String targetDir, String pattern) throws IOException {
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
    private static void createOutputFile(String output, String fileName) throws IOException {
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
     * This method reads the content of a file
     * @param fileName the name of the file to read
     * @return the content of the file
     * @throws IOException if an I/O error occurs
     */
    private static String readFileContent(String fileName) throws IOException {
        File file = new File(fileName);
        Path filePath = file.toPath();

        try {
            // Read the content of the file
            return Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IOException("Failed to read file: " + fileName, ex);
        }
    }

    /**
     * This method finds a file with the specified extension in the specified directory
     * @param directoryPath the path of the directory to search
     * @param fileExtension the extension of the file to find
     * @return the file as a string
     * @throws Exception if the file is not found, multiple files are found, or the directory is invalid
     */
    private static String findFileByExtension(String directoryPath, String fileExtension) throws IOException {
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            throw new IllegalArgumentException("The provided path is not a valid directory: " + directoryPath);
        }

        FilenameFilter filter = (dir, name) -> name.endsWith(fileExtension);
        File[] matchingFiles = directory.listFiles(filter);

        if (matchingFiles == null || matchingFiles.length == 0) {
            throw new FileNotFoundException("No file found with extension: " + fileExtension);
        }

        if (matchingFiles.length > 1) {
            throw new IOException("Multiple files found with extension: " + fileExtension);
        }

        return matchingFiles[0].getName();
    }

    /**
     * This method filters the lines of a file based on the specified strings
     * @param lines the lines to filter
     * @param excludeStrings the strings to exclude
     * @return the filtered lines
     */
    private static List<String> filterLines(List<String> lines, List<String> excludeStrings) {
        return lines.stream()
                .filter(line -> excludeStrings.stream().noneMatch(line::contains))
                .collect(Collectors.toList());
        }

    /**
     * This method is called before each test case to set up the test environment
     */
    @Before
    public void setUp() {
        this.inputDirectory = null;
        this.outputDirectory = null;
        this.commandArgs = null;
    }

    /**
     * This method is called before each test case to set up the test environment
     * @param inputDirectory the resource directory
     * @param outputDirectory the test directory
     * @param commandArgs the command arguments
     */
    @Given("the directories {string}, {string}, and command arguments {string}")
    public void test_directories_and_command_arguments(String inputDirectory, String outputDirectory, String commandArgs) {
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
        this.commandArgs = commandArgs;
    }

    /**
     * This method is called to run the lddtool command
     */
    @When("lddtool is run")
    public void run_lddtool() {
        String[] args = resolveArgumentStrings(this.commandArgs.split("\\s+"));  // resolve the commandArgs into a String array
        try {
            // run lddtool with the commandArgs and capture the output
            String actualResponse = LddToolRunner.runLddTool(args);

            // create an output file from the lddtool output and save it to target/generated-files directory
            createOutputFile(actualResponse, this.outputDirectory + "/lddtool-output.txt");

            // move lddtool-generated files to target/generated-files directory
            moveGeneratedFiles(".", this.outputDirectory + "/", "PDS4_");
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
     * @param actualOutputFile the source of the actual output
     */
    @Then("the produced output from lddtool command should {string} {string} in {string}")
    public void output_should_match_expected_response(String assertType, String output, String actualOutputFile) {
        String actualResponse = null;

        // If the actual output file starts with a dot, find the file with the specified extension in the output directory
        if (actualOutputFile.startsWith(".")) {
            try { 
                actualOutputFile = findFileByExtension(this.outputDirectory, actualOutputFile);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to find file", ex);
            }
        }

        actualOutputFile = this.outputDirectory + "/" + actualOutputFile;
        try {
            actualResponse = readFileContent(actualOutputFile);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read file", ex);
        }

        switch (assertType) {
            case "contain":
                assertTrue(actualResponse.contains(output),
                "Output: " + output + "\nActual response:\n" + actualResponse);
                break;
            case "not contain":
                assertFalse(actualResponse.contains(output),
                "Output: " + output + "\nActual response:\n" + actualResponse);
                break;
            default:
                throw new RuntimeException("Invalid assert type: " + assertType);
        }
    }

    /**
     * This method is called after each test case to compare the actual-generated file with the expected file
     * @param actualOutputFile the source of the actual output
     * @param expectedOutputFile the source of the expected output
     * @param excludeStrings the lines to exclude from the comparison
     */
    @Then("the contents of file {string} should match {string} except for comma-separated strings in {string}")
    public void output_should_match_expected_file(String actualOutputFile, String expectedOutputFile, String excludeStrings) {
        List<String> actualLines;
        List<String> expectedLines;
        List<String> excludeList = Arrays.asList(excludeStrings.split(",")); // convert the excludeStrings to a list of strings
        
        // If the actual output file starts with a dot, find the file with the specified extension in the output directory
        if (actualOutputFile.startsWith(".")) {
            try { 
                actualOutputFile = findFileByExtension(this.outputDirectory, actualOutputFile);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to find file", ex);
            }
        }
        
        try {
            actualLines = Files.readAllLines(Paths.get(this.outputDirectory, actualOutputFile));
            expectedLines = Files.readAllLines(Paths.get(this.inputDirectory, expectedOutputFile));
        } catch (IOException ex) {
            throw new RuntimeException("Failed to read file", ex);
        }

        // Filter the lines based on the excludeStrings
        actualLines = filterLines(actualLines, excludeList);
        expectedLines = filterLines(expectedLines, excludeList);

        assertEquals(expectedLines, actualLines);
        
    }

    /*
     * This method is called after each test case to clean up the test environment
     */
    @After
    public void tearDown() {
        this.inputDirectory = null;
        this.outputDirectory = null;
        this.commandArgs = null;
        LddToolRunner.clearStreams();
    }
}
