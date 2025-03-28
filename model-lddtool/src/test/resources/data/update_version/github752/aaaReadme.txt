
CCB#356: modifications to SCH:

Current:

  <sch:pattern>
    <sch:rule context="pds:Citation_Information">
      <sch:assert test="if (pds:doi) then (pds:author_list or pds:editor_list or pds:List_Author or pds:List_Editor) else true()">
        <title>pds:Citation_Information/author_list</title>
        The presence of "pds:doi" requires at least one of either "List_Author" or "List_Editor" classes.</sch:assert>
      <sch:assert test="if ((pds:author_list or pds:editor_list) and (pds:List_Author or pds:List_Editor)) then false() else true()">
        <title>pds:Citation_Information/author_list</title>
        The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.</sch:assert>
    </sch:rule>
  </sch:pattern>

Change to this – add role=WARN:
   -- Note that the 2nd <assert> has role=WARN”.

  <sch:pattern>
    <sch:rule context="pds:Citation_Information">
      <sch:assert test="if (pds:doi) then (pds:author_list or pds:editor_list or pds:List_Author or pds:List_Editor) else true()">
        <title>pds:Citation_Information/author_list</title>
        The presence of "pds:doi" requires at least one of either "List_Author" or "List_Editor" classes.</sch:assert>
      <sch:assert test="if ((pds:author_list or pds:editor_list) and (pds:List_Author or pds:List_Editor)) then false() else true()" role="WARN">
        <title>pds:Citation_Information/author_list</title>
        The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.</sch:assert>
    </sch:rule>
  </sch:pattern>


    
Product Level Validation Results

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1M_SCR_testing_20240901/CCB_15_Constraints_on_AuthorEditor_Lists/geo_bundle_hausrath_m2020_pixl_naltsos.xml
      ERROR  [error.label.schematron]   line 13, 27: The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
      WARNING  [warning.label.schematron]   line 14, 20: pds:Citation_Information/pds:author_list is deprecated and should not be used.
      WARNING  [warning.label.schematron]   line 15, 20: pds:Citation_Information/pds:editor_list is deprecated and should not be used.
      WARNING  [warning.label.context_ref_mismatch]   line 76: Context reference name mismatch. Value: 'Perseverance' Expected one of: '[Mars 2020]'
      WARNING  [warning.label.context_ref_mismatch]   line 85: Context reference name mismatch. Value: 'Mars 2020' Expected one of: '[Perseverance]'
        1 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1M_SCR_testing_20240901/CCB_15_Constraints_on_AuthorEditor_Lists/geo_bundle_mex_marsis_optim.xml
      ERROR  [error.label.schematron]   line 13, 27: The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
      WARNING  [warning.label.schematron]   line 14, 19: pds:Citation_Information/pds:author_list is deprecated and should not be used.
        2 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1M_SCR_testing_20240901/CCB_15_Constraints_on_AuthorEditor_Lists/geo_pixl_edr_sis.xml
      WARNING  [warning.label.schematron]   line 93, 22: pds:Document/pds:author_list is deprecated and should not be used.
        3 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1M_SCR_testing_20240901/CCB_15_Constraints_on_AuthorEditor_Lists/img_bundle_gbo.sdss-moc.phot.xml
      ERROR  [error.label.schematron]   line 13, 27: The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
      WARNING  [warning.label.schematron]   line 14, 20: pds:Citation_Information/pds:author_list is deprecated and should not be used.
        4 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1M_SCR_testing_20240901/CCB_15_Constraints_on_AuthorEditor_Lists/ppi_bundle-voyager2.pws.wf.xml
      ERROR  [error.label.schematron]   line 15, 27: The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
      WARNING  [warning.label.schematron]   line 16, 20: pds:Citation_Information/pds:author_list is deprecated and should not be used.
        5 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1M_SCR_testing_20240901/CCB_15_Constraints_on_AuthorEditor_Lists/ppi_TECH_DOC.xml
        6 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1M_SCR_testing_20240901/CCB_15_Constraints_on_AuthorEditor_Lists/test_case_collection_20240122.xml
      WARNING  [warning.label.schema]   The schema version(s) [] does/do not match the schematron version(s) [/PDS4/MSN/V1/PDS4_MSN_1K00_1300].
      ERROR  [error.label.schematron]   line 18, 29: The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
      WARNING  [warning.label.schematron]   line 19, 23: pds:Citation_Information/pds:author_list is deprecated and should not be used.
        7 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1M_SCR_testing_20240901/CCB_15_Constraints_on_AuthorEditor_Lists/test_file_1_only_author_20240122.xml
      WARNING  [warning.label.schematron]   line 16, 20: pds:Citation_Information/pds:author_list is deprecated and should not be used.
        8 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1M_SCR_testing_20240901/CCB_15_Constraints_on_AuthorEditor_Lists/test_file_2_list_author_20240122.xml
        9 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1M_SCR_testing_20240901/CCB_15_Constraints_on_AuthorEditor_Lists/test_file_5_20240122.xml
        10 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1M_SCR_testing_20240901/CCB_15_Constraints_on_AuthorEditor_Lists/test_file_ror_20240122.xml
      ERROR  [error.label.schematron]   line 16, 31: The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
      WARNING  [warning.label.schematron]   line 17, 26: pds:Citation_Information/pds:author_list is deprecated and should not be used.
      WARNING  [warning.label.schematron]   line 18, 26: pds:Citation_Information/pds:editor_list is deprecated and should not be used.
        11 product validation(s) completed

Summary:

  11 product(s)
  6 error(s)
  13 warning(s)
    
In the next release of the IM, the 9 errors associated with the test will be removed:
   -- The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
   
    