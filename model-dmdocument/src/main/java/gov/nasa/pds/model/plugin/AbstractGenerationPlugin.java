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
      this.generateArtifacts();
    } catch (RuntimeException ex) {
      throw ex;
    } catch (Throwable ex) {
      throw new MojoExecutionException(" error", ex);
    }
  }
}
