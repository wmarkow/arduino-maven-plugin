package com.github.wmarkow.amp.util.cmd;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandExecutor
{
    private Logger logger = LoggerFactory.getLogger( CommandExecutor.class );

    private StreamGobbler streamGobbler;
    private boolean consumed;

    public int execute( List< String > cmdArgs, File cmdWorkDir ) throws IOException, InterruptedException
    {
        if( consumed )
        {
            throw new IllegalStateException( "Can't use this CommandExecutor twice. Create a new instance." );
        }
        consumed = true;

        // Create ProcessBuilder with the command arguments
        ProcessBuilder pb = new ProcessBuilder( cmdArgs );

        // Redirect the stderr to the stdout
        pb.redirectErrorStream( true );

        pb.directory( cmdWorkDir );

        // Start the new process
        Process process = pb.start();

        // Adding to log the command
        StringBuilder builder = new StringBuilder();
        for( String s : cmdArgs )
        {

            builder.append( s );
            // Append space
            builder.append( " " );
        }
        logger.info( "[Executing] " + builder.toString() );

        getStreamGobbler().start( process.getInputStream() );

        return process.waitFor();
    }

    public StreamGobbler getStreamGobbler()
    {
        if( streamGobbler == null )
        {
            streamGobbler = new LoggingStreamGobbler();
        }

        return streamGobbler;
    }

    public void setStreamGobbler( StreamGobbler aStreamGobbler )
    {
        streamGobbler = aStreamGobbler;
    }
}
