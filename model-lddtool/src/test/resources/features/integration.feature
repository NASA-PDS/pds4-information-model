Feature: Running integration tests for lddtool
  As an lddtool user
  I want to run lddtool with specific command arguments
  So that I can verify if it provides the expected response

  Scenario Outline: Verifying lddtool output against specified assertions
    Given the directories <inputDirectory>, <outputDirectory>, and command arguments <commandArgs>
    When lddtool is run
    Then the produced output from lddtool command should <assertType> <output> in <actualOutputFile>

    Examples:
      | testName                                                                                                                        | inputDirectory                  | outputDirectory                    | commandArgs                                     | assertType    | output                                           | actualOutputFile     |
      #| "NASA-PDS/pds4-information-model#733 LDDTool is throwing ERROR SetMasterAttrXMLBaseDataTypeFromDataType - Data Type is missing" | "src/test/resources/github733"  | "target/generated-files/github733" | "-lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "not contain" | "ERROR SetMasterAttrXMLBaseDataTypeFromDataType" | "lddtool-output.txt" |
      #| "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO"                                                | "src/test/resources/github738"  | "target/generated-files/github738" | "-lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "contain"     | "http://www.kari.re.kr/kpds/mission/kplo/v1"     | ".xsd"               |
      #| "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO"                                                | "src/test/resources/github738"  | "target/generated-files/github738" | "-lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "not contain" | "http://www.kari.re.kr/kpdsmission/kplo/v1 "     | ".xsd"               |


  Scenario Outline: Comparing lddtool output files to expected files
    Given the directories <inputDirectory>, <outputDirectory>, and command arguments <commandArgs>
    When lddtool is run
    Then the contents of file <actualOutputFile> should match <expectedOutputFile> except for comma-separated strings in <excludeStrings>

    Examples:
      | testName                                                                         | inputDirectory                  | outputDirectory                    | commandArgs                                     | expectedOutputFile         | actualOutputFile    | excludeStrings                            |
      | "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO" | "src/test/resources/github738"  | "target/generated-files/github738" | "-lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "PDS4_KPLO_1N00_1000.xsd"  | ".xsd"              | "PDS4 XML/Schema,Generated from the PDS4" |
