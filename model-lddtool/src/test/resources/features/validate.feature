Feature: pds4_information_model_validate_integration

  Scenario Outline: <testId>
    Given the test <testName> at directory <testDir>
    Given the latest PDS4 schema/schematron is generated for IM Version <pds4Version>
    Given a new LDD is generated using the IngestLDDs <ingestLDDFileName>
    Given expected error/warning count <messageCount> with expected error from ValidateProblems enumeration <problemEnum>
    When execute validate command with <commandArgs> as arguments
    Then produced output from validate command should be similar to reported <testDir> JSON report or no error reported.

    # The scenario about will automatically build the latest PDS4 Information Model as part of the validation.
    
    # NOTE: The following flags are prepended to the execution by default: --disable-context-mismatch-warnings --report-style json --skip-content-validation --report-file {reportDir}/
    
    # WARNING: If you reuse the same LIDs for a single test case, be sure to reference each label individually in commandArgs
    #          otherwise the tests will not work properly.
    
    @v15.2.x
    Examples: 
| testId                               | testName                        | testDir     | messageCount         | problemEnum        | commandArgs                                             | ingestLDDFileName         | pds4Version |
| NASA-PDS/pds4-information-model#753  | "new Current units nA microA"   | "github753"  |           0  | "totalErrors" | "-t {resourceUpdateVersionDir}/github753/pc__d139.xml" | "PDS4_MARS2020_IngestLDD.xml" | "" |
| NASA-PDS/pds4-information-model#784a | "Encoded_Video for Product_Ancillary" | "github784a" |           0 | "totalErrors" | "-t {resourceUpdateVersionDir}/github784/FUV2017032anc.xml" | "" | "" |
| NASA-PDS/pds4-information-model#784b | "Encoded_Video for Product_Browse" | "github784b" |           0  | "totalErrors" | "-t {resourceUpdateVersionDir}/github784/FUV2017032brw.xml" | "" | "" |
| NASA-PDS/pds4-information-model#794  | "new namespace vikinglander"    | "github794"  |           0  | "totalErrors" | "-t {resourceUpdateVersionDir}/github794/ORB_35_STAR_SCANNER.xml" | "PDS4_VIKINGLANDER_IngestLDD.xml" | "" |
| NASA-PDS/pds4-information-model#795a | "reference_type Failures for all Data Types"             | "github795"  |          10  | "SCHEMATRON_ERROR,INTERNAL_ERROR" | "-R pds4.bundle -t {resourceUpdateVersionDir}/github795" | "" | "" |
| NASA-PDS/pds4-information-model#795b | "reference_type Failures for Product_External"             | "github795b" |           2  | "SCHEMATRON_ERROR" | "-t {resourceUpdateVersionDir}/github795b/occultation_prediction_som_manifest.xml" | "" | "" |
| NASA-PDS/pds4-information-model#797  | "test schematron for kernel_type checks"             | "github797"  |           3  | "SCHEMATRON_ERROR" | "-t {resourceUpdateVersionDir}/github797/test_label1_FAIL.xml {resourceUpdateVersionDir}/github797/u5.xml" | "PDS4_GEOM_IngestLDD.xml" | "" |
| NASA-PDS/pds4-information-model#829  | "new unit mrad/pixels"          | "github829"  |           0 |  "totalErrors" | "-t {resourceUpdateVersionDir}/github829/ORB_35_STAR_SCANNER.xml" | "PDS4_HST_IngestLDD.xml" | "" |
| NASA-PDS/pds4-information-model#831  | "add Array_1D_Spectrum"         | "github831"  |           0 |  "totalErrors" | "-t {resourceUpdateVersionDir}/github831/" | "" | "" |

    @v15.3.x
    Examples: 
| testId                                  | testName                                      | testDir       | messageCount | problemEnum   | commandArgs                                                                    | ingestLDDFileName               | pds4Version |
# Primary check is that LDDTool executes successfully, which it did not before
| NASA-PDS/pds4-information-model#852a  | "Test fix for bug generating LDDs for 1C00"   | "github852a"  |           0  | "totalErrors" | "-t {resourcePinnedVersionDir}/github852a/valid_1C00_lcross_nir2_cal_20090622162743896.xml" | "PDS4_LCROSS_IngestLDD_1000.xml" | "1C00" |
| NASA-PDS/pds4-information-model#852b  | "Test fix for bug generating LDDs for 1D00"   | "github852b"  |           0  | "totalErrors" | "-t {resourcePinnedVersionDir}/github852b/valid_1D00_lcross_nir2_cal_20090622162743896.xml" | "PDS4_LCROSS_IngestLDD_1000.xml" | "1D00" |
| NASA-PDS/pds4-information-model#852c  | "Test fix for bug generating LDDs for 1E00"   | "github852c"  |           0  | "totalErrors" | "-t {resourcePinnedVersionDir}/github852c/valid_1E00_lcross_nir2_cal_20090622162743896.xml" | "PDS4_LCROSS_IngestLDD_1000.xml" | "1E00" |



