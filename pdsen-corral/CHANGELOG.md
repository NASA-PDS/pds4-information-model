# Changelog

## [Unreleased](https://github.com/NASA-PDS/pds4-information-model/tree/HEAD)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v11.3.1...HEAD)

**Improvements:**

- LDDTool: Improve error messages to clearly designate between FATAL\_ERROR vs ERROR [\#178](https://github.com/NASA-PDS/pds4-information-model/issues/178)
- LDDTool: Add error counter or flag to enable unsuccessful termination System.exit [\#176](https://github.com/NASA-PDS/pds4-information-model/issues/176)
- LDDTool: Add capability to generate LDDs based on user-specified IM version [\#88](https://github.com/NASA-PDS/pds4-information-model/issues/88)
- LDDTool: Enhance software error handling and logging for improved usability [\#87](https://github.com/NASA-PDS/pds4-information-model/issues/87)

**Defects:**

- LDDtool combining two consecutive choice blocks into one block  [\#192](https://github.com/NASA-PDS/pds4-information-model/issues/192)
- LDDTool: requires one class with \(element\_flag = true\), even when no classes defined [\#188](https://github.com/NASA-PDS/pds4-information-model/issues/188)

**Closed issues:**

- LDDTool: System exit in code aborts MAVEN build [\#199](https://github.com/NASA-PDS/pds4-information-model/issues/199)
- LDDTool: Deprecation Rule being propogated to schematron file with an error in the assert statement. [\#197](https://github.com/NASA-PDS/pds4-information-model/issues/197)
- LDDTool: MSN namespace declaration is missing from the generated schematron file. [\#194](https://github.com/NASA-PDS/pds4-information-model/issues/194)
- LDDTool: Better handle LDD generation errors through more clear logging [\#86](https://github.com/NASA-PDS/pds4-information-model/issues/86)
- LDDTool: Cleanup output log messages to use consistent formatting and remove unnecessary messages [\#85](https://github.com/NASA-PDS/pds4-information-model/issues/85)

## [v11.3.1](https://github.com/NASA-PDS/pds4-information-model/tree/v11.3.1) (2020-05-22)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/11.4.0-SNAPSHOT...v11.3.1)

## [11.4.0-SNAPSHOT](https://github.com/NASA-PDS/pds4-information-model/tree/11.4.0-SNAPSHOT) (2020-05-22)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v11.3.0...11.4.0-SNAPSHOT)

## [v11.3.0](https://github.com/NASA-PDS/pds4-information-model/tree/v11.3.0) (2020-05-22)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/11.3.0-SNAPSHOT...v11.3.0)

**Improvements:**

- LDDTool: Add PDS Namespace Registry information to Help output [\#172](https://github.com/NASA-PDS/pds4-information-model/issues/172)
- LDDTool: Setup and configure CI/CD and Nightly Builds [\#158](https://github.com/NASA-PDS/pds4-information-model/issues/158)

**Defects:**

- LDDTool does not handle "unbounded" maximum cardinality in Choice blocks correctly [\#180](https://github.com/NASA-PDS/pds4-information-model/issues/180)

**Closed issues:**

- CCB-268 how to test this [\#143](https://github.com/NASA-PDS/pds4-information-model/issues/143)

## [11.3.0-SNAPSHOT](https://github.com/NASA-PDS/pds4-information-model/tree/11.3.0-SNAPSHOT) (2020-04-18)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v11.2.2...11.3.0-SNAPSHOT)

**Closed issues:**

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

**Closed issues:**

- CCB-279: Mis-Matched \<axes\> and Axis\_Array Specifications  [\#152](https://github.com/NASA-PDS/pds4-information-model/issues/152)
- CCB-272: Reinstate Array\_1D in the Information Model [\#148](https://github.com/NASA-PDS/pds4-information-model/issues/148)
- LDDtool: unescaped & in output .xml [\#141](https://github.com/NASA-PDS/pds4-information-model/issues/141)

## [v11.1.0](https://github.com/NASA-PDS/pds4-information-model/tree/v11.1.0) (2020-03-11)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v11.0.0...v11.1.0)

**Closed issues:**

- CCB-274 - Add attribute dictionary\_type to Ingest\_LDD - Update [\#144](https://github.com/NASA-PDS/pds4-information-model/issues/144)

## [v11.0.0](https://github.com/NASA-PDS/pds4-information-model/tree/v11.0.0) (2020-02-29)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v10.1.2...v11.0.0)

**Closed issues:**

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

**Closed issues:**

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

**Closed issues:**

- IMTool: LDDTool Config Properties Update [\#83](https://github.com/NASA-PDS/pds4-information-model/issues/83)
- LDDTool: IngestLDD 'Report' rules do not make it to Schematron [\#1](https://github.com/NASA-PDS/pds4-information-model/issues/1)

## [v10.1.0](https://github.com/NASA-PDS/pds4-information-model/tree/v10.1.0) (2019-10-11)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v10.0.0...v10.1.0)

**Defects:**

- LDDTool: XMLSchema Fix Null In Import Cleanup [\#81](https://github.com/NASA-PDS/pds4-information-model/issues/81)
- LDDTool: XMLSchema Fix Nul In Import File Name [\#79](https://github.com/NASA-PDS/pds4-information-model/issues/79)
- LDDTool: Fixed the calculation of class extensions and restrictions [\#77](https://github.com/NASA-PDS/pds4-information-model/issues/77)
-  CCB-252 Make\_Science\_Facets\_wavelength\_range\_nillable\_BugFix [\#64](https://github.com/NASA-PDS/pds4-information-model/issues/64)

**Closed issues:**

- Revert CCB-256 Permissible value definitions Ingest LDD [\#75](https://github.com/NASA-PDS/pds4-information-model/issues/75)
- CCB-256 Permissible\_value\_definitions\_Ingest\_LDD\_Update\_Data [\#72](https://github.com/NASA-PDS/pds4-information-model/issues/72)
- CCB-256 Permissible\_value\_definitions\_Ingest\_LDD\_Update [\#70](https://github.com/NASA-PDS/pds4-information-model/issues/70)
- DOMConv\_Depcreation\_FinalCleanup\_Deprecate\_InfoModel\_and\_Associated\_Classes [\#68](https://github.com/NASA-PDS/pds4-information-model/issues/68)
- DOMConv\_Deprecation\_FinalCleanup\_Continued [\#66](https://github.com/NASA-PDS/pds4-information-model/issues/66)

## [v10.0.0](https://github.com/NASA-PDS/pds4-information-model/tree/v10.0.0) (2019-09-12)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/f4d66f49a34894ec115bfdfc7370019578b59854...v10.0.0)

**Closed issues:**

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
