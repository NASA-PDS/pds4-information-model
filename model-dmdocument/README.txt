The Model DM-Document component provides Maven plugins that generate DM
documents and other artifacts from ontologies.  The software is packaged in a
JAR file and intended to be referenced from other Maven pom.xml files.

The software can be compiled with the "mvn compile" command, but you'll more
likely run "mvn install" to place the generated JAR file into your local
repository.  And if PDS has its Maven artifact distribution set up correctly,
you won't even need to do that; rather, you'll reference the "pds-model-
dmdocument-maven-plugin" artifact in the <build> section of your ontology Maven
pom.xml.

Documentation can be found at https://pds-cm.jpl.nasa.gov/pds4/model/model-
dmdocument.  Alternatively, you can run "mvn site:run" and view the
documentation locally with a browser at http://localhost:8080.

In order to create a complete package for distribiton, execute the following
commands:

% mvn site
% mvn package


