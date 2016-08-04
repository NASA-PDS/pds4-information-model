<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:geom  Version:1.0.0.0 - Thu Apr 30 14:01:45 EDT 2015 -->
  <!-- Generated from the PDS4 Information Model Version 1.4.0.0 - System Build 5b -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/geom/v1" prefix="geom"/>
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
    <sch:rule context="geom:Acceleration_Vector_Cartesian_Base/geom:x_acceleration">
      <sch:assert test="@unit = ('cm/s**2', 'km/s**2', 'm/s**2')">
        The attribute @unit must be equal to one of the following values 'cm/s**2', 'km/s**2', 'm/s**2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Acceleration_Vector_Cartesian_Base/geom:y_acceleration">
      <sch:assert test="@unit = ('cm/s**2', 'km/s**2', 'm/s**2')">
        The attribute @unit must be equal to one of the following values 'cm/s**2', 'km/s**2', 'm/s**2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Acceleration_Vector_Cartesian_Base/geom:z_acceleration">
      <sch:assert test="@unit = ('cm/s**2', 'km/s**2', 'm/s**2')">
        The attribute @unit must be equal to one of the following values 'cm/s**2', 'km/s**2', 'm/s**2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Acceleration_Vector_Cartesian_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Acceleration_Vector_Cartesian_Generic/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Central_Body_To_Spacecraft_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Central_Body_To_Spacecraft_Position_Vector_Planetocentric/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Central_Body_To_Target_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Central_Body_To_Target_Position_Vector_Planetocentric/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_System/geom:coordinate_system_type">
      <sch:assert test=". = ('Azimuth-Elevation', 'Cartesian', 'Planetocentric', 'Planetodetic', 'Planetographic', 'Spherical')">
        The attribute geom:coordinate_system_type must be equal to one of the following values 'Azimuth-Elevation', 'Cartesian', 'Planetocentric', 'Planetodetic', 'Planetographic', 'Spherical'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distance_Generic/geom:distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances/geom:spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances/geom:spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances/geom:spacecraft_to_central_body_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances/geom:spacecraft_to_target_boresight_intercept_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances/geom:spacecraft_to_target_center_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances/geom:spacecraft_to_target_subspacecraft_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances/geom:target_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances/geom:target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances/geom:target_ssb_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Earth_To_Central_Body_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Earth_To_Spacecraft_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Earth_To_Target_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_FOV_Range_Values/geom:illumination_range_designation">
      <sch:assert test=". = ('Field of View', 'Target')">
        The attribute geom:illumination_range_designation must be equal to one of the following values 'Field of View', 'Target'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_FOV_Range_Values/geom:maximum_emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_FOV_Range_Values/geom:maximum_incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_FOV_Range_Values/geom:maximum_phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_FOV_Range_Values/geom:minimum_emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_FOV_Range_Values/geom:minimum_incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_FOV_Range_Values/geom:minimum_phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Single_Values/geom:emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Single_Values/geom:incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Single_Values/geom:phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Single_Values/geom:reference_location">
      <sch:assert test=". = ('Boresight Intercept Point', 'Subspacecraft Point', 'Target Center')">
        The attribute geom:reference_location must be equal to one of the following values 'Boresight Intercept Point', 'Subspacecraft Point', 'Target Center'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Single_Values/geom:reference_pixel_location">
      <sch:assert test=". = ('Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner')">
        The attribute geom:reference_pixel_location must be equal to one of the following values 'Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Single_Values/geom:solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:List_Index_Angle/geom:index_value_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:List_Index_Length/geom:index_value_length">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:List_Index_Temperature/geom:index_value_temperature">
      <sch:assert test="@unit = ('K', 'degC')">
        The attribute @unit must be equal to one of the following values 'K', 'degC'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:body_positive_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:celestial_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:central_body_north_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:ecliptic_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:sun_direction_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:target_north_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:target_positive_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_North_East/geom:east_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_North_East/geom:north_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:celestial_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:declination_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:ecliptic_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:reference_pixel_location">
      <sch:assert test=". = ('Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner')">
        The attribute geom:reference_pixel_location must be equal to one of the following values 'Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:right_ascension_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:right_ascension_hour_angle">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 's', 'yr')">
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Dimensions/geom:horizontal_pixel_size_angular">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Dimensions/geom:vertical_pixel_size_angular">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Intercept/geom:pixel_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Intercept/geom:pixel_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Intercept/geom:reference_pixel_location">
      <sch:assert test=". = ('Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner')">
        The attribute geom:reference_pixel_location must be equal to one of the following values 'Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Size_Projected/geom:horizontal_pixel_size_projected">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Size_Projected/geom:reference_location">
      <sch:assert test=". = ('Boresight Intercept Point', 'Subspacecraft Point', 'Target Center')">
        The attribute geom:reference_location must be equal to one of the following values 'Boresight Intercept Point', 'Subspacecraft Point', 'Target Center'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Size_Projected/geom:vertical_pixel_size_projected">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Position_Vector_Cartesian_Base/geom:x_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Position_Vector_Cartesian_Base/geom:y_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Position_Vector_Cartesian_Base/geom:z_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Position_Vector_Cartesian_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Position_Vector_Cartesian_Generic/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Position_Vector_Planetocentric_Base/geom:latitude_position">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Position_Vector_Planetocentric_Base/geom:longitude_position">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Position_Vector_Planetocentric_Base/geom:radius_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Position_Vector_Planetocentric_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Position_Vector_Planetocentric_Generic/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Reference_Pixel/geom:horizontal_coordinate_pixel">
      <sch:assert test="@unit = ('DN', 'electron/DN', 'pixel')">
        The attribute @unit must be equal to one of the following values 'DN', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Reference_Pixel/geom:vertical_coordinate_pixel">
      <sch:assert test="@unit = ('DN', 'electron/DN', 'pixel')">
        The attribute @unit must be equal to one of the following values 'DN', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:SPICE_Kernel_Identification/pds:kernel_type">
      <sch:assert test=". = ('CK', 'DBK', 'DSK', 'EK', 'FK', 'IK', 'LSK', 'MK', 'PCK', 'SCLK', 'SPK')">
        The attribute pds:kernel_type must be equal to one of the following values 'CK', 'DBK', 'DSK', 'EK', 'FK', 'IK', 'LSK', 'MK', 'PCK', 'SCLK', 'SPK'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:SSB_To_Central_Body_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:SSB_To_Spacecraft_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:SSB_To_Target_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Spacecraft_Relative_To_Central_Body_Velocity_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Spacecraft_Relative_To_Earth_Velocity_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Spacecraft_Relative_To_SSB_Velocity_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Spacecraft_Relative_To_Sun_Velocity_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Spacecraft_Relative_To_Target_Velocity_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Spacecraft_Relative_To_Target_Velocity_Vector_Planetocentric/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Spacecraft_To_Target_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Spacecraft_To_Target_Position_Vector_Planetocentric/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Sun_To_Central_Body_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Sun_To_Spacecraft_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Sun_To_Target_Position_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry/geom:subsolar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry/geom:subsolar_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry/geom:subsolar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry/geom:subspacecraft_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry/geom:subspacecraft_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry/geom:subspacecraft_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Target_Relative_To_Central_Body_Velocity_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Target_Relative_To_Central_Body_Velocity_Vector_Planetocentric/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Target_Relative_To_Earth_Velocity_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Target_Relative_To_SSB_Velocity_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Target_Relative_To_Spacecraft_Velocity_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Target_Relative_To_Spacecraft_Velocity_Vector_Planetocentric/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Target_Relative_To_Sun_Velocity_Vector_Cartesian/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Pixel/geom:x_pixel">
      <sch:assert test="@unit = ('DN', 'electron/DN', 'pixel')">
        The attribute @unit must be equal to one of the following values 'DN', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Pixel/geom:y_pixel">
      <sch:assert test="@unit = ('DN', 'electron/DN', 'pixel')">
        The attribute @unit must be equal to one of the following values 'DN', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Pixel/geom:z_pixel">
      <sch:assert test="@unit = ('DN', 'electron/DN', 'pixel')">
        The attribute @unit must be equal to one of the following values 'DN', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Velocity_Vector_Cartesian_Base/geom:x_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Velocity_Vector_Cartesian_Base/geom:y_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Velocity_Vector_Cartesian_Base/geom:z_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Velocity_Vector_Cartesian_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Velocity_Vector_Cartesian_Generic/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Velocity_Vector_Planetocentric_Base/geom:latitude_velocity">
      <sch:assert test="@unit = ('deg/day', 'deg/s', 'rad/s')">
        The attribute @unit must be equal to one of the following values 'deg/day', 'deg/s', 'rad/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Velocity_Vector_Planetocentric_Base/geom:longitude_velocity">
      <sch:assert test="@unit = ('deg/day', 'deg/s', 'rad/s')">
        The attribute @unit must be equal to one of the following values 'deg/day', 'deg/s', 'rad/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Velocity_Vector_Planetocentric_Base/geom:radial_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Velocity_Vector_Planetocentric_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Velocity_Vector_Planetocentric_Generic/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
