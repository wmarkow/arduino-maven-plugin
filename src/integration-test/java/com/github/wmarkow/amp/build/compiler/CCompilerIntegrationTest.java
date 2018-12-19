package com.github.wmarkow.amp.build.compiler;

import static org.junit.Assert.assertEquals;

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

@Category( IntegrationTest.class )
public class CCompilerIntegrationTest
{
    private CCompiler compiler;
    private File objDir = new File( "target/obj" );

    @Before
    public void init() throws IOException
    {
        FileUtils.forceMkdir( objDir );

        PlatformFilesReader pfr = new PlatformFilesReader();

        PlatformPackageIndex platformPackageIndex =
            pfr.readFromJson( new File( "src/test/resources/package_index.json" ) );
        Platform platform = platformPackageIndex.getPackage( "arduino" ).getPlatformByVersion( "1.6.17" );
        PlatformVariables platformVariables =
            pfr.readPlatformVariablesFromFile( new File( "src/test/resources/arduino/platform.txt" ) );
        BoardVariables boardVariables =
            pfr.readBoardsVariables( new File( "src/test/resources/arduino/boards.txt" ) ).getBoardVariables(
                "uno" );

        compiler = new CCompiler( new CCompilerCommandBuilder( platform, platformVariables, boardVariables ) );

        compiler.addSrcDirectory( new File( "src/test/resources/arduino-blink-project" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.addIncludeDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler
            .addIncludeDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.setObjDirectory( objDir );
        compiler.setCommandExecutionDirectory( new File( "." ) );

        FileUtils.cleanDirectory( objDir );
        assertEquals( 0, FileUtils.listFiles( objDir, new String[]
        { "o" }, true ).size() );
    }

    @Test
    public void testCompile() throws IOException, InterruptedException
    {
        compiler.compile();

        assertEquals( 7, FileUtils.listFiles( objDir, new String[]
        { "o" }, true ).size() );
    }
}
