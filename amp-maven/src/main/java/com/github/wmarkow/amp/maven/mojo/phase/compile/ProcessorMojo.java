package com.github.wmarkow.amp.maven.mojo.phase.compile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.arduino.platform.manager.PlatformToolsManager;
import com.github.wmarkow.amp.arduino.variable.Variable;
import com.github.wmarkow.amp.maven.mojo.GenericBoardMojo;
import com.github.wmarkow.amp.util.ArtifactUtils;

public abstract class ProcessorMojo extends GenericBoardMojo
{
    @Parameter( property = "arduino-maven-plugin.buildVariables", required = false )
    private Map< String, String > buildVariables;

    protected File getObjectDir()
    {
        return new File( "target/obj" );
    }

    protected File getCommandExecutionDirectory()
    {
        return new File( "." );
    }

    protected Map< String, String > getBuildVariables()
    {
        return buildVariables;
    }

    protected BoardVariables getBoardVariables() throws IOException
    {
        BoardVariables boardVariables = getBoardsVariables().getBoardVariables( getBoard() );

        final Map< String, String > buildVariables = getBuildVariables();
        for( String key : buildVariables.keySet() )
        {
            boardVariables.putVariable( new Variable( key, buildVariables.get( key ) ) );
        }

        return boardVariables;
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

    protected PlatformVariables getPlatformVariables() throws IOException
    {
        File platformTxtFile = new File( getPathToUnpackedArduinoCore(), "/platform.txt" );

        PlatformFilesReader pfr = new PlatformFilesReader();
        return pfr.readPlatformVariablesFromFile( platformTxtFile );
    }

    protected File getPathToUnpackedLibrarySourcesDir( Artifact artifact )
    {
        File baseDir = new File( getGeneratedSourcesDirFile(), ArtifactUtils.getBaseFileName( artifact ) );

        File dirWithSrc = new File( baseDir, "src" );
        if( dirWithSrc.exists() )
        {
            return dirWithSrc;
        }

        return baseDir;
    }

    protected File getPathToUnpackedCoreLibrarySourcesDir( String libraryName )
    {
        File baseDir = getPathToUnpackedArduinoCore();
        File coreLibrariesDir = new File( baseDir, "libraries" );
        File libraryDir = new File( coreLibrariesDir, libraryName );

        if( !libraryDir.exists() )
        {
            return null;
        }

        File libraryDirWithSrc = new File( libraryDir, "src" );
        if( libraryDirWithSrc.exists() )
        {
            return libraryDirWithSrc;
        }

        return libraryDir;
    }

    protected File[] getPathToUnpackedArduinoCoreSourcesDir( String core, String variant )
    {
        File baseDir = getPathToUnpackedArduinoCore();

        List< File > result = new ArrayList< File >();
        result.add( new File( baseDir, "/cores/" + core ) );
        result.add( new File( baseDir, "/variants/" + variant ) );

        return result.toArray( new File[]
        {} );
    }

    protected String getToolChainBinDirPath()
    {
        File arduinoPlatformDir = getArduinoPlatformDirFile();
        PlatformToolsManager toolsManager = new PlatformToolsManager( arduinoPlatformDir );

        File toolchainBinDirPath =
            toolsManager.getToolchainBinDirPath( getPlatformPackageManager().getPlatformRepository(),
                getPlatform() );

        return toolchainBinDirPath.getPath() + "/";
    }
}
