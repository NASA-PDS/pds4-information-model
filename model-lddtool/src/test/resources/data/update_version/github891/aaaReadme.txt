
CCB#43: modifications to XSD:

| NASA-PDS/pds4-information-model#891  | "Incorrect value for Units_of_Gmass (gitHub-CCB-43)"                       | "github891"  |           5 |  "totalErrors"     |  "-t {resourceDir}/github891/tc_VALID_units_of_gmass.xml {resourceDir}/github891/tc_FAIL_units_of_gmass_no-unit.xml {resourceDir}/github891/tc_FAIL_units_of_gmass_invalid-unit.xml"  | "CCB-43_HST_IngestLDD_gmass.xml" | "" |


    <!-- CCB-43: modified value="km**3s**-2" to value="km**3/s**-2" -->
    <xs:simpleType name="Units_of_Gmass">
      <xs:restriction base="xs:string">
        <!--
        <xs:enumeration value="km**3s**-2"></xs:enumeration>
        -->
        <xs:enumeration value="km**3/s**2"></xs:enumeration>
      </xs:restriction>
    </xs:simpleType>
    
Product Level Validation Results

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-43_Units_of_Gmass/tc_FAIL_units_of_gmass_invalid-unit.xml
      ERROR  [error.label.schema]   line 157, 46: cvc-enumeration-valid: Value 'km**3s**-2' is not facet-valid with respect to enumeration '[km**3/s**2]'. It must be a value from the enumeration.
      ERROR  [error.label.schema]   line 157, 46: cvc-attribute.3: The value 'km**3s**-2' of attribute 'unit' on element 'hst:plate_scale' is not valid with respect to its type, 'Units_of_Gmass'.
      ERROR  [error.label.schematron]   line 157, 46: The attribute @unit must be equal to one of the following values 'km**3/s**2'.
        1 product validation(s) completed

  FAIL: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-43_Units_of_Gmass/tc_FAIL_units_of_gmass_no-unit.xml
      ERROR  [error.label.schema]   line 156, 28: cvc-complex-type.4: Attribute 'unit' must appear on element 'hst:plate_scale'.
      ERROR  [error.label.schematron]   line 156, 28: The attribute @unit must be equal to one of the following values 'km**3/s**2'.
        2 product validation(s) completed

  PASS: file:/D:/WINWORD/Data_Prep_HandBook/aaaVer_9_20130225/Build_SCR_xlsx/Build_1O_SCR_testing_20250331/CCB-43_Units_of_Gmass/tc_VALID_units_of_gmass.xml
        3 product validation(s) completed

Summary:

  3 product(s)
  5 error(s)
  0 warning(s)

  Product Validation Summary:
    1          product(s) passed
    2          product(s) failed
    0          product(s) skipped
    3          product(s) total

  Referential Integrity Check Summary:
    0          check(s) passed
    0          check(s) failed
    0          check(s) skipped
    0          check(s) total

  Message Types:
    3            error.label.schema
    2            error.label.schematron

End of Report