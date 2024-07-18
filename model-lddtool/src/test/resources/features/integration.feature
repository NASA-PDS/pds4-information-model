Feature: Running integration tests for lddtool
  As an lddtool user
  I want to run lddtool with specific command arguments
  So that I can verify if it provides the expected response

  Scenario Outline: Running lddtool with different parameters
    Given the test directories <resourceDirectory> and <testDirectory> and command arguments <commandArgs>
    When lddtool is run
    Then the produced output from lddtool command should <assertType> <output>

    Examples:
      | testName                                                                                                                        | resourceDirectory    | testDirectory | commandArgs                                                        | assertType    | output                                           |
      | "NASA-PDS/pds4-information-model#733 LDDTool is throwing ERROR SetMasterAttrXMLBaseDataTypeFromDataType - Data Type is missing" | "src/test/resources" | "github733"   | "-lpJ {resourceDirectory}/{testDirectory}/PDS4_KPLO_IngestLDD.xml" | "not contain" | "ERROR SetMasterAttrXMLBaseDataTypeFromDataType" |
      #| "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO"                                                | "src/test/resources" | "github738"   | "-lpJ {resourceDirectory}/{testDirectory}/PDS4_KPLO_IngestLDD.xml" | "contain"     | "0 error(s)"                                     |
