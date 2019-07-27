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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
* Base class for Maven-plugin-based artifact generators from PDS ontology data, which
* itself comes from Protégé.
*/
public abstract class AbstractGenerationPlugin extends AbstractMojo {
    @Parameter(property="ontologySrc", defaultValue="${basedir}/src/main/ontology", required=true)
    protected String ontologySrc;

    @Parameter(property="target", defaultValue="${project.build.outputDirectory}/ontology", required=true)
    protected String target;

    /** Subclasses must override this. */
    protected abstract void generateArtifacts() throws MojoExecutionException;

    /** Execute this plugin.  The exeuction of this plugin uses the Template Method
    * design pattern.
    *
    * @throws MojoExecutionException If the center does not hold.
    */
    public void execute() throws MojoExecutionException {
        try {
            getLog().info("Ontology sources are in ‘" + this.ontologySrc + "’");
            getLog().info("Writing artifacts from those sources to ‘" + this.target + "’");
            File d = new File(this.target);
            d.mkdirs();
            Map<String, String> envvars = new HashMap<String, String>();
            envvars.put("PARENT_DIR", this.ontologySrc);
            envvars.put("SCRIPT_DIR", "foo");
            envvars.put("LIB_DIR", "foo");
            envvars.put("JAVA_HOME", System.getProperty("java.home"));
            this.setEnv(envvars);
            DMDocument.outputDirPath = d.toString() + "/";
            this.generateArtifacts();
        } catch (RuntimeException ex) {
            throw ex;
        } catch (Throwable ex) {
            throw new MojoExecutionException(" error", ex);
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
