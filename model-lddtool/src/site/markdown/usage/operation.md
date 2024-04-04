The *Ingest LDD* schema is defined in the PDS4 Information Model (IM)
and is used to define local data dictionaries. Once a local data
dictionary is defined it can be ingested into the IM and the PDS4 Data
Dictionary Data Base (DDDB). A local data dictionary at the discipline
level is required to be ingested. A dictionary at the mission level can
optionally be ingested.

The PDS4 Information Model (IM) is the fundamental reference for PDS4
structure; its requirements can be validated automatically using
eXtensible Markup Language (XML) schemas.

The PDS4 Data Dictionary Data Base (DDDB) is the fundamental reference
for definitions of classes and attributes.

*Table of Contents*

* [1.1 Purpose](#1.1-purpose)
* 1.2 Scope
* 1.3 Audience
* 1.4 Document Organization
* 1.5 External Standards
* 1.6 Document Availability
* 2 Ingest LDD
* 2 Ingest LDD Components
* 2.1 Ingest LDD
* 2.2 DD_Attribute
* 2.2.1 Internal_Reference
* 2.2.2 Terminological_Entry
* 2.2.2.1 Internal_Reference_Extended
* 2.2.3 DD_Value_Domain
* 2.2.3.1 DD_Permissible_Value
* 2.3 DD_Class
* 2.3.1 DD_Association
* 2.4 DD Rule
* 2.4.1 DD Rule Statement
* 2.5 Property Maps
* 2.5.1 Property Map
* 2.5.1.1 Property Map Entry
* 3 Special Considerations
* 3.1 References to Attributes or Classes from Other Namespaces
* 3.2 Restrictions of Attributes from Other Namespaces
* 3.3 Allowing a Choice Between Several Attributes or Classes
* 3.4 Allowing Any Attribute or Class
* 3.5 Auto-Generating Data Dictionary Documentation
* 4 Examples
* 4.1 Display Local Data Dictionary
* 4.1.1 Ingest_LDD -- The header for the local data dictionary
* 4.1.2 DD_Attribute -- Defining an attribute
* 4.1.3 DD_Class -- Defining a class
* 4.1.4 DD_Association -- Relating the components to the class
* 4.1.5 DD_Rule -- Defining a rule
* 4.1.6 Property_Maps - Defining a Property_Mapropops


1.1 Purpose
-----------

This document describes the elements of *Ingest LDD* and how they are
used to create a local data dictionary.

*Ingest LDD* is both an XML schema defined in the PDS4 IM and an XML
template generated from the schema. The *Ingest LDD* template is used
during the design of a local data dictionary to capture the components
of the local data dictionary.

1.2 Scope
---------

The *Ingest LDD* Users Guide applies to local data dictionaries at the
discipline and mission levels of the PDS. The *Ingest LDD* constrains
the design of a local data dictionary so that it is consistent with the
core PDS4 IM and DDDB and all controlling standards documents.

1.3 Audience
------------

The Ingest LDD Users Guide is intended primarily to serve the community
of scientists and engineers responsible for creating local data
dictionaries for the PDS4 Information Model. These new dictionaries
augment the common model and have their own stewards and unique
namespaces. The audience includes personnel at PDS discipline and data
nodes, principal investigators and their staffs, and ground data system
engineers. The document will be most useful to people who have prior
experience with the PDS Information Model and modeling practices.

1.4 Document Organization
-------------------------

The Ingest LDD Users Guide is divided into four parts. The first is this
part. The second provides detailed information on the components of
Ingest LDD. The third describes special considerations for building a
local data dictionary. The final part is an example of a completed
Ingest_LDD template.

1.5 External Standards
----------------------

External standards, which apply to this document and to PDS4-compliant
data, include the following:

[International Standards Organization (ISO)]{.ul}

-   ISO/IEC 11179-3:2003 *Metadata registries (MDR) -- Part 3: Registry
    metamodel and basic attributes*

-   ISO/IEC 19757-3:2006 *Information technology \-- Document Schema
    Definition Languages (DSDL) \-- Part 3: Rule-based validation \--
    Schematron*

[World Wide Web Consortium (W3C)]{.ul}

-   *Extensible Markup Language (XML) 1.0* (Fifth Edition)

-   *W3C XML Schema Definition Language (XSD) 1.1 Part 1: Structures*
    (W3C, 2012a)

-   W3C XML Schema Definition Language (XSD) 1.1 Part 2: Datatypes (W3C,
    2012b)

1.6 Document Availability
-------------------------

PDS4 documents governing archive preparation are available online at:

<http://pds.nasa.gov/pds4/doc/>

For questions concerning these documents, contact any PDS data engineer
or contact the PDS Operator at <pds_operator@jpl.nasa.gov> or
818-393-7165.

Associated schemas for current and past versions of PDS4 can be found at

<http://pds.nasa.gov/pds4/schema/released/>

2 Ingest LDD
============

An *Ingest LDD* template is used during the design of a local data
dictionary to capture the components of the local data dictionary. In
the following sections each class is described as a series of attribute
definitions[^1]. If the class has component classes, these are
referenced by name. The component classes are each defined in their own
section.

Note that the *Ingest_LDD*, *DD_Attribute*, and *DD_Class* classes
described in this section are strictly classified as meta-classes since
they are being used to define classes at the user level. Likewise the
*comment*, *name*, and *description* attributes are strictly classified
as meta-attributes since they are used in the meta-classes. This
distinction is important to keep in mind while discussiong the
Ingest_LDD template since the meta-class uses meta-attributes to define
user level classes and attributes. However for the remainder of this
document and for simplicity the simpler terms *attribute* and *class*
will be used.

Finally there are special considerations for selected attributes. For
example, attributes defined in an external dictionary can be referenced
as a component when defining a class. The mechanism for distinguishing
local from external attributes is described in section three.

2 Ingest LDD Components 
=======================

2.1 Ingest LDD
--------------

The *Ingest_LDD* schema is used to define a new local data dictionary.
The *Ingest_LDD* class is the header for the dictionary.

![](media/image1.emf){width="6.5in" height="6.78253937007874in"}

2.2 DD_Attribute
----------------

The *DD_Attribute* class is used to define an attribute for the local
data dictionary. All attributes should be defined before any class is
defined. Each attribute is subsequently referenced in the class
definitions zero or more times. Within a class, an attribute is
indicated as Optional if its minimum and maximum cardinalities are 0:1.
An attribute is indicated as Required if its minimum and maximum
cardinalities are 1:1. For example "comment" is an Optional attribute
which may be omitted when completing a DD_Attribute class.

![](media/image2.emf){width="6.5in" height="8.295219816272965in"}

### 2.2.1 Internal_Reference

The Internal Reference class is used to reference a product in a PDS4
registry. Either the LID or the LIDVID of the product, but not both, may
be used.

![](media/image3.emf){width="6.5in" height="4.455777559055118in"}

### 2.2.2 Terminological_Entry

The Terminological Entry class is used to provide an alternate name and
definition.

![](media/image4.emf){width="6.5in" height="3.3015726159230097in"}

### 2.2.2.1 Internal_Reference_Extended

The Internal_Reference_Extended provides a reference to documents or
resources that are external to a PDS4 registry.

![](media/image5.emf){width="6.5in" height="3.632836832895888in"}

### 2.2.3 DD_Value_Domain

The DD_Value_Domain class is used to define the data type and value
constraints for a DD_Attribute.

![](media/image6.emf){width="6.5in" height="6.76134186351706in"}

2.2.3.1 DD_Permissible_Value
----------------------------

The DD_Permissible_Value class is used to define the permissible values
and value meanings for an attribute.

![](media/image7.emf){width="6.5in" height="1.8192661854768153in"}

2.3 DD_Class
------------

The DD_Class class is used to define a new class for the local data
dictionary. Once all attributes are defined, the classes are defined by
referencing their component attributes and classes through the
DD_Association class. Each attribute or class can be referenced zero or
more times.

![](media/image8.emf){width="6.5in" height="8.954268372703412in"}

2.3.1 DD_Association
--------------------

The DD_Association class is used to relate a class to each of its
components. The sequence order of the DD_Association classes is the
order assigned to the components.

![](media/image9.emf){width="6.5in" height="5.114827209098863in"}

2.4 DD Rule
-----------

The DD_Rule class is used to define a rule for validating constraints.

![](media/image10.emf){width="6.5in" height="2.642522965879265in"}

2.4.1 DD Rule Statement
-----------------------

The DD_Rule_Statement class is used to define a rule for validating
constraints.

![](media/image11.emf){width="6.5in" height="4.2918853893263345in"}

2.5 Property Maps
-----------------

The Property Maps class defines a collection of one or more related
Property Map(s). Each Property Map is an association of one or more
parameter/value pairs to a single attribute defined in the Information
Model. The purpose of a Property Map is to augment an attribute's
definition with additional information, for example a synonym from
another model.

![](media/image12.emf){width="6.5in" height="7.620866141732283in"}

2.5.1 Property Map
------------------

The Property Map class defines a table consisting of a set of data
elements and their values. Each Property Map is an association of one or
more parameter/value pairs to a single attribute defined in the
Information Model. The purpose of a Property Map is to augment an
attribute's definition with additional information, for example a
synonym from another model.

![](media/image13.emf){width="6.5in" height="5.620138888888889in"}

2.5.1.1 Property Map Entry
--------------------------

The property map entry consists of a property name and one or more
values. The purpose of a Property Map Entry is to augment an attribute's
definition with a single item of additional information, for example a
synonym from another model.

![](media/image14.emf){width="6.5in" height="1.4330708661417322in"}

3 Special Considerations
========================

3.1 References to Attributes or Classes from Other Namespaces 
-------------------------------------------------------------

In DD_Association, an attribute or a class can be referenced from
another namespace. The reference is provided as a value of
local_identifier and consists of the namespace followed by a period '.'
followed by the name of the attribute or class. In the case of an
attribute, assume that the attribute definition is generic and has not
been restricted in the referenced namespace.

![](media/image15.emf){width="6.5in" height="1.6553740157480314in"}

3.2 Restrictions of Attributes from Other Namespaces 
----------------------------------------------------

In DD_Attribute, an attribute from an external namespace can be
restricted and used in this namespace. The name of the attribute may or
may not change. The local attribute definition takes precedence over the
definition of the external attribute. The reference to the external
attribute is provided as a value of local_identifier and consists of the
unique identifier of the attribute. This identifier consists of the
class namespace id, class name, attribute namespace id, and attribute
name, each delimted by a period '.'.

![](media/image16.emf){width="6.5in" height="1.0613112423447069in"}

3.3 Allowing a Choice Between Several Attributes or Classes
-----------------------------------------------------------

In DD_Association a choice between several attributes or classes can be
indicated by including the special token *XSChoice\#* as a value of
local_identifier. All remaining values of local_identifier, attribute or
class names, will subsequently be grouped in a choice block.

![](media/image17.emf){width="6.5in" height="1.320444006999125in"}

3.4 Allowing Any Attribute or Class
-----------------------------------

In DD_Association the special token *XSAny\#* used as a value of
local_identifier indicates that any attribute or class can be added and
that they will not be verified as required or optionoal members. Any
remaining values of local_identifier, attribute or class names, will
subsequently be grouped in the *Any* block.

![](media/image18.emf){width="6.5in" height="1.6492377515310586in"}

4 Examples
==========

4.1 Display Local Data Dictionary 
---------------------------------

The *Display* dictionary describes how to display Array data on a
display device. In the following example, snippets of the dictionary
have been inserted.

### 4.1.1 Ingest_LDD -- The header for the local data dictionary

The Ingest_LDD class provides general information about the local data
dictionary. The names_space_id and ldd_version_id in particular are used
to name the resulting files, for example the XML schema file.

\</Ingest_LDD\>

\<name\>Display\</name\>

\<ldd_version_id\>1.1.0.0\</ldd_version_id\>

\<full_name\>Elizabeth D. Rye\</full_name\>

\<steward_id\>img\</steward_id\>

\<namespace_id\>disp\</namespace_id\>

\<comment\>This dictionary describes how to display Array data on a
display

device.\</comment\>

\<last_modification_date_time\>2014-02-21T20:12:59Z\</last_modification_date_time\>

### 4.1.2 DD_Attribute -- Defining an attribute

DD_Attribute is used to define an attribute. The two instances of
DD_Attribute provide below define a standard attribute that accepts a
simple token as a value and an attribute that has set of permissible
values, respectively.

\<DD_Attribute\>

\<name\>horizontal_display_axis\</name\>

\<version_id\>1.0\</version_id\>

\<local_identifier\>disp.horizontal_display_axis\</local_identifier\>

\<nillable_flag\>false\</nillable_flag\>

\<submitter_name\>Elizabeth D. Rye\</submitter_name\>

\<definition\>The horizontal_display_axis attribute identifies, by name,

the axis of an Array (or Array subclass) that is intended to be

displayed in the horizontal or \"sample\" dimension on a display

device. The value of this attribute must match the value of one, and

only one, axis_name attribute in an Axis_Array class of the

associated Array.\</definition\>

\<DD_Value_Domain\>

\<enumeration_flag\>false\</enumeration_flag\>

\<value_data_type\>ASCII_Short_String_Collapsed\</value_data_type\>

\<unit_of_measure_type\>Units_of_None\</unit_of_measure_type\>

\</DD_Value_Domain\>

\</DD_Attribute\>

\<DD_Attribute\>

\<name\>horizontal_display_direction\</name\>

\<version_id\>1.0\</version_id\>

\<local_identifier\>disp.horizontal_display_direction\</local_identifier\>

\<nillable_flag\>false\</nillable_flag\>

\<submitter_name\>Elizabeth.D.Rye\</submitter_name\>

\<definition\>The horizontal_display_direction attribute specifies the

direction across the screen of a display device that data along the

horizontal axis of an Array is supposed to be displayed.\</definition\>

\<DD_Value_Domain\>

\<enumeration_flag\>true\</enumeration_flag\>

\<value_data_type\>ASCII_Short_String_Collapsed\</value_data_type\>

\<unit_of_measure_type\>Units_of_None\</unit_of_measure_type\>

\<DD_Permissible_Value\>

\<value\>Left to Right\</value\>

\<value_meaning\>The lowest indexed element along an array axis should
be displayed at the left edge of a display device and elements with
higher indices should be displayed further to the right. Note that this
is the standard display direction for most major image
formats.\</value_meaning\>

\</DD_Permissible_Value\>

\<DD_Permissible_Value\>

\<value\>Right to Left\</value\>

\<value_meaning\>The lowest indexed element along an array axis should
be displayed at the right edge of a display device and elements with
higher indices should be displayed further to the left. Note that
virtually no image display formats use this display direction. Use this
only when deliberately mirroring the image around the vertical
axis.\</value_meaning\>

\</DD_Permissible_Value\>

\</DD_Value_Domain\>

\</DD_Attribute\>

### 4.1.3 DD_Class -- Defining a class

DD_Class is used to define a class. The namespace_id of the class is
inherited from the Ingest_LDD class. The local_identifier is used if
necessary to reference the class in this XML file however it has no role
in the resulting files, for example the XML schema file.

\<DD_Class\>

\<name\>Display_Direction\</name\>

\<version_id\>1.0\</version_id\>

\<local_identifier\>disp.Display_Direction\</local_identifier\>

\<submitter_name\>Elizabeth D. Rye\</submitter_name\>

\<definition\>The Display_Direction class specifies how two of the

dimensions of an Array object should be displayed in the vertical

(line) and horizontal (sample) dimensions of a display

device.\</definition\>

### 4.1.4 DD_Association -- Relating the components to the class

DD_Association is used to relate the components to the class. The
attribute local_identifier is used to reference either an attribute or a
class that is defined in this XML file. The reference_type attribute
indicates the type of the relationship. The minimum_occurrences and the
maximum_occurences indicate whether the component is optional or
required. As and example of a special consideration, in the following,
the attribute *comment* is being referenced from the pds namespace. This
is inferred since the value of local_identifier is not present in this
XML file.

\<DD_Association\>

\<local_identifier\>pds.comment\</local_identifier\>

\<reference_type\>attribute_of\</reference_type\>

\<minimum_occurrences\>0\</minimum_occurrences\>

\<maximum_occurrences\>1\</maximum_occurrences\>

\</DD_Association\>

\<DD_Association\>

\<local_identifier\>disp.horizontal_display_axis\</local_identifier\>

\<reference_type\>attribute_of\</reference_type\>

\<minimum_occurrences\>1\</minimum_occurrences\>

\<maximum_occurrences\>1\</maximum_occurrences\>

\</DD_Association\>

\<DD_Association\>

\<local_identifier\>disp.horizontal_display_direction\</local_identifier\>

\<reference_type\>attribute_of\</reference_type\>

\<minimum_occurrences\>1\</minimum_occurrences\>

\<maximum_occurrences\>1\</maximum_occurrences\>

\</DD_Association\>

\...

\</DD_Class\>

### 4.1.5 DD_Rule -- Defining a rule

DD_Rule is used to define a rule for validating constraints. The
following validates that if the Display_Direction class is in the label
then it must be contained in the Display_Settings class.

\<DD_Rule\>

\<local_identifier\>Display_Direction_Display_Settings\</local_identifier\>

\<rule_context\>pds:Discipline_Area\</rule_context\>

\<DD_Rule_Statement\>

\<rule_type\>Assert\</rule_type\>

\<rule_test\>if (disp:Display_Direction) then
(disp:Display_Settings/disp:Display_Direction) else true()\</rule_test\>

\<rule_message\>Display Dictionary: If the Display_Direction class is in
the label, it must be contained in a Display_Settings
class.\</rule_message\>

\</DD_Rule_Statement\>

\</DD_Rule\>

\</Ingest_LDD\>

### 4.1.6 Property_Maps - Defining a Property_Maps

The Property Maps class defines a collection of one or more related
Property Maps. Each Property Map is an association of one or more
parameter/value pairs to a single attribute defined in the Information
Model. The purpose of a Property Map is to augment an attribute's
definition with additional information, for example a synonym from
another model.

\<Property_Maps\>

\<identifier\>img_property_map_definitions\</identifier\>

\<namespace_id\>img\</namespace_id\>

\<property_map_type\>Synonym\</property_map_type\>

\<property_map_subtype\>PDS3 Keyword\</property_map_subtype\>

\<Property_Map\>

\<identifier\>0001_NASA_PDS_1.pds.Property_Map.

img.Instrument_Compression_Parameters.img.

instrument_compression_block_size_x\</identifier\>

\<model_object_id\>0001_NASA_PDS_1.img.

Instrument_Compression_Parameters.img.

instrument_compression_block_size_x\</model_object_id\>

\<model_object_type\>Attribute\</model_object_type\>

\<instance_id\>xpath to an XML label instance if needed\</instance_id\>

\<description\>IMG instrument_compression_block_size_x
definitions\</description\>

\<Property_Map_Entry\>

\<property_name\>PDS3 Keyword\</property_name\>

\<property_value\>INST_CMPRS_BLK_SIZE\</property_value\>

\</Property_Map_Entry

\</Property_Map\>

\<Property_Map\>

\<identifier\>0001_NASA_PDS_1.pds.Property_Map.img.

Instrument_Compression_Parameters.img.

instrument_compression_block_size_y\</identifier\>
\<model_object_id\>0001_NASA_PDS_1.img.Instrument_Compression_Parameters.img.

instrument_compression_block_size_y\</model_object_id\>

\<model_object_type\>Attribute\</model_object_type\>

\<instance_id\>xpath to an XML label instance if needed\</instance_id\>

\<description\>IMG instrument_compression_block_size_y
definitions\</description\>

\<Property_Map_Entry\>

\<property_name\>PDS3 Keyword\</property_name\>

\<property_value\>INST_CMPRS_BLK_SIZE\</property_value\>

\</Property_Map_Entry\>

\</Property_Map\>

\<Property_Maps\>

[^1]: Note that the classes *Ingest_LDD*, *DD_Attribute*, and *DD_Class*
    described in this section are strictly classified as meta-classes
    since they are being used to define classes at the *user* level.
    Likewise the attributes *comment*, *name*, and *description* are
    strictly classified as meta-attributes since they are used in the
    meta-classes. This distinction is important to keep in mind while
    discussiong the Ingest_LDD template since the meta-class uses
    meta-attributes to define the *user* level classes and attributes.
    However for the remainder of this document and for simplicity the
    simpler terms *attribute* and *class* will be used.
