package com.github.wmarkow.amp.arduino.platform.manager;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.wmarkow.amp.arduino.platform.Package;
import com.github.wmarkow.amp.arduino.platform.Platform;
import com.github.wmarkow.amp.arduino.platform.System;
import com.github.wmarkow.amp.arduino.platform.Tool;
import com.github.wmarkow.amp.arduino.platform.ToolsDependency;
import com.github.wmarkow.amp.util.CompressUtil;

public class ToolsManager extends PlatformManager
{
    private Logger logger = LoggerFactory.getLogger( ToolsManager.class );

    private final static String DOWNLOADS_DIR = "downloads";

    public ToolsManager( File platformDir )
    {
        super( platformDir );
    }

    public void resolve( Package _package, Platform platform )
    {
        logger.info( String.format( "Resolving tools dependencies for package %s and platform %s ...",
            _package, platform ) );

        for( ToolsDependency td : platform.getToolsDependencies() )
        {
            Tool tool = _package.getToolByNameAndVersion( td.getName(), td.getVersion() );

            try
            {
                downlaodToolIfNeeded( _package.getName(), tool );
                unpackToolIfNeeded( _package.getName(), tool );
            }
            catch( IOException e )
            {
                logger.error( e.getMessage(), e );
            }
        }
    }

    public File getToolchainBinDirPath( Package _package, Platform platform )
    {
        for( ToolsDependency td : platform.getToolsDependencies() )
        {
            if( td.getName().toLowerCase().contains( "gcc" ) )
            {
                Tool tool = _package.getToolByNameAndVersion( td.getName(), td.getVersion() );

                File packagesDir = new File( getPlatformDir(), "packages" );
                File dir =
                    new File( packagesDir, _package.getName() + "/tools/" + tool.getName() + "/"
                        + tool.getVersion() );

                // sometimes the distribution contains one additional dir in the structure. Strip it.
                File[] filesInside = dir.listFiles();
                if( filesInside.length == 1 )
                {
                    return new File( filesInside[ 0 ], "/bin/" );
                }
            }
        }

        return null;
    }

    private void downlaodToolIfNeeded( String packageName, Tool tool ) throws MalformedURLException,
        IOException
    {
        System system = getCurrentSystem( tool );
        File downloadDir = new File( getPlatformDir(), DOWNLOADS_DIR );
        File downloadFile = new File( downloadDir, system.getArchiveFileName() );

        if( downloadFile.exists() )
        {
            return;
        }

        logger.info( String.format( "Downloading %s into %s", system.getUrl(), downloadFile ) );

        FileUtils.copyURLToFile( new URL( system.getUrl() ), downloadFile, 5000, 2500 );
    }

    private void unpackToolIfNeeded( String packageName, Tool tool ) throws IOException
    {
        File packagesDir = new File( getPlatformDir(), "packages" );
        File targetDir =
            new File( packagesDir, packageName + "/tools/" + tool.getName() + "/" + tool.getVersion() );

        if( targetDir.exists() )
        {
            return;
        }

        System system = getCurrentSystem( tool );

        File downloadDir = new File( getPlatformDir(), DOWNLOADS_DIR );
        File downloadFile = new File( downloadDir, system.getArchiveFileName() );

        logger.info( String.format( "Unpacking %s into %s", downloadFile, targetDir ) );

        CompressUtil.unpack( downloadFile, targetDir );
    }

    // FIXME: silently for now only windows supported
    private System getCurrentSystem( Tool tool )
    {
        for( System system : tool.getSystems() )
        {
            if( system.getHost().toLowerCase().contains( "mingw" ) )
            {
                return system;
            }
        }

        return null;
    }
}
