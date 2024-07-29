Feature: Running integration tests for lddtool
  As an lddtool user
  I want to run lddtool with specific command arguments
  So that I can verify if it provides the expected response

  Scenario Outline: Verifying lddtool output against specified assertions
    Given the directories <inputDirectory>, <outputDirectory>, and command arguments <commandArgs>
    When lddtool is run
    Then the produced output from lddtool command should <assertType> <output> in <actualOutputFile> file

    Examples:
      | testName                                                                                                                        | inputDirectory                  | outputDirectory                    | commandArgs                                     | assertType    | output                                                                                                     | actualOutputFile     |
      #| "NASA-PDS/pds4-information-model#733 LDDTool is throwing ERROR SetMasterAttrXMLBaseDataTypeFromDataType - Data Type is missing" | "src/test/resources/github733"  | "target/generated-files/github733" | "-lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "not contain" | "ERROR SetMasterAttrXMLBaseDataTypeFromDataType"                                                           | "lddtool-output.txt" |
      #| "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO"                                                | "src/test/resources/github738"  | "target/generated-files/github738" | "-lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "contain"     | "http://www.kari.re.kr/kpds/mission/kplo/v1"                                                               | ".xsd"               |
      #| "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO"                                                | "src/test/resources/github738"  | "target/generated-files/github738" | "-lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "not contain" | "http://www.kari.re.kr/kpdsmission/kplo/v1 "                                                               | ".xsd"               |
      #| "NASA-PDS/pds4-information-model#716 [namespace-registry]update namespace URL for KPDS"                                         | "src/test/resources/github716"  | "target/generated-files/github716" | "-lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "contain"     | "http://www.kari.re.kr/kpds/mission/kplo/v1"                                                               | ".sch"               |
      #| "NASA-PDS/pds4-information-model#744 LDDTool producing invalid XSD for Voyager LDD"                                             | "src/test/resources/github744"  | "target/generated-files/github744" | "-lpJ {inputDirectory}/PDS4_VGR_IngestLDD.xml"  | "not contain" | "The element type \"xs:documentation\" must be terminated by the matching end-tag \"</xs:documentation>\"" | ".xsd"               | 


  Scenario Outline: Comparing lddtool output files to expected files
    Given the directories <inputDirectory>, <outputDirectory>, and command arguments <commandArgs>
    When lddtool is run
    Then the contents of file <actualOutputFile> should match <expectedOutputFile> except for lines containing comma-separated strings in <excludeStrings>

    Examples:
      | testName                                                                         | inputDirectory                  | outputDirectory                    | commandArgs                                     | expectedOutputFile         | actualOutputFile    | excludeStrings                            |
      | "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO" | "src/test/resources/github738"  | "target/generated-files/github738" | "-lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "PDS4_KPLO_1N00_1000.xsd"  | ".xsd"              | "PDS4 XML/Schema,Generated from the PDS4" |
