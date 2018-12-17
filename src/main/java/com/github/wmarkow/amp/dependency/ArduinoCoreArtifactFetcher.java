package com.github.wmarkow.amp.dependency;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.PlatformPackageManager;

public class ArduinoCoreArtifactFetcher
{
    private Logger logger = LoggerFactory.getLogger( ArduinoCoreArtifactFetcher.class );

    private PlatformPackageManager ppm;

    public ArduinoCoreArtifactFetcher( PlatformPackageManager ppm )
    {
        this.ppm = ppm;
    }

    public File fetch( String artifactId, String version, File targetDir ) throws IOException
    {
        logger.info( String.format( "Trying to fetch artifact %s-%s ...", artifactId, version ) );
        Platform platform = ppm.getPlatform( artifactId, version );

        if( platform == null )
        {
            return null;
        }

        logger.info( String.format( "... Downloading: %s", platform.getUrl() ) );
        final File targetFile = new File( targetDir, FilenameUtils.getName( platform.getUrl() ) );
        FileUtils.copyURLToFile( new URL( platform.getUrl() ), targetFile, 5000, 2500 );

        return targetFile;
    }
}
