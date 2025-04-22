
CCB#13: Added permissible value "ESA" to Funding_Acknowledgement funding_source #911
   -- modifications to XSD:
   
    <xs:simpleType name="ASCII_Directory_Path_Name">
      <xs:restriction base="xs:string">
        <xs:minLength value="1"/>
        <xs:maxLength value="255"/>
        <xs:pattern value='[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/'/>
      </xs:restriction>
    </xs:simpleType>

    <xs:simpleType name="ASCII_File_Name">
      <xs:restriction base="xs:string">
        <xs:minLength value="1"/>
        <xs:maxLength value="255"/>
        <xs:pattern value='[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+'/>
      </xs:restriction>
    </xs:simpleType>
   

cd D:\WINWORD\Data_Prep_HandBook\aaaVer_9_20130225\Build_SCR_xlsx\Build_1O_SCR_testing_20250331\CCB-13_directory_path_name\aaaTest_JMafi_updates_20241205\
validate_360 -t *.xml -r log_validate.txt --schema PDS4_PDS_1O00.xsd --schematron PDS4_PDS_1O00.sch --skip-content-validation

| NASA-PDS/pds4-information-model#912  | "CCB-13 Ensure file_name and directory_path_name adhere to SR (gitHub-CCB-13)"                       | "github912898"  |           5 |  "totalErrors"     |  "-t {resourceDir}/github891/tc_VALID_units_of_gmass.xml {resourceDir}/github891/tc_FAIL_units_of_gmass_no-unit.xml {resourceDir}/github891/tc_FAIL_units_of_gmass_invalid-unit.xml"  | "CCB-43_HST_IngestLDD_gmass.xml" | "" |



Product Level Validation Results

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-13_directory_path_name/aaaTest_JMafi_updates_20241205/Product_Collection_FAIL_test_filename_20241114.xml
      ERROR  [error.label.schema]   line 142, 18: cvc-pattern-valid: Value 'collectiondata-venucat' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 142, 18: cvc-type.3.1.3: The value 'collectiondata-venucat' of element 'file_name' is not valid.
      ERROR  [error.validation.internal_error]   Uncaught exception while validating: D:\WINWORD\Data_Prep_HandBook\aaaVer_9_20130225\Build_SCR_xlsx\Build_1O_SCR_testing_20250331\CCB-13_directory_path_name\aaaTest_JMafi_updates_20241205\collectiondata-venucat (The system cannot find the file specified)
        1 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-13_directory_path_name/aaaTest_JMafi_updates_20241205/Product_Collection_VALID_test_filename_20241114.xml
      ERROR  [error.validation.internal_error]   Uncaught exception while validating: D:\WINWORD\Data_Prep_HandBook\aaaVer_9_20130225\Build_SCR_xlsx\Build_1O_SCR_testing_20250331\CCB-13_directory_path_name\aaaTest_JMafi_updates_20241205\Collection-data-venus_1.0.csv (The system cannot find the file specified)
        2 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-13_directory_path_name/aaaTest_JMafi_updates_20241205/Product_Document_FAIL_filename_20241114.xml
      ERROR  [error.label.schema]   line 80, 24: cvc-pattern-valid: Value 'meca$_rdr_sis.pdf' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 80, 24: cvc-type.3.1.3: The value 'meca$_rdr_sis.pdf' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 106, 34: cvc-pattern-valid: Value 'meca.rdr_sis/' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 106, 34: cvc-type.3.1.3: The value 'meca.rdr_sis/' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 119, 24: cvc-pattern-valid: Value '!meca_rdr_sis.htm' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 119, 24: cvc-type.3.1.3: The value '!meca_rdr_sis.htm' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 128, 24: cvc-pattern-valid: Value 'image001.gif@' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 128, 24: cvc-type.3.1.3: The value 'image001.gif@' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 138, 24: cvc-pattern-valid: Value 'image002%.gif' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 138, 24: cvc-type.3.1.3: The value 'image002%.gif' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 148, 24: cvc-pattern-valid: Value 'image#003.jpg' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 148, 24: cvc-type.3.1.3: The value 'image#003.jpg' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 159, 24: cvc-pattern-valid: Value 'image004*.gif' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 159, 24: cvc-type.3.1.3: The value 'image004*.gif' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 170, 24: cvc-pattern-valid: Value 'image005.*gif' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 170, 24: cvc-type.3.1.3: The value 'image005.*gif' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 181, 24: cvc-pattern-valid: Value 'image006gif' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 181, 24: cvc-type.3.1.3: The value 'image006gif' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 191, 24: cvc-pattern-valid: Value '-image007.gif' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 191, 24: cvc-type.3.1.3: The value '-image007.gif' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 201, 24: cvc-pattern-valid: Value 'image008.gif-' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 201, 24: cvc-type.3.1.3: The value 'image008.gif-' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 211, 24: cvc-pattern-valid: Value '-image009.gif-' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 211, 24: cvc-type.3.1.3: The value '-image009.gif-' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 221, 24: cvc-pattern-valid: Value '_image010.gif' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 221, 24: cvc-type.3.1.3: The value '_image010.gif' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 232, 24: cvc-pattern-valid: Value 'image011.gif_' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 232, 24: cvc-type.3.1.3: The value 'image011.gif_' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 242, 24: cvc-pattern-valid: Value '_image012.gif_' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 242, 24: cvc-type.3.1.3: The value '_image012.gif_' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 252, 24: cvc-pattern-valid: Value '.image013.gif' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 252, 24: cvc-type.3.1.3: The value '.image013.gif' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 262, 24: cvc-pattern-valid: Value 'image014.gif.' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 262, 24: cvc-type.3.1.3: The value 'image014.gif.' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 272, 24: cvc-pattern-valid: Value '.image015gif.' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 272, 24: cvc-type.3.1.3: The value '.image015gif.' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 282, 24: cvc-pattern-valid: Value '/parent/child/image016.jpg' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 282, 24: cvc-type.3.1.3: The value '/parent/child/image016.jpg' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 292, 24: cvc-pattern-valid: Value 'parent/child/image017.gif' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 292, 24: cvc-type.3.1.3: The value 'parent/child/image017.gif' of element 'file_name' is not valid.
      WARNING  [warning.label.schematron]   line 50, 14: The number of instances of 'file_name' is: '22'. The number of distinct instances of 'file_name' is: '21'. The number of duplicate instances of 'file_name' is: '1'.
      FATAL_ERROR  [error.validation.internal_error]   Error occurred while reading the uri: Illegal char <*> at index 33: test/meca_rdr_sis_files/;image004*.gif
        3 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-13_directory_path_name/aaaTest_JMafi_updates_20241205/Product_Document_FAIL_pathname_20241114.xml
      ERROR  [error.label.schema]   line 94, 34: cvc-pattern-valid: Value '#meca.rdr_sis/' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 94, 34: cvc-type.3.1.3: The value '#meca.rdr_sis/' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 107, 24: cvc-pattern-valid: Value 'meca_rdr_sis' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 107, 24: cvc-type.3.1.3: The value 'meca_rdr_sis' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 113, 34: cvc-pattern-valid: Value '!meca.rdr_sis/' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 113, 34: cvc-type.3.1.3: The value '!meca.rdr_sis/' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 123, 34: cvc-pattern-valid: Value 'meca_rdr%_sis_files/' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 123, 34: cvc-type.3.1.3: The value 'meca_rdr%_sis_files/' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 134, 34: cvc-pattern-valid: Value 'meca._rdr_sis_files/' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 134, 34: cvc-type.3.1.3: The value 'meca._rdr_sis_files/' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 140, 31: cvc-id.2: There are multiple occurrences of ID value 'document.image002.gif'.
      ERROR  [error.label.schema]   line 140, 31: cvc-type.3.1.3: The value 'document.image002.gif' of element 'local_identifier' is not valid.
      ERROR  [error.label.schema]   line 145, 34: cvc-pattern-valid: Value 'meca%_rdr_sis_files/' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 145, 34: cvc-type.3.1.3: The value 'meca%_rdr_sis_files/' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 155, 34: cvc-pattern-valid: Value 'meca_rdr#_sis_files/' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 155, 34: cvc-type.3.1.3: The value 'meca_rdr#_sis_files/' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 166, 35: cvc-pattern-valid: Value '/test*/meca_rdr_sis_files/' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 166, 35: cvc-type.3.1.3: The value '/test*/meca_rdr_sis_files/' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 171, 24: cvc-pattern-valid: Value 'image005.*gif' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[.])*[.][a-zA-Z0-9]+' for type 'file_name'.
      ERROR  [error.label.schema]   line 171, 24: cvc-type.3.1.3: The value 'image005.*gif' of element 'file_name' is not valid.
      ERROR  [error.label.schema]   line 177, 34: cvc-pattern-valid: Value 'test/meca_rdr_sis_files./' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 177, 34: cvc-type.3.1.3: The value 'test/meca_rdr_sis_files./' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 187, 34: cvc-pattern-valid: Value './meca_rdr_sis_files/' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 187, 34: cvc-type.3.1.3: The value './meca_rdr_sis_files/' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 197, 34: cvc-pattern-valid: Value '../test/meca_rdr_sis_files/' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 197, 34: cvc-type.3.1.3: The value '../test/meca_rdr_sis_files/' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 218, 34: cvc-pattern-valid: Value '/meca_rdr_sis_files' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 218, 34: cvc-type.3.1.3: The value '/meca_rdr_sis_files' of element 'directory_path_name' is not valid.
      ERROR  [error.label.schema]   line 228, 34: cvc-pattern-valid: Value 'meca_rdr_sis_files' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 228, 34: cvc-type.3.1.3: The value 'meca_rdr_sis_files' of element 'directory_path_name' is not valid.
      WARNING  [warning.label.schematron]   line 50, 14: The number of instances of 'file_name' is: '22'. The number of distinct instances of 'file_name' is: '21'. The number of duplicate instances of 'file_name' is: '1'.
      FATAL_ERROR  [error.validation.internal_error]   Error occurred while reading the uri: Illegal char <*> at index 5: /test*/meca_rdr_sis_files/
        4 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-13_directory_path_name/aaaTest_JMafi_updates_20241205/Product_Document_VALID_pathname_20241114.xml
        5 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-13_directory_path_name/aaaTest_JMafi_updates_20241205/Product_Document_WARNING_duplicate_filename_20241114.xml
      WARNING  [warning.label.schematron]   line 50, 14: The number of instances of 'file_name' is: '5'. The number of distinct instances of 'file_name' is: '3'. The number of duplicate instances of 'file_name' is: '2'.
        6 product validation(s) completed

Summary:

  6 product(s)
  76 error(s)
  3 warning(s)

  Product Validation Summary:
    2          product(s) passed
    4          product(s) failed
    0          product(s) skipped
    6          product(s) total

  Referential Integrity Check Summary:
    0          check(s) passed
    0          check(s) failed
    0          check(s) skipped
    0          check(s) total

  Message Types:
    72           error.label.schema
    4            error.validation.internal_error
    3            warning.label.schematron

End of Report