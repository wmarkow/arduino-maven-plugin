package com.github.wmarkow.amp.build;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.github.wmarkow.amp.cmd.CommandExecutor;

public abstract class AbstractProcessor
{
    private String cmd;
    private List< String > cmdArgs = new ArrayList< String >();
    private File cmdExeDir;

    public void setCommand( String cmd )
    {
        this.cmd = cmd;
    }

    public void addCommandArg( String arg )
    {
        cmdArgs.add( arg );
    }

    public void addCommandArgs( List< String > args )
    {
        cmdArgs.addAll( args );
    }

    public void setCommandExecutionDirectory( File dir )
    {
        this.cmdExeDir = dir;
    }

    protected String getCommand()
    {
        return cmd;
    }

    protected List< String > getCommandArgs()
    {
        return cmdArgs;
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
