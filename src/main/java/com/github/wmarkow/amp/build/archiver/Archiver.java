package com.github.wmarkow.amp.build.archiver;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.build.AbstractProcessor;

public class Archiver extends AbstractProcessor
{
    private Logger logger = LoggerFactory.getLogger( Archiver.class );

    private ArchiverCommandBuilder archiverCommandBuilder;

    public Archiver( ArchiverCommandBuilder archiverCommandBuilder )
    {
        super();
        this.archiverCommandBuilder = archiverCommandBuilder;
    }

    public void archive( File objDir, File outputArchiveFile ) throws IOException, InterruptedException
    {
        archiverCommandBuilder.setOutputArchiveFile( outputArchiveFile );

        logger.info( "" );
        logger.info( String.format( "Archiving %s into %s", objDir.getPath(), outputArchiveFile.getPath() ) );

        for( File objFile : getObjectFiles( objDir ) )
        {
            archiverCommandBuilder.setObjFile( objFile );
            executeCommand( archiverCommandBuilder.buildCommand() );
        }
    }

    private Collection< File > getObjectFiles( File objDir )
    {
        return FileUtils.listFiles( objDir, new String[]
        { "o" }, true );
    }
}
