package com.github.wmarkow.amp.arduino.platform;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class PlatformPackageIndexReaderTest
{

    @Test
    public void testReadArduinoIndex() throws IOException
    {
        PlatformPackageIndexReader ppir = new PlatformPackageIndexReader();

        PlatformPackageIndex index = ppir.readFromJson( new File( "src/test/resources/package_index.json" ) );

        assertEquals( 8, index.getPackages().size() );
    }
}


