package com.github.wmarkow.amp.platform;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class PlatformPackageManager
{
    private Logger logger = LoggerFactory.getLogger( PlatformPackageManager.class );

    private List< URL > packageUrls = new ArrayList< URL >();
    private File workDir;

    private List< PlatformPackageIndex > loadedIndexes = new ArrayList< PlatformPackageIndex >();

    public PlatformPackageManager( File workDir )
    {
        this.workDir = workDir;
    }

    public void update()
    {
        try
        {
            FileUtils.forceMkdir( workDir );
        }
        catch( IOException e )
        {
            throw new RuntimeException( e );
        }

        loadedIndexes.clear();

        for( URL url : packageUrls )
        {
            try
            {
                final String filename = FilenameUtils.getName( url.getPath() );
                File file = new File( workDir, filename );

                if( !file.exists() )
                {
                    FileUtils.copyURLToFile( url, file, 5000, 2500 );
                }

                loadedIndexes.add( readFromJson( file ) );
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

    public List< Package > getPackages()
    {
        List< Package > result = new ArrayList< Package >();

        for( PlatformPackageIndex index : loadedIndexes )
        {
            result.addAll( index.getPackages() );
        }

        return result;
    }

    public List< String > getLatestPlatformsAsArtifacts()
    {
        List< String > result = new ArrayList< String >();
        for( PlatformPackageIndex index : loadedIndexes )
        {
            for( Package _package : index.getPackages() )
            {
                for( Platform platform : _package.getPlatforms() )
                {
                    result.add( _package.getName() + "-" + platform.getArchitecture() + "-"
                        + platform.getVersion() );
                }
            }
        }

        return result;
    }

    private PlatformPackageIndex readFromJson( File jsonFile ) throws IOException
    {
        String jsonString = FileUtils.readFileToString( jsonFile, "UTF-8" );
        Gson gson = new Gson();

        return gson.fromJson( jsonString, PlatformPackageIndex.class );
    }
}
