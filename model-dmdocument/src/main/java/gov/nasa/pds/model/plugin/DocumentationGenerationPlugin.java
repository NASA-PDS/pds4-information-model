package gov.nasa.pds.model.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
* DM document goal.
*/
@Mojo(name="generateDocumentation", defaultPhase = LifecyclePhase.PROCESS_SOURCES)
public class DocumentationGenerationPlugin extends AbstractMojo {
    @Parameter(property="ontologySrc", defaultValue="${basedir}/src/main/ontology", required=true)
    private String ontologySrc;

    @Parameter(property="target", defaultValue="${project.build.outputDirectory}/ontology", required=true)
    private String target;

    /** execute it
    *
    * @throws MojoExecutionException if something bad happens.
    */
    public void execute() throws MojoExecutionException {
        try {
            getLog().info("=== Look, I'm going to use " + this.ontologySrc);
            getLog().info("=== and writing to " + this.target);
            File d = new File(this.target);
            d.mkdirs();
            Map<String, String> envvars = new HashMap<String, String>();
            envvars.put("PARENT_DIR", this.ontologySrc);
            envvars.put("SCRIPT_DIR", "foo");
            envvars.put("LIB_DIR", "foo");
            envvars.put("JAVA_HOME", System.getProperty("java.home"));
            this.setEnv(envvars);
            DMDocument.outputDirPath = d.toString() + "/";
            DMDocument.main(new String[]{"-p"});
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new MojoExecutionException("DMDocument error", ex);
        }
    }

    /** Courtesy of http://stackoverflow.com/questions/318239/how-do-i-set-environment-variables-from-java */
    protected void setEnv(Map<String, String> newenv) throws Throwable {
        try {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        } catch (NoSuchFieldException e) {
            Class[] classes = Collections.class.getDeclaredClasses();
            Map<String, String> env = System.getenv();
            for (Class cl: classes) {
                if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                    Field field = cl.getDeclaredField("m");
                    field.setAccessible(true);
                    Object obj = field.get(env);
                    Map<String, String> map = (Map<String, String>) obj;
                    map.clear();
                    map.putAll(newenv);
                }
            }
        } 
    }
}
