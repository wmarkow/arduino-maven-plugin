package com.github.wmarkow.amp.arduino.platform;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;

import com.github.wmarkow.amp.util.CompressUtil;

public class ToolsManager extends PlatformManager
{
    private final static String DOWNLOADS_DIR = "downloads";

    public ToolsManager( File platformDir )
    {
        super( platformDir );
    }

    public void resolve( Package _package, Platform platform )
    {
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
                e.printStackTrace();
            }
        }
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
