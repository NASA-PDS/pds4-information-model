
IM#898: Schematron is not throwning a warning when validating Author/Editor Lists
          -- modifications to SCH:

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

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB_898_Warning_Constraints_on_AuthorEditor_Lists/test_file_ror_20240122.xml
      ERROR  [error.label.schematron]   line 16, 31: The pds:List_Author and pds:List_Editor classes should not be used in conjunction with the deprecated pds:author_list and pds:editor_list attributes.
      WARNING  [warning.label.schematron]   line 17, 26: pds:Citation_Information/pds:author_list is deprecated and should not be used.
      WARNING  [warning.label.schematron]   line 18, 26: pds:Citation_Information/pds:editor_list is deprecated and should not be used.
        1 product validation(s) completed

Summary:

  1 product(s)
  1 error(s)
  2 warning(s)

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
    1            error.label.schematron
    2            warning.label.schematron

End of Report
