package com.github.wmarkow.amp.build;

import static org.junit.Assert.assertEquals;
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
import com.github.wmarkow.amp.build.archiver.Archiver;
import com.github.wmarkow.amp.build.archiver.ArchiverCommandBuilder;
import com.github.wmarkow.amp.build.compiler.Compiler;
import com.github.wmarkow.amp.build.imager.EepromDumper;
import com.github.wmarkow.amp.build.imager.EepromImageCommandBuilder;
import com.github.wmarkow.amp.build.imager.HexDumper;
import com.github.wmarkow.amp.build.imager.HexImageCommandBuilder;
import com.github.wmarkow.amp.build.linker.Linker;
import com.github.wmarkow.amp.build.linker.LinkerCommandBuilder;

@Category( IntegrationTest.class )
public class BuildAllIntegrationTest
{
    /* Platform */
    private Platform platform;
    private PlatformVariables platformVariables;
    private BoardVariables boardVariables;

    /* Processors */
    private Compiler compiler;
    private Archiver archiver;
    private Linker linker;
    private EepromDumper eepromDumper;
    private HexDumper hexDumper;

    /* Files */
    private File objDir = new File( "target/obj" );
    private File arFile = new File( "target/output.ar" );
    private File elfFile = new File( "target/output.elf" );
    private File eepFile = new File( "target/output.eep" );
    private File hexFile = new File( "target/output.hex" );

    @Test
    public void testBuildAll() throws IOException, InterruptedException
    {
        testCompiler();
        testArchiver();
        testLinker();
        testHexImager();
        testEepromImager();
    }

    private void testCompiler() throws IOException, InterruptedException
    {
        assertEquals( 0, FileUtils.listFiles( objDir, new String[]
        { "o" }, true ).size() );

        compiler.compile();

        assertEquals( 26, FileUtils.listFiles( objDir, new String[]
        { "o" }, true ).size() );
    }

    private void testArchiver() throws IOException, InterruptedException
    {
        assertFalse( arFile.exists() );

        archiver.archive( objDir, arFile );

        assertTrue( arFile.exists() );
    }

    private void testLinker() throws IOException, InterruptedException
    {
        assertFalse( elfFile.exists() );

        linker.link( objDir, elfFile );

        assertTrue( elfFile.exists() );
    }

    private void testHexImager() throws IOException, InterruptedException
    {
        assertFalse( hexFile.exists() );

        hexDumper.makeHex( elfFile );

        assertTrue( hexFile.exists() );
    }

    private void testEepromImager() throws IOException, InterruptedException
    {
        assertFalse( eepFile.exists() );

        eepromDumper.makeEeprom( elfFile );

        assertTrue( eepFile.exists() );
    }

    @Before
    public void init() throws IOException
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

        compiler = new Compiler( platform, platformVariables, boardVariables );

        compiler.addSrcDirectory( new File( "src/test/resources/arduino-blink-project" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.addIncludeDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler
            .addIncludeDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.setObjDirectory( objDir );
        compiler.setCommandExecutionDirectory( new File( "." ) );

        archiver = new Archiver( new ArchiverCommandBuilder( platform, platformVariables, boardVariables ) );
        archiver.setCommandExecutionDirectory( new File( "." ) );

        linker = new Linker( new LinkerCommandBuilder( platform, platformVariables, boardVariables ) );
        linker.setCommandExecutionDirectory( new File( "." ) );

        eepromDumper =
            new EepromDumper( new EepromImageCommandBuilder( platform, platformVariables, boardVariables ) );
        eepromDumper.setCommandExecutionDirectory( new File( "." ) );

        hexDumper = new HexDumper( new HexImageCommandBuilder( platform, platformVariables, boardVariables ) );
        hexDumper.setCommandExecutionDirectory( new File( "." ) );

        FileUtils.forceMkdir( objDir );
        FileUtils.cleanDirectory( objDir );
        FileUtils.deleteQuietly( arFile );
        FileUtils.deleteQuietly( elfFile );
        FileUtils.deleteQuietly( eepFile );
        FileUtils.deleteQuietly( hexFile );
    }

}
