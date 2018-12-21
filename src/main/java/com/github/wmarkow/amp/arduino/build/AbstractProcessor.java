package com.github.wmarkow.amp.arduino.build;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.github.wmarkow.amp.cmd.CommandExecutor;

public abstract class AbstractProcessor
{
    private File cmdExeDir;

    public void setCommandExecutionDirectory( File dir )
    {
        this.cmdExeDir = dir;
    }

    protected void executeCommand( List< String > cmd ) throws IOException, InterruptedException
    {
        CommandExecutor ce = new CommandExecutor();
        int exitCode = ce.execute( cmd, cmdExeDir );

        if( exitCode != 0 )
        {
            throw new RuntimeException( String.format( "Command exited with exit code %s", exitCode ) );
        }
    }

    protected void executeCommand( String cmd ) throws IOException, InterruptedException
    {
        final String split[] = cmd.split( " " );

        List< String > cmdAsList = Arrays.asList( split );

        executeCommand( cmdAsList );
    }
}
