package com.github.wmarkow.amp.fetch;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.junit.Test;
import org.zeroturnaround.zip.ZipUtil;

import com.github.wmarkow.amp.ArtifactUtils;

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
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/abi.cpp" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/Arduino.h" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/main.cpp" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/wiring.c" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "abi.cpp" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "Arduino.h" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "main.cpp" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "wiring.c" ) );
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
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/abi.cpp" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/Arduino.h" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/main.cpp" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/wiring.c" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "abi.cpp" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "Arduino.h" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "main.cpp" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "wiring.c" ) );
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
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/abi.cpp" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/Arduino.h" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/main.cpp" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/wiring.c" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "abi.cpp" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "Arduino.h" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "main.cpp" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "wiring.c" ) );
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
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/abi.cpp" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/Arduino.h" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/main.cpp" ) );
        assertTrue( ZipUtil.containsEntry( outputZipFile, "src/wiring.c" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "abi.cpp" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "Arduino.h" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "main.cpp" ) );
        assertFalse( ZipUtil.containsEntry( outputZipFile, "wiring.c" ) );
    }
}
