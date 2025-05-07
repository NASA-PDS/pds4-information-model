
CCB#13: Ensure file_name and directory_path_name adhere to SR; #913
   -- Validate works on Windows; but fails on github / linux ?   
   -- see #913a and #913b
   
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
   

cd D:\WINWORD\Data_Prep_HandBook\aaaVer_9_20130225\Build_SCR_xlsx\Build_1O_SCR_testing_20250331\CCB-13_directory_path_name\aaaTest_JMafi_updates_20241205\aaaSave\
validate_360 -t *.xml -r log_validate.txt --schema PDS4_PDS_1O00.xsd --schematron PDS4_PDS_1O00.sch --skip-content-validation


Product Level Validation Results

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-13_directory_path_name/aaaTest_JMafi_updates_20241205/aaaSave/Product_Document_FAIL_pathname_20250422.xml
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
      ERROR  [error.label.schema]   line 166, 35: cvc-pattern-valid: Value '/test/meca_rdr_sis_files/' is not facet-valid with respect to pattern '[a-zA-Z0-9]([a-zA-Z0-9]|[-]|[_]|[/])*/' for type 'directory_path_name'.
      ERROR  [error.label.schema]   line 166, 35: cvc-type.3.1.3: The value '/test/meca_rdr_sis_files/' of element 'directory_path_name' is not valid.
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
        1 product validation(s) completed

Summary:

  1 product(s)
  28 error(s)
  1 warning(s)

  Product Validation Summary:
    0          product(s) passed
    1          product(s) failed
    0          product(s) skipped
    1          product(s) total

  Referential Integrity Check Summary:
    0          check(s) passed
    0          check(s) failed
    0          check(s) skipped
    0          check(s) total

  Message Types:
    28           error.label.schema
    1            warning.label.schematron

End of Report
