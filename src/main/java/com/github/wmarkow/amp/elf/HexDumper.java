package com.github.wmarkow.amp.elf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.processor.AbstractProcessor;

public class HexDumper extends AbstractProcessor
{
    private Logger logger = LoggerFactory.getLogger( HexDumper.class );

    public void makeHex( File inputElfFile, File outputHexFile ) throws IOException, InterruptedException
    {
        List< String > cmd = new ArrayList< String >();

        cmd.add( getCommand() );
        cmd.addAll( getCommandArgs() );
        cmd.add( inputElfFile.getPath() );
        cmd.add( outputHexFile.getPath() );

        logger.info( "" );
        logger.info( String.format( "Creating HEX image" ) );

        executeCommand( cmd );
    }
}
