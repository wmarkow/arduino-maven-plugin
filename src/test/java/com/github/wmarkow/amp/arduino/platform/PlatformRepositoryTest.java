package com.github.wmarkow.amp.arduino.platform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class PlatformRepositoryTest
{

    private PlatformRepository platformRepository;

    @Before
    public void test() throws IOException
    {
        platformRepository = new PlatformRepository();

        PlatformFilesReader reader = new PlatformFilesReader();

        platformRepository.addIndex( reader.readFromJson( new File(
            "src/test/resources/package_digistump_index.json" ) ) );
    }

    @Test
    public void testGetPlatformForDigistump() throws IOException
    {
        assertNotNull( platformRepository.getPlatform( "digistump-avr", "1.6.7" ) );

        assertEquals( 2, platformRepository.getPlatform( "digistump-avr", "1.6.7" ).getToolsDependencies()
            .size() );

        ToolsDependency td =
            platformRepository.getPlatform( "digistump-avr", "1.6.7" ).getToolsDependencies().get( 0 );
        assertEquals( "arduino", td.getPackager() );
        assertEquals( "avr-gcc", td.getName() );
        assertEquals( "4.8.1-arduino5", td.getVersion() );
    }

    @Test
    public void testGetToolByPackagerAndNameAndVersionWhenMissingArduinoIndex()
    {
        assertNull( platformRepository.getToolByPackagerAndNameAndVersion( "arduino", "avr-gcc",
            "4.8.1-arduino5" ) );
    }

    @Test
    public void testGetToolByPackagerAndNameAndVersionWhenArduinoIndex() throws IOException
    {
        PlatformFilesReader reader = new PlatformFilesReader();
        platformRepository.addIndex( reader
            .readFromJson( new File( "src/test/resources/package_index.json" ) ) );

        assertNotNull( platformRepository.getToolByPackagerAndNameAndVersion( "arduino", "avr-gcc",
            "4.8.1-arduino5" ) );
    }
}
