package com.example.lddtool.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LDDToolOutputComparisonTest {

	private static final String JAVA_EXECUTABLE = "java";
    private static final String USERDIR = System.getProperty("user.dir");
    private static final String EXPORTCURRENT = "/export/";
    private static final String EXPORTPREVIOUS = "/src/test/resources/github867/1N00/";	
    List<String> exclusionList = Arrays.asList("<!-- PDS4 XML/Schema", "<!-- PDS4 Schematron");
    List<String> fileIdList = Arrays.asList("PDS4_PDS_1N00.xsd", "PDS4_PDS_1N00.sch", "dd11179_GenPClass.pins");

    @Before
    public void setUp() throws Exception {
        // Ensure the output directory exists and is clean
//        Files.createDirectories(Paths.get(OUTPUT_DIR));
//        outputFiles = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        // Clean up output files
//        for (Path file : outputFiles) {
//            Files.deleteIfExists(file);
//        }
    }

    @Test
    public void testLDDToolAndComparison() throws Exception {
    	
    	// Run LDDTool    	
        System.out.println("Info: USERDIR:" + USERDIR);

        int exitCode = runLDDTool();
    	assertEquals("LDDTool execution failed", 0, exitCode);

    	// run file comparison code
    	for (String fileId: fileIdList) {
    		String fileSpecNameCurrent = Paths.get(USERDIR, EXPORTCURRENT, fileId).toString();
    		String fileSpecNamePrevious = Paths.get(USERDIR, EXPORTPREVIOUS, fileId).toString();
    		assertTrue("Comparison failed for " + fileId,
    				compareFiles(fileSpecNameCurrent, fileSpecNamePrevious, exclusionList));
    	}
    }    

    private int runLDDTool() throws Exception {
    	
        // Define the command and arguments
        String javaPath = "C:\\Program Files\\Java\\jdk-17\\bin\\java";
        String dataHome = "-Ddata.home=C:/AA7Ontologies/A01PDS4/Document/Data";
//   	String jarPath = Paths.get(USERDIR, "/target/", "model-lddtool-15.3.0-SNAPSHOT.jar").toString();
//   	String jarPath = Paths.get(USERDIR, "/target/classes/lib/", "DMDocument.jar").toString();
        String jarPath = "C:\\AA7Ontologies\\DMDocument\\DMDocument.jar";
        String outputFile = "list.txt";

        // Build the ProcessBuilder
        ProcessBuilder processBuilder = new ProcessBuilder(
            javaPath,
            dataHome,
            "-jar",
            jarPath,
            "-p"
        );

        // Redirect the output to a file
        processBuilder.redirectOutput(new File(outputFile));
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        int exitCode = 1;
        try {
            // Start the process
            Process process = processBuilder.start();

            // Wait for the process to complete
            exitCode = process.waitFor();

            // Print the exit code
//            System.out.println("LDDTool finished with exit code: " + exitCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        return exitCode;
    }
    
    // compare the current file to a previous instance of the file; exclusion list handles the date change
    public static boolean compareFiles(String fileSpecNameCurrent, String fileSpecNamePrevious, List<String> exclusionList) {
    	
    	File fileCurr = new File(fileSpecNameCurrent);
    	File filePrev = new File(fileSpecNamePrevious);

        try (BufferedReader reader1 = new BufferedReader(new FileReader(fileCurr));
             BufferedReader reader2 = new BufferedReader(new FileReader(filePrev))) {

            List<String> lines1 = reader1.lines().collect(Collectors.toList());
            List<String> lines2 = reader2.lines().collect(Collectors.toList());

            if (lines1.size() != lines2.size()) {
                System.err.println("Error: Files differ in number of lines - fileSpecNameCurrent:" + fileSpecNameCurrent);
                return false;
            }

            for (int i = 0; i < lines1.size(); i++) {
                String line1 = lines1.get(i).trim();
                String line2 = lines2.get(i).trim();

                // Skip comparison if any string in the exclusion list exists in the line
                if (exclusionList != null && exclusionList.stream().anyMatch(line1::contains)) {
                    continue;
                }

                if (!line1.equals(line2)) {
                    System.err.println("Error: Files differ at line " + (i + 1) + " - fileSpecNameCurrent:" + fileSpecNameCurrent);
                    return false;
                }
            }
            System.out.println("Info: Files are identical - fileSpecNameCurrent:" + fileSpecNameCurrent);
        } catch (IOException e) {
            System.err.println("Error: An exception occurred while reading the files: " + e.getMessage());
            return false;
        }
        return true;
    }
}

