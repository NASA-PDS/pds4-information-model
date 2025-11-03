# About DM-Document

This component is a Maven plugin that takes output from [Protégé](http://protege.stanford.edu/) in the form of ontology instance files and from that generates DM-documents and other artifacts that can be used to report, chronicle, detail, and describe the PDS ontology in a number of formats.

To use this component, you reference it from a Maven project that has ontology files and a `pom.xml` configured with a `build` section to execute the plugin on the ontology files. Typically, you will install your ontology files in `PROJECT-BASEDIR/src/ontology/Data`. Then, add the following `build` section to `pom.xml`:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>gov.nasa.pds.2010</groupId>
            <artifactId>pds-model-dmdocument-maven-plugin</artifactId>
            <version>1.6.0-dev</version>
            <configuration>
                <ontologySrc>${basedir}/src/ontology</ontologySrc>
            </configuration>
            <executions>
                <execution>
                    <id>default-compile</id>
                    <phase>compile</phase>
                    <goals>
                        <goal>generateDocumentation</goal>
                        <goal>generateClasses</goal>
                        <goal>generateAttributes</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

The above tells Maven to use the plugin called "pds-model-dmdocument-maven-plugin" version "1.6.0-dev", produced by the group "gov.nasa.pds.2010". Maven should use this plugin during the `compile` phase of software generation. During the compile phase, we ask the plugin to create documentation files (with the `generateDocumentation` goal), to generate class files (with the `generateClasses` goal), and generate attribute definitions (with the `generateAttributes` goal). The results will go into the project's JAR file (assuming the POM's packaging is set to `jar`).

To generate these artifacts, you can then run `mvn jar:jar`. The artifacts will be generated and bundled up into the project's `.jar` file in the `target` subdirectory.

Please send comments, change requests and bug reports to the [PDS Operator](mailto:pds_operator@jpl.nasa.gov) at pds_operator@jpl.nasa.gov.
