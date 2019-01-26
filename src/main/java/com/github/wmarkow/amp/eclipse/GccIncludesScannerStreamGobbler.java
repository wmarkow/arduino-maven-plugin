package com.github.wmarkow.amp.eclipse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.util.cmd.StreamGobbler;

public class GccIncludesScannerStreamGobbler extends StreamGobbler
{
    private Logger logger = LoggerFactory.getLogger( GccIncludesScannerStreamGobbler.class );

    @Override
    protected void onLineRead( String line )
    {
        logger.info( String.format( "          %s", line ) );
    }
}
