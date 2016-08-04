<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:sbngeom  Version:1.0.0.0 - Sun Jun 08 16:13:16 PDT 2014 -->
  <!-- Generated from the PDS4 Information Model Version 1.3.0.0 - System Build 5a -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://pds.nasa.gov/pds4/sbngeom/v1" prefix="sbngeom"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="sbngeom:Celestial_Coordinates">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:coordinate_system_origin) then sbngeom:coordinate_system_origin = ('spacecraft') else true()">
        The attribute sbngeom:coordinate_system_origin must be equal to the value 'spacecraft'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Celestial_Coordinates/sbngeom:declination">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Celestial_Coordinates/sbngeom:right_ascension">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Clock_Angles/sbngeom:celestial_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Clock_Angles/sbngeom:ecliptic_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Clock_Angles/sbngeom:sun_direction_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Display_Direction">
      <sch:assert test="if (sbngeom:horizontal_display_direction) then sbngeom:horizontal_display_direction = ('Left to Right', 'Right to Left') else true()">
        The attribute sbngeom:horizontal_display_direction must be equal to one of the following values 'Left to Right', 'Right to Left'.</sch:assert>
      <sch:assert test="if (sbngeom:vertical_display_direction) then sbngeom:vertical_display_direction = ('Bottom to Top', 'Top to Bottom') else true()">
        The attribute sbngeom:vertical_display_direction must be equal to one of the following values 'Bottom to Top', 'Top to Bottom'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Distances/sbngeom:spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Distances/sbngeom:spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Earth_to_Target_Position_Vector_Cartesian">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Earth_to_Target_Position_Vector_Cartesian/sbngeom:x">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Earth_to_Target_Position_Vector_Cartesian/sbngeom:y">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Earth_to_Target_Position_Vector_Cartesian/sbngeom:z">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Earth_to_Target_Velocity_Vector_Cartesian">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Earth_to_Target_Velocity_Vector_Cartesian/sbngeom:Vx">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Earth_to_Target_Velocity_Vector_Cartesian/sbngeom:Vy">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Earth_to_Target_Velocity_Vector_Cartesian/sbngeom:Vz">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Ecliptic_Coordinates">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:coordinate_system_origin) then sbngeom:coordinate_system_origin = ('spacecraft') else true()">
        The attribute sbngeom:coordinate_system_origin must be equal to the value 'spacecraft'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Ecliptic_Coordinates/sbngeom:ecliptic_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Ecliptic_Coordinates/sbngeom:ecliptic_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Illumination_Angles/sbngeom:phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Illumination_Angles/sbngeom:solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Earth_Position_Vector_Cartesian">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Earth_Position_Vector_Cartesian/sbngeom:x">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Earth_Position_Vector_Cartesian/sbngeom:y">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Earth_Position_Vector_Cartesian/sbngeom:z">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Earth_Velocity_Vector_Cartesian">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Earth_Velocity_Vector_Cartesian/sbngeom:Vx">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Earth_Velocity_Vector_Cartesian/sbngeom:Vy">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Earth_Velocity_Vector_Cartesian/sbngeom:Vz">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Sun_Position_Vector_Cartesian">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Sun_Position_Vector_Cartesian/sbngeom:x">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Sun_Position_Vector_Cartesian/sbngeom:y">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Sun_Position_Vector_Cartesian/sbngeom:z">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Sun_Velocity_Vector_Cartesian">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Sun_Velocity_Vector_Cartesian/sbngeom:Vx">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Sun_Velocity_Vector_Cartesian/sbngeom:Vy">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Sun_Velocity_Vector_Cartesian/sbngeom:Vz">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Target_Distance/sbngeom:distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Target_Position_Vector_Cartesian">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Target_Position_Vector_Cartesian/sbngeom:x">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Target_Position_Vector_Cartesian/sbngeom:y">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Target_Position_Vector_Cartesian/sbngeom:z">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Target_Velocity_Vector_Cartesian">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Target_Velocity_Vector_Cartesian/sbngeom:Vx">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Target_Velocity_Vector_Cartesian/sbngeom:Vy">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Spacecraft_to_Target_Velocity_Vector_Cartesian/sbngeom:Vz">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Sun_to_Target_Position_Vector_Cartesian">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Sun_to_Target_Position_Vector_Cartesian/sbngeom:x">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Sun_to_Target_Position_Vector_Cartesian/sbngeom:y">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Sun_to_Target_Position_Vector_Cartesian/sbngeom:z">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Sun_to_Target_Velocity_Vector_Cartesian">
      <sch:assert test="if (sbngeom:reference_frame_id) then sbngeom:reference_frame_id = ('ICRF', 'J2000') else true()">
        The attribute sbngeom:reference_frame_id must be equal to one of the following values 'ICRF', 'J2000'.</sch:assert>
      <sch:assert test="if (sbngeom:light_time_flag) then sbngeom:light_time_flag = ('true', 'false') else true()">
        The attribute sbngeom:light_time_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (sbngeom:stellar_aberration_flag) then sbngeom:stellar_aberration_flag = ('true', 'false') else true()">
        The attribute sbngeom:stellar_aberration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Sun_to_Target_Velocity_Vector_Cartesian/sbngeom:Vx">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Sun_to_Target_Velocity_Vector_Cartesian/sbngeom:Vy">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Sun_to_Target_Velocity_Vector_Cartesian/sbngeom:Vz">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Target_Geocentric_Distance/sbngeom:distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Target_Heliocentric_Distance/sbngeom:distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="sbngeom:Target_Pole_Clock_Angle/sbngeom:body_positive_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
