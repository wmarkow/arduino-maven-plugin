package com.github.wmarkow.amp.build.elf;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.build.AbstractProcessor;

public class HexDumper extends AbstractProcessor
{
    private Logger logger = LoggerFactory.getLogger( HexDumper.class );

    private HexImageCommandBuilder commandBuilder;

    public HexDumper( HexImageCommandBuilder commandBuilder )
    {
        super();
        this.commandBuilder = commandBuilder;
    }

    public void makeHex( File inputElfFile ) throws IOException, InterruptedException
    {
        logger.info( "" );
        logger.info( String.format( "Creating HEX image" ) );

        commandBuilder.setInputElfFile( inputElfFile );
        executeCommand( commandBuilder.buildCommand() );
    }
}
