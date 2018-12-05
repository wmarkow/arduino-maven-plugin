package com.github.wmarkow.amp.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.tools.ant.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.maven_nar.cpptasks.CCTask;
import com.github.maven_nar.cpptasks.CommandExecution;

public class ArduinoPackager
{
    private Logger logger = LoggerFactory.getLogger( ArduinoPackager.class );

    public void createHex( File inputElfFile, File outputHexFile ) throws ExecutionException, IOException,
        InterruptedException
    {
        List< String > commandArgs = new ArrayList< String >();

        commandArgs.add( "avr-objcopy" );
        commandArgs.add( "-O" );
        commandArgs.add( "ihex" );
        commandArgs.add( "-R" );
        commandArgs.add( ".eeprom" );
        commandArgs.add( inputElfFile.getAbsolutePath() );
        commandArgs.add( outputHexFile.getAbsolutePath() );

        int exitCode = CommandExecution.runCommand( commandArgs.toArray( new String[]
        {} ), inputElfFile.getParentFile(), new FakeCCTask() );

        if( exitCode != 0 )
        {
            throw new ExecutionException( String.format( "Programm exited with code %s", exitCode ), null );
        }
    }

    private class FakeCCTask extends CCTask
    {
        @Override
        public void log( String msg )
        {
            logger.info( msg, Project.MSG_INFO );
        }
    }
}
