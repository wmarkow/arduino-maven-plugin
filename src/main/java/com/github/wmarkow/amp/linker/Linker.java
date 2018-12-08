package com.github.wmarkow.amp.linker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.processor.AbstractProcessor;

public class Linker extends AbstractProcessor
{
    private Logger logger = LoggerFactory.getLogger( Linker.class );

    public void link( File objDir, File outputElfFile ) throws IOException, InterruptedException
    {
        List< String > cmd = new ArrayList< String >();

        cmd.add( getCommand() );
        cmd.addAll( getCommandArgs() );
        cmd.add( "-o" );
        cmd.add( outputElfFile.getPath() );

        Collection< File > objFiles = FileUtils.listFiles( objDir, new String[]
        { "o" }, true );
        for( File objFile : objFiles )
        {
            cmd.add( objFile.getPath() );
        }

        logger.info( "" );
        logger.info( String.format( "Linking %s into %s", objDir.getPath(), outputElfFile.getPath() ) );

        executeCommand( cmd );
    }
}
