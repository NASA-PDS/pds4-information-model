<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:disp  Version:1.1.0.0 - Fri Jun 06 18:34:31 PDT 2014 -->
  <!-- Generated from the PDS4 Information Model Version 1.3.0.0 - System Build 5a -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://pds.nasa.gov/pds4/disp/v1" prefix="disp"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="pds:Discipline_Area">
      <sch:assert test="if (disp:Display_Direction) then (disp:Display_Settings/disp:Display_Direction) else true()">
        Display Dictionary: If the Display_Direction class is in the label, it must be contained in a Display_Settings class.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="disp:Discipline_Area">
      <sch:assert test="if (disp:Display_Direction) then (disp:Display_Settings/disp:Display_Direction) else true()">
        Display Dictionary: If the Display_Direction class is in the label, it must be contained in a Display_Settings class.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="disp:Display_Direction">
      <sch:assert test="if (disp:horizontal_display_direction) then disp:horizontal_display_direction = ('Left to Right', 'Right to Left') else true()">
        The attribute disp:horizontal_display_direction must be equal to one of the following values 'Left to Right', 'Right to Left'.</sch:assert>
      <sch:assert test="if (disp:vertical_display_direction) then disp:vertical_display_direction = ('Bottom to Top', 'Top to Bottom') else true()">
        The attribute disp:vertical_display_direction must be equal to one of the following values 'Bottom to Top', 'Top to Bottom'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="disp:Display_Settings/pds:Local_Internal_Reference">
      <sch:assert test="disp:local_identifier_reference = //pds:*/pds:*/(pds:Array|pds:Array_2D|pds:Array_2D_Image|pds:Array_2D_Map|pds:Array_2D_Spectrum|pds:Array_3D|pds:Array_3D_Image|pds:Array_3D_Movie|pds:Array_3D_Spectrum)/pds:local_identifier">
        Display Dictionary: In the disp:Local_Internal_Reference class, the value of the disp:local_identifier_reference must match the value of the pds:local_identifer of an Array class or sub-class within the File_Area.</sch:assert>
      <sch:assert test="disp:local_reference_type = ('display_settings_to_array')">
        Display_Dictionary: In the disp:Local_Internal_Reference class, the value of the disp:local_reference_type must be 'display_settings_to_array'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="disp:Movie_Display_Settings">
      <sch:assert test="if (disp:loop_flag) then disp:loop_flag = ('false', 'true') else true()">
        The attribute disp:loop_flag must be equal to one of the following values 'false', 'true'.</sch:assert>
      <sch:assert test="if (disp:loop_back_and_forth_flag) then disp:loop_back_and_forth_flag = ('false', 'true') else true()">
        The attribute disp:loop_back_and_forth_flag must be equal to one of the following values 'false', 'true'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="disp:Movie_Display_Settings/disp:frame_rate">
      <sch:assert test="@unit = ('frames/s')">
        The attribute @unit must be equal to one of the following values 'frames/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="disp:Movie_Display_Settings/disp:loop_delay">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 's', 'yr')">
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="disp:Display_Settings/disp:Local_Internal_Reference">
      <sch:assert test="disp:local_identifier_reference = //pds:*/pds:*/(pds:Array|pds:Array_2D|pds:Array_2D_Image|pds:Array_2D_Map|pds:Array_2D_Spectrum|pds:Array_3D|pds:Array_3D_Image|pds:Array_3D_Movie|pds:Array_3D_Spectrum)/pds:local_identifier">
        Display Dictionary: In the disp:Local_Internal_Reference class, the value of the disp:local_identifier_reference must match the value of the pds:local_identifer of an Array class or sub-class within the File_Area.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="disp:Display_Settings/disp:Local_Internal_Reference">
      <sch:assert test="disp:local_reference_type = ('display_settings_to_array')">
        Display_Dictionary: In the disp:Local_Internal_Reference class, the value of the disp:local_reference_type must be 'display_settings_to_array'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
