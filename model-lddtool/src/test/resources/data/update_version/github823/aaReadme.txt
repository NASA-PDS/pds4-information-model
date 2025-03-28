CCB#27: modifications to SCH:

Product Level Validation Results

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB_27_DOI_and_Editor_Author_list/aaaTest_cases_final_20240912/test_label-CCB-27.xml
        1 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB_27_DOI_and_Editor_Author_list/aaaTest_cases_final_20240912/test_label_bundle_FAIL_list_author.xml
      ERROR  [error.label.schematron]   line 134, 27: The attribute reference_type must be set to one of the following values 'bundle_to_target'.
        2 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB_27_DOI_and_Editor_Author_list/aaaTest_cases_final_20240912/test_label_bundle_FAIL_list_author_mismatch.xml
      ERROR  [error.label.schematron]   line 21, 27: The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
      WARNING  [warning.label.schematron]   line 23, 20: pds:Citation_Information/pds:author_list is deprecated and should not be used.
      ERROR  [error.label.schematron]   line 140, 27: The attribute reference_type must be set to one of the following values 'bundle_to_target'.
        3 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB_27_DOI_and_Editor_Author_list/aaaTest_cases_final_20240912/test_label_bundle_FAIL_list_editor.xml
      ERROR  [error.label.schematron]   line 21, 27: The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
      WARNING  [warning.label.schematron]   line 26, 20: pds:Citation_Information/pds:editor_list is deprecated and should not be used.
      ERROR  [error.label.schematron]   line 141, 27: The attribute reference_type must be set to one of the following values 'bundle_to_target'.
        4 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB_27_DOI_and_Editor_Author_list/aaaTest_cases_final_20240912/test_label_bundle_FAIL_no_authors.xml
      ERROR  [error.label.schematron]   line 21, 27: The presence of "pds:doi" requires at least one of either "List_Author" or "List_Editor" classes.
      ERROR  [error.label.schematron]   line 142, 27: The attribute reference_type must be set to one of the following values 'bundle_to_target'.
        5 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB_27_DOI_and_Editor_Author_list/aaaTest_cases_final_20240912/test_label_collection_VALID_no_list_author.xml
        6 product validation(s) completed

Summary:

  6 product(s)
  7 error(s)
  2 warning(s)

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
    7            error.label.schematron
    2            warning.label.schematron
    
    