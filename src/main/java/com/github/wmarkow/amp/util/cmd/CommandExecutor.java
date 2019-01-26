package com.github.wmarkow.amp.util.cmd;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandExecutor
{
    private Logger logger = LoggerFactory.getLogger( CommandExecutor.class );

    public int execute( List< String > cmdArgs, File cmdWorkDir ) throws IOException, InterruptedException
    {
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

        // Create the StreamGobbler to read the process output
        StreamGobbler outputGobbler = new LoggingStreamGobbler( process.getInputStream() );

        outputGobbler.start();

        return process.waitFor();
    }
}
