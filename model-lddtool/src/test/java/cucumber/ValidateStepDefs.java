package cucumber;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gov.nasa.pds.model.plugin.util.Utility;
import gov.nasa.pds.tools.validate.CrossLabelFileAreaReferenceChecker;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.validate.ValidateLauncher;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ValidateStepDefs {
  // Class used as a glue to connect a feature file to cucumber test code.

  private static final Logger LOG = LoggerFactory.getLogger(ValidateStepDefs.class);
  private static final String DEFAULT_REPORT_FILENAME = "report_{testDir}.json";
  private static final String DEFAULT_VALIDATE_ARGS =
      "--update-context-products --report-style json --skip-content-validation --report-file {reportDir}/"
          + DEFAULT_REPORT_FILENAME + " ";
  private static final String DEFAULT_CORE_ARGS = "-p";
  private static final String DEFAULT_LDDTOOL_ARGS = "-lp";
  
  
  // For some strange reason, cucumber suppresses the printing of log output
  // unless the following parameters are added at command line:
  // Assuming slf4j-simple-1.7.28.jar exist in target/validate-1.25.0-SNAPSHOT/lib
  // directory.
  // -Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG

  // "-cp
  // "target/validate-1.25.0-SNAPSHOT/lib/slf4j-simple-1.7.28.jar:target/test-classes:target/validate-1.25.0-SNAPSHOT/lib/*
  // For example:
  //
  // java -Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG -cp
  // "target/validate-1.25.0-SNAPSHOT/lib/slf4j-simple-1.7.28.jar:target/test-classes:target/validate-1.25.0-SNAPSHOT/lib/*"
  // io.cucumber.core.cli.Main target/test-classes/features
  //
  // Note that the slf4j-simple-1.7.28.jar has to be explicitly called out first.

  private File outputData = null;
  // A class to run each test. It must be instantiated for each test in the
  // setUp() function.
  private ValidateLauncher launcher = null;

  // The values of these variables should come from a row in the table in the
  // feature file.
  private String testName;
  private String testDir;
  private int messageCount;
  private String messageText;
  private String problemEnum;
  private String resourceDir;
  private String reportDir;
  private String commandArgs;
  private String refOutputValue;

  private boolean createManifestFileFlag = false;

  /**
   * @throws java.lang.Exception
   */
  void setUp() throws Exception {
    this.outputData = new File(TestConstants.TEST_OUT_DIR);
    FileUtils.forceMkdir(this.outputData); // Create directory if one does not already exist.
    System.setProperty("resources.home", TestConstants.TEST_OUT_DIR);
    this.launcher = new ValidateLauncher();
    this.refOutputValue = DEFAULT_REPORT_FILENAME;
    this.reportDir = TestConstants.TEST_OUT_DIR;
    this.resourceDir = TestConstants.TEST_DATA_DIR;
    
    
  }

  /**
   * @throws java.lang.Exception
   */
  void tearDown() throws Exception {
    // FileUtils.forceDelete(this.outputData);
    this.launcher.flushValidators();
    // It seems the launcher does not completely flush any references to schematron
    // which causes problem for subsequent tests.
    // get rid of the cross references
    CrossLabelFileAreaReferenceChecker.reset();
  }

  private void createManifestFileDo(String testPath) {
    // Function create a manifest file if the flag createManifestFileFlag it true.
    // The file is normally used when --target-manifest is used in the command line.
    // As of 10/13/2020, there is only one test github50 uses the manifest file.

    if (this.createManifestFileFlag) {
      try {
        String outFilePath = TestConstants.TEST_OUT_DIR;
        String manifestFile = outFilePath + File.separator + "target-manifest.xml";
        String manifestText = testPath + File.separator + "ele_evt_12hr_orbit_2011-2012.xml\n"
            + testPath + File.separator + "ele_evt_8hr_orbit_2012-2013.xml";
        BufferedWriter writerManifest = new BufferedWriter(new FileWriter(manifestFile));
        writerManifest.write(manifestText);
        writerManifest.close();
      } catch (Exception e) {
        e.printStackTrace();
        fail("Test Failed Due To Exception: " + e.getMessage());
      }
    }
  }

  private void createCatalogFileDo(String catFile, String testPath, boolean forceFlag) {
    // Function create a catalog file if the flag createManifestFileFlag it true.
    // The file is normally used when --target-manifest is used in the command line.
    // As of 10/13/2020, there is only one test github50 uses the manifest file.
    if ((this.createManifestFileFlag) || (forceFlag)) {
      try {
        // Create catalog file
        String catText = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<!--\n"
            + "<!DOCTYPE catalog PUBLIC \"-//OASIS//DTD XML Catalogs V1.1//EN\" \"http://www.oasis-open.org/committees/entity/release/1.1/catalog.dtd\">\n"
            + "-->\n" + "<catalog xmlns=\"urn:oasis:names:tc:entity:xmlns:xml:catalog\">\n"
            + "    <rewriteURI uriStartString=\"http://pds.nasa.gov/pds4\" rewritePrefix=\"file://"
            + testPath + "\" />\n"
            + "    <rewriteURI uriStartString=\"https://pds.nasa.gov/pds4\" rewritePrefix=\"file://"
            + testPath + "\" />\n" + "</catalog>";

        BufferedWriter writer = new BufferedWriter(new FileWriter(catFile));
        writer.write(catText);
        writer.close();
      } catch (Exception e) {
        e.printStackTrace();
        fail("Test Failed Due To Exception: " + e.getMessage());
      }
    }
  }

  private int getMessageCountBasedOnProblemType(String problemEnum, JsonObject reportJson) {
    // Given an output report, retrieve the 'count' field based on the problemEnum
    // value.
    int totalCount = 0;
    int count = 0; // Individual enum
    // It is possible that the value is more than one: e.g
    // CONTEXT_REFERENCE_NOT_FOUND,CONTEXT_REFERENCE_FOUND_MISMATCH
    // Split using command and loop through each enum to fetch the count.
    String[] problemTokens = problemEnum.split(",");

    for (String strTemp : problemTokens) {
      if (strTemp.contains("total")) {
        count = reportJson.getAsJsonObject("summary").get(strTemp).getAsInt();
      } else {
        count = this.getMessageCount(reportJson,
            ProblemType.valueOf(ProblemType.class, strTemp).getKey());
      }
      LOG.debug("getMessageCountBasedOnProblemType: strTemp, count " + strTemp + " "
          + Integer.toString(count));
      totalCount += count;
    }

    LOG.debug("getMessageCountBasedOnProblemType: problemEnum, totalCount " + problemEnum
        + " " + Integer.toString(totalCount));

    return (totalCount);
  }

  private String[] resolveArgumentStrings(String commandArgs) throws URISyntaxException {
    // Given the value of this.commandArgs (just one long string), split using
    // spaces and replace all {reportDir} and {resourceDir} with actual value.
    // and return an array of tokens. This returned value can then be used to send
    // to processMain() function.
    // Example:
    //
    // "-r {reportDir}/report_github50_1.json -s json --no-data-check
    // --target-manifest {reportDir}/target-manifest.xml
    // becomes:
    // "-r target/test/report_github50_1.json -s, json, --no-data-check
    // --target-manifest target/test/target-manifest.xml

	  
    String array1[] = getDefaultValidateArguments(commandArgs);
    String[] args = new String[array1.length];
    int argIndex = 0;
    String resolvedToken = "";
    for (String temp : array1) {
      resolvedToken = temp.replace("{reportDir}", this.reportDir);
      resolvedToken = resolvedToken.replace("{testDir}", this.testDir);
      resolvedToken = resolvedToken.replace("{outDir}", TestConstants.TEST_OUT_DIR + File.separator + this.testDir);
      resolvedToken = resolvedToken.replace("{resourceDir}", this.resourceDir);
      resolvedToken = resolvedToken.replace("%20", " ");
      args[argIndex++] = resolvedToken;
    }

    LOG.debug("resolveArgumentStrings() commandArgs = [" + commandArgs + "]");
    LOG
        .info("args = [" + Arrays.toString(args) + "]");
    LOG.debug("resolveArgumentStrings() this.reportDir = [" + this.reportDir + "]");
    LOG.debug("resolveArgumentStrings() this.resourceDir = [" + this.resourceDir + "]");
    LOG.debug("resolveArgumentStrings() this.testName = [" + this.testName + "]");

    return (args);
  }

  @Given("the test {string} at directory {string}")
  public void a_test_string_with_string(String testName, String testDir) {
    // throw new io.cucumber.java.PendingException();
    this.testName = testName;
    this.testDir = testDir;
    this.reportDir = this.reportDir + File.separatorChar + testDir;
  }

  @Given("expected error\\/warning count {int} with expected error\\/warning text of {string} with expected error from ValidateProblems enumeration {string}")
  public void with_test_property(int messageCount, String messageText, String problemEnum) {
    this.messageCount = messageCount;
    this.messageText = messageText;
    this.problemEnum = problemEnum;
    LOG.debug("with_test_property:messageCount [" + Integer.toString(messageCount)
        + "], messageText [" + messageText + "]");
  }

  @Given("the latest PDS4 schema\\/schematron is generated")
  public void generate_pds4_schema() throws Exception {
    StepDefs.exec_lddtool(DEFAULT_CORE_ARGS,
        TestConstants.TEST_OUT_DIR + File.separator + this.testDir);
  }
  
  @Given("a new LDD is generated using the IngestLDDs {string}")
  public void generate_ldd(String ingestLDDFilename) throws Exception {
    String ingestLDDPath = TestConstants.TEST_DATA_DIR + File.separator + this.testDir
        + File.separator + ingestLDDFilename;
    StepDefs.exec_lddtool(DEFAULT_LDDTOOL_ARGS + " " + ingestLDDPath,
        TestConstants.TEST_OUT_DIR + File.separator + this.testDir);
  }
  
  @When("execute validate command with {string} as arguments")
  public void execute_a_validate_command(String commandArgs) {
    // Write code here that turns the phrase above into concrete actions
    // throw new io.cucumber.java.PendingException();
    LOG.debug("execute_a_validate_command:testDir " + this.testDir);
    LOG.debug("execute_a_validate_command:testName " + this.testName);

    this.commandArgs = commandArgs;

    try {
      this.setUp();

      // Because this.commandArgs is a String but processMain() is expecing a String
      // [], we have to
      // convert this.commandArgs into an array of strings.
      String[] args = this.resolveArgumentStrings(commandArgs);

      this.launcher.processMain(args);

      this.tearDown();

      // Will do the compare of the report in another function.
    } catch (ExitException e) {
      assertEquals(0, e.status, "Exit status");
    } catch (Exception e) {
      e.printStackTrace();
      fail("Test Failed Due To Exception: " + e.getMessage());
    }
  }

  @Then("produced output from validate command should be similar to reported {string} JSON report or no error reported.")
  public void produced_output_from_validate_command_should_be_similar_to_reference_ref_output_value_or_no_error_reported(
      String testDir) {

    try {
      Gson gson = new Gson();
      this.refOutputValue = this.refOutputValue.replace("{testDir}", this.testDir);
      File report = new File(this.reportDir + File.separator + this.refOutputValue);
      LOG.debug(
          "produced_output_from_validate_command_should_be_similar_to_reference_ref_output_value_or_no_error_reported:report = ["
              + report.getName() + "]");
      JsonObject reportJson = gson.fromJson(new FileReader(report), JsonObject.class);

      // Get the count for errors based on the value of problemEnum, e.g.
      // MISSING_REFERENCED_FILE
      int count = this.getMessageCountBasedOnProblemType(this.problemEnum, reportJson);

      LOG.debug(
          "produced_output_from_validate_command_should_be_similar_to_reference_ref_output_value_or_no_error_reported:testName,problemEnum,count,refOutputValue: "
              + this.testName + " " + problemEnum + " " + Integer.toString(count) + " "
              + this.refOutputValue);

      // Compare the count from this test with the this.messageCount from test table.
      assertEquals(count, this.messageCount, this.messageText + " " + reportJson.toString());

    } catch (ExitException e) {
      assertEquals(0, e.status, "Exit status");
    } catch (Exception e) {
      e.printStackTrace();
      fail("Test Failed Due To Exception: " + e.getMessage());
    }
  }

  protected static class ExitException extends SecurityException {
    private static final long serialVersionUID = -1535371619727142623L;

    public final int status;

    public ExitException(int status) {
      super("Program exited");
      this.status = status;
    }
  }

  int getMessageCount(JsonObject reportJson, String messageTypeName) {
    int i = 0;
    JsonObject message = null;
    int count = 0;
    if (messageTypeName.equals("totalErrors")) {
      return reportJson.getAsJsonObject("summary").get("totalErrors").getAsInt();
    }
    while (true) {
      try {
        message = reportJson.getAsJsonObject("summary").get("messageTypes").getAsJsonArray().get(i)
            .getAsJsonObject();
        if (message.get("messageType").getAsString().equals(messageTypeName)) {
          count = message.get("total").getAsInt();
          return count;
        }
      } catch (IndexOutOfBoundsException e) {
        return count;
      }
      i++;
    }
  }
  
  private String getAbsolutePath(String path) throws Exception {
	    String finalPath = "";
	    File testFile = new File(path);
	    if (!testFile.isAbsolute()) {
	      finalPath = System.getProperty("user.dir") + "/" + path;
	    } else {
	      finalPath = path;
	    }

	    if (!(new File(finalPath)).exists()) {
	      throw new Exception("Path does not exist: " + finalPath);
	    }

	    return finalPath;
	  }

      private String[] getDefaultValidateArguments(String commandArgs) throws URISyntaxException {
        List<String> argsList = new ArrayList<String>();
        String[] args = DEFAULT_VALIDATE_ARGS.concat(this.commandArgs).split("\\s+");
        
        // Get schema
        String[] schemas = Utility
            .getFilepaths(TestConstants.TEST_OUT_DIR + File.separatorChar + this.testDir, ".xsd");

        // Get schematron
        String[] schematrons = Utility
            .getFilepaths(TestConstants.TEST_OUT_DIR + File.separatorChar + this.testDir, ".sch");


        Collections.addAll(argsList, args);
        Collections.addAll(argsList, new String[] {"--schema"});
        Collections.addAll(argsList, schemas);
        Collections.addAll(argsList, new String[] {"--schematron"});
        Collections.addAll(argsList, schematrons);
        return Arrays.copyOf(argsList.toArray(), argsList.size(), String[].class);
      }
}
