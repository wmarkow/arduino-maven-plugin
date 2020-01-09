package com.github.wmarkow.amp.arduino.build.imager;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.arduino.build.AbstractProcessor;

public class EepromDumper extends AbstractProcessor
{
    private Logger logger = LoggerFactory.getLogger( EepromDumper.class );

    private EepromImageCommandBuilder commandBuilder;

    public EepromDumper( EepromImageCommandBuilder commandBuilder )
    {
        super();
        this.commandBuilder = commandBuilder;
    }

    public void makeEeprom( File inputElfFile ) throws IOException, InterruptedException
    {
        logger.info( "" );
        logger.info( String.format( "Creating EEPROM image" ) );

        commandBuilder.setInputElfFile( inputElfFile );
        executeCommand( commandBuilder.buildCommand() );
    }
}
