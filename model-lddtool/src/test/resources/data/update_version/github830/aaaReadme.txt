
CCB#47: modifications to SCH:

Product Level Validation Results

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB-47_Units_of_Spectral_Radiance/aaaTests_final_for_github/tc_FAIL_units_of_spectral_radiance_invalid-unit_20241122.xml
      ERROR  [error.label.schema]   line 169, 47: cvc-enumeration-valid: Value 'mrad/pixels' is not facet-valid with respect to enumeration '[W*m**-2*sr**-1*Hz**-1, W*m**-2*sr**-1*nm**-1, W*m**-2*sr**-1*um**-1, W*m**-3*sr**-1, W/cm**2/sr/?m, W/m**2/sr/Hz, W/m**2/sr/nm, W/m**2/sr/?m, W/m**3/sr, uW*cm**-2*sr**-1*um**-1, ?W/cm**2/sr/?m]'. It must be a value from the enumeration.
      ERROR  [error.label.schema]   line 169, 47: cvc-attribute.3: The value 'mrad/pixels' of attribute 'unit' on element 'hst:plate_scale' is not valid with respect to its type, 'Units_of_Spectral_Radiance'.
      ERROR  [error.label.schematron]   line 169, 47: The attribute @unit must be equal to one of the following values 'W*m**-2*sr**-1*Hz**-1', 'W*m**-2*sr**-1*nm**-1', 'W*m**-2*sr**-1*um**-1', 'W*m**-3*sr**-1', 'W/cm**2/sr/?m', 'W/m**2/sr/Hz', 'W/m**2/sr/nm', 'W/m**2/sr/?m', 'W/m**3/sr', 'uW*cm**-2*sr**-1*um**-1', '?W/cm**2/sr/?m'.
        1 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB-47_Units_of_Spectral_Radiance/aaaTests_final_for_github/tc_FAIL_units_of_spectral_radiance_no-unit_20241122.xml
      ERROR  [error.label.schema]   line 169, 28: cvc-complex-type.4: Attribute 'unit' must appear on element 'hst:plate_scale'.
      ERROR  [error.label.schematron]   line 169, 28: The attribute @unit must be equal to one of the following values 'W*m**-2*sr**-1*Hz**-1', 'W*m**-2*sr**-1*nm**-1', 'W*m**-2*sr**-1*um**-1', 'W*m**-3*sr**-1', 'W/cm**2/sr/?m', 'W/m**2/sr/Hz', 'W/m**2/sr/nm', 'W/m**2/sr/?m', 'W/m**3/sr', 'uW*cm**-2*sr**-1*um**-1', '?W/cm**2/sr/?m'.
        2 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB-47_Units_of_Spectral_Radiance/aaaTests_final_for_github/tc_VALID_units_of_spectral_radiance_20241122.xml
        3 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1N_SCR_testing_20241016/CCB-47_Units_of_Spectral_Radiance/aaaTests_final_for_github/tc_VALID_units_of_spectral_radiance_old_value_20241122.xml
        4 product validation(s) completed

Summary:

  4 product(s)
  5 error(s)
  0 warning(s)

  Product Validation Summary:
    2          product(s) passed
    2          product(s) failed
    0          product(s) skipped
    4          product(s) total

  Referential Integrity Check Summary:
    0          check(s) passed
    0          check(s) failed
    0          check(s) skipped
    0          check(s) total

  Message Types:
    3            error.label.schema
    2            error.label.schematron

End of Report
    

