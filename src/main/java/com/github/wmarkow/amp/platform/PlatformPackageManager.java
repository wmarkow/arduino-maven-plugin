package com.github.wmarkow.amp.platform;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

public class PlatformPackageManager
{

    private List< PlatformPackageIndex > indexes = new ArrayList< PlatformPackageIndex >();

    public PlatformPackageManager()
    {

    }

    public void addAndParsePackageIndex( File file ) throws IOException
    {
        PlatformPackageIndex ppi = readFromJson( file );

        indexes.add( ppi );
    }

    public List< String > getLatestPlatformsAsArtifacts()
    {
        List< String > result = new ArrayList< String >();
        for( PlatformPackageIndex index : indexes )
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

    public PlatformPackageIndex readFromJson( File jsonFile ) throws IOException
    {
        String jsonString = FileUtils.readFileToString( jsonFile, "UTF-8" );
        Gson gson = new Gson();

        return gson.fromJson( jsonString, PlatformPackageIndex.class );
    }
}
