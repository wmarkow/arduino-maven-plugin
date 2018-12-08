package com.github.wmarkow.amp.elf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.wmarkow.amp.IntegrationTest;
import com.github.wmarkow.amp.linker.LinkerIntegrationTest;

@Category( IntegrationTest.class )
public class HexDumperIntegrationTest
{

    private File inputElfFile = new File( "target/output.elf" );
    private File outputHexFile = new File( "target/output.hex" );

    @Before
    public void init() throws IOException, InterruptedException
    {
        LinkerIntegrationTest lit = new LinkerIntegrationTest();
        lit.init();
        lit.testLink();

        if( outputHexFile.exists() )
        {
            FileUtils.forceDelete( outputHexFile );
        }
    }

    @Test
    public void testHexDumper() throws IOException, InterruptedException
    {
        HexDumper hexDumper = new HexDumper();

        hexDumper.setCommand( "avr-objcopy" );
        hexDumper.addCommandArg( "-O" );
        hexDumper.addCommandArg( "ihex" );
        hexDumper.addCommandArg( "-R" );
        hexDumper.addCommandArg( ".eeprom" );
        hexDumper.setCommandExecutionDirectory( new File( "." ) );

        assertFalse( outputHexFile.exists() );

        hexDumper.makeHex( inputElfFile, outputHexFile );

        assertTrue( outputHexFile.exists() );
    }
}


