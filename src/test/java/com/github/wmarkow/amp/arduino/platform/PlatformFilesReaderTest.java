package com.github.wmarkow.amp.arduino.platform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class PlatformFilesReaderTest
{
    @Test
    public void testReadArduinoIndex() throws IOException
    {
        PlatformFilesReader pir = new PlatformFilesReader();

        PlatformPackageIndex index = pir.readFromJson( new File( "src/test/resources/package_index.json" ) );

        assertEquals( 8, index.getPackages().size() );
    }

    @Test
    public void testReadPlatformVariablesFromFile() throws IOException
    {
        PlatformFilesReader pir = new PlatformFilesReader();

        PlatformVariables variables =
            pir.readPlatformVariablesFromFile( new File( "src/test/resources/arduino/platform.txt" ) );

        assertEquals( 73, variables.getNames().size() );
        assertEquals( "Arduino AVR Boards", variables.getVariable( "name" ).getValue() );
        assertEquals( "1.6.23", variables.getVariable( "version" ).getValue() );
    }

    @Test
    public void testReadBoardsVariablesFromFile() throws IOException
    {
        PlatformFilesReader pir = new PlatformFilesReader();

        BoardsVariables variables =
            pir.readBoardsVariables( new File( "src/test/resources/arduino/boards.txt" ) );

        assertEquals( 26, variables.getBoardNames().size() );
    }

    @Test
    public void testReadBoardsVariablesFromFileForUno() throws IOException
    {
        PlatformFilesReader pir = new PlatformFilesReader();

        BoardsVariables variables =
            pir.readBoardsVariables( new File( "src/test/resources/arduino/boards.txt" ) );

        BoardVariables unoBoard = variables.getBoardVariables( "uno" );

        assertNotNull( unoBoard );
        assertEquals( "atmega328p", unoBoard.getVariable( "build.mcu" ).getValue() );
    }
}
