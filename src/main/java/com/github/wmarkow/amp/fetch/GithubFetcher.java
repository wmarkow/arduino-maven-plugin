package com.github.wmarkow.amp.fetch;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

public class GithubFetcher
{
    private final static String GITHUB_ARCHIVE_URL_FORMAT = "https://github.com/%s/%s/archive/%s.zip";

    public File fetchLibrary( GithubFetchDescriptor descriptor, File targetDir ) throws IOException
    {
        if( targetDir == null )
        {
            throw new IllegalArgumentException( "Target dir must not be null" );
        }

        if( !targetDir.isDirectory() )
        {
            throw new IllegalArgumentException( "Target dir must be a directory" );
        }

        if( !targetDir.exists() )
        {
            throw new IllegalArgumentException( "Target dir doesn't exist" );
        }

        final URL url = getArchiveUrl( descriptor.username, descriptor.repoName, descriptor.refName );

        File targetFile =
            new File( targetDir.getAbsolutePath(), getFileName( descriptor.username, descriptor.repoName,
                descriptor.refName ) );

        FileUtils.copyURLToFile( url, targetFile, 5000, 2500 );

        return targetFile;
    }

    private URL getArchiveUrl( String username, String repoName, String refName )
    {
        try
        {
            return new URL( String.format( GITHUB_ARCHIVE_URL_FORMAT, username, repoName, refName ) );
        }
        catch( MalformedURLException e )
        {
            return null;
        }
    }

    private String getFileName( String username, String repoName, String refName )
    {
        return String.format( "%s_%s_%s.zip", username, repoName, refName );
    }
}
