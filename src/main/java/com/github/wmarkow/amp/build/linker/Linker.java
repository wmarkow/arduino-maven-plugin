package com.github.wmarkow.amp.build.linker;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.build.AbstractProcessor;

public class Linker extends AbstractProcessor
{
    private Logger logger = LoggerFactory.getLogger( Linker.class );

    private LinkerCommandBuilder linkerCommandBuilder;

    public Linker( LinkerCommandBuilder linkerCommandBuilder )
    {
        super();
        this.linkerCommandBuilder = linkerCommandBuilder;
    }

    public void link( File objDir, File outputElfFile ) throws IOException, InterruptedException
    {
        linkerCommandBuilder.setObjectsDir( objDir );
        linkerCommandBuilder.setOutputElfFile( outputElfFile );

        logger.info( "" );
        logger.info( String.format( "Linking %s into %s", objDir.getPath(), outputElfFile.getPath() ) );

        executeCommand( linkerCommandBuilder.buildCommand() );
    }
}
