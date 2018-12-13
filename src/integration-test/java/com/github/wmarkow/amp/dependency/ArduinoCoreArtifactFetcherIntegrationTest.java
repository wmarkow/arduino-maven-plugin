package com.github.wmarkow.amp.dependency;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.github.wmarkow.amp.arduino.platform.PlatformPackageManager;
import com.github.wmarkow.amp.dependency.ArduinoCoreArtifactFetcher;

public class ArduinoCoreArtifactFetcherIntegrationTest
{
    private File workDir = new File( "target/amp" );
    private File fetchDir = new File( "target/arduino-maven-plugin/" );
    private PlatformPackageManager ppm;

    @Before
    public void init() throws IOException
    {
        ppm = new PlatformPackageManager( workDir );

        ppm.addPackageUrl( new URL( "https://downloads.arduino.cc/packages/package_index.json" ) );

        FileUtils.forceMkdir( workDir );

        ppm.update();

        FileUtils.forceMkdir( fetchDir );
        FileUtils.cleanDirectory( fetchDir );
    }

    @Test
    public void testFetchArduinoCore() throws IOException
    {
        ArduinoCoreArtifactFetcher fetcher = new ArduinoCoreArtifactFetcher( ppm );

        File file = fetcher.fetch( "arduino-avr", "1.6.23", fetchDir );

        assertTrue( file.exists() );
    }
}

