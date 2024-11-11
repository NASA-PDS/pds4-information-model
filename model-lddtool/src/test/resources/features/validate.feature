Feature: pds4_information_model_integration

  Scenario Outline: <testId>
    Given the test <testName> at directory <testDir>
    Given the latest PDS4 schema/schematron is generated
    Given a new LDD is generated using the IngestLDDs <ingestLDDFileName>
    Given expected error/warning count <messageCount> with expected error/warning text of <messageText> with expected error from ValidateProblems enumeration <problemEnum>
    When execute validate command with <commandArgs> as arguments
    Then produced output from validate command should be similar to reported <testDir> JSON report or no error reported.

    # The scenario about will automatically build the latest PDS4 Information Model as part of the validation.
    
    # NOTE: The following flags are prepended to the execution by default: --disable-context-mismatch-warnings --report-style json --skip-content-validation --report-file {reportDir}/
    
    @v15.2.x
    Examples: 
      | testId | testName | testDir     | messageCount | messageText         | problemEnum        | commandArgs                                             | ingestLDDFileName         |
      | "NASA-PDS/pds4-information-model#795a" | "Expected reference_type Failures for all Data Types"       | "github795" |           9 | "9 errors expected" | "SCHEMATRON_ERROR" | "--rule pds4.bundle --target {resourceDir}/github795/" | "" |

      | "NASA-PDS/pds4-information-model#795b"| "Expected reference_type Failure for Product_External"       | "github795b" |           1 | "1 errors expected" | "SCHEMATRON_ERROR" | "--target {resourceDir}/github795b/" | "" |
      
      | "NASA-PDS/pds4-information-model#797" | "Expected schematron failure for kernel_type values"         | "github797" |            3 | "3 errors expected" | "SCHEMATRON_ERROR" | "--target {resourceDir}/github797/test_label1_FAIL.xml {resourceDir}/github797/u5.xml" | "PDS4_GEOM_IngestLDD.xml" |
      
      | "NASA-PDS/pds4-information-model#816" | "Expected failure for Product_External LID formation rule"   | "github816" |            1 | "1 errors expected" | "SCHEMATRON_ERROR" | "--target {resourceDir}/github816/" | "" |
      
      

