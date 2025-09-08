
IM#944: funding_year only allows the value "9999"
          -- modifications to XSD:

--- only validate local directory; no children directories
cd D:\WINWORD\Standards-DataDictionary\PDS4_SCRs\CCB-59_funding_year_20250320\aaaBuild_testcases_20250829
validate_363 -t *.xml -r log_validate.txt --schema PDS4_PDS_1P00.xsd --schematron PDS4_PDS_1P00.sch --skip-content-validation --local

Change this:

  <xs:simpleType name="funding_year">
    <xs:annotation>
      <xs:documentation>The funding_year attribute provides the year
        that funding was awarded.</xs:documentation>
    </xs:annotation>
    <xs:restriction base="pds:ASCII_Short_String_Collapsed">
    	<xs:pattern value='9999'/>
     <xs:minLength value="1"/>
     <xs:maxLength value="4"/>
	   </xs:restriction>
  </xs:simpleType>
  
To this:

  <xs:simpleType name="funding_year">
    <xs:annotation>
      <xs:documentation>The funding_year attribute provides the year
        that funding was awarded.</xs:documentation>
    </xs:annotation>
    <xs:restriction base="pds:ASCII_Short_String_Collapsed">
    	<xs:pattern value='[0-9]{4}'/>
     <xs:minLength value="1"/>
     <xs:maxLength value="255"/>
	   </xs:restriction>
  </xs:simpleType>
    

PDS Validate Tool Report

Configuration:
   Version     3.6.3
   Date        2025-09-02T14:09:30Z

Parameters:
   Targets                        [file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-59_funding_year_20250320/aaaBuild_testcases_20250829/tc_FAIL_funding_year_20250829.xml, file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-59_funding_year_20250320/aaaBuild_testcases_20250829/tc_FAIL_funding_year_alpha_20250829.xml, file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-59_funding_year_20250320/aaaBuild_testcases_20250829/tc_VALID_funding_year_20250829.xml]
   User Specified Schemas         [file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-59_funding_year_20250320/aaaBuild_testcases_20250829/PDS4_PDS_1P00.xsd]
   User Specified Schematrons     [file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-59_funding_year_20250320/aaaBuild_testcases_20250829/PDS4_PDS_1P00.sch]
   Severity Level                 WARNING
   Recurse Directories            false
   File Filters Used              [*.xml, *.XML]
   Data Content Validation        off
   Product Level Validation       on
   Max Errors                     100000
   Registered Contexts File       E:\PDS_Validate_tool\validate_v3_6_3_20241122\validate-3.6.3\resources\registered_context_products.json



Product Level Validation Results

  FAIL: file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-59_funding_year_20250320/aaaBuild_testcases_20250829/tc_FAIL_funding_year_20250829.xml
      ERROR  [error.label.schema]   line 27, 31: cvc-pattern-valid: Value '10193' is not facet-valid with respect to pattern '[0-9]{4}' for type 'funding_year'.
      ERROR  [error.label.schema]   line 27, 31: cvc-type.3.1.3: The value '10193' of element 'funding_year' is not valid.
        1 product validation(s) completed

  FAIL: file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-59_funding_year_20250320/aaaBuild_testcases_20250829/tc_FAIL_funding_year_alpha_20250829.xml
      ERROR  [error.label.schema]   line 27, 31: cvc-pattern-valid: Value '01bc' is not facet-valid with respect to pattern '[0-9]{4}' for type 'funding_year'.
      ERROR  [error.label.schema]   line 27, 31: cvc-type.3.1.3: The value '01bc' of element 'funding_year' is not valid.
        2 product validation(s) completed

  PASS: file:/D:/WINWORD/Standards-DataDictionary/PDS4_SCRs/CCB-59_funding_year_20250320/aaaBuild_testcases_20250829/tc_VALID_funding_year_20250829.xml
        3 product validation(s) completed

Summary:

  3 product(s)
  4 error(s)
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
    4            error.label.schema

End of Report
