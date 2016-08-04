<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:rings  Version:1.1.0.0 - Wed Jul 31 14:31:35 EDT 2013 -->
  <!-- Generated from the PDS4 Information Model Version 1.0.0.0 - System Build 3b -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://pds.nasa.gov/pds4/rings/v1" prefix="rings"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="rings:Radio_Occultation">
      <sch:assert test="if (rings:occultation_type) then rings:occultation_type = ('Radio', 'Solar', 'Stellar') else true()">
        The attribute rings:occultation_type must be equal to one of the following values 'Radio', 'Solar', 'Stellar'.</sch:assert>
      <sch:assert test="if (rings:frequency_band) then rings:frequency_band = ('C', 'D', 'E', 'F', 'G', 'H', 'K', 'Ka', 'Ku', 'Q', 'R', 'S', 'U', 'V', 'W', 'X', 'Y') else true()">
        The attribute rings:frequency_band must be equal to one of the following values 'C', 'D', 'E', 'F', 'G', 'H', 'K', 'Ka', 'Ku', 'Q', 'R', 'S', 'U', 'V', 'W', 'X', 'Y'.</sch:assert>
      <sch:assert test="if (rings:ring_occultation_direction) then rings:ring_occultation_direction = ('Both', 'Egress', 'Ingress', 'Multiple') else true()">
        The attribute rings:ring_occultation_direction must be equal to one of the following values 'Both', 'Egress', 'Ingress', 'Multiple'.</sch:assert>
      <sch:assert test="if (rings:ring_profile_direction) then rings:ring_profile_direction = ('Egress', 'Ingress', 'Multiple') else true()">
        The attribute rings:ring_profile_direction must be equal to one of the following values 'Egress', 'Ingress', 'Multiple'.</sch:assert>
      <sch:assert test="if (rings:planetary_occultation_flag) then rings:planetary_occultation_flag = ('N', 'Y') else true()">
        The attribute rings:planetary_occultation_flag must be equal to one of the following values 'N', 'Y'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="rings:Radio_Occultation_Support">
      <sch:assert test="if (rings:occultation_type) then rings:occultation_type = ('Radio', 'Solar', 'Stellar') else true()">
        The attribute rings:occultation_type must be equal to one of the following values 'Radio', 'Solar', 'Stellar'.</sch:assert>
      <sch:assert test="if (rings:frequency_band) then rings:frequency_band = ('C', 'D', 'E', 'F', 'G', 'H', 'K', 'Ka', 'Ku', 'Q', 'R', 'S', 'U', 'V', 'W', 'X', 'Y') else true()">
        The attribute rings:frequency_band must be equal to one of the following values 'C', 'D', 'E', 'F', 'G', 'H', 'K', 'Ka', 'Ku', 'Q', 'R', 'S', 'U', 'V', 'W', 'X', 'Y'.</sch:assert>
      <sch:assert test="if (rings:ring_occultation_direction) then rings:ring_occultation_direction = ('Both', 'Egress', 'Ingress', 'Multiple') else true()">
        The attribute rings:ring_occultation_direction must be equal to one of the following values 'Both', 'Egress', 'Ingress', 'Multiple'.</sch:assert>
      <sch:assert test="if (rings:ring_profile_direction) then rings:ring_profile_direction = ('Egress', 'Ingress', 'Multiple') else true()">
        The attribute rings:ring_profile_direction must be equal to one of the following values 'Egress', 'Ingress', 'Multiple'.</sch:assert>
      <sch:assert test="if (rings:planetary_occultation_flag) then rings:planetary_occultation_flag = ('N', 'Y') else true()">
        The attribute rings:planetary_occultation_flag must be equal to one of the following values 'N', 'Y'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="rings:Stellar_Occultation">
      <sch:assert test="if (rings:occultation_type) then rings:occultation_type = ('Radio', 'Solar', 'Stellar') else true()">
        The attribute rings:occultation_type must be equal to one of the following values 'Radio', 'Solar', 'Stellar'.</sch:assert>
      <sch:assert test="if (rings:ring_occultation_direction) then rings:ring_occultation_direction = ('Both', 'Egress', 'Ingress', 'Multiple') else true()">
        The attribute rings:ring_occultation_direction must be equal to one of the following values 'Both', 'Egress', 'Ingress', 'Multiple'.</sch:assert>
      <sch:assert test="if (rings:ring_profile_direction) then rings:ring_profile_direction = ('Egress', 'Ingress', 'Multiple') else true()">
        The attribute rings:ring_profile_direction must be equal to one of the following values 'Egress', 'Ingress', 'Multiple'.</sch:assert>
      <sch:assert test="if (rings:planetary_occultation_flag) then rings:planetary_occultation_flag = ('N', 'Y') else true()">
        The attribute rings:planetary_occultation_flag must be equal to one of the following values 'N', 'Y'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
