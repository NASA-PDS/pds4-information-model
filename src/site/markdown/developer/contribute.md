# Contribute

## Adding New Test Cases

### Pre-requisites
* Eclipse installed (`brew cask install eclipse-java`)
* maven installed (`brew install maven`)
* Cloned validate repo

### Test Information
Each test added to a `validate.*.feature` performs the same basic execution under the hood:
  1. Generate PDS4 Information Model (`lddtool -p`)
  2. Run LDDTool to generate an LDD (see `ingestLDDFileName` column of the feature file) (optional)
  3. Run Validate against 1+ test product(s) (see `commandArgs` for specifying how validate is executed)
     * Note: The following default arguments are prepended to the `validateCommandArgs` you specify in the feature file:
        * `--disable-context-mismatch-warnings`
        *  `--report-style json`
        *  `--skip-content-validation`
        *  `--skip-context-validation`
        *  `--report-file {reportDir}/`
        *  For the latest set of defaults, see [these lines of source code](https://github.com/NASA-PDS/pds4-information-model/blob/main/model-lddtool/src/test/java/cucumber/ValidateStepDefs.java#L30-L32)
      
All data and logs generated when executing `mvn test` can be found under `model-lddtool/target/test`, e.g. (redacted some other noise from this):
```
model-lddtool/target/test/
├── github795
│   ├── export
│   │   ├── PDS4_PDS_1N00.JSON
│   │   ├── PDS4_PDS_1N00.csv
│   │   ├── PDS4_PDS_1N00.sch
│   │   ├── PDS4_PDS_1N00.xml
│   │   ├── PDS4_PDS_1N00.xsd
│   │   ├── PDS4_PDS_DD_1N00.xml
│   │   ├── PDS4_PDS_MODEL_1N00.rdf
│   │   ├── dd11179_GenPClass.pins
│   │   └── dd11179_GenPClass_241106.pins
│   ├── index_1N00.html
│   └── lddtool-output.txt
├── github795b
│   ├── export
│   │   ├── PDS4_PDS_1N00.JSON
│   │   ├── PDS4_PDS_1N00.csv
│   │   ├── PDS4_PDS_1N00.sch
│   │   ├── PDS4_PDS_1N00.xml
│   │   ├── PDS4_PDS_1N00.xsd
│   │   ├── PDS4_PDS_DD_1N00.xml
│   │   ├── PDS4_PDS_MODEL_1N00.rdf
│   │   ├── dd11179_GenPClass.pins
│   │   └── dd11179_GenPClass_241106.pins
│   ├── index_1N00.html
│   └── lddtool-output.txt
├── github797
│   ├── PDS4_GEOM_1N00_1970.csv
│   ├── PDS4_GEOM_1N00_1970.sch
│   ├── PDS4_GEOM_1N00_1970.txt
│   ├── PDS4_GEOM_1N00_1970.xml
│   ├── PDS4_GEOM_1N00_1970.xsd
│   ├── export
│   │   ├── PDS4_PDS_1N00.JSON
│   │   ├── PDS4_PDS_1N00.csv
│   │   ├── PDS4_PDS_1N00.sch
│   │   ├── PDS4_PDS_1N00.xml
│   │   ├── PDS4_PDS_1N00.xsd
│   │   ├── PDS4_PDS_DD_1N00.xml
│   │   ├── PDS4_PDS_MODEL_1N00.rdf
│   │   ├── dd11179_GenPClass.pins
│   │   └── dd11179_GenPClass_241106.pins
│   ├── index_1N00.html
│   └── lddtool-output.txt
├── report_github795.json
├── report_github795b.json
└── report_github797.json
```


### Procedure
TBD
