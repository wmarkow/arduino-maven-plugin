package com.github.wmarkow.amp.maven.mojo.build;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.plexus.util.FileUtils;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.maven.mojo.GenericMojo;

@Mojo( name = "package2", defaultPhase = LifecyclePhase.PACKAGE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class Package2Mojo extends GenericMojo
{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            Artifact projectArtifact = getProjectArtifact();
            // assuming that zip for arduinolib is available
            File inputZip =
                new File( String.format( "target/%s-%s.zip/", projectArtifact.getArtifactId(),
                    projectArtifact.getVersion() ) );

            File mainFile =
                new File( String.format( "target/%s-%s.%s/", projectArtifact.getArtifactId(),
                    projectArtifact.getVersion(), GenericMojo.ARDUINO_LIB_EXTENSION ) );

            FileUtils.copyFile( inputZip, mainFile );

            // this.mavenProject.setFile( mainFile.getAbsoluteFile() );
            this.mavenProject.getArtifact().setFile( mainFile.getAbsoluteFile() );
        }
        catch( Exception e )
        {
            throw new MojoFailureException( e.getMessage(), e );
        }
    }
}
