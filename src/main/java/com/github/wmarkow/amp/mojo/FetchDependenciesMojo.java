package com.github.wmarkow.amp.mojo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.installation.InstallRequest;
import org.eclipse.aether.installation.InstallationException;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;

import com.github.wmarkow.amp.ArtifactUtils;
import com.github.wmarkow.amp.fetch.GithubFetchDescriptor;
import com.github.wmarkow.amp.fetch.GithubFetcher;
import com.github.wmarkow.amp.fetch.LibraryRepacker;

@Mojo( name = "fetch-dependencies", defaultPhase = LifecyclePhase.INITIALIZE, requiresProject = true )
public class FetchDependenciesMojo extends ArduinoAbstractMojo
{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        List< Artifact > arduinoLibs = getMissingArduinoLibs();

        for( Artifact arduinoLib : arduinoLibs )
        {
            try
            {
                File file = prepareLibrary( arduinoLib );

                installLibrary( arduinoLib, file );
            }
            catch( IOException e )
            {
                throw new MojoFailureException( e.getMessage() );
            }
            catch( InstallationException e )
            {
                throw new MojoFailureException( e.getMessage() );
            }
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

    private File prepareLibrary( Artifact arduinoLib ) throws IOException
    {
        getLog().info( "Preparing library for " + artifactToString( arduinoLib ) );

        if( "com.github.arduino".equals( arduinoLib.getGroupId() )
            && "arduino-core".equals( arduinoLib.getArtifactId() )
            && "1.6.23".equals( arduinoLib.getBaseVersion() ) && "avr".equals( arduinoLib.getClassifier() ) )
        {
            // fetch com.github.arduino:arduino-core-1.6.23-avr
            GithubFetcher githubFetcher = new GithubFetcher();
            GithubFetchDescriptor descriptor = new GithubFetchDescriptor();
            descriptor.username = "arduino";
            descriptor.repoName = "ArduinoCore-avr";
            descriptor.refName = "1.6.23";

            File targetDir = new File( "target/arduino-maven-plugin" );
            FileUtils.forceMkdir( targetDir );

            File fetchedSources = githubFetcher.fetchLibrary( descriptor, targetDir );

            LibraryRepacker repacker = new LibraryRepacker();
            File arduinoZipLibrary = new File( targetDir, ArtifactUtils.getZipFileName( arduinoLib ) );
            repacker.repack( fetchedSources, "cores/arduino", arduinoZipLibrary );

            return arduinoZipLibrary;
        }

        if( "com.github.arduino".equals( arduinoLib.getGroupId() )
            && "arduino-variant".equals( arduinoLib.getArtifactId() )
            && "1.6.23".equals( arduinoLib.getBaseVersion() )
            && "avr-standard".equals( arduinoLib.getClassifier() ) )
        {
            // fetch com.github.arduino:arduino-core-1.6.23-avr
            GithubFetcher githubFetcher = new GithubFetcher();
            GithubFetchDescriptor descriptor = new GithubFetchDescriptor();
            descriptor.username = "arduino";
            descriptor.repoName = "ArduinoCore-avr";
            descriptor.refName = "1.6.23";

            File targetDir = new File( "target/arduino-maven-plugin" );
            FileUtils.forceMkdir( targetDir );

            File fetchedSources = githubFetcher.fetchLibrary( descriptor, targetDir );

            LibraryRepacker repacker = new LibraryRepacker();
            File arduinoZipLibrary = new File( targetDir, ArtifactUtils.getZipFileName( arduinoLib ) );
            repacker.repack( fetchedSources, "variants/standard", arduinoZipLibrary );

            return arduinoZipLibrary;
        }

        return null;
    }

    private void installLibrary( Artifact arduinoLib, File libraryFile ) throws IOException,
        InstallationException
    {
        InstallRequest installRequest = new InstallRequest();

        Artifact newArtifact = arduinoLib.setFile( libraryFile );
        installRequest.addArtifact( newArtifact );
        repoSystem.install( repoSession, installRequest );
    }
}
