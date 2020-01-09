package com.github.wmarkow.amp.maven.artifact.resolver;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.junit.Test;

import com.github.wmarkow.amp.maven.artifact.resolver.LibraryRepacker;
import com.github.wmarkow.amp.util.ArtifactUtils;

public class LibraryRepackerTest
{

    @Test
    public void testUnpackForZipWithRootNoSrc() throws IOException
    {
        LibraryRepacker repacker = new LibraryRepacker();

        File inputZipFile =
            new File( "target/test-classes/com/github/wmarkow/amp/fetch/test_sources_with_root_no_src.zip" );
        Artifact artifact = new DefaultArtifact( "default", "arduino-core-1", "avr", null, "1.6.23" );
        File outputZipFile = new File( inputZipFile.getParent(), ArtifactUtils.getZipFileName( artifact ) );

        repacker.repack( inputZipFile, "/cores/arduino", outputZipFile );

        assertTrue( outputZipFile.exists() );

        ZipFile zipFile = new ZipFile( outputZipFile );

        assertNotNull( zipFile.getEntry( "src/abi.cpp" ) );
        assertNotNull( zipFile.getEntry( "src/Arduino.h" ) );
        assertNotNull( zipFile.getEntry( "src/main.cpp" ) );
        assertNotNull( zipFile.getEntry( "src/wiring.c" ) );
        assertNull( zipFile.getEntry( "abi.cpp" ) );
        assertNull( zipFile.getEntry( "Arduino.h" ) );
        assertNull( zipFile.getEntry( "main.cpp" ) );
        assertNull( zipFile.getEntry( "wiring.c" ) );

        zipFile.close();
    }

    @Test
    public void testUnpackForZipWithRootWithSrc() throws IOException
    {
        LibraryRepacker repacker = new LibraryRepacker();

        File inputZipFile =
            new File( "target/test-classes/com/github/wmarkow/amp/fetch/test_sources_with_root_with_src.zip" );
        Artifact artifact = new DefaultArtifact( "default", "arduino-core-2", "avr", null, "1.6.23" );
        File outputZipFile = new File( inputZipFile.getParent(), ArtifactUtils.getZipFileName( artifact ) );

        repacker.repack( inputZipFile, "/cores/arduino", outputZipFile );

        assertTrue( outputZipFile.exists() );

        ZipFile zipFile = new ZipFile( outputZipFile );

        assertNotNull( zipFile.getEntry( "src/abi.cpp" ) );
        assertNotNull( zipFile.getEntry( "src/Arduino.h" ) );
        assertNotNull( zipFile.getEntry( "src/main.cpp" ) );
        assertNotNull( zipFile.getEntry( "src/wiring.c" ) );
        assertNull( zipFile.getEntry( "abi.cpp" ) );
        assertNull( zipFile.getEntry( "Arduino.h" ) );
        assertNull( zipFile.getEntry( "main.cpp" ) );
        assertNull( zipFile.getEntry( "wiring.c" ) );

        zipFile.close();
    }

    @Test
    public void testUnpackForZipNoRootNoSrc() throws IOException
    {
        LibraryRepacker repacker = new LibraryRepacker();

        File inputZipFile =
            new File( "target/test-classes/com/github/wmarkow/amp/fetch/test_sources_no_root_no_src.zip" );
        Artifact artifact = new DefaultArtifact( "default", "arduino-core-3", "avr", null, "1.6.23" );
        File outputZipFile = new File( inputZipFile.getParent(), ArtifactUtils.getZipFileName( artifact ) );

        repacker.repack( inputZipFile, "/cores/arduino", outputZipFile );

        assertTrue( outputZipFile.exists() );

        ZipFile zipFile = new ZipFile( outputZipFile );

        assertNotNull( zipFile.getEntry( "src/abi.cpp" ) );
        assertNotNull( zipFile.getEntry( "src/Arduino.h" ) );
        assertNotNull( zipFile.getEntry( "src/main.cpp" ) );
        assertNotNull( zipFile.getEntry( "src/wiring.c" ) );
        assertNull( zipFile.getEntry( "abi.cpp" ) );
        assertNull( zipFile.getEntry( "Arduino.h" ) );
        assertNull( zipFile.getEntry( "main.cpp" ) );
        assertNull( zipFile.getEntry( "wiring.c" ) );

        zipFile.close();
    }

    @Test
    public void testUnpackForZipNoRootWithSrc() throws IOException
    {
        LibraryRepacker repacker = new LibraryRepacker();

        File inputZipFile =
            new File( "target/test-classes/com/github/wmarkow/amp/fetch/test_sources_no_root_with_src.zip" );
        Artifact artifact = new DefaultArtifact( "default", "arduino-core-4", "avr", null, "1.6.23" );
        File outputZipFile = new File( inputZipFile.getParent(), ArtifactUtils.getZipFileName( artifact ) );

        repacker.repack( inputZipFile, "/cores/arduino", outputZipFile );

        assertTrue( outputZipFile.exists() );

        ZipFile zipFile = new ZipFile( outputZipFile );

        assertNotNull( zipFile.getEntry( "src/abi.cpp" ) );
        assertNotNull( zipFile.getEntry( "src/Arduino.h" ) );
        assertNotNull( zipFile.getEntry( "src/main.cpp" ) );
        assertNotNull( zipFile.getEntry( "src/wiring.c" ) );
        assertNull( zipFile.getEntry( "abi.cpp" ) );
        assertNull( zipFile.getEntry( "Arduino.h" ) );
        assertNull( zipFile.getEntry( "main.cpp" ) );
        assertNull( zipFile.getEntry( "wiring.c" ) );

        zipFile.close();
    }
}
