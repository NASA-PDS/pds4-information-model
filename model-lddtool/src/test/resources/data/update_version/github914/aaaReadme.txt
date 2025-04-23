
IM#898: Schematron is not throwning a warning when validating Author/Editor Lists
          -- modifications to SCH:

cd D:\WINWORD\Data_Prep_HandBook\aaaVer_9_20130225\Build_SCR_xlsx\Build_1O_SCR_testing_20250331\CCB_898_Warning_Constraints_on_AuthorEditor_Lists
validate_360 -t *.xml -r log_validate.txt --schema PDS4_PDS_1O00.xsd --schematron PDS4_PDS_1O00.sch --skip-content-validation

Change this:

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

To this – add role=WARN:

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

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB_898_Warning_Constraints_on_AuthorEditor_Lists/tc_Valid_Contributor_20240122.xml
        1 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB_898_Warning_Constraints_on_AuthorEditor_Lists/tc_Valid_Editor_20240122.xml
        2 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB_898_Warning_Constraints_on_AuthorEditor_Lists/tc_Valid_Warning_Author_20240122.xml
      WARNING  [warning.label.schematron]   line 16, 31: The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
      WARNING  [warning.label.schematron]   line 17, 26: pds:Citation_Information/pds:author_list is deprecated and should not be used.
        3 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB_898_Warning_Constraints_on_AuthorEditor_Lists/tc_Valid_Warning_both_20240122.xml
      WARNING  [warning.label.schematron]   line 16, 31: The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
      WARNING  [warning.label.schematron]   line 17, 26: pds:Citation_Information/pds:author_list is deprecated and should not be used.
      WARNING  [warning.label.schematron]   line 18, 26: pds:Citation_Information/pds:editor_list is deprecated and should not be used.
        4 product validation(s) completed

Summary:

  4 product(s)
  0 error(s)
  5 warning(s)

  Product Validation Summary:
    4          product(s) passed
    0          product(s) failed
    0          product(s) skipped
    4          product(s) total

  Referential Integrity Check Summary:
    0          check(s) passed
    0          check(s) failed
    0          check(s) skipped
    0          check(s) total

  Message Types:
    5            warning.label.schematron

End of Report
