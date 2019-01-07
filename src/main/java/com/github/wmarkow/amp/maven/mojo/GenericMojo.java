package com.github.wmarkow.amp.maven.mojo;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.github.wmarkow.amp.arduino.platform.Package;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.manager.PlatformPackageManager;
import com.github.wmarkow.amp.util.ArtifactUtils;

public abstract class GenericMojo extends AbstractMojo
{
    protected final static String ARDUINO_CORE_EXTENSION = "arduinocore";
    protected final static String ARDUINO_LIB_EXTENSION = "arduinolib";

    private PlatformPackageManager ppm;

    @Component
    protected MavenProject mavenProject;

    @Component
    protected RepositorySystem repoSystem;

    @Parameter( defaultValue = "${repositorySystemSession}" )
    protected RepositorySystemSession repoSession;

    @Parameter( property = "arduino-maven-plugin.packageIndexes", required = true )
    private URL[] packageIndexes;

    @Parameter( property = "arduino-maven-plugin.board", required = true )
    private String board;

    @Parameter( property = "arduino-maven-plugin.buildVariables", required = false )
    protected Map< String, String > buildVariables;

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

    protected org.eclipse.aether.artifact.Artifact getProjectArtifact()
    {
        return ArtifactUtils.mavenToAether( mavenProject.getArtifact() );
    }

    protected URL[] getPackageIndexesUrls()
    {
        return packageIndexes;
    }

    protected String getBoard() throws MalformedURLException
    {
        return board;
    }

    protected Map< String, String > getBuildVariables()
    {
        return buildVariables;
    }

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

    protected synchronized PlatformPackageManager getPlatformPackageManager()
    {
        if( ppm == null )
        {
            PlatformPackageManager ppm = new PlatformPackageManager( getArduinoPlatformDirFile() );
            for( URL url : getPackageIndexesUrls() )
            {
                ppm.addPackageUrl( url );
            }
            ppm.update();

            this.ppm = ppm;
        }

        return ppm;
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

    protected Package getPackage()
    {
        final Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        return getPlatformPackageManager().getPlatformRepository().getPackage(
            arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
    }

    protected Platform getPlatform()
    {
        final Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        return getPlatformPackageManager().getPlatformRepository().getPlatform(
            arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
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
