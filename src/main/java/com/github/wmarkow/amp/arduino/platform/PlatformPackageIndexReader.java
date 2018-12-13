package com.github.wmarkow.amp.arduino.platform;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

class PlatformPackageIndexReader
{
    public PlatformPackageIndex readFromJson( File jsonFile ) throws IOException
    {
        String jsonString = FileUtils.readFileToString( jsonFile, "UTF-8" );
        Gson gson = new Gson();

        return gson.fromJson( jsonString, PlatformPackageIndex.class );
    }
}


