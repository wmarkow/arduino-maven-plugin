package com.github.wmarkow.amp.build.compiler;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class SCompilerIntegrationTest
{
    SCompiler compiler;
    File objDir = new File( "target/obj" );

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

        compiler =
            new SCompiler( new CppCompilerCommandBuilder( platform, platformVariables, boardVariables ) );

        compiler.setCommand( "avr-gcc" );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-blink-project" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler.addSrcDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.addIncludeDirectory( new File( "src/test/resources/arduino-core-1.6.17-avr/src" ) );
        compiler
            .addIncludeDirectory( new File( "src/test/resources/arduino-variant-1.6.17-avr-standard/src" ) );
        compiler.setObjDirectory( new File( "target/obj" ) );
        compiler.setCommandExecutionDirectory( new File( "." ) );

        compiler.addCommandArgs( getDefaultCommandArgs() );

        FileUtils.cleanDirectory( objDir );
        assertEquals( 0, FileUtils.listFiles( objDir, new String[]
        { "o" }, true ).size() );
    }

    @Test
    public void testCompile() throws IOException, InterruptedException
    {
        compiler.compile();

        assertEquals( 1, FileUtils.listFiles( objDir, new String[]
        { "o" }, true ).size() );
    }

    public final static List< String > getDefaultCommandArgs()
    {
        List< String > args = new ArrayList< String >();

        args.add( "-c" );
        args.add( "-g" );
        args.add( "-x" );
        args.add( "assembler-with-cpp" );
        args.add( "-flto" );
        args.add( "-MMD" );
        args.add( "-MP" );
        args.add( "-mmcu=atmega328p" );
        args.add( "-DF_CPU=16000000L" );
        args.add( "-DARDUINO=10609" );
        args.add( "-DARDUINO_AVR_UNO" );
        args.add( "-DARDUINO_ARCH_AVR" );

        return args;
    }
}
