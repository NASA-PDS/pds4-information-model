Feature: Running integration tests for validate module

  Scenario Outline: Execute validate command for tests below.
    Given the test <testName> at directory <testDir>
    Given the latest PDS4 schema/schematron is generated
    Given a new LDD is generated using the IngestLDDs <ingestLDDFileName>
    Given expected error/warning count <messageCount> with expected error/warning text of <messageText> with expected error from ValidateProblems enumeration <problemEnum>
    When execute validate command with <commandArgs> as arguments
    Then produced output from validate command should be similar to reported <testDir> JSON report or no error reported.

    # The scenario about will automatically build the latest PDS4 Information Model as part of the validation.
    
    @NASA-PDS/pds4-information-model#797 @B15.0
    Examples: 
      | testName | testDir     | messageCount | messageText         | problemEnum        | commandArgs                                             | ingestLDDFileName         |
      | "Test1"  | "github797" |            2 | "2 errors expected" | "SCHEMATRON_ERROR" | "--target {resourceDir}/github797/test_label1_FAIL.xml" | "PDS4_GEOM_IngestLDD.xml" |
