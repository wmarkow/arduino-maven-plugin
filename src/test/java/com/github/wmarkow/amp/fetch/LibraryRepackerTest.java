package com.github.wmarkow.amp.fetch;

import java.io.File;
import java.io.IOException;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.junit.Test;

import com.github.wmarkow.amp.ArtifactUtils;

public class LibraryRepackerTest
{

    @Test
    public void testUnpack() throws IOException
    {
        LibraryRepacker repacker = new LibraryRepacker();

        File inputZipFile = new File( "target/arduino-maven-plugin/arduino_ArduinoCore-avr_1.6.23.zip" );
        Artifact artifact = new DefaultArtifact( "com.github.arduino", "arduino-core", "avr", null, "1.6.23" );
        File outputZipFile = new File( inputZipFile.getParent(), ArtifactUtils.getZipFileName( artifact ) );

        repacker.repack( inputZipFile, "/cores/arduino", outputZipFile );
    }
}
