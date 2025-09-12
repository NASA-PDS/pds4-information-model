
IM#950: Transfer_Manifest class should enforce data types
          -- modifications to SCH:

--- only validate local directory; no children directories
cd D:\WINWORD\Standards-DataDictionary\PDS4_SCRs\CCB-62_Transfer_Manifest_enforce_20250731\ccb62-test-package-20250730
validate_363 -t *.xml -r log_validate.txt --schema PDS4_PDS_1P00.xsd --schematron PDS4_PDS_1P00.sch --skip-content-validation --local

Add as new:

  <sch:pattern>
    <sch:rule context="pds:Transfer_Manifest/pds:offset">
      <sch:assert test=". = '0'">
        <title>pds:Transfer_Manifest/pds:offset</title>
        The attribute pds:Transfer_Manifest/pds:offset must be equal to '0'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Transfer_Manifest/pds:Record_Character">
      <sch:assert test="(count(pds:Field_Character) eq 2)">
        <title>pds:Transfer_Manifest/pds:Record_Character</title>
        pds:Transfer_Manifest.pds:Record_Character.pds:Field_Character does not match the expected number of instances (2).</sch:assert>
      <sch:assert test="pds:fields eq '2'">
        <title>pds:Transfer_Manifest/pds:Record_Character/name</title>
        The attribute pds:fields must be equal to '2'.</sch:assert>
      <sch:assert test="pds:groups eq '0'">
        <title>pds:Transfer_Manifest/pds:Record_Character/name</title>
        The attribute pds:groups must be equal to '0'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Transfer_Manifest/pds:Record_Character/pds:Field_Character[1]">
      <sch:assert test="pds:name eq 'LIDVID'">
        <title>pds:Transfer_Manifest/pds:Record_Character/pds:Field_Character[1]/name</title>
        The first field of a Transfer_Manifest must have name set to 'LIDVID'.</sch:assert>
      <sch:assert test="pds:field_number eq '1'">
        <title>pds:Transfer_Manifest/pds:Record_Character/pds:Field_Character[1]/field_number</title>
        The first field of a Transfer_Manifest must have field_number set to 1.</sch:assert>
      <sch:assert test="pds:field_location eq '1'">
        <title>pds:Transfer_Manifest/pds:Record_Character/pds:Field_Character[1]/field_location</title>
        The first field of a Transfer_Manifest must have field_location set to 1.</sch:assert>
      <sch:assert test="pds:data_type eq 'ASCII_LIDVID'">
        <title>pds:Transfer_Manifest/pds:Record_Character/pds:Field_Character[1]/data_type</title>
        The first field of a Transfer_Manifest must have data type set to 'ASCII_LIDVID'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Transfer_Manifest/pds:Record_Character/pds:Field_Character[2]">
      <sch:assert test="pds:name eq 'File Specification'">
        <title>pds:Transfer_Manifest/pds:Record_Character/pds:Field_Character[2]/name</title>
        The second field of a Transfer_Manifest must have name set to 'File Specification'.</sch:assert>
      <sch:assert test="pds:field_number eq '2'">
        <title>pds:Transfer_Manifest/pds:Record_Character/pds:Field_Character[2]/field_number</title>
        The second field of a Transfer_Manifest must have field_number set to 2.</sch:assert>
      <sch:assert test="pds:data_type eq 'ASCII_File_Specification_Name'">
        <title>pds:Transfer_Manifest/pds:Record_Character/pds:Field_Character[2]/data_type</title>
        The second field of a Transfer_Manifest must have data type set to 'ASCII_File_Specification_Name'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  
To this:

  see above
    

PDS Validate Tool Report

Configuration:
   Version     3.6.3
   Date        2025-09-12T18:02:26Z

Parameters:
   Targets                        [file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_FAIL_data_type_20250730.xml, file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_FAIL_field_count_20250730.xml, file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_FAIL_field_groups_20250730.xml, file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_FAIL_field_name_20250730.xml, file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_FAIL_field_number_20250730.xml, file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_PASS_20250730.xml]
   User Specified Schemas         [file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/PDS4_PDS_1P00.xsd]
   User Specified Schematrons     [file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/PDS4_PDS_1P00.sch]
   Severity Level                 WARNING
   Recurse Directories            false
   File Filters Used              [*.xml, *.XML]
   Data Content Validation        off
   Product Level Validation       on
   Max Errors                     100000
   Registered Contexts File       E:\PDS_Validate_tool\validate_v3_6_3_20241122\validate-3.6.3\resources\registered_context_products.json



Product Level Validation Results

  FAIL: file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_FAIL_data_type_20250730.xml
      ERROR  [error.label.schematron]   line 34, 38: The first field of a Transfer_Manifest must have data type set to 'ASCII_LIDVID'.
      ERROR  [error.table.bad_field_read]   line 0: Data Object #0 (): Cannot invoke "gov.nasa.arc.pds.xml.generated.FileAreaChecksumManifest.getFile()" because the return value of "gov.nasa.arc.pds.xml.generated.InformationPackageComponent.getFileAreaChecksumManifest()" is null
        1 product validation(s) completed

  FAIL: file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_FAIL_field_count_20250730.xml
      ERROR  [error.label.schematron]   line 29, 35: The number of Field_Character elements (3) does not match the number found in the 'fields' attribute (2).
      ERROR  [error.label.schematron]   line 29, 35: pds:Transfer_Manifest.pds:Record_Character.pds:Field_Character does not match the expected number of instances (2).
      ERROR  [error.table.bad_field_read]   line 0: Data Object #0 (): Cannot invoke "gov.nasa.arc.pds.xml.generated.FileAreaChecksumManifest.getFile()" because the return value of "gov.nasa.arc.pds.xml.generated.InformationPackageComponent.getFileAreaChecksumManifest()" is null
        2 product validation(s) completed

  FAIL: file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_FAIL_field_groups_20250730.xml
      ERROR  [error.label.schematron]   line 29, 35: The number of Field_Character elements (2) does not match the number found in the 'fields' attribute (1).
      ERROR  [error.label.schematron]   line 29, 35: The number of Group_Field_Character elements (0) does not match the number found in the 'groups' attribute (1).
      ERROR  [error.label.schematron]   line 29, 35: The attribute pds:fields must be equal to '2'.
      ERROR  [error.label.schematron]   line 29, 35: The attribute pds:groups must be equal to '0'.
      ERROR  [error.label.schematron]   line 35, 38: The first field of a Transfer_Manifest must have field_location set to 1.
      ERROR  [error.table.bad_field_read]   line 0: Data Object #0 (): Cannot invoke "gov.nasa.arc.pds.xml.generated.FileAreaChecksumManifest.getFile()" because the return value of "gov.nasa.arc.pds.xml.generated.InformationPackageComponent.getFileAreaChecksumManifest()" is null
        3 product validation(s) completed

  FAIL: file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_FAIL_field_name_20250730.xml
      ERROR  [error.label.schematron]   line 33, 38: The first field of a Transfer_Manifest must have name set to 'LIDVID'.
      ERROR  [error.label.schematron]   line 41, 38: The second field of a Transfer_Manifest must have name set to 'File Specification'.
      ERROR  [error.table.bad_field_read]   line 0: Data Object #0 (): Cannot invoke "gov.nasa.arc.pds.xml.generated.FileAreaChecksumManifest.getFile()" because the return value of "gov.nasa.arc.pds.xml.generated.InformationPackageComponent.getFileAreaChecksumManifest()" is null
        4 product validation(s) completed

  FAIL: file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_FAIL_field_number_20250730.xml
      ERROR  [error.label.schematron]   line 34, 38: The first field of a Transfer_Manifest must have field_number set to 1.
      ERROR  [error.label.schematron]   line 42, 38: The second field of a Transfer_Manifest must have field_number set to 2.
      ERROR  [error.table.bad_field_read]   line 0: Data Object #0 (): Cannot invoke "gov.nasa.arc.pds.xml.generated.FileAreaChecksumManifest.getFile()" because the return value of "gov.nasa.arc.pds.xml.generated.InformationPackageComponent.getFileAreaChecksumManifest()" is null
        5 product validation(s) completed

  FAIL: file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-62_Transfer_Manifest_enforce_20250731/aaaBuild_testcases_20250910/tc_manifest_PASS_20250730.xml
      ERROR  [error.table.bad_field_read]   line 0: Data Object #0 (): Cannot invoke "gov.nasa.arc.pds.xml.generated.FileAreaChecksumManifest.getFile()" because the return value of "gov.nasa.arc.pds.xml.generated.InformationPackageComponent.getFileAreaChecksumManifest()" is null
        6 product validation(s) completed

Summary:

  6 product(s)
  18 error(s)
  0 warning(s)

  Product Validation Summary:
    0          product(s) passed
    6          product(s) failed
    0          product(s) skipped
    6          product(s) total

  Referential Integrity Check Summary:
    0          check(s) passed
    0          check(s) failed
    0          check(s) skipped
    0          check(s) total

  Message Types:
    12           error.label.schematron
    6            error.table.bad_field_read

End of Report
