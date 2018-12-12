package com.github.wmarkow.amp.arduino.platform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.github.wmarkow.amp.arduino.platform.PlatformPackageManager;

public class PlatformPackageManagerIntegrationTest
{
    private File workDir = new File( "target/amp" );
    private PlatformPackageManager ppm;

    @Before
    public void init() throws IOException
    {
        ppm = new PlatformPackageManager( workDir );

        ppm.addPackageUrl( new URL( "https://downloads.arduino.cc/packages/package_index.json" ) );
        ppm.addPackageUrl( new URL( "http://arduino.esp8266.com/stable/package_esp8266com_index.json" ) );
        ppm.addPackageUrl( new URL(
            "https://raw.githubusercontent.com/digistump/arduino-boards-index/master/package_digistump_index.json" ) );

        FileUtils.forceMkdir( workDir );
        FileUtils.cleanDirectory( workDir );
    }

    @Test
    public void testUpdate()
    {
        assertEquals( 0, FileUtils.listFiles( workDir, new String[]
        { "json" }, false ).size() );

        ppm.update();

        assertEquals( 3, FileUtils.listFiles( workDir, new String[]
        { "json" }, false ).size() );
        assertTrue( new File( workDir, "package_index.json" ).exists() );
        assertTrue( new File( workDir, "package_esp8266com_index.json" ).exists() );
        assertTrue( new File( workDir, "package_digistump_index.json" ).exists() );
    }
}
