package com.github.wmarkow.amp.maven.artifact.resolver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.junit.Test;

import com.github.wmarkow.amp.arduino.platform.Library;
import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformLibrariesIndex;

public class ArduinloLibraryIndexArtifactResolverTest
{

    @Test
    public void testFindRF24() throws IOException
    {
        PlatformFilesReader pir = new PlatformFilesReader();

        PlatformLibrariesIndex index =
            pir.readPlatformLibrariesIndexFromJsonFile( new File( "src/test/resources/library_index.json" ) );

        ArduinloLibraryIndexArtifactResolver resolver = new ArduinloLibraryIndexArtifactResolver( index );
        Artifact artifact = new DefaultArtifact( "com.github.TMRh20", "RF24", null, "1.3.1" );
        Library library = resolver.findLibrary( artifact );

        assertEquals( "RF24", library.getName() );
        assertEquals( "1.3.1", library.getVersion() );
        assertEquals( "RF24-1.3.1.zip", library.getArchiveFileName() );
    }

    @Test
    public void testFindWmarkowRF24() throws IOException
    {
        PlatformFilesReader pir = new PlatformFilesReader();

        PlatformLibrariesIndex index =
            pir.readPlatformLibrariesIndexFromJsonFile( new File( "src/test/resources/library_index.json" ) );

        ArduinloLibraryIndexArtifactResolver resolver = new ArduinloLibraryIndexArtifactResolver( index );
        Artifact artifact = new DefaultArtifact( "com.github.wmarkow", "RF24", null, "1.3.1" );

        assertNull( resolver.findLibrary( artifact ) );
    }
}


