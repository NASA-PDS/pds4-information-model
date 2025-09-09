
IM#942: new permissible value for File_Area_Observational.Header.parsing_standard_id: CDF 3.9 ISTP/IACG
          -- modifications to SCH:

--- only validate local directory; no children directories
cd D:\WINWORD\Standards-DataDictionary\PDS4_SCRs\CCB-60_parsing_standard_id_20250430\aaaBuild_testcases_20250827
validate_363 -t *.xml -r log_validate.txt --schema PDS4_PDS_1P00.xsd --schematron PDS4_PDS_1P00.sch --skip-content-validation --local

Change this:

  <sch:pattern>
    <sch:rule context="pds:Header/pds:parsing_standard_id">
      <sch:assert test=". = ('7-Bit ASCII Text', 'CDF 3.4 ISTP/IACG', 'CDF 3.5 ISTP/IACG', 'CDF 3.6 ISTP/IACG', 'CDF 3.7 ISTP/IACG', 'CDF 3.8 ISTP/IACG', 'FITS 3.0', 'FITS 4.0', 'ISIS2', 'ISIS2 History Label', 'ISIS3', 'PDS DSV 1', 'PDS ODL 2', 'PDS3', 'Pre-PDS3', 'TIFF 6.0', 'UTF-8 Text', 'VICAR1', 'VICAR2')">
        <title>pds:Header/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:Header/pds:parsing_standard_id must be equal to one of the following values '7-Bit ASCII Text', 'CDF 3.4 ISTP/IACG', 'CDF 3.5 ISTP/IACG', 'CDF 3.6 ISTP/IACG', 'CDF 3.7 ISTP/IACG', 'CDF 3.8 ISTP/IACG', 'CDF 3.9 ISTP/IACG', 'FITS 3.0', 'FITS 4.0', 'ISIS2', 'ISIS2 History Label', 'ISIS3', 'PDS DSV 1', 'PDS ODL 2', 'PDS3', 'Pre-PDS3', 'TIFF 6.0', 'UTF-8 Text', 'VICAR1', 'VICAR2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  
To this:

  <sch:pattern>
    <sch:rule context="pds:Header/pds:parsing_standard_id">
      <sch:assert test=". = ('7-Bit ASCII Text', 'CDF 3.4 ISTP/IACG', 'CDF 3.5 ISTP/IACG', 'CDF 3.6 ISTP/IACG', 'CDF 3.7 ISTP/IACG', 'CDF 3.8 ISTP/IACG', 'CDF 3.9 ISTP/IACG', 'FITS 3.0', 'FITS 4.0', 'ISIS2', 'ISIS2 History Label', 'ISIS3', 'PDS DSV 1', 'PDS ODL 2', 'PDS3', 'Pre-PDS3', 'TIFF 6.0', 'UTF-8 Text', 'VICAR1', 'VICAR2')">
        <title>pds:Header/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:Header/pds:parsing_standard_id must be equal to one of the following values '7-Bit ASCII Text', 'CDF 3.4 ISTP/IACG', 'CDF 3.5 ISTP/IACG', 'CDF 3.6 ISTP/IACG', 'CDF 3.7 ISTP/IACG', 'CDF 3.8 ISTP/IACG', 'CDF 3.9 ISTP/IACG', 'FITS 3.0', 'FITS 4.0', 'ISIS2', 'ISIS2 History Label', 'ISIS3', 'PDS DSV 1', 'PDS ODL 2', 'PDS3', 'Pre-PDS3', 'TIFF 6.0', 'UTF-8 Text', 'VICAR1', 'VICAR2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  
    
=============================
PDS Validate Tool Report

Configuration:
   Version     3.6.3
   Date        2025-08-28T16:03:05Z

Parameters:
   Targets                        [file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-60_parsing_standard_id_20250430/aaaBuild_testcases_20250827/tc_Fail_invalid-value_20250828.xml, file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-60_parsing_standard_id_20250430/aaaBuild_testcases_20250827/tc_Valid_new_value_20250828.xml]
   User Specified Schemas         [file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-60_parsing_standard_id_20250430/aaaBuild_testcases_20250827/PDS4_PDS_1P00.xsd]
   User Specified Schematrons     [file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-60_parsing_standard_id_20250430/aaaBuild_testcases_20250827/PDS4_PDS_1P00.sch]
   Severity Level                 WARNING
   Recurse Directories            false
   File Filters Used              [*.xml, *.XML]
   Data Content Validation        off
   Product Level Validation       on
   Max Errors                     100000
   Registered Contexts File       E:\PDS_Validate_tool\validate_v3_6_3_20241122\validate-3.6.3\resources\registered_context_products.json



Product Level Validation Results

  FAIL: file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-60_parsing_standard_id_20250430/aaaBuild_testcases_20250827/tc_Fail_invalid-value_20250828.xml
      ERROR  [error.label.schematron]   line 104, 28: The attribute pds:Header/pds:parsing_standard_id must be equal to one of the following values '7-Bit ASCII Text', 'CDF 3.4 ISTP/IACG', 'CDF 3.5 ISTP/IACG', 'CDF 3.6 ISTP/IACG', 'CDF 3.7 ISTP/IACG', 'CDF 3.8 ISTP/IACG', 'CDF 3.9 ISTP/IACG', 'FITS 3.0', 'FITS 4.0', 'ISIS2', 'ISIS2 History Label', 'ISIS3', 'PDS DSV 1', 'PDS ODL 2', 'PDS3', 'Pre-PDS3', 'TIFF 6.0', 'UTF-8 Text', 'VICAR1', 'VICAR2'.
      WARNING  [warning.label.context_ref_mismatch]   line 47: Context reference name mismatch. Value: 'Product_Observational' Expected one of: '[Mars Reconnaissance Orbiter]'
        1 product validation(s) completed

  PASS: file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-60_parsing_standard_id_20250430/aaaBuild_testcases_20250827/tc_Valid_new_value_20250828.xml
      WARNING  [warning.label.context_ref_mismatch]   line 47: Context reference name mismatch. Value: 'Product_Observational' Expected one of: '[Mars Reconnaissance Orbiter]'
        2 product validation(s) completed

Summary:

  2 product(s)
  1 error(s)
  2 warning(s)

  Product Validation Summary:
    1          product(s) passed
    1          product(s) failed
    0          product(s) skipped
    2          product(s) total

  Referential Integrity Check Summary:
    0          check(s) passed
    0          check(s) failed
    0          check(s) skipped
    0          check(s) total

  Message Types:
    1            error.label.schematron
    2            warning.label.context_ref_mismatch

End of Report
=============================

=============================
mvn clean test
=============================

[INFO] Tests run: 42, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 130.6 s -- in cucumber.CucumberTest
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 42, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Reactor Summary for PDS4 Information Model 15.4.0-SNAPSHOT:
[INFO]
[INFO] PDS4 Information Model ............................. SUCCESS [  0.172 s]
[INFO] Model DM-Document Maven Plugin ..................... SUCCESS [  9.201 s]
[INFO] Model Ontology ..................................... SUCCESS [  0.603 s]
[INFO] Local Data Dictionary Tool ......................... SUCCESS [02:32 min]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  02:43 min
[INFO] Finished at: 2025-09-09T07:47:57-07:00
[INFO] ------------------------------------------------------------------------

D:\GitHub_projects\pds4-information-model>
