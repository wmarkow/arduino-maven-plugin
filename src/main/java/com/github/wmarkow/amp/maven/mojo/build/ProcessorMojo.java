package com.github.wmarkow.amp.maven.mojo.build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.aether.artifact.Artifact;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.arduino.platform.manager.PlatformToolsManager;
import com.github.wmarkow.amp.arduino.variable.Variable;
import com.github.wmarkow.amp.maven.mojo.GenericMojo;
import com.github.wmarkow.amp.util.AmpFileUtils;
import com.github.wmarkow.amp.util.ArtifactUtils;

public abstract class ProcessorMojo extends GenericMojo
{
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

    protected PlatformVariables getPlatformVariables() throws IOException
    {
        File platformTxtFile = new File( getPathToUnpackedCoreLibrary(), "/platform.txt" );

        PlatformFilesReader pfr = new PlatformFilesReader();
        return pfr.readPlatformVariablesFromFile( platformTxtFile );
    }

    protected BoardVariables getBoardVariables() throws IOException
    {
        File boardsTxtFile = new File( getPathToUnpackedCoreLibrary(), "/boards.txt" );

        PlatformFilesReader pfr = new PlatformFilesReader();
        BoardVariables boardVariables =
            pfr.readBoardsVariables( boardsTxtFile ).getBoardVariables( getBoard() );

        final Map< String, String > buildVariables = getBuildVariables();
        for( String key : buildVariables.keySet() )
        {
            boardVariables.putVariable( new Variable( key, buildVariables.get( key ) ) );
        }

        return boardVariables;
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

    protected File[] getPathToUnpackedCoreLibrarySourcesDir( Artifact artifact, String core,
        String variant )
    {
        File baseDir = getPathToUnpackedCoreLibrary( artifact );

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

    protected File getPathToUnpackedCoreLibrary()
    {
        final Artifact arduinoCoreArtifact = getArduinoCoreArtifact();

        return getPathToUnpackedCoreLibrary( arduinoCoreArtifact );
    }

    protected File getPathToUnpackedCoreLibrary( Artifact arduinoCoreArtifact )
    {
        File baseDir =
            new File( getGeneratedSourcesDirFile(), ArtifactUtils.getBaseFileName( arduinoCoreArtifact ) );

        return AmpFileUtils.stepIntoSingleFolderIfPossible( baseDir );
    }
}
