package com.github.wmarkow.amp.mojo;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.ArtifactUtils;
import com.github.wmarkow.amp.BuildConsoleListener;
import com.github.wmarkow.amp.builder.ArduinoBuilder;

@Mojo( name = "build", defaultPhase = LifecyclePhase.COMPILE,
    requiresDependencyResolution = ResolutionScope.TEST, requiresProject = true )
public class BuildMojo extends ArduinoAbstractMojo
{

    @Parameter( defaultValue = "${project.build.sourceDirectory}", required = true, readonly = true )
    private String sourceDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getLog().info( String.format( "Source directory is %s", sourceDirectory ) );
        getLog().info( String.format( "Target directory is %s", new File( "target" ).getAbsoluteFile() ) );

        org.apache.maven.artifact.Artifact art = mavenProject.getArtifact();

        ArduinoBuilder builder =
            new ArduinoBuilder( art.getArtifactId(), art.getBaseVersion(), art.getClassifier() );

        builder.addBuildListener( new BuildConsoleListener() );
        builder.setTargetBuildDirectory( new File( new File( "" ).getAbsolutePath(), "target" ) );
        builder.addSourceFileDir( new File( sourceDirectory ) );

        for( Artifact arduinoLib : getArduinoLibDependencies() )
        {
            File libSourcesDir = getPathToUnpackedLibrarySourcesDir( arduinoLib );

            builder.addSourceFileDir( libSourcesDir );
            builder.addIncludePath( libSourcesDir );
        }

        builder.build();
    }

    private File getPathToUnpackedLibrarySourcesDir( Artifact artifact )
    {
        File baseDir =
            new File( new File( "target/generated-sources/" ).getAbsolutePath(),
                ArtifactUtils.getBaseFileName( artifact ) );

        File dirWithSrc = new File( baseDir, "src" );
        if( dirWithSrc.exists() )
        {
            return dirWithSrc;
        }

        return baseDir;
    }
}
