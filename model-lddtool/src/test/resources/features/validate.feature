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
| testId                               | testName                                                                   | testDir      | messageCount| problemEnum        | commandArgs                                             | ingestLDDFileName         | pds4Version |
#Note that the errors in #752 will be corrected in the next version of the IM; see readme
| NASA-PDS/pds4-information-model#752  | "Update Constraints on Author/Editor Lists (gitHub-CCB-15/356)"            | "github752"  |           6 | "totalErrors"      | "-t {resourceDir}/github752/geo_bundle_hausrath_m2020_pixl_naltsos.xml {resourceDir}/github752/geo_bundle_mex_marsis_optim.xml {resourceDir}/github752/geo_pixl_edr_sis.xml {resourceDir}/github752/img_bundle_gbo.sdss-moc.phot.xml {resourceDir}/github752/ppi_TECH_DOC.xml {resourceDir}/github752/ppi_bundle-voyager2.pws.wf.xml {resourceDir}/github752/test_case_collection_20240122.xml {resourceDir}/github752/test_file_1_only_author_20240122.xml {resourceDir}/github752/test_file_2_list_author_20240122.xml {resourceDir}/github752/test_file_5_20240122.xml {resourceDir}/github752/test_file_ror_20240122.xml" | "" | "" |
| NASA-PDS/pds4-information-model#753  | "added nA & microA to Units_of_Current (gitHub-CCB-16)"                    | "github753"  |           0 | "totalErrors"      | "-t {resourceDir}/github753/pc__d139.xml" | "PDS4_MARS2020_IngestLDD.xml" | "" |
| NASA-PDS/pds4-information-model#784a | "Encoded_Video for Product_Ancillary (gitHub-CCB-325)"                     | "github784a" |           0 | "totalErrors"      | "-t {resourceDir}/github784/FUV2017032anc.xml" | "" | "" |
| NASA-PDS/pds4-information-model#784b | "Encoded_Video for Product_Browse (gitHub-CCB-325)"                        | "github784b" |           0 | "totalErrors"      | "-t {resourceDir}/github784/FUV2017032brw.xml" | "" | "" |
| NASA-PDS/pds4-information-model#794  | "new namespace vikinglander"                                               | "github794"  |           0 | "totalErrors"      | "-t {resourceDir}/github794/ORB_35_STAR_SCANNER.xml" | "PDS4_VIKINGLANDER_IngestLDD.xml" | "" |
| NASA-PDS/pds4-information-model#795a | "reference_type Failures for all Data Types"                               | "github795"  |          10 | "SCHEMATRON_ERROR,INTERNAL_ERROR" | "-R pds4.bundle -t {resourceDir}/github795" | "" | "" |
| NASA-PDS/pds4-information-model#795b | "reference_type Failures for Product_External"                             | "github795b" |           2 | "SCHEMATRON_ERROR" | "-t {resourceDir}/github795b/occultation_prediction_som_manifest.xml" | "" | "" |
| NASA-PDS/pds4-information-model#797  | "test schematron for kernel_type checks"                                   | "github797"  |           3 | "SCHEMATRON_ERROR" | "-t {resourceDir}/github797/test_label1_FAIL.xml {resourceDir}/github797/u5.xml" | "PDS4_GEOM_IngestLDD.xml" | "" |
| NASA-PDS/pds4-information-model#829  | "added mrad/pixel to Units_of_Pixel_Resolution_Angular (gitHub-CCB-38)"    | "github829"  |           5 |  "totalErrors"     | "-t {resourceDir}/github829/" | "PDS4_HST_IngestLDD.xml" | "" |
| NASA-PDS/pds4-information-model#831  | "add Array_1D_Spectrum (gitHub-CCB-42)"                                    | "github831"  |           0 |  "totalErrors"     | "-t {resourceDir}/github831/" | "" | "" |
| NASA-PDS/pds4-information-model#837  | "Incorrect SCH rules for type:External (gitHub-CCB-29)"                    | "github837"  |           2 |  "totalErrors"     | "-t {resourceDir}/github837/" | "" | "" |
| NASA-PDS/pds4-information-model#894  | "Additional schematron rules to check reference_type (gitHub-CCB-7)"       | "github894"  |          13 |  "totalErrors"     | "-t {resourceDir}/github894/test_case_browse_ref_FAIL_20240318.xml {resourceDir}/github894/test_case_browse_ref_VALID_20240318.xml {resourceDir}/github894/test_case_bundle_ref_FAIL_20240318.xml {resourceDir}/github894/test_case_bundle_ref_VALID_20240318.xml {resourceDir}/github894/test_case_collection_ref_FAIL_20240318.xml {resourceDir}/github894/test_case_collection_ref_VALID_20240318.xml {resourceDir}/github894/test_case_external_ref_FAIL_20240318.xml {resourceDir}/github894/test_case_external_ref_VALID_20240318.xml {resourceDir}/github894/test_case_native_ref_FAIL_202403318.xml {resourceDir}/github894/test_case_native_ref_VALID_202403318.xml {resourceDir}/github894/test_case_observational_FAIL_20240318.xml {resourceDir}/github894/test_case_observational_VALID_20240318.xml {resourceDir}/github894/test_case_SPICE_FAIL_20240318.xml {resourceDir}/github894/test_case_SPICE_VALID_20240318.xml {resourceDir}/github894/test_case_xml_schema_FAIL_20240318.xml {resourceDir}/github894/test_case_xml_schema_VALID_20240318.xml"  | "" | "" |


    @v15.3.x
    Examples: 
| testId                                  | testName                                      | testDir       | messageCount | problemEnum   | commandArgs                                                                    | ingestLDDFileName               | pds4Version |
# Primary check is that LDDTool executes successfully, which it did not before
| NASA-PDS/pds4-information-model#852a  | "Test fix for bug generating LDDs for 1C00"   | "github852a"  |           0  | "totalErrors" | "-t {resourceDir}/github852a/valid_1C00_lcross_nir2_cal_20090622162743896.xml" | "PDS4_LCROSS_IngestLDD_1000.xml" | "1C00" |
| NASA-PDS/pds4-information-model#852b  | "Test fix for bug generating LDDs for 1D00"   | "github852b"  |           0  | "totalErrors" | "-t {resourceDir}/github852b/valid_1D00_lcross_nir2_cal_20090622162743896.xml" | "PDS4_LCROSS_IngestLDD_1000.xml" | "1D00" |
| NASA-PDS/pds4-information-model#852c  | "Test fix for bug generating LDDs for 1E00"   | "github852c"  |           0  | "totalErrors" | "-t {resourceDir}/github852c/valid_1E00_lcross_nir2_cal_20090622162743896.xml" | "PDS4_LCROSS_IngestLDD_1000.xml" | "1E00" |



