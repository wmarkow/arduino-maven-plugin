package com.github.wmarkow.amp.maven.mojo.build;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformPackageManager;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.maven.mojo.GenericMojo;
import com.github.wmarkow.amp.util.ArtifactUtils;

public abstract class ProcessorMojo extends GenericMojo
{
    private PlatformPackageManager ppm;

    protected File getObjectDir()
    {
        return new File( "target/obj" );
    }

    protected File getCommandExecutionDirectory()
    {
        return new File( "." );
    }

    protected File getArchiveFile()
    {
        final Artifact projectArtifact = getProjectArtifact();
        final String archiveFileName = ArtifactUtils.getBaseFileName( projectArtifact ) + ".ar";
        return new File( "target/" + archiveFileName );
    }

    protected File getElfFile()
    {
        final Artifact projectArtifact = getProjectArtifact();
        final String elfFileName = ArtifactUtils.getBaseFileName( projectArtifact ) + ".elf";
        return new File( "target/" + elfFileName );
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

    protected Platform getPlatform() throws MalformedURLException
    {
        final Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        return getPlatformPackageManager().getPlatform( arduinoCoreArtifact.getArtifactId(),
            arduinoCoreArtifact.getVersion() );
    }

    protected PlatformVariables getPlatformVariables() throws IOException
    {
        final Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        // FIXME: derive path to platfrom.txt correctly
        File platformTxtFile =
            new File( getPathToUnpackedLibrarySourcesDir( arduinoCoreArtifact ), "avr/platform.txt" );

        PlatformFilesReader pfr = new PlatformFilesReader();
        return pfr.readPlatformVariablesFromFile( platformTxtFile );
    }

    protected BoardVariables getBoardVariables()
        throws IOException
    {
        final Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        // FIXME: derive path to boards.txt correctly
        File boardsTxtFile =
            new File( getPathToUnpackedLibrarySourcesDir( arduinoCoreArtifact ), "avr/boards.txt" );

        PlatformFilesReader pfr = new PlatformFilesReader();
        return pfr.readBoardsVariables( boardsTxtFile ).getBoardVariables( getBoard() );
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

    private synchronized PlatformPackageManager getPlatformPackageManager() throws MalformedURLException
    {
        if( ppm == null )
        {
            PlatformPackageManager ppm =
                new PlatformPackageManager( new File( "target/arduino-maven-plugin" ) );
            ppm.addPackageUrl( getPackageIndexUrl() );
            ppm.update();

            this.ppm = ppm;
        }

        return ppm;
    }
}
