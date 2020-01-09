package com.github.wmarkow.amp.arduino.platform.manager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.arduino.platform.PlatformFilesReader;
import com.github.wmarkow.amp.arduino.platform.PlatformRepository;

public class PlatformPackageManager extends PlatformManager
{
    private Logger logger = LoggerFactory.getLogger( PlatformPackageManager.class );

    private List< URL > packageUrls = new ArrayList< URL >();

    private PlatformRepository platformRepository = new PlatformRepository();

    public PlatformPackageManager( File workDir )
    {
        super( workDir );
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

        platformRepository = new PlatformRepository();

        for( URL url : packageUrls )
        {
            try
            {
                final String filename = FilenameUtils.getName( url.getPath() );
                File file = new File( getPlatformDir(), filename );

                if( !file.exists() )
                {
                    FileUtils.copyURLToFile( url, file, 5000, 2500 );
                }

                PlatformFilesReader pir = new PlatformFilesReader();

                platformRepository.addIndex( pir.readFromJson( file ) );
            }
            catch( IOException e )
            {
                logger.warn( String.format( "Exception while downloading file %s", url.toString() ), e );
            }
        }
    }

    public void addPackageUrl( URL url )
    {
        packageUrls.add( url );
    }

    public PlatformRepository getPlatformRepository()
    {
        return platformRepository;
    }
}
