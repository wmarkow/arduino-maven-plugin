package com.github.wmarkow.amp.platform;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class PlatformPackageManagerTest
{

    @Test
    public void testRead() throws IOException
    {
        PlatformPackageManager ppm = new PlatformPackageManager();
        ppm.addAndParsePackageIndex( new File( "src/test/resources/package_index.json" ) );
        ppm.addAndParsePackageIndex( new File( "src/test/resources/package_esp8266com_index.json" ) );

        for( String asd : ppm.getLatestPlatformsAsArtifacts() )
        {
            System.out.println( asd );
        }

        // PlatformPackageIndex ppi2 =
        // reader.readFromJson( new File( "src/test/resources/package_esp8266com_index.json" ) );

        // assertEquals( 8, ppi.getPackages().size() );
    }
}
