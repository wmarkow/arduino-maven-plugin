package com.github.wmarkow.amp.mojo;

import java.util.ArrayList;
import java.util.List;

//import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.util.graph.transformer.NoopDependencyGraphTransformer;

@Mojo( name = "fetch-dependencies", defaultPhase = LifecyclePhase.INITIALIZE, requiresProject = true )
public class FetchDependenciesMojo extends AbstractMojo
{
    @Component
    private MavenProject mavenProject;

    @Component
    private RepositorySystem repoSystem;

    @Parameter( defaultValue = "${repositorySystemSession}" )
    private RepositorySystemSession repoSession;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        List< Artifact > arduinoLibs = getMissingArduinoLibs();

        for( Artifact arduinoLib : arduinoLibs )
        {
            System.out.println( String.format( "Arduinolib is missing: %s", artifactToString( arduinoLib ) ) );
        }
    }

    private List< Artifact > getMissingArduinoLibs()
    {
        List< Artifact > missingLibs = new ArrayList<>();

        final List< Artifact > arduinoLibs = getArduinoLibDependencies();

        for( Artifact arduinoLib : arduinoLibs )
        {
            ArtifactRequest req = new ArtifactRequest().setArtifact( arduinoLib );
            ArtifactResult resolutionResult;
            try
            {
                resolutionResult = repoSystem.resolveArtifact( repoSession, req );

                if( resolutionResult.isMissing() )
                {
                    missingLibs.add( arduinoLib );
                }
            }
            catch( ArtifactResolutionException e )
            {
                missingLibs.add( arduinoLib );
            }
        }

        return missingLibs;
    }

    private List< Artifact > getArduinoLibDependencies()
    {
        DependencyNode node = getVerboseDependencyTree();

        List< Artifact > result = new ArrayList< Artifact >();

        for( DependencyNode dn : node.getChildren() )
        {
            if( "arduinolib".equals( dn.getArtifact().getExtension() ) )
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
        org.apache.maven.artifact.Artifact art = mavenProject.getArtifact();

        DefaultRepositorySystemSession session = new DefaultRepositorySystemSession( repoSession );

        System.out.println( String.format( "Local repo path = %s", repoSession.getLocalRepository()
            .getBasedir().getAbsolutePath() ) );

        // Set the No-Op Graph transformer so tree stays intact
        session.setDependencyGraphTransformer( new NoopDependencyGraphTransformer() );

        // Create Aether graph dependency object from params extracted above
        org.eclipse.aether.graph.Dependency dep =
            new org.eclipse.aether.graph.Dependency( new org.eclipse.aether.artifact.DefaultArtifact(
                art.getGroupId(), art.getArtifactId(), null, art.getVersion() ), null );

        // Set the root of the request, in this case the current project will be the root
        collectReq.setRoot( dep );

        try
        {
            return repoSystem.collectDependencies( session, collectReq ).getRoot();
        }
        catch( DependencyCollectionException exception )
        {
            this.getLog().warn( "Could not collect dependencies from repo system", exception );
            return null;
        }
    }

    private String artifactToString( org.eclipse.aether.artifact.Artifact artifact )
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
}
