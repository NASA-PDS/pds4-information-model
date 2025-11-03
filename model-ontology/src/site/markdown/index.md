# About Model Ontology

The PDS4 Model Ontology component encapsulates the Protégé ontology instances for the PDS4 information and data model. This package uses the Maven plugin "model-dmdocument" to generate documentation and other artifacts from the ontology and packages this output (including generated, DM documentation, classes and attribute definitions) into a JAR file.

To generate the output artifacts:

1. Place the Protégé output ontology instances in the `src/ontology` directory. (The output must go into a `Data` subdirectory beneath `src/ontology` due to the way the DM-Document plugin assumes things work).

2. Commit these changes to revision control (with `svn commit`, for example).

3. Run `mvn jar:jar` to generate a `target/model-ontology-VERSION.jar` file that contains all of the generated artifacts.

Please send comments, change requests and bug reports to the [PDS Operator](mailto:pds_operator@jpl.nasa.gov) at pds_operator@jpl.nasa.gov.
