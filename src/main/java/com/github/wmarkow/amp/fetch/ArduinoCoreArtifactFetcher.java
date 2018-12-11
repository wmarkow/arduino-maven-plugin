package com.github.wmarkow.amp.fetch;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import com.github.wmarkow.amp.platform.Package;
import com.github.wmarkow.amp.platform.Platform;
import com.github.wmarkow.amp.platform.PlatformPackageManager;

public class ArduinoCoreArtifactFetcher
{
    private PlatformPackageManager ppm;

    public ArduinoCoreArtifactFetcher( PlatformPackageManager ppm )
    {
        this.ppm = ppm;
    }

    public File fetch( String artifactId, String version, File targetDir ) throws IOException
    {
        Platform platform = getPlatform( artifactId, version, ppm );
        if( platform == null )
        {
            return null;
        }

        final File targetFile = new File( targetDir, FilenameUtils.getName( platform.getUrl() ) );
        FileUtils.copyURLToFile( new URL( platform.getUrl() ), targetFile, 5000, 2500 );

        return targetFile;
    }

    private Platform getPlatform( String artifactId, String version, PlatformPackageManager ppm )
    {
        for( Package _package : ppm.getPackages() )
        {
            for( Platform platform : _package.getPlatforms() )
            {
                final String _artifactId = _package.getName() + "-" + platform.getArchitecture();
                if( artifactId.equals( _artifactId ) && version.equals( platform.getVersion() ) )
                {
                    return platform;
                }
            }
        }

        return null;
    }
}
