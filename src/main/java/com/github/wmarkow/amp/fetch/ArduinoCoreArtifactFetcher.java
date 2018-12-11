package com.github.wmarkow.amp.fetch;

import java.io.File;

import com.github.wmarkow.amp.platform.PlatformPackageManager;

public class ArduinoCoreArtifactFetcher
{
    private PlatformPackageManager ppm;

    public ArduinoCoreArtifactFetcher( PlatformPackageManager ppm )
    {
        this.ppm = ppm;
    }

    public File fetch( String artifactId, String version )
    {
        return null;
    }
}


