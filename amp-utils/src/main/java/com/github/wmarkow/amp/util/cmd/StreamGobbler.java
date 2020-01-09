package com.github.wmarkow.amp.util.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StreamGobbler
{
    private Logger logger = LoggerFactory.getLogger( StreamGobbler.class );

    private InputStream is;
    private AtomicBoolean finished = new AtomicBoolean( false );

    public StreamGobbler()
    {
    }

    public void start( InputStream is )
    {
        this.is = is;

        Thread thread = new Thread()
        {
            @Override
            public void run()
            {
                StreamGobbler.this.run();
            }
        };
        thread.start();
    }

    public void waitFor()
    {
        while( finished.get() == false )
        {
            // do nothing; just wait
        }
    }

    protected abstract void onLineRead( String line );

    private void run()
    {
        try
        {
            InputStreamReader isr = new InputStreamReader( is );
            BufferedReader br = new BufferedReader( isr );
            String line;
            while( (line = br.readLine()) != null )
            {
                onLineRead( line );
            }

            br.close();
        }
        catch( IOException ioe )
        {
            logger.error( ioe.getMessage(), ioe );
        }

        finished.set( true );
    }
}
