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
import com.github.wmarkow.amp.build.compiler.CompilerIntegrationTest;

@Category( IntegrationTest.class )
public class ArchiverIntegrationTest
{
    Archiver archiver;
    File outputArchiveFile = new File( "target/output.ar" );

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

        ArchiverCommandBuilder lcb = new ArchiverCommandBuilder( platform, platformVariables, boardVariables );
        archiver = new Archiver( lcb );

        archiver.setCommandExecutionDirectory( new File( "." ) );

        if( outputArchiveFile.exists() )
        {
            FileUtils.forceDelete( outputArchiveFile );
        }

        CompilerIntegrationTest compiler = new CompilerIntegrationTest();
        compiler.init();
        compiler.testCompile();
    }

    @Test
    public void testArchive() throws IOException, InterruptedException
    {
        assertFalse( outputArchiveFile.exists() );

        archiver.archive( new File( "target/obj" ), outputArchiveFile );

        assertTrue( outputArchiveFile.exists() );
    }
}
