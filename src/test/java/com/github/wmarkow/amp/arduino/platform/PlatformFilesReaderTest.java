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
    public void testReadArduinoIndexForToolsDependencies() throws IOException
    {
        PlatformFilesReader pir = new PlatformFilesReader();

        PlatformPackageIndex index = pir.readFromJson( new File( "src/test/resources/package_index.json" ) );

        Platform platform = index.getPackage( "arduino" ).getPlatformByVersion( "1.6.23" );

        assertEquals( 3, platform.getToolsDependencies().size() );

        assertEquals( "arduino", platform.getToolsDependencies().get( 0 ).getPackager() );
        assertEquals( "avr-gcc", platform.getToolsDependencies().get( 0 ).getName() );
        assertEquals( "5.4.0-atmel3.6.1-arduino2", platform.getToolsDependencies().get( 0 ).getVersion() );
    }

    @Test
    public void testReadArduinoIndexForTools() throws IOException
    {
        PlatformFilesReader pir = new PlatformFilesReader();

        PlatformPackageIndex index = pir.readFromJson( new File( "src/test/resources/package_index.json" ) );

        assertEquals( 44, index.getPackage( "arduino" ).getTools().size() );
        final Tool tool =
            index.getPackage( "arduino" ).getToolByNameAndVersion( "avr-gcc", "5.4.0-atmel3.6.1-arduino2" );
        assertEquals( "avr-gcc", tool.getName() );
        assertEquals( "5.4.0-atmel3.6.1-arduino2", tool.getVersion() );

        assertEquals( 6, tool.getSystems().size() );

        assertEquals( "arm-linux-gnueabihf", tool.getSystems().get( 0 ).getHost() );
        assertEquals( "avr-gcc-5.4.0-atmel3.6.1-arduino2-armhf-pc-linux-gnu.tar.bz2",
            tool.getSystems().get( 0 ).getArchiveFileName() );
        assertEquals(
            "http://downloads.arduino.cc/tools/avr-gcc-5.4.0-atmel3.6.1-arduino2-armhf-pc-linux-gnu.tar.bz2",
            tool.getSystems().get( 0 ).getUrl() );
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
