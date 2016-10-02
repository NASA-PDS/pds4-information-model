<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:insight  Version:0.5.1.2 - Tue Dec 01 14:59:11 PST 2015 -->
  <!-- Generated from the PDS4 Information Model Version 1.5.1.0 - System Build 6a -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/insight/v0" prefix="insight"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="insight:Observation_Information/insight:mission_phase_name">
      <sch:assert test=". = ('ATLO', 'CRUISE', 'DEVELOPMENT', 'SURFACE MISSION', 'TEST')">
        The attribute insight:mission_phase_name must be equal to one of the following values 'ATLO', 'CRUISE', 'DEVELOPMENT', 'SURFACE MISSION', 'TEST'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="insight:Observation_Information/insight:product_type">
      <sch:assert test=". = ('ARK', 'ARM', 'ARO', 'BAY', 'CLM', 'CLR', 'DDD', 'DDL', 'DDS', 'DEM', 'DFF', 'DFL', 'DFS', 'DSE', 'DSG', 'DSL', 'DSP', 'DSR', 'DSS', 'EDR', 'EJP', 'ERP', 'GUH', 'GUS', 'IEF', 'IEP', 'ILC', 'ILM', 'ILP', 'ILT', 'IOF', 'IOI', 'ION', 'LIN', 'MAR', 'MDS', 'MSK', 'MXY', 'RAD', 'RAD-raw', 'RAF', 'RAS', 'RDM', 'RDR', 'RIE', 'RIF', 'RNE', 'RNF', 'RNG', 'RNM', 'RNO', 'RNR', 'RSM', 'RUH', 'RUS', 'SHD', 'SHO', 'SLO', 'SLP', 'SMG', 'SMO', 'SNO', 'SNT', 'STATIL-raw', 'TAU', 'TDS', 'TEM-raw', 'TFH', 'TFS', 'TFW', 'TLM-raw', 'TNF', 'TRO', 'UIH', 'UIS', 'UIW', 'UUF', 'UUU', 'UVF', 'UVO', 'UVP', 'UVS', 'UVT', 'UVW', 'VVF', 'VVV', 'WEA', 'WSH', 'WSS', 'WWF', 'WWW', 'XXF', 'XXX', 'XYE', 'XYF', 'XYM', 'XYO', 'XYR', 'XYZ', 'YYF', 'YYY', 'ZIH', 'ZIS', 'ZIW', 'ZZF', 'ZZZ')">
        The attribute insight:product_type must be equal to one of the following values 'ARK', 'ARM', 'ARO', 'BAY', 'CLM', 'CLR', 'DDD', 'DDL', 'DDS', 'DEM', 'DFF', 'DFL', 'DFS', 'DSE', 'DSG', 'DSL', 'DSP', 'DSR', 'DSS', 'EDR', 'EJP', 'ERP', 'GUH', 'GUS', 'IEF', 'IEP', 'ILC', 'ILM', 'ILP', 'ILT', 'IOF', 'IOI', 'ION', 'LIN', 'MAR', 'MDS', 'MSK', 'MXY', 'RAD', 'RAD-raw', 'RAF', 'RAS', 'RDM', 'RDR', 'RIE', 'RIF', 'RNE', 'RNF', 'RNG', 'RNM', 'RNO', 'RNR', 'RSM', 'RUH', 'RUS', 'SHD', 'SHO', 'SLO', 'SLP', 'SMG', 'SMO', 'SNO', 'SNT', 'STATIL-raw', 'TAU', 'TDS', 'TEM-raw', 'TFH', 'TFS', 'TFW', 'TLM-raw', 'TNF', 'TRO', 'UIH', 'UIS', 'UIW', 'UUF', 'UUU', 'UVF', 'UVO', 'UVP', 'UVS', 'UVT', 'UVW', 'VVF', 'VVV', 'WEA', 'WSH', 'WSS', 'WWF', 'WWW', 'XXF', 'XXX', 'XYE', 'XYF', 'XYM', 'XYO', 'XYR', 'XYZ', 'YYF', 'YYY', 'ZIH', 'ZIS', 'ZIW', 'ZZF', 'ZZZ'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="insight:Observation_Information/insight:start_solar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="insight:Observation_Information/insight:stop_solar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="insight:SEIS_Instrument_Parameters/insight:channel_name">
      <sch:assert test=". = ('SP_1', 'SP_2', 'SP_3', 'VBB_1_Position', 'VBB_1_Temperature', 'VBB_1_Velocity', 'VBB_2_Position', 'VBB_2_Temperature', 'VBB_2_Velocity', 'VBB_3_Position', 'VBB_3_Temperature', 'VBB_3_Velocity')">
        The attribute insight:channel_name must be equal to one of the following values 'SP_1', 'SP_2', 'SP_3', 'VBB_1_Position', 'VBB_1_Temperature', 'VBB_1_Velocity', 'VBB_2_Position', 'VBB_2_Temperature', 'VBB_2_Velocity', 'VBB_3_Position', 'VBB_3_Temperature', 'VBB_3_Velocity'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="insight:SEIS_Instrument_Parameters/insight:measurement_type">
      <sch:assert test=". = ('SP_Event', 'VBB_Continuous', 'VBB_Event')">
        The attribute insight:measurement_type must be equal to one of the following values 'SP_Event', 'VBB_Continuous', 'VBB_Event'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
