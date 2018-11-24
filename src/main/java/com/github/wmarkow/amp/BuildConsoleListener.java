package com.github.wmarkow.amp;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;

public class BuildConsoleListener implements BuildListener
{

    @Override
    public void buildStarted( BuildEvent aEvent )
    {
        logToConsole( aEvent );
    }

    @Override
    public void buildFinished( BuildEvent aEvent )
    {
        logToConsole( aEvent );
    }

    @Override
    public void targetStarted( BuildEvent aEvent )
    {
        logToConsole( aEvent );
    }

    @Override
    public void targetFinished( BuildEvent aEvent )
    {
        logToConsole( aEvent );
    }

    @Override
    public void taskStarted( BuildEvent aEvent )
    {
        logToConsole( aEvent );
    }

    @Override
    public void taskFinished( BuildEvent aEvent )
    {
        logToConsole( aEvent );
    }

    @Override
    public void messageLogged( BuildEvent aEvent )
    {
        logToConsole( aEvent );
    }

    private void logToConsole( BuildEvent aEvent )
    {
        if( aEvent.getException() != null )
        {
            aEvent.getException().printStackTrace( System.out );
        }
        System.out.println( aEvent.getMessage() );
    }
}
