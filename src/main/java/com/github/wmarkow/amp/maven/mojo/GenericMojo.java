package com.github.wmarkow.amp.maven.mojo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.graph.selector.AndDependencySelector;
import org.eclipse.aether.util.graph.selector.OptionalDependencySelector;
import org.eclipse.aether.util.graph.selector.ScopeDependencySelector;
import org.eclipse.aether.util.graph.transformer.NoopDependencyGraphTransformer;

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
        DependencyNode node = getVerboseDependencyTree();

        List< Artifact > result = new ArrayList< Artifact >();

        for( DependencyNode dn : node.getChildren() )
        {
            if( ARDUINO_CORE_EXTENSION.equals( dn.getArtifact().getExtension() )
                || ARDUINO_LIB_EXTENSION.equals( dn.getArtifact().getExtension() )
                || ARDUINO_CORE_LIB_EXTENSION.equals( dn.getArtifact().getExtension() ) )
            {
                result.add( dn.getArtifact() );
            }
        }

        return result;
    }

    private DependencyNode getVerboseDependencyTree()
    {
        // Create CollectRequest object that will be submitted to collect the dependencies
        CollectRequest collectReq = new CollectRequest();

        // Get artifact this Maven project is attempting to build
        Artifact artifact = getProjectArtifact();

        DefaultRepositorySystemSession session = new DefaultRepositorySystemSession( repoSession );

        // Set the No-Op Graph transformer so tree stays intact
        session.setDependencyGraphTransformer( new NoopDependencyGraphTransformer() );

        if( ARDUINO_LIB_EXTENSION.equals( artifact.getExtension() ) )
        {
            // for arduinolib add PROVIDED as well (so exclude TEST)
            DependencySelector dependencySelector =
                new AndDependencySelector( new ScopeDependencySelector( JavaScopes.TEST ),
                    new OptionalDependencySelector() );
            session.setDependencySelector( dependencySelector );
        }

        // Create Aether graph dependency object from params extracted above
        org.eclipse.aether.graph.Dependency dep = new org.eclipse.aether.graph.Dependency( artifact, null );

        // Set the root of the request, in this case the current project will be the root
        collectReq.setRoot( dep );

        try
        {
            return repoSystem.collectDependencies( session, collectReq ).getRoot();
        }
        catch( DependencyCollectionException exception )
        {
            getLog().warn( "Could not collect dependencies from repo system", exception );
            return null;
        }
    }
}
