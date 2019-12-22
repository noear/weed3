package org.noear.weed.generator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

@Mojo(name="generateEntity")
public class GenerateEntity extends AbstractMojo {
    @Parameter(defaultValue = "${basedir}")
    private File baseDir;

    @Parameter(defaultValue = "${project.build.sourceDirectory}",required = true,readonly = true)
    private File sourceDir;

    public void execute() throws MojoExecutionException, MojoFailureException {
        //getLog().info(baseDir.getAbsolutePath());
        //getLog().info(sourceDir.getAbsolutePath());

        System.out.println("Start building entity:");
        //XmlSqlMapperGenerator.generate(baseDir, sourceDir);
        //getLog().info("Hello MavenPlugin, I'm Weed3.");
    }
}
