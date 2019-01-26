package com.github.wmarkow.amp.util.cmd;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingStreamGobbler extends StreamGobbler
{
    private Logger logger = LoggerFactory.getLogger( LoggingStreamGobbler.class );

    public LoggingStreamGobbler( InputStream is )
    {
        super( is );
    }

    @Override
    protected void onLineRead( String line )
    {
        logger.info( line );
    }
}
