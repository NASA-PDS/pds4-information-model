Feature: <testId>
  As an lddtool user
  I want to run lddtool with specific command arguments
  So that I can verify if it provides the expected response

  Scenario Outline: Verifying lddtool output against specified assertions
    Given the directories <inputDirectory>, <outputDirectory>, and command arguments <commandArgs>
    When lddtool is run
    Then the produced output from lddtool command should <assertType> <output> in <actualOutputFile> file

		@NASA-PDS/pds4-information-model#738 @B15.0
    Examples:
      | testId                                | testName                                                                                                                        | inputDirectory                  | outputDirectory                    | commandArgs                                     | assertType    | output                                                                                                     | actualOutputFile     |
      | "NASA-PDS/pds4-information-model#733" | "NASA-PDS/pds4-information-model#733 LDDTool is throwing ERROR SetMasterAttrXMLBaseDataTypeFromDataType - Data Type is missing" | "src/test/resources/data/update_version/github733"  | "target/generated-files/github733" | "-V 1N00 -lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "not contain" | "ERROR SetMasterAttrXMLBaseDataTypeFromDataType"                                                           | "lddtool-output.txt" |
    
    @NASA-PDS/pds4-information-model#738 @B15.0  
    Examples:
      | testId                                | testName                                                                                                                        | inputDirectory                  | outputDirectory                    | commandArgs                                     | assertType    | output                                                                                                     | actualOutputFile     |
      | "NASA-PDS/pds4-information-model#738a" | "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO"                                                | "src/test/resources/data/update_version/github738"  | "target/generated-files/github738_1" | "-V 1N00 -lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "contain"     | "http://www.kari.re.kr/kpds/mission/kplo/v1"                                                               | ".xsd"               |
      | "NASA-PDS/pds4-information-model#738b" | "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO"                                                | "src/test/resources/data/update_version/github738"  | "target/generated-files/github738_2" | "-V 1N00 -lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "not contain" | "http://www.kari.re.kr/kpdsmission/kplo/v1 "                                                               | ".xsd"               |
      
		@NASA-PDS/pds4-information-model#716 @B15.0
    Examples:
      | testId                                | testName                                                                                                                        | inputDirectory                  | outputDirectory                    | commandArgs                                     | assertType    | output                                                                                                     | actualOutputFile     |
      | "NASA-PDS/pds4-information-model#716" | "NASA-PDS/pds4-information-model#716 [namespace-registry]update namespace URL for KPDS"                                         | "src/test/resources/data/update_version/github716"  | "target/generated-files/github716" | "-V 1N00 -lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "contain"     | "http://www.kari.re.kr/kpds/mission/kplo/v1"                                                               | ".sch"               |
    
    @NASA-PDS/pds4-information-model#744 @B15.0  
		Examples:
      | testId                                | testName                                                                                                                        | inputDirectory                  | outputDirectory                    | commandArgs                                     | assertType    | output                                                                                                     | actualOutputFile     |
      | "NASA-PDS/pds4-information-model#744" | "NASA-PDS/pds4-information-model#744 LDDTool producing invalid XSD for Voyager LDD"                                             | "src/test/resources/data/update_version/github744"  | "target/generated-files/github744" | "-lpJ {inputDirectory}/PDS4_VGR_IngestLDD.xml"  | "not contain" | "The element type \"xs:documentation\" must be terminated by the matching end-tag \"</xs:documentation>\"" | ".xsd"               | 


  Scenario Outline: Comparing lddtool output files to expected files
    Given the directories <inputDirectory>, <outputDirectory>, and command arguments <commandArgs>
    When lddtool is run
    Then the contents of file <actualOutputFile> should match <expectedOutputFile> except for lines containing comma-separated strings in <excludeStrings>

		@NASA-PDS/pds4-information-model#738 @B15.0
    Examples:
      | testId                                | testName                                                                         | inputDirectory                  | outputDirectory                    | commandArgs                                     | expectedOutputFile         | actualOutputFile    | excludeStrings                            |
      | "NASA-PDS/pds4-information-model#738" | "NASA-PDS/pds4-information-model#738 LDDTool is not creating valid URL for KPLO" | "src/test/resources/data/update_version/github738"  | "target/generated-files/github738_3" | "-V 1N00 -lpJ {inputDirectory}/PDS4_KPLO_IngestLDD.xml" | "PDS4_KPLO_1N00_1000.xsd"  | ".xsd"              | "PDS4 XML/Schema,Generated from the PDS4" |

    
    @NASA-PDS/pds4-information-model#738 @B15.1
    Examples:
      | testId                                | testName                                                                         | inputDirectory                       | outputDirectory                     | commandArgs                                     | expectedOutputFile              | actualOutputFile                      | excludeStrings                   |
      | "NASA-PDS/pds4-information-model#867a" | "Check schema for 1N00"                                                         | "src/test/resources/data/update_version/github867/1N00"  | "target/generated-files/github867a" | "-p -V 1N00"                                    | "PDS4_PDS_1N00.xsd"             | "PDS4_PDS_1N00.xsd"                   | "PDS4 XML/Schema"                |
      | "NASA-PDS/pds4-information-model#867b" | "Check schematron for 1N00"                                                     | "src/test/resources/data/update_version/github867/1N00"  | "target/generated-files/github867b" | "-p -V 1N00"                                    | "PDS4_PDS_1N00.sch"             | "PDS4_PDS_1N00.sch"                   | "PDS4 Schematron for Name Space" |
      | "NASA-PDS/pds4-information-model#867c" | "Check pins output for 1N00"                                                    | "src/test/resources/data/update_version/github867/1N00"  | "target/generated-files/github867c" | "-p -V 1N00"                                    | "dd11179_GenPClass.pins"        | "dd11179_GenPClass.pins"              | ""                               |
      | "NASA-PDS/pds4-information-model#867Oa" | "Check schema for 1O00"                                                         | "src/test/resources/data/update_version/github867/1O00"  | "target/generated-files/github867Oa" | "-p -V 1O00"                                    | "PDS4_PDS_1O00.xsd"             | "PDS4_PDS_1O00.xsd"                   | "PDS4 XML/Schema"                |
      | "NASA-PDS/pds4-information-model#867Ob" | "Check schematron for 1O00"                                                     | "src/test/resources/data/update_version/github867/1O00"  | "target/generated-files/github867Ob" | "-p -V 1O00"                                    | "PDS4_PDS_1O00.sch"             | "PDS4_PDS_1O00.sch"                   | "PDS4 Schematron for Name Space" |
      | "NASA-PDS/pds4-information-model#867Oc" | "Check pins output for 1O00"                                                    | "src/test/resources/data/update_version/github867/1O00"  | "target/generated-files/github867Oc" | "-p -V 1O00"                                    | "dd11179_GenPClass.pins"        | "dd11179_GenPClass.pins"              | ""                               |
      