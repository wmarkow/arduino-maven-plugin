package com.github.wmarkow.amp.maven.mojo;

import java.net.MalformedURLException;
import java.net.URL;
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
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.util.graph.transformer.NoopDependencyGraphTransformer;

import com.github.wmarkow.amp.util.ArtifactUtils;

public abstract class GenericMojo extends AbstractMojo
{
    protected final static String ARDUINO_CORE_EXTENSION = "arduinocore";
    protected final static String ARDUINO_LIB_EXTENSION = "arduinolib";

    @Component
    protected MavenProject mavenProject;

    @Component
    protected RepositorySystem repoSystem;

    @Parameter( defaultValue = "${repositorySystemSession}" )
    protected RepositorySystemSession repoSession;

    @Parameter( property = "packageIndexUrl", required = true )
    private String packageIndexUrl;

    @Parameter( property = "board", required = true )
    private String board;

    protected List< Artifact > getArduinoDependencies()
    {
        DependencyNode node = getVerboseDependencyTree();

        List< Artifact > result = new ArrayList< Artifact >();

        for( DependencyNode dn : node.getChildren() )
        {
            if( ARDUINO_CORE_EXTENSION.equals( dn.getArtifact().getExtension() )
                || ARDUINO_LIB_EXTENSION.equals( dn.getArtifact().getExtension() ) )
            {
                result.add( dn.getArtifact() );
            }
        }

        return result;
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

    protected org.eclipse.aether.artifact.Artifact getProjectArtifact()
    {
        return ArtifactUtils.mavenToAether( mavenProject.getArtifact() );
    }

    protected URL getPackageIndexUrl() throws MalformedURLException
    {
        return new URL( packageIndexUrl );
    }

    protected String getBoard() throws MalformedURLException
    {
        return board;
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
