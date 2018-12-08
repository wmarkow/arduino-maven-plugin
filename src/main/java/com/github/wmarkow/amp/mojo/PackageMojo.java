package com.github.wmarkow.amp.mojo;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.ArtifactUtils;

@Mojo( name = "package", defaultPhase = LifecyclePhase.PACKAGE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class PackageMojo extends ArduinoAbstractMojo
{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        Artifact artifact = getProjectArtifact();

        File elfFile =
            new File( new File( "" ).getAbsoluteFile(), "target/" + ArtifactUtils.getBaseFileName( artifact )
                + ".elf" );

        File hexFile =
            new File( new File( "" ).getAbsoluteFile(), "target/" + ArtifactUtils.getBaseFileName( artifact )
                + ".hex" );
    }
}
