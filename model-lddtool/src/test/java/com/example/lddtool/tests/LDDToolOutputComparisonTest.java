package com.example.lddtool.tests;

//public class LDDToolOutputComparisonTest {

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LDDToolOutputComparisonTest {

    private static final String LDDTOOL_EXECUTABLE = "path/to/lddtool"; // Path to the LDDTool executable
    private static final String LDDTOOL_PROJECT = "path/to/project";    // Path to the LDDTool project
    private static final String STANDARD_FILES_DIR = "path/to/standard/files"; // Path to standard files
    private static final String OUTPUT_DIR = "path/to/output/dir";      // Directory to store LDDTool outputs
    private static final String COMPARISON_APP = "path/to/comparison/app"; // Path to the comparison app

    private List<Path> outputFiles;

    @Before
    public void setUp() throws Exception {
        // Ensure the output directory exists and is clean
        Files.createDirectories(Paths.get(OUTPUT_DIR));
        outputFiles = new ArrayList<>();
    }

    @After
    public void tearDown() throws Exception {
        // Clean up output files
        for (Path file : outputFiles) {
            Files.deleteIfExists(file);
        }
    }

    @Test
    public void testLDDToolAndComparison() throws Exception {
        // Run the LDDTool project 8 times
        for (int i = 1; i <= 8; i++) {
            Path outputFile = Paths.get(OUTPUT_DIR, "output_run_" + i + ".xml");
            runLDDTool(LDDTOOL_PROJECT, outputFile);
            outputFiles.add(outputFile);
        }

        // Compare each output file against the corresponding standard file
        for (int i = 1; i <= 8; i++) {
            Path generatedFile = Paths.get(OUTPUT_DIR, "output_run_" + i + ".xml");
            Path standardFile = Paths.get(STANDARD_FILES_DIR, "standard_file_" + i + ".xml");
            assertTrue("Comparison failed for output_run_" + i,
                       compareFiles(generatedFile, standardFile));
        }
    }

    private void runLDDTool(String projectPath, Path outputFile) throws Exception {
        // Execute the LDDTool process
        ProcessBuilder pb = new ProcessBuilder(
            LDDTOOL_EXECUTABLE,
            "-project", projectPath,
            "-output", outputFile.toString()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();
        int exitCode = process.waitFor();

        assertEquals("LDDTool execution failed", 0, exitCode);
    }

    private boolean compareFiles(Path generatedFile, Path standardFile) throws Exception {
        // Execute the comparison app
        ProcessBuilder pb = new ProcessBuilder(
            COMPARISON_APP,
            "-generated", generatedFile.toString(),
            "-standard", standardFile.toString()
        );
        pb.redirectErrorStream(true);
        Process process = pb.start();
        int exitCode = process.waitFor();

        return exitCode == 0;
    }
}

