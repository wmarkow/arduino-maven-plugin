package com.github.wmarkow.amp.build.elf;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.github.wmarkow.amp.IntegrationTest;
import com.github.wmarkow.amp.build.linker.LinkerIntegrationTest;

@Category( IntegrationTest.class )
public class EepromDumperIntegrationTest
{

    private File inputElfFile = new File( "target/output.elf" );
    private File outputEepromFile = new File( "target/output.eep" );

    @Before
    public void init() throws IOException, InterruptedException
    {
        LinkerIntegrationTest lit = new LinkerIntegrationTest();
        lit.init();
        lit.testLink();

        if( outputEepromFile.exists() )
        {
            FileUtils.forceDelete( outputEepromFile );
        }
    }

    @Test
    public void testEepromDumper() throws IOException, InterruptedException
    {
        HexDumper hexDumper = new HexDumper();

        // hexDumper.setCommand( "avr-objcopy" );
        // hexDumper.addCommandArg( "-O" );
        // hexDumper.addCommandArg( "ihex" );
        // hexDumper.addCommandArg( "-j" );
        // hexDumper.addCommandArg( ".eeprom" );
        // hexDumper.addCommandArg( "--set-section-flags=.eeprom=alloc,load" );
        // hexDumper.addCommandArg( "--no-change-warnings" );
        // hexDumper.addCommandArg( "--change-section-lma" );
        // hexDumper.addCommandArg( ".eeprom=0" );
        // hexDumper.setCommandExecutionDirectory( new File( "." ) );

        assertFalse( outputEepromFile.exists() );

        hexDumper.makeHex( inputElfFile, outputEepromFile );

        assertTrue( outputEepromFile.exists() );
    }
}


