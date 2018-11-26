package com.github.wmarkow.amp.builder;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class ArduinoBuilderTest
{

    @Test( expected = IllegalStateException.class )
    public void testCleanTargetBuildDirectoryForException()
    {
        ArduinoBuilder builder = new ArduinoBuilder( "blink", "1.0.0", "avr-standard" );

        builder.cleanTargetBuildDirectory();
    }

    @Test
    public void testCleanTargetBuildDirectory() throws IOException
    {
        ArduinoBuilder builder = new ArduinoBuilder( "blink", "1.0.0", "avr-standard" );

        File targetBuildDirectory = new File( new File( "" ).getAbsoluteFile(), "target" );
        File targetObjDirectory = new File( targetBuildDirectory, "obj" );
        File targetElfFile = new File( targetBuildDirectory, "blink-1.0.0-avr-standard.elf" );

        builder.setTargetBuildDirectory( targetBuildDirectory );

        FileUtils.forceMkdir( targetObjDirectory );
        FileUtils.touch( targetElfFile );

        assertTrue( targetObjDirectory.exists() );
        assertTrue( targetElfFile.exists() );

        builder.cleanTargetBuildDirectory();

        assertFalse( targetObjDirectory.exists() );
        assertFalse( targetElfFile.exists() );
    }
}


