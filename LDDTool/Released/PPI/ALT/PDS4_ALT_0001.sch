<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:alt  Version:0.0.0.1 - Thu Apr 02 14:29:23 PDT 2015 -->
  <!-- Generated from the PDS4 Information Model Version 1.4.0.0 - System Build 5b -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/alt/v0" prefix="alt"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="alt:Local_Internal_Reference/alt:local_reference_type">
      <sch:assert test=". = ('data_values_to_data_values')">
        The attribute alt:local_reference_type must be equal to the value 'data_values_to_data_values'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
