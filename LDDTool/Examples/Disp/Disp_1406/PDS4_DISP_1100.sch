<?xml version="1.0" encoding="UTF-8"?>
<!-- PDS4 Schematron file for Display Dictionary -->
<!-- Generated from the PDS4_Display_1100_20140221201259_ext_ext.xml
  Ingest_LDD file (with supplemental Schematron rules) -->

<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://pds.nasa.gov/pds4/disp/v1" prefix="disp"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>

  <sch:pattern id="Attribute_Value_Matching">

    <sch:rule context="disp:Display_Settings/disp:Local_Internal_Reference">
      <sch:assert test="disp:local_identifier_reference = //pds:*/pds:*/(pds:Array|pds:Array_2D|pds:Array_2D_Image|pds:Array_2D_Map|pds:Array_2D_Spectrum|pds:Array_3D|pds:Array_3D_Image|pds:Array_3D_Movie|pds:Array_3D_Spectrum)/pds:local_identifier">
        Display Dictionary: In the disp:Local_Internal_Reference class, the value of the disp:local_identifier_reference must match the value of the pds:local_identifer of an Array class or sub-class within the File_Area.
      </sch:assert>
    </sch:rule>

    <sch:rule context="pds:Discipline_Area">
      <sch:assert test="if (disp:Display_Direction) then (disp:Display_Settings/disp:Display_Direction) else true()">
        Display Dictionary: If the Display_Direction class is in the label, it must be contained in a Display_Settings class.
      </sch:assert>
    </sch:rule>

    <sch:rule context="disp:Display_Settings/disp:Local_Internal_Reference">
      <sch:assert test="disp:local_reference_type = ('display_settings_to_array')">
        Display_Dictionary: In the disp:Local_Internal_Reference class, the value of the disp:local_reference_type must be 'display_settings_to_array'.
      </sch:assert>
    </sch:rule>

  </sch:pattern>

  <sch:pattern id="Enumerated_Value_Checks">

    <sch:rule context="disp:Display_Direction">
      <sch:assert test="disp:horizontal_display_direction = ('Left to Right', 'Right to Left')">
        Display Dictionary: In the disp:Display_Direction class, the disp:horizontal_display_direction must be equal to one of the following values: 'Left to Right', 'Right to Left'.
      </sch:assert>
      <sch:assert test="disp:vertical_display_direction = ('Top to Bottom', 'Bottom to Top')">
        Display Dictionary: In the disp:Display_Direction class, the disp:vertical_display_direction must be equal to one of the following values: 'Top to Bottom', 'Bottom to Top'.
      </sch:assert>
    </sch:rule>

    <sch:rule context="disp:Movie_Display_Settings">
      <sch:assert test="disp:loop_flag = ('true', 'false')">
        Display Dictionary: In the disp:Movie_Display_Settings class, the disp:loop_flag must be equal to one of the following values: 'true', 'false'.
      </sch:assert>
      <sch:assert test="disp:loop_back_and_forth_flag = ('true', 'false')">
        Display Dictionary: In the disp:Movie_Display_Settings class, the disp:loop_back_and_forth_flag must be equal to one of the following values: 'true', 'false'.
      </sch:assert>
    </sch:rule>
  </sch:pattern>

</sch:schema>
