package com.github.wmarkow.amp.maven.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;

public abstract class GenericMojo extends AbstractMojo
{
    public final static String ARDUINO_CORE_EXTENSION = "arduinocore";
    public final static String ARDUINO_LIB_EXTENSION = "arduinolib";
    public final static String ARDUINO_CORE_LIB_EXTENSION = "arduinocorelib";

    @Component
    protected MavenProject mavenProject;

    @Component
    protected RepositorySystem repoSystem;

    @Parameter( defaultValue = "${repositorySystemSession}" )
    protected RepositorySystemSession repoSession;

    protected File getGeneratedSourcesDirFile()
    {
        return new File( "target/generated-sources/" );
    }

    protected File getArduinoMavenPluginDirFile()
    {
        return new File( "target/arduino-maven-plugin" );
    }

    protected File getArduinoPlatformDirFile()
    {
        String path = System.getProperty( "user.home" );
        return new File( path, ".arduino-maven-plugin" );
    }

    protected String artifactToString( org.eclipse.aether.artifact.Artifact artifact )
    {
        StringBuilder sb = new StringBuilder();
        sb.append( artifact.getGroupId() );
        sb.append( ":" );
        sb.append( artifact.getArtifactId() );
        sb.append( ":" );
        sb.append( artifact.getExtension() );
        sb.append( ":" );
        sb.append( artifact.getClassifier() );
        sb.append( ":" );
        sb.append( artifact.getVersion() );

        return sb.toString();
    }

    protected Artifact getProjectArtifact()
    {
        final org.apache.maven.artifact.Artifact mavenArtifact = mavenProject.getArtifact();

        return new DefaultArtifact( mavenArtifact.getGroupId(), mavenArtifact.getArtifactId(),
            mavenArtifact.getClassifier(), mavenArtifact.getType(), mavenArtifact.getVersion() );
    }

    protected Artifact getArduinoCoreDependency()
    {
        for( Artifact artifact : getArduinoDependencies() )
        {
            if( ARDUINO_CORE_EXTENSION.equals( artifact.getExtension() ) )
            {
                return artifact;
            }
        }

        return null;
    }

    protected List< Artifact > getArduinoLibDependencies()
    {
        List< Artifact > result = new ArrayList< Artifact >();

        for( Artifact artifact : getArduinoDependencies() )
        {
            if( ARDUINO_LIB_EXTENSION.equals( artifact.getExtension() )
                || ARDUINO_CORE_LIB_EXTENSION.equals( artifact.getExtension() ) )
            {
                result.add( artifact );
            }
        }

        return result;
    }

    protected List< Artifact > getArduinoDependencies()
    {
        List< Artifact > result = new ArrayList< Artifact >();

        for( Artifact artifact : getDependencies() )
        {
            if( ARDUINO_CORE_EXTENSION.equals( artifact.getExtension() )
                || ARDUINO_LIB_EXTENSION.equals( artifact.getExtension() )
                || ARDUINO_CORE_LIB_EXTENSION.equals( artifact.getExtension() ) )
            {
                result.add( artifact );
            }
        }

        return result;
    }

    private List< Artifact > getDependencies()
    {
        List< Artifact > result = new ArrayList<>();

        for( org.apache.maven.artifact.Artifact artifact : mavenProject.getArtifacts() )
        {
            // String groupId, String artifactId, String classifier, String extension, String version
            String groupId = artifact.getGroupId();
            String artifactId = artifact.getArtifactId();
            String classifier = artifact.getClassifier();
            String extension = artifact.getType();
            String version = artifact.getVersion();

            Artifact aetherArtifact =
                new DefaultArtifact( groupId, artifactId, classifier, extension, version );

            result.add( aetherArtifact );
        }

        return result;
    }
}
