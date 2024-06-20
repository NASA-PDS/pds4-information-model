Feature: Running integration tests for lddtool
  As an lddtool user
  I want to run lddtool with specific command arguments
  So that I can verify if it provides the expected response

  Scenario Outline: Running lddtool with different parameters
    Given the test directories <resourceDirectory> and <testDirectory> and command arguments <commandArgs>
    When the lddtool tool is run
    Then the produced output from lddtool command should match or be similar to the expected response <expectedResponse>

    Examples:
      | testName                                                                         | resourceDirectory    | testDirectory | commandArgs                                                        | expectedResponse |
      | "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO" | "src/test/resources" | "github738"   | "-lpJ {resourceDirectory}/{testDirectory}/PDS4_KPLO_IngestLDD.xml" | "0 error(s)"     |
