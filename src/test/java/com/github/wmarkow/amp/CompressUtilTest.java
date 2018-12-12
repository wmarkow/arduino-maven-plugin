package com.github.wmarkow.amp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class CompressUtilTest
{

    private File uncompressZipDir = new File( "target/uncomporess-tests/test-zip" );
    private File uncompressTarBz2Dir = new File( "target/uncomporess-tests/test-tar-bz2" );

    @Before
    public void init() throws IOException
    {
        FileUtils.forceMkdir( uncompressZipDir );
        FileUtils.forceMkdir( uncompressTarBz2Dir );

        FileUtils.cleanDirectory( uncompressZipDir );
        FileUtils.cleanDirectory( uncompressTarBz2Dir );
    }

    @Test
    public void testUnpackZip() throws IOException, ArchiveException
    {
        assertEquals( 0, uncompressZipDir.list().length );

        boolean result = CompressUtil.unpack( new File( "src/test/resources/test.zip" ), uncompressZipDir );

        assertTrue( result );
        assertEquals( 1, uncompressZipDir.list().length );
        assertTrue( new File( uncompressZipDir, "src/hooks.c" ).exists() );
        assertTrue( new File( uncompressZipDir, "src/Client.h" ).exists() );
    }

    @Test
    public void testUnpackTarBz2() throws IOException, ArchiveException
    {
        boolean result =
            CompressUtil.unpack( new File( "src/test/resources/test.tar.bz2" ), uncompressTarBz2Dir );

        File testFile = new File( "target/test.txt" );
        FileUtils.touch( testFile );
        assertTrue( testFile.exists() );
        FileUtils.deleteQuietly( testFile );
        assertFalse( testFile.exists() );

        assertTrue( result );
        assertFalse( new File( uncompressTarBz2Dir, "test.tar.tar" ).exists() );
        assertEquals( 1, uncompressTarBz2Dir.list().length );
        assertTrue( new File( uncompressTarBz2Dir, "avr/bootloaders/atmega8/Makefile" ).exists() );
        assertTrue( new File( uncompressTarBz2Dir, "avr/boards.txt" ).exists() );
        assertTrue( new File( uncompressTarBz2Dir, "avr/platform.txt" ).exists() );
        assertTrue( new File( uncompressTarBz2Dir, "avr/programmers.txt" ).exists() );
    }
}
