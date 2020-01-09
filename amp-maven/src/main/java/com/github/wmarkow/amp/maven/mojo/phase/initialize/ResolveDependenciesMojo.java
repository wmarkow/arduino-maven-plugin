package com.github.wmarkow.amp.maven.mojo.phase.initialize;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.collection.CollectRequest;
import org.eclipse.aether.collection.DependencyCollectionException;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.graph.DependencyNode;
import org.eclipse.aether.graph.DependencyVisitor;
import org.eclipse.aether.installation.InstallRequest;
import org.eclipse.aether.installation.InstallationException;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.DependencyRequest;
import org.eclipse.aether.util.artifact.JavaScopes;
import org.eclipse.aether.util.graph.selector.AndDependencySelector;
import org.eclipse.aether.util.graph.selector.OptionalDependencySelector;
import org.eclipse.aether.util.graph.selector.ScopeDependencySelector;
import org.eclipse.aether.util.graph.transformer.NoopDependencyGraphTransformer;

import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformLibrariesIndex;
import com.github.wmarkow.amp.arduino.platform.PlatformRepository;
import com.github.wmarkow.amp.arduino.platform.manager.PlatformLibrariesManager;
import com.github.wmarkow.amp.arduino.platform.manager.PlatformToolsManager;
import com.github.wmarkow.amp.maven.artifact.resolver.ArduinloLibraryIndexArtifactResolver;
import com.github.wmarkow.amp.maven.artifact.resolver.ArduinoCoreArtifactResolver;
import com.github.wmarkow.amp.maven.mojo.GenericPlatformMojo;
import com.github.wmarkow.amp.util.CompressUtil;

@Mojo( name = "fetch-dependencies", defaultPhase = LifecyclePhase.INITIALIZE, requiresProject = true )
public class ResolveDependenciesMojo extends GenericPlatformMojo
{
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        try
        {
            updateArduinoPlatform();
        }
        catch( IOException e1 )
        {
            getLog().error( e1.getMessage(), e1 );
            throw new MojoFailureException( e1.getMessage() );
        }

        for( Artifact arduinoLib : getMissingArduinoDependencies() )
        {
            if( ARDUINO_CORE_EXTENSION.equals( arduinoLib.getExtension() ) )
            {
                final File workDir = getArduinoMavenPluginDirFile();

                try
                {
                    ArduinoCoreArtifactResolver fetcher =
                        new ArduinoCoreArtifactResolver( getPlatformPackageManager() );
                    File fetchedCore =
                        fetcher.fetch( arduinoLib.getArtifactId(), arduinoLib.getVersion(), workDir );
                    installLibrary( arduinoLib, fetchedCore );
                }
                catch( Exception e )
                {
                    throw new MojoFailureException( e.getMessage(), e );
                }
            }

            if( ARDUINO_LIB_EXTENSION.equals( arduinoLib.getExtension() ) )
            {
                final File workDir = getArduinoMavenPluginDirFile();

                try
                {
                    PlatformLibrariesIndex index = getPlatformLibrariesManager().getPlatformLibrariesIndex();
                    ArduinloLibraryIndexArtifactResolver resolver =
                        new ArduinloLibraryIndexArtifactResolver( index );
                    File file = resolver.fetch( arduinoLib, workDir );

                    installLibrary( arduinoLib, file );
                }
                catch( Exception e )
                {
                    throw new MojoFailureException( e.getMessage(), e );
                }
            }

            if( ARDUINO_CORE_LIB_EXTENSION.equals( arduinoLib.getExtension() ) )
            {
                // a special case: install an empty zip file as this arduino library (just to ignor Maven
                // errors about missing dependency). This library will be handled in a different way later.
                try
                {
                    File emptyZipDir = new File( "target/empty_zip_dir" );
                    emptyZipDir.mkdirs();

                    File emptyZipFile = new File( "target/empty_zip.zip" );
                    CompressUtil.packZip( emptyZipDir, emptyZipFile );

                    installLibrary( arduinoLib, emptyZipFile );
                }
                catch( Exception e )
                {
                    throw new MojoFailureException( e.getMessage(), e );
                }
            }
        }
    }

    private List< Artifact > getMissingArduinoDependencies()
    {
        List< Artifact > missingLibs = new ArrayList<>();

        final List< Artifact > arduinoLibs = collectArduinoDependencies();

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

    private void installLibrary( Artifact arduinoLib, File libraryFile ) throws IOException,
        InstallationException
    {
        InstallRequest installRequest = new InstallRequest();

        Artifact newArtifact = arduinoLib.setFile( libraryFile );
        installRequest.addArtifact( newArtifact );
        repoSystem.install( repoSession, installRequest );
    }

    private void updateArduinoPlatform() throws IOException
    {
        getLog().info( "Updating Arduino platform..." );

        File arduinoPlatformDir = getArduinoPlatformDirFile();
        PlatformToolsManager toolsManager = new PlatformToolsManager( arduinoPlatformDir );
        PlatformRepository platformRepository = getPlatformPackageManager().getPlatformRepository();

        for( Artifact arduinoCore : collectArduinoCoreDependencies() )
        {
            final Platform platform = getPlatform( arduinoCore );
            toolsManager.resolve( platformRepository, platform );
        }

        PlatformLibrariesManager librariesManager = new PlatformLibrariesManager( arduinoPlatformDir );
        librariesManager.update();

        getLog().info( "Arduino platform is up to date." );
    }

    private Platform getPlatform( Artifact arduinoCoreArtifact )
    {
        return getPlatformPackageManager().getPlatformRepository().getPlatform(
            arduinoCoreArtifact.getArtifactId(), arduinoCoreArtifact.getVersion() );
    }

    private List< Artifact > collectArduinoCoreDependencies()
    {
        List< Artifact > result = new ArrayList<>();

        for( Artifact artifact : collectArduinoDependencies() )
        {
            if( ARDUINO_CORE_EXTENSION.equals( artifact.getExtension() ) )
            {
                result.add( artifact );
            }
        }

        return result;
    }

    private List< Artifact > collectArduinoDependencies()
    {
        List< Artifact > result = new ArrayList< Artifact >();

        for( Artifact artifact : collectDependencies() )
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

    private List< Artifact > collectDependencies()
    {
        CollectRequest collectReq = new CollectRequest();

        Artifact projectArtifact = getProjectArtifact();

        DefaultRepositorySystemSession session = new DefaultRepositorySystemSession( repoSession );

        // Set the No-Op Graph transformer so tree stays intact
        session.setDependencyGraphTransformer( new NoopDependencyGraphTransformer() );

        if( ARDUINO_LIB_EXTENSION.equals( projectArtifact.getExtension() ) )
        {
            // for arduinolib add PROVIDED as well (so exclude TEST)
            DependencySelector dependencySelector =
                new AndDependencySelector( new ScopeDependencySelector( JavaScopes.TEST ),
                    new OptionalDependencySelector() );
            session.setDependencySelector( dependencySelector );
        }

        org.eclipse.aether.graph.Dependency dep =
            new org.eclipse.aether.graph.Dependency( projectArtifact, null );

        collectReq.setRoot( dep );

        try
        {
            DependencyRequest depReq = new DependencyRequest();
            depReq.setCollectRequest( collectReq );

            List< Artifact > result = new ArrayList<>();

            DependencyNode dn = repoSystem.collectDependencies( session, collectReq ).getRoot();
            dn.accept( new DependencyVisitor()
            {

                @Override
                public boolean visitEnter( DependencyNode node )
                {
                    final Artifact artifact = node.getArtifact();
                    if( artifact.getGroupId().equals( projectArtifact.getGroupId() )
                        && artifact.getArtifactId().equals( projectArtifact.getArtifactId() ) )
                    {
                        return true;
                    }

                    result.add( artifact );
                    return true;
                }

                @Override
                public boolean visitLeave( DependencyNode aNode )
                {
                    return true;
                }
            } );

            return result;
        }
        catch( DependencyCollectionException exception )
        {
            getLog().warn( "Could not collect dependencies from repo system", exception );
            return null;
        }
    }
}
