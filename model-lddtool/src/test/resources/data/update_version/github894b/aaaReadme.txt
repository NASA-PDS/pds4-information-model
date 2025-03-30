
CCB#7b: modifications to SCH:

#Note the files for "SEED" and "ASCII_FILE_NAME" have been moved here until error in Validate is fixed; see readme

    
Product Level Validation Results

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB-7_Product_Bundle_reference_type/CCB-7a_for_seed/test_case_external_ref_FAIL_20240318.xml
      WARNING  [warning.label.schematron]   line 17, 26: pds:Citation_Information/pds:author_list is deprecated and should not be used.
      WARNING  [warning.label.schematron]   line 18, 26: pds:Citation_Information/pds:editor_list is deprecated and should not be used.
      ERROR  [error.label.schematron]   line 79, 33: The attribute reference_type must be set to one of the following values 'external_to_target'.
      ERROR  [error.validation.internal_error]   Could not process the encoding type: encoding parameter 'ASCII_File_Name' is not known to this version of validate.
        1 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB-7_Product_Bundle_reference_type/CCB-7a_for_seed/test_case_external_ref_VALID_20240318.xml
      WARNING  [warning.label.schematron]   line 17, 26: pds:Citation_Information/pds:author_list is deprecated and should not be used.
      WARNING  [warning.label.schematron]   line 18, 26: pds:Citation_Information/pds:editor_list is deprecated and should not be used.
      ERROR  [error.validation.internal_error]   Could not process the encoding type: encoding parameter 'ASCII_File_Name' is not known to this version of validate.
        2 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB-7_Product_Bundle_reference_type/CCB-7a_for_seed/test_case_native_ref_FAIL_202403318.xml
      WARNING  [warning.label.schematron]   line 17, 26: pds:Citation_Information/pds:author_list is deprecated and should not be used.
      WARNING  [warning.label.schematron]   line 18, 26: pds:Citation_Information/pds:editor_list is deprecated and should not be used.
      ERROR  [error.label.schematron]   line 71, 33: The attribute reference_type must be set to one of the following values 'native_to_target'.
      ERROR  [error.validation.internal_error]   Could not process the encoding type: encoding parameter 'SEED 2.4' is not known to this version of validate.
        3 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB-7_Product_Bundle_reference_type/CCB-7a_for_seed/test_case_native_ref_VALID_202403318.xml
      WARNING  [warning.label.schematron]   line 17, 26: pds:Citation_Information/pds:author_list is deprecated and should not be used.
      WARNING  [warning.label.schematron]   line 18, 26: pds:Citation_Information/pds:editor_list is deprecated and should not be used.
      ERROR  [error.validation.internal_error]   Could not process the encoding type: encoding parameter 'SEED 2.4' is not known to this version of validate.
        4 product validation(s) completed

Summary:

  4 product(s)
  6 error(s)
  8 warning(s)

  Product Validation Summary:
    0          product(s) passed
    4          product(s) failed
    0          product(s) skipped
    4          product(s) total

  Referential Integrity Check Summary:
    0          check(s) passed
    0          check(s) failed
    0          check(s) skipped
    0          check(s) total

  Message Types:
    2            error.label.schematron
    4            error.validation.internal_error
    8            warning.label.schematron

End of Report
