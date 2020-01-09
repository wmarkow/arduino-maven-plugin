package com.github.wmarkow.amp.util.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingStreamGobbler extends StreamGobbler
{
    private Logger logger = LoggerFactory.getLogger( LoggingStreamGobbler.class );

    @Override
    protected void onLineRead( String line )
    {
        logger.info( line );
    }
}
