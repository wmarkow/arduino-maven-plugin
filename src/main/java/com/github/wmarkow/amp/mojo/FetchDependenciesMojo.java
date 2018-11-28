package com.github.wmarkow.amp.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

//@Mojo( name = "download-dependencies", defaultPhase = LifecyclePhase.GENERATE_SOURCES,
//    requiresProject = true, requiresDependencyResolution = ResolutionScope.TEST )
@Mojo( name = "download-dependencies" )
public class FetchDependenciesMojo extends AbstractMojo
{
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        System.out.println( "Hello world!" );
    }
}


