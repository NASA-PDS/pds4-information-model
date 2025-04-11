
CCB#356: modifications to SCH:

Current:


  <sch:pattern>
    <sch:rule context="pds:Encoded_Image/pds:encoding_standard_id">
      <sch:assert test=". = ('GIF', 'J2C', 'JPEG', 'PDF', 'PDF/A', 'PNG', 'TIFF')">
        <title>pds:Encoded_Image/pds:encoding_standard_id/pds:encoding_standard_id</title>
        The attribute pds:Encoded_Image/pds:encoding_standard_id must be equal to one of the following values 'GIF', 'J2C', 'JP2', 'JPEG', 'PDF', 'PDF/A', 'PNG', 'TIFF'.</sch:assert>
    </sch:rule>
  </sch:pattern>

Change to this – add 'JP2'

  <sch:pattern>
    <sch:rule context="pds:Encoded_Image/pds:encoding_standard_id">
      <sch:assert test=". = ('GIF', 'J2C', 'JP2', 'JPEG', 'PDF', 'PDF/A', 'PNG', 'TIFF')">
        <title>pds:Encoded_Image/pds:encoding_standard_id/pds:encoding_standard_id</title>
        The attribute pds:Encoded_Image/pds:encoding_standard_id must be equal to one of the following values 'GIF', 'J2C', 'JP2', 'JPEG', 'PDF', 'PDF/A', 'PNG', 'TIFF'.</sch:assert>
    </sch:rule>
  </sch:pattern>


    

Product Level Validation Results

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-48_add_JP2_to_encoding_standard_id/test_case_Fail_invalid-value_20241024.xml
      WARNING  [warning.label.schema]   The schema version(s) [/PDS4/PDS/V1/PDS4_PDS_1O00] does/do not match the schematron version(s) [].
      ERROR  [error.label.schematron]   line 162, 29: The attribute pds:Encoded_Image/pds:encoding_standard_id must be equal to one of the following values 'GIF', 'J2C', 'JP2', 'JPEG', 'PDF', 'PDF/A', 'PNG', 'TIFF'.
      ERROR  [error.validation.internal_error]   Could not process the encoding type: encoding parameter 'JP3' is not known to this version of validate.
        1 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-48_add_JP2_to_encoding_standard_id/test_case_Fail_null-value_20241024.xml
      WARNING  [warning.label.schema]   The schema version(s) [/PDS4/PDS/V1/PDS4_PDS_1O00] does/do not match the schematron version(s) [].
      ERROR  [error.label.schema]   line 162, 29: cvc-minLength-valid: Value '' with length = '0' is not facet-valid with respect to minLength '1' for type 'encoding_standard_id'.
      ERROR  [error.label.schema]   line 162, 29: cvc-type.3.1.3: The value '' of element 'encoding_standard_id' is not valid.
      ERROR  [error.label.schematron]   line 162, 29: The attribute pds:Encoded_Image/pds:encoding_standard_id must be equal to one of the following values 'GIF', 'J2C', 'JP2', 'JPEG', 'PDF', 'PDF/A', 'PNG', 'TIFF'.
        2 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-48_add_JP2_to_encoding_standard_id/test_case_Valid_20241024.xml
      WARNING  [warning.label.schema]   The schema version(s) [/PDS4/PDS/V1/PDS4_PDS_1O00] does/do not match the schematron version(s) [].
      ERROR  [error.validation.internal_error]   Could not process the encoding type: encoding parameter 'JP2' is not known to this version of validate.
        3 product validation(s) completed

Summary:

  3 product(s)
  6 error(s)
  3 warning(s)

  Product Validation Summary:
    0          product(s) passed
    3          product(s) failed
    0          product(s) skipped
    3          product(s) total

  Referential Integrity Check Summary:
    0          check(s) passed
    0          check(s) failed
    0          check(s) skipped
    0          check(s) total

  Message Types:
    2            error.label.schema
    2            error.label.schematron
    2            error.validation.internal_error
    3            warning.label.schema

End of Report   
    