package com.github.wmarkow.amp.build.elf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.build.AbstractProcessor;

public class EepromDumper extends AbstractProcessor
{
    private Logger logger = LoggerFactory.getLogger( EepromDumper.class );

    public void makeEeprom( File inputElfFile, File outputEepromFile ) throws IOException,
        InterruptedException
    {
        List< String > cmd = new ArrayList< String >();

        cmd.add( getCommand() );
        cmd.addAll( getCommandArgs() );
        cmd.add( inputElfFile.getPath() );
        cmd.add( outputEepromFile.getPath() );

        logger.info( "" );
        logger.info( String.format( "Creating EEPROM image" ) );

        executeCommand( cmd );
    }
}
