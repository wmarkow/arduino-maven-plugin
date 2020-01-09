package com.github.wmarkow.amp.maven.mojo.platform;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

import com.github.wmarkow.amp.maven.mojo.GenericMojo;
import com.github.wmarkow.amp.maven.mojo.phase.gensources.UnpackDependenciesMojo;

@Mojo( name = "list-libraries", defaultPhase = LifecyclePhase.NONE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class ListLibrariesMojo extends UnpackDependenciesMojo
{
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        final Artifact arduinoCore = getArduinoCoreDependency();
        final String[] names = getArduinoCoreLibrariesNames();

        for( String name : names )
        {
            final String groupId = arduinoCore.getGroupId();
            final String artifactId = name;
            final String extension = GenericMojo.ARDUINO_CORE_LIB_EXTENSION;
            final String version = arduinoCore.getVersion();
            
            Artifact dep = new DefaultArtifact( groupId, artifactId, extension, version );

            getLog().info( artifactToDependencyString( dep ) );
        }
    }

    private String[] getArduinoCoreLibrariesNames() throws MojoExecutionException
    {
        File unpackedArduinoCore = getPathToUnpackedArduinoCore();
        File libraries = new File( unpackedArduinoCore, "libraries" );

        if( !libraries.exists() )
        {
            throw new MojoExecutionException( String.format( "Directore not exists: %s",
                libraries.getAbsolutePath() ) );
        }

        return libraries.list();
    }

    private String artifactToDependencyString( Artifact artifact )
    {
        StringBuilder sb = new StringBuilder();
        sb.append( String.format( "<dependency>" ) );
        sb.append( String.format( "    <groupId>%s</groupId>", artifact.getGroupId() ) );
        sb.append( String.format( "    <artifactId>%s</artifactId>", artifact.getArtifactId() ) );
        sb.append( String.format( "    <version>%s</version>", artifact.getVersion() ) );
        sb.append( String.format( "    <type>%s</type>", artifact.getExtension() ) );
        sb.append( String.format( "</dependency>" ) );

        return sb.toString();
    }
}
