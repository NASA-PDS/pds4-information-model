# Contribute

## Adding New Test Cases

### Pre-requisites
* Eclipse installed (On Mac, `brew cask install eclipse-java`)
* maven installed (On Mac, `brew install maven`)
* Cloned [pds4-information-model repo](https://github.com/NASA-PDS/pds4-information-model/)

### Overview
* The PDS4 IM repo utilized [Cucumber Testing Framework](https://cucumber.io/) in order to automate testing.
* To simplify the use of this framework, all tests are put into [1 common scenario where validation is performed](https://github.com/NASA-PDS/pds4-information-model/blob/main/model-lddtool/src/test/resources/features/validate.feature#L3) on test products to ensure they meet the expectation of the new requirement.
* All test data goes under [model-lddtool/src/test/resource](https://github.com/NASA-PDS/pds4-information-model/tree/main/model-lddtool/src/test/resources)
* All test cases will go in the [validate.feature file]([url](https://github.com/NASA-PDS/pds4-information-model/blob/main/model-lddtool/src/test/resources/features/validate.feature))
* The automated testing performs the following steps:
   1. Automatically generate PDS Core schemas and schematrons based upon the latest Information Model (`lddtool -p`)
   2. Automatically generate any applicable LDDs needed to perform the testing
   3. Automatically validate the test files to ensure they are either successful or fail with some specific number of errors

### Tutorial
For this tutorial, we are using test data from [github753.tar.gz](https://github.com/user-attachments/files/17998198/github753.tar.gz). This is a duplicate of the data at https://github.com/NASA-PDS/pds4-information-model/tree/main/model-lddtool/src/test/resources/github753, but provides a good example for creating a new test case.

* Create new directory with the test data like `model-lddtool/src/test/resources/github753b`. NOTE: This test data should include, at minimum, the applicable XML label(s), some "data" file associated this this label (it can be 0 bytes), and any necessary IngestLDDs for applicable discipline or mission dictionaries.
* Create new "test suite" for this tutorial by copy pasting these lines to the bottom of the file and renaming `@v15.2.x` to `@my_tutorial`:
```
    @my_tutorial
    Examples: 
| testId | testName | testDir     | messageCount | messageText         | problemEnum        | commandArgs                                             | ingestLDDFileName         |
```
* Add new line(s) for test cases to `src/test/resources/features/validate.feature`: (NOTE: This feature file is organized by minor versions, 15.2.x, 15.3.x, etc. based upon the next expected minor version. If the next minor version is not included create a new section of the document. Contact Jordan for more details if needed.)
* 
  * **testId** - this should be the GitHub reference to the applicable Github ticket. For this use case, that should be set to `NASA-PDS/pds4-information-model#753`
  * **testName** - title for the test that would be useful from someone trying to review a test plan with that title.  For this use case, that should be set to `My test for new Current units nA, microA`
  * **testDir** - directory under `odel-lddtool/src/test/resources/` where your test data exists.  For this use case, that should be set to `github753b`
  * **messageCount** - number of expected errors or warnings. For this use case, that should be set to `0` (success).
  * **messageText** - the expected output should match very closely to messageCount, e.g. `4 errors expected`, `8 warnings expected` (not sure how this is really used or why we have this right now...). For this use case, that should be set to `0 errors expected` (success).
  * **problemEnum** - `totalErrors`, `totalWarnings`, or specific error you expect from validate output. For this use case, that should be set to `SCHEMATRON_ERROR`
    * for specific error, search [Validate ProblemTypes](https://github.com/NASA-PDS/validate/blob/main/src/main/java/gov/nasa/pds/tools/validate/ProblemType.java) for the error identifier, and put the ALL CAPS value in the file, e.g. for `error.label.schematron`, if I search the file I see this line `SCHEMATRON_ERROR("error.label.schematron")`, so the problemEnum value == `SCHEMATRON_ERROR`
  * **commandArgs** - these are the Validate Tool command-line arguments. For this example, let's set the target directory `--target {resourceDir}/github753/pc__d139.xml`. By default, the following are already included for validate under the hood of the test code: 
    * `--disable-context-mismatch-warnings`
    * `--report-style json`
    * `--skip-content-validation`
    * `--report-file {reportDir}/...`
  * **ingestLDDFileName** - the IngestLDD filenames you would like to have LDDTool generate the schema/schmematron for testing. These files should be in the test directory created earlier.
* Now run the test(s):
  * Via Eclipse: right-click `Run as` -> `Cucumber Feature`
  * Via Maven (replace with applicable tag for this build):
    * bash: `mvn clean test -Dcucumber.filter.tags='@my_tutorial'`
    * tcsh: `mvn clean test  -Dcucumber.filter.tags='@v15.2.x'`
 
