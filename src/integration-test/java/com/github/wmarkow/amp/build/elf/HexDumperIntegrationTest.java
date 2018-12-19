package com.github.wmarkow.amp.build.elf;

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
import com.github.wmarkow.amp.build.linker.LinkerIntegrationTest;

@Category( IntegrationTest.class )
public class HexDumperIntegrationTest
{
    private HexDumper hexDumper;
    private File inputElfFile = new File( "target/output.elf" );
    private File outputHexFile = new File( "target/output.hex" );

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

        HexImageCommandBuilder commandBuilder =
            new HexImageCommandBuilder( platform, platformVariables, boardVariables );
        hexDumper = new HexDumper( commandBuilder );
        hexDumper.setCommandExecutionDirectory( new File( "." ) );

        LinkerIntegrationTest lit = new LinkerIntegrationTest();
        lit.init();
        lit.testLink();

        if( outputHexFile.exists() )
        {
            FileUtils.forceDelete( outputHexFile );
        }
    }

    @Test
    public void testHexDumper() throws IOException, InterruptedException
    {
        assertFalse( outputHexFile.exists() );

        hexDumper.makeHex( inputElfFile );

        assertTrue( outputHexFile.exists() );
    }
}
