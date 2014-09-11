The PDS4 Model Ontology component encapsulates the Protégé ontology instances
for the PDS4 information and data model.  This package uses the Maven plugin
"pds-model-dmdocument-maven-plugin" to generate documentation and other
artifcats from the ontology and packages this output (including generated,
documentation, classes and attribute definitions) into a JAR file.

The package can be generated with "mvn jar:jar" command.  Documentation can be
found at https://pds-cm.jpl.nasa.gov/pds4/model/model-ontology.  Alternatively,
you can run "mvn site:run" and view the documentation locally with a browser at
http://localhost:8080.

In order to create a complete package for distribution, execute the 
following commands: 

% mvn site
% mvn package
