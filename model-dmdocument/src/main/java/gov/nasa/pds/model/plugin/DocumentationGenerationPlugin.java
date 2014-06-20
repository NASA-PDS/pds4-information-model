//      Copyright 2014, by the California Institute of Technology.
//      ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//      Any commercial use must be negotiated with the Office of Technology 
//      Transfer at the California Institute of Technology.
//      
//      This software is subject to U. S. export control laws and regulations 
//      (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software 
//      is subject to U.S. export control laws and regulations, the recipient has 
//      the responsibility to obtain export licenses or other export authority as 
//      may be required before exporting such information to foreign countries or 
//      providing access to foreign nationals.

package gov.nasa.pds.model.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
* Documentation generation plugin. This is a Maven plugin to create lots of various
* documentation from PDS ontology sources (which themselves come from Protégé).
*/
@Mojo(name="generateDocumentation", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class DocumentationGenerationPlugin extends AbstractGenerationPlugin {
    protected void generateArtifacts() throws MojoExecutionException {
        try {
            DMDocument.main(new String[]{"-p"});
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new MojoExecutionException("DMDocument error", ex);
        }
    }
}
