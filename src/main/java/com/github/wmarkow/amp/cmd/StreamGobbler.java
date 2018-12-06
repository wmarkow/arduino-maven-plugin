package com.github.wmarkow.amp.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StreamGobbler extends Thread
{
    private Logger logger = LoggerFactory.getLogger( StreamGobbler.class );

    InputStream is;

    public StreamGobbler( InputStream is )
    {
        this.is = is;
    }

    @Override
    public void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader( is );
            BufferedReader br = new BufferedReader( isr );
            String line;
            while( (line = br.readLine()) != null )
            {
                logger.info( line );
            }

            br.close();
        }
        catch( IOException ioe )
        {
            logger.error( ioe.getMessage(), ioe );
        }
    }
}


