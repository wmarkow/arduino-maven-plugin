package com.github.wmarkow.amp.arduino.platform;

import java.io.File;

public class PlatformManager
{
    private File platformDir;

    public PlatformManager( File platformDir )
    {
        this.platformDir = platformDir;
    }

    public File getPlatformDir()
    {
        return platformDir;
    }
}
