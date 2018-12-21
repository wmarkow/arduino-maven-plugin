package com.github.wmarkow.amp.maven.mojo;

import java.io.File;
import java.io.IOException;
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

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.util.ArtifactUtils;

public abstract class ArduinoAbstractMojo extends AbstractMojo
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
    protected String board;

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

    protected Artifact getArduinoCoreArtifact()
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

    protected PlatformVariables getPlatformVariables( Artifact arduinoCoreArtifact ) throws IOException
    {
        // FIXME: derive path to platfrom.txt correctly
        File platformTxtFile =
            new File( getPathToUnpackedLibrarySourcesDir( arduinoCoreArtifact ), "avr/platform.txt" );

        PlatformFilesReader pfr = new PlatformFilesReader();
        return pfr.readPlatformVariablesFromFile( platformTxtFile );
    }

    protected BoardVariables getBoardVariables( Artifact arduinoCoreArtifact, String board )
        throws IOException
    {
        // FIXME: derive path to boards.txt correctly
        File boardsTxtFile =
            new File( getPathToUnpackedLibrarySourcesDir( arduinoCoreArtifact ), "avr/boards.txt" );

        PlatformFilesReader pfr = new PlatformFilesReader();
        return pfr.readBoardsVariables( boardsTxtFile ).getBoardVariables( board );
    }

    protected File getPathToUnpackedLibrarySourcesDir( Artifact artifact )
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

    protected File[] getPathToUnpackedCoreLibrarySourcesDir( Artifact artifact, String arch, String core,
        String variant )
    {
        File baseDir =
            new File( new File( "target/generated-sources/" ).getAbsolutePath(),
                ArtifactUtils.getBaseFileName( artifact ) );

        List< File > result = new ArrayList< File >();
        result.add( new File( baseDir, arch + "/cores/" + core ) );
        result.add( new File( baseDir, arch + "/variants/" + variant ) );

        return result.toArray( new File[]
        {} );
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