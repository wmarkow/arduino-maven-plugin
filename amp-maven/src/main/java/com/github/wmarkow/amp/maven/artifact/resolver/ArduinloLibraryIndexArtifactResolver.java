package com.github.wmarkow.amp.maven.artifact.resolver;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.aether.artifact.Artifact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.arduino.platform.Library;
import com.github.wmarkow.amp.arduino.platform.PlatformLibrariesIndex;

public class ArduinloLibraryIndexArtifactResolver
{
    private Logger logger = LoggerFactory.getLogger( ArduinloLibraryIndexArtifactResolver.class );

    private PlatformLibrariesIndex librariesIndex;

    public ArduinloLibraryIndexArtifactResolver( PlatformLibrariesIndex librariesIndex )
    {
        this.librariesIndex = librariesIndex;
    }

    public File fetch( Artifact artifact, File targetDir ) throws MalformedURLException, IOException
    {
        logger.info( String.format( "Trying to fetch artifact %s ...", artifact ) );

        // find a correct library entry for a specific artifact
        Library library = findLibrary( librariesIndex, artifact );

        if( library == null )
        {
            return null;
        }

        final File targetFile = new File( targetDir, FilenameUtils.getName( library.getUrl() ) );
        logger.info( String.format( "... Downloading: %s into %s", library.getUrl(),
            targetFile.getAbsolutePath() ) );

        FileUtils.copyURLToFile( new URL( library.getUrl() ), targetFile, 5000, 2500 );

        return targetFile;
    }

    public Library findLibrary( Artifact artifact )
    {
        return this.findLibrary( librariesIndex, artifact );
    }

    private Library findLibrary( PlatformLibrariesIndex librariesIndex, Artifact artifact )
    {
        List< Library > result = new ArrayList< Library >();

        for( Library library : librariesIndex.getLibraries() )
        {
            if( !artifact.getArtifactId().equals( library.getName() ) )
            {
                continue;
            }

            if( !artifact.getVersion().equals( library.getVersion() ) )
            {
                continue;
            }

            if( !artifact.getGroupId().equals( repositoryUrlToGroupId( library.getRepository() ) ) )
            {
                continue;
            }

            result.add( library );
        }

        if( result.size() == 0 )
        {
            return null;
        }

        if( result.size() == 1 )
        {
            return result.get( 0 );
        }

        throw new IllegalStateException( String.format( "Found more than one library for artifact %s",
            artifact ) );
    }

    private String repositoryUrlToGroupId( String repositoryUrl )
    {
        URL repoUrl = null;
        try
        {
            repoUrl = new URL( repositoryUrl );
        }
        catch( MalformedURLException e )
        {
            logger.error( e.getMessage(), e );

            return null;
        }

        String host = repoUrl.getHost();
        String revertedHost = revertHost( host );

        return revertedHost + "." + getUsernameFromPath( repoUrl.getPath() );
    }

    private String revertHost( String host )
    {
        List< String > list = Arrays.asList( host.split( "\\." ) );
        Collections.reverse( list );

        StringBuilder sb = new StringBuilder();
        for( int q = 0; q < list.size(); q++ )
        {
            sb.append( list.get( q ) );
            if( q < list.size() - 1 )
            {
                sb.append( "." );
            }
        }

        return sb.toString();
    }

    private String getUsernameFromPath( String path )
    {
        return path.split( "/" )[ 1 ];
    }
}
