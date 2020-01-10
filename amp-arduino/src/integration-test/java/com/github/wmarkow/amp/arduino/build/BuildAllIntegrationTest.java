package com.github.wmarkow.amp.arduino.build;

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
import com.github.wmarkow.amp.arduino.build.archiver.Archiver;
import com.github.wmarkow.amp.arduino.build.archiver.ArchiverCommandBuilder;
import com.github.wmarkow.amp.arduino.build.compiler.Compiler;
import com.github.wmarkow.amp.arduino.build.imager.EepromDumper;
import com.github.wmarkow.amp.arduino.build.imager.EepromImageCommandBuilder;
import com.github.wmarkow.amp.arduino.build.imager.HexDumper;
import com.github.wmarkow.amp.arduino.build.imager.HexImageCommandBuilder;
import com.github.wmarkow.amp.arduino.build.linker.Linker;
import com.github.wmarkow.amp.arduino.build.linker.LinkerCommandBuilder;
import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.Package;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformRepository;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;
import com.github.wmarkow.amp.arduino.platform.manager.PlatformToolsManager;

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
        PlatformRepository platformRepository = new PlatformRepository();
        platformRepository.addIndex( pfr.readFromJson( new File( "src/test/resources/package_index.json" ) ) );

        Package _package = platformRepository.getPackageByName( "arduino" );
        Platform platform = _package.getPlatformByVersion( "1.6.17" );

        // update Arduino Platform so 1.6.17 is available
        PlatformToolsManager tm = new PlatformToolsManager( getPlatformDir() );
        tm.resolve( platformRepository, platform );

        PlatformVariables platformVariables =
            pfr.readPlatformVariablesFromFile( new File( "src/test/resources/arduino/platform.txt" ) );
        BoardVariables boardVariables =
            pfr.readBoardsVariables( new File( "src/test/resources/arduino/boards.txt" ) ).getBoardVariables(
                "uno" );

        compiler = new Compiler( platform, platformVariables, boardVariables );
        PlatformToolsManager toolsManager = new PlatformToolsManager( getPlatformDir() );
        File toolchainBinDirPath = toolsManager.getToolchainBinDirPath( platformRepository, platform );

        compiler.setToolchainBinDirPath( toolchainBinDirPath.getPath() + "/" );

        compiler.addSrcDirectory( new File( "src/integration-test/resources/arduino-blink-project" ) );
        compiler.addSrcDirectory( new File( "src/integration-test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler.addSrcDirectory( new File( "src/integration-test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.addIncludeDirectory( new File( "src/integration-test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler
            .addIncludeDirectory( new File( "src/integration-test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.setObjDirectory( objDir );
        compiler.setCommandExecutionDirectory( new File( "." ) );

        ArchiverCommandBuilder acb = new ArchiverCommandBuilder( platform, platformVariables, boardVariables );
        acb.setToolchainBinDirPath( toolchainBinDirPath.getPath() + "/" );
        archiver = new Archiver( acb );
        archiver.setCommandExecutionDirectory( new File( "." ) );

        LinkerCommandBuilder lcb = new LinkerCommandBuilder( platform, platformVariables, boardVariables );
        lcb.setToolchainBinDirPath( toolchainBinDirPath.getPath() + "/" );
        linker = new Linker( lcb );
        linker.setCommandExecutionDirectory( new File( "." ) );

        EepromImageCommandBuilder eicb =
            new EepromImageCommandBuilder( platform, platformVariables, boardVariables );
        eicb.setToolchainBinDirPath( toolchainBinDirPath + "/" );
        eepromDumper = new EepromDumper( eicb );
        eepromDumper.setCommandExecutionDirectory( new File( "." ) );

        HexImageCommandBuilder hicb =
            new HexImageCommandBuilder( platform, platformVariables, boardVariables );
        hicb.setToolchainBinDirPath( toolchainBinDirPath + "/" );
        hexDumper = new HexDumper( hicb );
        hexDumper.setCommandExecutionDirectory( new File( "." ) );

        FileUtils.forceMkdir( objDir );
        FileUtils.cleanDirectory( objDir );
        FileUtils.deleteQuietly( arFile );
        FileUtils.deleteQuietly( elfFile );
        FileUtils.deleteQuietly( eepFile );
        FileUtils.deleteQuietly( hexFile );
    }

    private File getPlatformDir()
    {
        String path = System.getProperty( "user.home" );
        return new File( path, ".arduino-maven-plugin" );
    }
}
