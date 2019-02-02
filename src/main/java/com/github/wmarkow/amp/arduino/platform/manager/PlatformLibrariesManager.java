package com.github.wmarkow.amp.arduino.platform.manager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlatformLibrariesManager extends PlatformManager
{
    private Logger logger = LoggerFactory.getLogger( PlatformLibrariesManager.class );

    private final static String LIBRARY_INDEX_URL =
        "http://downloads.arduino.cc/libraries/library_index.json";

    public PlatformLibrariesManager( File aPlatformDir )
    {
        super( aPlatformDir );
    }

    public void update()
    {
        try
        {
            FileUtils.forceMkdir( getPlatformDir() );
        }
        catch( IOException e )
        {
            throw new RuntimeException( e );
        }

        try
        {
            URL url = new URL( LIBRARY_INDEX_URL );

            final String filename = FilenameUtils.getName( url.getPath() );
            File file = new File( getPlatformDir(), filename );

            if( !file.exists() )
            {
                logger
                    .info( String.format( "Downloading %s into %s", url.toString(), file.getAbsolutePath() ) );
                FileUtils.copyURLToFile( url, file, 5000, 2500 );
            }
        }
        catch( MalformedURLException e )
        {
            logger.warn( String.format( "Exception while downloading file %s", LIBRARY_INDEX_URL ), e );
        }
        catch( IOException e )
        {
            logger.warn( String.format( "Exception while downloading file %s", LIBRARY_INDEX_URL ), e );
        }
    }
}
