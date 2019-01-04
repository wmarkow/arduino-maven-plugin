package com.github.wmarkow.amp.arduino.platform.manager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.junit.Before;
import org.junit.Test;

import com.github.wmarkow.amp.arduino.platform.Package;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformPackageIndex;
import com.github.wmarkow.amp.arduino.platform.manager.PlatformToolsManager;

public class PlatformToolsManagerIntegrationTest
{
    private final static File PLATFORM_DIR = new File( "target/arduino-maven-plugin" );

    @Before
    public void init() throws IOException
    {
        File downloadsDir = new File( PLATFORM_DIR, "downloads" );

        if( downloadsDir.exists() )
        {
            FileUtils.cleanDirectory( downloadsDir );
        }
    }

    public void test()
    {
        java.lang.System.getProperty( "os.arch" );
        java.lang.System.getProperty( "sun.desktop" );
        java.lang.System.getProperties();
    }

    @Test
    public void testResolveForArduinoWindows() throws IOException
    {
        final File downloadsDir = new File( PLATFORM_DIR, "downloads" );
        assertFalse( new File( downloadsDir, "arduinoOTA-1.2.1-windows_386.zip" ).exists() );
        assertFalse( new File( downloadsDir, "avrdude-6.3.0-arduino14-i686-w64-mingw32.zip" ).exists() );
        assertFalse( new File( downloadsDir, "avr-gcc-5.4.0-atmel3.6.1-arduino2-i686-w64-mingw32.zip" )
            .exists() );

        PlatformFilesReader pir = new PlatformFilesReader();
        PlatformPackageIndex index = pir.readFromJson( new File( "src/test/resources/package_index.json" ) );

        PlatformToolsManager tm = new PlatformToolsManager( PLATFORM_DIR );

        Package _package = index.getPackage( "arduino" );
        Platform platform = _package.getPlatformByVersion( "1.6.23" );

        tm.resolve( _package, platform );

        assertTrue( new File( downloadsDir, "arduinoOTA-1.2.1-windows_386.zip" ).exists() );
        assertTrue( new File( downloadsDir, "avrdude-6.3.0-arduino14-i686-w64-mingw32.zip" ).exists() );
        assertTrue( new File( downloadsDir, "avr-gcc-5.4.0-atmel3.6.1-arduino2-i686-w64-mingw32.zip" )
            .exists() );
    }
}