package com.github.wmarkow.amp.mojo;

import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo( name = "download-dependencies", defaultPhase = LifecyclePhase.GENERATE_SOURCES,
    requiresProject = true, requiresDependencyResolution = ResolutionScope.TEST )
public class FetchDependenciesMojo extends AbstractMojo
{
    @Parameter( defaultValue = "${localRepository}", readonly = true, required = true )
    private ArtifactRepository local;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        // TODO Auto-generated method stub

        System.out.println();
    }
}


