<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:mvn  Version:1.0.0.0 - Mon Mar 23 17:10:07 PDT 2015 -->
  <!-- Generated from the PDS4 Information Model Version 1.4.0.0 - System Build 5b -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/mvn/v1" prefix="mvn"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="mvn:MAVEN/mvn:mission_phase_name">
      <sch:assert test=". = ('Cruise', 'Launch', 'Orbital Insertion', 'Prime Mission')">
        The attribute mvn:mission_phase_name must be equal to one of the following values 'Cruise', 'Launch', 'Orbital Insertion', 'Prime Mission'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
