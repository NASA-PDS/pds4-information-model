// Copyright 2019, California Institute of Technology ("Caltech").
// U.S. Government sponsorship acknowledged.
//
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// * Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
// * Redistributions must reproduce the above copyright notice, this list of
// conditions and the following disclaimer in the documentation and/or other
// materials provided with the distribution.
// * Neither the name of Caltech nor its operating division, the Jet Propulsion
// Laboratory, nor the names of its contributors may be used to endorse or
// promote products derived from this software without specific prior written
// permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package gov.nasa.pds.model.plugin;

import java.io.File;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * Base class for Maven-plugin-based artifact generators from PDS ontology data, which itself comes
 * from Protégé.
 */
public abstract class AbstractGenerationPlugin extends AbstractMojo {
  @Parameter(property = "ontologySrc", defaultValue = "${basedir}/src/main/ontology",
      required = true)
  protected String ontologySrc;

  @Parameter(property = "target", defaultValue = "${project.build.outputDirectory}/ontology",
      required = true)
  protected String target;

  /** Subclasses must override this. */
  protected abstract void generateArtifacts() throws MojoExecutionException;

  /**
   * Execute this plugin. The exeuction of this plugin uses the Template Method design pattern.
   *
   * @throws MojoExecutionException If the center does not hold.
   */
  @Override
  public void execute() throws MojoExecutionException {
    try {
      getLog().info("Ontology sources are in ‘" + this.ontologySrc + "’");
      getLog().info("Writing artifacts from those sources to ‘" + this.target + "’");
      File d = new File(this.target);
      d.mkdirs();
      // DMDocument.outputDirPath = d.toString() + "/";
      this.generateArtifacts();
    } catch (RuntimeException ex) {
      throw ex;
    } catch (Throwable ex) {
      throw new MojoExecutionException(" error", ex);
    }
  }
}
