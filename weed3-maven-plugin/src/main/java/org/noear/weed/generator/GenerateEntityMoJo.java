package org.noear.weed.generator;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.noear.weed.generator.entity.XmlEntityGenerator;

import java.io.File;


@Mojo(name="generate-entity")
public class GenerateEntityMoJo extends AbstractMojo {
    @Parameter(defaultValue = "${basedir}")
    private File baseDir;

    @Parameter(defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true)
    private File sourceDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        //getLog().info(baseDir.getAbsolutePath());
        //getLog().info(sourceDir.getAbsolutePath());

        System.out.println("[Weed3] Start building entity files:");
        XmlEntityGenerator.generate(baseDir, sourceDir);
        //getLog().info("Hello MavenPlugin, I'm Weed3.");
    }
}