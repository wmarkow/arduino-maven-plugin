package com.github.wmarkow.amp.build.linker;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.wmarkow.amp.IntegrationTest;
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformPackageIndex;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.build.archiver.ArchiverIntegrationTest;
import com.github.wmarkow.amp.build.linker.Linker;
import com.github.wmarkow.amp.build.linker.LinkerCommandBuilder;

@Category( IntegrationTest.class )
public class LinkerIntegrationTest
{
    Linker linker;
    File outputElfFile = new File( "target/output.elf" );

    @Before
    public void init() throws IOException, InterruptedException
    {
        PlatformFilesReader pfr = new PlatformFilesReader();

        PlatformPackageIndex platformPackageIndex =
            pfr.readFromJson( new File( "src/test/resources/package_index.json" ) );
        Platform platform = platformPackageIndex.getPackage( "arduino" ).getPlatformByVersion( "1.6.17" );
        PlatformVariables platformVariables =
            pfr.readPlatformVariablesFromFile( new File( "src/test/resources/arduino/platform.txt" ) );
        BoardVariables boardVariables =
            pfr.readBoardsVariables( new File( "src/test/resources/arduino/boards.txt" ) ).getBoardVariables(
                "uno" );

        LinkerCommandBuilder lcb = new LinkerCommandBuilder( platform, platformVariables, boardVariables );
        linker = new Linker( lcb );

        linker.setCommandExecutionDirectory( new File( "." ) );

        if( outputElfFile.exists() )
        {
            FileUtils.forceDelete( outputElfFile );
        }

        ArchiverIntegrationTest compiler = new ArchiverIntegrationTest();
        compiler.init();
        compiler.testArchive();
    }

    @Test
    public void testLink() throws IOException, InterruptedException
    {
        assertFalse( outputElfFile.exists() );

        linker.link( new File( "target/obj" ), outputElfFile );

        assertTrue( outputElfFile.exists() );
    }
}
