<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:wave  Version:1.0.0.0 - Fri Apr 24 12:44:46 PDT 2015 -->
  <!-- Generated from the PDS4 Information Model Version 1.4.0.0 - System Build 5b -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/wave/v1" prefix="wave"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="wave:Local_Internal_Reference/wave:local_reference_type">
      <sch:assert test=". = ('wave_observation_to_axis_values', 'wave_observation_to_face_values', 'wave_observation_to_observation_values')">
        The attribute wave:local_reference_type must be equal to one of the following values 'wave_observation_to_axis_values', 'wave_observation_to_face_values', 'wave_observation_to_observation_values'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
