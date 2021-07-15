# Changelog

## [«unknown»](https://github.com/NASA-PDS/pds4-information-model/tree/«unknown») (2021-07-15)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v13.1.0-SNAPSHOT...«unknown»)

**Improvements:**

- \[namespace-registry\] add new namespace "clipper" [\#374](https://github.com/NASA-PDS/pds4-information-model/issues/374)

## [v13.1.0-SNAPSHOT](https://github.com/NASA-PDS/pds4-information-model/tree/v13.1.0-SNAPSHOT) (2021-07-08)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v13.0.2...v13.1.0-SNAPSHOT)

**Requirements:**

- As a developer I want to generate a query model that relates semantically similar permissible values. [\#361](https://github.com/NASA-PDS/pds4-information-model/issues/361)

**Improvements:**

- \[namespace-registry\] update path "\<clementine\>" in registry PDF \(possibly shorten name\) [\#373](https://github.com/NASA-PDS/pds4-information-model/issues/373)
- Evolution of CCB-256: Need method for providing permissible value definitions for external namespaces in Ingest\_LDD. [\#369](https://github.com/NASA-PDS/pds4-information-model/issues/369)
- \[namespace-registry\] add new namespace "ml" [\#353](https://github.com/NASA-PDS/pds4-information-model/issues/353)
- \[namespace-registry\] add new namespace "m2020" [\#352](https://github.com/NASA-PDS/pds4-information-model/issues/352)
- \[namespace-registry\] add new namespace "ctli" [\#344](https://github.com/NASA-PDS/pds4-information-model/issues/344)
- \[namespace-registry\] add new namespace "\<clementine\>" [\#332](https://github.com/NASA-PDS/pds4-information-model/issues/332)

**Defects:**

- IMTool/LDDTool still attempts to process dLDD ingested into Protege [\#378](https://github.com/NASA-PDS/pds4-information-model/issues/378)
- The Version Id of the Product\_XML\_Schema label is not being set to the proper value. [\#371](https://github.com/NASA-PDS/pds4-information-model/issues/371)
- LDDTool does not generate valid xpath for schematron rules for classes defined using associate\_external\_class [\#367](https://github.com/NASA-PDS/pds4-information-model/issues/367)
- LDDTool generates a 1C00 file when -V 1B00 is specified [\#322](https://github.com/NASA-PDS/pds4-information-model/issues/322)
- LDDTool does not generate the correct namespace for PSA dictionaries [\#301](https://github.com/NASA-PDS/pds4-information-model/issues/301)

**Other closed issues:**

- Ingest PDS4 IM into BlazeGraph [\#357](https://github.com/NASA-PDS/pds4-information-model/issues/357)

## [v13.0.2](https://github.com/NASA-PDS/pds4-information-model/tree/v13.0.2) (2021-05-04)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v13.0.1...v13.0.2)

**Defects:**

- CCB-328 : Inconsistency in \<title\> type definition [\#339](https://github.com/NASA-PDS/pds4-information-model/issues/339) [[s.low](https://github.com/NASA-PDS/pds4-information-model/labels/s.low)]

## [v13.0.1](https://github.com/NASA-PDS/pds4-information-model/tree/v13.0.1) (2021-04-28)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v12.1.0...v13.0.1)

**Requirements:**

- As a developer, I want to know the dLDD version from the output JSON data [\#338](https://github.com/NASA-PDS/pds4-information-model/issues/338)

**Improvements:**

- Add title to Rule Assertion to allow generation of regression tests. [\#298](https://github.com/NASA-PDS/pds4-information-model/issues/298)
- Update JSON output to include dependencies in output [\#293](https://github.com/NASA-PDS/pds4-information-model/issues/293)
- DocBook HTML/WebHelp generation and conversion processes [\#242](https://github.com/NASA-PDS/pds4-information-model/issues/242)
- Continuing refactoring of IMTool / LDDTool [\#238](https://github.com/NASA-PDS/pds4-information-model/issues/238)

**Defects:**

- Version flag does not return the latest information for the tool [\#342](https://github.com/NASA-PDS/pds4-information-model/issues/342) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- Invalid output schema when trying to set an Internal\_Reference reference\_type value set [\#331](https://github.com/NASA-PDS/pds4-information-model/issues/331) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- LDDTool outputs invalid schema with v13.0.0 [\#328](https://github.com/NASA-PDS/pds4-information-model/issues/328) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- Repo tests fail when trying to run back-to-back maven steps [\#327](https://github.com/NASA-PDS/pds4-information-model/issues/327) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- The 1F00 directory is missing from the Data directory for the 1G00 development release.  [\#316](https://github.com/NASA-PDS/pds4-information-model/issues/316) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- LDDTool does not generate the complete "All LDD" version of the  WebHelp PDS4 Data Dictionary Document [\#312](https://github.com/NASA-PDS/pds4-information-model/issues/312) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- LDDTool does not include the PSA namespace [\#304](https://github.com/NASA-PDS/pds4-information-model/issues/304) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- LDDTool does not allow the bundle to be specified for generated dictionaries [\#302](https://github.com/NASA-PDS/pds4-information-model/issues/302) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- CSV files fail to escape double quotes. [\#280](https://github.com/NASA-PDS/pds4-information-model/issues/280) [[s.low](https://github.com/NASA-PDS/pds4-information-model/labels/s.low)]
- Throw WARNING message when enumeration\_flag = false but enumerations are specified [\#266](https://github.com/NASA-PDS/pds4-information-model/issues/266) [[s.low](https://github.com/NASA-PDS/pds4-information-model/labels/s.low)]

**Other closed issues:**

- \[namespace-registry\] add new namespace "multi" [\#319](https://github.com/NASA-PDS/pds4-information-model/issues/319)
- CCB-264: Make the Line Feed \(LF\) character an allowed record delimiter [\#299](https://github.com/NASA-PDS/pds4-information-model/issues/299)
- CCB-311: Value meanings for \<rule\_type\> permissible values should be rewritten [\#296](https://github.com/NASA-PDS/pds4-information-model/issues/296)
- CCB-321: Add MPEG-4 as an encoding\_standard\_id for Product\_Native [\#288](https://github.com/NASA-PDS/pds4-information-model/issues/288)
- CCB-320 : Add 'W/m\*\*2/sr/nm/\(DN/s\)' under Units\_of\_Misc [\#286](https://github.com/NASA-PDS/pds4-information-model/issues/286)
- CCB-319: Add 'ns' – The abbreviated unit for 'Units\_of\_Time' \(1/10^9 s\) [\#285](https://github.com/NASA-PDS/pds4-information-model/issues/285)
- \[namespace-registry\] add new namespace "HYB2" [\#267](https://github.com/NASA-PDS/pds4-information-model/issues/267)
- CCB-302: No \<reference\_type\> values defined in DD\_Attribute or DD\_Class contexts [\#257](https://github.com/NASA-PDS/pds4-information-model/issues/257)
- CCB-312: \<ldd\_version\_id\> does not appear to be constrained the way LDDTool expects [\#256](https://github.com/NASA-PDS/pds4-information-model/issues/256)
- CCB-300: Apparently deprecated units of measure are not actually deprecated [\#255](https://github.com/NASA-PDS/pds4-information-model/issues/255)
- CCB-305: Missing validation constraint on \<specified\_unit\_id\> [\#254](https://github.com/NASA-PDS/pds4-information-model/issues/254)
- CCB-315: "PDS3" is an allowed parsing standard for Bundle documentation file [\#250](https://github.com/NASA-PDS/pds4-information-model/issues/250)
- Document how to generate the DocBook XML and how to generate HTML/WebHelp [\#243](https://github.com/NASA-PDS/pds4-information-model/issues/243)

## [v12.1.0](https://github.com/NASA-PDS/pds4-information-model/tree/v12.1.0) (2021-01-11)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v12.0.3...v12.1.0)

**Defects:**

- LDDTool generated LIDs for XML Schema Label Products  do not contain IM or LDD Version Ids [\#283](https://github.com/NASA-PDS/pds4-information-model/issues/283) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- LDD versionId list in the Data Dictionary Document introduction does not contain valid versionIds [\#277](https://github.com/NASA-PDS/pds4-information-model/issues/277) [[s.low](https://github.com/NASA-PDS/pds4-information-model/labels/s.low)]

**Other closed issues:**

- CCB-313: Definition of \<external\_source\_product\_identifier\> refers to non-existent documentation. [\#275](https://github.com/NASA-PDS/pds4-information-model/issues/275)

## [v12.0.3](https://github.com/NASA-PDS/pds4-information-model/tree/v12.0.3) (2020-12-23)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v12.0.2...v12.0.3)

**Other closed issues:**

- CCB-285:  GeoTIFF format as operational PDS4 image [\#166](https://github.com/NASA-PDS/pds4-information-model/issues/166)

## [v12.0.2](https://github.com/NASA-PDS/pds4-information-model/tree/v12.0.2) (2020-12-22)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v12.0.1...v12.0.2)

**Defects:**

- LDDTool forces use of LDD versions based upon config [\#271](https://github.com/NASA-PDS/pds4-information-model/issues/271) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]

**Other closed issues:**

- CCB-317: Add FITS 4.0 to parsing\_standard\_id enumerated values for Header object [\#273](https://github.com/NASA-PDS/pds4-information-model/issues/273)

## [v12.0.1](https://github.com/NASA-PDS/pds4-information-model/tree/v12.0.1) (2020-12-18)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/12.0.0...v12.0.1)

**Improvements:**

- \[namespace-registry\] add new namespace "\<chan1\>" [\#245](https://github.com/NASA-PDS/pds4-information-model/issues/245)
- Improvements from Build 11.0 testing [\#241](https://github.com/NASA-PDS/pds4-information-model/issues/241)

**Defects:**

- Incorrect error messages for DD\_Associate\_External\_Class [\#235](https://github.com/NASA-PDS/pds4-information-model/issues/235)
- PDS4\_PDS\_1E00.sch inconsistent rules for type of Investigation vs Investigation\_Area [\#186](https://github.com/NASA-PDS/pds4-information-model/issues/186)
- LDDTool: Displaying invalid Imaging Discipline Classes [\#175](https://github.com/NASA-PDS/pds4-information-model/issues/175)

**Other closed issues:**

- Stage the IMTool system for V1.16.0.0 to allow the implementation of approved SCRs and bug fixes. [\#262](https://github.com/NASA-PDS/pds4-information-model/issues/262)
- CCB-313: Definition of \<external\_source\_product\_identifier\> refers to non-existent documentation [\#253](https://github.com/NASA-PDS/pds4-information-model/issues/253)
- CCB-304: Cleanup unused Vector classes in IM before 2.0.0.0 [\#252](https://github.com/NASA-PDS/pds4-information-model/issues/252)
- \[namespace-registry\] add new namespace "SURVEY" [\#234](https://github.com/NASA-PDS/pds4-information-model/issues/234)
- \[namespace-registry\] add new namespace "NUCSPEC" [\#233](https://github.com/NASA-PDS/pds4-information-model/issues/233)
- LDDTool: Enable to ability to set custom namespace base URI in IngestLDD   [\#104](https://github.com/NASA-PDS/pds4-information-model/issues/104)

## [12.0.0](https://github.com/NASA-PDS/pds4-information-model/tree/12.0.0) (2020-10-12)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/11.3.2...12.0.0)

**Improvements:**

- LDDTool: Revise the handling of supplying the IM version \(alternate version\) via command-line [\#222](https://github.com/NASA-PDS/pds4-information-model/issues/222)
- LDDTool: Use sch:value-of to display a variable in Schematron validation [\#167](https://github.com/NASA-PDS/pds4-information-model/issues/167)

**Defects:**

- LDDTool: update LDD attribute checking to only check for "type" and "\_type" [\#230](https://github.com/NASA-PDS/pds4-information-model/issues/230)
- LDDTool: uses wrong version of XSD when referencing IMG dictionary [\#229](https://github.com/NASA-PDS/pds4-information-model/issues/229)
- LDDTool: erroneously replaces TBD values with null in JSON output [\#227](https://github.com/NASA-PDS/pds4-information-model/issues/227)
- CCB-308: IM schematron needs to be updated to handle SI unit prefixes [\#193](https://github.com/NASA-PDS/pds4-information-model/issues/193)

**Other closed issues:**

- lddtool-11.4.0-SNAPSHOT version output incorrect [\#228](https://github.com/NASA-PDS/pds4-information-model/issues/228)
- IMTool - Cleanup warning messages [\#225](https://github.com/NASA-PDS/pds4-information-model/issues/225)
- CCB-297: Inappropriate values in permissible values list for Product\_Observational/Observation\_Area/Target\_Identification/Internal\_Reference/reference\_type [\#220](https://github.com/NASA-PDS/pds4-information-model/issues/220)
- CCB-294: Add enumerated value to Units\_of\_Pixel\_Resolution\_Angular [\#218](https://github.com/NASA-PDS/pds4-information-model/issues/218)
- CCB-296: Duplicated value in enumerated value list of Product\_Context /Internal\_Reference /reference\_type. [\#216](https://github.com/NASA-PDS/pds4-information-model/issues/216)
- CCB-292:	New enumerated values for reference\_type. [\#214](https://github.com/NASA-PDS/pds4-information-model/issues/214)
- IMTool: Remove read of environment variable JAVA\_HOME [\#209](https://github.com/NASA-PDS/pds4-information-model/issues/209)
- CCB-284: Streamline process for adding or removing standard values. [\#165](https://github.com/NASA-PDS/pds4-information-model/issues/165)

## [11.3.2](https://github.com/NASA-PDS/pds4-information-model/tree/11.3.2) (2020-08-17)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v11.3.1...11.3.2)

**Improvements:**

- LDDTool: Improve error messages to clearly designate between FATAL\_ERROR vs ERROR [\#178](https://github.com/NASA-PDS/pds4-information-model/issues/178)
- LDDTool: Add error counter or flag to enable unsuccessful termination System.exit [\#176](https://github.com/NASA-PDS/pds4-information-model/issues/176)
- LDDTool: Add capability to generate LDDs based on user-specified IM version [\#88](https://github.com/NASA-PDS/pds4-information-model/issues/88)
- LDDTool: Enhance software error handling and logging for improved usability [\#87](https://github.com/NASA-PDS/pds4-information-model/issues/87)

**Defects:**

- LDDtool combining two consecutive choice blocks into one block  [\#192](https://github.com/NASA-PDS/pds4-information-model/issues/192)
- LDDTool: requires one class with \(element\_flag = true\), even when no classes defined [\#188](https://github.com/NASA-PDS/pds4-information-model/issues/188) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]

**Other closed issues:**

- CCB-289: Define new class Encoded\_Audio to describe supplementary audio data products [\#207](https://github.com/NASA-PDS/pds4-information-model/issues/207)
- LDDTool: Add the alternate Information Model Version 1E00 [\#205](https://github.com/NASA-PDS/pds4-information-model/issues/205)
- CCB-290: Add value 'Field Campaign' to Investigation\_Area type enumerated value list [\#203](https://github.com/NASA-PDS/pds4-information-model/issues/203)
- LDDTool: System exit in code aborts MAVEN build [\#199](https://github.com/NASA-PDS/pds4-information-model/issues/199)
- LDDTool: Deprecation Rule being propogated to schematron file with an error in the assert statement. [\#197](https://github.com/NASA-PDS/pds4-information-model/issues/197)
- LDDTool: MSN namespace declaration is missing from the generated schematron file. [\#194](https://github.com/NASA-PDS/pds4-information-model/issues/194)
- LDDTool: Better handle LDD generation errors through more clear logging [\#86](https://github.com/NASA-PDS/pds4-information-model/issues/86)
- LDDTool: Cleanup output log messages to use consistent formatting and remove unnecessary messages [\#85](https://github.com/NASA-PDS/pds4-information-model/issues/85)

## [v11.3.1](https://github.com/NASA-PDS/pds4-information-model/tree/v11.3.1) (2020-05-22)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v11.3.0...v11.3.1)

## [v11.3.0](https://github.com/NASA-PDS/pds4-information-model/tree/v11.3.0) (2020-05-22)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v11.2.2...v11.3.0)

**Improvements:**

- LDDTool: Add PDS Namespace Registry information to Help output [\#172](https://github.com/NASA-PDS/pds4-information-model/issues/172)
- LDDTool: Setup and configure CI/CD and Nightly Builds [\#158](https://github.com/NASA-PDS/pds4-information-model/issues/158)

**Defects:**

- LDDTool does not handle "unbounded" maximum cardinality in Choice blocks correctly [\#180](https://github.com/NASA-PDS/pds4-information-model/issues/180)

**Other closed issues:**

- CCB-268 how to test this [\#143](https://github.com/NASA-PDS/pds4-information-model/issues/143)
- CCB-256: Need method for providing permissible value definitions for external namespaces in Ingest\_LDD [\#130](https://github.com/NASA-PDS/pds4-information-model/issues/130)

## [v11.2.2](https://github.com/NASA-PDS/pds4-information-model/tree/v11.2.2) (2020-04-06)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v11.2.1...v11.2.2)

**Defects:**

- LDDTool: Version info bug introduced since v11.2.0 [\#168](https://github.com/NASA-PDS/pds4-information-model/issues/168)

## [v11.2.1](https://github.com/NASA-PDS/pds4-information-model/tree/v11.2.1) (2020-03-24)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v11.2.0...v11.2.1)

## [v11.2.0](https://github.com/NASA-PDS/pds4-information-model/tree/v11.2.0) (2020-03-13)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v11.1.0...v11.2.0)

**Defects:**

- Bugfix - Add code to fail gracefully for deprecated -M argument [\#154](https://github.com/NASA-PDS/pds4-information-model/issues/154)
- Bugfix - Change the error message reported for "nillable" attribute error. [\#146](https://github.com/NASA-PDS/pds4-information-model/issues/146)
- LDDtool: exposed class has a component class with a component exposed class [\#142](https://github.com/NASA-PDS/pds4-information-model/issues/142)

**Other closed issues:**

- CCB-279: Mis-Matched \<axes\> and Axis\_Array Specifications  [\#152](https://github.com/NASA-PDS/pds4-information-model/issues/152)
- CCB-272: Reinstate Array\_1D in the Information Model [\#148](https://github.com/NASA-PDS/pds4-information-model/issues/148)
- LDDtool: unescaped & in output .xml [\#141](https://github.com/NASA-PDS/pds4-information-model/issues/141)

## [v11.1.0](https://github.com/NASA-PDS/pds4-information-model/tree/v11.1.0) (2020-03-11)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v11.0.0...v11.1.0)

**Other closed issues:**

- CCB-274 - Add attribute dictionary\_type to Ingest\_LDD - Update [\#144](https://github.com/NASA-PDS/pds4-information-model/issues/144)

## [v11.0.0](https://github.com/NASA-PDS/pds4-information-model/tree/v11.0.0) (2020-02-29)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v10.1.2...v11.0.0)

**Other closed issues:**

- CCB-278: Fix errors in logical\_identifier, ASCII\_LID, ASCIIVID and ASCII\_LIDVID\_LID [\#139](https://github.com/NASA-PDS/pds4-information-model/issues/139)
- CCB-274 - Add attribute dictionary\_type to Ingest\_LDD [\#137](https://github.com/NASA-PDS/pds4-information-model/issues/137)
- CCB-220: Add ability to specify many source products via table. [\#135](https://github.com/NASA-PDS/pds4-information-model/issues/135)
- CCB-271: Add reference\_types for Product\_Ancillary [\#133](https://github.com/NASA-PDS/pds4-information-model/issues/133)
- Improve versioning documentation to include IM version information [\#132](https://github.com/NASA-PDS/pds4-information-model/issues/132)

## [v10.1.2](https://github.com/NASA-PDS/pds4-information-model/tree/v10.1.2) (2020-01-24)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v10.1.1...v10.1.2)

**Improvements:**

- Update LDDTool to write the PDS4 Data Dictionary \(DocBook\) for multiple LDDs [\#93](https://github.com/NASA-PDS/pds4-information-model/issues/93)

**Defects:**

- LDDTool: Fix invalid examples or link to valid versions online [\#105](https://github.com/NASA-PDS/pds4-information-model/issues/105)
- Improve error handling for LDDTool processing of CTLI IngestLDD file [\#91](https://github.com/NASA-PDS/pds4-information-model/issues/91)

**Other closed issues:**

- Sync up LDDTool version with Maven build version [\#127](https://github.com/NASA-PDS/pds4-information-model/issues/127)
- Nillable attributes are not declared nillable in class definitions.   [\#125](https://github.com/NASA-PDS/pds4-information-model/issues/125)
- Sync LDDTool version with Maven version [\#124](https://github.com/NASA-PDS/pds4-information-model/issues/124)
- CCB-204: Define and enforce best practices for discipline and project dictionaries. Part-4 [\#122](https://github.com/NASA-PDS/pds4-information-model/issues/122)
- CCB-204: Define and enforce best practices for discipline and project dictionaries. Part-3 [\#113](https://github.com/NASA-PDS/pds4-information-model/issues/113)
- LDDTool aborts on short filename [\#111](https://github.com/NASA-PDS/pds4-information-model/issues/111)
- Clean up IMTool/LDDTool UML/XMI file writer for MagicDraw UML Class Diagrams [\#109](https://github.com/NASA-PDS/pds4-information-model/issues/109)
- CCB-138 Fix mismatch between context object types and values of \<type\> in \<Observing\_System\_Component\> class [\#103](https://github.com/NASA-PDS/pds4-information-model/issues/103)
- CCB-204: Validate that no attribute is named "unit" - Part 2 [\#101](https://github.com/NASA-PDS/pds4-information-model/issues/101)
- CCB-268 Add optional attribute to class Terminological\_Entry [\#99](https://github.com/NASA-PDS/pds4-information-model/issues/99)
- CCB-204: Define and enforce best practices for discipline and project dictionaries. - Part 1 [\#97](https://github.com/NASA-PDS/pds4-information-model/issues/97)
- Multi LDD DataDictionary Cleanup [\#95](https://github.com/NASA-PDS/pds4-information-model/issues/95)
- Stage PDS4 Information Model V1E00 Build\_10b [\#89](https://github.com/NASA-PDS/pds4-information-model/issues/89)

## [v10.1.1](https://github.com/NASA-PDS/pds4-information-model/tree/v10.1.1) (2019-10-19)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v10.1.0...v10.1.1)

**Improvements:**

- LDDTool: Update version flag \(-v\) to show IM version as well as software version [\#74](https://github.com/NASA-PDS/pds4-information-model/issues/74)

**Other closed issues:**

- IMTool: LDDTool Config Properties Update [\#83](https://github.com/NASA-PDS/pds4-information-model/issues/83)
- LDDTool: IngestLDD 'Report' rules do not make it to Schematron [\#1](https://github.com/NASA-PDS/pds4-information-model/issues/1)

## [v10.1.0](https://github.com/NASA-PDS/pds4-information-model/tree/v10.1.0) (2019-10-11)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v10.0.0...v10.1.0)

**Defects:**

- LDDTool: XMLSchema Fix Null In Import Cleanup [\#81](https://github.com/NASA-PDS/pds4-information-model/issues/81)
- LDDTool: XMLSchema Fix Nul In Import File Name [\#79](https://github.com/NASA-PDS/pds4-information-model/issues/79)
- LDDTool: Fixed the calculation of class extensions and restrictions [\#77](https://github.com/NASA-PDS/pds4-information-model/issues/77)
-  CCB-252 Make\_Science\_Facets\_wavelength\_range\_nillable\_BugFix [\#64](https://github.com/NASA-PDS/pds4-information-model/issues/64)

**Other closed issues:**

- Revert CCB-256 Permissible value definitions Ingest LDD [\#75](https://github.com/NASA-PDS/pds4-information-model/issues/75)
- CCB-256 Permissible\_value\_definitions\_Ingest\_LDD\_Update\_Data [\#72](https://github.com/NASA-PDS/pds4-information-model/issues/72)
- CCB-256 Permissible\_value\_definitions\_Ingest\_LDD\_Update [\#70](https://github.com/NASA-PDS/pds4-information-model/issues/70)
- DOMConv\_Depcreation\_FinalCleanup\_Deprecate\_InfoModel\_and\_Associated\_Classes [\#68](https://github.com/NASA-PDS/pds4-information-model/issues/68)
- DOMConv\_Deprecation\_FinalCleanup\_Continued [\#66](https://github.com/NASA-PDS/pds4-information-model/issues/66)

## [v10.0.0](https://github.com/NASA-PDS/pds4-information-model/tree/v10.0.0) (2019-09-12)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/f4d66f49a34894ec115bfdfc7370019578b59854...v10.0.0)

**Other closed issues:**

- DOMConv Deprecation\_FinalCleanup - Fix Errors Found in Build 10a [\#62](https://github.com/NASA-PDS/pds4-information-model/issues/62)
- 190908b\_DOMConv\_Depcreation\_FinalCleanup\_Remove\_DOM\_From\_Filename - Part 1 [\#60](https://github.com/NASA-PDS/pds4-information-model/issues/60)
- 190908\_PDS4\_Information\_Model\_V1D00\_Database\_Update - Part 3 [\#58](https://github.com/NASA-PDS/pds4-information-model/issues/58)
- 190908\_PDS4\_Information\_Model\_V1D00\_Database\_Update - Part 2 [\#56](https://github.com/NASA-PDS/pds4-information-model/issues/56)
- 190908\_PDS4\_Information\_Model\_V1D00\_Database\_Update - Part 1 [\#54](https://github.com/NASA-PDS/pds4-information-model/issues/54)
- DOMConv Deprecation\_FinalCleanup - Deprecate Deprecate InfoModel [\#52](https://github.com/NASA-PDS/pds4-information-model/issues/52)
- DOMConv Deprecation\_FinalCleanup - Deprecate MasterInfoModel [\#50](https://github.com/NASA-PDS/pds4-information-model/issues/50)
- DOMConv Deprecation\_FinalCleanup - Deprecate LDDParser [\#48](https://github.com/NASA-PDS/pds4-information-model/issues/48)
- DOMConv Deprecation\_FinalCleanup - Deprecate GetModels [\#46](https://github.com/NASA-PDS/pds4-information-model/issues/46)
- DOMConv Deprecation\_FinalCleanup - Deprecate MOF Writers [\#44](https://github.com/NASA-PDS/pds4-information-model/issues/44)
- DOMConv Deprecation\_FinalCleanup - ExportModels [\#42](https://github.com/NASA-PDS/pds4-information-model/issues/42)
- CCB-256	Permissible\_value\_definitions\_Ingest\_LDD [\#40](https://github.com/NASA-PDS/pds4-information-model/issues/40)
- DOMConv Deprecation\_FinalCleanup - DeprecatedDefn [\#38](https://github.com/NASA-PDS/pds4-information-model/issues/38)
- DOMConv Deprecation\_FinalCleanup - DOMProtAttr [\#36](https://github.com/NASA-PDS/pds4-information-model/issues/36)
- DOMConv Deprecation\_FinalCleanup - DOMAttr [\#34](https://github.com/NASA-PDS/pds4-information-model/issues/34)
- DOMConv Deprecation\_FinalCleanup WriteDOMSpecification [\#32](https://github.com/NASA-PDS/pds4-information-model/issues/32)
- CCB-249	Schematron Rule Elements - Assert and Report [\#29](https://github.com/NASA-PDS/pds4-information-model/issues/29)
- CCB258 Software\_Updates [\#27](https://github.com/NASA-PDS/pds4-information-model/issues/27)
- CCB-253-Add\_Units\_of\_Force\_as\_a\_unit\_of\_measure [\#25](https://github.com/NASA-PDS/pds4-information-model/issues/25)
- CCB-251 - Add Units of Gmass as a unit of measure [\#23](https://github.com/NASA-PDS/pds4-information-model/issues/23)
- CCB-266-Change\_Data\_Type\_of\_External\_Reference\_reference\_text [\#21](https://github.com/NASA-PDS/pds4-information-model/issues/21)
- CCB-262-Add\_Supporting\_Observationan\_To\_Primary\_Result\_Summary\_purpose [\#19](https://github.com/NASA-PDS/pds4-information-model/issues/19)
- CCB-252\_Make\_Science\_Facets\_wavelength\_range\_nillable [\#17](https://github.com/NASA-PDS/pds4-information-model/issues/17)
- CCB\_250\_Add\_MD5\_To\_Checksum\_Type\_Deprecate\_MD5Deep [\#16](https://github.com/NASA-PDS/pds4-information-model/issues/16)
- CCB\_244\_Deprecate\_Earth-based\_from\_Instrument\_Host\_type [\#14](https://github.com/NASA-PDS/pds4-information-model/issues/14)
- CCB\_254\_Add\_Astrophysical\_To\_Targetype [\#12](https://github.com/NASA-PDS/pds4-information-model/issues/12)
- DOMConv - Install new dd11179.pins file [\#10](https://github.com/NASA-PDS/pds4-information-model/issues/10)
- CCB-237 - Change Attribute doi to Data Type ASCII\_DOI [\#9](https://github.com/NASA-PDS/pds4-information-model/issues/9)
- DOMConv\_UnitTesting\_PINS\_Test\_classOrder [\#7](https://github.com/NASA-PDS/pds4-information-model/issues/7)
- Update Maven docs to refer to Github release assets [\#6](https://github.com/NASA-PDS/pds4-information-model/issues/6)
- DOM Conversion [\#4](https://github.com/NASA-PDS/pds4-information-model/issues/4)



\* *This Changelog was automatically generated by [github_changelog_generator](https://github.com/github-changelog-generator/github-changelog-generator)*
