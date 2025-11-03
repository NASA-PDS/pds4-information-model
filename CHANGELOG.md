# Changelog

## [«unknown»](https://github.com/NASA-PDS/pds4-information-model/tree/«unknown») (2025-11-03)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v15.3.0...«unknown»)

**Requirements:**

- CCB-62: Transfer\_Manifest class should enforce data types for its two mandatory columns [\#949](https://github.com/NASA-PDS/pds4-information-model/issues/949)
- As a user, I want to override the default namespace version used by LDDTool [\#948](https://github.com/NASA-PDS/pds4-information-model/issues/948)
- CCB-59: funding\_year only allows the value "9999" [\#946](https://github.com/NASA-PDS/pds4-information-model/issues/946)
- CCB-60:  New permissible value for File\_Area\_Observational.Header.parsing\_standard\_id: "CDF 3.9 ISTP/IACG" [\#945](https://github.com/NASA-PDS/pds4-information-model/issues/945)
- As a user, I want to easily identify which IM and LDD version the WebHelp documentation is for [\#939](https://github.com/NASA-PDS/pds4-information-model/issues/939)
- As a data provider, I want to be able to use "Space" as a delimiter in the `Table_Delimited` class [\#673](https://github.com/NASA-PDS/pds4-information-model/issues/673)

**Improvements:**

- Upgrade to Java 17 for compatibility with validate dependency [\#968](https://github.com/NASA-PDS/pds4-information-model/issues/968)
- \[namespace-registry\] add new namespace "prov" [\#958](https://github.com/NASA-PDS/pds4-information-model/issues/958)
- \[namespace-registry\] add new namespace "galileo" [\#956](https://github.com/NASA-PDS/pds4-information-model/issues/956)
- \[namespace-registry\] add new namespace "galileo" [\#953](https://github.com/NASA-PDS/pds4-information-model/issues/953)
- \[namespace-registry\] add new namespace "dscovr" [\#935](https://github.com/NASA-PDS/pds4-information-model/issues/935)

**Defects:**

- PDS4 Instrument class \(Product\_Context\) lists attributes that cannot be used [\#951](https://github.com/NASA-PDS/pds4-information-model/issues/951) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- LDD generator is not generating valid LDDs [\#947](https://github.com/NASA-PDS/pds4-information-model/issues/947) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- Current WebHelp documentation \(1O00/1.24.0.0\) does not reflect latest Rings LDD [\#938](https://github.com/NASA-PDS/pds4-information-model/issues/938) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- Generated LDD documentation does not handle xs:choice right [\#916](https://github.com/NASA-PDS/pds4-information-model/issues/916) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]

**Other closed issues:**

- CCB-28: As a user, I want validate to allow delimited tables with whitespace-only numeric fields [\#967](https://github.com/NASA-PDS/pds4-information-model/issues/967)
- Cleanup the Namespace Registry [\#954](https://github.com/NASA-PDS/pds4-information-model/issues/954)
- Fix C Maintainability Rating on WriteDOM11179DDRDFFile [\#943](https://github.com/NASA-PDS/pds4-information-model/issues/943)
- Fix the SonarQube "Brain Method" finding associated with \#930 [\#937](https://github.com/NASA-PDS/pds4-information-model/issues/937)
- Stage the PDS4 Information Model V 1.25.0.0 \(1P00\) for Build 16.0 [\#933](https://github.com/NASA-PDS/pds4-information-model/issues/933)
- Refactoring that came out of \#924 investigation [\#929](https://github.com/NASA-PDS/pds4-information-model/issues/929)
- Release PDS4 Information Model V 1.24.0.0 \(1O00\) for Build 15.1 [\#918](https://github.com/NASA-PDS/pds4-information-model/issues/918)
- Small Bodies Ontology - Complete 1st draft for distribution [\#780](https://github.com/NASA-PDS/pds4-information-model/issues/780)
- Complete initial Planetary Systems Ontology and context products [\#779](https://github.com/NASA-PDS/pds4-information-model/issues/779)
- Update the writer for the anonymous version of the Data Dictionary [\#654](https://github.com/NASA-PDS/pds4-information-model/issues/654)

## [v15.3.0](https://github.com/NASA-PDS/pds4-information-model/tree/v15.3.0) (2025-04-28)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v15.2.4...v15.3.0)

**Requirements:**

- CCB-13 Ensure file\_name and directory\_path\_name adhere to SR [\#912](https://github.com/NASA-PDS/pds4-information-model/issues/912)
- CCB-55: Funding\_Ackowledgement class enhancement \(funding\_source and funding\_acknowledgement\_text\) [\#910](https://github.com/NASA-PDS/pds4-information-model/issues/910)
- CCB-54: Allow comment for DD\_Class [\#907](https://github.com/NASA-PDS/pds4-information-model/issues/907)
- CCB-43: Repair Incorrectly Formed Enumerated Value for Units\_of\_Gmass [\#891](https://github.com/NASA-PDS/pds4-information-model/issues/891)
- As a user, I want to generate the core PDS4 IM for a specific version of PDS4 [\#860](https://github.com/NASA-PDS/pds4-information-model/issues/860)

**Improvements:**

- \[namespace-registry\] add new JAXA namespaces for BepiColombo MMO instruments [\#906](https://github.com/NASA-PDS/pds4-information-model/issues/906)
- \[namespace-registry\] add new namespace sln, slim, hisaki, and mmx [\#882](https://github.com/NASA-PDS/pds4-information-model/issues/882)
- \[namespace-registry\] add new namespaces for vex, smart1, and giotto [\#876](https://github.com/NASA-PDS/pds4-information-model/issues/876)
- Archive all PDS Policy documents [\#872](https://github.com/NASA-PDS/pds4-information-model/issues/872)
- \[namespace-registry\] add new namespace "ama" [\#868](https://github.com/NASA-PDS/pds4-information-model/issues/868)
- Update command-line arguments to be in line with standard best practices [\#861](https://github.com/NASA-PDS/pds4-information-model/issues/861)

**Defects:**

- Schematron is not throwing a warning when validating Author/Editor Lists [\#898](https://github.com/NASA-PDS/pds4-information-model/issues/898)
- lddtool -V flag outputs invalid schemas for 1D00  [\#885](https://github.com/NASA-PDS/pds4-information-model/issues/885) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- lddtool `-V` flag outputs invalid schemas for 1C00 [\#874](https://github.com/NASA-PDS/pds4-information-model/issues/874) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- LDDTool does not propertly generate the All LDD Data Dictionary [\#867](https://github.com/NASA-PDS/pds4-information-model/issues/867) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- Unable to run \>1 cucumber test with LDDs [\#866](https://github.com/NASA-PDS/pds4-information-model/issues/866) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- `-V` flag does not work when attempting to generate core IM [\#859](https://github.com/NASA-PDS/pds4-information-model/issues/859) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- lddtool json export seems to export the whole IM, not just the current dictionary [\#723](https://github.com/NASA-PDS/pds4-information-model/issues/723)

**Other closed issues:**

- Develop script to update the IM Version for test data [\#899](https://github.com/NASA-PDS/pds4-information-model/issues/899)
- Stage the PDS4 Information Model V 1.24.0.0 \(1O00\) for Build 15.1 [\#879](https://github.com/NASA-PDS/pds4-information-model/issues/879)
- Update cucumber test code to support testing of \#852 [\#856](https://github.com/NASA-PDS/pds4-information-model/issues/856)

## [v15.2.4](https://github.com/NASA-PDS/pds4-information-model/tree/v15.2.4) (2025-01-07)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v15.2.3...v15.2.4)

**Improvements:**

- \[namespace-registry\] add new namespace `LCROSS` [\#845](https://github.com/NASA-PDS/pds4-information-model/issues/845)
- Add `Product_Native` test case for \#795 back to test suite [\#825](https://github.com/NASA-PDS/pds4-information-model/issues/825)

**Defects:**

- Exit codes no longer working as expected [\#857](https://github.com/NASA-PDS/pds4-information-model/issues/857) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- Issue trying to generate schemas 1E00 and 1D00 [\#852](https://github.com/NASA-PDS/pds4-information-model/issues/852) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- lddtool -h returns expected information twice and then error messages  [\#849](https://github.com/NASA-PDS/pds4-information-model/issues/849) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- lddtool -v creates/overwrites output directory and files [\#848](https://github.com/NASA-PDS/pds4-information-model/issues/848) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- Download links broken for lddtool for online documentation  [\#846](https://github.com/NASA-PDS/pds4-information-model/issues/846) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]

**Other closed issues:**

- Migrate initial prototype from Jupyter to a new Github repo [\#389](https://github.com/NASA-PDS/pds4-information-model/issues/389)

## [v15.2.3](https://github.com/NASA-PDS/pds4-information-model/tree/v15.2.3) (2024-11-20)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v15.2.2...v15.2.3)

**Requirements:**

- CCB-27: DOI requires at least an author or editor [\#823](https://github.com/NASA-PDS/pds4-information-model/issues/823)

## [v15.2.2](https://github.com/NASA-PDS/pds4-information-model/tree/v15.2.2) (2024-11-18)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v15.1.0...v15.2.2)

**Requirements:**

- CCB-19: Never finished CCB-325 documentation updates \(Title: Support for video and audio as product observational\) [\#839](https://github.com/NASA-PDS/pds4-information-model/issues/839)
- CCB-10: Schematron/Schema Version Conflict \(PDS-JIRA-366\) [\#838](https://github.com/NASA-PDS/pds4-information-model/issues/838)
- CCB-42: Add Array\_1D\_Spectrum to File\_Area\_Ancillary, File\_Area\_Browse, and File\_Area\_Observational\_Supplemental [\#831](https://github.com/NASA-PDS/pds4-information-model/issues/831)
- CCB-47: Add permissible value W/cm\*\*2/sr/μm to Units\_of\_Spectral\_Radiance [\#830](https://github.com/NASA-PDS/pds4-information-model/issues/830)
- CCB-38: Added mrad/pixel to Units\_of\_Pixel\_Resolution\_Angular [\#829](https://github.com/NASA-PDS/pds4-information-model/issues/829)
- CCB-24: Replaced all of the unit\_id standard value definitions [\#824](https://github.com/NASA-PDS/pds4-information-model/issues/824)
- CCB-29: Incorrect schematron rules for Bundle \(type:External\), Collection \(type:External\), and Product\_External logical\_identifiers [\#816](https://github.com/NASA-PDS/pds4-information-model/issues/816)
- CCB-7: Missing schematron rule - bundle\_to\_targets [\#795](https://github.com/NASA-PDS/pds4-information-model/issues/795)
- CCB-26: Fix definition of Units\_of\_Amount\_of\_Substance [\#782](https://github.com/NASA-PDS/pds4-information-model/issues/782)
- CCB-16: Allow micro amps and nano amps as units of current [\#753](https://github.com/NASA-PDS/pds4-information-model/issues/753)

**Improvements:**

- \[namespace-registry\] add new namespace juice [\#803](https://github.com/NASA-PDS/pds4-information-model/issues/803)
- \[namespace-registry\] add new namespace `vikinglander` [\#794](https://github.com/NASA-PDS/pds4-information-model/issues/794)

**Defects:**

- emrsp namespaces are configured to be produced as https: [\#834](https://github.com/NASA-PDS/pds4-information-model/issues/834) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- Tests fail when there are multiple tests with multiple schemas/schematrons [\#822](https://github.com/NASA-PDS/pds4-information-model/issues/822) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- Regression in schematron improvements for references \(\#795\) [\#819](https://github.com/NASA-PDS/pds4-information-model/issues/819) [[s.critical](https://github.com/NASA-PDS/pds4-information-model/labels/s.critical)]
- LDDTool does not resolve the JUICE namespaces with IM 1J00 [\#818](https://github.com/NASA-PDS/pds4-information-model/issues/818) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- Lack of object initialization in the code leads to infinite when trying to run main more than once [\#801](https://github.com/NASA-PDS/pds4-information-model/issues/801) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- GEOM LDD schematron rules are not executing as expected `<kernel_type>` validation [\#797](https://github.com/NASA-PDS/pds4-information-model/issues/797) [[s.critical](https://github.com/NASA-PDS/pds4-information-model/labels/s.critical)]
- Unable to build LDDs for 1E00 [\#776](https://github.com/NASA-PDS/pds4-information-model/issues/776) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]

**Other closed issues:**

- Add validate testing to support automated integration testing [\#808](https://github.com/NASA-PDS/pds4-information-model/issues/808)
- Create new LDDTool Cucumber scenario to support diffs to expected outputs [\#787](https://github.com/NASA-PDS/pds4-information-model/issues/787)
- Add current LDDTool tests to existing Cucumber Framework [\#786](https://github.com/NASA-PDS/pds4-information-model/issues/786)
- CCB-18: Complete JIRA-CCB-325 lien implementation for Browse and Ancillary products [\#784](https://github.com/NASA-PDS/pds4-information-model/issues/784)
- Identify key concepts we want to capture for each ontology object [\#778](https://github.com/NASA-PDS/pds4-information-model/issues/778)
- Rings Ontology: Complete 1st draft for distribution [\#774](https://github.com/NASA-PDS/pds4-information-model/issues/774)
- Stage the PDS4 Information Model V 1.23.0.0 \(1N00\) for Build 15.0 [\#772](https://github.com/NASA-PDS/pds4-information-model/issues/772)
- Generate WebHelp for all Discipline and Mission LDDs [\#759](https://github.com/NASA-PDS/pds4-information-model/issues/759)
- Initial Implementation of Cucumber Framework for Information Model / SCRs [\#715](https://github.com/NASA-PDS/pds4-information-model/issues/715)
- Initial Implementation of Cucumber Framework for LDDTool Tests [\#714](https://github.com/NASA-PDS/pds4-information-model/issues/714)

## [v15.1.0](https://github.com/NASA-PDS/pds4-information-model/tree/v15.1.0) (2024-05-24)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v15.0.7...v15.1.0)

**Defects:**

- LDDTool does not include all rules from IngestLDD file [\#771](https://github.com/NASA-PDS/pds4-information-model/issues/771) [[s.critical](https://github.com/NASA-PDS/pds4-information-model/labels/s.critical)]

## [v15.0.7](https://github.com/NASA-PDS/pds4-information-model/tree/v15.0.7) (2024-05-22)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v15.0.6...v15.0.7)

**Defects:**

- ERROR 11179 data dictionary class is missing for overwrite [\#770](https://github.com/NASA-PDS/pds4-information-model/issues/770) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]

**Other closed issues:**

- Remove Deprecated Objects Array [\#768](https://github.com/NASA-PDS/pds4-information-model/issues/768)
- Remove the MDPTNConfigClassDisp file  [\#766](https://github.com/NASA-PDS/pds4-information-model/issues/766)

## [v15.0.6](https://github.com/NASA-PDS/pds4-information-model/tree/v15.0.6) (2024-04-26)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v15.0.5...v15.0.6)

**Requirements:**

- CCB-15: Update Constraints on Author/Editor Lists \(JIRA-CCB-356\) [\#752](https://github.com/NASA-PDS/pds4-information-model/issues/752)

**Defects:**

- Version flag no longer works on Windows [\#763](https://github.com/NASA-PDS/pds4-information-model/issues/763)
- Documentation broken with latest upgrade for maven site plugin [\#761](https://github.com/NASA-PDS/pds4-information-model/issues/761) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]

## [v15.0.5](https://github.com/NASA-PDS/pds4-information-model/tree/v15.0.5) (2024-04-17)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v15.0.4...v15.0.5)

**Defects:**

- webpage links are not working [\#754](https://github.com/NASA-PDS/pds4-information-model/issues/754) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]

## [v15.0.4](https://github.com/NASA-PDS/pds4-information-model/tree/v15.0.4) (2024-04-16)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v15.0.2...v15.0.4)

**Improvements:**

- \[namespace-registry\] add new namespace VIPER [\#748](https://github.com/NASA-PDS/pds4-information-model/issues/748)

## [v15.0.2](https://github.com/NASA-PDS/pds4-information-model/tree/v15.0.2) (2024-04-10)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.3.0...v15.0.2)

**Requirements:**

- Add KARI as a new agency `urn:kari:kpds` [\#709](https://github.com/NASA-PDS/pds4-information-model/issues/709)
- CCB-357: Create Product\_External - LID Schematron Rule Update [\#698](https://github.com/NASA-PDS/pds4-information-model/issues/698)

**Improvements:**

- Add LDDTool Execution Smoke Test to Branch Testing and Unstable testing [\#734](https://github.com/NASA-PDS/pds4-information-model/issues/734)
- \[namespace-registry\]update namespace URL for KPDS [\#716](https://github.com/NASA-PDS/pds4-information-model/issues/716)
- \[vg2\] add new namespace "\<vg2\>" [\#696](https://github.com/NASA-PDS/pds4-information-model/issues/696)
- \[vg1\] add new namespace "\<vg1\>" [\#695](https://github.com/NASA-PDS/pds4-information-model/issues/695)

**Defects:**

- Unable to build maven site after JDK17 upgrade [\#750](https://github.com/NASA-PDS/pds4-information-model/issues/750) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- JDK17 issue re-introduced bug per \#733 [\#746](https://github.com/NASA-PDS/pds4-information-model/issues/746) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- LDDTool producing invalid XSD for Voyager LDD [\#744](https://github.com/NASA-PDS/pds4-information-model/issues/744) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- LDDTool is not creating valid URL for KPLO [\#738](https://github.com/NASA-PDS/pds4-information-model/issues/738) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- LDDTool is throwing ERROR SetMasterAttrXMLBaseDataTypeFromDataType - Data Type is missing -  [\#733](https://github.com/NASA-PDS/pds4-information-model/issues/733) [[s.critical](https://github.com/NASA-PDS/pds4-information-model/labels/s.critical)]
- PDS4 IM repo fails with openjdk 17+ [\#471](https://github.com/NASA-PDS/pds4-information-model/issues/471) [[s.low](https://github.com/NASA-PDS/pds4-information-model/labels/s.low)]

**Other closed issues:**

- Stage the PDS4 Information Model V 1.22.0.0 \(1.M.0.0\) for Build 14.1 [\#726](https://github.com/NASA-PDS/pds4-information-model/issues/726)
- IMTool Refactoring - Deprecate the Java "Deprecated Object Array" [\#724](https://github.com/NASA-PDS/pds4-information-model/issues/724)
- Set Product\_XML\_Schema label version id to 1.0 for all future releases. [\#719](https://github.com/NASA-PDS/pds4-information-model/issues/719)
- IMTool Refactoring Phase 6 - Use Protege dd11179.pins for class disposition [\#717](https://github.com/NASA-PDS/pds4-information-model/issues/717)
- Update to Support for OpenJDK17 [\#702](https://github.com/NASA-PDS/pds4-information-model/issues/702)
- Fix code scanning alert - Container contents are never accessed [\#693](https://github.com/NASA-PDS/pds4-information-model/issues/693)
- Develop first iteration of provenance model [\#692](https://github.com/NASA-PDS/pds4-information-model/issues/692)

## [v14.3.0](https://github.com/NASA-PDS/pds4-information-model/tree/v14.3.0) (2023-09-18)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.2.4...v14.3.0)

**Requirements:**

- CCB-336 Add a License\_Information class to the Identification\_Area [\#679](https://github.com/NASA-PDS/pds4-information-model/issues/679)

**Improvements:**

- Verify LDDTool Processing of the Discipline LDDs matches outputs previous releases [\#686](https://github.com/NASA-PDS/pds4-information-model/issues/686)
- Adds to default output JSON TermMap [\#670](https://github.com/NASA-PDS/pds4-information-model/issues/670)

**Defects:**

- lddtool does not generate the correct closing tag for rule\_type = Report [\#667](https://github.com/NASA-PDS/pds4-information-model/issues/667) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]

**Other closed issues:**

- Issue \#426 add new namespace "vco" [\#690](https://github.com/NASA-PDS/pds4-information-model/issues/690)
- Sonatype - Suggested Modifications - WriteDOMDDJSONFileLib - 230810 [\#687](https://github.com/NASA-PDS/pds4-information-model/issues/687)
- B14.0 Release of the PDS4 Information Model and LDDTool for System Integration and Test [\#684](https://github.com/NASA-PDS/pds4-information-model/issues/684)
- \[namespace-registry\] add new namespace "vco" [\#683](https://github.com/NASA-PDS/pds4-information-model/issues/683)
- Add a License\_Information class to the Identification\_Area Redux Update [\#681](https://github.com/NASA-PDS/pds4-information-model/issues/681)
- IMTool Refactoring - Phase 3 - Cleanup - update prior versions of the IM's ontology files [\#676](https://github.com/NASA-PDS/pds4-information-model/issues/676)
- B14.0 Prep IM and LDDTool for new IM Version [\#666](https://github.com/NASA-PDS/pds4-information-model/issues/666)
- \[namespace-registry\] add new namespace "mgn" [\#664](https://github.com/NASA-PDS/pds4-information-model/issues/664)
- Implement Term Mapping Example for EPNCore Attributes [\#545](https://github.com/NASA-PDS/pds4-information-model/issues/545)

## [v14.2.4](https://github.com/NASA-PDS/pds4-information-model/tree/v14.2.4) (2023-05-08)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.2.3...v14.2.4)

**Defects:**

- LDDTool fails for dependent LDDs since v14.2.0 [\#661](https://github.com/NASA-PDS/pds4-information-model/issues/661) [[s.critical](https://github.com/NASA-PDS/pds4-information-model/labels/s.critical)]

## [v14.2.3](https://github.com/NASA-PDS/pds4-information-model/tree/v14.2.3) (2023-05-03)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.2.2...v14.2.3)

**Defects:**

- Bug trying to generate LDD and missing files [\#658](https://github.com/NASA-PDS/pds4-information-model/issues/658) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]

## [v14.2.2](https://github.com/NASA-PDS/pds4-information-model/tree/v14.2.2) (2023-04-19)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.1.3...v14.2.2)

## [v14.1.3](https://github.com/NASA-PDS/pds4-information-model/tree/v14.1.3) (2023-04-12)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.2.1...v14.1.3)

**Defects:**

- Invalid `modification_date` for LDDs for versions 1H00 and earlier [\#650](https://github.com/NASA-PDS/pds4-information-model/issues/650) [[s.critical](https://github.com/NASA-PDS/pds4-information-model/labels/s.critical)]
- Revert date format change from \#641 in Product\_XML\_Schema [\#648](https://github.com/NASA-PDS/pds4-information-model/issues/648) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]

## [v14.2.1](https://github.com/NASA-PDS/pds4-information-model/tree/v14.2.1) (2023-04-05)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.2.0...v14.2.1)

## [v14.2.0](https://github.com/NASA-PDS/pds4-information-model/tree/v14.2.0) (2023-03-30)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.1.2...v14.2.0)

**Requirements:**

- As a user I want to export the IM in the RDF/OWL format in RDF format [\#620](https://github.com/NASA-PDS/pds4-information-model/issues/620)
- CCB-325: Support for video and audio as product observational [\#616](https://github.com/NASA-PDS/pds4-information-model/issues/616)
- CCB-357: Create Product\_External [\#614](https://github.com/NASA-PDS/pds4-information-model/issues/614)
- CCB-355: Add Funding\_Acknowledgement class to Citation\_Information Class [\#611](https://github.com/NASA-PDS/pds4-information-model/issues/611)
- CCB-354: Add new permissible values to Header/parsing\_standard\_id to support new versions of CDF [\#608](https://github.com/NASA-PDS/pds4-information-model/issues/608)
- CCB-362: Add a permissible value of nm/mm to Units\_of\_Misc [\#603](https://github.com/NASA-PDS/pds4-information-model/issues/603)
- CCB-361: Add a permissible value for microradians to Units\_of\_Angle [\#602](https://github.com/NASA-PDS/pds4-information-model/issues/602)
- CCB-360: Add new permissible value to Units\_of\_Misc [\#599](https://github.com/NASA-PDS/pds4-information-model/issues/599)
- CCB-350: Adopt more rigorous / stringent rules for leap seconds and use of "24:00:00" in datetimes [\#502](https://github.com/NASA-PDS/pds4-information-model/issues/502)
- CCB-348: Add Units\_of\_Mass\_Density as a unit of measure [\#499](https://github.com/NASA-PDS/pds4-information-model/issues/499)
- As a user, I want to describe a relationship between an attribute in the model and the column of a table [\#473](https://github.com/NASA-PDS/pds4-information-model/issues/473)
- As a developer I want to have the PDS4 Information Model expressed in the RDF/OWL/TTL format.  [\#458](https://github.com/NASA-PDS/pds4-information-model/issues/458)
- CCB-260: PDS4 label files do not have a unique file extension [\#119](https://github.com/NASA-PDS/pds4-information-model/issues/119)

**Improvements:**

- Update date format to be timezone aware [\#641](https://github.com/NASA-PDS/pds4-information-model/issues/641)

**Defects:**

- Repository for Released IM distribution package needs reorganization [\#636](https://github.com/NASA-PDS/pds4-information-model/issues/636)
- Maven did not allow the addition of new folders under pds4-information-model/docs [\#630](https://github.com/NASA-PDS/pds4-information-model/issues/630)
- Information Model does not have Product\_External included in Identification\_Area.product\_class [\#618](https://github.com/NASA-PDS/pds4-information-model/issues/618) [[s.critical](https://github.com/NASA-PDS/pds4-information-model/labels/s.critical)]
- LDDTool does not write a DocBook file for the Common Dictionary [\#606](https://github.com/NASA-PDS/pds4-information-model/issues/606) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- LDDTool is not including permissible values from certain Type\_Lists in the DD DocBook file [\#580](https://github.com/NASA-PDS/pds4-information-model/issues/580) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]

**Other closed issues:**

- LDDTool to generate one DocBook file per LDD - Custom Modifications [\#645](https://github.com/NASA-PDS/pds4-information-model/issues/645)
- \[namespace-registry\] add new namespace "clps" [\#640](https://github.com/NASA-PDS/pds4-information-model/issues/640)
- \[namespace-registry\] add new namespace "psyche" [\#638](https://github.com/NASA-PDS/pds4-information-model/issues/638)
- Release of the PDS4 Information Model Version 1.20.0.0 \(1K00\) [\#624](https://github.com/NASA-PDS/pds4-information-model/issues/624)
- Review and update code per Sonatype inputs on PR \#621 [\#622](https://github.com/NASA-PDS/pds4-information-model/issues/622)
- Develop initial target ontology for planetary systems [\#612](https://github.com/NASA-PDS/pds4-information-model/issues/612)
- Stage the PDS4 Information Model Version 1.20.0.0 \(1.K.0.0\) for Update [\#596](https://github.com/NASA-PDS/pds4-information-model/issues/596)
- \[namespace-registry\] add new namespace "lucy" [\#575](https://github.com/NASA-PDS/pds4-information-model/issues/575)
- \[namespace-registry\] add new namespace "msl" [\#566](https://github.com/NASA-PDS/pds4-information-model/issues/566)
- Issue \#552 : \[namespace-registry\] add new namespace "sb" [\#563](https://github.com/NASA-PDS/pds4-information-model/issues/563)
- \[namespace-registry\] add new namespace "sbn" [\#552](https://github.com/NASA-PDS/pds4-information-model/issues/552)
- Add flags to support generation of TTL/OWL files [\#467](https://github.com/NASA-PDS/pds4-information-model/issues/467)
- Testing CCB-268 [\#143](https://github.com/NASA-PDS/pds4-information-model/issues/143)

## [v14.1.2](https://github.com/NASA-PDS/pds4-information-model/tree/v14.1.2) (2022-10-19)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.1.1...v14.1.2)

**Defects:**

- LDDTool does not support IM version 1J00 [\#512](https://github.com/NASA-PDS/pds4-information-model/issues/512) [[s.critical](https://github.com/NASA-PDS/pds4-information-model/labels/s.critical)]

## [v14.1.1](https://github.com/NASA-PDS/pds4-information-model/tree/v14.1.1) (2022-10-03)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.1.0...v14.1.1)

**Defects:**

- Bug generating BepiColombo SERENA dictionary [\#507](https://github.com/NASA-PDS/pds4-information-model/issues/507) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- hyb2 has incorrect namespace for IM v1.14 [\#506](https://github.com/NASA-PDS/pds4-information-model/issues/506) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]

**Other closed issues:**

- \[namespace-registry\] add new namespace "mro"  [\#505](https://github.com/NASA-PDS/pds4-information-model/issues/505)

## [v14.1.0](https://github.com/NASA-PDS/pds4-information-model/tree/v14.1.0) (2022-09-21)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.13...v14.1.0)

**Requirements:**

- CCB-351: Term Mapping [\#503](https://github.com/NASA-PDS/pds4-information-model/issues/503)

**Defects:**

- LDDTool is generating multiple Schematron rules to test the same condition [\#468](https://github.com/NASA-PDS/pds4-information-model/issues/468) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]

**Other closed issues:**

- \[namespace-registry\] add new namespace "apollo" [\#495](https://github.com/NASA-PDS/pds4-information-model/issues/495)
- Stage the PDS4 Information Model Version 1.19.0.0 \(1.J.0.0\) for Update [\#493](https://github.com/NASA-PDS/pds4-information-model/issues/493)
- \[namespace-registry\] add new namespace "iras" [\#490](https://github.com/NASA-PDS/pds4-information-model/issues/490)
- CCB-209: Correct Definitions of Tagged\_\*\_Object [\#159](https://github.com/NASA-PDS/pds4-information-model/issues/159)

## [v14.0.13](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.13) (2022-06-21)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.12...v14.0.13)

## [v14.0.12](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.12) (2022-06-21)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.11...v14.0.12)

## [v14.0.11](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.11) (2022-06-17)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.10...v14.0.11)

## [v14.0.10](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.10) (2022-06-17)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.9...v14.0.10)

## [v14.0.9](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.9) (2022-06-17)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.7...v14.0.9)

## [v14.0.7](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.7) (2022-06-17)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.5...v14.0.7)

## [v14.0.5](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.5) (2022-06-17)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.4...v14.0.5)

## [v14.0.4](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.4) (2022-06-17)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.3...v14.0.4)

## [v14.0.3](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.3) (2022-06-17)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.2...v14.0.3)

## [v14.0.2](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.2) (2022-06-09)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.1...v14.0.2)

**Defects:**

- Data Provider’s Handbook v1.18.0 is missing the Appendix F text requested in the CCB-340 [\#480](https://github.com/NASA-PDS/pds4-information-model/issues/480) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- LDDTool is not combining Schematron Contexts correctly when adding to an auto-generated context [\#459](https://github.com/NASA-PDS/pds4-information-model/issues/459) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]

**Other closed issues:**

- Re-generate IM documents for release with reverted changes [\#489](https://github.com/NASA-PDS/pds4-information-model/issues/489)
- \[namespace-registry\] add new namespace "ody" [\#485](https://github.com/NASA-PDS/pds4-information-model/issues/485)
- \[namespace-registry\] add new namespace "near" [\#484](https://github.com/NASA-PDS/pds4-information-model/issues/484)
- \[namespace-registry\] add new namespace "hst" [\#482](https://github.com/NASA-PDS/pds4-information-model/issues/482)
- \[namespace-registry\] add "vgr" [\#476](https://github.com/NASA-PDS/pds4-information-model/issues/476)
- \[namespace-registry\] add existing vg1 and vg2 [\#474](https://github.com/NASA-PDS/pds4-information-model/issues/474)
- Update code to develop the TTL/OWL per user request for updates [\#466](https://github.com/NASA-PDS/pds4-information-model/issues/466)

## [v14.0.1](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.1) (2022-04-18)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v14.0.0...v14.0.1)

**Improvements:**

- \[namespace-registry\] move namespace "wave" [\#460](https://github.com/NASA-PDS/pds4-information-model/issues/460)

**Defects:**

- LDDTool 14 is not generating against past versions of the IM correctly [\#463](https://github.com/NASA-PDS/pds4-information-model/issues/463) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]

## [v14.0.0](https://github.com/NASA-PDS/pds4-information-model/tree/v14.0.0) (2022-04-13)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v13.1.0...v14.0.0)

**Requirements:**

- CCB-335: Inventory Specification Allows Too Many Delimiters [\#453](https://github.com/NASA-PDS/pds4-information-model/issues/453)
- CCB-340 : \<Local\_Internal\_Reference\>.\<local\_identifier\_reference\> cardinality [\#450](https://github.com/NASA-PDS/pds4-information-model/issues/450)
- CCB-343: Revise Product\_Metadata\_Supplemental [\#448](https://github.com/NASA-PDS/pds4-information-model/issues/448)
- CCB-344:  Add data\_to\_partially\_processed\_product to reference types for Internal\_Reference [\#446](https://github.com/NASA-PDS/pds4-information-model/issues/446)
- CCB-339: add Units\_of\_Power with SI watts as option [\#443](https://github.com/NASA-PDS/pds4-information-model/issues/443)
- CCB-337 - Change data type Modification\_Detail/modification\_date to ASCII\_Date\_Time\_YMD [\#436](https://github.com/NASA-PDS/pds4-information-model/issues/436)
- As an operator, I want to be able to convert from XML to RDF using the PDS4 Information Model [\#387](https://github.com/NASA-PDS/pds4-information-model/issues/387)
- As a user, I want to specify an IngestLDD using a relative path [\#341](https://github.com/NASA-PDS/pds4-information-model/issues/341)

**Improvements:**

- \[namespace-registry\] add new namespace "nh" [\#452](https://github.com/NASA-PDS/pds4-information-model/issues/452)
- XML Schema files generated by LDDTool should not write the source file names in the header [\#444](https://github.com/NASA-PDS/pds4-information-model/issues/444)
- remaining code cleanup / refactoring [\#440](https://github.com/NASA-PDS/pds4-information-model/issues/440)
- \[namespace-registry\] add new namespace "lt" [\#435](https://github.com/NASA-PDS/pds4-information-model/issues/435)
- IMTool Refactoring - Phase 2 - Move deprecated values to Protege ontology file [\#428](https://github.com/NASA-PDS/pds4-information-model/issues/428)
- \[namespace-registry\] add new namespace "\<neas\>" [\#421](https://github.com/NASA-PDS/pds4-information-model/issues/421)
- \[namespace-registry\] add new namespace "radar" [\#409](https://github.com/NASA-PDS/pds4-information-model/issues/409)
- Tag ops classes/attributes in IM so they are easily identifiable by users [\#281](https://github.com/NASA-PDS/pds4-information-model/issues/281)
- Refactor Class information from config to Data Dictionary Protege ontology [\#239](https://github.com/NASA-PDS/pds4-information-model/issues/239)

**Defects:**

- PDS4\_PDS\_1I00.sch has two bugs regarding CCB-339 [\#455](https://github.com/NASA-PDS/pds4-information-model/issues/455) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- Missing JSON LDDs \(alt & particle\) [\#424](https://github.com/NASA-PDS/pds4-information-model/issues/424) [[s.low](https://github.com/NASA-PDS/pds4-information-model/labels/s.low)]
- Inconsistent naming of JSON schema files [\#418](https://github.com/NASA-PDS/pds4-information-model/issues/418) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- LDDTool WARNING Header - New namespace id has been specified:ml [\#416](https://github.com/NASA-PDS/pds4-information-model/issues/416)
- PDS4 Information Model Release Notes have errors.  [\#412](https://github.com/NASA-PDS/pds4-information-model/issues/412)
- LDDTool documentation inconsistency [\#375](https://github.com/NASA-PDS/pds4-information-model/issues/375) [[s.low](https://github.com/NASA-PDS/pds4-information-model/labels/s.low)]
- Contradictory DISP rule assertions exist in the PDS4 IM from legacy ingestion [\#351](https://github.com/NASA-PDS/pds4-information-model/issues/351) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- The PDS4 IM Specification Document is not consistent in the ordering of Permissible Values [\#269](https://github.com/NASA-PDS/pds4-information-model/issues/269) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]
- LDDTool: DocBook generation does not work from any file system location [\#170](https://github.com/NASA-PDS/pds4-information-model/issues/170) [[s.medium](https://github.com/NASA-PDS/pds4-information-model/labels/s.medium)]

**Other closed issues:**

- Stage IMTool for V1.18.0.0 [\#431](https://github.com/NASA-PDS/pds4-information-model/issues/431)

## [v13.1.0](https://github.com/NASA-PDS/pds4-information-model/tree/v13.1.0) (2021-10-05)

[Full Changelog](https://github.com/NASA-PDS/pds4-information-model/compare/v13.0.2...v13.1.0)

**Requirements:**

- CCB-329 - Broaden Definition of Attribute aperture [\#403](https://github.com/NASA-PDS/pds4-information-model/issues/403)
- As a developer I want to generate a query model that relates semantically similar permissible values. [\#361](https://github.com/NASA-PDS/pds4-information-model/issues/361)
- As a user I want to know the output of the tool after it completes execution. [\#330](https://github.com/NASA-PDS/pds4-information-model/issues/330)
- An LDD developer desires to inherit a class/attribute defined using DD\_Associate\_External\_Class [\#324](https://github.com/NASA-PDS/pds4-information-model/issues/324)

**Improvements:**

- \[namespace-registry\] add new namespace "kplo" [\#406](https://github.com/NASA-PDS/pds4-information-model/issues/406)
- \[namespace-registry\] add new namespace "\<kpds\>" [\#402](https://github.com/NASA-PDS/pds4-information-model/issues/402)
- \[namespace-registry\] add new namespace "dart" [\#395](https://github.com/NASA-PDS/pds4-information-model/issues/395)
- \[namespace-registry\] add new namespace "kpds" [\#390](https://github.com/NASA-PDS/pds4-information-model/issues/390)
- \[namespace-registry\] add new namespace "Earth-Based Telescope Namespace" [\#386](https://github.com/NASA-PDS/pds4-information-model/issues/386)
- \[namespace-registry\] add new namespace "clipper" [\#374](https://github.com/NASA-PDS/pds4-information-model/issues/374)
- \[namespace-registry\] update path "\<clementine\>" in registry PDF \(possibly shorten name\) [\#373](https://github.com/NASA-PDS/pds4-information-model/issues/373)
- Evolution of CCB-256: Need method for providing permissible value definitions for external namespaces in Ingest\_LDD. [\#369](https://github.com/NASA-PDS/pds4-information-model/issues/369)
- \[namespace-registry\] add new namespace "ml" [\#353](https://github.com/NASA-PDS/pds4-information-model/issues/353)
- \[namespace-registry\] add new namespace "m2020" [\#352](https://github.com/NASA-PDS/pds4-information-model/issues/352)
- \[namespace-registry\] add new namespace "ctli" [\#344](https://github.com/NASA-PDS/pds4-information-model/issues/344)
- \[namespace-registry\] add new namespace "\<clementine\>" [\#332](https://github.com/NASA-PDS/pds4-information-model/issues/332)
- Improve argument handling using argument parsing library [\#240](https://github.com/NASA-PDS/pds4-information-model/issues/240)

**Defects:**

- LDDTool does not resolve refererences across files for a partitioned namespace. [\#398](https://github.com/NASA-PDS/pds4-information-model/issues/398)
- Issue with DD\_Associate\_External\_Class when trying to reference pds:Internal\_Reference and pds:Local\_Internal\_Reference [\#391](https://github.com/NASA-PDS/pds4-information-model/issues/391) [[s.high](https://github.com/NASA-PDS/pds4-information-model/labels/s.high)]
- IMTool/LDDTool still attempts to process dLDD ingested into Protege [\#378](https://github.com/NASA-PDS/pds4-information-model/issues/378)
- The Version Id of the Product\_XML\_Schema label is not being set to the proper value. [\#371](https://github.com/NASA-PDS/pds4-information-model/issues/371)
- LDDTool does not generate valid xpath for schematron rules for classes defined using associate\_external\_class [\#367](https://github.com/NASA-PDS/pds4-information-model/issues/367)
- lddtool's doc directory is not in tgz file [\#346](https://github.com/NASA-PDS/pds4-information-model/issues/346) [[s.low](https://github.com/NASA-PDS/pds4-information-model/labels/s.low)]
- LDDTool generates a 1C00 file when -V 1B00 is specified [\#322](https://github.com/NASA-PDS/pds4-information-model/issues/322)
- LDDTool does not generate the correct namespace for PSA dictionaries [\#301](https://github.com/NASA-PDS/pds4-information-model/issues/301)

**Other closed issues:**

- Develop initial prototype in AWS Neptune Notebooks [\#388](https://github.com/NASA-PDS/pds4-information-model/issues/388)
- Stage the IMTool system for V1.17.0.0 [\#384](https://github.com/NASA-PDS/pds4-information-model/issues/384)
- Deploy new Amazon Neptune instance for PDS4 IM prototype [\#359](https://github.com/NASA-PDS/pds4-information-model/issues/359)
- Replicate diagram from PDS4 IM Spec [\#358](https://github.com/NASA-PDS/pds4-information-model/issues/358)
- Ingest PDS4 IM into BlazeGraph [\#357](https://github.com/NASA-PDS/pds4-information-model/issues/357)
- CCB-283: Add reference\_type value document\_to\_data [\#164](https://github.com/NASA-PDS/pds4-information-model/issues/164)

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
