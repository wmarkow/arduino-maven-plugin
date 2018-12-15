package com.github.wmarkow.amp.build.compiler;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.wmarkow.amp.arduino.platform.BoardVariables;
import com.github.wmarkow.amp.arduino.platform.BoardsVariables;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformVariables;

public class CCompilerCommandBuilderTest
{

    @Test( expected = NullPointerException.class )
    public void testForEmptyVariables()
    {
        CCompilerCommandBuilder builder =
            new CCompilerCommandBuilder( new Platform(), new PlatformVariables(), new BoardVariables() );

        assertEquals( "", builder.buildCommand() );
    }

    @Test
    public void testForArduinoUnoVariables() throws IOException
    {
        PlatformFilesReader pir = new PlatformFilesReader();

        Platform platform =
            pir.readFromJson( new File( "src/test/resources/package_index.json" ) ).getPackage( "arduino" )
                .getPlatformByVersion( "1.6.23" );
        PlatformVariables platformVariables =
            pir.readPlatformVariablesFromFile( new File( "src/test/resources/arduino/platform.txt" ) );
        BoardsVariables boardsVariables =
            pir.readBoardsVariables( new File( "src/test/resources/arduino/boards.txt" ) );

        CCompilerCommandBuilder builder =
            new CCompilerCommandBuilder( platform, platformVariables,
                boardsVariables.getBoardVariables( "uno" ) );

        assertEquals(
            "\"{runtime.tools.avr-gcc.path}/bin/avr-gcc\" -c -g -Os -w -std=gnu11 -ffunction-sections -fdata-sections -MMD -flto -fno-fat-lto-objects -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10609 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR   {includes} \"{source_file}\" -o \"{object_file}\"",
            builder.buildCommand() );
    }

    @Test
    public void testForArduinoUnoVariablesFull() throws IOException
    {
        PlatformFilesReader pir = new PlatformFilesReader();

        Platform platform =
            pir.readFromJson( new File( "src/test/resources/package_index.json" ) ).getPackage( "arduino" )
                .getPlatformByVersion( "1.6.23" );
        PlatformVariables platformVariables =
            pir.readPlatformVariablesFromFile( new File( "src/test/resources/arduino/platform.txt" ) );
        BoardsVariables boardsVariables =
            pir.readBoardsVariables( new File( "src/test/resources/arduino/boards.txt" ) );

        CCompilerCommandBuilder builder =
            new CCompilerCommandBuilder( platform, platformVariables,
                boardsVariables.getBoardVariables( "uno" ) );

        builder.setSourceFile( new File( "src/main/cpp/main.c" ) );
        builder.setObjectFile( new File( "target/obj/main.c.o" ) );
        List< File > includes = new ArrayList< File >();
        includes.add( new File( "src/main/cpp" ) );

        builder.setIncludes( includes );

        assertEquals(
            "\"{runtime.tools.avr-gcc.path}/bin/avr-gcc\" -c -g -Os -w -std=gnu11 -ffunction-sections -fdata-sections -MMD -flto -fno-fat-lto-objects -mmcu=atmega328p -DF_CPU=16000000L -DARDUINO=10609 -DARDUINO_AVR_UNO -DARDUINO_ARCH_AVR   -Isrc\\main\\cpp  \"src\\main\\cpp\\main.c\" -o \"target\\obj\\main.c.o\"",
            builder.buildCommand() );
    }
}
